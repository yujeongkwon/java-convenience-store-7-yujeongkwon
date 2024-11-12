package store.order.domain;

import static store.order.validator.CartValidator.findInventoryItem;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import store.inventory.domain.InventoryItem;
import store.order.dto.FreeItemDto;
import store.order.dto.OrderItemDto;
import store.order.dto.ReceiptItemDto;

public class Cart {

    private final List<CartItem> items;

    public Cart(final List<CartItem> items) {
        this.items = items;
    }

    public static Cart of(
            final List<OrderItemDto> orderItems,
            final List<InventoryItem> inventoryItems,
            final LocalDate today) {
        List<CartItem> cartItems = orderItems.stream().map(orderItemDto -> {
            final InventoryItem inventoryItem = findInventoryItem(orderItemDto.productName(), inventoryItems);
            return CartItem.from(orderItemDto, inventoryItem, today);
        }).collect(Collectors.toList());
        return new Cart(cartItems);
    }

    public PaymentCalculator createPaymentCalculator(final boolean membershipStatus) {
        return new PaymentCalculator(this, membershipStatus);
    }

    public int calculateTotalPrice() {
        return items.stream().mapToInt(CartItem::calculateTotalPrice).sum();
    }

    public int calculatePromotionDiscount() {
        return items.stream().mapToInt(CartItem::calculatePromotionDiscount).sum();
    }

    public List<ReceiptItemDto> getPurchasedItems() {
        return ReceiptItemDto.from(items);
    }

    public List<FreeItemDto> getFreeItems() {
        return FreeItemDto.from(items);
    }

    public List<CartItem> getItems() {
        return items;
    }
}
