/**
 * 
 */
package com.icap.prime.number.strategy.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.Test;

import com.icap.prime.number.strategy.PrimeNumberStrategy;

/**
 * @author thomas
 *
 */
public abstract class AbstractPrimeNumberStrategyTest {

    PrimeNumberStrategy strategy;

    @Test
    public void testPrime30031() {
        assertFalse(strategy.isPrime(30031));
    }

    @Test
    public void testPrime17() {
        assertTrue(strategy.isPrime(17));
    }

    @Test
    public void testFindPrimes20() {
        assertEquals(Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19), strategy.listPrimes(20));
    }

    @Test
    public void testFindPrimesWithStream20() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        strategy.writePrimes(20, baos);
        assertEquals(Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19), Arrays.stream(new String(baos.toByteArray()).split(",")).mapToInt(Integer::valueOf).boxed().collect(Collectors.toList()));
    }
}
