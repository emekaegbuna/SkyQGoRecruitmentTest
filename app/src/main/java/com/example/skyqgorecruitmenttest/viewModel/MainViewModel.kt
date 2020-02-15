package com.example.skyqgorecruitmenttest.viewModel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.skyqgorecruitmenttest.data.model.Data
import com.example.skyqgorecruitmenttest.data.repository.Repository
import com.example.skyqgorecruitmenttest.ui.activity.MainActivity
import io.reactivex.disposables.CompositeDisposable
import java.net.UnknownHostException
import java.time.Duration
import java.util.*
import javax.inject.Inject


class MainViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val disposable = CompositeDisposable()
    var fetchTime: MutableLiveData<Duration> = MutableLiveData()

    var lastFetchedTime: Date? = null

    val repos: MutableLiveData<List<Data>> = MutableLiveData()
    val genre: MutableLiveData<List<String>> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()

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

    fun fetchGenre(movies: List<Data>){



        val newGenreList: MutableList<String> = mutableListOf()

        for (movie in movies){
            if (newGenreList.contains(movie.genre)){
                Log.d("GENRE TAG", "genre already exists")
            }else{
                newGenreList.add(movie.genre)
                Log.d("GENRE TAG", newGenreList.toString())
                genre.value = newGenreList
            }
        }
    }

    fun fetchMovieRepos(query: String?, filter: String?){


        loadingState.value = LoadingState.LOADING

        disposable.add(
            repository.fetchMovieRepos()
                .subscribe({
                    lastFetchedTime = Date()
                    if (it.data!!.isEmpty()) {
                        errorMessage.value = "No Movies Found"
                        loadingState.value = LoadingState.ERROR

                    } else {

                        fetchGenre(it.data)

                        var newMovieList: MutableList<Data> = mutableListOf()

                        if (query != null && query.isNotEmpty() && filter!!.isNotEmpty()){
                            for (movie in it.data){
                                if (movie.title.contains(query, true) && movie.genre.equals(filter)){
                                    newMovieList.add(movie)
                                }
                            }
                            repos.value = newMovieList

                        }else if (query != null && query.isNotEmpty()){
                            for (movie in it.data){
                                if (movie.title.contains(query, true)){
                                    newMovieList.add(movie)
                                }
                            }

                            repos.value = newMovieList

                        }else if (filter!!.isNotEmpty()){
                            for (movie in it.data){
                                if (movie.genre.equals(filter)){
                                    newMovieList.add(movie)
                                }
                            }

                            repos.value = newMovieList

                        }else{
                            repos.value = it.data
                        }

                        //totalCount.value = it.data.size
                        loadingState.value = LoadingState.SUCCESS


                    }
                }, {
                    lastFetchedTime = Date()

                    it.printStackTrace()


                    when (it) {
                        is UnknownHostException -> errorMessage.value = "No Network"
                        else -> errorMessage.value = it.localizedMessage
                    }

                    loadingState.value = LoadingState.ERROR

                })
        )
    }

    /*private fun myFilterFunc(it: Data): Boolean {

        it.title ==

    }*/
}