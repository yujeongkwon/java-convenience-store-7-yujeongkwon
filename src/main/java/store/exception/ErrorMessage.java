package store.exception;

public enum ErrorMessage {

    INVALID_FILE_FORMAT_ERROR("파일 처리 중 오류가 발생했습니다");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return "[ERROR] " + message;
    }
}
