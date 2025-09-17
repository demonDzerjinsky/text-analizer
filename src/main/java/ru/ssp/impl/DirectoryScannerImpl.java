package ru.ssp.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;

/**
 * реализует интерфейс поиска файлов.
 */
class DirectoryScannerImpl implements DirectoryScanner {

    /**
     * сканирует каталог и формирует метаданные для планировщика.
     *
     * @param dirName каталог
     * @return коллекция файл-объем в байтах
     */
    @Override
    public List<Pair<String, Long>> scanDir(
            final String dirName) {
        final Path searchRoot = Paths.get(dirName);
        try (var fstream = Files.walk(searchRoot)) {
            final List<Pair<String, Long>> retList = new ArrayList<>();
            // return fstream
            // .filter(f -> Files.isRegularFile(f))
            // .map(f -> new Pair(f.getFileName(), Files.size(f)))
            // .collect(Collectors.toList());
            var itr = fstream.iterator();
            while (itr.hasNext()) {
                final Path entry = itr.next();
                if (Files.isRegularFile(entry)) {
                    retList.add(
                            new Pair<String, Long>(
                                    entry.toString(),
                                    Files.size(entry)));
                }
            }
            return retList;
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
    }

}
