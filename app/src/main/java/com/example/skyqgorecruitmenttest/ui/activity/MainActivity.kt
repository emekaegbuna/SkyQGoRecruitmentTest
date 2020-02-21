package com.example.skyqgorecruitmenttest.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skyqgorecruitmenttest.MyTextWatcher
import com.example.skyqgorecruitmenttest.R
import com.example.skyqgorecruitmenttest.data.model.Data
import com.example.skyqgorecruitmenttest.ui.adapter.MovieAdapter
import com.example.skyqgorecruitmenttest.utils.toTimeDuration
import com.example.skyqgorecruitmenttest.viewModel.MainViewModel
import com.example.skyqgorecruitmenttest.viewModel.factory.MainViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_holder_filter.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel
    private lateinit var movieAdapter: MovieAdapter
    var apiStartTime: Long = System.currentTimeMillis()
    var movieGenre: String = ""
    var query: String = ""

    val filterRequestCode = 1001



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this, mainViewModelFactory).get(MainViewModel::class.java)


        viewModel.liveMovies.observe(this, Observer {
            setupRecycleView(it)
        })

        viewModel.toastMessage.observe(this, Observer {

            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.errorMessage.observe(this, Observer {

            tvErrorMessage.text = it
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
                fetchRepos(s.toString(), movieGenre)
            }
        })

        tvFilter.setOnClickListener {
            val intent = Intent(this, FilterActivity::class.java)
            intent.putExtra("selected_genre", movieGenre)
            startActivityForResult(intent, filterRequestCode)
        }
    }

    private fun setupRecycleView(movies: List<Data>){
        movieAdapter = MovieAdapter(movies.toMutableList())
        val layoutManager = GridLayoutManager(this, 3)
        rvRepos.layoutManager = layoutManager
        rvRepos.adapter = movieAdapter
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
             filterRequestCode -> {
                if (resultCode == Activity.RESULT_OK) {
                    movieGenre = data?.getStringExtra("selected_genre") ?: ""

                    tvDisplay.text = movieGenre
                    if (etSearch.text.toString() == "") {
                        fetchRepos(movieGenre = movieGenre)
                    } else {
                        fetchRepos(etSearch.text.toString(), movieGenre)

                    }
                }
            }
            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    fun fetchRepos(query: String = "", movieGenre: String = "") {
        apiStartTime = System.currentTimeMillis()
        viewModel.fetchMovieRepos(query, movieGenre)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                viewModel.fetchMovieRepos(query, movieGenre)
                true
            }
            else -> super.onOptionsItemSelected(item)

        }

    }

}
