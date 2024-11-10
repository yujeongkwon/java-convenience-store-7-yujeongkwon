package store.inventory.domain;

import java.time.LocalDate;
import java.util.Optional;
import store.exception.AdditionalBenefitException;
import store.exception.ErrorMessage;
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
        if (isApplicablePromotion(quantity, today)) {
            return promotion.get().calculateFreeQuantity(quantity, stock.getPromotionStock());
        }

        return ZERO;
    }

    public void drainStock(int totalQuantity, LocalDate today) {
        if (!isApplicablePromotion(totalQuantity, today)) {
            stock.useGeneralStock(totalQuantity);
        }

        checkForAdditionalBenefit(totalQuantity);
        applyPromotions(totalQuantity);
    }

    public void checkForAdditionalBenefit(int quantity) {
        int additionalEligibleQuantity = calculateRemainingFreeQuantity(quantity);
        if (isAdditionalBenefit(additionalEligibleQuantity, quantity)) {
            throw new AdditionalBenefitException(
                    ErrorMessage.ADDITIONAL_BENEFIT_AVAILABLE.format(getProductName(), additionalEligibleQuantity));
        }
    }

    public void applyPromotions(int quantity) {
        int promotionQuantity = promotion.get().calculateAvailablePromotionStock(quantity, stock.getPromotionStock());
        int nonPromotionQuantity = quantity - promotionQuantity;

        if (nonPromotionQuantity > 0) {
            throw new PromotionNotAvailableException(
                    ErrorMessage.PROMOTION_NOT_AVAILABLE.format(getProductName(), nonPromotionQuantity));
        }
        stock.drainDuringPromotionPeriod(promotionQuantity);
    }

    private boolean isApplicablePromotion(int quantity, LocalDate today) {
        return promotion.isPresent() && promotion.get().isApplicable(quantity, today);
    }

    private int calculateRemainingFreeQuantity(int quantity) {
        return promotion.map(promo -> promo.calculateRemainingFreeQuantity(quantity)).orElse(ZERO);
    }


    private boolean isAdditionalBenefit(int additionalEligibleQuantity, int quantity) {
        int available = promotion.get().calculateAvailablePromotionStock(quantity, stock.getPromotionStock());
        int PromotionSetSize = promotion.get().getPromotionSetSize();
        int totalQuantity = available + additionalEligibleQuantity * PromotionSetSize;

        boolean hasAdditionalBenefit = additionalEligibleQuantity > ZERO;
        boolean hasSufficientPromotionStock = stock.getPromotionStock() >= totalQuantity;

        return hasAdditionalBenefit && hasSufficientPromotionStock;
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
