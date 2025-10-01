package ru.ssp.impl;

import java.io.File;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import ru.ssp.dto.ReportTopRequestDto;

/**
 * Валидатор контракта {@code ReportTopRequestDto}.
 */
@Slf4j
class ReportTopRequestValidator implements Validator<ReportTopRequestDto> {

    /**
     * ограничение по количеству слов.
     * todo значение взято для примера, в последствии вынести в конфиг.
     */
    private static final int WLIMIT = 10;

    /**
     * сообщение в лог по ошибке валидации.
     */
    private static final String VALIDATION_ERR = "request validation err: {}";

    /**
     * проверка параметров входного контракта.
     *
     * @param param DTO входного контракта
     * @return {@code Optional} с объектом входного контракта в случае успешной
     *         валидации
     */
    @Override
    public Optional<ReportTopRequestDto> validate(
            final ReportTopRequestDto param) {
        return Optional.of(param).filter(this::checkNot);
    }

    /**
     * валидирует параметры вызова.
     * логирует сообщение с объектом параметра в сл если не прошла валидация.
     *
     * @param o объект параметров вызова
     * @return true - если нет ошибок, false - ошибки
     */
    private boolean checkNot(final ReportTopRequestDto o) {
        if (o == null
                || o.srcDir() == null
                || o.srcDir().isBlank()
                || o.nWords() <= 1
                || o.nWords() > WLIMIT
                || checkDirIsNotExists(o.srcDir())) {
            log.info(VALIDATION_ERR, o);
            return false;
        }
        return true;
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
