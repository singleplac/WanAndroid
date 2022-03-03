package com.example.demo.view

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
import com.example.demo.adpater.CollectAdapter
import com.example.demo.model.CollectModel
import com.example.demo.model.DataXCollect
import com.example.demo.network.AppService
import com.example.demo.network.ServiceCreator
import com.example.demo.utils.LoginUtil

class CollectionFragment : Fragment() {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: CollectAdapter
    private lateinit var appService: AppService
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_collection, container, false)
        collectionRecyclerview = view.findViewById(R.id.collect_article_Recyclerview)
        //获取Service接口的动态代理对象
        appService = ServiceCreator.createWithCookies(AppService::class.java)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //initialize
        layoutManager = LinearLayoutManager(activity)
        collectionRecyclerview.layoutManager = layoutManager
        adapter = CollectAdapter(articleList)
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
                        "CollectionFragment",
                        "total = $total, visibleItemCount  = $visibleItemCount, " +
                                "pastVisibleItem = $pastVisibleItem"
                    )


                    if (isLoading) {

                        Log.d("CollectionFragment", "is Loading...... please wait patiently")

                        if (total > previousTotal) {
                            isLoading = false
                            previousTotal = total

                            Log.d(
                                "CollectionFragment",
                                "previousTotal = $previousTotal , total = $total"
                            )
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

    private fun retrofitService() {
//        appService.getCollectList(pageNumber)
        appService.getCollectList(pageNumber)
            .enqueue(object : retrofit2.Callback<CollectModel> {

                override fun onResponse(
                    call: retrofit2.Call<CollectModel>,
                    response: retrofit2.Response<CollectModel>
                ) {
                    val responseData = response.body()
                    var errorCode = responseData?.errorCode
                    var testmsg = responseData?.errorMsg
                    Log.d("CollectionFragment","errorCode = " + "$errorCode " + "errormsg: $testmsg")
                    if (responseData != null ) {
                        if(LoginUtil.isLogin()){
                            articleList.addAll(responseData.data.datas)
                            var test = articleList[0].author
                            Log.d("CollectionFragment","$test")
                            adapter.notifyDataSetChanged()
                        }else{
                            Toast.makeText(requireActivity(),"[collectionFragment] Pls login first",Toast.LENGTH_SHORT).show()
                        }

                    } else {

                        Log.d("CollectionFragment", "find no results ")
                    }


                }

                override fun onFailure(call: retrofit2.Call<CollectModel>, t: Throwable) {
                    t.printStackTrace()
                }
            })

    }


}