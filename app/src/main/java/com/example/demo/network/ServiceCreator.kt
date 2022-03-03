package com.example.demo.network

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.Log
import com.blankj.utilcode.util.SPUtils
import okhttp3.*
import okhttp3.Interceptor.Chain
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import retrofit2.adapter.rxjava2.Result.response
import java.util.ArrayList
import java.util.HashSet

import java.util.prefs.Preferences

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import android.content.SharedPreferences
import com.blankj.utilcode.util.LogUtils
import kotlin.coroutines.coroutineContext


object ServiceCreator {
    private const val TAG = "OkHttp"
    private const val BASE_URL = "https://www.wanandroid.com"
    private lateinit var retrofitWithCookies: Retrofit

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    fun <T> createWithCookies(serviceClass: Class<T>): T {
        retrofitWithCookies = setCookies(true)
        return retrofitWithCookies.create(serviceClass)
    }

    inline fun <reified T> create(): T = create(T::class.java)

    private fun setCookies(isSaveCookies: Boolean): Retrofit {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(ReceivedCookiesInterceptor( ))
        builder.addInterceptor(AddCookiesInterceptor())
        return Retrofit.Builder()
            .client(builder.build())
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }


    class ReceivedCookiesInterceptor: Interceptor {
        override fun intercept(chain: Chain): Response {
            //The intercepted cookie is saved in the originalResponse
            var originalResponse: Response = chain.proceed(chain.request())
            //Print cookie information
            Log.i(TAG, "intercept: " + originalResponse.headers("Set-Cookie").toString())

            if (!originalResponse.headers("Set-Cookie").isEmpty()) {

                var cookies: HashSet<String> = object : HashSet<String>() {

                }
                for (header in originalResponse.headers("Set-Cookie")) {

                    Log.i(TAG, "intercept cookie is:" + header)
                    cookies.add(header)
                }
                SPUtils.getInstance("cookie").put("cookie", cookies)

            }
            return originalResponse
        }
    }

    //添加cookies
    class AddCookiesInterceptor : Interceptor {
        override fun intercept(chain: Chain): Response {
            val builder = chain.request().newBuilder()
            val preferences: HashSet<String> =
                SPUtils.getInstance("cookie").getStringSet("cookie", null) as HashSet<String>
            if (preferences != null) {
                for (cookie in preferences) {
                    builder.addHeader("Cookie", cookie)
                    Log.i(TAG,"[AddCookiesInterceptor] cookie: $cookie")
                    //保存用户名
                    if (TextUtils.equals("loginUserName", cookie.split("=")[0])) {
                        if (!TextUtils.isEmpty(cookie.split("=")[1])) {
                            val nameTest = cookie.split("=")[1]
                            val name = nameTest.split(";")[0]
                            Log.d(TAG,"[AddCookiesInterceptor] name is $name")
                            SPUtils.getInstance("cookie").put("userName", name)
                        }
                    }
                }
            }
            return chain.proceed(builder.build())
        }

    }

}
