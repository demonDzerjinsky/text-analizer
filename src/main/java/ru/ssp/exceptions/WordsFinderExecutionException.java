package ru.ssp.exceptions;

/**
 * исключение при выполнении поиска.
 */
public class WordsFinderExecutionException extends RuntimeException {
    /**
     * конструктор.
     *
     * @param message
     */
    public WordsFinderExecutionException(final String message) {
        super(message);
    }

    /**
     * конструктор.
     *
     * @param message
     * @param cause
     */
    public WordsFinderExecutionException(
            final String message,
            final Throwable cause) {
        super(message, cause);
    }

}
