package com.example.demo.network

import android.text.Editable
import com.example.demo.DataModel
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AppService {
    @GET("article/list/{page}/json")
    fun getAppData(@Path("page") page: Int): retrofit2.Call<DataModel>

    /**
     * 查找作者，不支持模糊匹配
     * 可查找的作者：鸿洋、郭霖
     */
    @GET("article/list/{page}/json")
    fun searchAuthor(@Path("page") page:Int, @Query("author") author: String): retrofit2.Call<DataModel>

    /**
     * 搜索
     */
    @POST("/article/query/{page}/json")
    fun searchKeywords(@Path("page") page: Int, @Query("k") keyword: String): retrofit2.Call<DataModel>

    /**
     * 搜索热词
     * @return
     */
    @GET("/hotkey/json")
    fun getHotKey(): retrofit2.Call<DataModel>

}