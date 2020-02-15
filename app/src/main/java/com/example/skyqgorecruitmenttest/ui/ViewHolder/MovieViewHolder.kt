package com.example.skyqgorecruitmenttest.ui.ViewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skyqgorecruitmenttest.R
import com.example.skyqgorecruitmenttest.data.model.Data
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_holder_repo.view.*

class MovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bindItem(repo: Data) {
        //Picasso.get().load(repo.poster).into(itemView.ivImage)
        Glide.with(itemView)
            .load(repo.poster)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .into(itemView.ivImage)
        itemView.tv_movie_genre.text = repo.genre
    }
}