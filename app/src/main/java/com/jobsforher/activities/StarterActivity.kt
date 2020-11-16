package com.jobsforher.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_starter.*
import kotlinx.android.synthetic.main.layout_bottom.*
import com.jobsforher.R
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import women.jobs.jobsforher.activities.BaseActivity

class StarterActivity : BaseActivity() {

    private val TAG = javaClass.simpleName
    private var retrofitInterface: RetrofitInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starter)

        progressBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        lets_start_showcasing.setOnClickListener {

            linear_ques_one.visibility= View.VISIBLE
            sub_content_view.visibility= View.GONE

        }

        btnOk.setOnClickListener {

            linear_ques_two.visibility = View.VISIBLE
            linear_ques_one.visibility= View.GONE

        }

        btnOkTwo.setOnClickListener {

            linear_ques_three.visibility = View.VISIBLE
            linear_ques_two.visibility = View.GONE

        }

        btnOkThree.setOnClickListener {

            linear_ques_four.visibility = View.VISIBLE
            linear_ques_three.visibility = View.GONE

        }

        btnOkFour.setOnClickListener {

            starter_info_view.visibility = View.VISIBLE
            linear_ques_four.visibility = View.GONE

        }

        btnWldLuvTo.setOnClickListener {

            var intent = Intent(this, StarterEducationActivity::class.java)
            startActivity(intent)

        }

    }

}