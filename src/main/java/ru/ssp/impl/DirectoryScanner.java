package ru.ssp.impl;

import java.util.List;

/**
 * собирает информацию о файлах.
 */
interface DirectoryScanner {

    /**
     * выполняет сканирование каталога и формирование списка файлов.
     *
     * @param dirName каталог для сканирования
     * @return список файлов
     */
    List<String> scanDir(String dirName);
}
