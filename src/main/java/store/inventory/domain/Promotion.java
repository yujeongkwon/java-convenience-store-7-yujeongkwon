package store.inventory.domain;

import java.time.LocalDate;

public class Promotion {
    private String name;
    private int buyQuantity;
    private int freeQuantity;
    private LocalDate startDate;
    private LocalDate endDate;

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

    public int calculateFreeQuantity(int quantity) {
        return (quantity / (buyQuantity + freeQuantity)) * freeQuantity;
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
