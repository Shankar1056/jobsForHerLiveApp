package com.jobsforher.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jobsforher.R
import com.jobsforher.helpers.HelperMethods
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.models.*
import com.jobsforher.network.responsemodels.*
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import com.jobsforher.ui.newsfeed.NewsFeedActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_homepage_preferences.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class HomePagePreferences : AppCompatActivity() {

    private var retrofitInterface: RetrofitInterface? = null
    private var retrofitInterface1: RetrofitInterface? = null
    private var retrofitInterface2: RetrofitInterface? = null
    private val GALLERY_PDF = 3
    public var picturepath_life:String? = ""
    public var imageEncoded_life = "";
    var listOfPreferencedata: ArrayList<PreferenceModel> = ArrayList()

    var listOfCategories: ArrayList<CategoryView> = ArrayList()
    var listOfCities: ArrayList<CityView> = ArrayList()
    var listOfJobType: ArrayList<JobTypeView> = ArrayList()
    var listOfJobFArea: ArrayList<FunctionalAreaView> = ArrayList()
    var listOfJobFAreaSorted: ArrayList<FunctionalAreaView> = ArrayList()
    var listOfJobIndustry: ArrayList<FunctionalAreaView> = ArrayList()
    var listOfCTCLacs: ArrayList<String> = ArrayList()
    var listOfCTCThousand: ArrayList<String> = ArrayList()

    var category: String=""
    var location_name: String=""
    var job_types: String=""
    var min_year: String=""
    var max_year: String=""
    var functional_area:String=""
    var industries:String=""
    var company_id:String=""
    var type: String=""
    private val PREF_ICON = "icon"
    var userID:Int = 0
    private val PREF_NAME = "name"
    private val PREF_PROFILEURL = "profileUrl"
    private val PREF_ACCESSTOKEN = "accesstoken"
    private val PREF_PERCENTAGE = "0"
    private val PREF_FCM = "fcmtoken"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage_preferences)
        userID = intent.getIntExtra("userID",0)
        val sharedPref: SharedPreferences = getSharedPreferences(
            "mysettings",
            Context.MODE_PRIVATE
        )
        EndPoints.PROFILE_ICON = sharedPref.getString(PREF_ICON, "0")!!
       // Toast.makeText(applicationContext,EndPoints.PROFILE_ICON,Toast.LENGTH_LONG).show()
        if (EndPoints.PROFILE_ICON.length > 4) {
            Picasso.with(applicationContext)
                .load(EndPoints.PROFILE_ICON)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(circleView)

        }
        EndPoints.ACCESS_TOKEN = sharedPref.getString(PREF_ACCESSTOKEN, "")!!
        EndPoints.USERNAME = sharedPref.getString(PREF_NAME, "")!!
        EndPoints.PROFILE_URL = sharedPref.getString(PREF_PROFILEURL, "")!!
        EndPoints.profileUrl=sharedPref.getString(PREF_PROFILEURL, "")!!
        row_text.setText("Welcome "+EndPoints.USERNAME+"!")
        if(userID==0){
            openScreen()
        }
        else{
            layout1.visibility= View.GONE
            //loadAccountSettings()
            loadCategoryData()

        }

    }

    fun loadCategoryData(){

        listOfCategories.clear()
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getCategories(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)
        call.enqueue(object : Callback<GetCategoryResponse> {

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(call: Call<GetCategoryResponse>, response: Response<GetCategoryResponse>) {

                Log.d("TAG","CODE"+ response.code().toString() + "")
                Log.d("TAG","MESSAGE"+ response.message() + "")
                Log.d("TAG","RESPONSE"+ "" + Gson().toJson(response))
                Log.d("TAG","URL"+ "" + response)
                //var str_response = Gson().toJson(response)
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                    val responseCode: Int = jsonObject1.getInt("response_code")
                    val message: String = jsonObject1.getString("message")
                    var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                    if (response.isSuccessful) {
                        for (i in 0 until response.body()!!.body!!.size) {
                            var json_objectdetail: JSONObject = jsonarray.getJSONObject(i)
                            val model: CategoryView = CategoryView();
                            model.id = json_objectdetail.getInt("id")
                            model.name = json_objectdetail.getString("name")
                            model.image_url = json_objectdetail.getString("image_url")
                            model.status = json_objectdetail.getString("status")
                            model.created_on = json_objectdetail.getString("created_on")
                            model.modified_on = json_objectdetail.getString("modified_on")
                            listOfCategories.add(model)
                        }
                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid")
                    }

                    Log.d("TAG", "DATA:" + listOfCategories.size)
                    loadCityData()
//                    openBottomSheetFilter()
                }
                else
                    Toast.makeText(applicationContext,"No data exists",Toast.LENGTH_LONG).show()
            }
            override fun onFailure(call: Call<GetCategoryResponse>, t: Throwable) {
                Logger.d("TAG", "FAILED : $t")
            }
        })
    }

    fun loadCityData(){

        listOfCities.clear()
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getCities(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)
        call.enqueue(object : Callback<GetCityResponse> {

            override fun onResponse(call: Call<GetCityResponse>, response: Response<GetCityResponse>) {

                Log.d("TAG","CODE"+ response.code().toString() + "")
                Log.d("TAG","MESSAGE"+ response.message() + "")
                Log.d("TAG","RESPONSE"+ "" + Gson().toJson(response))
                Log.d("TAG","URL"+ "" + response)
                //var str_response = Gson().toJson(response)
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                    val responseCode: Int = jsonObject1.getInt("response_code")
                    val message: String = jsonObject1.getString("message")
                    var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                    if (response.isSuccessful) {
                        for (i in 0 until response.body()!!.body!!.size) {
                            var json_objectdetail: JSONObject = jsonarray.getJSONObject(i)
                            val model: CityView = CityView();
                            model.id = json_objectdetail.getInt("id")
                            model.name = json_objectdetail.getString("label")
                            listOfCities.add(model)
                        }
                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid")
                    }


                    addJobType()
                }
                else
                    Toast.makeText(applicationContext,"No data exists",Toast.LENGTH_LONG).show()
            }
            override fun onFailure(call: Call<GetCityResponse>, t: Throwable) {
                Logger.d("TAG", "FAILED : $t")
            }
        })
    }

    fun addJobType(){

        listOfJobType.clear()
        listOfJobType.add(JobTypeView("Full Time","@drawable/ic_clock"))
        listOfJobType.add(JobTypeView("Part Time","@drawable/ic_clock"))
        listOfJobType.add(JobTypeView("Work From Home","@drawable/ic_house"))
        listOfJobType.add(JobTypeView("Returnee Program","@drawable/ic_clock"))
        listOfJobType.add(JobTypeView("Freelance/Projects","@drawable/ic_volunteer"))
        listOfJobType.add(JobTypeView("Volunteer","@drawable/ic_freelance"))

        listOfCTCLacs.clear()
        listOfCTCLacs.add("0 Lacs")
        listOfCTCLacs.add("1 Lacs")
        listOfCTCLacs.add("2 Lacs")
        listOfCTCLacs.add("3 Lacs")
        listOfCTCLacs.add("4 Lacs")
        listOfCTCLacs.add("5 Lacs")
        listOfCTCLacs.add("6 Lacs")
        listOfCTCLacs.add("7 Lacs")
        listOfCTCLacs.add("8 Lacs")
        listOfCTCLacs.add("9 Lacs")
        listOfCTCLacs.add("10 Lacs")
        listOfCTCLacs.add("11 Lacs")
        listOfCTCLacs.add("12 Lacs")
        listOfCTCLacs.add("13 Lacs")
        listOfCTCLacs.add("14 Lacs")
        listOfCTCLacs.add("15 Lacs")
        listOfCTCLacs.add("16 Lacs")
        listOfCTCLacs.add("17 Lacs")
        listOfCTCLacs.add("18 Lacs")
        listOfCTCLacs.add("19 Lacs")
        listOfCTCLacs.add("20 Lacs")
        listOfCTCLacs.add("21 Lacs")
        listOfCTCLacs.add("22 Lacs")
        listOfCTCLacs.add("23 Lacs")
        listOfCTCLacs.add("24 Lacs")
        listOfCTCLacs.add("25 Lacs")
        listOfCTCLacs.add("26 Lacs")
        listOfCTCLacs.add("27 Lacs")
        listOfCTCLacs.add("28 Lacs")
        listOfCTCLacs.add("29 Lacs")
        listOfCTCLacs.add("30 Lacs")

        listOfCTCThousand.clear()
        listOfCTCThousand.add("0 Thousand")
        listOfCTCThousand.add("5 Thousand")
        listOfCTCThousand.add("10 Thousand")
        listOfCTCThousand.add("15 Thousand")
        listOfCTCThousand.add("20 Thousand")
        listOfCTCThousand.add("25 Thousand")
        listOfCTCThousand.add("30 Thousand")
        listOfCTCThousand.add("35 Thousand")
        listOfCTCThousand.add("40 Thousand")
        listOfCTCThousand.add("45 Thousand")
        listOfCTCThousand.add("50 Thousand")
        listOfCTCThousand.add("55 Thousand")
        listOfCTCThousand.add("60 Thousand")
        listOfCTCThousand.add("65 Thousand")
        listOfCTCThousand.add("70 Thousand")
        listOfCTCThousand.add("75 Thousand")
        listOfCTCThousand.add("80 Thousand")
        listOfCTCThousand.add("85 Thousand")
        listOfCTCThousand.add("90 Thousand")
        listOfCTCThousand.add("95 Thousand")


        addJobFunctionalArea()
    }

    fun addJobFunctionalArea(){
        listOfJobFArea.clear()
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getFunctionalArea(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)
        call.enqueue(object : Callback<GetMobilityResponse> {

            override fun onResponse(call: Call<GetMobilityResponse>, response: Response<GetMobilityResponse>) {

                Log.d("TAG","CODE"+ response.code().toString() + "")
                Log.d("TAG","MESSAGE"+ response.message() + "")
                Log.d("TAG","RESPONSE"+ "" + Gson().toJson(response))
                Log.d("TAG","URL"+ "" + response)
                //var str_response = Gson().toJson(response)
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                    val responseCode: Int = jsonObject1.getInt("response_code")
                    val message: String = jsonObject1.getString("message")
                    var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                    if (response.isSuccessful) {
                        for (i in 0 until response.body()!!.body!!.size) {
                            var json_objectdetail: JSONObject = jsonarray.getJSONObject(i)
                            val model: FunctionalAreaView = FunctionalAreaView();
                            //model.id = json_objectdetail.getInt("id")
                            model.name = json_objectdetail.getString("name")
                            listOfJobFArea.add(model)
                        }
                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid")
                    }

                }
                else
                    Toast.makeText(applicationContext,"No data exists",Toast.LENGTH_LONG).show()
            }
            override fun onFailure(call: Call<GetMobilityResponse>, t: Throwable) {
                Logger.d("TAG", "FAILED : $t")
            }
        })

        Collections.sort(listOfJobFArea, object : Comparator<FunctionalAreaView> {
            override fun compare(lhs: FunctionalAreaView, rhs: FunctionalAreaView): Int {
                return lhs.name!!.compareTo(rhs.name!!)
            }
        })
