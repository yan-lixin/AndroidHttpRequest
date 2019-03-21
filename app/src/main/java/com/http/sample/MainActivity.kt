package com.http.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.http.library.IJsonDataListener
import com.http.sample.bean.ResponseBean
import com.http.sample.http.HttpRequest.sendJsonRequest

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName

    private val url = "http://v.juhe.cn/historyWeather/citys?province_id=2&key=bb52107206585ab074f5e59a8c73875b"

//    private val url = "x"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sendJsonRequest(null, url, ResponseBean::class.java, object : IJsonDataListener<ResponseBean> {
            override fun onSuccess(m: ResponseBean) {
                Log.e(TAG, m.toString())
            }

            override fun onFailure() {
                TODO("doErrorSomethings")
            }

        })
    }
}
