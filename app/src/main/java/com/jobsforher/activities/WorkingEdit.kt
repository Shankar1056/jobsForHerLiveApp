package com.jobsforher.activities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jobsforher.R
import com.jobsforher.adapters.MonthsAdapter
import com.jobsforher.adapters.YearsAdapter
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.models.*
import com.jobsforher.network.responsemodels.*
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_selecting_location.*
import kotlinx.android.synthetic.main.layout_workingedit.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import women.jobs.jobsforher.activities.BaseActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


private var retrofitInterface: RetrofitInterface? = null
var workId = 0
private val GALLERY_IMAGE = 1
public var picturepath_work: String? = ""
public var imageEncoded_work = "";
val listIntegerWork = ArrayList<String>()

class WorkingEdit : BaseActivity() {

    private var awesomeValidation_work: AwesomeValidation? = null
    var selectedId: Int = 0
    var selectedCity: String = ""
    internal var textlength = 0

    var listOfMonths: ArrayList<Months> = ArrayList()
    var listOfYears: ArrayList<Years> = ArrayList()
    var selectedFromMonth: String = ""
    var selectedToMonth: String = ""
    var selectedFromYear: String = ""
    var selectedToYear: String = ""
    var seletedFromMonthId: Int = 0
    var selectedToMonthId: Int = 0
    var selectType: String = ""

    var listOfJobFArea: ArrayList<FunctionalAreaView> = ArrayList()
    var listOfJobFAreaSorted: ArrayList<FunctionalAreaView> = ArrayList()
    var listOfJobIndustry: ArrayList<FunctionalAreaView> = ArrayList()


    var listOfTopMetropolitan: ArrayList<Cities> = ArrayList()
    var listOfOtherCities: ArrayList<Cities> = ArrayList()
    var listOfOtherCountryCities: ArrayList<Cities> = ArrayList()
    var listOfOtherCountries: ArrayList<Cities> = ArrayList()

