package com.ataya.inventory.service.impl;

import com.ataya.inventory.dto.InventoryItemInfo;
import com.ataya.inventory.dto.UpdateInventoryRequest;
import com.ataya.inventory.dto.product.ProductDto;
import com.ataya.inventory.enums.ItemUnit;
import com.ataya.inventory.exception.custom.InvalidOperationException;
import com.ataya.inventory.exception.custom.ResourceNotFoundException;
import com.ataya.inventory.exception.custom.ValidationException;
import com.ataya.inventory.mapper.InventoryMapper;
import com.ataya.inventory.model.Inventory;
import com.ataya.inventory.model.User;
import com.ataya.inventory.repo.InventoryRepository;
import com.ataya.inventory.service.InventoryService;
import com.ataya.inventory.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.ataya.inventory.service.impl.CommonService.*;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryMapper inventoryMapper;
    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryMapper inventoryMapper, InventoryRepository inventoryRepository) {
        this.inventoryMapper = inventoryMapper;
        this.inventoryRepository = inventoryRepository;
    }


    @Override
    public ApiResponse<List<InventoryItemInfo>> getFilteredInventoryItems(String quantity, String price, String discount, String discountRate, String storeId, String companyId, String productId, int page, int size) {
        if(companyId == null) {
            throw new InvalidOperationException(
                    "view Inventory Items","not authorized"
            );
        }

        List<Criteria> criteria = new ArrayList<Criteria>();
        addCriteriaWithRange(criteria, "quantity", quantity);
        addCriteriaWithRange(criteria, "price", price);
        addCriteriaWithRange(criteria, "discount", discount);
        addCriteriaWithRange(criteria, "discountRate", discountRate);
        addCriteria(criteria, "storeId", storeId);
        addCriteria(criteria, "companyId", companyId);
        addCriteria(criteria, "productId", productId);

        Query query = new Query();
        query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));

        long total = inventoryRepository.countInventoryByQuery(query);
        if (size <= 0) {
            size = total == 0 ? 1 : (int) total;
        }
        PageRequest pageRequest = PageRequest.of(page, size);
        query.with(pageRequest);
        List<Inventory> inventories = inventoryRepository.findInventoryByQuery(query);

        return ApiResponse.<List<InventoryItemInfo>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .total(total)
                .page(page)
                .size(size)
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(inventories.stream().map(inventoryMapper::toInventoryItemInfo).toList())
                .message("Inventory items retrieved successfully")
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
        Inventory inventory = inventoryRepository.findByProductIdAndStoreId(productId, storeId).orElseThrow(() -> {
            throw new ResourceNotFoundException(
                    "Inventory Item", "product", productId + " not found in store "
            );
        });
        if (inventory.getCompanyId() == null || !inventory.getCompanyId().equals(user.getCompanyId())) {
            throw new InvalidOperationException(
                    "update Inventory Item", "not authorized"
            );
        }
        if (inventory.getStoreId() != null && !inventory.getStoreId().equals(user.getStoreId())) {
            throw new InvalidOperationException(
                    "update Inventory Item", "not authorized"
            );
        }

        if (ItemUnit.isValidUnit(requestBody.getUnit())) {
            inventory.setUnit(ItemUnit.getUnit(requestBody.getUnit()));
        } else {
            throw new ValidationException("ItemUnit", requestBody.getUnit(), "ItemUnit not valid");
        }

        setQuantity(inventory,requestBody.getQuantity());
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
    public void createProductInventory(ProductDto productDto) {
        // create inventory for products in company stores
        if (productDto.getStoreIds() == null || productDto.getStoreIds().isEmpty()) {
            throw new ValidationException("storeId", productDto.getStoreIds(), "Store id cannot be null");
        }
        List<Inventory> toSave = new ArrayList<>();
        for (String storeId : productDto.getStoreIds()) {
            Inventory inventory = Inventory.builder()
                    .storeId(storeId)
                    .companyId(productDto.getCompanyId())
                    .productId(productDto.getId())
                    .price(0.0)
                    .quantity(0.0)
                    .discount(0.0)
                    .discountRate(0.0)
                    .isDiscounted(false)
                    .reorderLevel(0.0)
                    .unit(ItemUnit.PIECE)
                    .lastSupplyQuantity(0.0)
                    .build();
            toSave.add(inventory);
        }
        inventoryRepository.saveAll(toSave);
    }

    private void setDiscount(Inventory inventory, Double discount, Double discountRate) {
        if (discount != null) {
            useDiscount(inventory, discount);
        } else if (discountRate != null) {
            useDiscountRate(inventory, discountRate);
        } else {
            inventory.setDiscount(null);
            inventory.setDiscountRate(null);
            inventory.setIsDiscounted(false);
        }
    }

    private void useDiscountRate(Inventory inventory, Double discountRate) {
        if (discountRate < 0) {
            throw new ValidationException("discountRate", discountRate, "Discount rate cannot be negative");
        }
        if (discountRate >= 100) {
            throw new ValidationException("discountRate", discountRate, "Discount rate cannot be greater than or equal to 100");
        }
        Double discount = inventory.getPrice() * discountRate / 100;
        inventory.setDiscount(discount);
        inventory.setDiscountRate(discountRate);
        inventory.setIsDiscounted(true);
    }

    private void useDiscount(Inventory inventory, Double discount) {
        if (discount < 0) {
            throw new ValidationException("discount", discount, "Discount cannot be negative");
        }
        if (discount >= inventory.getPrice()) {
            throw new ValidationException("discount", discount, "Discount cannot be greater than or equal to price");
        }
        inventory.setDiscount(discount);
        inventory.setDiscountRate(discount/inventory.getPrice() * 100);
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


}
