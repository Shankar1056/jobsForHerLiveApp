package com.jobsforher.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jobsforher.R
import com.jobsforher.adapters.HomePageAdapter
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.network.responsemodels.AccountSettingsDetails
import com.jobsforher.network.responsemodels.RegisterIdResponse
import com.jobsforher.network.responsemodels.UserSelection
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import kotlinx.android.synthetic.main.activity_homepage.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

private val PREF_STATUS = "isLoggedInStatus"
private val PREF_NAME = "name"
private val PREF_PROFILEURL = "profileUrl"
private val PREF_ACCESSTOKEN = "accesstoken"
private val PREF_PERCENTAGE = "0"
private val PREF_FCM = "fcmtoken"
private var retrofitInterface: RetrofitInterface? = null
private var retrofitInterface1: RetrofitInterface? = null
private var retrofitInterface2: RetrofitInterface? = null


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class HomePage : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false
    var mRecyclerView: RecyclerView? = null
    var mAdapter: RecyclerView.Adapter<*>? = null
    var listOfCategories: ArrayList<String> = ArrayList()
    var listOfDescription: ArrayList<String> = ArrayList()
    var listOfmages: ArrayList<Int> = ArrayList()
    var choice: ArrayList<String> = ArrayList()
    var id:Int=0
    var userid:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        //var fcm = intent.getStringExtra("fcm")


        val sharedPref: SharedPreferences = getSharedPreferences("mysettings",
            Context.MODE_PRIVATE);
        EndPoints.ACCESS_TOKEN = sharedPref.getString(PREF_ACCESSTOKEN, "")!!
        EndPoints.USERNAME = sharedPref.getString(PREF_NAME, "")!!
        EndPoints.PROFILE_URL = sharedPref.getString(PREF_PROFILEURL, "")!!
        EndPoints.profileUrl=sharedPref.getString(PREF_PROFILEURL, "")!!

        var fcm :String= sharedPref.getString(PREF_FCM,"")!!


        Log.d("TAGG","HOMEPAGE"+fcm)

        registerFCMToken(
            Settings.Secure.getString(applicationContext.getContentResolver(),
                Settings.Secure.ANDROID_ID),fcm!!)

        loadAccountSettings()

        listOfCategories.add( "Groups")
        listOfCategories.add( "Jobs")
        //  listOfCategories.add( "Companies")
        listOfCategories.add( "Events")
        listOfCategories.add( "Mentors")
        listOfCategories.add( "Reskilling")
        listOfCategories.add( "Blogs")
        // listOfCategories.add( "Option8")


        listOfmages.add(R.drawable.ic_groups)
        listOfmages.add(R.drawable.ic_briefcase1)
        // listOfmages.add(R.drawable.ic_companiess)
        listOfmages.add(R.drawable.ic_events)
        listOfmages.add(R.drawable.ic_mentors)
        listOfmages.add(R.drawable.ic_reskilling)
        listOfmages.add(R.drawable.ic_blog)
        //  listOfmages.add(R.drawable.ic_newsfeed_events)


        listOfDescription.add( "Connect with Like-minded Women")
        listOfDescription.add( "Apply to Family-friendly Companies")
        //  listOfDescription.add( "Companies")
        listOfDescription.add( "Attend Networking and Recruitment Events")
        listOfDescription.add( "Gain Guidance and Inspiration")
        listOfDescription.add( "Explore Courses, Assessments & Experts")
        listOfDescription.add( "Get Advice,\n Tips and More")
        //listOfDescription.add( "Events")


        getUserSelection()


        skip.setOnClickListener {
            intent = Intent(applicationContext, NewsFeed::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

        }

        next.setOnClickListener {
            Log.d("TAGG",choice.toString())
            if(id==0)
                postUserSelection(choice)
            else
                updateUserSelection(choice)
        }
    }

    fun createInterest(s:String){
        choice.add(s)
    }

    fun deleteInterest(s:String){
        choice.remove(s)
    }


    fun getUserSelection(){

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getUserSelection(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<UserSelection> {
            override fun onResponse(call: Call<UserSelection>, response: Response<UserSelection>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE follow company", "" + Gson().toJson(response))

                if (response.isSuccessful && response.body()!!.responseCode ==10000) {
                    //   Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                    val a :String = response.body()!!.body!!.entity_values!!
                    var output:List<String> = a . split (",");
                    choice.addAll(output)
                    id = response.body()!!.body!!.id!!




                } else {
                    //ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())


                }
                Log.d("TAGG","Choice before"+choice.toString())
                mRecyclerView = findViewById(R.id.my_recycler_view)
                val mLayoutManager = GridLayoutManager(applicationContext, 2)
                mRecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
                mAdapter = HomePageAdapter(listOfCategories,listOfmages,choice,listOfDescription)
                mRecyclerView!!.adapter = mAdapter
            }

            override fun onFailure(call: Call<UserSelection>, t: Throwable) {
                Logger.d("TAGG", "Apply Job FAILED : $t")
            }
        })
    }

    fun postUserSelection(arr:ArrayList<String>){

        val params = HashMap<String, String>()

        params["entity_type"] = "newsfeed"
        params["entity_values"] = arr.toString().substring(1,arr.toString().length-1)
        Log.d("TAGG",params.toString())

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.postUserSelection(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)

        call.enqueue(object : Callback<UserSelection> {
            override fun onResponse(call: Call<UserSelection>, response: Response<UserSelection>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE follow company", "" + Gson().toJson(response))

                if (response.isSuccessful && response.body()!!.responseCode ==10000) {
                    //  Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                    intent = Intent(applicationContext, HomePagePreferences::class.java)
                    intent.putExtra("userID",userid)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                } else {
                    //ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())


                }
            }

            override fun onFailure(call: Call<UserSelection>, t: Throwable) {
                Logger.d("TAGG", "Apply Job FAILED : $t")
            }
        })
    }

    fun updateUserSelection(arr:ArrayList<String>){

        val params = HashMap<String, String>()

        params["entity_type"] = "newsfeed"
        params["entity_values"] = arr.toString().substring(1,arr.toString().length-1).toString()
        Log.d("TAGG",params.toString())
        retrofitInterface1 = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface1!!.updateUserSelection(id.toString(),EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)

        call.enqueue(object : Callback<UserSelection> {
            override fun onResponse(call: Call<UserSelection>, response: Response<UserSelection>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE follow company", "" + Gson().toJson(response))

                if (response.isSuccessful && response.body()!!.responseCode ==10000) {
                    //  Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                    intent = Intent(applicationContext, HomePagePreferences::class.java)
                    intent.putExtra("userID",userid)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())


                }
            }

            override fun onFailure(call: Call<UserSelection>, t: Throwable) {
                Logger.d("TAGG", "Apply Job FAILED : $t")
            }
        })
    }

    fun loadAccountSettings(){

        retrofitInterface2 = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface2!!.getAccountSettingsData(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)
        call.enqueue(object : Callback<AccountSettingsDetails> {

            override fun onResponse(call: Call<AccountSettingsDetails>, response: Response<AccountSettingsDetails>) {

                Logger.d("URL", "Applied" + response+ EndPoints.CLIENT_ID)
                Logger.d("CODE", "Applied"+response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "Applied" + Gson().toJson(response))

                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                val jsonObject1 : JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                val jsonarray_info: JSONObject = jsonObject1.getJSONObject("body")
                Log.d("TAGG","Settings "+jsonarray_info.getInt("id"))

                if (response.isSuccessful) {

                    userid = jsonarray_info.getInt("id")

                } else {
                    // ToastHelper.makeToast(applicationContext, message)
                }
            }

            override fun onFailure(call: Call<AccountSettingsDetails>, t: Throwable) {
                Logger.d("TAGG", "FAILED AccNT Settings : $t")
            }
        })

    }

    private fun registerFCMToken(device_id: String, fcm_id:String) {

        val params = HashMap<String, String>()

        val installationId = device_id

        params["device_id"] = installationId
        params["fcm_id"] = fcm_id

        Log.d("TAGG", "Params : " + params)

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.registerFcmId("application/json", EndPoints.CLIENT_ID,"Bearer "+EndPoints.ACCESS_TOKEN,
            params)
        call.enqueue(object : Callback<RegisterIdResponse> {

            override fun onResponse(call: Call<RegisterIdResponse>, response: Response<RegisterIdResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGESplash", response.message() + "")
                Logger.d("RESPONSE FCM", "" + Gson().toJson(response))
                Logger.d("URL", "" + response)


                if (response.isSuccessful) {
//                    ToastHelper.makeToast(applicationContext, response.body()!!.auth!!.access_token)
                    //   ToastHelper.makeToast(applicationContext, "FCm token registered")

                    Logger.d("URLDeviceId", "" + response.body()!!.body!!.device_id.toString())
                } else {
                    ToastHelper.makeToast(applicationContext, "Response Failed")
                }

            }

            override fun onFailure(call: Call<RegisterIdResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })
    }


    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true

        ToastHelper.makeToast(applicationContext, "Please click BACK again to exit")

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }
}