package com.example.skyqgorecruitmenttest.di

import com.example.skyqgorecruitmenttest.ui.activity.FilterActivity
import com.example.skyqgorecruitmenttest.ui.activity.MainActivity
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [NetworkModule::class, RepositoryModule::class])
interface ActivityComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(filterActivity: FilterActivity)

}