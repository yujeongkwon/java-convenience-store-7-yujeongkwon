package store.order.dto;

import java.util.List;

public record ReceiptDto(List<ReceiptItemDto> purchasedItems, List<FreeItemDto> freeItems, int totalAmount,
                         int promotionDiscount, int membershipDiscount, int finalAmount) {

}