package ru.ssp.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void generateReturnsTasksToOneThread() {
        final int nThread = 5;
        final long minBytesOnThreadLimit = 2000;
        planner.setLimit(minBytesOnThreadLimit);
        final List<Pair<String, Long>> files = List.of(
                new Pair<>("file1", 100L),
                new Pair<>("file2", 200L),
                new Pair<>("file3", 140L));
        final long sumSize = planner.calcSumSize(files);
        assertEquals(440L, sumSize);
        final long eThreads = planner.calcEffectiveThreads(sumSize, nThread);
        assertEquals(1L, eThreads);
        var expected = planner.generateTasks(files, sumSize, eThreads);
        assertThat(expected).size().isEqualTo(1);
    }
    /*
     *
     *
     *
     *
     *
     *
     *
     */
}
