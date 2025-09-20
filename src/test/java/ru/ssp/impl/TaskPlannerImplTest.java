package ru.ssp.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskPlannerImplTest {

    private TaskPlannerImpl planner;

    @BeforeEach
    void prepare() {
        planner = new TaskPlannerImpl();
    }

    @Test
    void calcSumReturnsValWhenFilesWithSize() {
        final List<Pair<String, Long>> files = List.of(
                new Pair<String, Long>("file1", 1000L),
                new Pair<String, Long>("file2", 1523L),
                new Pair<String, Long>("file3", 1500L));
        final long expected = files.get(0).getValue1()
                + files.get(1).getValue1()
                + files.get(2).getValue1();
        assertThat(planner.calcSumSize(files)).isEqualTo(expected);
    }

    @Test
    void calcSumThrowsWhenNoFilesWithSize() {
        final List<Pair<String, Long>> files = List.of(
                new Pair<String, Long>("file1", 0L),
                new Pair<String, Long>("file2", 0L),
                new Pair<String, Long>("file3", 0L));
        assertThrows(RuntimeException.class, () -> planner.calcSumSize(null));
        assertThrows(RuntimeException.class, () -> planner.calcSumSize(files));
    }

    @Test
    void calcThreadsReturnsNumberOfThreadsWhenParamsCorrect() {
        planner.setLimit(20);
        assertThat(planner.calcEffectiveThreads(10, 5)).isEqualTo(1);
        assertThat(planner.calcEffectiveThreads(20, 5)).isEqualTo(1);
        assertThat(planner.calcEffectiveThreads(21, 5)).isEqualTo(1);
        assertThat(planner.calcEffectiveThreads(39, 5)).isEqualTo(1);
        assertThat(planner.calcEffectiveThreads(40, 5)).isEqualTo(2);
        assertThat(planner.calcEffectiveThreads(41, 5)).isEqualTo(2);
        assertThat(planner.calcEffectiveThreads(100, 5)).isEqualTo(5);
        assertThat(planner.calcEffectiveThreads(200, 5)).isEqualTo(5);
    }

    @Test
    void checkGenerateReturnTasks() {
        final int nThread = 5;
        final long minBytesOnThreadLimit = 2000;
        planner.setLimit(minBytesOnThreadLimit);
        final List<Pair<String, Long>> files = List.of(
                new Pair<>("file1", 100L),
                new Pair<>("file2", 200L),
                new Pair<>("file3", 141L));
        final long sumSize = planner.calcSumSize(files);
        assertEquals(441L, sumSize);
        long eThreads;
        // суммарный объем файлов меньше чем лимит на один поток
        eThreads = planner.calcEffectiveThreads(sumSize, nThread);
        assertEquals(1L, eThreads);
        List<List<Triplet<String, Long, Long>>> tasks1 = planner.generateTasks(files, sumSize, eThreads);
        List<List<Triplet<String, Long, Long>>> expected1 = List.of(List.of(
                new Triplet<String, Long, Long>("file1", 0L, 99L),
                new Triplet<String, Long, Long>("file2", 0L, 199L),
                new Triplet<String, Long, Long>("file3", 0L, 140L)));
        log.info("case 1 tasks: {}", tasks1);
        assertThat(tasks1).containsExactlyInAnyOrderElementsOf(expected1);
        // суммарный объем файлов равен лимиту на один поток
        planner.setLimit(sumSize);
        eThreads = planner.calcEffectiveThreads(sumSize, nThread);
        assertEquals(1L, eThreads);
        List<List<Triplet<String, Long, Long>>> tasks2 = planner.generateTasks(files, sumSize, eThreads);
        log.info("case 2 tasks: {}", tasks2);
        assertThat(tasks2).containsExactlyInAnyOrderElementsOf(expected1);
        // суммарный объем файлов больше лимита на один поток
        planner.setLimit(sumSize / 2 + 1);
        eThreads = planner.calcEffectiveThreads(sumSize, nThread);
        assertEquals(1L, eThreads);
        List<List<Triplet<String, Long, Long>>> tasks3 = planner.generateTasks(files, sumSize, eThreads);
        log.info("case 3 tasks: {}", tasks3);
        assertThat(tasks3).size().isEqualTo(1);
        assertThat(tasks3).containsExactlyInAnyOrderElementsOf(expected1);
        // суммарный объем файлов больше лимита на два потока
        planner.setLimit(sumSize / 3 + 10);
        eThreads = planner.calcEffectiveThreads(sumSize, nThread);
        assertEquals(2L, eThreads);
        List<List<Triplet<String, Long, Long>>> tasks4 = planner.generateTasks(files, sumSize, eThreads);
        log.info("case 4 tasks: {}", tasks4);
        assertThat(tasks4).size().isEqualTo(2);
        // суммарный объем файлов больше лимита на три потока
        planner.setLimit(sumSize / 4 + 10);
        eThreads = planner.calcEffectiveThreads(sumSize, nThread);
        assertEquals(3L, eThreads);
        List<List<Triplet<String, Long, Long>>> tasks5 = planner.generateTasks(files, sumSize, eThreads);
        log.info("case 5 tasks: {}", tasks5);
        assertThat(tasks5).size().isEqualTo(3);
        // [[[file1, 0, 99], [file2, 0, 46]], [[file2, 47, 192]], [[file2, 193, 199],
        // [file3, 0, 138], [file3, 139, 140]]]
        List<List<Triplet<String, Long, Long>>> expected5 = List.of(
                List.of(
                        new Triplet<String, Long, Long>("file1", 0L, 99L),
                        new Triplet<String, Long, Long>("file2", 0L, 46L)),
                List.of(
                        new Triplet<String, Long, Long>("file2", 47L, 192L)),
                List.of(
                        new Triplet<String, Long, Long>("file2", 193L, 199L),
                        new Triplet<String, Long, Long>("file3", 0L, 138L),
                        new Triplet<String, Long, Long>("file3", 139L, 140L))
        );
        assertThat(tasks5).containsExactlyInAnyOrderElementsOf(expected5);
        // файл большого объема должен быть распределен по потокам
    }

    @Test
    void checkGenerateReturnTasksWhenBigFile() {
        final int nThread = 5;
        final long minBytesOnThreadLimit = 2000;
        planner.setLimit(minBytesOnThreadLimit);
        final List<Pair<String, Long>> files = List.of(
                new Pair<>("file1", 10000000L),
                new Pair<>("file2", 200L),
                new Pair<>("file3", 141L));
        final long sumSize = planner.calcSumSize(files);
        long eThreads;
        eThreads = planner.calcEffectiveThreads(sumSize, nThread);
        assertThat(eThreads).isEqualTo(5);
        var result = planner.generateTasks(files, sumSize, eThreads);
        log.info("result = {}", result);
        assertThat(result).size().isEqualTo(5);
    }         
}
