package store.domain;

public class Stock {

    private int generalStock;
    private int promotionStock;

    public Stock(int generalStock, int promotionStock) {
        this.generalStock = generalStock;
        this.promotionStock = promotionStock;
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
}
