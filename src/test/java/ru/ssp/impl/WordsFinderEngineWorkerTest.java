package ru.ssp.impl;

import java.util.List;
import java.util.function.Function;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({ MockitoExtension.class })
public class WordsFinderEngineWorkerTest {
    private final List<Pair<String, Integer>>

    files = List.of(new Pair<String, Integer>("file1", 111));

    private final List<List<Triplet<String, Integer, Integer>>>

    tasks = List.of(List.of( // tasks for thread 1
            new Triplet<String, Integer, Integer>("file1", 1, 100)));

    private final List<Pair<String, Integer>>

    words = List.of(
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

    private Function<String, List<Pair<String, Integer>>>

    scannerThr = (dir) -> Mockito
            .doThrow(RuntimeException.class).when(scanner).scanDir(dir);

    private Function<String, List<Pair<String, Integer>>>

    scannerRet = (dir) -> Mockito
            .doReturn(files).when(scanner).scanDir(dir);

    private Function<List<Pair<String, Integer>>, List<List<Triplet<String, Integer, Integer>>>>

    plannerRet = (fls) -> Mockito
            .doReturn(tasks).when(planner).makeTasks(fls, nThreads);

    private Function<List<Pair<String, Integer>>, List<List<Triplet<String, Integer, Integer>>>>

    plannerThr = (fls) -> Mockito
            .doThrow(RuntimeException.class).when(planner).makeTasks(fls, nThreads);

    private Function<List<List<Triplet<String, Integer, Integer>>>, List<Pair<String, Integer>>>

    executorRet = (tsks) -> Mockito
            .doReturn(words).when(executor).executeTasks(tsks, nWords, nThreads);

    private Function<List<List<Triplet<String, Integer, Integer>>>, List<Pair<String, Integer>>>

    executionThr = (tsks) -> Mockito
            .doThrow(RuntimeException.class).when(executor).executeTasks(tsks, nWords, nThreads);

}
