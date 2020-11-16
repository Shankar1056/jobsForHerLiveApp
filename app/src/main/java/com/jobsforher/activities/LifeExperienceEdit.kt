package com.jobsforher.activities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
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
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_selecting_location.*
import kotlinx.android.synthetic.main.layout_lifeexperienceedit.*
import kotlinx.android.synthetic.main.layout_lifeexperienceedit.deletethis
import kotlinx.android.synthetic.main.layout_lifeexperienceedit.expirydate
import kotlinx.android.synthetic.main.layout_lifeexperienceedit.header_text
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.jobsforher.R
import com.jobsforher.adapters.MonthsAdapter
import com.jobsforher.adapters.YearsAdapter
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.models.Cities
import com.jobsforher.models.LifeModel
import com.jobsforher.models.Months
import com.jobsforher.models.Years
import com.jobsforher.network.responsemodels.CitiesListResponse
import com.jobsforher.network.responsemodels.DeletePostResponse
import com.jobsforher.network.responsemodels.UpdateLifeResponse
import com.jobsforher.network.responsemodels.ViewLifeResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import women.jobs.jobsforher.activities.BaseActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


private var retrofitInterface: RetrofitInterface? = null
var lifeId = 0
private val GALLERY_IMAGE = 1
public var picturepath_life = ""
public var imageEncoded_life = "";
val listIntegerLife= ArrayList<String>()

class LifeExperienceEdit : BaseActivity() {

    val list1= ArrayList<String>()
    val listcaption= ArrayList<String>()
    private var awesomeValidation_life: AwesomeValidation? = null

    var listOfMonths : ArrayList<Months> = ArrayList()
    var listOfYears : ArrayList<Years> = ArrayList()
    var selectedFromMonth : String = ""
    var selectedToMonth : String = ""
    var selectedFromYear : String = ""
    var selectedToYear : String = ""
    var seletedFromMonthId : Int = 0
    var selectedToMonthId : Int = 0
    var selectType : String = ""

    internal var textlength = 0
    var listOfTopMetropolitan: ArrayList<Cities> = ArrayList()
    var listOfOtherCities: ArrayList<Cities> = ArrayList()
    var listOfOtherCountryCities: ArrayList<Cities> = ArrayList()
    var listOfOtherCountries: ArrayList<Cities> = ArrayList()

    var listOfTopMetropolitanCompare: ArrayList<Cities> = ArrayList()
    var listOfOtherCitiesCompare: ArrayList<Cities> = ArrayList()
    var listOfOtherCountryCitiesCompare: ArrayList<Cities> = ArrayList()
    var listOfOtherCountriesCompare: ArrayList<Cities> = ArrayList()

