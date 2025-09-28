package ru.ssp.impl;

import java.util.Optional;

import ru.ssp.dto.ReportTopRequestDto;
import ru.ssp.dto.ReportTopResultDto;

/**
 * реализация отчета со статистикой TOP слов.
 */
public final class ReportTop {

    /**
     * валидатор контракта вызова.
     */
    private final Validator<ReportTopRequestDto> validator;

    /**
     * построитель отчета.
     */
    private final ReportTopBuilder reportBuilder;

    ReportTop(final Validator<ReportTopRequestDto> requestValidator,
            final ReportTopBuilder reportTopBuilder) {
        this.validator = requestValidator;
        this.reportBuilder = reportTopBuilder;
    }

    /**
     * строит отчет.
     *
     * @param reportParam пераметры вызова
     * @return отчет в контракте API
     */
    public static Optional<ReportTopResultDto> makeReport(
            final ReportTopRequestDto reportParam) {
        return createInstance().execute(reportParam);
    }

    private static ReportTop createInstance() {
        return new ReportTop(
                new ReportTopRequestValidator(),
                new ReportTopBuilderDelegate());
    }

    /**
     * фасад.
     * валидирует входной контракт, мапирует параметры при передаче построителю
     * , принимает результат и маппирует в выходной контракт.
     *
     * @param reportParam входные параметры для построения отчета
     * @return отчет в контракте API
     */
    Optional<ReportTopResultDto> execute(final ReportTopRequestDto reportParam) {
        return validator.validate(reportParam)
                .flatMap(p -> reportBuilder
                        .buildReport(
                                p.srcDir(),
                                p.nWords(),
                                p.nThreads()))
                .map(ReportTopResultDto::new);
    }

}
