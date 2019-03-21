package com.http.library

/**
 * Copyright (c), 2018-2019
 * @author: lixin
 * Date: 2019/3/21
 * Description:
 */
interface IJsonDataListener<M> {

    fun onSuccess(m: M)

    fun onFailure()
}