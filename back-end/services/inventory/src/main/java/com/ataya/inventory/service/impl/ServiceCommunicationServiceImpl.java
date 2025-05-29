package com.ataya.inventory.service.impl;

import com.ataya.inventory.dto.contributor.ProductItemDto;
import com.ataya.inventory.model.Inventory;
import com.ataya.inventory.repo.InventoryRepository;
import com.ataya.inventory.service.ServiceCommunicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceCommunicationServiceImpl implements ServiceCommunicationService {

    private final InventoryRepository inventoryRepository;


    @Override
    public List<ProductItemDto> getProducts(String storeId) {

        List<Inventory> inventories = inventoryRepository.findByStoreIdAndQuantityGreaterThanReorderLevel(storeId);
        return inventories.stream()
                .map(inventory -> ProductItemDto.builder()
                        .itemId(inventory.getId())
                        .productId(inventory.getCompanyId())
                        .productName(inventory.getProductName())
                        .productBrand(inventory.getProductBrand())
                        .productCategory(inventory.getProductCategory())
                        .availableQuantity(inventory.getQuantity() < 2*inventory.getReorderLevel() ? inventory.getQuantity() - inventory.getReorderLevel() : null)
                        .isDiscounted(inventory.getIsDiscounted())
                        .discountedPrice(inventory.getDiscountedPrice())
                        .price(inventory.getPrice())
                        .imageUrl(inventory.getProductImageUrl())
                        .build())
                .toList();
    }
}
