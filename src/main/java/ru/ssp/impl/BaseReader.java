package ru.ssp.impl;

import java.util.stream.Stream;

/**
 * базовый класс - источник текстовых строк для анализа.
 */
abstract class BaseReader {

    /**
     * абстрактный метод - поставшик данных из "ичточника".
     *
     * @return поток текстовых строк
     */
    abstract Stream<String> read();
}
