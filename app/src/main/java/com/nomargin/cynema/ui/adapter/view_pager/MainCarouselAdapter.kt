package com.nomargin.cynema.ui.adapter.view_pager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nomargin.cynema.data.remote.entity.MovieModel
import com.nomargin.cynema.databinding.ItemMainCarouselBinding

class MainCarouselAdapter(private val movieList: ArrayList<MovieModel>): RecyclerView.Adapter<MainCarouselAdapter.MainCarouselViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainCarouselAdapter.MainCarouselViewHolder {
        val view = ItemMainCarouselBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MainCarouselViewHolder(view)
    }

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(holder: MainCarouselViewHolder, position: Int) {
        val currentItem = movieList[position]
        holder.itemMainCarouselBinding.carouselItemName.text = currentItem.name
        holder.itemMainCarouselBinding.carouselItemImage.setImageResource(currentItem.poster)
    }

    class MainCarouselViewHolder(val itemMainCarouselBinding: ItemMainCarouselBinding): RecyclerView.ViewHolder(itemMainCarouselBinding.root)
}