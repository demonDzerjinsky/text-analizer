package ru.ssp.exceptions;

/**
 * исключение вызванное попыткой вызова в процессе обработки предыдущего вызова.
 *
 */
public class WordsFinderConcurrentException extends RuntimeException {
    /**
     * конструктор.
     *
     * @param message
     */
    public WordsFinderConcurrentException(final String message) {
        super(message);
    }
}
