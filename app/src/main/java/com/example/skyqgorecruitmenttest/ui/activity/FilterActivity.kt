package com.example.skyqgorecruitmenttest.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skyqgorecruitmenttest.R
import com.example.skyqgorecruitmenttest.ui.adapter.FilterAdapter
import com.example.skyqgorecruitmenttest.viewModel.MainViewModel
import com.example.skyqgorecruitmenttest.viewModel.factory.MainViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_filter.*
import javax.inject.Inject

class FilterActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel
    lateinit var adapter: FilterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        viewModel = ViewModelProviders.of(this, mainViewModelFactory).get(MainViewModel::class.java)

        viewModel.fetchMovieRepos("", intent?.getStringExtra("selected_genre") ?: "")
        viewModel.genre.observe(this, Observer {

            Log.d("GENRE TAG", it.toString())

            rv_languages.layoutManager = LinearLayoutManager(this)
            adapter = FilterAdapter(
                it,
                intent?.getStringExtra("selected_genre") ?: ""
            )
            rv_languages.adapter = adapter



        })

        btnApply.setOnClickListener {
            val intent = Intent()
            intent.putExtra("selected_genre", adapter.selectedGenre)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }


}
