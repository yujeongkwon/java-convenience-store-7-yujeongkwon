package store.exception;

public class UserDecisionException extends RuntimeException {

    private final boolean userChoice;
    private final int quantity;

    public UserDecisionException(final RuntimeException cause, final boolean userChoice, final int quantity) {
        super(cause);
        this.userChoice = userChoice;
        this.quantity = quantity;
    }

    public boolean getUserChoice() {
        return userChoice;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isPromotionNotAvailableException() {
        return getCause() instanceof PromotionNotAvailableException;
    }

    public boolean isAdditionalBenefitException() {
        return getCause() instanceof AdditionalBenefitException;
    }
}
