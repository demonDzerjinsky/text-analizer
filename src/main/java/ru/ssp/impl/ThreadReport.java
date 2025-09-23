package ru.ssp.impl;

import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.Map;

/**
 * отчет по частоте слов на потоке.
 */
abstract class ThreadReport {

    /**
     * хранит статистику слов.
     */
    private final Map<String, MutableCounter> wordCountMap = new HashMap<>();

    /**
     * формирует срез финального отчета на потоке
     * для участия в общем мерже отчета.
     * @return отчет слово - количество повторений, выявленное на потоке
     */
    Map<String, Integer> getReport() {
        return wordCountMap.entrySet().stream().collect(toMap(
                Map.Entry::getKey, v -> v.getValue().getCount()));
    }
}
