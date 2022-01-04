package com.example.demo

import retrofit2.http.GET
import retrofit2.http.Path

interface AppService {
    @GET("article/list/{page}/json")
    fun getAppData(@Path("page") page: Int): retrofit2.Call<DataModel>

}