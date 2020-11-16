package com.jobsforher.activities

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.layout_edithistory_comments.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.jobsforher.R
import com.jobsforher.adapters.EditedHistoryCommentsAdapter
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.models.GroupsEditedCommentsHistoryModel
import com.jobsforher.network.responsemodels.EditHistoryCommentResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import women.jobs.jobsforher.activities.BaseActivity
import java.util.*

class ZEditComentHistoryActivity : BaseActivity() {

    var id:Int=0
    var data: String= "";
    var icon:String=""
    private var retrofitInterfaceposts: RetrofitInterface? = null
    var listOfData: ArrayList<GroupsEditedCommentsHistoryModel> = ArrayList()
    var mRecyclerViewData: RecyclerView? = null
    var mAdapterData: RecyclerView.Adapter<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.layout_edithistory_comments)
        id=intent.getIntExtra("id",0)
        icon=intent.getStringExtra("icon")
        Log.d("TAGG2", "INSIDE functioj"+icon)
        editComentsHistoryData(id)
//        if(icon.isNotEmpty()) {
//            Picasso.with(applicationContext)
//                .load(icon)
//                .placeholder(R.drawable.ic_launcher_foreground)
//                .into(commenticon)
//        }else{
//            Picasso.with(applicationContext)
//                .load(R.drawable.ic_launcher_foreground)
//                .placeholder(R.drawable.ic_launcher_foreground)
//                .into(commenticon)
//        }
        edit_history_back.setOnClickListener{
            finish()
        }
    }



    fun editComentsHistoryData(id: Int){

        retrofitInterfaceposts = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        Log.d("TAGG2", "INSIDE functioj"+id)
//        Toast.makeText(applicationContext, id.toString()+"", Toast.LENGTH_LONG).show()
        val call = retrofitInterfaceposts!!.getEditedCommentHistory(id, EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)
        call.enqueue(object : Callback<EditHistoryCommentResponse> {

            override fun onResponse(call: Call<EditHistoryCommentResponse>, response: Response<EditHistoryCommentResponse>) {

                Log.d("TAGG2","CODE"+ response.code().toString() + "")
                Log.d("TAGG2","MESSAGE"+ response.message() + "")
                Log.d("TAGG2","RESPONSE"+ "" + Gson().toJson(response))
                Log.d("TAGG2","URL"+ "" + response)
                //var str_response = Gson().toJson(response)
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                val jsonObject1 : JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                if (response.isSuccessful) {
                    for (i in 0 until response.body()!!.body!!.size) {
                        var json_objectdetail: JSONObject = jsonarray.getJSONObject(i)
                        var model= GroupsEditedCommentsHistoryModel();
                        model.index=json_objectdetail.getInt("index")
                        model.entity_type=json_objectdetail.getString("entity_type")
                        model.entity_value=json_objectdetail.getString("entity_value")
                        model.url=json_objectdetail.getString("url")
                        model.modified_on=json_objectdetail.getString("modified_on")
                        model.modified_on_str=json_objectdetail.getString("modified_on_str")
                        model.created_by=json_objectdetail.getString("created_by")
                        model.username=json_objectdetail.getString("username")
                        model.email=json_objectdetail.getString("email")
                        model.profile_icon=json_objectdetail.getString("profile_icon")
                        model.user_role=json_objectdetail.getString("user_role")

                        listOfData.add(model)

                    }

                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid")
                }

                mRecyclerViewData = findViewById(R.id.recycler_view_edithistorycoments)
                var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                mRecyclerViewData!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                mAdapterData = EditedHistoryCommentsAdapter(listOfData)
                mRecyclerViewData!!.adapter = mAdapterData

            }

            override fun onFailure(call: Call<EditHistoryCommentResponse>, t: Throwable) {

                Logger.d("TAG", "FAILED : $t")
            }
        })


    }


}
