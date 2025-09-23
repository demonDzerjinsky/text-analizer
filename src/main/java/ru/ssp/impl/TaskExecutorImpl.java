package ru.ssp.impl;

import static java.util.Optional.of;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return launch(fls, ths)
                .flatMap(this::merge)
                .flatMap(m -> this.getTop(m, wds));
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
     * @return суммарная карта статистики слов
     */
    Optional<Map<String, Integer>> merge(
            final List<ThreadReport> threadResults) {
        return of(threadResults.stream()
                .flatMap(r -> r.getReport().entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (v1, v2) -> v1 + v2)));
    }

    /**
     * определяет top-N слов.
     * кастомный метод, снижаем вычислительную сложность - уходим от
     * полных сортировок и определяем элементы в один проход.
     *
     * @param mergedMap полная карта статистики всех слов
     * @param wds       количество TopN по скольки построить отчет
     * @return отчет TopN
     */
    Optional<List<Pair<String, Integer>>> getTop(
            final Map<String, Integer> mergedMap,
            final int wds) {
        return Optional.empty();
    }

}
