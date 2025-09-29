package ru.ssp.executors;

/**
 * Фабричные методы под кастомные реализации пулов выполнения задачи
 * сбора статистики по словам.
 */
public class CustomExecutors {

    /**
     * формирует пул с параметрами по умолчанию.
     * @return сервис сбора статистики слов
     */
    public static CustomExecutorService newDefaultExecutor() {
        return new OneReaderManyConsumersExecutor.Builder().build();
    }

    /**
     * формирует пул с требуемыми параметрами.
     *
     * @param buffers размер очереди потока чтения строк
     * @param threads количество потоков-обработчиков строк
     * @return сервис сбора статистики слов
     */
    public static CustomExecutorService newConfigurableExecutor(
            final int buffers,
            final int threads) {
        return new OneReaderManyConsumersExecutor.Builder()
                .setBuffers(buffers)
                .setThreads(threads)
                .build();
    }
}
