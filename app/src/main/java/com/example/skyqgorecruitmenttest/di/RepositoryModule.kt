package com.example.skyqgorecruitmenttest.di

import com.example.skyqgorecruitmenttest.data.repository.RepositoryImpl
import com.example.skyqgorecruitmenttest.viewModel.factory.MainViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideMainViewModelFactory(repository: RepositoryImpl): MainViewModelFactory {
        return MainViewModelFactory(repository)
    }
}