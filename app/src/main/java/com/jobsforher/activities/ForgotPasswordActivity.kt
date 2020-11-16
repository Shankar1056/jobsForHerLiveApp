package com.jobsforher.activities

import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import com.jobsforher.R
import com.jobsforher.helpers.Constants
import com.jobsforher.helpers.Logger
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import entertainment.minersinc.tfhy.network.responsemodels.ForgotPasswordResponse
import kotlinx.android.synthetic.main.activity_forgotpassword_new.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import women.jobs.jobsforher.activities.BaseActivity
import java.util.*

class ForgotPasswordActivity : BaseActivity() {

    private val TAG = javaClass.simpleName
    private var retrofitInterface: RetrofitInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_forgotpassword_new)

        iv_Back.setOnClickListener{

            onBackPressed()
        }



        btnSendEmail.setOnClickListener {
            resetPassword(etEmail_Id.text.toString())
        }

    }


    private fun resetPassword(email: String) {

        //Toast.makeText(applicationContext,"Hello" +email,Toast.LENGTH_LONG).show()

        val params = HashMap<String, String>()

        val email = email

        params["email"] = email
        params["role"] = "user"

        Logger.d(TAG, "Params : " + Gson().toJson(params))


        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.passwodReset("application/json", EndPoints.CLIENT_ID, params)
        call.enqueue(object : Callback<ForgotPasswordResponse> {
            override fun onResponse(call: Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
                Logger.d("URL", "" + response)

                if (response.isSuccessful) {
                    Toast.makeText(applicationContext,"A password reset message has been sent to your registered email id, " +
                            "please click the link in that message to set your password.",Toast.LENGTH_LONG).show()
                    finish()
                } else {

                }


            }

            override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {

                Logger.d(TAG, "FAILED : $t")
            }
        })
    }



}
