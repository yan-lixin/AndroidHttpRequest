package com.http.library

import android.util.Log
import java.util.concurrent.*

/**
 * Copyright (c), 2018-2019
 * @author: lixin
 * Date: 2019/3/21
 * Description: 线程池管理
 */
class ThreadPoolManager private constructor(){
    private val TAG = ThreadPoolManager::class.java.simpleName

    companion object {
        private var ourInstance: ThreadPoolManager? = null
            get() {
                if (field == null) {
                    field = ThreadPoolManager()
                }
                return field
            }

        fun getInstance(): ThreadPoolManager {
            return ourInstance!!
        }
    }

    //创建队列、用于保存异步请求任务、指定泛型为子线程
    private val mQueue = LinkedBlockingDeque<Runnable>()

    //创建延迟队列
    private val mDelayQueue: DelayQueue<HttpTask> = DelayQueue()

    //创建线程池
    private var mThreadPoolExecutor: ThreadPoolExecutor? = null

    /**
     * 队列与线程池的通信线程
     */
    private val communicateThread = Runnable {
        var runnable: Runnable? = null
        while (true) {
            runnable = mQueue.take()
            mThreadPoolExecutor?.execute(runnable)
        }
    }

    private val delayThread = Runnable {
        var httpTask: HttpTask? = null
        while (true) {
            try {
                httpTask = mDelayQueue.take()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (httpTask?.retryCount!! < 3) {
                mThreadPoolExecutor?.execute(httpTask)
                httpTask.retryCount = httpTask.retryCount + 1
                Log.e(TAG, "重试current>> ${httpTask.retryCount}")
            } else {
                Log.e(TAG, "重试次数超限")
            }
        }
    }

    init {
        mThreadPoolExecutor = ThreadPoolExecutor(3, 10, 15, TimeUnit.SECONDS, ArrayBlockingQueue<Runnable>(4),
            RejectedExecutionHandler { r, _ ->
                addTask(r)
            })
        mThreadPoolExecutor?.execute(communicateThread)
        mThreadPoolExecutor?.execute(delayThread)
    }

    /**
     * 添加异步任务到队列
     */
    fun addTask(runnable: Runnable?) {
        if (runnable != null) {
            mQueue.put(runnable)
        }
    }

    fun addDelayTask(httpTask: HttpTask?) {
        if (httpTask != null) {
            httpTask.delayTime = 3000
            mDelayQueue.offer(httpTask)
        }
    }
}
