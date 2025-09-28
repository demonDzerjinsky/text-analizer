package ru.ssp.impl;

import java.util.List;
import java.util.Optional;

import org.javatuples.Pair;

interface ReportTopBuilder {
    Optional<List<Pair<String, Integer>>> buildReport(String dir,
            int nWords, int nThreads);
}
