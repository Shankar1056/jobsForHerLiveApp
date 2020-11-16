package com.jobsforher.activities

import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.layout_awardsedit.*
import kotlinx.android.synthetic.main.layout_awardsedit.editawards_back
import kotlinx.android.synthetic.main.profilesummaryedit.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.jobsforher.R
import com.jobsforher.helpers.Logger
import com.jobsforher.network.responsemodels.RecognitionResponse
import com.jobsforher.network.responsemodels.UpdateProfileResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import women.jobs.jobsforher.activities.BaseActivity
import java.util.*


private var retrofitInterface: RetrofitInterface? = null
var profId = 0
var profSumm = ""


class ProfileSummaryEdit : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profilesummaryedit)
        profId = intent.getIntExtra("sumId",0)
        profSumm = intent.getStringExtra("summaryData")
        editawards_back.setOnClickListener{
            this.finish()

        }
        saveprof_up.setOnClickListener {
            updateProfileSummaryData()
        }

        saveSummary.setOnClickListener {
            updateProfileSummaryData()
        }

        editawards_back.setOnClickListener {
            finish()
        }

        if (profId>0)
            edittext_titlrsummary.setText(profSumm)
    }

    fun loadProileSummaryData(){

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.GetRecognitionDetails(recogId, "application/json",EndPoints.CLIENT_ID, "Bearer PxCsyHL1vwJT5W30hC0o8Y9YIsvzzJNnudBfiBTNHD")

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
//
//                        var skilldata: ArrayList<String> = ArrayList()
//                        for (k in 0 until json_objectdetail.getString("skills").length) {
//                            skilldata.add(json_objectdetail.getJSONArray("skills").getString(k))
//                        }
//                        edittext_whatskillsdidyougain.setText(skilldata.toString())
                        edittext_describeyourachievemet.setText(json_objectdetail.getString("description"))
                    }
                }
                else {
                    // ToastHelper.makeToast(applicationContext, "message")
                }
            }
            override fun onFailure(call: Call<RecognitionResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
//                Toast.makeText(applicationContext,"No Certificates to load!!", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun updateProfileSummaryData(){

        val params = HashMap<String, String>()
        params["profile_summary"] = edittext_titlrsummary.text.toString()

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.UpdateSummaryData(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        Logger.d("URL", "" + params)
        call.enqueue(object : Callback<UpdateProfileResponse> {
            override fun onResponse(call: Call<UpdateProfileResponse>, response: Response<UpdateProfileResponse>) {
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
                    Toast.makeText(applicationContext,"Update Profile Success!!", Toast.LENGTH_LONG).show()
                    setResult(1);
                    finish()
                }
                else {
                    //ToastHelper.makeToast(applicationContext, "message")
                }

            }
            override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext,"Update Failed!!", Toast.LENGTH_LONG).show()

            }
        })
    }
}