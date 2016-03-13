/**
 * 
 */
package com.icap.prime.number.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author thomas
 *
 */
public interface PrimeNumberService {
	/**
	 * Find all the prime numbers up to maxRange
	 * 
	 * @param maxRange
	 *            Limit to search for the prime numbers
	 * @return list of prime numbers from 2 up to the max range
	 */
	public List<Integer> findPrimes(int maxRange);

	/**
	 * Find all the prime numbers up to maxRange
	 * 
	 * @param maxRange
	 *            Limit to search for the prime numbers
	 * @param outputStream
	 *            write the result in this output stream
	 * @throws IOException
	 */
	public void findPrimes(int maxRange, OutputStream outputStream) throws IOException;
}
