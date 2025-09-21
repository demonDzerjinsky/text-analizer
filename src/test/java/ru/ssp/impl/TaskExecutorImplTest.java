package ru.ssp.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.javatuples.Triplet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskExecutorImplTest {
    /*
     *
     * [[[resources/folder/file2.txt, 0, 10075], [resources/folder/file3.txt, 0,
     * 6578]], [[resources/folder/file3.txt, 6579, 10070],
     * [resources/folder/file1.txt, 0, 10007], [resources/folder/file4.txt, 0,
     * 3153]], [[resources/folder/file4.txt, 3154, 10062],
     * [resources/folder/file5.txt, 0, 9744], [resources/folder/file5.txt, 9745,
     * 9747]]]
     *
     */

    private TaskExecutorImpl executor;

    @BeforeEach
    void prepare() {
        executor = new TaskExecutorImpl();
    }

    @Test
    void checkExecuteTasks() {
        final List<List<Triplet<String, Long, Long>>> tasks = List.of(
                List.of(),
                List.of(),
                List.of());
        var result = executor.executeTasks(tasks, 10, 3);
        assertThat(result).isNotNull();
    }

}
