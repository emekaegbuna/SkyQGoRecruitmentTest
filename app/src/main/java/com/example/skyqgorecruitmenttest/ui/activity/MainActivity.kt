package com.example.skyqgorecruitmenttest.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skyqgorecruitmenttest.MyTextWatcher
import com.example.skyqgorecruitmenttest.R
import com.example.skyqgorecruitmenttest.di.DaggerActivityComponent
import com.example.skyqgorecruitmenttest.di.NetworkModule
import com.example.skyqgorecruitmenttest.di.RepositoryModule
import com.example.skyqgorecruitmenttest.ui.adapter.MovieAdapter
import com.example.skyqgorecruitmenttest.utils.toTimeDuration
import com.example.skyqgorecruitmenttest.viewModel.MainViewModel
import com.example.skyqgorecruitmenttest.viewModel.factory.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel
    private lateinit var movieAdapter: MovieAdapter

    var filter: String = ""

    val RC_FILTER = 1001

    var apiStartTime: Long = System.currentTimeMillis()

    var isLoadingMoreData = false

    var clearOldItems = true

    var PER_PAGE_ITEM = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        movieAdapter = MovieAdapter(mutableListOf())
        val layoutManager = GridLayoutManager(this, 3)
        rvRepos.layoutManager = layoutManager
        rvRepos.adapter = movieAdapter
        rvRepos.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!isLoadingMoreData) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemsCount = layoutManager.itemCount
                    val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                    if (pastVisibleItems + visibleItemCount >= totalItemsCount) {
                        fetchMoreData(totalItemsCount)
                    }
                }

            }
        })
        DaggerActivityComponent.builder()
            .networkModule(NetworkModule())
            .repositoryModule(RepositoryModule())
            .build()
            .inject(this)

        viewModel = ViewModelProviders.of(this, mainViewModelFactory).get(MainViewModel::class.java)

        viewModel.repos.observe(this, Observer {
            isLoadingMoreData = false
            movieAdapter.updateItems(it, clearOldItems)
        })

        viewModel.totalCount.observe(this, Observer {

            val currentTime = System.currentTimeMillis()
            val elapsedTime = currentTime - apiStartTime
            tvDisplay.text = getString(R.string.total_repo_count, it, elapsedTime.toTimeDuration())
        })

        viewModel.errorMessage.observe(this, Observer {
            isLoadingMoreData = false
            tvErrorMessage.text = it
        })

        viewModel.toastMessage.observe(this, Observer {
            isLoadingMoreData = false
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        })

        viewModel.loadingState.observe(this, Observer {
            when (it) {
                MainViewModel.LoadingState.LOADING -> displayProgressbar()
                MainViewModel.LoadingState.SUCCESS -> displayRepositories()
                MainViewModel.LoadingState.ERROR -> displayErrorMessage()
                else -> displayErrorMessage()
            }
        })
        if (viewModel.lastFetchedTime == null) {
            fetchRepos()
        }
        btnRetry.setOnClickListener {
            fetchRepos()
        }

        etSearch.addTextChangedListener(object : MyTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)
                clearOldItems = true
                fetchRepos(s.toString(), filter)
            }
        })

        tvFilter.setOnClickListener {
            val intent = Intent(this, FilterActivity::class.java)
            intent.putExtra("selected_language", filter)
            startActivityForResult(intent, RC_FILTER)
        }
    }

    private fun fetchMoreData(totalItemCount: Int) {
        isLoadingMoreData = true
        Toast.makeText(
            this@MainActivity,
            "Reached end of recyclerview with no of items: $totalItemCount",
            Toast.LENGTH_SHORT
        ).show()
        clearOldItems = false

        if (etSearch.text.toString() == "") {
            fetchRepos("org:github", filter,(totalItemCount / PER_PAGE_ITEM) + 1)
        } else {
            fetchRepos(etSearch.text.toString(), filter,(totalItemCount / PER_PAGE_ITEM) + 1)
        }
    }

    private fun displayProgressbar() {
        progressbar.visibility = View.VISIBLE
        rvRepos.visibility = View.GONE
        tvErrorMessage.visibility = View.GONE
        btnRetry.visibility = View.GONE
        tvDisplay.visibility = View.GONE
    }

    private fun displayErrorMessage() {
        progressbar.visibility = View.GONE
        rvRepos.visibility = View.GONE
        tvErrorMessage.visibility = View.VISIBLE
        btnRetry.visibility = View.VISIBLE
        tvDisplay.visibility = View.GONE
    }

    private fun displayRepositories() {
        rvRepos.visibility = View.VISIBLE
        progressbar.visibility = View.GONE
        tvErrorMessage.visibility = View.GONE
        btnRetry.visibility = View.GONE
        tvDisplay.visibility = View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RC_FILTER -> {
                if (resultCode == Activity.RESULT_OK) {
                    filter = data?.getStringExtra("selected_language") ?: ""
                    clearOldItems = true
                    if (etSearch.text.toString() == "") {
                        fetchRepos(language = filter)
                    } else {
                        fetchRepos(etSearch.text.toString(), filter)
                    }
                }
            }
            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    fun fetchRepos(query: String = "org:github", language: String = "", pageNo: Int = 1) {
        apiStartTime = System.currentTimeMillis()
        viewModel.fetchMovieRepos(query, language, clearOldItems)
    }
}
