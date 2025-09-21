package ru.ssp.impl;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import lombok.extern.slf4j.Slf4j;
import ru.ssp.core.ThreadWordsAnalizer;
import ru.ssp.core.impl.ThreadWordsAnalizerImpl;
import ru.ssp.exceptions.NullResultInTaskExecutor;
import ru.ssp.exceptions.TaskExecutorInterruptedException;

/**
 * выполняет пул задач в многопоточном режиме
 * и формирует отчет по TOP слов.
 */
@Slf4j
class TaskExecutorImpl implements TaskExecutor {

    /**
     * сообщение в лог.
     */
    private static final String MSG_START_THREAD

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
     * выполняет пул задач.
     *
     * @param tasks   пул задач сформированных для {@code nThread} потоков
     * @param nWord   параметр количества слов в отчете
     * @param nThread параметр количества потоков
     * @return общий отчет
     */
    @Override
    public List<Pair<String, Integer>> executeTasks(
            final List<List<Triplet<String, Long, Long>>> tasks,
            final int nWord,
            final int nThread) {
        log.debug("execute tasks with: {}", tasks);
        return launchThreads(tasks, nWord, nThread)
                .flatMap(this::mergeThreadResults)
                .orElseThrow(NullResultInTaskExecutor::new);
    }

    /**
     * распределяет задачи по потокам и дожидается их выполнения.
     *
     * @param tasks   пул задач на все потоки
     * @param nWord   параметры отчета - количество слов
     * @param nThread количество потоков
     * @return коллекция выполненных потоков
     */
    Optional<List<ThreadWordsAnalizer>> launchThreads(
            final List<List<Triplet<String, Long, Long>>> tasks,
            final int nWord,
            final int nThread) {
        try {
            log.info(MSG_START_THREAD, nThread);
            if (tasks.size() != nThread) {
                log.info(ILLEGAL_LAUNCH_ARGS);
                return empty();
            }
            final CountDownLatch latch = new CountDownLatch(nThread);
            final List<ThreadWordsAnalizer> threads = new ArrayList<>();
            tasks.forEach(t -> threads.add(
                    new ThreadWordsAnalizerImpl(latch, t, nWord)));
            threads.forEach(t -> new Thread(t).start());
            log.info(MSG_WAIT_THREAD);
            latch.await();
            return of(threads);
        } catch (InterruptedException ie) {
            throw new TaskExecutorInterruptedException();
        }
    }

    /**
     * объединяет результаты по всем потокам в один отчет.
     * работа с коллециями фиксированной длины, оценка вычислительной
     * сложности от объема и количества файлов - O(1), т.е. за константное
     * время.
     *
     * @param threadResults коллекция отработавших потоков
     * @return суммарный отчет
     */
    Optional<List<Pair<String, Integer>>> mergeThreadResults(
            final List<ThreadWordsAnalizer> threadResults) {
        // TODO
        return Optional.empty();
    }
}
