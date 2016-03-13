/**
 * 
 */
package com.icap.prime.number.strategy.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author thomas
 *
 */
public class MultithreadedPrimeNumberWithSqrtFunctionCalculatorTest extends AbstractPrimeNumberStrategyTest {

    @Before
    public void setUp() {
        strategy = new MultithreadedPrimeNumberWithSqrtFunctionCalculator();
    }

    @Test
    // take about 15 s
    public void testCountPrimeFor30M() throws IOException {
        assertEquals(1857859, strategy.countPrime(30000000));
    }

    @Test
    // take about 15 s
    public void testPrimeFor30M() throws IOException {
        assertEquals(1857859, strategy.listPrimes(30000000).size());
    }

    @Test
    @Ignore
    // take about 80 s
    public void testPrimeFor100M() throws IOException {
        assertEquals(5761455, strategy.listPrimes(100000000).size());
    }

    @Test
    @Ignore
    public void testPrimeForMaxInteger() throws IOException {
        assertEquals(5761455, strategy.listPrimes(Integer.MAX_VALUE).size());
    }

}