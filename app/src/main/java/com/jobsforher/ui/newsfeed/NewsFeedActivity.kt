package com.jobsforher.ui.newsfeed

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jobsforher.R

class NewsFeedActivity : AppCompatActivity() {
    val viewModel: NewsFeedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zactivity_newsfeed)

        initWidgit()
        callAPI()
    }

    private fun callAPI() {

    }

    private fun initWidgit() {


    }
}