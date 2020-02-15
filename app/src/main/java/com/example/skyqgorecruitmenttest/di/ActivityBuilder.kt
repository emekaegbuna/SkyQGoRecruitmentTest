package com.example.skyqgorecruitmenttest.di

import com.example.skyqgorecruitmenttest.ui.activity.FilterActivity
import com.example.skyqgorecruitmenttest.ui.activity.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(modules = [])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [])
    abstract fun contributeFilterActivity(): FilterActivity

}