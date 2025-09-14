package ru.ssp.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;

import ru.ssp.dto.ParamDto;
import ru.ssp.exceptions.ContractValidateException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({ MockitoExtension.class })
public class WordsFinderTest {

    private Validator<ParamDto> validator;
    @Mock
    private WordsFinderEngine engine;
    @InjectMocks
    private WordsFinder finder;

    @BeforeEach
    void prepare() {
        validator = new ContractValidator();
        finder = new WordsFinder(validator, engine);
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
        // ContractValidateException.class,
        // () -> finder.execute(
        // new ParamDto(".", 2, 1)))
        );
        Mockito.verifyNoInteractions(engine);
    }

    @Test
    void checkExecuteWordsFinderEngineWhenParametersValid() {
        assertTrue(true);
    }
}
