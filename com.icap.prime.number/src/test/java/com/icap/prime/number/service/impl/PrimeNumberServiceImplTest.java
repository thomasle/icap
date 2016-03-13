/**
 * 
 */
package com.icap.prime.number.service.impl;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.icap.prime.number.Application;
import com.icap.prime.number.service.PrimeNumberService;

/**
 * @author thomas
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Application.class })
public class PrimeNumberServiceImplTest {
	@Inject
	private PrimeNumberService service;

	@Test
	public void testFindPrimes20() {
		assertEquals(Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19), service.findPrimes(20));
	}

	@Test
	public void testFindPrimes120() {
		assertEquals(Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79,
				83, 89, 97, 101, 103, 107, 109, 113), service.findPrimes(120));
	}

	@Test
	public void testFindNotPrimes30031() {
		assertEquals(3248, service.findPrimes(30030).size());
		assertEquals(3248, service.findPrimes(30032).size());
	}

	@Test
	public void testFindPrimes1M() {
		// size found on internet https://primes.utm.edu/howmany.html
		assertEquals(78498, service.findPrimes(1000000).size());
	}

	@Test
	public void testFindPrimes10M() throws IOException {
		// size found on internet https://primes.utm.edu/howmany.html
		testWithOutputStream(10000000, 664579);
	}

	@Test(expected = UnsupportedOperationException.class)
	// this test is getting long around 2.5min
	public void testFindPrimes100M() throws IOException {
		// size found on internet https://primes.utm.edu/howmany.html
		// testWithOutputStream(100000000, 5761455);
		assertEquals(5761455, service.findPrimes(100000000).size());

	}

	@Test(expected = UnsupportedOperationException.class)
	// way too long
	public void testFindPrimesMaxInteger() throws IOException {
		// TODO find size and change the value
		testWithOutputStream(Integer.MAX_VALUE, 50847534);
	}

	private void testWithOutputStream(int maxRange, int expectedLength) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		service.findPrimes(maxRange, baos);
		assertEquals(expectedLength, new String(baos.toByteArray()).split(",").length);
	}
}
