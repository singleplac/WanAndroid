package com.example.demo.view.project

import android.content.ClipData
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.demo.R
import com.example.demo.adpater.ItemAdapter
import com.example.demo.adpater.ProjectAdapter
import com.example.demo.adpater.ProjectCategoryAdapter
import com.example.demo.model.DataModel
import com.example.demo.model.DataX
import com.example.demo.model.ProjectData
import com.example.demo.model.ProjectsTreeModel
import com.example.demo.network.AppService
import com.example.demo.network.ServiceCreator
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Response
import java.util.ArrayList





class ProjectsFragment : Fragment() {

    private lateinit var projectViewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    //Adapter
    private lateinit var adapter: ProjectCategoryAdapter

    private lateinit var appService: AppService




    //Fragments and TitleList
    var projectCategoryTitleList = ArrayList<ProjectData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view :View = inflater.inflate(R.layout.fragment_projects, container, false)
        //Initialize components
        projectViewPager = view.findViewById(R.id.projectViewPager)
        tabLayout = view.findViewById(R.id.project_tabLayout)

        /**
         * adapter：ProjectCategoryAdapter
         */
        adapter = activity?.let { ProjectCategoryAdapter(it.supportFragmentManager,lifecycle) }!!
        projectViewPager.adapter=adapter


        //get net service interface
        appService = ServiceCreator.create(AppService::class.java)
        //apply network, load response in projectCategoryTitleList
        retrofitService()

        return view
    }

    /**
     * 获取项目目录服务
     */
    private fun retrofitService(){
        appService.getProjectCategory()
            .enqueue(object : retrofit2.Callback<ProjectsTreeModel> {

                override fun onResponse(
                    call: retrofit2.Call<ProjectsTreeModel>,
                    response: retrofit2.Response<ProjectsTreeModel>
                ) {
                    val responseData = response.body()
                    if (responseData != null) {
                        //get TitleList
                        projectCategoryTitleList.addAll(responseData.data)
                        TabLayoutMediator(tabLayout,projectViewPager) {
                                tab, position ->
                            tab.text = projectCategoryTitleList[position].name
                        }.attach()
                        adapter.notifyDataSetChanged()

                    } else {

                        Log.d("ProjectsFragment", "find no results ")
                    }


                }

                override fun onFailure(call: Call<ProjectsTreeModel>, t: Throwable) {
                    t.printStackTrace()
                }
            })

    }




}
