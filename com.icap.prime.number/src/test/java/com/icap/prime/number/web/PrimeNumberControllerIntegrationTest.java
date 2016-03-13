/**
 * 
 */
package com.icap.prime.number.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.icap.prime.number.Application;

/**
 * @author thomas
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebIntegrationTest(randomPort = true)
public class PrimeNumberControllerIntegrationTest {
    private RestTemplate template = null;

    @Value("${local.server.port}")
    private int port;

    private static final String MAX_RANGE_STR = "maxRange";
    private static final String URL = "http://localhost:%s/{maxRange}";
    private static final String STREAMED_URL = "http://localhost:%s/stream/{maxRange}";
    private static int MAX_RANGE_FOR_REDUCE_STRATEGY = 120;
    private static int MAX_RANGE_FOR_SQRT_STRATEGY = 1200;

    @Before
    public void setUp() {
        template = new TestRestTemplate();
    }

    @Test
    public void testReduceStrategy() {
        query(URL, MAX_RANGE_FOR_REDUCE_STRATEGY);
    }

    @Test
    public void testReduceStrategyWithStream() {
        query(STREAMED_URL, MAX_RANGE_FOR_REDUCE_STRATEGY);
    }

    @Test
    public void testSqrtStrategy() {
        query(URL, MAX_RANGE_FOR_SQRT_STRATEGY);
    }

    @Test
    public void testSqrtStrategyWithStream() {
        query(STREAMED_URL, MAX_RANGE_FOR_SQRT_STRATEGY);
    }

    @SuppressWarnings("unchecked")
    private void query(String url, int maxRange) {
        Map<String, Object> result = template.getForObject(String.format(url, port), Map.class, maxRange);
        assertNotNull(result);
        assertEquals(maxRange, result.get(MAX_RANGE_STR));
    }
}
