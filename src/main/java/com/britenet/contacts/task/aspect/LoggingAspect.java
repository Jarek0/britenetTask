package com.britenet.contacts.task.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(public * *(..))")
    public void allMethods() {}

    @Before("@annotation(LogExecutionTime)")
    public void logBefore(JoinPoint joinPoint){
        log.info("Execution method " + joinPoint.getSignature().getName());
    }

    @After("@annotation(LogExecutionTime)")
    public void logAfter(JoinPoint joinPoint){
        log.info("After method " + joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "@annotation(LogExecutionTime)", returning = "returnValue")
    public void logAfterReturning(Object returnValue){
        log.info("Returned value: " + returnValue);
    }

    @AfterThrowing(pointcut = "@annotation(LogExecutionTime)", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception){
        log.info("Method " + joinPoint.getSignature().getName() + " has thrown " + exception);
    }

    @Around("@annotation(LogExecutionTime)")
    public void logExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        long start = System.currentTimeMillis();
        proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis();
        log.info("Execution time: " + (end - start) + " ms");
    }

}