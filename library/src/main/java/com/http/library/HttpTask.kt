package com.http.library

import com.google.gson.Gson
import java.util.concurrent.Delayed
import java.util.concurrent.TimeUnit

/**
 * Copyright (c), 2018-2019
 * @author: lixin
 * Date: 2019/3/21
 * Description: 异步请求任务
 */
class HttpTask(requestData: Map<String, String>?, url: String, private var httpRequest: IHttpRequest, callbackListener: CallbackListener) : Runnable, Delayed{

    //超时时间
    var delayTime: Long = 0
        set(delayTime) {
            field = System.currentTimeMillis() + delayTime
        }

    //重试次数
    var retryCount: Int = 0

    init {
        httpRequest.setUrl(url)
        httpRequest.setListener(callbackListener)
        val content = Gson().toJson(requestData)
        httpRequest.setData(content.toByteArray())
    }

    override fun run() {
        try {
            httpRequest.execute()
        } catch (e: Exception) {
            ThreadPoolManager.getInstance().addDelayTask(this)
        }
    }

    override fun compareTo(other: Delayed?): Int {
        return 0
    }

    override fun getDelay(unit: TimeUnit?): Long {
        return unit?.convert(delayTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS)!!
    }
}