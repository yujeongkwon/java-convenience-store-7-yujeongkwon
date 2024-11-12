package store.order.domain;

import static store.exception.messages.ErrorMessage.INSUFFICIENT_STOCK;

import java.time.LocalDate;
import store.inventory.domain.InventoryItem;
import store.order.dto.FreeItemDto;
import store.order.dto.OrderItemDto;

public class CartItem {

    private final String product;
    private final InventoryItem inventoryItem;
    private int buyQuantity;
    private int freeQuantity;

    public CartItem(
            final String product,
            final InventoryItem inventoryItem,
            final int quantity,
            final int freeQuantity) {
        this.product = product;
        this.inventoryItem = inventoryItem;
        this.buyQuantity = quantity;
        this.freeQuantity = freeQuantity;
    }

    public static CartItem from(
            final OrderItemDto orderItemDto,
            final InventoryItem inventoryItem,
            final LocalDate today) {
        validateStock(orderItemDto, inventoryItem);
        final int freeQuantity = inventoryItem.calculateFreeQuantity(orderItemDto.quantity(), today);
        final int buyQuantity = orderItemDto.quantity() - freeQuantity;
        return new CartItem(orderItemDto.productName(), inventoryItem, buyQuantity, freeQuantity);
    }

    private static void validateStock(final OrderItemDto orderItemDto, final InventoryItem inventoryItem) {
        if (!inventoryItem.hasSufficientStock(orderItemDto.quantity())) {
            throw new IllegalArgumentException(INSUFFICIENT_STOCK.getMessage());
        }
    }

    public void drainStock(final LocalDate today) {
        final int totalQuantity = getTotalQuantity();
        inventoryItem.drainStock(totalQuantity, today);
    }

    public void handlePromotionShortage(final boolean userChoice, final int nonPromotionQuantity) {
        if (userChoice) {
            inventoryItem.drainNonPromotionQuantity(nonPromotionQuantity);
            return;
        }

        buyQuantity -= nonPromotionQuantity;
    }

    public void addEligibleFreeItems(final int additionalEligibleQuantity) {
        inventoryItem.drainNonPromotionQuantity(additionalEligibleQuantity);
        freeQuantity += additionalEligibleQuantity;
    }

    public int calculateTotalPrice() {
        return inventoryItem.calculateTotalPriceForQuantity(buyQuantity);
    }

    public int calculatePromotionDiscount() {
        return inventoryItem.calculateTotalPriceForQuantity(freeQuantity);
    }

    public int getTotalQuantity() {
        return buyQuantity + freeQuantity;
    }

    public FreeItemDto getFreeItems() {
        return new FreeItemDto(product, freeQuantity);
    }

    public String getProductName() {
        return product;
    }
}
