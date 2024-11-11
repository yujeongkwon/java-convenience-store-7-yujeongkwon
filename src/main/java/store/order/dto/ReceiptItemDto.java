package store.order.dto;

import java.util.List;
import store.order.domain.CartItem;

public record ReceiptItemDto(String productName, int quantity, int price) {

    public static List<ReceiptItemDto> from(List<CartItem> items) {
        return items.stream()
                .map(item -> new ReceiptItemDto(
                        item.getProductName(),
                        item.getTotalQuantity(),
                        item.calculateTotalPrice()
                ))
                .toList();
    }
}