package ru.ssp.impl;

import java.util.Optional;

/**
 * обобщенный интерфейс валидации.
 *
 * @param <T> дженерик-тип
 */
interface Validator<T> {
    Optional<T> validate(T t);
}
