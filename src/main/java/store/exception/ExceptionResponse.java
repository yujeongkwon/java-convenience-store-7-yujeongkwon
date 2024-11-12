package store.exception;

public record ExceptionResponse<T>(T result, UserDecisionException userDecisionException) {

    public boolean hasException() {
        return userDecisionException != null;
    }
}