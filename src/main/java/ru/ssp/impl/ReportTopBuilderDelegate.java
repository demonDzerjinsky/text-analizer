package ru.ssp.impl;

import static ru.ssp.executors.CustomExecutors.newDefaultExecutor;

import java.util.List;
import java.util.Optional;

import org.javatuples.Pair;

import ru.ssp.executors.CustomExecutorService;

/**
 * создает нужную реализацию {@code ReportTopBuilder} и делегирует ей
 * выполнение.
 */
class ReportTopBuilderDelegate implements ReportTopBuilder,
        ReportTopBuilderAware {

    /**
     * реализация интерфейса поиска через создание и вызов.
     *
     * @param dir    каталог
     * @param nWords параметр количества слов
     * @param minLen минимальная длина
     */
    @Override
    public Optional<List<Pair<String, Integer>>> buildReport(
            final String dir, final int nWords, final int minLen) {
        return create().buildReport(dir, nWords, minLen);
    }

    /**
     * фабричный метод.
     * использует пул потоков с настройками по умолчанию
     * {@code CustomExecutors.newDefaultExecutor}.
     * в последствии можно предусмотреть формирование с настройками из
     * конфигурационного файла и использовать
     * {@code CustomExecutors.newConfigurableExecutor} с параметрами
     * на основе тестов производительности и конфигурации конкретной
     * платформы.
     */
    @Override
    public ReportTopBuilder create() {
        final DirectoryScanner scanner = new DirectoryScannerImpl();
        final CustomExecutorService executor = newDefaultExecutor();
        return new ReportTopBuilderImpl(scanner, executor);
    }
}
