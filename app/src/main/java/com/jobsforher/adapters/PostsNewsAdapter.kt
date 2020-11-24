package com.jobsforher.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.facebook.CallbackManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jobsforher.R
import com.jobsforher.activities.NewsFeed
import com.jobsforher.activities.ZActivityCommentPage
import com.jobsforher.activities.ZActivityGroupsDetails
import com.jobsforher.helpers.Logger
import com.jobsforher.models.GroupsPostModel
import com.jobsforher.network.responsemodels.*
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import com.squareup.picasso.Picasso
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.collections.dropLastWhile
import kotlin.collections.set
import kotlin.collections.toTypedArray


class PostsNewsAdapter(private val mDataList: ArrayList<GroupsPostModel>, val isLoggedIn: Boolean) :
    RecyclerView.Adapter<PostsNewsAdapter.MyViewHolder>() {

    public var context: Context? = null
    private var retrofitInterface: RetrofitInterface? = null
    private var callbackManager: CallbackManager? = null
    var type_commentorreply: String = "comment"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.posts_adapter_row, parent, false)

        this.context = parent.context;
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.setIsRecyclable(false)
        holder.row_username.text = mDataList[position].username
        holder.row_datetime.text = mDataList[position].created_on_str
        holder.row_description.text = Html.fromHtml(mDataList[position].description)
        holder.row_upvote.text = mDataList[position].upvote_count.toString()
        holder.row_downvote.text = mDataList[position].downvote_count.toString()
        holder.row_comment.text = mDataList[position].comments_count
        holder.status_Textview.text = "none"
        holder.status_Textview_comments.text = "none"
        if (EndPoints.PROFILE_ICON.length > 4) {
            Picasso.with(context)
                .load(EndPoints.PROFILE_ICON)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.postt_icadd)
            Picasso.with(context)
                .load(EndPoints.PROFILE_ICON)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.coment_icadd)
        }
        if (mDataList[position].profile_icon!!.isNotEmpty()) {
            Picasso.with(context)
                .load(mDataList[position].profile_icon)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.row_icon)
        } else {
            Picasso.with(context)
                .load(R.drawable.ic_default_profile_icon)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.row_icon)
        }
        Log.d("TAGG", "ADAPTER..." + holder.row_description.text)

        TODO("Pending")
        holder.view_all_comments.setOnClickListener {
            val intent = Intent(context, ZActivityCommentPage::class.java)
            intent.putExtra("comment_Id", mDataList[position].id)
            (context as Activity).startActivityForResult(intent, 1)
        }

        if (mDataList[position].post_type.toString().equals("text")) {
            holder.row_image.visibility = View.GONE
            holder.row_document.visibility = View.GONE
            holder.progressBar.setVisibility(View.GONE)
        } else if (mDataList[position].post_type.toString().equals("document")) {
            holder.row_image.visibility = View.GONE
            holder.row_document.visibility = View.VISIBLE
            holder.row_document.getSettings().setJavaScriptEnabled(true);
//            holder.row_document!!.webViewClient = object : WebViewClient() {
//                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                    view?.loadUrl(url)
//                    return true
//                }
//            }
            holder.row_document.setWebViewClient(object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    // do your stuff here
                    holder.progressBar.setVisibility(View.GONE)
                }
            })
