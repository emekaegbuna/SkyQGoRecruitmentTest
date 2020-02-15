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
    fun fetchRepos_with_success() {
        var repos = MovieRepo(items)

        val expectedRepos = listOf<Data>(Data(912312,"Dunkirk",2017,"History","https://goo.gl/1zUyyq"),
            Data(11241,"Jumanji: welcome to the jungle",2017,"Action","https://image.tmdb.org/t/p/w370_and_h556_bestv2/bXrZ5iHBEjH7WMidbUDQ0U2xbmr.jpg"),
            Data(55122,"The Maze Runner",2014,"Action","https://image.tmdb.org/t/p/w370_and_h556_bestv2/coss7RgL0NH6g4fC2s5atvf3dFO.jpg"))
        every {repository.fetchMovieRepos()} returns (Single.just(repos))

        mainViewModel.fetchMovieRepos("","")

        assertEquals(expectedRepos, mainViewModel.repos.value)
        assertEquals(MainViewModel.LoadingState.SUCCESS, mainViewModel.loadingState.value)
        assertEquals(null, mainViewModel.errorMessage.value)
    }

    @Test
    fun searchAdded_ReturnMoviesWithSearchTerm(){
        var repos = MovieRepo(items)

        val expectedRepos = listOf<Data>(Data(11241,"Jumanji: welcome to the jungle",2017,"Action","https://image.tmdb.org/t/p/w370_and_h556_bestv2/bXrZ5iHBEjH7WMidbUDQ0U2xbmr.jpg"))
        every {repository.fetchMovieRepos()} returns (Single.just(repos))

        mainViewModel.fetchMovieRepos("jumanji","")

        assertEquals(expectedRepos, mainViewModel.repos.value)
        assertEquals(MainViewModel.LoadingState.SUCCESS, mainViewModel.loadingState.value)
        assertEquals(null, mainViewModel.errorMessage.value)
    }

    @Test
    fun filterAdded_ReturnMoviesWithFilter(){
        var repos = MovieRepo(items)

        val expectedRepos = listOf<Data>(Data(11241,"Jumanji: welcome to the jungle",2017,"Action","https://image.tmdb.org/t/p/w370_and_h556_bestv2/bXrZ5iHBEjH7WMidbUDQ0U2xbmr.jpg"),
            Data(55122,"The Maze Runner",2014,"Action","https://image.tmdb.org/t/p/w370_and_h556_bestv2/coss7RgL0NH6g4fC2s5atvf3dFO.jpg"))
        every {repository.fetchMovieRepos()} returns (Single.just(repos))

        mainViewModel.fetchMovieRepos("","Action")

        assertEquals(expectedRepos, mainViewModel.repos.value)
        assertEquals(MainViewModel.LoadingState.SUCCESS, mainViewModel.loadingState.value)
        assertEquals(null, mainViewModel.errorMessage.value)
    }

    @Test
    fun fetchRepos_without_success_Nothing_Returned() {
        var repos = MovieRepo(listOf())

        every {repository.fetchMovieRepos()} returns Single.just(repos)

        mainViewModel.fetchMovieRepos("","")

        assertEquals(null, mainViewModel.repos.value)
        assertEquals(MainViewModel.LoadingState.ERROR, mainViewModel.loadingState.value)
        assertEquals("No Movies Found", mainViewModel.errorMessage.value)
    }

    @Test
    fun fetchRepos_with_NetworkError() {
        every {repository.fetchMovieRepos()} returns (Single.error(UnknownHostException("Something Wrong")))

        mainViewModel.fetchMovieRepos("","")

        assertEquals(null, mainViewModel.repos.value)
        assertEquals(MainViewModel.LoadingState.ERROR, mainViewModel.loadingState.value)
        assertEquals("No Network", mainViewModel.errorMessage.value)
    }

    @Test
    fun fetchRepos_with_otherError() {
        every {repository.fetchMovieRepos()} returns (Single.error(RuntimeException("ABC")))

        mainViewModel.fetchMovieRepos("","")

        assertEquals(null, mainViewModel.repos.value)
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