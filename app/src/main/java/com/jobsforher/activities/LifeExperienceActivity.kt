package com.jobsforher.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_life_exp.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.jobsforher.R
import com.jobsforher.adapters.*
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.models.*
import com.jobsforher.models.Skills
import com.jobsforher.network.responsemodels.*
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import women.jobs.jobsforher.activities.BaseActivity
import java.util.*
import kotlin.collections.ArrayList

class LifeExperienceActivity : BaseActivity() {

    private val TAG = javaClass.simpleName

    var type: String = ""
    internal var textlength = 0

    private var retrofitInterface: RetrofitInterface? = null

    var listOfExpMonths: ArrayList<Months> = ArrayList()
    var listOfExpYears: ArrayList<Years> = ArrayList()
    var listOfSkills: ArrayList<Skills> = ArrayList()
    var listOfSkillsCompare: ArrayList<Skills> = ArrayList()
    var listOfLifeExperiences: ArrayList<LifeExperiences> = ArrayList()

    var selectedFromMonth: String = ""
    var selectedToMonth: String = ""
    var selectedFromYear: String = ""
    var selectedToYear: String = ""
    var selectedLifeExp: String = ""

    var seletedFromMonthId: Int = 0
    var selectedToMonthId: Int = 0
    var selectedLifeExpId: Int = 0

    var selectType: String = ""

    val listLifeExperience = mutableListOf("")
    val listSkillsSelected = mutableListOf("")

    private var awesomeValidation_life: AwesomeValidation? = null

    var location: String = ""

    var selectedCheckBox: String = ""