//        listOfJobFAreaSorted = listOfJobFArea.sortedWith(compareBy({ it.customProperty }))
//        for (obj in listOfJobFAreaSorted) {
//            println(obj.customProperty)
//        }
        val functionalareaList: ArrayList<String> = ArrayList()

        for (model in listOfJobFArea) {
            functionalareaList.add(model.name!!.toString())
        }

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, functionalareaList)
//        functionalarea.setAdapter(adapter)
//        functionalarea.setThreshold(1)
//        functionalarea.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        addJobIndustry()

    }

    fun addJobIndustry(){

        listOfJobIndustry.clear()
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getIndustry(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)
        call.enqueue(object : Callback<GetMobilityResponse> {

            override fun onResponse(call: Call<GetMobilityResponse>, response: Response<GetMobilityResponse>) {

                Log.d("TAG","CODE"+ response.code().toString() + "")
                Log.d("TAG","MESSAGE"+ response.message() + "")
                Log.d("TAG","RESPONSE"+ "" + Gson().toJson(response))
                Log.d("TAG","URL"+ "" + response)
                //var str_response = Gson().toJson(response)
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                    val responseCode: Int = jsonObject1.getInt("response_code")
                    val message: String = jsonObject1.getString("message")
                    var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                    if (response.isSuccessful) {
                        for (i in 0 until response.body()!!.body!!.size) {
                            var json_objectdetail: JSONObject = jsonarray.getJSONObject(i)
                            val model: FunctionalAreaView = FunctionalAreaView();
                            //model.id = json_objectdetail.getInt("id")
                            model.name = json_objectdetail.getString("name")
                            listOfJobIndustry.add(model)
                        }
                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid")
                    }

                }
                else
                    Toast.makeText(applicationContext,"No data exists",Toast.LENGTH_LONG).show()
            }
            override fun onFailure(call: Call<GetMobilityResponse>, t: Throwable) {
                Logger.d("TAG", "FAILED : $t")
            }
        })

        Collections.sort(listOfJobIndustry, object : Comparator<FunctionalAreaView> {
            override fun compare(lhs: FunctionalAreaView, rhs: FunctionalAreaView): Int {
                return lhs.name!!.compareTo(rhs.name!!)
            }
        })

        val industryList: ArrayList<String> = ArrayList()

        for (model in listOfJobIndustry) {
            industryList.add(model.name!!.toString())
        }

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, industryList)
//        industry.setAdapter(adapter)
//        industry.setThreshold(1)
//        industry.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())


        val cityList: ArrayList<String> = ArrayList()

        for (model in listOfCities) {
            cityList.add(model.name!!.toString())
        }

        val adapter1 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cityList)
