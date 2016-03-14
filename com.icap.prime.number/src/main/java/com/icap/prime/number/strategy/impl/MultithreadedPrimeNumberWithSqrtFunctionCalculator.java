/**
 * 
 */
package com.icap.prime.number.strategy.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.icap.prime.number.strategy.PrimeNumberStrategy;

/**
 * @author thomas
 *
 *         This implementation uses the same strategy as
 *         {@link PrimeNumberWithSqrtFunctionCalculator} except it uses an
 *         executor service to spwan threads. This is good for integer medium
 *         big < 100M (Around 80s to calculate all prime number with a max range
 *         of 100M)
 */
public class MultithreadedPrimeNumberWithSqrtFunctionCalculator extends PrimeNumberWithSqrtFunctionCalculator {
    private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MultithreadedPrimeNumberWithSqrtFunctionCalculator.class);
    private ExecutorService executorService = Executors.newFixedThreadPool(200);
    private static final int CHUNCK = 1000000;

    private static class ListPrimesRunner implements Callable<List<Integer>> {
        public final PrimeNumberStrategy strategy;
        public final int startRange;
        public final int endRange;

        @SuppressWarnings("unused")
        public ListPrimesRunner(PrimeNumberStrategy strategy, int startRange, int endrange) {
            this.strategy = strategy;
            this.startRange = startRange;
            this.endRange = endrange;
        }

        @Override
        public List<Integer> call() throws Exception {
            return IntStream.range(startRange, endRange).parallel().filter(strategy::isPrime).boxed().collect(Collectors.toList());
        }

    }

    private static class CountPrimesRunner implements Callable<Long> {
        public final PrimeNumberStrategy strategy;
        public final int startRange;
        public final int endRange;

        @SuppressWarnings("unused")
        public CountPrimesRunner(PrimeNumberStrategy strategy, int startRange, int endrange) {
            this.strategy = strategy;
            this.startRange = startRange;
            this.endRange = endrange;
        }

        @Override
        public Long call() throws Exception {
            return IntStream.range(startRange, endRange).parallel().filter(strategy::isPrime).count();
        }
    }

    private static class WritePrimesRunner implements Callable<String> {
        public final PrimeNumberStrategy strategy;
        public final int startRange;
        public final int endRange;
        public final OutputStream outputStream;
        public final ByteArrayOutputStream test;

        @SuppressWarnings("unused")
        public WritePrimesRunner(PrimeNumberStrategy strategy, int startRange, int endrange, OutputStream outputStream, ByteArrayOutputStream test) {
            this.strategy = strategy;
            this.startRange = startRange;
            this.endRange = endrange;
            this.outputStream = outputStream;
            this.test = test;
        }

        @Override
        public String call() throws Exception {
            IntStream.range(startRange, endRange).parallel().filter(strategy::isPrime).sorted().collect(ByteArrayOutputStream::new, (s, i) -> {
                try {
                    synchronized (test) {
                        if (test.toByteArray().length > 0) {
                            outputStream.write(("," + String.valueOf(i)).getBytes());
                        } else {
                            outputStream.write(String.valueOf(i).getBytes());
                            test.write("got something out".getBytes());
                        }
                        outputStream.flush();
                    }
                } catch (Exception e) {
                    logger.error(String.format("can't write %s to stream", i), e);
                }
            }, (s1, s2) -> {
            });
            return "ok";
        }
    }

    @Override
    public List<Integer> listPrimes(int maxRange) {
        if (maxRange <= CHUNCK) {
            return super.listPrimes(maxRange);
        }

        try {
            List<Integer> results = new ArrayList<>();
            results.addAll(invokeAll(createCallables(maxRange, ListPrimesRunner.class.getConstructor(PrimeNumberStrategy.class, int.class, int.class)))
                    .collect(HashSet::new, HashSet::addAll, HashSet::addAll));
            return results;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long countPrimes(int maxRange) {
        if (maxRange <= CHUNCK) {
            return super.countPrimes(maxRange);
        }
        try {
            return invokeAll(createCallables(maxRange, CountPrimesRunner.class.getConstructor(PrimeNumberStrategy.class, int.class, int.class)))
                    .collect(Collectors.summingLong(e -> e));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writePrimes(int maxRange, OutputStream outputStream) throws IOException {
        if (maxRange <= CHUNCK) {
            super.writePrimes(maxRange, outputStream);
            return;
        }
        try {
            ByteArrayOutputStream test = new ByteArrayOutputStream();
            invokeAll(createCallables(maxRange,
                    WritePrimesRunner.class.getConstructor(PrimeNumberStrategy.class, int.class, int.class, OutputStream.class, ByteArrayOutputStream.class),
                    outputStream, test));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> List<Callable<T>> createCallables(int maxRange, Constructor<? extends Callable<T>> constructor, OutputStream... output) {
        List<Callable<T>> callables = new ArrayList<>();
        int threads = maxRange / CHUNCK;
        IntStream.rangeClosed(0, threads).forEach(i -> {
            if (i * CHUNCK <= maxRange) {
                try {
                    List<Object> args = new ArrayList<>();
                    args.add(this);
                    args.add(i * CHUNCK);
                    if (CHUNCK * (1 + i) < maxRange) {
                        args.add(CHUNCK * (1 + i));
                    } else {
                        args.add(maxRange);
                    }
                    if (output.length > 0) {
                        args.addAll(Arrays.asList(output));
                    }
                    callables.add((Callable<T>) constructor.newInstance(args.toArray(new Object[0])));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return callables;
    }

    private <T> Stream<T> invokeAll(List<? extends Callable<T>> callables) throws InterruptedException {
        return executorService.invokeAll(callables).stream().parallel().map(future -> {
            try {
                return future.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
