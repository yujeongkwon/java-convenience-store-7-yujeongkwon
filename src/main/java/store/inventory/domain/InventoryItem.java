package store.inventory.domain;

import java.time.LocalDate;

public class InventoryItem {

    private static final int ZERO = 0;

    private final Product product;
    private final Stock stock;
    private final PromotionHandler promotionHandler;

    public InventoryItem(final Product product, final Stock stock, final Promotion promotion) {
        this.product = product;
        this.stock = stock;
        this.promotionHandler = new PromotionHandler(promotion, stock);
    }

    public boolean hasSufficientStock(final int quantity) {
        return stock.getTotalStock() >= quantity;
    }

    public int calculateFreeQuantity(final int quantity,final LocalDate today) {
        if (promotionHandler.isApplicable(quantity, today)) {
            return promotionHandler.calculateFreeQuantity(quantity);
        }
        return ZERO;
    }

    public void drainStock(final int totalQuantity, final LocalDate today) {
        if (promotionHandler.isApplicable(totalQuantity, today)) {
            promotionHandler.checkAdditionalBenefitEligibility(totalQuantity, getProductName());
            promotionHandler.applyDiscount(totalQuantity, getProductName());
            return;
        }

        stock.useGeneralStock(totalQuantity);
    }

    public int calculateTotalPriceForQuantity(final int quantity) {
        return quantity * getPrice();
    }

    public void drainNonPromotionQuantity(final int nonPromotionQuantity) {
        stock.drainDuringPromotionPeriod(nonPromotionQuantity);
    }

    public void addGeneralStock(final int quantity) {
        stock.addGeneralStock(quantity);
    }

    public void addPromotionStock(final int quantity) {
        stock.addPromotionStock(quantity);
    }

    public void changePromotion(final Promotion promotion) {
        promotionHandler.changePromotion(promotion);
    }

    public String getProductName() {
        return product.name();
    }

    public String getPromotionName() {
        return promotionHandler.getPromotionName();
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
}