//            "http://www3.nd.edu/~cpoellab/teaching/cse40816/android_tutorial.pdf"
            holder.row_document.getSettings().setBuiltInZoomControls(true);
            holder.row_document.setHorizontalScrollBarEnabled(true);
            holder.row_document.setVerticalScrollBarEnabled(true);
            holder.row_document.loadUrl("http://docs.google.com/gview?embedded=true&url=" + mDataList[position].url);
        } else if (mDataList[position].post_type.toString().equals("video")) {
            holder.row_image.visibility = View.GONE
            holder.progressBar.setVisibility(View.GONE)
            holder.row_document.setWebViewClient(WebViewClient())
            holder.row_document.getSettings().setJavaScriptEnabled(true)
            holder.row_document.getSettings().setJavaScriptCanOpenWindowsAutomatically(true)
            holder.row_document.getSettings().setPluginState(WebSettings.PluginState.ON)
            holder.row_document.getSettings().setMediaPlaybackRequiresUserGesture(false)
            holder.row_document.setWebChromeClient(WebChromeClient())

            var text = mDataList[position].url.toString().replace("watch?", "embed/")
            var t = extractYTId(text)
            Log.d("TAGG", "URL IS" + t)
            if (t!!.contains("v=")) {
                val splitText = t!!.split("v=")
                val v =
                    splitText[1].split("&".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                Log.d("TAGG", mDataList[position].url.toString().replace("watch?v=", "embed/"))
                holder.row_document.loadUrl("https://www.youtube.com/embed/" + v[0])
            } else {
                holder.row_document.loadUrl("https://www.youtube.com/embed/" + t)
            }
        } else {
            holder.row_document.visibility = View.GONE
            holder.progressBar.setVisibility(View.GONE)
            if (mDataList[position].url!!.isNotEmpty()) {
                Picasso.with(context)
                    .load(mDataList[position].url)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(holder.row_image)
            } else {
                Picasso.with(context)
                    .load(R.drawable.ic_default_profile_icon)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(holder.row_image)
            }
        }
        if (mDataList[position].comments_count.toString().equals("0")) {
            holder.row_comment_block.visibility = View.GONE
        } else {

            holder.row_comment_block.visibility = View.VISIBLE
            if (mDataList[position].comment_list != null) {
                holder.comment_name.setText(mDataList[position].comment_list?.username!!)
                holder.comment_datetime.setText(mDataList[position].comment_list?.created_on_str)
                holder.comment_data.setText(Html.fromHtml(mDataList[position].comment_list?.entity_value))
                holder.comment_upvote.setText(mDataList[position].comment_list?.upvote_count)
                holder.comment_downvote.setText(mDataList[position].comment_list?.downvote_count)
                holder.comment_replies.setText(mDataList[position].comment_list?.replies_counts)

                if (mDataList[position].comment_list?.profile_icon!!.isNotEmpty()) {
                    Picasso.with(context)
                        .load(mDataList[position].comment_list?.profile_icon)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(holder.comment_icon)
                } else {
                    Picasso.with(context)
                        .load(R.drawable.ic_default_profile_icon)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(holder.comment_icon)
                }
            }
        }

        holder.row_comment_block.setOnClickListener {
            checkReportComment(position)

        }

        holder.row_comment_block.setOnLongClickListener {
            checkReportComment(position)

            true
        }

        holder.upvoteblock.setOnClickListener {

            if (holder.status_Textview.text.equals("none") || holder.status_Textview.text.equals("downvote")) {
                var s: Int = holder.row_upvote.text.toString().toInt()
                s = s + 1
                holder.row_upvote.setText(s.toString())
                holder.status_Textview.setText("vote")
                doVote(
                    "post",
                    mDataList[position].id.toString(),
                    "upvote",
                    holder,
                    position
                )
            } else {
                var s: Int = holder.row_upvote.text.toString().toInt()
                s = s - 1
                if (s >= 0)
                    holder.row_upvote.setText(s.toString())
                holder.status_Textview.setText("none")
                doVote(
                    "post",
                    mDataList[position].id.toString(),
                    "upvote",
                    holder,
                    position
                )
            }

        }
        holder.downvoteblock.setOnClickListener {
            if (holder.status_Textview.text.equals("none") || holder.status_Textview.text.equals("vote")) {
                var s: Int = holder.row_downvote.text.toString().toInt()
                s = s + 1
                holder.row_downvote.setText(s.toString())
                holder.status_Textview.setText("downvote")
                doVote(
                    "post",
                    mDataList[position].id.toString(),
                    "downvote",
                    holder,
                    position
                )
            } else {
                var s: Int = holder.row_downvote.text.toString().toInt()
                s = s - 1
                if (s >= 0)
                    holder.row_downvote.setText(s.toString())
                holder.status_Textview.setText("none")
                doVote(
                    "post",
                    mDataList[position].id.toString(),
                    "downvote",
                    holder,
                    position
                )
            }
        }
        holder.commentblockbig.setOnClickListener {
            type_commentorreply = "comment"
            holder.row_comment_post.visibility = View.VISIBLE
            holder.row_comment_edittext.setText("")
        }
//        holder.shareblock.setOnClickListener {
//            holder.row_share.callOnClick()
//        }

//        holder.row_upvote.setOnClickListener {
//            var arrayList = ArrayList<String>()
//            doVote(
//                "post",
//                mDataList[position].id.toString(),
//                "upvote",
//                holder,
//                position
//            )
//        }
//        holder.row_downvote.setOnClickListener {
//            var arrayList = ArrayList<String>()
//             doVote(
//                "post",
//                mDataList[position].id.toString(),
//                "downvote",
//                holder,
//                position
//            )
//        }
//        holder.row_comment.setOnClickListener {
//            type_commentorreply = "comment"
//            holder.row_comment_post.visibility = View.VISIBLE
//            holder.row_comment_edittext.setText("")
//        }

//        holder.row_comment_edittext.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
//                val DRAWABLE_LEFT :Int = 0;
//                val DRAWABLE_TOP :Int = 1;
//                val DRAWABLE_RIGHT: Int = 2;
//                val DRAWABLE_BOTTOM: Int = 3;
//
//                if(event!!.action == MotionEvent.ACTION_UP) {
//                    if (event.getRawX() >= (holder.row_comment_edittext.getRight() - holder.row_comment_edittext.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
//                        Log.d("TAGG", "SEND" + holder.row_comment_edittext.text)
//                        if (holder.row_comment_edittext.text.toString().length>1) {
//                            if (type_commentorreply.equals("comment")) {
//                                addComment(
//                                    type_commentorreply,
//                                    holder.row_comment_edittext.text.toString(),
//                                    mDataList[position].id,
//                                    holder,
//                                    position
//                                )
//                            } else if (type_commentorreply.equals("reply")) {
//                                addReply(
//                                    type_commentorreply,
//                                    holder.row_comment_edittext.text.toString(),
//                                    mDataList[position].comment_list!!.id,
//                                    holder,
//                                    position
//                                )
//                            }
//                        }
//                        else{
//                            Toast.makeText(context, "Please enter some text", Toast.LENGTH_LONG).show()
//                        }
//                        return true;
//                    }
//                }
//                return false;
//            }
//        })


//        holder.row_comment_edittext1.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
//                val DRAWABLE_LEFT :Int = 0;
//                val DRAWABLE_TOP :Int = 1;
//                val DRAWABLE_RIGHT: Int = 2;
//                val DRAWABLE_BOTTOM: Int = 3;
//
//                if(event!!.action == MotionEvent.ACTION_UP) {
//                    if (event.getRawX()+45 >= (holder.row_comment_edittext1.getRight() - holder.row_comment_edittext1.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
//                        Log.d("TAGG", "SEND" + holder.row_comment_edittext.text)
//                        if (holder.row_comment_edittext1.text.toString().length>1) {
//                            if (type_commentorreply.equals("comment")) {
//                                addComment(
//                                    type_commentorreply,
//                                    holder.row_comment_edittext1.text.toString(),
//                                    mDataList[position].id,
//                                    holder,
//                                    position
//                                )
//                            } else if (type_commentorreply.equals("reply")) {
//                                addReply(
//                                    type_commentorreply,
//                                    holder.row_comment_edittext1.text.toString(),
//                                    mDataList[position].comment_list!!.id,
//                                    holder,
//                                    position
//                                )
//                            }
//                        }
//                        else{
//                            Toast.makeText(context, "Please enter some text", Toast.LENGTH_LONG).show()
//                        }
//                        return true;
//                    }
//                }
//                return false;
//            }
//        })

        holder.send_comment.setOnClickListener {
            if (holder.row_comment_edittext.text.toString().length > 1) {

                if (type_commentorreply.equals("comment")) {
                    addComment(
                        type_commentorreply,
                        holder.row_comment_edittext.text.toString(),
                        mDataList[position].id!!,
                        holder,
                        position
                    )
                    holder.row_comment_edittext.text.clear()
                } else if (type_commentorreply.equals("reply")) {
                    addReply(
                        type_commentorreply,
                        holder.row_comment_edittext.text.toString(),
                        mDataList[position].comment_list?.id!!,
                        holder,
                        position
                    )
                }
            } else {
                Toast.makeText(context, "Please enter some text", Toast.LENGTH_LONG).show()
            }
        }

        holder.send_replys.setOnClickListener {
            if (holder.row_comment_edittext1.text.toString().length > 1) {
                if (type_commentorreply.equals("comment")) {
                    addComment(
                        type_commentorreply,
                        holder.row_comment_edittext1.text.toString(),
                        mDataList[position].id!!,
                        holder,
                        position
                    )
                } else if (type_commentorreply.equals("reply")) {
                    addReply(
                        type_commentorreply,
                        holder.row_comment_edittext1.text.toString(),
                        mDataList[position].comment_list?.id!!,
                        holder,
                        position
                    )
                }
            } else {
                Toast.makeText(context, "Please enter some text", Toast.LENGTH_LONG).show()
            }
        }

        holder.comment_upvote_block.setOnClickListener {
            if (holder.status_Textview_comments.text.equals("none") || holder.status_Textview_comments.text.equals(
                    "downvote"
                )
            ) {
                var s: Int = holder.comment_upvote.text.toString().toInt()
                s = s + 1
                holder.comment_upvote.setText(s.toString())
                holder.status_Textview_comments.setText("vote")
                doVote(
                    "comment",
                    mDataList[position].comment_list?.id.toString(),
                    "upvote",
                    holder,
                    position
                )
            } else {
                var s: Int = holder.comment_upvote.text.toString().toInt()
                s = s - 1
                if (s >= 0)
                    holder.comment_upvote.setText(s.toString())
                holder.status_Textview_comments.setText("none")
                doVote(
                    "comment",
                    mDataList[position].comment_list?.id.toString(),
                    "upvote",
                    holder,
                    position
                )
            }
        }
        holder.comment_downvote_block.setOnClickListener {
            if (holder.status_Textview_comments.text.equals("none") || holder.status_Textview_comments.text.equals(
                    "vote"
                )
            ) {
                var s: Int = holder.comment_downvote.text.toString().toInt()
                s = s + 1
                holder.comment_downvote.setText(s.toString())
                holder.status_Textview_comments.setText("downvote")
                doVote(
                    "comment",
                    mDataList[position].comment_list?.id.toString(),
                    "downvote",
                    holder,
                    position
                )
            } else {
                var s: Int = holder.comment_downvote.text.toString().toInt()
                s = s - 1
                if (s >= 0)
                    holder.comment_downvote.setText(s.toString())
                holder.status_Textview_comments.setText("none")
                doVote(
                    "comment",
                    mDataList[position].comment_list?.id.toString(),
                    "downvote",
                    holder,
                    position
                )
            }
        }
        holder.comment_replies_block.setOnClickListener {
            type_commentorreply = "reply"
            holder.row_comment_post.visibility = View.VISIBLE
            holder.row_comment_edittext.setText("")
            holder.row_comment_post1.visibility = View.VISIBLE
            holder.row_comment_edittext1.setText("")
        }

        holder.shareblock.setOnClickListener {
            (context as NewsFeed).sharegroupdetails()
//            (context as ShareDataActivity).shareItem(mDataList[position].url)

        }

//        holder.comment_upvote.setOnClickListener {
//            doVote(
//                "comment",
//                mDataList[position].comment_list!!.id.toString(),
//                "upvote",
//                holder,
//                position
//            )
//        }
//
//        holder.comment_downvote.setOnClickListener {
//            doVote(
//                "comment",
//                mDataList[position].comment_list!!.id.toString(),
//                "downvote",
//                holder,
//                position
//            )
//        }
//
//        holder.comment_replies.setOnClickListener {
//            type_commentorreply = "reply"
//            holder.row_comment_post.visibility = View.VISIBLE
//            holder.row_comment_edittext.setText("")
//        }

        holder.see_more.setOnClickListener(object : View.OnClickListener {
            override
            fun onClick(arg0: View) {
                //openBottomSheet(arg0)
                checkReportPost(position)

            }
        })

    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var row_username: TextView
        internal var row_datetime: TextView
        internal var row_description: TextView
        internal var row_image: ImageView
        internal var row_document: WebView
        internal var row_icon: ImageView
        internal var row_upvote: TextView
        internal var row_downvote: TextView
        internal var row_comment: TextView
        internal var row_share: TextView
        internal var row_comment_block: LinearLayout
        internal var row_comment_post: LinearLayout
        internal var row_comment_post1: LinearLayout
        internal var row_comment_edittext: EditText
        internal var row_comment_edittext1: EditText
        internal var progressBar: ProgressBar
        internal var upvoteblock: LinearLayout
        internal var downvoteblock: LinearLayout
        internal var commentblockbig: LinearLayout
        internal var shareblock: LinearLayout

        internal var comment_name: TextView
        internal var comment_datetime: TextView
        internal var comment_data: TextView
        internal var comment_upvote: TextView
        internal var comment_downvote: TextView
        internal var comment_replies: TextView
        internal var comment_icon: ImageView
        internal var view_all_comments: TextView
        internal var see_more: TextView
        internal var comment_upvote_block: LinearLayout
        internal var comment_downvote_block: LinearLayout
        internal var comment_replies_block: LinearLayout
        internal var send_replys: ImageView
        internal var send_comment: ImageView
        internal var status_Textview: TextView
        internal var status_Textview_comments: TextView
        internal var postt_icadd: ImageView
        internal var coment_icadd: ImageView


        init {
            row_username = itemView.findViewById<View>(R.id.postusername) as TextView
            row_datetime = itemView.findViewById<View>(R.id.postdatetime) as TextView
            row_description = itemView.findViewById<View>(R.id.postdata) as TextView
            row_image = itemView.findViewById<View>(R.id.postimage) as ImageView
            row_document = itemView.findViewById<View>(R.id.postdocument) as WebView
            row_icon = itemView.findViewById<View>(R.id.posticon) as ImageView
            row_upvote = itemView.findViewById<View>(R.id.upvote) as TextView
            row_downvote = itemView.findViewById<View>(R.id.downvote) as TextView
            row_comment = itemView.findViewById<View>(R.id.comment) as TextView
            row_share = itemView.findViewById<View>(R.id.share) as TextView
            row_comment_block = itemView.findViewById<View>(R.id.comment_block) as LinearLayout
            row_comment_post = itemView.findViewById<View>(R.id.coment_post) as LinearLayout
            comment_name = itemView.findViewById<View>(R.id.commentusername) as TextView
            comment_datetime = itemView.findViewById<View>(R.id.commentdatetime) as TextView
            comment_data = itemView.findViewById<View>(R.id.commentdata) as TextView
            comment_upvote = itemView.findViewById<View>(R.id.comment_upvote) as TextView
            comment_downvote = itemView.findViewById<View>(R.id.comment_downvote) as TextView
            comment_replies = itemView.findViewById<View>(R.id.comment_replies) as TextView
            comment_icon = itemView.findViewById<View>(R.id.commenticon) as ImageView
            row_comment_post1 = itemView.findViewById<View>(R.id.coment_post1) as LinearLayout
            row_comment_edittext1 = itemView.findViewById<View>(R.id.coment_edittext1) as EditText
            row_comment_edittext = itemView.findViewById<View>(R.id.coment_edittext) as EditText
            view_all_comments = itemView.findViewById<View>(R.id.commentText) as TextView
            see_more = itemView.findViewById<View>(R.id.seemore) as TextView
            progressBar = itemView.findViewById<View>(R.id.progressbar) as ProgressBar
            upvoteblock = itemView.findViewById<View>(R.id.upvote_block) as LinearLayout
            downvoteblock = itemView.findViewById<View>(R.id.downvote_block) as LinearLayout
            commentblockbig = itemView.findViewById<View>(R.id.comment_blockbig) as LinearLayout
            shareblock = itemView.findViewById<View>(R.id.share_block) as LinearLayout
            comment_upvote_block =
                itemView.findViewById<View>(R.id.comment_upvote_block) as LinearLayout
            comment_downvote_block =
                itemView.findViewById<View>(R.id.comment_downvote_block) as LinearLayout
            comment_replies_block =
                itemView.findViewById<View>(R.id.comment_replies_block) as LinearLayout
            send_replys = itemView.findViewById<View>(R.id.send_reply) as ImageView
            send_comment = itemView.findViewById<View>(R.id.send_comment) as ImageView
            status_Textview = itemView.findViewById<View>(R.id.statusTextview) as TextView
            status_Textview_comments =
                itemView.findViewById<View>(R.id.status_textview_comments) as TextView

            postt_icadd = itemView.findViewById<View>(R.id.post_icadd) as ImageView
            coment_icadd = itemView.findViewById<View>(R.id.comment_icadd) as ImageView
        }
    }


    private fun doVote(
        entity_type: String,
        entity_id: String,
        vote_type: String,
        holder: MyViewHolder,
        position: Int
    ) {

        val params = HashMap<String, String>()

        val entity_type1 = entity_type
        val entity_id1 = entity_id
        val vote_type1 = vote_type

        params["entity_type"] = entity_type1
        params["entity_id"] = entity_id1
        params["vote_type"] = vote_type1

        Logger.d("TAG", "Params : " + Gson().toJson(params))

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val arrayList = ArrayList<String>()
        val call = retrofitInterface!!.getVoteData(
            "application/json",
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<VoteResponse> {

            override fun onResponse(call: Call<VoteResponse>, response: Response<VoteResponse>) {

                Log.d("TAG", "CODE" + response.code().toString() + "")
                Log.d("TAG", "MESSAGE" + response.message() + "")
                Log.d("TAG", "RESPONSE" + "" + Gson().toJson(response))
                Log.d("TAGG", "URL" + "" + response)

                //var str_response = Gson().toJson(response)
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject =
                    JSONObject(
                        str_response.substring(
                            str_response.indexOf("{"),
                            str_response.lastIndexOf("}") + 1
                        )
                    )
                val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                var jsonaobj: JSONObject = jsonObject1.optJSONObject("body")
                if (response.isSuccessful) {
                    if (responseCode == 10701) {
                        Log.d("TAGG", "VOTE" + jsonaobj.getString("downvote_count"))
                        if (entity_type1 == "post") {
                            holder.row_downvote.setText(jsonaobj.getString("downvote_count"))
                            holder.row_upvote.setText(jsonaobj.getString("upvote_count"))
                        } else if (entity_type1 == "comment") {
                            holder.comment_downvote.setText(jsonaobj.getString("downvote_count"))
                            holder.comment_upvote.setText(jsonaobj.getString("upvote_count"))
                        }
                    } else if (responseCode == 11700) {
                        deleteVote(entity_type1, entity_id1, vote_type1, holder, position)
                    }
                } else {

                }
            }

            override fun onFailure(call: Call<VoteResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })

    }

    private fun deleteVote(
        entity_type: String,
        entity_id: String,
        vote_type: String,
        holder: MyViewHolder,
        position: Int
    ) {

        val params = HashMap<String, String>()

        val entity_type2 = entity_type
        val entity_id2 = entity_id
        val vote_type2 = vote_type

        params["entity_type"] = entity_type2
        params["entity_id"] = entity_id2
        params["vote_type"] = vote_type2

        Logger.d("TAG", "Params : " + Gson().toJson(params))

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val arrayList = ArrayList<String>()
        val call = retrofitInterface!!.deleteVoteData(
            "application/json",
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<VoteResponse> {

            override fun onResponse(call: Call<VoteResponse>, response: Response<VoteResponse>) {

                Log.d("TAG", "CODE" + response.code().toString() + "")
                Log.d("TAG", "MESSAGE" + response.message() + "")
                Log.d("TAG", "RESPONSE" + "" + Gson().toJson(response))
                Log.d("TAGG", "URL" + "" + response)

                //var str_response = Gson().toJson(response)
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject =
                    JSONObject(
                        str_response.substring(
                            str_response.indexOf("{"),
                            str_response.lastIndexOf("}") + 1
                        )
                    )
                val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                var jsonaobj: JSONObject = jsonObject1.optJSONObject("body")
                if (response.isSuccessful) {
                    if (responseCode == 10702) {
                        Log.d("TAGG", "VOTE" + jsonaobj.getString("downvote_count"))
                        if (entity_type2 == "post") {
                            holder.row_downvote.setText(jsonaobj.getString("downvote_count"))
                            holder.row_upvote.setText(jsonaobj.getString("upvote_count"))
                        } else if (entity_type2 == "comment") {
                            holder.comment_downvote.setText(jsonaobj.getString("downvote_count"))
                            holder.comment_upvote.setText(jsonaobj.getString("upvote_count"))
                        }
                    } else {

                    }
                } else {

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
        holder: MyViewHolder,
        position: Int
    ) {

        val params = HashMap<String, String>()

        val entity_type3 = entity_type
        val entity_id3 = entity_value

        params["entity_type"] = entity_type3
        params["entity_value"] = entity_id3

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val arrayList = ArrayList<String>()
        val call = retrofitInterface!!.addComment(
            id,
            "application/json",
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<CreateCommentResponse> {

            override fun onResponse(
                call: Call<CreateCommentResponse>,
                response: Response<CreateCommentResponse>
            ) {

                Log.d("TAG", "CODE Value" + response.code().toString() + "")
                Log.d("TAG", "MESSAGE VAlue" + response.message() + "")
                Log.d("TAG", "RESPONSE Value" + "" + Gson().toJson(response))
                Log.d("TAGG", "URL Value" + "" + response)

                //var str_response = Gson().toJson(response)
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject =
                    JSONObject(
                        str_response.substring(
                            str_response.indexOf("{"),
                            str_response.lastIndexOf("}") + 1
                        )
                    )
                val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                var jsonaobj: JSONObject = jsonObject1.optJSONObject("body")
                if (response.isSuccessful) {
                    if (responseCode == 10301) {
                        //mDataList[position].comment_list!! =
                        Log.d("TAGG", "COMMENT" + jsonaobj.getString("id"))
                        holder.row_comment_block.visibility = View.VISIBLE
                        mDataList[position].comment_list?.id = jsonaobj.getInt("id")
//                        mDataList[position].comment_list!!.parent_id = jsonaobj.getString("parent_id")
                        mDataList[position].comment_list?.entity_type = entity_type
                        mDataList[position].comment_list?.entity_value = entity_value
                        mDataList[position].comment_list?.group_id = jsonaobj.getString("group_id")
                        mDataList[position].comment_list?.post_id = id.toString()
                        mDataList[position].comment_list?.created_by =
                            jsonaobj.getString("created_by")
                        mDataList[position].comment_list?.upvote_count =
                            jsonaobj.getString("upvote_count")
                        mDataList[position].comment_list?.downvote_count =
                            jsonaobj.getString("downvote_count")
                        mDataList[position].comment_list?.url = jsonaobj.getString("url")
                        mDataList[position].comment_list?.hash_tags =
                            jsonaobj.getString("hash_tags")
                        mDataList[position].comment_list?.status = jsonaobj.getString("status")
                        mDataList[position].comment_list?.edited = jsonaobj.getString("edited")
                        mDataList[position].comment_list?.created_on =
                            jsonaobj.getString("created_on")
                        mDataList[position].comment_list?.modified_on =
                            jsonaobj.getString("modified_on")
                        mDataList[position].comment_list?.created_on_str =
                            jsonaobj.getString("created_on_str")
//                        mDataList[position].comment_list!!.comment_count = jsonaobj.getString("comment_count")
//                        mDataList[position].comment_list!!.replies_count = jsonaobj.getString("replies_count")
                        mDataList[position].comment_list?.aggregate_count =
                            jsonaobj.getString("aggregate_count")
                        mDataList[position].comment_list?.username = jsonaobj.getString("username")
                        mDataList[position].comment_list?.email = jsonaobj.getString("email")
                        mDataList[position].comment_list?.profile_icon =
                            jsonaobj.getString("profile_icon")
                        mDataList[position].comment_list?.is_owner = jsonaobj.getString("is_owner")

                        holder.comment_name.setText(jsonaobj.getString("username"))
                        holder.comment_datetime.setText(jsonaobj.getString("created_on_str"))
                        holder.comment_data.setText(Html.fromHtml(entity_value))
                        holder.comment_upvote.setText(jsonaobj.getString("upvote_count"))
                        holder.comment_downvote.setText(jsonaobj.getString("downvote_count"))
                        holder.comment_replies.setText("0")
                        var s: Int = holder.row_comment.text.toString().toInt()
                        s = s + 1
                        holder.row_comment.setText(s.toString())
                        if (EndPoints.PROFILE_ICON.length > 4) {
                            Picasso.with(context)
                                .load(EndPoints.PROFILE_ICON)
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .into(holder.comment_icon)
                        }
                        holder.row_comment_post1.visibility = View.GONE
                    } else {

                    }
                } else {

                }
            }

            override fun onFailure(call: Call<CreateCommentResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })

    }

    private fun addReply(
        entity_type: String,
        entity_value: String,
        id: Int,
        holder: MyViewHolder,
        position: Int
    ) {

        val params = HashMap<String, String>()

        val entity_type3 = entity_type
        val entity_id3 = entity_value

        params["entity_type"] = entity_type3
        params["entity_value"] = entity_id3

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val arrayList = ArrayList<String>()
        val call = retrofitInterface!!.addReply(
            id.toInt(),
            "application/json",
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<CreateReplyResponse> {

            override fun onResponse(
                call: Call<CreateReplyResponse>,
                response: Response<CreateReplyResponse>
            ) {

                Log.d("TAG", "CODE Value" + response.code().toString() + "")
                Log.d("TAG", "MESSAGE VAlue" + response.message() + "")
                Log.d("TAG", "RESPONSE Value" + "" + Gson().toJson(response))
                Log.d("TAGG", "URL Value" + "" + response)

                //var str_response = Gson().toJson(response)
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject =
                    JSONObject(
                        str_response.substring(
                            str_response.indexOf("{"),
                            str_response.lastIndexOf("}") + 1
                        )
                    )
                val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                var jsonaobj: JSONObject = jsonObject1.optJSONObject("body")
                if (response.isSuccessful) {
                    if (responseCode == 10301) {
                        //mDataList[position].comment_list!! =
                        Log.d("TAGG", "COMMENT" + jsonaobj.getString("id"))
                        //    holder.replyblock.visibility = View.VISIBLE
//                        mDataList[position].reply_list!!.id = jsonaobj.getInt("id")
//                        mDataList[position].reply_list!!.parent_id = jsonaobj.getString("parent_id")
//                        mDataList[position].reply_list!!.entity_type = entity_type
//                        mDataList[position].reply_list!!.entity_value = entity_value
//                        mDataList[position].reply_list!!.group_id = jsonaobj.getString("group_id")
//                        mDataList[position].reply_list!!.post_id = id.toString()
//                        mDataList[position].reply_list!!.created_by = jsonaobj.getString("created_by")
//                        mDataList[position].reply_list!!.upvote_count = jsonaobj.getString("upvote_count")
//                        mDataList[position].reply_list!!.downvote_count = jsonaobj.getString("downvote_count")
//                        mDataList[position].reply_list!!.url = jsonaobj.getString("url")
//                        mDataList[position].reply_list!!.hash_tags = jsonaobj.getString("hash_tags")
//                        mDataList[position].reply_list!!.status = jsonaobj.getString("status")
//                        mDataList[position].reply_list!!.edited = jsonaobj.getString("edited")
//                        mDataList[position].reply_list!!.created_on = jsonaobj.getString("created_on")
//                        mDataList[position].reply_list!!.modified_on = jsonaobj.getString("modified_on")
//                        mDataList[position].reply_list!!.created_on_str = jsonaobj.getString("created_on_str")
////                        mDataList[position].reply_list!!.comment_count = jsonaobj.getString("comment_count")
////                        mDataList[position].reply_list!!.replies_count = jsonaobj.getString("replies_count")
//                        mDataList[position].reply_list!!.aggregate_count = jsonaobj.getString("aggregate_count")
//                        mDataList[position].reply_list!!.username = jsonaobj.getString("username")
//                        mDataList[position].reply_list!!.email = jsonaobj.getString("email")
//                        mDataList[position].reply_list!!.profile_icon = jsonaobj.getString("profile_icon")
//                        mDataList[position].reply_list!!.is_owner = jsonaobj.getString("is_owner")

//                        holder.reply_name.setText(jsonaobj.getString("username"))
//                        holder.reply_datetime.setText(jsonaobj.getString("created_on_str"))
//                        holder.reply_data.setText(Html.fromHtml(entity_value))
                        holder.comment_upvote.setText(jsonaobj.getString("upvote_count"))
                        holder.comment_downvote.setText(jsonaobj.getString("downvote_count"))
                        var s: Int = holder.comment_replies.text.toString().toInt()
                        s = s + 1
                        holder.comment_replies.setText(s.toString())
//                        holder.comment_edittext.setText("0")
                        holder.row_comment_post1.visibility = View.GONE

                        val intent = Intent(context, ZActivityCommentPage::class.java)
                        intent.putExtra("comment_Id", mDataList[position].id)
                        (context as Activity).startActivityForResult(intent, 1)
                    } else {

                    }
                } else {

                }
            }

            override fun onFailure(call: Call<CreateReplyResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })

    }

    private fun checkReportPost(pos: Int) {
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.ReportCheckPost(
            mDataList[pos].id!!,
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN
        )

        call.enqueue(object : Callback<DeletePostResponse> {
            override fun onResponse(
                call: Call<DeletePostResponse>,
                response: Response<DeletePostResponse>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("TAGG", "CODE IS " + response.body()!!.responseCode.toString())
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful && response.body()!!.responseCode.toString()
                        .equals("10802")
                ) {      //11804
                    // Toast.makeText(context,"Hello",Toast.LENGTH_LONG).show()
                    (context as NewsFeed).openBottomSheet(
                        mDataList[pos].edited?.toString(),
                        mDataList[pos].id!!,
                        mDataList[pos].description,
                        mDataList[pos].is_owner!!,
                        mDataList[pos].profile_icon.toString(),
                        mDataList[pos].url.toString(),
                        mDataList[pos].post_type.toString(),
                        "yes"
                    )
                } else {

                    (context as NewsFeed).openBottomSheet(
                        mDataList[pos].edited?.toString(),
                        mDataList[pos].id!!,
                        mDataList[pos].description,
                        mDataList[pos].is_owner!!,
                        mDataList[pos].profile_icon.toString(),
                        mDataList[pos].url.toString(),
                        mDataList[pos].post_type.toString(),
                        "no"
                    )
                }
            }

            override fun onFailure(call: Call<DeletePostResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })
    }

    private fun checkReportComment(pos: Int) {
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = mDataList[pos].comment_list?.id?.let {
            retrofitInterface!!.ReportCheckComment(
                it,
                EndPoints.CLIENT_ID,
                "Bearer " + EndPoints.ACCESS_TOKEN
            )
        }

        call?.enqueue(object : Callback<DeletePostResponse> {
            override fun onResponse(
                call: Call<DeletePostResponse>,
                response: Response<DeletePostResponse>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("TAGG", "CODE IS " + response.body()!!.responseCode.toString())
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful && response.body()!!.responseCode.toString()
                        .equals("10802")
                ) {      //11804
                    // Toast.makeText(context,"Hello",Toast.LENGTH_LONG).show()
                    (context as NewsFeed).openBottomSheetComments(
                        mDataList[pos].comment_list?.edited,
                        mDataList[pos].comment_list?.id!!,
                        mDataList[pos].comment_list?.entity_value,
                        mDataList[pos].comment_list?.is_owner,
                        mDataList[pos].comment_list?.profile_icon!!.toString(),
                        "yes"
                    )
                } else {

                    (context as NewsFeed).openBottomSheetComments(
                        mDataList[pos].comment_list?.edited,
                        mDataList[pos].comment_list?.id!!,
                        mDataList[pos].comment_list?.entity_value,
                        mDataList[pos].comment_list?.is_owner,
                        mDataList[pos].comment_list?.profile_icon!!.toString(),
                        "no"
                    )
                }
            }

            override fun onFailure(call: Call<DeletePostResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })
    }

    fun extractYTId(ytUrl: String): String? {
        var vId: String? = null
        val pattern = Pattern.compile(
            "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
            Pattern.CASE_INSENSITIVE
        )
        val matcher = pattern.matcher(ytUrl)
        if (matcher.matches()) {
            vId = matcher.group(1)
        }
        return vId
    }
}