package com.example.skyqgorecruitmenttest.viewModel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.skyqgorecruitmenttest.MovieApplication
import com.example.skyqgorecruitmenttest.data.model.Data
import com.example.skyqgorecruitmenttest.data.model.MovieRepo
import com.example.skyqgorecruitmenttest.data.repository.Repository
import com.example.skyqgorecruitmenttest.data.repository.RepositoryImpl
import com.example.skyqgorecruitmenttest.ui.activity.MainActivity
import io.reactivex.disposables.CompositeDisposable
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.timerTask


class MainViewModel @Inject constructor(val repository: RepositoryImpl): ViewModel() {

    private val disposable = CompositeDisposable()
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

    /*fun cacheMovieRepo(movieRepo: MovieRepo){
        disposable.add(
        repository.cacheMovieRepo(movieRepo)
            .subscribe(
                {Log.d("DB TAG", "DB updated")},
                {t: Throwable? -> Log.d("DB TAG", "Error updating DB")  }
            ))
    }*/

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
                        //cacheMovieRepo(it)
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

                    //fetchMovieCache(query, filter)

                    //loadingState.value = LoadingState.ERROR

                })
        )
    }

/*    private fun fetchMovieCache(query: String?, filter: String?) {
        loadingState.value = LoadingState.LOADING

        disposable.add(
            repository.fetchMovieCache()
                .subscribe({
                    if (it.data!!.isEmpty()) {
                        errorMessage.value = "No Movies Found"
                        loadingState.value = LoadingState.ERROR

                    } else {

                        fetchGenre(it.data)
                        cacheMovieRepo(it)
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

                    it.printStackTrace()


                    when (it) {
                        is UnknownHostException -> errorMessage.value = "No Network"
                        else -> errorMessage.value = it.localizedMessage
                    }

                    loadingState.value = LoadingState.ERROR

                })
        )
    }*/

    /*private fun myFilterFunc(it: Data): Boolean {

        it.title ==

    }*/
}