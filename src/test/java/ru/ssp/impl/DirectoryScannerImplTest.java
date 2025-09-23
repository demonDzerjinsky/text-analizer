package ru.ssp.impl;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DirectoryScannerImplTest {
    private DirectoryScanner scanner;

    @BeforeEach
    void prepare() {
        scanner = new DirectoryScannerImpl();
    }

    @Test
    void scanReturnsNonEmptyListWhenDirNotEmpty() {
        var result = scanner.scanDir("./resources/folder");
        final List<String> expected = of(
                "./resources/folder/file1.txt",
                "./resources/folder/file2.txt",
                "./resources/folder/file3.txt",
                "./resources/folder/file4.txt",
                "./resources/folder/file5.txt");
        log.info(result.toString());
        assertThat(result).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void scanReturnsEmptyListWhenDirIsEmpty() {
        var result = scanner.scanDir("./resources/emptyFolder");
        assertThat(result).isEmpty();
    }

    @Test
    void scanThrowsWhenDirNotExists() {
        var ex = assertThrows(RuntimeException.class, () -> scanner.scanDir("any"));
    }
}
