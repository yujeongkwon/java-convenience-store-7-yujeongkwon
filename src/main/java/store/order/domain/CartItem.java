package store.order.domain;

import static store.exception.ErrorMessage.INSUFFICIENT_STOCK;

import store.inventory.domain.InventoryItem;
import store.order.dto.OrderItemDto;

public class CartItem {

    private final String product;
    private final int quantity;

    public CartItem(String product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public static CartItem from(OrderItemDto orderItemDto, InventoryItem inventoryItem) {
        validateStock(orderItemDto, inventoryItem);
        return new CartItem(orderItemDto.productName(), orderItemDto.quantity());
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
