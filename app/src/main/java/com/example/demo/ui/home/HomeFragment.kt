package com.example.demo.ui.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import com.example.demo.ui.adpater.ItemAdapter
import com.example.demo.network.ServiceCreator
import kotlin.collections.ArrayList
import android.widget.ImageButton

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.core.view.ViewCompat.setNestedScrollingEnabled
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.ViewUtils.runOnUiThread
import com.example.demo.Communicator
import com.example.demo.MainActivity
import com.example.demo.R
import com.example.demo.WanAndroidApplication
import com.example.demo.databinding.FragmentItemBinding
import com.example.demo.relateddata.model.Data
import com.example.demo.ui.adpater.BannerAdapter
import com.example.demo.relateddata.model.DataBanner
import com.example.demo.relateddata.model.DataModel
import com.example.demo.relateddata.model.DataX
import com.example.demo.relateddata.repository.HomeRepository
import com.example.demo.ui.base.BaseVmFragment
import com.example.demo.utils.LogUtil
import com.example.demo.utils.RecyclerViewClickListener2
import com.example.demo.utils.px
import retrofit2.Call
import java.lang.ref.WeakReference
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

@Suppress("UNCHECKED_CAST")
class HomeFragment : BaseVmFragment<HomeViewModel, FragmentItemBinding>() {


    private lateinit var searchView: SearchView
    private lateinit var toolbar: Toolbar

    private val indicateViewList = arrayListOf<View>()   //自动轮播的小圆点

    private var pageNumber = 0


    private var mArticleListData: ArrayList<DataX> = ArrayList()
    private lateinit var articleListAdapter: ItemAdapter
    var tempArrayList = ArrayList<DataX>()  //存放搜索结果
    private lateinit var layoutManager: LinearLayoutManager

    private var mHomeBannerBeanListData: ArrayList<DataBanner> = ArrayList()
    private lateinit var mHomeBannerHandler: HomeBannerHandler
    private lateinit var mHomeBannerExecutorService: ScheduledExecutorService

    private lateinit var communicator: Communicator

    override fun setVmFactory(): ViewModelProvider.Factory = HomeVmFactory(HomeRepository())

    override fun initData() {
        viewModel.setHomeArticleList(pageNumber)
        viewModel.setHomeBannerList()

    }

