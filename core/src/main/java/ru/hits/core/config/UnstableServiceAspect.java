package ru.hits.core.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Aspect
@Component
@Slf4j
public class UnstableServiceAspect {

    @Around("execution(* ru.hits.core.service..*(..))")
    public Object aroundServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        if (shouldFail()) {
            log.error("Service error simulated");
            throw new RuntimeException("Service unavailable");
        }

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            log.info("Method {} executed in {} ms", joinPoint.getSignature(), duration);
            return result;
        } catch (Exception e) {
            log.error("Method {} failed with exception: {}", joinPoint.getSignature(), e.getMessage());
            throw e;
        }
    }

    private boolean shouldFail() {
        int minute = LocalDateTime.now().getMinute();
        int errorPercentage = minute % 2 == 0 ? 90 : 50;
        return ThreadLocalRandom.current().nextInt(100) < errorPercentage;
    }
}