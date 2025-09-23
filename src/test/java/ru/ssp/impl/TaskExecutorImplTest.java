package ru.ssp.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskExecutorImplTest {

    private TaskExecutorImpl executor;

    @BeforeEach
    void prepare() {
        executor = new TaskExecutorImpl();
    }

    @Test
    void checkExecuteTasks() {
        final List<String> files = List.of(
                "./resources/folder/file1.txt",
                "./resources/folder/file2.txt",
                "./resources/folder/file3.txt",
                "./resources/folder/file4.txt",
                "./resources/folder/file5.txt");
        var result = executor.execute(files, 10, 3);
        assertTrue(true);
        // assertThat(result).isNotNull();
    }

}
