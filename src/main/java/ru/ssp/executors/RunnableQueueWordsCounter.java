package ru.ssp.executors;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import lombok.extern.slf4j.Slf4j;
import ru.ssp.exceptions.EosReceivedException;

/**
 * счетчик статистики слов из потока строк в очереди.
 * консьюмер, читает очередь и ведет подсчет количества слов.
 * поддерживает возможность выполнения в отдельном потоке.
 */
@Slf4j
class RunnableQueueWordsCounter extends BaseWordsCounter implements Runnable {

    /**
     * сообщение при завершении потока.
     */
    private static final String THREAD_TERMINATED = "thread terminated";

    /**
     * сообщение при получении признака завершения передачи данных.
     */
    private static final String END_OF_STREAM = "thread received <EOS>";

    /**
     * блокировка для синхронизации завершения.
     */
    private final CountDownLatch latch;

    /**
     * очередь - источник данных для подсчета статистики.
     */
    private final LinkedBlockingQueue<String> queue;

    RunnableQueueWordsCounter(final LinkedBlockingQueue<String> sourceQueue,
            final CountDownLatch consumersLatch) {
        this.latch = consumersLatch;
        this.queue = sourceQueue;
    }

    @Override
    public void run() {
        try {
            this.countWords();
        } catch (EosReceivedException ex) {
            log.debug(END_OF_STREAM);
        } finally {
            latch.countDown();
            log.debug(THREAD_TERMINATED);
        }
    }

    @Override
    void countWords() throws EosReceivedException {
        // TODO
    }
}
