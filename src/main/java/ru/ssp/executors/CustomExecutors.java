package ru.ssp.executors;

/**
 * Утилитарный класс порождающих методов объектов классов
 * реализующих {@code CustomExecutorService}
 * Фабричные методы под кастомные реализации пулов выполнения задачи
 * сбора статистики по словам.
 */
public final class CustomExecutors {

    private CustomExecutors() {
    }

    /**
     * формирует пул с параметрами по умолчанию.
     *
     * @return сервис сбора статистики слов
     */
    public static CustomExecutorService newDefaultExecutor() {
        return new CustomThreadPoolExecutor.Builder().build();
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
        return new CustomThreadPoolExecutor.Builder()
                .setBuffers(buffers)
                .setThreads(threads)
                .build();
    }
}
