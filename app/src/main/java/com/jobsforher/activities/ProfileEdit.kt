package com.jobsforher.activities

import android.os.Bundle
import com.jobsforher.R
import com.jobsforher.models.ProfileModel
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import women.jobs.jobsforher.activities.BaseActivity
import java.util.*


private var retrofitInterface: RetrofitInterface? = null
var listOfProfiledataEdit: ArrayList<ProfileModel> = ArrayList()



class ProfileEdit : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_profileedit)
//        listOfProfiledataEdit=intent.getStringArrayListExtra("data")
    }

}