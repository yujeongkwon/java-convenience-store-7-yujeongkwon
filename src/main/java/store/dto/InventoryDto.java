package store.dto;

import store.domain.Inventory;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.Stock;

public record InventoryDto(String productName, int price, int stock, String promotionName) {

    public static InventoryDto createGeneralStockDto(Inventory inventory) {
        int stock = inventory.getGeneralStock();
        return new InventoryDto(inventory.getProductName(), inventory.getPrice(), stock, null);
    }

    public static InventoryDto createPromotionStockDto(Inventory inventory) {
        String promotionName = inventory.getPromotionName();
        if (promotionName != null && !promotionName.isBlank()) {
            return new InventoryDto(inventory.getProductName(), inventory.getPrice(), inventory.getPromotionStock(),
                    promotionName);
        }
        return null;
    }

    public Product toProduct(Promotion promotion) {
        return new Product(productName, price, promotion);
    }

    public Stock toStock() {
        if (promotionName == null) {
            return new Stock(stock, 0);
        }
        return new Stock(0, stock);
    }
}