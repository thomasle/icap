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
	boolean isPrime(int n);

	long countPrime(int maxRange);

	List<Integer> listPrimes(int maxRange);

	void writePrimes(int maxRange, OutputStream outputStream) throws IOException;
}
