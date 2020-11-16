package com.jobsforher.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import kotlinx.android.synthetic.main.activity_restarter.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.jobsforher.R
import com.jobsforher.adapters.LifeExpAdapter
import com.jobsforher.adapters.MonthsAdapter
import com.jobsforher.adapters.SkillsAdapter
import com.jobsforher.adapters.YearsAdapter
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.models.LifeExperiences
import com.jobsforher.models.Months
import com.jobsforher.models.Skills
import com.jobsforher.models.Years
import com.jobsforher.network.responsemodels.CreatelifeExperienceResponse
import com.jobsforher.network.responsemodels.LifeExperienceResponse
import com.jobsforher.network.responsemodels.SkillsResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import women.jobs.jobsforher.activities.BaseActivity
import java.util.*
import kotlin.collections.ArrayList

class RestarterActivity : BaseActivity() {

    private val TAG = javaClass.simpleName

    var type : String = ""

    var location: String = ""
    internal var textlength = 0

    var listOfLifeExperiences: ArrayList<LifeExperiences> = ArrayList()

    private var retrofitInterface: RetrofitInterface? = null

    val listLifeExperience = mutableListOf("")

    var listOfExpMonths: ArrayList<Months> = ArrayList()
    var listOfExpYears: ArrayList<Years> = ArrayList()
    var listOfSkills: ArrayList<Skills> = ArrayList()
    var listOfSkillsCompare: ArrayList<Skills> = ArrayList()

    var selectedFromMonth: String = ""
    var selectedToMonth: String = ""
    var selectedFromYear: String = ""
    var selectedToYear: String = ""
    var selectedLifeExp: String = ""

    var seletedFromMonthId: Int = 0
    var selectedToMonthId: Int = 0
    var selectedLifeExpId: Int = 0

    var selectType: String = ""

    var listOfSelectedSkills: ArrayList<Skills> = ArrayList()

    private var doubleBackToExitPressedOnce = false
    private var awesomeValidation_life: AwesomeValidation? = null

