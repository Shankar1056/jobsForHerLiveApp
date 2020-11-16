package com.jobsforher.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jobsforher.R
import com.jobsforher.adapters.CollegesAdapter
import com.jobsforher.adapters.DegreesAdapter
import com.jobsforher.adapters.MonthsAdapter
import com.jobsforher.adapters.YearsAdapter
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.models.Colleges
import com.jobsforher.models.Degrees
import com.jobsforher.models.Months
import com.jobsforher.models.Years
import com.jobsforher.network.responsemodels.CollegesListResponse
import com.jobsforher.network.responsemodels.CreateEducationResponse
import com.jobsforher.network.responsemodels.DegreesListResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import kotlinx.android.synthetic.main.activity_education.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import women.jobs.jobsforher.activities.BaseActivity
import java.util.*
import kotlin.collections.ArrayList

class EducationActivity : BaseActivity() {

    private val TAG = javaClass.simpleName

    var listOfDegrees : ArrayList<Degrees> = ArrayList()

    var listOfColleges : ArrayList<Colleges> = ArrayList()

    var listOfMonths : ArrayList<Months> = ArrayList()
    var listOfYears : ArrayList<Years> = ArrayList()

    var type : String = ""
    var location : String = ""

    var selectedDegreeId : Int = 0
    var selectedDegree : String = ""

    var selectedCollegeId : Int = 0
    var selectedCollege : String = ""

    var selectedFromMonth : String = ""
    var selectedToMonth : String = ""

    var selectedFromYear : String = ""
    var selectedToYear : String = ""

    var seletedFromMonthId : Int = 0
    var selectedToMonthId : Int = 0

    var selectType : String = ""

    private var retrofitInterface: RetrofitInterface? = null

    val collegeDialog: Dialog? = null

