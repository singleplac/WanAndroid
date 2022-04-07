package com.example.demo.relateddata.repository

import com.example.demo.network.ApiWrapper
import com.example.demo.relateddata.repository.base.BaseRepository
import com.example.demo.relateddata.repository.base.BaseResult

class CollectionRepository : BaseRepository() {

    suspend fun collectArticle(articleId: Int): BaseResult<Any> {
        return safeApiCall("collectArticle", call = { requestCollectArticle(articleId) })
    }

    private suspend fun requestCollectArticle(articleId: Int): BaseResult<Any> =
        executeResponse(ApiWrapper.getInstance().collectArticle(articleId))

    suspend fun unCollectArticle(articleId: Int): BaseResult<Any> {
        return safeApiCall("unCollectArticle", call = { requestUnCollectArticle(articleId) })
    }

    private suspend fun requestUnCollectArticle(articleId: Int): BaseResult<Any> =
        executeResponse(ApiWrapper.getInstance().unCollectArticle(articleId))

}