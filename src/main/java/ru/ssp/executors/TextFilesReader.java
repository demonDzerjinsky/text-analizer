package ru.ssp.executors;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import ru.ssp.exceptions.ProducerTerminatedException;

/**
 * отвечает за формирование единого потока строк.
 * источником является коллекция файлов.
 */
@Slf4j
class TextFilesReader extends BaseReader {

    /**
     * коллекция имен файлов для чтения в поток.
     */
    private final List<String> fileNames;

    /**
     * поток строковых данных.
     */
    private final Flux<String> fluxString;

    /**
     * признак окончания потока.
     */
    public static final String EOS = "<EOS>";

    /**
     * сообщение ошибки чтения.
     */
    private static final String IO_ERROR = "io error {}";

    /**
     * сообщение ошибки при открытии потока чтения файла.
     */
    private static final String OPEN_FILE_STREAM_ERROR

            = "stream open error: {}";

    /**
     * сообщение в лог.
     */
    private static final String READER_TERMINATED

            = "files reader thread is terminated";

    /**
     * конструктор.
     *
     * @param dirFileNames коллекция файлов
     */
    TextFilesReader(final List<String> dirFileNames) {
        this.fileNames = dirFileNames;
        this.fluxString = Flux.create(this::readFiles);
    }

    @Override
    final Flux<String> getAsStream() {
        return fluxString;
    }

    /**
     * Формирует поток {@code Flux } строк.
     * При получении сигнала терминации потока дочитывает и
     * публикует последнюю строку в поток, закрывает ресурсы IO
     * источника данных и выходит с пробросом исключения.
     *
     * @param sink {@code FluxSink}
     */
    private void readFiles(final FluxSink<String> sink) {
        try (var lines = this.getLinesStream()) {
            lines.forEach(line -> {
                if (Thread.interrupted()) {
                    Thread.currentThread().interrupt();
                    throw new ProducerTerminatedException();
                }
                sink.next(line);
            });
        } finally {
            log.info(READER_TERMINATED);
        }
    }

    private Stream<String> getLinesStream() {
        var lines = fileNames
                .stream()
                .flatMap(fl -> {
                    final FileReader fr;
                    try {
                        fr = new FileReader(fl);
                        final BufferedReader br = new BufferedReader(fr);
                        return br.lines().onClose(() -> {
                            try {
                                br.close();
                                log.info("io stream closed"); //todo remove
                            } catch (IOException e) {
                                log.error(IO_ERROR, e);
                                throw new ProducerTerminatedException(e);
                            }
                        });
                    } catch (FileNotFoundException fnfe) {
                        throw new ProducerTerminatedException(fnfe);
                    }
                });
        return lines;
    }
}
