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
    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    fun getLoginUser(): String {
        return SPUtils.getInstance("cookie").getString("userName", "")
    }

    /**
     * 清空登录信息
     */
    fun clearLoginInfo() {
        SPUtils.getInstance("cookie").put("userName", "")
    }

    /**
     * 是否已经登录
     *
     * @return
     */
    fun isLogin(): Boolean {
        return !TextUtils.isEmpty(getLoginUser())
    }
}