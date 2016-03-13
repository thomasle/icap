/**
 * 
 */
package com.icap.prime.number.strategy.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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
 */
public class MultithreadedPrimeNumberWithSqrtFunctionCalculator extends PrimeNumberWithSqrtFunctionCalculator {
    private ExecutorService executorService = Executors.newFixedThreadPool(50);
    private static final int CHUNCK = 10000000;

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
            return IntStream.rangeClosed(startRange, endRange).parallel().filter(strategy::isPrime).count();
        }
    }

    private static class WritePrimesRunner implements Callable<ByteArrayOutputStream> {
        public final PrimeNumberStrategy strategy;
        public final int startRange;
        public final int endRange;

        @SuppressWarnings("unused")
        public WritePrimesRunner(PrimeNumberStrategy strategy, int startRange, int endrange) {
            this.strategy = strategy;
            this.startRange = startRange;
            this.endRange = endrange;
        }

        @Override
        public ByteArrayOutputStream call() throws Exception {
            return IntStream.rangeClosed(startRange, endRange).parallel().filter(strategy::isPrime).sorted().collect(ByteArrayOutputStream::new,
                    PrimeNumberWithSqrtFunctionCalculator.accumulator, PrimeNumberWithSqrtFunctionCalculator.combiner);
        }

    }

    @Override
    public List<Integer> listPrimes(int maxRange) {
        if (maxRange <= CHUNCK) {
            return super.listPrimes(maxRange);
        }

        try {
            List<Integer> results = new ArrayList<>();
            results.addAll(invokeAll(createCallables(maxRange, ListPrimesRunner.class)).collect(HashSet::new, HashSet::addAll, HashSet::addAll));
            return results;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long countPrime(int maxRange) {
        if (maxRange <= CHUNCK) {
            return super.countPrime(maxRange);
        }
        try {
            return invokeAll(createCallables(maxRange, CountPrimesRunner.class)).collect(Collectors.summingLong(e -> e));
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
            ByteArrayOutputStream baos = invokeAll(createCallables(maxRange, WritePrimesRunner.class)).collect(ByteArrayOutputStream::new, combiner, combiner);
            baos.writeTo(outputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> List<Callable<T>> createCallables(int maxRange, Class<? extends Callable<T>> primeRunner) {
        List<Callable<T>> callables = new ArrayList<>();
        int threads = maxRange / CHUNCK;
        IntStream.rangeClosed(0, threads).forEach(i -> {
            if (i * CHUNCK < maxRange) {
                Callable<T> callable;
                try {
                    if (CHUNCK * (1 + i) < maxRange) {
                        callable = (Callable<T>) primeRunner.getConstructor(PrimeNumberStrategy.class, int.class, int.class).newInstance(this, i * CHUNCK,
                                CHUNCK * (1 + i));
                    } else {
                        callable = (Callable<T>) primeRunner.getConstructor(PrimeNumberStrategy.class, int.class, int.class).newInstance(this, i * CHUNCK,
                                maxRange);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                callables.add(callable);
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
