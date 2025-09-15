package ru.ssp.impl;

import java.util.List;

import org.javatuples.Pair;

/**
 * собирает информацию о файлах для планировщика задач.
 */
interface DirectoryScanner {
    /**
     * выполняет сбор метаданных по файлам {@code file-size}.
     *
     * @param dirName каталог для сканирования
     * @return метаданные для планировщика задач
     */
    List<Pair<String, Integer>> scanDir(String dirName);
}
