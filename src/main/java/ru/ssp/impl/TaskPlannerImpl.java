package ru.ssp.impl;

import java.util.ArrayList;
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
     * сообщение ошибки.
     */
    private static final String SOMETHING_WRONG = "something wrong";
    /**
     * сообщение ошибки.
     */
    private static final String ZERO_SIZED_FILES = "0 - sized files";
    /**
     * сообщение ошибки.
     */
    private static final String BAD_PARAMS = "bad params";
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
            final List<Pair<String, Long>> files, final int nThread) {
        final long sumFilesSize = calcSumSize(files);
        final long effThreads = calcEffectiveThreads(sumFilesSize, nThread);
        return generateTasks(files, sumFilesSize, effThreads);
    }

    /**
     * распределяем всю работу объемом {@code sumFilesSize} примерно равными
     * долями среди {@code effectiveThreads} потоков.
     *
     * @param files
     * @param sumFilesSize
     * @param effectiveThreads
     * @return коллекция задач по потокам
     */
    List<List<Triplet<String, Long, Long>>> generateTasks(
            final List<Pair<String, Long>> files,
            final long sumFilesSize,
            final long effectiveThreads) {
        final long bytesPerThread = sumFilesSize / effectiveThreads;
        long currPos = 1;
        long nextPos = 0;
        int currFilesIndex = 0;
        long currFileBytesIndex = 0;
        var resTasks = new ArrayList<List<Triplet<String, Long, Long>>>();
        while (currPos < sumFilesSize) {
            resTasks.add(new ArrayList<>());
            nextPos = Math.min(currPos + bytesPerThread, sumFilesSize);
            while (currPos < nextPos) {
                long nextFileBytesChank = nextPos - currPos;
                var mabyOffset = currFileBytesIndex + nextFileBytesChank;
                if (mabyOffset < files.get(currFilesIndex).getValue1()) {
                    resTasks.get(resTasks.size() - 1).add(
                            new Triplet<String, Long, Long>(
                                    files.get(currFilesIndex).getValue0(),
                                    currFileBytesIndex,
                                    currFileBytesIndex + nextFileBytesChank));
                    currFileBytesIndex += nextFileBytesChank;
                    currPos = nextPos;
                    break;
                }
                nextFileBytesChank = files.get(currFilesIndex)
                        .getValue1() - currFileBytesIndex;
                currPos += nextFileBytesChank;
                resTasks.get(resTasks.size() - 1).add(
                        new Triplet<String, Long, Long>(
                                files.get(currFilesIndex).getValue0(),
                                currFileBytesIndex,
                                files.get(currFilesIndex).getValue1()));
                currFilesIndex++;
                currFileBytesIndex = 0;
            }
        }
        return resTasks;
    }

    /**
     * определяет сколько нужно реально потоков обработки
     * для выполнения всей работы объемом {@code sumFilesSize}
     * с загруженностью потока не меньше установленного лимита
     * и верхней границе количества потоков.
     *
     * @param sumFilesSize общий объем работы в байтах
     * @param nThread      верхняя граница количеств потоков обработки
     * @return количестро потоков
     */
    long calcEffectiveThreads(
            final long sumFilesSize,
            final int nThread) {
        if (nThread <= 0 || sumFilesSize <= 0) {
            throw new RuntimeException(BAD_PARAMS);
        }
        if (sumFilesSize > nThread * minBytesLimit) {
            return nThread;
        }
        if (sumFilesSize < minBytesLimit) {
            return 1;
        }
        return sumFilesSize / minBytesLimit;
    }

    Long calcSumSize(
            @NonNull final List<Pair<String, Long>> files) {
        long sum = 0;
        for (var it : files) {
            sum += it.getValue1();
        }
        if (sum == 0) {
            throw new RuntimeException(ZERO_SIZED_FILES);
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
