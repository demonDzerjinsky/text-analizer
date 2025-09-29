package ru.ssp.dto;

/**
 * контракт вызова модуля поиска.
 *
 * @param srcDir каталог содержащий текстовые файлы
 * @param nWords количество слов в top
 */
public record ReportTopRequestDto(String srcDir, int nWords) {
}
