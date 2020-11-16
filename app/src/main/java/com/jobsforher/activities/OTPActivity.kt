package com.jobsforher.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.google.gson.Gson
import entertainment.minersinc.tfhy.network.responsemodels.LoginResponse
import kotlinx.android.synthetic.main.activity_otp.*
import kotlinx.android.synthetic.main.toolbar_back.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.jobsforher.R
import com.jobsforher.helpers.Constants
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import women.jobs.jobsforher.activities.BaseActivity
import java.util.*

class OTPActivity : BaseActivity() {

    private val TAG = javaClass.simpleName
    private var retrofitInterface: RetrofitInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_otp)


        etOTPBox_One.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

                etOTPBox_Two.requestFocus()

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })


        etOTPBox_Two.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

                etOTPBox_Three.requestFocus()

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })



        etOTPBox_Three.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

                etOTPBox_Four.requestFocus()

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })





        etOTPBox_Four.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

                etOTPBox_Five.requestFocus()

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })





        etOTPBox_Six.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

                etOTPBox_Two.requestFocus()

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })








        ivBack.setOnClickListener{
            onBackPressed()
        }

        btnLetMeIn.setOnClickListener {
            var intent = Intent(this, SuccessPasswordActivity::class.java)
            startActivity(intent)
        }


    }


    private fun doLogin(email: String, password:String) {

        val params = HashMap<String, String>()


        val username = ""
        val email = email
        val password = password
        val role ="user"


        params["username"] = username
        params["email"] = email
        params["password"] = password
        params["role"] = role

        Logger.d(TAG, "Params : " + Gson().toJson(params))


        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.doLogin("application/json", EndPoints.CLIENT_ID, params)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {


                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
                Logger.d("URL", "" + response)


                if (response.isSuccessful) {
                    ToastHelper.makeToast(applicationContext, response.body()!!.auth!!.access_token)
                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid Username or Password")
                }


            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

                Logger.d(TAG, "FAILED : $t")
            }
        })
    }



}
