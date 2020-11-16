package com.jobsforher.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_signup_welcome_new.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.jobsforher.R
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.network.responsemodels.CheckUsernameResponse
import com.jobsforher.network.responsemodels.UserDetailsResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import com.jobsforher.network.retrofithelpers.UpdateProfileUrl
import women.jobs.jobsforher.activities.BaseActivity
import java.util.HashMap

class SignUpWelcomeActivity : BaseActivity() {

    private val TAG = javaClass.simpleName
    private var retrofitInterface: RetrofitInterface? = null
    private val PREF_STATUS = "isLoggedInStatus"
    private val PREF_NAME = "false"
    private val PREF_PROFILEURL = "profileUrl"
    private val PREF_ACCESSTOKEN = "accesstoken"
    private val PREF_PERCENTAGE = "0"

    private var profileVisibility : String = ""
    private var profileUrl : String = ""

    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_signup_welcome_new)
        val sharedPref: SharedPreferences = getSharedPreferences(
            "mysettings",
            Context.MODE_PRIVATE
        )
        if (sharedPref.getBoolean(PREF_STATUS, false) && sharedPref.getString(PREF_PERCENTAGE, "0")!!.toInt() > 9) {
            val homeIntent = Intent(this, ProfileView::class.java)
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            homeIntent.putExtra("isLoggedIn", true)
            startActivity(homeIntent)
            finish()

        }

        getUserDetails()

        il_do_later.setOnClickListener {

            //            ToastHelper.makeToast(applicationContext, "Groups Under Maintanence")

            val intent = Intent(applicationContext, ZActivityDashboard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("isLoggedIn",true)
            startActivity(intent)
        }

        btnEdit.setOnClickListener {

            edtProfileUrl.setEnabled(true)

        }

        edtProfileUrl.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

                tvProfileUrl.text = "www.jobsforher/profile/"+s.toString()

                if (s.toString().equals(profileUrl)) {

                    tvUsernameAlreadyExists.text = "Your Current Profile Url"

                }else{

                    checkForUsername(s.toString())
                    tvUsernameAlreadyExists.text = ""

                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

            }
        })

        btnGetStarted.setOnClickListener {


            val editProfileValue = edtProfileUrl.text

            showDialog(editProfileValue.toString())

        }
    }

    fun updateProfileUrl(profileUrl : String){

        val params = HashMap<String, String>()

        val profile_Url = profileUrl

        params["profile_url"] = profile_Url

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.UpdateProfileUrl(EndPoints.CLIENT_ID, "Bearer "+
                EndPoints.ACCESS_TOKEN, params)

        call.enqueue(object : Callback<UpdateProfileUrl> {
            override fun onResponse(call: Call<UpdateProfileUrl>, response: Response<UpdateProfileUrl>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    EndPoints.ACCESS_TOKEN = response.body()!!.auth!!.accessToken.toString()

                    if (response.body()!!.message.toString().equals("User profile url updated")){

                        val intent = Intent(applicationContext, StageOfLifeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                    } else {

                        ToastHelper.makeToast(applicationContext, "User profile url already Updated")

                    }

                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<UpdateProfileUrl>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })

    }

    fun showDialog(editProfileValue:String) {

        val dialog = Dialog(this@SignUpWelcomeActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_confirmation_dialog)
        dialog.show()

        val yesBtn = dialog .findViewById(R.id.btnYes) as Button
        val noBtn = dialog .findViewById(R.id.btnNo) as Button

        yesBtn.setOnClickListener {


            updateProfileUrl(editProfileValue)
        }

        noBtn.setOnClickListener { dialog .dismiss() }



    }


    fun checkForUsername(username : String){

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.CheckUsernameExists(username, EndPoints.CLIENT_ID, "Bearer "+
                EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<CheckUsernameResponse> {
            override fun onResponse(call: Call<CheckUsernameResponse>, response: Response<CheckUsernameResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    EndPoints.ACCESS_TOKEN = response.body()!!.auth!!.accessToken.toString()

                   // ToastHelper.makeToast(applicationContext, response.body()!!.body!!.toString())

                    if (response.body()!!.message.toString().equals("User profile url already present")){

                     //   ToastHelper.makeToast(applicationContext, response.body()!!.body!!.suggestion.toString())

                        if (username.equals(profileUrl)){

                            btnGetStarted.setEnabled(true)

                        }else {

                            tvUsernameAlreadyExists.text = "User profile url already present"

                            btnGetStarted.setEnabled(false)

                        }

                    } else {

                        ToastHelper.makeToast(applicationContext, "Unique")

                        tvUsernameAlreadyExists.text = ""

                        btnGetStarted.setEnabled(true)

                    }

                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<CheckUsernameResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })


    }

    fun getUserDetails(){

        Log.d("TAGG", EndPoints.ACCESS_TOKEN)

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.GetUserDetails(EndPoints.CLIENT_ID, "Bearer "+ EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<UserDetailsResponse> {
            override fun onResponse(call: Call<UserDetailsResponse>, response: Response<UserDetailsResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE  User Details", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    EndPoints.ACCESS_TOKEN = response.body()!!.auth!!.accessToken.toString()

                    headline1.text = "Congratulations "+ response.body()!!.body!!.username + " !"

                    tvProfileUrl.text = "www.jobsforher/profile/"+response.body()!!.body!!.profile_url

                    edtProfileUrl?.setText(response.body()!!.body!!.profile_url)

                    profileUrl = response.body()!!.body!!.profile_url.toString()

                    profileVisibility = response.body()!!.body!!.profile_visibility.toString()

                    if (response.body()!!.body!!.profile_visibility.toString().equals("false")){

                        btnEdit.visibility = View.VISIBLE

                    } else {

                        btnEdit.visibility = View.GONE
                    }

                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<UserDetailsResponse>, t: Throwable) {

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
