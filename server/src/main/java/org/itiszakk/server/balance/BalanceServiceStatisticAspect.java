package org.itiszakk.server.balance;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.LongAdder;

@Aspect
@Component
public class BalanceServiceStatisticAspect {

    private final static String USAGE_DELAY_STRING = "PT10S";

    private final static Logger logger = LoggerFactory.getLogger(BalanceServiceStatisticAspect.class);

    private final LongAdder getBalanceCalls = new LongAdder();
    private final LongAdder changeBalanceCalls = new LongAdder();
    private final LongAdder overallCalls = new LongAdder();

    @Before("execution(* org.itiszakk.server.balance.BalanceServiceImpl.getBalance(..))")
    public void logGetBalanceRequest(JoinPoint joinPoint) {
        getBalanceCalls.increment();
        overallCalls.increment();
    }

    @Before("execution(* org.itiszakk.server.balance.BalanceServiceImpl.changeBalance(..))")
    public void logChangeBalanceRequest(JoinPoint joinPoint) {
        changeBalanceCalls.increment();
        overallCalls.increment();
    }

    @Scheduled(initialDelayString = USAGE_DELAY_STRING, fixedDelayString = USAGE_DELAY_STRING)
    public void logUsage() {
        logger.info("Method getBalance calls: {}", getBalanceCalls.sumThenReset());
        logger.info("Method changeBalance calls: {}", changeBalanceCalls.sumThenReset());
        logger.info("Overall calls: {}", overallCalls.sumThenReset());
    }
}