//        city.setAdapter(adapter1)
//        city.setThreshold(1)
//        city.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        //openBottomSheetFilter()

        val numbers = ArrayList<String>()
        for(i in 0..45)
            numbers.add(i.toString())
        val adapter2 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, numbers)
//        experience.setAdapter(adapter2)
//        experience.setThreshold(1)
//        experience.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        Handler().postDelayed({
            checkPref()
        }, 1000)


    }

    public fun openBottomSheetPreferences(id:Int) {

        val dialog = Dialog(this)
//        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog .setCancelable(false)
        dialog .setContentView(R.layout.bottom_sheet_preferences_homepage)
        val pref_keyword= dialog.findViewById(R.id.pref_keyword) as EditText
        val pref_location_multiAutoCompleteTextView = dialog.findViewById(R.id.pref_location_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val locationList: ArrayList<String> = ArrayList()
        for (model in listOfCities) {
            locationList.add(model.name!!.toString())
        }
        val locationArray = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        pref_location_multiAutoCompleteTextView.setAdapter(locationArray)
        pref_location_multiAutoCompleteTextView.setThreshold(1)
        pref_location_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        pref_location_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                pref_location_multiAutoCompleteTextView.showDropDown()
                return false
            }
        })

        val jobtype_multiAutoCompleteTextView = dialog.findViewById(R.id.jobtype_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val jobTypeList: ArrayList<String> = ArrayList()
        for (model in listOfJobType) {
            jobTypeList.add(model.name!!.toString())
        }
        val randomArray = ArrayAdapter(this, android.R.layout.simple_list_item_1, jobTypeList)
        jobtype_multiAutoCompleteTextView.setAdapter(randomArray)
        jobtype_multiAutoCompleteTextView.setThreshold(1)
        jobtype_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        jobtype_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                jobtype_multiAutoCompleteTextView.showDropDown()
                return false
            }
        })


        val farea_multiAutoCompleteTextView = dialog.findViewById(R.id.farea_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val functionalareaList: ArrayList<String> = ArrayList()
        Log.d("TAGGLIST"," "+listOfJobFArea.size)
        for (model in listOfJobFArea) {
            functionalareaList.add(model.name!!.toString())
        }
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, functionalareaList)
        farea_multiAutoCompleteTextView.setAdapter(adapter)
        farea_multiAutoCompleteTextView.setThreshold(1)
        farea_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        farea_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                farea_multiAutoCompleteTextView.showDropDown()
                return false
            }
        })

        val industry_multiAutoCompleteTextView = dialog.findViewById(R.id.industry_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val industryList: ArrayList<String> = ArrayList()
        for (model in listOfJobIndustry) {
            industryList.add(model.name!!.toString())
        }
        val experienceArray = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, industryList)
        industry_multiAutoCompleteTextView.setAdapter(experienceArray)
        industry_multiAutoCompleteTextView.setThreshold(1)
        industry_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        industry_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                industry_multiAutoCompleteTextView.showDropDown()
                return false
            }
        })

        val experience_multiAutoCompleteTextView = dialog.findViewById(R.id.experience_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val experienceList: ArrayList<String> = ArrayList()
        for(i in 0..50)
            experienceList.add(i.toString())
        val adapter1 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, experienceList)
        experience_multiAutoCompleteTextView.setAdapter(adapter1)
        experience_multiAutoCompleteTextView.setThreshold(1)
        experience_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        experience_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                experience_multiAutoCompleteTextView.showDropDown()
                return false
            }
        })

        val experienceto_multiAutoCompleteTextView = dialog.findViewById(R.id.experienceto_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        experienceto_multiAutoCompleteTextView.setAdapter(adapter1)
        experienceto_multiAutoCompleteTextView.setThreshold(1)
        experienceto_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        experienceto_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                experienceto_multiAutoCompleteTextView.showDropDown()
                return false
            }
        })

        val ctc_multiAutoCompleteTextView = dialog.findViewById(R.id.ctc_multiAutoCompleteTextView) as Spinner
        val ctcList: ArrayList<String> = ArrayList()
        for(i in 0..30)
            ctcList.add(i.toString())
        val adapter2 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ctcList)
        ctc_multiAutoCompleteTextView.setAdapter(adapter2)

        val ctc1_multiAutoCompleteTextView = dialog.findViewById(R.id.ctc1_multiAutoCompleteTextView) as Spinner
        val ctcList1: ArrayList<String> = ArrayList()
        for(i in 0..30)
            ctcList1.add(i.toString())
        val adapter3 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ctcList1)
        ctc1_multiAutoCompleteTextView.setAdapter(adapter3)

        val restitle = dialog.findViewById(R.id.resumeTitle) as EditText
        val upload_doc = dialog.findViewById(R.id.upload_doc) as RelativeLayout
        upload_doc.setOnClickListener {
            showPictureDialog()

        }

        val crossbutton_pref = dialog.findViewById(R.id.closetext) as TextView
