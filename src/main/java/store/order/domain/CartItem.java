package store.order.domain;

import static store.exception.ErrorMessage.INSUFFICIENT_STOCK;

import java.time.LocalDate;
import store.inventory.domain.InventoryItem;
import store.order.dto.OrderItemDto;

public class CartItem {

    private final String product;
    private final InventoryItem inventoryItem;
    private final int quantity;
    private final int freeQuantity;

    public CartItem(String product, InventoryItem inventoryItem, int quantity, int freeQuantity) {
        this.product = product;
        this.inventoryItem = inventoryItem;
        this.quantity = quantity;
        this.freeQuantity = freeQuantity;
    }

    public static CartItem from(OrderItemDto orderItemDto, InventoryItem inventoryItem, LocalDate today) {
        validateStock(orderItemDto, inventoryItem);
        int freeQuantity = inventoryItem.calculateFreeQuantity(orderItemDto.quantity(), today);
        return new CartItem(orderItemDto.productName(), inventoryItem, orderItemDto.quantity(), freeQuantity);
    }

    public void applyPromotions() {
        if(freeQuantity != 0) {
            inventoryItem.checkForPromotionStock(freeQuantity);
            inventoryItem.checkForAdditionalBenefit(quantity, freeQuantity);
        }
    }

    private static void validateStock(OrderItemDto orderItemDto, InventoryItem inventoryItem) {
        if (!inventoryItem.hasSufficientStock(orderItemDto.quantity())) {
            throw new IllegalArgumentException(INSUFFICIENT_STOCK.getMessage());
        }
    }

    public String getProductName() {
        return product;
    }
}
