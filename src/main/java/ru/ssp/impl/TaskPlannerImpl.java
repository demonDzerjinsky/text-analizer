package ru.ssp.impl;

import java.util.List;

import org.javatuples.Pair;
import org.javatuples.Triplet;

/**
 * реализует интерфейс формирования пула задач.
 */
class TaskPlannerImpl implements TaskPlanner {

    /**
     * формирует пул задач на nThread потоков.
     *
     * @param files метаданные файл-объем
     * @param nThread параметр количества потоков
     * @return пул задач на потоки
     */
    @Override
    public List<List<Triplet<String, Long, Long>>> makeTasks(
            final List<Pair<String, Long>> files,
            final int nThread) {
        throw new UnsupportedOperationException();
    }

}
