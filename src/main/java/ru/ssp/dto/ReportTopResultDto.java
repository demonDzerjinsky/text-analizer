package ru.ssp.dto;

import java.util.List;

import org.javatuples.Pair;

/**
 * контракт ответа.
 * @param top коллекция пар слово-количество раз
 */
public record ReportTopResultDto(List<Pair<String, Integer>> top) {
}
