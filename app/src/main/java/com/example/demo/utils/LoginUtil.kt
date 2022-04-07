package com.example.demo.utils

import android.text.TextUtils
import android.util.Log
import com.blankj.utilcode.util.SPUtils
import java.util.HashSet

/**
 * Created with Android Studio.
 * Description: 判断登陆状态
 *
 * @author: ZFC
 * @date: 2022/03/02
 * Time: 13:46
 */
object LoginUtil {

    private const val SETTINGS_SP_KEY: String = "SETTINGS_SP"
    private const val IS_LOGIN_KEY = "IS_LOGIN"
    private const val USERNAME_KEY = "USERNAME"
    private const val AVATAR_URI_STRING_KEY = "AVATAR_URI_STRING"

    fun setIsLogin(isLogin: Boolean) {
//        getSp().edit().putBoolean(IS_LOGIN_KEY, isLogin).apply()
        SPUtils.getInstance(IS_LOGIN_KEY).put(IS_LOGIN_KEY, isLogin)
    }

    fun setUsername(username: String) {
//        getSp().edit().putString(USERNAME_KEY, username).apply()
        SPUtils.getInstance(USERNAME_KEY).put(USERNAME_KEY, username)
    }


    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    fun getLoginUser(): String {
        return SPUtils.getInstance(USERNAME_KEY).getString(USERNAME_KEY, "")
    }

    /**
     * 清空登录信息
     */
    fun clearLoginInfo() {
        SPUtils.getInstance(USERNAME_KEY).put(USERNAME_KEY, "")
    }

    /**
     * 是否已经登录
     *
     * @return
     */
    fun isLogin(): Boolean {
        return SPUtils.getInstance(IS_LOGIN_KEY).getBoolean(IS_LOGIN_KEY)
    }
}