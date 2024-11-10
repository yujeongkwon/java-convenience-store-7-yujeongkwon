package store.exception;

import static store.exception.messages.UserPromotionMessage.ADDITIONAL_BENEFIT_AVAILABLE;

public class AdditionalBenefitException extends RuntimeException {
    private final int additionalEligibleQuantity;

    public AdditionalBenefitException(String productName, int additionalEligibleQuantity) {
        super(ADDITIONAL_BENEFIT_AVAILABLE.format(productName, additionalEligibleQuantity));
        this.additionalEligibleQuantity = additionalEligibleQuantity;
    }

    public int getAdditionalEligibleQuantity() {
        return additionalEligibleQuantity;
    }
}
