package ru.ssp.impl;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

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
                // возвращаем управение эмиттеру с его обработкой прерывания
                Thread.currentThread().interrupt();
            }
        });

    }

}
