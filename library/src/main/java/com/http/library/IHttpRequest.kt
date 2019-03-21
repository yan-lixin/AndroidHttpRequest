package com.http.library

/**
 * Copyright (c), 2018-2019
 * @author: lixin
 * Date: 2019/3/21
 * Description: 请求接口
 */
interface IHttpRequest {

    fun setUrl(url: String)

    fun setData(data: ByteArray)

    fun setListener(callbackListener: CallbackListener)

    fun execute()
}