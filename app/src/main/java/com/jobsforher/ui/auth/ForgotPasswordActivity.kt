package com.jobsforher.ui.auth

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.jobsforher.R
import com.jobsforher.databinding.ActivityForgotpasswordNewBinding
import com.jobsforher.util.Utility
import kotlinx.android.synthetic.main.activity_forgotpassword_new.*
import women.jobs.jobsforher.activities.BaseActivity

class ForgotPasswordActivity : BaseActivity() {

    private val TAG = javaClass.simpleName
    val viewModel: ForgotPasswordViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityForgotpasswordNewBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_forgotpassword_new)
        binding?.viewmodel = viewModel
        binding?.lifecycleOwner = this

        initObservables()
        clickListener()

    }

    private fun initObservables() {

        viewModel.inputError.observe(this, androidx.lifecycle.Observer {
            Utility.showToast(this, it)
        })

        viewModel.errorMessage.observe(this, androidx.lifecycle.Observer {
            Utility.showToast(this, it)
            finish()
        })
    }

    private fun clickListener() {
        btn_Back.setOnClickListener {

            onBackPressed()
        }
    }
}
