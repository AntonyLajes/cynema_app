package com.nomargin.cynema.ui.adapter.recycler_view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nomargin.cynema.data.remote.retrofit.entity.MovieModel
import com.nomargin.cynema.databinding.ItemMoviePosterBinding
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.extension.AdapterOnItemClickListener

class MoviePosterAdapter(
    private val movieList: List<MovieModel>,
    private val adapterOnItemClickListener: AdapterOnItemClickListener
    ) : RecyclerView.Adapter<MoviePosterAdapter.MoviePosterViewHolder>() {

    private lateinit var parentContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviePosterViewHolder {
        parentContext = parent.context
        val view = ItemMoviePosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoviePosterViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoviePosterViewHolder, position: Int) {
        val currentItem = movieList[position]
        Glide.with(parentContext)
            .load(Constants.TMDB_PATH_URLs.posterPathUrl + currentItem.posterPath)
            .centerCrop()
            .into(holder.moviePoster.posterItemImage)
        holder.moviePoster.posterItemName.text = currentItem.title
        holder.moviePoster.posterItem.setOnClickListener {
            adapterOnItemClickListener.onItemClickListener(
                currentItem,
                position
            )
        }
    }

    override fun getItemCount(): Int = movieList.size

    class MoviePosterViewHolder(val moviePoster: ItemMoviePosterBinding) :
        RecyclerView.ViewHolder(moviePoster.root)

}