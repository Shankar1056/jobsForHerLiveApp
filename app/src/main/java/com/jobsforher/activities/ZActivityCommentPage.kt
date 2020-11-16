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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.zactivity_commentspage.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.jobsforher.R
import com.jobsforher.adapters.CommentsAdapter
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.models.GroupsCommentModel
import com.jobsforher.models.GroupsReplyModel
import com.jobsforher.network.responsemodels.GroupCommentsNew
import com.jobsforher.network.responsemodels.ReportResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import women.jobs.jobsforher.activities.BaseActivity
import java.util.*


class ZActivityCommentPage : BaseActivity() {

    private val TAG = javaClass.simpleName
    private var retrofitInterface1: RetrofitInterface? = null
    var commentId = 0
    var listOfCommentdata: ArrayList<GroupsCommentModel> = ArrayList()
    var listOfCommentdataDump: ArrayList<GroupsCommentModel> = ArrayList()
    var mRecyclerViewComments: RecyclerView? = null
    var mAdapterComments: RecyclerView.Adapter<*>? = null
    private var retrofitInterface: RetrofitInterface? = null

    var next_page_no_posts: String="1"
    var scroll = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.zactivity_commentspage)
        commentId = intent.getIntExtra("comment_Id",0)

        mRecyclerViewComments = findViewById(R.id.comments_recycler_view)

        listOfCommentdata.clear()
        loadGroupComments(next_page_no_posts)

        my_swipeRefresh_Layout1.setOnRefreshListener {
            listOfCommentdata.clear()
            loadGroupComments("1")
        }

        backBtn.setOnClickListener{
            setResult(1);
            finish()
        }


