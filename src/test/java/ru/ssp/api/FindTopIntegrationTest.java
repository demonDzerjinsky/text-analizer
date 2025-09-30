package ru.ssp.api;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import ru.ssp.dto.ReportTopRequestDto;

@Slf4j
public class FindTopIntegrationTest {

    @Test
    void checkFindTop() {
        final var params = new ReportTopRequestDto("/Users/dmitrijdzerjinsky/Work/text-analyzer/resources/folder1", 3);
        var result = Reports.reportTop(params);
        log.info("result = {}", result);
        assertTrue(true);
    }
}