    private var doubleBackToExitPressedOnce = false
    private var awesomeValidation_edu: AwesomeValidation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_education)
        getDegrees()

        getColleges()

        getMonthAndYears()

        etDegree.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                etDegree.showDropDown()
                return false
            }
        })
        etCollege.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                etCollege.showDropDown()
                return false
            }
        })

        val degree = arrayOf("PU", "PUC", "BE", "MCA",
            "BSc", "MSc", "BCA", "B.Tech", "M.Tech", "ME",
            "Diploma", "Ph.D", "Others")
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, degree)
        val actv = findViewById<View>(R.id.etDegree) as AutoCompleteTextView
        actv.setThreshold(1)//will start working from first character
        actv.setAdapter(adapter)//setting the adapter data into the AutoCompleteTextView

        val college = arrayOf("SPUC", "The Wharton School", "IIT Delhi", "APS College of Engineering",
            "BMS College", "Madras University", "Bangalore University", "APC College of Engineering", "RV College of Engineering", "Don Bosco College of Engineering",
            "KLE", "Madras University", "Chennai Univesity", "Others")
        val adapterCollege = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, college)
        val actvCollege = findViewById<View>(R.id.etCollege) as AutoCompleteTextView
        actvCollege.setThreshold(1)//will start working from first character
        actvCollege.setAdapter(adapterCollege)//setting the adapter data into the AutoCompleteTextView

        location = intent.getStringExtra("location")
        type = intent.getStringExtra("type")

        if (type.equals("starter")){

            tvSkip.visibility=View.GONE

        }else{

            tvSkip.visibility=View.VISIBLE

        }

        etDegree.setOnClickListener {

            //showDegreeDialog("Degree")
        }

        etCollege.setOnClickListener {

            //showCollegeDialog()
        }

        etFromMonth.setOnClickListener {

            showFromDateDialog("From")

            selectType = "From"
        }

        etFromYear.setOnClickListener {

            showFromDateDialog("From")

            selectType = "From"
        }

        etToMonth.setOnClickListener {

            showFromDateDialog("To")

            selectType = "To"
        }

        etToYear.setOnClickListener {

            showFromDateDialog("To")

            selectType = "To"
        }


        tvSkip.setOnClickListener {

            val intent = Intent(applicationContext, AddProfileActivity::class.java)
            intent.putExtra("type",type)
            intent.putExtra("location",location)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        awesomeValidation_edu = AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation_edu!!.addValidation(etDegree, RegexTemplate.NOT_EMPTY,"Please enter the qualification")
        awesomeValidation_edu!!.addValidation(etCollege, RegexTemplate.NOT_EMPTY,"Please enter school/ college/university name");
        //awesomeValidation_edu!!.addValidation(edittext_whatskillsEdu, RegexTemplate.NOT_EMPTY,"Please enter atleast one skill");

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val date = c.get(Calendar.MONTH)+1

        btnSubmit.setOnClickListener {

            if(awesomeValidation_edu!!.validate()) {
                if (etFromYear.getText().toString().trim().isEmpty() || etFromMonth.getText()
                        .toString().trim().isEmpty()
                ) {
                    ToastHelper.makeToast(applicationContext, "From date can't be blank")
                    return@setOnClickListener
                }
                if (!cb_eduongoing.isChecked) {
                    if (etToYear.text.toString().trim().isEmpty() || etToMonth.text.toString()
                            .trim().isEmpty()
                    ) {
                        ToastHelper.makeToast(applicationContext, "To date can't be blank")
                        return@setOnClickListener
                    }
                }

//                if (etFromMonth.length() > 0 && etFromYear.length() > 0) {
//            if (etCollege.length()>0){
                if (cb_eduongoing.isChecked == false && Integer.parseInt(
                        etFromYear.getText().toString()
                    ) == Integer.parseInt(etToYear.text.toString())
                    && seletedFromMonthId <= selectedToMonthId
                ) {
                    val editDegree = etDegree.text
                    val editCollege = etCollege.text
                    val editFromMonth = etFromMonth.text
                    val editFromYear = etFromYear.text
                    val editToMonth = etToMonth.text
                    val editToYear = etToYear.text

                        submitEducationDetails(
                            editDegree.toString(), editCollege.toString(), editFromMonth.toString(),
                            editFromYear.toString(), editToMonth.toString(), editToYear.toString()
                        )
                    }
                    else if(cb_eduongoing.isChecked==false && Integer.parseInt(etFromYear.getText().toString()) < Integer.parseInt(etToYear.text.toString())){
                        val editDegree = etDegree.text
                        val editCollege = etCollege.text
                        val editFromMonth = etFromMonth.text
                        val editFromYear = etFromYear.text
                        val editToMonth = etToMonth.text
                        val editToYear = etToYear.text

                        submitEducationDetails(
                            editDegree.toString(), editCollege.toString(), editFromMonth.toString(),
                            editFromYear.toString(), editToMonth.toString(), editToYear.toString()
                        )
                    }
                    else if(cb_eduongoing.isChecked==true){
                        if(Integer.parseInt(etFromYear.text.toString())<year) {
                            val editDegree = etDegree.text
                            val editCollege = etCollege.text
                            val editFromMonth = etFromMonth.text
                            val editFromYear = etFromYear.text
                            val editToMonth = etToMonth.text
                            val editToYear = etToYear.text

                            submitEducationDetails(
                                editDegree.toString(), editCollege.toString(), editFromMonth.toString(),
                                editFromYear.toString(), editToMonth.toString(), editToYear.toString()
                            )
                        }
                        else if(seletedFromMonthId<=date && Integer.parseInt(etFromYear.text.toString())==year){
                            val editDegree = etDegree.text
                            val editCollege = etCollege.text
                            val editFromMonth = etFromMonth.text
                            val editFromYear = etFromYear.text
                            val editToMonth = etToMonth.text
                            val editToYear = etToYear.text

                            submitEducationDetails(
                                editDegree.toString(), editCollege.toString(), editFromMonth.toString(),
                                editFromYear.toString(), editToMonth.toString(), editToYear.toString()
                            )
                        }
                        else
                            ToastHelper.makeToast(applicationContext, "From date should be less than Current date")
                    }
                    else
                        ToastHelper.makeToast(
                            applicationContext,
                            "From date should be less than To date"
                        )


            }
            else {  }
        }
    }

    fun submitEducationDetails(degree : String, college : String, fromMonth : String, fromYear : String, toMonth : String,
                               toYear : String){

        val params = HashMap<String, String>()

        val userDegree = degree
        val userCollege = college
        val userStartDate = fromYear+"-"+seletedFromMonthId+"-01"

        var userEndDate = ""

        if (cb_eduongoing.isChecked){

            userEndDate = ""

        }else {

            userEndDate = toYear+"-"+ selectedToMonthId+"-01"

        }

        params["degree"] = userDegree
        params["ongoing"] = cb_eduongoing.isChecked.toString()
        params["college"] = userCollege
        params["start_date"] = userStartDate
        params["end_date"] = userEndDate
        params["location"] = location
        params["image_filename"] = ""
        params["image_file"] = ""
        params["description"] = ""
        params["skills"] = "['Python']"
        params["default_image"] = "1"

        Logger.d("DATA", userDegree)
        Logger.d("DATA", userCollege)
        Logger.d("DATA", userStartDate)
        Logger.d("DATA", userEndDate)
        Logger.d("DATA", location)


        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.CreateEducation(EndPoints.CLIENT_ID, "Bearer "+
                EndPoints.ACCESS_TOKEN, params)

        call.enqueue(object : Callback<CreateEducationResponse> {
            override fun onResponse(call: Call<CreateEducationResponse>, response: Response<CreateEducationResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    EndPoints.ACCESS_TOKEN = response.body()!!.auth!!.accessToken.toString()

                    if (response.body()!!.message.toString().equals("Education Details created.")){

                        if (type.equals("starter")){

                            val intent = Intent(applicationContext, LifeExperienceActivity::class.java)
                            intent.putExtra("type",type)
                            intent.putExtra("location",location)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)

                        }else if (type.equals("restarter")){

                            val intent = Intent(applicationContext, AddProfileActivity::class.java)
                            intent.putExtra("type",type)
                            intent.putExtra("location",location)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)

                        }else if (type.equals("riser")){

                            val intent = Intent(applicationContext, AddProfileActivity::class.java)
                            intent.putExtra("type",type)
                            intent.putExtra("location",location)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)

                        }


                    } else {

                        ToastHelper.makeToast(applicationContext, "User profile url already Updated")

                    }

                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<CreateEducationResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })
    }


    fun getMonthAndYears(){

        listOfMonths.add(Months(1, "January"))
        listOfMonths.add(Months(2, "February"))
        listOfMonths.add(Months(3, "March"))
        listOfMonths.add(Months(4, "April"))
        listOfMonths.add(Months(5, "May"))
        listOfMonths.add(Months(6, "June"))
        listOfMonths.add(Months(7, "July"))
        listOfMonths.add(Months(8, "August"))
        listOfMonths.add(Months(9, "September"))
        listOfMonths.add(Months(10, "October"))
        listOfMonths.add(Months(11, "November"))
        listOfMonths.add(Months(12, "December"))

        val years = ArrayList<String>()
        val thisYear = Calendar.getInstance().get(Calendar.YEAR)
        for (i in 1975..thisYear) {
            listOfYears.add(Years(Integer.toString(i)))
        }
    }

    fun showFromDateDialog(type:String){

        val dialog = Dialog(this@EducationActivity, R.style.AppTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.activity_alert_date)
        dialog.show()

        val monthrecyclerView = dialog.findViewById(R.id.monthRecyclerView) as RecyclerView
        val yearRecyclerView = dialog.findViewById(R.id.yearRecyclerView) as RecyclerView

        val closeDialog = dialog.findViewById(R.id.closeDialog) as ImageView
        val tvSave = dialog.findViewById(R.id.tvSave) as TextView
        val tvTitle = dialog.findViewById(R.id.tvTitle) as TextView

        if (type.equals("From")){

            tvTitle.setText("From Date")
        }else{

            tvTitle.setText("To Date")
        }

        var mAdapterMonths: RecyclerView.Adapter<*>? = null

        val mLayoutManagerMonth = LinearLayoutManager(applicationContext)
        monthrecyclerView!!.layoutManager = mLayoutManagerMonth
        mAdapterMonths = MonthsAdapter(listOfMonths, "Education")
        monthrecyclerView!!.adapter = mAdapterMonths

        var mAdapterYears: RecyclerView.Adapter<*>? = null

        val mLayoutManagerYears = LinearLayoutManager(applicationContext)
        yearRecyclerView!!.layoutManager = mLayoutManagerYears
        mAdapterYears = YearsAdapter(listOfYears, "Education")
        yearRecyclerView!!.adapter = mAdapterYears

        closeDialog.setOnClickListener {

            dialog.dismiss()
        }

        tvSave.setOnClickListener {

            dialog.dismiss()

        }
    }

    fun onCheckboxEducationClicked(view : View){

        if (cb_eduongoing.isChecked){

            etToMonth.setEnabled(false)

            etToYear.setEnabled(false)

            llToDate.visibility = View.INVISIBLE


        }else{

            llToDate.visibility = View.VISIBLE

            etToMonth.setEnabled(true)

            etToYear.setEnabled(true)

        }
    }

    fun showCollegeDialog(){

        val dialog = Dialog(this@EducationActivity, R.style.AppTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.activity_alert_college)
        dialog.show()

        val recyclerView = dialog.findViewById(R.id.locationRecyclerView) as RecyclerView
        val closeDialog = dialog.findViewById(R.id.closeDialog) as ImageView
        val tvSave = dialog.findViewById(R.id.tvSave) as TextView
        val etAlertCollege = dialog.findViewById(R.id.etAlertCollege) as EditText

        var mAdapterColleges: RecyclerView.Adapter<*>? = null

        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.layoutManager = mLayoutManager
        mAdapterColleges = CollegesAdapter(listOfColleges, "Education")
        recyclerView!!.adapter = mAdapterColleges

        closeDialog.setOnClickListener {

            dialog.dismiss()
        }

        tvSave.setOnClickListener {

            if (etAlertCollege.length()>0){

                val editDegree = etAlertCollege.text

                selectedCollege = editDegree.toString()

                etCollege?.setText(selectedCollege)

            }

            dialog.dismiss()

        }
    }

    fun showDegreeDialog(type:String){

        val dialog = Dialog(this@EducationActivity, R.style.AppTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.activity_alert_degree)
        dialog.show()

        val recyclerView = dialog.findViewById(R.id.locationRecyclerView) as RecyclerView
        val closeDialog = dialog.findViewById(R.id.closeDialog) as ImageView
        val tvSave = dialog.findViewById(R.id.tvSave) as TextView
        val etAlertDegree = dialog.findViewById(R.id.etAlertDegree) as EditText

        val tvTitle = dialog.findViewById(R.id.tvTitle) as TextView
        val tvSelectTitle = dialog.findViewById(R.id.tvSelectTitle) as TextView

        var mAdapterDegrees: RecyclerView.Adapter<*>? = null

        if (type.equals("Degree")){

            tvTitle.setText("Degrees")

            tvSelectTitle.setText("Select Degree")
        }

        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.layoutManager = mLayoutManager
        mAdapterDegrees = DegreesAdapter(listOfDegrees, "Education")
        recyclerView!!.adapter = mAdapterDegrees

        closeDialog.setOnClickListener {

            dialog.dismiss()
        }

        tvSave.setOnClickListener {

            if (etAlertDegree.length()>0){

                val editDegree = etAlertDegree.text

                selectedDegree = editDegree.toString()

                etDegree?.setText(selectedDegree)

            }

            dialog.dismiss()

        }

    }

    public fun SelectedDegree(id : Int, degree : String){

        etDegree?.setText(degree)

        selectedDegree = degree

        selectedDegreeId = id

    }

    public fun SelectedCollege(id : Int, college : String){

        etCollege?.setText(college)

        selectedCollege = college

        selectedCollegeId = id

    }

    public fun SelectedMonth(monthId : Int, month : String){

        if (selectType.equals("From")){

            etFromMonth?.setText(month)

            selectedFromMonth = month

            seletedFromMonthId = monthId

        } else {

            etToMonth?.setText(month)

            selectedToMonth = month

            selectedToMonthId = monthId
        }

    }

    public fun SelectedYear(year : String){

        if (selectType.equals("From")){

            etFromYear?.setText(year)

            selectedFromYear = year

        }else {

            etToYear?.setText(year)

            selectedToYear = year
        }

    }

    fun getDegrees(){

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.GetDegrees(EndPoints.CLIENT_ID, "Bearer " + EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<DegreesListResponse> {
            override fun onResponse(call: Call<DegreesListResponse>, response: Response<DegreesListResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    EndPoints.ACCESS_TOKEN = response.body()!!.auth!!.accessToken.toString()

                    val gson = GsonBuilder().serializeNulls().create()
                    val str_response = gson.toJson(response)

                    val jsonObject: JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1))

                    val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                    val jsonarray_info: JSONArray = jsonObject1.getJSONArray("body")

                    Logger.e("RESPONSE array degree", "" + jsonarray_info.toString())

                    for (i in 0 until response.body()!!.body!!.size) {

                        val json_objectdetail: JSONObject = jsonarray_info.getJSONObject(i)

                        Logger.e("RESPONSE degree", "" + jsonarray_info.getJSONObject(i))

                        val model: Degrees = Degrees();

                        model.status = json_objectdetail.getBoolean("status")

                        model.id = json_objectdetail.getInt("id")

                        model.sDegree = json_objectdetail.getString("degree")


                        listOfDegrees.add(Degrees(model.status, model.id, model.sDegree!!))
                    }
                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<DegreesListResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })

    }

    fun getColleges(){

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.GetColleges(EndPoints.CLIENT_ID, "Bearer " + EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<CollegesListResponse> {
            override fun onResponse(call: Call<CollegesListResponse>, response: Response<CollegesListResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    EndPoints.ACCESS_TOKEN = response.body()!!.auth!!.accessToken.toString()

                    val gson = GsonBuilder().serializeNulls().create()
                    val str_response = gson.toJson(response)

                    val jsonObject: JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1))

                    val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                    val jsonarray_info: JSONArray = jsonObject1.getJSONArray("body")

                    for (i in 0 until response.body()!!.body!!.size) {

                        val json_objectdetail: JSONObject = jsonarray_info.getJSONObject(i)

                        val model: Colleges = Colleges();

                        model.status = json_objectdetail.getBoolean("status")

                        model.id = json_objectdetail.getInt("id")

                        model.college = json_objectdetail.getString("college")

                        listOfColleges.add(Colleges(model.status, model.id, model.college!!))
                    }
                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<CollegesListResponse>, t: Throwable) {

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
