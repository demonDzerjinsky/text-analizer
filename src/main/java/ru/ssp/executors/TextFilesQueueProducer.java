package ru.ssp.executors;

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

    /**
     * публикует сообщения из потока в очередь.
     * если в процессе ожидания буфера очереди возникло прерывание,
     * возвращает управление эмиттеру потока сообщений, который закрывает
     * ресурсы потока чтения, перестает публиковать сообщения и завершает
     * работу. пре этом, все консьюмеры смогут дочитать из очереди и
     * корректно обработать вычитанные к моменту останова строки.
     *
     * @param queue очередь куда публикуются сообщения
     */
    @Override
    public final void publish(final LinkedBlockingQueue<String> queue) {
        this.getAsStream().subscribe(txt -> {
            try {
                queue.put(txt);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        });
    }

}
