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
    private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MultithreadedPrimeNumberWithSqrtFunctionCalculatorTest.class);

    @Before
    public void setUp() {
        strategy = new MultithreadedPrimeNumberWithSqrtFunctionCalculator();
    }

    @Test
    @Ignore
    // take about 15 s
    public void testCountPrimeFor30M() throws IOException {
        assertEquals(1857859, strategy.countPrimes(30000000));
    }

    @Test
    @Ignore
    // take about 15 s
    public void testPrimeFor30MWithStream() throws IOException {
        testWithOutputStreamBySize(30000000, 1857859);
    }

    @Test
    @Ignore
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
    // still too long
    public void testPrimeForMaxInteger() throws IOException {
        // TODO find size and change the value
        // assertEquals(5761455, strategy.listPrimes(Integer.MAX_VALUE).size());
        logger.info("" + Integer.MAX_VALUE);
        testWithOutputStreamBySize(Integer.MAX_VALUE, 5761455);
    }

}
