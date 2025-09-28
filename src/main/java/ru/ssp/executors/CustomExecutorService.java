package ru.ssp.executors;

import java.util.List;
import java.util.Optional;

/**
 * интерфейс под разные реализации executor-ов выполняющих
 * специализированную задачу - сбор статистики с коллекции файлов.
 */
public interface CustomExecutorService {

    /**
     * проанализировать файлы {@code fileNames} в {@code threads} потоках,
     * дождаться результата и вернуть коллекцию результатов по каждому потоку.
     *
     * @param fileNames коллекция файлов
     * @param threads количество потоков
     * @return коллекция результатов по каждому потоку
     */
    Optional<List<BaseWordsCounter>> submitAndWait(List<String> fileNames,
            int threads);
}
