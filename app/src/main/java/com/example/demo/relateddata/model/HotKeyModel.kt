package com.example.demo.relateddata.model

data class HotKeyModel(
    val `data`: List<DataHotKey>,
    val errorCode: Int,
    val errorMsg: String
)

data class DataHotKey(
    val id: Int,
    val link: String,
    val name: String,
    val order: Int,
    val visible: Int
)