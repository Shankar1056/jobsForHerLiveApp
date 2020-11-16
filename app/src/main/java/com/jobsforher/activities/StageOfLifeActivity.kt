package com.jobsforher.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_stage_life_up.*
import com.jobsforher.R
import com.jobsforher.helpers.ToastHelper
import women.jobs.jobsforher.activities.BaseActivity

class StageOfLifeActivity : BaseActivity() {

    private val TAG = javaClass.simpleName

    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stage_life_up)

        llStudying.setOnClickListener {

            val intent = Intent(applicationContext, SelectingLocationActivity::class.java)
            intent.putExtra("type", "starter")
            //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

        }

        llOnBreak.setOnClickListener {

            val intent = Intent(applicationContext, SelectingLocationActivity::class.java)
            intent.putExtra("type", "restarter")
            //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

        }

        llCurrentlyWorking.setOnClickListener {

            val intent = Intent(applicationContext, SelectingLocationActivity::class.java)
            intent.putExtra("type", "riser")
            //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

        }

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