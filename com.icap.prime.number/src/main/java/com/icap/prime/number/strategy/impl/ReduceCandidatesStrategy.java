/**
 * 
 */
package com.icap.prime.number.strategy.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.icap.prime.number.strategy.PrimeNumberStrategy;

/**
 * @author thomas
 *
 *         This implementation identifies all prime numbers from 2 up to a max
 *         range by dividing all candidates with the previously found prime
 *         numbers. When a prime number is found, all related composites are
 *         removed from the list. Because of the way this works, using
 *         {@link PrimeNumberStrategy.writePrimes} would not spare memory.
 *         This implementation is good for small integer < 1M
 */
public class ReduceCandidatesStrategy implements PrimeNumberStrategy {

    @Override
    public List<Integer> listPrimes(int maxRange) {
        try {
            return findPrimesInternal(maxRange, null);
        } catch (IOException e) {
            // should not be happening
            return new ArrayList<>();
        }
    }

    @Override
    public boolean isPrime(int n) {
        return listPrimes(n).contains(Integer.valueOf(n));
    }

    @Override
    public long countPrimes(int maxRange) {
        return listPrimes(maxRange).size();
    }

    @Override
    public void writePrimes(int maxRange, OutputStream outputStream) throws IOException {
        findPrimesInternal(maxRange, outputStream);
    }

    protected void addCandidateToPrimeListIfPrime(List<Integer> primes, int candidate, OutputStream outputStream) throws IOException {
        if (primes.stream().parallel().noneMatch(prime -> candidate % prime.intValue() == 0)) {
            if (outputStream != null) {
                if (primes.size() > 0) {
                    outputStream.write(",".getBytes());
                }
                outputStream.write(String.valueOf(candidate).getBytes());
            }
            primes.add(candidate);
        }
    }

    protected List<Integer> filterOutCandidatesCompositeOfPrime(List<Integer> candidates, int primeNumber) {
        return candidates.stream().parallel().filter(p -> {
            return (p % primeNumber) != 0;
        }).collect(Collectors.toList());
    }

    protected List<Integer> generateCandidates(int maxRange) {
        return IntStream.rangeClosed(2, maxRange).boxed().collect(Collectors.toList());
    }

    private List<Integer> findPrimesInternal(int maxRange, OutputStream outputStream) throws IOException {
        List<Integer> primes = new ArrayList<>();

        List<Integer> candidates = generateCandidates(maxRange);

        while (candidates.size() > 0) {
            int candidate = candidates.remove(0);
            addCandidateToPrimeListIfPrime(primes, candidate, outputStream);
            candidates = filterOutCandidatesCompositeOfPrime(candidates, candidate);
        }

        return primes;
    }

}
