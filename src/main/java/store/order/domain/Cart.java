package store.order.domain;

import static store.exception.messages.ErrorMessage.DUPLICATE_ORDER_ITEM;
import static store.exception.messages.ErrorMessage.NOT_FOUND;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import store.inventory.domain.InventoryItem;
import store.order.dto.OrderItemDto;

public class Cart {

    private final List<CartItem> items;

    public Cart(List<CartItem> items) {
        this.items = items;
    }

    public static Cart of(List<OrderItemDto> orderItems, List<InventoryItem> inventoryItems, LocalDate today) {
        validateDuplicate(orderItems);
        List<CartItem> cartItems = orderItems.stream()
                .map(orderItemDto -> {
                    InventoryItem inventoryItem = findInventoryItem(orderItemDto.productName(), inventoryItems);
                    return CartItem.from(orderItemDto, inventoryItem, today);
                })
                .collect(Collectors.toList());
        return new Cart(cartItems);
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

    private static InventoryItem findInventoryItem(String productName, List<InventoryItem> inventoryItems) {
        return inventoryItems.stream()
                .filter(item -> item.getProductName().equals(productName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND.getMessage()));
    }
    public List<CartItem> getItems() {
        return items;
    }
}
