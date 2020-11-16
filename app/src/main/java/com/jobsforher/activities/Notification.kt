package com.jobsforher.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.jobsforher.R
import com.jobsforher.adapters.NotificationAdapter
import com.jobsforher.helpers.Constants
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.models.NotificationModel
import com.jobsforher.network.responsemodels.NotificationResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import kotlinx.android.synthetic.main.notification.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

private var retrofitInterface: RetrofitInterface? = null
var listOfPostdataDump: ArrayList<NotificationModel> = ArrayList()
var mRecyclerViewJobs: RecyclerView? = null
var mAdapterJobs: RecyclerView.Adapter<*>? = null

class Notification : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notification)

        //loadNotificationData("1")

        go_back.setOnClickListener {
            finish()
        }
    }


    fun loadNotificationData(pageno:String){

        val params = HashMap<String, String>()

        params["page_no"] = pageno.toString()
        params["page_size"] = Constants.MAXIMUM_PAGINATION_COUNT
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.GetNotificationData(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN, params)

        call.enqueue(object : Callback<NotificationResponse> {
            override fun onResponse(call: Call<NotificationResponse>, response: Response<NotificationResponse>) {
                Logger.d("URL", "" + response+ EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
                var str_response = Gson().toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                val jsonObject1 : JSONObject = jsonObject.optJSONObject("body")
                val responseCode: Int = jsonObject1.optInt("response_code")
                val message: String = jsonObject1.optString("message")
                var jsonarray: JSONArray = jsonObject1.optJSONArray("body")
                var model: NotificationModel=NotificationModel();
                if(response.isSuccessful){
                    listOfPostdataDump.clear()
                    for (i in 0 until response.body()!!.body!!.size) {
                        var json_objectdetail:JSONObject=jsonarray.getJSONObject(i)
                        model = NotificationModel()
                        model.id = json_objectdetail.getInt("id")
                        model.entity_id = json_objectdetail.getInt("entity_id")
                        model.entity_type = json_objectdetail.getString("entity_type")
                        model.created_on = json_objectdetail.getString("created_on")
                        model.created_on_str = json_objectdetail.getString("created_on_str")
                        model.notification_str = json_objectdetail.getString("notification_str")
                        model.post_id = json_objectdetail.getInt("post_id")
                        if(json_objectdetail.has("comment_id"))
                         model.comment_id = json_objectdetail.getInt("comment_id")
                        else
                            model.comment_id = 0
                        model.group_id = json_objectdetail.getInt("group_id")
                        model.group_name = json_objectdetail.getString("group_name")
                        model.viewed = json_objectdetail.getBoolean("viewed")
                        model.notification_type = json_objectdetail.getString("notification_type")
                        model.icon = json_objectdetail.getString("icon")
                        listOfPostdataDump.add(
                            NotificationModel(
                                model.id,
                                model.entity_id!!,
                                model.entity_type!!,
                                model.created_on!!,
                                model.created_on_str!!,
                                model.notification_str!!,
                                model.post_id!!,
                                model.comment_id!!,
                                model.group_id!!,
                                model.group_name!!,
                                model.viewed!!,
                                model.notification_type!!,
                                model.icon!!
                            )
                        )
                    }
                    Log.d("TAGG", " SIZE"+listOfPostdataDump.size)
                    mRecyclerViewJobs = findViewById(R.id.notificaton_recycler_view)
                    var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                    mRecyclerViewJobs!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                    mAdapterJobs = NotificationAdapter(listOfPostdataDump)
                    mRecyclerViewJobs!!.adapter = mAdapterJobs
                }
                else {
                    ToastHelper.makeToast(applicationContext, "message")
                }
            }
            override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadNotificationData("1")
    }

}