    val listSkillsSelected = mutableListOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restarter)

        type = intent.getStringExtra("type")

        location = intent.getStringExtra("location")

        etReason.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                etReason.showDropDown()
                return false
            }
        })

        getLifeExperiences()

        getMonthAndYears()

        getSkills()

        awesomeValidation_life = AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation_life!!.addValidation(etReason, RegexTemplate.NOT_EMPTY,"Please enter the career break")
        awesomeValidation_life!!.addValidation(etCaption, RegexTemplate.NOT_EMPTY,"Please enter the caption");
        awesomeValidation_life!!.addValidation(etOnBreakSkills, RegexTemplate.NOT_EMPTY,"Please enter the skills");

        val life = arrayOf("Motherhood", "Elderly-Care", "Health", "Travel",
            "Marriage", "Relocation","Others")
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, life)
        val actv = findViewById<View>(R.id.etReason) as AutoCompleteTextView
        actv.setThreshold(1)//will start working from first character
        actv.setAdapter(adapter)//setting the adapter data into the AutoCompleteTextView
        actv.setOnItemClickListener(object : AdapterView.OnItemClickListener {

            override fun onItemClick(parent: AdapterView<*>, arg1: View, position: Int, arg3: Long) {
                if (etReason.text.toString().equals("Motherhood"))
                    etCaption.setText("Boss Mom")
                else if (etReason.text.toString().equals("Elderly-Care"))
                    etCaption.setText("Constant Custodian")
                else if (etReason.text.toString().equals("Health"))
                    etCaption.setText("Return of the Warrior")
                else if (etReason.text.toString().equals("Travel"))
                    etCaption.setText("Adventures")
                else if (etReason.text.toString().equals("Marriage"))
                    etCaption.setText("New Beginnings")
                else if (etReason.text.toString().equals("Relocation"))
                    etCaption.setText("New discoveries")

            }
        })

        btnOnBreakSubmit.setOnClickListener {

            if(awesomeValidation_life!!.validate()) {
                if (etOnBreakFromMonth.length() > 0 && etOnBreakFromYear.length() > 0){

                    if (cb_OnBreakOngoing.isChecked==false && Integer.parseInt(etOnBreakFromYear.getText().toString()) < Integer.parseInt(etOnBreakToYear.text.toString())
                        && seletedFromMonthId<=selectedToMonthId) {
                        val editLifeExp = etReason.text
                        val editCaption = etCaption.text
                        val editFromMonth = etOnBreakFromMonth.text
                        val editFromYear = etOnBreakFromYear.text
                        val editToMonth = etOnBreakToMonth.text
                        val editToYear = etOnBreakToYear.text
                        val editSkill = etOnBreakSkills.text

                        submitOnBreakDetails(
                            editLifeExp.toString(), editCaption.toString(), editFromMonth.toString(),
                            editFromYear.toString(), editToMonth.toString(), editToYear.toString(), editSkill.toString()
                        )
                    }
                    else if(cb_OnBreakOngoing.isChecked==false && Integer.parseInt(etOnBreakFromYear.getText().toString()) < Integer.parseInt(etOnBreakToYear.text.toString())){
                        val editLifeExp = etReason.text
                        val editCaption = etCaption.text
                        val editFromMonth = etOnBreakFromMonth.text
                        val editFromYear = etOnBreakFromYear.text
                        val editToMonth = etOnBreakToMonth.text
                        val editToYear = etOnBreakToYear.text
                        val editSkill = etOnBreakSkills.text

                        submitOnBreakDetails(
                            editLifeExp.toString(), editCaption.toString(), editFromMonth.toString(),
                            editFromYear.toString(), editToMonth.toString(), editToYear.toString(), editSkill.toString()
                        )
                    }
                    else if(cb_OnBreakOngoing.isChecked==true){
                        val editLifeExp = etReason.text
                        val editCaption = etCaption.text
                        val editFromMonth = etOnBreakFromMonth.text
                        val editFromYear = etOnBreakFromYear.text
                        val editToMonth = etOnBreakToMonth.text
                        val editToYear = etOnBreakToYear.text
                        val editSkill = etOnBreakSkills.text

                        submitOnBreakDetails(
                            editLifeExp.toString(), editCaption.toString(), editFromMonth.toString(),
                            editFromYear.toString(), editToMonth.toString(), editToYear.toString(), editSkill.toString()
                        )
                    }
                    else
                        ToastHelper.makeToast(applicationContext, "From date should be less than To date")
                }
                else{
                    ToastHelper.makeToast(applicationContext, "Enter the dates")
                }
            } else {

//                ToastHelper.makeToast(applicationContext, "Enter the skills")

            }

        }

        etReason.setOnClickListener {

            //showLifeExpDialog("LifeExp")
            listLifeExperience.clear()
        }

        etOnBreakFromMonth.setOnClickListener {

            showFromDateDialog("From")

            selectType = "From"

        }

        etOnBreakFromYear.setOnClickListener {

            showFromDateDialog("From")

            selectType = "From"

        }

        etOnBreakToMonth.setOnClickListener {

            showFromDateDialog("To")

            selectType = "To"

        }

        etOnBreakToYear.setOnClickListener {

            showFromDateDialog("To")

            selectType = "To"

        }

        etOnBreakSkills.setOnClickListener {

            awesomeValidation_life!!.clear()
            // showSkillDialog("Skill")

            listSkillsSelected.clear()

        }

    }

    fun onCheckboxOngoingClicked(view: View) {

        if (cb_OnBreakOngoing.isChecked) {

            etOnBreakToMonth.setEnabled(false)

            etOnBreakToYear.setEnabled(false)
            todate.visibility = View.INVISIBLE
//            selectedCheckBox = "true"

        } else {

            etOnBreakToMonth.setEnabled(true)

            etOnBreakToYear.setEnabled(true)
            todate.visibility = View.VISIBLE
//            selectedCheckBox = "false"

        }
    }

    fun submitOnBreakDetails(editLifeExp: String, editCaption: String, fromMonth: String, fromYear: String,
                             toMonth: String, toYear: String, editLifeSkill: String) {

        listLifeExperience.add(editLifeExp.drop(0))

        var skills: String = etOnBreakSkills.text.toString()
        var skillValue: String = ""
        if (skills.contains(",")) {
            var array: List<String> = skills.split(",")
            for (l in 0 until array.size) {
                skillValue = "'" + array[l].toString() + "'"
            }
        }
        else
            skillValue = "'" + skills + "'"


        var exp: String = etReason.text.toString()
        var expValue: String = ""
        if (exp.contains(",")) {
            var array: List<String> = exp.split(",")
            for (l in 0 until array.size) {
                expValue = "'" + array[l].toString() + "'"
            }
        }
        else
            expValue = "'" + exp + "'"


        val params = HashMap<String, String>()

        val userStartDate = fromYear + "-" + seletedFromMonthId + "-01"

        val userCaption = editCaption

        var userEndDate = ""
        var userOnGoing = ""

        if (cb_OnBreakOngoing.isChecked) {

            userEndDate = ""
            userOnGoing = "true"

        } else {
            userEndDate = toYear + "-" + selectedToMonthId + "-01"
            userOnGoing = "false"
        }

        params["life_experience"] = "["+expValue+"]"
        params["caption"] = userCaption
        params["start_date"] = userStartDate
        params["end_date"] = userEndDate
        params["ongoing"] = userOnGoing
        params["location"] = location
        params["description"] = ""
        params["skills"] = "["+skillValue+"]"
        params["default_image"] = "1"

        Logger.d("TAGG", "["+expValue+"]")
        Logger.d("TAGG", userStartDate)
        Logger.d("TAGG", userEndDate)
        Logger.d("TAGG", location)
        Logger.d("TAGG", "["+skillValue+"]")

        Logger.d("TAGG", params.toString())

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.CreateLifeExperience(EndPoints.CLIENT_ID, "Bearer "+
                EndPoints.ACCESS_TOKEN, params)

        call.enqueue(object : Callback<CreatelifeExperienceResponse> {
            override fun onResponse(call: Call<CreatelifeExperienceResponse>, response: Response<CreatelifeExperienceResponse>) {

                Logger.d("CODE life exp", response.code().toString() + "")
                Logger.d("MESSAGE life exp", response.message() + "")
                Logger.d("URL life exp", "" + response)
                Logger.d("RESPONSE life exp", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    EndPoints.ACCESS_TOKEN = response.body()!!.auth!!.accessToken.toString()

                    if (response.body()!!.message.toString().equals("Life Experience Details created.")){

                        val intent = Intent(applicationContext, WorkExperienceActivity::class.java)
                        intent.putExtra("type",type)
                        intent.putExtra("location",location)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                    } else {

                        ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())

                    }

                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<CreatelifeExperienceResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })
    }

    fun showSkillDialog(type:String) {

        //listOfSelectedSkills.clear()

        val dialog = Dialog(this@RestarterActivity, R.style.AppTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.activity_alert_skill)
        dialog.show()

        val recyclerView = dialog.findViewById(R.id.locationRecyclerView) as RecyclerView
        val closeDialog = dialog.findViewById(R.id.closeDialog) as ImageView
        val tvSave = dialog.findViewById(R.id.tvSave) as TextView
        val etsearch:EditText = dialog.findViewById(R.id.editText) as EditText

        val tvTitle = dialog.findViewById(R.id.tvTitle) as TextView
        val tvSelectTitle = dialog.findViewById(R.id.tvSelectTitle) as TextView

        if (type.equals("Skill")){

            tvTitle.setText("Skills")

            tvSelectTitle.setText("Select Skills")
        }

        var mAdapterSkils: RecyclerView.Adapter<*>? = null

        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.layoutManager = mLayoutManager
        mAdapterSkils = SkillsAdapter(listOfSkills, "OnBreak",listOfSelectedSkills)
        recyclerView!!.adapter = mAdapterSkils

        closeDialog.setOnClickListener {

            dialog.dismiss()
        }

        tvSave.setOnClickListener {

            dialog.dismiss()

            var sbSkill: String? = ""

            for (i in 0 until listOfSelectedSkills.size) {

                Logger.d("DATAsbSelectedSkill", listOfSelectedSkills.get(i).name.toString())

                sbSkill = sbSkill+listOfSelectedSkills.get(i).name+","

            }

            if(sbSkill!!.length>0)
                etOnBreakSkills?.setText(sbSkill!!.substring(0,sbSkill!!.length-1))
            else
                etOnBreakSkills?.setText("")

        }

        etsearch!!.addTextChangedListener(object : TextWatcher {


            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                textlength = etsearch!!.text.length
                listOfSkillsCompare.clear()
                for (i in listOfSkills.indices) {
                    if (textlength <= listOfSkills[i].name!!.length) {
                        Log.d("ertyyy", listOfSkills[i].name!!.toLowerCase().trim())
                        if (listOfSkills[i].name!!.toLowerCase().trim().contains(
                                etsearch!!.text.toString().toLowerCase().trim { it <= ' ' })
                        ) {
                            listOfSkillsCompare.add(listOfSkills[i])
                        }
                    }
                }

                mAdapterSkils = SkillsAdapter(listOfSkillsCompare,"OnBreak",listOfSelectedSkills)
                recyclerView!!.adapter = mAdapterSkils
                recyclerView!!.layoutManager =
                    LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

            }
        })
    }

    public fun SelectedSkill(approve: Boolean, id: Int, category_type: String, skill: String) {


        Logger.e("DATAsbSelectedSkill", skill)
        var a: Boolean = true

        for (x in 0 until listOfSelectedSkills.size){
            if (listOfSelectedSkills.get(x).name.equals(skill)) {
                a = false
                listOfSelectedSkills.removeAt(x)
                break
            }
        }

        if(a==true){
            listOfSelectedSkills.add(Skills(skill))
        }

    }

    fun showFromDateDialog(type:String) {

        val dialog = Dialog(this@RestarterActivity, R.style.AppTheme)
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
        mAdapterMonths = MonthsAdapter(listOfExpMonths, "OnBreak")
        monthrecyclerView!!.adapter = mAdapterMonths

        var mAdapterYears: RecyclerView.Adapter<*>? = null

        val mLayoutManagerYears = LinearLayoutManager(applicationContext)
        yearRecyclerView!!.layoutManager = mLayoutManagerYears
        mAdapterYears = YearsAdapter(listOfExpYears, "OnBreak")
        yearRecyclerView!!.adapter = mAdapterYears

        closeDialog.setOnClickListener {

            dialog.dismiss()
        }

        tvSave.setOnClickListener {

            dialog.dismiss()

        }
    }

    public fun SelectedMonth(monthId: Int, month: String) {

        if (selectType.equals("From")) {

            etOnBreakFromMonth?.setText(month)

            selectedFromMonth = month

            seletedFromMonthId = monthId

        } else {

            etOnBreakToMonth?.setText(month)

            selectedToMonth = month

            selectedToMonthId = monthId
        }

    }

    public fun SelectedYear(year: String) {

        if (selectType.equals("From")) {

            etOnBreakFromYear?.setText(year)

            selectedFromYear = year

        } else {

            etOnBreakToYear?.setText(year)

            selectedToYear = year
        }

    }

    fun getMonthAndYears() {

        listOfExpMonths.add(Months(1, "January"))
        listOfExpMonths.add(Months(2, "February"))
        listOfExpMonths.add(Months(3, "March"))
        listOfExpMonths.add(Months(4, "April"))
        listOfExpMonths.add(Months(5, "May"))
        listOfExpMonths.add(Months(6, "June"))
        listOfExpMonths.add(Months(7, "July"))
        listOfExpMonths.add(Months(8, "August"))
        listOfExpMonths.add(Months(9, "September"))
        listOfExpMonths.add(Months(10, "October"))
        listOfExpMonths.add(Months(11, "November"))
        listOfExpMonths.add(Months(12, "December"))

        val thisYear = Calendar.getInstance().get(Calendar.YEAR)
        for (i in 1975..thisYear) {
            listOfExpYears.add(Years(Integer.toString(i)))
        }


    }

    fun showLifeExpDialog(type: String){

        val dialog = Dialog(this@RestarterActivity, R.style.AppTheme)
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

        if (type.equals("LifeExp")){

            tvTitle.setText("Life Experiences")

            tvSelectTitle.setText("Select Life Experiences")

            etAlertDegree.setHint("Type your Life Experiences here")

        }

        var mAdapterLifeExps: RecyclerView.Adapter<*>? = null

        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.layoutManager = mLayoutManager
        mAdapterLifeExps = LifeExpAdapter(listOfLifeExperiences,"OnBreak")
        recyclerView!!.adapter = mAdapterLifeExps

        closeDialog.setOnClickListener {

            dialog.dismiss()
        }

        tvSave.setOnClickListener {

            if (etAlertDegree.length()>0){

                val editDegree = etAlertDegree.text

                selectedLifeExp = editDegree.toString()

                etReason?.setText(selectedLifeExp)

            }

            dialog.dismiss()

        }

    }

    fun getLifeExperiences(){

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.GetLifeExperiences(EndPoints.CLIENT_ID, "Bearer " + EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<LifeExperienceResponse> {
            override fun onResponse(call: Call<LifeExperienceResponse>, response: Response<LifeExperienceResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    EndPoints.ACCESS_TOKEN = response.body()!!.auth!!.accessToken.toString()

                    val str_response = Gson().toJson(response)
                    val jsonObject: JSONObject = JSONObject(
                        str_response.substring(
                            str_response.indexOf("{"),
                            str_response.lastIndexOf("}") + 1
                        )
                    )

                    val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                    val jsonarray_info: JSONArray = jsonObject1.getJSONArray("body")


                    for (i in 0 until response.body()!!.body!!.size) {

                        val json_objectdetail: JSONObject = jsonarray_info.getJSONObject(i)

                        val model: LifeExperiences = LifeExperiences();

                        model.status = json_objectdetail.getBoolean("status")

                        model.name = json_objectdetail.getString("name")

                        model.id = json_objectdetail.getInt("id")

                        listOfLifeExperiences.add(LifeExperiences(model.status, model.name!!, model.id))
                    }


                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<LifeExperienceResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })
    }

    public fun SelectedLifeExp(id: Int, lifeExp: String) {

        etReason?.setText(lifeExp)

        selectedLifeExp = lifeExp

        selectedLifeExpId = id

    }

    fun getSkills() {

        val params = HashMap<String, String>()

        params["type"] = "life_experience"

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.GetSkills(EndPoints.CLIENT_ID, "Bearer " + EndPoints.ACCESS_TOKEN, params)

        call.enqueue(object : Callback<SkillsResponse> {
            override fun onResponse(call: Call<SkillsResponse>, response: Response<SkillsResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    EndPoints.ACCESS_TOKEN = response.body()!!.auth!!.accessToken.toString()

                    val str_response = Gson().toJson(response)
                    val jsonObject: JSONObject = JSONObject(
                        str_response.substring(
                            str_response.indexOf("{"),
                            str_response.lastIndexOf("}") + 1
                        )
                    )

                    val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                    val jsonarray_info: JSONArray = jsonObject1.getJSONArray("body")


                    for (i in 0 until response.body()!!.body!!.size) {

                        val json_objectdetail: JSONObject = jsonarray_info.getJSONObject(i)

                        val model: Skills = Skills();

                        model.approve = json_objectdetail.getBoolean("approve")

                        model.id = json_objectdetail.getInt("id")

                        model.category_type = json_objectdetail.getString("category_type")

                        model.name = json_objectdetail.getString("name")


                        listOfSkills.add(Skills(model.approve, model.id, model.category_type!!, model.name!!))
                        listOfSkillsCompare.add(Skills(model.approve, model.id, model.category_type!!, model.name!!))
                    }


                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<SkillsResponse>, t: Throwable) {

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
