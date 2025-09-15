package ru.ssp.impl;

import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.ssp.dto.ParamDto;
import ru.ssp.exceptions.ContractValidateException;

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
    void executeThrowExceptionWhenParametersNotValid() {
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
                                new ParamDto("./someNotExistsDir", 1, 1))));
        Mockito.verifyNoInteractions(engine);
    }

    @Test
    void executeInvoceFindWhenValidParameters() {
        doReturn(
                of(List.of(new Pair<String, Integer>("word", 1))))
                .when(engine).find(anyString(), anyInt(), anyInt());
        finder.execute(new ParamDto(".", 2, 1));
        verify(engine, times(1))
                .find(anyString(), anyInt(), anyInt());
    }
}
