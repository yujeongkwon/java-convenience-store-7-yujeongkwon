package store.domain;

import static store.exception.ErrorMessage.INSUFFICIENT_STOCK;

import store.dto.OrderItemDto;
import store.inventory.domain.InventoryItem;

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

    public String getProductName() {
        return product;
    }

    private static void validateStock(OrderItemDto orderItemDto, InventoryItem inventoryItem) {
        if (!inventoryItem.hasSufficientStock(orderItemDto.quantity())) {
            throw new IllegalArgumentException(INSUFFICIENT_STOCK.getMessage());
        }
    }
}
