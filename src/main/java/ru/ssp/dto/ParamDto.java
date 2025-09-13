package ru.ssp.dto;

/**
 * контракт вызова модуля поиска.
 *
 * @param srcDir каталог содержащий текстовые файлы
 * @param nWords количество слов в результирующей top
 * @param nThreads максимальное количество потоков обработки
 */
public record ParamDto(String srcDir, int nWords, int nThreads) {
}
