package com.example.demo.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo.relateddata.model.LogInModel
import com.example.demo.relateddata.model.LoginResultBean
import com.example.demo.relateddata.repository.LoginRepository
import com.example.demo.relateddata.repository.base.BaseResult
import com.example.demo.utils.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    val isLoginLdBean: MutableLiveData<LoginResultBean> = MutableLiveData()

    fun setIsLoginLd(username: String, password: String) {
        val paramsMap = mapOf("username" to username, "password" to password)
        viewModelScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                LogUtil.instance.addSth("LoginViewModel","call internet connection")
                repository.login(paramsMap)
            }
            if (result is BaseResult.Success) {
                isLoginLdBean.value =
                    LoginResultBean(
                        true,
                        result.data.username
                    )
            } else if (result is BaseResult.Error) {
                isLoginLdBean.value =
                    result.exception.message?.let {
                        LoginResultBean(
                            false,
                            it
                        )
                    }
            }
        }
    }

}