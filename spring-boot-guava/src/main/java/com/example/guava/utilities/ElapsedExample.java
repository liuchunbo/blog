package com.example.guava.utilities;

import com.google.common.base.Stopwatch;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * Creat by liuchunbo 2023/7/2
 */
@Slf4j
public class ElapsedExample {

    public static void main(String[] args) throws InterruptedException {
        ElapsedExample example = new ElapsedExample();
        example.process("55555");
    }

    public void process(String orderNo) throws InterruptedException {
        log.info("start process the order {}", orderNo);
        Stopwatch stopwatch = Stopwatch.createStarted();
        TimeUnit.SECONDS.sleep(1);
        // log.info("The orderNo {} process successful and elapsed {}.", orderNo, stopwatch.stop());
        log.info("The orderNo {} process successful and elapsed {}.", orderNo, stopwatch.stop().elapsed(TimeUnit.MINUTES));
    }

}
