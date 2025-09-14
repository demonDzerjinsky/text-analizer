package ru.ssp.impl;

import java.io.File;

import ru.ssp.dto.ParamDto;
import ru.ssp.exceptions.ContractValidateException;

/**
 * проверки параметров вызова.
 */
public class ContractValidator {
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
     * проверка параметров входного контракта.
     *
     * каталог задан и не пробелами
     * количество слов передано положительным и в пределах ограничений
     * количество потоков обработки передано положительным числом
     * и в пределах ограничений
     *
     * @param param
     */
    public void validate(final ParamDto param) {
        if (param == null
                || param.srcDir() == null
                || checkDirIsNotExists(param.srcDir())
                || param.srcDir().isBlank()
                || param.nWords() <= 0
                || param.nWords() > WLIMIT
                || param.nThreads() <= 0
                || param.nThreads() > PLIMIT) {
            throw new ContractValidateException(ERROR_MSG);
        }
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
