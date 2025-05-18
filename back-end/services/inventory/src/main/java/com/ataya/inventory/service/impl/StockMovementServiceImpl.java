package com.ataya.inventory.service.impl;

import com.ataya.inventory.dto.company.ProductDto;
import com.ataya.inventory.dto.stockMovement.EditQuantityRequest;
import com.ataya.inventory.dto.stockMovement.GetMovementsParameters;
import com.ataya.inventory.dto.stockMovement.MovementInfo;
import com.ataya.inventory.dto.stockMovement.SupplyRequest;
import com.ataya.inventory.enums.MovementType;
import com.ataya.inventory.exception.custom.InvalidOperationException;
import com.ataya.inventory.model.StockMovement;
import com.ataya.inventory.model.User;
import com.ataya.inventory.repo.StockMovementRepository;
import com.ataya.inventory.service.StockMovementService;
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
import java.util.Set;

import static com.ataya.inventory.service.impl.CommonService.addCriteria;
import static com.ataya.inventory.service.impl.CommonService.addCriteriaWithDateRange;

@Service
@RequiredArgsConstructor
public class StockMovementServiceImpl implements StockMovementService {

    private final StockMovementRepository stockMovementRepository;

    @Override
    public void addSupplyMove(String inventoryId,double quantity, SupplyRequest request, User user) {
        System.out.println("Hi, I am in addSupplyMove method");
        StockMovement sm = StockMovement.builder()
                .inventoryId(inventoryId)
                .type(MovementType.INCOMING)
                .storeId(request.getStoreId())
                .quantity(quantity)
                .note(request.getNote())
                .reason(request.getReason())
                .happenedAt(LocalDateTime.now())
                .by(user.getUsername())
                .build();
        stockMovementRepository.save(sm);
    }

    @Override
    public void addCreateInventoryMove(String inventoryId, ProductDto productDto, double quantity, String storeId) {
        StockMovement sm = StockMovement.builder()
                .inventoryId(inventoryId)
                .type(MovementType.NEW)
                .storeId(storeId)
                .quantity(0.0)
                .note("New inventory item created")
                .reason("New Product in store")
                .happenedAt(LocalDateTime.now())
                .by("System")
                .build();
        stockMovementRepository.save(sm);
        User user = User.builder().username(productDto.getUsername()).build();
        SupplyRequest supplyRequest = SupplyRequest.builder()
                .storeId(storeId)
                .note("first supply")
                .reason("first supply")
                .build();
        addSupplyMove(inventoryId, quantity, supplyRequest, user);
    }

    @Override
    public void editInventoryItemQuantity(EditQuantityRequest request,double quantityDifferent , String username) {
        StockMovement sm = StockMovement.builder()
                .inventoryId(request.getInventoryId())
                .type(MovementType.ADJUSTMENT)
                .storeId(request.getStoreId())
                .quantity(quantityDifferent)
                .note(request.getNote())
                .reason(request.getReason())
                .happenedAt(LocalDateTime.now())
                .by(username)
                .build();
        stockMovementRepository.save(sm);
    }

    @Override
    public ApiResponse<List<MovementInfo>> getStockMovements(GetMovementsParameters parameters) {
        if (parameters.getStoreId() == null) {
            throw new InvalidOperationException("Retrieve stock movement info", "Store ID is required");
        }
        List<Criteria> criteria = new ArrayList<>();
        addCriteria(criteria, "storeId", parameters.getStoreId());
        addCriteria(criteria, "inventoryId", parameters.getInventoryId());
        addCriteria(criteria, "type", parameters.getType());
        addCriteriaWithDateRange(criteria, "happenedAt", parameters.getDateRange());

        Query query = new Query();
        query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));

        long total = stockMovementRepository.countStockMovementsByQuery(query);

        int pg = parameters.getPage() == null || parameters.getPage() < 0 ? 0 : parameters.getPage();
        int sz = parameters.getSize() == null || parameters.getSize() < 0 ? 10 : parameters.getSize();

        PageRequest pageRequest = PageRequest.of(pg, sz);
        query.with(pageRequest);
        List<StockMovement> stockMovements = stockMovementRepository.findStockMovementsByQuery(query);

        List<MovementInfo> movementInfos = stockMovements.stream()
                .map(movement -> MovementInfo.builder()
                        .id(movement.getId())
                        .inventoryId(movement.getInventoryId())
                        .storeId(movement.getStoreId())
                        .type(movement.getType().name())
                        .quantity(movement.getQuantity())
                        .note(movement.getNote())
                        .reason(movement.getReason())
                        .happenedAt(movement.getHappenedAt().toString())
                        .by(movement.getBy())
                        .build()
                ).toList();

        return ApiResponse.<List<MovementInfo>>builder()
                .message("Stock movements retrieved successfully")
                .data(movementInfos)
                .total(total)
                .page(pg)
                .size(sz)
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .build();
    }


}
