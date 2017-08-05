package com.suprised.thread;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * 
 * 只支持java7
 * 使用fork/join框架
 * 
 * Fork/Join框架是Java提供了的一个用于并行执行任务的框架,
 * 是一个把大任务分割成若干个小任务，最终汇总每个小任务结果得到大任务结果的框架
 */
public class ForkJoinCounter {

    public static void main(String[] args) {
        // 需要通过ForkJoinPool来调用任务
        ForkJoinPool forkjoinPool = new ForkJoinPool();
        CounterTask task = new CounterTask(1, 10000000000l, 100000000l);
        //forkjoinPool.execute(task);
        //forkjoinPool.submit(task);
        
        long start = System.currentTimeMillis();
        // long result = forkjoinPool.invoke(task);
        ForkJoinTask<Long> taskLong = forkjoinPool.submit(task);
        try {
			long resultByForkJoin = taskLong.get();
			System.out.println(resultByForkJoin);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
        // System.out.println(result);
        long sum = 0;
        for (long i=1; i<=10000000000l; i++) {
            sum += i;
        }
        System.out.println(sum);
        System.out.println(System.currentTimeMillis() - start);
        
        ConcurrentHashMap<String, String> threadIds = CounterTask.threadIds;
        System.out.println("ThreadSize: " + threadIds.size());
        for (String id : threadIds.keySet()) {
            System.out.println(id);
        }
    }
    
}

/**
 * 一个连串数相加的任务，例如： 1+2+3+4+。。。+1000
 * 
 * 如果任务有返回值：则继承RecursiveTask，否则继承：RecursiveAction
 */
class CounterTask extends RecursiveTask<Long> {
    
    private static final long serialVersionUID = -173469748627828161L;

    private long count = 10000; //作为一个分割子任务的依据
    
    private long start;
    private long end;
    static ConcurrentHashMap<String, String> threadIds = new ConcurrentHashMap<String, String>();
    
    public CounterTask(long start, long end, long split) {
        this.start = start;
        this.end = end;
        this.count = split;
    }
    
    @Override
    protected Long compute() {
        //System.out.println(Thread.currentThread().getId() + ", " + Thread.currentThread().getName());
        String id = Thread.currentThread().getId() + ", " + Thread.currentThread().getName();
        threadIds.put(id, id);
        long sum = 0;
        boolean con = (end - start) <= count;
        if (con) { // 如果任务很小，则进行计算
            //System.out.println(start + ", " + end);
            for (long i=start; i<=end; i++) {
                sum += i;
            }
        } else { // 否则继续分割
            long middle = (end + start) / 2;
            CounterTask leftTask = new CounterTask(start, middle, count);
            CounterTask rightTask = new CounterTask(middle + 1, end, count);
            
            // 执行子任务
            leftTask.fork(); 
            rightTask.fork();
            
            
            // 等待子任务执行完成后，并得到结果
            long leftResult = leftTask.join();
            long rightResult = rightTask.join();
            
            if (leftTask.isCompletedAbnormally()) { // 如果子任务抛出了异常，则打印出异常
                System.out.println(leftTask.getException().getMessage());
            }
            
            // 合并结果
            sum = leftResult + rightResult;
            //System.out.println("results....");
            // + leftResult3 + rightResult4 + leftResult5 + rightResult6 + leftResult7 + rightResult8 + leftResult9 + rightResult10;
        }
        return sum;
    }
    
}