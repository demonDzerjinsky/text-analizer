package ru.ssp.executors;

import java.util.List;
import java.util.Optional;

/**
 * сбор статистики на пуле потоков обработки.
 */
public class OneReaderManyConsumersExecutor implements CustomExecutorService {

    @Override
    public Optional<List<BaseWordsCounter>> submitAndWait(List<String> fileNames, int threads) {
        return Optional.empty();
    }

}
