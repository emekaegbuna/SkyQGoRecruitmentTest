package com.example.skyqgorecruitmenttest.data.repository

import com.example.skyqgorecruitmenttest.data.model.MovieRepo
import io.reactivex.Single

interface Repository {
    fun fetchMovieRepos(): Single<MovieRepo>
}