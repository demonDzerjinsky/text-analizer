package ru.ssp.impl;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import lombok.extern.slf4j.Slf4j;
import ru.ssp.exceptions.EosReceivedException;

/**
 * отчет, формируемый из источника-очереди.
 * консьюмер, читает очередь и формирует отчет с возможностью
 * выполнения в отдельном потоке.
 */
@Slf4j
class RunnableQueueReport extends BaseReport
        implements RunnableReport {

    /**
     * сообщение при завершении потока.
     */
    private static final String THREAD_TERMINATED = "thread terminated";

    /**
     * сообщение при получении признака завершения передачи данных.
     */
    private static final String END_OF_STREAM = "thread received <EOS>";

    /**
     * общая защелка выполнения всех консьюмеров.
     */
    private final CountDownLatch latch;

    /**
     * очередь - источник данных для формирования отчета.
     */
    private final LinkedBlockingQueue queue;

    RunnableQueueReport(final LinkedBlockingQueue sourceQueue,
            final CountDownLatch consumersLatch) {
        this.latch = consumersLatch;
        this.queue = sourceQueue;
    }

    @Override
    public void run() {
        try {
            fillReport();
        } catch (EosReceivedException ex) {
            log.debug(END_OF_STREAM);
        } finally {
            latch.countDown();
            log.debug(THREAD_TERMINATED);
        }
    }

    @Override
    void fillReport() throws EosReceivedException {
        // TODO Auto-generated method stub

    }
}
