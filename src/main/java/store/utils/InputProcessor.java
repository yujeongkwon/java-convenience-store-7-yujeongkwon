package store.utils;

import static store.exception.messages.ErrorMessage.INVALID_NUMBER;
import static store.exception.messages.ErrorMessage.MAXIMUM_NUMBER_LENGTH;

public class InputProcessor {

    private static final int MAX_LENGTH = 9;

    public static Integer parseInteger(final String input) {
        validateInt(input);

        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(INVALID_NUMBER.getMessage());
        }
    }

    private static void validateInt(final String input) {
        validatePresence(input);
        validateLength(input);
    }

    private static void validatePresence(final String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException(INVALID_NUMBER.getMessage());
        }
    }

    public static void validateLength(final String input) {
        if (input.trim().length() > MAX_LENGTH) {
            throw new IllegalArgumentException(MAXIMUM_NUMBER_LENGTH.getMessage());
        }
    }
}