//        crossbutton_pref.setOnClickListener {
//            //dialog.cancel()
//        }

        val cancel_pref = dialog.findViewById(R.id.cancel_pref) as Button
        cancel_pref.setOnClickListener {
            dialog.cancel()
            openScreen()
        }
        val save_pref = dialog.findViewById(R.id.save_pref) as Button
        save_pref.setOnClickListener {
            if (type.equals("post")) {
                if (pref_location_multiAutoCompleteTextView.text.trim().isNullOrEmpty()) {
                    Toast.makeText(this, "Please select Preferred Location", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                if (jobtype_multiAutoCompleteTextView.text.trim().isNullOrEmpty()) {
                    Toast.makeText(this, "Please select Job Type", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (farea_multiAutoCompleteTextView.text.trim().isNullOrEmpty()) {
                    Toast.makeText(this, "Please enter your functional area", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                if (industry_multiAutoCompleteTextView.text.trim().isNullOrEmpty()) {
                    Toast.makeText(
                        this,
                        "Please enter your preferred Industries",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                if (experience_multiAutoCompleteTextView.text.trim().isNullOrEmpty()) {
                    Toast.makeText(this, "Please enter from experiance", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (experienceto_multiAutoCompleteTextView.text.trim().isNullOrEmpty()) {
                    Toast.makeText(this, "Please enter to experiance", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (pref_keyword.text.trim().isNullOrEmpty()) {
                    Toast.makeText(this, "Please enter your skills", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (picturepath_life!!.length > 0) {
                    if (restitle.getText().length > 0) {
                        if (Integer.parseInt(experience_multiAutoCompleteTextView.text.toString()) <
                            Integer.parseInt(experienceto_multiAutoCompleteTextView.text.toString())
                        ) {
                            uploadResume(restitle.getText().toString())
                            savePreferences(
                                pref_location_multiAutoCompleteTextView.text.trim().toString(),
                                jobtype_multiAutoCompleteTextView.text.trim().toString(),
                                farea_multiAutoCompleteTextView.text.trim().toString(),
                                industry_multiAutoCompleteTextView.text.trim().toString(),
                                pref_keyword.text.trim().toString(),
                                experience_multiAutoCompleteTextView.text.trim().toString(),
                                experienceto_multiAutoCompleteTextView.text.trim().toString()
//                                ctc_multiAutoCompleteTextView.selectedItem.toString(),
//                                ctc1_multiAutoCompleteTextView.selectedItem.toString()
                            )
                            dialog.cancel()
                            openScreen()
                            }
                            else
                                Toast.makeText(applicationContext,"From experience should be less then To experience",Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(applicationContext, "Please enter resume title", Toast.LENGTH_LONG).show()
                        }
                    } else
                        Toast.makeText(applicationContext, "Please upload a resume", Toast.LENGTH_LONG).show()


            } else {
                if (pref_location_multiAutoCompleteTextView.text.trim().isNullOrEmpty()) {
                    Toast.makeText(this, "Please select Preferred Location", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                if (jobtype_multiAutoCompleteTextView.text.trim().isNullOrEmpty()) {
                    Toast.makeText(this, "Please select Job Type", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (farea_multiAutoCompleteTextView.text.trim().isNullOrEmpty()) {
                    Toast.makeText(this, "Please enter your functional area", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                if (industry_multiAutoCompleteTextView.text.trim().isNullOrEmpty()) {
                    Toast.makeText(
                        this,
                        "Please enter your preferred Industries",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                if (experience_multiAutoCompleteTextView.text.trim().isNullOrEmpty()) {
                    Toast.makeText(this, "Please enter from experiance", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (experienceto_multiAutoCompleteTextView.text.trim().isNullOrEmpty()) {
                    Toast.makeText(this, "Please enter to experiance", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (pref_keyword.text.trim().isNullOrEmpty()) {
                    Toast.makeText(this, "Please enter your skills", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (picturepath_life!!.length > 0) {
                    if (restitle.getText().length > 0) {
                        uploadResume(restitle.getText().toString())
                        updatePreferences(
                            pref_location_multiAutoCompleteTextView.text.trim().toString(),
                            jobtype_multiAutoCompleteTextView.text.trim().toString(),
                            farea_multiAutoCompleteTextView.text.trim().toString(),
                            industry_multiAutoCompleteTextView.text.trim().toString(),
                            pref_keyword.text.trim().toString(),
                            experience_multiAutoCompleteTextView.text.trim().toString(),
                            experienceto_multiAutoCompleteTextView.text.trim().toString()
//                                ctc_multiAutoCompleteTextView.selectedItem.toString(),
//                                ctc1_multiAutoCompleteTextView.selectedItem.toString()
                        )
                        dialog.cancel()
                        openScreen()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Please enter resume title",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else
                    Toast.makeText(applicationContext, "Please upload a resume", Toast.LENGTH_LONG)
                        .show()


            }
        }

        if(id>0){
            var i:Int =0
            if(listOfPreferencedata[i].job_type.equals("null")||listOfPreferencedata[i].job_type.equals(""))
                jobtype_multiAutoCompleteTextView.setText("Not specified")
            else
                jobtype_multiAutoCompleteTextView.setText(listOfPreferencedata[i].job_type+",")
            if(listOfPreferencedata[i].functional_area.equals("null")||listOfPreferencedata[i].functional_area.equals(""))
                farea_multiAutoCompleteTextView.setText("Not specified")
            else
                farea_multiAutoCompleteTextView.setText(listOfPreferencedata[i].functional_area+",")
            if(listOfPreferencedata[i].industry.equals("null")||listOfPreferencedata[i].industry.equals(""))
                industry_multiAutoCompleteTextView.setText("Not specified")
            else
                industry_multiAutoCompleteTextView.setText(listOfPreferencedata[i].industry+",")

            if(listOfPreferencedata[i].experience_min_year==0||listOfPreferencedata[i].experience_min_year==0)
                experience_multiAutoCompleteTextView.setText("Not specified")
            else
                experience_multiAutoCompleteTextView.setText(listOfPreferencedata[i].experience_min_year.toString())
            if(listOfPreferencedata[i].experience_max_year==0||listOfPreferencedata[i].experience_max_year==0)
                experienceto_multiAutoCompleteTextView.setText("Not specified")
            else
                experienceto_multiAutoCompleteTextView.setText(listOfPreferencedata[i].experience_max_year.toString())
            if(listOfPreferencedata[i].skills.equals("null")||listOfPreferencedata[i].skills.equals(""))
                pref_keyword.setText("Not specified")
            else
                pref_keyword.setText(listOfPreferencedata[i].skills+",")
            pref_location_multiAutoCompleteTextView.setText(listOfPreferencedata[i].city_name+",")


        }



        var window : Window = dialog.getWindow()!!;
        window.setBackgroundDrawable(null);
        var wlp: WindowManager.LayoutParams = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog .show()
    }

    fun checkPref(){

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.CheckPreferences("application/json",EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<CheckPreferenceResponse> {
            override fun onResponse(call: Call<CheckPreferenceResponse>, response: Response<CheckPreferenceResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                val jsonObject1 : JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                val jsonarray_info: JSONArray = jsonObject1.getJSONArray("body")
                if(response.body()!!.responseCode == 11405)
                    type = "post"
                if (response.isSuccessful ) {
                    listOfPreferencedata.add(
                        PreferenceModel(
                            jsonarray_info.getJSONObject(0).getString("preferred_industry"),
                            jsonarray_info.getJSONObject(0).getInt("exp_from_year"),
                            jsonarray_info.getJSONObject(0).getString("skills"),
                            "",
                            jsonarray_info.getJSONObject(0).getInt("id"),
                            jsonarray_info.getJSONObject(0).getString("preferred_city"),
                            jsonarray_info.getJSONObject(0).getInt("exp_to_year"),
                            jsonarray_info.getJSONObject(0).getString("preferred_functional_area"),
                            jsonarray_info.getJSONObject(0).getString("preferred_job_type"),
                            "",
                            jsonarray_info.getJSONObject(0).getInt("id")
                        )
                    )
                    if(jsonarray_info.getJSONObject(0).isNull("preferred_industry")||
                        jsonarray_info.getJSONObject(0).isNull("exp_to_year")   ||
                        jsonarray_info.getJSONObject(0).isNull("skills") ||
                        jsonarray_info.getJSONObject(0).isNull("preferred_city") ||
                        jsonarray_info.getJSONObject(0).isNull("exp_from_year") ||
                        jsonarray_info.getJSONObject(0).isNull("preferred_functional_area") ||
                        jsonarray_info.getJSONObject(0).isNull("preferred_job_type") ||
                        jsonarray_info.getJSONObject(0).getString("preferred_industry").equals("")||
                        jsonarray_info.getJSONObject(0).getInt("exp_to_year")==0   ||
                        jsonarray_info.getJSONObject(0).getString("skills").equals("") ||
                        jsonarray_info.getJSONObject(0).getString("preferred_city").equals("") ||
                        jsonarray_info.getJSONObject(0).getInt("exp_from_year")==0 ||
                        jsonarray_info.getJSONObject(0).getString("preferred_functional_area").equals("") ||
                        jsonarray_info.getJSONObject(0).getString("preferred_job_type").equals("") )
                        openBottomSheetPreferences(listOfPreferencedata[0].id!!)
                    else
                        openScreen()

                } else {
                    openScreen()
                }
            }

            override fun onFailure(call: Call<CheckPreferenceResponse>, t: Throwable) {
                Logger.d("TAGG", "Apply Job FAILED : $t")
                type = "post"
                openBottomSheetPreferences(0)
//                val homeIntent = Intent(applicationContext, HomePage::class.java)
//                startActivity(homeIntent)
//                finish()
            }
        })
    }

    private fun showPictureDialog() {

        val mimeTypes = arrayOf(
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
            "application/pdf"
        )
        val chooseFile: Intent
        var intent: Intent = Intent()
        chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE)
        chooseFile.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        chooseFile.type = if (mimeTypes.size === 1) mimeTypes[0] else "*/*"
        if (mimeTypes.size > 0) {
            chooseFile.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        }
        //chooseFile.type = ("application/pdf|application/msword")
        intent = Intent.createChooser(chooseFile, "Choose a file")
        startActivityForResult(intent, GALLERY_PDF)
    }

    fun openScreen(){

        layout1.visibility= View.VISIBLE
        val myThread = object : Thread() {
            override fun run() {
                try {
                    Thread.sleep(1800)
//                    intent = Intent(applicationContext, NewsFeed::class.java)
                    intent = Intent(applicationContext, NewsFeedActivity::class.java)
                    startActivity(intent)
                    finish()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        }
        myThread.start()
    }

    fun savePreferences(pref_location: String, jobtype: String, farea:String, industry: String, pref_keyword:String,experience:String,
                        maxexperience:String /*,ctc:String, ctc1:String */){

        val params = HashMap<String, String>()

        if (pref_location.equals("")){}
        else{
            params["preferred_city"] = pref_location.toString()
        }
        if (jobtype.equals("")){}
        else{
            params["preferred_job_type"] = jobtype.substring(0,jobtype.length-1)
        }
        if (farea.equals("")){}
        else{
            params["preferred_functional_area"] = farea.substring(0,farea.length-1)
        }
        if (industry.equals("")){}
        else{
            params["preferred_industry"] = industry.substring(0,industry.length-1)
        }
        if (pref_keyword.equals("")){}
        else{
            params["skills"] = pref_keyword.substring(0,pref_keyword.length-1)
        }
        if (experience.equals("")){}
        else{
            if(experience.contains(",")) {
                params["exp_from_year"] = experience.substring(0, experience.length - 1)
            }
            else{
                params["exp_from_year"] = experience.toString()
            }
        }
        if (maxexperience.equals("")){}
        else{
            if(maxexperience.contains(",")) {
                params["exp_to_year"] = maxexperience.substring(0, experience.length - 1)
            }
            else{
                params["exp_to_year"] = maxexperience.toString()
            }
        }

        Log.d("TAGG", params.toString())

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.PostPreferences(EndPoints.CLIENT_ID, "Bearer "+
                EndPoints.ACCESS_TOKEN,params)

        call.enqueue(object : Callback<UpdateReplyResponse> {
            override fun onResponse(call: Call<UpdateReplyResponse>, response: Response<UpdateReplyResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.e("RESPONSE report group", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                    Logger.e("TAGG", "SAVE PREFERENCES" + response.body()!!.message.toString())
                    openScreen()
                } else {

                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<UpdateReplyResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                openScreen()
            }
        })

    }

    fun updatePreferences(pref_location: String, jobtype: String, farea:String, industry: String, pref_keyword:String,experience:String,
                          maxexperience:String /*,ctc:String, ctc1:String*/){

        val params = HashMap<String, String>()

        if (pref_location.equals("")){}
        else{
            params["preferred_city"] = pref_location.substring(0,pref_location.length-1)
        }
        if (jobtype.equals("")||jobtype.equals("Not specified")){params["preferred_job_type"] = ""}
        else{
            params["preferred_job_type"] = jobtype.substring(0,jobtype.length-1)
        }
        if (farea.equals("")||farea.equals("Not specified")){params["preferred_functional_area"] = ""}
        else{
            params["preferred_functional_area"] = farea.substring(0,farea.length-1)
        }
        if (industry.equals("")||industry.equals("Not specified")){params["preferred_industry"] =""}
        else{
            params["preferred_industry"] = industry.substring(0,industry.length-1)
        }
        if (pref_keyword.equals("")||pref_keyword.equals("Not specified")){params["skills"] =""}
        else{
            params["skills"] = pref_keyword.substring(0,pref_keyword.length-1)
        }

        if (experience.equals("")||experience.equals("Not specified")){params["exp_from_year"] = ""
            params["exp_to_year"] = ""}
        else{
            if(experience.contains(",")) {
                params["exp_from_year"] = experience.substring(0, experience.length - 1)
            }
            else{
                params["exp_from_year"] = experience.toString()
            }
        }
        if (maxexperience.equals("")||experience.equals("Not specified")){params["exp_from_year"] = ""
            params["exp_to_year"] = ""}
        else{
            if(maxexperience.contains(",")) {
                params["exp_to_year"] = maxexperience.substring(0, experience.length - 1)
            }
            else{
                params["exp_to_year"] = maxexperience.toString()
            }
        }

        Log.d("TAGG", params.toString())
        val s:Int = userID
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.updatePreferences(s,EndPoints.CLIENT_ID, "Bearer "+
                EndPoints.ACCESS_TOKEN,params)

        call.enqueue(object : Callback<UpdateReplyResponse> {
            override fun onResponse(call: Call<UpdateReplyResponse>, response: Response<UpdateReplyResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.e("RESPONSE report group", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                    Logger.e("TAGG", "SAVE PREFERENCES" + response.body()!!.message.toString())
                    openScreen()
                } else {

                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<UpdateReplyResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                openScreen()
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

                    //  userId = jsonarray_info.getInt("id")

                } else {
                    ToastHelper.makeToast(applicationContext, message)
                }
            }

            override fun onFailure(call: Call<AccountSettingsDetails>, t: Throwable) {
                Logger.d("TAGG", "FAILED AccNT Settings : $t")
            }
        })

        loadCategoryData()
    }


    fun uploadResume(title: String){

        val params = HashMap<String, String>()

        params["title"] = title
        params["resume_filename"] = picturepath_life.toString()
        params["resume_file"] = imageEncoded_life.toString()

        Logger.d("TAGG", "PARAMS: "+params)
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.SendResume(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        Logger.d("URL", "" + params)
        call.enqueue(object : Callback<UpdateReplyResponse> {
            override fun onResponse(call: Call<UpdateReplyResponse>, response: Response<UpdateReplyResponse>) {
                Logger.d("URL", "" + response+ EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
                var str_response = Gson().toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                val jsonObject1 : JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                var jsonarray: JSONObject = jsonObject1.getJSONObject("body")

                if(response.isSuccessful){
                    Toast.makeText(applicationContext,"Upload Success!!", Toast.LENGTH_LONG).show()
                    val resumeId  = jsonarray.getInt("id")

                    //saveJob(jobId,"note",resumeId, "Applied")
                }
                else {
                    ToastHelper.makeToast(applicationContext, "message")
                }
            }
            override fun onFailure(call: Call<UpdateReplyResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext,"Adding Failed!!", Toast.LENGTH_LONG).show()

            }
        })
    }

    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?)  {

        if (requestCode == GALLERY_PDF) {

            if (data != null) {
                try {

                    val fileName = HelperMethods.getFilePath(this, data.data)

                    val fileString = HelperMethods.testUriFile(this, fileName!!, data.data)

                    val  file = data?.data?.let {
                        HelperMethods.getFile(
                            this@HomePagePreferences,
                            it, fileName
                        )
                    }


                    if (file != null) {
                        picturepath_life = fileName
                        imageEncoded_life = HelperMethods.convertToBase64(file!!)
                        Toast.makeText(applicationContext,"Upload Success", Toast.LENGTH_LONG).show()
                    } else {
                        ToastHelper.makeToast(this, "Unable to convert as Base64")
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.d("TAGG", "STACK" + e.printStackTrace())
                    Toast.makeText(applicationContext, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
