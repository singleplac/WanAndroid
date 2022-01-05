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
    private lateinit var itemRecyclerview: RecyclerView

    var progressBar: ProgressBar? = null
    var itemList = ArrayList<DataX>()
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
        itemRecyclerview.layoutManager = layoutManager
        adapter = ItemAdapter(itemList)
        itemRecyclerview.adapter = adapter
        retrofitService(false)

        //滑动窗口监控

        itemRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

    private fun retrofitService(pagination: Boolean) {

        appService.getAppData(pageNumber).enqueue(object : retrofit2.Callback<DataModel> {

            override fun onResponse(
                call: retrofit2.Call<DataModel>,
                response: retrofit2.Response<DataModel>
            ) {

                val responseData = response.body()
                if (responseData!!.errorCode == 0) {

                    responseData.data.datas.forEach {

                        val item = DataX(
                            it.apkLink, it.audit, it.author, it.canEdit,
                            it.chapterId, it.chapterName, it.collect, it.courseId,
                            it.desc, it.descMd, it.envelopePic, it.fresh,
                            it.host, it.id, it.link, it.niceDate,
                            it.niceShareDate, it.origin, it.prefix, it.projectLink,
                            it.publishTime, it.realSuperChapterId, it.selfVisible, it.shareDate,
                            it.shareUser, it.superChapterId, it.superChapterName, it.tags,
                            it.title, it.type, it.userId, it.visible,
                            it.zan
                        )

                        itemList.add(item)

                    }
                    adapter.notifyDataSetChanged()

                    if (pagination) {
                        Log.d("MainActivity", "page $pageNumber is loaded...")
                        Toast.makeText(
                            activity,
                            "page $pageNumber is loaded...",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        progressBar?.visibility = View.GONE
                    }

                } else {

                    Log.d("MainActivity", "Error loading")

                }

            }

            override fun onFailure(call: retrofit2.Call<DataModel>, t: Throwable) {
                t.printStackTrace()
            }


        })

    }

    private fun performPagination() {


        progressBar?.visibility = View.VISIBLE

        retrofitService(true)

        progressBar?.visibility = View.GONE
    }

    //创建自己的Adapter
    //传入list
    inner class ItemAdapter(private val newsList: List<DataX>) :
        RecyclerView.Adapter<ItemAdapter.ViewHolder>() {


        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            //继承RecyclerView.ViewHolder

            var title: TextView = view.findViewById(R.id.title)
            var shareUser: TextView = view.findViewById(R.id.shareUser)

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


            val item = newsList[position]
            //设置参数
            holder.title.text = item.title
            holder.shareUser.text = item.shareUser

        }

        override fun getItemCount() = newsList.size

    }


}
