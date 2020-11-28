package com.jobsforher.expert_chat

//import com.jobsforher.activities.TestActivity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jobsforher.R
import com.jobsforher.activities.DetailsNotificationActivity
import com.jobsforher.databinding.ActivityExpertChatBinding
import com.jobsforher.helpers.HelperMethods
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.models.Categories
import com.jobsforher.models.GroupsView
import com.jobsforher.network.responsemodels.Featured_Group
import com.jobsforher.network.responsemodels.JoinGroupResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import com.jobsforher.expert_chat.adapter.ExpertChatAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_expert_chat.*
import kotlinx.android.synthetic.main.toolbar_groups.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ExpertChatActivity : AppCompatActivity() {
    val viewModel: ExpertChatViewModel by viewModels()
    private var retrofitInterface: RetrofitInterface? = null
    var listOfMyGroupdata: ArrayList<GroupsView> = ArrayList()
    var listOfCompareGroupdata: ArrayList<GroupsView> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityExpertChatBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_expert_chat
        )

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        loadMyGroupData("1")

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        viewModel.getCurrentDate(this@ExpertChatActivity)
        viewModel.getExpertChat(1, 30)
       // viewModel.getExpertChatWeekly(23,27)

        if(EndPoints.PROFILE_ICON.length>0) {
            Picasso.with(applicationContext)
                .load(EndPoints.PROFILE_ICON)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(img_profile_toolbar)
        }


        viewModel.expertChatList.observe(this, Observer {
            if(it.size==0){
                empty_view.visibility = View.VISIBLE
                bottomCV.visibility = View.GONE
            }
            else {
                empty_view.visibility = View.GONE
                bottomCV.visibility = View.VISIBLE
            }

            expertChatRV.adapter = ExpertChatAdapter(it, object :
                ExpertChatAdapter.OnItemClickListener {
                override fun onJoinClicked(pos: Int) {
                    for (i in 0 until listOfMyGroupdata.size) {
                        if (it[pos].group_id == listOfMyGroupdata[i].id) {

//                            startActivity(Intent(this@ExpertChatActivity, ZActivityGroupsDetails::class.java).
//                            putExtra("group_id", listOfMyGroupdata[i].id.toString()).
//                            putExtra("group_type", listOfMyGroupdata[i].groupType).
//                            putExtra("isMygroup", 1). //listOfMyGroupdata[i].is_member
//                            putExtra("page", Constants.EXPERT_CHAT)
//                            )

                            startActivity(
                                Intent(
                                    this@ExpertChatActivity,
                                    DetailsNotificationActivity::class.java
                                ).putExtra("post_id", it[pos].post_id.toString())
                            )
                            return
                        }
                    }

                    val builder = AlertDialog.Builder(this@ExpertChatActivity)
                    builder.setTitle("Join group")
                    builder.setMessage("Are you sure you want to join this group?")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))
                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                        it[pos].group_id?.let { it1 ->
                            joinGroup(
                                it1,
                                "private",
                                it[pos].post_id!!
                            )
                        }
                    }

                    builder.setNegativeButton(android.R.string.no) { dialog, which ->
                        dialog.cancel()
                    }
                    builder.show()
                }

                override fun onViewDetailsClicked(pos: Int) {
                    onJoinClicked(pos)
                }

            })
        })
        buttonGo.setOnClickListener {
            viewModel.onGoClicked(it)
            foo(HelperMethods.getMonthsInInt(selectMonth.text!!.toString())!! - 1)
        }

        foo(10)
    }

    fun showDefaultText(){


        if(viewModel.expertChatForFilter.size==0){
//            expertChatRV.visibility = View.GONE
//            bottomCV.visibility = View.GONE
            empty_view.visibility = View.VISIBLE
            Log.d("TAGG", "SIZE if" + viewModel.expertChatForFilter.size)
        }
        else{
//            expertChatRV.visibility = View.VISIBLE
//            bottomCV.visibility = View.VISIBLE
            empty_view.visibility = View.GONE
            Log.d("TAGG", "SIZE else" + viewModel.expertChatForFilter.size)
        }
    }

    fun joinGroup(id: Int, type: String, postId: Int){

        Logger.d("CODE", id.toString() + "")

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.JoinGroup(
            id,
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN
        )

        call.enqueue(object : Callback<JoinGroupResponse> {
            override fun onResponse(
                call: Call<JoinGroupResponse>,
                response: Response<JoinGroupResponse>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful && response.body()!!.responseCode == 10104) {

                    if (response.body()!!.responseCode == 10108) {
                        if (response.body()!!.responseCode == 10108) {
                            startActivity(
                                Intent(
                                    this@ExpertChatActivity,
                                    DetailsNotificationActivity::class.java
                                ).putExtra("post_id", postId.toString())
                            )
                        }
                    } else {
                        if (type.equals("private")) {
                            //btnJoinGroup.text = "Requested"
                        } else {
//                            btnJoinGroup.visibility = View.GONE
//                            btnJoined.visibility = View.VISIBLE
                        }
                    }
                    startActivity(
                        Intent(
                            this@ExpertChatActivity,
                            DetailsNotificationActivity::class.java
                        ).putExtra("post_id", postId.toString())
                    )

                } else {
                    if (response.body()!!.responseCode == 10104) {
                        //btnJoinGroup.text = "Requested"
                        startActivity(
                            Intent(
                                this@ExpertChatActivity,
                                DetailsNotificationActivity::class.java
                            ).putExtra("post_id", postId.toString())
                        )
                    }
                }
            }

            override fun onFailure(call: Call<JoinGroupResponse>, t: Throwable) {

                Logger.d("TAGG", "Join Group FAILED : $t")

            }
        })

    }

    fun foo(month: Int) {

        Log.d("TAGG", "Month" + month)
        if ((week as LinearLayout).getChildCount() > 0) (week as LinearLayout).removeAllViews()
        val calendar = Calendar.getInstance()
        calendar.setFirstDayOfWeek(Calendar.MONDAY)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, 2020)
        val date1: Date = calendar.getTime()
        //current date to check that our week lies in same month or not
        //current date to check that our week lies in same month or not
        val checkformate = SimpleDateFormat("MM/yyyy")
        val currentCheckdate = checkformate.format(date1)

        val weekn: Int = calendar.get(Calendar.WEEK_OF_MONTH)
        val month: Int = calendar.get(Calendar.MONTH)
        val year: Int = calendar.get(Calendar.YEAR)
        //resat calender without date
        //resat calender without date
        var check: Boolean = true
        Log.d("TAGG", "Total weeks:" + calendar.getActualMaximum(Calendar.WEEK_OF_MONTH).toString())
        for (i in 0 until calendar.getActualMaximum(Calendar.WEEK_OF_MONTH) + 1) {
            val myTextViews = arrayOfNulls<TextView>(calendar.getActualMaximum(Calendar.WEEK_OF_MONTH) + 1)
            calendar.clear()
            calendar.setFirstDayOfWeek(Calendar.MONDAY)
            calendar.set(Calendar.WEEK_OF_MONTH, i)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.YEAR, year)

            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

            val datef: Date = calendar.getTime()
            //move date to 6 days + to get last date of week
            //move date to 6 days + to get last date of week
            val timeSixDayPlus: Long = calendar.getTimeInMillis() + 518400000L
            val dateL = Date(timeSixDayPlus)
            var firtdate = simpleDateFormat.format(datef)
            var lastdate = simpleDateFormat.format(dateL)
            val firtdateCheck = checkformate.format(datef)
            val lastdateCheck = checkformate.format(dateL)

            //if our week lies in two different months then we show only current month week part only

            //if our week lies in two different months then we show only current month week part only
            if (!firtdateCheck.toString().equals(currentCheckdate, ignoreCase = true)) {
                firtdate =
                    "1" + "/" + (Integer.parseInt(
                        firtdate.split("/").get(1)
                    ) + 1) + "/" + calendar.get(
                        Calendar.YEAR
                    )
//                "1" + "/" + calendar.get(Calendar.MONTH+2) + "/" + calendar.get(Calendar.YEAR)
            }

            if (!lastdateCheck.toString().equals(currentCheckdate, ignoreCase = true)) {
                val ma: Int = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                lastdate =
                    ma.toString() + "/" + (Integer.parseInt(
                        lastdate.split("/").get(1)
                    ) - 1) + "/" + calendar.get(
                        Calendar.YEAR
                    )
//                ma.toString() + "/" + calendar.get(Calendar.MONTH+1) + "/" + calendar.get(Calendar.YEAR)
            }

            if (Integer.parseInt(
                    (firtdate.split("/").get(1))
                ) == month+1 && Integer.parseInt((lastdate.split("/").get(1))) == month+1
            ) {
                Log.d("TAGG", "=>>$firtdate to $lastdate")
                var rowTextView: TextView = TextView(applicationContext);
                val monthString = HelperMethods.getMonthsInString(month + 1)!!.substring(0, 3)
                rowTextView.setText(
                    "WEEK " + i + " \n" + monthString + "${firtdate.split("/").get(0)} - ${
                        lastdate.split(
                            "/"
                        ).get(0)
                    }"
                );
                rowTextView.setBackgroundResource(R.drawable.top_curved_black)
                rowTextView.setTextColor(Color.WHITE)
                rowTextView.gravity= Gravity.CENTER_HORIZONTAL
                rowTextView.setPadding(10, 5, 10, 5)
                week.addView(rowTextView);
                myTextViews[i] = rowTextView;
                rowTextView.setOnClickListener {
                    for(k in 0 until (week as LinearLayout).getChildCount()){
                        (week as LinearLayout).getChildAt(k).setBackgroundResource(R.drawable.top_curved_black)
                    }
                    rowTextView.setBackgroundResource(R.drawable.top_curved_green)
                    viewModel.getExpertChat(
                        Integer.parseInt(firtdate.split("/").get(0)),
                        Integer.parseInt(lastdate.split("/").get(0))
                    )
//                    showDefaultText()
//                    viewModel.getExpertChatWeekly(Integer.parseInt(firtdate.split("/").get(0)),
//                                                    Integer.parseInt(lastdate.split("/").get(0)))
                }
            }
        }

//        showDefaultText()
    }

    fun loadMyGroupData(pageno: String){

        val params = HashMap<String, String>()

        params["page_no"] = pageno.toString()
        //  params["page_size"] = Constants.MAXIMUM_PAGINATION_COUNT // it was 30 before

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface!!.getMyGroupData(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<Featured_Group> {
            override fun onResponse(
                call: Call<Featured_Group>,
                response: Response<Featured_Group>
            ) {

                Logger.d("URL", "" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)

                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf(
                            "{"
                        ), str_response.lastIndexOf("}") + 1
                    )
                )
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
                    if (Integer.parseInt(pagenoo) > 1)
                        prev_page = Integer.parseInt(pagenoo) - 1
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
                            val categoriesArray: JSONArray =
                                json_objectdetail.optJSONArray("categories")
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
                } else {
                    Toast.makeText(applicationContext, "No data exists", Toast.LENGTH_LONG).show()

                }

            }

            override fun onFailure(call: Call<Featured_Group>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(
                    applicationContext,
                    "No Groups Exists, Join a group today!",
                    Toast.LENGTH_LONG
                ).show()

            }
        })
    }
}