package com.example.demo.ui.search

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.MainActivity
import com.example.demo.R
import com.example.demo.ui.adpater.ItemAdapter
import com.example.demo.relateddata.model.DataHotKey
import com.example.demo.relateddata.model.DataModel
import com.example.demo.relateddata.model.DataX
import com.example.demo.relateddata.model.HotKeyModel
import com.example.demo.network.ServiceCreator
import com.zhy.view.flowlayout.TagFlowLayout

import com.zhy.view.flowlayout.FlowLayout

import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout.OnTagClickListener


class SearchFragment : Fragment() {

    private var hasGetHotWords: Boolean = false  //用于保证热词只加载一次,false 不曾加载过，true加载过
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: ItemAdapter
    private lateinit var articlesRecyclerview: RecyclerView
    private lateinit var searchButton: Button
    private lateinit var keywords: EditText
    private lateinit var searchBack: ImageView
    private lateinit var flowLayout: TagFlowLayout

    var articleList = ArrayList<DataX>()

    var hotKeyList: ArrayList<DataHotKey> =  ArrayList()

    private var pageNumber = 0


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

        //搜索热词布局
        flowLayout = view.findViewById(R.id.hotWords_fl)


        applySearchHotWords()

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //initialize recyclerView
        layoutManager = LinearLayoutManager(activity)
        articlesRecyclerview.layoutManager = layoutManager
        adapter = ItemAdapter(articleList, requireContext(),object:ItemAdapter.AdapterItemClickListener{
            override fun onPositionClicked(v: View?, position: Int) {
                //借助activity传递网页
                val title: TextView = v!!.findViewById(R.id.title)
                val communicator = requireContext() as MainActivity
                //点击标题跳转到另一个fragment
                title.setOnClickListener {
                    Toast.makeText(requireContext(), "${title.text} is clicked", Toast.LENGTH_SHORT)
                        .show()

                    val item = articleList[position]
                    //调用接口传参
                    communicator.passDataCom(item.link)
                }
            }
            override fun onLongClicked(v: View?, position: Int) {
                return
            }

        })
        articlesRecyclerview.adapter = adapter


        searchButton.setOnClickListener {
            //get search response
            applySearch()

        }
        searchBack.setOnClickListener{
            Log.d("SearchFragment","====== where will i go ======")
        }
    }

    private fun applySearch(){
        ServiceCreator.checkWithoutLog().searchKeywords(pageNumber, keywords.text.toString())
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

    private fun applySearchHotWords(){
        if(!hasGetHotWords) {
            hasGetHotWords = true
            ServiceCreator.checkWithoutLog().getHotKey().enqueue(object : retrofit2.Callback<HotKeyModel> {

                override fun onResponse(
                    call: retrofit2.Call<HotKeyModel>,
                    response: retrofit2.Response<HotKeyModel>
                ) {
                    val responseData = response.body()
                    if (responseData != null) {
                        hotKeyList.addAll(responseData.data)
                        val tabNames = ArrayList<String>()
                        for (data in hotKeyList){
                            tabNames.add(data.name)
                        }
//                        hotKeyList.forEach {
//                            DataHotKey -> tabNames.add(DataHotKey.name)
//                        }
                        flowLayout.adapter = object : TagAdapter<String>(tabNames) {
                            override fun getView(
                                parent: FlowLayout?,
                                position: Int,
                                t: String?
                            ): View {
                                val tagText = LayoutInflater.from(parent!!.context).inflate(
                                    R.layout.hotwords,
                                    parent, false
                                ) as TextView
                                tagText.text = tabNames[position]
                                tagText.getBackground().setColorFilter(resources.getColor(R.color.colorPrimary) , PorterDuff.Mode.SRC_ATOP)
                                flowLayout.setOnTagClickListener(OnTagClickListener { view: View?, position: Int, parent: FlowLayout? ->
                                    //在搜索框填充搜索用的关键词
                                    keywords.setText(tabNames[position])
                                    //在下方的Recycler中填充搜索结果
                                    applySearch()
                                    true
                                })
                                return tagText
                            }

                        }
                    } else {

                        Log.d("SearchFragment", "get hot keys failed")
                    }


                }
                override fun onFailure(call: retrofit2.Call<HotKeyModel>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }else{
            return
        }

    }

}







