package ru.ssp.impl;

import java.util.List;
import java.util.Optional;

import org.javatuples.Pair;

/**
 * создает {@code WordsFinderEngineWorker} и делегирует ему выполнение.
 */
class WordsFinderEngine implements FindWords,
        WordsFinderEngineWorkerBuildAware {

    /**
     * реализация интерфейса поиска через создание и вызов.
     *
     * @param dir      каталог
     * @param nWords   параметр количества слов
     * @param nThreads параметр количества потоков
     */
    @Override
    public Optional<List<Pair<String, Integer>>> find(
            final String dir,
            final int nWords,
            final int nThreads) {
        return createWorker().find(dir, nWords, nThreads);
    }

    /**
     * фабричный метод.
     */
    @Override
    public WordsFinderEngineWorker createWorker() {
        final DirectoryScanner dscanner = new DirectoryScannerImpl();
        final TaskExecutor executor = new TaskExecutorImpl();
        return new WordsFinderEngineWorker(dscanner, executor);
    }
}
