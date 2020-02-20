package com.example.skyqgorecruitmenttest.data.remote

import com.example.skyqgorecruitmenttest.data.model.Data
import com.example.skyqgorecruitmenttest.data.model.MovieRepo
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET

interface WebServices {

    @GET("movies")
    fun fetchMovieWebService(): Single<MovieRepo>

    @GET("movies")
    fun fetchMovieWebServiceRawList(): Single<List<Data>>
}