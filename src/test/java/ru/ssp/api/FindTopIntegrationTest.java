package ru.ssp.api;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import ru.ssp.dto.ParamDto;

@Slf4j
public class FindTopIntegrationTest {

    private TextAnalizerApi analizer;

    @BeforeEach
    void prepare() {
        analizer = new TextAnalizerApi();
    }

    @Test
    void checkFindTop() { //TODO 
        final var params = new ParamDto("./resources/folder", 10, 3);
        analizer.makeReport(params);
        assertTrue(true);
    }
}
