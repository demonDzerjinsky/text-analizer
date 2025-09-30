package ru.ssp.executors;

import static ru.ssp.executors.TextFilesReader.EOS;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

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
    private static final String MSG_THREAD_TERMINATED = "thread terminated";

    /**
     * сообщение при получении признака завершения передачи данных.
     */
    private static final String MSG_END_OF_STREAM = "thread received <EOS>";

    /**
     * сообщение при получении прерывания потока.
     */
    private static final String MSG_THREAD_INTERRUPTED = "thread interrupted";

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
            log.debug(MSG_END_OF_STREAM);
        } finally {
            latch.countDown();
            log.debug(MSG_THREAD_TERMINATED);
        }
    }

    @Override
    void countWords() throws EosReceivedException {
        final Pattern dp = Pattern.compile("\\s+");
        try {
            while (true) {
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                final String nextLine = queue.take();
                if (nextLine.equals(EOS)) {
                    throw new EosReceivedException();
                }
                for (String word : dp.split(nextLine)) {
                    this.getWordCountMap().compute(word,
                            (k, v) -> (v == null) ? new MutCounter() : v).inc();
                }
            }
        } catch (InterruptedException ie) {
            log.debug(MSG_THREAD_INTERRUPTED);
            Thread.currentThread().interrupt();
        }
    }
}