    override fun initView() {
        mHomeBannerHandler = HomeBannerHandler(this)

        initHomeArticleListRecyclerView()

        initHomeBannerViewPager()


        //减少放入NestedScrollView带来的卡顿
        setNestedScrollingEnabled( binding.nestedScrollView,false)

        //外层NestedScrollView的滑动监控
        binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v,_, scrollY, _, oldScrollY ->

            /** 上滑下滑
            if (scrollY > oldScrollY) {
                Log.i(TAG, "Scroll DOWN")
            }
            if (scrollY < oldScrollY) {
                Log.i(TAG, "Scroll UP")
            }

            if (scrollY == 0) {
                Log.i(TAG, "TOP SCROLL")
            }
            */

            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())
            ) {

                LogUtil.instance.addSth(TAG, "BOTTOM SCROLL")
                //load more
                pageNumber++
                showToast("$pageNumber is loaded")
                viewModel.setHomeArticleList(pageNumber)

            }
            /**direction >0下滑 <0 上滑
            if (!v.canScrollVertically(1)) {
                pageNumber++
                viewModel.setHomeArticleList(pageNumber)
            }
            */
        })

        //自动轮播
        startAutoLoopBanner()

        //设置搜索栏
        setHasOptionsMenu(true)

        //自定义toolbar
        toolbar = requireView().findViewById(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
    }

    override fun initObserve() {
        super.initObserve()

        viewModel.articleListLd.observe(viewLifecycleOwner, Observer {
            mArticleListData.addAll(it.datas)
            articleListAdapter.setData(mArticleListData)
        })

        viewModel.homeBannerBeanListLd.observe(viewLifecycleOwner, Observer {
            mHomeBannerBeanListData.clear()
            mHomeBannerBeanListData.addAll(it)

            //初始化切页的小圆点
            initDot()

            binding.bannerViewPager.apply {
                adapter = BannerAdapter(mHomeBannerBeanListData)
                currentItem = 1
            }

        })

    }

    /**
     * 搜索按钮
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.main_menu, menu)
        val menuItem: MenuItem = menu.findItem(R.id.main_search)
        searchView = menuItem.actionView as SearchView
        Log.d(TAG, "======before searchView.setOnQueryTextListener ======")
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.bannerViewPager.visibility = GONE

                tempArrayList.clear()
                val searchText = newText!!.toString()
                if (searchText.isNotEmpty()) {
                    mArticleListData.forEach {
                        //目前只能搜索标题
                        if (it.title.contains(searchText)) {
                            tempArrayList.add(it)
                        }
                    }
                    articleListAdapter.setData(tempArrayList)
                    articleListAdapter!!.notifyDataSetChanged()
                    Log.d(TAG, "======after filter ======")
                } else {
                    tempArrayList.clear()
                    tempArrayList.addAll(mArticleListData)
                    articleListAdapter.setData(tempArrayList)
                    articleListAdapter!!.notifyDataSetChanged()
                }

                return true
            }

        })

        //SearchView.OnCloseListener()只能在安卓3.2以下才有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            MenuItemCompat.setOnActionExpandListener(menuItem,
                object: MenuItemCompat.OnActionExpandListener {
                    //展开搜索框
                    override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                        return true
                    }
                    //收缩搜索框
                    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                        articleListAdapter.setData(tempArrayList)
                        articleListAdapter!!.notifyDataSetChanged()
                        binding.bannerViewPager.visibility = VISIBLE
                        return true
                    }

                })
        } else {

            searchView.setOnCloseListener (object: SearchView.OnCloseListener{
                override fun onClose(): Boolean {
                    articleListAdapter.setData(tempArrayList)
                    articleListAdapter!!.notifyDataSetChanged()
                    binding.bannerViewPager.visibility = VISIBLE
                    return true
                }

            })
        }
        super.onCreateOptionsMenu(menu, inflater)
    }


    /**
     * banner上的三个切换圆点
     */
    private fun initDot() {

        for (i in 0 until mHomeBannerBeanListData.size) {
            val layParams = LinearLayout.LayoutParams(10, 10).apply {
                setMargins(5, 0, 5, 0)
            }
            val indicateView = TextView(context).apply {
                layoutParams = layParams
                background = ContextCompat.getDrawable(context, R.drawable.shape_banner_nav_point_oval_gray)
            }
            indicateViewList.add(indicateView)
            binding.dotLayout.addView(indicateView)

        }
    }

    /**
     * 简化版自动轮播，有bug
     */
    fun autoBanner(isBanner: Boolean) {
        mHomeBannerExecutorService = Executors.newScheduledThreadPool(1)
        mHomeBannerExecutorService.scheduleAtFixedRate({
            val msg = Message()
            msg.what = 1
//            mHomeBannerHandler.sendMessage(msg) 什么玩意啊😭
        }, 2, 5, TimeUnit.SECONDS)
    }

    /**
     * 用到了弱引用，在内存不足时，GC会回收
     */
    inner class HomeBannerHandler(homeFragment: HomeFragment) : Handler() {
        private val weakReference: WeakReference<HomeFragment> = WeakReference(homeFragment)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 1) {
                weakReference.get()?.binding?.bannerViewPager?.apply {
                    val vpCurrentPosition: Int = currentItem
                    currentItem = if (vpCurrentPosition == mHomeBannerBeanListData.size) {
                        1
                    } else {
                        vpCurrentPosition + 1
                    }
                }
            }
        }
    }

    /**
     * 初始化banner的viewpager
     */
    private fun initHomeBannerViewPager() {
        binding.apply {

            bannerViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    bannerViewPager.setCurrentItem(position, true)
                    for (i in 0 until mHomeBannerBeanListData.size) {
                        if (position == i) {
                            indicateViewList[i].setBackgroundResource(R.drawable.shape_banner_nav_point_highlight_oval_white)
                        } else {
                            indicateViewList[i].setBackgroundResource(R.drawable.shape_banner_nav_point_oval_gray)
                        }
                    }

                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    if (state == ViewPager2.SCROLL_STATE_DRAGGING && !mHomeBannerExecutorService.isShutdown) {
                        mHomeBannerExecutorService.shutdown()
                    }
                }
            })
        }

    }

    /**
     * 初始化文章列表使用的recyclerview
     */
    private fun initHomeArticleListRecyclerView() {
        articleListAdapter = ItemAdapter(  mArticleListData, requireContext(), object :
                ItemAdapter.AdapterItemClickListener {
                override fun onPositionClicked(view: View?, position: Int) {
                    var  title: TextView = view!!.findViewById(R.id.title)
                    title.setOnClickListener {
                        Toast.makeText(requireContext(), "${title.text} is clicked", Toast.LENGTH_SHORT)
                            .show()

                        val item = mArticleListData[position]
                        //借助activity传递web link
                        communicator = requireContext() as MainActivity
                        //调用接口传参
                        communicator.passDataCom(item.link)

                    }
                }

                override fun onLongClicked(v: View?, position: Int) {
                }

            })
        layoutManager = LinearLayoutManager(activity)
        binding.itemRecyclerview.layoutManager = layoutManager
        binding.itemRecyclerview.adapter = articleListAdapter

    }

    /**
     * banner自动轮播
     * 我只动了1次，还跳页了😜
     */
    private fun startAutoLoopBanner() {
        //使用newScheduledThreadPool线程池
        mHomeBannerExecutorService = Executors.newScheduledThreadPool(1)
        mHomeBannerExecutorService.scheduleAtFixedRate({
            val msg = Message()
            msg.what = 1
            mHomeBannerHandler.sendMessage(msg)
        }, 2, 5, TimeUnit.SECONDS)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        //关闭定时周期任务
        if (!mHomeBannerExecutorService.isShutdown) {
            mHomeBannerExecutorService.shutdown()
        }
    }
}



