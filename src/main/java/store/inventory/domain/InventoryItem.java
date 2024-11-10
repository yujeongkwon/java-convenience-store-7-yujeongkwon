package store.inventory.domain;

import java.time.LocalDate;
import java.util.Optional;

public class InventoryItem {

    private final Product product;
    private final Stock stock;
    private Optional<Promotion> promotion;

    public InventoryItem(Product product, Stock stock, Promotion promotion) {
        this.product = product;
        this.stock = stock;
        this.promotion = Optional.ofNullable(promotion);
    }

    public boolean hasSufficientStock(int quantity) {
        return stock.getTotalStock() >= quantity;
    }

    public String getProductName() {
        return product.name();
    }

    public String getPromotionName() {
        return promotion.map(Promotion::getName).orElse("");
    }

    public int getPrice() {
        return product.price();
    }

    public int getGeneralStock() {
        return stock.getGeneralStock();
    }

    public int getPromotionStock() {
        return stock.getPromotionStock();
    }

    public void changePromotion(Promotion promotion) {
        this.promotion = Optional.ofNullable(promotion);
    }

    public void addGeneralStock(int quantity) {
        stock.addGeneralStock(quantity);
    }

    public void addPromotionStock(int quantity) {
        stock.addPromotionStock(quantity);
    }
}
