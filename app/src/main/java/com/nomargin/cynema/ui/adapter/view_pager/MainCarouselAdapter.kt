package com.nomargin.cynema.ui.adapter.view_pager

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nomargin.cynema.databinding.ItemMainCarouselBinding
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.extension.AdapterOnItemClickListener
import com.nomargin.cynema.util.model.CarouselModel

class MainCarouselAdapter(
    private val movieList: List<CarouselModel>,
    private val adapterOnItemClickListener: AdapterOnItemClickListener,
) : RecyclerView.Adapter<MainCarouselAdapter.MainCarouselViewHolder>() {

    private lateinit var parentContext: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainCarouselViewHolder {
        parentContext = parent.context
        val view =
            ItemMainCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainCarouselViewHolder(view)
    }

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(holder: MainCarouselViewHolder, position: Int) {
        val currentItem = movieList[position]
        holder.itemMainCarouselBinding.carouselItemName.text = currentItem.title
        Glide.with(parentContext)
            .load(Constants.TMDB_PATH_URLs.posterPathUrl + currentItem.backgroundPath)
            .centerCrop()
            .into(holder.itemMainCarouselBinding.carouselItemImage)
        holder.itemMainCarouselBinding.moreInfoItem.setOnClickListener {
            adapterOnItemClickListener.onItemClickListener(
                currentItem,
                position
            )
        }
        val genreDesc: MutableList<String> = mutableListOf()
        for (genre in currentItem.genres) {
            genre.genreDesc?.let {
                genreDesc.add(it)
            }
        }
        if (genreDesc.isNotEmpty()) {
            holder.itemMainCarouselBinding.carouselItemGenre.text =
                genreDesc.take(2).joinToString(" • ")
        }
    }

    class MainCarouselViewHolder(val itemMainCarouselBinding: ItemMainCarouselBinding) :
        RecyclerView.ViewHolder(itemMainCarouselBinding.root)
}