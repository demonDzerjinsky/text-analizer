package ru.ssp.executors;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import ru.ssp.impl.TextFilesReader;

/**
 * читает все строки из текстовых файлов и публикует в очередь.
 */
class TextFilesQueueProducer extends TextFilesReader
        implements QueueProducer {

    TextFilesQueueProducer(final List<String> fileNames) {
        super(fileNames);
    }

    @Override
    public final void publish(final LinkedBlockingQueue<String> queue) {
        this.getAsStream().subscribe(txt -> {
            try {
                queue.put(txt);
            } catch (InterruptedException ie) {
                // возвращаем управение эмиттеру с его логикой
                // обработки завершения и закрытия ресурсов
                Thread.currentThread().interrupt();
            }
        });

    }

}
