package com.suprised.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 并发计数器
 * 
 * 使用AtomicInteger 做并发环境下的计数器
 */
public class CasCounter {

    public static void main(String[] args) {
        final Counter counter = new Counter();
        List<Thread> threads = new ArrayList<Thread>(600);
        long start = System.currentTimeMillis();
        for (int i=0; i<100; i++) { // 一百个线程
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j=0; j<10000; j++) { // 每个线程计数1w次
                        counter.count();
                        counter.safeCount();
                    }
                }
            });
            threads.add(thread);
        }
        
        for (Thread thread : threads) {
            thread.start();
        }
        
        for (Thread thread : threads) {
            try {
                thread.join(); // 强制执行完
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("非线程安全计数：" + counter.count);
        System.out.println("线程安全计数：" + counter.atomicInt.get());
        System.out.println(System.currentTimeMillis() - start);
    }
    
}

class Counter {
    public AtomicInteger atomicInt = new AtomicInteger(0); // 实现原子更新
    public int count ;
    
    public void safeCount() {
        while(true) {
            int i = atomicInt.get();
            boolean flag = atomicInt.compareAndSet(i, ++i);
            if (flag) {
                break;
            }
        }
    }
    
    /**
     * 非安全计数器
     */
    public void count() {
        count++;
    }
}