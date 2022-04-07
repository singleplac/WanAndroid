package com.example.demo.ui.base

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

abstract class BaseActivity : AppCompatActivity() {

    /**
     * 当前Activity渲染的视图View
     */
    private var mContextView: View? = null

    /**
     * 日志输出标志
     */
    protected val TAG = this.javaClass.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContextView = LayoutInflater.from(this).inflate(bindLayout(), null)

        setContentView(mContextView)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

//        setStatusBar()

        initView(mContextView)
        doBusiness(this)
    }

    /**
     * [绑定布局]
     * @return
     */
    abstract fun bindLayout(): Int

    /**
     * [初始化控件]
     *
     * @param view
     */
    abstract fun initView(view: View?)

    /**
     * [业务操作]
     *
     * @param mContext
     */
    abstract fun doBusiness(mContext: Context?)

//    protected open fun setStatusBar() {
//        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.dark_gray), 0)
//    }

    protected fun showToast(msgContent: String?) {
        Toast.makeText(this, msgContent, Toast.LENGTH_SHORT).show()
    }




}