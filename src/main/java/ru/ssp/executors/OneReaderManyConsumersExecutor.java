package ru.ssp.executors;

import java.util.List;
import java.util.Optional;

/**
 * сбор статистики на пуле потоков обработки.
 */
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
    public Optional<List<BaseWordsCounter>> submitAndWait(List<String> fileNames, int threads) {
        return Optional.empty();
    }

}
