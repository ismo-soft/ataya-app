package com.ataya.inventory.service.impl;

import com.ataya.inventory.dto.contributor.ItemInfoDto;
import com.ataya.inventory.dto.contributor.ProductItemDto;
import com.ataya.inventory.dto.contributor.ProductItemDtoPage;
import com.ataya.inventory.model.Inventory;
import com.ataya.inventory.repo.InventoryRepository;
import com.ataya.inventory.service.ServiceCommunicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceCommunicationServiceImpl implements ServiceCommunicationService {

    private final InventoryRepository inventoryRepository;

    @Override
    public ProductItemDtoPage getProducts(String storeId, String name, String category,
                                                Double minPrice, Double maxPrice, String brand,
                                                int page, int size) {

        List<Inventory> inventories = inventoryRepository.findByStoreIdAndQuantityGreaterThanReorderLevel(storeId);
        List<ProductItemDto> products = inventories.stream()
                .filter(inventory -> applyFilters(inventory, name, category, minPrice, maxPrice, brand))
                .skip((long) page * size)
                .limit(size)
                .map(inventory -> ProductItemDto.builder()
                        .itemId(inventory.getId())
                        .productId(inventory.getCompanyId())
                        .productName(inventory.getProductName())
                        .productBrand(inventory.getProductBrand())
                        .productCategory(inventory.getProductCategory())
                        .availableQuantity(inventory.getQuantity() < inventory.getReorderLevel() + 10
                                ? inventory.getQuantity() - inventory.getReorderLevel()
                                : null)
                        .isDiscounted(inventory.getIsDiscounted())
                        .discountedPrice(inventory.getDiscountedPrice())
                        .price(inventory.getPrice())
                        .imageUrl(inventory.getProductImageUrl())
                        .build())
                .toList();
        return ProductItemDtoPage.builder()
                .products(products)
                .totalPages((int) Math.ceil((double) inventories.size() / size))
                .totalElements(inventories.size())
                .pageNumber(page)
                .pageSize(size)
                .build();
    }

    @Override
    public Double getProductPrice(String itemId) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findById(itemId);
        if (inventoryOpt.isPresent()) {
            Inventory inventory = inventoryOpt.get();
            // Get the effective price (discounted if available, otherwise regular price)
            return Boolean.TRUE.equals(inventory.getIsDiscounted()) && inventory.getDiscountedPrice() != null
                    ? inventory.getDiscountedPrice()
                    : inventory.getPrice();
        }
        return 0.0; // Return 0.0 if the item is not found
    }

    @Override
    public List<ItemInfoDto> getProducts(String items) {
        List<String> itemIds = List.of(items.split(","));
        List<Inventory> inventories = inventoryRepository.findAllById(itemIds);
        return inventories.stream()
                .map(inventory -> ItemInfoDto.builder()
                        .itemId(inventory.getId())
                        .productId(inventory.getCompanyId())
                        .productName(inventory.getProductName())
                        .productBrand(inventory.getProductBrand())
                        .productCategory(inventory.getProductCategory())
                        .price(inventory.getIsDiscounted()?
                                inventory.getDiscountedPrice() != null ? inventory.getDiscountedPrice() : inventory.getPrice()
                                : inventory.getPrice())
                        .imageUrl(inventory.getProductImageUrl())
                        .build())
                .toList();
    }


    @Override
    public Map<String, Boolean> areItemsAvailableToBuy(List<String> items) {
        // for each item in items, split "itemId,quantity" and put them in map
        Map<String, Boolean> areItemsAvailable = new HashMap<>();
        for (String item : items) {
            String[] parts = item.split(",");
            if (parts.length != 2) {
                continue; // Skip invalid entries
            }
            String itemId = parts[0].trim();
            Double quantity = Double.parseDouble(parts[1].trim());
            Optional<Inventory> inventoryOpt = inventoryRepository.findById(itemId);
            if (inventoryOpt.isPresent()) {
                Inventory inventory = inventoryOpt.get();
                boolean isAvailable = inventory.getQuantity() >= quantity;
                areItemsAvailable.put(itemId, isAvailable);
            } else {
                areItemsAvailable.put(itemId, false); // Item not found
            }
        }
        return areItemsAvailable;

    }

    @Override
    public ProductItemDtoPage getProductsToDeliver(String storeId, String name, String category, String brand, int page, int size) {
        List<Inventory> inventories = inventoryRepository.findByStoreIdAndWaitingForBeneficiaryQuantityGreaterThan(storeId, 0.0);
        // print all inventories
        inventories.forEach(inventory -> System.out.println("Inventory: " + inventory.getId() + ", Quantity: " + inventory.getWaitingForBeneficiaryQuantity()));
        List<ProductItemDto> products = inventories.stream()
//                .filter(inventory -> applyFilters(inventory, name, category, brand))
                .skip((long) page * size)
                .limit(size)
                .map(inventory -> ProductItemDto.builder()
                        .itemId(inventory.getId())
                        .productId(inventory.getCompanyId())
                        .productName(inventory.getProductName())
                        .productBrand(inventory.getProductBrand())
                        .productCategory(inventory.getProductCategory())
                        .availableQuantity(1.0)
                        .imageUrl(inventory.getProductImageUrl())
                        .build())
                .toList();
        return ProductItemDtoPage.builder()
                .products(products)
                .totalPages((int) Math.ceil((double) inventories.size() / size))
                .totalElements(inventories.size())
                .pageNumber(page)
                .pageSize(size)
                .build();
    }

    private boolean applyFilters(Inventory inventory, String name, String category,
                                 Double minPrice, Double maxPrice, String brand) {

        // Get the effective price (discounted if available, otherwise regular price)
        Double effectivePrice = Boolean.TRUE.equals(inventory.getIsDiscounted()) && inventory.getDiscountedPrice() != null
                ? inventory.getDiscountedPrice()
                : inventory.getPrice();

        // Apply name filter
        if (name != null && !inventory.getProductName().toLowerCase().contains(name.toLowerCase())) {
            return false;
        }

        // Apply category filter
        if (category != null && !inventory.getProductCategory().toLowerCase().contains(category.toLowerCase())) {
            return false;
        }

        // Apply brand filter
        if (brand != null && !inventory.getProductBrand().toLowerCase().contains(brand.toLowerCase())) {
            return false;
        }

        // Apply price filters
        if (minPrice != null && effectivePrice < minPrice) {
            return false;
        }

        return maxPrice == null || effectivePrice <= maxPrice;
    }

    private boolean applyFilters(Inventory inventory, String name, String category,
                                 String brand) {

        // Apply name filter
        if (name != null && !inventory.getProductName().toLowerCase().contains(name.toLowerCase())) {
            return false;
        }

        // Apply category filter
        if (category != null && !inventory.getProductCategory().toLowerCase().contains(category.toLowerCase())) {
            return false;
        }

        // Apply brand filter
        return brand == null || inventory.getProductBrand().toLowerCase().contains(brand.toLowerCase());
        // No price filters applied, so we return true
    }
}
