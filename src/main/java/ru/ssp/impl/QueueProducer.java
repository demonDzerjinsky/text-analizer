package ru.ssp.impl;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * интерфейс публикации потока строк в очередь.
 */
public interface QueueProducer {

    /**
     * метод публикации потока строк в очередь.
     *
     * @param queue очередь
     */
    void publish(LinkedBlockingQueue<String> queue);
}
