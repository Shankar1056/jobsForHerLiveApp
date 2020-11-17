package com.jobsforher.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.jobsforher.R
import com.jobsforher.adapters.NotificationCommentAdapter
import com.jobsforher.helpers.Logger
import com.jobsforher.models.PostDetailsModel
import com.jobsforher.network.responsemodels.*
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import kotlinx.android.synthetic.main.activity_notification_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class DetailsNotificationActivity : AppCompatActivity() {
    private var list = ArrayList<GroupCommentsBodyNew>()
    private var list1 = GroupCommentsBodyNew()
    private var postCommentId: Int? = null
    private var replyReqCount: Int = 0
    private var replyResCount: Int = 0
    private var postId: Int? = null
    private var commentId: Int? = null
    private var replytId: Int? = null
    private lateinit var notificationAdapter: NotificationCommentAdapter
    private lateinit var body: GroupPostsBodyNew

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_notification_details)

        postId =
            Integer.parseInt(intent.getStringExtra("post_id")) //100 // Get this value from Notification

        Log.d("TAGG", "" + postId)
        if (intent.hasExtra("comment_id"))
            commentId =
                Integer.parseInt(intent.getStringExtra("comment_id")) //100 // Get this value from Notification
        if (intent.hasExtra("reply_id"))
            replytId =
                Integer.parseInt(intent.getStringExtra("reply_id"))//99 // Get this value from Notification


       // postId?.let { getNotificationPostDetails(it) }
        Log.d("TAGG", "" + commentId)
        Log.d("TAGG", "" + replytId)

        setSupportActionBar(toolbar)

//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white)

        loadPrev.setOnClickListener {
            postCommentId?.let { it1 -> getComment(it1) }
        }

        toolbar.setNavigationOnClickListener {
            finish()
        }

        comment.setOnClickListener {

            startActivity(
                Intent(
                    this@DetailsNotificationActivity,
                    CommentReplyActivity::class.java
                ).putExtra("list", list).putExtra("body", body).putExtra("operation", "post")
                    .putExtra("pos", 0)
            )


        }

    }


    private fun getNotificationPostDetails(post_id: Int) {

        val call = RetrofitClient.client!!.create(RetrofitInterface::class.java)!!
            .getPostDetails(
                post_id,
                EndPoints.CLIENT_ID,
                "Bearer " + EndPoints.ACCESS_TOKEN
            )

        call.enqueue(object : Callback<PostDetailsModel> {
            override fun onResponse(
                call: Call<PostDetailsModel>,
                response: Response<PostDetailsModel>
            ) {
                val response = response.body()?.body
                if (response != null) {
                    body = response
                    setdata()
                    getCommentNotification(body)
                }
            }

            override fun onFailure(call: Call<PostDetailsModel>, t: Throwable) {
                Logger.d("TAGG", "Apply Job FAILED : $t")
            }
        })
    }

    private fun getCommentNotification(body: GroupPostsBodyNew) {
        if (body?.comments_count!! > 0) {
            if (body?.comments_count!! > 1) {
                loadPrev.visibility = View.VISIBLE
            }
            if (commentId != null) {
                postCommentId = body.id
                commentId?.let { getComment(postCommentId!!) }
            } else {
                body.id?.let { getComment(it) }
            }
        }
    }

    private fun setdata() {
        if (body.profile_icon != null && body.profile_icon!!.isNotEmpty()) {
            Glide.with(this).load(body.profile_icon).into(posticon)
        }
        postusername.text = body.username
        postdatetime.text = body.created_on_str
        postDetails.text =
            body.description?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY) }
