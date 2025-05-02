package com.ataya.inventory.controller;

/*
 * Endpoints:
 * endpoint to get all items of store
 * endpoint to get all items of store by search filters
 * endpoint ot create item
 * endpoint to update item
 */

import com.ataya.inventory.dto.InventoryItemInfo;
import com.ataya.inventory.model.User;
import com.ataya.inventory.service.InventoryService;
import com.ataya.inventory.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/get")
    @Operation(
            summary = "get items by store id and search filters",
            description = """
                    - This endpoint reads token and authorize items those can user access
                    - Filter items by request parameters
                    - Request parameters:
                        - qty: quantity of item
                        - prc: price of item
                        - dCnt: discount of item
                        - dCntR: discount rate of item
                        - productId: id of product
                    - qty, prc,dCnt, dCntR and productId are optional
                    - qty, prc,dCnt and dCntR are range values, use '-' to separate min and max values


                    - Authentication: Token is required
                    - Authorization: No Authority for this endpoint (user with any role can access this endpoint)
                    """
    )
    public ResponseEntity<ApiResponse<List<InventoryItemInfo>>> getFilteredInventoryItems(@RequestParam(required = false, name = "qty") String quantity,
                                                                                          @RequestParam(required = false, name = "prc") String price,
                                                                                          @RequestParam(required = false, name = "dCnt") String discount,
                                                                                          @RequestParam(required = false, name = "dCntR") String discountRate,
                                                                                          @AuthenticationPrincipal User user,
                                                                                          @RequestParam (required = false, name = "page") int page,
                                                                                            @RequestParam (required = false, name = "size") int size,
                                                                                          @RequestParam(required = false, name = "prd") String productId){
        return ResponseEntity.ok(inventoryService.getFilteredInventoryItems(
                quantity,price,discount,discountRate,user.getStoreId(),user.getCompanyId(),productId,page,size
        ));
    }
}
