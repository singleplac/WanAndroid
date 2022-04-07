package com.example.demo.utils

import android.util.Log

class LogUtil {
    //双重检测锁

    private constructor() {}
    private constructor(args: Int) {

    }


    companion object {
        val instance: LogUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            LogUtil()
        }
        @Volatile
        private var mInstance: LogUtil? = null

        fun getInstance(args: Int) =
            mInstance ?: synchronized(this) {  mInstance ?: LogUtil(args).also { mInstance = it }
            }
    }

    /**双重检测锁，可带
    class LogUtil {

        private constructor(args: Int) {

        }

        companion object {
            @Volatile
            private var instance: LogUtil? = null

            fun getInstance(args: Int) =
                instance ?: synchronized(this) {
                    instance ?: LogUtil(args).also { instance = it }
                }
        }
    }
    */

    @Synchronized
    fun addTrace(tag: String,msg: String) {
        var sb = StringBuilder()
        sb.append(tag).append(msg).append("\n")
        try {

            val stackTrace = Thread.currentThread().stackTrace

            for (stackTraceElement in stackTrace) {
                 sb.append(stackTraceElement.className).append(".")
                    .append(stackTraceElement.methodName).append("(")
                    .append(stackTraceElement.fileName).append(":")
                    .append(stackTraceElement.lineNumber).append(")\n")
            }
//            sb.append(tag).append("========================WanAndroidLog========================").append(msg)
            Log.d("=====WanAndroidLog===",sb.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    @Synchronized
    fun addSth(tag: String, msg:String) {
        var sb = StringBuilder().append("\n")
        sb.append(tag).append(msg)
        Log.i("=====WanAndroidLog===",sb.toString())
    }
}