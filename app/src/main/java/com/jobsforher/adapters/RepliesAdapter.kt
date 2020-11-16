package com.jobsforher.adapters

import android.content.Context
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.facebook.CallbackManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.jobsforher.R
import com.jobsforher.activities.ZActivityRepliesPage
import com.jobsforher.helpers.Logger
import com.jobsforher.models.GroupsReplyModel
import com.jobsforher.network.responsemodels.CreateReplyResponse
import com.jobsforher.network.responsemodels.DeletePostResponse
import com.jobsforher.network.responsemodels.VoteResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class RepliesAdapter(private val mDataList: ArrayList<GroupsReplyModel>) : RecyclerView.Adapter<RepliesAdapter.MyViewHolder>() {

    public var context: Context? = null
    private var retrofitInterface: RetrofitInterface? = null
    private var callbackManager: CallbackManager? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.replies_adapter_row, parent, false)

        this.context = parent.context;
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.reply_name.setText(mDataList[position].username)
//        holder.reply_datetime.setText(mDataList[position].reply_list!!.created_on_str)
        holder.reply_data.setText(Html.fromHtml(mDataList[position].entity_value))
//        holder.reply_upvote.setText(mDataList[position].reply_list!!.upvote_count)
//        holder.reply_downvote.setText(mDataList[position].reply_list!!.downvote_count)
        holder.reply_replies.setText("")

        if(mDataList[position].profile_icon!!.isNotEmpty()) {
            Picasso.with(context)
                .load(mDataList[position].profile_icon)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.replyiconpage1)
        }else{
            Picasso.with(context)
                .load(R.drawable.ic_launcher_foreground)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.replyiconpage1)
        }


        holder.reply_upvote.setOnClickListener {
            //            var arrayList = ArrayList<String>()
//            doVote(
//                "reply",
//                mDataList[position].id.toString(),
//                "upvote",
//                holder,
//                position
//            )
        }
        holder.reply_downvote.setOnClickListener {
            //            var arrayList = ArrayList<String>()
//             doVote(
//                "reply",
//                mDataList[position].id.toString(),
//                "downvote",
//                holder,
//                position
//            )
        }
//        holder.comment_replies.setOnClickListener {
//            holder.row_comment_post1.visibility = View.VISIBLE
//            holder.comment_edittext.setText("")
//        }
//
//        holder.longclick_block.setOnLongClickListener{
//
//            (context as ZActivityCommentPage).openBottomSheet(mDataList[position].id, mDataList[position].entity_value, mDataList[position].is_owner)
//            true
//        }


//               holder.replyblock.setOnLongClickListener{
        //            editReply(position)
//
//            true
//        }

        holder.seemoreReply.setOnClickListener {
            editReply(position)
        }




        holder.comment_edittext.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
                val DRAWABLE_LEFT :Int = 0;
                val DRAWABLE_TOP :Int = 1;
                val DRAWABLE_RIGHT: Int = 2;
                val DRAWABLE_BOTTOM: Int = 3;

                if(event!!.action == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (holder.comment_edittext.getRight() - holder.comment_edittext.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Log.d("TAGG", "SEND" + holder.comment_edittext.text)
                        addReply("reply",holder.comment_edittext.text.toString(),mDataList[position].id,holder,position)
                        return true;
                    }
                }
                return false;
            }
        })

//        holder.reply_upvote.setOnClickListener {
//            doVote(
//                "reply",
//                mDataList[position].reply_list!!.id.toString(),
//                "upvote",
//                holder,
//                position
//            )
//        }
//
//        holder.reply_downvote.setOnClickListener {
//            doVote(
//                "reply",
//                mDataList[position].reply_list!!.id.toString(),
//                "downvote",
//                holder,
//                position
//            )
//        }

    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //        internal var comment_name: TextView
//        internal var comment_datetime: TextView
//        internal var comment_data: TextView
//        internal var comment_upvote: TextView
//        internal var comment_downvote: TextView
//        internal var comment_replies: TextView
        internal var comment_edittext: EditText
        internal var replyblock: LinearLayout
        //        internal var row_comment_post1: LinearLayout
