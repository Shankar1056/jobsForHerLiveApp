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
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.layout_awardsedit.*
import kotlinx.android.synthetic.main.layout_awardsedit.deletethis
import kotlinx.android.synthetic.main.layout_awardsedit.header_text
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
import com.jobsforher.models.Months
import com.jobsforher.models.Years
import com.jobsforher.network.responsemodels.DeletePostResponse
import com.jobsforher.network.responsemodels.RecognitionResponse
import com.jobsforher.network.responsemodels.RecognitionUpdateResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import women.jobs.jobsforher.activities.BaseActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*


private var retrofitInterface: RetrofitInterface? = null
var recogId = 0
private val GALLERY_IMAGE = 1
public var picturepath_awards:String? = ""
public var imageEncoded_awards = "";
val listIntegersAwards = ArrayList<String>()

class AwardsEdit : BaseActivity() {

    private var awesomeValidation_award: AwesomeValidation? = null

    var listOfMonths : ArrayList<Months> = ArrayList()
    var listOfYears : ArrayList<Years> = ArrayList()
    var selectedFromMonth : String = ""
    var selectedFromYear : String = ""
    var seletedFromMonthId : Int = 0
    var selectType : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_awardsedit)
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        awesomeValidation_award = AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation_award!!.addValidation(edittext_titlr, RegexTemplate.NOT_EMPTY,"Please enter the award title")
        awesomeValidation_award!!.addValidation(edittext_awardedby, RegexTemplate.NOT_EMPTY,"Please enter awarded by");

        recogId = intent.getIntExtra("Id",0)
        editawards_back.setOnClickListener{
            this.finish()

        }
        getMonthAndYears()
        val spinner2 = findViewById<View>(R.id.spinner2) as Spinner
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

        listIntegersAwards.add("01")
        listIntegersAwards.add("02")
        listIntegersAwards.add("03")
        listIntegersAwards.add("04")
        listIntegersAwards.add("05")
        listIntegersAwards.add("06")
        listIntegersAwards.add("07")
        listIntegersAwards.add("08")
        listIntegersAwards.add("09")
        listIntegersAwards.add("10")
        listIntegersAwards.add("11")
        listIntegersAwards.add("12")
        val dataAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1, listMonths
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.setAdapter(dataAdapter)

        save.setOnClickListener {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus()!!.getWindowToken(), 0)
            }
            if (recogId>0) {
                if (awesomeValidation_award!!.validate()) {
                    if (etFromMonth.length() > 0 && etFromYear.length() > 0) {
                        updateRecognitionData()
                        save.isEnabled = false
                        save_up.isEnabled = false
                    }
                }
                else {
                    ToastHelper.makeToast(applicationContext, "Please enter the dates")
                }
            }
            else {
                if (awesomeValidation_award!!.validate()) {
                    if (etFromMonth.length() > 0 && etFromYear.length() > 0) {
                        AddRecognitionData()
                        save.isEnabled = false
                        save_up.isEnabled = false
                    }
                    else {
                        ToastHelper.makeToast(applicationContext, "Please enter the dates")
                    }
                }
            }
        }
        save_up.setOnClickListener {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus()!!.getWindowToken(), 0)
            }
            if (recogId>0) {
                if (awesomeValidation_award!!.validate()) {
                    if (etFromMonth.length() > 0 && etFromYear.length() > 0) {
                        updateRecognitionData()
                        save.isEnabled = false
                        save_up.isEnabled = false
                    }
                }
                else {
                    ToastHelper.makeToast(applicationContext, "Please enter the dates")
                }
            }
            else {
                if (awesomeValidation_award!!.validate()) {
                    if (etFromMonth.length() > 0 && etFromYear.length() > 0) {
                    AddRecognitionData()
                    save.isEnabled = false
                    save_up.isEnabled = false
                    }
                    else {
                        ToastHelper.makeToast(applicationContext, "Please enter the dates")
                    }
                }
            }
        }
        editawards_back.setOnClickListener {
            finish()
        }
        edittext_uploadimageofyourawards.setOnClickListener {
            showPictureDialog()
        }

        etFromMonth.setOnClickListener {

            showFromDateDialog("From")

            selectType = "From"
        }

        etFromYear.setOnClickListener {

            showFromDateDialog("From")

            selectType = "From"
        }

        val years = ArrayList<String>()
        val thisYear = Calendar.getInstance().get(Calendar.YEAR)
        for (i in 1975..thisYear) {
            years.add(Integer.toString(i))
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, years)

        val spinYear = findViewById<View>(R.id.spinner3) as Spinner
        spinYear.adapter = adapter

        if (recogId>0) {
            loadRecognitionData()
            header_text.setText("Edit Recognition")
        }
        else{
            header_text.setText("Add Recognition")
        }

        deletethis.setOnClickListener {
            deleteRecognitionData()
        }
    }

    private fun showPictureDialog() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setActivityTitle("Awards & Recognition")
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .setCropMenuCropButtonTitle("Done")
            .setMultiTouchEnabled(true)
            .setMinCropWindowSize(538,320)
            .setRequestedSize(538, 320)
            .setFixAspectRatio(false)
            .setAspectRatio(538,320)
            .setCropMenuCropButtonIcon(R.drawable.ic_tick)
            .start(this)
    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY_IMAGE)
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
        mAdapterMonths = MonthsAdapter(listOfMonths, "Awards_Edit")
        monthrecyclerView!!.adapter = mAdapterMonths

        var mAdapterYears: RecyclerView.Adapter<*>? = null

        val mLayoutManagerYears = LinearLayoutManager(applicationContext)
        yearRecyclerView!!.layoutManager = mLayoutManagerYears
        mAdapterYears = YearsAdapter(listOfYears, "Awards_Edit")
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

        } else { }

    }

    public fun SelectedYear(year : String){

        if (selectType.equals("From")){

            etFromYear?.setText(year)

            selectedFromYear = year

        }else { }

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
                    picturepath_awards = f.getName()+".png"
                    Log.d("TAGG", picturepath_awards.toString())
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
//                    pick_image!!.setImageBitmap(bitmap)
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val b = baos.toByteArray()
                    imageEncoded_awards = Base64.encodeToString(b, Base64.DEFAULT)

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
                Toast.makeText(this, "Cropping successful" , Toast.LENGTH_LONG).show()
                val contentURI = result.uri
                val f = File(contentURI.toString())
                picturepath_awards = f.getName()//+".png"
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
//                pick_image.setPadding(0,0,0,0)
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val b = baos.toByteArray()
                imageEncoded_awards = Base64.encodeToString(b, Base64.DEFAULT)

            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.error, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun loadRecognitionData(){

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.GetRecognitionDetails(recogId, "application/json",EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<RecognitionResponse> {
            override fun onResponse(call: Call<RecognitionResponse>, response: Response<RecognitionResponse>) {
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
                        edittext_titlr.setText(json_objectdetail.getString("title"))
                        edittext_awardedby.setText(json_objectdetail.getString("organization"))
                        var dates = json_objectdetail.getString("start_date")
                        dates = dates.split("T").toString()
                        val arrOfStr = dates.split("-")
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
                        edittext_describeyourachievemet.setText(json_objectdetail.getString("description"))

                    }
                }
                else {
                    ToastHelper.makeToast(applicationContext, "message")
                }
            }
            override fun onFailure(call: Call<RecognitionResponse>, t: Throwable) {
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

    fun updateRecognitionData(){

        val params = HashMap<String, String>()


        if (picturepath_awards.equals("")){

            params["organization"] = edittext_awardedby.text.toString()
            params["start_date"] = etFromYear.text.toString()+"-"+seletedFromMonthId+"-01"
//                spinner3.selectedItem.toString()+"-"+listIntegersAwards.get(spinner2.selectedItemPosition)+"-01"
            params["title"] = edittext_titlr.text.toString()
            params["description"]=edittext_describeyourachievemet.text.toString()
            params["default_image"] = "1"
        }
        else{

            params["organization"] = edittext_awardedby.text.toString()
            params["start_date"] = etFromYear.text.toString()+"-"+seletedFromMonthId+"-01"
//                spinner3.selectedItem.toString()+"-"+listIntegersAwards.get(spinner2.selectedItemPosition)+"-01"
            params["image_filename"] = picturepath_awards.toString()
            params["image_file"] = imageEncoded_awards
            params["title"] = edittext_titlr.text.toString()
            params["description"]=edittext_describeyourachievemet.text.toString()
            params["default_image"] = "0"
            picturepath_awards = ""
            imageEncoded_awards = ""
        }


        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.UpdateRecognitionData(recogId,EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        Logger.d("URL", "" + params)
        call.enqueue(object : Callback<RecognitionUpdateResponse> {
            override fun onResponse(call: Call<RecognitionUpdateResponse>, response: Response<RecognitionUpdateResponse>) {
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
            override fun onFailure(call: Call<RecognitionUpdateResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext,"Update Failed!!", Toast.LENGTH_LONG).show()

            }
        })
    }

    fun AddRecognitionData(){

        val params = HashMap<String, String>()


        if (picturepath_awards.equals("")){

            params["organization"] = edittext_awardedby.text.toString()
            params["start_date"] = etFromYear.text.toString()+"-"+seletedFromMonthId+"-01"
//                spinner3.selectedItem.toString()+"-"+listIntegersAwards.get(spinner2.selectedItemPosition)+"-01"
            params["title"] = edittext_titlr.text.toString()
            params["description"]=edittext_describeyourachievemet.text.toString()
            params["default_image"] = "1"
        }
        else{

            params["organization"] = edittext_awardedby.text.toString()
            params["start_date"] = etFromYear.text.toString()+"-"+seletedFromMonthId+"-01"
//                spinner3.selectedItem.toString()+"-"+listIntegersAwards.get(spinner2.selectedItemPosition)+"-01"
            params["image_filename"] = picturepath_awards.toString()
            params["image_file"] = imageEncoded_awards
            params["title"] = edittext_titlr.text.toString()
            params["description"]=edittext_describeyourachievemet.text.toString()
            params["default_image"] = "0"
            picturepath_awards = ""
            imageEncoded_awards = ""
        }


        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.AddRecognitionData(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        Logger.d("URL", "" + params)
        call.enqueue(object : Callback<RecognitionUpdateResponse> {
            override fun onResponse(call: Call<RecognitionUpdateResponse>, response: Response<RecognitionUpdateResponse>) {
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
            override fun onFailure(call: Call<RecognitionUpdateResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext,"Adding Failed!!", Toast.LENGTH_LONG).show()

            }
        })
    }

    fun deleteRecognitionData(){


        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.DeleteRecognitionData(recogId,EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)

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