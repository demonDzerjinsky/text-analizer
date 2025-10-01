package ru.ssp.impl;

import static java.util.Optional.of;

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
    private final Validator<ReportTopRequestDto, ReportTopResultDto> validator;

    /**
     * построитель отчета.
     */
    private final ReportTopBuilder reportBuilder;

    ReportTop(final Validator<ReportTopRequestDto, ReportTopResultDto> vl,
            final ReportTopBuilder rb) {
        this.validator = vl;
        this.reportBuilder = rb;
    }

    /**
     * строит отчет.
     *
     * @param reportParam пераметры вызова
     * @return отчет в контракте API
     */
    public static Optional<ReportTopResultDto> makeReport(
            final ReportTopRequestDto reportParam) {
        return of(createInstance().execute(reportParam));
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
    ReportTopResultDto execute(final ReportTopRequestDto reportParam) {
        return validator.validate(reportParam)
                .orElseGet(() -> reportBuilder
                        .buildReport(reportParam.srcDir(), reportParam.nWords())
                        .map(r -> new ReportTopResultDto(r, null))
                        .orElseThrow());
    }
}
