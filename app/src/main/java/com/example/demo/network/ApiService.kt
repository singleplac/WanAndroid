package com.example.demo.network

import com.example.demo.relateddata.model.Data
import com.example.demo.relateddata.model.DataBanner
import com.example.demo.relateddata.model.DataCollect
import com.example.demo.relateddata.model.DataLogin
import com.example.demo.relateddata.repository.base.BaseResponse
import retrofit2.http.*

/**
 *
 */
interface ApiService {

    /**
     * 获取主页Banner数据
     */
    @GET("banner/json")
    suspend fun getHomeBannerList(): BaseResponse<List<DataBanner>>

    /**
     * 获取主页文章列表
     */
    @GET("article/list/{pageNumber}/json")
    suspend fun getHomeArticleList(@Path("pageNumber") pageNumber: Int): BaseResponse<Data>


    /**
     * 获取收藏的文章列表
     * @return
     */
    @GET("/lg/collect/list/{pageId}/json")
    suspend fun getCollectionArticleList(@Path("pageId") pageId: Int): BaseResponse<DataCollect>


    /**
     * 收藏文章
     * @param id
     * @return
     */
    @POST("/lg/collect/{id}/json")
    suspend fun collectArticle(@Path("id") id: Int): BaseResponse<Any>

    /**
     * 取消文章收藏
     * @param id
     * @return
     */
    @POST("/lg/uncollect_originId/{id}/json")
    suspend fun unCollectArticle(@Path("id") id: Int): BaseResponse<Any>

    /**
     * 登入
     */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(@FieldMap paramsMap: @JvmSuppressWildcards Map<String, Any>): BaseResponse<DataLogin>


}