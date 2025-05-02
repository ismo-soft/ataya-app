package com.ataya.inventory.service.impl;

import com.ataya.inventory.dto.InventoryItemInfo;
import com.ataya.inventory.exception.custom.InvalidOperationException;
import com.ataya.inventory.mapper.InventoryMapper;
import com.ataya.inventory.model.Inventory;
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
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;


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
}
