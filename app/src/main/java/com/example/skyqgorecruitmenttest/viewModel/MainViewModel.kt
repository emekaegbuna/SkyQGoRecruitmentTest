package com.example.skyqgorecruitmenttest.viewModel

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.skyqgorecruitmenttest.data.model.Data
import com.example.skyqgorecruitmenttest.data.repository.Repository
import com.example.skyqgorecruitmenttest.ui.activity.MainActivity
import io.reactivex.disposables.CompositeDisposable
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject


class MainViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val disposable = CompositeDisposable()
    var lastFetchedTime: Date? = null

    val repos: MutableLiveData<List<Data>> = MutableLiveData()
    val totalCount: MutableLiveData<Int> = MutableLiveData()

    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val toastMessage: MutableLiveData<String> = MutableLiveData()

    val loadingState = MutableLiveData<LoadingState>()

    enum class LoadingState {
        LOADING,
        SUCCESS,
        ERROR
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

    fun getActivity(): Class<out Activity> {
        return MainActivity::class.java
    }

    fun fetchMovieRepos(query: String?, filter: String?, clearOldItem: Boolean){

        if (clearOldItem) {
            loadingState.value = LoadingState.LOADING
        }
        disposable.add(
            repository.fetchMovieRepos()
                .subscribe({
                    lastFetchedTime = Date()
                    if (it.data.isEmpty()) {
                        if (clearOldItem) {
                            errorMessage.value = "No Movies Found"
                            loadingState.value = LoadingState.ERROR
                        } else {
                            toastMessage.value = "No more data found"
                        }
                    } else {

                        repos.value = it.data
                        /*if (query!!.isNotEmpty()){
                            when(filter){
                                "title" -> {
                                    it.data.filter {
                                        myFilterFunc(it)
                                    }
                                }
                                "genre" -> {

                                }
                            }
                        }*/
                        //totalCount.value = it.data.size
                        loadingState.value = LoadingState.SUCCESS
                    }
                }, {
                    lastFetchedTime = Date()

                    it.printStackTrace()

                    if (clearOldItem) {
                        when (it) {
                            is UnknownHostException -> errorMessage.value = "No Network"
                            else -> errorMessage.value = it.localizedMessage
                        }

                        loadingState.value = LoadingState.ERROR
                    } else {
                        when (it) {
                            is UnknownHostException -> toastMessage.value = "No Network"
                            else -> toastMessage.value = it.localizedMessage
                        }
                    }
                })
        )
    }

    /*private fun myFilterFunc(it: Data): Boolean {

        it.title ==

    }*/
}