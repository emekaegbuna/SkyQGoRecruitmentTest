package com.example.skyqgorecruitmenttest.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.skyqgorecruitmenttest.data.model.MovieRepo
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single


@Dao
interface MovieDao {

    @Query("select * from MovieRepo")
    fun getMovieCache(): Single<MovieRepo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun cacheMovieRepo(movieRepo: MovieRepo): Completable
}