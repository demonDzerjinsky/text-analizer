package ru.ssp.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import ru.ssp.core.ThreadWordsAnalizer;

/**
 * Реализует логику анализа текста на одном потоке.
 */
public final class ThreadWordsAnalizerImpl implements ThreadWordsAnalizer {
    /**
     * результат работы анализатора.
     */
    private final List<Pair<String, Integer>> threadTopResult;
    /**
     * задача на анализ текска для потока.
     * коллекция файл - начальная позиция - конечная позиция
     * для чтения и анализа.
     */
    private final List<Triplet<String, Long, Long>> threadTask;
    /**
     * защелка для синхронизации выполнения с параллельными потоками.
     */
    private final CountDownLatch threadLatch;

    /**
     * конструктор.
     *
     * @param latch
     * @param task задача на текущий поток
     */
    public ThreadWordsAnalizerImpl(
            final CountDownLatch latch,
            final List<Triplet<String, Long, Long>> task) {
        this.threadTask = task;
        this.threadLatch = latch;
        this.threadTopResult = new ArrayList<>();
    }

    /**
     * реализация интерфейса через который можно получить
     * результаты выполненного задания.
     */
    @Override
    public List<Pair<String, Integer>> getResult() {
        return threadTopResult;
    }

    /**
     * метод выполнения задачи потока.
     */
    @Override
    public void run() {
        // TODO Auto-generated method stub
    }
}
