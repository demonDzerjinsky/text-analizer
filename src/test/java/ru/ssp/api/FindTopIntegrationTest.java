package ru.ssp.api;

import ru.ssp.api.FindTop;
import ru.ssp.dto.ParamDto;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FindTopIntegrationTest {

    @Test
    void checkFindTop() {
        FindTop sss = new FindTop() {
            
        };
        sss.find(new ParamDto("resources/folder", 10, 3));
    }
}
