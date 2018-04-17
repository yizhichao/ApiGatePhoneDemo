package com.hik.apigatephonedemo.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池类
 * Created by xiadaidai on 2016/3/24.
 */
public class ThreadPoolUtil {

    /**
     * 线程池中的核心线程数量
     */
    private final static int CORE_POOL_SIZE = 15;

    /**
     * 线程池中的最大线程数量
     */
    private final static int MAXIMUM_POOL_SIZE = 15;

    private final static long KEEP_ALIVE = 0L;

    /**
     * 线程池对象
     */
    private static ThreadPoolExecutor mThreadPool = null;

    /**
     * @ Description:创建线程池
     * @ param
     * @ author xiadaidai
     * @ date 2016/3/24 17:46
     */
    public static void createThreadPool() {
        if (null == mThreadPool) {
            mThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit
                    .MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        }
    }

    /**
     * @ Description:向线程池中添加任务
     * @ param
     * @ author xiadaidai
     * @ date 2016/3/24 17:19
     */
    public static void submitThreadPoolTask(Runnable runnable) {
        if (null == runnable) {
            return;
        }
        try {
            if (null == mThreadPool) {
                mThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit
                        .MILLISECONDS, new LinkedBlockingQueue<Runnable>());
            }

            if (!mThreadPool.isShutdown()) {
                mThreadPool.execute(runnable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @ Description:销毁线程池
     * @ param
     * @ author xiadaidai
     * @ date 2016/3/24 17:20
     */
    public static void destroyThreadPool() {
        if (null != mThreadPool) {
            mThreadPool.shutdown();
            mThreadPool = null;
        }
    }

}
