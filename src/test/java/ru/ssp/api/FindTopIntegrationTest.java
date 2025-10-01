package ru.ssp.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import ru.ssp.dto.ReportTopRequestDto;

@Slf4j
public class FindTopIntegrationTest {

    @Test
    void checkFindTopInOneFile() {
        //final var params = new ReportTopRequestDto("/Users/dmitrijdzerjinsky/Work/text-analyzer/resources/folder1", 3);
        final var params = new ReportTopRequestDto("resources/folder1", 3);
        var resultOpt = Reports.reportTop(params);
        assertThat(resultOpt).isPresent();
        resultOpt.ifPresent(r -> assertThat(r.top()).containsExactlyInAnyOrder(
                new Pair<String, Integer>("amet", 37),
                new Pair<String, Integer>("quam", 38),
                new Pair<String, Integer>("vitae", 43)));
    }

    @Test
    void checkFindTopInManyFiles() {
        //final var params = new ReportTopRequestDto("/Users/dmitrijdzerjinsky/Work/text-analyzer/resources/folder", 10);
        final var params = new ReportTopRequestDto("resources/folder", 10);
        var resultOpt = Reports.reportTop(params);
        assertThat(resultOpt).isPresent();
        resultOpt.ifPresent(r -> assertThat(r.top()).size().isEqualTo(13)); //в тестовых данных три слова делят рейтинг с другими словами по этому попали в топ
        log.info("result: {}", resultOpt);
    }

    @Test
    void shouldReturnErrorWhenNotValidRequestParams() {
        // на примере параметра количества слов в топ-отчете (ограничитель выставлен в
        // 10)
        final int errWordsParam = 11;
        //final var params = new ReportTopRequestDto("/Users/dmitrijdzerjinsky/Work/text-analyzer/resources/folder",
        final var params = new ReportTopRequestDto("resources/folder", errWordsParam);
        var resultOpt = Reports.reportTop(params);
        assertThat(resultOpt).isPresent();
        resultOpt.ifPresent(result -> {
            assertAll(
                    () -> assertThat(result.top()).isNull(),
                    () -> assertThat(result.err()).isNotNull());
        });
        log.info("result: {}", resultOpt);
    }

}
