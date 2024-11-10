package store.inventory.domain;

import java.time.LocalDate;
import java.util.Optional;
import store.exception.AdditionalBenefitException;
import store.exception.PromotionNotAvailableException;

public class InventoryItem {

    private static final int ZERO = 0;

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

    public int calculateFreeQuantity(int quantity, LocalDate today) {
        if (isPromotionApplicable(quantity, today)) {
            return promotion.get().calculateFreeQuantity(quantity, stock.getPromotionStock());
        }
        return ZERO;
    }

    public void drainStock(int totalQuantity, LocalDate today) {
        if (!isPromotionApplicable(totalQuantity, today)) {
            stock.useGeneralStock(totalQuantity);
            return;
        }

        checkForAdditionalBenefit(totalQuantity);
        applyPromotionDiscount(totalQuantity);
    }

    public void checkForAdditionalBenefit(int quantity) {
        int additionalEligibleQuantity = calculateRemainingFreeQuantity(quantity);
        if (isEligibleForAdditionalBenefit(additionalEligibleQuantity, quantity)) {
            throw new AdditionalBenefitException(getProductName(), additionalEligibleQuantity);
        }
    }

    public void addEligibleQuantity(int quantity) {
        stock.drainDuringPromotionPeriod(quantity);
    }

    public void applyPromotionDiscount(int quantity) {
        int promotionQuantity = calculatePromotionStock(quantity);
        int nonPromotionQuantity = quantity - promotionQuantity;

        stock.drainDuringPromotionPeriod(promotionQuantity);
        if (nonPromotionQuantity > ZERO) {
            throw new PromotionNotAvailableException(getProductName(), nonPromotionQuantity);
        }
    }

    public void drainNonPromotionQuantity(int nonPromotionQuantity) {
        stock.drainDuringPromotionPeriod(nonPromotionQuantity);
    }

    private boolean isPromotionApplicable(int quantity, LocalDate today) {
        return promotion.isPresent() && promotion.get().isApplicable(quantity, today);
    }

    public int calculateRemainingFreeQuantity(int quantity) {
        return promotion.map(promo -> promo.calculateRemainingFreeQuantity(quantity)).orElse(ZERO);
    }

    private boolean isEligibleForAdditionalBenefit(int additionalEligibleQuantity, int quantity) {
        int availablePromotionStock = calculatePromotionStock(quantity);
        int totalRequiredStock = calculateTotalRequiredStock(additionalEligibleQuantity, availablePromotionStock);

        return additionalEligibleQuantity > ZERO && stock.getPromotionStock() >= totalRequiredStock;
    }

    private int calculatePromotionStock(int quantity) {
        return promotion.get().calculateAvailablePromotionStock(quantity, stock.getPromotionStock());
    }

    private int calculateTotalRequiredStock(int additionalEligibleQuantity, int availablePromotionStock) {
        int promotionSetSize = promotion.get().getPromotionSetSize();
        return availablePromotionStock + additionalEligibleQuantity * promotionSetSize;
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
