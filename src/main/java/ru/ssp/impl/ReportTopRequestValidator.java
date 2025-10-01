package ru.ssp.impl;

import static java.lang.String.format;

import java.io.File;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import ru.ssp.dto.ReportTopRequestDto;
import ru.ssp.dto.ReportTopResultDto;

/**
 * Валидатор контракта {@code ReportTopRequestDto}.
 */
@Slf4j
class ReportTopRequestValidator
        implements Validator<ReportTopRequestDto, ReportTopResultDto> {

    /**
     * ограничение по количеству слов.
     * todo значение взято для примера, в последствии вынести в конфиг.
     */
    private static final int WLIMIT = 10;

    /**
     * сообщение в лог по ошибке валидации.
     */
    private static final String VALID_ERR = "request validation err: %s";

    /**
     * проверка параметров входного контракта.
     *
     * @param param DTO входного контракта
     * @return {@code Optional} empty - если ошибок во входном контракте не
     *         найдено и контракт ответа с ошибкой если валидация не прошла.
     */
    @Override
    public Optional<ReportTopResultDto> validate(
            final ReportTopRequestDto param) {
        return Optional.of(param)
                .filter(this::checkIfErr)
                .map(p -> new ReportTopResultDto(null, format(VALID_ERR, p)));
    }

    /**
     * валидирует параметры вызова.
     * логирует сообщение с объектом параметра в сл если не прошла валидация.
     *
     * @param param объект параметров вызова
     * @return true - если валидация не прошла.
     */
    private boolean checkIfErr(final ReportTopRequestDto param) {
        if (param == null
                || param.srcDir() == null
                || param.srcDir().isBlank()
                || param.nWords() <= 1
                || param.nWords() > WLIMIT
                || checkDirIsNotExists(param.srcDir())) {
            // todo в последствии развить - вернуть детали ошибки
            return true;
        }
        return false;
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
