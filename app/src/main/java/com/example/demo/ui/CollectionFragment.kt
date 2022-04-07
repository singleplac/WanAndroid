package com.example.demo.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.R
import com.example.demo.network.ServiceCreator
import com.example.demo.utils.LoginUtil
import android.widget.ImageView
import androidx.lifecycle.viewModelScope
import com.example.demo.network.ApiService
import com.example.demo.network.ApiWrapper
import com.example.demo.network.AppService
import com.example.demo.relateddata.model.*
import com.example.demo.ui.adpater.ItemAdapter
import com.example.demo.relateddata.repository.base.BaseResponse
import com.example.demo.relateddata.repository.base.BaseResult
import com.example.demo.ui.adpater.CollectAdapter
import com.example.demo.ui.login.LoginActivity
import com.example.demo.utils.LogUtil
import com.example.demo.utils.RecyclerViewClickListener2
import kotlinx.coroutines.*
import java.io.IOException


class CollectionFragment : Fragment() {

    companion object{
        @JvmStatic
        val TAG = "CollectionFragment"
    }


    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: CollectAdapter
    private lateinit var collectionRecyclerview: RecyclerView

    //滑动监控的参数
    var pastVisibleItem = 0
    var visibleItemCount = 0
    var total = 0
    var previousTotal = 0
    var viewThreshold = 20
    var isLoading = true


    var articleList = ArrayList<DataXCollect>()

    private var pageNumber = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_collection, container, false)
        collectionRecyclerview = view.findViewById(R.id.collect_article_Recyclerview)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //initialize
        layoutManager = LinearLayoutManager(activity)
        collectionRecyclerview.layoutManager = layoutManager
        adapter = CollectAdapter(articleList)
        collectionRecyclerview.adapter = adapter

        initData()

        //滑动窗口监控
        collectionRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (dy > 0) { //向下滚动

                    //当前页面含有的个数
                    visibleItemCount = layoutManager.childCount
                    total = layoutManager.itemCount
                    //划过的个数
                    pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()

                    if (isLoading) {
                        if (total > previousTotal) {
                            isLoading = false
                            previousTotal = total
                        }
                    }

                    if (!isLoading && ((total - visibleItemCount) <= (pastVisibleItem + viewThreshold))) {

                        pageNumber++

                        initData()
                    }
                }
            }
        })

    }
    private fun initData() {
        MainScope().launch {
            val result = withContext(Dispatchers.IO) {
                getArticleList()
            }
            if (result is BaseResult.Success) {
                articleList.addAll(result.data.datas)
                adapter.notifyDataSetChanged()
            }else{

                LogUtil.instance.addTrace(TAG," init data fails, please login first")
                requireContext().startActivity(Intent(context, LoginActivity::class.java))
            }
        }

    }
    private suspend fun getArticleList():BaseResult<DataCollect>  {
        val result: BaseResult<DataCollect> = coroutineScope {
            val response = ApiWrapper.getInstance().getCollectionArticleList(pageNumber)
                if (response.errorCode == 0) {
                    BaseResult.Success(response.data)
                } else {
                    BaseResult.Error(IOException(response.errorMsg))
                }


        }
        return result

    }

}