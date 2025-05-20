package com.ataya.inventory.controller;

import com.ataya.inventory.dto.InventoryItemInfo;
import com.ataya.inventory.dto.UpdateInventoryRequest;
import com.ataya.inventory.dto.stockMovement.EditQuantityRequest;
import com.ataya.inventory.dto.stockMovement.SupplyRequest;
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
import java.util.Map;
import java.util.Set;

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
                    
                    Request parameters:\s
                    
                        - qty: quantity of item \n\t
                        - prc: price of item \n\t
                        - dis-prc: discounted price of item \n\t
                        - dCnt: discount of item \n\t
                        - dCntR: discount rate of item \n\t
                        - prd: id of product \n\t
                        - pg: page number \n\t
                        - sz: page size \n\t
                    
                    qty, prc, dis-prc, dCnt, dCntR and productId are optional \s
                    qty, prc, dis-prc, dCnt and dCntR are range values, use '-' to separate min and max values \s
                    
                    ### Authentication: \t Token is required \s
                    ### Authorization: \t No Authority for this endpoint (user with any role can access this endpoint) \t
                    """
    )
    public ResponseEntity<ApiResponse<List<InventoryItemInfo>>> getFilteredInventoryItems(@RequestParam(required = false, name = "qty") String quantity,
                                                                                          @RequestParam(required = false, name = "prc") String price,
                                                                                          @RequestParam(required = false, name = "dis-prc") String discountedPrice,
                                                                                          @RequestParam(required = false, name = "dCnt") String discount,
                                                                                          @RequestParam(required = false, name = "dCntR") String discountRate,
                                                                                          @RequestParam(name = "strId") String storeId,
                                                                                          @AuthenticationPrincipal User user,
                                                                                          @RequestParam (required = false, name = "pg") Integer page,
                                                                                          @RequestParam (required = false, name = "sz") Integer size,
                                                                                          @RequestParam(required = false, name = "prd") String productId){
        return ResponseEntity.ok(inventoryService.getFilteredInventoryItems(
                quantity,
                price,
                discountedPrice,
                discount,
                discountRate,
                storeId,
                user.getCompanyId(),
                productId,
                page,
                size
        ));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(
            summary = "update item by store id and product id",
            description = """
                    This endpoint reads token and authorize items those can user access \s
                    This endpoint updates item. when quantity, price, discount and discount rate getting updated the new values will be set \s
                    
                    Request parameters: \s
                    
                        - str: store id \n\t
                        - prd: product id \n\t
                    
                    ### Authentication: \t Token is required \s
                    ### Authorization: \t Admin and Manager can access this endpoint \t
                    """
    )
    public ResponseEntity<ApiResponse<InventoryItemInfo>> updateInventoryItem(@RequestBody UpdateInventoryRequest requestBody,
                                                                              @AuthenticationPrincipal User user,
                                                                              @RequestParam(name = "str") String storeId,
                                                                              @RequestParam(name = "prd") String productId){
        return ResponseEntity.ok(inventoryService.updateInventoryItem(requestBody, user,productId, storeId));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("/update-price/{storeId}")
    @Operation(
            summary = "update item price by store id and product id",
            description = """
                    This endpoint reads token and authorize items those can user access \s
                    This endpoint updates item price. when price getting updated the new values will be set \s
                    
                    Path variables: \s
                    
                        - storeId: store id \n\t
                    
                    ### Authentication: \t Token is required \s
                    ### Authorization: \t Admin and Manager can access this endpoint \t
                    """
    )
    public ResponseEntity<ApiResponse<List<InventoryItemInfo>>> updateProductPrice(@RequestBody Map<String,Double> prdId_priceMap,
                                                                                  @AuthenticationPrincipal User user,
                                                                                  @PathVariable String storeId){
        return ResponseEntity.ok(inventoryService.updateProductPrice(prdId_priceMap, user,storeId));
    }

    @PutMapping("/raise-percentage/{storeId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(
            summary = "update item price by store id and product id",
            description = """
                    This endpoint reads token and authorize items those can user access \s
                    This endpoint updates item price. when price getting updated the new values will be set \s
                    
                    Path variables: \s
                    
                        - storeId: store id \n\t
                    
                    ### Authentication: \t Token is required \s
                    ### Authorization: \t Admin and Manager can access this endpoint \t
                    """
    )
    public ResponseEntity<ApiResponse<List<InventoryItemInfo>>> raiseProductPrice(@RequestBody Map<String,Double> prdId_percentageMap,
                                                                                  @AuthenticationPrincipal User user,
                                                                                  @PathVariable String storeId){
        return ResponseEntity.ok(inventoryService.raiseProductPrice(prdId_percentageMap, user,storeId));
    }

    @PutMapping("/raise-all-percentage/{storeId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(
            summary = "update item price by store id and product id",
            description = """
                    This endpoint reads token and authorize items those can user access \s
                    This endpoint updates item price. when price getting updated the new values will be set \s
                    
                    Path variables: \s
                    
                        - storeId: store id \n\t
                    
                    Request parameters: \s
                    
                        - pct: percentage of price \n\t
                    
                    ### Authentication: \t Token is required \s
                    ### Authorization: \t Admin and Manager can access this endpoint \t
                    """
    )
    public ResponseEntity<ApiResponse<List<InventoryItemInfo>>> raiseAllProductPrice(@RequestBody Set<String> prdIds,
                                                                        @AuthenticationPrincipal User user,
                                                                        @PathVariable String storeId,
                                                                        @RequestParam(name = "pct") double percentage){
        return ResponseEntity.ok(inventoryService.raiseAllProductPrice(prdIds, user,storeId, percentage));
    }

    @PutMapping("/discount/{storeId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(
            summary = "update item discount by store id and product id",
            description = """
                    This endpoint reads token and authorize items those can user access \s
                    This endpoint updates item discount. when discount getting updated the new values will be set \s
                    
                    Path variables: \s
                    
                        - storeId: store id \n\t
                    
                    ### Authentication: \t Token is required \s
                    ### Authorization: \t Admin and Manager can access this endpoint \t
                    """
    )
    public ResponseEntity<ApiResponse<List<InventoryItemInfo>>> updateProductDiscount(@RequestBody Map<String,Double> prdId_discountMap,
                                                                                      @AuthenticationPrincipal User user,
                                                                                      @PathVariable String storeId){
        return ResponseEntity.ok(inventoryService.setDiscount(prdId_discountMap, user,storeId));
    }
    @PutMapping("/discount-rate/{storeId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(
            summary = "update item discount rate by store id and product id",
            description = """
                    This endpoint reads token and authorize items those can user access \s
                    This endpoint updates item discount rate. when discount rate getting updated the new values will be set \s
                    
                    Path variables: \s
                    
                        - storeId: store id \n\t
                    
                    ### Authentication: \t Token is required \s
                    ### Authorization: \t Admin and Manager can access this endpoint \t
                    """
    )
    public ResponseEntity<ApiResponse<List<InventoryItemInfo>>> setDiscountRate(@RequestBody Map<String,Double> prdId_discountRateMap,
                                                                                          @AuthenticationPrincipal User user,
                                                                                          @PathVariable String storeId){
        return ResponseEntity.ok(inventoryService.setDiscountRate(prdId_discountRateMap, user,storeId));
    }
    @PutMapping("/discount-same-rate/{storeId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(
            summary = "update item discount rate by store id and product id",
            description = """
                    This endpoint reads token and authorize items those can user access \s
                    This endpoint updates item discount rate. when discount rate getting updated the new values will be set \s
                    
                    Path variables: \s
                    
                        - storeId: store id \n\t
                    
                    Request parameters: \s
                    
                        - pct: percentage of price \n\t
                    
                    ### Authentication: \t Token is required \s
                    ### Authorization: \t Admin and Manager can access this endpoint \t
                    """
    )
    public ResponseEntity<ApiResponse<List<InventoryItemInfo>>> setSameDiscountRate(@RequestBody Set<String> prdIds,
                                                                                      @AuthenticationPrincipal User user,
                                                                                      @PathVariable String storeId,
                                                                                      @RequestParam(name = "pct") String percentage){
        return ResponseEntity.ok(inventoryService.setSameDiscountRate(prdIds, user,storeId, percentage));
    }

    /*
    * POST: supply inventory item
    * PUT: edit quantity
    * PUT: set Transfer
    * */

    @PostMapping("/supply")
    @Operation(
            summary = "supply inventory item",
            description = """
                    This endpoint reads token and authorize items those can user access \s
                    This endpoint supply inventory item. when quantity getting updated the new values will be set \s
                    
                    ### Authentication: \t Token is required \s
                    ### Authorization: \t Admin and Manager can access this endpoint \t
                    """
    )
    public ResponseEntity<ApiResponse<List<InventoryItemInfo>>> supplyInventoryItem(@RequestBody SupplyRequest request,
                                                                              @AuthenticationPrincipal User user){
        return ResponseEntity.ok(inventoryService.supplyInventoryItems(request, user));
    }

    @PutMapping("/edit-quantity")
    @Operation(
            summary = "edit inventory item quantity",
            description = """
                    This endpoint reads token and authorize items those can user access \s
                    This endpoint edit inventory item quantity. when quantity getting updated the new values will be set \s
                    
                    Request parameters: \s
                    
                        - str: store id \n\t
                        - prd: product id \n\t
                    
                    ### Authentication: \t Token is required \s
                    ### Authorization: \t Admin and Manager can access this endpoint \t
                    """
    )
    public ResponseEntity<ApiResponse<InventoryItemInfo>> editInventoryItemQuantity(@RequestBody EditQuantityRequest request,
                                                                                     @AuthenticationPrincipal User user){
        return ResponseEntity.ok(inventoryService.editInventoryItemQuantity(request, user));
    }



}
