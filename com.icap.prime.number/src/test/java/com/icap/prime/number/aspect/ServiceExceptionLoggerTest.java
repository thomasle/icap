/**
 * 
 */
package com.icap.prime.number.aspect;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.icap.prime.number.Application;
import com.icap.prime.number.service.TestService;

/**
 * @author thomas
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Application.class })
public class ServiceExceptionLoggerTest {
	@Inject
	private TestService service;

	/**
	 * Test method for
	 * {@link com.icap.prime.number.aspect.ServiceExceptionLogger#logException(org.aspectj.lang.JoinPoint, java.lang.Throwable)}
	 * .
	 */
	@Test
	public void testLogException() {
		try {
			service.test();
			fail("should have thrown an exception");
		} catch (Exception e) {
			assertTrue(true);
		}
	}

}
