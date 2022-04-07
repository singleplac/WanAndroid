package com.example.demo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.demo.relateddata.repository.HomeRepository


@Suppress("UNCHECKED_CAST")
class HomeVmFactory(private val repository: HomeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}