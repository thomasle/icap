/**
 * 
 */
package com.icap.prime.number.strategy.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.icap.prime.number.strategy.PrimeNumberStrategy;

/**
 * @author thomas
 *
 *         This is a delegator for prime number calculator strategy
 */
@Component
public class PrimeNumberStrategyDelegate implements PrimeNumberStrategy {
    private static Map<String, PrimeNumberStrategy> strategies;
    private static final String REDUCE = "reduce";
    private static final String SQRT = "sqrt";
    private static final String MT_SQRT = "mtsqrt";

    static {
        strategies = new HashMap<>();
        strategies.put(REDUCE, new ReduceCandidatesStrategy());
        strategies.put(SQRT, new PrimeNumberWithSqrtFunctionCalculator());
        strategies.put(MT_SQRT, new MultithreadedPrimeNumberWithSqrtFunctionCalculator());
    }

    @Override
    public boolean isPrime(int n) {
        return getStrategy(n).isPrime(n);
    }

    @Override
    public long countPrimes(int maxRange) {
        return getStrategy(maxRange).countPrimes(maxRange);
    }

    @Override
    public List<Integer> listPrimes(int maxRange) {
        return getStrategy(maxRange).listPrimes(maxRange);
    }

    @Override
    public void writePrimes(int maxRange, OutputStream outputStream) throws IOException {
        getStrategy(maxRange).writePrimes(maxRange, outputStream);
    }

    private PrimeNumberStrategy getStrategy(int maxRange) {
        if (maxRange <= 1000) {
            return strategies.get(REDUCE);
        }
        if (maxRange <= 10000000) {
            return strategies.get(SQRT);
        }
        return strategies.get(MT_SQRT);
    }
}
