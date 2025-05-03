package com.ataya.inventory.controller;

/*
 * Endpoints:
 * endpoint to get all items by search filters
 * endpoint ot create item
 * endpoint to update item
 */

import com.ataya.inventory.dto.InventoryItemInfo;
import com.ataya.inventory.dto.UpdateInventoryRequest;
import com.ataya.inventory.model.User;
import com.ataya.inventory.service.InventoryService;
import com.ataya.inventory.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
                    This endpoint reads token and authorize items those can user access \f
                    
                    Filter items by request parameters \b
                    
                    Request parameters:
                    \s\t
                        - qty: quantity of item \n\t
                        - prc: price of item \n\t
                        - dCnt: discount of item \n\t
                        - dCntR: discount rate of item \n\t
                        - productId: id of product \n\t
                    
                    qty, prc,dCnt, dCntR and productId are optional \s
                    
                    qty, prc,dCnt and dCntR are range values, use '-' to separate min and max values \s

                    ## Authentication: \t Token is required \s
                    ## Authorization: \t No Authority for this endpoint (user with any role can access this endpoint) \t
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

    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<ApiResponse<InventoryItemInfo>> updateInventoryItem(@RequestBody UpdateInventoryRequest requestBody,
                                                                                  @AuthenticationPrincipal User user,
                                                                              @RequestParam(name = "str") String storeId,
                                                                              @RequestParam(name = "prd") String productId){
        return ResponseEntity.ok(inventoryService.updateInventoryItem(requestBody, user,storeId, productId));
    }
}
