package com.http.library

import java.io.InputStream

/**
 * Copyright (c), 2018-2019
 * @author: lixin
 * Date: 2019/3/21
 * Description: 请求回调
 */
interface CallbackListener {

    fun onSuccess(inputStream: InputStream)

    fun onFailure()
}