package com.example.demo.relateddata.repository

import com.example.demo.network.ApiWrapper
import com.example.demo.relateddata.model.DataLogin
import com.example.demo.relateddata.repository.base.BaseRepository
import com.example.demo.relateddata.repository.base.BaseResult
import com.example.demo.utils.LoginUtil

class LoginRepository : BaseRepository() {

    suspend fun login(paramsMap: Map<String, String>): BaseResult<DataLogin> {
        return safeApiCall("login", call = { requestLogin(paramsMap) })
    }

    private suspend fun requestLogin(paramsMap: Map<String, String>): BaseResult<DataLogin> =
        executeResponse(ApiWrapper.getInstance().login(paramsMap), {
            LoginUtil.setIsLogin(true)
        }, {
             LoginUtil.setIsLogin(false)
        })

}