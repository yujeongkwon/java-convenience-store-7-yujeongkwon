package store.inventory.domain;

import static store.exception.messages.ErrorMessage.INSUFFICIENT_STOCK;

public class Stock {

    private int generalStock;
    private int promotionStock;

    public Stock(final int generalStock, final int promotionStock) {
        this.generalStock = generalStock;
        this.promotionStock = promotionStock;
    }

    public void useGeneralStock(final int quantity) {
        validateGeneralStock(quantity);
        generalStock -= quantity;
    }

    public void drainDuringPromotionPeriod(final int totalQuantity) {
        final int remainingPromo = usePromotionStock(totalQuantity);
        useGeneralStock(remainingPromo);
    }

    public void addGeneralStock(final int quantity) {
        this.generalStock += quantity;
    }

    public void addPromotionStock(final int quantity) {
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

    private int usePromotionStock(final int quantity) {
        final int usedStock = Math.min(quantity, promotionStock);
        promotionStock -= usedStock;
        return quantity - usedStock;
    }

    private void validateGeneralStock(final int quantity) {
        if (quantity > generalStock) {
            throw new IllegalArgumentException(INSUFFICIENT_STOCK.getMessage());
        }
    }
}
