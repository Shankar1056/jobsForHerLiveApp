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
import com.jobsforher.network.responsemodels.GroupCommentsBodyNew
import de.hdodenhof.circleimageview.CircleImageView

class NotificationCommentReplyAdapter(
    private val commentsList: ArrayList<GroupCommentsBodyNew>,
    private val replysList: ArrayList<GroupCommentReplyBodyNew>,
    private val context: Context,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<NotificationCommentReplyAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView =
            inflater.inflate(R.layout.item_notification_details, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return if (commentsList.size > 0) {
            commentsList.size
        } else {
            replysList.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (commentsList.size > 0) {
            val commentList = commentsList[position]
            holder.postusername.text = commentList.username
            holder.postdatetime.text = commentList.created_on_str
            holder.postDetails.text = commentList.entity_value?.let {
                HtmlCompat.fromHtml(
                    it,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }
            holder.like.text = commentList.upvote_count.toString()
            holder.disLike.text = commentList.downvote_count.toString()
            holder.commentCount.text = commentList.comments_count.toString()
            if (commentList.profile_icon.isNullOrEmpty()) {
                Glide.with(context).load(commentList.profile_icon).into(holder.posticon)
            }

        } else {
            val commentList = replysList[position]
            holder.postusername.text = commentList.username
            holder.postdatetime.text = commentList.created_on_str
            holder.postDetails.text = commentList.entity_value?.let {
                HtmlCompat.fromHtml(
                    it,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }
            holder.like.text = commentList.upvote_count.toString()
            holder.disLike.text = commentList.downvote_count.toString()
            holder.commentCount.text = commentList.comments_count.toString()
            if (commentList.profile_icon.isNullOrEmpty()) {
                Glide.with(context).load(commentList.profile_icon).into(holder.posticon)
            }
        }

        holder.commentCount.visibility = View.GONE
        holder.repkyRV.visibility = View.GONE

        holder.like.setOnClickListener {

            listener.onCommentLikeClicked(position)
        }
        holder.disLike.setOnClickListener {

            listener.onCommentUnLikeClicked(position)
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
        val repkyRV: RecyclerView = itemView.findViewById(R.id.repkyRV)
    }

    interface OnItemClickListener {
        fun onCommentLikeClicked(pos: Int)
        fun onCommentUnLikeClicked(pos: Int)

    }
}

