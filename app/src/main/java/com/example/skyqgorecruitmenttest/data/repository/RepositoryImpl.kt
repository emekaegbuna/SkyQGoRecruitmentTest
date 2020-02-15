package com.example.skyqgorecruitmenttest.data.repository


import com.example.skyqgorecruitmenttest.data.model.MovieRepo
import com.example.skyqgorecruitmenttest.data.remote.WebServices
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

open class RepositoryImpl @Inject constructor(private val webServices: WebServices) : Repository {
    override fun fetchMovieRepos(): Single<MovieRepo> {
        return webServices.fetchMovieWebService()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}