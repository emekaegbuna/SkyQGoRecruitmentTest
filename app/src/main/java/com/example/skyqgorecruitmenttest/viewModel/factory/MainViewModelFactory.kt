package com.example.skyqgorecruitmenttest.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skyqgorecruitmenttest.data.repository.Repository
import com.example.skyqgorecruitmenttest.viewModel.MainViewModel


@Suppress("UNCHECKED_CAST")
class MainViewModelFactory constructor(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}