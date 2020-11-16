package com.jobsforher.activities

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_edit_post.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.jobsforher.R
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.network.responsemodels.CreatePostResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import women.jobs.jobsforher.activities.BaseActivity
import java.util.*

class ZEditCommentActivity : BaseActivity() {

    var id:Int=0
    var data: String= "";
    var icon: String=""
    private var retrofitInterface: RetrofitInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.layout_edit_post)
        id=intent.getIntExtra("id",0)
        data = intent.getStringExtra("data")
        icon = intent.getStringExtra("icon")
        load_image.visibility = View.GONE
        editpost_edittext.text = Editable.Factory.getInstance().newEditable(data)
        if(icon.isNotEmpty()) {
            Picasso.with(applicationContext)
                .load(icon)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(icon_profile)
        }else{
            Picasso.with(applicationContext)
                .load(R.drawable.ic_launcher_foreground)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(icon_profile)
        }
        //groupId=intent.getStringExtra("groupID")
        update_post.setOnClickListener {
            if(editpost_edittext.text.trim().toString().length>1) {
                editComentData(id, editpost_edittext.text.trim().toString())
            }
            else{
                Toast.makeText(applicationContext, "Please enter some text", Toast.LENGTH_LONG).show()
            }
        }

        cancel_post.setOnClickListener {
            finish()
        }
    }

    fun editComentData(id: Int, data: String?){

        val params = HashMap<String, String>()

        params["comment"] = editpost_edittext.text.toString()

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.updateComent(id, "application/json",EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN, params)
        call.enqueue(object : Callback<CreatePostResponse> {

            override fun onResponse(call: Call<CreatePostResponse>, response: Response<CreatePostResponse>) {

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

                if (response.isSuccessful && responseCode==10302) {
//                    for (i in 0 until response.body()!!.body!!.size) {
//                        var json_objectdetail: JSONObject = jsonarray.getJSONObject(i)
//                        listOfPhotos.add(json_objectdetail.getString("url"))
//
//                    }
                    Toast.makeText(applicationContext,"Comment Updated!!", Toast.LENGTH_LONG).show()
                    setResult(1);
                    finish()
                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid")
                }

            }

            override fun onFailure(call: Call<CreatePostResponse>, t: Throwable) {

                Logger.d("TAG", "FAILED : $t")
            }
        })


    }


}
