package com.jobsforher.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jobsforher.R
import com.jobsforher.network.responsemodels.GroupCommentReplyBodyNew
import de.hdodenhof.circleimageview.CircleImageView

class NotificationReplyAdapter(
    private val responseList: ArrayList<GroupCommentReplyBodyNew>,
    private val context: Context,
    private val listener: OnReplyItemClickListener
) :
    RecyclerView.Adapter<NotificationReplyAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView =
            inflater.inflate(R.layout.sub_item_notification_details, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = responseList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val GroupCommentReplyBodyNew = responseList[position]

        holder.postusername.text = GroupCommentReplyBodyNew.username
        holder.postdatetime.text = GroupCommentReplyBodyNew.created_on_str
        holder.postDetails.text = GroupCommentReplyBodyNew.entity_value?.let {
            HtmlCompat.fromHtml(
                it,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }
//        holder.postDetails.text = GroupCommentReplyBodyNew.entity_value
        holder.like.text = GroupCommentReplyBodyNew.upvote_count.toString()
        holder.disLike.text = GroupCommentReplyBodyNew.downvote_count.toString()
        if (GroupCommentReplyBodyNew.comments_count == null) {
            holder.commentCount.text = "0"
        } else {
            holder.commentCount.text = GroupCommentReplyBodyNew.comments_count.toString()
        }
        if (GroupCommentReplyBodyNew.profile_icon.isNullOrEmpty()) {
            Glide.with(context).load(GroupCommentReplyBodyNew.profile_icon).into(holder.posticon)
        }

        holder.like.setOnClickListener {
            listener.onReplyLikeClick(position)
        }
        holder.disLike.setOnClickListener {
            listener.onReplyUnLikeClick(position)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postusername: TextView = itemView.findViewById(R.id.postusername)
        val posticon: CircleImageView = itemView.findViewById(R.id.posticon)
        val postdatetime: TextView = itemView.findViewById(R.id.postdatetime)
        val postDetails: TextView = itemView.findViewById(R.id.postDetails)
        val like: TextView = itemView.findViewById(R.id.like)
        val disLike: TextView = itemView.findViewById(R.id.disLike)
        val commentCount: TextView = itemView.findViewById(R.id.commentCount)
    }

    interface OnReplyItemClickListener {
        fun onReplyLikeClick(pos: Int)
        fun onReplyUnLikeClick(pos: Int)
    }
}

