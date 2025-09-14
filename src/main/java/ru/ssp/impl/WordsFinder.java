package ru.ssp.impl;

import java.util.Optional;

import ru.ssp.dto.ParamDto;
import ru.ssp.dto.ResultDto;

/**
 * основной класс, логика поиска, точка входа.
 *
 *
 */
public final class WordsFinder {
    /**
     * валидатор контракта вызова.
     */
    private final ContractValidator validator;

    WordsFinder(final ContractValidator pvalidator) {
        this.validator = pvalidator;
    }

    /**
     * реализует итерфейс поиска.
     *
     * только один вызов обрабатывается, остальные вызовы в момент обработки
     * получат исключение.
     * Исходим из того что вызов занимает все ядра и параллельные вызовы
     * могут привести к общей деградации, по этому должны откидываться
     *
     * @param findParam пераметры вызова
     * @return результаты вызова
     * @exception WordsFinderConcurrentException
     *                                           в случае если вызов сделан
     *                                           в время работы предыдущего
     *                                           вызова
     */
    public static ResultDto find(final ParamDto findParam) {
        // TODO lock and check locking
        final WordsFinder finder = new WordsFinder(
                new ContractValidator()

        );
        return finder.execute(findParam);
    }

    /**
     * фасад.
     *
     * @param param входные параметры для выполнения поиска
     * @return результат выполнения поиска
     */
    ResultDto execute(final ParamDto param) {
        return validate(param)
                .map(p -> new ResultDto())
                .orElseThrow(RuntimeException::new);
    }

    private Optional<ParamDto> validate(final ParamDto param) {
        validator.validate(param);
        return Optional.of(param);
    }
}
