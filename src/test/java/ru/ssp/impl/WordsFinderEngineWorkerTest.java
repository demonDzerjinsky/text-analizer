package ru.ssp.impl;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.List;
import java.util.function.Consumer;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.ssp.exceptions.WordsFinderExecutionException;

@ExtendWith({ MockitoExtension.class })
public class WordsFinderEngineWorkerTest {
    private final List<Pair<String, Long>> files = of(new Pair<String, Long>("file1", 111L));

    private final List<List<Triplet<String, Long, Long>>> tasks = of(
            of(new Triplet<String, Long, Long>("file1", 1L, 100L)));

    private final List<Pair<String, Integer>> words = of(
            new Pair<String, Integer>("word6", 10),
            new Pair<String, Integer>("word1", 15),
            new Pair<String, Integer>("word7", 16),
            new Pair<String, Integer>("word2", 20),
            new Pair<String, Integer>("word8", 40),
            new Pair<String, Integer>("word3", 50),
            new Pair<String, Integer>("word9", 51),
            new Pair<String, Integer>("word4", 55),
            new Pair<String, Integer>("word0", 60),
            new Pair<String, Integer>("word5", 93));

    private final int nThreads = 1;
    private final int nWords = 10;

    @InjectMocks
    private WordsFinderEngineWorker eWorker;
    @Mock
    private DirectoryScanner scanner;
    @Mock
    private TaskPlanner planner;
    @Mock
    private TaskExecutor executor;

    private Consumer<String> scannerThr = (dir) -> Mockito.doThrow(RuntimeException.class).when(scanner).scanDir(dir);

    private Consumer<String> scannerRet = (dir) -> Mockito.doReturn(files).when(scanner).scanDir(dir);

    private Consumer<List<Pair<String, Long>>> plannerRet = (fls) -> Mockito.doReturn(tasks).when(planner)
            .makeTasks(fls, nThreads);

    private Consumer<List<Pair<String, Long>>> plannerThr = (fls) -> Mockito.doThrow(RuntimeException.class)
            .when(planner).makeTasks(fls, nThreads);

    private Consumer<List<List<Triplet<String, Long, Long>>>> executorRet = (tsks) -> Mockito.doReturn(words)
            .when(executor).executeTasks(tsks, nWords, nThreads);

    private Consumer<List<List<Triplet<String, Long, Long>>>> executionThr = (tsks) -> Mockito
            .doThrow(RuntimeException.class).when(executor).executeTasks(tsks, nWords, nThreads);

    @Test
    void findReturnsResultWhenAllSuccess() {
        final String dir = "someDir";
        scannerRet.accept(dir);
        plannerRet.accept(files);
        executorRet.accept(tasks);
        var result = eWorker.find(dir, nWords, nThreads);
        assertThat(result).isPresent();
        result.ifPresent(it -> assertThat(it).containsExactlyInAnyOrderElementsOf(words));
        verify(scanner, times(1)).scanDir(dir);
        verify(planner, times(1)).makeTasks(files, nThreads);
        verify(executor, times(1)).executeTasks(tasks, nWords, nThreads);
    }

    @Test
    void findThrowsWhenScannerFail() {
        final String dir = "someDir";
        scannerThr.accept(dir);
        assertThrows(WordsFinderExecutionException.class, () -> eWorker.find(dir, nWords, nThreads));
        verify(scanner, times(1)).scanDir(dir);
        verifyNoInteractions(planner, executor);
    }

    @Test
    void findThrowsWhenPlannerFail() {
        final String dir = "someDir";
        scannerRet.accept(dir);
        plannerThr.accept(files);
        assertThrows(WordsFinderExecutionException.class, () -> eWorker.find(dir, nWords, nThreads));
        verify(scanner, times(1)).scanDir(dir);
        verify(planner, times(1)).makeTasks(files, nThreads);
        verifyNoInteractions(executor);
    }

    @Test
    void findThrowsWhenExecutorFail() {
        final String dir = "someDir";
        scannerRet.accept(dir);
        plannerRet.accept(files);
        executionThr.accept(tasks);
        assertThrows(WordsFinderExecutionException.class, () -> eWorker.find(dir, nWords, nThreads));
        verify(scanner, times(1)).scanDir(dir);
        verify(planner, times(1)).makeTasks(files, nThreads);
        verify(executor, times(1)).executeTasks(tasks, nWords, nThreads);
    }

}
