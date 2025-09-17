package ru.ssp.impl;

import java.util.List;

import org.javatuples.Pair;
import org.javatuples.Triplet;

/**
 * выполняет пул задач в заданном количестве потоков.
 */
interface TaskExecutor {
    List<Pair<String, Integer>> executeTasks(
            List<List<Triplet<String, Long, Long>>> tasks,
            int nWord,
            int nThread);
}
