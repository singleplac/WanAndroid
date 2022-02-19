package com.example.demo.view.project

import android.content.ClipData
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.demo.R
import com.example.demo.model.ProjectData
import com.example.demo.model.ProjectsTreeModel
import com.example.demo.network.AppService
import com.example.demo.network.ServiceCreator
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import java.util.ArrayList





class ProjectsFragmentViewPage : Fragment() {

    private lateinit var projectViewPager: ViewPager
    private lateinit var tabLayout: TabLayout


    private lateinit var appService: AppService




    //Fragments and TitleList
    var projectCategoryTitleList = ArrayList<ProjectData>()
    var fragments = ArrayList<Fragment>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view :View = inflater.inflate(R.layout.fragment_projects_view_page, container, false)
        //Initialize components
        projectViewPager = view.findViewById(R.id.projectViewPager)
        tabLayout = view.findViewById(R.id.project_tabLayout)



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

                        //add fragments
                        for(i in 0..5){
                            fragments.add(ProjectArticlesFragment.newInstance(1,projectCategoryTitleList[i].id))
                        }


                        //set adapter
                        projectViewPager.adapter = object:  FragmentStatePagerAdapter(activity!!.supportFragmentManager) {
                            override fun getCount(): Int {
                                return fragments.size
                            }

                            override fun getItem(position: Int): Fragment {
                                return fragments[position]
                            }

                            override fun getPageTitle(position: Int): CharSequence{

                                return projectCategoryTitleList[position].name
                            }
                        }
                        //deliver viewPager to tabLayout
                        tabLayout.setupWithViewPager(projectViewPager)

                    } else {

                        Log.d("ProjectsViewPage", "find no results ")
                    }


                }

                override fun onFailure(call: Call<ProjectsTreeModel>, t: Throwable) {
                    t.printStackTrace()
                }
            })

    }

}
