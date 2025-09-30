package ru.ssp.exceptions;

/**
 * исключение при получении признака завершения потока.
 * end-of-stream.
 */
public class EosReceivedException extends Exception {

    /**
     * конструктор.
     */
    public EosReceivedException() {
        super();
    }
}
