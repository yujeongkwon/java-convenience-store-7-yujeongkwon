package store.exception;

import java.io.IOException;

@FunctionalInterface
public interface RunnableWithIOException {
    void run() throws IOException;
}