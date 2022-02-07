package com.example.demo.adpater

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.example.demo.DataX
import com.example.demo.model.ProjectsTreeModel
import java.util.ArrayList

class ProjectAdapter : PagerAdapter() {
    private lateinit var newsList: ArrayList<ProjectsTreeModel>

    override fun getCount(): Int {
        TODO("Not yet implemented")
    }


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        TODO("Not yet implemented")
    }


}