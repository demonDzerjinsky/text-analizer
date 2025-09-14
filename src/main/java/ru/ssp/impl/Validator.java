package ru.ssp.impl;

/**
 * обобщенный интерфейс.
 *
 * @param <T> дженерик-тип
 */
interface Validator<T> {
    /**
     * метод валидации объекта дженерик-типа.
     *
     * @param t объект
     */
    void validate(T t);
}
