package ru.ssp.impl;

import static java.util.Optional.of;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.javatuples.Pair;

import ru.ssp.executors.BaseWordsCounter;
import ru.ssp.executors.CustomExecutorService;

/**
 * построитель отчета TOP слов.
 */
class ReportTopBuilderImpl implements ReportTopBuilder {

    /**
     * поставщик перечня файлов.
     */
    private final DirectoryScanner scnr;

    /**
     * кастомный пул потоков для сбора статистики по словам.
     */
    private final CustomExecutorService exctr;

    ReportTopBuilderImpl(final DirectoryScanner scanner, final CustomExecutorService executor) {
        this.scnr = scanner;
        this.exctr = executor;
    }

    @Override
    public Optional<List<Pair<String, Integer>>> buildReport(String dir, int nWords, int nThreads) {
        return of(dir)
                .map(scnr::scanDir)
                .flatMap(fls -> exctr.submitAndWait(fls, nThreads))
                .flatMap(this::merge)
                .flatMap(m -> this.getTop(m, nWords))
                .flatMap(this::transform);
    }

    /**
     * сливает результаты в один отчет.
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
     * полных сортировок больших коллекций и определяем только нужные элементы
     * в один проход.
     *
     * @param mergedMap полная карта статистики всех слов
     * @param wds       количество TopN по скольки построить отчет
     * @return отчет TopN в формате число-коллекция слов
     */
    Optional<List<Pair<Integer, List<String>>>> getTop(
            final Map<String, Integer> mergedMap, final int wds) {
        final LinkedList<Pair<Integer, List<String>>> rpt = new LinkedList<>();
        mergedMap.entrySet().forEach(it -> {
            if (rpt.size() < wds || rpt.get(0).getValue0() <= it.getValue()) {
                for (int i = 0; i < wds; i++) {
                    if (i == rpt.size()) {
                        rpt.add(new Pair<Integer, List<String>>(
                                it.getValue(),
                                Arrays.asList(it.getKey())));
                        break;
                    } else if (rpt.get(i).getValue0() == it.getValue()) {
                        rpt.get(i).getValue1().add(it.getKey());
                        break;
                    } else if (rpt.get(i).getValue0() > it.getValue()) {
                        rpt.add(i, new Pair<Integer, List<String>>(
                                it.getValue(),
                                Arrays.asList(it.getKey())));
                        break;
                    }
                }
                while (rpt.size() >= wds) {
                    rpt.pop();
                }
            }
        });
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
