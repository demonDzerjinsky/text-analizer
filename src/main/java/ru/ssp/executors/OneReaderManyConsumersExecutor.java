package ru.ssp.executors;

import static java.util.Optional.of;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import lombok.extern.slf4j.Slf4j;
import ru.ssp.exceptions.CustomExecutorInterruptedException;

/**
 * сбор статистики на пуле потоков обработки.
 */
@Slf4j
public class OneReaderManyConsumersExecutor implements CustomExecutorService {

    /**
     * сообщение в лог.
     */
    private static final String MSG_START = "Start {} threads";
    /**
     * сообщение в лог.
     */
    private static final String MSG_WAIT = "Waiting";

    /**
     * сообщение в лог.
     */
    private static final String MAIN_THREAD_INTERRUPTED =

            "main thread interrupted, stop all child threads";

    /**
     * размер буфера обмена по умолчанию.
     */
    static final int DEFAULT_BUFFERS = 1000;

    /**
     * количество обработчиков строк по умолчанию.
     */
    static final int DEFAULT_THREADS = 3;

    /**
     * размер буфера обмена.
     */
    private final int buffers;

    /**
     * количество обработчиков строк.
     */
    private final int threads;

    private OneReaderManyConsumersExecutor(final Builder builder) {
        this.buffers = builder.buffers;
        this.threads = builder.threads;
    }

    public static class Builder {
        private int buffers = DEFAULT_BUFFERS;
        private int threads = DEFAULT_THREADS;

        public Builder setBuffers(final int b) {
            this.buffers = b;
            return this;
        }

        public Builder setThreads(final int t) {
            this.threads = t;
            return this;
        }

        public OneReaderManyConsumersExecutor build() {
            return new OneReaderManyConsumersExecutor(this);
        }

    }

    @Override
    public Optional<List<BaseWordsCounter>> submitAndWait(
            final List<String> fileNames) {
        final var queue = new LinkedBlockingQueue<String>(buffers);
        final var counsumersLatch = new CountDownLatch(threads);
        final var producer = new RunnableTextFilesQueueProducer(fileNames, queue);
        final var consumers = new RunnableQueueWordsCounter[threads];
        final Thread tProducer = new Thread(producer);
        final Thread[] tConsumers = new Thread[threads];
        // инициализируем и запускаем пул обработчиков
        // консьюмеры встают в ожидании потока строк в очереди
        for (int i = 0; i < threads; i++) {
            consumers[i] = new RunnableQueueWordsCounter(queue, counsumersLatch);
            tConsumers[i] = new Thread(consumers[i]);
            tConsumers[i].start();
        }
        // запускаем продьюсера - начинает писать текст в queue
        tProducer.start();
        try {
            tProducer.join();
        } catch (InterruptedException ie) {
            tProducer.interrupt(); // если нас прервали останавливаем продьюсер
            Thread.currentThread().interrupt();
        }
        // под каждого консьюмера отправляем end-of-stream для их завершения
        for (int i = 0; i < threads; i++) {
            while (true) {
                try {
                    queue.put(TextFilesReader.EOS);
                    break;
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        if (Thread.interrupted()) {
            throw new CustomExecutorInterruptedException();
        }
        // ждем пока вычитают оставшиеся строки и end-of-stream и завершатся
        try {
            counsumersLatch.wait();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new CustomExecutorInterruptedException();
        }
        return of(List.of(consumers));
    }
}
