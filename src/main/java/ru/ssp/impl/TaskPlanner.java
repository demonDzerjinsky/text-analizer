package ru.ssp.impl;

import java.util.List;

import org.javatuples.Pair;
import org.javatuples.Triplet;

/**
 * нарезает задачи по переданному пулу файлов на n worker-ов.
 *
 * необходимо достич примерно одинаковой загрузки на все выделенные ресурсы.
 */
interface TaskPlanner {
    /**
     * формикует пул задач.
     *
     * @param files метафинформация по файлам {@code Tuple}
     * @param nThread количество потоков
     * @return пул задач на каждый поток
     */
    List<List<Triplet<String, Integer, Integer>>> makeTasks(
            List<Pair<String, Integer>> files,
            int nThread);
}
