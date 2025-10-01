package ru.ssp.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.javatuples.Pair;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import ru.ssp.dto.ReportTopRequestDto;

@Slf4j
public class FindTopIntegrationTest {

    @Disabled
    @Test
    void checkFindTopInOneFile() {
        final var params = new ReportTopRequestDto("/Users/dmitrijdzerjinsky/Work/text-analyzer/resources/folder1", 3);
        var resultOpt = Reports.reportTop(params);
        assertThat(resultOpt).isPresent();
        resultOpt.ifPresent(r -> assertThat(r.top()).containsExactlyInAnyOrder(
                new Pair<String, Integer>("amet", 37),
                new Pair<String, Integer>("quam", 38),
                new Pair<String, Integer>("vitae", 43)));
    }

    @Tag("report10")
    @Test
    void checkFindTopInManyFiles() {
        final var params = new ReportTopRequestDto("/Users/dmitrijdzerjinsky/Work/text-analyzer/resources/folder", 10);
        var resultOpt = Reports.reportTop(params);
        log.info("result: {}", resultOpt);
        // assertThat(resultOpt).isPresent();
        // resultOpt.ifPresent(r -> assertThat(r.top()).containsExactlyInAnyOrder(
        // new Pair<String, Integer>("amet", 37),
        // new Pair<String, Integer>("quam", 38),
        // new Pair<String, Integer>("vitae", 43)));

    }
}
