package ru.ssp.impl;

import static java.util.Optional.of;

import java.util.List;
import java.util.Optional;

import org.javatuples.Pair;

/**
 * выполняет поиск слов в каталоге по условиям задачи.
 * фасад.
 */
class WordsFinderEngineWorker implements FindWords {

    /**
     * поставщик перечня файлов.
     */
    private final DirectoryScanner scnr;

    /**
     * исполнитель.
     */
    private final TaskExecutor exctr;

    WordsFinderEngineWorker(final DirectoryScanner scanner,
            final TaskExecutor executor) {
        this.scnr = scanner;
        this.exctr = executor;
    }

    @Override
    public Optional<List<Pair<String, Integer>>> find(
            final String dir,
            final int nWords,
            final int nThreads) {
        return of(scnr.scanDir(dir))
                .flatMap(fls -> exctr.execute(fls, nWords, nThreads));
    }
}
