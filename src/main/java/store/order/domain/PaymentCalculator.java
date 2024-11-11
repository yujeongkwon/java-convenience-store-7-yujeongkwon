package store.order.domain;

import java.util.List;
import store.order.dto.FreeItemDto;
import store.order.dto.ReceiptDto;
import store.order.dto.ReceiptItemDto;

public class PaymentCalculator {

    private static final int MEMBERSHIP_DISCOUNT_LIMIT = 8000;
    private static final double MEMBERSHIP_DISCOUNT_RATE = 0.3;

    private final Cart cart;
    private final Double membershipDiscountRate;

    public PaymentCalculator(final Cart cart, final boolean membershipStatus) {
        this.cart = cart;
        this.membershipDiscountRate = initializeMembershipDiscountRate(membershipStatus);
    }

    public ReceiptDto generateReceipt() {
        List<ReceiptItemDto> purchasedItems = cart.getPurchasedItems();
        List<FreeItemDto> freeItems = cart.getFreeItems();
        int totalAmount = cart.calculateTotalPrice();
        int promotionDiscount = cart.calculatePromotionDiscount();
        int membershipDiscount = calculateMembershipDiscount(totalAmount, promotionDiscount);
        int finalAmount = calculateFinalAmount(totalAmount, promotionDiscount, membershipDiscount);

        return new ReceiptDto(purchasedItems,
                freeItems,
                totalAmount,
                promotionDiscount,
                membershipDiscount,
                finalAmount);
    }

    private Double initializeMembershipDiscountRate(final boolean membershipStatus) {
        if (membershipStatus) {
            return MEMBERSHIP_DISCOUNT_RATE;
        }
        return 0.0;
    }

    private int calculateMembershipDiscount(final int totalAmount, final int promotionDiscount) {
        final int nonDiscountedAmount = totalAmount - promotionDiscount;
        final int maxMembershipDiscount = (int) (nonDiscountedAmount * membershipDiscountRate);
        return Math.min(maxMembershipDiscount, MEMBERSHIP_DISCOUNT_LIMIT);
    }

    private int calculateFinalAmount(final int totalAmount, final int promotionDiscount, final int membershipDiscount) {
        return totalAmount - promotionDiscount - membershipDiscount;
    }
}
