package com.example.demo.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo.relateddata.model.BannerDataModel
import com.example.demo.relateddata.model.Data
import com.example.demo.relateddata.model.DataBanner
import com.example.demo.relateddata.model.ProjectsTreeModel
import com.example.demo.relateddata.repository.HomeRepository
import com.example.demo.relateddata.repository.base.BaseResult
import com.example.demo.utils.LogUtil
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {
    val homeBannerBeanListLd: MutableLiveData<List<DataBanner>> = MutableLiveData()
    val articleListLd: MutableLiveData<Data> = MutableLiveData()

    fun setHomeBannerList() {

        viewModelScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                //读写数据不可以放在主线程，所以需要线程转换
                repository.getHomeBanner()
            }
            if (result is BaseResult.Success) {
                homeBannerBeanListLd.value = result.data
            } else if (result is BaseResult.Error) {
                //请求失败
                Log.e("jereTest", "setHomeBannerList: ${result.exception.message}")
            }
        }
    }

    fun setHomeArticleList(pageNumber: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                repository.getHomeArticle(pageNumber)
            }
            if (result is BaseResult.Success) {
                articleListLd.value = result.data
            }
        }
    }

}