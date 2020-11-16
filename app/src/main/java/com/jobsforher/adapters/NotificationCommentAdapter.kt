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
import com.jobsforher.network.responsemodels.GroupCommentsBodyNew
import de.hdodenhof.circleimageview.CircleImageView

class NotificationCommentAdapter(
    private val responseList: ArrayList<GroupCommentsBodyNew>,
    private val context: Context,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<NotificationCommentAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView =
            inflater.inflate(R.layout.item_notification_details, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = responseList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val commentList = responseList[position]

        holder.postusername.text = responseList[position].username
        holder.postdatetime.text = responseList[position].created_on_str
        holder.postDetails.text = responseList[position].entity_value?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY) }
//        holder.postDetails.text = responseList[position].entity_value
        holder.like.text = responseList[position].upvote_count.toString()
        holder.disLike.text = responseList[position].downvote_count.toString()
        holder.commentCount.text = responseList[position].replies_count.toString()
        if (commentList.profile_icon.isNullOrEmpty()) {
            Glide.with(context).load(commentList.profile_icon).into(holder.posticon)
        }

        holder.repkyRV.adapter = commentList.replyModel?.let {
            NotificationReplyAdapter(
                it, context,
                object : NotificationReplyAdapter.OnReplyItemClickListener {
                    override fun onReplyLikeClick(pos: Int) {
                        listener.onReplyLikeClicked(position, pos)
                    }

                    override fun onReplyUnLikeClick(pos: Int) {

                        listener.onReplyUnLikeClicked(position, pos)
                    }


                })
        }

        holder.commentCount.setOnClickListener {
            listener.onCommentCommentClicked(position)
        }

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
        fun onCommentCommentClicked(pos: Int)
        fun onReplyLikeClicked(position : Int, pos: Int)
        fun onReplyUnLikeClicked(position : Int, pos: Int)
    }
}

