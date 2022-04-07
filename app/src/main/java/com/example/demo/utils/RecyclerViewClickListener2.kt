package com.example.demo.utils

import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import android.view.View

import android.view.GestureDetector

import android.content.Context
import android.view.GestureDetector.SimpleOnGestureListener


open class RecyclerViewClickListener2: RecyclerView.OnItemTouchListener {

    private var mGestureDetector: GestureDetector? = null
    private var mListener: OnItemClickListener? = null

    //内部接口，定义点击方法以及长按方法
    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
        fun onItemLongClick(view: View?, position: Int)
    }

   constructor(
        context: Context?,
        recyclerView: RecyclerView,
        listener: OnItemClickListener?
    ) {
        mListener = listener
        mGestureDetector = GestureDetector(context,
            object : SimpleOnGestureListener() {
                //这里选择SimpleOnGestureListener实现类，可以根据需要选择重写的方法
                //单击事件
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    val childView = recyclerView.findChildViewUnder(e.x, e.y)
                    if (childView != null && mListener != null) {
                        mListener!!.onItemClick(
                            childView,
                            recyclerView.getChildLayoutPosition(childView)
                        )
                        return true
                    }
                    return false
                }

                //长按事件
                override fun onLongPress(e: MotionEvent) {
                    val childView = recyclerView.findChildViewUnder(e.x, e.y)
                    if (childView != null && mListener != null) {
                        mListener!!.onItemLongClick(
                            childView,
                            recyclerView.getChildLayoutPosition(childView)
                        )
                    }
                }
            })
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        //把事件交给GestureDetector处理
        if(mGestureDetector!!.onTouchEvent(e)){
            return true
        }else
            return false

    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        TODO("Not yet implemented")
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        TODO("Not yet implemented")
    }

}