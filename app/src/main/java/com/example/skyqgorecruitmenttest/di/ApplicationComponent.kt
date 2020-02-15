package com.example.skyqgorecruitmenttest.di

import android.app.Application
import com.example.skyqgorecruitmenttest.MovieApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ActivityBuildersModule::class, AndroidSupportInjectionModule::class])
interface ApplicationComponent: AndroidInjector<MovieApplication> {

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }
}