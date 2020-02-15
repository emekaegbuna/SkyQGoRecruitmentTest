package com.example.skyqgorecruitmenttest.ui.ViewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_holder_filter.view.*

class FilterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {



    fun bind(
        language: String,
        selectedLanguage: String,
        languageClicked: (language: String) -> Unit
    ) {
        itemView.tvSearchFilter.text = language
        itemView.rbFilter.isChecked = language == selectedLanguage
        itemView.setOnClickListener {
            languageClicked(language)
        }
        itemView.rbFilter.setOnClickListener {
            languageClicked(language)
        }
    }
}