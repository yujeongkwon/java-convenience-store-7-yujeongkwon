package store.inventory.domain;

import java.time.LocalDate;

public class Promotion {

    private static final int ZERO = 0;

    private final String name;
    private final int buyQuantity;
    private final int freeQuantity;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(String name, int buyQuantity, int freeQuantity, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.freeQuantity = freeQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public int calculateFreeQuantity(int quantity, int availableQuantity) {
        int promotionStock = calculateAvailablePromotionStock(quantity, availableQuantity);
        int promotionSetSize = getPromotionSetSize();
        return promotionStock/promotionSetSize;
    }
    
    public int calculateAvailablePromotionStock(int quantity, int availablePromoStock) {
        int promotionSetSize = getPromotionSetSize();
        int maxPromotionStock = (availablePromoStock / promotionSetSize) * promotionSetSize;
        int needs = (quantity / promotionSetSize) * promotionSetSize;
        return Math.min(maxPromotionStock, needs);
    }

    public int calculateRemainingFreeQuantity(int quantity) {
        if ((quantity % getPromotionSetSize()) >= buyQuantity) {
            return freeQuantity;
        }
        return ZERO;
    }

    public int getPromotionSetSize() {
        return buyQuantity + freeQuantity;
    }

    public boolean isApplicable(int quantity, LocalDate today) {
        return isActive(today) && isEligibleForPromotion(quantity);
    }

    private boolean isActive(LocalDate today) {
        return (today.isEqual(startDate) ||
                today.isAfter(startDate)) && (today.isBefore(endDate) ||
                today.isEqual(endDate));
    }

    private boolean isEligibleForPromotion(int quantity) {
        return quantity >= buyQuantity;
    }
}
