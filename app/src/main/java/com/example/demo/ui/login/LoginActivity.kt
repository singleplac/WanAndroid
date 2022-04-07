package com.example.demo.ui.login

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.demo.R
import com.example.demo.databinding.ActivityLoginBinding
import com.example.demo.relateddata.repository.LoginRepository
import com.example.demo.ui.base.BaseVmActivity
import com.example.demo.utils.LogUtil
import com.example.demo.utils.LoginUtil

class LoginActivity : BaseVmActivity<LoginViewModel, ActivityLoginBinding>(), View.OnClickListener {

    private var isLogin: Boolean = true
    private lateinit var loginVm: LoginViewModel
    //MVVM 会给view.root做绑定，这里再使用setContentView(R.layout.activity_login)会顶替掉binding后的view
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////        setContentView(R.layout.activity_login)
//    }

    override fun setVmFactory(): ViewModelProvider.Factory = LoginVmFactory(LoginRepository())

    override fun initView() {
        showToast("Login initView")


        loginVm = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.apply {
            login.setOnClickListener(this@LoginActivity)
            register.setOnClickListener(this@LoginActivity)
            LogUtil.instance.addSth(TAG," I setOnClickListener")
            Log.i(TAG, "initView:  click is set ")
            //register.setOnClickListener(this@LoginActivity)
        }
        loginVm.isLoginLdBean.observe(this, Observer {
            LogUtil.instance.addSth(TAG," isLoginLdBean is being observed")
            if (it.isSuccess) {
                LoginUtil.setIsLogin(true)
                LoginUtil.setUsername(it.msg)
                showToast(getString(R.string.login_successful_cn))
                finish()
            } else {
                showToast(it.msg)
            }
        })

    }



    override fun onClick(v: View?) {
        LogUtil.instance.addSth(TAG," I setOnClickListener ==============")
        Log.i(TAG, "initView:  click is set ============ ")

        binding.apply {
            when (v) {
                login-> {
                    val userName = username.text.toString().trim()
                    val password = password.text.toString().trim()
                    /**
                     * Test flow
                     */
                    showToast("Login is clicked!!!!")
                    LogUtil.instance.addSth(TAG,"Login is clicked")
                    LogUtil.instance.addSth(TAG,"isLogin = $isLogin")
                    if(TextUtils.isEmpty(userName)){
                        LogUtil.instance.addSth(TAG,"userName is null")
                    }else{
                        LogUtil.instance.addSth(TAG,"userName is not null")
                    }

                    if(TextUtils.isEmpty(password)){
                        LogUtil.instance.addSth(TAG,"password is null")
                    }else{
                        LogUtil.instance.addSth(TAG,"password is not null")
                    }
                    /**
                     * test flow finished
                     */
                    if (isLogin) {
                        if (userName.isEmpty() || password.isEmpty()) {
                            showToast(getString(R.string.pls_input_right_username_password_cn))
                            return
                        }
                        loginVm.setIsLoginLd(userName, password)
                    } else {
                        if (userName.isEmpty()
                            || password.isEmpty()
                        ) {
                            showToast(getString(R.string.pls_input_right_username_password_cn))
                            return
                        }
//                        loginVm.setIsRegisterLd(userName, password, rePassword)
                    }
                }
                register ->{
                    showToast(" register function hasn't been implement")
                }

            }
        }
    }
}