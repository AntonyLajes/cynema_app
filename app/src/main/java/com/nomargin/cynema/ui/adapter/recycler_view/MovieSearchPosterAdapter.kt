package com.nomargin.cynema.ui.adapter.recycler_view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nomargin.cynema.R
import com.nomargin.cynema.data.local.entity.MovieSearchedDetailsModel
import com.nomargin.cynema.databinding.ItemSearchByQueryResultBinding
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.extension.AdapterOnItemClickListener

class MovieSearchPosterAdapter(
    private var movieDetailsList: List<MovieSearchedDetailsModel>,
    private val onItemClickListener: AdapterOnItemClickListener,
) : RecyclerView.Adapter<MovieSearchPosterAdapter.MyViewHolder>() {

    private var parentContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemSearchByQueryResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        parentContext = parent.context
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = movieDetailsList[position]
        holder.item.movieName.text = currentItem.title
        Glide.with(parentContext!!)
            .load(Constants.TMDB_PATH_URLs.posterPathUrl + currentItem.backgroundPath)
            .error(R.drawable.bg_button)
            .centerCrop()
            .into(holder.item.posterItemImage)
        setGenres(currentItem, holder)
        holder.item.movieDetails.visibility = if (currentItem.releaseDate.isNullOrEmpty()) {
            View.GONE
        } else {
            holder.item.movieDetails.text = currentItem.releaseDate.take(4)
            View.VISIBLE
        }
        holder.item.searchItem.setOnClickListener {
            onItemClickListener.onItemClickListener(currentItem, position)
        }
    }

    override fun getItemCount(): Int = movieDetailsList.size

    fun setSearchedMovies(searchedMovies: List<MovieSearchedDetailsModel>) {
        movieDetailsList = searchedMovies
        notifyDataSetChanged()
    }

    private fun setGenres(currentItem: MovieSearchedDetailsModel, holder: MyViewHolder) {
        val genreDesc: MutableList<String> = mutableListOf()
        for (genre in currentItem.genres) {
            genre.genreDesc?.let {
                genreDesc.add(it)
            }
        }
        if (genreDesc.isNotEmpty()) {
            holder.item.movieGenres.text =
                genreDesc.take(2).joinToString(" • ")
        }
    }

    inner class MyViewHolder(val item: ItemSearchByQueryResultBinding) :
        RecyclerView.ViewHolder(item.root)

}