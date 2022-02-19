package com.example.demo.adpater

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.model.ProjectData
import com.example.demo.model.ProjectsTreeModel
import java.util.ArrayList

class ProjectAdapter (private var projectCategoryList:ArrayList<ProjectData>): RecyclerView.Adapter<ProjectAdapter.ViewHolder>() {
    private lateinit var newsList: ArrayList<ProjectsTreeModel>

   inner class ViewHolder(view: View) :RecyclerView.ViewHolder(view) {

   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount() = projectCategoryList.size


}