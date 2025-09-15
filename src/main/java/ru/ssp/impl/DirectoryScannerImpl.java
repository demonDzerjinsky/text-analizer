package ru.ssp.impl;

import java.util.List;

import org.javatuples.Pair;

/**
 * реализует шнтерфейс поиска файлов.
 */
class DirectoryScannerImpl implements DirectoryScanner {

    /**
     * сканирует каталог и формирует метаданные для планировщика.
     *
     * @param dirName каталог
     * @return коллекция файл-объем
     */
    @Override
    public List<Pair<String, Integer>> scanDir(
            final String dirName) {
        throw new UnsupportedOperationException();
    }

}
