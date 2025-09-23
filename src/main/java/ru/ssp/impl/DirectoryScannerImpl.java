package ru.ssp.impl;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * реализует интерфейс поиска файлов.
 */
@Slf4j
class DirectoryScannerImpl implements DirectoryScanner {

    /**
     * сканирует каталог и формирует список файлов.
     *
     * @param dirName каталог
     * @return коллекция файлов
     */
    @Override
    public List<String> scanDir(final String dirName) {
        final Path searchRoot = Paths.get(dirName);
        try (var fstream = Files.walk(searchRoot)) {
            return fstream.filter(f -> Files.isRegularFile(f))
                    .map(f -> f.getFileName().toString())
                    .collect(toList());
        } catch (IOException ex) {
            log.debug(ex.getMessage(), ex);
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
    }
}
