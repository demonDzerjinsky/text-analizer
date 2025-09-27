package ru.ssp.exceptions;

/**
 * исключение прерывания процесса поставки текстовых строк потребителям.
 */
public class ProducerTerminatedException extends RuntimeException {

    /**
     * конструктор.
     *
     * @param ex {@code Throwable}
     */
    public ProducerTerminatedException(final Throwable ex) {
        super(ex);
    }

    /**
     * конструктор.
     */
    public ProducerTerminatedException() {
        super();
    }
}
