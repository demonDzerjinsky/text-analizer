package ru.ssp.api;

import ru.ssp.dto.FindParamDto;
import ru.ssp.dto.TopResultDto;
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
    default TopResultDto find(final FindParamDto findParam) {
        return WordsFinder.find(findParam);
    }
}
