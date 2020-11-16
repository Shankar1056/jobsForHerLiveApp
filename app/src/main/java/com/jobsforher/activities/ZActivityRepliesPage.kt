package com.jobsforher.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.zactivity_commentspage.backBtn
import kotlinx.android.synthetic.main.zactivity_commentspage.my_swipeRefresh_Layout1
import kotlinx.android.synthetic.main.zactivity_repliesspage.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.jobsforher.R
import com.jobsforher.adapters.RepliesAdapter
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.models.GroupsReplyModel
import com.jobsforher.network.responsemodels.GroupCommentsNew
import com.jobsforher.network.responsemodels.ReportResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import women.jobs.jobsforher.activities.BaseActivity
import java.util.*

class ZActivityRepliesPage : BaseActivity() {

    private val TAG = javaClass.simpleName
    private var retrofitInterface1: RetrofitInterface? = null
    private var retrofitInterface: RetrofitInterface? = null
    var commentId = 0
    var comment_name=""
    var comment_datetime=""
    var comment_desc=""
    var comment_icon=""
    var listOfReplydata: ArrayList<GroupsReplyModel> = ArrayList()
    var mRecyclerViewComments: RecyclerView? = null
    var mAdapterComments: RecyclerView.Adapter<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.zactivity_repliesspage)
        commentId = intent.getIntExtra("comment_Id",0)
        comment_name = intent.getStringExtra("comment_name")
        comment_datetime = intent.getStringExtra("comment_datetime")
        comment_desc = intent.getStringExtra("comment_desc")
        comment_icon = intent.getStringExtra("comment_icon")
        mRecyclerViewComments = findViewById(R.id.replies_recycler_view)

        commentusername.setText(comment_name)
        commentdatetime.setText(comment_datetime)
        commentdata.setText(comment_desc)

        if(comment_icon.isNotEmpty()) {
            Picasso.with(applicationContext)
                .load(comment_icon)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(commenticon_page2)
        }else{
            Picasso.with(applicationContext)
                .load(R.drawable.ic_launcher_foreground)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(commenticon_page2)
        }
        loadGroupRelies()

        my_swipeRefresh_Layout1.setOnRefreshListener {
            listOfReplydata.clear()
            loadGroupRelies()
        }

        backBtn.setOnClickListener{
            setResult(1);
            finish()
        }

    }

    override fun onBackPressed() {
        setResult(1);
        finish()
    }

    fun loadGroupRelies(){
        listOfReplydata.clear()
        retrofitInterface1 = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface1!!.getPostReplies(
            commentId,
            EndPoints.CLIENT_ID,
            "Bearer "+EndPoints.ACCESS_TOKEN
        )
        Logger.d("URL", "" + "HI")
        call.enqueue(object : Callback<GroupCommentsNew> {
            override fun onResponse(call: Call<GroupCommentsNew>, response1: Response<GroupCommentsNew>) {
                Logger.d("URL2", "" + response1 + EndPoints.CLIENT_ID)
                Logger.d("CODE2", response1.code().toString() + "")
                Logger.d("MESSAGE2", response1.message() + "")
                Logger.d("RESPONSE2", "" + Gson().toJson(response1))
                // var str_responses = Gson().toJson(response1)
                val gson = GsonBuilder().serializeNulls().create()
                var str_responses = gson.toJson(response1)
                val jsonObj: JSONObject = JSONObject(
                    str_responses.substring(
                        str_responses.indexOf("{"),
                        str_responses.lastIndexOf("}") + 1
                    )
                )
                val jsonObj1: JSONObject = jsonObj.getJSONObject("body")
                //Log.d("TAGG", "Response "+jsonObj1.toString())
                val response_code: Int = jsonObj1.getInt("response_code")

                val messages: String = jsonObj1.getString("message")
                Log.d("TAGG1", "Response " + messages)
                var jsonArray: JSONArray = jsonObj1.getJSONArray("body")
                if (response1.isSuccessful) {
                    for (j in 0 until response1.body()!!.body!!.size) {
                        var json_objectdetails: JSONObject = jsonArray.getJSONObject(j)
                        Log.d(
                            "TAGG1",
                            "Response " + jsonArray.getJSONObject(j).getInt("parent_id")
                        )
                        var model1: GroupsReplyModel = GroupsReplyModel();

                        model1 = GroupsReplyModel(
                            json_objectdetails.getInt("id"),
                            jsonArray.getJSONObject(j).getInt("parent_id").toString()!!,
                            jsonArray.getJSONObject(j).getString("entity_type")!!,
                            jsonArray.getJSONObject(j).getString("entity_value")!!,
                            jsonArray.getJSONObject(j).getString("group_id")!!,
                            jsonArray.getJSONObject(j).getString("post_id")!!,
                            jsonArray.getJSONObject(j).getString("created_by")!!,
                            jsonArray.getJSONObject(j).getString("upvote_count")!!,
                            jsonArray.getJSONObject(j).getString("downvote_count")!!,
                            jsonArray.getJSONObject(j).getString("url")!!,
                            jsonArray.getJSONObject(j).getString("hash_tags")!!,
                            jsonArray.getJSONObject(j).getString("status")!!,
                            jsonArray.getJSONObject(j).getString("edited")!!,
                            jsonArray.getJSONObject(j).getString("created_on")!!,
                            jsonArray.getJSONObject(j).getString("modified_on")!!,
                            jsonArray.getJSONObject(j).getString("created_on_str")!!,
                            jsonArray.getJSONObject(j).getString("aggregate_count")!!,
                            jsonArray.getJSONObject(j).getString("username")!!,
                            jsonArray.getJSONObject(j).getString("email")!!,
                            jsonArray.getJSONObject(j).getString("profile_icon")!!,
                            jsonArray.getJSONObject(j).getString("is_owner")!!
                        )
                        listOfReplydata.add(model1)
                    }
                    var mgroupsLayoutManager = GridLayoutManager(applicationContext,1)
                    mRecyclerViewComments!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                    mAdapterComments = RepliesAdapter(listOfReplydata)
                    mRecyclerViewComments!!.adapter = mAdapterComments
                }
                else {
//                        ToastHelper.makeToast(applicationContext, "message")
                }

//                var isReply:Boolean=false
//                for (y in 0 until listOfCommentdata.size){
//                    if (listOfCommentdata[y].replies_counts.equals("0"))
//                    //isComment = false
//                    else
//                        isReply = true
//                }

//                for (x in 0 until listOfCommentdata.size){
//                    Log.d("TAGG1","Reply value is"+isReply.toString())
//                    if (!isReply) {
//                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
//                        mRecyclerViewComments!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
//                        mAdapterComments = CommentsAdapter(listOfCommentdata)
//                        mRecyclerViewComments!!.adapter = mAdapterComments
//
//                    } else {
//                        retrofitInterface1 = RetrofitClient.client!!.create(RetrofitInterface::class.java)
//
//                        val call = retrofitInterface1!!.getReply(
//                            listOfCommentdata[x].id,
//                            EndPoints.CLIENT_ID,
//                            "Bearer "+ EndPoints.ACCESS_TOKEN
//                        )
//                        Logger.d("URL1", "" + "HI")
//                        call.enqueue(object : Callback<GroupComments> {
//                            override fun onResponse(call: Call<GroupComments>, response1: Response<GroupComments>) {
//                                Logger.d("URL1", "REPLY is" + response1 + EndPoints.CLIENT_ID)
//                                Logger.d("CODE1", "REPLY is" +response1.code().toString() + "")
//                                Logger.d("MESSAGE1", "REPLY is" +response1.message() + "")
//                                Logger.d("RESPONSE1", "REPLY IS"+"" + Gson().toJson(response1))
//                                //var str_responses = Gson().toJson(response1)
//                                val gson = GsonBuilder().serializeNulls().create()
//                                var str_responses = gson.toJson(response1)
//                                val jsonObj: JSONObject = JSONObject(
//                                    str_responses.substring(
//                                        str_responses.indexOf("{"),
//                                        str_responses.lastIndexOf("}") + 1
//                                    )
//                                )
//                                val jsonObj1: JSONObject = jsonObj.getJSONObject("body")
//                                //Log.d("TAGG", "Response "+jsonObj1.toString())
//                                val response_code: Int = jsonObj1.getInt("response_code")
//
////                                if (response_code == 11301) {
////                                    listOfPostdata[x].comment_list = GroupsCommentModel()
////                                } else {
//                                val messages: String = jsonObj1.getString("message")
//                                Log.d("TAGG1", "Response " + messages)
//                                var jsonArray: JSONArray = jsonObj1.getJSONArray("body")
//                                if (response1.isSuccessful) {
//                                    // for (j in 0 until response1.body()!!.body!!.size) {
//                                    var json_objectdetails: JSONObject = jsonArray.getJSONObject(0)
//                                    Log.d(
//                                        "TAGG1",
//                                        "Reply" + jsonArray.getJSONObject(0).getInt("parent_id")
//                                    )
//                                    var model2: GroupsReplyModel = GroupsReplyModel();
//                                    model2 = GroupsReplyModel(
//                                        json_objectdetails.getInt("id"),
//                                        jsonArray.getJSONObject(0).getInt("parent_id").toString()!!,
//                                        jsonArray.getJSONObject(0).getString("entity_type")!!,
//                                        jsonArray.getJSONObject(0).getString("entity_value")!!,
//                                        jsonArray.getJSONObject(0).getString("group_id")!!,
//                                        jsonArray.getJSONObject(0).getString("post_id")!!,
//                                        jsonArray.getJSONObject(0).getString("created_by")!!,
//                                        jsonArray.getJSONObject(0).getString("upvote_count")!!,
//                                        jsonArray.getJSONObject(0).getString("downvote_count")!!,
//                                        jsonArray.getJSONObject(0).getString("url")!!,
//                                        jsonArray.getJSONObject(0).getString("hash_tags")!!,
//                                        jsonArray.getJSONObject(0).getString("status")!!,
//                                        jsonArray.getJSONObject(0).getString("edited")!!,
//                                        jsonArray.getJSONObject(0).getString("created_on")!!,
//                                        jsonArray.getJSONObject(0).getString("modified_on")!!,
//                                        jsonArray.getJSONObject(0).getString("created_on_str")!!,
//                                        jsonArray.getJSONObject(0).getString("aggregate_count")!!,
//                                        jsonArray.getJSONObject(0).getString("username")!!,
//                                        jsonArray.getJSONObject(0).getString("email")!!,
//                                        jsonArray.getJSONObject(0).getString("profile_icon")!!,
//                                        jsonArray.getJSONObject(0).getString("is_owner")!!
//                                    )
//
//                                    listOfCommentdata[x].reply_list = model2
//
//                                } else {
//                                    ToastHelper.makeToast(applicationContext, "message")
//                                }
//                                var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
//                                mRecyclerViewComments!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
//                                mAdapterComments = CommentsAdapter(listOfCommentdata)
//                                mRecyclerViewComments!!.adapter = mAdapterComments
////                                }
//                            }
//
//                            override fun onFailure(call: Call<GroupComments>, t: Throwable) {
//                                Logger.d("TAGG", "FAILED : $t")
//                            }
//                        })
//
//                    }
//
//                }
            }

            override fun onFailure(call: Call<GroupCommentsNew>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
            }
        })

        if(my_swipeRefresh_Layout1.isRefreshing){
            my_swipeRefresh_Layout1.isRefreshing = false
        }
    }


    public fun openBottomSheet(id: Int, data: String?, isOwner: String?) {

        val dialog = Dialog(this)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog .setCancelable(true)
        dialog .setContentView(R.layout.bottom_sheet_comments)
        val editpost = dialog .findViewById(R.id.edit_post) as LinearLayout

        editpost.setOnClickListener {
            if(isOwner.equals("true")) {
                dialog.cancel()
                intent = Intent(applicationContext, ZEditCommentActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra("data", data)
                startActivity(intent)
            }
            else
                Toast.makeText(applicationContext, "You cannot edit the comment", Toast.LENGTH_LONG).show()
        }

        val edit_post_history = dialog .findViewById(R.id.edit_post_history) as LinearLayout
        edit_post_history.setOnClickListener {
            if(isOwner.equals("true")) {
                intent = Intent(applicationContext, ZEditComentHistoryActivity::class.java)
                intent.putExtra("id", id)
                startActivity(intent)
            }
            else
                Toast.makeText(applicationContext, "You cannot view the edit history", Toast.LENGTH_LONG).show()
        }

        val cancel = dialog .findViewById(R.id.cancel) as LinearLayout
        cancel.setOnClickListener {
            dialog.cancel()
        }

        var window : Window = dialog.getWindow()!!;
        var wlp: WindowManager.LayoutParams = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog .show()



    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 1) {
            loadGroupRelies()
        }
    }
    public fun openBottomSheetReplies(id: Int, data: String?, isOwner: String?, icon:String?,yesno: String) {

        val dialog = Dialog(this)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog .setCancelable(true)
        dialog .setContentView(R.layout.bottom_sheet_replies)
        val editcomment = dialog .findViewById(R.id.edit_comment) as LinearLayout

        editcomment.setOnClickListener {
            if(isOwner.equals("true")) {
                dialog.cancel()
                intent = Intent(applicationContext, ZEditReplyActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra("data", data)
                intent.putExtra("icon",icon)
                startActivityForResult(intent,1)
                dialog.cancel()
            }
            else
                Toast.makeText(applicationContext, "You cannot edit the reply", Toast.LENGTH_LONG).show()
        }

        val edit_commenthistory = dialog .findViewById(R.id.edit_comment_history) as LinearLayout
        edit_commenthistory.setOnClickListener {
            if(isOwner.equals("true")) {
                intent = Intent(applicationContext, ZEditReplyHistoryActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra("icon",icon)
                startActivity(intent)
                dialog.cancel()
            }
            else
                Toast.makeText(applicationContext, "You cannot view the edit history", Toast.LENGTH_LONG).show()
        }

        val reportcomment = dialog .findViewById(R.id.report_comment) as LinearLayout
        val reporttext = dialog.findViewById(R.id.reporttextcomment) as TextView
        Log.d("REPORT",yesno)
        if (yesno.compareTo("yes")==0){
            reporttext.setText("Reported")
            reporttext.setTextColor(Color.RED)
            reportcomment.isEnabled = false
        }
        else{
            reporttext.setText("Report this comment")
            //reporttext.setTextColor(Color.BLACK)
            reportcomment.isEnabled = true
        }
        reportcomment.setOnClickListener {
            dialog.cancel()
            openBottomSheetReports(id,data,isOwner,"", "reply")
        }

        val cancel = dialog .findViewById(R.id.cancel) as LinearLayout
        cancel.setOnClickListener {
            dialog.cancel()
        }

        if(isOwner.equals("true")){
            editcomment.visibility= View.VISIBLE
            edit_commenthistory.visibility = View.VISIBLE
        }
        else{
            editcomment.visibility= View.GONE
            edit_commenthistory.visibility = View.GONE
        }

        var window : Window = dialog.getWindow()!!;
        var wlp: WindowManager.LayoutParams = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog .show()

    }

    public fun openBottomSheetReports(id: Int, data: String?, isOwner: String?, icon:String?, type:String) {

        val dialog = Dialog(this)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog .setCancelable(true)
        dialog .setContentView(R.layout.bottom_sheet_reports)
        val spinner = dialog.findViewById(R.id.report_spinner) as Spinner
        val adapter = ArrayAdapter.createFromResource(this, R.array.report_list, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val reportdetails = dialog .findViewById(R.id.reportdetails) as EditText

        val sendreport = dialog .findViewById(R.id.sendreport) as LinearLayout
        sendreport.setOnClickListener {
            reportGroup(id,type, spinner.selectedItem.toString(), reportdetails.text.toString(), dialog)
        }

        val cancel = dialog .findViewById(R.id.cancelblock) as LinearLayout
        cancel.setOnClickListener {
            dialog.cancel()
        }


        var window : Window = dialog.getWindow()!!;
        var wlp: WindowManager.LayoutParams  = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog .show()
    }


    fun reportGroup(id: Int, type: String, problem_type:String, reportdetail: String, dialog:Dialog){

//        ToastHelper.makeToast(applicationContext, "Report Group")
        val params = HashMap<String, String>()

        params["entity_id"] = id.toString()
        params["entity_type"] = type
        params["problem_type"] = problem_type
        params["reason"] = reportdetail

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.ReportGroup(EndPoints.CLIENT_ID, "Bearer "+
                EndPoints.ACCESS_TOKEN,params)

        call.enqueue(object : Callback<ReportResponse> {
            override fun onResponse(call: Call<ReportResponse>, response: Response<ReportResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.e("RESPONSE report group", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())

                    Logger.e("TAGG", "" + response.body()!!.message.toString())
                    dialog.cancel()

                } else {

                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<ReportResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })

    }
}
