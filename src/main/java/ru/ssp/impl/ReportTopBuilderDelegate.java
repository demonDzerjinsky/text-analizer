package ru.ssp.impl;

import java.util.List;
import java.util.Optional;

import org.javatuples.Pair;

import ru.ssp.executors.CustomExecutorService;
import ru.ssp.executors.CustomExecutors;

/**
 * создает нужную реализацию {@code ReportTopBuilder} и делегирует ей
 * выполнение.
 */
class ReportTopBuilderDelegate implements ReportTopBuilder,
        ReportTopBuilderAware {

    /**
     * реализация интерфейса поиска через создание и вызов.
     *
     * @param dir      каталог
     * @param nWords   параметр количества слов
     * @param nThreads параметр количества потоков
     */
    @Override
    public Optional<List<Pair<String, Integer>>> buildReport(
            final String dir,
            final int nWords,
            final int nThreads) {
        return create().buildReport(dir, nWords, nThreads);
    }

    /**
     * фабричный метод.
     */
    @Override
    public ReportTopBuilder create() {
        final DirectoryScanner scanner = new DirectoryScannerImpl();
        final CustomExecutorService executor = CustomExecutors.newOneReaderManyConsumesExecutor();
        return new ReportTopBuilderImpl(scanner, executor);
    }
}
