package ru.ssp.executors;

import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import ru.ssp.exceptions.EosReceivedException;

/**
 * абстрактный счетчик статистики слов.
 * формирование статистики {@code countWords} зависит от источника
 * и реализовано в конкретных классах, например при подсчете из
 * очереди - {@code RunnableQueueWordsCounter}
 */
public abstract class BaseWordsCounter {

    /**
     * считаем за слово последовательность алф-цифр символов начиная
     * с такого количества символов.
     */
    public static final int MIN_WORD_LEN = 3;

    /**
     * хранит статистику слов.
     */
    @Getter
    private final Map<String, MutCounter> wordCountMap = new HashMap<>();

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