//        comments_recycler_view.getViewTreeObserver().addOnScrollChangedListener(object : ViewTreeObserver.OnScrollChangedListener {
//            override fun onScrollChanged() {
//                Log.d("TAGG","END EFORE")
//                val view: View = comments_recycler_view.getChildAt(comments_recycler_view.getChildCount() - 1);
//
//                val diff:Int = (view.getBottom() - (comments_recycler_view.getHeight() + comments_recycler_view.getScrollY()));
//
//                if (diff == 0 && scroll ==1) {
//                    Log.d("TAGG","END"+next_page_no_posts)
//                    loadGroupComments(next_page_no_posts)
//
//                }
//            }
//        });

        comments_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(scroll==1) {
//                    if (comments_recycler_view.canScrollVertically(1) && scroll == 1) {
//                        listOfCommentdata.clear()
//                        loadGroupComments(next_page_no_posts)
//                    }
                    Toast.makeText(applicationContext,isRecyclerScrollable(comments_recycler_view!!).toString(),Toast.LENGTH_LONG).show()
                    if (!isRecyclerScrollable(comments_recycler_view!!) && scroll==1){
//                    Thread.sleep(1000)
                        loadGroupComments(next_page_no_posts)
                    }
                }
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 1) {
            listOfCommentdata.clear()
            loadGroupComments("1")
        }
    }

    fun isRecyclerScrollable(comments_recycler_view: RecyclerView): Boolean {
        //Toast.makeText(applicationContext,"True",Toast.LENGTH_LONG).show()
        return comments_recycler_view.computeVerticalScrollRange() > comments_recycler_view.height
    }

    fun loadGroupComments(pageno:String){

        listOfCommentdataDump.clear()
        retrofitInterface1 = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val params = HashMap<String, String>()
        loadnextc.visibility = View.GONE
        params["page_no"] = pageno.toString()
        params["page_size"]= "3"
        var model1: GroupsCommentModel = GroupsCommentModel();

        val call = retrofitInterface1!!.getPostComments(
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
                var jsonarray_pagination: JSONObject = jsonObj1.getJSONObject("pagination")
                val has_next: String  =jsonarray_pagination.getBoolean("has_next").toString()
//                if (has_next.equals("true")) {
//                    val next_page: String = jsonarray_pagination.getInt("next_page").toString()
//                    loadnextc.visibility = View.VISIBLE
//                    next_page_no_posts = next_page
//                    scroll = 1
//                }
//                else {
//                    loadnextc.visibility = View.GONE
//                    scroll=0
//                }


                val messages: String = jsonObj1.getString("message")
                Log.d("TAGG1", "Response " + messages)
                var jsonArray: JSONArray = jsonObj1.getJSONArray("body")
                if (response1.isSuccessful) {
                    for (j in 0 until response1.body()!!.body!!.size) {
                        var json_objectdetails: JSONObject = jsonArray.getJSONObject(j)
                        Log.d(
                            "TAGG1",
                            "Response " + jsonArray.getJSONObject(j).getInt("id")
                        )

                        model1 = GroupsCommentModel()
                        model1 = GroupsCommentModel(
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
                            jsonArray.getJSONObject(j).getString("comment_count")!!,
                            jsonArray.getJSONObject(j).getString("replies_count")!!,
                            jsonArray.getJSONObject(j).getString("aggregate_count")!!,
                            jsonArray.getJSONObject(j).getString("username")!!,
                            jsonArray.getJSONObject(j).getString("email")!!,
                            jsonArray.getJSONObject(j).getString("profile_icon")!!,
                            jsonArray.getJSONObject(j).getString("is_owner")!!,
                            GroupsReplyModel()
                        )

                        listOfCommentdataDump.add(model1)
                    }
//                        var mgroupsLayoutManager = GridLayoutManager(applicationContext,1)
//                        mRecyclerViewComments!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
//                        mAdapterComments = CommentsAdapter(listOfCommentdata)
//                        mRecyclerViewComments!!.adapter = mAdapterComments
                }
                else {
//                        ToastHelper.makeToast(applicationContext, "message")
                }

                var isReply:Boolean=false
                for (y in 0 until listOfCommentdataDump.size){
                    if (listOfCommentdataDump[y].replies_counts.equals("0")){
                        isReply = false
                    }
                    else {
                        isReply = true
                        break
                    }
                }

                for (x in 0 until listOfCommentdataDump.size){

//                    if (!isReply) {
//                        listOfCommentdata.add(listOfCommentdataDump[x])
//                        var mgroupsLayoutManager = LinearLayoutManager(applicationContext)
//                        mRecyclerViewComments!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
//                        mAdapterComments = CommentsAdapter(listOfCommentdata)
//                        mRecyclerViewComments!!.adapter = mAdapterComments
//
//                    }
                    if(listOfCommentdataDump[x].replies_counts.equals("0")){
                        listOfCommentdata.add(listOfCommentdataDump[x])
                    }
                    else {
                        Log.d("TAGG1","ID is "+listOfCommentdataDump[x].id)
                        retrofitInterface1 = RetrofitClient.client!!.create(RetrofitInterface::class.java)

                        val call = retrofitInterface1!!.getReply(
                            listOfCommentdataDump[x].id,
                            EndPoints.CLIENT_ID,
                            "Bearer "+ EndPoints.ACCESS_TOKEN
                        )
                        Logger.d("URL1", "" + "HI")
                        call.enqueue(object : Callback<GroupCommentsNew> {
                            override fun onResponse(call: Call<GroupCommentsNew>, response1: Response<GroupCommentsNew>) {
                                Logger.d("URL1", "REPLY is" + response1 + EndPoints.CLIENT_ID)
                                Logger.d("CODE1", "REPLY is" +response1.code().toString() + "")
                                Logger.d("MESSAGE1", "REPLY is" +response1.message() + "")
                                Logger.d("RESPONSE1", "REPLY IS"+"" + Gson().toJson(response1))
                                //var str_responses = Gson().toJson(response1)
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

//                                if (response_code == 11301) {
//                                    listOfPostdata[x].comment_list = GroupsCommentModel()
//                                } else {
                                val messages: String = jsonObj1.getString("message")
                                Log.d("TAGG1", "Response " + messages)
                                var jsonArray: JSONArray = jsonObj1.getJSONArray("body")
                                if (response1.isSuccessful) {
                                    // for (j in 0 until response1.body()!!.body!!.size) {
                                    var json_objectdetails: JSONObject = jsonArray.getJSONObject(0)
                                    Log.d(
                                        "TAGG1",
                                        "Reply" + jsonArray.getJSONObject(0).getInt("parent_id")
                                    )
                                    val l:Int = jsonArray.length()-1
                                    var model2: GroupsReplyModel = GroupsReplyModel();
                                    model2 = GroupsReplyModel(
                                        json_objectdetails.getInt("id"),
                                        jsonArray.getJSONObject(l).getInt("parent_id").toString()!!,
                                        jsonArray.getJSONObject(l).getString("entity_type")!!,
                                        jsonArray.getJSONObject(l).getString("entity_value")!!,
                                        jsonArray.getJSONObject(l).getString("group_id")!!,
                                        jsonArray.getJSONObject(l).getString("post_id")!!,
                                        jsonArray.getJSONObject(l).getString("created_by")!!,
                                        jsonArray.getJSONObject(l).getString("upvote_count")!!,
                                        jsonArray.getJSONObject(l).getString("downvote_count")!!,
                                        jsonArray.getJSONObject(l).getString("url")!!,
                                        jsonArray.getJSONObject(l).getString("hash_tags")!!,
                                        jsonArray.getJSONObject(l).getString("status")!!,
                                        jsonArray.getJSONObject(l).getString("edited")!!,
                                        jsonArray.getJSONObject(l).getString("created_on")!!,
                                        jsonArray.getJSONObject(l).getString("modified_on")!!,
                                        jsonArray.getJSONObject(l).getString("created_on_str")!!,
                                        jsonArray.getJSONObject(l).getString("aggregate_count")!!,
                                        jsonArray.getJSONObject(l).getString("username")!!,
                                        jsonArray.getJSONObject(l).getString("email")!!,
                                        jsonArray.getJSONObject(l).getString("profile_icon")!!,
                                        jsonArray.getJSONObject(l).getString("is_owner")!!
                                    )

                                    listOfCommentdataDump[x].reply_list = model2
                                    listOfCommentdata.add(listOfCommentdataDump[x])
                                    listOfCommentdata.sortWith(compareByDescending<GroupsCommentModel> { it.created_on })
                                    mAdapterComments!!.notifyDataSetChanged()

                                } else {
//                                    ToastHelper.makeToast(applicationContext, "message")
                                }
//                                }
                            }
                            override fun onFailure(call: Call<GroupCommentsNew>, t: Throwable) {
                                Logger.d("TAGG", "FAILED : $t")
                            }
                        })
                    }

                }
                //Thread.sleep(1000)
//                listOfCommentdata.sortWith(compareBy { it.modified_on})
                listOfCommentdata.sortWith(compareByDescending<GroupsCommentModel> { it.created_on })

//                Collections.sort(listOfCommentdata, object : Comparator<GroupsCommentModel> {
//                    override fun compare(obj1: GroupsCommentModel, obj2: GroupsCommentModel): Int {
//                        // ## Ascending order
//                        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//                        return dateFormat.parse(obj1.created_on).compareTo(dateFormat.parse(obj2.created_on.toString())) // To compare string values
//
//                        // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values
//
//                        // ## Descending order
//                        // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
//                        // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
//                    }
//                })
                var mgroupsLayoutManager = LinearLayoutManager(applicationContext)
                mRecyclerViewComments!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                mAdapterComments = CommentsAdapter(listOfCommentdata)
                mRecyclerViewComments!!.adapter = mAdapterComments
            }

            override fun onFailure(call: Call<GroupCommentsNew>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
            }
        })

        if(my_swipeRefresh_Layout1.isRefreshing){
            my_swipeRefresh_Layout1.isRefreshing = false
        }

    }


    public fun openBottomSheet(edited:String?,id: Int, data: String?, isOwner: String?, icon:String?,yesno: String) {

        Log.d("TAGG","Edited is"+id)
        val dialog = Dialog(this)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog .setCancelable(true)
        dialog .setContentView(R.layout.bottom_sheet_comments)
        val editcomment = dialog .findViewById(R.id.edit_comment) as LinearLayout

        editcomment.setOnClickListener {
            if(isOwner.equals("true")) {
                dialog.cancel()
                intent = Intent(applicationContext, ZEditCommentActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra("data", data)
                intent.putExtra("icon",icon)
                startActivityForResult(intent,1)
                dialog.cancel()
            }
            else
                Toast.makeText(applicationContext, "You cannot edit the comment", Toast.LENGTH_LONG).show()
        }

        val edit_commenthistory = dialog .findViewById(R.id.edit_comment_history) as LinearLayout
        edit_commenthistory.setOnClickListener {
            if(isOwner.equals("true")) {
                intent = Intent(applicationContext, ZEditComentHistoryActivity::class.java)
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
            openBottomSheetReports(id,data,isOwner,"", "comment")
        }

        val cancel = dialog .findViewById(R.id.cancel) as LinearLayout
        cancel.setOnClickListener {
            dialog.cancel()
        }

        if(isOwner.equals("true")){
            editcomment.visibility= View.VISIBLE
            edit_commenthistory.visibility = View.VISIBLE
            if(edited.equals("true")){
                edit_commenthistory.visibility = View.VISIBLE
            }
            else{
                edit_commenthistory.visibility = View.GONE
            }
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

    override fun onBackPressed() {
        // code here to show dialog
        setResult(1);
        finish()  // optional depending on your needs
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
