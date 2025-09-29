package ru.ssp.impl;

import static java.util.Optional.of;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import org.javatuples.Pair;

import lombok.extern.slf4j.Slf4j;
import ru.ssp.executors.BaseWordsCounter;

/**
 * выполняет формирование отчета.
 */
@Slf4j
class ReportImpl implements Report {



    /**
     * выполняет обработку файлов.
     *
     * @param fls коллекция файлов
     * @param ths количество потоков
     * @return коллекция построенных потоками отчетов
     * Executors
     */
    Optional<List<BaseWordsCounter>> launch(final List<String> fls, final int ths) {
        final CustomExecutorService svc = CustomExecutors.newOneReaderManyConsumersExecutor();
        Optional<List<BaseWordsCounter>> results = svc.submitAndWait(fls, ths);
        // final var queue = new LinkedBlockingQueue<String>(INT_BUFFERS);
        // final int nConsumers = ths - 1;
        // final var latch = new CountDownLatch(nConsumers);
        // final var producer = new RunnableTextFilesQueueProducer(fls, queue);
        // final Thread producerThread = new Thread(producer);
        // final var consumers = new RunnableQueueReport[nConsumers];
        // final Thread[] consumersThreads = new Thread[nConsumers];
        // for (int i = 0; i < nConsumers; i++) {
        // consumers[i] = new RunnableQueueReport(queue, latch);
        // consumersThreads[i] = new Thread(consumers[i]);
        // }
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

}
