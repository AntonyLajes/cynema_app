package com.nomargin.cynema.ui.adapter.recycler_view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nomargin.cynema.R
import com.nomargin.cynema.data.local.entity.CommentAppearanceModel
import com.nomargin.cynema.databinding.ItemMovieDiscussionPostCommentBinding
import com.nomargin.cynema.util.FrequencyFunctions
import com.nomargin.cynema.util.extension.AdapterOnItemClickListenerWithView

class MovieDiscussionPostCommentAdapter(
    private val onItemClickListener: AdapterOnItemClickListenerWithView,
) : RecyclerView.Adapter<MovieDiscussionPostCommentAdapter.MovieDiscussionPostCommentViewHolder>() {

    private var commentsList: MutableList<CommentAppearanceModel> = mutableListOf()
    private lateinit var parentContext: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MovieDiscussionPostCommentViewHolder {
        parentContext = parent.context
        val item = ItemMovieDiscussionPostCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieDiscussionPostCommentViewHolder(item)
    }

    override fun onBindViewHolder(holder: MovieDiscussionPostCommentViewHolder, position: Int) {
        val currentItem = commentsList[position]
        Glide.with(parentContext)
            .load(currentItem.user?.profilePicture)
            .error(R.drawable.pic_profile_picture)
            .into(holder.item.profilePicture)
        holder.item.commentOwner.text = buildString {
            append(currentItem.user?.firstName)
            append(" ")
            append(currentItem.user?.lastName)
        }
        holder.item.commentBody.text = currentItem.body
        holder.item.commentDate.text = currentItem.timestamp
        holder.item.voteValue.text = currentItem.votes
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
            FrequencyFunctions.updateVoteColors(holder.item, parentContext, isUpVoted, isDownVoted)
        } else {
            FrequencyFunctions.updateVoteColors(
                holder.item,
                parentContext,
                isUpVoted = false,
                isDownVoted = false
            )
        }
    }

    override fun getItemCount(): Int = commentsList.size

    @SuppressLint("NotifyDataSetChanged")
    fun getDiscussionPostComments(comments: List<CommentAppearanceModel>) {
        commentsList = comments.toMutableList()
        notifyDataSetChanged()
    }

    fun getUpdatedComment(comment: CommentAppearanceModel, position: Int) {
        commentsList[position] = comment
        this.notifyItemChanged(position)
    }

    inner class MovieDiscussionPostCommentViewHolder(
        val item: ItemMovieDiscussionPostCommentBinding,
    ) : RecyclerView.ViewHolder(item.root)

}