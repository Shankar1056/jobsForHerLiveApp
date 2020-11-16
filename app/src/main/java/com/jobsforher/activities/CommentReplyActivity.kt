package com.jobsforher.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.jobsforher.R
import com.jobsforher.adapters.NotificationCommentReplyAdapter
import com.jobsforher.helpers.Logger
import com.jobsforher.models.CommentNotificationModel
import com.jobsforher.models.ReplyNotificationModel
import com.jobsforher.network.responsemodels.GroupCommentReplyBodyNew
import com.jobsforher.network.responsemodels.GroupCommentsBodyNew
import com.jobsforher.network.responsemodels.GroupPostsBodyNew
import com.jobsforher.network.responsemodels.VoteResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class CommentReplyActivity : AppCompatActivity() {
    lateinit var comentReplyAdapter: NotificationCommentReplyAdapter
    lateinit var commentList: ArrayList<GroupCommentsBodyNew>
    lateinit var replyList: ArrayList<GroupCommentReplyBodyNew>
    private var commentId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_sheet_layout)
        commentList = intent.getParcelableArrayListExtra("list")
        val body: GroupPostsBodyNew = intent.getParcelableExtra("body")
        val operation = intent.getStringExtra("operation")
        val position = intent.getIntExtra("pos", 0)

        when (operation) {
            "post" -> {
                setData(body)
                replyList = ArrayList<GroupCommentReplyBodyNew>()
                setAdapter()
            }

            "comment" -> {
                setData(commentList[position])
                replyList = commentList[position].replyModel
                commentId = commentList[position].id!!
                commentList = ArrayList<GroupCommentsBodyNew>()
                setAdapter()
            }
        }

        send_comment.setOnClickListener {
            if (coment_edittext.text.toString().trim().isNotEmpty()) {
                when (operation) {

                    "post" -> {
                        addComment(
                            "comment",
                            coment_edittext.text.toString(),
                            body.id!!,
                            position
                        )
                    }

                    "comment" -> {
                        addReply(
                            "reply",
                            coment_edittext.text.toString(),
                            commentId,
                            position
                        )
                    }

                }

            } else {
                Toast.makeText(this, "Please say somthing to post", Toast.LENGTH_SHORT).show()
            }
        }

        titleToolbar.setOnClickListener {
            finish()
        }

    }

    private fun setData(body: GroupCommentsBodyNew) {
        if (body.profile_icon != null && body.profile_icon!!.isNotEmpty()) {
            Glide.with(this).load(body.profile_icon).into(posticon)
        }
        postusername.text = body.username
        titleToolbar.text = "Reply on ${body.username}'s Comment"
        postdatetime.text = body.created_on_str
        postDetails.text =
            body.entity_value?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY) }
        like.text = body.upvote_count.toString()
        disLike.text = body.downvote_count.toString()
        commentCount.text = body.replies_count.toString()
        if (body.url != null && body.url!!.isNotEmpty()) {
            Glide.with(this).load(body.url).into(postImage)
        } else {
            postImage.visibility = View.GONE
        }

        like.setOnClickListener {
            replyList = ArrayList<GroupCommentReplyBodyNew>()
            updateLikeUnlike(
                body.id.toString(),
                "upvote",
                -1,
                "comment"
            )
        }

        disLike.setOnClickListener {
            replyList = ArrayList<GroupCommentReplyBodyNew>()
            updateLikeUnlike(
                body.id.toString(),
                "downvote",
                -1,
                "comment"
            )
        }
    }

    private fun setAdapter() {
        comentReplyAdapter = NotificationCommentReplyAdapter(
            commentList, replyList, this,
            object : NotificationCommentReplyAdapter.OnItemClickListener {
                override fun onCommentLikeClicked(pos: Int) {
                    if (commentList.isNotEmpty()) {
                        updateLikeUnlike(
                            commentList[pos].id.toString(),
                            "upvote",
                            pos,
                            "comment"
                        )
                    } else {
                        updateLikeUnlike(
                            replyList[pos].id.toString(),
                            "upvote",
                            pos,
                            "reply"
                        )
                    }
                }

                override fun onCommentUnLikeClicked(pos: Int) {
                    if (commentList.isNotEmpty()) {
                        updateLikeUnlike(
                            commentList[pos].id.toString(),
                            "downvote",
                            pos,
                            "comment"
                        )
                    } else {
                        updateLikeUnlike(
                            replyList[pos].id.toString(),
                            "downvote",
                            pos,
                            "reply"
                        )
                    }
                }


            })

        commentRv.adapter = comentReplyAdapter
        if (commentList.size > 0) {
            commentRv.scrollToPosition(commentList.size - 1)
        } else {
            commentRv.scrollToPosition(replyList.size - 1)
        }
    }

    private fun setData(body: GroupPostsBodyNew) {
        if (body.profile_icon != null && body.profile_icon!!.isNotEmpty()) {
            Glide.with(this).load(body.profile_icon).into(posticon)
        }
        postusername.text = body.username
        titleToolbar.text = "Comment on ${body.username}'s post"
        postdatetime.text = body.created_on_str
        postDetails.text =
            body.description?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY) }
        like.text = body.upvote_count.toString()
        disLike.text = body.downvote_count.toString()
        commentCount.text = body.comments_count.toString()
        if (body.url != null && body.url!!.isNotEmpty()) {
            Glide.with(this).load(body.url).into(postImage)
        } else {
            postImage.visibility = View.GONE
        }

        like.setOnClickListener {
            replyList = ArrayList<GroupCommentReplyBodyNew>()
            updateLikeUnlike(
                body.id.toString(),
                "upvote",
                -1,
                "post"
            )
        }

        disLike.setOnClickListener {
            replyList = ArrayList<GroupCommentReplyBodyNew>()
            updateLikeUnlike(
                body.id.toString(),
                "downvote",
                -1,
                "post"
            )
        }
    }

    private fun updateLikeUnlike(
        id: String,
        upDownVote: String,
        position: Int,
        entity_type: String
    ) {
        val params = HashMap<String, String>()

        params["entity_type"] = entity_type
        params["entity_id"] = id
        params["vote_type"] = upDownVote

        val retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface!!.getVoteData(
            "application/json",
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<VoteResponse> {

            override fun onResponse(call: Call<VoteResponse>, response: Response<VoteResponse>) {
                if (response.isSuccessful) {

                    if (response.body() != null) {
                        if (position >= 0) {
                            if (commentList.isNotEmpty()) {
                                commentList[position].upvote_count =
                                    response.body()!!.body?.upvote_count
                                commentList[position].downvote_count =
                                    response.body()!!.body?.downvote_count
                            } else {
                                replyList[position].upvote_count =
                                    response.body()!!.body?.upvote_count
                                replyList[position].downvote_count =
                                    response.body()!!.body?.downvote_count
                            }
                        } else {
                            if (position == -1) {
                                like.text = response.body()!!.body?.upvote_count.toString()
                                disLike.text = response.body()!!.body?.downvote_count.toString()
                            }
                        }
                    }
                    comentReplyAdapter.notifyDataSetChanged()

                } else {
                    Toast.makeText(
                        this@CommentReplyActivity,
                        response.message(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            override fun onFailure(call: Call<VoteResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })
    }


    private fun addComment(
        entity_type: String,
        entity_value: String,
        id: Int,
        pos: Int
    ) {
        val params = HashMap<String, String>()

        params["entity_type"] = entity_type
        params["entity_value"] = entity_value

        val retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val arrayList = ArrayList<String>()
        val call = retrofitInterface!!.addCommentForNotification(
            id,
            "application/json",
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<CommentNotificationModel> {

            override fun onResponse(
                call: Call<CommentNotificationModel>,
                response: Response<CommentNotificationModel>
            ) {
                coment_edittext.setText("")

                Log.i("response", response.toString())
                response.body()?.body?.let { commentList.add(it) }
                comentReplyAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<CommentNotificationModel>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })

    }

    private fun addReply(
        entity_type: String,
        entity_value: String,
        id: Int,
        pos: Int
    ) {
        val params = HashMap<String, String>()

        params["entity_type"] = entity_type
        params["entity_value"] = entity_value

        val retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val arrayList = ArrayList<String>()
        val call = retrofitInterface!!.addReplyNotification(
            id,
            "application/json",
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<ReplyNotificationModel> {

            override fun onResponse(
                call: Call<ReplyNotificationModel>,
                response: Response<ReplyNotificationModel>
            ) {
                coment_edittext.setText("")
                Log.i("response", response.toString())
                response.body()?.body?.let { replyList.add(it) }
                comentReplyAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<ReplyNotificationModel>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })

    }


}