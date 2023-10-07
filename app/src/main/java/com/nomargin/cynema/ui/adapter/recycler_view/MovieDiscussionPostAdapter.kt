package com.nomargin.cynema.ui.adapter.recycler_view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nomargin.cynema.R
import com.nomargin.cynema.data.local.entity.PostAppearanceModel
import com.nomargin.cynema.databinding.ItemDiscussionPostBinding
import com.nomargin.cynema.util.extension.AdapterOnItemClickListenerWithView

class MovieDiscussionPostAdapter(
    private val onItemClickListener: AdapterOnItemClickListenerWithView,
) :
    RecyclerView.Adapter<MovieDiscussionPostAdapter.MovieDiscussionPostViewHolder>() {

    private var postsList: MutableList<PostAppearanceModel> = mutableListOf()
    private lateinit var parentContext: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MovieDiscussionPostViewHolder {
        parentContext = parent.context
        val view =
            ItemDiscussionPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieDiscussionPostViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieDiscussionPostViewHolder, position: Int) {
        val currentItem = postsList[position]
        holder.item.postTitle.text = currentItem.title
        holder.item.postBody.text = currentItem.body
        Glide.with(parentContext)
            .load(currentItem.user?.profilePicture)
            .error(R.drawable.pic_profile_picture)
            .into(holder.item.profilePicture)
        holder.item.postOwner.text = buildString {
            append(parentContext.getString(R.string.posted_by))
            append(" ")
            append(currentItem.user?.firstName)
            append(" ")
            append(currentItem.user?.lastName)
        }
        holder.item.postDate.text = currentItem.timestamp
        holder.item.upVoteValue.text = currentItem.votes
        holder.item.answerValue.text = currentItem.commentsQuantity
        holder.item.post.setOnClickListener {
            onItemClickListener.onItemClickListener(it, currentItem, position)
        }
        holder.item.buttonUpVote.setOnClickListener {
            onItemClickListener.onItemClickListener(it, currentItem, position)
        }
        holder.item.buttonDownVote.setOnClickListener {
            onItemClickListener.onItemClickListener(it, currentItem, position)
        }

        if (currentItem.usersWhoVoted.contains(currentItem.currentUser?.uid ?: "")) {
            val isUpVoted = currentItem.usersWhoUpVoted.contains(currentItem.currentUser?.uid ?: "")
            val isDownVoted =
                currentItem.usersWhoDownVoted.contains(currentItem.currentUser?.uid ?: "")
            holder.updateVoteColors(isUpVoted, isDownVoted)
        } else {
            holder.updateVoteColors(isUpVoted = false, isDownVoted = false)
        }
    }

    override fun getItemCount(): Int = postsList.size

    @SuppressLint("NotifyDataSetChanged")
    fun getDiscussionPosts(posts: List<PostAppearanceModel>) {
        postsList = posts.toMutableList()
        notifyDataSetChanged()
    }

    fun getUpdatedPost(post: PostAppearanceModel, position: Int) {
        postsList[position] = post
        this.notifyItemChanged(position)
    }

    inner class MovieDiscussionPostViewHolder(
        val item: ItemDiscussionPostBinding,
    ) : RecyclerView.ViewHolder(item.root) {
        fun updateVoteColors(isUpVoted: Boolean, isDownVoted: Boolean) {
            when {
                isUpVoted -> {
                    setVoteItems(
                        item,
                        itemView.context,
                        R.drawable.ic_up_voted,
                        R.drawable.ic_down_vote,
                        R.color.color_primary
                    )
                }

                isDownVoted -> {
                    setVoteItems(
                        item,
                        itemView.context,
                        R.drawable.ic_up_vote,
                        R.drawable.ic_down_voted,
                        R.color.red
                    )
                }

                else -> {
                    setVoteItems(
                        item,
                        itemView.context,
                        R.drawable.ic_up_vote,
                        R.drawable.ic_down_vote,
                        R.color.custom_black
                    )
                }
            }
        }
    }

    private fun setVoteItems(
        item: ItemDiscussionPostBinding,
        itemViewContext: Context,
        @DrawableRes upVoteRes: Int,
        @DrawableRes downVoteRes: Int,
        @ColorRes voteValueColor: Int,
    ) {
        item.buttonUpVote.setImageDrawable(
            AppCompatResources.getDrawable(
                parentContext,
                upVoteRes
            )
        )
        item.buttonDownVote.setImageDrawable(
            AppCompatResources.getDrawable(
                parentContext,
                downVoteRes
            )
        )
        item.upVoteValue.setTextColor(
            ContextCompat.getColor(itemViewContext, voteValueColor)
        )
    }
}