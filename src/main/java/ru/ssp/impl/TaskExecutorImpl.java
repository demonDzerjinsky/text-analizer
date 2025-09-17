package ru.ssp.impl;

import java.util.List;

import org.javatuples.Pair;
import org.javatuples.Triplet;

/**
 * выполняет пул задач в многопоточном режиме
 * и формирует отчет по TOP слов.
 */
class TaskExecutorImpl implements TaskExecutor {

    /**
     * выполняет пул задач.
     *
     * @param tasks   пул задач сформированных для {@code nThread} потоков
     * @param nWord   параметр количества слов в отчете
     * @param nThread параметр количества потоков
     */
    @Override
    public List<Pair<String, Integer>> executeTasks(
            final List<List<Triplet<String, Long, Long>>> tasks,
            final int nWord,
            final int nThread) {
        throw new UnsupportedOperationException();
    }

}
