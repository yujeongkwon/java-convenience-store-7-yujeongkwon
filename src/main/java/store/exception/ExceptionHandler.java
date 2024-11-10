package store.exception;

import static store.exception.messages.ErrorMessage.INVALID_FILE_FORMAT_ERROR;

import java.io.IOException;
import java.util.function.Supplier;
import store.order.dto.YesOrNoDto;
import store.view.InputView;
import store.view.OutputView;

public class ExceptionHandler {

    public <T> ExceptionResponse<T> handleWithRetry(Supplier<T> supplier) {
        while (true) {
            try {
                T result = supplier.get();
                return new ExceptionResponse<>(result, null);
            } catch (PromotionNotAvailableException e) {
                boolean userChoice = getUserConfirmation(e.getMessage());
                return new ExceptionResponse<>(null, new UserDecisionException(e, userChoice, e.getNonPromotionQuantity()));
            } catch (AdditionalBenefitException e) {
                boolean userChoice = getUserConfirmation(e.getMessage());
                return new ExceptionResponse<>(null, new UserDecisionException(e, userChoice, e.getAdditionalEligibleQuantity()));
            } catch (IllegalArgumentException e) {
                OutputView.displayErrorMessage(e.getMessage());
            }
        }
    }

    public void handleVoidWithIOException(RunnableWithIOException runnable) {
        try {
            runnable.run();
        } catch (IOException e) {
            OutputView.displayErrorMessage(INVALID_FILE_FORMAT_ERROR.getMessage());
        }
    }

    private boolean getUserConfirmation(String message) {
        return handleWithRetry(() -> YesOrNoDto.from(InputView.askAboutPromotion(message))).result().isYesOrNo();
    }
}
