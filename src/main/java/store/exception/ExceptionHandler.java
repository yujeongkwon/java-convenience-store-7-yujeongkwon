package store.exception;

import static store.exception.ErrorMessage.INVALID_FILE_FORMAT_ERROR;

import java.io.IOException;
import java.util.function.Supplier;
import store.view.OutputView;

public class ExceptionHandler {

    public void handleVoidWithIOException(RunnableWithIOException runnable) {
        try {
            runnable.run();
        } catch (IOException e) {
            OutputView.displayErrorMessage(INVALID_FILE_FORMAT_ERROR.getMessage());
        }
    }
}
