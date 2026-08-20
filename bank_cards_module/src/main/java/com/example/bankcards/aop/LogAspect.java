package com.example.bankcards.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class LogAspect {

    @Around("execution(* com.example.bankcards.service.*.*(..))")
    public void logBeforeMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Начинается метод " + joinPoint.getSignature().getName());
        double startTime = System.currentTimeMillis();

        joinPoint.proceed();

        System.out.println(System.currentTimeMillis() - startTime);

    }
}
