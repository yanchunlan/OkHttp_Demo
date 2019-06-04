package com.example.library;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * author:  ycl
 * date:  2019/06/03 22:09
 * desc:
 */
class ThreadPoolManager {

    private static ThreadPoolManager sThreadPoolManager = null;

    private ThreadPoolExecutor mThreadPoolExecutor = null;

    private LinkedBlockingQueue<Runnable> mQueue = new LinkedBlockingQueue<>();

    private DelayQueue<HttpTask> mDelayQueue = new DelayQueue<>();


    private ThreadPoolManager() {
        mThreadPoolExecutor = new ThreadPoolExecutor(3,
                10,
                15,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(4),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        addTask(r);
                    }
                });

        // 开启循环，从队列中不断取数据
        mThreadPoolExecutor.execute(coreThread);
        mThreadPoolExecutor.execute(delayThread);
    }

    static ThreadPoolManager getInstance() {
        if (sThreadPoolManager == null) {
            synchronized (ThreadPoolManager.class) {
                if (sThreadPoolManager == null) {
                    sThreadPoolManager = new ThreadPoolManager();
                }
            }
        }
        return sThreadPoolManager;
    }

    void addTask(Runnable runnable) {
        if (runnable != null) {
            try {
                mQueue.put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Runnable coreThread = new Runnable() {
        Runnable run = null;

        @Override
        public void run() {
            while (true) {
                try {
                    run = mQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mThreadPoolExecutor.execute(run);
            }
        }
    };


    //  ---------------- 重试机制 start -----------------

    void addDelayTask(HttpTask httpTask) {
        if (httpTask != null) {
            httpTask.setDelayTime(3000);
            mDelayQueue.put(httpTask);
        }
    }

    private Runnable delayThread = new Runnable() {
        HttpTask httpTask = null;

        @Override
        public void run() {
            while (true) {
                try {
                    httpTask = mDelayQueue.take();
                    if (httpTask.getRetryCount() < 3) {
                        mThreadPoolExecutor.execute(httpTask);
                        httpTask.setRetryCount(httpTask.getRetryCount() + 1);
                        System.out.println(" --- 重试机制 ---- retryCount:"+httpTask.getRetryCount()+" currentTime: "+System.currentTimeMillis());
                    } else {
                        System.out.println(" --- 重试机制 ---- 重试超过3次，直接放弃");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


}
