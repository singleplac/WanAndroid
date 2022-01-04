package com.example.demo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ItemFrag : Fragment() {

    private lateinit var communicator: Communicator
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: ItemFrag.ItemAdapter
    private lateinit var appService: AppService

    var itemRecyclerview: RecyclerView? = null
    var progressBar: ProgressBar? = null

    var itemList = ArrayList<Item>()

    var isLoading = true
    var pageNumber = 0

    //滑动监控的参数
    var pastVisibleItem = 0
    var visibleItemCount = 0
    var total = 0
    var previousTotal = 0
    var viewThreshold = 20


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.item_frag, container, false)
        itemRecyclerview = view.findViewById(R.id.itemRecyclerview)

        progressBar = view.findViewById(R.id.processorBar)

        progressBar?.visibility = View.VISIBLE

        //获取Service接口的动态代理对象
        appService = ServiceCreator.create(AppService::class.java)

        return view
    }

    //使用layoutManager管理Recyclerview
    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)

        //设置layoutManger
        layoutManager = LinearLayoutManager(activity)
        itemRecyclerview?.layoutManager = layoutManager
        adapter = ItemAdapter(itemList)
        itemRecyclerview?.adapter = adapter

        appService.getAppData(pageNumber).enqueue(object : retrofit2.Callback<DataModel> {

            override fun onResponse(
                call: retrofit2.Call<DataModel>,
                response: retrofit2.Response<DataModel>
            ) {
                val responseData = response.body()
                responseData?.data?.datas?.forEach {

                    val item = Item(it.title, it.shareUser, it.link, responseData.data.curPage)
                    itemList.add(item)
//                    adapter = ItemAdapter(itemList)
//                    itemRecyclerview?.adapter = adapter
                    adapter.notifyDataSetChanged()
                    progressBar?.visibility = View.GONE

                }
            }

            override fun onFailure(call: retrofit2.Call<DataModel>, t: Throwable) {
                t.printStackTrace()
            }

        })


        //滑动窗口监控

        itemRecyclerview?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (dy > 0) { //向下滚动

                    //当前页面含有的个数
                    visibleItemCount = layoutManager.childCount
                    total = layoutManager.itemCount
                    //划过的个数
                    pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                    Log.d(
                        "MainActivity", "total = $total, visibleItemCount  = $visibleItemCount, " +
                                "pastVisibleItem = $pastVisibleItem"
                    )
                    if (isLoading) {
                        Log.d("MainActivity", "is Loading...... please wait patiently")
                        if (total > previousTotal) {
                            isLoading = false
                            previousTotal = total

                            Log.d("MainActivity", "previousTotal = $previousTotal , total = $total")
                        }
                    }
                    Log.d(
                        "MainActivity", "total = $total, visibleItemCount  = $visibleItemCount," +
                                " pastVisibleItem = $pastVisibleItem,previous_total = $previousTotal "
                    )
                    if (!isLoading && ((total - visibleItemCount) <= (pastVisibleItem + viewThreshold))) {
                        pageNumber++
                        Log.d("MainActivity", "Loading page is $pageNumber")
                        performPagination()
                        isLoading = true
                    }
                }
            }
        })


    }

    private fun performPagination() {

        progressBar?.visibility = View.VISIBLE
        val items = ArrayList<Item>()
        Log.d("MainActivity", "Hello? I'm in perform pagination")
        appService.getAppData(pageNumber).enqueue(object : retrofit2.Callback<DataModel> {
            override fun onResponse(
                call: retrofit2.Call<DataModel>,
                response: retrofit2.Response<DataModel>
            ) {

                val responseData = response.body()
                if (responseData!!.errorCode == 0) {

                    responseData.data.datas.forEach {

                        val item = Item(it.title, it.shareUser, it.link, responseData.data.curPage)

                        items.add(item)

                    }
                    val itemSize = items.size
                    Log.d("MainActivity", "items.size = $itemSize")
                    adapter.addItem(items)
//                    adapter = ItemAdapter(itemList)
//                    itemRecyclerview?.adapter = adapter
                    adapter.notifyDataSetChanged()
                    Log.d("MainActivity", "page $pageNumber is loaded...")
                    Toast.makeText(activity, "page $pageNumber is loaded...", Toast.LENGTH_SHORT)
                        .show()


                } else {
                    Log.d("MainActivity", "No more items available")
                }

            }

            override fun onFailure(call: retrofit2.Call<DataModel>, t: Throwable) {
                t.printStackTrace()
            }


        })
        progressBar?.visibility = View.GONE
    }

    //创建自己的Adapter
    //传入list

    inner class ItemAdapter(private val newsList: List<Item>) :
        RecyclerView.Adapter<ItemAdapter.ViewHolder>() {


        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            //继承RecyclerView.ViewHolder

            var title: TextView = view.findViewById(R.id.title)
            var shareUser: TextView = view.findViewById(R.id.shareUser)
            var curPage: TextView = view.findViewById(R.id.curPage)

        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            //创建ViewHolder实例

            //引入单个item的XML

            val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
            val holder = ViewHolder(view)
            val title: TextView = view.findViewById(R.id.title)

            if (activity != null) {
                communicator = activity as Communicator
            }


            //点击标题跳转到另一个fragment
            title.setOnClickListener {
                Toast.makeText(parent.context, "${title.text} is clicked", Toast.LENGTH_SHORT)
                    .show()

                val item = newsList[holder.adapterPosition]
                if (item.title != null && context != null) {

                    //调用接口传参
                    communicator.passDataCom(item.link)

                }

            }

            return holder

        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            //设置参数

            val item = newsList[position]

            holder.title.text = item.title
            holder.shareUser.text = item.shareUser
            holder.curPage.text = "${item.curPage}"
        }

        override fun getItemCount() = newsList.size

        fun addItem(items: List<Item>) {
            for (i in items) {
                itemList.add(i)
            }

        }

    }


}
