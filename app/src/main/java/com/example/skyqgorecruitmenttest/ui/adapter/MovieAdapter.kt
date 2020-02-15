package com.example.skyqgorecruitmenttest.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.skyqgorecruitmenttest.R
import com.example.skyqgorecruitmenttest.data.model.Data
import com.example.skyqgorecruitmenttest.ui.ViewHolder.MovieViewHolder
import com.example.skyqgorecruitmenttest.utils.inflate

class MovieAdapter(var movieList: MutableList<Data>): RecyclerView.Adapter<MovieViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {

        return MovieViewHolder(parent.inflate(R.layout.view_holder_repo, false))
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindItem(movieList[holder.adapterPosition])

    }

    fun updateItems(movies: List<Data>, clearOldItems: Boolean = true) {
        if (clearOldItems) {
            movieList.clear()
        }
        movieList.addAll(movies)
        if (clearOldItems) {
            notifyDataSetChanged()
        } else {
            notifyItemRangeInserted(movieList.count() - movies.count(), movies.count())
        }
    }
}