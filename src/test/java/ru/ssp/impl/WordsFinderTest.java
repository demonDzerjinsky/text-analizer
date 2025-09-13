package ru.ssp.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ru.ssp.dto.ParamDto;
import ru.ssp.exceptions.ContractValidateException;

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

    @Test
    void throwExceptionWhenParametersNotValid() {
        final WordsFinder inst = WordsFinder.getInstance();
        assertAll(

                () -> assertThrows(
                        ContractValidateException.class,
                        () -> inst.execute(
                                new ParamDto(null, 1, 1)))

        );
    }
}
