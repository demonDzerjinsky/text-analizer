package ru.ssp.impl;

import static java.util.Optional.of;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.javatuples.Pair;

import lombok.extern.slf4j.Slf4j;
import ru.ssp.executors.BaseWordsCounter;
import ru.ssp.executors.CustomExecutorService;

/**
 * построитель отчета TOP слов.
 */
@Slf4j
class ReportTopBuilderImpl implements ReportTopBuilder {

    /**
     * поставщик перечня файлов.
     */
    private final DirectoryScanner scnr;

    /**
     * кастомный пул потоков для сбора статистики по словам.
     */
    private final CustomExecutorService exctr;

    ReportTopBuilderImpl(final DirectoryScanner scanner,
            final CustomExecutorService executor) {
        this.scnr = scanner;
        this.exctr = executor;
    }

    @Override
    public Optional<List<Pair<String, Integer>>> buildReport(final String dir,
            final int nWords) {
        return of(dir)
                .map(scnr::scanDir)
                // сбор статистики делегируем пулу потоков
                .map(exctr::submitAndWait)
                // сводим - складываем полученные от потоков статистики
                .flatMap(this::merge)
                // выделяем top-слов из общей статистики
                .flatMap(m -> this.getTop(m, nWords))
                .flatMap(this::transform);
    }

    /**
     * совмещает статистики по словам от потоков в единую статистику.
     *
     * @param wCounts коллекция объектов-отчетов из отработавших потоков
     * @return суммарная карта статистики слов
     */
    Optional<Map<String, Integer>> merge(
            final List<BaseWordsCounter> wCounts) {
        return of(wCounts.stream()
                .flatMap(r -> r.getWordsCounts().entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (v1, v2) -> v1 + v2)));
    }

    /**
     * определяет top-N слов.
     * кастомный метод, снижаем вычислительную сложность - уходим от
     * полной сортировки большой карты статистики слов, определяем
     * только нужные top-N элементы в один проход.
     *
     * @param mergedMap полная карта статистики всех слов
     * @param wds       количество TopN по скольки построить отчет
     * @return отчет TopN в формате число-коллекция слов
     */
    Optional<List<Pair<Integer, List<String>>>> getTop(
            final Map<String, Integer> mergedMap, final int wds) {
        final LinkedList<Pair<Integer, List<String>>> rpt = new LinkedList<>();
        for (var it : mergedMap.entrySet()) {
            final String word = it.getKey();
            final Integer cnt = it.getValue();
            if (rpt.size() == 0) {
                rpt.addFirst(
                        new Pair<Integer, List<String>>(
                                cnt,
                                new ArrayList<String>(List.of(word))));
                continue;
            }
            if (cnt < rpt.get(0).getValue0()) {
                if (rpt.size() >= wds) {
                    continue;
                }
                rpt.addFirst(
                        new Pair<Integer, List<String>>(
                                cnt,
                                new ArrayList<String>(List.of(word))));
                continue;
            }
            boolean found = false;
            for (int i = 0; i < rpt.size(); i++) {
                if (cnt == rpt.get(i).getValue0()) {
                    rpt.get(i).getValue1().add(word);
                    found = true;
                    break;
                }
                if (cnt < rpt.get(i).getValue0()) {
                    rpt.add(i, new Pair<Integer, List<String>>(
                            cnt,
                            new ArrayList<String>(List.of(word))));
                    found = true;
                    if (rpt.size() > wds) {
                        rpt.removeFirst();
                    }
                    break;
                }
            }
            if (found) {
                continue;
            }
            rpt.addLast(
                    new Pair<Integer, List<String>>(
                            cnt,
                            new ArrayList<String>(List.of(word))));
            if (rpt.size() > wds) {
                rpt.removeFirst();
            }
        }
        return of(rpt);
    }

    /**
     * преобразует отчет из внутреннего формата, полученного при выборке
     * к формату контракта.
     *
     * @param rpt отчет во внутреннем формате
     * @return отчет в формате контракта слово-частота
     */
    Optional<List<Pair<String, Integer>>> transform(
            final List<Pair<Integer, List<String>>> rpt) {
        final List<Pair<String, Integer>> result = new ArrayList<>();
        for (var entry : rpt) {
            for (int i = 0; i < entry.getValue1().size(); i++) {
                result.add(
                        new Pair<String, Integer>(
                                entry.getValue1().get(i),
                                entry.getValue0()));
            }
        }
        return of(result);
    }

}