//        internal var longclick_block: LinearLayout
//        internal var commetsiconpage2: ImageView
        internal var replyiconpage1: ImageView
        internal var seemoreReply: TextView

        internal var reply_name: TextView
        internal  var reply_datetime: TextView
        internal  var reply_data:TextView
        //        internal  var view_all_replies:TextView
        internal var reply_upvote: TextView
        internal var reply_downvote: TextView
        internal var reply_replies: TextView


        init {
//            comment_name= itemView.findViewById<View>(R.id.commentusername) as TextView
//            comment_datetime= itemView.findViewById<View>(R.id.commentdatetime) as TextView
//            comment_data= itemView.findViewById<View>(R.id.commentdata) as TextView
//            comment_upvote= itemView.findViewById<View>(R.id.comment_upvote) as TextView
//            comment_downvote= itemView.findViewById<View>(R.id.comment_downvote) as TextView
//            comment_replies= itemView.findViewById<View>(R.id.comment_replies) as TextView
            comment_edittext = itemView.findViewById<View>(R.id.coment_edittext1) as EditText
//            commetsiconpage2 = itemView.findViewById<View>(R.id.commenticon_page2) as ImageView
            replyblock = itemView.findViewById<View>(R.id.comment_block_whole) as LinearLayout
//            row_comment_post1 = itemView.findViewById<View>(R.id.coment_post1) as LinearLayout
//            longclick_block = itemView.findViewById<View>(R.id.longclick_block) as LinearLayout

            reply_name = itemView.findViewById<View>(R.id.replyusername) as TextView
            reply_datetime = itemView.findViewById<View>(R.id.replydatetime) as TextView
            reply_data = itemView.findViewById<View>(R.id.replydata) as TextView
//            view_all_replies = itemView.findViewById<View>(R.id.replyText) as TextView
            reply_upvote= itemView.findViewById<View>(R.id.reply_upvote) as TextView
            reply_downvote= itemView.findViewById<View>(R.id.reply_downvote) as TextView
            reply_replies= itemView.findViewById<View>(R.id.reply_replies) as TextView
            replyiconpage1 = itemView.findViewById<View>(R.id.replyicon) as ImageView
            seemoreReply = itemView.findViewById<View>(R.id.seemoreReply) as TextView
        }
    }


    private fun doVote(entity_type: String, entity_id:String, vote_type:String, holder: MyViewHolder, position: Int){

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
            "Bearer "+EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<VoteResponse> {

            override fun onResponse(call: Call<VoteResponse>, response: Response<VoteResponse>) {

                Log.d("TAG", "CODE" + response.code().toString() + "")
                Log.d("TAG", "MESSAGE" + response.message() + "")
                Log.d("TAG", "RESPONSE" + "" + Gson().toJson(response))
                Log.d("TAGG", "URL" + "" + response)

                // var str_response = Gson().toJson(response)
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject =
                    JSONObject(str_response.substring(str_response.indexOf("{"), str_response.lastIndexOf("}") + 1))
                val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                var jsonaobj: JSONObject = jsonObject1.getJSONObject("body")
                if (response.isSuccessful) {
                    if(responseCode==10701 || responseCode== 10700) {
                        Log.d("TAGG", "VOTE" + jsonaobj.getString("downvote_count"))
                        if (entity_type1 == "reply") {
                            holder.reply_downvote.setText(jsonaobj.getString("downvote_count"))
                            holder.reply_upvote.setText(jsonaobj.getString("upvote_count"))
                        }
                        else if (entity_type1 == "comment"){
//                        holder.comment_downvote.setText(jsonaobj.getString("downvote_count"))
//                        holder.comment_upvote.setText(jsonaobj.getString("upvote_count"))
                        }
                    }
                    else if(responseCode==11700){
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

    private fun deleteVote(entity_type: String, entity_id:String, vote_type:String, holder: MyViewHolder, position: Int){

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
            "Bearer "+EndPoints.ACCESS_TOKEN,
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
                    JSONObject(str_response.substring(str_response.indexOf("{"), str_response.lastIndexOf("}") + 1))
                val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                var jsonaobj: JSONObject = jsonObject1.getJSONObject("body")
                if (response.isSuccessful) {
                    if(responseCode==10702) {
                        Log.d("TAGG", "VOTE" + jsonaobj.getString("downvote_count"))
                        if (entity_type2 == "reply") {
                            holder.reply_downvote.setText(jsonaobj.getString("downvote_count"))
                            holder.reply_upvote.setText(jsonaobj.getString("upvote_count"))
                        }
                        else if (entity_type2 == "comment"){
//                            holder.comment_downvote.setText(jsonaobj.getString("downvote_count"))
//                            holder.comment_upvote.setText(jsonaobj.getString("upvote_count"))
                        }
                    }
                    else{

                    }
                } else {

                }
            }

            override fun onFailure(call: Call<VoteResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })

    }

    private fun addReply(entity_type: String, entity_value:String, id:Int,holder: MyViewHolder, position: Int){

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
            "Bearer "+EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<CreateReplyResponse> {

            override fun onResponse(call: Call<CreateReplyResponse>, response: Response<CreateReplyResponse>) {

                Log.d("TAG", "CODE Value" + response.code().toString() + "")
                Log.d("TAG", "MESSAGE VAlue" + response.message() + "")
                Log.d("TAG", "RESPONSE Value" + "" + Gson().toJson(response))
                Log.d("TAGG", "URL Value" + "" + response)

                //var str_response = Gson().toJson(response)
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject =
                    JSONObject(str_response.substring(str_response.indexOf("{"), str_response.lastIndexOf("}") + 1))
                val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                var jsonaobj: JSONObject = jsonObject1.getJSONObject("body")
                if (response.isSuccessful) {
                    if(responseCode==10301) {
                        //mDataList[position].comment_list!! =
                        Log.d("TAGG", "REPLY" + jsonaobj.getString("id"))
//                        holder.replyblock.visibility = View.VISIBLE
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
//                        holder.comment_upvote.setText(jsonaobj.getString("upvote_count"))
//                        holder.comment_downvote.setText(jsonaobj.getString("downvote_count"))
//                        holder.comment_edittext.setText("0")
//                        holder.row_comment_post1.visibility = View.GONE
                    }
                    else{

                    }
                } else {

                }
            }

            override fun onFailure(call: Call<CreateReplyResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })

    }


    private fun editReply(pos:Int){

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.ReportCheckReply(mDataList[pos].id,EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<DeletePostResponse> {
            override fun onResponse(call: Call<DeletePostResponse>, response: Response<DeletePostResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("TAGG","CODE IS "+response.body()!!.responseCode.toString())
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful && response.body()!!.responseCode.toString().equals("11804") ) {      //11804
                    // Toast.makeText(context,"Hello",Toast.LENGTH_LONG).show()
                    (context as ZActivityRepliesPage).openBottomSheetReplies(mDataList[pos].id, mDataList[pos].entity_value, mDataList[pos].is_owner,
                        mDataList[pos].profile_icon!!.toString(),"no")
                } else {

                    (context as ZActivityRepliesPage).openBottomSheetReplies(mDataList[pos].id, mDataList[pos].entity_value, mDataList[pos].is_owner,
                        mDataList[pos].profile_icon!!.toString(),"yes")
                }
            }

            override fun onFailure(call: Call<DeletePostResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })
    }
}