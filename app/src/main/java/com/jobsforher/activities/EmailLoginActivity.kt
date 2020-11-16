package com.jobsforher.activities;

import android.content.Intent
import android.os.Bundle
import com.jobsforher.R
import com.jobsforher.helpers.Constants
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import entertainment.minersinc.tfhy.network.responsemodels.LoginResponse
import kotlinx.android.synthetic.main.activity_email_login.*
import kotlinx.android.synthetic.main.toolbar_back.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import women.jobs.jobsforher.activities.BaseActivity
import java.util.*

class EmailLoginActivity : BaseActivity() {

    private val TAG = javaClass.simpleName
    private var retrofitInterface: RetrofitInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_email_login)

        btnNormalLogin.setOnClickListener {

            if(etEmail.text!!.trim().length!=0 && etPassword.text!!.trim().length!=0) {

                var intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
//                doLogin(etEmail.text!!.trim().toString(), HelperMethods.md5(etPassword.text!!.trim().toString()))
            }
            else
            {
                ToastHelper.makeToast(applicationContext, "Enter a valid email and password")
            }
        }


        tvSignup.setOnClickListener {
            var intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }


        ivBack.setOnClickListener{
            onBackPressed()
        }

        tvForgotPassword.setOnClickListener {
            var intent = Intent(this, ForgotPasswordActivity::class.java)
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

//        Logger.d(TAG, "Params : " + Gson().toJson(params))


        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.doLogin("application/json", EndPoints.CLIENT_ID, params)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                if (response.isSuccessful) {
                    ToastHelper.makeToast(applicationContext, response.body()!!.auth!!.access_token)
                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid Username or Password")
                }


            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

            }
        })
    }



}
