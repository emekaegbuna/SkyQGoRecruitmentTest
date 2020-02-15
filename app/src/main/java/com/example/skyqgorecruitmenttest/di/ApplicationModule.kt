package com.example.skyqgorecruitmenttest.di

import android.app.Application
import android.appwidget.AppWidgetProvider
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.skyqgorecruitmenttest.MovieApplication
import com.example.skyqgorecruitmenttest.data.db.MovieDao
import com.example.skyqgorecruitmenttest.data.db.MovieDatabase
import com.example.skyqgorecruitmenttest.data.remote.WebServices
import com.example.skyqgorecruitmenttest.data.repository.Repository
import com.example.skyqgorecruitmenttest.data.repository.RepositoryImpl
import com.example.skyqgorecruitmenttest.utils.Constant
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class ApplicationModule (){


    @Singleton
    @Provides
    fun provideRepository(webServices: WebServices, userDao: MovieDao, application: Application): RepositoryImpl {
        return RepositoryImpl(userDao, webServices, application)
    }


    @Singleton
    @Provides
    fun provideWebServices(retrofit: Retrofit): WebServices {

        return retrofit.create(WebServices::class.java)
    }


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constant.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .readTimeout(1000, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor).build()
    }


    @Provides
    @Singleton
    fun provideUserDao(database: MovieDatabase): MovieDao{
        return database.movieDao()
    }

    @Provides
    @Singleton
    fun provideUserDatabase(application: Application): MovieDatabase {

        return Room
            .databaseBuilder(application, MovieDatabase::class.java,
                Constant.DB_NAME
            )
            .build()

    }

}