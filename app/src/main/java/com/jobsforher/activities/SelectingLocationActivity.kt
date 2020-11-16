package com.jobsforher.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_selecting_location.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.jobsforher.R
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.models.Cities
import com.jobsforher.network.responsemodels.CitiesListResponse
import com.jobsforher.network.responsemodels.UpdateLocation
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import women.jobs.jobsforher.activities.BaseActivity
import java.util.HashMap

class SelectingLocationActivity : BaseActivity() {

    private val TAG = javaClass.simpleName

    var type : String = ""
    var selectedId : Int = 0
    var selectedCity : String = ""
    internal var textlength = 0

    private var retrofitInterface: RetrofitInterface? = null

    private var doubleBackToExitPressedOnce = false

    var listOfCities: ArrayList<Cities> = ArrayList()
    var listOfTopMetropolitan: ArrayList<Cities> = ArrayList()
    var listOfOtherCities: ArrayList<Cities> = ArrayList()
    var listOfOtherCountryCities: ArrayList<Cities> = ArrayList()
    var listOfOtherCountries: ArrayList<Cities> = ArrayList()

    var listOfTopMetropolitanCompare: ArrayList<Cities> = ArrayList()
    var listOfOtherCitiesCompare: ArrayList<Cities> = ArrayList()
    var listOfOtherCountryCitiesCompare: ArrayList<Cities> = ArrayList()
    var listOfOtherCountriesCompare: ArrayList<Cities> = ArrayList()

