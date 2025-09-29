package ru.ssp.executors;

import lombok.Getter;

/**
 * мутабельный счетчик для использования
 * при подсчете статистики слов.
 * для снижения нагрузки на процессор - не создаем каждый раз
 * при инкременте новый объект в хипе.
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
