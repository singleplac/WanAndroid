package com.example.demo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.demo.adpater.ItemAdapter
import com.example.demo.adpater.ProjectAdapter
import com.example.demo.model.DataX
import com.example.demo.model.ProjectsTreeModel
import com.example.demo.network.AppService
import com.example.demo.network.ServiceCreator
import retrofit2.Call


class ProjectsFragment : Fragment() {

    private lateinit var projectViewPager: ViewPager
    private lateinit var adapter: ProjectAdapter
    private lateinit var appService: AppService

    var projectList = ArrayList<DataX>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view :View = inflater.inflate(R.layout.fragment_projects, container, false)
        projectViewPager = view.findViewById(R.id.projectViewPager)
        appService = ServiceCreator.create(AppService::class.java)
        projectViewPager.adapter = adapter
        retrofitService()

        return view
    }

    private fun retrofitService(){
        appService.getProjectCategory()
            .enqueue(object : retrofit2.Callback<ProjectsTreeModel> {

                override fun onResponse(
                    call: retrofit2.Call<ProjectsTreeModel>,
                    response: retrofit2.Response<ProjectsTreeModel>
                ) {
                    val responseData = response.body()
                    if (responseData != null) {
                        projectList.clear()
                        projectList.addAll(responseData.data)
                        adapter.notifyDataSetChanged()
                    } else {

                        Log.d("CollectionFragment", "find no results ")
                    }


                }

                override fun onFailure(call: Call<ProjectsTreeModel>, t: Throwable) {
                    t.printStackTrace()
                }
            })

    }


}
