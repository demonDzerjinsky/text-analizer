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
     * todo в последствии вынести в конфиг, здесь оставить дефолтные.
     */
    private static final int WLIMIT = 10;

    /**
     * ограничение по количеству потоков.
     * todo в последствии вынести в конфиг, здесь оставить дефолтные.
     */
    private static final int PLIMIT = 5;

    /**
     * проверка параметров входного контракта.
     *
     * каталог задан и не пробелами
     * количество слов в отчете передано положительным и в пределах ограничений
     * количество потоков обработки передано положительным числом и больше 2
     * (в тек реализации исходим из того что 1 читатель и как минимум 1
     * обработчик, можно больше в пределах {@code PLIMIT - 1})
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
        return (o == null
                || o.srcDir() == null
                || o.srcDir().isBlank()
                || o.nWords() <= 1
                || o.nWords() > WLIMIT
                || o.nThreads() <= 0
                || o.nThreads() > PLIMIT
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
