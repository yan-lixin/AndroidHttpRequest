package com.http.sample.http

import com.http.library.*

/**
 * Copyright (c), 2018-2019
 * @author: lixin
 * Date: 2019/3/21
 * Description:
 */
object HttpRequest {
    fun <M> sendJsonRequest(requestData: Map<String, String>?, url: String, response: Class<M>, listener: IJsonDataListener<M>) {
        val httpRequest = JsonHttpRequest()
        val callbackListener = JsonCallbackListener(response, listener)
        val httpTask = HttpTask(requestData, url, httpRequest, callbackListener)
        ThreadPoolManager.getInstance().addTask(httpTask)
    }
}