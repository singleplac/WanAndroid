package com.example.demo.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.R
import com.example.demo.adpater.ItemAdapter
import com.example.demo.model.DataModel
import com.example.demo.model.DataX
import com.example.demo.network.AppService
import com.example.demo.network.ServiceCreator


class SearchFragment : Fragment() {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: ItemAdapter
    private lateinit var appService: AppService  //network
    private lateinit var articlesRecyclerview: RecyclerView
    private lateinit var searchButton: Button
    private lateinit var keywords: EditText
    private lateinit var searchBack: ImageView

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
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        articlesRecyclerview = view.findViewById(R.id.search_articles_recyclerview)

        searchButton = view.findViewById(R.id.search_button)
        keywords = view.findViewById(R.id.search_keywords)
        searchBack = view.findViewById(R.id.search_back)

        //获取Service接口的动态代理对象
        appService = ServiceCreator.create(AppService::class.java)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //initialize
        layoutManager = LinearLayoutManager(activity)
        articlesRecyclerview.layoutManager = layoutManager
        adapter = ItemAdapter(articleList)

        articlesRecyclerview.adapter = adapter

        searchButton.setOnClickListener {
            //get response
            appService.searchKeywords(pageNumber, keywords.text.toString())
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

                            Log.d("MainActivity", "find no results ${keywords.text}")
                        }


                    }
                    override fun onFailure(call: retrofit2.Call<DataModel>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        }
        searchBack.setOnClickListener{
            Log.d("SearchFragment","====== where will i go ======")
        }
    }

}







