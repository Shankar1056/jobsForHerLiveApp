package com.jobsforher.ui.newsfeed

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.jobsforher.R
import com.jobsforher.activities.*
import com.jobsforher.data.model.NewsPostBody
import com.jobsforher.helpers.HelperMethods
import com.jobsforher.ui.newsfeed.adapter.*
import com.jobsforher.util.Utility
import kotlinx.android.synthetic.main.activity_newsfeed.*
import kotlinx.android.synthetic.main.zactivity_newsfeed.*

class NewsFeedActivity : Footer(), NavigationView.OnNavigationItemSelectedListener {
    val viewModel: NewsFeedViewModel by viewModels()
    private var clickedPos: Int? = null
    lateinit var newsPostAdapter: NewsFeedAdapter
    var newsPostList = ArrayList<NewsPostBody>()

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

        viewModel.voteResponse.observe(this, Observer {
            if (it != null) {
                if (clickedPos != null) {
                    newsPostList[clickedPos!!].upvote_count = it.upvote_count
                    newsPostList[clickedPos!!].downvote_count = it.downvote_count
                    newsPostAdapter.notifyDataSetChanged()
                }
            }
        })

        viewModel.newsPostResponseList.observe(this, Observer {
            newsPostList.clear()
            newsPostList.addAll(it)
            Log.i("response :::", newsPostList[0].post_type)

            newsPostAdapter.notifyDataSetChanged()

        })

        viewModel.homeBannerResponse.observe(this, Observer {
            jobsAds_recycler_view.layoutManager = LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL,
                false
            )
            jobsAds_recycler_view.adapter = HomeBannerAdapter(it)

            val handler = Handler()
            handler.postDelayed(object : Runnable {
                override fun run() {
                    scroll(it.size)
                    handler.postDelayed(this, 5000)//1 sec delay
                }
            }, 0)
        })

        viewModel.errorMessage.observe(this, Observer {
            Utility.showToast(this@NewsFeedActivity, it)
        })
    }
    var position: Int = 0
    fun scroll(size: Int) {
        if (position == size) {
            position = 0;

        } else {
            position++
        }
        jobsAds_recycler_view!!.smoothScrollToPosition(position)
    }

    private fun initWidgit() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close
        )
        toggle.getDrawerArrowDrawable().color = Color.WHITE
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        newsPostAdapter = NewsFeedAdapter(newsPostList, object : NewsFeedClickLietener {
            override fun onUpVoteClicked(pos: Int) {
                clickedPos = pos
                viewModel.upVoteClicked(newsPostList[pos].id)
            }

            override fun onDownVoteClicked(pos: Int) {
                clickedPos = pos
                viewModel.downVoteClicked(newsPostList[pos].id)
            }

            override fun onCommentCountClicked(pos: Int) {
                TODO("Not yet implemented")
            }

            override fun onCommentClicked(pos: Int, comment: String) {
                TODO("Not yet implemented")
            }
        })


        posts_recycler_view.adapter = newsPostAdapter
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val navView: NavigationView = findViewById(R.id.nav_view)
        val m = navView.getMenu()

        when (item.itemId) {

            R.id.action_dashboard -> {
               goToTargetActivity(ZActivityDashboard())
            }
            R.id.action_groups -> {
                goToTargetActivity(ZActivityGroups())

            }
            R.id.action_jobs -> {
                goToTargetActivity(ZActivityJobs())
            }
            R.id.action_companies -> {
                goToTargetActivity(ZActivityCompanies())
            }
            R.id.action_reskilling -> {
                intent = Intent(applicationContext, ZActivityDashboard::class.java)
                intent.putExtra("isLoggedIn", true)
                startActivity(intent)
                closeDrawer()
            }
            R.id.action_mentors -> {
                intent = Intent(applicationContext, WebActivity::class.java)
                intent.putExtra("value", "https://www.jobsforher.com/mentors")
                startActivity(intent)
                closeDrawer()
            }
            R.id.action_events -> {
                goToTargetActivity(ZActivityEvents())

            }
            R.id.action_blogs -> {
                intent = Intent(applicationContext, WebActivity::class.java)
                intent.putExtra("value", "https://www.jobsforher.com/blogs")
                startActivity(intent)
                closeDrawer()
            }
            R.id.action_sett -> {
                if (m.findItem(R.id.action_logout).isVisible == true)
                    m.findItem(R.id.action_logout).setVisible(false)
                else
                    m.findItem(R.id.action_logout).setVisible(true)
                //m.findItem(R.id.action_logout).setVisible(true)
            }
            R.id.action_logout -> {
                val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
                drawerLayout.closeDrawer(GravityCompat.START)

                Utility.doLogoutPopup(this@NewsFeedActivity)

            }
            R.id.action_share -> {
                HelperMethods.showAppShareOptions(this@NewsFeedActivity)
                closeDrawer()
            }
        }


        return true
    }

    private fun goToTargetActivity(activity: Any) {
        drawer_layout.closeDrawer(GravityCompat.START)
        intent = Intent(applicationContext, activity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.putExtra("isLoggedIn", true)
        startActivity(intent)
        closeDrawer()
    }

    private fun closeDrawer() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
    }
}