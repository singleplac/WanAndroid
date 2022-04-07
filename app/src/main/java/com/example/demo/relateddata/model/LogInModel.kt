package com.example.demo.relateddata.model

data class LogInModel(
    val `data`: DataLogin,
    val errorCode: Int,
    val errorMsg: String
)
data class LoginResultBean(
    val isSuccess: Boolean,
    val msg: String
)

data class DataLogin(
    val admin: Boolean,
    val chapterTops: List<Any>,
    val coinCount: Int,
    val collectIds: List<Int>,
    val email: String,
    val icon: String,
    val id: Int,
    val nickname: String,
    val password: String,
    val publicName: String,
    val token: String,
    val type: Int,
    val username: String
)