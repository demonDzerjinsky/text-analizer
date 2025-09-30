package ru.ssp.impl;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

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
        var result = scanner.scanDir("/Users/dmitrijdzerjinsky/Work/text-analyzer/resources/folder");
        final List<String> expected = of(
                "/Users/dmitrijdzerjinsky/Work/text-analyzer/resources/folder/file1.txt",
                "/Users/dmitrijdzerjinsky/Work/text-analyzer/resources/folder/file2.txt",
                "/Users/dmitrijdzerjinsky/Work/text-analyzer/resources/folder/file3.txt",
                "/Users/dmitrijdzerjinsky/Work/text-analyzer/resources/folder/file4.txt",
                "/Users/dmitrijdzerjinsky/Work/text-analyzer/resources/folder/file5.txt");
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
