package com.nomargin.cynema.ui.adapter.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nomargin.cynema.data.remote.retrofit.entity.GenreModel
import com.nomargin.cynema.databinding.GenreButtonBinding

class FragmentHomeGenresAdapter(private val genreList: List<GenreModel>): RecyclerView.Adapter<FragmentHomeGenresAdapter.FragmentHomeGenresViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FragmentHomeGenresViewHolder {
        val item = GenreButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FragmentHomeGenresViewHolder(item)
    }

    override fun onBindViewHolder(holder: FragmentHomeGenresViewHolder, position: Int) {
        val currentGenre = genreList[position]
        holder.item.genreButton.text = currentGenre.genreDesc
    }

    override fun getItemCount(): Int = genreList.size

    class FragmentHomeGenresViewHolder(val item: GenreButtonBinding) :
        RecyclerView.ViewHolder(item.root)

}