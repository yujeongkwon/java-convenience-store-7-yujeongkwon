package store.inventory.dto;

import store.inventory.domain.InventoryItem;

public record InventoryPairDto(InventoryDto generalStockDto, InventoryDto promotionStockDto) {

    public static InventoryPairDto fromInventory(final InventoryItem inventoryItem) {
        InventoryDto generalStockDto = InventoryDto.createGeneralStockDto(inventoryItem);
        InventoryDto promotionStockDto = InventoryDto.createPromotionStockDto(inventoryItem);
        return new InventoryPairDto(generalStockDto, promotionStockDto);
    }
}
