package com.example.skyqgorecruitmenttest.data.db

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.skyqgorecruitmenttest.MovieApplication
import com.example.skyqgorecruitmenttest.data.model.Data
import com.example.skyqgorecruitmenttest.data.model.MovieRepo
import com.example.skyqgorecruitmenttest.utils.Constant


@Database(entities = [Data::class], version = 1, exportSchema = false)
abstract class MovieDatabase: RoomDatabase() {

    abstract fun movieDao(): MovieDao

}