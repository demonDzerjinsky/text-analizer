package ru.ssp.api;

import java.util.Optional;

import ru.ssp.dto.ReportTopRequestDto;
import ru.ssp.dto.ReportTopResultDto;
import ru.ssp.impl.ReportTop;

/**
 * API отчетов.
 * реализовант один отчет с поиском N самых часто встречаемых слов
 * в соответствии с задачей.
 */
public final class Reports {

    /**
     * выполняет поиск слов и формирует отчет.
     *
     * @param requestDto параметры вызова с критериями поиска
     * @return отчет по результатам поиска {@code ReportTopResultDto}
     */
    public Optional<ReportTopResultDto> reportTop(final ReportTopRequestDto requestDto) {
        return ReportTop.makeReport(requestDto);
    }
}
