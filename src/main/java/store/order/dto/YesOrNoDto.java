package store.order.dto;

import static store.exception.messages.ErrorMessage.INVALID_ANSWER;

public record YesOrNoDto(boolean isYesOrNo) {

    public static YesOrNoDto from(final String input) {
        String answer = input.trim().toUpperCase();
        if ("Y".equals(answer)) {
            return new YesOrNoDto(true);
        }
        if ("N".equals(answer)) {
            return new YesOrNoDto(false);
        }

        throw new IllegalArgumentException(INVALID_ANSWER.getMessage());
    }
}
