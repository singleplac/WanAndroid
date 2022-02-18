package com.example.demo.model

import com.example.demo.Data

data class ProjectsArticlesModel(
    val `data`: Data,
    val errorCode: Int,
    val errorMsg: String
)