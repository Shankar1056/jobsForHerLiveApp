package com.jobsforher.activities

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.editreply_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.jobsforher.R
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.network.responsemodels.UpdateReplyResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import women.jobs.jobsforher.activities.BaseActivity
import java.util.*

class ZEditReplyActivity : BaseActivity() {

    var id:Int=0
    var data: String= "";
    var icon: String=""
    private var retrofitInterface: RetrofitInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.editreply_layout)
        id=intent.getIntExtra("id",0)
        data = intent.getStringExtra("data")
        icon = intent.getStringExtra("icon")
//        load_image.visibility = View.GONE
        editReplyId.text = Editable.Factory.getInstance().newEditable(data)
//        if(icon.isNotEmpty()) {
//            Picasso.with(applicationContext)
//                .load(icon)
//                .placeholder(R.drawable.ic_launcher_foreground)
//                .into(icon_profile)
//        }else{
//            Picasso.with(applicationContext)
//                .load(R.drawable.ic_launcher_foreground)
//                .placeholder(R.drawable.ic_launcher_foreground)
//                .into(icon_profile)
//        }
        //groupId=intent.getStringExtra("groupID")
        update.setOnClickListener {
            editReplyData(id, data)
        }

        can.setOnClickListener {
            finish()
        }

        cancel.setOnClickListener {
            finish()
        }
    }


    fun editReplyData(id: Int, data: String?){

        val params = HashMap<String, String>()

        params["reply"] = editReplyId.text.toString()

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.UpdateReply(id, "application/json",EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN, params)
        call.enqueue(object : Callback<UpdateReplyResponse> {

            override fun onResponse(call: Call<UpdateReplyResponse>, response: Response<UpdateReplyResponse>) {

                Log.d("TAG","CODE"+ response.code().toString() + "")
                Log.d("TAG","MESSAGE"+ response.message() + "")
                Log.d("TAG","RESPONSE"+ "" + Gson().toJson(response))
                Log.d("TAG","URL"+ "" + response)
                // var str_response = Gson().toJson(response)
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                val jsonObject1 : JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
//                var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                if (response.isSuccessful ) {
//                    for (i in 0 until response.body()!!.body!!.size) {
//                        var json_objectdetail: JSONObject = jsonarray.getJSONObject(i)
//                        listOfPhotos.add(json_objectdetail.getString("url"))
//
//                    }
                    Toast.makeText(applicationContext,"Reply Updated!!", Toast.LENGTH_LONG).show()
                    setResult(1);
                    finish()
                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid")
                }

            }

            override fun onFailure(call: Call<UpdateReplyResponse>, t: Throwable) {

                Logger.d("TAG", "FAILED : $t")
            }
        })


    }


}
