package store.inventory.domain;

import static store.exception.ErrorMessage.INSUFFICIENT_STOCK;

public class Stock {

    private static final int ZERO = 0;

    private int generalStock;
    private int promotionStock;

    public Stock(int generalStock, int promotionStock) {
        this.generalStock = generalStock;
        this.promotionStock = promotionStock;
    }

    public void useGeneralStock(int quantity) {
        validateGeneralStock(quantity);
        generalStock -= quantity;
    }

    public void drainDuringPromotionPeriod(int totalQuantity) {
        int remainingPromo = usePromotionStock(totalQuantity);
        useGeneralStock(remainingPromo);
    }

    private int usePromotionStock(int quantity) {
        int usedStock = Math.min(quantity, promotionStock);
        promotionStock -= usedStock;
        return quantity - usedStock;
    }

    private void validateGeneralStock(int quantity) {
        if (quantity > generalStock){
            throw new IllegalArgumentException(INSUFFICIENT_STOCK.getMessage());
        }
    }

    public void addGeneralStock(int quantity) {
        this.generalStock += quantity;
    }

    public void addPromotionStock(int quantity) {
        this.promotionStock += quantity;
    }

    public int getGeneralStock() {
        return generalStock;
    }

    public int getPromotionStock() {
        return promotionStock;
    }

    public int getTotalStock() {
        return generalStock + promotionStock;
    }

    public boolean hasSufficientPromotionStock(int freeQuantity) {
        return promotionStock >= freeQuantity;
    }
}
