package com.example.demo.relateddata.repository
import androidx.lifecycle.liveData
import com.example.demo.network.ApiWrapper
import com.example.demo.network.ServiceCreator
import com.example.demo.relateddata.model.BannerDataModel
import com.example.demo.relateddata.model.Data
import com.example.demo.relateddata.model.DataBanner
import com.example.demo.relateddata.repository.base.BaseRepository
import com.example.demo.relateddata.repository.base.BaseResult
import kotlinx.coroutines.Dispatchers


class HomeRepository : BaseRepository() {

    suspend fun getHomeBanner(): BaseResult<List<DataBanner>> {
        return safeApiCall("getHomeBanner", call = { requestBanners() })
    }

    private suspend fun requestBanners(): BaseResult<List<DataBanner>> =
        executeResponse(ApiWrapper.getInstance().getHomeBannerList())

    suspend fun getHomeArticle(pageNumber: Int): BaseResult<Data> {
        return safeApiCall("getHomeArticle", call = { requestArticle(pageNumber) })
    }

    private suspend fun requestArticle(pageNumber: Int): BaseResult<Data> =
        executeResponse(ApiWrapper.getInstance().getHomeArticleList(pageNumber))

}