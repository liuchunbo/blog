package com.example.guava.concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

//令牌桶
public class TokenBucketLimiter {
    //桶的容量
    private static long capacity = 10;
    //放入令牌的速率,每秒2个
    private static long rate = 2;
    //上次放置令牌的时间
    private static long lastTime = System.currentTimeMillis();
    //桶中令牌的余量
    private static AtomicLong tokenNum = new AtomicLong();

    /**
     * true 代表放行，请求可已通过
     * false 代表限制，不让请求通过
     */
    public synchronized static boolean tryAcquire() {
        //更新桶中剩余令牌的数量
        long now = System.currentTimeMillis();
        tokenNum.addAndGet((now - lastTime) / 1000 * rate);
        tokenNum.set(Math.min(capacity, tokenNum.get()));
        //更新时间
        lastTime += (now - lastTime) / 1000 * 1000;
        //桶中还有令牌就放行
        if (tokenNum.get() > 0) {
            tokenNum.decrementAndGet();
            return true;
        } else {
            return false;
        }
    }


    //测试
    public static void main(String[] args) {

        //线程池，用于多线程模拟测试
        ExecutorService pool = Executors.newFixedThreadPool(10);
        // 被限制的次数
        AtomicInteger limited = new AtomicInteger(0);
        // 线程数
        final int threads = 2;
        // 每条线程的执行轮数
        final int turns = 20;
        // 同步器
        CountDownLatch countDownLatch = new CountDownLatch(threads);
        long start = System.currentTimeMillis();
        for (int i = 0; i < threads; i++) {
            pool.submit(() ->
            {
                try {

                    for (int j = 0; j < turns; j++) {

                        boolean flag = tryAcquire();
                        if (!flag) {
                            // 被限制的次数累积
                            limited.getAndIncrement();
                        }
                        Thread.sleep(200);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //等待所有线程结束
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        float time = (System.currentTimeMillis() - start) / 1000F;
        //输出统计结果
        System.out.println("限制的次数为：" + limited.get() +
                ",通过的次数为：" + (threads * turns - limited.get()));
        System.out.println("限制的比例为：" + (float) limited.get() / (float) (threads * turns));
        System.out.println("运行的时长为：" + time + "s");
    }

}
