package com.example.demo.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import com.example.demo.adpater.ItemAdapter
import com.example.demo.network.AppService
import com.example.demo.network.ServiceCreator
import kotlin.collections.ArrayList
import android.widget.ImageButton

import android.view.View
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.example.demo.R
import com.example.demo.adpater.BannerAdapter
import com.example.demo.model.BannerDataModel
import com.example.demo.model.DataBanner
import com.example.demo.model.DataModel
import com.example.demo.model.DataX
import retrofit2.Call
import retrofit2.Response


class ItemFragment : Fragment() {


    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: ItemAdapter
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var appService: AppService
    private lateinit var itemRecyclerview: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var toolbar: Toolbar
    private lateinit var myFavorite: ImageButton
    private lateinit var viewPager: ViewPager2


    var progressBar: ProgressBar? = null
    var itemList = ArrayList<DataX>()
    var tempArrayList = ArrayList<DataX>()
    var bannerList = ArrayList<DataBanner>()
    var isLoading = true
    var pageNumber = 0

    //滑动监控的参数
    var pastVisibleItem = 0
    var visibleItemCount = 0
    var total = 0
    var previousTotal = 0
    var viewThreshold = 20
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        Log.d("ItemFragment","======setHasOptionsMenu ======")
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_item, container, false)

        viewPager = view.findViewById(R.id.bannerViewPager)


        itemRecyclerview = view.findViewById(R.id.itemRecyclerview)

//        myFavorite = view.findViewById(R.id.item_favorite)

        progressBar = view.findViewById(R.id.processorBar)
        progressBar?.visibility = View.VISIBLE

        //获取Service接口的动态代理对象
        appService = ServiceCreator.create(AppService::class.java)
        //获取banner data
        applyBanner()

        //设置toolbar
        toolbar = view.findViewById(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.main_menu,menu)
        val menuItem :MenuItem = menu.findItem(R.id.main_search)
        searchView = menuItem.actionView as SearchView
        Log.d("ItemFragment","======before searchView.setOnQueryTextListener ======")
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                tempArrayList.clear()
                val searchText = newText!!.toString()
                if(searchText.isNotEmpty()) {
                    itemList.forEach{
                        //目前只能搜索标题
                        if(it.title.contains(searchText)){
                            tempArrayList.add(it)
                        }
                    }
                    adapter!!.notifyDataSetChanged()
                    Log.d("ItemFragment","======after filter ======")
                }else{
                    tempArrayList.clear()
                    tempArrayList.addAll(itemList)
                    adapter!!.notifyDataSetChanged()
                }



                return true
            }

        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun applyBanner() {
        bannerAdapter = BannerAdapter(bannerList)
        viewPager.adapter = bannerAdapter
        appService.getBannerData().enqueue(object : retrofit2.Callback<BannerDataModel> {
            override fun onResponse(
                call: Call<BannerDataModel>,
                response: Response<BannerDataModel>
            ) {
                val responseData = response.body()
                if(responseData!=null){
                    bannerList.addAll(responseData.data)
                    bannerAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BannerDataModel>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }



    //使用layoutManager管理Recyclerview
    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)

//        myFavorite.setOnTouchListener { v, event ->
//            if (event.action == MotionEvent.ACTION_DOWN) {
//                Log.d("ItemFragment","The favorite button is clicked")
//                //重新设置按下时的背景图片
//                (v as ImageButton).setImageDrawable(resources.getDrawable(R.drawable.item_favorite_selected))
//            } else if (event.action == MotionEvent.ACTION_UP) {
//                //再修改为抬起时的正常图片
//                (v as ImageButton).setImageDrawable(resources.getDrawable(R.drawable.item_favorite_unselected))
//            }
//            false
//        }

        //设置layoutManger
        layoutManager = LinearLayoutManager(activity)
        itemRecyclerview.layoutManager = layoutManager
        adapter = ItemAdapter(tempArrayList)

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
                        "ItemFragment", "total = $total, visibleItemCount  = $visibleItemCount, " +
                                "pastVisibleItem = $pastVisibleItem"
                    )


                    if (isLoading) {

                        Log.d("ItemFragment", "is Loading...... please wait patiently")

                        if (total > previousTotal) {
                            isLoading = false
                            previousTotal = total

                            Log.d("ItemFragment", "previousTotal = $previousTotal , total = $total")
                        }

                    }

                    Log.d(
                        "ItemFragment", "total = $total, visibleItemCount  = $visibleItemCount," +
                                " pastVisibleItem = $pastVisibleItem,previous_total = $previousTotal "
                    )

                    if (!isLoading && ((total - visibleItemCount) <= (pastVisibleItem + viewThreshold))) {

                        pageNumber++

                        Log.d("ItemFragment", "Loading page is $pageNumber")

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

                    itemList.addAll(responseData.data.datas)
                    adapter.notifyDataSetChanged()
                    tempArrayList.addAll(itemList)

                    if (pagination) {
                        Log.d("ItemFragment", "page $pageNumber is loaded...")
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

                    Log.d("ItemFragment", "Error loading")

                }

            }

            override fun onFailure(call: Call<DataModel>, t: Throwable) {
                t.printStackTrace()
            }


        })

    }

    private fun performPagination() {

        progressBar?.visibility = View.VISIBLE
        retrofitService(true)
        progressBar?.visibility = View.GONE
    }




}



