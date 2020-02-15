package com.example.skyqgorecruitmenttest.data.db

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.skyqgorecruitmenttest.MovieApplication
import com.example.skyqgorecruitmenttest.data.model.MovieRepo
import com.example.skyqgorecruitmenttest.utils.Constant


@Database(entities = [MovieRepo::class], version = 1)
abstract class MovieDatabase: RoomDatabase() {

    abstract fun movieDao(): MovieDao

    open fun getAppDataBase(context: MovieApplication): MovieDatabase? {
        return Room.databaseBuilder<MovieDatabase>(
            context,
            MovieDatabase::class.java,
            Constant.DB_NAME
        )
            .allowMainThreadQueries().build()
    }

}