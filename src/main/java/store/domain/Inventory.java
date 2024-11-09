package store.domain;

public class Inventory {

    private final Product product;
    private final Stock stock;

    public Inventory(Product product, Stock stock) {
        this.product = product;
        this.stock = stock;
    }

    public String getProductName() {
        return product.getName();
    }

    public String getPromotionName() {
        return product.getPromotionName();
    }

    public int getPrice() {
        return product.getPrice();
    }

    public void changePromotion(Promotion promotion) {
        product.changePromotion(promotion);
    }

    public void addGeneralStock(int quantity) {
        stock.addGeneralStock(quantity);
    }

    public void addPromotionStock(int quantity) {
        stock.addPromotionStock(quantity);
    }

    public int getGeneralStock() {
        return stock.getGeneralStock();
    }

    public int getPromotionStock() {
        return stock.getPromotionStock();
    }
}