    var listOfTopMetropolitanCompare: ArrayList<Cities> = ArrayList()
    var listOfOtherCitiesCompare: ArrayList<Cities> = ArrayList()
    var listOfOtherCountryCitiesCompare: ArrayList<Cities> = ArrayList()
    var listOfOtherCountriesCompare: ArrayList<Cities> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_workingedit)
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        workId = intent.getIntExtra("ID", 0)

        editwork_back.setOnClickListener {
            this.finish()
        }

        edittext_designation.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                edittext_designation.showDropDown()
                return false
            }
        })
        edittext_companyname.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                edittext_companyname.showDropDown()
                return false
            }
        })


        getMonthAndYears()

        Log.d("TAGG", "ID iS" + workId)

        awesomeValidation_work = AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation_work!!.addValidation(
            edittext_designation,
            RegexTemplate.NOT_EMPTY,
            "Please enter your designation"
        )
        awesomeValidation_work!!.addValidation(
            edittext_companyname,
            RegexTemplate.NOT_EMPTY,
            "Please enter your company name"
        );
        awesomeValidation_work!!.addValidation(
            edittext_location,
            RegexTemplate.NOT_EMPTY,
            "Please enter the location"
        );
        awesomeValidation_work!!.addValidation(
            edittext_whatskills,
            RegexTemplate.NOT_EMPTY,
            "Please enter atleast one skill"
        );

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
        val adapter1 =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, designation)
        val actv = findViewById<View>(R.id.edittext_designation) as AutoCompleteTextView
        actv.setThreshold(1)//will start working from first character
        actv.setAdapter(adapter1)//setting the adapter data into the AutoCompleteTextView

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
        val actvComplany = findViewById<View>(R.id.edittext_companyname) as AutoCompleteTextView
        actvComplany.setThreshold(1)//will start working from first character
        actvComplany.setAdapter(adapterCompany)//setting the adapter data into the AutoCompleteTextView


        val spinner1aw = findViewById<View>(R.id.spinner1awork) as Spinner
        val spinner2aw = findViewById<View>(R.id.spinner2awork) as Spinner
        val listMonths = ArrayList<String>()
        listMonths.add("Jan")
        listMonths.add("Feb")
        listMonths.add("Mar")
        listMonths.add("Apr")
        listMonths.add("May")
        listMonths.add("Jun")
        listMonths.add("Jul")
        listMonths.add("Aug")
        listMonths.add("Sep")
        listMonths.add("Oct")
        listMonths.add("Nov")
        listMonths.add("Dec")

        listIntegerWork.add("01")
        listIntegerWork.add("02")
        listIntegerWork.add("03")
        listIntegerWork.add("04")
        listIntegerWork.add("05")
        listIntegerWork.add("06")
        listIntegerWork.add("07")
        listIntegerWork.add("08")
        listIntegerWork.add("09")
        listIntegerWork.add("10")
        listIntegerWork.add("11")
        listIntegerWork.add("12")
        val dataAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1, listMonths
        )

        saveWork.setOnClickListener {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus()!!.getWindowToken(), 0)
            }
            if (workId > 0) {
                if (awesomeValidation_work!!.validate()) {
                    if (etFromYear.getText().toString().trim().isEmpty()) {
                        ToastHelper.makeToast(applicationContext, "From date can't be blank")
                        return@setOnClickListener
                    }
                    if (!checkBox1.isChecked) {
                        if (etToYear.text.toString().trim().isEmpty()) {
                            ToastHelper.makeToast(applicationContext, "To date can't be blank")
                            return@setOnClickListener
                        }
                    }

                    if (checkBox1.isChecked == false && Integer.parseInt(
                            etFromYear.getText().toString()
                        ) == Integer.parseInt(etToYear.text.toString())
                        && seletedFromMonthId <= selectedToMonthId
                    ) {
                        updateWorkingData()
                        saveWork.isEnabled = false
                        save_UP.isEnabled = false
                    } else if (checkBox1.isChecked == false && Integer.parseInt(
                            etFromYear.getText().toString()
                        ) < Integer.parseInt(etToYear.text.toString())
                    ) {
                        updateWorkingData()
                        saveWork.isEnabled = false
                        save_UP.isEnabled = false
                    } else if (checkBox1.isChecked == true) {
                        updateWorkingData()
                        saveWork.isEnabled = false
                        save_UP.isEnabled = false
                    } else
                        ToastHelper.makeToast(
                            applicationContext,
                            "From date should be less than To date"
                        )

                }
            } else {
                if (awesomeValidation_work!!.validate()) {
                    if (etFromYear.getText().toString().trim().isEmpty()) {
                        ToastHelper.makeToast(applicationContext, "From date can't be blank")
                        return@setOnClickListener
                    }
                    if (!checkBox1.isChecked) {
                        if (etToYear.text.toString().trim().isEmpty()) {
                            ToastHelper.makeToast(applicationContext, "To date can't be blank")
                            return@setOnClickListener
                        }
                    }

                    if (!checkBox1.isChecked && Integer.parseInt(
                            etFromYear.getText().toString()
                        ) == Integer.parseInt(etToYear.text.toString()) && seletedFromMonthId <= selectedToMonthId
                    ) {
                        AddWorkingData()
                        saveWork.isEnabled = false
                        save_UP.isEnabled = false
                    } else if (checkBox1.isChecked == false && Integer.parseInt(
                            etFromYear.getText().toString()
                        ) < Integer.parseInt(etToYear.text.toString())
                    ) {
                        AddWorkingData()
                        saveWork.isEnabled = false
                        save_UP.isEnabled = false
                    } else if (checkBox1.isChecked == true) {
                        AddWorkingData()
                        saveWork.isEnabled = false
                        save_UP.isEnabled = false
                    } else
                        ToastHelper.makeToast(
                            applicationContext,
                            "From date should be less than To date"
                        )


                }
            }

        }

        save_UP.setOnClickListener {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus()!!.getWindowToken(), 0)
            }
            if (workId > 0) {
                if (awesomeValidation_work!!.validate()) {
                    if (etFromYear.getText().toString().trim().isEmpty()) {
                        ToastHelper.makeToast(applicationContext, "From date can't be blank")
                        return@setOnClickListener
                    }
                    if (!checkBox1.isChecked) {
                        if (etToYear.text.toString().trim().isEmpty()) {
                            ToastHelper.makeToast(applicationContext, "To date can't be blank")
                            return@setOnClickListener
                        }
                    }
                    if (checkBox1.isChecked == false && Integer.parseInt(
                            etFromYear.getText().toString()
                        ) == Integer.parseInt(etToYear.text.toString())
                        && seletedFromMonthId <= selectedToMonthId
                    ) {
                        updateWorkingData()
                        saveWork.isEnabled = false
                        save_UP.isEnabled = false
                    } else if (checkBox1.isChecked == false && Integer.parseInt(
                            etFromYear.getText().toString()
                        ) < Integer.parseInt(etToYear.text.toString())
                    ) {
                        updateWorkingData()
                        saveWork.isEnabled = false
                        save_UP.isEnabled = false
                    } else if (checkBox1.isChecked == true) {
                        updateWorkingData()
                        saveWork.isEnabled = false
                        save_UP.isEnabled = false
                    } else
                        ToastHelper.makeToast(
                            applicationContext,
                            "From date should be less than To date"
                        )

                }
            } else {
                if (awesomeValidation_work!!.validate()) {
                    if (etFromYear.getText().toString().trim().isEmpty()) {
                        ToastHelper.makeToast(applicationContext, "From date can't be blank")
                        return@setOnClickListener
                    }
                    if (!checkBox1.isChecked) {
                        if (!checkBox1.isChecked) {
                            if (etToYear.text.toString().trim().isEmpty()) {
                                ToastHelper.makeToast(applicationContext, "To date can't be blank")
                                return@setOnClickListener
                            }
                        }
                    }
                    if (checkBox1.isChecked == false && Integer.parseInt(
                            etFromYear.getText().toString()
                        ) == Integer.parseInt(etToYear.text.toString())
                        && seletedFromMonthId <= selectedToMonthId
                    ) {
                        AddWorkingData()
                        saveWork.isEnabled = false
                        save_UP.isEnabled = false
                    } else if (checkBox1.isChecked == false && Integer.parseInt(
                            etFromYear.getText().toString()
                        ) < Integer.parseInt(etToYear.text.toString())
                    ) {
                        AddWorkingData()
                        saveWork.isEnabled = false
                        save_UP.isEnabled = false
                    } else if (checkBox1.isChecked == true) {
                        AddWorkingData()
                        saveWork.isEnabled = false
                        save_UP.isEnabled = false
                    } else
                        ToastHelper.makeToast(
                            applicationContext,
                            "From date should be less than To date"
                        )
                }
            }
        }

        editwork_back.setOnClickListener {
            finish()
        }

        edittext_uploadimageofyourworkexperience.setOnClickListener {
            showPictureDialog()
        }

        deletethis.setOnClickListener {
            deleteWorkingData()
        }

        edittext_functionalarea.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                edittext_functionalarea.showDropDown()
                return false
            }
        })

        edittext_industry.setOnTouchListener(object: View.OnTouchListener{

            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                edittext_industry.showDropDown()
                return false
            }
        })

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1aw.setAdapter(dataAdapter)
        spinner2aw.setAdapter(dataAdapter)

        val years = ArrayList<String>()
        val thisYear = Calendar.getInstance().get(Calendar.YEAR)
        for (i in 1975..thisYear) {
            years.add(Integer.toString(i))
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, years)

        val spinYearw = findViewById<View>(R.id.spinner1bwork) as Spinner
        spinYearw.adapter = adapter
        val spinYear1w = findViewById<View>(R.id.spinner2bwork) as Spinner
        spinYear1w.adapter = adapter

        if (workId > 0) {
            loadWorkingData()
            header_text.setText("Edit Work Experience")
        } else {
            header_text.setText("Add Work Experience")
        }

        edittext_location.setOnClickListener {
            showLocationDialog()
            awesomeValidation_work!!.clear()
        }

        getCities()

        checkBox1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                llToDate.visibility = View.GONE
            } else
                llToDate.visibility = View.VISIBLE
        }

        spinner2bwork.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                // your code here
                checkSpinnerValidation()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        })
        spinner2awork.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                checkSpinnerValidation()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        })
        spinner1awork.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                checkSpinnerValidation()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        })
        spinner1bwork.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                checkSpinnerValidation()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        })

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

    }


    fun checkSpinnerValidation() {
        if (expirydate.visibility == View.VISIBLE) {
            if (Integer.parseInt(spinner1bwork.selectedItem.toString()) > Integer.parseInt(
                    spinner2bwork.selectedItem.toString()
                )
            ) {
                Toast.makeText(
                    applicationContext,
                    "To date should be greater than From date",
                    Toast.LENGTH_LONG
                ).show()
                //spinner1b.setSelection(spinner1b.getAdapter().getCount()-1)
                spinner2bwork.setSelection(spinner1bwork.getAdapter().getCount() - 1)
                //spinner1a.setSelection(spinner1a.getAdapter().getCount()-1)
                spinner2awork.setSelection(spinner1awork.getAdapter().getCount() - 1)
            } else if (Integer.parseInt(spinner1bwork.selectedItem.toString()) == Integer.parseInt(
                    spinner2bwork.selectedItem.toString()
                )
            ) {
                if (Integer.parseInt(listIntegerWork.get(spinner1awork.selectedItemPosition)) > Integer.parseInt(
                        listIntegerWork.get(spinner2awork.selectedItemPosition)
                    )
                ) {
                    Toast.makeText(
                        applicationContext,
                        "To date should be greater than From date",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    //spinner1b.setSelection(spinner1b.getAdapter().getCount()-1)
                    spinner2bwork.setSelection(spinner1bwork.getAdapter().getCount() - 1)
                    //spinner1a.setSelection(spinner1a.getAdapter().getCount()-1)
                    spinner2awork.setSelection(spinner1awork.getAdapter().getCount() - 1)
                }
            }
        }
    }

    fun getMonthAndYears() {

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

    fun showFromDateDialog(type: String) {

        val dialog = Dialog(this, R.style.AppTheme)
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
        mAdapterMonths = MonthsAdapter(listOfMonths, "Work_Edit")
        monthrecyclerView!!.adapter = mAdapterMonths

        var mAdapterYears: RecyclerView.Adapter<*>? = null

        val mLayoutManagerYears = LinearLayoutManager(applicationContext)
        yearRecyclerView!!.layoutManager = mLayoutManagerYears
        mAdapterYears = YearsAdapter(listOfYears, "Work_Edit")
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

            etFromMonth?.setText(month)

            selectedFromMonth = month

            seletedFromMonthId = monthId

        } else {

            etToMonth?.setText(month)

            selectedToMonth = month

            selectedToMonthId = monthId
        }

    }

    public fun SelectedYear(year: String) {

        if (selectType.equals("From")) {

            etFromYear?.setText(year)

            selectedFromYear = year

        } else {

            etToYear?.setText(year)

            selectedToYear = year
        }

    }


    fun loadWorkingData() {
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.GetWorkingDetails(
            workId,
            "application/json",
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN
        )

        call.enqueue(object : Callback<ViewWorkingResponse> {
            override fun onResponse(
                call: Call<ViewWorkingResponse>,
                response: Response<ViewWorkingResponse>
            ) {
                Logger.d("URL", "" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )
                val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                var jsonarray: JSONArray = jsonObject1.getJSONArray("body")
                Logger.d("URL", "" + "HI OUTSIDE FOR lOOP")
                if (response.isSuccessful) {
                    for (i in 0 until response.body()!!.body!!.size) {

                        Logger.d("URL", "" + "HI OUTSIDE FOR lOOP")
                        var json_objectdetail: JSONObject = jsonarray.getJSONObject(i)
                        edittext_designation.setText(json_objectdetail.getString("designation"))
                        edittext_companyname.setText(json_objectdetail.getString("company_name"))
                        edittext_location.setText(json_objectdetail.getString("location"))
                        edittext_functionalarea.setText(json_objectdetail.getString("functional_area"))
                        edittext_industry.setText(json_objectdetail.getString("functional_industry"))
                        var skilldata: ArrayList<String> = ArrayList()
                        for (k in 0 until response.body()!!.body!![i].skills!!.size) {
                            skilldata.add(json_objectdetail.getJSONArray("skills").getString(k))
                        }
                        if (json_objectdetail.getJSONArray("skills").equals("[]"))
                            edittext_whatskills.setText("")
                        else
                            edittext_whatskills.setText(
                                skilldata.toString().replace("[", "").replace("]", "")
                            )
                        edittext_describeyourworkexperience.setText(json_objectdetail.getString("description"))

                        if (json_objectdetail.getString("current_company").equals("true")) {
                            checkBox1.isChecked = true
                            expirydate.visibility = View.GONE
                            var dates = json_objectdetail.getString("start_date")
                            Log.d("TAGG", dates)
                            dates = dates.split("T").toString()
                            val arrOfStr = dates.split("-")
//                            var dates1 = json_objectdetail.getString("end_date")
//                            dates1 = dates1.split("T").toString()
//                            val arrOfStr1 = dates1.split("-")
//                            spinner1bwork.setSelection(getIndex(spinner1bwork, arrOfStr[0].replace("[", "")));
//                            spinner1awork.setSelection(listIntegerWork.indexOf(arrOfStr[1].replace("[", "")));
                            for (car in listOfMonths) {
                                if (arrOfStr[1].replace("[", "").startsWith("0")) {
                                    if (arrOfStr[1].replace("[", "").substring(1)
                                            .equals(car.monthsId.toString())
                                    ) {
                                        etFromMonth.setText(car.months)
                                        seletedFromMonthId = car.monthsId
                                    }
                                } else {
                                    if (arrOfStr[1].replace("[", "")
                                            .equals(car.monthsId.toString())
                                    ) {
                                        etFromMonth.setText(car.months)
                                        seletedFromMonthId = car.monthsId
                                    }
                                }
                            }
                            etFromYear.setText(arrOfStr[0].replace("[", "").toString())
//                            spinner2bwork.setSelection(getIndex(spinner2bwork, arrOfStr1[0].replace("[", "")));
//                            spinner2awork.setSelection(getIndex(spinner2awork, arrOfStr1[1].replace("[", "")));
                        } else {
                            checkBox1.isChecked = false
                            expirydate.visibility = View.VISIBLE
                            var dates = json_objectdetail.getString("start_date")
                            dates = dates.split("T").toString()
                            val arrOfStr = dates.split("-")
                            var dates1 = json_objectdetail.getString("end_date")
                            dates1 = dates1.split("T").toString()
                            val arrOfStr1 = dates1.split("-")
//                            spinner1bwork.setSelection(getIndex(spinner1bwork, arrOfStr[0].replace("[", "")));
//                            spinner1awork.setSelection(listIntegerWork.indexOf(arrOfStr[1].replace("[", "")));
//                            spinner2bwork.setSelection(getIndex(spinner2bwork, arrOfStr1[0].replace("[", "")));
//                            spinner2awork.setSelection(listIntegerWork.indexOf(arrOfStr1[1].replace("[", "")));
                            for (car in listOfMonths) {
                                if (arrOfStr[1].replace("[", "").startsWith("0")) {
                                    if (arrOfStr[1].replace("[", "").substring(1)
                                            .equals(car.monthsId.toString())
                                    ) {
                                        etFromMonth.setText(car.months)
                                        seletedFromMonthId = car.monthsId
                                    }
                                } else {
                                    if (arrOfStr[1].replace("[", "")
                                            .equals(car.monthsId.toString())
                                    ) {
                                        etFromMonth.setText(car.months)
                                        seletedFromMonthId = car.monthsId
                                    }
                                }
                                if (arrOfStr[1].replace("[", "").startsWith("0")) {
                                    if (arrOfStr1[1].replace("[", "").substring(1)
                                            .equals(car.monthsId.toString())
                                    ) {
                                        etToMonth.setText(car.months)
                                        selectedToMonthId = car.monthsId
                                    }
                                } else {
                                    if (arrOfStr1[1].replace("[", "")
                                            .equals(car.monthsId.toString())
                                    ) {
                                        etToMonth.setText(car.months)
                                        selectedToMonthId = car.monthsId
                                    }
                                }
                            }
                            etFromYear.setText(arrOfStr[0].replace("[", "").toString())
                            etToYear.setText(arrOfStr1[0].replace("[", "").toString())
                        }
                    }
                } else {
                    ToastHelper.makeToast(applicationContext, "message")
                }
            }

            override fun onFailure(call: Call<ViewWorkingResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
//                Toast.makeText(applicationContext,"No Certificates to load!!", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getIndex(spinner: Spinner, myString: String): Int {

        var index = 0

        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i) == myString) {
                index = i
            }
        }
        return index
    }


    private fun showPictureDialog() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setActivityTitle("Working")
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .setCropMenuCropButtonTitle("Done")
            .setMultiTouchEnabled(true)
            .setMinCropWindowSize(538, 320)
            .setFixAspectRatio(false)
            .setAspectRatio(538, 320)
            .setRequestedSize(538, 320)
            .setCropMenuCropButtonIcon(R.drawable.ic_tick)
            .start(this)
//        val pictureDialog = AlertDialog.Builder(this)
//        pictureDialog.setTitle("Select Action")
//        val pictureDialogItems = arrayOf("Select photo from gallery")
//        pictureDialog.setItems(pictureDialogItems
//        ) { dialog, which ->
//            when (which) {
//                0 -> choosePhotoFromGallary()
//                //1 -> takePhotoFromCamera()
//            }
//        }
//        pictureDialog.show()
    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(galleryIntent, GALLERY_IMAGE)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        /* if (resultCode == this.RESULT_CANCELED)
         {
         return
         }*/
        if (requestCode == GALLERY_IMAGE) {
            if (data != null) {
                val contentURI = data!!.data
                try {
                    val f = File(contentURI.toString())
                    picturepath_work = f.getName() + ".png"
                    Log.d("TAGG", picturepath_work.toString())
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
//                    pick_image!!.setImageBitmap(bitmap)
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val b = baos.toByteArray()
                    imageEncoded_work = Base64.encodeToString(b, Base64.DEFAULT)

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(applicationContext, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === Activity.RESULT_OK) {
//                (findViewById(R.id.circleView) as ImageView).setImageURI(result.uri)
                Toast.makeText(this, "Cropping successful", Toast.LENGTH_LONG).show()
//                Picasso.with(applicationContext)
//                    .load(result.uri)
//                    .centerCrop().resize(150,150)
//                    .placeholder(R.drawable.ic_launcher_foreground)
//                    .into(pick_image)
                val contentURI = result.uri
                val f = File(contentURI.toString())
                picturepath_work = f.getName()//+".png"
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
//                pick_image.setPadding(0,0,0,0)
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val b = baos.toByteArray()
                imageEncoded_work = Base64.encodeToString(b, Base64.DEFAULT)

            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.error, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun updateWorkingData() {

        val params = HashMap<String, String>()

        if (picturepath_work.equals("")) {
            var s: String = edittext_whatskills.text.toString().replace("[", "")
            s = s.replace("]", "")
            var value: String = ""
            if (s.contains(",")) {
                var array: ArrayList<String> = s.split(",") as ArrayList<String>

                for (l in 0 until array.size) {
                    value = value + "'" + array[l].toString() + "',"
                }
                value = "[" + value + "]"
            } else {
                value = "['" + s + "']"
            }

            params["designation"] = edittext_designation.text.toString()
            params["company_name"] = edittext_companyname.text.toString()
            params["current_company"] = checkBox1.isChecked.toString()
            params["start_date"] = etFromYear.text.toString() + "-" + seletedFromMonthId + "-01"
//                spinner1bwork.selectedItem.toString()+"-"+ listIntegerWork.get(spinner1awork.selectedItemPosition)+"-01"
            params["end_date"] = etToYear.text.toString() + "-" + selectedToMonthId + "-01"
//                spinner2bwork.selectedItem.toString()+"-"+listIntegerWork.get(spinner2awork.selectedItemPosition)+"-01"
            params["description"] = edittext_describeyourworkexperience.text.toString()
            params["skills"] = value
            params["location"] = edittext_location.text.toString()
            params["default_image"] = "1"
            if(edittext_functionalarea.text.contains(","))
                params["functional_area"] = edittext_functionalarea.text.substring(0,edittext_functionalarea.text.length-2)
            else
                params["functional_area"] = edittext_functionalarea.text.toString()
            if(edittext_industry.text.contains(","))
                params["functional_industry"] = edittext_industry.text.substring(0,edittext_industry.text.length-2)
            else
                params["functional_industry"] = edittext_industry.text.toString()
        } else {
            var s: String = edittext_whatskills.text.toString().replace("[", "")
            s = s.replace("]", "")
            var value: String = ""
            if (s.contains(",")) {
                var array: ArrayList<String> = s.split(",") as ArrayList<String>

                for (l in 0 until array.size) {
                    value = value + "'" + array[l].toString() + "',"
                }
                value = "[" + value + "]"
            } else {
                value = "['" + s + "']"
            }
            params["designation"] = edittext_designation.text.toString()
            params["company_name"] = edittext_companyname.text.toString()
            params["current_company"] = checkBox1.isChecked.toString()
            params["start_date"] = etFromYear.text.toString() + "-" + seletedFromMonthId + "-01"
//                spinner1bwork.selectedItem.toString()+"-"+listIntegerWork.get(spinner1awork.selectedItemPosition)+"-01"
            params["end_date"] = etToYear.text.toString() + "-" + selectedToMonthId + "-01"
//                spinner2bwork.selectedItem.toString()+"-"+listIntegerWork.get(spinner2awork.selectedItemPosition)+"-01"
            params["description"] = edittext_describeyourworkexperience.text.toString()
            params["skills"] = value
            params["location"] = edittext_location.text.toString()
            params["image_filename"] = picturepath_work.toString()
            params["image_file"] = imageEncoded_work
            params["default_image"] = "0"
            if(edittext_functionalarea.text.contains(","))
                params["functional_area"] = edittext_functionalarea.text.substring(0,edittext_functionalarea.text.length-2)
            else
                params["functional_area"] = edittext_functionalarea.text.toString()
            if(edittext_industry.text.contains(","))
                params["functional_industry"] = edittext_industry.text.substring(0,edittext_industry.text.length-2)
            else
                params["functional_industry"] = edittext_industry.text.toString()
            picturepath_work = ""
            imageEncoded_work = ""
        }


        var model: WorkingModel = WorkingModel();
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.UpdateWorkExperienceData(
            workId,
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        Logger.d("URL", "" + params)
        call.enqueue(object : Callback<CreateWorkResponse> {
            override fun onResponse(
                call: Call<CreateWorkResponse>,
                response: Response<CreateWorkResponse>
            ) {
                Logger.d("URL", "" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
                var str_response = Gson().toJson(response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )
                val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                // var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "Updated Successfully!!", Toast.LENGTH_LONG)
                        .show()
                    setResult(1);
                    finish()
                } else {
                    ToastHelper.makeToast(applicationContext, "message")
                }
            }

            override fun onFailure(call: Call<CreateWorkResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext, "Update Failed!!", Toast.LENGTH_LONG).show()

            }
        })
    }

    fun AddWorkingData() {

        val params = HashMap<String, String>()


        if (picturepath_work.equals("")) {
            var s: String = edittext_whatskills.text.toString().replace("[", "")
            s = s.replace("]", "")
            var value: String = ""
            if (s.contains(",")) {
                var array: ArrayList<String> = s.split(",") as ArrayList<String>

                for (l in 0 until array.size) {
                    value = value + "'" + array[l].toString() + "',"
                }
                value = "[" + value + "]"
            } else {
                value = "['" + s + "']"
            }
            params["designation"] = edittext_designation.text.toString()
            params["company_name"] = edittext_companyname.text.toString()
            params["current_company"] = checkBox1.isChecked.toString()
            params["start_date"] = etFromYear.text.toString() + "-" + seletedFromMonthId + "-01"
//                spinner1bwork.selectedItem.toString()+"-"+listIntegerWork.get(spinner1awork.selectedItemPosition)+"-01"
            params["end_date"] = etToYear.text.toString() + "-" + selectedToMonthId + "-01"
//                spinner2bwork.selectedItem.toString()+"-"+listIntegerWork.get(spinner2awork.selectedItemPosition)+"-01"
            params["description"] = edittext_describeyourworkexperience.text.toString()
            params["skills"] = value
            params["location"] = edittext_location.text.toString()
            params["default_image"] = "1"
            if(edittext_functionalarea.text.contains(","))
                params["functional_area"] = edittext_functionalarea.text.substring(0,edittext_functionalarea.text.length-2)
            else
                params["functional_area"] = edittext_functionalarea.text.toString()
            if(edittext_industry.text.contains(","))
                params["functional_industry"] = edittext_industry.text.substring(0,edittext_industry.text.length-2)
            else
                params["functional_industry"] = edittext_industry.text.toString()
        } else {
            var s: String = edittext_whatskills.text.toString().replace("[", "")
            s = s.replace("]", "")
            var value: String = ""
            if (s.contains(",")) {
                var array: ArrayList<String> = s.split(",") as ArrayList<String>

                for (l in 0 until array.size) {
                    value = value + "'" + array[l].toString() + "',"
                }
                value = "[" + value + "]"
            } else {
                value = "['" + s + "']"
            }
            params["designation"] = edittext_designation.text.toString()
            params["company_name"] = edittext_companyname.text.toString()
            params["current_company"] = checkBox1.isChecked.toString()
            params["start_date"] = etFromYear.text.toString() + "-" + seletedFromMonthId + "-01"
//                spinner1bwork.selectedItem.toString()+"-"+listIntegerWork.get(spinner1awork.selectedItemPosition)+"-01"
            params["end_date"] = etToYear.text.toString() + "-" + selectedToMonthId + "-01"
//                spinner2bwork.selectedItem.toString()+"-"+listIntegerWork.get(spinner2awork.selectedItemPosition)+"-01"
            params["description"] = edittext_describeyourworkexperience.text.toString()
            params["skills"] = value
            params["location"] = edittext_location.text.toString()
            params["image_filename"] = picturepath_work.toString()
            params["image_file"] = imageEncoded_work
            params["default_image"] = "0"
            if(edittext_functionalarea.text.contains(","))
                params["functional_area"] = edittext_functionalarea.text.substring(0,edittext_functionalarea.text.length-2)
            else
                params["functional_area"] = edittext_functionalarea.text.toString()
            if(edittext_industry.text.contains(","))
                params["functional_industry"] = edittext_industry.text.substring(0,edittext_industry.text.length-2)
            else
                params["functional_industry"] = edittext_industry.text.toString()
            picturepath_work = ""
            imageEncoded_work = ""
        }


        var model: WorkingModel = WorkingModel();
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.AddWorkExperienceData(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        Logger.d("URL", "" + params)
        call.enqueue(object : Callback<CreateWorkResponse> {
            override fun onResponse(
                call: Call<CreateWorkResponse>,
                response: Response<CreateWorkResponse>
            ) {
                Logger.d("URL", "" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
                var str_response = Gson().toJson(response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )
                val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                // var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "Added successfully!!", Toast.LENGTH_LONG)
                        .show()
                    setResult(1);
                    finish()
                } else {
                    ToastHelper.makeToast(applicationContext, "message")
                }

            }

            override fun onFailure(call: Call<CreateWorkResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext, "Adding Failed!!", Toast.LENGTH_LONG).show()

            }
        })
    }

    fun deleteWorkingData() {


        var model: WorkingModel = WorkingModel();
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.DeleteWorkExperienceData(
            workId,
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN
        )
        call.enqueue(object : Callback<DeletePostResponse> {
            override fun onResponse(
                call: Call<DeletePostResponse>,
                response: Response<DeletePostResponse>
            ) {
                Logger.d("URL", "" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
                var str_response = Gson().toJson(response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )
                val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                // var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "Deleted Successfully!!", Toast.LENGTH_LONG)
                        .show()
                    setResult(1);
                    finish()
                } else {
                    ToastHelper.makeToast(applicationContext, "message")
                }

            }

            override fun onFailure(call: Call<DeletePostResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext, "Update Failed!!", Toast.LENGTH_LONG).show()

            }
        })
    }

    fun showLocationDialog() {

        val dialog = Dialog(this, R.style.AppTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.activity_locations)
        dialog.show()


        var expandableAdapter: com.jobsforher.adapters.CitiesExpandableAdapter1
        val parents = arrayOf(
            "Top Metropolitan Cities",
            "Other Cities",
            "Other Country Cities",
            "Other Countries"
        )
        val childList: ArrayList<ArrayList<Cities>>

//        val recyclerView = dialog.findViewById(R.id.locationRecyclerView) as RecyclerView
        val closeDialog = dialog.findViewById(R.id.closeDialog) as ImageView
        val tvSave = dialog.findViewById(R.id.tvSave) as TextView
        val etAlertLocation = dialog.findViewById(R.id.etAlertLocation) as EditText
        val etsearch: EditText = dialog.findViewById(R.id.etAlertLocation) as EditText

        val expList = dialog.findViewById(R.id.locationRecyclerView) as ExpandableListView

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
        listDataHeader.add("Top Metropolitan Cities")
        listDataHeader.add("Other Cities")
        listDataHeader.add("Other Country Cities")
        listDataHeader.add("Other Countries")

        val listDataChild = HashMap<String, List<Cities>>()
        listDataChild.put(listDataHeader.get(0), listOfTopMetropolitan); // Header, Child data
        listDataChild.put(listDataHeader.get(1), listOfOtherCities);
        listDataChild.put(listDataHeader.get(2), listOfOtherCountryCities);
        listDataChild.put(listDataHeader.get(3), listOfOtherCountries)

//        expandableAdapter = CitiesExpandableAdapter1(this, childList, parents)
        expandableAdapter =
            com.jobsforher.adapters.CitiesExpandableAdapter1(
                this,
                listDataHeader,
                listDataChild
            )
        expList.setAdapter(expandableAdapter)
        val listDataHeader1 = ArrayList<String>()
        val listDataChild1 = HashMap<String, List<Cities>>()
        expList.expandGroup(0)

        expList.setOnChildClickListener(object : ExpandableListView.OnChildClickListener {

            override fun onChildClick(
                parent: ExpandableListView, v: View,
                groupPosition: Int, childPosition: Int, id: Long
            ): Boolean {

                if (etAlertLocation.text.length > 0) {
                    val cityy: String =
                        listDataChild1.get(listDataHeader1.get(groupPosition))!!
                            .get(childPosition).name!!
                    val id: Int = listDataChild1.get(listDataHeader1.get(groupPosition))!!
                        .get(childPosition).id
                    etAlertLocation.setText("")
                    etAlertLocation.setText(cityy)

                    Toast.makeText(
                        applicationContext,
                        listDataHeader.get(groupPosition)
                                + " : "
                                + cityy, Toast.LENGTH_SHORT
                    )
                        .show()

                    SelectedCity(
                        id,
                        cityy
                    )
                } else {
                    val cityy: String =
                        listDataChild.get(listDataHeader.get(groupPosition))!!
                            .get(childPosition).name!!
                    val id: Int =
                        listDataChild.get(listDataHeader.get(groupPosition))!!.get(childPosition).id
                    etAlertLocation.setText("")
                    etAlertLocation.setText(cityy)

                    Toast.makeText(
                        applicationContext,
                        listDataHeader.get(groupPosition)
                                + " : "
                                + cityy, Toast.LENGTH_SHORT
                    )
                        .show()

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
                listDataHeader1.add("Top Metropolitan Cities")
                listDataHeader1.add("Other Cities")
                listDataHeader1.add("Other Country Cities")
                listDataHeader1.add("Other Countries")


                listDataChild1.put(
                    listDataHeader1.get(0),
                    listOfTopMetropolitanCompare
                ); // Header, Child data
                listDataChild1.put(listDataHeader1.get(1), listOfOtherCitiesCompare);
                listDataChild1.put(listDataHeader1.get(2), listOfOtherCountryCitiesCompare);
                listDataChild1.put(listDataHeader1.get(3), listOfOtherCountriesCompare)

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

            if (etAlertLocation.length() > 0) {

                val editDegree = etAlertLocation.text

                selectedCity = editDegree.toString()

                etLocation?.setText(selectedCity)

            }
            dialog.dismiss()

        }

    }


    public fun SelectedCity(id: Int, city: String) {

        edittext_location?.setText(city)

        selectedCity = city

        selectedId = id

    }

    fun getCities() {

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call =
            retrofitInterface!!.GetCities(EndPoints.CLIENT_ID, "Bearer " + EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<CitiesListResponse> {
            override fun onResponse(
                call: Call<CitiesListResponse>,
                response: Response<CitiesListResponse>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    EndPoints.ACCESS_TOKEN = response.body()!!.auth!!.accessToken.toString()

                    val gson = GsonBuilder().serializeNulls().create()
                    var str_response = gson.toJson(response)

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

                        val model: Cities = Cities();

                        model.id = json_objectdetail.getInt("id")
                        model.name = json_objectdetail.getString("label")
                        if (json_objectdetail.getInt("ordinal") == 0) {
                            listOfTopMetropolitan.add(
                                Cities(model.id, model.name!!)
                            )
                        } else if (json_objectdetail.getInt("ordinal") == 1) {
                            listOfOtherCities.add(
                                Cities(model.id, model.name!!)
                            )
                        } else if (json_objectdetail.getInt("ordinal") == 2) {
                            listOfOtherCountryCities.add(
                                Cities(model.id, model.name!!)
                            )
                        } else if (json_objectdetail.getInt("ordinal") == 3) {
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

                addJobFunctionalArea()
            }

            override fun onFailure(call: Call<CitiesListResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })
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

                        setFA()
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

    }

    fun setFA(){

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


        //        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, functionalareaList)
        //        functionalarea.setAdapter(adapter)
        //        functionalarea.setThreshold(1)
        //        functionalarea.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, functionalareaList)
        edittext_functionalarea.setAdapter(adapter)
        edittext_functionalarea.setThreshold(1)
        edittext_functionalarea.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

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
                        setInd()
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

    }

    fun setInd(){
        Collections.sort(listOfJobIndustry, object : Comparator<FunctionalAreaView> {
            override fun compare(lhs: FunctionalAreaView, rhs: FunctionalAreaView): Int {
                return lhs.name!!.compareTo(rhs.name!!)
            }
        })

        val industryList: ArrayList<String> = ArrayList()

        for (model in listOfJobIndustry) {
            industryList.add(model.name!!.toString())
        }

//        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, industryList)
//        industry.setAdapter(adapter)
//        industry.setThreshold(1)
//        industry.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, industryList)
        edittext_industry.setAdapter(adapter)
        edittext_industry.setThreshold(1)
        edittext_industry.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
    }


}