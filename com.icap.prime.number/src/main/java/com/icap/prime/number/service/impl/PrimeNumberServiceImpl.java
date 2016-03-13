/**
 * 
 */
package com.icap.prime.number.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.icap.prime.number.service.PrimeNumberService;
import com.icap.prime.number.strategy.PrimeNumberStrategy;

/**
 * @author thomas
 *
 */
@Service
public class PrimeNumberServiceImpl implements PrimeNumberService {
	@Inject
	private PrimeNumberStrategy delegate;

	@Override
	public List<Integer> findPrimes(int maxRange) {
		return delegate.listPrimes(maxRange);
	}

	@Override
	public void findPrimes(int maxRange, OutputStream outputStream) throws IOException {
		delegate.writePrimes(maxRange, outputStream);
	}

}
