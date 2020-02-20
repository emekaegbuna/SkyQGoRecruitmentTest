package com.example.skyqgorecruitmenttest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.skyqgorecruitmenttest.data.model.Data
import com.example.skyqgorecruitmenttest.data.model.MovieRepo
import com.example.skyqgorecruitmenttest.data.repository.RepositoryImpl
import com.example.skyqgorecruitmenttest.ui.activity.MainActivity
import com.example.skyqgorecruitmenttest.viewModel.MainViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner
import java.net.UnknownHostException
import java.util.*

@RunWith(BlockJUnit4ClassRunner::class)
class MainViewModelTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @MockK
    lateinit var repository: RepositoryImpl
    private lateinit var mainViewModel: MainViewModel

    private var totalCount : Int = 1001
    private var items = mutableListOf<Data>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mainViewModel = MainViewModel(repository)
        items.add(Data(912312,"Dunkirk",2017,"History","https://goo.gl/1zUyyq"))
        items.add(Data(11241,"Jumanji: welcome to the jungle",2017,"Action","https://image.tmdb.org/t/p/w370_and_h556_bestv2/bXrZ5iHBEjH7WMidbUDQ0U2xbmr.jpg"))
        items.add(Data(55122,"The Maze Runner",2014,"Action","https://image.tmdb.org/t/p/w370_and_h556_bestv2/coss7RgL0NH6g4fC2s5atvf3dFO.jpg"))

    }

    @Test
    fun fetchRepos_withSuccess() {
        var liveMovies = MovieRepo(items)

        val expectedRepos = listOf<Data>(Data(912312,"Dunkirk",2017,"History","https://goo.gl/1zUyyq"),
            Data(11241,"Jumanji: welcome to the jungle",2017,"Action","https://image.tmdb.org/t/p/w370_and_h556_bestv2/bXrZ5iHBEjH7WMidbUDQ0U2xbmr.jpg"),
            Data(55122,"The Maze Runner",2014,"Action","https://image.tmdb.org/t/p/w370_and_h556_bestv2/coss7RgL0NH6g4fC2s5atvf3dFO.jpg"))
        every {repository.fetchMovieRepos()} returns (Single.just(liveMovies))
        every {repository.fetchMovieCache()} returns (Single.just(liveMovies.data))
        every {repository.cacheMovieRepo(any())} returns (Completable.complete())
        every {repository.clearMovieCache()} returns (Completable.complete())



        mainViewModel.fetchMovieRepos("","")


        assertEquals(expectedRepos, mainViewModel.liveMovies.value)
        assertEquals(MainViewModel.LoadingState.SUCCESS, mainViewModel.loadingState.value)
        assertEquals(null, mainViewModel.errorMessage.value)
    }

    @Test
    fun fetchRepos_emptyListReturned() {
        var liveMovies = MovieRepo(mutableListOf())

        every {repository.fetchMovieRepos()} returns (Single.just(liveMovies))
        every {repository.fetchMovieCache()} returns (Single.just(liveMovies.data))
        every {repository.cacheMovieRepo(any())} returns (Completable.complete())


        mainViewModel.fetchMovieRepos("","")


        assertEquals(null, mainViewModel.liveMovies.value)
        assertEquals("No Movies Found", mainViewModel.toastMessage.value)
    }

    @Test
    fun fetchRepos_searchAdded_returnMoviesWithSearchTerm(){
        var liveMovies = MovieRepo(items)

        val expectedRepos = listOf<Data>(Data(11241,"Jumanji: welcome to the jungle",2017,"Action","https://image.tmdb.org/t/p/w370_and_h556_bestv2/bXrZ5iHBEjH7WMidbUDQ0U2xbmr.jpg"))
        every {repository.fetchMovieRepos()} returns (Single.just(liveMovies))
        every {repository.fetchMovieCache()} returns (Single.just(liveMovies.data))
        every {repository.cacheMovieRepo(any())} returns (Completable.complete())
        every {repository.clearMovieCache()} returns (Completable.complete())


        mainViewModel.fetchMovieRepos("jumanji","")

        assertEquals(expectedRepos, mainViewModel.liveMovies.value)
        assertEquals(MainViewModel.LoadingState.SUCCESS, mainViewModel.loadingState.value)
        assertEquals(null, mainViewModel.errorMessage.value)
    }

    @Test
    fun fetchRepos_filterAdded_returnMoviesWithFilter(){
        var liveMovies = MovieRepo(items)

        val expectedRepos = listOf<Data>(Data(11241,"Jumanji: welcome to the jungle",2017,"Action","https://image.tmdb.org/t/p/w370_and_h556_bestv2/bXrZ5iHBEjH7WMidbUDQ0U2xbmr.jpg"),
            Data(55122,"The Maze Runner",2014,"Action","https://image.tmdb.org/t/p/w370_and_h556_bestv2/coss7RgL0NH6g4fC2s5atvf3dFO.jpg"))
        every {repository.fetchMovieRepos()} returns (Single.just(liveMovies))
        every {repository.fetchMovieCache()} returns (Single.just(liveMovies.data))
        every {repository.cacheMovieRepo(any())} returns (Completable.complete())
        every {repository.clearMovieCache()} returns (Completable.complete())


        mainViewModel.fetchMovieRepos("","Action")

        assertEquals(expectedRepos, mainViewModel.liveMovies.value)
        assertEquals(MainViewModel.LoadingState.SUCCESS, mainViewModel.loadingState.value)
        assertEquals(null, mainViewModel.errorMessage.value)
    }



    @Test
    fun fetchRepos_withError_cacheMoviesAvailable_returnCacheMovies() {
        val expectedRepos = listOf<Data>(Data(912312,"Dunkirk",2017,"History","https://goo.gl/1zUyyq"),
            Data(11241,"Jumanji: welcome to the jungle",2017,"Action","https://image.tmdb.org/t/p/w370_and_h556_bestv2/bXrZ5iHBEjH7WMidbUDQ0U2xbmr.jpg"),
            Data(55122,"The Maze Runner",2014,"Action","https://image.tmdb.org/t/p/w370_and_h556_bestv2/coss7RgL0NH6g4fC2s5atvf3dFO.jpg"))

        val liveMovies = MovieRepo(items)
        mainViewModel.lastFetchedTime = Date()
        every {repository.fetchMovieRepos()} returns (Single.error(UnknownHostException("Something Wrong")))
        every {repository.fetchMovieCache()} returns (Single.just(liveMovies.data))
        every {repository.cacheMovieRepo(any())} returns (Completable.complete())
        mainViewModel.fetchMovieRepos("","")


        assertEquals(expectedRepos, mainViewModel.liveMovies.value)
        assertEquals("Something Wrong", mainViewModel.toastMessage.value)
    }

    @Test
    fun fetchRepos_withError_noCacheMovies_returnError() {
        mainViewModel.lastFetchedTime = null
        every {repository.fetchMovieRepos()} returns (Single.error(RuntimeException("ABC")))
        every {repository.fetchMovieCache()} returns (Single.just(listOf()))
        every {repository.cacheMovieRepo(any())} returns (Completable.complete())

        mainViewModel.fetchMovieRepos("","")

        assertEquals(null, mainViewModel.liveMovies.value)
        assertEquals(MainViewModel.LoadingState.ERROR, mainViewModel.loadingState.value)
        assertEquals("ABC", mainViewModel.errorMessage.value)
    }

    @Test
    fun getActivityTest(){
        val activityClass = mainViewModel.getActivity()
        assertTrue(activityClass == MainActivity::class.java)
    }



    @After
    fun tearDown() {
    }
}