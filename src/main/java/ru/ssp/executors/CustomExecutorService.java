package ru.ssp.executors;

import java.util.List;
import java.util.Optional;

/**
 * интерфейс под разные реализации executor-ов выполняющих
 * специализированную задачу - сбор статистики использования слов
 * с коллекции файлов.
 * коллекция может быть большой, соответственно, вся задача распараллеливается
 * на пуле потоков.
 */
public interface CustomExecutorService {

    /**
     * собрать статистику по количеству слов с файлов {@code fileNames}
     * на пуле потоков.
     * дождаться результата выполнения всех потоков и вернуть коллекцию
     * результатов по каждому потоку.
     *
     * @param fileNames коллекция файлов
     * @return коллекция результатов по каждому потоку
     */
    Optional<List<BaseWordsCounter>> submitAndWait(List<String> fileNames);
}
