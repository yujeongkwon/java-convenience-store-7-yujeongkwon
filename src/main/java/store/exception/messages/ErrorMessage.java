package store.exception.messages;

public enum ErrorMessage {

    INVALID_FILE_FORMAT_ERROR("파일 처리 중 오류가 발생했습니다"),
    INVALID_FORMAT("잘못된 입력 형식입니다. [상품명-수량] 형식으로 입력해 주세요."),
    INVALID_NUMBER("유효한 숫자를 입력해 주세요."),
    INVALID_ANSWER("잘못된 입력입니다. Y 또는 N으로 다시 입력해 주세요."),
    MAXIMUM_NUMBER_LENGTH("숫자는 9자 이내여야합니다."),
    DUPLICATE_ORDER_ITEM("중복된 상품이 있습니다. 다시 입력해 주세요."),
    INSUFFICIENT_STOCK("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    NOT_FOUND("존재하지 않는 상품입니다. 다시 입력해 주세요.");
    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return "[ERROR] " + message;
    }

    public String format(Object... args) {
        return String.format(getMessage(), args);
    }
}
