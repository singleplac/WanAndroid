package com.example.demo.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.demo.WanAndroidApplication
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
abstract class BaseVmActivity<VM : ViewModel, B : ViewBinding> : AppCompatActivity() {

    lateinit var viewModel: VM
    lateinit var binding: B

    protected val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, setVmFactory())[getViewModelClass()]
        val method = getViewBindingClass().getDeclaredMethod("inflate", LayoutInflater::class.java)
        binding = method.invoke(null, layoutInflater) as B
        setContentView(binding.root)
        initData()
        initView()
        initObserve()
    }

    private fun getViewModelClass(): Class<VM> {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        return type as Class<VM>
    }

    private fun getViewBindingClass(): Class<B> {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1]
        return type as Class<B>
    }


    abstract fun setVmFactory(): ViewModelProvider.Factory

    abstract fun initView()

    open fun initData() {}

    open fun initObserve() {}

    fun showToast(msgContent: String?) {
        Toast.makeText(WanAndroidApplication.context, msgContent, Toast.LENGTH_SHORT).show()
    }
}