    private var awesomeValidation_loc: AwesomeValidation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selecting_location)

        backButton.setOnClickListener {

            finish()

        }

        getCities()

        awesomeValidation_loc = AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation_loc!!.addValidation(etLocation, RegexTemplate.NOT_EMPTY,"Please enter the location")

        type = intent.getStringExtra("type")

        if (type.equals("starter")){

            llStarter.visibility= View.VISIBLE
            llReStarter.visibility= View.GONE
            llRiser.visibility= View.GONE

        } else if(type.equals("restarter")){

            llStarter.visibility= View.GONE
            llReStarter.visibility= View.VISIBLE
            llRiser.visibility= View.GONE

        } else {

            llStarter.visibility= View.GONE
            llReStarter.visibility= View.GONE
            llRiser.visibility= View.VISIBLE

        }

        etLocation.setOnClickListener {

            awesomeValidation_loc!!.clear()
            showLocationDialog()

        }

        btnNext.setOnClickListener {

            if (awesomeValidation_loc!!.validate()){

                updateLocationData(type)

            } else {

                //ToastHelper.makeToast(applicationContext, "Please Select Location")


            }
        }

    }

    fun updateLocationData(type : String){

        val params = HashMap<String, String>()

        val userType = type
        val cityId = selectedId
        val cityName = selectedCity

        params["stage_type"] = userType
        params["location_id"] = cityId.toString()
        params["location"] = cityName

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.UpdateLocation(EndPoints.CLIENT_ID, "Bearer "+
                EndPoints.ACCESS_TOKEN, params)

        call.enqueue(object : Callback<UpdateLocation> {
            override fun onResponse(call: Call<UpdateLocation>, response: Response<UpdateLocation>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    if (response.equals("null")){

                        ToastHelper.makeToast(applicationContext, "Invalid Request")

                    }else {

                        if (response.body()!!.message.toString().equals("User updated")){


                            if (type.equals("starter")){

                                val intent = Intent(applicationContext, EducationActivity::class.java)
                                intent.putExtra("location",selectedCity)
                                intent.putExtra("type",type)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)

                            } else if (type.equals("restarter")){

                                val intent = Intent(applicationContext, RestarterActivity::class.java)
                                intent.putExtra("location",selectedCity)
                                intent.putExtra("type",type)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)

                            }else if (type.equals("riser")){

                                val intent = Intent(applicationContext, WorkExperienceActivity::class.java)
                                intent.putExtra("location",selectedCity)
                                intent.putExtra("type",type)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)

                            }

                        } else {

                            ToastHelper.makeToast(applicationContext, "User profile url already Updated")

                        }

                    }

                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<UpdateLocation>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })

    }

    fun showLocationDialog() {

        val dialog = Dialog(this@SelectingLocationActivity, R.style.AppTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.activity_locations)
        dialog.show()


        var expandableAdapter: com.jobsforher.adapters.CitiesExpandableAdapter1
        val parents = arrayOf("Top Metropolitan Cities", "Other Cities", "Other Country Cities","Other Countries")
        val childList: ArrayList<ArrayList<Cities>>

//        val recyclerView = dialog.findViewById(R.id.locationRecyclerView) as RecyclerView
        val closeDialog = dialog.findViewById(R.id.closeDialog) as ImageView
        val tvSave = dialog.findViewById(R.id.tvSave) as TextView
        val etAlertLocation = dialog.findViewById(R.id.etAlertLocation) as EditText
        val etsearch:EditText = dialog.findViewById(R.id.etAlertLocation) as EditText

        val expList = dialog.findViewById(R.id.locationRecyclerView)as ExpandableListView

        var mAdapterCities: RecyclerView.Adapter<*>? = null

        childList = ArrayList()

        childList.add(listOfTopMetropolitan)
        childList.add(listOfOtherCities)
        childList.add(listOfOtherCountryCities)
        childList.add(listOfOtherCountries)
//        val mLayoutManager = LinearLayoutManager(applicationContext)
//        recyclerView!!.layoutManager = mLayoutManager
//        mAdapterCities = CitiesAdapter(listOfCities, "LocationAct")
//        recyclerView!!.adapter = mAdapterCities

        val listDataHeader = ArrayList<String>()
        listDataHeader.add("Top Metropolitan Cities" )
        listDataHeader.add("Other Cities")
        listDataHeader.add("Other Country Cities")
        listDataHeader.add("Other Countries")

        val listDataChild = HashMap<String, List<Cities>>()
        listDataChild.put(listDataHeader.get(0), listOfTopMetropolitan); // Header, Child data
        listDataChild.put(listDataHeader.get(1), listOfOtherCities);
        listDataChild.put(listDataHeader.get(2), listOfOtherCountryCities);
        listDataChild.put(listDataHeader.get(3),listOfOtherCountries)

//        expandableAdapter = CitiesExpandableAdapter1(this, childList, parents)
        expandableAdapter =
            com.jobsforher.adapters.CitiesExpandableAdapter1(this, listDataHeader, listDataChild)
        expList.setAdapter(expandableAdapter)
        val listDataHeader1 = ArrayList<String>()
        val listDataChild1 = HashMap<String, List<Cities>>()
        expList.expandGroup(0)

        expList.setOnChildClickListener(object: ExpandableListView.OnChildClickListener {

            override fun onChildClick(parent:ExpandableListView, v:View,
                                      groupPosition:Int, childPosition:Int, id:Long):Boolean {

                if (etAlertLocation.text.length>0) {
                    val cityy: String =
                        listDataChild1.get(listDataHeader1.get(groupPosition))!!.get(childPosition).name!!
                    val id: Int = listDataChild1.get(listDataHeader1.get(groupPosition))!!.get(childPosition).id
                    etAlertLocation.setText("")
                    etAlertLocation.setText(cityy)
//
//                    Toast.makeText(
//                        applicationContext,
//                        listDataHeader.get(groupPosition)
//                                + " : "
//                                + cityy, Toast.LENGTH_SHORT
//                    )
//                        .show()

                    SelectedCity(
                        id,
                        cityy
                    )
                }
                else{
                    val cityy: String =
                        listDataChild.get(listDataHeader.get(groupPosition))!!.get(childPosition).name!!
                    val id: Int = listDataChild.get(listDataHeader.get(groupPosition))!!.get(childPosition).id
                    etAlertLocation.setText("")
                    etAlertLocation.setText(cityy)

//                    Toast.makeText(
//                        applicationContext,
//                        listDataHeader.get(groupPosition)
//                                + " : "
//                                + cityy, Toast.LENGTH_SHORT
//                    )
//                        .show()

                    SelectedCity(
                        id,
                        cityy
                    )
                }

                return false
            }
        })

        etsearch!!.addTextChangedListener(object : TextWatcher {


            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                textlength = etsearch!!.text.length
                listOfTopMetropolitanCompare.clear()
                listOfOtherCitiesCompare.clear()
                listOfOtherCountryCitiesCompare.clear()
                listOfOtherCountriesCompare.clear()
                for (i in listOfTopMetropolitan.indices) {
                    if (textlength <= listOfTopMetropolitan[i].name!!.length) {
                        Log.d("ertyyy", listOfTopMetropolitan[i].name!!.toLowerCase().trim())
                        if (listOfTopMetropolitan[i].name!!.toLowerCase().trim().contains(
                                etsearch!!.text.toString().toLowerCase().trim { it <= ' ' })
                        ) {
                            listOfTopMetropolitanCompare.add(listOfTopMetropolitan[i])
                        }
                    }
                }
                for (i in listOfOtherCities.indices) {
                    if (textlength <= listOfOtherCities[i].name!!.length) {
                        Log.d("ertyyy", listOfOtherCities[i].name!!.toLowerCase().trim())
                        if (listOfOtherCities[i].name!!.toLowerCase().trim().contains(
                                etsearch!!.text.toString().toLowerCase().trim { it <= ' ' })
                        ) {
                            listOfOtherCitiesCompare.add(listOfOtherCities[i])
                        }
                    }
                }
                for (i in listOfOtherCountryCities.indices) {
                    if (textlength <= listOfOtherCountryCities[i].name!!.length) {
                        Log.d("ertyyy", listOfOtherCountryCities[i].name!!.toLowerCase().trim())
                        if (listOfOtherCountryCities[i].name!!.toLowerCase().trim().contains(
                                etsearch!!.text.toString().toLowerCase().trim { it <= ' ' })
                        ) {
                            listOfOtherCountryCitiesCompare.add(listOfOtherCountryCities[i])
                        }
                    }
                }
                for (i in listOfOtherCountries.indices) {
                    if (textlength <= listOfOtherCountries[i].name!!.length) {
                        Log.d("ertyyy", listOfOtherCountries[i].name!!.toLowerCase().trim())
                        if (listOfOtherCountries[i].name!!.toLowerCase().trim().contains(
                                etsearch!!.text.toString().toLowerCase().trim { it <= ' ' })
                        ) {
                            listOfOtherCountriesCompare.add(listOfOtherCountries[i])
                        }
                    }
                }
                listDataHeader1.clear()
                listDataChild1.clear()
                listDataHeader1.add("Top Metropolitan Cities" )
                listDataHeader1.add("Other Cities")
                listDataHeader1.add("Other Country Cities")
                listDataHeader1.add("Other Countries")


                listDataChild1.put(listDataHeader1.get(0), listOfTopMetropolitanCompare); // Header, Child data
                listDataChild1.put(listDataHeader1.get(1), listOfOtherCitiesCompare);
                listDataChild1.put(listDataHeader1.get(2), listOfOtherCountryCitiesCompare);
                listDataChild1.put(listDataHeader1.get(3),listOfOtherCountriesCompare)

                expandableAdapter = com.jobsforher.adapters.CitiesExpandableAdapter1(
                    applicationContext,
                    listDataHeader1,
                    listDataChild1
                )
                expList.setAdapter(expandableAdapter)
                expList.expandGroup(0)
                expList.expandGroup(1)
                expList.expandGroup(2)
                expList.expandGroup(3)
            }
        })

        closeDialog.setOnClickListener {

            dialog.dismiss()
        }

        tvSave.setOnClickListener {

            if (etAlertLocation.length()>0){

                val editDegree = etAlertLocation.text

                selectedCity = editDegree.toString()

                etLocation?.setText(selectedCity)

            }
            dialog.dismiss()

        }

    }

    fun optString(json: JSONObject, key: String): String? {
        // http://code.google.com/p/android/issues/detail?id=13830
        return if (json.isNull(key))
            null
        else
            json.optString(key, null)
    }

    public fun SelectedCity(id : Int, city : String){

        etLocation?.setText(city)

        selectedCity = city

        selectedId = id

    }

    fun getCities(){

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.GetCities(EndPoints.CLIENT_ID, "Bearer " + EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<CitiesListResponse> {
            override fun onResponse(call: Call<CitiesListResponse>, response: Response<CitiesListResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    EndPoints.ACCESS_TOKEN = response.body()!!.auth!!.accessToken.toString()

                    val gson = GsonBuilder().serializeNulls().create()
                    var str_response = gson.toJson(response)

                    val jsonObject: JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1))

                    val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                    val jsonarray_info: JSONArray = jsonObject1.getJSONArray("body")

                    for (i in 0 until response.body()!!.body!!.size) {

                        val json_objectdetail: JSONObject = jsonarray_info.getJSONObject(i)

                        val model: Cities = Cities();

                        model.id = json_objectdetail.getInt("id")
                        model.name = json_objectdetail.getString("label")
                        if (json_objectdetail.getInt("ordinal")==0){
                            listOfTopMetropolitan.add(
                                Cities(model.id, model.name!!)
                            )
                        }
                        else if (json_objectdetail.getInt("ordinal")==1){
                            listOfOtherCities.add(
                                Cities(model.id, model.name!!)
                            )
                        }
                        else if (json_objectdetail.getInt("ordinal")==2){
                            listOfOtherCountryCities.add(
                                Cities(model.id, model.name!!)
                            )
                        }
                        else if (json_objectdetail.getInt("ordinal")==3){
                            listOfOtherCountries.add(
                                Cities(model.id, model.name!!)
                            )
                        }
//                        listOfCities.add(
//                            Cities(model.id, model.name!!)
//                        )
                    }


                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<CitiesListResponse>, t: Throwable) {

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
