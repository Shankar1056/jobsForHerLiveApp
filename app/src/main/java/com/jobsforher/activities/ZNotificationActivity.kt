package com.jobsforher.activities


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.gson.Gson
import com.jobsforher.R
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.network.responsemodels.RegisterIdResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import kotlinx.android.synthetic.main.activity_splash.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ZNotificationActivity() : AppCompatActivity() {


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
    var i:Int =0;
    var fcm:String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val mHandler = Handler(mainLooper)
        val mRunnable = Runnable {
        }
        mHandler.postDelayed(mRunnable, 500)
        val fade_in = ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
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
                    callIntent()
                }
                mHandler.postDelayed(mRunnable, 500)
            }
            override fun onAnimationRepeat(animation: Animation) {
            }
        })
    }

    var notification_type:String = ""
    var entity_type:String = ""

    fun callIntent(){
        val sharedPref: SharedPreferences = getSharedPreferences("mysettings",
            Context.MODE_PRIVATE);
        val editor = sharedPref.edit()
        editor.putString(PREF_FCM,fcm)
        editor.commit()

        EndPoints.ACCESS_TOKEN = sharedPref.getString(PREF_ACCESSTOKEN,"access")!!


        var bundle: Bundle ?=intent.extras!!
        if (bundle != null) {
            notification_type = bundle!!.getString("notification_type").toString()
            entity_type = bundle!!.getString("entity_type").toString()
            Log.d("TAGG", "TYPE:"+notification_type)
            Log.d("TAGG", "REPLY" + bundle!!.getString("comment_id").toString())

//            if(notification_type.equals("group_details")) {
//                val bundle1 = Bundle()
//                val intent = Intent(applicationContext, ZActivityGroupsDetails::class.java)
//                bundle1.putBoolean("isLoggedIn",false)
//                bundle1.putString("group_type", bundle!!.getString("group_type").toString())
//                bundle1.putString("group_id",  bundle!!.getString("entity_id").toString())
//                bundle1.putInt("isMygroup",1)
//                bundle1.putString("page","NewsFeed")
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                intent.putExtras(bundle1)
//                startActivity(intent)
//
//            }
//
//            else `
            if(entity_type.equals("post")){
                val intent = Intent(applicationContext, DetailsNotificationActivity::class.java)
                //intent.putExtra("isLoggedIn",true)
                intent.putExtra("post_id", bundle!!.getString("entity_id").toString())
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent)
            }

            else if(entity_type.equals("comment")){
                val intent = Intent(applicationContext, DetailsNotificationActivity::class.java)
                //intent.putExtra("isLoggedIn",true)
                intent.putExtra("comment_id", bundle!!.getString("entity_id").toString())
                intent.putExtra("post_id", bundle!!.getString("post_id").toString())
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent)
            }

            else if(entity_type.equals("reply")){
                val intent = Intent(applicationContext, DetailsNotificationActivity::class.java)
                //intent.putExtra("isLoggedIn",true)
                intent.putExtra("reply_id", bundle!!.getString("entity_id").toString())
                intent.putExtra("post_id", bundle!!.getString("post_id").toString())
                intent.putExtra("comment_id", bundle!!.getString("comment_id").toString())
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent)
            }


            else if(notification_type.equals("job_details")){
                val intent = Intent(applicationContext, ZActivityJobDetails::class.java)
                intent.putExtra("isLoggedIn",true)
                intent.putExtra("group_Id", Integer.parseInt(bundle!!.getString("entity_id").toString()))
                intent.putExtra("title", "")
                intent.putExtra("isMygroup",1)
                intent.putExtra("page","NewsFeed")
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent)
            }

            else if(notification_type.equals("company_details")){
                val intent = Intent(applicationContext, ZActivityCompanyDetails::class.java)
                intent.putExtra("isLoggedIn",true)
                intent.putExtra("group_Id", Integer.parseInt(bundle!!.getString("entity_id").toString()))
                intent.putExtra("title", "")
                intent.putExtra("isMygroup",1)
                intent.putExtra("page","NewsFeed")
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent)
            }

            else if(notification_type.equals("events_details")){
                val intent = Intent(applicationContext, ZActivityEventDetails::class.java)
                intent.putExtra("isLoggedIn",true)
                intent.putExtra("group_Id", Integer.parseInt(bundle!!.getString("entity_id").toString()))
                intent.putExtra("title", "")
                intent.putExtra("isMygroup",1)
                intent.putExtra("page","NewsFeed")
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent)
            }
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


}