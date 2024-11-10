package store.order.domain;

import static store.exception.messages.ErrorMessage.INSUFFICIENT_STOCK;

import java.time.LocalDate;
import store.inventory.domain.InventoryItem;
import store.order.dto.OrderItemDto;

public class CartItem {

    private final String product;
    private final InventoryItem inventoryItem;
    private int buyQuantity;
    private int freeQuantity;

    public CartItem(String product, InventoryItem inventoryItem, int quantity, int freeQuantity) {
        this.product = product;
        this.inventoryItem = inventoryItem;
        this.buyQuantity = quantity;
        this.freeQuantity = freeQuantity;
    }

    public static CartItem from(OrderItemDto orderItemDto, InventoryItem inventoryItem, LocalDate today) {
        validateStock(orderItemDto, inventoryItem);
        int freeQuantity = inventoryItem.calculateFreeQuantity(orderItemDto.quantity(), today);
        int buyQuantity = orderItemDto.quantity() - freeQuantity;
        return new CartItem(orderItemDto.productName(), inventoryItem, buyQuantity, freeQuantity);
    }

    public void drainStock(LocalDate today) {
        int totalQuantity = getTotalQuantity();
        inventoryItem.drainStock(totalQuantity,today);
    }

    public void handlePromotionShortage(boolean userChoice, int nonPromotionQuantity) {
        if (userChoice) {
            inventoryItem.drainNonPromotionQuantity(nonPromotionQuantity);
            return ;
        }

        buyQuantity -= nonPromotionQuantity;
    }

    public void addEligibleFreeItems(int additionalEligibleQuantity) {
        inventoryItem.addEligibleQuantity(additionalEligibleQuantity);
        freeQuantity += additionalEligibleQuantity;
    }

    private static void validateStock(OrderItemDto orderItemDto, InventoryItem inventoryItem) {
        if (!inventoryItem.hasSufficientStock(orderItemDto.quantity())) {
            throw new IllegalArgumentException(INSUFFICIENT_STOCK.getMessage());
        }
    }

    private int getTotalQuantity() {
        return buyQuantity + freeQuantity;
    }

    public String getProductName() {
        return product;
    }
}
