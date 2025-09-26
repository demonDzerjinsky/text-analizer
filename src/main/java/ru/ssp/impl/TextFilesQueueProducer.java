package ru.ssp.impl;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * направляет поток строк из коллекции файлов в очередь.
 */
class TextFilesQueueProducer extends TextFilesReader
        implements QueueProducer {

    TextFilesQueueProducer(final List<String> fileNames) {
        super(fileNames);
    }

    @Override
    public final void publish(final LinkedBlockingQueue<String> queue) {

    }

}
