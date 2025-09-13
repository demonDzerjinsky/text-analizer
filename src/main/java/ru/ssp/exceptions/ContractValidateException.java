package ru.ssp.exceptions;

/**
 * исключение при валидации контракта вызова API.
 */
public class ContractValidateException extends RuntimeException {
    /**
     * конструктор.
     *
     * @param message сообщение ошибки
     */
    public ContractValidateException(final String message) {
        super(message);
    }
}
