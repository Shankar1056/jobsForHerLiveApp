package com.jobsforher.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_signup_process.*
import com.jobsforher.R
import women.jobs.jobsforher.activities.BaseActivity

class SignUpProcessActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_signup_process)

        btnEducation.setOnClickListener {


            btnAddEducation.visibility= View.VISIBLE
            btnAddWork.visibility= View.GONE
            btnAddLifeExp.visibility= View.GONE
            btnAddSkills.visibility= View.GONE
        }

        btnWork.setOnClickListener {

            btnAddEducation.visibility= View.GONE
            btnAddWork.visibility= View.VISIBLE
            btnAddLifeExp.visibility= View.GONE
            btnAddSkills.visibility= View.GONE
        }

        btnLifeExp.setOnClickListener {

            btnAddEducation.visibility= View.GONE
            btnAddWork.visibility= View.GONE
            btnAddLifeExp.visibility= View.VISIBLE
            btnAddSkills.visibility= View.GONE

        }

        btnSkills.setOnClickListener {


            btnAddEducation.visibility= View.GONE
            btnAddWork.visibility= View.GONE
            btnAddLifeExp.visibility= View.GONE
            btnAddSkills.visibility= View.VISIBLE

        }


        btnAddEducation.setOnClickListener {

            var intent = Intent(this, com.jobsforher.activities.AddEducationStatusActivity::class.java)
            startActivity(intent)
        }
    }

}


