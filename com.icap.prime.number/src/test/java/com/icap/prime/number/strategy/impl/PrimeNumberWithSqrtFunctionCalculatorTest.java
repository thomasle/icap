/**
 * 
 */
package com.icap.prime.number.strategy.impl;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author thomas
 *
 */
public class PrimeNumberWithSqrtFunctionCalculatorTest extends AbstractPrimeNumberStrategyTest {

    @Before
    public void setUp() {
        strategy = new PrimeNumberWithSqrtFunctionCalculator();
    }

    @Test
    @Ignore
    // take about a minute
    public void testPrimeFor30M() throws IOException {
        testWithOutputStreamBySize(30000000, 1857859);
    }
}
