package com.nyc.schools.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class NetworkInterceptor : Interceptor {
    private val TAG = "NetworkInterceptor"

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        Log.d(TAG, "Request URL " + request.url)
        return chain.proceed(request)
    }

}