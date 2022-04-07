package com.example.demo.ui.adpater

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.demo.ui.project.ProjectArticlesFragment
import com.example.demo.ui.project.ProjectArticlesFragment2

class ProjectCategoryAdapter(fragmentManager: FragmentManager, lifecycle:Lifecycle):FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0-> return ProjectArticlesFragment.newInstance(1,294)
            1 -> return ProjectArticlesFragment.newInstance(1,402)
            2 -> return ProjectArticlesFragment.newInstance(1,323)
            3 -> return ProjectArticlesFragment2.newInstance(1,367)
            else-> return ProjectArticlesFragment.newInstance(1,314)
        }
    }
}