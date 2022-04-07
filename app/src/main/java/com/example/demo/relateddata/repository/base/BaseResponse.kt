package com.example.demo.relateddata.repository.base

data class BaseResponse<out T>(
    val errorCode: Int,
    val errorMsg: String,
    val data: T
)