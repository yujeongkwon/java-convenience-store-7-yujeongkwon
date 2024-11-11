package store.order.dto;

import static store.exception.messages.ErrorMessage.INVALID_FORMAT;

import store.utils.InputProcessor;

public record OrderItemDto(String productName, int quantity) {

    private static final String INPUT_PATTERN = "\\[[^\\[\\]-]+-[0-9]+]";

    public static OrderItemDto from(final String input) {
        validateFormat(input);

        String[] parts = input.replaceAll("[\\[\\]]", "").split("-");
        String productName = parts[0].trim();
        int quantity = InputProcessor.parseInteger(parts[1].trim());

        return new OrderItemDto(productName, quantity);
    }

    private static void validateFormat(final String input) {
        if (!input.matches(INPUT_PATTERN)) {
            throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
        }
    }
}