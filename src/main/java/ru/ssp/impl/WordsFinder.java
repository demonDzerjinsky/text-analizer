package ru.ssp.impl;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import lombok.SneakyThrows;
import ru.ssp.dto.ParamDto;
import ru.ssp.dto.ResultDto;
import ru.ssp.exceptions.WordsFinderConcurrentException;

/**
 * входная точка в импементацию API.
 *
 */
public final class WordsFinder  {
    /**
     * блокировка запуска второго метода {@code WordsFinder.find(...)}.
     */
    private static ReentrantLock lock = new ReentrantLock();
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
    @SneakyThrows
    public static ResultDto find(final ParamDto findParam) {
        if (!lock.tryLock(1, TimeUnit.SECONDS)) {
            throw new WordsFinderConcurrentException();
        }
        try {
            return createInstance()
                    .execute(findParam);
        } finally {
            lock.unlock();
        }
    }

    static WordsFinder createInstance() {
        final WordsFinder finder = new WordsFinder(
                new ContractValidator(),
                new WordsFinderEngine());
        return finder;
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
