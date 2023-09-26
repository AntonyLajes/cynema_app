package com.nomargin.cynema.ui.adapter.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nomargin.cynema.databinding.ItemDiscussionPostBinding

class MovieDiscussionPostAdapter(): RecyclerView.Adapter<MovieDiscussionPostAdapter.MovieDiscussionPostViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieDiscussionPostViewHolder {
        val view = ItemDiscussionPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieDiscussionPostViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieDiscussionPostViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = 10

    inner class MovieDiscussionPostViewHolder(
        val item: ItemDiscussionPostBinding
    ) : RecyclerView.ViewHolder(item.root)

}