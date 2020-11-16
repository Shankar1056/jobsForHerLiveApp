package com.jobsforher.activities

import android.os.Bundle
import com.jobsforher.R
import women.jobs.jobsforher.activities.BaseActivity


class WelcomeActivity : BaseActivity() {

    private val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_welcome)


    }

}
