package ru.ssp.impl;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * формирует поток строк из всех файлов в очередь для обработки
 * с возможностью выполнения в отдельном потоке выполнения.
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
        // TODO Auto-generated method stub
    }

}
