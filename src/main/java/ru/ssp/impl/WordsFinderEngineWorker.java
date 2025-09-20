package ru.ssp.impl;

import static java.util.Optional.of;

import java.util.List;
import java.util.Optional;

import org.javatuples.Pair;

import ru.ssp.exceptions.WordsFinderExecutionException;

/**
 * выполняет поиск слов в каталоге по условиям задачи.
 *
 * фасад. верхнеуровневая последовательность шагов.
 */
class WordsFinderEngineWorker implements FindWords {

    /**
     * поставщик данных перечня файлов.
     */
    private final DirectoryScanner dscanner;
    /**
     * формирует задачи разрезая весь объем входных файлов по подзадачам
     * на поток.
     */
    private final TaskPlanner planner;
    /**
     * выполняет пул задач в параллельном режиме.
     */
    private final TaskExecutor executor;

    WordsFinderEngineWorker(
            final DirectoryScanner pdscanner,
            final TaskPlanner pplanner,
            final TaskExecutor pexecutor) {
        this.dscanner = pdscanner;
        this.planner = pplanner;
        this.executor = pexecutor;
    }

    @Override
    public Optional<List<Pair<String, Integer>>> find(
            final String dir,
            final int nWords,
            final int nThreads) {
        try {
            return of(dscanner.scanDir(dir))
                    .map(fls -> planner.makeTasks(fls, nThreads))
                    .map(tsks -> executor.executeTasks(tsks, nWords, nThreads));
        } catch (Exception e) {
            throw new WordsFinderExecutionException(
                    e.getMessage(),
                    e.getCause());
        }
    }

}
