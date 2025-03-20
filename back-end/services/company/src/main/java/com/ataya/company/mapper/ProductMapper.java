package com.ataya.company.mapper;

import com.ataya.company.dto.product.ProductInfoResponse;
import com.ataya.company.model.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductInfoResponse toProductInfoResponse(Product product);

    List<ProductInfoResponse> toProductInfoResponseList(List<Product> products);
}
