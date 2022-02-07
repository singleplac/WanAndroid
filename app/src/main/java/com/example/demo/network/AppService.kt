package com.example.demo.network

import com.example.demo.DataModel
import com.example.demo.model.DataX
import com.example.demo.model.LogInModel
import com.example.demo.model.ProjectsTreeModel
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AppService {
    /**
     * 获取首页文章
     * @param page
     * @return
     */
    @GET("article/list/{page}/json")
    fun getAppData(@Path("page") page: Int): retrofit2.Call<DataModel>

    /**
     * 查找作者，不支持模糊匹配
     * 可查找的作者：鸿洋、郭霖...
     * @param page
     * @param author
     * @return
     */
    @GET("article/list/{page}/json")
    fun searchAuthor(
        @Path("page") page: Int,
        @Query("author") author: String
    ): retrofit2.Call<DataModel>

    /**
     * 搜索
     * @param page
     * @param keyword
     * @return
     */
    @POST("/article/query/{page}/json")
    fun searchKeywords(
        @Path("page") page: Int,
        @Query("k") keyword: String
    ): retrofit2.Call<DataModel>

    /**
     * 搜索热词
     */
    @GET("/hotkey/json")
    fun getHotKey(): retrofit2.Call<DataModel>

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @POST("/user/login")
    fun logIn(
        @Query("username") username: String?,
        @Query("password") password: String?
    ): retrofit2.Call<LogInModel>

    /**
     * 获取收藏文章列表
     * @param page
     * @return
     */
    @GET("/lg/collect/list/{page}/json")
    fun getCollectList(@Path("page") page: Int): retrofit2.Call<DataModel>

    /**
     * 收藏文章
     * @param id
     * @return
     */
    @POST("/lg/collect/{id}/json")
    fun collectArticle(@Path("id") id: Int): retrofit2.Call<DataModel>

    /**
     * 取消收藏文章
     * @param id
     * @param originId
     * @return
     */
    @POST("/lg/uncollect/{id}/json")
    fun cancelCollectArticle(
        @Path("id") id: Int,
        @Query("originId") originId: Int
    ): retrofit2.Call<DataModel>


    /**
     * 项目分类
     * @return
     */
    @GET("/project/tree/json")
    fun getProjectCategory(): retrofit2.Call<ProjectsTreeModel>

    /**
     * 项目列表数据
     * @return
     */
    @GET("/project/list/{page}/json")
    fun getProjectList(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): retrofit2.Call<DataModel>

}