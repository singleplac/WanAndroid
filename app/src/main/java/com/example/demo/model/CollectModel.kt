package com.example.demo.model

data class CollectModel(
    val `data`: DataCollect,
    val errorCode: Int,
    val errorMsg: String
)
data class DataCollect(
    val curPage: Int,
    val datas: List<DataXCollect>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)

data class DataXCollect(
    val author: String,
    val chapterId: Int,
    val chapterName: String,
    val courseId: Int,
    val desc: String,
    val envelopePic: String,
    val id: Int,
    val link: String,
    val niceDate: String,
    val origin: String,
    val originId: Int,
    val publishTime: Long,
    val title: String,
    val userId: Int,
    val visible: Int,
    val zan: Int
)