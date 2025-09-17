package ru.ssp.impl;

import java.util.List;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * реализует интерфейс формирования пула задач.
 */
@Slf4j
class TaskPlannerImpl implements TaskPlanner {
    /**
     * default limit constant.
     */
    private static final Long DEFAULT_LIMIT = 20L;
    /**
     * минимальный объем на поток.
     * конфигурационный параметр.
     */
    private Long minBytesLimit = DEFAULT_LIMIT;

    /**
     * формирует пул задач на nThread потоков.
     *
     * входной пул файлов разбивается на задачи обработки для каждого
     * отдельного потока.
     * протоков не может быть больше заданного в параметре вызова
     * {@code nThread}, но может быть меньше по решению планировщика -
     * если объем задачи таков что выделение отдельного потока
     * не целесообразно.
     * решение о целесообразности под какой минимальный объем обработки нужно
     * выделить поток произвоится на основе {@code MIN_BYTES_LIMIT}
     *
     * @param files   метаданные файл-объем
     * @param nThread параметр количества потоков - максимальное количество
     *                потоков
     * @return пул задач на потоки
     */
    @Override
    public List<List<Triplet<String, Long, Long>>> makeTasks(
            final List<Pair<String, Long>> files,
            final int nThread) {
        final long sumFilesSize = calcSumSize(files);
        final int effectiveThreads = calcEffectiveThreads(
                sumFilesSize,
                nThread);
        return generateTasks(files, sumFilesSize, effectiveThreads);
    }

    List<List<Triplet<String, Long, Long>>> generateTasks(
            final List<Pair<String, Long>> files,
            final long sumFilesSize,
            final int effectiveThreads) {
        throw new UnsupportedOperationException("");
    }

    int calcEffectiveThreads(
            final long sumFilesSize,
            final int nThread) {
        throw new UnsupportedOperationException("");
    }

    Long calcSumSize(
            @NonNull final List<Pair<String, Long>> files) {
        long sum = 0;
        for (var it : files) {
            sum += it.getValue1();
        }
        if (sum == 0) {
            throw new RuntimeException("0 - sized files");
        }
        return sum;
    }

    /**
     * устонавливает минимальный лимит по объему работ на поток.
     * объем ниже данного значения в задачу для отдельного потока
     * не выделяется.
     *
     * @param limit объем задачи, байты
     */
    public void setLimit(final long limit) {
        if (limit <= 0) {
            throw new RuntimeException("limit should be greater than 0");
        }
        this.minBytesLimit = limit;
    }

    /*
     *
     *
     *
     *
     *
     *
     *
     *
     */

}
