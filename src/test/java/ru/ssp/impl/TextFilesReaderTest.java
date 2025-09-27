package ru.ssp.impl;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;

public class TextFilesReaderTest {

    private TextFilesReader reader;

    @BeforeEach
    void prepare() {
        reader = new TextFilesReader(List.of("file1", "file2", "file3"));
    }
}
