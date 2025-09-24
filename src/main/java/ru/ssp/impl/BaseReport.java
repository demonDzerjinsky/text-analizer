package ru.ssp.impl;

import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.Map;
import ru.ssp.exceptions.EosReceivedException;

/**
 * базовый отчет.
 * предусмотрен контракт наполнения и получения данных отчета.
 */
abstract class BaseReport {

    /**
     * хранит статистику слов.
     */
    private final Map<String, MutableCounter> wordCountMap = new HashMap<>();

    /**
     * формирует срез финального отчета на потоке
     * для участия в общем мерже отчета.
     *
     * @return отчет слово - количество повторений, выявленное на потоке
     */
    final Map<String, Integer> getReport() {
        return wordCountMap.entrySet().stream().collect(
            toMap(Map.Entry::getKey, v -> v.getValue().getCount()));
    }

    /**
     * наполняет отчет данными.
     * реализация зависит от источника данных.
     */
    abstract void fillReport() throws EosReceivedException;
}
