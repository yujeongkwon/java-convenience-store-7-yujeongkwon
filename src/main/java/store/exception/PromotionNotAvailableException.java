package store.exception;

import static store.exception.messages.UserPromotionMessage.PROMOTION_NOT_AVAILABLE;

public class PromotionNotAvailableException extends RuntimeException {

    private final int nonPromotionQuantity;

    public PromotionNotAvailableException(final String productName, final int nonPromotionQuantity) {
        super(PROMOTION_NOT_AVAILABLE.format(productName, nonPromotionQuantity));
        this.nonPromotionQuantity = nonPromotionQuantity;
    }

    public int getNonPromotionQuantity() {
        return nonPromotionQuantity;
    }
}
