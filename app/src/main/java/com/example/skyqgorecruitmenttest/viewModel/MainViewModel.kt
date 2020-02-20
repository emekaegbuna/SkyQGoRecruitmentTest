package com.example.skyqgorecruitmenttest.viewModel

import android.app.Activity
import android.os.Handler
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.skyqgorecruitmenttest.data.model.Data
import com.example.skyqgorecruitmenttest.data.repository.RepositoryImpl
import com.example.skyqgorecruitmenttest.ui.activity.MainActivity
import io.reactivex.disposables.CompositeDisposable
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject


class MainViewModel @Inject constructor(val repository: RepositoryImpl): ViewModel() {

    private val disposable = CompositeDisposable()
    var lastFetchedTime: Date? = null
    val liveMovies: MutableLiveData<List<Data>> = MutableLiveData()
    val genre: MutableLiveData<List<String>> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loadingState = MutableLiveData<LoadingState>()
    val toastMessage: MutableLiveData<String> = MutableLiveData()

    val handler = Handler()

    private fun doTheAutoRefresh(query: String, filter: String) {
        handler.postDelayed(Runnable {
            // Write code for your refresh logic

            fetchMovieRepos(query, filter)
        }, 1000*60*10)
    }


    fun fetchMovieRepos(query: String, filter: String){

        loadingState.value = LoadingState.LOADING
        doTheAutoRefresh(query, filter)

        disposable.add(
            repository.fetchMovieRepos()
                .subscribe({
                    lastFetchedTime = Date()
                    if (it.data.isEmpty()) {
                        toastMessage.value = "No Movies Found"
                    } else {
                        clearMovieCache()
                        cacheMovieRepo(it.data)
                        fetchGenre(it.data)
                        if (query.isNotEmpty() && filter.isNotEmpty()){
                            liveMovies.value = filterMoviesByTitleAndGenre(query, filter, it.data)
                        }else if (query.isNotEmpty() && filter.isEmpty()){
                            liveMovies.value = filterMoviesByTitleOnly(query, it.data)
                        }else if (query.isEmpty() && filter.isNotEmpty()){
                            liveMovies.value = filterMoviesByGenreOnly(filter, it.data)
                        }else{
                            liveMovies.value = it.data
                        }

                        loadingState.value = LoadingState.SUCCESS


                    }
                }, {

                    if (lastFetchedTime != null) {
                        fetchMovieCache(query, filter)
                        toastMessage.value = it.localizedMessage

                    }else{
                        errorMessage.value = it.message
                        loadingState.value = LoadingState.ERROR
                    }

                })
        )
    }

    private fun clearMovieCache() {
        disposable.add(
        repository.clearMovieCache()
            .subscribe({
                Log.d("ROOM TAG", "Movies deleted from room cache")
            },{
                Log.d("ROOM TAG", it.printStackTrace().toString())
            }))
    }

    fun cacheMovieRepo(movieRepo: List<Data>){

        disposable.add(
            repository.cacheMovieRepo(movieRepo).subscribe({
                Log.d("ROOM TAG", "Movies inserted")
            },{
                Log.d("ROOM TAG", it.printStackTrace().toString())
            }))

    }

    fun filterMoviesByTitleAndGenre(query: String, filter: String, movies: List<Data>): List<Data>?{
        val newMovieList: MutableList<Data> = mutableListOf()
        for (movie in movies){
            if (movie.genre.equals(filter) && movie.title.contains(query, true)){
                newMovieList.add(movie)
            }
        }
        if (newMovieList.isEmpty()){
            return null
        }else{

            return newMovieList
        }
    }

    fun filterMoviesByTitleOnly(query: String,movies: List<Data>): List<Data>?{
        val newMovieList: MutableList<Data> = mutableListOf()
        for (movie in movies){
            if (movie.title.contains(query, true)){
                newMovieList.add(movie)
            }
        }
        if (newMovieList.isEmpty()){
            return null
        }else{
            return newMovieList
        }

    }

    fun filterMoviesByGenreOnly(filter: String?, movies: List<Data>): List<Data>?{
        val newMovieList: MutableList<Data> = mutableListOf()
        for (movie in movies){
            if (movie.genre.equals(filter)){
                newMovieList.add(movie)
            }
        }
        if (newMovieList.isEmpty()){
            return null
        }else{
            return newMovieList
        }

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


    private fun fetchMovieCache(query: String?, filter: String?) {
        loadingState.value = LoadingState.LOADING

        disposable.add(
            repository.fetchMovieCache()
                .subscribe({
                    if (it.isEmpty()) {
                        errorMessage.value = "No Movies Found"
                        loadingState.value = LoadingState.ERROR

                    } else {

                        fetchGenre(it)
                        cacheMovieRepo(it)
                        val newMovieList: MutableList<Data> = mutableListOf()

                        if (query != null && query.isNotEmpty() && filter!!.isNotEmpty()){
                            for (movie in it){
                                if (movie.title.contains(query, true) && movie.genre.equals(filter)){
                                    newMovieList.add(movie)
                                }
                            }
                            liveMovies.value = newMovieList

                        }else if (query != null && query.isNotEmpty()){
                            for (movie in it){
                                if (movie.title.contains(query, true)){
                                    newMovieList.add(movie)
                                }
                            }

                            liveMovies.value = newMovieList

                        }else if (filter!!.isNotEmpty()){
                            for (movie in it){
                                if (movie.genre.equals(filter)){
                                    newMovieList.add(movie)
                                }
                            }

                            liveMovies.value = newMovieList

                        }else{
                            liveMovies.value = it
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
    }

}