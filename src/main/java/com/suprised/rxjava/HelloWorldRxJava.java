package com.suprised.rxjava;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.Executors;

import com.suprised.utils.Threads;

public class HelloWorldRxJava {
    
    public static void main(String[] args) {

        // 观察者
        Observer<String> observer = new Observer<String>() {

            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("onSubscribe():" + d.toString());
            }

            @Override
            public void onNext(String t) {
                Threads.sleep(1000);
                System.out.println("onNext():" + t);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError()：" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Threads.sleep(1000);
                System.out.println("onComplete();");
            }
        };

        // 被观察者
        Observable<String> observable = Observable.just("suprised", "happness", "liuliu").subscribeOn(
            Schedulers.computation()); // 默认线程数等于处理器的数量

        Observable<String> observableThread = Observable.just("suprised", "happness", "liuliu").subscribeOn(
            Schedulers.from(Executors.newFixedThreadPool(1)));// 单线程执行

        // 多个观察者订阅同一个数据
        
        // 订阅: 观察者和被观察者关联
        observable.subscribe(observer);
        observable.subscribe(observer);
        observable.subscribe(observer);
        observable.subscribe(observer);
        observable.subscribe(observer);
        observable.subscribe(observer);
        observable.subscribe(observer);
        observable.subscribe(observer);

        // 订阅： 使用多线程的方式执行
        observableThread.subscribe(observer);
        observableThread.subscribe(observer);
        observableThread.subscribe(observer);
        observableThread.subscribe(observer);
        observableThread.subscribe(observer);
        observableThread.subscribe(observer);
        observableThread.subscribe(observer);
        observableThread.subscribe(observer);

        System.out.println("process continue.");

        while (true) {
        }
    }
}
