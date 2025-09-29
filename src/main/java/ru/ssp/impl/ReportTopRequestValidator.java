package ru.ssp.impl;

import java.io.File;
import java.util.Optional;

import ru.ssp.dto.ReportTopRequestDto;

/**
 * Валидатор контракта {@code ReportTopRequestDto}.
 */
class ReportTopRequestValidator implements Validator<ReportTopRequestDto> {

    /**
     * ограничение по количеству слов.
     * todo значение взято для примера, в последствии вынести в конфиг.
     */
    private static final int WLIMIT = 10;

    /**
     * проверка параметров входного контракта.
     *
     * каталог задан и не пробелами и существует
     * количество слов в отчете передано положительным и в пределах ограничений
     *
     * @param param DTO входного контракта
     * @return {@code Optional} с объектом входного контракта в случае успешной
     *         валидации
     */
    @Override
    public Optional<ReportTopRequestDto> validate(final ReportTopRequestDto param) {
        return Optional.of(param).filter(this::check);
    }

    private boolean check(final ReportTopRequestDto o) {
        // перечисление ошибочных кейсов
        return (o == null
                || o.srcDir() == null
                || o.srcDir().isBlank()
                || o.nWords() <= 1
                || o.nWords() > WLIMIT
                || checkDirIsNotExists(o.srcDir()))
                        ? false
                        : true;
    }

    /**
     * проверяет если заданный каталог не существует.
     *
     * @param dirName наименование каталога
     * @return true если каталог не существует
     */
    private boolean checkDirIsNotExists(final String dirName) {
        File dir = new File(dirName);
        if (dir.exists() && dir.isDirectory()) {
            return false;
        }
        return true;
    }
}
