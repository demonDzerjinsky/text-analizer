package ru.ssp.executors;

/**
 * Фабричные методы под кастомные реализации пулов выполнения задачи
 * сбора статистики по словам.
 */
public class CustomExecutors {

    public static CustomExecutorService newOneReaderManyConsumesExecutor() {
        return new OneReaderManyConsumersExecutor();
    }
}
