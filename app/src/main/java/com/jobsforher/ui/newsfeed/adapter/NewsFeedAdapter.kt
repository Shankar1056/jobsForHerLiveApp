package com.jobsforher.ui.newsfeed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.jobsforher.BR
import com.jobsforher.R
import com.jobsforher.data.model.NewsPostBody
import com.jobsforher.databinding.ItemNewsFeedBinding

class NewsFeedAdapter(
    private val list: ArrayList<NewsPostBody>,
    private val listener: NewsFeedClickLietener
) :
    RecyclerView.Adapter<NewsFeedAdapter.ViewHolder>() {
    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {


        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_news_feed, parent, false
        )

        return ViewHolder(binding as ItemNewsFeedBinding)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        val imageModelList = list[position]
        holder.bind(imageModelList)
    }

    override fun getItemCount(): Int {
        return if (list.size > 2) 2 else list.size
    }

    inner class ViewHolder(private var itemRowBinding: ItemNewsFeedBinding) :
        RecyclerView.ViewHolder(itemRowBinding.root) {

        fun bind(obj: Any) {
            itemRowBinding.setVariable(BR.model, obj)
            itemRowBinding.executePendingBindings()
            handleClickListener(itemRowBinding, adapterPosition)

        }
    }

    private fun handleClickListener(itemRowBinding: ItemNewsFeedBinding, position: Int) {
        itemRowBinding.upvote.setOnClickListener {
            listener.onUpVoteClicked(position)
        }
        itemRowBinding.upvoteText.setOnClickListener {
            listener.onUpVoteClicked(position)
        }
        itemRowBinding.downvote.setOnClickListener {
            listener.onDownVoteClicked(position)
        }
        itemRowBinding.downvoteText.setOnClickListener {
            listener.onDownVoteClicked(position)
        }
        itemRowBinding.comment.setOnClickListener {
            listener.onCommentCountClicked(position)
        }
        itemRowBinding.commentText.setOnClickListener {
            listener.onCommentCountClicked(position)
        }
        itemRowBinding.sendComment.setOnClickListener {
            listener.onCommentClicked(position, itemRowBinding.comentEdittext.text.toString())
        }
    }

}

interface NewsFeedClickLietener {
    fun onUpVoteClicked(pos: Int)
    fun onDownVoteClicked(pos: Int)
    fun onCommentCountClicked(pos: Int)
    fun onCommentClicked(pos: Int, comment: String)
}
