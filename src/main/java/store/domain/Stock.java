package store.domain;

public class Stock {

    private int generalStock;
    private final int promotionStock;

    public Stock(int generalStock, int promotionStock) {
        this.generalStock = generalStock;
        this.promotionStock = promotionStock;
    }

    public void addGeneralStock(int quantity) {
        this.generalStock += quantity;
    }
}
