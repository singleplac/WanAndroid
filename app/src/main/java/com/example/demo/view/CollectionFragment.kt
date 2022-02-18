package com.example.demo.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.DataModel
import com.example.demo.DataX
import com.example.demo.R
import com.example.demo.adpater.ItemAdapter
import com.example.demo.network.AppService
import com.example.demo.network.ServiceCreator

class CollectionFragment : Fragment() {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: ItemAdapter
    private lateinit var appService: AppService
    private lateinit var collectionRecyclerview: RecyclerView


    //滑动监控的参数
    var pastVisibleItem = 0
    var visibleItemCount = 0
    var total = 0
    var previousTotal = 0
    var viewThreshold = 20
    var isLoading = true


    var articleList = ArrayList<DataX>()

    private var pageNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view :View = inflater.inflate(R.layout.fragment_collection, container, false)
        collectionRecyclerview = view.findViewById(R.id.collect_article_Recyclerview)
        //获取Service接口的动态代理对象
        appService = ServiceCreator.create(AppService::class.java)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //initialize
        layoutManager = LinearLayoutManager(activity)
        collectionRecyclerview.layoutManager = layoutManager
        adapter = ItemAdapter(articleList)

        collectionRecyclerview.adapter = adapter
        retrofitService()

        //滑动窗口监控
        collectionRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (dy > 0) { //向下滚动

                    //当前页面含有的个数
                    visibleItemCount = layoutManager.childCount
                    total = layoutManager.itemCount
                    //划过的个数
                    pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()

                    Log.d(
                        "CollectionFragment", "total = $total, visibleItemCount  = $visibleItemCount, " +
                                "pastVisibleItem = $pastVisibleItem"
                    )


                    if (isLoading) {

                        Log.d("CollectionFragment", "is Loading...... please wait patiently")

                        if (total > previousTotal) {
                            isLoading = false
                            previousTotal = total

                            Log.d("CollectionFragment", "previousTotal = $previousTotal , total = $total")
                        }

                    }


                    if (!isLoading && ((total - visibleItemCount) <= (pastVisibleItem + viewThreshold))) {

                        pageNumber++

                        Log.d("CollectionFragment", "Loading page is $pageNumber")

                        retrofitService()

                    }

                }
            }
        })


    }

    private fun retrofitService(){
//        appService.getCollectList(pageNumber)
        appService.getAppData(pageNumber)
            .enqueue(object : retrofit2.Callback<DataModel> {

                override fun onResponse(
                    call: retrofit2.Call<DataModel>,
                    response: retrofit2.Response<DataModel>
                ) {
                    val responseData = response.body()
                    if (responseData != null) {
                        articleList.clear()
                        articleList.addAll(responseData.data.datas)
                        adapter.notifyDataSetChanged()
                    } else {

                        Log.d("CollectionFragment", "find no results ")
                    }


                }
                override fun onFailure(call: retrofit2.Call<DataModel>, t: Throwable) {
                    t.printStackTrace()
                }
            })

    }


}