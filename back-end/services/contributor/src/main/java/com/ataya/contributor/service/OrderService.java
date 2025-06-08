package com.ataya.contributor.service;

import com.ataya.contributor.dto.order.ApplyOrderRequest;
import com.ataya.contributor.model.Contributor;
import com.ataya.contributor.util.ApiResponse;

public interface OrderService {
    ApiResponse<String> applyOrder(ApplyOrderRequest request, Contributor user);
}
