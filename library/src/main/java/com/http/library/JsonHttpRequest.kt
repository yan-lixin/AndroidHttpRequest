package com.http.library

import java.io.BufferedOutputStream
import java.lang.RuntimeException
import java.net.HttpURLConnection
import java.net.URL

/**
 * Copyright (c), 2018-2019
 * @author: lixin
 * Date: 2019/3/21
 * Description:
 */
class JsonHttpRequest: IHttpRequest {

    private var mUrl: String? = null
    private var mData: ByteArray? = null
    private var mCallbackListener: CallbackListener? = null
    private var mUrlConnection: HttpURLConnection? = null

    override fun setUrl(url: String) {
        this.mUrl = url
    }

    override fun setData(data: ByteArray) {
        this.mData = data
    }

    override fun setListener(callbackListener: CallbackListener) {
        this.mCallbackListener = callbackListener
    }

    override fun execute() {
        var url: URL? = null
        try {
            url = URL(mUrl)
            mUrlConnection = url.openConnection() as HttpURLConnection //打开http链接
            mUrlConnection!!.apply {
                connectTimeout = 6000 //连接超时时间
                useCaches = false //不使用缓存
                instanceFollowRedirects = true //连接是否可以被重定向
                readTimeout = 3000 //响应超时时间
                doInput = true //设置这个连接是否可以写入数据
                doOutput = true //设置这个连接是否可以输出数据
                requestMethod = "POST" //设置请求方式
                setRequestProperty("Content-Type", "application/json;charset=UTF-8") //设置消息类型
                connect() //与服务器建立TCP连接
            }

            //---使用字节流发送数据---
            val outPutStream = mUrlConnection?.outputStream
            val bos = BufferedOutputStream(outPutStream) //缓冲字节流包装字节流
            bos.write(mData)
            bos.flush()
            outPutStream?.close()
            bos.close()
            //---字符流写入数据---
            if (mUrlConnection?.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = mUrlConnection?.inputStream
                mCallbackListener?.onSuccess(inputStream!!)
            } else {
                throw RuntimeException("请求失败")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("请求失败")
        } finally {
            mUrlConnection?.disconnect()
        }

    }
}