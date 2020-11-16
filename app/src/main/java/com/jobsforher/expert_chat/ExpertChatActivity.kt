package com.jobsforher.expert_chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jobsforher.R
import com.jobsforher.activities.ZActivityGroupsDetails
//import com.jobsforher.activities.TestActivity
import com.jobsforher.databinding.ActivityExpertChatBinding
import com.jobsforher.helpers.Constants
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.models.Categories
import com.jobsforher.models.GroupsView
import com.jobsforher.network.responsemodels.Featured_Group
import com.jobsforher.network.responsemodels.JoinGroupResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import com.jobsforher.ui.expert_chat.adapter.ExpertChatAdapter
import kotlinx.android.synthetic.main.activity_expert_chat.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class ExpertChatActivity : AppCompatActivity() {
    val viewModel: ExpertChatViewModel by viewModels()
    private var retrofitInterface: RetrofitInterface? = null
    var listOfMyGroupdata: ArrayList<GroupsView> = ArrayList()
    var listOfCompareGroupdata: ArrayList<GroupsView> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityExpertChatBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_expert_chat)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.getCurrentDate(this@ExpertChatActivity)
        viewModel.getExpertChat()

        viewModel.expertChatList.observe(this, Observer {
            expertChatRV.adapter = ExpertChatAdapter(it, object :
                ExpertChatAdapter.OnItemClickListener {
                override fun onJoinClicked(pos: Int) {
                    for (i in 0 until listOfMyGroupdata.size) {
                        if (it[pos].group_id == listOfMyGroupdata[i].id) {

                            startActivity(Intent(this@ExpertChatActivity, ZActivityGroupsDetails::class.java).
                            putExtra("group_id", listOfMyGroupdata[i].id.toString()).
                            putExtra("group_type", listOfMyGroupdata[i].groupType).
                            putExtra("isMygroup", 1). //listOfMyGroupdata[i].is_member
                            putExtra("page", Constants.EXPERT_CHAT)
                            )
                            return
                        }
                    }

                    val builder = AlertDialog.Builder(this@ExpertChatActivity)
                    builder.setTitle("Join group")
                    builder.setMessage("Are you sure you want to join this group?")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))
                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                        it[pos].group_id?.let { it1 -> joinGroup(it1, "private") }
                    }

                    builder.setNegativeButton(android.R.string.no) { dialog, which ->
                        dialog.cancel()
                    }
                    builder.show()
                }

                override fun onViewDetailsClicked(pos: Int) {
                    Toast.makeText(
                        this@ExpertChatActivity,
                        "viewDetails Clicked : pos - " + pos,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        })
        buttonGo.setOnClickListener {
            //startActivity(Intent(this@ExpertChatActivity, TestActivity::class.java))
        }
    }

    fun joinGroup(id:Int, type:String){

        Logger.d("CODE",id.toString() + "")

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.JoinGroup(id, EndPoints.CLIENT_ID, "Bearer "+ EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<JoinGroupResponse> {
            override fun onResponse(call: Call<JoinGroupResponse>, response: Response<JoinGroupResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful && response.body()!!.responseCode == 10104) {

                    if(response.body()!!.responseCode == 10108){
                        if (response.body()!!.responseCode == 10108){
                            //btnJoinGroup.text = "Requested"
                        }
                    }
                    else {
                        if (type.equals("private")) {
                            //btnJoinGroup.text = "Requested"
                        } else {
//                            btnJoinGroup.visibility = View.GONE
//                            btnJoined.visibility = View.VISIBLE
                        }

                    }

                } else {
                    if (response.body()!!.responseCode == 10108){
                        //btnJoinGroup.text = "Requested"
                    }
                }
            }

            override fun onFailure(call: Call<JoinGroupResponse>, t: Throwable) {

                Logger.d("TAGG", "Join Group FAILED : $t")

            }
        })

    }

    fun loadMyGroupData(pageno:String){

        val params = HashMap<String, String>()

        params["page_no"] = pageno.toString()
        //  params["page_size"] = Constants.MAXIMUM_PAGINATION_COUNT // it was 30 before

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface!!.getMyGroupData( EndPoints.CLIENT_ID, "Bearer "+ EndPoints.ACCESS_TOKEN,params)
        call.enqueue(object : Callback<Featured_Group> {
            override fun onResponse(call: Call<Featured_Group>, response: Response<Featured_Group>) {

                Logger.d("URL", "" + response+ EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)

                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                Log.d("TAGG", "Outside body")
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {

                    Log.d("TAGG", "Inside body")
                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")

                    var jsonarray_info: JSONArray = jsonObject1.optJSONArray("body")

                    var jsonarray_pagination: JSONObject = jsonObject1.optJSONObject("pagination")

                    val has_next: String = jsonarray_pagination.optBoolean("has_next").toString()

                    val pagenoo: String = jsonarray_pagination.optString("page_no")
                    val prev_page: Int
//                    if (jsonarray_pagination.has("prev_page") && !jsonarray_pagination.optString("prev_page").equals(""))
//                        prev_page = jsonarray_pagination.optInt("prev_page")
//                    else
//                        prev_page = 0
                    if (Integer.parseInt(pagenoo)>1)
                        prev_page = Integer.parseInt(pagenoo)-1
                    else
                        prev_page = 0

                    if (response.isSuccessful) {

                        // ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                        Log.d("TAGG", "Inside response success")
                        for (i in 0 until response.body()!!.body!!.size) {
                            Log.d("TAGG", "Inside response body")
                            var json_objectdetail: JSONObject = jsonarray_info.optJSONObject(i)

                            val model: GroupsView = GroupsView();

                            model.id = json_objectdetail.optInt("id")
                            model.label = json_objectdetail.optString("name")
                            model.icon_url = json_objectdetail.optString("icon_url")
                            model.groupType = json_objectdetail.optString("visiblity_type")
                            model.noOfMembers = json_objectdetail.optString("no_of_members")
                            model.description = json_objectdetail.optString("excerpt")
                            model.featured = json_objectdetail.optBoolean("featured")
                            model.status = json_objectdetail.optString("status")
                            model.is_member = json_objectdetail.optBoolean("is_member")

                            var citiesArray: JSONArray = json_objectdetail.optJSONArray("cities")
                            val listOfCity: ArrayList<Int> = ArrayList()
//                        for (j in 0 until citiesArray.length()) {
//                            var citiesObject:JSONObject=citiesArray.optJSONObject(j).
//                            listOfCity.add(citiesObject)
//                        }
                            val categoriesArray: JSONArray = json_objectdetail.optJSONArray("categories")
                            val listOfCategories: ArrayList<Categories> = ArrayList()
                            for (k in 0 until categoriesArray.length()) {
                                var categoriesIdObj: JSONObject = categoriesArray.optJSONObject(k)
                                listOfCategories.add(
                                    Categories(
                                        categoriesIdObj.optInt("category_id"),
                                        categoriesIdObj.optString("category")
                                    )
                                )
                            }
                            model.categories = listOfCategories
                            model.cities = listOfCity


                            listOfMyGroupdata.add(
                                GroupsView(
                                    model.id,
                                    model.icon_url!!,
                                    model.label!!,
                                    "",
                                    model.groupType!!,
                                    model.noOfMembers!!,
                                    model.description!!,
                                    model.featured!!,
                                    model.status!!,
                                    model.cities!!,
                                    model.categories!!,
                                    model.is_member!!
                                )
                            )

                            listOfCompareGroupdata.add(
                                GroupsView(
                                    model.id,
                                    model.icon_url!!,
                                    model.label!!,
                                    "",
                                    model.groupType!!,
                                    model.noOfMembers!!,
                                    model.description!!,
                                    model.featured!!,
                                    model.status!!,
                                    model.cities!!,
                                    model.categories!!,
                                    model.is_member!!
                                )
                            )
                        }



                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid Request")
                    }

//                    if (pagenoo == "1")
//                        loadprev.visibility = View.GONE
//                    else {
//                        loadprev.visibility = View.VISIBLE
//                        prev_page_no_featured = prev_page.toString()
//                    }
                }
                else {
                    Toast.makeText(applicationContext, "No data exists", Toast.LENGTH_LONG).show()

                }

            }

            override fun onFailure(call: Call<Featured_Group>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext, "No Groups Exists, Join a group today!", Toast.LENGTH_LONG).show()

            }
        })
    }
}