package ru.ssp.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class DirectoryScannerImplTest {

    @Test
    void scanReturnsNonEmptyListWhenDirNotEmpty() {
        var scanner = new DirectoryScannerImpl();
        var result = scanner.scanDir("./resources/folder");
        Assertions.assertThat(result).isNotEmpty();
    }

    @Test
    void scatReturnsEmptyListWhenDirIsEmpty() {
        var scanner = new DirectoryScannerImpl();
        var result = scanner.scanDir("./resources/emptyFolder");
        Assertions.assertThat(result).isEmpty();
    }
}
