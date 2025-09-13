package ru.ssp.api;

import ru.ssp.dto.ParamDto;
import ru.ssp.dto.ResultDto;
import ru.ssp.impl.WordsFinder;

/**
 * интерфейс api модуля поиска.
 *
 * выполняет поиск в дирректории наиболее часто встречаемых слов
 */
public interface FindTop {
    /**
     * выполняет поиск слов и формирует отчет.
     *
     * @param findParam параметры вызова с критериями поиска
     * @return отчет по результатам поиска
     */
    default ResultDto find(final ParamDto findParam) {
        return WordsFinder.find(findParam);
    }
}
