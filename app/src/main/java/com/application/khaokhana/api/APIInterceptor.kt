package com.application.khaokhana.api

import android.text.TextUtils
import android.util.Log
import com.application.khaokhana.utils.AppConstant
import com.application.khaokhana.utils.AppUtil
import com.application.khaokhana.utils.PreferenceKeeper
import com.application.khaokhana.BuildConfig


import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class APIInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = PreferenceKeeper.getInstance().accessToken
        Log.i("MMMMMMMMM", "ACCESS TOKEN $token")
        val originalRequest = chain.request()
        if (TextUtils.isEmpty(token)) {
            val builder = originalRequest.newBuilder()
            val oldReq = builder
                .addHeader("authorization", BuildConfig.BASE_AUTH)
                .addHeader("osVersion", AppUtil.getDeviceOS())
                .addHeader("appVersion", BuildConfig.VERSION_NAME)
                .addHeader("deviceType", "${AppConstant.DEVICE_TYPE}")
                .build()
            return chain.proceed(oldReq)
        }
        val builder = originalRequest.newBuilder()
        val oldReq = builder
            .addHeader("accessToken", token!!)
            .addHeader("authorization", BuildConfig.BASE_AUTH)
            .addHeader("osVersion", AppUtil.getDeviceOS())
            .addHeader("appVersion", BuildConfig.VERSION_NAME)
            .addHeader("deviceType", "${AppConstant.DEVICE_TYPE}")
            .build()
        return chain.proceed(oldReq)
    }
}