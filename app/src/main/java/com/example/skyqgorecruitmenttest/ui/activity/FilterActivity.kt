package com.example.skyqgorecruitmenttest.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skyqgorecruitmenttest.R
import com.example.skyqgorecruitmenttest.ui.adapter.FilterAdapter
import kotlinx.android.synthetic.main.activity_filter.*

class FilterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        rv_languages.layoutManager = LinearLayoutManager(this)
        val adapter =
            FilterAdapter(
                listOf("title", "genre"),
                intent?.getStringExtra("selected_language") ?: ""
            )
        rv_languages.adapter = adapter

        btnApply.setOnClickListener {
            val intent = Intent()
            intent.putExtra("selected_language", adapter.selectedLanguage)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
