package com.ataya.inventory.service.impl;

import com.ataya.inventory.dto.InventoryItemInfo;
import com.ataya.inventory.dto.InventoryStatistics;
import com.ataya.inventory.dto.UpdateInventoryRequest;
import com.ataya.inventory.dto.company.ProductDto;
import com.ataya.inventory.dto.stockMovement.EditQuantityRequest;
import com.ataya.inventory.dto.stockMovement.SupplyRequest;
import com.ataya.inventory.enums.ItemUnit;
import com.ataya.inventory.exception.custom.InvalidOperationException;
import com.ataya.inventory.exception.custom.ResourceNotFoundException;
import com.ataya.inventory.exception.custom.ValidationException;
import com.ataya.inventory.mapper.InventoryMapper;
import com.ataya.inventory.model.Inventory;
import com.ataya.inventory.model.User;
import com.ataya.inventory.repo.InventoryRepository;
import com.ataya.inventory.service.InventoryService;
import com.ataya.inventory.service.StockMovementService;
import com.ataya.inventory.service.kafka.CorrelationStorage;
import com.ataya.inventory.service.kafka.producer.ProductRequestProducer;
import com.ataya.inventory.util.ApiResponse;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static com.ataya.inventory.service.impl.CommonService.*;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryMapper inventoryMapper;
    private final InventoryRepository inventoryRepository;
    private final StockMovementService stockMovementService;
    private final ProductRequestProducer productRequestProducer;
    private final CorrelationStorage correlationStorage;



    @Override
    public ApiResponse<List<InventoryItemInfo>> getFilteredInventoryItems(String quantity, String price, String discountedPrice, String discount, String discountRate, String storeId, String companyId, String productId, Integer page, Integer size) {
        if(companyId == null) {
            throw new InvalidOperationException(
                    "view Inventory Items","not authorized"
            );
        }

        List<Criteria> criteria = new ArrayList<>();
        addCriteriaWithRange(criteria, "quantity", quantity);
        addCriteriaWithRange(criteria, "price", price);
        addCriteriaWithRange(criteria, "discountedPrice", discountedPrice);
        addCriteriaWithRange(criteria, "discount", discount);
        addCriteriaWithRange(criteria, "discountRate", discountRate);
        addCriteria(criteria, "storeId", storeId);
        addCriteria(criteria, "companyId", companyId);
        addCriteria(criteria, "productId", productId);

        Query query = new Query();
        query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));

        long total = inventoryRepository.countInventoryByQuery(query);

        int pg = page == null || page < 0 ? 0 : page;
        int sz = size == null || size <  0 ? 10 : size;
        PageRequest pageRequest = PageRequest.of(pg, sz);
        query.with(pageRequest);
        List<Inventory> inventories = inventoryRepository.findInventoryByQuery(query);

        return ApiResponse.<List<InventoryItemInfo>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .total(total)
                .page(pg)
                .size(sz)
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(inventories.stream().map(inventoryMapper::toInventoryItemInfo).toList())
                .message("Inventory items retrieved successfully")
                .statistics(getStatistics(storeId))
                .build();
    }



    private InventoryStatistics getStatistics(String storeId) {

        Integer allProducts = inventoryRepository.countAllByStoreId(storeId);
        Integer inReorderLevel = inventoryRepository.countWhereReorderLevelIsGreaterThanQuantity(storeId);
        Integer inStock = inventoryRepository.countAllByStoreIdAndQuantityGreaterThan(storeId, 0.0);
        Integer outOfStock = inventoryRepository.countAllByStoreIdAndQuantity(storeId, 0.0);
        Integer inDiscount = inventoryRepository.countAllInventoriesByStoreIdAndIsDiscounted(storeId, true);

        return InventoryStatistics.builder()
                .allProducts(allProducts)
                .inReorderLevel(inReorderLevel)
                .inStock(inStock)
                .outOfStock(outOfStock)
                .inDiscount(inDiscount)
                .build();
    }

    @Override
    public ApiResponse<InventoryItemInfo> updateInventoryItem(UpdateInventoryRequest requestBody, User user, String productId, String storeId) {
        if (user.getCompanyId() == null) {
            throw new InvalidOperationException(
                    "update Inventory Item", "not authorized"
            );
        }
        if (user.getStoreId() != null && user.getStoreId().equals(storeId)) {
            throw new InvalidOperationException(
                    "update Inventory Item", "not authorized"
            );
        }
        Inventory inventory = inventoryRepository.findByProductIdAndStoreId(productId, storeId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Inventory Item", "product", productId + " not found in store "
                )
        );
        if (inventory.getCompanyId() == null || !inventory.getCompanyId().equals(user.getCompanyId())) {
            throw new InvalidOperationException(
                    "update Inventory Item", "not authorized"
            );
        }

        if (ItemUnit.isValidUnit(requestBody.getUnit())) {
            inventory.setUnit(ItemUnit.getUnit(requestBody.getUnit()));
        } else {
            throw new ValidationException("ItemUnit", requestBody.getUnit(), "ItemUnit not valid");
        }

        inventory.setPrice(requestBody.getPrice());
        setDiscount(inventory, requestBody.getDiscount(), requestBody.getDiscountRate());
        inventory.setReorderLevel(requestBody.getReorderLevel());
        inventory.setUpdatedAt(LocalDateTime.now());
        inventory.setUpdatedBy(user.getUsername());

        inventoryRepository.save(inventory);

        return ApiResponse.<InventoryItemInfo>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(inventoryMapper.toInventoryItemInfo(inventory))
                .message("Inventory item updated successfully")
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<List<InventoryItemInfo>> updateProductPrice(Map<String, Double> prdIdPriceMap, User user, String storeId) {

        checkBeforeFetchInventory(user, storeId,prdIdPriceMap.keySet());
        List<Inventory> inventories = fetchInventoryByProductIdAndStoreId(prdIdPriceMap.keySet(), storeId);
        List<InventoryItemInfo> updatedInventories = new ArrayList<>();
        List<Inventory> toSave = new ArrayList<>();
        for (Inventory inventory : inventories) {
            Double price = prdIdPriceMap.get(inventory.getProductId());
            if (price != null) {
                if (price <= 0) {
                    throw new ValidationException("price", price, "Price cannot be negative");
                }
                if (inventory.getCompanyId() == null || !inventory.getCompanyId().equals(user.getCompanyId())) {
                    throw new InvalidOperationException(
                            "update Inventory Item", "not authorized"
                    );
                }
                resetDiscount(inventory);
                inventory.setPrice(price);
                inventory.setUpdatedAt(LocalDateTime.now());
                inventory.setUpdatedBy(user.getUsername());
                toSave.add(inventory);
                updatedInventories.add(inventoryMapper.toInventoryItemInfo(inventory));
            }
        }
        inventoryRepository.saveAll(toSave);

        return ApiResponse.<List<InventoryItemInfo>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(updatedInventories)
                .message("Inventory item updated successfully")
                .build();
    }


    @Override
    @Transactional
    public ApiResponse<List<InventoryItemInfo>> raiseProductPrice(Map<String, Double> prdIdPercentageMap, User user, String storeId) {
        checkBeforeFetchInventory(user, storeId,prdIdPercentageMap.keySet());
        List<Inventory> inventories = fetchInventoryByProductIdAndStoreId(prdIdPercentageMap.keySet(), storeId);
        List<InventoryItemInfo> updatedInventories = new ArrayList<>();
        List<Inventory> toSave = new ArrayList<>();
        for (Inventory inventory : inventories) {
            Double percentage = prdIdPercentageMap.get(inventory.getProductId());
            if (percentage != null) {
                if (inventory.getCompanyId() == null || !inventory.getCompanyId().equals(user.getCompanyId())) {
                    throw new InvalidOperationException(
                            "update Inventory Item", "not authorized"
                    );
                }
                resetDiscount(inventory);
                raiseProductPriceByPercentage(inventory, percentage,user.getUsername());
                toSave.add(inventory);
                updatedInventories.add(inventoryMapper.toInventoryItemInfo(inventory));
            }
        }
        inventoryRepository.saveAll(toSave);

        return ApiResponse.<List<InventoryItemInfo>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(updatedInventories)
                .message("Inventory item updated successfully")
                .build();
    }



    @Override
    @Transactional
    public ApiResponse<List<InventoryItemInfo>> raiseAllProductPrice(Set<String> prdIds, User user, String storeId, double percentage) {
        checkBeforeFetchInventory(user, storeId,prdIds);
        List<Inventory> inventories = fetchInventoryByProductIdAndStoreId(prdIds, storeId);
        List<InventoryItemInfo> updatedInventories = new ArrayList<>();
        List<Inventory> toSave = new ArrayList<>();
        for (Inventory inventory : inventories) {
            if (inventory.getCompanyId() == null || !inventory.getCompanyId().equals(user.getCompanyId())) {
                throw new InvalidOperationException(
                        "update Inventory Item", "not authorized"
                );
            }
            resetDiscount(inventory);
            raiseProductPriceByPercentage(inventory, percentage,user.getUsername());
            toSave.add(inventory);
            updatedInventories.add(inventoryMapper.toInventoryItemInfo(inventory));
        }
        inventoryRepository.saveAll(toSave);

        return ApiResponse.<List<InventoryItemInfo>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(updatedInventories)
                .message("Inventory item updated successfully")
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<List<InventoryItemInfo>> setDiscount(Map<String, Double> prdIdDiscountMap, User user, String storeId) {
        checkBeforeFetchInventory(user, storeId,prdIdDiscountMap.keySet());
        List<Inventory> inventories = fetchInventoryByProductIdAndStoreId(prdIdDiscountMap.keySet(), storeId);
        List<InventoryItemInfo> updatedInventories = new ArrayList<>();
        List<Inventory> toSave = new ArrayList<>();
        for (Inventory inventory : inventories) {
            Double discount = prdIdDiscountMap.get(inventory.getProductId());
            if (discount != null) {
                setDiscount(inventory, discount, null);
                inventory.setUpdatedAt(LocalDateTime.now());
                inventory.setUpdatedBy(user.getUsername());
                toSave.add(inventory);
                updatedInventories.add(inventoryMapper.toInventoryItemInfo(inventory));
            }
        }
        inventoryRepository.saveAll(toSave);

        return ApiResponse.<List<InventoryItemInfo>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(updatedInventories)
                .message("Inventory item updated successfully")
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<List<InventoryItemInfo>> setDiscountRate(Map<String, Double> prdIdDiscountRateMap, User user, String storeId) {
        checkBeforeFetchInventory(user, storeId,prdIdDiscountRateMap.keySet());
        List<Inventory> inventories = fetchInventoryByProductIdAndStoreId(prdIdDiscountRateMap.keySet(), storeId);
        List<InventoryItemInfo> updatedInventories = new ArrayList<>();
        List<Inventory> toSave = new ArrayList<>();
        for (Inventory inventory : inventories) {
            Double discountRate = prdIdDiscountRateMap.get(inventory.getProductId());
            if (discountRate != null) {
                setDiscount(inventory, null, discountRate);
                inventory.setUpdatedAt(LocalDateTime.now());
                inventory.setUpdatedBy(user.getUsername());
                toSave.add(inventory);
                updatedInventories.add(inventoryMapper.toInventoryItemInfo(inventory));
            }
        }
        inventoryRepository.saveAll(toSave);

        return ApiResponse.<List<InventoryItemInfo>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(updatedInventories)
                .message("Inventory item updated successfully")
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<List<InventoryItemInfo>> setSameDiscountRate(Set<String> prdIds, User user, String storeId, String percentage) {
        checkBeforeFetchInventory(user, storeId,prdIds);
        List<Inventory> inventories = fetchInventoryByProductIdAndStoreId(prdIds, storeId);
        List<InventoryItemInfo> updatedInventories = new ArrayList<>();
        List<Inventory> toSave = new ArrayList<>();
        for (Inventory inventory : inventories) {
            Double discountRate = Double.parseDouble(percentage);
            setDiscount(inventory, null, discountRate);
            inventory.setUpdatedAt(LocalDateTime.now());
            inventory.setUpdatedBy(user.getUsername());
            toSave.add(inventory);
            updatedInventories.add(inventoryMapper.toInventoryItemInfo(inventory));
        }
        inventoryRepository.saveAll(toSave);

        return ApiResponse.<List<InventoryItemInfo>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(updatedInventories)
                .message("Inventory item updated successfully")
                .build();
    }


    @Override
    public ApiResponse<InventoryItemInfo> editInventoryItemQuantity(EditQuantityRequest request, User user) {
        if (user.getCompanyId() == null) {
            throw new InvalidOperationException(
                    "edit Inventory Item", "not authorized"
            );
        }
        if (user.getStoreId() != null && user.getStoreId().equals(request.getStoreId())) {
            throw new InvalidOperationException(
                    "edit Inventory Item", "not authorized"
            );
        }
        Inventory inventory = inventoryRepository.findByIdAndStoreId(request.getInventoryId(), request.getStoreId()).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Inventory Item", "inventory", request.getInventoryId() + " not found in store "
                )
        );
        if (inventory.getCompanyId() == null || !inventory.getCompanyId().equals(user.getCompanyId())) {
            throw new InvalidOperationException(
                    "edit Inventory Item", "not authorized"
            );
        }
        if (request.getNewQuantity() == null || request.getNewQuantity() <= 0) {
            throw new ValidationException("quantity", request.getNewQuantity(), "Quantity cannot be null");
        }
        double quantityDifferent = request.getNewQuantity() - inventory.getQuantity();
        setQuantity(inventory, request.getNewQuantity());
        inventory.setUpdatedBy(user.getUsername());
        inventory.setUpdatedAt(LocalDateTime.now());

        inventoryRepository.save(inventory);
        stockMovementService.editInventoryItemQuantity(request,quantityDifferent , user.getUsername());

        return ApiResponse.<InventoryItemInfo>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(inventoryMapper.toInventoryItemInfo(inventory))
                .message("Inventory item updated successfully")
                .build();
    }

    @Override
    public ApiResponse<List<InventoryItemInfo>> supplyInventoryItems(SupplyRequest request, User user) {
        System.out.println("Hi, I am in supplyInventoryItems");
        if (user.getCompanyId() == null) {
            throw new InvalidOperationException(
                    "supply Inventory Item", "not authorized"
            );
        }
        if (user.getStoreId() != null && !user.getStoreId().equals(request.getStoreId())) {
            throw new InvalidOperationException(
                    "supply Inventory Item", "not authorized"
            );
        }
        List<InventoryItemInfo> updatedInventories = new ArrayList<>();
        request.getProduct_quantity().forEach(
                (productId, quantity) -> {
                    if (quantity < 0) {
                        throw new ValidationException("productId", quantity, "Quantity cannot be negative");
                    }
                    Optional<Inventory> inventoryOpt = inventoryRepository.findByProductIdAndStoreId(productId, request.getStoreId());
                    if (inventoryOpt.isPresent()) {
                        Inventory inventory = inventoryOpt.get();
                        if (inventory.getCompanyId() == null || !inventory.getCompanyId().equals(user.getCompanyId())) {
                            throw new InvalidOperationException(
                                    "supply Inventory Item", "not authorized"
                            );
                        }
                        inventory.setQuantity(inventory.getQuantity() + quantity);
                        inventory.setLastSupplyQuantity(quantity);
                        inventory.setUpdatedAt(LocalDateTime.now());
                        inventory.setUpdatedBy(user.getUsername());
                        inventory = inventoryRepository.save(inventory);
                        updatedInventories.add(inventoryMapper.toInventoryItemInfo(inventory));
                        stockMovementService.addSupplyMove(inventory.getId(),quantity ,request, user);
                    } else {
                        productRequestProducer.requestProductDetails(request.getStoreId(), productId, quantity, user.getUsername());
                        updatedInventories.add(
                                InventoryItemInfo.builder()
                                        .productId(productId)
                                        .storeId(request.getStoreId())
                                        .quantity(quantity)
                                        .build()
                        );
                    }
                }
        );
        return ApiResponse.<List<InventoryItemInfo>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(updatedInventories)
                .message("Inventory item updated successfully")
                .build();
    }

    @Override
    public void createInventoryFromProductDto(ProductDto productDto) {
        String StoreId = correlationStorage.getStoreId(productDto.getCorrelationId());
        double quantity = correlationStorage.getQuantity(productDto.getCorrelationId());

        Inventory inventory = Inventory.builder()
                .storeId(productDto.getStoreId())
                .productId(productDto.getId())
                .companyId(productDto.getCompanyId())
                .productName(productDto.getName())
                .productBrand(productDto.getBrand())
                .productCategory(productDto.getProductCategory())
                .quantity(quantity)
                .reorderLevel(10.0)
                .lastSupplyQuantity(0.0)
                .unit(ItemUnit.PIECE)
                .price(0.1)
                .discount(0.0)
                .discountRate(0.0)
                .discountedPrice(0.0)
                .isDiscounted(false)
                .updatedBy(productDto.getUsername())
                .updatedAt(LocalDateTime.now())
                .build();
        inventory =  inventoryRepository.save(inventory);
        stockMovementService.addCreateInventoryMove(inventory.getId() ,productDto, quantity, StoreId);
        correlationStorage.remove(productDto.getCorrelationId());
    }


    private void setDiscount(Inventory inventory, Double discount, Double discountRate) {
        if (discount != null) {
            useDiscount(inventory, discount);
        } else if (discountRate != null) {
            useDiscountRate(inventory, discountRate);
        } else {
            resetDiscount(inventory);
        }
    }

    private void useDiscountRate(Inventory inventory, Double discountRate) {
        if (discountRate <= 0) {
            throw new ValidationException("discountRate", discountRate, "Discount rate cannot be negative or zero");
        }
        if (discountRate >= 100) {
            throw new ValidationException("discountRate", discountRate, "Discount rate cannot be greater than or equal to 100");
        }
        Double discount = inventory.getPrice() * discountRate / 100;
        inventory.setDiscount(discount);
        inventory.setDiscountRate(discountRate);
        inventory.setDiscountedPrice(inventory.getPrice() - discount);
        inventory.setIsDiscounted(true);
    }

    private void useDiscount(Inventory inventory, Double discount) {
        if (discount <= 0) {
            throw new ValidationException("discount", discount, "Discount cannot be negative or zero");
        }
        if (discount >= inventory.getPrice()) {
            throw new ValidationException("discount", discount, "Discount cannot be greater than or equal to price");
        }
        inventory.setDiscount(discount);
        inventory.setDiscountRate(discount/inventory.getPrice() * 100);
        inventory.setDiscountedPrice(inventory.getPrice() - discount);
        inventory.setIsDiscounted(true);
    }

    private void setQuantity(Inventory inventory, Double quantity) {
        if (quantity != null) {
            if (quantity < 0) {
                throw new ValidationException("quantity", quantity, "Quantity cannot be negative");
            }
            inventory.setQuantity(quantity);
        }
    }

    private void checkBeforeFetchInventory(User user, String storeId,Set<String> prdIds) {
        if (user.getCompanyId() == null) {
            throw new InvalidOperationException(
                    "update Inventory Item", "not authorized"
            );
        }
        if (user.getStoreId() != null && user.getStoreId().equals(storeId)) {
            throw new InvalidOperationException(
                    "update Inventory Item", "not authorized"
            );
        }
        if (prdIds == null || prdIds.isEmpty()) {
            throw new ValidationException("productId", prdIds, "Product id cannot be null");
        }
    }
    private List<Inventory> fetchInventoryByProductIdAndStoreId(Set<String> productIds, String storeId1) {
        List<Inventory> inventories = inventoryRepository.findByProductIdInAndStoreId(productIds, storeId1);
        if (inventories.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Inventory Item", "product", productIds + " not found in store "
            );
        }
        return inventories;
    }

    private void raiseProductPriceByPercentage(Inventory inventory, Double percentage, String updatedBy) {
        if (percentage <= 0) {
            throw new ValidationException("percentage", percentage, "Percentage cannot be negative");
        }
        Double price = inventory.getPrice();
        Double newPrice = price + price * percentage / 100;
        inventory.setPrice(newPrice);
        inventory.setUpdatedAt(LocalDateTime.now());
        inventory.setUpdatedBy(updatedBy);
    }

    private void resetDiscount(Inventory inventory) {
        inventory.setDiscount(null);
        inventory.setDiscountRate(null);
        inventory.setDiscountedPrice(null);
        inventory.setIsDiscounted(false);
    }


}
