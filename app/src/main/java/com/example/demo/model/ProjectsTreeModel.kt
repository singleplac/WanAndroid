package com.example.demo.model

data class ProjectsTreeModel(
    val `data`: List<ProjectData>,
    val errorCode: Int,
    val errorMsg: String
)

data class ProjectData (
    val children: List<Any>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)