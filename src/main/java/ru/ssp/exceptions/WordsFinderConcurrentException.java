package ru.ssp.exceptions;

/**
 * исключение вызванное попыткой вызова в процессе обработки предыдущего вызова.
 */
public class WordsFinderConcurrentException extends RuntimeException {
    /**
     * конструктор.
     */
    public WordsFinderConcurrentException() {
        super();
    }
}
