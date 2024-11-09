package store.inventory.domain;

import java.util.Optional;

public class Product {

    private final String name;
    private final int price;
    private Optional<Promotion> promotion;

    public Product(String name, int price, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.promotion = Optional.ofNullable(promotion);
    }

    public String getName() {
        return name;
    }

    public String getPromotionName() {
        return promotion.map(Promotion::getName).orElse("");
    }

    public int getPrice() {
        return price;
    }

    protected void changePromotion(Promotion promotion) {
        this.promotion = Optional.ofNullable(promotion);
    }
}
