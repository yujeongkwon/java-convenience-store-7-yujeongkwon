package store.order.domain;

import static store.exception.ErrorMessage.DUPLICATE_ORDER_ITEM;
import static store.exception.ErrorMessage.NOT_FOUND;

import java.util.List;
import java.util.stream.Collectors;
import store.order.dto.OrderItemDto;
import store.inventory.domain.InventoryItem;

public class Cart {

    private final List<CartItem> items;

    public Cart(List<CartItem> items) {
        this.items = items;
    }

    public static Cart of(List<OrderItemDto> orderItems, List<InventoryItem> inventoryItems) {
        validateDuplicate(orderItems);
        List<CartItem> cartItems = orderItems.stream()
                .map(orderItemDto -> {
                    InventoryItem inventoryItem = findInventoryItem(orderItemDto, inventoryItems);
                    return CartItem.from(orderItemDto, inventoryItem);
                })
                .collect(Collectors.toList());
        return new Cart(cartItems);
    }

    public List<CartItem> getItems() {
        return items;
    }

    private static void validateDuplicate(List<OrderItemDto> orderItems) {
        long distinctCount = orderItems
                .stream()
                .map(OrderItemDto::productName)
                .distinct()
                .count();

        if (distinctCount != orderItems.size()) {
            throw new IllegalArgumentException(DUPLICATE_ORDER_ITEM.getMessage());
        }
    }

    private static InventoryItem findInventoryItem(OrderItemDto dto, List<InventoryItem> inventoryItems) {
        return inventoryItems.stream()
                .filter(item -> item.getProductName().equals(dto.productName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND.getMessage()));
    }
}
