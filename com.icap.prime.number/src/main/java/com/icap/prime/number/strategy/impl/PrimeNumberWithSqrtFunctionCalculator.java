/**
 * 
 */
package com.icap.prime.number.strategy.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.ObjIntConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.icap.prime.number.strategy.PrimeNumberStrategy;

/**
 * @author thomas
 *
 */
public class PrimeNumberWithSqrtFunctionCalculator implements PrimeNumberStrategy {
    private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PrimeNumberWithSqrtFunctionCalculator.class);

    protected static ObjIntConsumer<ByteArrayOutputStream> accumulator = (s, i) -> {
        try {
            if (s.toByteArray().length > 0) {
                s.write(",".getBytes());
            }
            s.write(String.valueOf(i).getBytes());
        } catch (Exception e) {
            logger.error(String.format("can't write %s to stream", i), e);
        }
    };

    protected static BiConsumer<ByteArrayOutputStream, ByteArrayOutputStream> combiner = (s1, s2) -> {
        try {
            if (s1.toByteArray().length > 0) {
                s1.write(",".getBytes());
            }
            s1.write(s2.toByteArray());
        } catch (Exception e) {
            logger.error(String.format("can't merge %s and %s to stream", s1, s2), e);
        }
    };

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
        ByteArrayOutputStream baos = IntStream.rangeClosed(2, maxRange).parallel().filter(this::isPrime).sorted().collect(ByteArrayOutputStream::new,
                accumulator, combiner);
        baos.writeTo(outputStream);
    }

    @Override
    public boolean isPrime(int n) {
        return n > 1 && IntStream.rangeClosed(2, BigDecimal.valueOf(Math.sqrt(n)).intValue()).noneMatch(divisor -> n % divisor == 0);
    }

}
