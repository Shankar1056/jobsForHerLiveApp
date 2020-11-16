package com.jobsforher.activities

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.arindicatorview.ARIndicatorView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_view_pager.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.jobsforher.R
import com.jobsforher.adapters.ViewPagerAdapter
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.models.ImageModel
import com.jobsforher.network.responsemodels.RegisterIdResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import java.util.HashMap

class ViewPagerActivity : AppCompatActivity() {

    lateinit var viewPager: ViewPager
    lateinit var arIndicatorView: ARIndicatorView
    lateinit var textView: TextView

    var listOfImage : ArrayList<ImageModel> = ArrayList()

    private val TAG = javaClass.simpleName
    private var retrofitInterface: RetrofitInterface? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager)

        registerSocialToken(
            Settings.Secure.getString(applicationContext.getContentResolver(),
                Settings.Secure.ANDROID_ID))

        listOfImage.add(ImageModel(R.drawable.screen_one))
        listOfImage.add(ImageModel(R.drawable.screen_two))
        listOfImage.add(ImageModel(R.drawable.screen_three))
        listOfImage.add(ImageModel(R.drawable.screen_four))
        listOfImage.add(ImageModel(R.drawable.screen_five))
        listOfImage.add(ImageModel(R.drawable.screen_six))

        this.supportActionBar?.apply {
            title = "View Pager"
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        this.viewPager = view_pager
        this.arIndicatorView = ar_indicator_viewpager
        this.viewPager.adapter = ViewPagerAdapter(this, listOfImage)
        this.arIndicatorView.attachTo(viewPager)

        btnSignIn.setOnClickListener {

            var intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

        }

        btnSignUp.setOnClickListener {

            var intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    private fun registerSocialToken(device_id: String) {

        val params = HashMap<String, String>()

        val installationId = device_id

        params["device_id"] = installationId

        Logger.d(TAG, "Params : " + Gson().toJson(params))

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.registerInstallationId("application/json", EndPoints.CLIENT_ID, params)
        call.enqueue(object : Callback<RegisterIdResponse> {

            override fun onResponse(call: Call<RegisterIdResponse>, response: Response<RegisterIdResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGESplash", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
                Logger.d("URL", "" + response)


                if (response.isSuccessful) {
//                    ToastHelper.makeToast(applicationContext, response.body()!!.auth!!.access_token)
                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())

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

}
