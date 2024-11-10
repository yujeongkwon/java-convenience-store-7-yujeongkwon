package store.exception;

public class UserDecisionException extends RuntimeException {
    private final boolean userChoice;

    public UserDecisionException(Exception cause, boolean userChoice) {
        super(cause);
        this.userChoice = userChoice;
    }

    public boolean getUserChoice() {
        return userChoice;
    }

    public boolean isPromotionNotAvailableException() {
        return getCause() instanceof PromotionNotAvailableException;
    }

    public boolean isAdditionalBenefitException() {
        return getCause() instanceof AdditionalBenefitException;
    }
}