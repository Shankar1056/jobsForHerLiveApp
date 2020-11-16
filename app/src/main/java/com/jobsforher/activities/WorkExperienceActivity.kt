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
import com.jobsforher.R
import com.jobsforher.adapters.MonthsAdapter
import com.jobsforher.adapters.SkillsAdapter
import com.jobsforher.adapters.YearsAdapter
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.models.Months
import com.jobsforher.models.Skills
import com.jobsforher.models.Years
import com.jobsforher.network.responsemodels.CreateWorkResponse
import com.jobsforher.network.responsemodels.SkillsResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import kotlinx.android.synthetic.main.activity_work_exp.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import women.jobs.jobsforher.activities.BaseActivity
import java.util.*
import kotlin.collections.ArrayList


class WorkExperienceActivity : BaseActivity() {

    private val TAG = javaClass.simpleName

    private var retrofitInterface: RetrofitInterface? = null

    var type: String = ""

    var location: String = ""

    var selectedCheckBox: String = ""
    internal var textlength = 0

    var listOfExpMonths: ArrayList<Months> = ArrayList()
    var listOfExpYears: ArrayList<Years> = ArrayList()
    var listOfSkills: ArrayList<Skills> = ArrayList()
    var listOfSkillsCompare: ArrayList<Skills> = ArrayList()

    var selectedFromMonth: String = ""
    var selectedToMonth: String = ""
    var selectedFromYear: String = ""
    var selectedToYear: String = ""

    var seletedFromMonthId: Int = 0
    var selectedToMonthId: Int = 0

    var selectType: String = ""

    val listSkillsSelected = mutableListOf("")

    var listOfSelectedSkills: ArrayList<Skills> = ArrayList()

    private var doubleBackToExitPressedOnce = false

