/**
 * 
 */
package com.icap.prime.number.strategy;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author thomas
 *
 */
public interface PrimeNumberStrategy {
    /**
     * determine whether an integer is a prime number
     * 
     * @param n
     *            the integer to evaluate
     * @return true if n is a prime number
     */
    boolean isPrime(int n);

    /**
     * count the number of prime available up to the max range
     * 
     * @param maxRange
     * @return
     */
    long countPrimes(int maxRange);

    /**
     * list all prime numbers up to the max range
     * 
     * @param maxRange
     * @return a list of prime numbers.
     */
    List<Integer> listPrimes(int maxRange);

    /**
     * write list of prime numbers to the output stream This should be helping
     * by not storing the prime numbers in variable but write directly to the
     * output stream.
     * 
     * @param maxRange
     * @param outputStream
     * @throws IOException
     */
    void writePrimes(int maxRange, OutputStream outputStream) throws IOException;
}
