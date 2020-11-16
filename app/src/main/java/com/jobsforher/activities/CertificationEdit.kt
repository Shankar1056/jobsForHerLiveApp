package com.jobsforher.activities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
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
import kotlinx.android.synthetic.main.layout_certificateedit.*
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
import com.jobsforher.models.CertificateModel
import com.jobsforher.models.Months
import com.jobsforher.models.Years
import com.jobsforher.network.responsemodels.CertificateResponse
import com.jobsforher.network.responsemodels.CertificateUpdateResponse
import com.jobsforher.network.responsemodels.DeletePostResponse
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
var certId = 0
private val GALLERY_IMAGE = 1
public var picturepath:String? = ""
public var imageEncoded = "";
val listIntegers = ArrayList<String>()

var listOfMonths : ArrayList<Months> = ArrayList()
var listOfYears : ArrayList<Years> = ArrayList()
var selectedFromMonth : String = ""
var selectedToMonth : String = ""
var selectedFromYear : String = ""
var selectedToYear : String = ""
var seletedFromMonthId : Int = 0
var selectedToMonthId : Int = 0
var selectType : String = ""


class CertificationEdit : BaseActivity() {

    private var awesomeValidation_cert: AwesomeValidation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_certificateedit)
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        awesomeValidation_cert = AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation_cert!!.addValidation(edittext_nameofcertification, RegexTemplate.NOT_EMPTY,"Please enter certification name")
        awesomeValidation_cert!!.addValidation(edittext_issuingorganization, RegexTemplate.NOT_EMPTY,"Please enter the organization name");
        // to validate with a simple custom validator function


        certId = intent.getIntExtra("ID",0)
        editcertificate_back.setOnClickListener{
            this.finish()
        }

        getMonthAndYears()

        Log.d("TAGG", "ID iS"+ certId)
        val spinner1ac = findViewById<View>(R.id.spinner1a) as Spinner
        val spinner2ac = findViewById<View>(R.id.spinner2a) as Spinner
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

        listIntegers.add("01")
        listIntegers.add("02")
        listIntegers.add("03")
        listIntegers.add("04")
        listIntegers.add("05")
        listIntegers.add("06")
        listIntegers.add("07")
        listIntegers.add("08")
        listIntegers.add("09")
        listIntegers.add("10")
        listIntegers.add("11")
        listIntegers.add("12")
        val dataAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1, listMonths
        )

        editcertificate_back.setOnClickListener {
            finish()
        }



        spinner2b.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                // your code here
                checkSpinnerValidation()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        })
        spinner2a.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                checkSpinnerValidation()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        })
        spinner1a.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                checkSpinnerValidation()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        })
        spinner1b.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                checkSpinnerValidation()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        })



        saveCert.setOnClickListener {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus()!!.getWindowToken(), 0)
            }
            if (certId>0) {
                if (awesomeValidation_cert!!.validate()) {
                    if (etFromMonth.length() > 0 && etFromYear.length() > 0) {
                        if (checkBox.isChecked==false && Integer.parseInt(etFromYear.getText().toString()) == Integer.parseInt(etToYear.text.toString())
                            && seletedFromMonthId<=selectedToMonthId) {
                            updateCertificateData()
                            saveCert.isEnabled = false
                            saveCert_up.isEnabled = false
                        }
                        else if(checkBox.isChecked==false && Integer.parseInt(etFromYear.getText().toString()) < Integer.parseInt(etToYear.text.toString())){
                            updateCertificateData()
                            saveCert.isEnabled = false
                            saveCert_up.isEnabled = false
                        }
                        else if(checkBox.isChecked==true) {

                            updateCertificateData()
                            saveCert.isEnabled = false
                            saveCert_up.isEnabled = false
                        }
                        else
                            ToastHelper.makeToast(applicationContext, "From date should be less than To date")
                    }
                    else {
                        ToastHelper.makeToast(applicationContext, "Please enter the dates")
                    }
                }
                else {
//                    ToastHelper.makeToast(applicationContext, "Please enter the dates")
                }
            }
            else {
                if (awesomeValidation_cert!!.validate()){
                    if (etFromMonth.length() > 0 && etFromYear.length() > 0) {
                        val c = Calendar.getInstance()
                        val year = c.get(Calendar.YEAR)
                        val month = c.get(Calendar.MONTH)
                        val day = c.get(Calendar.DAY_OF_MONTH)
                        if (checkBox.isChecked==false && Integer.parseInt(etFromYear.getText().toString()) == Integer.parseInt(etToYear.text.toString())
                            && seletedFromMonthId<=selectedToMonthId) {
                            AddCertificateData()
                            saveCert.isEnabled = false
                            saveCert_up.isEnabled = false
                        }
                        else if(checkBox.isChecked==false && Integer.parseInt(etFromYear.getText().toString()) < Integer.parseInt(etToYear.text.toString())){
                            AddCertificateData()
                            saveCert.isEnabled = false
                            saveCert_up.isEnabled = false
                        }
                        else if(checkBox.isChecked==true) {
                            if(Integer.parseInt(etFromYear.getText().toString())>year){
                                Toast.makeText(applicationContext, "From date should be less than current date",Toast.LENGTH_LONG).show()
                            }
                            else if(Integer.parseInt(etFromYear.getText().toString())==year && seletedFromMonthId>month){
                                Toast.makeText(applicationContext, "From date should be less than current date",Toast.LENGTH_LONG).show()
                            }
                            else {
                                AddCertificateData()
                                saveCert.isEnabled = false
                                saveCert_up.isEnabled = false
                            }
                        }
                        else
                            ToastHelper.makeToast(applicationContext, "From date should be less than To date")
                    }
                    else {
                        ToastHelper.makeToast(applicationContext, "Please enter the dates")
                    }
                }
                else {
//                    ToastHelper.makeToast(applicationContext, "Please enter the dates")
                }
            }
        }

        saveCert_up.setOnClickListener {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus()!!.getWindowToken(), 0)
            }
            if (certId>0) {
                if (awesomeValidation_cert!!.validate()) {
                    if (etFromMonth.length() > 0 && etFromYear.length() > 0) {
                        if (checkBox.isChecked==false && Integer.parseInt(etFromYear.getText().toString()) == Integer.parseInt(etToYear.text.toString())
                            && seletedFromMonthId<=selectedToMonthId) {
                            updateCertificateData()
                            saveCert.isEnabled = false
                            saveCert_up.isEnabled = false
                        }
                        else if(checkBox.isChecked==false && Integer.parseInt(etFromYear.getText().toString()) < Integer.parseInt(etToYear.text.toString())){
                            updateCertificateData()
                            saveCert.isEnabled = false
                            saveCert_up.isEnabled = false
                        }
                        else if(checkBox.isChecked==true) {
                            updateCertificateData()
                            saveCert.isEnabled = false
                            saveCert_up.isEnabled = false
                        }
                        else
                            ToastHelper.makeToast(applicationContext, "From date should be less than To date")
                    }
                    else {
                        ToastHelper.makeToast(applicationContext, "Please enter the dates")
                    }
                }
                else {
//                    ToastHelper.makeToast(applicationContext, "Please enter the dates")
                }
            }
            else {
                if (awesomeValidation_cert!!.validate()){
                    if (etFromMonth.length() > 0 && etFromYear.length() > 0) {
                        val c = Calendar.getInstance()
                        val year = c.get(Calendar.YEAR)
                        val month = c.get(Calendar.MONTH)
                        val day = c.get(Calendar.DAY_OF_MONTH)
                        if (checkBox.isChecked==false && Integer.parseInt(etFromYear.getText().toString()) == Integer.parseInt(etToYear.text.toString())
                            && seletedFromMonthId<=selectedToMonthId) {
                            AddCertificateData()
                            saveCert.isEnabled = false
                            saveCert_up.isEnabled = false
                        }
                        else if(checkBox.isChecked==false && Integer.parseInt(etFromYear.getText().toString()) < Integer.parseInt(etToYear.text.toString())){
                            AddCertificateData()
                            saveCert.isEnabled = false
                            saveCert_up.isEnabled = false
                        }
                        else if(checkBox.isChecked==true) {
                            if(Integer.parseInt(etFromYear.getText().toString())>year){
                                Toast.makeText(applicationContext, "From date should be less than current date",Toast.LENGTH_LONG).show()
                            }
                            else if(Integer.parseInt(etFromYear.getText().toString())==year && seletedFromMonthId>month){
                                Toast.makeText(applicationContext, "From date should be less than current date",Toast.LENGTH_LONG).show()
                            }
                            else {
                                AddCertificateData()
                                saveCert.isEnabled = false
                                saveCert_up.isEnabled = false
                            }
                        }
                        else
                            ToastHelper.makeToast(applicationContext, "From date should be less than To date")
                    }
                    else {
                        ToastHelper.makeToast(applicationContext, "Please enter the dates")
                    }
                }
                else { }
            }

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

        edittext_uploadimageofyourcertification.setOnClickListener {
            showPictureDialog()
        }

        deletethis.setOnClickListener {
            deleteCertificateData()
        }

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1ac.setAdapter(dataAdapter)
        spinner2ac.setAdapter(dataAdapter)

        val years = ArrayList<String>()
        val thisYear = Calendar.getInstance().get(Calendar.YEAR)
        for (i in 1975..thisYear) {
            years.add(Integer.toString(i))
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, years)

        val spinYearc = findViewById<View>(R.id.spinner1b) as Spinner
        spinYearc.adapter = adapter
        val spinYear1c = findViewById<View>(R.id.spinner2b) as Spinner
        spinYear1c.adapter = adapter

        if (certId>0) {
            loadCertificateData()
            header_text.setText("Edit Certification")
        }
        else{
            header_text.setText("Add Certification")
        }


        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                llToDate.visibility = View.GONE
            }
            else
                llToDate.visibility = View.VISIBLE
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
        mAdapterMonths = MonthsAdapter(listOfMonths, "Certification_Edit")
        monthrecyclerView!!.adapter = mAdapterMonths

        var mAdapterYears: RecyclerView.Adapter<*>? = null

        val mLayoutManagerYears = LinearLayoutManager(applicationContext)
        yearRecyclerView!!.layoutManager = mLayoutManagerYears
        mAdapterYears = YearsAdapter(listOfYears, "Certification_Edit")
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

    fun checkSpinnerValidation(){
        if (expirydate.visibility == View.VISIBLE){
            if (Integer.parseInt(spinner1b.selectedItem.toString()) > Integer.parseInt(spinner2b.selectedItem.toString())) {
                Toast.makeText(applicationContext, "Expiry date should be greater than date", Toast.LENGTH_LONG).show()
                //spinner1b.setSelection(spinner1b.getAdapter().getCount()-1)
                spinner2b.setSelection(spinner1b.getAdapter().getCount()-1)
                //spinner1a.setSelection(spinner1a.getAdapter().getCount()-1)
                spinner2a.setSelection(spinner1a.getAdapter().getCount()-1)
            }
            else if(Integer.parseInt(spinner1b.selectedItem.toString()) == Integer.parseInt(spinner2b.selectedItem.toString())){
                if (Integer.parseInt(listIntegers.get(spinner1a.selectedItemPosition)) > Integer.parseInt(listIntegers.get(spinner2a.selectedItemPosition))) {
                    Toast.makeText(applicationContext, "Expiry date should be greater than date", Toast.LENGTH_LONG)
                        .show()
                    //spinner1b.setSelection(spinner1b.getAdapter().getCount()-1)
                    spinner2b.setSelection(spinner1b.getAdapter().getCount()-1)
                    //spinner1a.setSelection(spinner1a.getAdapter().getCount()-1)
                    spinner2a.setSelection(spinner1a.getAdapter().getCount()-1)
                }
            }
        }
        else{
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            Toast.makeText(applicationContext, ""+year+",,"+month,Toast.LENGTH_LONG).show()
            if(Integer.parseInt(spinner1b.selectedItem.toString())> year ){
                Toast.makeText(applicationContext, "From date should be less than vurrent date",Toast.LENGTH_LONG).show()
            }
            else if(Integer.parseInt(listIntegers.get(spinner1a.selectedItemPosition))>month)
                Toast.makeText(applicationContext, "From date should be less than vurrent date",Toast.LENGTH_LONG).show()
        }
    }


    fun loadCertificateData(){

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.GetCertificateDetails(certId, "application/json",EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<CertificateResponse> {
            override fun onResponse(call: Call<CertificateResponse>, response: Response<CertificateResponse>) {
                Logger.d("URL", "" + response+ EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))

                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)

                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                val jsonObject1 : JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                if(response.isSuccessful){

                    for (i in 0 until response.body()!!.body!!.size) {

                        var json_objectdetail: JSONObject = jsonarray.getJSONObject(i)
                        edittext_nameofcertification.setText(json_objectdetail.getString("name"))
                        edittext_issuingorganization.setText(json_objectdetail.getString("organization"))

                        var skilldata: ArrayList<String> = ArrayList()
                        for (k in 0 until response.body()!!.body!![i].skills!!.size) {
                            skilldata.add(json_objectdetail.getJSONArray("skills").getString(k))
                        }
                        if (json_objectdetail.getJSONArray("skills").equals("[]"))
                            edittext_whatskillsdidyougain.setText("")
                        else
                            edittext_whatskillsdidyougain.setText(skilldata.toString().replace("[","").replace("]",""))
                        edittext_describeyourcertification.setText(json_objectdetail.getString("description"))

                        var dates = json_objectdetail.getString("start_date")
                        dates = dates.split("T").toString()
                        val arrOfStr = dates.split("-")
                        spinner1b.setSelection(getIndex(spinner1b, arrOfStr[0].replace("[","")));
                        spinner1a.setSelection(listIntegers.indexOf(arrOfStr[1].replace("[","")));

                        if (json_objectdetail.getBoolean("expires")==false) {
                            checkBox.isChecked = false
                            expirydate.visibility = View.VISIBLE
                            var dates = json_objectdetail.getString("expires_on")
                            dates = dates.split("T").toString()
                            val arrOfStr = dates.split("-")
                            spinner2b.setSelection(getIndex(spinner2b, arrOfStr[0].replace("[", "")));
                            //spinner2a.setSelection(listIntegers.indexOf(arrOfStr[1].replace("[", "")));
                            var dates1 = json_objectdetail.getString("start_date")
                            dates1 = dates1.split("T").toString()
                            val arrOfStr1 = dates1.split("-")
                            //spinner1b.setSelection(getIndex(spinner1b, arrOfStr1[0].replace("[", "")));
                            //spinner1a.setSelection(listIntegers.indexOf(arrOfStr1[1].replace("[", "")));
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
                        else {
                            checkBox.isChecked = true
                            expirydate.visibility = View.GONE
                            var dates1 = json_objectdetail.getString("start_date")
                            dates1 = dates1.split("T").toString()
                            val arrOfStr1 = dates1.split("-")
                            //spinner1b.setSelection(getIndex(spinner1b, arrOfStr1[0].replace("[", "")));
                            //spinner1a.setSelection(listIntegers.indexOf(arrOfStr1[1].replace("[", "")));
                            for (car in listOfMonths) {
                                if (arrOfStr1[1].replace("[", "").startsWith("0")) {
                                    if (arrOfStr1[1].replace("[", "").substring(1).equals(car.monthsId.toString())) {
                                        etFromMonth.setText(car.months)
                                        seletedFromMonthId = car.monthsId
                                    }
                                }
                                else{
                                    if (arrOfStr1[1].replace("[", "").equals(car.monthsId.toString())) {
                                        etFromMonth.setText(car.months)
                                        seletedFromMonthId = car.monthsId
                                    }
                                }
                            }
                            etFromYear.setText(arrOfStr1[0].replace("[", "").toString())
                        }
                    }
                }
                else {
                    ToastHelper.makeToast(applicationContext, "message")
                }
            }
            override fun onFailure(call: Call<CertificateResponse>, t: Throwable) {
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
            .setActivityTitle("Certification")
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .setCropMenuCropButtonTitle("Done")
            .setMultiTouchEnabled(true)
            .setMinCropWindowSize(538,320)
            .setRequestedSize(538, 320)
            .setFixAspectRatio(false)
            .setAspectRatio(538,320)
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
                    picturepath = f.getName()+".png"
                    Log.d("TAGG", picturepath.toString())
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
//                    pick_image!!.setImageBitmap(bitmap)
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val b = baos.toByteArray()
                    imageEncoded = Base64.encodeToString(b, Base64.DEFAULT)

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

                Toast.makeText(this, "Cropping successful", Toast.LENGTH_LONG).show()
//                Picasso.with(applicationContext)
//                    .load(result.uri)
//                    .centerCrop().resize(150,150)
//                    .placeholder(R.drawable.ic_launcher_foreground)
//                    .into(pick_image)
                val contentURI = result.uri
                val f = File(contentURI.toString())
                picturepath = f.getName()//+".png"
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
//                circleView!!.setImageBitmap(bitmap)
//                pick_image.setPadding(0,0,0,0)
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val b = baos.toByteArray()
                imageEncoded = Base64.encodeToString(b, Base64.DEFAULT)

            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.error, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun updateCertificateData(){

        val params = HashMap<String, Any>()


        if (picturepath.equals("")){
            var s: String = edittext_whatskillsdidyougain.text.toString().replace("[","").replace("]","")
            Log.d("TAGG", s)
            var value: String = ""
            if (s.contains(",")) {
                var array: ArrayList<String> = s.split(",") as ArrayList<String>
                for (l in 0 until array.size) {
                    value = value+ "'" + array[l].toString() + "',"
                }
            }
            else
                value = "'"+s+"'"

            value = "["+ value +"]"
            params["name"] = edittext_nameofcertification.text.toString()
            params["organization"] = edittext_issuingorganization.text.toString()
            params["start_date"] = etFromYear.text.toString()+"-"+seletedFromMonthId+"-01"
//                spinner1b.selectedItem.toString()+"-"+ listIntegers.get(spinner1a.selectedItemPosition)+"-01"
            if (checkBox.isChecked==false) {
                params["expires_on"] =etToYear.text.toString()+"-"+ selectedToMonthId+"-01"
                //spinner2b.selectedItem.toString() + "-" + listIntegers.get(spinner2a.selectedItemPosition) + "-01"
            }
            params["expires"] = checkBox.isChecked
            params["description"] = edittext_describeyourcertification.text.toString()
            params["skills"] = value
            params["default_image"] = "1"
        }
        else{
            var s: String = edittext_whatskillsdidyougain.text.toString().replace("[","").replace("]","")
            Log.d("TAGG", s)
            var value: String = ""
            if (s.contains(",")) {
                var array: ArrayList<String> = s.split(",") as ArrayList<String>

                for (l in 0 until array.size) {
                    value = value+ "'" + array[l].toString() + "',"
                }
            }
            else
                value = "'"+s+"'"

            value = "["+ value +"]"
            params["name"] = edittext_nameofcertification.text.toString()
            params["organization"] = edittext_issuingorganization.text.toString()
            params["start_date"] = etFromYear.text.toString()+"-"+seletedFromMonthId+"-01"
            //spinner1b.selectedItem.toString()+"-"+listIntegers.get(spinner1a.selectedItemPosition)+"-01"
            if (checkBox.isChecked==false) {
                params["expires_on"] =etToYear.text.toString()+"-"+ selectedToMonthId+"-01"
                //spinner2b.selectedItem.toString() + "-" + listIntegers.get(spinner2a.selectedItemPosition) + "-01"
            }
            params["expires"] = checkBox.isChecked
            params["description"] = edittext_describeyourcertification.text.toString()
            params["skills"] = value
            params["image_filename"] = picturepath.toString()
            params["image_file"] = imageEncoded
            params["default_image"] = "0"
            picturepath = ""
            imageEncoded=""
        }


        var model: CertificateModel = CertificateModel()
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.UpdateCertificationData(certId,EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        Logger.d("URL", "" + params)
        call.enqueue(object : Callback<CertificateUpdateResponse> {
            override fun onResponse(call: Call<CertificateUpdateResponse>, response: Response<CertificateUpdateResponse>) {
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
            override fun onFailure(call: Call<CertificateUpdateResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext,"Update Failed!!", Toast.LENGTH_LONG).show()

            }
        })
    }

    fun AddCertificateData(){

        val params = HashMap<String, Any>()


        if (picturepath.equals("")){
            var s: String = edittext_whatskillsdidyougain.text.toString()
            var value: String = ""
            if (s.contains(",")) {
                var array: ArrayList<String> = s.split(",") as ArrayList<String>
                for (l in 0 until array.size) {
                    value = value+ "'" + array[l].toString() + "',"
                }
            }
            else
                value = "'"+s+"'"

            value = "["+ value +"]"
            params["name"] = edittext_nameofcertification.text.toString()
            params["organization"] = edittext_issuingorganization.text.toString()
            params["start_date"] = etFromYear.text.toString()+"-"+seletedFromMonthId+"-01"
//                spinner1b.selectedItem.toString()+"-"+listIntegers.get(spinner1a.selectedItemPosition)+"-01"
            if (checkBox.isChecked==false) {
                params["expires_on"] =etToYear.text.toString()+"-"+ selectedToMonthId+"-01"
                //spinner2b.selectedItem.toString() + "-" + listIntegers.get(spinner2a.selectedItemPosition) + "-01"
            }
            params["expires"] = checkBox.isChecked
            params["description"] = edittext_describeyourcertification.text.toString()
            params["skills"] = value
            params["default_image"] = "1"
        }
        else{
            var s: String = edittext_whatskillsdidyougain.text.toString()
            var value: String = ""
            if (s.contains(",")) {
                var array: List<String> = s.split(",")
                var value: String = ""
                for (l in 0 until array.size) {
                    value = value+ "'" + array[l].toString() + "',"
                }
            }
            else
                value = "'"+s+"'"

            value = "["+ value +"]"
            params["name"] = edittext_nameofcertification.text.toString()
            params["organization"] = edittext_issuingorganization.text.toString()
            params["start_date"] = etFromYear.text.toString()+"-"+seletedFromMonthId+"-01"
            //spinner1b.selectedItem.toString()+"-"+listIntegers.get(spinner1a.selectedItemPosition)+"-01"
            if (checkBox.isChecked==false) {
                params["expires_on"] =etToYear.text.toString()+"-"+ selectedToMonthId+"-01"
                //spinner2b.selectedItem.toString() + "-" + listIntegers.get(spinner2a.selectedItemPosition) + "-01"
            }
            params["expires"] = checkBox.isChecked
            params["description"] = edittext_describeyourcertification.text.toString()
            params["skills"] = value
            params["image_filename"] = picturepath.toString()
            params["image_file"] = imageEncoded
            params["default_image"] = "0"
            picturepath = ""
            imageEncoded=""
        }


        var model: CertificateModel = CertificateModel();
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.AddCertificateData(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        Logger.d("URL", "" + params)
        call.enqueue(object : Callback<CertificateUpdateResponse> {
            override fun onResponse(call: Call<CertificateUpdateResponse>, response: Response<CertificateUpdateResponse>) {
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
            override fun onFailure(call: Call<CertificateUpdateResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext,"Adding Failed!!", Toast.LENGTH_LONG).show()

            }
        })
    }

    fun deleteCertificateData(){

        var model: CertificateModel = CertificateModel()
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.DeleteCertificationData(certId,EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)

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
                    Toast.makeText(applicationContext,"Updated Successfully!!", Toast.LENGTH_LONG).show()
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


