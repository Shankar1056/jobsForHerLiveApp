package com.jobsforher.ui.newsfeed

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.jobsforher.BuildConfig
import com.jobsforher.R
import com.jobsforher.activities.*
import com.jobsforher.data.model.NewsPostBody
import com.jobsforher.helpers.Constants
import com.jobsforher.helpers.HelperMethods
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.ui.newsfeed.adapter.*
import com.jobsforher.ui.preference.fragment.CityFragment
import com.jobsforher.ui.preference.fragment.FunctionalAreaFragment
import com.jobsforher.util.Utility
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_newsfeed.*
import kotlinx.android.synthetic.main.toolbar_groups.*
import kotlinx.android.synthetic.main.zactivity_newsfeed.*

class NewsFeedActivity : Footer(), NavigationView.OnNavigationItemSelectedListener {
    val viewModel: NewsFeedViewModel by viewModels()
    private var clickedPos: Int? = null
    lateinit var newsPostAdapter: NewsFeedAdapter
    lateinit var allNewsPostAdapter: AllNewsFeedAdapter
    var newsPostList = ArrayList<NewsPostBody>()
    private var loading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zactivity_newsfeed)

        initWidgit()

        clickListener()

        handleBackgrounds(btnHome)

        listenViewModelData()

    }


    private fun listenViewModelData() {
        viewModel.jobsResponseList.observe(this, Observer {
            jobs_recycler_view.adapter = JobsAdapter(it, object : JobsAdapter.JobsItemClicked {
                override fun onApplyClicked(pos: Int) {

                    startActivity(
                        Intent(this@NewsFeedActivity, ZActivityJobDetails::class.java)
                            .putExtra("isLoggedIn", false)
                            .putExtra("group_Id", it[pos].id)
                            .putExtra("isRequired", it[pos].resume_required)
                            .putExtra("isboosted", it[pos].boosted)
                            .putExtra("title", it[pos].title)
                            .putExtra("page", "NewsFeed")
                    )


                }

            })
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
                    allNewsPostAdapter.notifyDataSetChanged()
                }
            }
        })

        viewModel.newsPostResponseList.observe(this, Observer {
            newsPostList.addAll(it)

            if (newsPostList.size <= 10) {
                newsPostAdapter.notifyDataSetChanged()
            }
            allNewsPostAdapter.notifyDataSetChanged()
            if (my_swipeRefresh_Layout.isRefreshing) {
                my_swipeRefresh_Layout.isRefreshing = false
            }
            loading = true
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
            if (it == "HTTP 412 PRECONDITION FAILED") {
                Utility.sessionExpiredPopup(
                    this@NewsFeedActivity,
                    resources.getString(R.string.server412Message)
                )
            }
        })

        viewModel.successMessage.observe(this, Observer {
            Utility.showToast(this@NewsFeedActivity, it)
        })

        viewModel.isResumeUploaded.observe(this, Observer {
            // resume_layout.visibility = View.GONE
            uploadNow.text = resources.getString(R.string.text_resume_uploaded)
            later.visibility = View.GONE
        })

        viewModel.notificationCount.observe(this, Observer {
            cart_badge.text = it.toString()
        })

        viewModel.switchPreferenceName.observe(this, Observer {
            when (it) {
                NewsFeedViewModel.PreferenceName.PREFERRED_CITY -> goToFragment(CityFragment())
                NewsFeedViewModel.PreferenceName.PREFERRED_FUNCTIONAL_AREA -> goToFragment(FunctionalAreaFragment(Constants.TYPE_AREAS))
                NewsFeedViewModel.PreferenceName.PREFERRED_INDUSTRIES -> goToFragment(FunctionalAreaFragment(Constants.TYPE_INDUSTRIES))
                NewsFeedViewModel.PreferenceName.PREFERRED_JOB_TYPE -> goToFragment(FunctionalAreaFragment(Constants.TYPE_JOB))
            }
        })

        viewModel.allPreferenceUpdated.observe(this, Observer {
            preference_layout.visibility = View.GONE
        })
    }

    private fun goToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.preferenceFrameLayout, fragment).commit()
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
        mappingWidgets()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close
        )
        toggle.getDrawerArrowDrawable().color = Color.WHITE
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        val hView = nav_view.getHeaderView(0)

        val profile_name = hView.findViewById(R.id.profile_name) as TextView
        val img_profile_sidemenu =
            hView.findViewById(R.id.img_profile_sidemenu) as CircleImageView
        profile_name.setText(EndPoints.USERNAME)
        if (EndPoints.PROFILE_ICON.length > 4) {
            Picasso.with(applicationContext)
                .load(EndPoints.PROFILE_ICON)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(img_profile_sidemenu)
            Picasso.with(applicationContext)
                .load(EndPoints.PROFILE_ICON)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(img_profile_toolbar)
        }

        version.text = "Version - " + BuildConfig.VERSION_NAME

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
                startActivity(
                    Intent(this@NewsFeedActivity, ZActivityCommentPage::class.java)
                        .putExtra("comment_Id", newsPostList[pos].id)
                )

            }

            override fun onCommentClicked(pos: Int, comment: String) {
                startActivity(
                    Intent(this@NewsFeedActivity, ZActivityCommentPage::class.java)
                        .putExtra("comment_Id", newsPostList[pos].id)
                )
            }
        })

        posts_recycler_view.adapter = newsPostAdapter

        allNewsPostAdapter = AllNewsFeedAdapter(newsPostList, object : NewsFeedClickLietener {
            override fun onUpVoteClicked(pos: Int) {
                clickedPos = pos
                viewModel.upVoteClicked(newsPostList[pos].id)
            }

            override fun onDownVoteClicked(pos: Int) {
                clickedPos = pos
                viewModel.downVoteClicked(newsPostList[pos].id)
            }

            override fun onCommentCountClicked(pos: Int) {
                startActivity(
                    Intent(this@NewsFeedActivity, ZActivityCommentPage::class.java)
                        .putExtra("comment_Id", newsPostList[pos].id)
                )
            }

            override fun onCommentClicked(pos: Int, comment: String) {
                startActivity(
                    Intent(this@NewsFeedActivity, ZActivityCommentPage::class.java)
                        .putExtra("comment_Id", newsPostList[pos].id)
                )
            }
        })

        posts_recycler_view1.adapter = allNewsPostAdapter

        uploadNow.setOnClickListener {
            if (uploadNow.text == resources.getString(R.string.text_upload_now)) {
                viewModel.startIntentToOpenFile(this@NewsFeedActivity)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NewsFeedViewModel.GALLERY_PDF) {

            if (data != null) {
                viewModel.uploadResume(data, this@NewsFeedActivity)
            }
        }
    }

    private fun clickListener() {

        nav_view.setNavigationItemSelectedListener(this)

        notifLayout.setOnClickListener {
            cart_badge.setText("0")
            startActivity(Intent(applicationContext, Notification::class.java))
        }

        jobssheader1.setOnClickListener {
            startActivity(
                Intent(applicationContext, ZActivityJobs::class.java)
                    .putExtra("isLoggedIn", true)
            )
        }
        companiessheader1.setOnClickListener {
            startActivity(
                Intent(applicationContext, ZActivityCompanies::class.java)
                    .putExtra("isLoggedIn", true)
            )
        }
        groupssheader1.setOnClickListener {
            startActivity(
                Intent(applicationContext, ZActivityGroups::class.java)
                    .putExtra("isLoggedIn", true)
            )
        }

        img_profile_toolbar.setOnClickListener {
            startActivity(
                Intent(applicationContext, ZActivityDashboard::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    .putExtra("isLoggedIn", true)
                    .putExtra("pagetype", 0)
            )
        }
        my_swipeRefresh_Layout.setOnRefreshListener {
            newsPostList.clear()
            viewModel.loadGroupPosts("1", Constants.MAXIMUM_PAGINATION_COUNT)
            onResume()
        }

        mainScroll_grpdetails.getViewTreeObserver()
            .addOnScrollChangedListener(object : ViewTreeObserver.OnScrollChangedListener {
                override fun onScrollChanged() {
                    if (!loading) {
                        return
                    }

                    Log.d("TAGG", "END EFORE")
                    val view: View =
                        mainScroll_grpdetails.getChildAt(mainScroll_grpdetails.getChildCount() - 1);

                    val diff: Int =
                        (view.getBottom() - (mainScroll_grpdetails.getHeight() + mainScroll_grpdetails.getScrollY()));

                    if (diff == 0 && loading) {
                        if (viewModel.newsPostpagination != null && viewModel.newsPostpagination.has_next != null) {
                            if (viewModel.newsPostpagination.has_next!!) {
                                viewModel.loadGroupPosts(
                                    viewModel.newsPostpagination.next_page!!,
                                    Constants.MAXIMUM_PAGINATION_COUNT
                                )
                                loading = false
                            }
                        }

                    }
                }
            })

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
            R.id.action_sett -> {
                if (m.findItem(R.id.action_logout).isVisible == true)
                    m.findItem(R.id.action_logout).setVisible(false)
                else
                    m.findItem(R.id.action_logout).setVisible(true)
            }
            R.id.action_logout -> {
                val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
                drawerLayout.closeDrawer(GravityCompat.START)

                Utility.doLogoutPopup(
                    this@NewsFeedActivity,
                    resources.getString(R.string.logout_warning_message)
                )

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

    override fun onResume() {
        super.onResume()
        viewModel.loadNotificationbubble()
    }
}