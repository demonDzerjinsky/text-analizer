package ru.ssp.executors;

import lombok.Getter;

/**
 * мутабельный счетчик для использования в одном потоке
 * при подсчете статистики слов.
 * для снижения нагрузки на процессор и хип при подсчете
 * статистики на больших коллекциях слов.
 */
final class MutableCounter {

    /**
     * состояние счетчика.
     */
    @Getter
    private int count;

    /**
     * инкремент счетчика.
     */
    public void inc() {
        this.count++;
    }
}
