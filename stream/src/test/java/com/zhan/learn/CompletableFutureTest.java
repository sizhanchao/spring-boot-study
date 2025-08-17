package com.zhan.learn;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class CompletableFutureTest {

    @Test
    public void testCompletableFuture() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        // 异步起线程执行业务 无返回值
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
        }, executor);


        //异步起线程执行业务 有返回值
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 0;
            System.out.println("运行结果：" + i);
            return i;
        }, executor).whenComplete((res,exc)->{
            // 可以接收到返回值和异常类型，但是无法处理异常
            System.out.println("异步任务成功完成了...结果是：" + res + ";异常是：" + exc);
        }).exceptionally(throwable -> {
            // 处理异常，返回一个自定义的值，和上边返回值无关。
            return 10;
        });

        //方法执行完成后的处理
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 0;
            System.out.println("运行结果：" + i);
            return i;
        }, executor).handle((res,thr)->{
            // 无论线程是否正确执行，都会执行这里，可以对返回值进行操作。
            if(res != null){
                return res * 2;
            }
            if(thr != null){
                return 0;
            }
            return 0;
        });

    }

}
