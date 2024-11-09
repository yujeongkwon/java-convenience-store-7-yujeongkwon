package store.dto;

import store.domain.Inventory;

public record InventoryPairDto(InventoryDto generalStockDto, InventoryDto promotionStockDto) {

    public static InventoryPairDto fromInventory(Inventory inventory) {
        InventoryDto generalStockDto = InventoryDto.createGeneralStockDto(inventory);
        InventoryDto promotionStockDto = InventoryDto.createPromotionStockDto(inventory);
        return new InventoryPairDto(generalStockDto, promotionStockDto);
    }
}