    private var awesomeValidation_work: AwesomeValidation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_exp)

        location = intent.getStringExtra("location")

        type = intent.getStringExtra("type")

        if (type.equals("starter")) {

            tvWorkExpTitle.setText("Tell us about your \nWork/Internship")

            tvWorkSkip.visibility = View.VISIBLE


        } else if (type.equals("restarter")) {

            tvWorkExpTitle.setText("Tell us about your \nLatest Work Experience")

            tvWorkSkip.visibility = View.VISIBLE


        } else if (type.equals("riser")) {

            tvWorkExpTitle.setText("Tell us about your \nWork Experience")

            tvWorkSkip.visibility = View.GONE

        }

        etWorkDesig.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                etWorkDesig.showDropDown()
                return false
            }
        })
        etWorkCompany.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                etWorkCompany.showDropDown()
                return false
            }
        })


        val designation = arrayOf(
            "Product Manager",
            "Product Designer",
            "VP Engineering",
            "Software Developer",
            "Front End Developer",
            "Marketing Head",
            "AVP Sales",
            "VP Sales",
            "Product Analyst",
            "User Interface Designer",
            "User Experience Designer",
            "Digital Marketing Manager",
            "Sales Manager"
        )
        val adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, designation)
        val actv = findViewById<View>(R.id.etWorkDesig) as AutoCompleteTextView
        actv.setThreshold(1)//will start working from first character
        actv.setAdapter(adapter)//setting the adapter data into the AutoCompleteTextView

        val company = arrayOf(
            "IBM India Pvt Ltd",
            "Wipro Technologies Pvt Ltd",
            "Quanted Technologies",
            "Sify Technologies",
            "UNISYS India Pvt Ltd",
            "HCL Technologies Pvt Ltd"
        )
        val adapterCompany =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, company)
        val actvComplany = findViewById<View>(R.id.etWorkCompany) as AutoCompleteTextView
        actvComplany.setThreshold(1)//will start working from first character
        actvComplany.setAdapter(adapterCompany)//setting the adapter data into the AutoCompleteTextView

        listOfSkills.clear()
        listOfSkillsCompare.clear()

        getSkills()

        getMonthAndYears()

        etWorkFromMonth.setOnClickListener {

            showFromDateDialog("From")

            selectType = "From"
        }


        etWorkFromYear.setOnClickListener {

            showFromDateDialog("From")

            selectType = "From"
        }

        etWorkToMonth.setOnClickListener {

            showFromDateDialog("To")

            selectType = "To"
        }

        etWorkToYear.setOnClickListener {

            showFromDateDialog("To")

            selectType = "To"
        }

        etWorkSkills.setOnClickListener {

            awesomeValidation_work!!.clear()
            // showSkillDialog("Skill")

            listSkillsSelected.clear()

        }

        awesomeValidation_work = AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation_work!!.addValidation(
            etWorkDesig,
            RegexTemplate.NOT_EMPTY,
            "Please enter your designation"
        )
        awesomeValidation_work!!.addValidation(
            etWorkCompany,
            RegexTemplate.NOT_EMPTY,
            "Please enter your company name"
        );
        awesomeValidation_work!!.addValidation(
            etWorkSkills,
            RegexTemplate.NOT_EMPTY,
            "Please enter atleast one skill"
        );

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val date = c.get(Calendar.MONTH) + 1
        btnWorkExpSubmit.setOnClickListener {

            if (awesomeValidation_work!!.validate()) {
                if (etWorkFromYear.getText().toString().trim().isEmpty()) {
                    ToastHelper.makeToast(applicationContext, "From date can't be blank")
                    return@setOnClickListener
                }
                if (!cb_WorkOngoing.isChecked) {
                    if (etWorkToYear.text.toString().trim().isEmpty()) {
                        ToastHelper.makeToast(applicationContext, "To date can't be blank")
                        return@setOnClickListener
                    }
                }
                if (cb_WorkOngoing.isChecked == false && Integer.parseInt(
                        etWorkFromYear.getText().toString()
                    ) <= Integer.parseInt(etWorkToYear.text.toString())
                    && seletedFromMonthId <= selectedToMonthId
                ) {
                    val editWorkDesig = etWorkDesig.text
                    val editWorkCompany = etWorkCompany.text
                    val editWorkFromMonth = etWorkFromMonth.text
                    val editWorkFromYear = etWorkFromYear.text
                    val editWorkToMonth = etWorkToMonth.text
                    val editWorkToYear = etWorkToYear.text
                    val editWorkSkill = etWorkSkills.text

                    submitWorkExperienceDetails(
                        editWorkDesig.toString(),
                        editWorkCompany.toString(),
                        editWorkFromMonth.toString(),
                        editWorkFromYear.toString(),
                        editWorkToMonth.toString(),
                        editWorkToYear.toString(),
                        editWorkSkill.toString()
                    )
                } else if (cb_WorkOngoing.isChecked == false && Integer.parseInt(
                        etWorkFromYear.getText().toString()
                    ) <= Integer.parseInt(etWorkToYear.text.toString())
                ) {
                    val editWorkDesig = etWorkDesig.text
                    val editWorkCompany = etWorkCompany.text
                    val editWorkFromMonth = etWorkFromMonth.text
                    val editWorkFromYear = etWorkFromYear.text
                    val editWorkToMonth = etWorkToMonth.text
                    val editWorkToYear = etWorkToYear.text
                    val editWorkSkill = etWorkSkills.text

                    submitWorkExperienceDetails(
                        editWorkDesig.toString(),
                        editWorkCompany.toString(),
                        editWorkFromMonth.toString(),
                        editWorkFromYear.toString(),
                        editWorkToMonth.toString(),
                        editWorkToYear.toString(),
                        editWorkSkill.toString()
                    )
                } else if (cb_WorkOngoing.isChecked == true) {
                    if (seletedFromMonthId <= date && Integer.parseInt(etWorkFromYear.text.toString()) <= year) {
                        val editWorkDesig = etWorkDesig.text
                        val editWorkCompany = etWorkCompany.text
                        val editWorkFromMonth = etWorkFromMonth.text
                        val editWorkFromYear = etWorkFromYear.text
                        val editWorkToMonth = etWorkToMonth.text
                        val editWorkToYear = etWorkToYear.text
                        val editWorkSkill = etWorkSkills.text

                        submitWorkExperienceDetails(
                            editWorkDesig.toString(),
                            editWorkCompany.toString(),
                            editWorkFromMonth.toString(),
                            editWorkFromYear.toString(),
                            editWorkToMonth.toString(),
                            editWorkToYear.toString(),
                            editWorkSkill.toString()
                        )
                    } else if (seletedFromMonthId <= date && Integer.parseInt(etWorkFromYear.text.toString()) == year) {
                        val editWorkDesig = etWorkDesig.text
                        val editWorkCompany = etWorkCompany.text
                        val editWorkFromMonth = etWorkFromMonth.text
                        val editWorkFromYear = etWorkFromYear.text
                        val editWorkToMonth = etWorkToMonth.text
                        val editWorkToYear = etWorkToYear.text
                        val editWorkSkill = etWorkSkills.text

                        submitWorkExperienceDetails(
                            editWorkDesig.toString(),
                            editWorkCompany.toString(),
                            editWorkFromMonth.toString(),
                            editWorkFromYear.toString(),
                            editWorkToMonth.toString(),
                            editWorkToYear.toString(),
                            editWorkSkill.toString()
                        )
                    } else
                        ToastHelper.makeToast(
                            applicationContext,
                            "From date should be less than Current date"
                        )
                } else {
                    ToastHelper.makeToast(
                        applicationContext,
                        "From date should be less than To date"
                    )
                }

            } else {

//                ToastHelper.makeToast(applicationContext, "Enter the skills")

            }

        }

        tvWorkSkip.setOnClickListener {

            if (type.equals("starter")) {

                val intent = Intent(applicationContext, AddProfileActivity::class.java)
                intent.putExtra("type", type)
                intent.putExtra("location", location)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

            } else if (type.equals("restarter")) {

                val intent = Intent(applicationContext, EducationActivity::class.java)
                intent.putExtra("type", type)
                intent.putExtra("location", location)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

            }

        }


    }

    fun onCheckboxWorkClicked(view: View) {

        if (cb_WorkOngoing.isChecked) {

            etWorkToMonth.setEnabled(false)

            etWorkToYear.setEnabled(false)

            selectedCheckBox = "true"

            toWorkLayout.visibility = View.INVISIBLE

        } else {

            etWorkToMonth.setEnabled(true)

            etWorkToYear.setEnabled(true)

            selectedCheckBox = "false"

            toWorkLayout.visibility = View.VISIBLE

        }
    }

    fun getSkills() {

        val params = HashMap<String, String>()

        params["type"] = "life_experience"

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.GetSkills(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )

        call.enqueue(object : Callback<SkillsResponse> {
            override fun onResponse(
                call: Call<SkillsResponse>,
                response: Response<SkillsResponse>
            ) {

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


                        listOfSkills.add(
                            Skills(
                                model.approve,
                                model.id,
                                model.category_type!!,
                                model.name!!
                            )
                        )
                        listOfSkillsCompare.add(
                            Skills(
                                model.approve,
                                model.id,
                                model.category_type!!,
                                model.name!!
                            )
                        )
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

    fun showFromDateDialog(type: String) {

        val dialog = Dialog(this@WorkExperienceActivity, R.style.AppTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.activity_alert_date)
        dialog.show()

        val monthrecyclerView = dialog.findViewById(R.id.monthRecyclerView) as RecyclerView
        val yearRecyclerView = dialog.findViewById(R.id.yearRecyclerView) as RecyclerView

        val closeDialog = dialog.findViewById(R.id.closeDialog) as ImageView
        val tvSave = dialog.findViewById(R.id.tvSave) as TextView
        val tvTitle = dialog.findViewById(R.id.tvTitle) as TextView

        if (type.equals("From")) {

            tvTitle.setText("From Date")
        } else {

            tvTitle.setText("To Date")
        }

        var mAdapterMonths: RecyclerView.Adapter<*>? = null

        val mLayoutManagerMonth = LinearLayoutManager(applicationContext)
        monthrecyclerView!!.layoutManager = mLayoutManagerMonth
        mAdapterMonths = MonthsAdapter(listOfExpMonths, "WorkExp")
        monthrecyclerView!!.adapter = mAdapterMonths

        var mAdapterYears: RecyclerView.Adapter<*>? = null

        val mLayoutManagerYears = LinearLayoutManager(applicationContext)
        yearRecyclerView!!.layoutManager = mLayoutManagerYears
        mAdapterYears = YearsAdapter(listOfExpYears, "WorkExp")
        yearRecyclerView!!.adapter = mAdapterYears

        closeDialog.setOnClickListener {

            dialog.dismiss()
        }

        tvSave.setOnClickListener {

            dialog.dismiss()

        }
    }

    public fun SelectedYear(year: String) {

        if (selectType.equals("From")) {

            etWorkFromYear?.setText(year)

            selectedFromYear = year

        } else {

            etWorkToYear?.setText(year)

            selectedToYear = year
        }

    }

    public fun SelectedMonth(monthId: Int, month: String) {

        if (selectType.equals("From")) {

            etWorkFromMonth?.setText(month)

            selectedFromMonth = month

            seletedFromMonthId = monthId

        } else {

            etWorkToMonth?.setText(month)

            selectedToMonth = month

            selectedToMonthId = monthId
        }

    }

    fun showSkillDialog(type: String) {

        //listOfSelectedSkills.clear()

        val dialog = Dialog(this@WorkExperienceActivity, R.style.AppTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.activity_alert_skill)
        dialog.show()

        val recyclerView = dialog.findViewById(R.id.locationRecyclerView) as RecyclerView
        val closeDialog = dialog.findViewById(R.id.closeDialog) as ImageView
        val tvSave = dialog.findViewById(R.id.tvSave) as TextView
        val etsearch: EditText = dialog.findViewById(R.id.editText) as EditText

        val tvTitle = dialog.findViewById(R.id.tvTitle) as TextView
        val tvSelectTitle = dialog.findViewById(R.id.tvSelectTitle) as TextView

        if (type.equals("Skill")) {

            tvTitle.setText("Skills")

            tvSelectTitle.setText("Select Skills")
        }

        var mAdapterSkils: RecyclerView.Adapter<*>? = null

        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.layoutManager = mLayoutManager
        mAdapterSkils = SkillsAdapter(listOfSkills, "WorkExp", listOfSelectedSkills)
        recyclerView!!.adapter = mAdapterSkils

        closeDialog.setOnClickListener {

            dialog.dismiss()
        }

        tvSave.setOnClickListener {

            dialog.dismiss()

            var sbSkill: String? = ""

            for (i in 0 until listOfSelectedSkills.size) {

                sbSkill = sbSkill + listOfSelectedSkills.get(i).name + ","

            }

            if (sbSkill!!.length > 0)
                etWorkSkills?.setText(sbSkill!!.substring(0, sbSkill!!.length - 1))
            else
                etWorkSkills?.setText(etsearch.text)
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

                mAdapterSkils = SkillsAdapter(listOfSkillsCompare, "WorkExp", listOfSelectedSkills)
                recyclerView!!.adapter = mAdapterSkils
                recyclerView!!.layoutManager =
                    LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

            }
        })
    }


    public fun SelectedSkill(approve: Boolean, id: Int, category_type: String, skill: String) {


        Logger.e("DATAsbSelectedSkill", skill)
        var a: Boolean = true

        for (x in 0 until listOfSelectedSkills.size) {
            if (listOfSelectedSkills.get(x).name.equals(skill)) {
                a = false
                listOfSelectedSkills.removeAt(x)
                break
            }
        }

        if (a == true) {
            listOfSelectedSkills.add(Skills(skill))
        }

    }

    fun submitWorkExperienceDetails(
        editWorkDesig: String, editWorkCompany: String, editWorkFromMonth: String,
        editWorkFromYear: String, editWorkToMonth: String, editWorkToYear: String,
        editWorkSkill: String
    ) {

        var skills: String = etWorkSkills.text.toString()
        var skillValue: String = ""
        if (skills.contains(",")) {
            var array: List<String> = skills.split(",")
            for (l in 0 until array.size) {
                skillValue = "'" + array[l].toString() + "'"
            }
        } else
            skillValue = "'" + skills + "'"

        val params = HashMap<String, String>()

        val userDesignation = editWorkDesig
        val userCompanyName = editWorkCompany

        val userStartDate = editWorkFromYear + "-" + seletedFromMonthId + "-01"

        var userEndDate = ""
        var userOnGoing = ""

        if (cb_WorkOngoing.isChecked) {

            userEndDate = ""
            userOnGoing = "true"

        } else {

            userEndDate = editWorkToYear + "-" + selectedToMonthId + "-01"
            userOnGoing = "false"
        }

        params["designation"] = userDesignation
        params["company_name"] = userCompanyName
        params["current_company"] = userOnGoing
        params["start_date"] = userStartDate
        params["end_date"] = userEndDate
        params["location"] = location
        params["description"] = ""
        params["skills"] = "[" + skillValue + "]"
        params["default_image"] = "1"

        Logger.d("TAGG", "")
        Logger.d("TAGG", userStartDate)
        Logger.d("TAGG", userEndDate)
        Logger.d("TAGG", location)
        Logger.d("TAGG", "[" + skillValue + "]")

        Logger.d("TAGG", params.toString())

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.CreateWorkExperience(
            EndPoints.CLIENT_ID, "Bearer " +
                    EndPoints.ACCESS_TOKEN, params
        )

        call.enqueue(object : Callback<CreateWorkResponse> {
            override fun onResponse(
                call: Call<CreateWorkResponse>,
                response: Response<CreateWorkResponse>
            ) {

                Logger.d("CODE life exp", response.code().toString() + "")
                Logger.d("MESSAGE life exp", response.message() + "")
                Logger.d("URL life exp", "" + response)
                Logger.d("RESPONSE life exp", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    EndPoints.ACCESS_TOKEN = response.body()!!.auth!!.accessToken.toString()

                    if (response.body()!!.message.toString()
                            .equals("Working Profile Details created.")
                    ) {

                        if (type.equals("restarter")) {

                            val intent = Intent(applicationContext, EducationActivity::class.java)
                            intent.putExtra("type", type)
                            intent.putExtra("location", location)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)

                        } else if (type.equals("starter")) {

                            val intent = Intent(applicationContext, AddProfileActivity::class.java)
                            intent.putExtra("type", type)
                            intent.putExtra("location", location)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)

                        } else if (type.equals("riser")) {

                            val intent =
                                Intent(applicationContext, LifeExperienceActivity::class.java)
                            intent.putExtra("type", type)
                            intent.putExtra("location", location)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }


                    } else {

                        ToastHelper.makeToast(
                            applicationContext,
                            response.body()!!.message.toString()
                        )

                    }

                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<CreateWorkResponse>, t: Throwable) {

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
