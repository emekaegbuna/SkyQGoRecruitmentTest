package com.example.skyqgorecruitmenttest.data.repository

import com.example.skyqgorecruitmenttest.data.model.MovieRepo
import io.reactivex.Completable
import io.reactivex.Single

interface Repository {
    fun fetchMovieRepos(): Single<MovieRepo>
/*    fun fetchMovieCache(): Single<MovieRepo>
    fun cacheMovieRepo(movieRepo: MovieRepo):Completable*/
}