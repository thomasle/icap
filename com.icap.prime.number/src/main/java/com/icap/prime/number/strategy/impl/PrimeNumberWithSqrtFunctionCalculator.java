/**
 * 
 */
package com.icap.prime.number.strategy.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.icap.prime.number.strategy.PrimeNumberStrategy;

/**
 * @author thomas
 *
 */
public class PrimeNumberWithSqrtFunctionCalculator implements PrimeNumberStrategy {
    private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PrimeNumberWithSqrtFunctionCalculator.class);

    @Override
    public long countPrime(int maxRange) {
        return IntStream.rangeClosed(1, maxRange).parallel().filter(this::isPrime).count();
    }

    @Override
    public List<Integer> listPrimes(int maxRange) {
        return IntStream.rangeClosed(2, maxRange).parallel().filter(this::isPrime).boxed().collect(Collectors.toList());
    }

    @Override
    public void writePrimes(int maxRange, OutputStream outputStream) throws IOException {
        ByteArrayOutputStream test = new ByteArrayOutputStream();
        IntStream.rangeClosed(2, maxRange).parallel().filter(this::isPrime).sorted().collect(ByteArrayOutputStream::new, (s, i) -> {
            try {
                synchronized (test) {
                    if (test.toByteArray().length > 0) {
                        outputStream.write(("," + String.valueOf(i)).getBytes());
                    } else {
                        outputStream.write(String.valueOf(i).getBytes());
                        test.write("got something out".getBytes());
                    }
                    outputStream.flush();
                }
            } catch (Exception e) {
                logger.error(String.format("can't write %s to stream", i), e);
            }
        }, (s1, s2) -> {
        });
    }

    @Override
    public boolean isPrime(int n) {
        return n > 1 && IntStream.rangeClosed(2, BigDecimal.valueOf(Math.sqrt(n)).intValue()).noneMatch(divisor -> n % divisor == 0);
    }

}
