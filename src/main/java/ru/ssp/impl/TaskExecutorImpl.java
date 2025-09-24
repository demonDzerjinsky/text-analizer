package ru.ssp.impl;

import static java.util.Optional.of;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
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
                .flatMap(m -> this.getTop(m, wds))
                .flatMap(this::transform);
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
     * полных сортировок больших коллекций и определяем только нужные элементы
     * в один проход.
     *
     * @param mergedMap полная карта статистики всех слов
     * @param wds       количество TopN по скольки построить отчет
     * @return отчет TopN в формате число-коллекция слов
     */
    Optional<List<Pair<Integer, List<String>>>> getTop(
            final Map<String, Integer> mergedMap, final int wds) {
        final LinkedList<Pair<Integer, List<String>>> rpt = new LinkedList<>();
        mergedMap.entrySet().forEach(it -> {
            if (rpt.size() < wds || rpt.get(0).getValue0() <= it.getValue()) {
                for (int i = 0; i < wds; i++) {
                    if (i == rpt.size()) {
                        rpt.add(new Pair<Integer, List<String>>(
                                it.getValue(),
                                Arrays.asList(it.getKey())));
                        break;
                    } else if (rpt.get(i).getValue0() == it.getValue()) {
                        rpt.get(i).getValue1().add(it.getKey());
                        break;
                    } else if (rpt.get(i).getValue0() > it.getValue()) {
                        rpt.add(i, new Pair<Integer, List<String>>(
                                it.getValue(),
                                Arrays.asList(it.getKey())));
                        break;
                    }
                }
                while (rpt.size() >= wds) {
                    rpt.pop();
                }
            }
        });
        return of(rpt);
    }

    /**
     * преобразует отчет из внутреннего формата, полученного при выборке
     * к формату контракта.
     *
     * @param rpt отчет во внутреннем формате
     * @return отчет в формате контракта слово-частота
     */
    Optional<List<Pair<String, Integer>>> transform(
            final List<Pair<Integer, List<String>>> rpt) {
        final List<Pair<String, Integer>> result = new ArrayList<>();
        for (var entry : rpt) {
            for (int i = 0; i < entry.getValue1().size(); i++) {
                result.add(
                    new Pair<String, Integer>(
                        entry.getValue1().get(i), entry.getValue0()));
            }
        }
        return Optional.empty();
    }
}
