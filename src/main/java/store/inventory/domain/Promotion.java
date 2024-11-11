package store.inventory.domain;

import java.time.LocalDate;

public class Promotion {

    private static final int ZERO = 0;

    private final String name;
    private final int buyQuantity;
    private final int freeQuantity;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(final String name, final int buyQuantity, final int freeQuantity, final LocalDate startDate,
            final LocalDate endDate) {
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.freeQuantity = freeQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int calculateFreeQuantity(final int quantity, final int availableQuantity) {
        int promotionStock = calculateAvailablePromotionStock(quantity, availableQuantity);
        int promotionSetSize = getPromotionSetSize();
        return promotionStock / promotionSetSize;
    }

    public int calculateAvailablePromotionStock(final int quantity, final int availablePromoStock) {
        int promotionSetSize = getPromotionSetSize();
        int maxPromotionStock = (availablePromoStock / promotionSetSize) * promotionSetSize;
        int needs = (quantity / promotionSetSize) * promotionSetSize;
        return Math.min(maxPromotionStock, needs);
    }

    public int calculateRemainingFreeQuantity(final int quantity) {
        if ((quantity % getPromotionSetSize()) >= buyQuantity) {
            return freeQuantity;
        }
        return ZERO;
    }

    public int getPromotionSetSize() {
        return buyQuantity + freeQuantity;
    }

    public String getName() {
        return name;
    }

    public boolean isApplicable(final int quantity, final LocalDate today) {
        return isActive(today) && isEligibleForPromotion(quantity);
    }

    private boolean isActive(final LocalDate today) {
        return (today.isEqual(startDate) ||
                today.isAfter(startDate)) && (today.isBefore(endDate) ||
                today.isEqual(endDate));
    }

    private boolean isEligibleForPromotion(final int quantity) {
        return quantity >= buyQuantity;
    }
}
