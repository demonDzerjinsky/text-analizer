package ru.ssp.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class WordsFinderTest {

    @Test
    void findTopShouldExecuteTextFinder() {
        // TODO
        assertThat(true);
    }

    @Test
    void getInstanceShouldReturnInstance() {
        final WordsFinder inst = WordsFinder.getInstance();
        assertThat(inst).isNotNull();
    }
}
