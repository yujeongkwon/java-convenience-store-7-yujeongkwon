package store.exception.messages;

public enum UserPromotionMessage {

    ADDITIONAL_BENEFIT_AVAILABLE("\n현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까?"),
    PROMOTION_NOT_AVAILABLE("\n현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까?");

    private final String message;

    UserPromotionMessage(String message) {
        this.message = message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }
}
