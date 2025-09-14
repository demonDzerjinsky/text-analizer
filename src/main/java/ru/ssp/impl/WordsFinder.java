package ru.ssp.impl;

import java.util.Optional;

import ru.ssp.dto.ParamDto;
import ru.ssp.dto.ResultDto;

/**
 * фасад.
 *
 */
public final class WordsFinder {
    /**
     * валидатор контракта вызова.
     */
    private final Validator<ParamDto> validator;
    /**
     * поисковый движок.
     */
    private final FindWords finderEngine;

    WordsFinder(
            final Validator<ParamDto> pvalidator,
            final FindWords pfinder) {
        this.validator = pvalidator;
        this.finderEngine = pfinder;
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
     */
    public static ResultDto find(final ParamDto findParam) {
        // TODO lock and check locking
        final WordsFinder finder = new WordsFinder(// можно вынести в фабр.
                new ContractValidator(),
                new WordsFinderEngine());
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
                .flatMap(p -> finderEngine.find(
                        p.srcDir(),
                        p.nWords(),
                        p.nThreads()))
                .map(ResultDto::new)
                .orElseThrow(RuntimeException::new);
    }

    private Optional<ParamDto> validate(final ParamDto param) {
        validator.validate(param);
        return Optional.of(param);
    }
}
