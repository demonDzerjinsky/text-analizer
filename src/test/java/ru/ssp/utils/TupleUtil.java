package ru.ssp.utils;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;

import org.assertj.core.groups.Tuple;
import org.javatuples.Pair;

import lombok.NonNull;

public final class TupleUtil {

    private TupleUtil() {
    }

    public static Tuple fromPair(@NonNull Pair<?, ?> pair) {
        return tuple(pair.getValue0(), pair.getValue1());
    }

    public static <T1, T2> List<Tuple> fromPairsList(@NonNull List<Pair<T1, T2>> pairs) {
        return pairs.stream().map(TupleUtil::fromPair).collect(toList());
    }
}
