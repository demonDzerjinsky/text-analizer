package ru.ssp.impl;

import java.util.List;
import java.util.stream.Stream;

/**
 * отвечает за формирование единого потока строк.
 * источником является коллекция файлов.
 */
class TextFilesReader extends BaseReader {

    /**
     * коллекция имен файлов для чтения в поток.
     */
    private final List<String> fileNames;

    /**
     * конструктор.
     * @param dirFileNames коллекция файлов
     */
    TextFilesReader(final List<String> dirFileNames) {
        this.fileNames = dirFileNames;
    }

    @Override
    final Stream<String> read() {
        // TODO Auto-generated method stub
        return null;
    }

}
