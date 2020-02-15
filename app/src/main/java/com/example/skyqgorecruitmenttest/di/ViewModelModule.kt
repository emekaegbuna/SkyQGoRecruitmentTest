package com.example.skyqgorecruitmenttest.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skyqgorecruitmenttest.viewModel.FilterViewModel
import com.example.skyqgorecruitmenttest.viewModel.MainViewModel
import com.example.skyqgorecruitmenttest.viewModel.factory.MainViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FilterViewModel::class)
    abstract fun bindFilterViewModel(filterViewModel: FilterViewModel): ViewModel

    @Binds
    abstract fun provideMainViewModelFactory(viewModelFactory: MainViewModelFactory): ViewModelProvider.Factory


/*    @Binds
    abstract fun bindRepository(repository: RepositoryImplementation): Repository*/

}