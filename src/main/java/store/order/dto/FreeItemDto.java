package store.order.dto;

import java.util.List;
import java.util.stream.Collectors;
import store.order.domain.CartItem;

public record FreeItemDto(String productName, int quantity) {

    public static List<FreeItemDto> from(List<CartItem> items) {
        return items.stream()
                .map(CartItem::getFreeItems)
                .collect(Collectors.toList());
    }
}
