package ru.ssp.impl;

import ru.ssp.dto.ParamDto;
import ru.ssp.dto.ResultDto;

/**
 * основной класс, логика поиска, точка входа.
 *
 *
 */
public final class WordsFinder {
    private WordsFinder() {
    }

    /**
     * фабричный метод получение экземпляра с доступом только из пакета.
     *
     * @return экземпляр класса
     */
    static WordsFinder getInstance() {
        return new WordsFinder();
    }

    /**
     * реализует итерфейс поиска.
     *
     * только один вызов обрабатывается, остальные вызовы в момент обработки
     * получат исключение.
     * Исходим из того что вызов занимает все ядра и параллельные вызовы
     * могут привести к общей деградации
     *
     * @param findParam пераметры вызова
     * @return результаты вызова
     * @exception WordsFinderConrurrentException в случае если вызов сделан
     * в время работы предыдущего вызова
     */
    public static ResultDto find(final ParamDto findParam) {
        // TODO lock and check locking
        final WordsFinder finder = WordsFinder.getInstance();
        return finder.execute(findParam);
    }

    /**
     * выполняет поиск.
     *
     * @param findParam входные параметры для выполнения поиска
     * @return результат выполнения поиска
     */
    private ResultDto execute(final ParamDto findParam) {
        throw new RuntimeException();
    }
}
