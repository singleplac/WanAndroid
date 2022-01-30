package com.example.demo

import android.app.Activity
import android.app.Application
import android.content.Context
import android.widget.Toast

/**
 * 全局获取Context
 */
class WanAndroidApplication : Application() {
    companion object {
        @SuppressWarnings("StaticFieldLeak") //添加注解，忽视掉"内存泄漏"警告
        lateinit var context: Context

    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }


    //String扩展函数
    fun String.showToast(duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(WanAndroidApplication.context, this, duration).show()
    }

    //Int扩展函数
    fun Int.showToast(duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(WanAndroidApplication.context, this, duration).show()
    }
}