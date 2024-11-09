package store.exception;

public enum ErrorMessage {

    INVALID_FILE_FORMAT_ERROR("파일 처리 중 오류가 발생했습니다"),
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
}
