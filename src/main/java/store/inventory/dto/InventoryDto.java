package store.inventory.dto;

import store.inventory.domain.InventoryItem;
import store.inventory.domain.Product;
import store.inventory.domain.Stock;

public record InventoryDto(String productName, int price, int stock, String promotionName) {

    public static InventoryDto createGeneralStockDto(InventoryItem inventoryItem) {
        int stock = inventoryItem.getGeneralStock();
        return new InventoryDto(inventoryItem.getProductName(), inventoryItem.getPrice(), stock, null);
    }

    public static InventoryDto createPromotionStockDto(InventoryItem inventoryItem) {
        String promotionName = inventoryItem.getPromotionName();
        if (promotionName != null && !promotionName.isBlank()) {
            return new InventoryDto(inventoryItem.getProductName(), inventoryItem.getPrice(), inventoryItem.getPromotionStock(),
                    promotionName);
        }
        return null;
    }

    public Product toProduct() {
        return new Product(productName, price);
    }

    public Stock toStock() {
        if (promotionName == null) {
            return new Stock(stock, 0);
        }
        return new Stock(0, stock);
    }
}