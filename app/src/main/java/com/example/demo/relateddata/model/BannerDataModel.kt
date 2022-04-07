package com.example.demo.relateddata.model

data class BannerDataModel(
    val `data`: List<DataBanner>,
    val errorCode: Int,
    val errorMsg: String
)

data class DataBanner(
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String
)