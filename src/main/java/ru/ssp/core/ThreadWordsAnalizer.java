package ru.ssp.core;

import java.util.List;

import org.javatuples.Pair;

public interface ThreadWordsAnalizer extends Runnable {

    /**
     * получить результат выполнения анализа.
     *
     * @return рейтинг слов по результату выполнения на текущем потоке.
     */
    List<Pair<String, Integer>> getResult();
}
