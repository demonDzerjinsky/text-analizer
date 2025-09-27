package ru.ssp.impl;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * делает тоже что родитель {@code TextFilesQueueProducer} но
 * с возможностью выполнения в отдельном потоке Thread-е.
 */
class RunnableTextFilesQueueProducer extends TextFilesQueueProducer
        implements Runnable {

    /**
     * очередь для публикации потока.
     */
    private final LinkedBlockingQueue<String> queue;

    RunnableTextFilesQueueProducer(final List<String> fileNames,
            final LinkedBlockingQueue<String> dstQueue) {
        super(fileNames);
        this.queue = dstQueue;
    }

    @Override
    public void run() {
        this.publish(this.queue);
    }

}
