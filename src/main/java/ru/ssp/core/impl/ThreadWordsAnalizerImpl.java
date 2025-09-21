package ru.ssp.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import lombok.extern.slf4j.Slf4j;
import ru.ssp.core.ThreadWordsAnalizer;

/**
 * Реализует логику анализа текста на одном потоке.
 */
@Slf4j
public final class ThreadWordsAnalizerImpl implements ThreadWordsAnalizer {
    /**
     * сообщение в лог.
     */
    private static final String MSG_START_THREAD

            = "запуск потока с задачей {}";
    /**
     * результат работы анализатора.
     */
    private final List<Pair<String, Integer>> threadTopResult;
    /**
     * задача на анализ текста для потока.
     * коллекция файл - начальная позиция - конечная позиция
     * для чтения и анализа.
     */
    private final List<Triplet<String, Long, Long>> threadTask;
    /**
     * защелка для синхронизации выполнения с параллельными потоками.
     */
    private final CountDownLatch threadLatch;
    /**
     * количество слов в отчете анализатора.
     */
    private int threadReportLength;

    /**
     * конструктор.
     *
     * @param latch
     * @param task   задача на текущий поток
     * @param nWords количество слов в отчете анализа слов
     */
    public ThreadWordsAnalizerImpl(
            final CountDownLatch latch,
            final List<Triplet<String, Long, Long>> task,
            final int nWords) {
        this.threadTask = task;
        this.threadLatch = latch;
        this.threadReportLength = nWords;
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
        try {
            log.info(MSG_START_THREAD, this.threadTask);
        } finally {
            threadLatch.countDown();
        }
    }
}
