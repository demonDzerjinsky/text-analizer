package ru.ssp.impl;

import java.util.Optional;

/**
 * обобщенный интерфейс валидации.
 *
 * @param <T> дженерик-тип - входной контракт
 * @param <R> дженерик-тип - выходной контракт (в сл ошибок валидации)
 */
interface Validator<T, R> {
    Optional<R> validate(T t);
}
