package com.jobsforher.ui.newsfeed

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.jobsforher.R
import com.jobsforher.ui.newsfeed.adapter.CompaniesAdapter
import com.jobsforher.ui.newsfeed.adapter.GroupsAdapter
import com.jobsforher.ui.newsfeed.adapter.JobsAdapter
import com.jobsforher.util.Utility
import kotlinx.android.synthetic.main.activity_newsfeed.*

class NewsFeedActivity : AppCompatActivity() {
    val viewModel: NewsFeedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zactivity_newsfeed)

        initWidgit()

        listenViewModelData()
    }

    private fun listenViewModelData() {
        viewModel.jobsResponseList.observe(this, Observer {
            jobs_recycler_view.adapter = JobsAdapter(it)
        })

        viewModel.companiesResponseList.observe(this, Observer {
            companies_recycler_view.adapter = CompaniesAdapter(it)
        })

        viewModel.groupsResponseList.observe(this, Observer {
            groups_recycler_view.adapter = GroupsAdapter(it)
        })

        viewModel.errorMessage.observe(this, Observer {
            Utility.showToast(this@NewsFeedActivity, it)
        })
    }


    private fun initWidgit() {

    }
}