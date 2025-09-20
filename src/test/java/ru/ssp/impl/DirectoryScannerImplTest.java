package ru.ssp.impl;

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
        final List<Pair<String, Long>> expected = List.of(new Pair<String, Long>("./resources/folder/file1.txt", 22L));
        assertThat(result).containsExactlyInAnyOrderElementsOf(expected);
        log.info(result.toString());
    }

    @Test
    void scanReturnsEmptyListWhenDirIsEmpty() {
        var result = scanner.scanDir("./resources/emptyFolder");
        assertThat(result).isEmpty();
    }

    @Test
    void scanThrowsWhenDirNotExists() {
        var ex = assertThrows(RuntimeException.class, () -> scanner.scanDir("any"));
        // log.info(ex.getMessage(), ex);
    }
}
