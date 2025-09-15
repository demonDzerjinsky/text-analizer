package ru.ssp.impl;

import java.util.List;
import java.util.Optional;

import org.javatuples.Pair;

/**
 * отвечает за поставку нужных компонент в {@code WordsFinderEngineWorker}.
 * создает {@code WordsFinderEngineWorker} и делегирует ему выполнение.
 */
class WordsFinderEngine
        implements FindWords, WordsFinderEngineWorkerBuildAware {

    /**
     * реализация интерфейса поиска через создание и вызов
     * функционального класса.
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
        WordsFinderEngineWorker worker = this.create();
        return worker.find(dir, nWords, nThreads);
    }

    /**
     * фабричный метод.
     *
     */
    @Override
    public WordsFinderEngineWorker create() {
        final DirectoryScanner dscanner = new DirectoryScannerImpl();
        final TaskPlanner planner = new TaskPlannerImpl();
        final TaskExecutor executor = new TaskExecutorImpl();
        final WordsFinderEngineWorker worker = new WordsFinderEngineWorker(
                dscanner, planner, executor);
        return worker;
    }

}
