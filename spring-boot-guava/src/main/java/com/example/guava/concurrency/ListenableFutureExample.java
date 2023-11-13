package com.example.guava.concurrency;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.sun.istack.internal.Nullable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ListenableFutureExample {

    static ExecutorService service = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // futureAndGet();
        // listenableFuture();
        completableFuture();
    }

    /**
     * Java8 的 CompletableFuture 更加简洁方便
     */
    public static void completableFuture() {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 100;
        }, service);
        future.whenComplete((result, cause) -> {
            System.out.println("I am finished and the result is " + result);
        });
    }

    /**
     * Guava ListenableFuture 类 函数回调
     */
    public static void listenableFuture() {
        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(service);
        ListenableFuture<Integer> future = listeningExecutorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 100;
        });
        // future1.addListener(() -> System.out.println("I am finished"), service);
        // 回调函数
        Futures.addCallback(future, new MyCallBack(), service);
    }

    static class MyCallBack implements FutureCallback<Integer> {
        @Override
        public void onSuccess(@Nullable Integer result) {
            System.out.println("I am finished and the result is " + result);
        }

        @Override
        public void onFailure(Throwable throwable) {
        }
    }


    /**
     * Future 类 通过get阻塞方法，获取异步记过
     */
    public static void futureAndGet() throws ExecutionException, InterruptedException {
        Future<?> future = service.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });
        // 阻塞的方式获取并处理
        Object result = future.get();
        System.out.println(result);
    }
}
