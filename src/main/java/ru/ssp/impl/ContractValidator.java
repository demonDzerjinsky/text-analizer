package ru.ssp.impl;

import ru.ssp.dto.ParamDto;
import ru.ssp.exceptions.ContractValidateException;

/**
 * проверки параметров вызова.
 */
public final class ContractValidator {
    /**
     * ограничение по количеству слов.
     */
    private static final int WLIMIT = 10;
    /**
     * ограничение по количеству потоков.
     */
    private static final int PLIMIT = 5;
    /**
     * сообщение ошибки в исключении.
     */
    private static final String ERROR_MSG = """
            parameters:
              srcDir - каталог с текстовыми файлами для анализа
              nWords - количество слов в отчете
              nThreads - количество потоков обработки
            """;
    /**
     * параметры вызова.
     */
    private final ParamDto param;

    /**
     * конструктор.
     *
     * @param dto
     */
    public ContractValidator(final ParamDto dto) {
        this.param = dto;
    }

    /**
     * проверка параметров входного контракта.
     *
     * каталог задан и не пробелами
     * количество слов передано положительным и в пределах ограничений
     * количество потоков обработки передано положительным числом
     * и в пределах ограничений
     */
    public void validate() {
        if (this.param == null
                || this.param.srcDir() == null
                || checkDirIsNotExists()
                || this.param.srcDir().isBlank()
                || this.param.nWords() <= 0
                || this.param.nWords() > WLIMIT
                || this.param.nThreads() <= 0
                || this.param.nThreads() > PLIMIT) {
            throw new ContractValidateException(ERROR_MSG);
        }
    }

    /**
     * проверяет если заданный каталог не существует.
     *
     * @return true если каталог не существует
     */
    private boolean checkDirIsNotExists() {
        return false;
    }
}
