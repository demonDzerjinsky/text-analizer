package ru.ssp.executors;

import static java.util.List.of;
import static ru.ssp.executors.TextFilesReader.EOS;

import java.util.List;
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

    /**
     * выполняет сбор статистики по словам на пуле потоков.
     * ожидает завершения выполнения всех потоков после чего возвращает
     * все результаты от каждого обработчика в единой коллекции для
     * последующего мержа в общую статистику и выделения из нее TOP слов.
     *
     * @param fileNames коллекция имен файлов
     * @return коллекция результатов подсчета статистике на каждом потоке
     */
    @Override
    public List<BaseWordsCounter> submitAndWait(
            final List<String> fileNames) {
        final var queue = new LinkedBlockingQueue<String>(buffers);
        final var latch = new CountDownLatch(threads);
        final var producer = new RunnableTextFilesQueueProducer(fileNames, queue);
        final var consumers = new RunnableQueueWordsCounter[threads];
        final Thread tProducer = new Thread(producer);
        final Thread[] tConsumers = new Thread[threads];
        // инициализируем и запускаем пул консьюмеров
        // консьюмеры встают в ожидании потока строк в queue
        for (int i = 0; i < threads; i++) {
            consumers[i] = new RunnableQueueWordsCounter(queue, latch);
            tConsumers[i] = new Thread(consumers[i]);
            tConsumers[i].start();
        }
        // запускаем продьюсера - начинает писать текст в queue
        tProducer.start();
        try {
            tProducer.join();
        } catch (InterruptedException ie) {
            // если мы были прерваны кидаем сигнал останова продьюсеру
            // чтобы он мог завершить IO и остановиться на том что успел вычитать
            tProducer.interrupt();
            Thread.currentThread().interrupt();
        }
        // если продьюсер не просто завершился а был кемто или нами прерван
        // переводим основной поток в состояние - прерван
        if (tProducer.isInterrupted()) {
            Thread.currentThread().interrupt();
        }
        // после завершения чтения данных в очередь
        // под каждого консьюмера отправляем end-of-stream в ту же очередь
        // для их завершения
        for (int i = 0; i < threads; i++) {
            while (true) {
                try {
                    queue.put(EOS);
                    break;
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        // если текущий поток в состоянии прерван,
        // результат сбора статистики нас не интересует
        if (Thread.interrupted()) {
            for (int i = 0; i > threads; i++) {
                tConsumers[i].interrupt();
            }
            throw new CustomExecutorInterruptedException();
        }
        // даем возможность дочитать все что накоплено в очереди
        // ждем пока все консьюмеры вычитают оставшиеся строки
        // и последний end-of-stream и завершатся
        try {
            latch.wait();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new CustomExecutorInterruptedException();
        }
        // если ктото из консьюмеров завершился по прерыванию
        // результат статистики не достоверен, выходим по исключению
        for (int i = 0; i < threads; i++) {
            if (tConsumers[i].isInterrupted()) {
                throw new CustomExecutorInterruptedException();
            }
        }
        return of(consumers);
    }
}