    var listOfSelectedSkills: ArrayList<Skills> = ArrayList()
    val listcaption= ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_life_exp)

        location = intent.getStringExtra("location")

        type = intent.getStringExtra("type")

        listOfSkills.clear()
        listOfSkillsCompare.clear()

        getSkills()

        getMonthAndYears()

        getLifeExperiences()

        listcaption.add("Boss Mom")
        listcaption.add("Constant Custodian")
        listcaption.add("Return of the Warrior")
        listcaption.add("Adventuress")
        listcaption.add("New Beginnings")
        listcaption.add("New discoveries")

        val life = arrayOf("Motherhood", "Elderly-Care", "Health", "Travel",
            "Marriage", "Relocation","Others")
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, life)
        val actv = findViewById<View>(R.id.etLifeExp) as AutoCompleteTextView
        actv.setThreshold(1)//will start working from first character
        actv.setAdapter(adapter)//setting the adapter data into the AutoCompleteTextView
        actv.setOnItemClickListener(object : AdapterView.OnItemClickListener {

            override fun onItemClick(parent: AdapterView<*>, arg1: View, position: Int, arg3: Long) {
                //if (position==0)
            }
        })

        etLifeExp.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                etLifeExp.showDropDown()
                return false
            }
        })

        awesomeValidation_life = AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation_life!!.addValidation(etLifeExp, RegexTemplate.NOT_EMPTY,"Please enter the life experience")
        // awesomeValidation_life!!.addValidation(etCaption, RegexTemplate.NOT_EMPTY,"Please enter the caption");
        awesomeValidation_life!!.addValidation(etSkillsExp, RegexTemplate.NOT_EMPTY,"Please enter the skills");

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val date = c.get(Calendar.MONTH)+1

        btnLifeExpSubmit.setOnClickListener {

            //            val intent = Intent(applicationContext, WorkExperienceActivity::class.java)
//            intent.putExtra("type",type)
//            startActivity(intent)
            if(awesomeValidation_life!!.validate()) {
                if (etLifeFromYear.getText().toString().trim().isEmpty() || etLifeFromMonth.getText().toString().trim().isEmpty() ) {
                    ToastHelper.makeToast(applicationContext, "From date can't be blank")
                    return@setOnClickListener
                }
                if (!cb_LifeExpOngoing.isChecked) {
                    if (etLifeToYear.text.toString().trim().isEmpty() || etLifeToMonth.text.toString().trim().isEmpty()) {
                        ToastHelper.makeToast(applicationContext, "To date can't be blank")
                        return@setOnClickListener
                    }
                }
                if (cb_LifeExpOngoing.isChecked==false && Integer.parseInt(etLifeFromYear.getText().toString()) <= Integer.parseInt(etLifeToYear.text.toString())
                    && seletedFromMonthId<=selectedToMonthId) {
                    val editLifeExp = etLifeExp.text
                    val editFromMonth = etLifeFromMonth.text
                    val editFromYear = etLifeFromYear.text
                    val editToMonth = etLifeToMonth.text
                    val editToYear = etLifeToYear.text
                    val editSkill = etSkillsExp.text

                    submitLifeExperienceDetails(
                        editLifeExp.toString(), editFromMonth.toString(), editFromYear.toString(),
                        editToMonth.toString(), editToYear.toString(), editSkill.toString()
                    )
                }
                else if(cb_LifeExpOngoing.isChecked==false && Integer.parseInt(etLifeFromYear.getText().toString()) <= Integer.parseInt(etLifeToYear.text.toString())){
                    val editLifeExp = etLifeExp.text
                    val editFromMonth = etLifeFromMonth.text
                    val editFromYear = etLifeFromYear.text
                    val editToMonth = etLifeToMonth.text
                    val editToYear = etLifeToYear.text
                    val editSkill = etSkillsExp.text

                    submitLifeExperienceDetails(
                        editLifeExp.toString(), editFromMonth.toString(), editFromYear.toString(),
                        editToMonth.toString(), editToYear.toString(), editSkill.toString()
                    )
                }
                else if(cb_LifeExpOngoing.isChecked==true){
                    if(Integer.parseInt(etLifeFromYear.text.toString())<year) {
                        val editLifeExp = etLifeExp.text
                        val editFromMonth = etLifeFromMonth.text
                        val editFromYear = etLifeFromYear.text
                        val editToMonth = etLifeToMonth.text
                        val editToYear = etLifeToYear.text
                        val editSkill = etSkillsExp.text

                        submitLifeExperienceDetails(
                            editLifeExp.toString(), editFromMonth.toString(), editFromYear.toString(),
                            editToMonth.toString(), editToYear.toString(), editSkill.toString()
                        )
                    }
                    else if(seletedFromMonthId<=date && Integer.parseInt(etLifeFromYear.text.toString())==year){
                        val editLifeExp = etLifeExp.text
                        val editFromMonth = etLifeFromMonth.text
                        val editFromYear = etLifeFromYear.text
                        val editToMonth = etLifeToMonth.text
                        val editToYear = etLifeToYear.text
                        val editSkill = etSkillsExp.text

                        submitLifeExperienceDetails(
                            editLifeExp.toString(), editFromMonth.toString(), editFromYear.toString(),
                            editToMonth.toString(), editToYear.toString(), editSkill.toString()
                        )
                    }
                    else
                        ToastHelper.makeToast(applicationContext, "From date should be less than Current date")
                }
                else
                    ToastHelper.makeToast(applicationContext, "From date should be less than To date")

            }
            else {

//                ToastHelper.makeToast(applicationContext, "Enter the skills")

            }
        }

        etSkillsExp.setOnClickListener {

            //showSkillDialog("Skill")
            awesomeValidation_life!!.clear()
            listSkillsSelected.clear()

        }



        etLifeFromMonth.setOnClickListener {

            showFromDateDialog("From")

            selectType = "From"
        }

        etLifeFromYear.setOnClickListener {

            showFromDateDialog("From")

            selectType = "From"

        }

        etLifeToMonth.setOnClickListener {

            showFromDateDialog("To")

            selectType = "To"
        }

        etLifeToYear.setOnClickListener {

            showFromDateDialog("To")

            selectType = "To"

        }

        etLifeExp.setOnClickListener {

            //showLifeExpDialog("LifeExp")
            listLifeExperience.clear()
        }

        tvLifeSkip.setOnClickListener {

            if (type.equals("riser")){

                val intent = Intent(applicationContext, EducationActivity::class.java)
                intent.putExtra("type",type)
                intent.putExtra("location",location)
                startActivity(intent)

            }else if (type.equals("starter")){

                val intent = Intent(applicationContext, WorkExperienceActivity::class.java)
                intent.putExtra("type",type)
                intent.putExtra("location",location)
                startActivity(intent)

            }
        }
    }

    fun showLifeExpDialog(type: String){

        val dialog = Dialog(this@LifeExperienceActivity, R.style.AppTheme)
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
        mAdapterLifeExps = LifeExpAdapter(listOfLifeExperiences, "LifeExp")
        recyclerView!!.adapter = mAdapterLifeExps

        closeDialog.setOnClickListener {

            dialog.dismiss()
        }

        tvSave.setOnClickListener {

            if (etAlertDegree.length()>0){

                val editDegree = etAlertDegree.text

                selectedLifeExp = editDegree.toString()


                etLifeExp?.setText(selectedLifeExp)

            }

            dialog.dismiss()

        }

    }

    fun onCheckboxLifeOngoingClicked(view: View) {

        if (cb_LifeExpOngoing.isChecked) {

            etLifeToMonth.setEnabled(false)

            etLifeToYear.setEnabled(false)

            selectedCheckBox = "true"

            toLayout.visibility = View.INVISIBLE

        } else {

            toLayout.visibility = View.VISIBLE

            etLifeToMonth.setEnabled(true)

            etLifeToYear.setEnabled(true)

            selectedCheckBox = "false"

        }
    }

    fun submitLifeExperienceDetails(editLifeExp: String, fromMonth: String, fromYear: String, toMonth: String,
                                    toYear: String, editLifeSkill: String) {

        listLifeExperience.add(editLifeExp.drop(0))

        val s1 =
            etSkillsExp.getText().toString().drop(0).split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()

        for (i in 0 until s1.size) {

            if (s1.get(i).length > 0) {

                listSkillsSelected.add(s1.get(i))

            }

            Logger.d("TAGG", s1.get(i))

        }

        var skills: String = etSkillsExp.text.toString()
        var skillValue: String = ""
        if (skills.contains(",")) {
            var array: List<String> = skills.split(",")
            for (l in 0 until array.size) {
                skillValue = "'" + array[l].toString() + "'"
            }
        }
        else
            skillValue = "'" + skills + "'"


        var exp: String = etLifeExp.text.toString()
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

        var userEndDate = ""
        var userOnGoing = ""

        if (cb_LifeExpOngoing.isChecked) {

            userEndDate = ""
            userOnGoing = "true"

        } else {

            userEndDate = toYear + "-" + selectedToMonthId + "-01"
            userOnGoing = "false"


        }

        val s = expValue.substring(1,expValue.length-1)
        if (s.equals("Motherhood"))
            params["caption"]  ="Boss Mom"
        else if (s.equals("Elderly-Care"))
            params["caption"]  ="Constant Custodian"
        else if (s.equals("Health"))
            params["caption"]  ="Return of the Warrior"
        else if (s.equals("Travel"))
            params["caption"]  ="Adventuress"
        else if (s.equals("Marriage"))
            params["caption"]  ="New Beginnings"
        else if (s.equals("Relocation"))
            params["caption"]  ="New discoveries"

        params["life_experience"] = "["+expValue+"]"
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

                        if (type.equals("riser")){

                            val intent = Intent(applicationContext, EducationActivity::class.java)
                            intent.putExtra("type",type)
                            intent.putExtra("location",location)
                            startActivity(intent)

                        }else if (type.equals("starter")){

                            val intent = Intent(applicationContext, WorkExperienceActivity::class.java)
                            intent.putExtra("type",type)
                            intent.putExtra("location",location)
                            startActivity(intent)

                        }


                    } else {

//                        ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())

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

    fun getLifeExperiences(){

        listOfLifeExperiences.clear()
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

    fun showFromDateDialog(type:String) {

        val dialog = Dialog(this@LifeExperienceActivity, R.style.AppTheme)
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
        mAdapterMonths = MonthsAdapter(listOfExpMonths, "LifeExp")
        monthrecyclerView!!.adapter = mAdapterMonths

        var mAdapterYears: RecyclerView.Adapter<*>? = null

        val mLayoutManagerYears = LinearLayoutManager(applicationContext)
        yearRecyclerView!!.layoutManager = mLayoutManagerYears
        mAdapterYears = YearsAdapter(listOfExpYears, "LifeExp")
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

            etLifeFromMonth?.setText(month)

            selectedFromMonth = month

            seletedFromMonthId = monthId

        } else {

            etLifeToMonth?.setText(month)

            selectedToMonth = month

            selectedToMonthId = monthId
        }

    }

    public fun SelectedYear(year: String) {

        if (selectType.equals("From")) {

            etLifeFromYear?.setText(year)

            selectedFromYear = year

        } else {

            etLifeToYear?.setText(year)

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

        listOfExpYears.add(Years("1995"))
        listOfExpYears.add(Years("1996"))
        listOfExpYears.add(Years("1997"))
        listOfExpYears.add(Years("1998"))
        listOfExpYears.add(Years("1999"))
        listOfExpYears.add(Years("2000"))
        listOfExpYears.add(Years("2001"))
        listOfExpYears.add(Years("2002"))
        listOfExpYears.add(Years("2003"))
        listOfExpYears.add(Years("2004"))
        listOfExpYears.add(Years("2005"))
        listOfExpYears.add(Years("2006"))
        listOfExpYears.add(Years("2007"))
        listOfExpYears.add(Years("2008"))
        listOfExpYears.add(Years("2009"))
        listOfExpYears.add(Years("2010"))
        listOfExpYears.add(Years("2011"))
        listOfExpYears.add(Years("2012"))
        listOfExpYears.add(Years("2013"))
        listOfExpYears.add(Years("2014"))
        listOfExpYears.add(Years("2015"))
        listOfExpYears.add(Years("2016"))
        listOfExpYears.add(Years("2017"))
        listOfExpYears.add(Years("2018"))
        listOfExpYears.add(Years("2019"))
        listOfExpYears.add(Years("2020"))
        listOfExpYears.add(Years("2021"))
        listOfExpYears.add(Years("2022"))
        listOfExpYears.add(Years("2023"))
        listOfExpYears.add(Years("2024"))
        listOfExpYears.add(Years("2025"))

    }

    public fun SelectedSkill(approve: Boolean, id: Int, category_type: String, skill: String) {

//        etSkillsExp?.setText(skill)

//        selectedSkill = skill
//
//        selectedSkillId = id

        var a: Boolean = true
        Logger.e("DATAsbSelectedSkill", skill)

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

    public fun SelectedLifeExp(id: Int, lifeExp: String) {

        etLifeExp?.setText(lifeExp)

        selectedLifeExp = lifeExp

        selectedLifeExpId = id

    }

    fun showSkillDialog(type:String) {

        //listOfSelectedSkills.clear()

        val dialog = Dialog(this@LifeExperienceActivity, R.style.AppTheme)
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
        mAdapterSkils = SkillsAdapter(listOfSkills, "lifeExp", listOfSelectedSkills)
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

            if (sbSkill!!.length>0)
                etSkillsExp?.setText(sbSkill!!.substring(0,sbSkill!!.length-1))
            else
                etSkillsExp?.setText(etsearch.text)


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

                mAdapterSkils = SkillsAdapter(listOfSkillsCompare,"lifeExp",listOfSelectedSkills)
                recyclerView!!.adapter = mAdapterSkils
                recyclerView!!.layoutManager =
                    LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

            }
        })
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

}
