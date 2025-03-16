package com.ataya.company.mapper;

import com.ataya.company.dto.store.response.StoreInfoResponse;
import com.ataya.company.model.Store;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StoreMapper {
     StoreInfoResponse toStoreInfoResponse(Store store);

    List<StoreInfoResponse> toStoreInfoResponseList(List<Store> stores);
}