    var selectedId : Int = 0
    var selectedCity : String = ""
    var captionValue: String = ""
    private var awesomeValidation_lif: AwesomeValidation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_lifeexperienceedit)
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        lifeId = intent.getIntExtra("ID",0)
        editlife_back.setOnClickListener{
            this.finish()
        }

        awesomeValidation_lif = AwesomeValidation(ValidationStyle.BASIC);
        //awesomeValidation_lif!!.addValidation(edittext_typeoflifeexperience, RegexTemplate.NOT_EMPTY,"Please enter the life experience")
        awesomeValidation_lif!!.addValidation(edittext_caption, RegexTemplate.NOT_EMPTY,"Please enter the caption");
        awesomeValidation_lif!!.addValidation(edittext_whatskillslife, RegexTemplate.NOT_EMPTY,"Please enter atleast one skill");
        awesomeValidation_lif!!.addValidation(edittext_locationlife, RegexTemplate.NOT_EMPTY,"Please enter the location");

        getMonthAndYears()


        Log.d("TAGG", "ID iS"+ lifeId)
        val spinnertype = findViewById<View>(R.id.edittext_typeoflifeexperience) as Spinner
        list1.add("Motherhood")
        list1.add("Elderly-Care")
        list1.add("Health")
        list1.add("Travel")
        list1.add("Marriage")
        list1.add("Relocation")

        listcaption.add("Boss Mom")
        listcaption.add("Constant Custodian")
        listcaption.add("Return of the Warrior")
        listcaption.add("Adventuress")
        listcaption.add("New Beginnings")
        listcaption.add("New discoveries")

        var dataAdapter1 = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item  , list1)

        spinnertype.setAdapter(dataAdapter1)
        spinnertype.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                // your code here
                if(captionValue.equals("")){
                    edittext_caption.setText(listcaption.get(position).toString())
                }
                else{
                    captionValue = ""
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        })
        edittext_caption.setText(listcaption.get(0).toString())

        val spinner1al = findViewById<View>(R.id.spinner1alife) as Spinner
        val spinner2al = findViewById<View>(R.id.spinner2alife) as Spinner
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

        listIntegerLife.add("01")
        listIntegerLife.add("02")
        listIntegerLife.add("03")
        listIntegerLife.add("04")
        listIntegerLife.add("05")
        listIntegerLife.add("06")
        listIntegerLife.add("07")
        listIntegerLife.add("08")
        listIntegerLife.add("09")
        listIntegerLife.add("10")
        listIntegerLife.add("11")
        listIntegerLife.add("12")
        val dataAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1, listMonths
        )

        saveLife.setOnClickListener {

            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus()!!.getWindowToken(), 0)
            }
            if (lifeId>0) {
                if (awesomeValidation_lif!!.validate()) {
                    if (etFromMonth.length() > 0 && etFromYear.length() > 0) {
                        if (checkBox2.isChecked == false && Integer.parseInt(etFromYear.getText().toString()) == Integer.parseInt(
                                etToYear.text.toString()
                            )
                            && seletedFromMonthId <= selectedToMonthId
                        ) {
                            updateLifeData()
                            save_life_up.isEnabled = false
                            saveLife.isEnabled = false
                        } else if (checkBox2.isChecked == false && Integer.parseInt(etFromYear.getText().toString()) < Integer.parseInt(
                                etToYear.text.toString()
                            )
                        ) {
                            updateLifeData()
                            save_life_up.isEnabled = false
                            saveLife.isEnabled = false
                        } else if (checkBox2.isChecked == true) {
                            updateLifeData()
                            save_life_up.isEnabled = false
                            saveLife.isEnabled = false
                        } else
                            ToastHelper.makeToast(applicationContext, "From date should be less than To date")
                    } else {

                        ToastHelper.makeToast(applicationContext, "Please enter the dates")

                    }
                }

            }
            else {
                if (awesomeValidation_lif!!.validate()) {
                    if (etFromMonth.length() > 0 && etFromYear.length() > 0) {
                        if (checkBox2.isChecked == false && Integer.parseInt(etFromYear.getText().toString()) == Integer.parseInt(
                                etToYear.text.toString()
                            )
                            && seletedFromMonthId <= selectedToMonthId
                        ) {
                            AddLifeData()
                            save_life_up.isEnabled = false
                            saveLife.isEnabled = false
                        } else if (checkBox2.isChecked == false && Integer.parseInt(etFromYear.getText().toString()) < Integer.parseInt(
                                etToYear.text.toString()
                            )
                        ) {
                            AddLifeData()
                            save_life_up.isEnabled = false
                            saveLife.isEnabled = false
                        } else if (checkBox2.isChecked == true) {
                            AddLifeData()
                            save_life_up.isEnabled = false
                            saveLife.isEnabled = false
                        } else
                            ToastHelper.makeToast(applicationContext, "From date should be less than To date")
                    } else {
                        ToastHelper.makeToast(applicationContext, "Please enter the dates")
                    }
                }

            }

        }

        save_life_up.setOnClickListener {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (getCurrentFocus() != null){
                imm.hideSoftInputFromWindow(getCurrentFocus()!!.getWindowToken(), 0)
            }
            if (lifeId>0) {
                if (awesomeValidation_lif!!.validate()) {
                    if (etFromMonth.length() > 0 && etFromYear.length() > 0) {
                        if (checkBox2.isChecked == false && Integer.parseInt(etFromYear.getText().toString()) == Integer.parseInt(
                                etToYear.text.toString()
                            )
                            && seletedFromMonthId <= selectedToMonthId
                        ) {
                            updateLifeData()
                            save_life_up.isEnabled = false
                            saveLife.isEnabled = false
                        } else if (checkBox2.isChecked == false && Integer.parseInt(etFromYear.getText().toString()) < Integer.parseInt(
                                etToYear.text.toString()
                            )
                        ) {
                            updateLifeData()
                            save_life_up.isEnabled = false
                            saveLife.isEnabled = false
                        } else if (checkBox2.isChecked == true) {
                            updateLifeData()
                            save_life_up.isEnabled = false
                            saveLife.isEnabled = false
                        } else
                            ToastHelper.makeToast(applicationContext, "From date should be less than To date")
                    } else {

                        ToastHelper.makeToast(applicationContext, "Please enter the dates")

                    }
                }
            }
            else {
                if (awesomeValidation_lif!!.validate()) {
                    if (etFromMonth.length() > 0 && etFromYear.length() > 0) {
                        if (checkBox2.isChecked == false && Integer.parseInt(etFromYear.getText().toString()) == Integer.parseInt(
                                etToYear.text.toString()
                            )
                            && seletedFromMonthId <= selectedToMonthId
                        ) {
                            AddLifeData()
                            save_life_up.isEnabled = false
                            saveLife.isEnabled = false
                        } else if (checkBox2.isChecked == false && Integer.parseInt(etFromYear.getText().toString()) < Integer.parseInt(
                                etToYear.text.toString()
                            )
                        ) {
                            AddLifeData()
                            save_life_up.isEnabled = false
                            saveLife.isEnabled = false
                        } else if (checkBox2.isChecked == true) {
                            AddLifeData()
                            save_life_up.isEnabled = false
                            saveLife.isEnabled = false
                        } else
                            ToastHelper.makeToast(applicationContext, "From date should be less than To date")
                    } else {
                        ToastHelper.makeToast(applicationContext, "Please enter the dates")
                    }
                }
            }
        }
        getCities()

        edittext_uploadimageofyourlifeexperience.setOnClickListener {
            showPictureDialog()
        }
        editlife_back.setOnClickListener {
            finish()
        }

        deletethis.setOnClickListener {
            deleteLifeData()
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

        spinner2blife.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                // your code here
                checkSpinnerValidation()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        })
        spinner2alife.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                checkSpinnerValidation()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        })
        spinner1alife.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                checkSpinnerValidation()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        })
        spinner1blife.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                checkSpinnerValidation()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        })

        edittext_locationlife.setOnClickListener {
            showLocationDialog()
            awesomeValidation_lif!!.clear()
        }

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1al.setAdapter(dataAdapter)
        spinner2al.setAdapter(dataAdapter)

        val years = ArrayList<String>()
        val thisYear = Calendar.getInstance().get(Calendar.YEAR)
        for (i in 1975..thisYear) {
            years.add(Integer.toString(i))
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, years)

        val spinYearl = findViewById<View>(R.id.spinner1blife) as Spinner
        spinYearl.adapter = adapter
        val spinYear1l = findViewById<View>(R.id.spinner2blife) as Spinner
        spinYear1l.adapter = adapter

        if (lifeId>0) {
            loadLifeData()
            header_text.setText("Edit Life Experience")
        }
        else{
            header_text.setText("Add Life Experience")
        }

        checkBox2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                llToDate.visibility = View.GONE
            }
            else
                llToDate.visibility = View.VISIBLE
        }

    }

    fun checkSpinnerValidation(){
        if (expirydate.visibility == View.VISIBLE){
            if (Integer.parseInt(spinner1blife.selectedItem.toString()) > Integer.parseInt(spinner2blife.selectedItem.toString())) {
                Toast.makeText(applicationContext, "To date should be greater than From date", Toast.LENGTH_LONG).show()
                //spinner1b.setSelection(spinner1b.getAdapter().getCount()-1)
                spinner2blife.setSelection(spinner1blife.getAdapter().getCount()-1)
                //spinner1a.setSelection(spinner1a.getAdapter().getCount()-1)
                spinner2alife.setSelection(spinner1alife.getAdapter().getCount()-1)
            }
            else if(Integer.parseInt(spinner1blife.selectedItem.toString()) == Integer.parseInt(spinner2blife.selectedItem.toString())){
                if (Integer.parseInt(listIntegerLife.get(spinner1alife.selectedItemPosition)) > Integer.parseInt(listIntegerLife.get(spinner2alife.selectedItemPosition))) {
                    Toast.makeText(applicationContext, "To date should be greater than From date", Toast.LENGTH_LONG)
                        .show()
                    //spinner1b.setSelection(spinner1b.getAdapter().getCount()-1)
                    spinner2blife.setSelection(spinner1blife.getAdapter().getCount()-1)
                    //spinner1a.setSelection(spinner1a.getAdapter().getCount()-1)
                    spinner2alife.setSelection(spinner1alife.getAdapter().getCount()-1)
                }
            }
        }
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

        if (type.equals("From")){

            tvTitle.setText("From Date")
        }else{

            tvTitle.setText("To Date")
        }

        var mAdapterMonths: RecyclerView.Adapter<*>? = null

        val mLayoutManagerMonth = LinearLayoutManager(applicationContext)
        monthrecyclerView!!.layoutManager = mLayoutManagerMonth
        mAdapterMonths = MonthsAdapter(listOfMonths, "Life_Edit")
        monthrecyclerView!!.adapter = mAdapterMonths

        var mAdapterYears: RecyclerView.Adapter<*>? = null

        val mLayoutManagerYears = LinearLayoutManager(applicationContext)
        yearRecyclerView!!.layoutManager = mLayoutManagerYears
        mAdapterYears = YearsAdapter(listOfYears, "Life_Edit")
        yearRecyclerView!!.adapter = mAdapterYears

        closeDialog.setOnClickListener {

            dialog.dismiss()
        }

        tvSave.setOnClickListener {

            dialog.dismiss()

        }
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


    fun showLocationDialog() {

        val dialog = Dialog(this, R.style.AppTheme)
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
        val etsearch: EditText = dialog.findViewById(R.id.etAlertLocation) as EditText

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

            override fun onChildClick(parent: ExpandableListView, v:View,
                                      groupPosition:Int, childPosition:Int, id:Long):Boolean {

                if (etAlertLocation.text.length>0) {
                    val cityy: String =
                        listDataChild1.get(listDataHeader1.get(groupPosition))!!.get(childPosition).name!!
                    val id: Int = listDataChild1.get(listDataHeader1.get(groupPosition))!!.get(childPosition).id
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
                else{
                    val cityy: String =
                        listDataChild.get(listDataHeader.get(groupPosition))!!.get(childPosition).name!!
                    val id: Int = listDataChild.get(listDataHeader.get(groupPosition))!!.get(childPosition).id
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


    public fun SelectedCity(id : Int, city : String){

        edittext_locationlife?.setText(city)

        selectedCity = city

        selectedId = id

    }



    fun loadLifeData(){
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.GetLifeExperienceDetails(lifeId, "application/json",EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<ViewLifeResponse> {
            override fun onResponse(call: Call<ViewLifeResponse>, response: Response<ViewLifeResponse>) {
                Logger.d("URL", "" + response+ EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
                var str_response = Gson().toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                val jsonObject1 : JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                var jsonarray: JSONArray = jsonObject1.getJSONArray("body")
                Logger.d("URL", "" + "HI OUTSIDE FOR lOOP")
                if(response.isSuccessful){
                    for (i in 0 until response.body()!!.body!!.size) {

                        Logger.d("URL", "" + "HI OUTSIDE FOR lOOP")
                        var json_objectdetail: JSONObject = jsonarray.getJSONObject(i)
                        var skilldata1: ArrayList<String> = ArrayList()
                        for (k in 0 until response.body()!!.body!![i].life_experience!!.size) {
                            skilldata1.add(json_objectdetail.getJSONArray("life_experience").getString(k))
                        }

                        edittext_typeoflifeexperience.setSelection(getIndex(edittext_typeoflifeexperience, skilldata1.toString().replace("[","").replace("]","")))
//                        edittext_typeoflifeexperience.setText(skilldata1.toString().replace("[","").replace("]",""))
                        edittext_caption.setText(json_objectdetail.getString("caption"))
                        captionValue = json_objectdetail.getString("caption")
                        Toast.makeText(applicationContext,json_objectdetail.getString("caption"),Toast.LENGTH_LONG).show()
                        //edittext_whycareerbreak.setText(json_objectdetail.getBoolean("career_break").toString())
                        edittext_whycareerbreak.isChecked = json_objectdetail.getBoolean("career_break")
                        edittext_locationlife.setText(json_objectdetail.getString("location"))
                        edittext_describeyourlifeexperience.setText(json_objectdetail.getString("location"))
                        if (json_objectdetail.getString("ongoing").equals("true")) {
                            checkBox2.isChecked = true
                            expirydate.visibility = View.GONE
                            var dates = json_objectdetail.getString("start_date")
                            Log.d("TAGG", dates)
                            dates = dates.split("T").toString()
                            val arrOfStr = dates.split("-")
//                            var dates1 = json_objectdetail.getString("end_date")
//                            dates1 = dates1.split("T").toString()
//                            val arrOfStr1 = dates1.split("-")
//                            spinner1blife.setSelection(getIndex(spinner1blife, arrOfStr[0].replace("[", "")));
//                            spinner1alife.setSelection(listIntegerLife.indexOf(arrOfStr[1].replace("[", "")));
                            for (car in listOfMonths) {
                                if (arrOfStr[1].replace("[", "").startsWith("0")) {
                                    if (arrOfStr[1].replace("[", "").substring(1).equals(car.monthsId.toString())) {
                                        etFromMonth.setText(car.months)
                                        seletedFromMonthId = car.monthsId
                                    }
                                }
                                else{
                                    if (arrOfStr[1].replace("[", "").equals(car.monthsId.toString())) {
                                        etFromMonth.setText(car.months)
                                        seletedFromMonthId = car.monthsId
                                    }
                                }
                            }
                            etFromYear.setText(arrOfStr[0].replace("[", "").toString())
//                            spinner2blife.setSelection(getIndex(spinner2blife, arrOfStr1[0].replace("[", "")));
//                            spinner2alife.setSelection(getIndex(spinner2alife, arrOfStr1[1].replace("[", "")));
                        }
                        else{
                            checkBox2.isChecked = false
                            expirydate.visibility = View.VISIBLE
                            var dates = json_objectdetail.getString("start_date")
                            dates = dates.split("T").toString()
                            val arrOfStr = dates.split("-")
                            var dates1 = json_objectdetail.getString("end_date")
                            dates1 = dates1.split("T").toString()
                            val arrOfStr1 = dates1.split("-")
//                            spinner1blife.setSelection(getIndex(spinner1blife, arrOfStr[0].replace("[", "")));
//                            spinner1alife.setSelection(listIntegerLife.indexOf(arrOfStr[1].replace("[", "")));
//                            spinner2blife.setSelection(getIndex(spinner2blife, arrOfStr1[0].replace("[", "")));
//                            spinner2alife.setSelection(listIntegerLife.indexOf(arrOfStr1[1].replace("[", "")));
                            for (car in listOfMonths) {
                                if (arrOfStr[1].replace("[", "").startsWith("0")) {
                                    if (arrOfStr[1].replace("[", "").substring(1).equals(car.monthsId.toString())) {
                                        etFromMonth.setText(car.months)
                                        seletedFromMonthId = car.monthsId
                                    }
                                }
                                else{
                                    if (arrOfStr[1].replace("[", "").equals(car.monthsId.toString())) {
                                        etFromMonth.setText(car.months)
                                        seletedFromMonthId = car.monthsId
                                    }
                                }
                                if (arrOfStr[1].replace("[", "").startsWith("0")) {
                                    if (arrOfStr1[1].replace("[", "").substring(1).equals(car.monthsId.toString())) {
                                        etToMonth.setText(car.months)
                                        selectedToMonthId = car.monthsId
                                    }
                                }
                                else{
                                    if (arrOfStr1[1].replace("[", "").equals(car.monthsId.toString())) {
                                        etToMonth.setText(car.months)
                                        selectedToMonthId = car.monthsId
                                    }
                                }
                            }
                            etFromYear.setText(arrOfStr[0].replace("[", "").toString())
                            etToYear.setText(arrOfStr1[0].replace("[", "").toString())
                        }
                        var skilldata: ArrayList<String> = ArrayList()
                        for (k in 0 until response.body()!!.body!![i].skills!!.size) {
                            skilldata.add(json_objectdetail.getJSONArray("skills").getString(k))
                        }
                        if (json_objectdetail.getJSONArray("skills").equals("[]"))
                            edittext_whatskillslife.setText("")
                        else
                            edittext_whatskillslife.setText(skilldata.toString().replace("[","").replace("]",""))
                        edittext_describeyourlifeexperience.setText(json_objectdetail.getString("description"))
                    }
                }
                else {
                    ToastHelper.makeToast(applicationContext, "message")
                }
            }
            override fun onFailure(call: Call<ViewLifeResponse>, t: Throwable) {
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
            .setActivityTitle("Life Experience")
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .setCropMenuCropButtonTitle("Done")
            .setFixAspectRatio(false)
            .setAutoZoomEnabled(true)
            .setMultiTouchEnabled(true)
            .setMinCropWindowSize(538,320)
            .setAspectRatio(538,320)
            .setRequestedSize(538, 320)
            .setCropMenuCropButtonIcon(R.drawable.ic_tick)
            .start(this)

    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY_IMAGE)
    }

    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        /* if (resultCode == this.RESULT_CANCELED)
         {
         return
         }*/
        if (requestCode == GALLERY_IMAGE)
        {
            if (data != null)
            {
                val contentURI = data!!.data
                try
                {
                    val f = File(contentURI.toString())
                    picturepath_life = f.getName()+".png"
                    Log.d("TAGG", picturepath_life.toString())
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
//                    pick_image!!.setImageBitmap(bitmap)
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val b = baos.toByteArray()
                    imageEncoded_life = Base64.encodeToString(b, Base64.DEFAULT)

                }
                catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(applicationContext, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === Activity.RESULT_OK) {
//                (findViewById(R.id.circleView) as ImageView).setImageURI(result.uri)
                Toast.makeText(this, "Cropping successful" , Toast.LENGTH_LONG).show()
                val contentURI = result.uri
                val f = File(contentURI.toString())
                picturepath_life = f.getName()//+".png"

                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
//                pick_image.setPadding(0,0,0,0)
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val b = baos.toByteArray()
                imageEncoded_life = Base64.encodeToString(b, Base64.DEFAULT)

            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.error, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun updateLifeData(){

        val params = HashMap<String, String>()


        if (picturepath_life.equals("")){
            var s: String = edittext_whatskillslife.text.toString().replace("[","")
            s = s.replace("]","")
            var value: String = ""
            if (s.contains(",")) {
                var array: ArrayList<String> = s.split(",") as ArrayList<String>

                for (l in 0 until array.size) {
                    value = value+"'" + array[l].toString() + "',"
                }
                value = "[" + value + "]"
            }else
                value = "['"+s+"']"

            var value1: String=""
            var s1: String = edittext_typeoflifeexperience.selectedItem.toString().replace("[","")
            s1 = s1.replace("]","")
            if (s1.contains(",")) {
                var array1: ArrayList<String> = s1.split(",") as ArrayList<String>

                for (l in 0 until array1.size) {
                    value1 = value1+"'" + array1[l].toString() + "',"
                }
                value1 = "[" + value1 + "]"
            }else
                value1 = "['"+s1+"']"

            params["life_experience"] = value1
            params["start_date"] = etFromYear.text.toString()+"-"+seletedFromMonthId+"-01"
            //spinner1blife.selectedItem.toString()+"-"+ listIntegerLife.get(spinner1alife.selectedItemPosition)+"-01"
            params["ongoing"] = checkBox2.isChecked.toString()
//            if (!checkBox2.isChecked) {
            params["end_date"] =etToYear.text.toString()+"-"+ selectedToMonthId+"-01"
            //spinner2blife.selectedItem.toString() + "-" + listIntegerLife.get(spinner2alife.selectedItemPosition) + "-01"
//            }
            params["career_break"] = edittext_whycareerbreak.isChecked.toString()
            params["description"] = edittext_describeyourlifeexperience.text.toString()
            params["skills"] = value
            params["location"]  =edittext_locationlife.text.toString()
            params["caption"]  =edittext_caption.text.toString()
            params["default_image"] = "1"
        }
        else{
            var s: String = edittext_whatskillslife.text.toString().replace("[","")
            s = s.replace("]","")
            var value: String = ""
            if (s.contains(",")) {
                var array: ArrayList<String> = s.split(",") as ArrayList<String>

                for (l in 0 until array.size) {
                    value = value+"'" + array[l].toString() + "',"
                }
                value = "[" + value + "]"
            }else
                value = "['"+s+"']"

            var value1: String=""
            var s1: String = edittext_typeoflifeexperience.selectedItem.toString().replace("[","")
            s1 = s1.replace("]","")
            if (s1.contains(",")) {
                var array1: ArrayList<String> = s1.split(",") as ArrayList<String>

                for (l in 0 until array1.size) {
                    value1 = value1+"'" + array1[l].toString() + "',"
                }
                value1 = "[" + value1 + "]"
            }else
                value1 = "['"+s1+"']"


            params["life_experience"] = value1
            params["start_date"] = etFromYear.text.toString()+"-"+seletedFromMonthId+"-01"
//                spinner1blife.selectedItem.toString()+"-"+listIntegerLife.get(spinner1alife.selectedItemPosition)+"-01"
            params["ongoing"] = checkBox2.isChecked.toString()
//            if (!checkBox2.isChecked) {
            params["end_date"] =etToYear.text.toString()+"-"+ selectedToMonthId+"-01"
            //spinner2blife.selectedItem.toString() + "-" + listIntegerLife.get(spinner2alife.selectedItemPosition)+ "-01"
//            }
            params["career_break"] = edittext_whycareerbreak.isChecked.toString()
            params["description"] = edittext_describeyourlifeexperience.text.toString()
            params["skills"] = value
            params["location"]  =edittext_locationlife.text.toString()
            params["caption"]  =edittext_caption.text.toString()
            params["image_filename"] = picturepath_life.toString()
            params["image_file"] = imageEncoded_life
            params["default_image"] = "0"
            picturepath_life=""
            imageEncoded_life=""
        }

        Logger.d("TAGG", "PARAMS: "+params)
        var model: LifeModel = LifeModel();
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.UpdateLifeExperienceData(lifeId,EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        Logger.d("URL", "" + params)
        call.enqueue(object : Callback<UpdateLifeResponse> {
            override fun onResponse(call: Call<UpdateLifeResponse>, response: Response<UpdateLifeResponse>) {
                Logger.d("URL", "" + response+ EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
                var str_response = Gson().toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                val jsonObject1 : JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                // var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                if(response.isSuccessful){
                    Toast.makeText(applicationContext,"Updated Successfully!!", Toast.LENGTH_LONG).show()
                    setResult(1);
                    finish()
                }
                else {
                    ToastHelper.makeToast(applicationContext, "message")
                }

            }
            override fun onFailure(call: Call<UpdateLifeResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext,"Update Failed!!", Toast.LENGTH_LONG).show()

            }
        })
    }

    fun AddLifeData(){

        val params = HashMap<String, String>()

        if (picturepath_life.equals("")){
            var s: String = edittext_whatskillslife.text.toString().replace("[","")
            s = s.replace("]","")
            var value: String = ""
            if (s.contains(",")) {
                var array: ArrayList<String> = s.split(",") as ArrayList<String>

                for (l in 0 until array.size) {
                    value = "'" + array[l].toString() + "',"
                }
                value = "[" + value + "]"
            }else
                value = "['"+s+"']"

            var value1: String=""
            var s1: String = edittext_typeoflifeexperience.selectedItem.toString().replace("[","")
            s1 = s1.replace("]","")
            if (s1.contains(",")) {
                var array1: ArrayList<String> = s1.split(",") as ArrayList<String>

                for (l in 0 until array1.size) {
                    value1 = "'" + array1[l].toString() + "',"
                }
                value1 = "['" + value1 + "']"
            }else
                value1 = "['"+s1+"']"

            params["life_experience"] = value1
            params["start_date"] = etFromYear.text.toString()+"-"+seletedFromMonthId+"-01"
//                spinner1blife.selectedItem.toString()+"-"+listIntegerLife.get(spinner1alife.selectedItemPosition)+"-01"
            params["ongoing"] = checkBox2.isChecked.toString()
            params["end_date"] = etToYear.text.toString()+"-"+ selectedToMonthId+"-01"
//                spinner2blife.selectedItem.toString()+"-"+listIntegerLife.get(spinner2alife.selectedItemPosition)+"-01"
            params["description"] = edittext_describeyourlifeexperience.text.toString()
            params["skills"] = value
            params["career_break"] = edittext_whycareerbreak.isChecked.toString()
            params["location"]  =edittext_locationlife.text.toString()
            params["caption"]  =edittext_caption.text.toString()
            params["default_image"] = "1"
        }
        else{
            var s: String = edittext_whatskillslife.text.toString().replace("[","")
            s = s.replace("]","")
            var value: String = ""
            if (s.contains(",")) {
                var array: ArrayList<String> = s.split(",") as ArrayList<String>

                for (l in 0 until array.size) {
                    value = value+"'" + array[l].toString() + "',"
                }
                value = "[" + value + "]"
            }else
                value = "['"+s+"']"

            var value1: String=""
            var s1: String = edittext_typeoflifeexperience.selectedItem.toString().replace("[","")
            s1 = s1.replace("]","")
            if (s1.contains(",")) {
                var array1: ArrayList<String> = s1.split(",") as ArrayList<String>

                for (l in 0 until array1.size) {
                    value1 = value1+"'" + array1[l].toString() + "',"
                }
                value1 = "[" + value1 + "]"
            }else
                value1 = "['"+s1+"']"


            params["life_experience"] = value1
            params["start_date"] = etFromYear.text.toString()+"-"+seletedFromMonthId+"-01"
//                spinner1blife.selectedItem.toString()+"-"+listIntegerLife.get(spinner1alife.selectedItemPosition)+"-01"
            params["ongoing"] = checkBox2.isChecked.toString()
//            if (!checkBox2.isChecked) {
            params["end_date"] =etToYear.text.toString()+"-"+ selectedToMonthId+"-01"
//                spinner2blife.selectedItem.toString() + "-" + listIntegerLife.get(spinner2alife.selectedItemPosition) + "-01"
//            }
            params["career_break"] = edittext_whycareerbreak.isChecked.toString()
            params["description"] = edittext_describeyourlifeexperience.text.toString()
            params["skills"] = value
            params["location"]  =edittext_locationlife.text.toString()
            params["caption"]  =edittext_caption.text.toString()
            params["image_filename"] = picturepath_life.toString()
            params["image_file"] = imageEncoded_life
            params["default_image"] = "0"
            picturepath_life=""
            imageEncoded_life=""

        }

        Logger.d("TAGG", "PARAMS: "+params)
        var model: LifeModel = LifeModel();
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.AddLifeExperienceData(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        Logger.d("URL", "" + params)
        call.enqueue(object : Callback<UpdateLifeResponse> {
            override fun onResponse(call: Call<UpdateLifeResponse>, response: Response<UpdateLifeResponse>) {
                Logger.d("URL", "" + response+ EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
                var str_response = Gson().toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                val jsonObject1 : JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                // var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                if(response.isSuccessful){
                    Toast.makeText(applicationContext,"Added Successfully!!", Toast.LENGTH_LONG).show()
                    setResult(1);
                    finish()
                }
                else {
                    ToastHelper.makeToast(applicationContext, "message")
                }
            }
            override fun onFailure(call: Call<UpdateLifeResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext,"Adding Failed!!", Toast.LENGTH_LONG).show()

            }
        })
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
    fun deleteLifeData(){

        var model: LifeModel = LifeModel();
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.DeleteLifeExperienceData(lifeId,EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)
        call.enqueue(object : Callback<DeletePostResponse> {
            override fun onResponse(call: Call<DeletePostResponse>, response: Response<DeletePostResponse>) {
                Logger.d("URL", "" + response+ EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
                var str_response = Gson().toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                val jsonObject1 : JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                // var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                if(response.isSuccessful){
                    Toast.makeText(applicationContext,"Deleted Successfully!!", Toast.LENGTH_LONG).show()
                    setResult(1);
                    finish()
                }
                else {
                    ToastHelper.makeToast(applicationContext, "message")
                }

            }
            override fun onFailure(call: Call<DeletePostResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext,"Update Failed!!", Toast.LENGTH_LONG).show()

            }
        })
    }
}