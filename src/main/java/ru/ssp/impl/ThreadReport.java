package ru.ssp.impl;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

/**
 * отчет по частоте слов на потоке.
 */
abstract class ThreadReport {

    /**
     * хранит статистику слов.
     */
    @Getter
    private final Map<String, Integer> wordCountMap = new HashMap<>();
    // TODO заменить на мутабельный счетчик
}
