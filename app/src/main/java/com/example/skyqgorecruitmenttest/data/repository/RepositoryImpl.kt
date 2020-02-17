package com.example.skyqgorecruitmenttest.data.repository


import android.app.Application
import com.example.skyqgorecruitmenttest.data.db.MovieDao
import com.example.skyqgorecruitmenttest.data.model.Data
import com.example.skyqgorecruitmenttest.data.model.MovieRepo
import com.example.skyqgorecruitmenttest.data.remote.WebServices
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

open class RepositoryImpl @Inject constructor(val movieDao: MovieDao, val webServices: WebServices, val application: Application) : Repository {




    override fun fetchMovieRepos(): Single<MovieRepo> {
        return webServices.fetchMovieWebService()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun fetchMovieCache(): Single<List<Data>> {
        return movieDao.getMovieCache()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun cacheMovieRepo(movie: List<Data>): Completable {
        return movieDao.cacheMovieRepo(movie)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


}