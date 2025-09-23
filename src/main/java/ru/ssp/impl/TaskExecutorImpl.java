package ru.ssp.impl;

import java.util.List;
import java.util.Optional;

import org.javatuples.Pair;

import lombok.extern.slf4j.Slf4j;

/**
 * выполняет обработку файлов в многопоточном режиме
 * и формирует отчет по TOP слов.
 */
@Slf4j
class TaskExecutorImpl implements TaskExecutor {

    /**
     * сообщение в лог.
     */
    private static final String MSG_START_THREADS

            = "Start analize in {} threads ...";
    /**
     * сообщение в лог.
     */
    private static final String MSG_WAIT_THREAD

            = "waiting...";
    /**
     * сообщение в лог.
     */
    private static final String ILLEGAL_LAUNCH_ARGS

            = "nThread anf tasks size not equals";

    /**
     * выполняет.
     *
     * @param fls коллекция файлов для обработки
     * @param wds параметр количества слов в отчете
     * @param ths параметр количества потоков
     * @return общий отчет
     */
    @Override
    public Optional<List<Pair<String, Integer>>> execute(
            final List<String> fls, final int wds, final int ths) {
        return launch(fls, ths).flatMap(t -> merge(t, wds));
    }

    /**
     * распределяет задачи по потокам и дожидается их выполнения.
     *
     * @param fls коллекция файлов
     * @param ths количество потоков
     * @return коллекция объектов на которых потоки завершили выполнение
     */
    Optional<List<ThreadReport>> launch(
            final List<String> fls, final int ths) {
        // todo не забыть определить эфективное количество потоков
        return Optional.empty();
        // try {
        // log.info(MSG_START_THREADS, nThread);
        // final CountDownLatch latch = new CountDownLatch(nThread);
        // final List<ThreadWordsAnalizer> threads = new ArrayList<>();
        // tasks.forEach(t -> threads.add(
        // new ThreadWordsAnalizerImpl(latch, t, nWord)));
        // threads.forEach(t -> new Thread(t).start());
        // log.info(MSG_WAIT_THREAD);
        // latch.await();
        // return of(threads);
        // } catch (InterruptedException ie) {
        // throw new TaskExecutorInterruptedException();
        // }
    }

    /**
     * сливает результаты в один отчет.
     *
     * @param threadResults коллекция объектов-отчетов из отработавших потоков
     * @param wds           количество слов в требуемом отчете
     * @return суммарный отчет
     */
    Optional<List<Pair<String, Integer>>> merge(
            final List<ThreadReport> threadResults, final int wds) {
        return Optional.empty();
    }
}
