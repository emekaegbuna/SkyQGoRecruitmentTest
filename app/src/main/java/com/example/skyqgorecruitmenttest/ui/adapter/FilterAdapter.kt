package com.example.skyqgorecruitmenttest.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.skyqgorecruitmenttest.R
import com.example.skyqgorecruitmenttest.ui.ViewHolder.FilterViewHolder

class FilterAdapter(private val languages: List<String>, var selectedLanguage: String): RecyclerView.Adapter<FilterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        return FilterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_holder_filter, parent, false ))
    }

    override fun getItemCount(): Int {
        return languages.size
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(languages[holder.adapterPosition], selectedLanguage) { language ->
            selectedLanguage = language
            notifyDataSetChanged()
        }
    }
}