package com.ataya.contributor.controller;

import com.ataya.contributor.dto.order.ApplyOrderRequest;
import com.ataya.contributor.model.Contributor;
import com.ataya.contributor.service.OrderService;
import com.ataya.contributor.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/apply")
    public ResponseEntity<ApiResponse<String>> applyOrder(@RequestBody ApplyOrderRequest request,
                                                          @AuthenticationPrincipal Contributor user) {
        return ResponseEntity.ok(orderService.applyOrder(request, user));
    }
}
