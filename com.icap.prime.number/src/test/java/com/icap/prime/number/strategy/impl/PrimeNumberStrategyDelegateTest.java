/**
 * 
 */
package com.icap.prime.number.strategy.impl;

import org.junit.Before;

/**
 * @author thomas
 *
 */
public class PrimeNumberStrategyDelegateTest extends AbstractPrimeNumberStrategyTest {

    @Before
    public void setUp() {
        strategy = new PrimeNumberStrategyDelegate();
    }
}
