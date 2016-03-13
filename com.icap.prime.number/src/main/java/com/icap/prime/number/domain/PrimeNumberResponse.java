/**
 * 
 */
package com.icap.prime.number.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author thomas
 *
 */
public class PrimeNumberResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8631409965111948383L;

	private final int maxRange;
	private final List<Integer> primes;

	public PrimeNumberResponse(int maxRange, List<Integer> primes) {
		this.maxRange = maxRange;
		this.primes = primes;
	}
	
	public int getMaxRange() {
		return maxRange;
	}

	public List<Integer> getPrimes() {
		return primes;
	}

}
