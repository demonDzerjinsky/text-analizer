package ru.ssp.impl;

import lombok.Getter;

/**
 * мутабельный счетчик для использования в одном потоке
 * при подсчете статистики слов.
 * для снижения нагрузки на процессор и хип.
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
