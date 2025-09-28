package ru.ssp.executors;

import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.Map;
import ru.ssp.exceptions.EosReceivedException;

/**
 * счетчик статистики слов.
 * предусмотрен контракт наполнения и получения статистики по словам.
 */
public abstract class BaseWordsCounter {

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
    public final Map<String, Integer> getWordsCounts() {
        return wordCountMap.entrySet().stream().collect(
            toMap(Map.Entry::getKey, v -> v.getValue().getCount()));
    }

    /**
     * наполняет отчет данными.
     * реализация зависит от источника данных.
     */
    abstract void countWords() throws EosReceivedException;
}
