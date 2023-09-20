package com.nomargin.cynema.ui.adapter.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nomargin.cynema.data.remote.entity.MovieModel
import com.nomargin.cynema.databinding.ItemMoviePosterBinding

class MoviePosterAdapter(private val movieList: List<MovieModel>) : RecyclerView.Adapter<MoviePosterAdapter.MoviePosterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviePosterViewHolder {
        val view = ItemMoviePosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoviePosterViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoviePosterViewHolder, position: Int) {
        val currentMovie = movieList[position]
        holder.moviePoster.posterItemImage.setImageResource(currentMovie.poster)
        holder.moviePoster.posterItemName.text = currentMovie.name
    }

    override fun getItemCount(): Int = movieList.size

    class MoviePosterViewHolder(val moviePoster: ItemMoviePosterBinding) :
        RecyclerView.ViewHolder(moviePoster.root)

}