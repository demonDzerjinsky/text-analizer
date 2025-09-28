package ru.ssp.executors;

import reactor.core.publisher.Flux;

/**
 * базовый класс - источник текстовых строк для анализа.
 */
abstract class BaseReader {

    /**
     * абстрактный метод - поставшик данных из "ичточника".
     *
     * @return поток текстовых строк
     */
    abstract Flux<String> getAsStream();
}
