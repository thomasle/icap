package com.icap.prime.number.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

/**
 * 
 * @author thomas
 *
 */
@Aspect
@Service("service.exception.logger")
public class ServiceExceptionLogger {

    /**
     * 
     */
    public ServiceExceptionLogger() {
        super();
    }

    @AfterThrowing(pointcut = "execution(public * com.icap..service.*.*(..))", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        logger.error(exception.getMessage(), exception);
    }
}
