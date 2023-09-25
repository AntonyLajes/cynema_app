package com.nomargin.cynema.ui.adapter.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nomargin.cynema.data.remote.retrofit.entity.GenreModel
import com.nomargin.cynema.databinding.MovieGenreButtonBinding

class FragmentMovieDetailsGenresAdapter(private val genreList: List<GenreModel>) :
    RecyclerView.Adapter<FragmentMovieDetailsGenresAdapter.FragmentMovieDetailsGenresViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FragmentMovieDetailsGenresViewHolder {
        val item =
            MovieGenreButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FragmentMovieDetailsGenresViewHolder(item)
    }

    override fun onBindViewHolder(holder: FragmentMovieDetailsGenresViewHolder, position: Int) {
        val currentGenre = genreList[position]
        holder.item.genreButton.text = currentGenre.genreDesc
    }

    override fun getItemCount(): Int = genreList.size

    class FragmentMovieDetailsGenresViewHolder(val item: MovieGenreButtonBinding) :
        RecyclerView.ViewHolder(item.root)

}