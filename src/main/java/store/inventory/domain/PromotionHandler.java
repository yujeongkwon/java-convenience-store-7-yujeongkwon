package store.inventory.domain;

import java.time.LocalDate;
import java.util.Optional;
import store.exception.AdditionalBenefitException;
import store.exception.PromotionNotAvailableException;

public class PromotionHandler {

    private static final int ZERO = 0;

    private final Stock stock;
    private Optional<Promotion> promotion;

    public PromotionHandler(final Promotion promotion, final Stock stock) {
        this.promotion = Optional.ofNullable(promotion);
        this.stock = stock;
    }

    public boolean isApplicable(final int quantity, final LocalDate today) {
        return promotion.isPresent() && promotion.get().isApplicable(quantity, today);
    }

    public int calculateFreeQuantity(final int quantity) {
        return promotion.map(promo -> promo.calculateFreeQuantity(quantity, stock.getPromotionStock())).orElse(0);
    }

    public void applyDiscount(final int quantity, String productName) {
        final int promotionQuantity = calculatePromotionStock(quantity);
        final int nonPromotionQuantity = quantity - promotionQuantity;

        stock.drainDuringPromotionPeriod(promotionQuantity);
        if (nonPromotionQuantity > ZERO) {
            throw new PromotionNotAvailableException(productName, nonPromotionQuantity);
        }
    }

    public void checkAdditionalBenefitEligibility(final int quantity, final String productName) {
        final int additionalEligibleQuantity = calculateRemainingFreeQuantity(quantity);
        if (isEligibleForAdditionalBenefit(additionalEligibleQuantity, quantity)) {
            throw new AdditionalBenefitException(productName, additionalEligibleQuantity);
        }
    }

    public void changePromotion(final Promotion promotion) {
        this.promotion = Optional.ofNullable(promotion);
    }

    public String getPromotionName() {
        return promotion.map(Promotion::getName).orElse("");
    }

    private int calculateRemainingFreeQuantity(final int quantity) {
        return promotion.map(promo -> promo.calculateRemainingFreeQuantity(quantity)).orElse(ZERO);
    }

    private boolean isEligibleForAdditionalBenefit(final int additionalEligibleQuantity, final int quantity) {
        final int availablePromotionStock = calculatePromotionStock(quantity);
        final int totalRequiredStock = calculateTotalRequiredStock(additionalEligibleQuantity, availablePromotionStock);

        return additionalEligibleQuantity > ZERO && stock.getPromotionStock() >= totalRequiredStock;
    }

    private int calculatePromotionStock(final int quantity) {
        return promotion.map(promo -> promo.calculateAvailablePromotionStock(quantity, stock.getPromotionStock()))
                .orElse(ZERO);
    }

    private int calculateTotalRequiredStock(final int additionalEligibleQuantity, final int availablePromotionStock) {
        final int promotionSetSize = promotion.get().getPromotionSetSize();
        return availablePromotionStock + additionalEligibleQuantity * promotionSetSize;
    }
}
