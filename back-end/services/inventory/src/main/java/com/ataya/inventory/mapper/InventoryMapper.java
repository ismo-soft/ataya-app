package com.ataya.inventory.mapper;

import com.ataya.inventory.dto.InventoryItemInfo;
import com.ataya.inventory.model.Inventory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    InventoryItemInfo toInventoryItemInfo(Inventory inventory);
}
