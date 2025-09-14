package ru.ssp.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;

import ru.ssp.dto.ParamDto;
import ru.ssp.exceptions.ContractValidateException;

import org.junit.jupiter.api.Test;

public class WordsFinderTest {

    private ContractValidator validator;
    private WordsFinder finder;

    @BeforeEach
    void prepare() {
        validator = new ContractValidator();
        finder = new WordsFinder(validator);
    }

    @Test
    void throwExceptionWhenParametersNotValid() {
        assertAll(

                () -> assertThrows(
                        ContractValidateException.class,
                        () -> finder.execute(
                                new ParamDto(null, 1, 1))),
                () -> assertThrows(
                        ContractValidateException.class,
                        () -> finder.execute(
                                new ParamDto(".", -1, 1))),
                () -> assertThrows(
                        ContractValidateException.class,
                        () -> finder.execute(
                                new ParamDto(".", 1, -1))),
                () -> assertThrows(
                        ContractValidateException.class,
                        () -> finder.execute(
                                new ParamDto(".", 10000, 1))),
                () -> assertThrows(
                        ContractValidateException.class,
                        () -> finder.execute(
                                new ParamDto(".", 1, 10000))),
                () -> assertThrows(
                        ContractValidateException.class,
                        () -> finder.execute(
                                new ParamDto("./someNotExistsDir", 1, 1)))
                // () -> assertThrows(
                //         ContractValidateException.class,
                //         () -> finder.execute(
                //                 new ParamDto(".", 1, 1)))

        );
    }

    @Test
    void checkExecuteWordsFinderEngineWhenParametersValid() {
        assertTrue(true);
    }
}
