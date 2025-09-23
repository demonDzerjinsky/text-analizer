package ru.ssp.impl;

import java.util.List;
import java.util.Optional;

import org.javatuples.Pair;

interface FindWords {
    Optional<List<Pair<String, Integer>>> find(String dir,
            int nWords, int nThreads);
}
