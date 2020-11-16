package com.jobsforher.activities


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.provider.Settings.Secure
import android.util.Log
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.jobsforher.BuildConfig
import com.jobsforher.R
import com.jobsforher.helpers.HelperMethods
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.models.AppVersionResponse
import com.jobsforher.network.responsemodels.RegisterIdResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import kotlinx.android.synthetic.main.activity_splash.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class SplashActivity() : AppCompatActivity() {


    private val TAG = javaClass.simpleName
    private var retrofitInterface: RetrofitInterface? = null
    private var PRIVATE_MODE = 0
    private val PREF_STATUS = "isLoggedInStatus"
    private val PREF_FCM = "fcmtoken"
    private val PREF_NAME = "false"
    private val PREF_PROFILEURL = "profileUrl"
    private val PREF_ACCESSTOKEN = "accesstoken"
    private val PREF_PERCENTAGE = "0"
    val iv1: ImageView? = null
    val Anim: AnimationDrawable? = null
    val imageArray: ArrayList<Int>? = null
    var i: Int = 0;
    var fcm: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (getIntent().hasExtra("pushnotification")) {
            Log.d("TAGG", "YES")
            val intent = Intent(this, ZActivityEvents::class.java)
            startActivity(intent)
            finish()
        }
        try {
            registerSocialToken(
                Secure.getString(
                    this.getContentResolver(),
                    Secure.ANDROID_ID
                )
            )
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }

        FirebaseApp.initializeApp(applicationContext)
        val mHandler = Handler(mainLooper)
        val mRunnable = Runnable {
        }
        mHandler.postDelayed(mRunnable, 500)
        val fade_in = ScaleAnimation(
            0f,
            1f,
            0f,
            1f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        fade_in.setDuration(1000)     // animation duration in milliseconds
        fade_in.setFillAfter(true)    // If fillAfter is true, the transformation that this animation performed will persist when it is finished.
        imageView1.startAnimation(fade_in)
        fade_in.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }

            override fun onAnimationEnd(animation: Animation) {
                val mHandler = Handler(mainLooper)
                val mRunnable = Runnable {
                    imageView1.setImageResource(R.drawable.screen_3)
                    FirebaseInstanceId.getInstance().instanceId
                        .addOnCompleteListener(OnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                Log.d("TAGG", "getInstanceId failed", task.exception)
                                return@OnCompleteListener
                            }

                            // Get new Instance ID token
                            val token = task.result?.token

                            // Log and toast
                            val msg = getString(R.string.msg_token_fmt, token)
                            fcm = token.toString()
                            Log.d("TAGG", "ID IS:" + token)
//                            callIntent(fcm)
                            checkAppUpdate()
                        })

                }
                mHandler.postDelayed(mRunnable, 500)
            }

            override fun onAnimationRepeat(animation: Animation) {
            }
        })
    }


    fun callIntent(fcm: String) {
        val sharedPref: SharedPreferences = getSharedPreferences(
            "mysettings",
            Context.MODE_PRIVATE
        );
        val editor = sharedPref.edit()
        editor.putString(PREF_FCM, fcm)
        editor.commit()

        if (sharedPref.getBoolean(PREF_STATUS, false)) {
            Log.d("TAGG", "Splash " + fcm)
            val homeIntent = Intent(this, HomePagePreferences::class.java) //HomePage
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            // intent.putExtra("fcm",fcm)
            startActivity(homeIntent)
            finish()


        } else if (!!sharedPref.getBoolean(PREF_STATUS, false)) {
            Log.d("TAGG", "Splash" + fcm)
            val homeIntent = Intent(this, HomePagePreferences::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            //intent.putExtra("fcm",fcm)
            startActivity(homeIntent)
            finish()
        } else {
            val homeIntent = Intent(this, com.jobsforher.activities.ZSplashActivityNew::class.java)
            intent.putExtra("fcm", fcm)
            startActivity(homeIntent)
            finish()
        }
    }


    override fun onResume() {
        super.onResume()

        sendFcmRegistrationToken()
    }

    override fun onPause() {
        super.onPause()
    }

    private fun sendFcmRegistrationToken() {
        val intent = Intent(this, FcmTokenRegistrationService::class.java)
        startService(intent)
    }

    private fun checkGoogleApiAvailability() {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)

        if (resultCode != ConnectionResult.SUCCESS) {
            if (resultCode == ConnectionResult.SUCCESS) {
                Logg.d("GoogleApi is available")
            } else {
                apiAvailability.getErrorDialog(this, resultCode, 1).show()
            }
        }
    }

    private fun registerSocialToken(device_id: String) {

        val params = HashMap<String, String>()

        val installationId = device_id

        params["device_id"] = installationId


        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.registerInstallationId(
            "application/json",
            EndPoints.CLIENT_ID,
            params
        )
        call.enqueue(object : Callback<RegisterIdResponse> {

            override fun onResponse(
                call: Call<RegisterIdResponse>,
                response: Response<RegisterIdResponse>
            ) {



                if (response.isSuccessful) {


                } else {
                    ToastHelper.makeToast(applicationContext, "Response Failed")
                }

            }

            override fun onFailure(call: Call<RegisterIdResponse>, t: Throwable) {

                Logger.d(TAG, "FAILED : $t")
            }
        })


    }

    private fun registerFCMToken(device_id: String, fcm_id: String) {

        val params = HashMap<String, String>()

        val installationId = device_id

        params["device_id"] = installationId
        params["fcm_id"] = fcm_id

        Log.d(TAG, "Params : " + params)

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.registerFcmId(
            "application/json", EndPoints.CLIENT_ID, EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<RegisterIdResponse> {

            override fun onResponse(
                call: Call<RegisterIdResponse>,
                response: Response<RegisterIdResponse>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGESplash", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
                Logger.d("URL", "" + response)


                if (response.isSuccessful) {
//                    ToastHelper.makeToast(applicationContext, response.body()!!.auth!!.access_token)
                    ToastHelper.makeToast(applicationContext, "FCm token registered")

                    Logger.d("URLDeviceId", "" + response.body()!!.body!!.device_id.toString())
                } else {
                    ToastHelper.makeToast(applicationContext, "Response Failed")
                }

            }

            override fun onFailure(call: Call<RegisterIdResponse>, t: Throwable) {

                Logger.d(TAG, "FAILED : $t")
            }
        })
    }


    private fun checkAppUpdate() {
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getAppVersion(
            EndPoints.CLIENT_ID
        )

        call.enqueue(object : Callback<AppVersionResponse> {
            override fun onResponse(
                call: Call<AppVersionResponse>,
                response: Response<AppVersionResponse>
            ) {
                if (HelperMethods.isSuccessResponse(response.body()?.response_code)) {
                    if (response.body()?.body?.version!! > BuildConfig.VERSION_CODE) {
                        showAppUpdateAlertDialog()
                    } else {
                        callIntent(fcm)
                    }
                } else {
                    Toast.makeText(
                        this@SplashActivity,
                        response.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<AppVersionResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                //showAppUpdateAlertDialog()
            }
        })
    }

    private fun showAppUpdateAlertDialog() {
        val builder: AlertDialog.Builder =
            AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
        builder.setTitle("JobsForHer")
        builder.setCancelable(false)
        builder.setMessage("A new version of the app is available. Please update it now")
        builder.setPositiveButton(
            "Update"
        ) { dialog, which -> HelperMethods.openPlayStore(this@SplashActivity) }
        builder.setNegativeButton(
            "Later"
        ) { dialog, which ->
            callIntent(fcm)
            finish()
        }
        builder.show()
    }

}