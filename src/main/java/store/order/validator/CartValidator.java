package store.order.validator;

import static store.exception.messages.ErrorMessage.DUPLICATE_ORDER_ITEM;
import static store.exception.messages.ErrorMessage.NOT_FOUND;

import java.util.List;
import store.inventory.domain.InventoryItem;
import store.order.dto.OrderItemDto;

public class CartValidator {

    public static void validateDuplicate(final List<OrderItemDto> orderItems) {
        long distinctCount = orderItems.stream()
                .map(OrderItemDto::productName)
                .distinct()
                .count();

        if (distinctCount != orderItems.size()) {
            throw new IllegalArgumentException(DUPLICATE_ORDER_ITEM.getMessage());
        }
    }

    public static InventoryItem findInventoryItem(final String productName, final List<InventoryItem> inventoryItems) {
        return inventoryItems.stream()
                .filter(item -> item.getProductName().equals(productName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND.getMessage()));
    }
}
