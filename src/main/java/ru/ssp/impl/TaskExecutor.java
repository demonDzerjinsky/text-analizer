package ru.ssp.impl;

import java.util.List;
import java.util.Optional;

import org.javatuples.Pair;

/**
 * выполняет анализ.
 */
interface TaskExecutor {
    Optional<List<Pair<String, Integer>>> execute(
            List<String> fls, int wds, int ths);
}
