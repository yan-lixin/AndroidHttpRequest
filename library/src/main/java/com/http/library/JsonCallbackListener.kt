package com.http.library

import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

/**
 * Copyright (c), 2018-2019
 * @author: lixin
 * Date: 2019/3/21
 * Description: 主线程回调
 */
class JsonCallbackListener<M>(private var responseClass: Class<M>, private var iJsonDataListener: IJsonDataListener<M>): CallbackListener {

    private val handler = Handler(Looper.getMainLooper())

    override fun onSuccess(inputStream: InputStream) {
        val response = getContent(inputStream)
        val clazz = Gson().fromJson<M>(response, responseClass)
        handler.post {
            iJsonDataListener.onSuccess(clazz)
        }
    }

    override fun onFailure() {
        handler.post {
            iJsonDataListener.onFailure()
        }
    }

    private fun getContent(inputStream: InputStream): String? {
        try {
            val reader = BufferedReader(InputStreamReader(inputStream))
            val sb = StringBuilder()
            var line: String? = null
            try {
                do {
                    line = reader.readLine()
                    if (line == null) {
                        break
                    }
                    sb.append(line + "\n")
                } while (true)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                inputStream.close()
            }
            return sb.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}