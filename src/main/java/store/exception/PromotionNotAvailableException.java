package store.exception;

public class PromotionNotAvailableException extends RuntimeException {
    public PromotionNotAvailableException(String message) {
        super(message);
    }
}