//        postDetails.text = body.description
        like.text = body.upvote_count.toString()
        disLike.text = body.downvote_count.toString()
        commentCount.text = body.comments_count.toString()
       // shareCount.text = body.comments_count.toString()
        if (body.url != null && body.url!!.isNotEmpty()) {
            Glide.with(this).load(body.url).into(postImage)
        } else {
            postImage.visibility = View.GONE
        }

        upvote.setOnClickListener {
            updateLikeUnlike(body.id.toString(), "upvote", list, -1, -2, "post")
        }

        downvote.setOnClickListener {
            updateLikeUnlike(body.id.toString(), "downvote", list, -1, -2, "post")
        }

    }

    private fun getComment(id: Int) {

        val call = RetrofitClient.client!!.create(RetrofitInterface::class.java)!!
            .getPostComments(
                id,
                EndPoints.CLIENT_ID,
                "Bearer " + EndPoints.ACCESS_TOKEN
            )

        call.enqueue(object : Callback<GroupCommentsNew> {
            override fun onResponse(
                call: Call<GroupCommentsNew>,
                response: Response<GroupCommentsNew>
            ) {

                val body = response.body()?.body
                if (body != null) {
                    list.clear()
                    list.addAll(body)
                    if (replytId != null ) {
                        replyReqCount++
                        replytId?.let {
                        getReply(commentId!!, 0)
                        }
                    } else {
                        for (i in 0 until body.size) {
                            if (body[i].replies_count!! > 0) {
                                replyReqCount++
                                body[i].id?.let { getReply(it, i) }
                            }
                        }
                    }
                    if (replyReqCount == 0) {
                        setAdapter()
                    }
                }
            }

            override fun onFailure(call: Call<GroupCommentsNew>, t: Throwable) {
                Logger.d("TAGG", "Apply Job FAILED : $t")
            }
        })

    }



    private fun getReply(groupId: Int, position: Int) {

        Log.d("TAGG","id"+groupId)
        val call = RetrofitClient.client!!.create(RetrofitInterface::class.java)!!
            .getNotificationCommentsReply(
                groupId,
                EndPoints.CLIENT_ID,
                "Bearer " + EndPoints.ACCESS_TOKEN
            )

        call.enqueue(object : Callback<GroupReplyNotificationModel> {
            override fun onResponse(
                call: Call<GroupReplyNotificationModel>,
                response: Response<GroupReplyNotificationModel>
            ) {
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE follow company", "" + Gson().toJson(response))

                val body = response.body()?.body
                if (body != null) {
                    replyResCount++
                    list[position].replyModel?.addAll(body)
                }

                if (replyReqCount == replyResCount) {
                    setAdapter() // body is in Object when there is no data, It should be in Array. Will be modified later.
                }
            }

            override fun onFailure(call: Call<GroupReplyNotificationModel>, t: Throwable) {
                Logger.d("TAGG", "Apply Job FAILED : $t")
                setAdapter()
            }
        })

    }

    private fun setAdapter() {
        notificationAdapter = NotificationCommentAdapter(
            list, this@DetailsNotificationActivity,
            object : NotificationCommentAdapter.OnItemClickListener {
                override fun onCommentLikeClicked(pos: Int) {

                    updateLikeUnlike(list[pos].id.toString(), "upvote", list, pos, -2, "comment")

                }

                override fun onCommentUnLikeClicked(pos: Int) {

                    updateLikeUnlike(list[pos].id.toString(), "downvote", list, pos, -2, "comment")
                }

                override fun onCommentCommentClicked(pos: Int) {
                    startActivity(
                        Intent(
                            this@DetailsNotificationActivity,
                            CommentReplyActivity::class.java
                        ).putExtra("list", list).putExtra("body", body)
                            .putExtra("operation", "comment").putExtra("pos", pos)
                    )
                }

                override fun onReplyLikeClicked(position: Int, pos: Int) {
                    updateLikeUnlike(
                        list[position].replyModel[pos].id.toString(),
                        "upvote",
                        list,
                        pos,
                        position, "reply"
                    )
                }

                override fun onReplyUnLikeClicked(position: Int, pos: Int) {
                    updateLikeUnlike(
                        list[position].replyModel[pos].id.toString(),
                        "downvote",
                        list,
                        pos,
                        position,
                        "reply"
                    )
                }

            })

        notifyAdapter()
    }

    private fun updateLikeUnlike(
        id: String,
        upDownVote: String,
        list: ArrayList<GroupCommentsBodyNew>,
        pos: Int,
        position: Int, entity_type: String
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
                            list[position].replyModel[pos].upvote_count =
                                response.body()!!.body?.upvote_count
                            list[position].replyModel[pos].downvote_count =
                                response.body()!!.body?.downvote_count
                        } else {
                            if (pos == -1) {
                                like.text = response.body()!!.body?.upvote_count.toString()
                                disLike.text = response.body()!!.body?.downvote_count.toString()
                            } else {
                                list[pos].upvote_count = response.body()!!.body?.upvote_count
                                list[pos].downvote_count = response.body()!!.body?.downvote_count
                            }
                        }
                    }
                    //notificationAdapter.notifyDataSetChanged()

                } else {
                    Toast.makeText(
                        this@DetailsNotificationActivity,
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

    private fun notifyAdapter() {
        commentRv.adapter = notificationAdapter
    }

    override fun onResume() {
        super.onResume()
        postId?.let { getNotificationPostDetails(it) }
    }


}
