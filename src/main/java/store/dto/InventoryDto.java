package store.dto;

import store.domain.Product;
import store.domain.Promotion;
import store.domain.Stock;

public record InventoryDto(String productName, int price, int stock, String promotionName) {

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