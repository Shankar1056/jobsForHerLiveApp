package com.jobsforher.activities

//import com.msg91.sendotpandroid.library.internal.SendOTP
//import com.msg91.sendotpandroid.library.roots.SendOTPConfigBuilder
import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jobsforher.R
import com.jobsforher.adapters.*
import com.jobsforher.helpers.HelperMethods
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.models.*
import com.jobsforher.models.EventCompanies
import com.jobsforher.models.Following
import com.jobsforher.models.Rec_Groups
import com.jobsforher.network.responsemodels.*
import com.jobsforher.network.retrofithelpers.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.bottom_sheet_applyjobs.*
import kotlinx.android.synthetic.main.toolbar_groups.*
import kotlinx.android.synthetic.main.zdashboard_content_main.*
import kotlinx.android.synthetic.main.zdashboard_content_main.mobile
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.net.URLConnection
import java.net.URLEncoder
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set


class ZActivityDashboard : Footer(), NavigationView.OnNavigationItemSelectedListener {

    private val TAG = "ZActivityDashboard"
    private var retrofitInterface: RetrofitInterface? = null
    private var retrofitInterface1: RetrofitInterface? = null
    private var awesomeValidation: AwesomeValidation? = null
    var isLoggedIn = false
    var groupId = 0
    var userId = 0
    var mRecyclerViewPosts: RecyclerView? = null
    var mRecyclerViewGroups: RecyclerView? = null
    var mRecyclerViewCompanies: RecyclerView? = null
    var mRecyclerViewEvents: RecyclerView? = null
    var mRecyclerViewResumes: RecyclerView? = null
    var mRecyclerViewJobAlert: RecyclerView? = null
    var mRecyclerViewFollowing: RecyclerView? = null
    private val PREF_PERCENTAGE = "0"
    private val PREF_VERIFIED_EMAIL = ""

    private var awesomeValidation_starter: AwesomeValidation? = null
    private var awesomeValidation_riser: AwesomeValidation? = null
    private var awesomeValidation_restarter: AwesomeValidation? = null

    var pemail: String = ""

    var listOfPreferencedata: ArrayList<PreferenceModel> = ArrayList()

    var mAdapterJobs: RecyclerView.Adapter<*>? = null
    var mAdapterGroups: RecyclerView.Adapter<*>? = null
    var mAdapterEvents: RecyclerView.Adapter<*>? = null
    var mAdapterCompanies: RecyclerView.Adapter<*>? = null
    var mAdapterResumesDashboard: RecyclerView.Adapter<*>? = null
    var mAdapterJobAlert: RecyclerView.Adapter<*>? = null

    var listOfhotJobsdata: ArrayList<JobsView> = ArrayList()
    var listOfhotJobsdataTrimmed: ArrayList<JobsView> = ArrayList()
    var listOfCompareJoineddata: ArrayList<Int> = ArrayList()
    var listOfSavedJobsdata: ArrayList<JobsView> = ArrayList()
    var listOfSavedJobsdataTrimmed: ArrayList<JobsView> = ArrayList()
    var listOfAppliedJobsdata: ArrayList<JobsView> = ArrayList()
    var listOfAppliedJobsdataTrimmed: ArrayList<JobsView> = ArrayList()

    var listOfGroupdata: ArrayList<GroupsView> = ArrayList()
    var listOfCompareGroupdata: ArrayList<GroupsView> = ArrayList()
    var listOfGroupdataTrimmed: ArrayList<GroupsView> = ArrayList()
    var listOfMyGroupdata: ArrayList<GroupsView> = ArrayList()
    var listOfMyGroupdataTrimmed: ArrayList<GroupsView> = ArrayList()

    var listOfCompaniesdataTrimmed: ArrayList<CompaniesView> = ArrayList()
    var listOfCompaniesdata: ArrayList<CompaniesView> = ArrayList()
    var listOfMyCompaniesdataTrimmed: ArrayList<CompaniesView> = ArrayList()
    var listOfMyCompaniesdata: ArrayList<CompaniesView> = ArrayList()


    var listOfEventsdata: ArrayList<EventsView> = ArrayList()
    var listOfEventsdataTrimmed: ArrayList<EventsView> = ArrayList()
    var listOfMyEventsdataTrimmed: ArrayList<EventsView> = ArrayList()
    var listOfMyEventsdata: ArrayList<EventsView> = ArrayList()

    var listOfCategories: ArrayList<CategoryView> = ArrayList()
    var listOfCities: ArrayList<CityView> = ArrayList()
    var listOfJobType: ArrayList<JobTypeView> = ArrayList()
    var listOfJobFArea: ArrayList<FunctionalAreaView> = ArrayList()
    var listOfJobFAreaSorted: ArrayList<FunctionalAreaView> = ArrayList()
    var listOfJobIndustry: ArrayList<FunctionalAreaView> = ArrayList()
    private val GALLERY_PDF = 3

    var typeJobsClick: Int = 1
    var typeGroupClick: Int = 1
    var typeCompanyClick: Int = 1
    var typeEventClick: Int = 1

    var hasnextRecommendedJobs: Boolean = true
    var pagenoRecommendedJobs: String = "1"
    var pagenoAppliedJobs: String = "1"
    var hasnextAppliedJobs: Boolean = true
    var pagenoSavedJobs: String = "1"
    var hasnextSavedJobs: Boolean = false
    var pagenoMyGroups: String = "1"
    var hasnextMyGroups: Boolean = true
    var hasnextRecommendedGroups: Boolean = true
    var pagenoRecommendedGroups: String = "1"
    var hasnextRecommendedCompanies: Boolean = true
    var pagenoRecommendedCompanies: String = "1"
    var pagenoFollowedCompanies: String = "1"
    var hasnextFollowedCompanies: Boolean = true
    var hasnextRecommendedEvents: Boolean = true
    var pagenoRecommendedEvents: String = "1"
    var pagenoAppEvents: String = "1"
    var hasnextAppEvents: Boolean = true
    private val PREF_USERID = "id"

    var pageType: Int = 0
    var resumesdata_Separate: ArrayList<MyResume> = ArrayList()
    private var pStatus = 0
    private var handler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zactivity_dashboard)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        isLoggedIn = intent.getBooleanExtra("isLoggedIn", false)
        groupId = intent.getIntExtra("group_Id", 0)
        pageType = intent.getIntExtra("pagetype", 0)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close
        )
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        loadNotificationbubble()
        mappingWidgets()

        mRecyclerViewPosts = findViewById(R.id.posts_recycler_view)
        mRecyclerViewGroups = findViewById(R.id.groups_recycler_view)
        mRecyclerViewCompanies = findViewById(R.id.companies_recycler_view)
        mRecyclerViewEvents = findViewById(R.id.events_recycler_view)
        mRecyclerViewResumes = findViewById(R.id.resumes_recycler_view)
        mRecyclerViewJobAlert = findViewById(R.id.jobalert_recycler_view)
        mRecyclerViewFollowing = findViewById(R.id.following_recycler_view)


        img_profile_toolbar.setOnClickListener {
            intent = Intent(applicationContext, ZActivityDashboard::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("isLoggedIn", true)
            intent.putExtra("pagetype", 0)
            startActivity(intent)
        }


        if (isLoggedIn) {

            if (navView != null) {
                val menu = navView.menu
                menu.findItem(R.id.action_employerzone).setVisible(false)
                menu.findItem(R.id.action_mentorzone).setVisible(false)
                menu.findItem(R.id.action_partnerzone).setVisible(false)
                menu.findItem(R.id.action_logout).setVisible(false)
                menu.findItem(R.id.action_settings).setVisible(false)
                menu.findItem(R.id.action_signup).setVisible(false)
                menu.findItem(R.id.action_login).setVisible(false)
                menu.findItem(R.id.action_dashboard).title =
                    Html.fromHtml("<font color='#99CA3B'>Dashboard</font>")
                // menu.findItem(R.id.action_dashboard).getIcon().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
                navView.setNavigationItemSelectedListener(this)
                val hView = navView.getHeaderView(0)
                val loggedin_header = hView.findViewById(R.id.loggedin_layout) as RelativeLayout
                val nologgedin_layout = hView.findViewById(R.id.nologgedin_layout) as RelativeLayout
                loggedin_header.visibility = View.VISIBLE
                nologgedin_layout.visibility = View.GONE
                val profile_name = hView.findViewById(R.id.profile_name) as TextView
                val img_profile_sidemenu =
                    hView.findViewById(R.id.img_profile_sidemenu) as CircleImageView
                //val img_profile_toolbar = hView.findViewById(R.id.img_profile_toolbar) as CircleImageView
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

                loggedin_header.setOnClickListener {
                    intent = Intent(applicationContext, ProfileView::class.java)
                    startActivity(intent)
                }
            }
        } else {
            if (navView != null) {
                val menu = navView.menu
                menu.findItem(R.id.action_employerzone).setVisible(true)
                menu.findItem(R.id.action_mentorzone).setVisible(true)
                menu.findItem(R.id.action_partnerzone).setVisible(true)
                menu.findItem(R.id.action_logout).setVisible(false)
                menu.findItem(R.id.action_settings).setVisible(false)
                menu.findItem(R.id.action_signup).setVisible(true)
                menu.findItem(R.id.action_login).setVisible(true)
                menu.findItem(R.id.action_dashboard).title =
                    Html.fromHtml("<font color='#99CA3B'>Dashboard</font>")
//                menu.findItem(R.id.action_dashboard).getIcon().setColorFilter(get, PorterDuff.Mode.SRC_ATOP);
//                menu.findItem(R.id.action_jobs).getIcon().setColorFilter("#99CA3B", PorterDuff.Mode.SRC_ATOP);
                navView.setNavigationItemSelectedListener(this)
                val hView = navView.getHeaderView(0)
                val loggedin_header = hView.findViewById(R.id.loggedin_layout) as RelativeLayout
                val nologgedin_layout = hView.findViewById(R.id.nologgedin_layout) as RelativeLayout
                loggedin_header.visibility = View.GONE
                nologgedin_layout.visibility = View.VISIBLE
            }
        }



        loadCategoryData()

        if (isLoggedIn) {
            img_profile_toolbar.visibility = View.VISIBLE
//            notificaton.visibility = View.VISIBLE
            sign_in.visibility = View.GONE
//            mygroups.visibility = View.VISIBLE
////            mygroups_selected.visibility = View.VISIBLE
////            listOfMyGroupdata.clear()
////            loadMyGroupData("1")
////            listOfCompareGroupdata.clear()
////            type_group=1
            dash_myjobs.setTypeface(null, Typeface.NORMAL);
            dash_mygroups.setTypeface(null, Typeface.NORMAL);
            dash_mycompanies.setTypeface(null, Typeface.NORMAL);
            dash_mymentors.setTypeface(null, Typeface.NORMAL);
            dash_myevents.setTypeface(null, Typeface.NORMAL);
            dash_accnt_settings.setTypeface(null, Typeface.NORMAL);
            dash_my_preferences.setTypeface(null, Typeface.NORMAL);
            dash_manage_resume.setTypeface(null, Typeface.NORMAL);
            dash_manage_jobalert.setTypeface(null, Typeface.NORMAL);
        } else {
            img_profile_toolbar.visibility = View.GONE
//            notificaton.visibility = View.GONE
            sign_in.visibility = View.VISIBLE
//            mygroups.visibility = View.GONE
//            mygroups_selected.visibility = View.GONE
//            featured_selected.visibility = View.VISIBLE
//            listOfMyGroupdata.clear()
//            loadFeaturedGroupData("1")
//            type_group=2
            dash_myjobs.setTypeface(null, Typeface.NORMAL);
            dash_mygroups.setTypeface(null, Typeface.NORMAL);
            dash_mycompanies.setTypeface(null, Typeface.NORMAL);
            dash_mymentors.setTypeface(null, Typeface.NORMAL);
            dash_myevents.setTypeface(null, Typeface.NORMAL);
            dash_accnt_settings.setTypeface(null, Typeface.NORMAL);
            dash_my_preferences.setTypeface(null, Typeface.NORMAL);
            dash_manage_resume.setTypeface(null, Typeface.NORMAL);
            dash_manage_jobalert.setTypeface(null, Typeface.NORMAL);
        }

        dashboard.setOnClickListener {
            intent = Intent(applicationContext, ZActivityDashboard::class.java)
            intent.putExtra("isLoggedIn", true)
            intent.putExtra("pagetype", 0)
            finish()
            startActivity(intent)

        }

        view_profile.setOnClickListener {
            intent = Intent(applicationContext, SignUpWelcomeActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("isLoggedIn", true)
            startActivity(intent)
        }

        edit_profile.setOnClickListener {
            edit_icon.callOnClick()
        }

        val sharedPref: SharedPreferences = getSharedPreferences(
            "mysettings",
            Context.MODE_PRIVATE
        )
        if (sharedPref.getString(PREF_PERCENTAGE, "0")!!.toInt() < 9) {
            view_profile.setText("Create Profile")
            edit_profile.visibility = View.GONE
        }

        edit_icon.setOnClickListener {
            val sharedPref: SharedPreferences = getSharedPreferences(
                "mysettings",
                Context.MODE_PRIVATE
            )
            if (sharedPref.getString(PREF_PERCENTAGE, "0")!!.toInt() > 9) {
                openEditProfile()
            } else {
                view_profile.callOnClick()
                //Toast.makeText(applicationContext, "Please create your profile to edit it", Toast.LENGTH_LONG).show()
            }
        }

        resumesupload.setOnClickListener {
            var array: ArrayList<ResumeView> = ArrayList()
            openBottomSheetUploadDoc(0, "", array)

        }

        resumesupload1.setOnClickListener {
            resumesupload.callOnClick()
        }

        jobalertupload.setOnClickListener {
            openBottomSheetJobAlerts(0)
        }

        jobalertupload1.setOnClickListener {
            jobalertupload.callOnClick()
        }

        //  loadDashboardData()

        default_company_companies.setOnClickListener {
            companiessheader1.callOnClick()
        }

        default_company_jobs.setOnClickListener {
            postsheader1.callOnClick()
        }

        default_company_groups.setOnClickListener {
            groupssheader1.callOnClick()
        }

        default_events_companies.setOnClickListener {
            eventssheader1.callOnClick()
        }


        dash_myjobs.setOnClickListener {
            dash_myjobs.setTextColor(resources.getColor(R.color.green))
            dash_mygroups.setTextColor(resources.getColor(R.color.black))
            dash_mycompanies.setTextColor(resources.getColor(R.color.black))
            dash_mymentors.setTextColor(resources.getColor(R.color.black))
            dash_myevents.setTextColor(resources.getColor(R.color.black))
            dash_accnt_settings.setTextColor(resources.getColor(R.color.black))
            dash_my_preferences.setTextColor(resources.getColor(R.color.black))
            dash_manage_resume.setTextColor(resources.getColor(R.color.black))
            dash_manage_jobalert.setTextColor(resources.getColor(R.color.black))

            dash_myjobs_selected.visibility = View.VISIBLE
            dash_mygroups_selected.visibility = View.GONE
            dash_mycompanies_selected.visibility = View.GONE
            dash_mymentors_selected.visibility = View.GONE
            dash_myevents_selected.visibility = View.GONE
            dash_accnt_settings_selected.visibility = View.GONE
            dash_my_preferences_selected.visibility = View.GONE
            dash_manage_resume_selected.visibility = View.GONE
            dash_manage_jobalert_selected.visibility = View.GONE

            jobs_layout.visibility = View.VISIBLE
            groups_layout.visibility = View.VISIBLE
            companies_layout.visibility = View.VISIBLE
            mentors_layout.visibility = View.GONE
            events_layout.visibility = View.GONE
            accountsettings_layout.visibility = View.GONE
            preference_layout.visibility = View.GONE
            jobalert_layout.visibility = View.GONE
            resumes_layout.visibility = View.GONE
            following_layout.visibility = View.GONE
        }

        dash_mygroups.setOnClickListener {
            dash_myjobs.setTextColor(resources.getColor(R.color.black))
            dash_mygroups.setTextColor(resources.getColor(R.color.green))
            dash_mycompanies.setTextColor(resources.getColor(R.color.black))
            dash_mymentors.setTextColor(resources.getColor(R.color.black))
            dash_myevents.setTextColor(resources.getColor(R.color.black))
            dash_accnt_settings.setTextColor(resources.getColor(R.color.black))
            dash_my_preferences.setTextColor(resources.getColor(R.color.black))
            dash_manage_resume.setTextColor(resources.getColor(R.color.black))
            dash_manage_jobalert.setTextColor(resources.getColor(R.color.black))

            dash_myjobs_selected.visibility = View.GONE
            dash_mygroups_selected.visibility = View.VISIBLE
            dash_mycompanies_selected.visibility = View.GONE
            dash_mymentors_selected.visibility = View.GONE
            dash_myevents_selected.visibility = View.GONE
            dash_accnt_settings_selected.visibility = View.GONE
            dash_my_preferences_selected.visibility = View.GONE
            dash_manage_resume_selected.visibility = View.GONE
            dash_manage_jobalert_selected.visibility = View.GONE

            jobs_layout.visibility = View.GONE
            groups_layout.visibility = View.VISIBLE
            companies_layout.visibility = View.VISIBLE
            mentors_layout.visibility = View.GONE
            events_layout.visibility = View.GONE
            accountsettings_layout.visibility = View.GONE
            preference_layout.visibility = View.GONE
            resumes_layout.visibility = View.GONE
            jobalert_layout.visibility = View.GONE
            following_layout.visibility = View.GONE
        }

        dash_mycompanies.setOnClickListener {
            dash_myjobs.setTextColor(resources.getColor(R.color.black))
            dash_mygroups.setTextColor(resources.getColor(R.color.black))
            dash_mycompanies.setTextColor(resources.getColor(R.color.green))
            dash_mymentors.setTextColor(resources.getColor(R.color.black))
            dash_myevents.setTextColor(resources.getColor(R.color.black))
            dash_accnt_settings.setTextColor(resources.getColor(R.color.black))
            dash_my_preferences.setTextColor(resources.getColor(R.color.black))
            dash_manage_resume.setTextColor(resources.getColor(R.color.black))
            dash_manage_jobalert.setTextColor(resources.getColor(R.color.black))

            dash_myjobs_selected.visibility = View.GONE
            dash_mygroups_selected.visibility = View.GONE
            dash_mycompanies_selected.visibility = View.VISIBLE
            dash_mymentors_selected.visibility = View.GONE
            dash_myevents_selected.visibility = View.GONE
            dash_accnt_settings_selected.visibility = View.GONE
            dash_my_preferences_selected.visibility = View.GONE
            dash_manage_resume_selected.visibility = View.GONE
            dash_manage_jobalert_selected.visibility = View.GONE

            jobs_layout.visibility = View.GONE
            groups_layout.visibility = View.GONE
            companies_layout.visibility = View.VISIBLE
            mentors_layout.visibility = View.GONE
            events_layout.visibility = View.GONE
            accountsettings_layout.visibility = View.GONE
            preference_layout.visibility = View.GONE
            resumes_layout.visibility = View.GONE
            jobalert_layout.visibility = View.GONE
            following_layout.visibility = View.GONE
        }

        dash_mymentors.setOnClickListener {
            dash_myjobs.setTextColor(resources.getColor(R.color.black))
            dash_mygroups.setTextColor(resources.getColor(R.color.black))
            dash_mycompanies.setTextColor(resources.getColor(R.color.black))
            dash_mymentors.setTextColor(resources.getColor(R.color.green))
            dash_myevents.setTextColor(resources.getColor(R.color.black))
            dash_accnt_settings.setTextColor(resources.getColor(R.color.black))
            dash_my_preferences.setTextColor(resources.getColor(R.color.black))
            dash_manage_resume.setTextColor(resources.getColor(R.color.black))
            dash_manage_jobalert.setTextColor(resources.getColor(R.color.black))

            dash_myjobs_selected.visibility = View.GONE
            dash_mygroups_selected.visibility = View.GONE
            dash_mycompanies_selected.visibility = View.GONE
            dash_mymentors_selected.visibility = View.VISIBLE
            dash_myevents_selected.visibility = View.GONE
            dash_accnt_settings_selected.visibility = View.GONE
            dash_my_preferences_selected.visibility = View.GONE
            dash_manage_resume_selected.visibility = View.GONE
            dash_manage_jobalert_selected.visibility = View.GONE

            jobs_layout.visibility = View.GONE
            groups_layout.visibility = View.GONE
            companies_layout.visibility = View.GONE
            mentors_layout.visibility = View.GONE
            events_layout.visibility = View.GONE
            accountsettings_layout.visibility = View.GONE
            preference_layout.visibility = View.GONE
            resumes_layout.visibility = View.GONE
            jobalert_layout.visibility = View.GONE
            following_layout.visibility = View.GONE
        }

        dash_myevents.setOnClickListener {
            dash_myjobs.setTextColor(resources.getColor(R.color.black))
            dash_mygroups.setTextColor(resources.getColor(R.color.black))
            dash_mycompanies.setTextColor(resources.getColor(R.color.black))
            dash_mymentors.setTextColor(resources.getColor(R.color.black))
            dash_myevents.setTextColor(resources.getColor(R.color.green))
            dash_accnt_settings.setTextColor(resources.getColor(R.color.black))
            dash_my_preferences.setTextColor(resources.getColor(R.color.black))
            dash_manage_resume.setTextColor(resources.getColor(R.color.black))
            dash_manage_jobalert.setTextColor(resources.getColor(R.color.black))

            dash_myjobs_selected.visibility = View.GONE
            dash_mygroups_selected.visibility = View.GONE
            dash_mycompanies_selected.visibility = View.GONE
            dash_mymentors_selected.visibility = View.GONE
            dash_myevents_selected.visibility = View.VISIBLE
            dash_accnt_settings_selected.visibility = View.GONE
            dash_my_preferences_selected.visibility = View.GONE
            dash_manage_resume_selected.visibility = View.GONE
            dash_manage_jobalert_selected.visibility = View.GONE

            jobs_layout.visibility = View.GONE
            groups_layout.visibility = View.GONE
            companies_layout.visibility = View.GONE
            mentors_layout.visibility = View.GONE
            events_layout.visibility = View.VISIBLE
            accountsettings_layout.visibility = View.GONE
            preference_layout.visibility = View.GONE
            resumes_layout.visibility = View.GONE
            jobalert_layout.visibility = View.GONE
            following_layout.visibility = View.GONE

            // loadAppliedEvents("1")
            listOfEventsdata.clear()
            loadRecommendedEvents("1")
        }

        dash_accnt_settings.setOnClickListener {
            dash_myjobs.setTextColor(resources.getColor(R.color.black))
            dash_mygroups.setTextColor(resources.getColor(R.color.black))
            dash_mycompanies.setTextColor(resources.getColor(R.color.black))
            dash_mymentors.setTextColor(resources.getColor(R.color.black))
            dash_myevents.setTextColor(resources.getColor(R.color.black))
            dash_accnt_settings.setTextColor(resources.getColor(R.color.green))
            dash_my_preferences.setTextColor(resources.getColor(R.color.black))
            dash_manage_resume.setTextColor(resources.getColor(R.color.black))
            dash_manage_jobalert.setTextColor(resources.getColor(R.color.black))

            dash_myjobs_selected.visibility = View.GONE
            dash_mygroups_selected.visibility = View.GONE
            dash_mycompanies_selected.visibility = View.GONE
            dash_mymentors_selected.visibility = View.GONE
            dash_myevents_selected.visibility = View.GONE
            dash_accnt_settings_selected.visibility = View.VISIBLE
            dash_my_preferences_selected.visibility = View.GONE
            dash_manage_resume_selected.visibility = View.GONE
            dash_manage_jobalert_selected.visibility = View.GONE

            // horizontal_layout.visibility = View.GONE
            jobs_layout.visibility = View.GONE
            groups_layout.visibility = View.GONE
            companies_layout.visibility = View.GONE
            mentors_layout.visibility = View.GONE
            events_layout.visibility = View.GONE
            accountsettings_layout.visibility = View.VISIBLE
            preference_layout.visibility = View.GONE
            resumes_layout.visibility = View.GONE
            jobalert_layout.visibility = View.GONE
            following_layout.visibility = View.GONE
            loadAccountSettings()
        }

        dash_my_preferences.setOnClickListener {
            dash_myjobs.setTextColor(resources.getColor(R.color.black))
            dash_mygroups.setTextColor(resources.getColor(R.color.black))
            dash_mycompanies.setTextColor(resources.getColor(R.color.black))
            dash_mymentors.setTextColor(resources.getColor(R.color.black))
            dash_myevents.setTextColor(resources.getColor(R.color.black))
            dash_accnt_settings.setTextColor(resources.getColor(R.color.black))
            dash_my_preferences.setTextColor(resources.getColor(R.color.green))
            dash_manage_resume.setTextColor(resources.getColor(R.color.black))
            dash_manage_jobalert.setTextColor(resources.getColor(R.color.black))

            dash_myjobs_selected.visibility = View.GONE
            dash_mygroups_selected.visibility = View.GONE
            dash_mycompanies_selected.visibility = View.GONE
            dash_mymentors_selected.visibility = View.GONE
            dash_myevents_selected.visibility = View.GONE
            dash_accnt_settings_selected.visibility = View.GONE
            dash_my_preferences_selected.visibility = View.VISIBLE
            dash_manage_resume_selected.visibility = View.GONE
            dash_manage_jobalert_selected.visibility = View.GONE

            jobs_layout.visibility = View.GONE
            groups_layout.visibility = View.GONE
            companies_layout.visibility = View.GONE
            mentors_layout.visibility = View.GONE
            events_layout.visibility = View.GONE
            accountsettings_layout.visibility = View.GONE
            preference_layout.visibility = View.VISIBLE
            resumes_layout.visibility = View.GONE
            jobalert_layout.visibility = View.GONE
            following_layout.visibility = View.GONE
            openBottomSheetPreferencesDashboard()
        }

        dash_manage_resume.setOnClickListener {
            dash_myjobs.setTextColor(resources.getColor(R.color.black))
            dash_mygroups.setTextColor(resources.getColor(R.color.black))
            dash_mycompanies.setTextColor(resources.getColor(R.color.black))
            dash_mymentors.setTextColor(resources.getColor(R.color.black))
            dash_myevents.setTextColor(resources.getColor(R.color.black))
            dash_accnt_settings.setTextColor(resources.getColor(R.color.black))
            dash_my_preferences.setTextColor(resources.getColor(R.color.black))
            dash_manage_resume.setTextColor(resources.getColor(R.color.green))
            dash_manage_jobalert.setTextColor(resources.getColor(R.color.black))

            dash_myjobs_selected.visibility = View.GONE
            dash_mygroups_selected.visibility = View.GONE
            dash_mycompanies_selected.visibility = View.GONE
            dash_mymentors_selected.visibility = View.GONE
            dash_myevents_selected.visibility = View.GONE
            dash_accnt_settings_selected.visibility = View.GONE
            dash_my_preferences_selected.visibility = View.GONE
            dash_manage_resume_selected.visibility = View.VISIBLE
            dash_manage_jobalert_selected.visibility = View.GONE

            jobs_layout.visibility = View.GONE
            groups_layout.visibility = View.GONE
            companies_layout.visibility = View.GONE
            mentors_layout.visibility = View.GONE
            events_layout.visibility = View.GONE
            accountsettings_layout.visibility = View.GONE
            preference_layout.visibility = View.GONE
            resumes_layout.visibility = View.VISIBLE
            jobalert_layout.visibility = View.GONE
            following_layout.visibility = View.GONE

            openResumesPage()
            pageType = 2

        }

        dash_manage_jobalert.setOnClickListener {
            dash_myjobs.setTextColor(resources.getColor(R.color.black))
            dash_mygroups.setTextColor(resources.getColor(R.color.black))
            dash_mycompanies.setTextColor(resources.getColor(R.color.black))
            dash_mymentors.setTextColor(resources.getColor(R.color.black))
            dash_myevents.setTextColor(resources.getColor(R.color.black))
            dash_accnt_settings.setTextColor(resources.getColor(R.color.black))
            dash_my_preferences.setTextColor(resources.getColor(R.color.black))
            dash_manage_resume.setTextColor(resources.getColor(R.color.black))
            dash_manage_jobalert.setTextColor(resources.getColor(R.color.green))

            dash_myjobs_selected.visibility = View.GONE
            dash_mygroups_selected.visibility = View.GONE
            dash_mycompanies_selected.visibility = View.GONE
            dash_mymentors_selected.visibility = View.GONE
            dash_myevents_selected.visibility = View.GONE
            dash_accnt_settings_selected.visibility = View.GONE
            dash_my_preferences_selected.visibility = View.GONE
            dash_manage_resume_selected.visibility = View.GONE
            dash_manage_jobalert_selected.visibility = View.VISIBLE

            jobs_layout.visibility = View.GONE
            groups_layout.visibility = View.GONE
            companies_layout.visibility = View.GONE
            mentors_layout.visibility = View.GONE
            events_layout.visibility = View.GONE
            accountsettings_layout.visibility = View.GONE
            preference_layout.visibility = View.GONE
            resumes_layout.visibility = View.GONE
            jobalert_layout.visibility = View.VISIBLE
            following_layout.visibility = View.GONE

            loadJobAlert()
        }

        following_id_layout.setOnClickListener {
            horizontal_layout.visibility = View.GONE
            jobs_layout.visibility = View.GONE
            groups_layout.visibility = View.GONE
            companies_layout.visibility = View.GONE
            mentors_layout.visibility = View.GONE
            events_layout.visibility = View.GONE
            accountsettings_layout.visibility = View.GONE
            preference_layout.visibility = View.GONE
            resumes_layout.visibility = View.GONE
            following_layout.visibility = View.VISIBLE
            loadFollowingList()
        }

        postsheader1.setOnClickListener {
            intent = Intent(applicationContext, ZActivityJobs::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("isLoggedIn", true)
            startActivity(intent)
        }

        groupssheader1.setOnClickListener {
            intent = Intent(applicationContext, ZActivityGroups::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("isLoggedIn", true)
            startActivity(intent)
        }

        companiessheader1.setOnClickListener {
            intent = Intent(applicationContext, ZActivityCompanies::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("isLoggedIn", true)
//                intent = Intent(applicationContext, WebActivity::class.java)
//                intent.putExtra("value","https://www.jobsforher.com/company")
            startActivity(intent)
        }

        eventssheader1.setOnClickListener {
            intent = Intent(applicationContext, ZActivityEvents::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("isLoggedIn", true)
//                intent = Intent(applicationContext, WebActivity::class.java)
//                intent.putExtra("value","https://www.jobsforher.com/company")
            startActivity(intent)
        }

        //Jobs layout button clicks
        rec_myjobs_button.setOnClickListener {

            listOfhotJobsdata.clear()
            listOfhotJobsdataTrimmed.clear()
            listOfCompareJoineddata.clear()
            pagenoRecommendedJobs = "1"

            rec_myjobs.setBackgroundTintList(null)
            app_myjobs.setBackgroundTintList(resources.getColorStateList(R.color.greyseparator_company))
            sav_myjobs.setBackgroundTintList(resources.getColorStateList(R.color.greyseparator_company))
            rec_myjobs_button.setTextColor(resources.getColor(R.color.green))
            app_myjobs_button.setTextColor(resources.getColor(R.color.black))
            sav_myjobs_button.setTextColor(resources.getColor(R.color.black))
            mark1.setBackgroundResource(R.drawable.circle_dashboard)
            mark2.setBackgroundResource(R.drawable.greycircle_dashboard)
            mark3.setBackgroundResource(R.drawable.greycircle_dashboard)

            if (listOfhotJobsdata.size > 0) {
                mRecyclerViewPosts!!.visibility = View.VISIBLE
                default_company_jobs.visibility = View.GONE
                if (hasnextRecommendedJobs)
                    seemore_jobs.visibility = View.VISIBLE
                else
                    seemore_jobs.visibility = View.GONE
                if (listOfhotJobsdata.size > 3) {
                    var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                    mRecyclerViewPosts!!.layoutManager =
                        mgroupsLayoutManager as RecyclerView.LayoutManager?
                    mAdapterJobs = JobsAdapter(
                        listOfhotJobsdataTrimmed,
                        isLoggedIn,
                        2,
                        0,
                        listOfCompareJoineddata,
                        1,
                        "Dashboard"
                    )
                    mRecyclerViewPosts!!.adapter = mAdapterJobs
                } else {
                    var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                    mRecyclerViewPosts!!.layoutManager =
                        mgroupsLayoutManager as RecyclerView.LayoutManager?
                    mAdapterJobs = JobsAdapter(
                        listOfhotJobsdata,
                        isLoggedIn,
                        2,
                        0,
                        listOfCompareJoineddata,
                        1,
                        "Dashboard"
                    )
                    mRecyclerViewPosts!!.adapter = mAdapterJobs
                }
            } else {
                loadRecommendedJobs(pagenoRecommendedJobs)
            }
            typeJobsClick = 1
        }

        app_myjobs_button.setOnClickListener {

            listOfAppliedJobsdata.clear()
            listOfAppliedJobsdataTrimmed.clear()
            listOfCompareJoineddata.clear()

            rec_myjobs.setBackgroundTintList(resources.getColorStateList(R.color.greyseparator_company))
            app_myjobs.setBackgroundTintList(null)
            sav_myjobs.setBackgroundTintList(resources.getColorStateList(R.color.greyseparator_company))
            rec_myjobs_button.setTextColor(resources.getColor(R.color.black))
            app_myjobs_button.setTextColor(resources.getColor(R.color.green))
            sav_myjobs_button.setTextColor(resources.getColor(R.color.black))
            mark1.setBackgroundResource(R.drawable.greycircle_dashboard)
            mark2.setBackgroundResource(R.drawable.circle_dashboard)
            mark3.setBackgroundResource(R.drawable.greycircle_dashboard)
            if (listOfAppliedJobsdata.size > 0) {
                mRecyclerViewPosts!!.visibility = View.VISIBLE
                default_company_jobs.visibility = View.GONE

                if (hasnextAppliedJobs)
                    seemore_jobs.visibility = View.VISIBLE
                else
                    seemore_jobs.visibility = View.GONE

                var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                mRecyclerViewPosts!!.layoutManager =
                    mgroupsLayoutManager as RecyclerView.LayoutManager?
                mAdapterJobs =
                    JobsAdapter(
                        listOfAppliedJobsdata,
                        isLoggedIn,
                        2,
                        0,
                        listOfCompareJoineddata,
                        1,
                        "Dashboard"
                    )
                mRecyclerViewPosts!!.adapter = mAdapterJobs

            } else
                loadAppliedJobs("1")

            typeJobsClick = 2
        }

        sav_myjobs_button.setOnClickListener {

            listOfSavedJobsdata.clear()
            listOfSavedJobsdataTrimmed.clear()
            listOfCompareJoineddata.clear()

            rec_myjobs.setBackgroundTintList(resources.getColorStateList(R.color.greyseparator_company))
            app_myjobs.setBackgroundTintList(resources.getColorStateList(R.color.greyseparator_company))
            sav_myjobs.setBackgroundTintList(null)
            rec_myjobs_button.setTextColor(resources.getColor(R.color.black))
            app_myjobs_button.setTextColor(resources.getColor(R.color.black))
            sav_myjobs_button.setTextColor(resources.getColor(R.color.green))
            mark1.setBackgroundResource(R.drawable.greycircle_dashboard)
            mark2.setBackgroundResource(R.drawable.greycircle_dashboard)
            mark3.setBackgroundResource(R.drawable.circle_dashboard)
            if (listOfSavedJobsdata.size > 0) {
                mRecyclerViewPosts!!.visibility = View.VISIBLE
                default_company_jobs.visibility = View.GONE
                if (hasnextSavedJobs)
                    seemore_jobs.visibility = View.VISIBLE
                else
                    seemore_jobs.visibility = View.GONE

                var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                mRecyclerViewPosts!!.layoutManager =
                    mgroupsLayoutManager as RecyclerView.LayoutManager?
                mAdapterJobs =
                    JobsAdapter(
                        listOfSavedJobsdata,
                        isLoggedIn,
                        2,
                        0,
                        listOfCompareJoineddata,
                        1,
                        "Dashboard"
                    )
                mRecyclerViewPosts!!.adapter = mAdapterJobs
            } else
                loadSavedJobs("1")

            typeJobsClick = 3
        }

        seemore_jobs.setOnClickListener {

            if (typeJobsClick == 2)
                loadAppliedJobs(pagenoAppliedJobs)
            else if (typeJobsClick == 3)
                loadSavedJobs(pagenoSavedJobs)
            else
                loadRecommendedJobs(pagenoRecommendedJobs)

        }


        //Groups layout button clicks
        rec_groups_button.setOnClickListener {

            listOfGroupdata.clear()
            listOfGroupdataTrimmed.clear()
            listOfCompareJoineddata.clear()
            pagenoRecommendedGroups = "1"

            rec_groups.setBackgroundTintList(null)
            app_groups.setBackgroundTintList(resources.getColorStateList(R.color.greyseparator_company))
            rec_groups_button.setTextColor(resources.getColor(R.color.green))
            app_groups_button.setTextColor(resources.getColor(R.color.black))
            mark4.setBackgroundResource(R.drawable.circle_dashboard)
            mark5.setBackgroundResource(R.drawable.greycircle_dashboard)
            if (listOfGroupdata.size > 0) {
                mRecyclerViewGroups!!.visibility = View.VISIBLE
                default_company_groups.visibility = View.GONE
                if (hasnextRecommendedGroups)
                    seemore_groups.visibility = View.VISIBLE
                else
                    seemore_groups.visibility = View.GONE

                if (listOfGroupdata.size > 3) {
                    var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                    mRecyclerViewGroups!!.layoutManager =
                        mgroupsLayoutManager as RecyclerView.LayoutManager?
                    mAdapterGroups =
                        MyGroupsAdapter(
                            listOfGroupdataTrimmed,
                            isLoggedIn,
                            3,
                            listOfCompareGroupdata,
                            0,
                            1,
                            "Dashboard"
                        )
                    mRecyclerViewGroups!!.adapter = mAdapterGroups
                } else {
                    var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                    mRecyclerViewGroups!!.layoutManager =
                        mgroupsLayoutManager as RecyclerView.LayoutManager?
                    mAdapterGroups = MyGroupsAdapter(
                        listOfGroupdata,
                        isLoggedIn,
                        3,
                        listOfCompareGroupdata,
                        0,
                        1,
                        "Dashboard"
                    )
                    mRecyclerViewGroups!!.adapter = mAdapterGroups
                }
            } else {
                loadRecommendedMyGroupData(pagenoRecommendedGroups)
            }
            typeGroupClick = 1
        }

        app_groups_button.setOnClickListener {

            listOfMyGroupdata.clear()
            listOfMyGroupdataTrimmed.clear()
            listOfCompareJoineddata.clear()
            pagenoMyGroups = "1"

            rec_groups.setBackgroundTintList(resources.getColorStateList(R.color.greyseparator_company))
            app_groups.setBackgroundTintList(null)
            rec_groups_button.setTextColor(resources.getColor(R.color.black))
            app_groups_button.setTextColor(resources.getColor(R.color.green))
            mark4.setBackgroundResource(R.drawable.greycircle_dashboard)
            mark5.setBackgroundResource(R.drawable.circle_dashboard)

            if (listOfMyGroupdata.size > 0) {
                mRecyclerViewGroups!!.visibility = View.VISIBLE
                default_company_groups.visibility = View.GONE
                if (hasnextMyGroups)
                    seemore_groups.visibility = View.VISIBLE
                else
                    seemore_groups.visibility = View.GONE

                var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                mRecyclerViewGroups!!.layoutManager =
                    mgroupsLayoutManager as RecyclerView.LayoutManager?
                mAdapterGroups = MyGroupsAdapter(
                    listOfMyGroupdata,
                    isLoggedIn,
                    1,
                    listOfCompareGroupdata,
                    0,
                    1,
                    "Dashboard"
                )
                mRecyclerViewGroups!!.adapter = mAdapterGroups
            } else {
                loadMyGroupData(pagenoMyGroups)
            }
            typeGroupClick = 2
        }

        seemore_groups.setOnClickListener {


            if (typeGroupClick == 2)
                loadMyGroupData(pagenoMyGroups)
            else
                loadRecommendedMyGroupData(pagenoRecommendedGroups)
        }

        //Companies layout button clicks
        rec_companies_button.setOnClickListener {

            listOfCompareJoineddata.clear()
            listOfCompaniesdata.clear()
            listOfCompaniesdataTrimmed.clear()
            pagenoRecommendedCompanies = "1"

            rec_companies.setBackgroundTintList(null)
            app_copanies.setBackgroundTintList(resources.getColorStateList(R.color.greyseparator_company))
            rec_companies_button.setTextColor(resources.getColor(R.color.green))
            app_copanies_button.setTextColor(resources.getColor(R.color.black))
            mark6.setBackgroundResource(R.drawable.circle_dashboard)
            mark7.setBackgroundResource(R.drawable.greycircle_dashboard)
            if (listOfCompaniesdata.size > 0) {

                mRecyclerViewCompanies!!.visibility = View.VISIBLE
                default_company_companies.visibility = View.GONE
                if (hasnextRecommendedCompanies)
                    seemore_companies.visibility = View.VISIBLE
                else
                    seemore_companies.visibility = View.GONE

                if (listOfCompaniesdata.size > 3) {
                    var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                    mRecyclerViewCompanies!!.layoutManager =
                        mgroupsLayoutManager as RecyclerView.LayoutManager?
                    mAdapterCompanies =
                        CompaniesAdapter(
                            listOfCompaniesdataTrimmed,
                            isLoggedIn,
                            3,
                            0,
                            listOfCompareJoineddata,
                            1,
                            "Dashboard"
                        )
                    mRecyclerViewCompanies!!.adapter = mAdapterCompanies
                } else {
                    var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                    mRecyclerViewCompanies!!.layoutManager =
                        mgroupsLayoutManager as RecyclerView.LayoutManager?
                    mAdapterCompanies =
                        CompaniesAdapter(
                            listOfCompaniesdata,
                            isLoggedIn,
                            3,
                            0,
                            listOfCompareJoineddata,
                            1,
                            "Dashboard"
                        )
                    mRecyclerViewCompanies!!.adapter = mAdapterCompanies
                }
            } else {
                loadRecommendedCompanies(pagenoRecommendedCompanies)
            }
            typeCompanyClick = 1
        }

        app_copanies_button.setOnClickListener {

            listOfCompareJoineddata.clear()
            listOfMyCompaniesdata.clear()
            listOfMyCompaniesdataTrimmed.clear()
            pagenoFollowedCompanies = "1"

            rec_companies.setBackgroundTintList(resources.getColorStateList(R.color.greyseparator_company))
            app_copanies.setBackgroundTintList(null)
            rec_companies_button.setTextColor(resources.getColor(R.color.black))
            app_copanies_button.setTextColor(resources.getColor(R.color.green))
            mark6.setBackgroundResource(R.drawable.greycircle_dashboard)
            mark7.setBackgroundResource(R.drawable.circle_dashboard)
            if (listOfMyCompaniesdata.size > 0) {
                mRecyclerViewCompanies!!.visibility = View.VISIBLE
                default_company_companies.visibility = View.GONE
                if (hasnextFollowedCompanies)
                    seemore_companies.visibility = View.VISIBLE
                else
                    seemore_companies.visibility = View.GONE

                var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                mRecyclerViewCompanies!!.layoutManager =
                    mgroupsLayoutManager as RecyclerView.LayoutManager?
                mAdapterCompanies = CompaniesAdapter(
                    listOfMyCompaniesdata,
                    isLoggedIn,
                    3,
                    0,
                    listOfCompareJoineddata,
                    1,
                    "Dashboard"
                )
                mRecyclerViewCompanies!!.adapter = mAdapterCompanies
            } else {
                loadFollowedCompanies(pagenoFollowedCompanies)
            }
            typeCompanyClick = 2
        }

        seemore_companies.setOnClickListener {

            if (typeCompanyClick == 2)
                loadFollowedCompanies(pagenoFollowedCompanies)
            else
                loadRecommendedCompanies(pagenoRecommendedCompanies)
        }


        //Events layout button clicks
        rec_events_button.setOnClickListener {

            listOfCompareJoineddata.clear()
            listOfEventsdata.clear()
            listOfEventsdataTrimmed.clear()
            pagenoRecommendedEvents = "1"

            rec_events.setBackgroundTintList(null)
            app_events.setBackgroundTintList(resources.getColorStateList(R.color.greyseparator_company))
            rec_events_button.setTextColor(resources.getColor(R.color.green))
            app_events_button.setTextColor(resources.getColor(R.color.black))
            mark8.setBackgroundResource(R.drawable.circle_dashboard)
            mark9.setBackgroundResource(R.drawable.greycircle_dashboard)
            if (listOfEventsdata.size > 0) {

                mRecyclerViewEvents!!.visibility = View.VISIBLE
                default_events_companies.visibility = View.GONE
                if (hasnextRecommendedEvents)
                    seemore_events.visibility = View.VISIBLE
                else
                    seemore_events.visibility = View.GONE

                if (listOfEventsdata.size > 3) {
                    var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                    mRecyclerViewEvents!!.layoutManager =
                        mgroupsLayoutManager as RecyclerView.LayoutManager?
                    mAdapterEvents =
                        EventsAdapter(
                            listOfEventsdataTrimmed,
                            isLoggedIn,
                            3,
                            0,
                            listOfCompareJoineddata,
                            1,
                            "Dashboard"
                        )
                    mRecyclerViewEvents!!.adapter = mAdapterEvents
                } else {
                    var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                    mRecyclerViewEvents!!.layoutManager =
                        mgroupsLayoutManager as RecyclerView.LayoutManager?
                    mAdapterEvents =
                        EventsAdapter(
                            listOfEventsdata,
                            isLoggedIn,
                            3,
                            0,
                            listOfCompareJoineddata,
                            1,
                            "Dashboard"
                        )
                    mRecyclerViewEvents!!.adapter = mAdapterEvents
                }
            } else {
                loadRecommendedEvents(pagenoRecommendedEvents)
            }
            typeEventClick = 1
        }

        app_events_button.setOnClickListener {

            listOfCompareJoineddata.clear()
            listOfMyEventsdata.clear()
            listOfMyEventsdataTrimmed.clear()
            pagenoAppEvents = "1"

            rec_events.setBackgroundTintList(resources.getColorStateList(R.color.greyseparator_company))
            app_events.setBackgroundTintList(null)
            rec_events_button.setTextColor(resources.getColor(R.color.black))
            app_events_button.setTextColor(resources.getColor(R.color.green))
            mark8.setBackgroundResource(R.drawable.greycircle_dashboard)
            mark9.setBackgroundResource(R.drawable.circle_dashboard)
            if (listOfMyEventsdata.size > 0) {
                mRecyclerViewEvents!!.visibility = View.VISIBLE
                default_events_companies.visibility = View.GONE
                if (hasnextAppEvents)
                    seemore_events.visibility = View.VISIBLE
                else
                    seemore_events.visibility = View.GONE

                var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                mRecyclerViewEvents!!.layoutManager =
                    mgroupsLayoutManager as RecyclerView.LayoutManager?
                mAdapterEvents = EventsAdapter(
                    listOfMyEventsdata,
                    isLoggedIn,
                    3,
                    0,
                    listOfCompareJoineddata,
                    1,
                    "Dashboard"
                )
                mRecyclerViewEvents!!.adapter = mAdapterEvents
            } else {
                loadAppliedEvents(pagenoAppEvents)
            }
            typeEventClick = 2
        }


        seemore_events.setOnClickListener {
            if (typeEventClick == 2)
                loadAppliedEvents(pagenoAppEvents)
            else
                loadRecommendedEvents(pagenoRecommendedEvents)
        }

        // Account settings button clicks
        mobileClick.setOnClickListener {
            openBottomSheetChangeName("phone")
        }

        fullnameClick.setOnClickListener {

            openBottomSheetChangeName("name")
        }

        secemailAdd.setOnClickListener {
            openBottomSheetAddSecondaryEmail()
        }

        secemailClick.setOnClickListener {
            openBottomSheetAddSecondaryEmail()
        }

        removeemail.setOnClickListener {
            deleteEmail()
        }

        email_resendp.setOnClickListener {
            sendVerifyEmail(
                email.text.toString(),
                email_resendp.getTag(R.string.Follow_us_on) as Int
            )
        }

        val sharedPref1: SharedPreferences = getSharedPreferences(
            "mysettings",
            Context.MODE_PRIVATE
        )
        Log.d("TAGG", "USERID" + sharedPref1.getString(PREF_USERID, "0")!!)
        if (sharedPref1.getString(PREF_VERIFIED_EMAIL, "0").equals("true")) {
            emailverify_bottom.visibility = View.GONE
        } else
            emailverify_bottom.visibility = View.VISIBLE

        resend_down_button.setOnClickListener {

            sendVerifyEmail(pemail, Integer.parseInt(sharedPref.getString(PREF_USERID, "id")!!))
            emailverify_bottom.visibility = View.GONE
        }

        emailresend_close.setOnClickListener {
            emailverify_bottom.visibility = View.GONE
        }

        email_resend.setOnClickListener {
            sendVerifyEmail(
                secemail.text.toString(),
                email_resendp.getTag(R.string.Follow_us_on) as Int
            )
        }

        mmobile_verify.setOnClickListener {
            sendRequestOTP(
                mobile.text.toString(),
                email_resendp.getTag(R.string.Follow_us_on) as Int
            )
        }

        changePassword.setOnClickListener {
            openBottomSheetChangePassword()
        }

        binaryemailClick.setOnClickListener {
            makeprimaryEmail()
        }

        deactivateAccnt.setOnClickListener {
            deleteAccount()
        }

        notifLayout.setOnClickListener {
            cart_badge.setText("0")
            intent = Intent(applicationContext, Notification::class.java)
            startActivity(intent)
        }

    }


    fun loadDashboardData() {

        var retrofitInterface2: RetrofitInterface? = null

        retrofitInterface2 = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface2!!.getDashboardData(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN
        )

        call.enqueue(object : Callback<DashboardApiDetails> {
            override fun onResponse(
                call: Call<DashboardApiDetails>,
                response: Response<DashboardApiDetails>
            ) {

                Logger.d("URL", "JOBS" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "Dashboard DETAILS" + Gson().toJson(response))

                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )

                var jsonObject1: JSONObject? = null
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    jsonObject1 = jsonObject.optJSONObject("body")
                }


                val responseCode: Int? = jsonObject1?.optInt("response_code")
                val message: String? = jsonObject1?.optString("message")
                //val jsonarray_info: JSONArray = jsonObject1.optJSONArray("body")
                val json_objectdetail: JSONObject = jsonObject1?.optJSONObject("body")!!

                if (response.isSuccessful) {

                    //var json_objectdetail: JSONObject =jsonarray_info.optJSONObject(0)
                    val model: DashboardData = DashboardData();

                    //Counts display
                    val rec_job: Int =
                        if (json_objectdetail.isNull("rec_jobs_count")) 0 else json_objectdetail.optInt(
                            "rec_jobs_count"
                        )
                    mark1.setText(rec_job.toString())
                    val applied_jobs_count: Int =
                        if (json_objectdetail.isNull("applied_jobs_count")) 0 else json_objectdetail.optInt(
                            "applied_jobs_count"
                        )
                    mark2.setText(applied_jobs_count.toString())
                    val saved_jobs_count: Int =
                        if (json_objectdetail.isNull("saved_jobs_count")) 0 else json_objectdetail.optInt(
                            "saved_jobs_count"
                        )
                    mark3.setText(saved_jobs_count.toString())
                    val rec_groups_count: Int =
                        if (json_objectdetail.isNull("rec_groups_count")) 0 else json_objectdetail.optInt(
                            "rec_groups_count"
                        )
                    mark4.setText(rec_groups_count.toString())
                    val my_groups_count: Int =
                        if (json_objectdetail.isNull("my_groups_count")) 0 else json_objectdetail.optInt(
                            "my_groups_count"
                        )
                    mark5.setText(my_groups_count.toString())
                    val rec_companies_count: Int =
                        if (json_objectdetail.isNull("rec_companies_count")) 0 else json_objectdetail.optInt(
                            "rec_companies_count"
                        )
                    mark6.setText(rec_companies_count.toString())
                    val followed_comp_count: Int =
                        if (json_objectdetail.isNull("followed_comp_count")) 0 else json_objectdetail.optInt(
                            "followed_comp_count"
                        )
                    mark7.setText(followed_comp_count.toString())

                    // My profile

                    var profile: JSONObject
                    val profileModel: MyProfile = MyProfile()
                    if (json_objectdetail.isNull("my_profile")) {
                    } else {
                        profile = json_objectdetail.optJSONObject("my_profile")
                        profileModel.id = if (profile.isNull("id")) 0 else profile.optInt("id")
                        profileModel.username =
                            if (profile.isNull("username")) "" else profile.optString("username")
                        profileModel.profile_percentage =
                            if (profile.isNull("profile_percentage")) 0 else profile.optInt("profile_percentage")
                        group_icon.setValue(profileModel.profile_percentage!!.toFloat())
                        // val first = profileModel.profile_percentage!! + "%"
                        val next =
                            "<font color='#99CA3B'>" + profileModel.profile_percentage!! + "%</font>" + " Complete"
                        percentage_text.setText(Html.fromHtml(next))
                        // percentage_text.setText(profileModel.profile_percentage!!.toString()+"% Complete")
                        profileModel.profile_image =
                            if (profile.isNull("profile_image")) "" else profile.optString("profile_image")
                        profileModel.title =
                            if (profile.isNull("title")) "" else profile.optString("title")
                        profileModel.caption =
                            if (profile.isNull("caption")) "" else profile.optString("caption")
                        profileModel.organization =
                            if (profile.isNull("organization")) "" else profile.optString("organization")
                        profileModel.description =
                            if (profile.isNull("description")) "" else profile.optString("description")
                        profileModel.location =
                            if (profile.isNull("location")) "" else profile.optString("location")
                        profileModel.profile_summary =
                            if (profile.isNull("profile_summary")) "" else profile.optString("profile_summary")
                        profileModel.stage_type =
                            if (profile.isNull("stage_type")) "" else profile.optString("stage_type")
//                        profileModel.profile_visibility = if (profile.isNull("profile_visibility")) false else profile.optBoolean("profile_visibility")
                        profileModel.profile_url =
                            if (profile.isNull("profile_url")) "" else profile.optString("profile_url")
                        profileModel.followers =
                            if (profile.isNull("followers")) 0 else profile.optInt("followers")
                        profileModel.following =
                            if (profile.isNull("following")) 0 else profile.optInt("following")

                    }
//                    userId = profileModel.id!!     //Sneha check this
                    model.my_profile = profileModel

                    //Recommended Jobs
                    var jobs: JSONArray
                    var jobsdata: ArrayList<Rec_Jobs> = ArrayList()
                    if (json_objectdetail.isNull("rec_jobs")) {
                    } else {
                        jobs = json_objectdetail.optJSONArray("rec_jobs")

                        for (k in 0 until jobs.length()) {

                            val jobsModel: Rec_Jobs = Rec_Jobs()
                            jobsModel.modified_on =
                                if (jobs.optJSONObject(k)
                                        .isNull("modified_on")
                                ) "" else jobs.optJSONObject(k).optString(
                                    "modified_on"
                                )
                            jobsModel.application_count =
                                if (jobs.optJSONObject(k)
                                        .isNull("application_count")
                                ) 0 else jobs.optJSONObject(k).optInt(
                                    "application_count"
                                )
                            jobsModel.resume_required =
                                if (jobs.optJSONObject(k)
                                        .isNull("resume_required")
                                ) false else jobs.optJSONObject(k).optBoolean(
                                    "resume_required"
                                )
                            jobsModel.company_id =
                                if (jobs.optJSONObject(k)
                                        .isNull("company_id")
                                ) 0 else jobs.optJSONObject(k).optInt(
                                    "company_id"
                                )
                            var jobs_types: JSONArray
                            var jobsdata_types: ArrayList<String> = ArrayList()
                            if (jobs.optJSONObject(k).isNull("job_types")) {
                            } else {
                                jobs_types = jobs.optJSONObject(k).optJSONArray("job_types")
                                for (a in 0 until jobs_types.length()) {
                                    jobsdata_types.add(jobs_types.optString(a))
                                }
                            }
                            jobsModel.job_types = jobsdata_types

                            jobsModel.employer_name =
                                if (jobs.optJSONObject(k)
                                        .isNull("employer_name")
                                ) "" else jobs.optJSONObject(k).optString("employer_name")

                            jobsModel.new_application_count =
                                if (jobs.optJSONObject(k)
                                        .isNull("new_application_count")
                                ) 0 else jobs.optJSONObject(k).optInt(
                                    "new_application_count"
                                )
                            jobsModel.title =
                                if (jobs.optJSONObject(k)
                                        .isNull("title")
                                ) "" else jobs.optJSONObject(k).optString("title")
                            jobsModel.max_year =
                                if (jobs.optJSONObject(k)
                                        .isNull("max_year")
                                ) 0 else jobs.optJSONObject(k).optInt("max_year")

                            jobsModel.location_name =
                                if (jobs.optJSONObject(k)
                                        .isNull("location_name")
                                ) "" else jobs.optJSONObject(k).optString(
                                    "location_name"
                                )
                            jobsModel.company_logo =
                                if (jobs.optJSONObject(k)
                                        .isNull("company_logo")
                                ) "" else jobs.optJSONObject(
                                    k
                                ).optString("company_logo")
                            jobsModel.created_on =
                                if (jobs.optJSONObject(k)
                                        .isNull("created_on")
                                ) "" else jobs.optJSONObject(k).optString(
                                    "created_on"
                                )
                            jobsModel.company_name =
                                if (jobs.optJSONObject(k)
                                        .isNull("company_name")
                                ) "" else jobs.optJSONObject(k).optString(
                                    "company_name"
                                )
                            jobsModel.id =
                                if (jobs.optJSONObject(k).isNull("id")) 0 else jobs.optJSONObject(k)
                                    .optInt("id")
                            jobsModel.boosted =
                                if (jobs.optJSONObject(k)
                                        .isNull("boosted")
                                ) false else jobs.optJSONObject(k).optBoolean(
                                    "boosted"
                                )

                            jobsModel.min_year =
                                if (jobs.optJSONObject(k)
                                        .isNull("min_year")
                                ) 0 else jobs.optJSONObject(k).optInt("min_year")

                            jobsModel.employer_id =
                                if (jobs.optJSONObject(k)
                                        .isNull("employer_id")
                                ) 0 else jobs.optJSONObject(k).optInt("employer_id")
                            jobsModel.status =
                                if (jobs.optJSONObject(k)
                                        .isNull("status")
                                ) "" else jobs.optJSONObject(k).optString("status")

                            jobsdata.add(jobsModel)
                        }
                    }
                    model.rec_jobs = jobsdata

                    //Recommended Groups

                    var grousp: JSONArray
                    var groupsdata: ArrayList<Rec_Groups> = ArrayList()
                    if (json_objectdetail.isNull("rec_groups")) {
                    } else {
                        grousp = json_objectdetail.optJSONArray("rec_groups")

                        for (k in 0 until grousp.length()) {

                            val groupsModel: Rec_Groups = Rec_Groups()
                            groupsModel.id = if (grousp.optJSONObject(k)
                                    .isNull("id")
                            ) 0 else grousp.optJSONObject(k).optInt("id")
                            groupsModel.icon_url = if (grousp.optJSONObject(k)
                                    .isNull("icon_url")
                            ) "" else grousp.optJSONObject(k).optString("icon_url")
                            groupsModel.featured = if (grousp.optJSONObject(k)
                                    .isNull("featured")
                            ) false else grousp.optJSONObject(k).optBoolean("featured")
                            groupsModel.noOfMembers = if (grousp.optJSONObject(k)
                                    .isNull("no_of_members")
                            ) 0 else grousp.optJSONObject(k).optInt("no_of_members")

                            var groups_city: JSONArray
                            var groups_city_data: ArrayList<Int> = ArrayList()
                            if (grousp.optJSONObject(k).isNull("cities")) {
                            } else {
                                groups_city = grousp.optJSONObject(k).optJSONArray("cities")
                                for (a in 0 until groups_city.length()) {
                                    groups_city_data.add(groups_city.optInt(a))
                                }
                            }
                            groupsModel.cities = groups_city_data

                            groupsModel.visiblity_type = if (grousp.optJSONObject(k)
                                    .isNull("visiblity_type")
                            ) "" else grousp.optJSONObject(k).optString("visiblity_type")
                            groupsModel.name = if (grousp.optJSONObject(k)
                                    .isNull("name")
                            ) "" else grousp.optJSONObject(k).optString("name")
                            groupsModel.id = if (grousp.optJSONObject(k)
                                    .isNull("id")
                            ) 0 else grousp.optJSONObject(k).optInt("id")

                            var category: JSONArray
                            var category_data: ArrayList<CategoryDashboard> = ArrayList()
                            if (grousp.optJSONObject(k).isNull("categories")) {
                            } else {
                                category = grousp.optJSONObject(k).optJSONArray("categories")
                                for (a in 0 until category.length()) {
                                    val category_Model: CategoryDashboard = CategoryDashboard()
                                    category_Model.category_id = if (category.optJSONObject(a)
                                            .isNull("category_id")
                                    ) 0 else category.optJSONObject(a).optInt("category_id")
                                    category_Model.category = if (category.optJSONObject(a)
                                            .isNull("category")
                                    ) "" else category.optJSONObject(a).optString("category")
                                    category_data.add(category_Model)
                                }
                            }
                            groupsModel.categories = category_data

                            groupsModel.excerpt = if (grousp.optJSONObject(k)
                                    .isNull("excerpt")
                            ) "" else grousp.optJSONObject(k).optString("excerpt")
                            groupsModel.status = if (grousp.optJSONObject(k)
                                    .isNull("status")
                            ) "" else grousp.optJSONObject(k).optString("status")


                            groupsdata.add(groupsModel)
                        }
                    }
                    model.rec_groups = groupsdata


                    //Recommended Companies
                    var companies: JSONArray
                    var companiesdata: ArrayList<Rec_Company> = ArrayList()

                    if (json_objectdetail.isNull("rec_companies")) {
                    } else {
                        companies = json_objectdetail.optJSONArray("rec_companies")

                        for (k in 0 until companies.length()) {

                            val companiesModel: Rec_Company = Rec_Company()
                            companiesModel.company_type =
                                if (companies.optJSONObject(k)
                                        .isNull("company_type")
                                ) "" else companies.optJSONObject(k).optString("company_type")
                            companiesModel.featured =
                                if (companies.optJSONObject(k)
                                        .isNull("featured")
                                ) false else companies.optJSONObject(k).optBoolean("featured")
                            companiesModel.website =
                                if (companies.optJSONObject(k)
                                        .isNull("website")
                                ) "" else companies.optJSONObject(k).optString("website")
                            companiesModel.active_jobs_count =
                                if (companies.optJSONObject(k)
                                        .isNull("active_jobs_count")
                                ) 0 else companies.optJSONObject(k).optInt("active_jobs_count")
                            companiesModel.cities =
                                if (companies.optJSONObject(k)
                                        .isNull("cities")
                                ) "" else companies.optJSONObject(k).optString("cities")
                            companiesModel.follow_count =
                                if (companies.optJSONObject(k)
                                        .isNull("follow_count")
                                ) 0 else companies.optJSONObject(k).optInt("follow_count")
                            companiesModel.name =
                                if (companies.optJSONObject(k)
                                        .isNull("name")
                                ) "" else companies.optJSONObject(k).optString("name")
                            companiesModel.logo =
                                if (companies.optJSONObject(k)
                                        .isNull("logo")
                                ) "" else companies.optJSONObject(k).optString("logo")

                            var comp_types: JSONArray
                            var compdata_types: ArrayList<String> = ArrayList()
                            if (companies.optJSONObject(k).isNull("industry")) {
                            } else {
                                comp_types = companies.optJSONObject(k).optJSONArray("industry")
                                for (a in 0 until comp_types.length()) {
                                    compdata_types.add(comp_types.optString(a))
                                }
                            }
                            companiesModel.industry = compdata_types

                            companiesModel.id =
                                if (companies.optJSONObject(k)
                                        .isNull("id")
                                ) 0 else companies.optJSONObject(k).optInt("id")
                            companiesModel.banner_image =
                                if (companies.optJSONObject(k)
                                        .isNull("banner_image")
                                ) "" else companies.optJSONObject(k).optString("banner_image")
                            companiesModel.status =
                                if (companies.optJSONObject(k)
                                        .isNull("status")
                                ) "" else companies.optJSONObject(k).optString("status")

                            companiesdata.add(companiesModel)

                        }
                    }
                    model.rec_companies = companiesdata

                    //My Resumes
                    var resumes: JSONArray
                    var resumesdata: ArrayList<MyResume> = ArrayList()
                    resumesdata_Separate.clear()

                    if (json_objectdetail.isNull("my_resumes")) {
                    } else {
                        resumes = json_objectdetail.optJSONArray("my_resumes")

                        for (k in 0 until resumes.length()) {

                            val resumesModel: MyResume = MyResume()
                            resumesModel.id = if (resumes.optJSONObject(k)
                                    .isNull("id")
                            ) 0 else resumes.optJSONObject(k).optInt("id")
                            resumesModel.title = if (resumes.optJSONObject(k)
                                    .isNull("title")
                            ) "" else resumes.optJSONObject(k).optString("title")
                            resumesModel.created_on = if (resumes.optJSONObject(k)
                                    .isNull("created_on")
                            ) "" else resumes.optJSONObject(k).optString("created_on")
                            resumesModel.deleted = if (resumes.optJSONObject(k)
                                    .isNull("deleted")
                            ) false else resumes.optJSONObject(k).optBoolean("deleted")
                            resumesModel.is_parsed = if (resumes.optJSONObject(k)
                                    .isNull("is_parsed")
                            ) false else resumes.optJSONObject(k).optBoolean("is_parsed")
                            resumesModel.modified_on = if (resumes.optJSONObject(k)
                                    .isNull("modified_on")
                            ) "" else resumes.optJSONObject(k).optString("modified_on")
                            resumesModel.is_default = if (resumes.optJSONObject(k)
                                    .isNull("is_default")
                            ) "" else resumes.optJSONObject(k).optString("is_default")
                            resumesModel.user_id = if (resumes.optJSONObject(k)
                                    .isNull("user_id")
                            ) 0 else resumes.optJSONObject(k).optInt("user_id")
                            resumesModel.path = if (resumes.optJSONObject(k)
                                    .isNull("path")
                            ) "" else resumes.optJSONObject(k).optString("path")
                            resumesdata.add(resumesModel)
                        }
                    }
                    resumesdata_Separate = resumesdata
                    model.my_resumes = resumesdata

                    //My Preferences
                    var preferences: JSONArray
                    var preferencesdata: ArrayList<MyPreference> = ArrayList()

                    if (json_objectdetail.isNull("my_preferences")) {
                    } else {
                        preferences = json_objectdetail.optJSONArray("my_preferences")

                        for (k in 0 until preferences.length()) {

                            val preferencesModel: MyPreference = MyPreference()
                            preferencesModel.id = if (preferences.optJSONObject(k)
                                    .isNull("id")
                            ) 0 else preferences.optJSONObject(k).optInt("id")
                            preferencesModel.preferred_job_type = if (preferences.optJSONObject(k)
                                    .isNull("preferred_job_type")
                            ) "" else preferences.optJSONObject(k).optString("preferred_job_type")
                            preferencesModel.exp_to_year = if (preferences.optJSONObject(k)
                                    .isNull("exp_to_year")
                            ) 0 else preferences.optJSONObject(k).optInt("exp_to_year")
                            preferencesModel.skills = if (preferences.optJSONObject(k)
                                    .isNull("skills")
                            ) "" else preferences.optJSONObject(k).optString("skills")
                            preferencesModel.preferred_city = if (preferences.optJSONObject(k)
                                    .isNull("preferred_city")
                            ) "" else preferences.optJSONObject(k).optString("preferred_city")
                            preferencesModel.preferred_industry = if (preferences.optJSONObject(k)
                                    .isNull("preferred_industry")
                            ) "" else preferences.optJSONObject(k).optString("preferred_industry")
                            preferencesModel.preferred_functional_area =
                                if (preferences.optJSONObject(k)
                                        .isNull("is_depreferred_functional_areafault")
                                ) "" else preferences.optJSONObject(k)
                                    .optString("preferred_functional_area")
                            preferencesModel.exp_from_year = if (preferences.optJSONObject(k)
                                    .isNull("exp_from_year")
                            ) 0 else preferences.optJSONObject(k).optInt("exp_from_year")
                            preferencesModel.user_id = if (preferences.optJSONObject(k)
                                    .isNull("user_id")
                            ) 0 else preferences.optJSONObject(k).optInt("user_id")
                            preferencesdata.add(preferencesModel)
                        }
                    }
                    model.my_preferences = preferencesdata

                    // Display of data
                    stagetype.setText(model.my_profile!!.stage_type)
                    groupname.setText(model.my_profile!!.username)
                    followers_id!!.text = model.my_profile!!.followers.toString()
                    following_id!!.text = model.my_profile!!.following.toString()

                    if (model.my_profile!!.profile_image!!.isNotEmpty()) {
                        Picasso.with(applicationContext)
                            .load(model.my_profile!!.profile_image!!)
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .into(group_icon)

                    } else {
                        Picasso.with(applicationContext)
                            .load(R.drawable.ic_launcher_foreground)
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .into(group_icon)
                    }
                    listOfProfiledata1.clear()
                    listOfProfiledata1.add(model.my_profile!!)
                    if (model.my_profile!!.stage_type!!.toLowerCase().equals("starter")) {
                        if (model.my_profile!!.title!!.length > 0) {
                            degree!!.visibility = View.VISIBLE
                            degreeValue!!.visibility = View.VISIBLE
                            degreeValue!!.text = model.my_profile!!.title
                        } else {
                            degree!!.visibility = View.GONE
                            degreeValue!!.visibility = View.GONE
                        }

                        if (model.my_profile!!.organization!!.length > 0) {
                            instittion!!.visibility = View.VISIBLE
                            institutionValue!!.visibility = View.VISIBLE
                            institutionValue!!.text = model.my_profile!!.organization
                        } else {
                            instittion!!.visibility = View.GONE
                            institutionValue!!.visibility = View.GONE
                        }

                        if (model.my_profile!!.location!!.length > 0) {
                            location!!.visibility = View.VISIBLE
                            locationValue!!.visibility = View.VISIBLE
                            locationValue!!.text = model.my_profile!!.location
                        } else {
                            location!!.visibility = View.GONE
                            locationValue!!.visibility = View.GONE
                        }

                        caption!!.visibility = View.GONE
                        captionValue!!.visibility = View.GONE
                        previous!!.visibility = View.GONE
                        previousValue!!.visibility = View.GONE
                        company!!.visibility = View.GONE
                        companyValue!!.visibility = View.GONE
                        designation!!.visibility = View.GONE
                        designationValue!!.visibility = View.GONE
                    } else if (model.my_profile!!.stage_type!!.toLowerCase().equals("restarter")) {
                        degree!!.visibility = View.GONE
                        degreeValue!!.visibility = View.GONE
                        instittion!!.visibility = View.GONE
                        institutionValue!!.visibility = View.GONE
                        designation!!.visibility = View.GONE
                        designationValue!!.visibility = View.GONE

                        if (model.my_profile!!.caption!!.length > 0) {
//                            caption!!.visibility = View.VISIBLE
//                            captionValue!!.visibility = View.VISIBLE
//                            captionValue!!.text = model.my_profile!!.caption
                            caption!!.visibility = View.GONE
                            captionValue!!.visibility = View.GONE
                        } else {
                            caption!!.visibility = View.GONE
                            captionValue!!.visibility = View.GONE
                        }

                        if (model.my_profile!!.title!!.length > 0) {
                            previous!!.visibility = View.VISIBLE
                            previousValue!!.visibility = View.VISIBLE
                            previousValue!!.text = model.my_profile!!.title
                        } else {
                            previous!!.visibility = View.GONE
                            previousValue!!.visibility = View.GONE
                        }

                        if (model.my_profile!!.organization!!.length > 0) {
                            company!!.visibility = View.VISIBLE
                            companyValue!!.visibility = View.VISIBLE
                            companyValue!!.text = model.my_profile!!.organization
                        } else {
                            company!!.visibility = View.GONE
                            companyValue!!.visibility = View.GONE
                        }

                        if (model.my_profile!!.location!!.length > 0) {
                            location!!.visibility = View.VISIBLE
                            locationValue!!.visibility = View.VISIBLE
                            locationValue!!.text = model.my_profile!!.location
                        } else {
                            location!!.visibility = View.GONE
                            locationValue!!.visibility = View.GONE
                        }


                    } else if (model.my_profile!!.stage_type!!.toLowerCase().equals("riser")) {
                        degree!!.visibility = View.GONE
                        degreeValue!!.visibility = View.GONE
                        instittion!!.visibility = View.GONE
                        institutionValue!!.visibility = View.GONE
                        caption!!.visibility = View.GONE
                        captionValue!!.visibility = View.GONE
                        previous!!.visibility = View.GONE
                        previousValue!!.visibility = View.GONE
                        previousValue!!.text = ""

                        if (model.my_profile!!.organization!!.length > 0) {
                            company!!.visibility = View.VISIBLE
                            companyValue!!.visibility = View.VISIBLE
                            companyValue!!.text = model.my_profile!!.organization
                        } else {
                            company!!.visibility = View.GONE
                            companyValue!!.visibility = View.GONE
                        }

                        if (model.my_profile!!.location!!.length > 0) {
                            location!!.visibility = View.VISIBLE
                            locationValue!!.visibility = View.VISIBLE
                            locationValue!!.text = model.my_profile!!.location
                        } else {
                            location!!.visibility = View.GONE
                            locationValue!!.visibility = View.GONE
                        }

                        if (model.my_profile!!.title!!.length > 0) {
                            designation!!.visibility = View.VISIBLE
                            designationValue!!.visibility = View.VISIBLE
                            designationValue!!.text = model.my_profile!!.title
                        } else {
                            designation!!.visibility = View.GONE
                            designationValue!!.visibility = View.GONE
                        }

                    } else {
                        degree!!.visibility = View.GONE
                        degreeValue!!.visibility = View.GONE
                        instittion!!.visibility = View.GONE
                        institutionValue!!.visibility = View.GONE
                        caption!!.visibility = View.GONE
                        captionValue!!.visibility = View.GONE
                        previous!!.visibility = View.GONE
                        previousValue!!.visibility = View.GONE
                        previousValue!!.text = ""
                        company!!.visibility = View.GONE
                        companyValue!!.visibility = View.GONE
                        location!!.visibility = View.GONE
                        locationValue!!.visibility = View.GONE
                        designation!!.visibility = View.GONE
                        designationValue!!.visibility = View.GONE
                    }


                    // Jobs display
                    listOfhotJobsdata.clear()
                    listOfhotJobsdataTrimmed.clear()
                    if (model!!.rec_jobs!!.size > 0) {
                        for (i in 0 until model!!.rec_jobs!!.size) {
                            val model_jobs: Rec_Jobs = Rec_Jobs()
                            model_jobs.id = model!!.rec_jobs!![i].id
                            model_jobs.title = model!!.rec_jobs!![i].title
                            model_jobs.company_name = model!!.rec_jobs!![i].company_name
                            model_jobs.company_logo = model!!.rec_jobs!![i].company_logo
                            model_jobs.location_name = model!!.rec_jobs!![i].location_name
                            model_jobs.max_year = model!!.rec_jobs!![i].max_year
                            model_jobs.min_year = model!!.rec_jobs!![i].min_year
                            model_jobs.status = model!!.rec_jobs!![i].status
                            model_jobs.modified_on = model!!.rec_jobs!![i].modified_on
                            model_jobs.application_count = model!!.rec_jobs!![i].application_count
                            model_jobs.company_id = model!!.rec_jobs!![i].company_id
                            model_jobs.employer_name = model!!.rec_jobs!![i].employer_name
                            model_jobs.new_application_count =
                                model!!.rec_jobs!![i].new_application_count
                            model_jobs.created_on = model!!.rec_jobs!![i].created_on
                            model_jobs.boosted = model!!.rec_jobs!![i].boosted
                            model_jobs.employer_id = model!!.rec_jobs!![i].employer_id
                            model_jobs.resume_required = model!!.rec_jobs!![i].resume_required

//                            var job_typesArray: JSONArray = json_objectdetail.optJSONArray("job_types")
                            val listOfJobTypes: ArrayList<String> = ArrayList()
//                            for (j in 0 until job_typesArray.length()) {
//                                    listOfJobTypes.add(job_typesArray.optString())
//                            }

                            model_jobs.job_types = listOfJobTypes
                            listOfCompareJoineddata.add(0)
                            listOfhotJobsdata.add(
                                JobsView(
                                    model_jobs.id,
                                    model_jobs.modified_on!!,
                                    model_jobs.application_count!!,
                                    model_jobs.company_id!!,
                                    model_jobs.job_types!!,
                                    model_jobs.employer_name!!,
                                    model_jobs.new_application_count!!,
                                    model_jobs.title!!,
                                    model_jobs.max_year!!,
                                    model_jobs.location_name!!,
                                    model_jobs.company_logo!!,
                                    model_jobs.created_on!!,
                                    model_jobs.company_name!!,
                                    model_jobs.boosted!!,
                                    model_jobs.min_year!!,
                                    model_jobs.employer_id!!,
                                    model_jobs.status!!,
                                    model_jobs.resume_required!!
                                )
                            )
                            if (i < 3) {
                                listOfhotJobsdataTrimmed.add(
                                    JobsView(
                                        model_jobs.id,
                                        model_jobs.modified_on!!,
                                        model_jobs.application_count!!,
                                        model_jobs.company_id!!,
                                        model_jobs.job_types!!,
                                        model_jobs.employer_name!!,
                                        model_jobs.new_application_count!!,
                                        model_jobs.title!!,
                                        model_jobs.max_year!!,
                                        model_jobs.location_name!!,
                                        model_jobs.company_logo!!,
                                        model_jobs.created_on!!,
                                        model_jobs.company_name!!,
                                        model_jobs.boosted!!,
                                        model_jobs.min_year!!,
                                        model_jobs.employer_id!!,
                                        model_jobs.status!!,
                                        model_jobs.resume_required!!
                                    )
                                )
                            }
                        }

                        if (model!!.rec_jobs!!.size > 3) {
                            var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                            mRecyclerViewPosts!!.layoutManager =
                                mgroupsLayoutManager as RecyclerView.LayoutManager?
                            mAdapterJobs = JobsAdapter(
                                listOfhotJobsdataTrimmed,
                                isLoggedIn,
                                2,
                                0,
                                listOfCompareJoineddata,
                                1,
                                "Dashboard"
                            )
                            mRecyclerViewPosts!!.adapter = mAdapterJobs
                            seemore_jobs.visibility = View.VISIBLE
                        } else {
                            var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                            mRecyclerViewPosts!!.layoutManager =
                                mgroupsLayoutManager as RecyclerView.LayoutManager?
                            mAdapterJobs =
                                JobsAdapter(
                                    listOfhotJobsdata,
                                    isLoggedIn,
                                    2,
                                    0,
                                    listOfCompareJoineddata,
                                    1,
                                    "Dashboard"
                                )
                            mRecyclerViewPosts!!.adapter = mAdapterJobs
                            seemore_jobs.visibility = View.VISIBLE
                        }
                    } else {
                        mRecyclerViewPosts!!.visibility = View.GONE
                        default_company_jobs.visibility = View.VISIBLE
                    }

                    // Groups display
                    if (model!!.rec_groups!!.size > 0) {
                        for (i in 0 until model!!.rec_groups!!.size) {
                            val model_groups: GroupsView = GroupsView();

                            model_groups.id = model!!.rec_groups!![i].id!!
                            model_groups.label = model!!.rec_groups!![i].name
                            model_groups.icon_url = model!!.rec_groups!![i].icon_url
                            model_groups.groupType = model!!.rec_groups!![i].visiblity_type
                            model_groups.noOfMembers =
                                model!!.rec_groups!![i].noOfMembers.toString()
                            model_groups.description = model!!.rec_groups!![i].excerpt
                            model_groups.featured = model!!.rec_groups!![i].featured
                            model_groups.status = model!!.rec_groups!![i].status
                            model_groups.cities = model!!.rec_groups!![i].cities
                            //model_groups.is_member = model!!.rec_groups!![i].is_member

                            val catArray: ArrayList<Categories> = ArrayList()
                            for (j in 0 until model!!.rec_groups!![i].categories!!.size) {
                                val cat: Categories = Categories();
                                cat.category_id =
                                    model!!.rec_groups!![i].categories!![j].category_id!!
                                cat.category = model!!.rec_groups!![i].categories!![j].category!!
                                catArray.add(cat)
                            }
                            model_groups.categories = catArray

                            listOfGroupdata.add(
                                GroupsView(
                                    model_groups.id,
                                    model_groups.icon_url!!,
                                    model_groups.label!!,
                                    "",
                                    model_groups.groupType!!,
                                    model_groups.noOfMembers!!,
                                    model_groups.description!!,
                                    model_groups.featured!!,
                                    model_groups.status!!,
                                    model_groups.cities!!,
                                    model_groups.categories!!,
                                    false
                                )
                            )

                            if (i < 3) {
                                listOfGroupdataTrimmed.add(
                                    GroupsView(
                                        model_groups.id,
                                        model_groups.icon_url!!,
                                        model_groups.label!!,
                                        "",
                                        model_groups.groupType!!,
                                        model_groups.noOfMembers!!,
                                        model_groups.description!!,
                                        model_groups.featured!!,
                                        model_groups.status!!,
                                        model_groups.cities!!,
                                        model_groups.categories!!,
                                        false
                                    )
                                )
                            }
                        }

                        if (model!!.rec_groups!!.size > 3) {
                            var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                            mRecyclerViewGroups!!.layoutManager =
                                mgroupsLayoutManager as RecyclerView.LayoutManager?
                            mAdapterGroups = MyGroupsAdapter(
                                listOfGroupdataTrimmed,
                                isLoggedIn,
                                3,
                                listOfCompareGroupdata,
                                0,
                                1,
                                "Dashboard"
                            )
                            mRecyclerViewGroups!!.adapter = mAdapterGroups
                            seemore_groups.visibility = View.VISIBLE
                        } else {
                            var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                            mRecyclerViewGroups!!.layoutManager =
                                mgroupsLayoutManager as RecyclerView.LayoutManager?
                            mAdapterGroups = MyGroupsAdapter(
                                listOfGroupdata,
                                isLoggedIn,
                                3,
                                listOfCompareGroupdata,
                                0,
                                1,
                                "Dashboard"
                            )
                            mRecyclerViewGroups!!.adapter = mAdapterGroups
                            seemore_groups.visibility = View.GONE

                        }
                    } else {
                        groups_recycler_view.visibility = View.GONE
                        default_company_groups.visibility = View.VISIBLE
                        seemore_groups.visibility = View.GONE
                    }

                    // Companies display
                    if (model!!.rec_companies!!.size > 0) {
                        for (i in 0 until model!!.rec_companies!!.size) {
                            val models: CompaniesView = CompaniesView()

                            models.id = model!!.rec_companies!![i].id
                            models.company_type = model!!.rec_companies!![i].company_type
                            models.featured = model!!.rec_companies!![i].featured
                            models.website = model!!.rec_companies!![i].website
                            models.active_jobs_count = model!!.rec_companies!![i].active_jobs_count
                            models.cities = model!!.rec_companies!![i].cities
                            models.follow_count = model!!.rec_companies!![i].follow_count
                            models.name = model!!.rec_companies!![i].name
                            models.logo = model!!.rec_companies!![i].logo
                            models.industry = model!!.rec_companies!![i].industry
                            models.banner_image = model!!.rec_companies!![i].banner_image
                            models.status = model!!.rec_companies!![i].status
                            listOfCompareJoineddata.add(0)
                            listOfCompaniesdata.add(
                                CompaniesView(
                                    models.id,
                                    models.company_type!!,
                                    models.featured!!,
                                    models.website!!,
                                    models.active_jobs_count!!,
                                    models.cities!!,
                                    models.follow_count!!,
                                    models.name!!,
                                    models.logo!!,
                                    models.industry!!,
                                    models.banner_image!!,
                                    models.status!!
                                )
                            )
                            if (i < 3) {
                                listOfCompaniesdataTrimmed.add(
                                    CompaniesView(
                                        models.id,
                                        models.company_type!!,
                                        models.featured!!,
                                        models.website!!,
                                        models.active_jobs_count!!,
                                        models.cities!!,
                                        models.follow_count!!,
                                        models.name!!,
                                        models.logo!!,
                                        models.industry!!,
                                        models.banner_image!!,
                                        models.status!!
                                    )
                                )
                            }
                        }

                        if (model!!.rec_companies!!.size > 3) {
                            var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                            mRecyclerViewCompanies!!.layoutManager =
                                mgroupsLayoutManager as RecyclerView.LayoutManager?
                            mAdapterCompanies = CompaniesAdapter(
                                listOfCompaniesdataTrimmed,
                                isLoggedIn,
                                3,
                                0,
                                listOfCompareJoineddata,
                                1,
                                "Dashboard"
                            )
                            mRecyclerViewCompanies!!.adapter = mAdapterCompanies
                            seemore_companies.visibility = View.VISIBLE
                        } else {
                            var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                            mRecyclerViewCompanies!!.layoutManager =
                                mgroupsLayoutManager as RecyclerView.LayoutManager?
                            mAdapterCompanies = CompaniesAdapter(
                                listOfCompaniesdata,
                                isLoggedIn,
                                3,
                                0,
                                listOfCompareJoineddata,
                                1,
                                "Dashboard"
                            )
                            mRecyclerViewCompanies!!.adapter = mAdapterCompanies
                            seemore_companies.visibility = View.GONE
                        }
                    } else {
                        default_company_companies.visibility = View.VISIBLE
                        mRecyclerViewCompanies!!.visibility = View.GONE
                        seemore_companies.visibility = View.GONE
                    }


                } else {
                    if (message != null) {
                        ToastHelper.makeToast(applicationContext, message)
                    }
                    finish()
                    overridePendingTransition(0, 0);
                }
            }

            override fun onFailure(call: Call<DashboardApiDetails>, t: Throwable) {

                Log.d("TAGG", "FAILED : $t")
            }
        })


        val handler = Handler()
        handler.postDelayed({
            if (pageType == 2) {
                openResumesPage()
                horizontal_layout.visibility = View.GONE
                jobs_layout.visibility = View.GONE
                groups_layout.visibility = View.GONE
                companies_layout.visibility = View.GONE
                mentors_layout.visibility = View.GONE
                events_layout.visibility = View.GONE
                accountsettings_layout.visibility = View.GONE
                preference_layout.visibility = View.GONE
                resumes_layout.visibility = View.VISIBLE
            } else if (pageType == 4) {
                dash_accnt_settings.callOnClick()
            } else if (pageType == 5) {
                dash_my_preferences.callOnClick()
            } else if (pageType == 3) {
                loadJobAlert()
                horizontal_layout.visibility = View.GONE
                jobs_layout.visibility = View.GONE
                groups_layout.visibility = View.GONE
                companies_layout.visibility = View.GONE
                mentors_layout.visibility = View.GONE
                events_layout.visibility = View.GONE
                accountsettings_layout.visibility = View.GONE
                preference_layout.visibility = View.GONE
                resumes_layout.visibility = View.GONE
                jobalert_layout.visibility = View.VISIBLE
            }
        }, 1000)

        listOfEventsdata.clear()
        loadAppliedEvents("1")
        loadRecommendedEvents("1")


    }

    fun loadNotificationbubble() {


        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.GetNotificationBubble(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN
        )

        call.enqueue(object : Callback<NotificationBubbleResponse> {
            override fun onResponse(
                call: Call<NotificationBubbleResponse>,
                response: Response<NotificationBubbleResponse>
            ) {
                var str_response = Gson().toJson(response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                    val responseCode: Int = jsonObject1.optInt("response_code")
                    val message: String = jsonObject1.optString("message")
                    var jsonarray: JSONObject = JSONObject()
                    if (jsonObject1.has("body")) {
                        jsonarray = jsonObject1.optJSONObject("body")
                        if (response.isSuccessful) {

                            if (jsonarray.has("new_notification")) {
                                Log.d("TAGG", "bubble:" + jsonarray.optInt("new_notification"))
                                cart_badge.setText(jsonarray.optInt("new_notification").toString())
                            } else {
                                cart_badge.setText("0")
                            }
                        } else {
                            ToastHelper.makeToast(applicationContext, "message")
                        }
                    }
                }
            }

            override fun onFailure(call: Call<NotificationBubbleResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
            }
        })
    }

    fun openResumesPage() {


        if (resumesdata_Separate.size > 0) {

            if (resumesdata_Separate.size < 5) {
                var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                mRecyclerViewResumes!!.layoutManager =
                    mgroupsLayoutManager as RecyclerView.LayoutManager?
                mAdapterResumesDashboard = ResumeAdapterDashboard(resumesdata_Separate)
                mRecyclerViewResumes!!.adapter = mAdapterResumesDashboard
                default_resumes_companies.visibility = View.GONE
                resumesupload1.visibility = View.GONE
                resumesupload.visibility = View.VISIBLE
            } else {
                var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                mRecyclerViewResumes!!.layoutManager =
                    mgroupsLayoutManager as RecyclerView.LayoutManager?
                mAdapterResumesDashboard = ResumeAdapterDashboard(resumesdata_Separate)
                mRecyclerViewResumes!!.adapter = mAdapterResumesDashboard
                default_resumes_companies.visibility = View.GONE
                resumesupload1.visibility = View.GONE
                resumesupload.visibility = View.GONE
            }

        } else {
            mRecyclerViewResumes!!.visibility = View.GONE
            default_resumes_companies.visibility = View.VISIBLE
            resumesupload1.visibility = View.VISIBLE
            resumesupload.visibility = View.VISIBLE
        }
    }

    fun loadJobAlert() {
        listOfPreferencedata.clear()
        retrofitInterface1 = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface1!!.getJobAlert(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN
        )
        call.enqueue(object : Callback<PreferenceResponse> {
            override fun onResponse(
                call: Call<PreferenceResponse>,
                response: Response<PreferenceResponse>
            ) {

                Logger.d("URL", "Job Alert" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", "Job Alert" + response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "Job Alert" + Gson().toJson(response))

                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )

                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                    val responseCode: Int = jsonObject1.optInt("response_code")
                    val message: String = jsonObject1.optString("message")
                    val jsonarray_info: JSONArray = jsonObject1.optJSONArray("body")

                    if (response.isSuccessful) {
                        for (i in 0 until response.body()!!.body!!.size) {
                            var json_objectdetail: JSONObject = jsonarray_info.optJSONObject(i)
                            val model: PreferenceModel = PreferenceModel();
                            model.id = json_objectdetail.optInt("id")
                            model.industry = json_objectdetail.optString("industry")
                            model.experience_min_year =
                                if (json_objectdetail.isNull("experience_min_year")) 0 else json_objectdetail.optInt(
                                    "experience_min_year"
                                )
                            model.skills = json_objectdetail.optString("skills")
                            model.modified_on = json_objectdetail.optString("modified_on")
                            model.user_id = json_objectdetail.optInt("user_id")
                            model.city_name = json_objectdetail.optString("city_name")
                            model.experience_max_year =
                                if (json_objectdetail.isNull("experience_max_year")) 0 else json_objectdetail.optInt(
                                    "experience_max_year"
                                )
                            model.functional_area = json_objectdetail.optString("functional_area")
                            model.job_type = json_objectdetail.optString("job_type")
                            model.title = json_objectdetail.optString("title")
                            model.id = json_objectdetail.optInt("id")

                            listOfPreferencedata.add(
                                PreferenceModel(
                                    model.industry!!,
                                    model.experience_min_year!!,
                                    model.skills!!,
                                    model.modified_on!!,
                                    model.user_id!!,
                                    model.city_name!!,
                                    model.experience_max_year!!,
                                    model.functional_area!!,
                                    model.job_type!!,
                                    model.title!!,
                                    model.id!!
                                )
                            )
                        }

                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                        mRecyclerViewJobAlert!!.layoutManager =
                            mgroupsLayoutManager as RecyclerView.LayoutManager?
                        mAdapterJobAlert = JobAlertAdapter(listOfPreferencedata)
                        mRecyclerViewJobAlert!!.adapter = mAdapterJobAlert

                        if (listOfPreferencedata.size > 4)
                            jobalertupload.visibility = View.GONE
                        else
                            jobalertupload.visibility = View.VISIBLE

                        if (listOfPreferencedata.size > 0) {
                            default_jobalert_companies.visibility = View.GONE
                            jobalertupload1.visibility = View.GONE
                            mRecyclerViewJobAlert!!.visibility = View.VISIBLE
                        } else {
                            mRecyclerViewJobAlert!!.visibility = View.GONE
                            default_jobalert_companies.visibility = View.VISIBLE
                            jobalertupload1.visibility = View.VISIBLE
                        }

                    } else {
                        ToastHelper.makeToast(applicationContext, message)
                        default_jobalert_companies.visibility = View.VISIBLE
                        jobalertupload1.visibility = View.VISIBLE
                        mRecyclerViewJobAlert!!.visibility = View.GONE
                    }
                }
            }

            override fun onFailure(call: Call<PreferenceResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                default_jobalert_companies.visibility = View.VISIBLE
                jobalertupload1.visibility = View.VISIBLE
                mRecyclerViewJobAlert!!.visibility = View.GONE
            }
        })

    }

    fun loadAccountSettings() {

        retrofitInterface1 = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface1!!.getAccountSettingsData(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN
        )
        call.enqueue(object : Callback<AccountSettingsDetails> {

            override fun onResponse(
                call: Call<AccountSettingsDetails>,
                response: Response<AccountSettingsDetails>
            ) {


                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )

                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                    val responseCode: Int = jsonObject1.optInt("response_code")
                    val message: String = jsonObject1.optString("message")
                    val jsonarray_info: JSONObject = jsonObject1.optJSONObject("body")
                    Log.d("TAGG", "Settings " + jsonarray_info.toString())

                    if (response.isSuccessful) {

                        fullnameClick.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG)
                        emailClick.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG)
                        secemailClick.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG)
                        removeemail.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG)
                        binaryemailClick.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG)
                        mobileClick.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG)
                        secemailAdd.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG)

                        fullname.setText(jsonarray_info.optString("username"))
                        email.setText(jsonarray_info.optString("email"))
                        pemail = jsonarray_info.optString("email")
                        //userId = jsonarray_info.optInt("id")
                        email_resendp.setTag(R.string.Follow_us_on, jsonarray_info.optInt("id"))
                        if (jsonarray_info.optBoolean("verified_email")) {
                            textemailp.visibility = View.GONE
                            email_resendp.visibility = View.GONE
                            emailverify_bottom.visibility = View.GONE
                        } else {
                            textemailp.visibility = View.VISIBLE
                            email_resendp.visibility = View.VISIBLE
                            emailverify_bottom.visibility = View.VISIBLE
                        }

                        if (jsonarray_info.isNull("secondary_email") || jsonarray_info.optString("secondary_email")
                                .equals("")
                        ) {
                            secemail.setText("")
                            secemailClick.visibility = View.GONE
                            secemailAdd.visibility = View.VISIBLE
                            secemail_layout.visibility = View.GONE
                            textemail.visibility = View.GONE
                            email_resend.visibility = View.GONE
                        } else {
                            secemail.setText(jsonarray_info.optString("secondary_email"))
                            secemailClick.visibility = View.VISIBLE
                            secemailAdd.visibility = View.GONE
                            secemail_layout.visibility = View.VISIBLE
                            textemail.visibility = View.VISIBLE
                            email_resend.visibility = View.VISIBLE
                        }
                        if (jsonarray_info.optBoolean("secondary_email_verified")) {
                            textemailp.visibility = View.GONE
                            email_resendp.visibility = View.GONE
                        } else {
                            textemailp.visibility = View.VISIBLE
                            email_resendp.visibility = View.VISIBLE
                        }
                        mobile.setText(jsonarray_info.optString("phone_no"))
                        if (jsonarray_info.optBoolean("phone_no_verified")) {
                            textmobile.visibility = View.GONE
                            mmobile_verify.visibility = View.GONE
                        } else {
                            textmobile.visibility = View.VISIBLE
                            mmobile_verify.visibility = View.VISIBLE
                        }

                    } else {
                        ToastHelper.makeToast(applicationContext, message)
                    }

                }
            }

            override fun onFailure(call: Call<AccountSettingsDetails>, t: Throwable) {
                Logger.d("TAGG", "FAILED AccNT Settings : $t")
            }
        })
    }

    fun loadRecommendedJobs(pageno: String) {


        val params = java.util.HashMap<String, String>()

        params["page_no"] = pageno.toString()
        params["page_size"] = 3.toString()
        retrofitInterface1 = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface1!!.getRecommendedJobs(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<HotJobs> {
            override fun onResponse(call: Call<HotJobs>, response: Response<HotJobs>) {

                Logger.d("URL", "Applied" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", "Applied" + response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "Applied OnClick" + Gson().toJson(response))

                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )

                var jsonObject1: JSONObject? = null
                var jsonarray_info: JSONArray? = null

                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    jsonObject1 = jsonObject.optJSONObject("body")
                }

                if (jsonObject1 != null) {
                    val responseCode: Int = jsonObject1.optInt("response_code")
                    val message: String = jsonObject1.optString("message")
                    jsonarray_info = jsonObject1.optJSONArray("body")
                    var jsonarray_pagination: JSONObject = jsonObject1.optJSONObject("pagination")
                    hasnextRecommendedJobs = jsonarray_pagination.optBoolean("has_next")
                    val pagenoo: String = jsonarray_pagination.optString("next_page")
                    if (hasnextRecommendedJobs) {
                        seemore_jobs.visibility = View.VISIBLE
                        pagenoRecommendedJobs = pagenoo
                    } else
                        seemore_jobs.visibility = View.GONE
                }

                if (response.isSuccessful) {

                    for (k in 0 until response.body()!!.body!!.size) {
                        var jobs: JSONObject = jsonarray_info?.optJSONObject(k)!!
                        val model1: AppliedJobDetailsData = AppliedJobDetailsData();

                        val jobsModel: Rec_Jobs = Rec_Jobs()
                        jobsModel.modified_on =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("modified_on")
                            ) "" else jsonarray_info.optJSONObject(k).optString(
                                "modified_on"
                            )
                        jobsModel.application_count =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("application_count")
                            ) 0 else jsonarray_info.optJSONObject(k).optInt(
                                "application_count"
                            )
                        jobsModel.resume_required =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("resume_required")
                            ) false else jsonarray_info.optJSONObject(k).optBoolean(
                                "resume_required"
                            )
                        jobsModel.company_id =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("company_id")
                            ) 0 else jsonarray_info.optJSONObject(k).optInt(
                                "company_id"
                            )
                        var jobs_types: JSONArray
                        var jobsdata_types: ArrayList<String> = ArrayList()
                        if (jsonarray_info.optJSONObject(k).isNull("job_types")) {
                        } else {
                            jobs_types = jsonarray_info.optJSONObject(k).optJSONArray("job_types")
                            for (a in 0 until jobs_types.length()) {
                                jobsdata_types.add(jobs_types.optString(a))
                            }
                        }
                        jobsModel.job_types = jobsdata_types

                        jobsModel.employer_name =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("employer_name")
                            ) "" else jsonarray_info.optJSONObject(k).optString("employer_name")

                        jobsModel.new_application_count =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("new_application_count")
                            ) 0 else jsonarray_info.optJSONObject(k).optInt(
                                "new_application_count"
                            )
                        jobsModel.title =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("title")
                            ) "" else jsonarray_info.optJSONObject(k).optString("title")
                        jobsModel.max_year =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("max_year")
                            ) 0 else jsonarray_info.optJSONObject(k).optInt("max_year")

                        jobsModel.location_name =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("location_name")
                            ) "" else jsonarray_info.optJSONObject(k).optString(
                                "location_name"
                            )
                        jobsModel.company_logo =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("company_logo")
                            ) "" else jsonarray_info.optJSONObject(
                                k
                            ).optString("company_logo")
                        jobsModel.created_on =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("created_on")
                            ) "" else jsonarray_info.optJSONObject(k).optString(
                                "created_on"
                            )
                        jobsModel.company_name =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("company_name")
                            ) "" else jsonarray_info.optJSONObject(k).optString(
                                "company_name"
                            )
                        jobsModel.id =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("id")
                            ) 0 else jsonarray_info.optJSONObject(k).optInt("id")
                        jobsModel.boosted =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("boosted")
                            ) false else jsonarray_info.optJSONObject(k).optBoolean(
                                "boosted"
                            )

                        jobsModel.min_year =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("min_year")
                            ) 0 else jsonarray_info.optJSONObject(k).optInt("min_year")

                        jobsModel.employer_id =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("employer_id")
                            ) 0 else jsonarray_info.optJSONObject(k).optInt("employer_id")
                        jobsModel.status =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("status")
                            ) "" else jsonarray_info.optJSONObject(k).optString("status")



                        listOfhotJobsdata.add(
                            JobsView(
                                jobsModel.id,
                                jobsModel.modified_on!!,
                                jobsModel.application_count!!,
                                jobsModel.company_id!!,
                                jobsModel.job_types!!,
                                jobsModel.employer_name!!,
                                jobsModel.new_application_count!!,
                                jobsModel.title!!,
                                jobsModel.max_year!!,
                                jobsModel.location_name!!,
                                jobsModel.company_logo!!,
                                jobsModel.created_on!!,
                                jobsModel.company_name!!,
                                jobsModel.boosted!!,
                                jobsModel.min_year!!,
                                jobsModel.employer_id!!,
                                jobsModel.status!!,
                                jobsModel.resume_required!!
                            )
                        )
                        listOfCompareJoineddata.add(0)
                        if (k < 3) {
                            listOfhotJobsdataTrimmed.add(
                                JobsView(
                                    jobsModel.id,
                                    jobsModel.modified_on!!,
                                    jobsModel.application_count!!,
                                    jobsModel.company_id!!,
                                    jobsModel.job_types!!,
                                    jobsModel.employer_name!!,
                                    jobsModel.new_application_count!!,
                                    jobsModel.title!!,
                                    jobsModel.max_year!!,
                                    jobsModel.location_name!!,
                                    jobsModel.company_logo!!,
                                    jobsModel.created_on!!,
                                    jobsModel.company_name!!,
                                    jobsModel.boosted!!,
                                    jobsModel.min_year!!,
                                    jobsModel.employer_id!!,
                                    jobsModel.status!!,
                                    jobsModel.resume_required!!
                                )
                            )
                        }
                    }

//                    if (listOfAppliedJobsdata.size>3) {
//                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
//                        mRecyclerViewPosts!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
//                        mAdapterJobs = JobsAdapter(listOfAppliedJobsdataTrimmed, isLoggedIn, 2, 0, listOfCompareJoineddata,1)
//                        mRecyclerViewPosts!!.adapter = mAdapterJobs
//                    }
//                    else{
                    if (listOfhotJobsdata.size > 0) {
                        mRecyclerViewPosts!!.visibility = View.VISIBLE
                        default_company_jobs.visibility = View.GONE

                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                        mRecyclerViewPosts!!.layoutManager =
                            mgroupsLayoutManager as RecyclerView.LayoutManager?
                        mAdapterJobs =
                            JobsAdapter(
                                listOfhotJobsdata,
                                isLoggedIn,
                                2,
                                0,
                                listOfCompareJoineddata,
                                1,
                                "Dashboard"
                            )
                        mRecyclerViewPosts!!.adapter = mAdapterJobs
                    } else {
                        mRecyclerViewPosts!!.visibility = View.GONE
                        default_company_jobs.visibility = View.VISIBLE
                    }
//                    }

                } else {
                    if (jsonObject1 != null) {
                        ToastHelper.makeToast(applicationContext, jsonObject1.optString("message"))
                    }
                }
            }

            override fun onFailure(call: Call<HotJobs>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                mRecyclerViewPosts!!.visibility = View.GONE
                default_company_jobs.visibility = View.VISIBLE
                seemore_jobs.visibility = View.GONE
            }
        })
    }

    fun loadAppliedJobs(pageno: String) {


        val params = java.util.HashMap<String, String>()

        params["page_no"] = pageno.toString()
        params["page_size"] = 3.toString()
        retrofitInterface1 = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface1!!.getAppliedJobs(
            "application/json",
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<AppliedJobDetails> {
            override fun onResponse(
                call: Call<AppliedJobDetails>,
                response: Response<AppliedJobDetails>
            ) {


                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )

                var jsonObject1: JSONObject? = null
                var jsonarray_info: JSONArray? = null

                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    jsonObject1 = jsonObject.optJSONObject("body")
                }
                if (jsonObject1 != null) {
                    val responseCode: Int = jsonObject1.optInt("response_code")
                    val message: String = jsonObject1.optString("message")
                    jsonarray_info = jsonObject1.optJSONArray("body")
                    var jsonarray_pagination: JSONObject = jsonObject1.optJSONObject("pagination")
                    hasnextAppliedJobs = jsonarray_pagination.optBoolean("has_next")
                    val pagenoo: String = jsonarray_pagination.optString("next_page")
                    if (hasnextAppliedJobs) {
                        seemore_jobs.visibility = View.VISIBLE
                        pagenoAppliedJobs = pagenoo
                    } else
                        seemore_jobs.visibility = View.GONE
                }

                if (response.isSuccessful) {

                    for (k in 0 until response.body()!!.body!!.size) {
                        var jobs: JSONObject = jsonarray_info?.optJSONObject(k)!!
                        val model1: AppliedJobDetailsData = AppliedJobDetailsData();

                        val jobsModel: Rec_Jobs = Rec_Jobs()
                        jobsModel.modified_on =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("modified_on")
                            ) "" else jsonarray_info.optJSONObject(k).optString(
                                "modified_on"
                            )
                        jobsModel.application_count =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("application_count")
                            ) 0 else jsonarray_info.optJSONObject(k).optInt(
                                "application_count"
                            )
                        jobsModel.resume_required =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("resume_required")
                            ) false else jsonarray_info.optJSONObject(k).optBoolean(
                                "resume_required"
                            )
                        jobsModel.company_id =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("company_id")
                            ) 0 else jsonarray_info.optJSONObject(k).optInt(
                                "company_id"
                            )
                        var jobs_types: JSONArray
                        var jobsdata_types: ArrayList<String> = ArrayList()
                        if (jsonarray_info.optJSONObject(k).isNull("job_types")) {
                        } else {
                            jobs_types = jsonarray_info.optJSONObject(k).optJSONArray("job_types")
                            for (a in 0 until jobs_types.length()) {
                                jobsdata_types.add(jobs_types.optString(a))
                            }
                        }
                        jobsModel.job_types = jobsdata_types

                        jobsModel.employer_name =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("employer_name")
                            ) "" else jsonarray_info.optJSONObject(k).optString("employer_name")

                        jobsModel.new_application_count =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("new_application_count")
                            ) 0 else jsonarray_info.optJSONObject(k).optInt(
                                "new_application_count"
                            )
                        jobsModel.title =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("title")
                            ) "" else jsonarray_info.optJSONObject(k).optString("title")
                        jobsModel.max_year =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("max_year")
                            ) 0 else jsonarray_info.optJSONObject(k).optInt("max_year")

                        jobsModel.location_name =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("location_name")
                            ) "" else jsonarray_info.optJSONObject(k).optString(
                                "location_name"
                            )
                        jobsModel.company_logo =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("company_logo")
                            ) "" else jsonarray_info.optJSONObject(
                                k
                            ).optString("company_logo")
                        jobsModel.created_on =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("created_on")
                            ) "" else jsonarray_info.optJSONObject(k).optString(
                                "created_on"
                            )
                        jobsModel.company_name =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("company_name")
                            ) "" else jsonarray_info.optJSONObject(k).optString(
                                "company_name"
                            )
                        jobsModel.id =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("id")
                            ) 0 else jsonarray_info.optJSONObject(k).optInt("id")
                        jobsModel.boosted =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("boosted")
                            ) false else jsonarray_info.optJSONObject(k).optBoolean(
                                "boosted"
                            )

                        jobsModel.min_year =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("min_year")
                            ) 0 else jsonarray_info.optJSONObject(k).optInt("min_year")

                        jobsModel.employer_id =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("employer_id")
                            ) 0 else jsonarray_info.optJSONObject(k).optInt("employer_id")
                        jobsModel.status =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("status")
                            ) "" else jsonarray_info.optJSONObject(k).optString("status")



                        listOfAppliedJobsdata.add(
                            JobsView(
                                jobsModel.id,
                                jobsModel.modified_on!!,
                                jobsModel.application_count!!,
                                jobsModel.company_id!!,
                                jobsModel.job_types!!,
                                jobsModel.employer_name!!,
                                jobsModel.new_application_count!!,
                                jobsModel.title!!,
                                jobsModel.max_year!!,
                                jobsModel.location_name!!,
                                jobsModel.company_logo!!,
                                jobsModel.created_on!!,
                                jobsModel.company_name!!,
                                jobsModel.boosted!!,
                                jobsModel.min_year!!,
                                jobsModel.employer_id!!,
                                jobsModel.status!!,
                                jobsModel.resume_required!!
                            )
                        )
                        listOfCompareJoineddata.add(jobsModel.id!!)
                        if (k < 3) {
                            listOfAppliedJobsdataTrimmed.add(
                                JobsView(
                                    jobsModel.id,
                                    jobsModel.modified_on!!,
                                    jobsModel.application_count!!,
                                    jobsModel.company_id!!,
                                    jobsModel.job_types!!,
                                    jobsModel.employer_name!!,
                                    jobsModel.new_application_count!!,
                                    jobsModel.title!!,
                                    jobsModel.max_year!!,
                                    jobsModel.location_name!!,
                                    jobsModel.company_logo!!,
                                    jobsModel.created_on!!,
                                    jobsModel.company_name!!,
                                    jobsModel.boosted!!,
                                    jobsModel.min_year!!,
                                    jobsModel.employer_id!!,
                                    jobsModel.status!!,
                                    jobsModel.resume_required!!
                                )
                            )
                        }
                    }

//                    if (listOfAppliedJobsdata.size>3) {
//                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
//                        mRecyclerViewPosts!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
//                        mAdapterJobs = JobsAdapter(listOfAppliedJobsdataTrimmed, isLoggedIn, 2, 0, listOfCompareJoineddata,1)
//                        mRecyclerViewPosts!!.adapter = mAdapterJobs
//                    }
//                    else{

                    if (listOfAppliedJobsdata.size > 0) {
                        mRecyclerViewPosts!!.visibility = View.VISIBLE
                        default_company_jobs.visibility = View.GONE
                        //seemore_jobs.visibility = View.VISIBLE
                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                        mRecyclerViewPosts!!.layoutManager =
                            mgroupsLayoutManager as RecyclerView.LayoutManager?
                        mAdapterJobs =
                            JobsAdapter(
                                listOfAppliedJobsdata,
                                isLoggedIn,
                                2,
                                0,
                                listOfCompareJoineddata,
                                1,
                                "Dashboard"
                            )
                        mRecyclerViewPosts!!.adapter = mAdapterJobs
                    } else {
                        mRecyclerViewPosts!!.visibility = View.GONE
                        default_company_jobs.visibility = View.VISIBLE
                    }
//                    }

                } else {
                    if (jsonObject1 != null) {
                        ToastHelper.makeToast(applicationContext, jsonObject1.optString("message"))
                    }
                }
            }

            override fun onFailure(call: Call<AppliedJobDetails>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                mRecyclerViewPosts!!.visibility = View.GONE
                default_company_jobs.visibility = View.VISIBLE
                //default_company_jobs.setText("You haven't applied to any jobs. Browse through hundreds of jobs at jobsforher.com/jobs to find the right one for you.")
                default_company_jobs.setText(Html.fromHtml("You haven't applied to any jobs. Browse through hundreds of jobs at <font color='#3FA9E6'><u>Jobs</u></font> to find the right one for you."));

                seemore_jobs.visibility = View.GONE
            }
        })
    }

    fun loadSavedJobs(pageno: String) {


        val params = java.util.HashMap<String, String>()

        params["page_no"] = pageno.toString()
        params["page_size"] = 3.toString()
        retrofitInterface1 = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface1!!.getSavedJobs(
            "application/json",
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<AppliedJobDetails> {
            override fun onResponse(
                call: Call<AppliedJobDetails>,
                response: Response<AppliedJobDetails>
            ) {

                Logger.d("URL", "Applied" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", "Applied" + response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "Applied" + Gson().toJson(response))

                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )

                var jsonObject1: JSONObject? = null
                var jsonarray_info: JSONArray? = null

                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    jsonObject1 = jsonObject.optJSONObject("body")
                }
                if (jsonObject1 != null) {
                    val responseCode: Int = jsonObject1.optInt("response_code")
                    val message: String = jsonObject1.optString("message")
                    jsonarray_info = jsonObject1.optJSONArray("body")
                    Log.d("TAGG", "Applied " + jsonarray_info.toString())
                    var jsonarray_pagination: JSONObject = jsonObject1.optJSONObject("pagination")
                    hasnextSavedJobs = jsonarray_pagination.optBoolean("has_next")
                    val pagenoo: String = jsonarray_pagination.optString("next_page")
                    Log.d("RESPONSE", "VALUE IS " + hasnextSavedJobs.toString())
                    if (hasnextSavedJobs) {
                        seemore_jobs.visibility = View.VISIBLE
                        pagenoSavedJobs = pagenoo
                    } else
                        seemore_jobs.visibility = View.GONE
                }
                if (response.isSuccessful) {

                    for (k in 0 until response.body()!!.body!!.size) {
                        var jobs: JSONObject = jsonarray_info?.optJSONObject(k)!!
                        val model1: AppliedJobDetailsData = AppliedJobDetailsData();

                        val jobsModel: Rec_Jobs = Rec_Jobs()
                        jobsModel.modified_on =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("modified_on")
                            ) "" else jsonarray_info.optJSONObject(k).optString(
                                "modified_on"
                            )
                        jobsModel.application_count =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("application_count")
                            ) 0 else jsonarray_info.optJSONObject(k).optInt(
                                "application_count"
                            )
                        jobsModel.resume_required =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("resume_required")
                            ) false else jsonarray_info.optJSONObject(k).optBoolean(
                                "resume_required"
                            )
                        jobsModel.company_id =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("company_id")
                            ) 0 else jsonarray_info.optJSONObject(k).optInt(
                                "company_id"
                            )
                        var jobs_types: JSONArray
                        var jobsdata_types: ArrayList<String> = ArrayList()
                        if (jsonarray_info.optJSONObject(k).isNull("job_types")) {
                        } else {
                            jobs_types = jsonarray_info.optJSONObject(k).optJSONArray("job_types")
                            for (a in 0 until jobs_types.length()) {
                                jobsdata_types.add(jobs_types.optString(a))
                            }
                        }
                        jobsModel.job_types = jobsdata_types

                        jobsModel.employer_name =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("employer_name")
                            ) "" else jsonarray_info.optJSONObject(k).optString("employer_name")

                        jobsModel.new_application_count =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("new_application_count")
                            ) 0 else jsonarray_info.optJSONObject(k).optInt(
                                "new_application_count"
                            )
                        jobsModel.title =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("title")
                            ) "" else jsonarray_info.optJSONObject(k).optString("title")
                        jobsModel.max_year =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("max_year")
                            ) 0 else jsonarray_info.optJSONObject(k).optInt("max_year")

                        jobsModel.location_name =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("location_name")
                            ) "" else jsonarray_info.optJSONObject(k).optString(
                                "location_name"
                            )
                        jobsModel.company_logo =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("company_logo")
                            ) "" else jsonarray_info.optJSONObject(
                                k
                            ).optString("company_logo")
                        jobsModel.created_on =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("created_on")
                            ) "" else jsonarray_info.optJSONObject(k).optString(
                                "created_on"
                            )
                        jobsModel.company_name =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("company_name")
                            ) "" else jsonarray_info.optJSONObject(k).optString(
                                "company_name"
                            )
                        jobsModel.id =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("id")
                            ) 0 else jsonarray_info.optJSONObject(k).optInt("id")
                        jobsModel.boosted =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("boosted")
                            ) false else jsonarray_info.optJSONObject(k).optBoolean(
                                "boosted"
                            )

                        jobsModel.min_year =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("min_year")
                            ) 0 else jsonarray_info.optJSONObject(k).optInt("min_year")

                        jobsModel.employer_id =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("employer_id")
                            ) 0 else jsonarray_info.optJSONObject(k).optInt("employer_id")
                        jobsModel.status =
                            if (jsonarray_info.optJSONObject(k)
                                    .isNull("status")
                            ) "" else jsonarray_info.optJSONObject(k).optString("status")

                        if (jsonarray_info.optJSONObject(k).isNull("applied_status")) {
                        } else {
                            if (jsonarray_info.optJSONObject(k).optString("applied_status")
                                    .equals("applied")
                            )
                                listOfCompareJoineddata.add(jobsModel.id)
                        }

                        listOfSavedJobsdata.add(
                            JobsView(
                                jobsModel.id,
                                jobsModel.modified_on!!,
                                jobsModel.application_count!!,
                                jobsModel.company_id!!,
                                jobsModel.job_types!!,
                                jobsModel.employer_name!!,
                                jobsModel.new_application_count!!,
                                jobsModel.title!!,
                                jobsModel.max_year!!,
                                jobsModel.location_name!!,
                                jobsModel.company_logo!!,
                                jobsModel.created_on!!,
                                jobsModel.company_name!!,
                                jobsModel.boosted!!,
                                jobsModel.min_year!!,
                                jobsModel.employer_id!!,
                                jobsModel.status!!,
                                jobsModel.resume_required!!
                            )
                        )

                    }

                    mRecyclerViewPosts!!.visibility = View.VISIBLE
                    default_company_jobs.visibility = View.GONE
                    //seemore_jobs.visibility = View.VISIBLE

                    if (listOfSavedJobsdata.size > 3) {
                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                        mRecyclerViewPosts!!.layoutManager =
                            mgroupsLayoutManager as RecyclerView.LayoutManager?
                        mAdapterJobs = JobsAdapter(
                            listOfSavedJobsdataTrimmed,
                            isLoggedIn,
                            2,
                            0,
                            listOfCompareJoineddata,
                            1,
                            "Dashboard"
                        )
                        mRecyclerViewPosts!!.adapter = mAdapterJobs
                    } else {
                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                        mRecyclerViewPosts!!.layoutManager =
                            mgroupsLayoutManager as RecyclerView.LayoutManager?
                        mAdapterJobs =
                            JobsAdapter(
                                listOfSavedJobsdata,
                                isLoggedIn,
                                2,
                                0,
                                listOfCompareJoineddata,
                                1,
                                "Dashboard"
                            )
                        mRecyclerViewPosts!!.adapter = mAdapterJobs
                    }

                } else {
                    if (jsonObject1 != null) {
                        ToastHelper.makeToast(applicationContext, jsonObject1.optString("message"))
                    }
                }
            }

            override fun onFailure(call: Call<AppliedJobDetails>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                mRecyclerViewPosts!!.visibility = View.GONE
                default_company_jobs.visibility = View.VISIBLE
                // default_company_jobs.setText("You haven't saved any jobs. Browse through hundreds of jobs at jobsforher.com/jobs, find one that fits your profile, and save it here to review it at a later date.")
                default_company_jobs.setText(Html.fromHtml("You haven't saved any jobs. Browse through hundreds of jobs at <font color='#3FA9E6'><u>Jobs</u></font> find one that fits your profile, and save it here to review it at a later date."));

                seemore_jobs.visibility = View.GONE
            }
        })
    }

    fun loadRecommendedMyGroupData(pageno: String) {

        rec_groups.setBackgroundTintList(null)
        app_groups.setBackgroundTintList(resources.getColorStateList(R.color.greyseparator_company))
        rec_groups_button.setTextColor(resources.getColor(R.color.green))
        app_groups_button.setTextColor(resources.getColor(R.color.black))
        mark4.setBackgroundResource(R.drawable.circle_dashboard)
        mark5.setBackgroundResource(R.drawable.greycircle_dashboard)


        val params = java.util.HashMap<String, String>()

        params["page_no"] = pageno.toString()
        params["page_size"] = 3.toString()

        mRecyclerViewCetificates = findViewById(R.id.recycler_view_groups)
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface!!.getRecommendedMyGroupData(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<Featured_Group> {
            override fun onResponse(
                call: Call<Featured_Group>,
                response: Response<Featured_Group>
            ) {

                Logger.d("URL", "" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)

                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )
                Log.d("TAGG", "Outside body")
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {

                    Log.d("TAGG", "Inside body")
                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")

                    var jsonarray_info: JSONArray = jsonObject1.optJSONArray("body")

                    var jsonarray_pagination: JSONObject = jsonObject1.optJSONObject("pagination")

                    hasnextRecommendedGroups = jsonarray_pagination.optBoolean("has_next")

                    val pagenoo: String = jsonarray_pagination.optString("next_page")

                    if (hasnextRecommendedGroups) {
                        seemore_groups.visibility = View.VISIBLE
                        pagenoRecommendedGroups = pagenoo
                    } else
                        seemore_groups.visibility = View.GONE

                    if (response.isSuccessful) {

//                        ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                        Log.d("TAGG", "Inside response success")
                        for (i in 0 until response.body()!!.body!!.size) {
                            Log.d("TAGG", "Inside response body")
                            var json_objectdetail: JSONObject = jsonarray_info.optJSONObject(i)

                            val model: GroupsView = GroupsView();

                            model.id = json_objectdetail.optInt("id")
                            model.label = json_objectdetail.optString("name")
                            model.icon_url = json_objectdetail.optString("icon_url")
                            model.groupType = json_objectdetail.optString("visiblity_type")
                            model.noOfMembers = json_objectdetail.optString("no_of_members")
                            model.description = json_objectdetail.optString("excerpt")
                            model.featured = json_objectdetail.optBoolean("featured")
                            model.status = json_objectdetail.optString("status")
                            model.is_member = false//json_objectdetail.optBoolean("is_member")

                            var citiesArray: JSONArray = json_objectdetail.optJSONArray("cities")
                            val listOfCity: ArrayList<Int> = ArrayList()
//                        for (j in 0 until citiesArray.length()) {
//                            var citiesObject:JSONObject=citiesArray.optJSONObject(j).
//                            listOfCity.add(citiesObject)
//                        }
                            val categoriesArray: JSONArray =
                                json_objectdetail.optJSONArray("categories")
                            val listOfCategories: ArrayList<Categories> = ArrayList()
                            for (k in 0 until categoriesArray.length()) {
                                var categoriesIdObj: JSONObject = categoriesArray.optJSONObject(k)
                                listOfCategories.add(
                                    Categories(
                                        categoriesIdObj.optInt("category_id"),
                                        categoriesIdObj.optString("category")
                                    )
                                )
                            }
                            model.categories = listOfCategories
                            model.cities = listOfCity


                            listOfGroupdata.add(
                                GroupsView(
                                    model.id,
                                    model.icon_url!!,
                                    model.label!!,
                                    "",
                                    model.groupType!!,
                                    model.noOfMembers!!,
                                    model.description!!,
                                    model.featured!!,
                                    model.status!!,
                                    model.cities!!,
                                    model.categories!!,
                                    model.is_member!!
                                )
                            )

                        }
                        mRecyclerViewGroups!!.visibility = View.VISIBLE
                        default_company_groups.visibility = View.GONE
                        // seemore_groups.visibility = View.VISIBLE

                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                        mRecyclerViewGroups!!.layoutManager =
                            mgroupsLayoutManager as RecyclerView.LayoutManager?
                        mAdapterGroups = MyGroupsAdapter(
                            listOfGroupdata,
                            isLoggedIn,
                            3,
                            listOfCompareGroupdata,
                            0,
                            1,
                            "Dashboard"
                        )
                        mRecyclerViewGroups!!.adapter = mAdapterGroups

                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid Request")
                    }
                } else
                    Toast.makeText(applicationContext, "No data exists", Toast.LENGTH_LONG).show()

            }

            override fun onFailure(call: Call<Featured_Group>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                //Toast.makeText(applicationContext, "No Groups Exixts, Join a group today!", Toast.LENGTH_LONG).show()
                mRecyclerViewGroups!!.visibility = View.GONE
                default_company_groups.visibility = View.VISIBLE
                // default_company_groups.setText("There are no recommended groups for you at the moment. Meet peers, interact with experts and stay up-to-date with industry trends by joining the many JobsForHer Groups here: jobsforher.com/groups")
                default_company_groups.setText(Html.fromHtml("There are no recommended groups for you at the moment. Meet peers, interact with experts and stay up-to-date with industry trends by joining the many JobsForHer Groups here: <font color='#3FA9E6'><u>Groups</u></font> "));

                seemore_groups.visibility = View.GONE
            }
        })
    }

    fun loadMyGroupData(pageno: String) {

        rec_groups.setBackgroundTintList(resources.getColorStateList(R.color.greyseparator_company))
        app_groups.setBackgroundTintList(null)
        rec_groups_button.setTextColor(resources.getColor(R.color.black))
        app_groups_button.setTextColor(resources.getColor(R.color.green))
        mark4.setBackgroundResource(R.drawable.greycircle_dashboard)
        mark5.setBackgroundResource(R.drawable.circle_dashboard)


        val params = java.util.HashMap<String, String>()

        params["page_no"] = pageno.toString()
        params["page_size"] = 3.toString()

        mRecyclerViewCetificates = findViewById(R.id.recycler_view_groups)
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface!!.getMyGroupData(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<Featured_Group> {
            override fun onResponse(
                call: Call<Featured_Group>,
                response: Response<Featured_Group>
            ) {

                Logger.d("URL", "" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)

                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )
                Log.d("TAGG", "Outside body")
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {

                    Log.d("TAGG", "Inside body")
                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")

                    var jsonarray_info: JSONArray = jsonObject1.optJSONArray("body")

                    var jsonarray_pagination: JSONObject = jsonObject1.optJSONObject("pagination")

                    hasnextMyGroups = jsonarray_pagination.optBoolean("has_next")

                    val pagenoo: String = jsonarray_pagination.optString("next_page")
                    mark5.setText(jsonarray_pagination.optString("total_items"))

                    if (hasnextMyGroups) {
                        seemore_groups.visibility = View.VISIBLE
                        pagenoMyGroups = pagenoo
                    } else
                        seemore_groups.visibility = View.GONE

                    if (response.isSuccessful) {

//                        ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                        Log.d("TAGG", "Inside response success")
                        for (i in 0 until response.body()!!.body!!.size) {
                            Log.d("TAGG", "Inside response body")
                            var json_objectdetail: JSONObject = jsonarray_info.optJSONObject(i)

                            val model: GroupsView = GroupsView();

                            model.id = json_objectdetail.optInt("id")
                            model.label = json_objectdetail.optString("name")
                            model.icon_url = json_objectdetail.optString("icon_url")
                            model.groupType = json_objectdetail.optString("visiblity_type")
                            model.noOfMembers = json_objectdetail.optString("no_of_members")
                            model.description = json_objectdetail.optString("excerpt")
                            model.featured = json_objectdetail.optBoolean("featured")
                            model.status = json_objectdetail.optString("status")
                            model.is_member = json_objectdetail.optBoolean("is_member")

                            var citiesArray: JSONArray = json_objectdetail.optJSONArray("cities")
                            val listOfCity: ArrayList<Int> = ArrayList()
//                        for (j in 0 until citiesArray.length()) {
//                            var citiesObject:JSONObject=citiesArray.optJSONObject(j).
//                            listOfCity.add(citiesObject)
//                        }
                            val categoriesArray: JSONArray =
                                json_objectdetail.optJSONArray("categories")
                            val listOfCategories: ArrayList<Categories> = ArrayList()
                            for (k in 0 until categoriesArray.length()) {
                                var categoriesIdObj: JSONObject = categoriesArray.optJSONObject(k)
                                listOfCategories.add(
                                    Categories(
                                        categoriesIdObj.optInt("category_id"),
                                        categoriesIdObj.optString("category")
                                    )
                                )
                            }
                            model.categories = listOfCategories
                            model.cities = listOfCity


                            listOfMyGroupdata.add(
                                GroupsView(
                                    model.id,
                                    model.icon_url!!,
                                    model.label!!,
                                    "",
                                    model.groupType!!,
                                    model.noOfMembers!!,
                                    model.description!!,
                                    model.featured!!,
                                    model.status!!,
                                    model.cities!!,
                                    model.categories!!,
                                    model.is_member!!
                                )
                            )

                        }

                        mRecyclerViewGroups!!.visibility = View.VISIBLE
                        default_company_groups.visibility = View.GONE
                        // seemore_groups.visibility = View.VISIBLE

                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                        mRecyclerViewGroups!!.layoutManager =
                            mgroupsLayoutManager as RecyclerView.LayoutManager?
                        mAdapterGroups = MyGroupsAdapter(
                            listOfMyGroupdata,
                            isLoggedIn,
                            1,
                            listOfCompareGroupdata,
                            0,
                            1,
                            "Dashboard"
                        )
                        mRecyclerViewGroups!!.adapter = mAdapterGroups

                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid Request")
                    }
                } else
                    Toast.makeText(applicationContext, "No data exists", Toast.LENGTH_LONG).show()

            }

            override fun onFailure(call: Call<Featured_Group>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                //Toast.makeText(applicationContext, "No Groups Exixts, Join a group today!", Toast.LENGTH_LONG).show()
                mark5.setText("0")
                mRecyclerViewGroups!!.visibility = View.GONE
                default_company_groups.visibility = View.VISIBLE
                //default_company_groups.setText("You have not joined any groups. Meet peers, interact with experts and stay up-to-date with industry trends by joining the many JobsForHer Groups here: jobsforher.com/groups")
                default_company_groups.setText(Html.fromHtml("You have not joined any groups. Meet peers, interact with experts and stay up-to-date with industry trends by joining the many JobsForHer Groups here: <font color='#3FA9E6'><u>Groups</u></font> "));

                seemore_groups.visibility = View.GONE
            }
        })
    }

    fun loadRecommendedCompanies(pageno: String) {

        rec_companies.setBackgroundTintList(null)
        app_copanies.setBackgroundTintList(resources.getColorStateList(R.color.greyseparator_company))
        rec_companies_button.setTextColor(resources.getColor(R.color.green))
        app_copanies_button.setTextColor(resources.getColor(R.color.black))
        mark6.setBackgroundResource(R.drawable.circle_dashboard)
        mark7.setBackgroundResource(R.drawable.greycircle_dashboard)


        val params = HashMap<String, String>()

        params["page_no"] = pageno.toString()
        params["page_size"] = 3.toString()

        retrofitInterface1 = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface1!!.getRecommendedCompanies(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<Company> {
            override fun onResponse(call: Call<Company>, response: Response<Company>) {

                Logger.d("URL", "Applied" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", "Applied" + response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "Applied" + Gson().toJson(response))

                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )

                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                    val responseCode: Int = jsonObject1.optInt("response_code")
                    val message: String = jsonObject1.optString("message")
                    val jsonarray_info: JSONArray = jsonObject1.optJSONArray("body")
                    var jsonarray_pagination: JSONObject = jsonObject1.optJSONObject("pagination")

                    hasnextRecommendedCompanies = jsonarray_pagination.optBoolean("has_next")

                    val pagenoo: String = jsonarray_pagination.optString("next_page")
                    mark6.setText(jsonarray_pagination.optString("total_items"))

                    if (hasnextRecommendedCompanies) {
                        seemore_companies.visibility = View.VISIBLE
                        pagenoRecommendedCompanies = pagenoo
                    } else
                        seemore_companies.visibility = View.GONE

                    if (response.isSuccessful) {

                        for (i in 0 until response.body()!!.body!!.size) {
                            Log.d("TAGG", "Inside response body")
                            // var json_objectdetail: JSONObject = jsonarray_info.optJSONObject(i)

                            val model: CompaniesView = CompaniesView()

                            model.id = jsonarray_info.optJSONObject(i).optInt("id")
                            model.company_type =
                                jsonarray_info.optJSONObject(i).optString("company_type")
                            model.featured = jsonarray_info.optJSONObject(i).optBoolean("featured")
                            model.website = jsonarray_info.optJSONObject(i).optString("website")
                            model.active_jobs_count =
                                jsonarray_info.optJSONObject(i).optInt("active_jobs_count")
                            model.cities = jsonarray_info.optJSONObject(i).optString("cities")
                            model.follow_count =
                                0//jsonarray_info.optJSONObject(i).optInt("follow_count")
                            model.name = jsonarray_info.optJSONObject(i).optString("name")
                            model.logo = jsonarray_info.optJSONObject(i).optString("logo")
                            val listOfIndustry: ArrayList<String> = ArrayList()
                            if (jsonarray_info.optJSONObject(i).isNull("industry")) {
                            } else {
                                var industryArray: JSONArray =
                                    jsonarray_info.optJSONObject(i).optJSONArray("industry")
                                for (j in 0 until industryArray.length()) {
                                    listOfIndustry.add(industryArray.optString(j))
                                }
                            }

                            model.industry = listOfIndustry
                            model.banner_image = ""//json_objectdetail.optString("banner_image")
                            model.status = jsonarray_info.optJSONObject(i).optString("status")

                            listOfCompaniesdata.add(
                                CompaniesView(
                                    model.id,
                                    model.company_type!!,
                                    model.featured!!,
                                    model.website!!,
                                    model.active_jobs_count!!,
                                    model.cities!!,
                                    model.follow_count!!,
                                    model.name!!,
                                    model.logo!!,
                                    model.industry!!,
                                    model.banner_image!!,
                                    model.status!!
                                )
                            )
                            listOfCompareJoineddata.add(0)
                        }

                        mRecyclerViewCompanies!!.visibility = View.VISIBLE
                        default_company_companies.visibility = View.GONE

                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                        mRecyclerViewCompanies!!.layoutManager =
                            mgroupsLayoutManager as RecyclerView.LayoutManager?
                        mAdapterCompanies = CompaniesAdapter(
                            listOfCompaniesdata,
                            isLoggedIn,
                            3,
                            0,
                            listOfCompareJoineddata,
                            1,
                            "Dashboard"
                        )
                        mRecyclerViewCompanies!!.adapter = mAdapterCompanies

                    } else {
                        ToastHelper.makeToast(applicationContext, message)
                    }
                }
            }

            override fun onFailure(call: Call<Company>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                mRecyclerViewCompanies!!.visibility = View.GONE
                default_company_companies.visibility = View.VISIBLE
                //default_company_companies.setText("There are no recommended companies for you at the moment. Follow your favoured companies on jobsforher.com/companies to stay up-to-date with their job openings and policies offered.")
                default_company_companies.setText(Html.fromHtml("There are no recommended companies for you at the moment. Follow your favoured companies on <font color='#3FA9E6'><u>Company</u></font> to stay up-to-date with their job openings and policies offered."));

                seemore_companies.visibility = View.GONE
            }
        })
    }


    fun loadFollowedCompanies(pageno: String) {

        rec_companies.setBackgroundTintList(resources.getColorStateList(R.color.greyseparator_company))
        app_copanies.setBackgroundTintList(null)
        rec_companies_button.setTextColor(resources.getColor(R.color.black))
        app_copanies_button.setTextColor(resources.getColor(R.color.green))
        mark6.setBackgroundResource(R.drawable.greycircle_dashboard)
        mark7.setBackgroundResource(R.drawable.circle_dashboard)


        val params = HashMap<String, String>()

        params["page_no"] = pageno.toString()
        params["page_size"] = 3.toString()

        retrofitInterface1 = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface1!!.getMyCompanies(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<Company> {
            override fun onResponse(call: Call<Company>, response: Response<Company>) {

                Logger.d("URL", "Applied" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", "Applied" + response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "Applied" + Gson().toJson(response))

                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )

                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                    val responseCode: Int = jsonObject1.optInt("response_code")
                    val message: String = jsonObject1.optString("message")
                    val jsonarray_info: JSONArray = jsonObject1.optJSONArray("body")
                    var jsonarray_pagination: JSONObject = jsonObject1.optJSONObject("pagination")

                    hasnextFollowedCompanies = jsonarray_pagination.optBoolean("has_next")

                    val pagenoo: String = jsonarray_pagination.optString("next_page")
                    mark7.setText(jsonarray_pagination.optString("total_items"))

                    if (hasnextFollowedCompanies) {
                        seemore_companies.visibility = View.VISIBLE
                        pagenoFollowedCompanies = pagenoo
                    } else
                        seemore_companies.visibility = View.GONE

                    if (response.isSuccessful) {

                        for (i in 0 until response.body()!!.body!!.size) {
                            Log.d("TAGG", "Inside response body")
                            // var json_objectdetail: JSONObject = jsonarray_info.optJSONObject(i)

                            val model: CompaniesView = CompaniesView()

                            model.id = jsonarray_info.optJSONObject(i).optInt("id")
                            model.company_type =
                                jsonarray_info.optJSONObject(i).optString("company_type")
                            model.featured = jsonarray_info.optJSONObject(i).optBoolean("featured")
                            model.website = jsonarray_info.optJSONObject(i).optString("website")
                            model.active_jobs_count =
                                jsonarray_info.optJSONObject(i).optInt("active_jobs_count")
                            model.cities = jsonarray_info.optJSONObject(i).optString("cities")
                            model.follow_count =
                                0//jsonarray_info.optJSONObject(i).optInt("follow_count")
                            model.name = jsonarray_info.optJSONObject(i).optString("name")
                            model.logo = jsonarray_info.optJSONObject(i).optString("logo")
                            val listOfIndustry: ArrayList<String> = ArrayList()
                            if (jsonarray_info.optJSONObject(i).isNull("industry")) {
                            } else {
                                var industryArray: JSONArray =
                                    jsonarray_info.optJSONObject(i).optJSONArray("industry")
                                for (j in 0 until industryArray.length()) {
                                    listOfIndustry.add(industryArray.optString(j))
                                }
                            }

                            model.industry = listOfIndustry
                            model.banner_image = ""//json_objectdetail.optString("banner_image")
                            model.status = jsonarray_info.optJSONObject(i).optString("status")

                            listOfMyCompaniesdata.add(
                                CompaniesView(
                                    model.id,
                                    model.company_type!!,
                                    model.featured!!,
                                    model.website!!,
                                    model.active_jobs_count!!,
                                    model.cities!!,
                                    model.follow_count!!,
                                    model.name!!,
                                    model.logo!!,
                                    model.industry!!,
                                    model.banner_image!!,
                                    model.status!!
                                )
                            )
                            listOfCompareJoineddata.add(model.id!!)
                        }

                        mRecyclerViewCompanies!!.visibility = View.VISIBLE
                        default_company_companies.visibility = View.GONE

                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                        mRecyclerViewCompanies!!.layoutManager =
                            mgroupsLayoutManager as RecyclerView.LayoutManager?
                        mAdapterCompanies = CompaniesAdapter(
                            listOfMyCompaniesdata,
                            isLoggedIn,
                            3,
                            0,
                            listOfCompareJoineddata,
                            1,
                            "Dashboard"
                        )
                        mRecyclerViewCompanies!!.adapter = mAdapterCompanies

                    } else {
                        ToastHelper.makeToast(applicationContext, message)
                    }
                }
            }

            override fun onFailure(call: Call<Company>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                mark7.setText("0")
                mRecyclerViewCompanies!!.visibility = View.GONE
                default_company_companies.visibility = View.VISIBLE
                // default_company_companies.setText("You are currently not following any companies. Follow your favoured companies on jobsforher.com/companies to stay up-to-date with their job openings and policies offered.")
                default_company_companies.setText(Html.fromHtml("You are currently not following any companies. Follow your favoured companies on <font color='#3FA9E6'><u>Company</u></font> to stay up-to-date with their job openings and policies offered."));

                seemore_companies.visibility = View.GONE
            }
        })
    }

    fun loadRecommendedEvents(pageno: String) {

        mRecyclerViewEvents!!.visibility = View.VISIBLE
        default_events_companies.visibility = View.GONE

        rec_events.setBackgroundTintList(null)
        app_events.setBackgroundTintList(resources.getColorStateList(R.color.greyseparator_company))
        rec_events_button.setTextColor(resources.getColor(R.color.green))
        app_events_button.setTextColor(resources.getColor(R.color.black))
        mark8.setBackgroundResource(R.drawable.circle_dashboard)
        mark9.setBackgroundResource(R.drawable.greycircle_dashboard)


        val params = java.util.HashMap<String, String>()

        params["page_no"] = pageno.toString()
        params["page_size"] = 4.toString()
        params["payment_type"] = "both"

        retrofitInterface1 = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface1!!.getRecommendedEvents(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<Event> {
            override fun onResponse(call: Call<Event>, response: Response<Event>) {

                Logger.d("URL", "Applied" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", "Applied" + response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "Recommended" + Gson().toJson(response))

                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )

                var jsonObject1: JSONObject? = null
                var jsonarray_info: JSONArray? = null

                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    jsonObject1 = jsonObject.optJSONObject("body")
                }

                if (jsonObject1 != null) {
                    val responseCode: Int = jsonObject1.optInt("response_code")

                    jsonarray_info = jsonObject1.optJSONArray("body")
                    var jsonarray_pagination: JSONObject = jsonObject1.optJSONObject("pagination")
                    val hasnextRecommendedEvents = jsonarray_pagination.optBoolean("has_next")
                    val pagenoo: String = jsonarray_pagination.optString("next_page")
                    mark8.setText(jsonarray_pagination.optString("total_items"))
                    if (hasnextRecommendedEvents) {
                        //val next_page: String = jsonarray_pagination.optInt("next_page").toString()
                        seemore_events.visibility = View.VISIBLE
                        pagenoRecommendedEvents = pagenoo
                    } else {
                        seemore_events.visibility = View.GONE
                    }
                }

                if (response.isSuccessful) {

                    for (k in 0 until response.body()!!.body!!.size) {
                        var json_objectdetail: JSONObject = jsonarray_info?.optJSONObject(k)!!
                        val model_events: EventsView = EventsView();

                        var locations: JSONArray
                        var locdata: ArrayList<EventLocation> = ArrayList()
                        if (json_objectdetail.isNull("events_locations")) {
                        } else {
                            locations = json_objectdetail.optJSONArray("events_locations")

                            for (k in 0 until locations.length()) {
                                val locModel: EventLocation = EventLocation()
                                locModel.pincode =
                                    if (locations.optJSONObject(k)
                                            .isNull("pincode")
                                    ) "" else locations.optJSONObject(k).optString("pincode")
                                locModel.discounted_price =
                                    if (locations.optJSONObject(k)
                                            .isNull("discounted_price")
                                    ) 0 else locations.optJSONObject(k).optInt("discounted_price")
                                locModel.event_register_start_date_time =
                                    if (locations.optJSONObject(k)
                                            .isNull("event_register_start_date_time")
                                    ) "" else locations.optJSONObject(k)
                                        .optString("event_register_start_date_time")
                                locModel.event_type =
                                    if (locations.optJSONObject(k)
                                            .isNull("event_type")
                                    ) "" else locations.optJSONObject(k).optString("event_type")
                                locModel.event_start_date_time =
                                    if (locations.optJSONObject(k)
                                            .isNull("event_start_date_time")
                                    ) "" else locations.optJSONObject(k)
                                        .optString("event_start_date_time")
                                locModel.amount =
                                    if (locations.optJSONObject(k)
                                            .isNull("amount")
                                    ) 0 else locations.optJSONObject(k).optInt("amount")
                                locModel.discount_start_date_time =
                                    if (locations.optJSONObject(k)
                                            .isNull("discount_start_date_time")
                                    ) "" else locations.optJSONObject(k)
                                        .optString("discount_start_date_time")
                                locModel.event_register_end_date_time =
                                    if (locations.optJSONObject(k)
                                            .isNull("event_register_end_date_time")
                                    ) "" else locations.optJSONObject(k)
                                        .optString("event_register_end_date_time")
                                locModel.discount_end_date_time =
                                    if (locations.optJSONObject(k)
                                            .isNull("discount_end_date_time")
                                    ) "" else locations.optJSONObject(k)
                                        .optString("discount_end_date_time")
                                locModel.discount_active =
                                    if (locations.optJSONObject(k)
                                            .isNull("discount_active")
                                    ) "" else locations.optJSONObject(k)
                                        .optString("discount_active")
                                locModel.address =
                                    if (locations.optJSONObject(k)
                                            .isNull("address")
                                    ) "" else locations.optJSONObject(k).optString("address")
                                locModel.event_end_date_time =
                                    if (locations.optJSONObject(k)
                                            .isNull("event_end_date_time")
                                    ) "" else locations.optJSONObject(k)
                                        .optString("event_end_date_time")

                                locModel.id =
                                    if (locations.optJSONObject(k)
                                            .isNull("id")
                                    ) 0 else locations.optJSONObject(k).optInt("id")
                                locModel.state =
                                    if (locations.optJSONObject(k)
                                            .isNull("state")
                                    ) "" else locations.optJSONObject(k).optString("state")
                                locModel.seats =
                                    if (locations.optJSONObject(k)
                                            .isNull("seats")
                                    ) "" else locations.optJSONObject(k).optString("seats")
                                locModel.event_id =
                                    if (locations.optJSONObject(k)
                                            .isNull("event_id")
                                    ) 0 else locations.optJSONObject(k).optInt("event_id")
                                locModel.country =
                                    if (locations.optJSONObject(k)
                                            .isNull("country")
                                    ) "" else locations.optJSONObject(k).optString("country")
                                locModel.registration_open =
                                    if (locations.optJSONObject(k)
                                            .isNull("registration_open")
                                    ) false else locations.optJSONObject(k)
                                        .optBoolean("registration_open")
                                locModel.google_map_url =
                                    if (locations.optJSONObject(k)
                                            .isNull("google_map_url")
                                    ) "" else locations.optJSONObject(k).optString("google_map_url")
                                locModel.city =
                                    if (locations.optJSONObject(k)
                                            .isNull("city")
                                    ) "" else locations.optJSONObject(k).optString("city")

                                locdata.add(locModel)
                            }

                        }
                        model_events.event_locations = locdata

                        model_events.discount_start_date_time =
                            if (json_objectdetail.isNull("discount_start_date_time")) "" else json_objectdetail.optString(
                                "discount_start_date_time"
                            )
                        model_events.discount_end_date_time =
                            if (json_objectdetail.isNull("discount_end_date_time")) "" else json_objectdetail.optString(
                                "discount_end_date_time"
                            )
                        model_events.price_after_discount =
                            if (json_objectdetail.isNull("price_after_discount")) 0 else json_objectdetail.optInt(
                                "price_after_discount"
                            )
                        model_events.price_before_discount =
                            if (json_objectdetail.isNull("price_before_discount")) 0 else json_objectdetail.optInt(
                                "price_before_discount"
                            )


                        model_events.interested =
                            if (json_objectdetail.isNull("interested")) false else json_objectdetail.optBoolean(
                                "interested"
                            )
                        model_events.registered =
                            if (json_objectdetail.isNull("registered")) false else json_objectdetail.optBoolean(
                                "registered"
                            )
                        model_events.event_category =
                            if (json_objectdetail.isNull("events_category")) "" else json_objectdetail.optString(
                                "events_category"
                            )

                        model_events.company_name =
                            if (json_objectdetail.isNull("company_name")) "" else json_objectdetail.optString(
                                "company_name"
                            )
                        model_events.featured_end_date_time =
                            if (json_objectdetail.isNull("featured_end_date_time")) "" else json_objectdetail.optString(
                                "featured_end_date_time"
                            )
                        model_events.preference_required =
                            if (json_objectdetail.isNull("preference_required")) false else json_objectdetail.optBoolean(
                                "preference_required"
                            )
                        model_events.terms_and_conditions =
                            if (json_objectdetail.isNull("terms_and_conditions")) "" else json_objectdetail.optString(
                                "terms_and_conditions"
                            )
                        model_events.featured_start_date_time =
                            if (json_objectdetail.isNull("featured_start_date_time")) "" else json_objectdetail.optString(
                                "featured_start_date_time"
                            )
                        model_events.featured_event =
                            if (json_objectdetail.isNull("featured_event")) false else json_objectdetail.optBoolean(
                                "featured_event"
                            )
                        model_events.faq =
                            if (json_objectdetail.isNull("faq")) "" else json_objectdetail.optString(
                                "faq"
                            )

                        model_events.resume_required =
                            if (json_objectdetail.isNull("resume_required")) false else json_objectdetail.optBoolean(
                                "resume_required"
                            )
                        model_events.ticket_type =
                            if (json_objectdetail.isNull("ticket_type")) "" else json_objectdetail.optString(
                                "ticket_type"
                            )
                        model_events.seats =
                            if (json_objectdetail.isNull("seats")) "" else json_objectdetail.optString(
                                "seats"
                            )
                        model_events.event_type =
                            if (json_objectdetail.isNull("event_type")) "" else json_objectdetail.optString(
                                "event_type"
                            )
                        model_events.display_price =
                            if (json_objectdetail.isNull("display_price")) 0 else json_objectdetail.optInt(
                                "display_price"
                            )
                        model_events.agenda =
                            if (json_objectdetail.isNull("agenda")) "" else json_objectdetail.optString(
                                "agenda"
                            )


                        model_events.modified_by =
                            if (json_objectdetail.isNull("modified_by")) "" else json_objectdetail.optString(
                                "modified_by"
                            )
                        model_events.interested_count =
                            if (json_objectdetail.isNull("interested_count")) 0 else json_objectdetail.optInt(
                                "interested_count"
                            )
                        model_events.view_count =
                            if (json_objectdetail.isNull("view_count")) 0 else json_objectdetail.optInt(
                                "view_count"
                            )
                        model_events.share_count =
                            if (json_objectdetail.isNull("share_count")) 0 else json_objectdetail.optInt(
                                "share_count"
                            )
                        model_events.payment =
                            if (json_objectdetail.isNull("payment")) false else json_objectdetail.optBoolean(
                                "payment"
                            )
                        model_events.status =
                            if (json_objectdetail.isNull("status")) "" else json_objectdetail.optString(
                                "status"
                            )

                        model_events.priority_order =
                            if (json_objectdetail.isNull("priority_order")) false else json_objectdetail.optBoolean(
                                "priority_order"
                            )
                        model_events.excerpt =
                            if (json_objectdetail.isNull("excerpt")) "" else json_objectdetail.optString(
                                "excerpt"
                            )
                        model_events.event_register_start_date_time =
                            if (json_objectdetail.isNull("event_register_start_date_time")) "" else json_objectdetail.optString(
                                "event_register_start_date_time"
                            )
                        model_events.created_on =
                            if (json_objectdetail.isNull("created_on")) "" else json_objectdetail.optString(
                                "created_on"
                            )
                        model_events.author_id =
                            if (json_objectdetail.isNull("author_id")) "" else json_objectdetail.optString(
                                "author_id"
                            )
                        model_events.is_online =
                            if (json_objectdetail.isNull("is_online")) false else json_objectdetail.optBoolean(
                                "is_online"
                            )

                        model_events.gtm_id =
                            if (json_objectdetail.isNull("gtm_id")) 0 else json_objectdetail.optInt(
                                "gtm_id"
                            )
                        model_events.author_name =
                            if (json_objectdetail.isNull("author_name")) "" else json_objectdetail.optString(
                                "author_name"
                            )
                        model_events.is_private =
                            if (json_objectdetail.isNull("is_private")) false else json_objectdetail.optBoolean(
                                "is_private"
                            )
                        model_events.register_count =
                            if (json_objectdetail.isNull("register_count")) "" else json_objectdetail.optString(
                                "register_count"
                            )
                        model_events.title =
                            if (json_objectdetail.isNull("title")) "" else json_objectdetail.optString(
                                "title"
                            )
                        model_events.event_end_date_time =
                            if (json_objectdetail.isNull("event_end_date_time")) "" else json_objectdetail.optString(
                                "event_end_date_time"
                            )

                        model_events.slug =
                            if (json_objectdetail.isNull("slug")) "" else json_objectdetail.optString(
                                "slug"
                            )
                        model_events.event_register_end_date_time =
                            if (json_objectdetail.isNull("event_register_end_date_time")) "" else json_objectdetail.optString(
                                "event_register_end_date_time"
                            )
                        model_events.event_by =
                            if (json_objectdetail.isNull("event_by")) "" else json_objectdetail.optString(
                                "event_by"
                            )
                        model_events.id =
                            if (json_objectdetail.isNull("id")) 0 else json_objectdetail.optInt("id")
                        model_events.post_for =
                            if (json_objectdetail.isNull("post_for")) "" else json_objectdetail.optString(
                                "post_for"
                            )
                        model_events.publish_date =
                            if (json_objectdetail.isNull("publish_date")) "" else json_objectdetail.optString(
                                "publish_date"
                            )

                        model_events.event_start_date_time =
                            if (json_objectdetail.isNull("event_start_date_time")) "" else json_objectdetail.optString(
                                "event_start_date_time"
                            )
                        model_events.created_by =
                            if (json_objectdetail.isNull("created_by")) 0 else json_objectdetail.optInt(
                                "created_by"
                            )
                        model_events.company_id =
                            if (json_objectdetail.isNull("company_id")) 0 else json_objectdetail.optInt(
                                "company_id"
                            )
                        model_events.address =
                            if (json_objectdetail.isNull("address")) "" else json_objectdetail.optString(
                                "address"
                            )
                        model_events.modified_on =
                            if (json_objectdetail.isNull("modified_on")) "" else json_objectdetail.optString(
                                "modified_on"
                            )
                        model_events.payment_note =
                            if (json_objectdetail.isNull("payment_note")) "" else json_objectdetail.optString(
                                "payment_note"
                            )

                        model_events.description =
                            if (json_objectdetail.isNull("description")) "" else json_objectdetail.optString(
                                "description"
                            )
                        model_events.city =
                            if (json_objectdetail.isNull("city")) "" else json_objectdetail.optString(
                                "city"
                            )
                        model_events.event_url =
                            if (json_objectdetail.isNull("event_url")) "" else json_objectdetail.optString(
                                "event_url"
                            )
                        model_events.show_on_search =
                            if (json_objectdetail.isNull("show_on_search")) false else json_objectdetail.optBoolean(
                                "show_on_search"
                            )
                        model_events.image_url =
                            if (json_objectdetail.isNull("image_url")) "" else json_objectdetail.optString(
                                "image_url"
                            )
                        model_events.thumbnail_url =
                            if (json_objectdetail.isNull("thumbnail_url")) "" else json_objectdetail.optString(
                                "thumbnail_url"
                            )
                        var compdata: List<String>
                        var data: ArrayList<EventCompanies> = ArrayList()
                        if (json_objectdetail.isNull("link_companies_name")) {

                        } else {
                            var s: String = ""
                            compdata = json_objectdetail.optString("link_companies_name")
                                .split(Regex(","), 0)
                            for (k in 0 until compdata.size) {
                                val locModel: EventCompanies = EventCompanies()
                                locModel.company_id = 0
                                locModel.company_name = compdata[k].toString()
                                data.add(locModel)
                            }

                        }
                        model_events.link_companies_name = data


                        listOfEventsdata.add(
                            EventsView(
                                model_events.discount_start_date_time!!,
                                model_events.discount_end_date_time!!,
                                model_events.price_after_discount!!,
                                model_events.price_before_discount!!,
                                model_events.event_category!!,
                                model_events.interested!!,
                                model_events.registered!!,
                                model_events.event_locations!!,
                                model_events.company_name!!,
                                model_events.resume_required!!,
                                model_events.ticket_type!!,
                                model_events.seats!!,
                                model_events.event_type!!,
                                model_events.display_price!!,
                                model_events.agenda!!,
                                model_events.featured_end_date_time!!,
                                model_events.preference_required!!,
                                model_events.terms_and_conditions!!,
                                model_events.featured_start_date_time!!,
                                model_events.featured_event!!,
                                model_events.faq!!,
                                model_events.modified_by!!,
                                model_events.interested_count!!,
                                model_events.view_count!!,
                                model_events.share_count!!,
                                model_events.payment!!,
                                model_events.status!!,
                                model_events.priority_order!!,
                                model_events.excerpt!!,
                                model_events.event_register_start_date_time!!,
                                model_events.created_on!!,
                                model_events.author_id!!,
                                model_events.is_online!!,
                                model_events.gtm_id!!,
                                model_events.author_name!!,
                                model_events.is_private!!,
                                model_events.register_count!!,
                                model_events.title!!,
                                model_events.event_end_date_time!!,
                                model_events.slug!!,
                                model_events.event_register_end_date_time!!,
                                model_events.event_by!!,
                                model_events.id!!,
                                model_events.post_for!!,
                                model_events.publish_date!!,
                                model_events.event_start_date_time!!,
                                model_events.created_by!!,
                                model_events.company_id!!,
                                model_events.address!!,
                                model_events.modified_on!!,
                                model_events.payment_note!!,
                                model_events.description!!,
                                model_events.city!!,
                                model_events.event_url!!,
                                model_events.show_on_search!!,
                                model_events.image_url!!,
                                model_events.thumbnail_url!!,
                                model_events.link_companies_name!!
                            )
                        )
                    }

//                    if (listOfAppliedJobsdata.size>3) {
//                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
//                        mRecyclerViewPosts!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
//                        mAdapterJobs = JobsAdapter(listOfAppliedJobsdataTrimmed, isLoggedIn, 2, 0, listOfCompareJoineddata,1)
//                        mRecyclerViewPosts!!.adapter = mAdapterJobs
//                    }
//                    else{

                    var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                    mRecyclerViewEvents!!.layoutManager =
                        mgroupsLayoutManager as RecyclerView.LayoutManager?
                    mAdapterEvents = EventsAdapter(
                        listOfEventsdata,
                        isLoggedIn,
                        1,
                        0,
                        listOfCompareJoineddata,
                        1,
                        "Dashboard"
                    )
                    mRecyclerViewEvents!!.adapter = mAdapterEvents
//                    }

                } else {
                    if (jsonObject1 != null) {
                        ToastHelper.makeToast(applicationContext, jsonObject1.optString("message"))
                    }
                }
            }

            override fun onFailure(call: Call<Event>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                mark8.setText("0")
                mRecyclerViewEvents!!.visibility = View.GONE
                default_events_companies.visibility = View.VISIBLE
                //default_events_companies.setText("You have not registered for any events. Stay up-to-date with all that JobsForHer has to offer on jobsforher.com/events.")
                default_events_companies.setText(Html.fromHtml("You have not registered for any events. Stay up-to-date with all that JobsForHer has to offer on <font color='#3FA9E6'><u>Events</u></font> "));

                seemore_events.visibility = View.GONE
            }
        })
    }

    fun loadAppliedEvents(pageno: String) {    //sneha
//
        typeEventClick = 2
        rec_events.setBackgroundTintList(resources.getColorStateList(R.color.greyseparator_company))
        app_events.setBackgroundTintList(null)
        rec_events_button.setTextColor(resources.getColor(R.color.black))
        app_events_button.setTextColor(resources.getColor(R.color.green))
        mark8.setBackgroundResource(R.drawable.greycircle_dashboard)
        mark9.setBackgroundResource(R.drawable.circle_dashboard)


        val params = java.util.HashMap<String, String>()
        params["page_size"] = 4.toString()
        params["page_no"] = pageno.toString()
        Log.d("TAGG", params.toString())

        listOfCompareJoineddata.clear()
        listOfMyEventsdata.clear()
        listOfMyEventsdataTrimmed.clear()
        retrofitInterface1 = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface1!!.getMyEvents(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<MyEvent> {
            override fun onResponse(call: Call<MyEvent>, response: Response<MyEvent>) {

                Logger.d("URL", "Applied" + response + EndPoints.ACCESS_TOKEN)
                Logger.d("CODE", "Applied" + response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "Applied" + Gson().toJson(response))

                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )

                 var jsonObject1: JSONObject? = null
                var jsonarray_info: JSONArray? = null

                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    jsonObject1 = jsonObject.optJSONObject("body")
                }

                if (jsonObject1 != null) {
                    val responseCode: Int = jsonObject1.optInt("response_code")
                    jsonarray_info = jsonObject1.optJSONArray("body")

                    var jsonarray_pagination: JSONObject = jsonObject1.optJSONObject("pagination")
                    val hasnextAppEvents = jsonarray_pagination.optBoolean("has_next")
                    val pagenoo: String = jsonarray_pagination.optString("next_page")
                    mark9.setText(jsonarray_pagination.optString("total_items"))

                    if (hasnextAppEvents) {
                        //val next_page: String = jsonarray_pagination.optInt("next_page").toString()
                        seemore_events.visibility = View.VISIBLE
                        pagenoAppEvents = pagenoo
                    } else {
                        seemore_events.visibility = View.GONE
                    }
                }

                if (response.isSuccessful) {

                    for (k in 0 until response.body()!!.body!!.size) {
                        //val jsonObject2:JSONObject = jsonarray_info.optJSONObject(0).optJSONObject("events")
                        if (jsonarray_info?.optJSONObject(k)!!.isNull("events")) {
                        } else {
                            var json_objectdetail: JSONObject =
                                jsonarray_info?.optJSONObject(k)!!.optJSONObject("events")

                            val model_events: EventsView = EventsView();

                            var locations: JSONArray
                            var locdata: ArrayList<EventLocation> = ArrayList()
                            if (json_objectdetail.isNull("events_locations")) {
                            } else {
                                locations = json_objectdetail.optJSONArray("events_locations")

                                for (k in 0 until locations.length()) {
                                    val locModel: EventLocation = EventLocation()
                                    locModel.pincode =
                                        if (locations.optJSONObject(k)
                                                .isNull("pincode")
                                        ) "" else locations.optJSONObject(
                                            k
                                        ).optString("pincode")
                                    locModel.discounted_price =
                                        if (locations.optJSONObject(k)
                                                .isNull("discounted_price")
                                        ) 0 else locations.optJSONObject(
                                            k
                                        ).optInt("discounted_price")
                                    locModel.event_register_start_date_time =
                                        if (locations.optJSONObject(k)
                                                .isNull("event_register_start_date_time")
                                        ) "" else locations.optJSONObject(
                                            k
                                        ).optString("event_register_start_date_time")
                                    locModel.event_type =
                                        if (locations.optJSONObject(k)
                                                .isNull("event_type")
                                        ) "" else locations.optJSONObject(
                                            k
                                        ).optString("event_type")
                                    locModel.event_start_date_time =
                                        if (locations.optJSONObject(k)
                                                .isNull("event_start_date_time")
                                        ) "" else locations.optJSONObject(
                                            k
                                        ).optString("event_start_date_time")
                                    locModel.amount =
                                        if (locations.optJSONObject(k)
                                                .isNull("amount")
                                        ) 0 else locations.optJSONObject(
                                            k
                                        ).optInt("amount")
                                    locModel.discount_start_date_time =
                                        if (locations.optJSONObject(k)
                                                .isNull("discount_start_date_time")
                                        ) "" else locations.optJSONObject(
                                            k
                                        ).optString("discount_start_date_time")
                                    locModel.event_register_end_date_time =
                                        if (locations.optJSONObject(k)
                                                .isNull("event_register_end_date_time")
                                        ) "" else locations.optJSONObject(
                                            k
                                        ).optString("event_register_end_date_time")
                                    locModel.discount_end_date_time =
                                        if (locations.optJSONObject(k)
                                                .isNull("discount_end_date_time")
                                        ) "" else locations.optJSONObject(
                                            k
                                        ).optString("discount_end_date_time")
                                    locModel.discount_active =
                                        if (locations.optJSONObject(k)
                                                .isNull("discount_active")
                                        ) "" else locations.optJSONObject(
                                            k
                                        ).optString("discount_active")
                                    locModel.address =
                                        if (locations.optJSONObject(k)
                                                .isNull("address")
                                        ) "" else locations.optJSONObject(
                                            k
                                        ).optString("address")
                                    locModel.event_end_date_time =
                                        if (locations.optJSONObject(k)
                                                .isNull("event_end_date_time")
                                        ) "" else locations.optJSONObject(
                                            k
                                        ).optString("event_end_date_time")

                                    locModel.id =
                                        if (locations.optJSONObject(k)
                                                .isNull("id")
                                        ) 0 else locations.optJSONObject(k).optInt(
                                            "id"
                                        )
                                    locModel.state =
                                        if (locations.optJSONObject(k)
                                                .isNull("state")
                                        ) "" else locations.optJSONObject(
                                            k
                                        ).optString("state")
                                    locModel.seats =
                                        if (locations.optJSONObject(k)
                                                .isNull("seats")
                                        ) "" else locations.optJSONObject(
                                            k
                                        ).optString("seats")
                                    locModel.event_id =
                                        if (locations.optJSONObject(k)
                                                .isNull("event_id")
                                        ) 0 else locations.optJSONObject(
                                            k
                                        ).optInt("event_id")
                                    locModel.country =
                                        if (locations.optJSONObject(k)
                                                .isNull("country")
                                        ) "" else locations.optJSONObject(
                                            k
                                        ).optString("country")
                                    locModel.registration_open =
                                        if (locations.optJSONObject(k)
                                                .isNull("registration_open")
                                        ) false else locations.optJSONObject(
                                            k
                                        ).optBoolean("registration_open")
                                    locModel.google_map_url =
                                        if (locations.optJSONObject(k)
                                                .isNull("google_map_url")
                                        ) "" else locations.optJSONObject(
                                            k
                                        ).optString("google_map_url")
                                    locModel.city =
                                        if (locations.optJSONObject(k)
                                                .isNull("city")
                                        ) "" else locations.optJSONObject(k).optString(
                                            "city"
                                        )

                                    locdata.add(locModel)
                                }

                            }
                            model_events.event_locations = locdata

                            model_events.discount_start_date_time =
                                if (json_objectdetail.isNull("discount_start_date_time")) "" else json_objectdetail.optString(
                                    "discount_start_date_time"
                                )
                            model_events.discount_end_date_time =
                                if (json_objectdetail.isNull("discount_end_date_time")) "" else json_objectdetail.optString(
                                    "discount_end_date_time"
                                )
                            model_events.price_after_discount =
                                if (json_objectdetail.isNull("price_after_discount")) 0 else json_objectdetail.optInt(
                                    "price_after_discount"
                                )
                            model_events.price_before_discount =
                                if (json_objectdetail.isNull("price_before_discount")) 0 else json_objectdetail.optInt(
                                    "price_before_discount"
                                )


                            model_events.interested =
                                if (json_objectdetail.isNull("interested")) false else json_objectdetail.optBoolean(
                                    "interested"
                                )
                            model_events.registered =
                                if (json_objectdetail.isNull("registered")) false else json_objectdetail.optBoolean(
                                    "registered"
                                )
                            model_events.event_category =
                                if (json_objectdetail.isNull("events_category")) "" else json_objectdetail.optString(
                                    "events_category"
                                )

                            model_events.company_name =
                                if (json_objectdetail.isNull("company_name")) "" else json_objectdetail.optString(
                                    "company_name"
                                )
                            model_events.featured_end_date_time =
                                if (json_objectdetail.isNull("featured_end_date_time")) "" else json_objectdetail.optString(
                                    "featured_end_date_time"
                                )
                            model_events.preference_required =
                                if (json_objectdetail.isNull("preference_required")) false else json_objectdetail.optBoolean(
                                    "preference_required"
                                )
                            model_events.terms_and_conditions =
                                if (json_objectdetail.isNull("terms_and_conditions")) "" else json_objectdetail.optString(
                                    "terms_and_conditions"
                                )
                            model_events.featured_start_date_time =
                                if (json_objectdetail.isNull("featured_start_date_time")) "" else json_objectdetail.optString(
                                    "featured_start_date_time"
                                )
                            model_events.featured_event =
                                if (json_objectdetail.isNull("featured_event")) false else json_objectdetail.optBoolean(
                                    "featured_event"
                                )
                            model_events.faq =
                                if (json_objectdetail.isNull("faq")) "" else json_objectdetail.optString(
                                    "faq"
                                )

                            model_events.resume_required =
                                if (json_objectdetail.isNull("resume_required")) false else json_objectdetail.optBoolean(
                                    "resume_required"
                                )
                            model_events.ticket_type =
                                if (json_objectdetail.isNull("ticket_type")) "" else json_objectdetail.optString(
                                    "ticket_type"
                                )
                            model_events.seats =
                                if (json_objectdetail.isNull("seats")) "" else json_objectdetail.optString(
                                    "seats"
                                )
                            model_events.event_type =
                                if (json_objectdetail.isNull("event_type")) "" else json_objectdetail.optString(
                                    "event_type"
                                )
                            model_events.display_price =
                                if (json_objectdetail.isNull("display_price")) 0 else json_objectdetail.optInt(
                                    "display_price"
                                )
                            model_events.agenda =
                                if (json_objectdetail.isNull("agenda")) "" else json_objectdetail.optString(
                                    "agenda"
                                )


                            model_events.modified_by =
                                if (json_objectdetail.isNull("modified_by")) "" else json_objectdetail.optString(
                                    "modified_by"
                                )
                            model_events.interested_count =
                                if (json_objectdetail.isNull("interested_count")) 0 else json_objectdetail.optInt(
                                    "interested_count"
                                )
                            model_events.view_count =
                                if (json_objectdetail.isNull("view_count")) 0 else json_objectdetail.optInt(
                                    "view_count"
                                )
                            model_events.share_count =
                                if (json_objectdetail.isNull("share_count")) 0 else json_objectdetail.optInt(
                                    "share_count"
                                )
                            model_events.payment =
                                if (json_objectdetail.isNull("payment")) false else json_objectdetail.optBoolean(
                                    "payment"
                                )
                            model_events.status =
                                if (json_objectdetail.isNull("status")) "" else json_objectdetail.optString(
                                    "status"
                                )

                            model_events.priority_order =
                                if (json_objectdetail.isNull("priority_order")) false else json_objectdetail.optBoolean(
                                    "priority_order"
                                )
                            model_events.excerpt =
                                if (json_objectdetail.isNull("excerpt")) "" else json_objectdetail.optString(
                                    "excerpt"
                                )
                            model_events.event_register_start_date_time =
                                if (json_objectdetail.isNull("event_register_start_date_time")) "" else json_objectdetail.optString(
                                    "event_register_start_date_time"
                                )
                            model_events.created_on =
                                if (json_objectdetail.isNull("created_on")) "" else json_objectdetail.optString(
                                    "created_on"
                                )
                            model_events.author_id =
                                if (json_objectdetail.isNull("author_id")) "" else json_objectdetail.optString(
                                    "author_id"
                                )
                            model_events.is_online =
                                if (json_objectdetail.isNull("is_online")) false else json_objectdetail.optBoolean(
                                    "is_online"
                                )

                            model_events.gtm_id =
                                if (json_objectdetail.isNull("gtm_id")) 0 else json_objectdetail.optInt(
                                    "gtm_id"
                                )
                            model_events.author_name =
                                if (json_objectdetail.isNull("author_name")) "" else json_objectdetail.optString(
                                    "author_name"
                                )
                            model_events.is_private =
                                if (json_objectdetail.isNull("is_private")) false else json_objectdetail.optBoolean(
                                    "is_private"
                                )
                            model_events.register_count =
                                if (json_objectdetail.isNull("register_count")) "" else json_objectdetail.optString(
                                    "register_count"
                                )
                            model_events.title =
                                if (json_objectdetail.isNull("title")) "" else json_objectdetail.optString(
                                    "title"
                                )
                            model_events.event_end_date_time =
                                if (json_objectdetail.isNull("event_end_date_time")) "" else json_objectdetail.optString(
                                    "event_end_date_time"
                                )

                            model_events.slug =
                                if (json_objectdetail.isNull("slug")) "" else json_objectdetail.optString(
                                    "slug"
                                )
                            model_events.event_register_end_date_time =
                                if (json_objectdetail.isNull("event_register_end_date_time")) "" else json_objectdetail.optString(
                                    "event_register_end_date_time"
                                )
                            model_events.event_by =
                                if (json_objectdetail.isNull("event_by")) "" else json_objectdetail.optString(
                                    "event_by"
                                )
                            model_events.id =
                                if (json_objectdetail.isNull("id")) 0 else json_objectdetail.optInt(
                                    "id"
                                )
                            model_events.post_for =
                                if (json_objectdetail.isNull("post_for")) "" else json_objectdetail.optString(
                                    "post_for"
                                )
                            model_events.publish_date =
                                if (json_objectdetail.isNull("publish_date")) "" else json_objectdetail.optString(
                                    "publish_date"
                                )

                            model_events.event_start_date_time =
                                if (json_objectdetail.isNull("event_start_date_time")) "" else json_objectdetail.optString(
                                    "event_start_date_time"
                                )
                            model_events.created_by =
                                if (json_objectdetail.isNull("created_by")) 0 else json_objectdetail.optInt(
                                    "created_by"
                                )
                            model_events.company_id =
                                if (json_objectdetail.isNull("company_id")) 0 else json_objectdetail.optInt(
                                    "company_id"
                                )
                            model_events.address =
                                if (json_objectdetail.isNull("address")) "" else json_objectdetail.optString(
                                    "address"
                                )
                            model_events.modified_on =
                                if (json_objectdetail.isNull("modified_on")) "" else json_objectdetail.optString(
                                    "modified_on"
                                )
                            model_events.payment_note =
                                if (json_objectdetail.isNull("payment_note")) "" else json_objectdetail.optString(
                                    "payment_note"
                                )

                            model_events.description =
                                if (json_objectdetail.isNull("description")) "" else json_objectdetail.optString(
                                    "description"
                                )
                            model_events.city =
                                if (json_objectdetail.isNull("city")) "" else json_objectdetail.optString(
                                    "city"
                                )
                            model_events.event_url =
                                if (json_objectdetail.isNull("event_url")) "" else json_objectdetail.optString(
                                    "event_url"
                                )
                            model_events.show_on_search =
                                if (json_objectdetail.isNull("show_on_search")) false else json_objectdetail.optBoolean(
                                    "show_on_search"
                                )
                            model_events.image_url =
                                if (json_objectdetail.isNull("image_url")) "" else json_objectdetail.optString(
                                    "image_url"
                                )
                            model_events.thumbnail_url =
                                if (json_objectdetail.isNull("thumbnail_url")) "" else json_objectdetail.optString(
                                    "thumbnail_url"
                                )
                            var compdata: JSONArray
                            var data: ArrayList<EventCompanies> = ArrayList()
                            if (json_objectdetail.isNull("link_companies_name")) {

                            } else {
                                var s: String = ""
                                compdata = json_objectdetail.optJSONArray("link_companies_name")
                                for (k in 0 until compdata.length()) {

                                    val locModel: EventCompanies = EventCompanies()
                                    locModel.company_id =
                                        compdata!!.optJSONObject(k).optString("company_id").toInt()
                                    locModel.company_name =
                                        compdata!!.optJSONObject(k).optString("company_name")
                                    data.add(locModel)
                                }

                            }
                            model_events.link_companies_name = data


                            listOfMyEventsdata.add(
                                EventsView(
                                    model_events.discount_start_date_time!!,
                                    model_events.discount_end_date_time!!,
                                    model_events.price_after_discount!!,
                                    model_events.price_before_discount!!,
                                    model_events.event_category!!,
                                    model_events.interested!!,
                                    model_events.registered!!,
                                    model_events.event_locations!!,
                                    model_events.company_name!!,
                                    model_events.resume_required!!,
                                    model_events.ticket_type!!,
                                    model_events.seats!!,
                                    model_events.event_type!!,
                                    model_events.display_price!!,
                                    model_events.agenda!!,
                                    model_events.featured_end_date_time!!,
                                    model_events.preference_required!!,
                                    model_events.terms_and_conditions!!,
                                    model_events.featured_start_date_time!!,
                                    model_events.featured_event!!,
                                    model_events.faq!!,
                                    model_events.modified_by!!,
                                    model_events.interested_count!!,
                                    model_events.view_count!!,
                                    model_events.share_count!!,
                                    model_events.payment!!,
                                    model_events.status!!,
                                    model_events.priority_order!!,
                                    model_events.excerpt!!,
                                    model_events.event_register_start_date_time!!,
                                    model_events.created_on!!,
                                    model_events.author_id!!,
                                    model_events.is_online!!,
                                    model_events.gtm_id!!,
                                    model_events.author_name!!,
                                    model_events.is_private!!,
                                    model_events.register_count!!,
                                    model_events.title!!,
                                    model_events.event_end_date_time!!,
                                    model_events.slug!!,
                                    model_events.event_register_end_date_time!!,
                                    model_events.event_by!!,
                                    model_events.id!!,
                                    model_events.post_for!!,
                                    model_events.publish_date!!,
                                    model_events.event_start_date_time!!,
                                    model_events.created_by!!,
                                    model_events.company_id!!,
                                    model_events.address!!,
                                    model_events.modified_on!!,
                                    model_events.payment_note!!,
                                    model_events.description!!,
                                    model_events.city!!,
                                    model_events.event_url!!,
                                    model_events.show_on_search!!,
                                    model_events.image_url!!,
                                    model_events.thumbnail_url!!,
                                    model_events.link_companies_name!!
                                )
                            )
                            listOfCompareJoineddata.add(model_events.id!!)
                        }
                    }
                    mRecyclerViewEvents!!.visibility = View.VISIBLE
                    default_events_companies.visibility = View.GONE
//                    if (listOfAppliedJobsdata.size>3) {
//                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
//                        mRecyclerViewPosts!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
//                        mAdapterJobs = JobsAdapter(listOfAppliedJobsdataTrimmed, isLoggedIn, 2, 0, listOfCompareJoineddata,1)
//                        mRecyclerViewPosts!!.adapter = mAdapterJobs
//                    }
//                    else{

                    var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                    mRecyclerViewEvents!!.layoutManager =
                        mgroupsLayoutManager as RecyclerView.LayoutManager?
                    mAdapterEvents = EventsAdapter(
                        listOfMyEventsdata,
                        isLoggedIn,
                        1,
                        0,
                        listOfCompareJoineddata,
                        1,
                        "Dashboard"
                    )
                    mRecyclerViewEvents!!.adapter = mAdapterEvents
//                    }

                } else {
                    if (jsonObject1 != null) {
                        ToastHelper.makeToast(applicationContext, jsonObject1.optString("message"))
                    }
                }
            }

            override fun onFailure(call: Call<MyEvent>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                mark9.setText("0")
                mRecyclerViewEvents!!.visibility = View.GONE
                default_events_companies.visibility = View.VISIBLE
                default_events_companies.setText(Html.fromHtml("You have not registered for any events. Stay up-to-date with all that JobsForHer has to offer on <font color='#3FA9E6'><u>Events</u></font> "));

                seemore_events.visibility = View.GONE
            }
        })
    }

    //Groups functions

    fun leaveGroup(id: Int, btnJoinGroup: Button, btnJoined: Button) {

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.leaveGroup(
            id, EndPoints.CLIENT_ID, "Bearer " +
                    EndPoints.ACCESS_TOKEN
        )

        call.enqueue(object : Callback<JoinGroupResponse> {
            override fun onResponse(
                call: Call<JoinGroupResponse>,
                response: Response<JoinGroupResponse>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.e("RESPONSE leave group", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                    //listOfMyGroupdata.clear()
//                    listOfCompareGroupdata.clear()
//                    loadMyGroupData("1")
                    btnJoinGroup.visibility = View.VISIBLE
                    btnJoined.visibility = View.GONE
                    loadRecommendedMyGroupData("1")
                    loadMyGroupData("1")


                    Logger.e("TAGG", "" + response.body()!!.message.toString())


                    listOfCompareGroupdata.clear()
                    listOfMyGroupdata.clear()


//                        val intent = Intent(applicationContext, ZActivityGroups::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or  Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        intent.putExtra("isLoggedIn", true)
//                        startActivity(intent)
//                        finish()

//                    else{
//                        mgroupsAdapter!!.notifyDataSetChanged()
//                    }

                } else {

                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<JoinGroupResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })
    }

    fun joinGroup(id: Int, btnJoinGroup: Button, type: String, btnJoined: Button) {

        Logger.d("CODE", id.toString() + "")

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.JoinGroup(
            id,
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN
        )

        call.enqueue(object : Callback<JoinGroupResponse> {
            override fun onResponse(
                call: Call<JoinGroupResponse>,
                response: Response<JoinGroupResponse>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful && response.body()!!.responseCode == 10104) {


                    if (type.equals("private")) {
                        btnJoinGroup.text = "Requested"
                        loadRecommendedMyGroupData("1")
                        loadMyGroupData("1")
                    } else {
                        btnJoinGroup.visibility = View.GONE
                        btnJoined.visibility = View.VISIBLE
                        loadRecommendedMyGroupData("1")
                        loadMyGroupData("1")

                    }

//                    if (response.body()!!.message.equals("User added to group")){
//                        btnJoinGroup.text = "Requested"
//
//                    }else {
//
//                        ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
//                    }

                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid Group Request")
                }
            }

            override fun onFailure(call: Call<JoinGroupResponse>, t: Throwable) {

                Logger.d("TAGG", "Join Group FAILED : $t")

            }
        })

    }

    fun reportGroup(
        id: Int,
        type: String,
        problem_type: String,
        reportdetail: String,
        dialog: Dialog
    ) {

        //ToastHelper.makeToast(applicationContext, "Report Group")
        val params = HashMap<String, String>()

        params["entity_id"] = id.toString()
        params["entity_type"] = type
        params["problem_type"] = problem_type
        params["reason"] = reportdetail

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.ReportGroup(
            EndPoints.CLIENT_ID, "Bearer " +
                    EndPoints.ACCESS_TOKEN, params
        )

        call.enqueue(object : Callback<ReportResponse> {
            override fun onResponse(
                call: Call<ReportResponse>,
                response: Response<ReportResponse>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.e("RESPONSE report group", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())

                    Logger.e("TAGG", "" + response.body()!!.message.toString())

                    val builder = AlertDialog.Builder(dialog.context)
                    builder.setTitle("Thanks for letting us know")
                    builder.setMessage("Report sent successfully! Your request is under review, we will notify you on this")
                    builder.setIcon(android.R.drawable.ic_dialog_alert)
                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                        // Toast.makeText(applicationContext,"clicked yes",Toast.LENGTH_LONG).show()
                        dialog.cancel()
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                } else {

                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<ReportResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })

    }

    public fun openBottomSheetReports(id: Int, type: String) {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.bottom_sheet_reports)
        val textheadline = dialog.findViewById(R.id.textreports) as TextView
        if (type.equals("group"))
            textheadline.setText("Report or give feedback on this group")
        else if (type.equals("post"))
            textheadline.setText("Report or give feedback on this post")
        else if (type.equals("comment"))
            textheadline.setText("Report or give feedback on this comment")
        val spinner = dialog.findViewById(R.id.report_spinner) as Spinner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.report_list,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val reportdetails = dialog.findViewById(R.id.reportdetails) as EditText

        val sendreport = dialog.findViewById(R.id.sendreport) as LinearLayout
        sendreport.setOnClickListener {
            reportGroup(
                id,
                type,
                spinner.selectedItem.toString(),
                reportdetails.text.toString(),
                dialog
            )
            dialog.cancel()
        }

        val cancel = dialog.findViewById(R.id.cancelblock) as LinearLayout
        cancel.setOnClickListener {
            dialog.cancel()
        }


        var window: Window = dialog.getWindow()!!;
        var wlp: WindowManager.LayoutParams = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog.show()
    }


    //Jobs functions
    var listOfResumes: ArrayList<ResumeView> = ArrayList()

    fun applyJob(
        id: Int,
        btnJoinGroup: Button,
        title: String,
        btnJoined: Button,
        isRequired: Boolean
    ) {

        Logger.d("CODE", id.toString() + "")

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.CheckPreferences(
            "application/json",
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN
        )

        call.enqueue(object : Callback<CheckPreferenceResponse> {
            override fun onResponse(
                call: Call<CheckPreferenceResponse>,
                response: Response<CheckPreferenceResponse>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful && response.body()!!.responseCode == 11405) {
                    Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG)
                        .show()
                    openBottomSheetPreferences()
                } else {
                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                    val gson = GsonBuilder().serializeNulls().create()
                    val str_response = Gson().toJson(response)
                    val jsonObject: JSONObject = JSONObject(
                        str_response.substring(
                            str_response.indexOf("{"),
                            str_response.lastIndexOf("}") + 1
                        )
                    )
                    if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                        val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                        val jsonarray_info: JSONArray = jsonObject1.optJSONArray("body")

                        var a: Int = 0
                        for (i in 0 until response.body()!!.body!!.size) {

                            val json_objectdetail: JSONObject = jsonarray_info.optJSONObject(0)
                            a = json_objectdetail.optInt("user_id")
                            Log.d(
                                "TAGG",
                                "PREf ID:" + json_objectdetail.optInt("user_id").toString()
                            )
                        }
                        checkDefault(a, title, id, isRequired)
                    }

                }
            }

            override fun onFailure(call: Call<CheckPreferenceResponse>, t: Throwable) {
                Logger.d("TAGG", "Apply Job FAILED : $t")
                openBottomSheetPreferences()
            }
        })
    }

    fun checkDefault(id: Int, title: String, jobId: Int, isRequired: Boolean) {

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.CheckDefault(
            id,
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN
        )

        call.enqueue(object : Callback<CheckDefaultResponse> {
            override fun onResponse(
                call: Call<CheckDefaultResponse>,
                response: Response<CheckDefaultResponse>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.e("RESPONSE leave group", "" + Gson().toJson(response))

                if (response.isSuccessful && response.body()!!.responseCode == 10996) {

                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                    val gson = GsonBuilder().serializeNulls().create()
                    val str_response = Gson().toJson(response)
                    val jsonObject: JSONObject = JSONObject(
                        str_response.substring(
                            str_response.indexOf("{"),
                            str_response.lastIndexOf("}") + 1
                        )
                    )
                    if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                        val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                        val jsonarray_info: JSONArray = jsonObject1.optJSONArray("body")
                        listOfResumes.clear()
                        for (i in 0 until response.body()!!.body!!.size) {

                            val json_objectdetail: JSONObject = jsonarray_info.optJSONObject(i)

                            val model: ResumeView = ResumeView();
                            model.is_default = ""//json_objectdetail.optString("is_default")
                            model.path = json_objectdetail.optString("path")
                            model.id = json_objectdetail.optInt("id")
                            model.title =
                                if (json_objectdetail.isNull("title")) "" else json_objectdetail.optString(
                                    "title"
                                )
                            model.is_parsed = false//json_objectdetail.optBoolean("is_parsed")
                            model.created_on = json_objectdetail.optString("created_on")
                            model.modified_on = json_objectdetail.optString("modified_on")
                            model.deleted = json_objectdetail.optBoolean("deleted")
                            model.user_id = json_objectdetail.optInt("user_id")

                            listOfResumes.add(
                                ResumeView(
                                    model.is_default!!, model.path!!,
                                    model.id, model.title!!, model.is_parsed!!,
                                    model.created_on!!, model.modified_on!!,
                                    model.deleted!!, model.user_id!!
                                )
                            )
                        }

                    }
                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
                if (isRequired)
                    openBottomSheetUploadDoc(jobId, title, listOfResumes)
                else {
                    saveJob(jobId, note.text.toString(), listOfResumes[0].id, "Applied")
                }
            }

            override fun onFailure(call: Call<CheckDefaultResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                if (isRequired)
                    openBottomSheetUploadDoc(jobId, title, listOfResumes)
                else {
                    saveJob(jobId, note.text.toString(), listOfResumes[0].id, "Applied")
                }
            }
        })
    }

    var isSelectedId: Int = 0
    fun getRadioSelected(a: Int) {
        Log.d("TAGG", "Slected" + a)
        isSelectedId = a
    }

    public fun openBottomSheetUploadDoc(
        jobId: Int,
        title: String,
        listOfResume: ArrayList<ResumeView>
    ) {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.bottom_sheet_applyjobs)
        val add_resume = dialog.findViewById(R.id.add_resume) as LinearLayout
        val add_mobile_layout = dialog.findViewById(R.id.mobile_layout) as LinearLayout
        add_mobile_layout.visibility = View.GONE
        val add_resume_title = dialog.findViewById(R.id.add_resume_title) as LinearLayout
        val note = dialog.findViewById(R.id.note) as EditText
        val note_layout = dialog.findViewById(R.id.note_layout) as LinearLayout
        if (pageType == 2)
            note_layout.visibility = View.GONE
        else
            note_layout.visibility = View.VISIBLE
        var mAdapterResume: RecyclerView.Adapter<*>? = null
        val mRecyclerView = dialog.findViewById(R.id.resume_recycler_view) as RecyclerView
        val mLayoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        var checklist: java.util.ArrayList<Int> = java.util.ArrayList()
        for (k in 0 until listOfResume.size) {
            if (k == 0)
                checklist.add(1)
            else
                checklist.add(0)
        }
        if (listOfResumes.size > 1)
            mRecyclerView.getLayoutParams().height = 600;

        mAdapterResume = ResumeAdapter1(listOfResumes, checklist, "Dashboard")
        mRecyclerView!!.adapter = mAdapterResume
        mRecyclerView!!.layoutManager = mLayoutManager
        val mobileLayout = dialog.findViewById(R.id.mobile_layout) as LinearLayout
        if (EndPoints.PHONE_NO.length > 2)
            mobileLayout.visibility = View.GONE
        val closebutton = dialog.findViewById(R.id.reportedtext) as TextView
        closebutton.setOnClickListener {
            dialog.cancel()
        }
        closebutton.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val DRAWABLE_LEFT = 0;
                val DRAWABLE_TOP = 1;
                val DRAWABLE_RIGHT = 2;
                val DRAWABLE_BOTTOM = 3;
                if (event!!.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (closebutton.getRight() - closebutton.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds()
                            .width())
                    ) {
                        dialog.cancel()
                        return true;
                    }
                }
                return false;

                return v?.onTouchEvent(event) ?: true
            }
        })
        val jobname = dialog.findViewById(R.id.jobname) as TextView
        jobname.setText(title)
        val upload_doc = dialog.findViewById(R.id.upload_doc) as RelativeLayout
        upload_doc.setOnClickListener {
            if (isStoragePermissionGranted()) {
                showPictureDialog()
            } else {

            }

        }
        val cancel_pref = dialog.findViewById(R.id.cancel_pref) as Button
        cancel_pref.setOnClickListener {
            dialog.cancel()
        }
        val restitle = dialog.findViewById(R.id.resumeTitle) as EditText
        val mobileno = dialog.findViewById(R.id.mobile) as EditText
        val save_pref = dialog.findViewById(R.id.save_pref) as Button
        save_pref.setOnClickListener {
            if (restitle.getText().length > 0) {
                if (listOfResumes.size > 0) {
                    //if(isSelected) {
                    Toast.makeText(applicationContext, "Applied", Toast.LENGTH_LONG).show()
                    saveJob(jobId, note.text.toString(), isSelectedId, "Applied")
                    dialog.cancel()
//                    }
//                    else{
//                        Toast.makeText(applicationContext,"Please select a resume",Toast.LENGTH_LONG).show()
//                    }
                } else {
                    sendResume(restitle.getText().toString(), jobId)
                    dialog.cancel()
                }
            } else {
                Toast.makeText(applicationContext, "Please enter resume title", Toast.LENGTH_LONG)
                    .show()
            }
        }
        var isSelected = true
        val apply_job = dialog.findViewById(R.id.apply_pref) as Button
        apply_job.setText("Upload")
        apply_job.setOnClickListener {

            if (listOfResumes.size > 0) {
//                    if(isSelected) {
                Toast.makeText(applicationContext, "Applied", Toast.LENGTH_LONG).show()
                saveJob(jobId, note.text.toString(), isSelectedId, "Applied")
                dialog.cancel()
//                    }
//                    else{
//                        Toast.makeText(applicationContext,"Please select a resume",Toast.LENGTH_LONG).show()
//                    }
            } else {
                if (picturepath_life!!.length > 0) {
                    if (restitle.getText().length > 0) {
                        sendResume(restitle.getText().toString(), jobId)
                        dialog.cancel()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Please enter resume title",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else
                    Toast.makeText(applicationContext, "Please upload a resume", Toast.LENGTH_LONG)
                        .show()
            }

        }
        if (listOfResume.size > 0) {
            closebutton.setText("Select Resume")
            mRecyclerView.visibility = View.VISIBLE
            add_resume.visibility = View.GONE
            add_resume_title.visibility = View.GONE
        } else {
            closebutton.setText("Upload Resume")
            mRecyclerView.visibility = View.GONE
            add_resume.visibility = View.VISIBLE
            add_resume_title.visibility = View.VISIBLE
        }
//        val jobtype_multiAutoCompleteTextView = dialog.findViewById(R.id.jobtype_multiAutoCompleteTextView) as MultiAutoCompleteTextView
//        val randomArray = ArrayAdapter(this, android.R.layout.simple_list_item_1, listOfJobType)
//        jobtype_multiAutoCompleteTextView.setAdapter(randomArray)
//        jobtype_multiAutoCompleteTextView.setThreshold(1)
//        jobtype_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
//        val editreport = dialog .findViewById(R.id.edit_report) as LinearLayout
//        editreport.setOnClickListener {
//            if(isOwner.equals("true")) {
//                dialog.cancel()
//                intent = Intent(applicationContext, ZEditPostActivity::class.java)
//                intent.putExtra("id", id)
//                intent.putExtra("data", data)
//                intent.putExtra("icon",icon)
//                startActivity(intent)
//            }
//            else
//                Toast.makeText(applicationContext, "You cannot edit the post",Toast.LENGTH_LONG).show()
//        }


//        val share_link = dialog.findViewById(R.id.share_link) as LinearLayout
//        share_link.setOnClickListener {
//            sharegroupdetails()
//        }

//        val deletepost = dialog .findViewById(R.id.delete_post) as LinearLayout
//        deletepost.setOnClickListener {
//            if(isOwner.equals("true")) {
//                retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
//
//                val call = retrofitInterface!!.deletePost(id, EndPoints.CLIENT_ID, "Bearer "+
//                        EndPoints.ACCESS_TOKEN)
//
//                call.enqueue(object : Callback<DeletePostResponse> {
//                    override fun onResponse(call: Call<DeletePostResponse>, response: Response<DeletePostResponse>) {
//
//                        Logger.d("CODE", response.code().toString() + "")
//                        Logger.d("MESSAGE", response.message() + "")
//                        Logger.d("URL", "" + response)
//                        Logger.e("RESPONSE ", "" + Gson().toJson(response))
//
//                        if (response.isSuccessful) {
//
//                            ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
//                            Logger.e("TAGG", "" + response.body()!!.message.toString())
//                            dialog.cancel()
//
//                        } else {
//
//                            ToastHelper.makeToast(applicationContext, "Invalid Request")
//                        }
//                    }
//
//                    override fun onFailure(call: Call<DeletePostResponse>, t: Throwable) {
//
//                        Logger.d("TAGG", "FAILED : $t")
//                    }
//                })
//                dialog.cancel()
//            }
//            else
//                Toast.makeText(applicationContext, "You cannot delete the post",Toast.LENGTH_LONG).show()
//        }

        var window: Window = dialog.getWindow()!!;
        window.setBackgroundDrawable(null);
        var wlp: WindowManager.LayoutParams = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog.show()
    }


    fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                === PackageManager.PERMISSION_GRANTED
            ) {
                Log.v(TAG, "Permission is granted")
                true
            } else {
                Log.v(TAG, "Permission is revoked")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    1
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted")
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0])
            //resume tasks needing this permission
        }
    }

    fun saveJob(job_id: Int, note: String, resume_id: Int, applied_status: String) {

        val params = HashMap<String, String>()

        params["job_id"] = job_id.toString()
        params["note"] = note
        params["resume_id"] = resume_id.toString()
        params["applied_status"] = applied_status

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.saveJob(
            EndPoints.CLIENT_ID, "Bearer " +
                    EndPoints.ACCESS_TOKEN, params
        )

        call.enqueue(object : Callback<SaveJobResponse> {
            override fun onResponse(
                call: Call<SaveJobResponse>,
                response: Response<SaveJobResponse>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.e("RESPONSE leave group", "" + Gson().toJson(response))

                if (response.isSuccessful) {
                    listOfhotJobsdata.clear()
                    loadDashboardData()
                    listOfCompareGroupdata.clear()
                } else {
                    //ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<SaveJobResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
            }
        })
    }

    public fun openBottomSheetPreferencesDashboard() {

        //loadAccountSettings()
//        val dialog = Dialog(this)
//        dialog .setCancelable(true)
//        dialog .setContentView(R.layout.bottom_sheet_preferencesdashboard)

        val pref_keyword = findViewById(R.id.pref_keyword) as EditText
        val pref_location_multiAutoCompleteTextView =
            findViewById(R.id.pref_location_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val locationList: ArrayList<String> = ArrayList()
        for (model in listOfCities) {
            locationList.add(model.name!!.toString())
        }
        val locationArray = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        pref_location_multiAutoCompleteTextView.setAdapter(locationArray)
        pref_location_multiAutoCompleteTextView.setThreshold(1)
        pref_location_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        pref_location_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                pref_location_multiAutoCompleteTextView.showDropDown()
                return false
            }
        })

        val jobtype_multiAutoCompleteTextView =
            findViewById(R.id.jobtype_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val jobTypeList: ArrayList<String> = ArrayList()
        for (model in listOfJobType) {
            jobTypeList.add(model.name!!.toString())
        }
        val randomArray = ArrayAdapter(this, android.R.layout.simple_list_item_1, jobTypeList)
        jobtype_multiAutoCompleteTextView.setAdapter(randomArray)
        jobtype_multiAutoCompleteTextView.setThreshold(1)
        jobtype_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        jobtype_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                jobtype_multiAutoCompleteTextView.showDropDown()
                return false
            }
        })

        val farea_multiAutoCompleteTextView =
            findViewById(R.id.farea_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val functionalareaList: ArrayList<String> = ArrayList()
        for (model in listOfJobFArea) {
            functionalareaList.add(model.name!!.toString())
        }
        val adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, functionalareaList)
        farea_multiAutoCompleteTextView.setAdapter(adapter)
        farea_multiAutoCompleteTextView.setThreshold(1)
        farea_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        farea_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                farea_multiAutoCompleteTextView.showDropDown()
                return false
            }
        })

        var type: String = ""
        val industry_multiAutoCompleteTextView =
            findViewById(R.id.industry_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val industryList: ArrayList<String> = ArrayList()
        for (model in listOfJobIndustry) {
            industryList.add(model.name!!.toString())
        }
        val experienceArray =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, industryList)
        industry_multiAutoCompleteTextView.setAdapter(experienceArray)
        industry_multiAutoCompleteTextView.setThreshold(1)
        industry_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        industry_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                industry_multiAutoCompleteTextView.showDropDown()
                return false
            }
        })

        val experience_multiAutoCompleteTextView =
            findViewById(R.id.experience_multiAutoCompleteTextView) as Spinner
        val experienceList: ArrayList<String> = ArrayList()
        for (i in 0..30)
            experienceList.add(i.toString())
        val adapter1 =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, experienceList)
        experience_multiAutoCompleteTextView.setAdapter(adapter1)
//        experience_multiAutoCompleteTextView.setThreshold(1)
//        experience_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
//        experience_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {
//
//            override fun onTouch(v: View, event: MotionEvent): Boolean {
//                experience_multiAutoCompleteTextView.showDropDown()
//                return false
//            }
//        })

        val ctc_multiAutoCompleteTextView =
            findViewById(R.id.ctc_multiAutoCompleteTextView) as Spinner
        val ctcList: ArrayList<String> = ArrayList()
        for (i in 0..30)
            ctcList.add(i.toString())
        val adapter2 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ctcList)
        ctc_multiAutoCompleteTextView.setAdapter(adapter2)

        val ctc1_multiAutoCompleteTextView =
            findViewById(R.id.ctc1_multiAutoCompleteTextView) as Spinner
        val ctcList1: ArrayList<String> = ArrayList()
        for (i in 0..30)
            ctcList1.add(i.toString())
        val adapter3 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ctcList1)
        ctc1_multiAutoCompleteTextView.setAdapter(adapter3)


        val experienceto_multiAutoCompleteTextView =
            findViewById(R.id.experienceto_multiAutoCompleteTextView) as Spinner
        experienceto_multiAutoCompleteTextView.setAdapter(adapter1)
//        experienceto_multiAutoCompleteTextView.setThreshold(1)
//        experienceto_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
//        experienceto_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {
//
//            override fun onTouch(v: View, event: MotionEvent): Boolean {
//                experienceto_multiAutoCompleteTextView.showDropDown()
//                return false
//            }
//        })

//        val crossbutton_pref = findViewById(R.id.closetext) as TextView
//        crossbutton_pref.setOnClickListener {
//            //dialog.cancel()
//        }

        val cancel_pref = findViewById(R.id.cancel_pref) as Button
        cancel_pref.setOnClickListener {
            dash_myjobs.callOnClick()
            //dialog.cancel()
        }
        val save_pref = findViewById(R.id.save_pref) as Button
        save_pref.setOnClickListener {
            if (pref_location_multiAutoCompleteTextView.text.trim().toString().length > 0 ||
                pref_keyword.text.trim().toString().length > 0
            ) {
                if (Integer.parseInt(experience_multiAutoCompleteTextView.selectedItem.toString()) <
                    Integer.parseInt(experienceto_multiAutoCompleteTextView.selectedItem.toString())
                ) {
                    if (type.equals("post")) {
                        savePreferences(
                            pref_location_multiAutoCompleteTextView.text.trim().toString(),
                            jobtype_multiAutoCompleteTextView.text.trim().toString(),
                            farea_multiAutoCompleteTextView.text.trim().toString(),
                            industry_multiAutoCompleteTextView.text.trim().toString(),
                            pref_keyword.text.trim().toString(),
                            experience_multiAutoCompleteTextView.selectedItem.toString(),
                            experienceto_multiAutoCompleteTextView.selectedItem.toString()
//                            ctc_multiAutoCompleteTextView.selectedItem.toString(),
//                            ctc1_multiAutoCompleteTextView.selectedItem.toString()
                        )                                                        //do post and put
                    } else {
                        updatePreferences(
                            pref_location_multiAutoCompleteTextView.text.trim().toString(),
                            jobtype_multiAutoCompleteTextView.text.trim().toString(),
                            farea_multiAutoCompleteTextView.text.trim().toString(),
                            industry_multiAutoCompleteTextView.text.trim().toString(),
                            pref_keyword.text.trim().toString(),
                            experience_multiAutoCompleteTextView.selectedItem.toString(),
                            experienceto_multiAutoCompleteTextView.selectedItem.toString()
//                            ctc_multiAutoCompleteTextView.selectedItem.toString(),
//                            ctc1_multiAutoCompleteTextView.selectedItem.toString()
                        )
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "From experience should be less then To experience",
                        Toast.LENGTH_LONG
                    ).show()
                }

                //dialog.cancel()
            } else {
                Toast.makeText(applicationContext, "Please enter all the values", Toast.LENGTH_LONG)
                    .show()
            }
        }

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.CheckPreferences(
            "application/json",
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN
        )

        call.enqueue(object : Callback<CheckPreferenceResponse> {
            override fun onResponse(
                call: Call<CheckPreferenceResponse>,
                response: Response<CheckPreferenceResponse>
            ) {


                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )

                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                    val responseCode: Int = jsonObject1.optInt("response_code")
                    val message: String = jsonObject1.optString("message")
                    val jsonarray_info: JSONArray = jsonObject1.optJSONArray("body")
                    if (response.body()!!.responseCode == 11405)
                        type = "post"

                    if (response.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            response.body()!!.message,
                            Toast.LENGTH_LONG
                        )
                            .show()


                        if (jsonarray_info.optJSONObject(0).optString("preferred_job_type")
                                .equals("null") || jsonarray_info.optJSONObject(0)
                                .optString("preferred_job_type").equals("")
                        )
                            jobtype_multiAutoCompleteTextView.setText("Not specified")
                        else
                            jobtype_multiAutoCompleteTextView.setText(
                                jsonarray_info.optJSONObject(0)
                                    .optString("preferred_job_type") + ","
                            )
                        if (jsonarray_info.optJSONObject(0).optString("preferred_functional_area")
                                .equals("null") || jsonarray_info.optJSONObject(0)
                                .optString("preferred_functional_area").equals("")
                        )
                            farea_multiAutoCompleteTextView.setText("Not specified")
                        else
                            farea_multiAutoCompleteTextView.setText(
                                jsonarray_info.optJSONObject(0)
                                    .optString("preferred_functional_area") + ","
                            )
                        if (jsonarray_info.optJSONObject(0).optString("preferred_industry")
                                .equals("null") || jsonarray_info.optJSONObject(0)
                                .optString("preferred_industry").equals("")
                        )
                            industry_multiAutoCompleteTextView.setText("Not specified")
                        else
                            industry_multiAutoCompleteTextView.setText(
                                jsonarray_info.optJSONObject(0)
                                    .optString("preferred_industry") + ","
                            )

                        if (jsonarray_info.optJSONObject(0).optString("exp_to_year")
                                .equals("null") || jsonarray_info.optJSONObject(0)
                                .optString("exp_to_year").equals("0")
                        )
                            experienceto_multiAutoCompleteTextView.setSelection(0)
                        else
                            experienceto_multiAutoCompleteTextView.setSelection(
                                Integer.parseInt(
                                    jsonarray_info.optJSONObject(0).optString("exp_to_year")
                                        .toString()
                                )
                            )

                        if (jsonarray_info.optJSONObject(0).optString("exp_from_year")
                                .equals("null") || jsonarray_info.optJSONObject(0)
                                .optString("exp_from_year").equals("0")
                        )
                            experience_multiAutoCompleteTextView.setSelection(0)
                        else
                            experience_multiAutoCompleteTextView.setSelection(
                                Integer.parseInt(
                                    jsonarray_info.optJSONObject(0).optString("exp_from_year")
                                        .toString()
                                )
                            )

                        if (jsonarray_info.optJSONObject(0).optString("skills")
                                .equals("null") || jsonarray_info.optJSONObject(0)
                                .optString("skills")
                                .equals("")
                        )
                            pref_keyword.setText("Not specified")
                        else
                            pref_keyword.setText(
                                jsonarray_info.optJSONObject(0).optString("skills")
                            )
                        pref_location_multiAutoCompleteTextView.setText(
                            jsonarray_info.optJSONObject(0).optString("preferred_city") + ","
                        )

                        if (jsonarray_info.optJSONObject(0).optString("preferred_salary")
                                .equals("null") || jsonarray_info.optJSONObject(0)
                                .optString("preferred_salary").equals("0.0")
                        )
                        // Log.d("TAGG",jsonarray_info.optJSONObject(0).optString("preferred_salary"))
                            ctc_multiAutoCompleteTextView.setSelection(0)
                        else {
                            val s: String =
                                jsonarray_info.optJSONObject(0).optString("preferred_salary")
                                    .toString()
                            val s1 = s.split(".")
                            ctc_multiAutoCompleteTextView.setSelection(Integer.parseInt(s1[0]))
                            ctc1_multiAutoCompleteTextView.setSelection(Integer.parseInt(s1[1]))
                        }
                    }

                }
            }

            override fun onFailure(call: Call<CheckPreferenceResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                type = "post"
            }
        })

//        var window :Window = dialog.getWindow();
//        window.setBackgroundDrawable(null);
//        var wlp: WindowManager.LayoutParams = window.getAttributes();
//        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        wlp.gravity = Gravity.BOTTOM;
//        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        window.setAttributes(wlp);
//        dialog .show()
    }

    public fun openBottomSheetJobAlerts(id: Int) {

        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.bottom_sheet_preferencesdashboard)

        val header = dialog.findViewById(R.id.closetext) as TextView
        if (id == 0)
            header.setText("Add Job Alert")
        else
            header.setText("Edit Job Alert")

        val pref_title = dialog.findViewById(R.id.title) as EditText
        val pref_keyword = dialog.findViewById(R.id.pref_keyword) as EditText
        val pref_location_multiAutoCompleteTextView =
            dialog.findViewById(R.id.pref_location_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val locationList: ArrayList<String> = ArrayList()
        for (model in listOfCities) {
            locationList.add(model.name!!.toString())
        }
        val locationArray = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        pref_location_multiAutoCompleteTextView.setAdapter(locationArray)
        pref_location_multiAutoCompleteTextView.setThreshold(1)
        pref_location_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        val jobtype_multiAutoCompleteTextView =
            dialog.findViewById(R.id.jobtype_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val jobTypeList: ArrayList<String> = ArrayList()
        for (model in listOfJobType) {
            jobTypeList.add(model.name!!.toString())
        }
        val randomArray = ArrayAdapter(this, android.R.layout.simple_list_item_1, jobTypeList)
        jobtype_multiAutoCompleteTextView.setAdapter(randomArray)
        jobtype_multiAutoCompleteTextView.setThreshold(1)
        jobtype_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        val farea_multiAutoCompleteTextView =
            dialog.findViewById(R.id.farea_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val functionalareaList: ArrayList<String> = ArrayList()
        for (model in listOfJobFArea) {
            functionalareaList.add(model.name!!.toString())
        }
        val adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, functionalareaList)
        farea_multiAutoCompleteTextView.setAdapter(adapter)
        farea_multiAutoCompleteTextView.setThreshold(1)
        farea_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        val industry_multiAutoCompleteTextView =
            dialog.findViewById(R.id.industry_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val industryList: ArrayList<String> = ArrayList()
        for (model in listOfJobIndustry) {
            industryList.add(model.name!!.toString())
        }
        val experienceArray =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, industryList)
        industry_multiAutoCompleteTextView.setAdapter(experienceArray)
        industry_multiAutoCompleteTextView.setThreshold(1)
        industry_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        val experience_multiAutoCompleteTextView =
            dialog.findViewById(R.id.experience_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val experienceList: ArrayList<String> = ArrayList()
        for (i in 0..50)
            experienceList.add(i.toString())
        val adapter1 =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, experienceList)
        experience_multiAutoCompleteTextView.setAdapter(adapter1)
        experience_multiAutoCompleteTextView.setThreshold(1)
        experience_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        val crossbutton_pref = dialog.findViewById(R.id.closetext) as TextView
        crossbutton_pref.setOnClickListener {
            dialog.cancel()
        }

        val cancel_pref = dialog.findViewById(R.id.cancel_pref) as Button
        cancel_pref.setOnClickListener {
            dialog.cancel()
        }
        val save_pref = dialog.findViewById(R.id.save_pref) as Button
        save_pref.setOnClickListener {
            if (pref_location_multiAutoCompleteTextView.text.trim().toString().length > 0 &&
                pref_title.text.trim().toString().length > 0
            ) {


                if (id > 0) {
                    saveJobAlerts(
                        id,
                        pref_title.text.trim().toString(),
                        pref_location_multiAutoCompleteTextView.text.trim().toString(),
                        jobtype_multiAutoCompleteTextView.text.trim().toString(),
                        farea_multiAutoCompleteTextView.text.trim().toString(),
                        industry_multiAutoCompleteTextView.text.trim().toString(),
                        pref_keyword.text.trim().toString(),
                        experience_multiAutoCompleteTextView.text.trim().toString()
                    )
                } else {
                    addJobAlerts(
                        pref_title.text.trim().toString(),
                        pref_location_multiAutoCompleteTextView.text.trim().toString(),
                        jobtype_multiAutoCompleteTextView.text.trim().toString(),
                        farea_multiAutoCompleteTextView.text.trim().toString(),
                        industry_multiAutoCompleteTextView.text.trim().toString(),
                        pref_keyword.text.trim().toString(),
                        experience_multiAutoCompleteTextView.text.trim().toString()
                    )
                }
                dialog.cancel()
            } else {
                Toast.makeText(applicationContext, "Please enter title and city", Toast.LENGTH_LONG)
                    .show()
            }
        }

        if (id > 0) {
            for (i in 0 until listOfPreferencedata.size) {
                if (id == listOfPreferencedata[i].id) {
                    pref_title.setText(listOfPreferencedata[i].title)
                    pref_location_multiAutoCompleteTextView.setText(listOfPreferencedata[i].city_name + ",")
                    if (listOfPreferencedata[i].job_type.equals("null") || listOfPreferencedata[i].job_type.equals(
                            ""
                        )
                    )
                        jobtype_multiAutoCompleteTextView.setText("Not specified")
                    else
                        jobtype_multiAutoCompleteTextView.setText(listOfPreferencedata[i].job_type + ",")
                    if (listOfPreferencedata[i].functional_area.equals("null") || listOfPreferencedata[i].functional_area.equals(
                            ""
                        )
                    )
                        farea_multiAutoCompleteTextView.setText("Not specified")
                    else
                        farea_multiAutoCompleteTextView.setText(listOfPreferencedata[i].functional_area + ",")
                    if (listOfPreferencedata[i].industry.equals("null") || listOfPreferencedata[i].industry.equals(
                            ""
                        )
                    )
                        industry_multiAutoCompleteTextView.setText("Not specified")
                    else
                        industry_multiAutoCompleteTextView.setText(listOfPreferencedata[i].industry + ",")

                    if (listOfPreferencedata[i].experience_max_year == 0 || listOfPreferencedata[i].experience_min_year == 0)
                        experience_multiAutoCompleteTextView.setText("Not specified")
                    else
                        experience_multiAutoCompleteTextView.setText(listOfPreferencedata[i].experience_min_year.toString())
                    if (listOfPreferencedata[i].skills.equals("null") || listOfPreferencedata[i].skills.equals(
                            ""
                        )
                    )
                        pref_keyword.setText("Not specified")
                    else
                        pref_keyword.setText(listOfPreferencedata[i].skills)
                }
            }
        }


        var window: Window = dialog.getWindow()!!;
        window.setBackgroundDrawable(null);
        var wlp: WindowManager.LayoutParams = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog.show()
    }

    fun shareProfile() {

        intent = Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Following | JobsForHer");
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Click on the link \n http://www.workingnaari.in/dashboard/following" + "\n\n" +
                    "Application Link : https://play.google.com/store/apps/details?id=${applicationContext.getPackageName()}"
        );
        //intent.putExtra(Intent.EXTRA_TEXT, "Application Link : https://play.google.com/store/apps/details?id=${context.getPackageName()}")
        startActivity(Intent.createChooser(intent, "Share Following link!"));
    }

    public fun openBottomSheetPreferences() {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.bottom_sheet_preferences)

        val pref_keyword = dialog.findViewById(R.id.pref_keyword) as EditText
        val pref_location_multiAutoCompleteTextView =
            dialog.findViewById(R.id.pref_location_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val locationList: ArrayList<String> = ArrayList()
        for (model in listOfCities) {
            locationList.add(model.name!!.toString())
        }
        val locationArray = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        pref_location_multiAutoCompleteTextView.setAdapter(locationArray)
        pref_location_multiAutoCompleteTextView.setThreshold(1)
        pref_location_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        pref_location_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                pref_location_multiAutoCompleteTextView.showDropDown()
                return false
            }
        })

        val jobtype_multiAutoCompleteTextView =
            dialog.findViewById(R.id.jobtype_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val jobTypeList: ArrayList<String> = ArrayList()
        for (model in listOfJobType) {
            jobTypeList.add(model.name!!.toString())
        }
        val randomArray = ArrayAdapter(this, android.R.layout.simple_list_item_1, jobTypeList)
        jobtype_multiAutoCompleteTextView.setAdapter(randomArray)
        jobtype_multiAutoCompleteTextView.setThreshold(1)
        jobtype_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        jobtype_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                jobtype_multiAutoCompleteTextView.showDropDown()
                return false
            }
        })

        val farea_multiAutoCompleteTextView =
            dialog.findViewById(R.id.farea_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val functionalareaList: ArrayList<String> = ArrayList()
        for (model in listOfJobFArea) {
            functionalareaList.add(model.name!!.toString())
        }
        val adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, functionalareaList)
        farea_multiAutoCompleteTextView.setAdapter(adapter)
        farea_multiAutoCompleteTextView.setThreshold(1)
        farea_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        farea_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                farea_multiAutoCompleteTextView.showDropDown()
                return false
            }
        })

        val industry_multiAutoCompleteTextView =
            dialog.findViewById(R.id.industry_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val industryList: ArrayList<String> = ArrayList()
        for (model in listOfJobIndustry) {
            industryList.add(model.name!!.toString())
        }
        val experienceArray =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, industryList)
        industry_multiAutoCompleteTextView.setAdapter(experienceArray)
        industry_multiAutoCompleteTextView.setThreshold(1)
        industry_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        industry_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                industry_multiAutoCompleteTextView.showDropDown()
                return false
            }
        })

        val experience_multiAutoCompleteTextView =
            dialog.findViewById(R.id.experience_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val experienceList: ArrayList<String> = ArrayList()
        for (i in 0..50)
            experienceList.add(i.toString())
        val adapter1 =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, experienceList)
        experience_multiAutoCompleteTextView.setAdapter(adapter1)
        experience_multiAutoCompleteTextView.setThreshold(1)
        experience_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        val experienceto_multiAutoCompleteTextView =
            dialog.findViewById(R.id.experienceto_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        experienceto_multiAutoCompleteTextView.setAdapter(adapter1)
        experienceto_multiAutoCompleteTextView.setThreshold(1)
        experienceto_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        experienceto_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                experienceto_multiAutoCompleteTextView.showDropDown()
                return false
            }
        })

        val ctc_multiAutoCompleteTextView =
            dialog.findViewById(R.id.ctc_multiAutoCompleteTextView) as Spinner
        val ctcList: ArrayList<String> = ArrayList()
        for (i in 0..30)
            ctcList.add(i.toString())
        val adapter2 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ctcList)
        ctc_multiAutoCompleteTextView.setAdapter(adapter2)

        val ctc1_multiAutoCompleteTextView =
            dialog.findViewById(R.id.ctc1_multiAutoCompleteTextView) as Spinner
        val ctcList1: ArrayList<String> = ArrayList()
        for (i in 0..30)
            ctcList1.add(i.toString())
        val adapter3 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ctcList1)
        ctc1_multiAutoCompleteTextView.setAdapter(adapter3)

        val crossbutton_pref = dialog.findViewById(R.id.closetext) as TextView
        crossbutton_pref.setOnClickListener {
            dialog.cancel()
        }

        val cancel_pref = dialog.findViewById(R.id.cancel_pref) as Button
        cancel_pref.setOnClickListener {
            dialog.cancel()
        }
        val save_pref = dialog.findViewById(R.id.save_pref) as Button
        save_pref.setOnClickListener {
            if (pref_location_multiAutoCompleteTextView.text.trim().toString().length > 0 ||
                jobtype_multiAutoCompleteTextView.text.trim().toString().length > 0 ||
                farea_multiAutoCompleteTextView.text.trim().toString().length > 0 ||
                industry_multiAutoCompleteTextView.text.trim().toString().length > 0 ||
                pref_keyword.text.trim().toString().length > 0 ||
                experience_multiAutoCompleteTextView.text.trim().toString().length > 0
            ) {
                savePreferences(
                    pref_location_multiAutoCompleteTextView.text.trim().toString(),
                    jobtype_multiAutoCompleteTextView.text.trim().toString(),
                    farea_multiAutoCompleteTextView.text.trim().toString(),
                    industry_multiAutoCompleteTextView.text.trim().toString(),
                    pref_keyword.text.trim().toString(),
                    experience_multiAutoCompleteTextView.text.trim().toString(),
                    experienceto_multiAutoCompleteTextView.text.trim().toString()
//                    ctc_multiAutoCompleteTextView.selectedItem.toString(),
//                    ctc1_multiAutoCompleteTextView.selectedItem.toString()
                )
                dialog.cancel()
            } else {
                Toast.makeText(applicationContext, "Please enter all the values", Toast.LENGTH_LONG)
                    .show()
            }
        }


//        val editreport = dialog .findViewById(R.id.edit_report) as LinearLayout
//        editreport.setOnClickListener {
//            if(isOwner.equals("true")) {
//                dialog.cancel()
//                intent = Intent(applicationContext, ZEditPostActivity::class.java)
//                intent.putExtra("id", id)
//                intent.putExtra("data", data)
//                intent.putExtra("icon",icon)
//                startActivity(intent)
//            }
//            else
//                Toast.makeText(applicationContext, "You cannot edit the post",Toast.LENGTH_LONG).show()
//        }


//        val share_link = dialog.findViewById(R.id.share_link) as LinearLayout
//        share_link.setOnClickListener {
//            sharegroupdetails()
//        }

//        val deletepost = dialog .findViewById(R.id.delete_post) as LinearLayout
//        deletepost.setOnClickListener {
//            if(isOwner.equals("true")) {
//                retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
//
//                val call = retrofitInterface!!.deletePost(id, EndPoints.CLIENT_ID, "Bearer "+
//                        EndPoints.ACCESS_TOKEN)
//
//                call.enqueue(object : Callback<DeletePostResponse> {
//                    override fun onResponse(call: Call<DeletePostResponse>, response: Response<DeletePostResponse>) {
//
//                        Logger.d("CODE", response.code().toString() + "")
//                        Logger.d("MESSAGE", response.message() + "")
//                        Logger.d("URL", "" + response)
//                        Logger.e("RESPONSE ", "" + Gson().toJson(response))
//
//                        if (response.isSuccessful) {
//
//                            ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
//                            Logger.e("TAGG", "" + response.body()!!.message.toString())
//                            dialog.cancel()
//
//                        } else {
//
//                            ToastHelper.makeToast(applicationContext, "Invalid Request")
//                        }
//                    }
//
//                    override fun onFailure(call: Call<DeletePostResponse>, t: Throwable) {
//
//                        Logger.d("TAGG", "FAILED : $t")
//                    }
//                })
//                dialog.cancel()
//            }
//            else
//                Toast.makeText(applicationContext, "You cannot delete the post",Toast.LENGTH_LONG).show()
//        }

        var window: Window = dialog.getWindow()!!;
        window.setBackgroundDrawable(null);
        var wlp: WindowManager.LayoutParams = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog.show()
    }


    private fun showPictureDialog() {

        val mimeTypes = arrayOf(
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
            //"application/vnd.ms-powerpoint",
            //"application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
            //"application/vnd.ms-excel",
            //"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
            //"text/plain",
            "application/pdf"
            //"application/zip"
        )
        val chooseFile: Intent
        var intent: Intent = Intent()
        chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE)
        chooseFile.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        chooseFile.type = if (mimeTypes.size === 1) mimeTypes[0] else "*/*"
        if (mimeTypes.size > 0) {
            chooseFile.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        }
        //chooseFile.type = ("application/pdf|application/msword")
        intent = Intent.createChooser(chooseFile, "Choose a file")
        startActivityForResult(intent, GALLERY_PDF)
    }

    fun sendResume(title: String, jobId: Int) {
        if (imageEncoded_life.isNullOrEmpty()) {
            Toast.makeText(this, "Please try uploading your resume Again.", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val params = HashMap<String, String>()

        params["title"] = title
        params["resume_filename"] = picturepath_life.toString()
        params["resume_file"] = imageEncoded_life.toString()

        Logger.d("TAGG", "PARAMS: " + params)
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.SendResume(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        Logger.d("URL", "" + params)
        call.enqueue(object : Callback<UpdateReplyResponse> {
            override fun onResponse(
                call: Call<UpdateReplyResponse>,
                response: Response<UpdateReplyResponse>
            ) {
                var str_response = Gson().toJson(response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )

                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                    val responseCode: Int = jsonObject1.optInt("response_code")
                    val message: String = jsonObject1.optString("message")
                    var jsonarray: JSONObject = jsonObject1.optJSONObject("body")

                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Adding Success!!", Toast.LENGTH_LONG)
                            .show()
                        val resumeId = jsonarray.optInt("id")
                        picturepath_life = ""
                        mRecyclerViewResumes!!.visibility = View.VISIBLE
                        loadDashboardData()
                        pageType = 2
                        saveJob(jobId, "note", resumeId, "Applied")
                    } else {
                        ToastHelper.makeToast(applicationContext, "message")
                    }
                }
            }

            override fun onFailure(call: Call<UpdateReplyResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext, "Adding Failed!!", Toast.LENGTH_LONG).show()

            }
        })
    }

    fun savePreferences(
        pref_location: String,
        jobtype: String,
        farea: String,
        industry: String,
        pref_keyword: String,
        experience: String,
        maxexperience: String/*, ctc:String, ctc1:String*/
    ) {

        val params = HashMap<String, String>()

        if (pref_location.equals("")) {
        } else {
            params["preferred_city"] = pref_location.substring(0, pref_location.length - 1)
        }
        if (jobtype.equals("")) {
        } else {
            params["preferred_job_type"] = jobtype.substring(0, jobtype.length - 1)
        }
        if (farea.equals("")) {
        } else {
            params["preferred_functional_area"] = farea.substring(0, farea.length - 1)
        }
        if (industry.equals("")) {
        } else {
            params["preferred_industry"] = industry.substring(0, industry.length - 1)
        }
        if (pref_keyword.equals("")) {
        } else {
            params["skills"] = pref_keyword.toString()
        }
        if (experience.equals("")) {
        } else {
            if (experience.contains(",")) {
                params["exp_from_year"] = experience.substring(0, experience.length - 1)
            } else {
                params["exp_from_year"] = experience.toString()
            }
        }
        if (maxexperience.equals("")) {
        } else {
            if (maxexperience.contains(",")) {
                params["exp_to_year"] = maxexperience.substring(0, experience.length - 1)
            } else {
                params["exp_to_year"] = maxexperience.toString()
            }
        }
//        if(ctc.equals("")){}
//        else{
//            if(ctc.contains(",")) {
//                params["preferred_salary"] = ctc+"."+ctc1
//            }
//            else{
//                params["preferred_salary"] = ctc+"."+ctc1
//            }
//        }

        Log.d("TAGG", params.toString())

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.PostPreferences(
            EndPoints.CLIENT_ID, "Bearer " +
                    EndPoints.ACCESS_TOKEN, params
        )

        call.enqueue(object : Callback<UpdateReplyResponse> {
            override fun onResponse(
                call: Call<UpdateReplyResponse>,
                response: Response<UpdateReplyResponse>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.e("RESPONSE report group", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                    Logger.e("TAGG", "SAVE PREFERENCES" + response.body()!!.message.toString())

                } else {

                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<UpdateReplyResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })

    }

    fun updatePreferences(
        pref_location: String,
        jobtype: String,
        farea: String,
        industry: String,
        pref_keyword: String,
        experience: String,
        maxexperience: String/*, ctc:String, ctc1:String*/
    ) {

        val params = HashMap<String, String>()

        if (pref_location.equals("")) {
        } else {
            params["preferred_city"] = pref_location.substring(0, pref_location.length - 1)
        }
        if (jobtype.equals("") || jobtype.equals("Not specified")) {
            params["preferred_job_type"] = ""
        } else {
            params["preferred_job_type"] = jobtype.substring(0, jobtype.length - 1)
        }
        if (farea.equals("") || farea.equals("Not specified")) {
            params["preferred_functional_area"] = ""
        } else {
            params["preferred_functional_area"] = farea.substring(0, farea.length - 1)
        }
        if (industry.equals("") || industry.equals("Not specified")) {
            params["preferred_industry"] = ""
        } else {
            params["preferred_industry"] = industry.substring(0, industry.length - 1)
        }
        if (pref_keyword.equals("") || pref_keyword.equals("Not specified")) {
            params["skills"] = ""
        } else {
            params["skills"] = pref_keyword.toString()
        }
        if (experience.equals("") || experience.equals("Not specified")) {
            params["exp_from_year"] = ""
            params["exp_to_year"] = ""
        } else {
            if (experience.contains(",")) {
                params["exp_from_year"] = experience.substring(0, experience.length - 1)
            } else {
                params["exp_from_year"] = experience.toString()
            }
        }
        if (maxexperience.equals("") || experience.equals("Not specified")) {
            params["exp_from_year"] = ""
            params["exp_to_year"] = ""
        } else {
            if (maxexperience.contains(",")) {
                params["exp_to_year"] = maxexperience.substring(0, experience.length - 1)
            } else {
                params["exp_to_year"] = maxexperience.toString()
            }
        }
//        if(ctc.equals("")){}
////        else{
////            if(ctc.contains(",")) {
////                params["preferred_salary"] = ctc+"."+ctc1
////            }
////            else{
////                params["preferred_salary"] = ctc+"."+ctc1
////            }
////        }

        Log.d("TAGG", params.toString())
        //val s:Int = email_resendp.getTag(R.string.Follow_us_on) as Int
        val sharedPref: SharedPreferences = getSharedPreferences(
            "mysettings",
            Context.MODE_PRIVATE
        )
        Log.d("TAGG", "USERID" + sharedPref.getString(PREF_USERID, "id")!!)
        val s: Int = Integer.parseInt(sharedPref.getString(PREF_USERID, "id")!!)
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.updatePreferences(
            s, EndPoints.CLIENT_ID, "Bearer " +
                    EndPoints.ACCESS_TOKEN, params
        )

        call.enqueue(object : Callback<UpdateReplyResponse> {
            override fun onResponse(
                call: Call<UpdateReplyResponse>,
                response: Response<UpdateReplyResponse>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.e("RESPONSE report group", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                    Logger.e("TAGG", "SAVE PREFERENCES" + response.body()!!.message.toString())

                } else {

                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<UpdateReplyResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })

    }

    fun saveJobAlerts(
        id: Int,
        pref_title: String,
        pref_location: String,
        jobtype: String,
        farea: String,
        industry: String,
        pref_keyword: String,
        experience: String
    ) {

        val params = HashMap<String, String>()

        params["title"] = pref_title.toString()
        if (pref_location.equals("")) {
            params["city_name"] = ""
        } else {
            params["city_name"] = pref_location.substring(0, pref_location.length - 1)
        }
        if (jobtype.equals("") || jobtype.equals("Not specified")) {
            params["job_type"] = ""
        } else {
            params["job_type"] = jobtype.substring(0, jobtype.length - 1)
        }
        if (farea.equals("") || farea.equals("Not specified")) {
            params["functional_area"] = ""
        } else {
            params["functional_area"] = farea.substring(0, farea.length - 1)
        }
        if (industry.equals("") || industry.equals("Not specified")) {
            params["industry"] = ""
        } else {
            params["industry"] = industry.substring(0, industry.length - 1)
        }
        if (pref_keyword.equals("") || pref_keyword.equals("Not specified")) {
            params["skills"] = ""
        } else {
            params["skills"] = pref_keyword.toString()
        }
        if (experience.equals("") || experience.equals("Not specified")) {
            params["experience_max_year"] = ""
            params["experience_min_year"] = ""
        } else {
            params["experience_max_year"] = experience.toString()
            params["experience_min_year"] = experience.toString()
        }


        Log.d("TAGG", params.toString())

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.UpdateJobAlerts(
            id, "application/json", EndPoints.CLIENT_ID, "Bearer " +
                    EndPoints.ACCESS_TOKEN, params
        )

        call.enqueue(object : Callback<JobAlertResponse> {
            override fun onResponse(
                call: Call<JobAlertResponse>,
                response: Response<JobAlertResponse>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.e("RESPONSE report group", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    ToastHelper.makeToast(applicationContext, "Job alert updated")
                    ///Logger.e("TAGG", "Update Success" + response.body()!!.message.toString())
                    loadJobAlert()
                } else {

                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<JobAlertResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                loadJobAlert()
            }

        })

    }

    fun addJobAlerts(
        pref_title: String,
        pref_location: String,
        jobtype: String,
        farea: String,
        industry: String,
        pref_keyword: String,
        experience: String
    ) {

        val params = HashMap<String, String>()

        params["title"] = pref_title.toString()
        if (pref_location.equals("")) {
        } else {
            params["city_name"] = pref_location.substring(0, pref_location.length - 1)
        }
        if (jobtype.equals("")) {
        } else {
            params["job_type"] = jobtype.substring(0, jobtype.length - 1)
        }
        if (farea.equals("")) {
        } else {
            params["functional_area"] = farea.substring(0, farea.length - 1)
        }
        if (industry.equals("")) {
        } else {
            params["industry"] = industry.substring(0, industry.length - 1)
        }
        if (pref_keyword.equals("")) {
        } else {
            params["skills"] = pref_keyword.toString()
        }
        if (experience.equals("")) {
        } else {
            params["experience_max_year"] = experience.toString()
            params["experience_min_year"] = "0"
        }

        Log.d("TAGG", params.toString())

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.AddJobAlerts(
            EndPoints.CLIENT_ID, "Bearer " +
                    EndPoints.ACCESS_TOKEN, params
        )

        call.enqueue(object : Callback<JobAlertResponse> {
            override fun onResponse(
                call: Call<JobAlertResponse>,
                response: Response<JobAlertResponse>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.e("RESPONSE report group", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    ToastHelper.makeToast(applicationContext, "Job alert Added")
                    Logger.e("TAGG", "Add Success" + response.body()!!.message.toString())
                    loadJobAlert()
                } else {
                    loadJobAlert()
                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<JobAlertResponse>, t: Throwable) {
                loadJobAlert()
                Logger.d("TAGG", "FAILED : $t")
            }
        })
        //Toast.makeText(applicationContext,"Job Alert Added",Toast.LENGTH_LONG).show()
    }

    fun deleteJobAlerts(id: Int) {

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.DeleteJobAlerts(
            id, "application/json", EndPoints.CLIENT_ID, "Bearer " +
                    EndPoints.ACCESS_TOKEN
        )

        call.enqueue(object : Callback<JobAlertResponse> {
            override fun onResponse(
                call: Call<JobAlertResponse>,
                response: Response<JobAlertResponse>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.e("RESPONSE report group", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    ToastHelper.makeToast(applicationContext, "Job alert deleted")
                    Logger.e("TAGG", "Delete Success" + response.body()!!.message.toString())
                    loadJobAlert()
                } else {

                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                    loadJobAlert()
                }
            }

            override fun onFailure(call: Call<JobAlertResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                loadJobAlert()
            }
        })
    }

    fun deleteResume(id: Int, userId: Int) {

        val params = HashMap<String, String>()

        params["user_id"] = userId.toString()
        params["deleted"] = 1.toString()

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.DeleteResume(
            id, EndPoints.CLIENT_ID, "Bearer " +
                    EndPoints.ACCESS_TOKEN, params
        )

        call.enqueue(object : Callback<PreferenceResponse> {
            override fun onResponse(
                call: Call<PreferenceResponse>,
                response: Response<PreferenceResponse>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.e("RESPONSE report group", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                    Logger.e("TAGG", "Delete Success" + response.body()!!.message.toString())
                    loadDashboardData()
                    pageType = 2

                } else {

                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                    loadDashboardData()
                    pageType = 2
                }
            }

            override fun onFailure(call: Call<PreferenceResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                ToastHelper.makeToast(applicationContext, "Deleted success!!")
                loadDashboardData()
                pageType = 2
            }
        })

    }

    fun makeDefaultResume(id: Int, userId: Int) {

        val params = HashMap<String, String>()

        params["is_default"] = "1"
        params["user_id"] = userId.toString()

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.makeDefaultResume(
            id, EndPoints.CLIENT_ID, "Bearer " +
                    EndPoints.ACCESS_TOKEN, params
        )

        call.enqueue(object : Callback<PreferenceResponse> {
            override fun onResponse(
                call: Call<PreferenceResponse>,
                response: Response<PreferenceResponse>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.e("RESPONSE report group", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                    Logger.e("TAGG", "Default Success" + response.body()!!.message.toString())
                    loadDashboardData()
                    pageType = 2

                } else {

                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                    loadDashboardData()
                    pageType = 2
                }
            }

            override fun onFailure(call: Call<PreferenceResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                loadDashboardData()
                pageType = 2
            }
        })

    }


    //Company functions

    fun followCompany(
        id: Int,
        btnJoinGroup: Button,
        btnJoined: Button,
        status: String,
        name: String
    ) {

        Logger.d("CODE", id.toString() + "")

        val params = HashMap<String, String>()

        params["entity_id"] = id.toString()
        params["entity_type"] = "company"
        params["status"] = status

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.FollowCompayny(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )

        call.enqueue(object : Callback<UpdateReplyResponse> {
            override fun onResponse(
                call: Call<UpdateReplyResponse>,
                response: Response<UpdateReplyResponse>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE follow company", "" + Gson().toJson(response))

                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG)
                        .show()
                    if (status.compareTo("follow") == 0) {
                        btnJoinGroup.visibility = View.GONE
                        btnJoined.visibility = View.VISIBLE
                        loadRecommendedCompanies("1")
                        loadFollowedCompanies("1")

                        Snackbar.make(
                            main,
                            "You have successfully followed '" + name + "'",
                            Snackbar.LENGTH_SHORT
                        ).show()

                    } else {
                        btnJoinGroup.visibility = View.VISIBLE
                        btnJoined.visibility = View.GONE

                        loadFollowedCompanies("1")
                        loadRecommendedCompanies("1")
                        Snackbar.make(
                            main,
                            "You have successfully unfollowed '" + name + "'",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }


                } else {
                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())


                }
            }

            override fun onFailure(call: Call<UpdateReplyResponse>, t: Throwable) {
                Logger.d("TAGG", "Apply Job FAILED : $t")
            }
        })
    }

    fun unfollowProfile(id: Int, name: String) {

        Logger.d("CODE", id.toString() + "")

        val params = HashMap<String, String>()

        params["user_follower_id"] = id.toString()

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.unFollowProfile(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )

        call.enqueue(object : Callback<UpdateReplyResponse> {
            override fun onResponse(
                call: Call<UpdateReplyResponse>,
                response: Response<UpdateReplyResponse>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE follow company", "" + Gson().toJson(response))

                if (response.isSuccessful) {
                    //Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                    Snackbar.make(
                        main,
                        "You have succesfully unfollowed " + name,
                        Snackbar.LENGTH_SHORT
                    ).show()

                } else {
                    //ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                    Snackbar.make(
                        main,
                        "You have succesfully unfollowed " + name,
                        Snackbar.LENGTH_SHORT
                    ).show()

                }
                following_id_layout.callOnClick()
            }

            override fun onFailure(call: Call<UpdateReplyResponse>, t: Throwable) {
                Logger.d("TAGG", "Apply Job FAILED : $t")
                Snackbar.make(
                    main,
                    "You have succesfullyunfollowed " + name,
                    Snackbar.LENGTH_SHORT
                ).show()
                following_id_layout.callOnClick()
            }
        })
    }

    public fun openBottomSheetChangeName(type: String) {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.bottom_sheet_changename)
        val changename_text = dialog.findViewById(R.id.changename_text) as EditText
        val changename = dialog.findViewById(R.id.changename) as TextView
        if (type.compareTo("name") == 0)
            changename.setText("Update Name")
        else if (type.compareTo("phone") == 0)
            changename.setText("Update Phone")
        else
            changename.setText("Update Email")

        changename.setOnClickListener {
            dialog.cancel()
        }

        val cancel_changename = dialog.findViewById(R.id.cancel_changename) as Button
        cancel_changename.setOnClickListener {
            dialog.cancel()
        }
        val save_changename = dialog.findViewById(R.id.save_changename) as Button
        save_changename.setOnClickListener {
            if (changename_text.text.trim().toString().length > 0) {
                if (type.compareTo("name") == 0) {
                    updateName(changename_text.text.trim().toString())
                } else if (type.compareTo("phone") == 0) {
                    updatePhone(changename_text.text.trim().toString())
                }
                dialog.cancel()
            } else {
                Toast.makeText(applicationContext, "Please enter the value", Toast.LENGTH_LONG)
                    .show()
            }
        }

        var window: Window = dialog.getWindow()!!;
        window.setBackgroundDrawable(null);
        var wlp: WindowManager.LayoutParams = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog.show()
    }

    fun updateName(name: String) {

        val params = HashMap<String, String>()

        params["username"] = name

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.UpdateName(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )

        call.enqueue(object : Callback<AccountSettingsBody> {
            override fun onResponse(
                call: Call<AccountSettingsBody>,
                response: Response<AccountSettingsBody>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful) {
                    fullname.setText(name)
                    ToastHelper.makeToast(applicationContext, "Name Updated")
                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid ")
                }
            }

            override fun onFailure(call: Call<AccountSettingsBody>, t: Throwable) {

                Logger.d("TAGG", "Join Group FAILED : $t")

            }
        })

    }

    fun deleteEmail() {

        val params = HashMap<String, String>()

        params["delete_secondary"] = "true"

        retrofitInterface1 = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface1!!.UpdateName(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )

        call.enqueue(object : Callback<AccountSettingsBody> {
            override fun onResponse(
                call: Call<AccountSettingsBody>,
                response: Response<AccountSettingsBody>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "DELETE EMAIL" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful) {
                    secemail.setText("")
                    secemailClick.visibility = View.GONE
                    secemail_layout.visibility = View.GONE
                    secemailAdd.visibility = View.VISIBLE
                    ToastHelper.makeToast(applicationContext, "Email deleted")
                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid ")
                }
            }

            override fun onFailure(call: Call<AccountSettingsBody>, t: Throwable) {

                Logger.d("TAGG", " FAILED : $t")

            }
        })

    }

    public fun openBottomSheetChangePassword() {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.bottom_sheet_changepassword)

        val new_pass = dialog.findViewById(R.id.new_pass) as EditText
        val conf_pass = dialog.findViewById(R.id.conf_pass) as EditText
        val pass = dialog.findViewById(R.id.pass) as TextView
        pass.setOnClickListener {
            dialog.cancel()
        }

        awesomeValidation = AwesomeValidation(ValidationStyle.BASIC);
        val regexPassword = ".{6,}"
        awesomeValidation!!.addValidation(
            new_pass,
            regexPassword,
            "Password must be atleast 6 characters"
        )
//        awesomeValidation!!.addValidation(new_pass, conf_pass, "Password must be same")
        awesomeValidation!!.addValidation(
            conf_pass,
            regexPassword,
            "Password must be atleast 6 characters"
        )
        val save_pass = dialog.findViewById(R.id.save_pass) as Button
        save_pass.setOnClickListener {
            //Log.d("TAGG",new_pass.text.toString()+",,"+conf_pass.text.toString())
            if (awesomeValidation!!.validate()) {
                changePassword(
                    HelperMethods.md5(new_pass.text.toString()),
                    HelperMethods.md5(conf_pass.text.toString())
                )
                dialog.cancel()
            }

        }


        var window: Window = dialog.getWindow()!!;
        window.setBackgroundDrawable(null);
        var wlp: WindowManager.LayoutParams = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog.show()
    }

    public fun openBottomSheetAddSecondaryEmail() {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.bottom_sheet_secondaryemail)
        val secemail_text = dialog.findViewById(R.id.secemail_text) as EditText
        val secemail = dialog.findViewById(R.id.secemail) as TextView
        secemail.setOnClickListener {
            dialog.cancel()
        }

        val cancel_secemail = dialog.findViewById(R.id.cancel_secemail) as Button
        cancel_secemail.setOnClickListener {
            dialog.cancel()
        }
        val save_secemail = dialog.findViewById(R.id.save_secemail) as Button
        save_secemail.setOnClickListener {
            if (secemail_text.text.trim().toString().length > 0) {
                updateSecEmail(
                    secemail_text.text.trim().toString()
                )
                dialog.cancel()
            } else {
                Toast.makeText(applicationContext, "Please enter all the values", Toast.LENGTH_LONG)
                    .show()
            }
        }


        var window: Window = dialog.getWindow()!!;
        window.setBackgroundDrawable(null);
        var wlp: WindowManager.LayoutParams = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog.show()
    }

    fun changePassword(new: String, old: String) {

        val params = HashMap<String, String>()

        params["old_password"] = old
        params["new_password"] = new

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.resetPassword(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )

        call.enqueue(object : Callback<AccountSettingsBody> {
            override fun onResponse(
                call: Call<AccountSettingsBody>,
                response: Response<AccountSettingsBody>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    ToastHelper.makeToast(applicationContext, "Password reset!! Please login again")
                    val sharedPref: SharedPreferences = getSharedPreferences(
                        "mysettings",
                        Context.MODE_PRIVATE
                    )
                    sharedPref.edit().clear().commit();
                    val intent = Intent(applicationContext, SplashActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid ")
                }
            }

            override fun onFailure(call: Call<AccountSettingsBody>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")

            }
        })

    }

    fun deleteAccount() {

//        val params = HashMap<String, String>()
//        params["make_primary "] = "1"

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.DeleteAccount(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN
        )

        call.enqueue(object : Callback<AccountSettingsBody> {
            override fun onResponse(
                call: Call<AccountSettingsBody>,
                response: Response<AccountSettingsBody>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful) {
//                    secemail.setText(name)
//                    secemailClick.visibility = View.VISIBLE
//                    secemail_layout.visibility = View.VISIBLE
//                    secemailAdd.visibility = View.GONE
                    ToastHelper.makeToast(applicationContext, "Account deactivated")
                    val sharedPref: SharedPreferences = getSharedPreferences(
                        "mysettings",
                        Context.MODE_PRIVATE
                    )
                    sharedPref.edit().clear().commit();
                    val intent = Intent(applicationContext, SplashActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid ")
                }
            }

            override fun onFailure(call: Call<AccountSettingsBody>, t: Throwable) {

                Logger.d("TAGG", "Join Group FAILED : $t")

            }
        })

    }

    fun makeprimaryEmail() {

        val params = HashMap<String, String>()

        params["make_primary "] = "1"

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.UpdateName(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )

        call.enqueue(object : Callback<AccountSettingsBody> {
            override fun onResponse(
                call: Call<AccountSettingsBody>,
                response: Response<AccountSettingsBody>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful) {
//                    secemail.setText(name)
//                    secemailClick.visibility = View.VISIBLE
//                    secemail_layout.visibility = View.VISIBLE
//                    secemailAdd.visibility = View.GONE
                    ToastHelper.makeToast(
                        applicationContext,
                        "Please login with your primary email id"
                    )
                    val sharedPref: SharedPreferences = getSharedPreferences(
                        "mysettings",
                        Context.MODE_PRIVATE
                    )
                    sharedPref.edit().clear().commit();
                    val intent = Intent(applicationContext, SplashActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid ")
                }
            }

            override fun onFailure(call: Call<AccountSettingsBody>, t: Throwable) {

                Logger.d("TAGG", "Join Group FAILED : $t")

            }
        })

    }

    fun updateSecEmail(name: String) {

        val params = HashMap<String, String>()

        params["secondary_email"] = name

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.UpdateName(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )

        call.enqueue(object : Callback<AccountSettingsBody> {
            override fun onResponse(
                call: Call<AccountSettingsBody>,
                response: Response<AccountSettingsBody>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful) {
                    secemail.setText(name)
                    secemailClick.visibility = View.VISIBLE
                    secemail_layout.visibility = View.VISIBLE
                    secemailAdd.visibility = View.GONE
                    ToastHelper.makeToast(applicationContext, "Email Added")
                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid ")
                }
            }

            override fun onFailure(call: Call<AccountSettingsBody>, t: Throwable) {

                Logger.d("TAGG", "Join Group FAILED : $t")

            }
        })

    }

    fun sendVerifyEmail(email: String, id: Int) {

        val params = HashMap<String, String>()

        params["secondary_email"] = email
        params["user_id"] = id.toString()
        params["role"] = "user"
        Log.d("TAGG", params.toString())
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.sendVerifyEmail(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )

        call.enqueue(object : Callback<AccountSettingsBody> {
            override fun onResponse(
                call: Call<AccountSettingsBody>,
                response: Response<AccountSettingsBody>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful) {
                    ToastHelper.makeToast(
                        applicationContext,
                        "Verification mail has been sent to your primary email id. Please verify it."
                    )
                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid ")
                }
            }

            override fun onFailure(call: Call<AccountSettingsBody>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")

            }
        })

    }

    fun updatePhone(phone: String) {

        val params = HashMap<String, String>()

        params["phone_no"] = phone

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.UpdateName(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )

        call.enqueue(object : Callback<AccountSettingsBody> {
            override fun onResponse(
                call: Call<AccountSettingsBody>,
                response: Response<AccountSettingsBody>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful) {
                    mobile.setText(phone)
                    ToastHelper.makeToast(applicationContext, "Phone Number updated")
                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid ")
                }
            }

            override fun onFailure(call: Call<AccountSettingsBody>, t: Throwable) {

                Logger.d("TAGG", "Join Group FAILED : $t")

            }
        })

    }

    fun sendRequestOTP(mobile: String, id: Int) {

        val params = HashMap<String, String>()

        params["user_id"] = id.toString()
        params["phone_no"] = mobile

        Log.d("TAGG", params.toString())
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.requestOTP(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )

        call.enqueue(object : Callback<AccountSettingsDetails> {
            override fun onResponse(
                call: Call<AccountSettingsDetails>,
                response: Response<AccountSettingsDetails>
            ) {


                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                Logger.d("RESPONSE join group", "" + str_response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )

                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                    val jsonarray_info: JSONObject = jsonObject1.optJSONObject("body")

                    if (response.isSuccessful) {

                        val a: String = jsonarray_info.optString("verification_code")
                        ToastHelper.makeToast(applicationContext, "OTP is been sent")

                        requestOTP(mobile, id, a)
                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid ")
                    }
                }
            }

            override fun onFailure(call: Call<AccountSettingsDetails>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")

            }
        })

    }

    fun requestOTPValue() {

        //Your authentication key
        val authkey: String = "107233ArlxzfJZ256e242ed"
        //Multiple mobiles numbers separated by comma
        val mobiles: String = "8792119844"
        //Sender ID,While using route4 sender id should be 6 characters long.
        val senderId: String = "MSGIND"
        //Your message to send, Add URL encoding here.
        val message: String = "Test message"
        //define route
        val route: String = "default";

        var myURLConnection: URLConnection
        var myURL: URL
        var reader: BufferedReader

        //encoding message
        val encoded_message: String = URLEncoder.encode(message);

        //Send SMS API
        var mainUrl: String = "http://api.msg91.com/api/sendhttp.php?";

        //Prepare parameter string
        val sbPostData: StringBuilder = StringBuilder(mainUrl);
        sbPostData.append("authkey=" + authkey);
        sbPostData.append("&mobiles=" + mobiles);
        sbPostData.append("&message=" + encoded_message);
        sbPostData.append("&route=" + route);
        sbPostData.append("&sender=" + senderId);

        //final string
        mainUrl = sbPostData.toString();
        try {
            //prepare connection
            myURL = URL(mainUrl);
            myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            reader = BufferedReader(InputStreamReader(myURLConnection.getInputStream()));

            //reading response
            var response: String = ""
            while ((reader.readLine()) != null)
                response = reader.readLine()
            //print response
            Log.d("RESPONSE", "" + response);

            //finally close connection
            reader.close();
        } catch (e: IOException) {
            e.printStackTrace();
        }
    }

    fun requestOTP(mobile: String, id: Int, otp: String) {


//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//            mobile,                     // Phone number to verify
//            60,                           // Timeout duration
//            TimeUnit.SECONDS,                // Unit of timeout
//            ZActivityDashboard.this,        // Activity (for callback binding)
//            mCallback);                      // OnVerificationStateChangedCallbacks
//         SendOTPConfigBuilder()
//                .setCountryCode(91)
//                .setMobileNumber(mobile)
//                //////////////////direct verification while connect with mobile network/////////////////////////
//                .setVerifyWithoutOtp(true)
//                //////////////////Auto read otp from Sms And Verify///////////////////////////
//                .setAutoVerification(this)
//                .setOtpExpireInMinute(5)//default value is one day
//                .setOtpHits(3) //number of otp request per number
//                .setOtpHitsTimeOut(0L)//number of otp request time out reset in milliseconds default is 24 hours
//                .setSenderId("ABCDEF")
//             .setOtp(Integer.parseInt(otp))
//                .setMessage(otp+" is Your verification digits.")
//                .setOtpLength(5).build();
//        SendOTP.getInstance().getTrigger().initiate();

        val params = HashMap<String, String>()

        //params["user_id"] = id.toString()
        params["mobile"] = mobile
        params["otp"] = otp
        params["version"] = "1.0"
        params["clientname"] = "App"

//        val jsonObj_ = JsonObject()
//        jsonObj_.addProperty("mobile", "8792119844")
//        jsonObj_.addProperty("otp", otp)
//        jsonObj_.addProperty("version", "App")
//        jsonObj_.addProperty("clientname", "App")

//        Log.d("TAGG", params.toString())
//
//        val retrofit: Retrofit = Retrofit.Builder()
//            .baseUrl("http://www.workingnaari.in/") //"http://dev.jobsforher.com/"
//            .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
//            .build();
//
//        val pas:String = "jFhUser:c@ndid@teRegister@pi"
//        val data = pas.toByteArray(charset("UTF-8"))
//        val base64 = Base64.encodeToString(data, Base64.NO_WRAP)
//
//        retrofitInterface = retrofit.create(RetrofitInterface::class.java)
//
//        val call = retrofitInterface!!.requestpartyOTP("akZoVXNlcjpjQG5kaWRAdGVSZWdpc3RlckBwaQ==",params)
//
//        call.enqueue(object : Callback<AccountSettingsDetails> {
//            override fun onResponse(call: Call<AccountSettingsDetails>, response: Response<AccountSettingsDetails>) {
//
//                Logger.d("CODE", response.code().toString() + "")
//                Logger.d("MESSAGE", response.message() + "")
//                Logger.d("URL", "" + response)
//                Logger.d("RESPONSE OTP", "" + Gson().toJson(response))
//
//                if (response.isSuccessful) {
//
////                    val a:String = jsonarray_info.optString("verification_code")
//                    ToastHelper.makeToast(applicationContext, "OTP is been sent")
//
//                } else {
//                    ToastHelper.makeToast(applicationContext, "Invalid ")
//                }
//            }
//
//            override fun onFailure(call: Call<AccountSettingsDetails>, t: Throwable) {
//
//                Logger.d("TAGG", "FAILED : $t")
//
//            }
//        })


        val call = BasicAuthClient<DemoRemoteService>().create(DemoRemoteService::class.java)
            .requestpartyOTP(params)

        call.enqueue(object : Callback<AccountSettingsDetails> {
            override fun onFailure(call: Call<AccountSettingsDetails>, t: Throwable) {
                //Log.e("DemoClass", t.message, t)
            }

            override fun onResponse(
                call: Call<AccountSettingsDetails>,
                response: Response<AccountSettingsDetails>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE OTP", "" + Gson().toJson(response))

                if (response.isSuccessful) {
                    ToastHelper.makeToast(applicationContext, "OTP is been sent")
                    openBottomSheetOTP()
                } else {
                    //Log.e("DemoClass", "Error: ${response.code()} ${response.message()}")
                }
            }
        })


    }

    public fun openBottomSheetOTP() {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.bottom_sheet_otp)
        val secemail_text = dialog.findViewById(R.id.secemail_text) as EditText

        val save_secemail = dialog.findViewById(R.id.save_secemail) as Button
        save_secemail.setOnClickListener {
            if (secemail_text.text.trim().toString().length > 0) {
//                updateSecEmail(
//                    secemail_text.text.trim().toString()
//                )
                Toast.makeText(applicationContext, "Verified phone number", Toast.LENGTH_LONG)
                    .show()
                dialog.cancel()
            } else {
                Toast.makeText(applicationContext, "Please enter all the values", Toast.LENGTH_LONG)
                    .show()
            }
        }


        var window: Window = dialog.getWindow()!!;
        window.setBackgroundDrawable(null);
        var wlp: WindowManager.LayoutParams = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog.show()
    }


    public fun openEditProfile() {

        val dialog = Dialog(this, R.style.AppTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_profileedit)
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        var selectedItemId: Int = 0
        val lifestageVal = dialog.findViewById(R.id.edittext_leftstages) as EditText
        val fullnameVal = dialog.findViewById(R.id.edittext_fullname) as EditText
        val degreeVal = dialog.findViewById(R.id.edittext_degree) as EditText
        val universityVal = dialog.findViewById(R.id.edittext_university) as EditText
        val whycareerbreakVal = dialog.findViewById(R.id.edittext_whycareerbreak) as EditText
        val designationVal = dialog.findViewById(R.id.edittext_designation) as EditText
        val companyVal = dialog.findViewById(R.id.edittext_company) as EditText
        val locationVal = dialog.findViewById(R.id.edittext_location) as EditText
        val aboutmeVal = dialog.findViewById(R.id.edittext_aboutme) as EditText
        val saveTop = dialog.findViewById(R.id.saveTop) as TextView

        val lifestage = dialog.findViewById(R.id.lifestages) as LinearLayout
        val fullname = dialog.findViewById(R.id.fullname) as LinearLayout
        val degree = dialog.findViewById(R.id.degree) as LinearLayout
        val university = dialog.findViewById(R.id.university) as LinearLayout
        val whycareerbreak = dialog.findViewById(R.id.whyimoncareerbreak) as LinearLayout
        val designation = dialog.findViewById(R.id.designation) as LinearLayout
        val company = dialog.findViewById(R.id.company) as LinearLayout
        val location = dialog.findViewById(R.id.location) as LinearLayout
        val aboutme = dialog.findViewById(R.id.aboutme) as LinearLayout
        val savecontinue = dialog.findViewById(R.id.savecontinue) as Button
        val editprofile_back = dialog.findViewById(R.id.editprofile_back) as ImageView

        fullnameVal.setOnClickListener {
            Toast.makeText(dialog.context, "Name is not editable", Toast.LENGTH_SHORT).show()
        }

        editprofile_back.setOnClickListener {
            dialog.cancel()
        }

        val params = HashMap<String, String>()
        val spinnerlifestage = dialog.findViewById(R.id.spinner1) as Spinner
        val languages = arrayOf("Riser", "Restarter", "Starter")
        //val adapter = ArrayAdapter(this, android.R.layout.select_dialog_singlechoice, languages)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerlifestage.setAdapter(adapter)
//        spinnerlifestage.setOnItemSelectedListener(OnSpinnerItemClicked())

        spinnerlifestage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                selectedItemId = spinnerlifestage.selectedItemPosition
                // Notify the selected item text
                Log.d("TAGG", "Val is" + adapter.getItem(position))

                lifestageVal.setText(adapter.getItem(position).toString())

                if (adapter.getItem(position).toString().equals("Riser")) {
                    lifestage.visibility = View.VISIBLE
                    fullname.visibility = View.VISIBLE
                    degree.visibility = View.GONE
                    university.visibility = View.GONE
                    whycareerbreak.visibility = View.GONE
                    designation.visibility = View.VISIBLE
                    company.visibility = View.VISIBLE
                    location.visibility = View.VISIBLE
                    aboutme.visibility = View.VISIBLE
                    selectedItemId = 0
                } else if (adapter.getItem(position).toString().equals("Restarter")) {
                    lifestage.visibility = View.VISIBLE
                    fullname.visibility = View.VISIBLE
                    degree.visibility = View.GONE
                    university.visibility = View.GONE
                    whycareerbreak.visibility = View.VISIBLE
                    designation.visibility = View.VISIBLE
                    company.visibility = View.VISIBLE
                    location.visibility = View.VISIBLE
                    aboutme.visibility = View.VISIBLE
                    selectedItemId = 1
                } else if (adapter.getItem(position).toString().equals("Starter")) {
                    lifestage.visibility = View.VISIBLE
                    fullname.visibility = View.VISIBLE
                    degree.visibility = View.VISIBLE
                    university.visibility = View.VISIBLE
                    whycareerbreak.visibility = View.GONE
                    designation.visibility = View.GONE
                    company.visibility = View.GONE
                    location.visibility = View.VISIBLE
                    aboutme.visibility = View.VISIBLE
                    selectedItemId = 2
                }

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                Log.d("TAGG", "HIII")
            }
        }

        for (i in 0 until lifestage.getChildCount()) {
            val view = lifestage.getChildAt(i)
            //view.setEnabled(false)
            view.isFocusable = false
            view.alpha = 0.5f
        }

        lifestageVal.setOnClickListener {
            if (lifestageVal.isFocusable) {
                spinnerlifestage.performClick()
            } else {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Are you sure you want to change your life stages?")
                builder.setMessage("Changing of life stage will change your profile appearance on your public profile+. Past life stage data will be discarded")
                //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                    //                    Toast.makeText(
//                        applicationContext,
//                        android.R.string.yes, Toast.LENGTH_SHORT
//                    ).show()
                    for (i in 0 until lifestage.getChildCount()) {
                        val view = lifestage.getChildAt(i)
                        //view.setEnabled(true) // Or whatever you want to do with the view.
                        view.isFocusable = true
                        view.alpha = 1f
                    }

                }

                builder.setNegativeButton(android.R.string.no) { dialog, which ->
                    //                    Toast.makeText(
//                        applicationContext,
//                        android.R.string.no, Toast.LENGTH_SHORT
//                    ).show()
                    for (i in 0 until lifestage.getChildCount()) {
                        val view = lifestage.getChildAt(i)
                        //view.setEnabled(false) // Or whatever you want to do with the view.
                        view.isFocusable = false
                        view.alpha = 0.5f
                    }
                }
                builder.show()
            }
        }

        saveTop.setOnClickListener {
            savecontinue.callOnClick()
        }

        savecontinue.setOnClickListener {
            if (selectedItemId == 2) {
//            if(listOfProfiledata[0].stage_type!!.toLowerCase().equals("starter") ||
//                    lifestageVal.text.toString().toLowerCase().equals("starter")) {
                awesomeValidation_starter = AwesomeValidation(ValidationStyle.BASIC);
                awesomeValidation_starter!!.addValidation(
                    degreeVal,
                    RegexTemplate.NOT_EMPTY,
                    "Please enter the degree"
                )
                awesomeValidation_starter!!.addValidation(
                    locationVal,
                    RegexTemplate.NOT_EMPTY,
                    "Please enter the location"
                );
                if (awesomeValidation_starter!!.validate()) {
                    params["stage_type"] = lifestageVal.text.toString().toLowerCase()
                    params["username"] = fullnameVal.text.toString()
                    params["title"] = degreeVal.text.toString()
                    params["organization"] = universityVal.text.toString()
                    params["location"] = locationVal.text.toString()
                    params["description"] = aboutmeVal.text.toString()
                    Log.d("TAGG", "PARAMS" + params)
                    updateProfileData(params)
                    dialog.cancel()
                }
            } else if (selectedItemId == 1) {
//                if(listOfProfiledata[0].stage_type!!.toLowerCase().equals("restarter") ||
//                lifestageVal.text.toString().toLowerCase().equals("restarter")){
                awesomeValidation_restarter = AwesomeValidation(ValidationStyle.BASIC);
                awesomeValidation_restarter!!.addValidation(
                    whycareerbreakVal,
                    RegexTemplate.NOT_EMPTY,
                    "Please enter the career break"
                )
                awesomeValidation_restarter!!.addValidation(
                    locationVal,
                    RegexTemplate.NOT_EMPTY,
                    "Please enter the location"
                );
                if (awesomeValidation_restarter!!.validate()) {
                    params["stage_type"] = lifestageVal.text.toString().toLowerCase()
                    params["username"] = fullnameVal.text.toString()
                    params["caption"] = whycareerbreakVal.text.toString()
                    params["title"] = designationVal.text.toString()
                    params["organization"] = companyVal.getText().toString()
                    params["location"] = locationVal.text.toString()
                    params["description"] = aboutmeVal.text.toString()
                    Log.d("TAGG", "PARAMS" + params)
                    updateProfileData(params)
                    dialog.cancel()
                }

            } else if (selectedItemId == 0) {
//                if(listOfProfiledata[0].stage_type!!.toLowerCase().equals("riser")||
//                lifestageVal.text.toString().toLowerCase().equals("riser")){
                awesomeValidation_riser = AwesomeValidation(ValidationStyle.BASIC);
                awesomeValidation_riser!!.addValidation(
                    designationVal,
                    RegexTemplate.NOT_EMPTY,
                    "Please enter the designation"
                )
                awesomeValidation_riser!!.addValidation(
                    locationVal,
                    RegexTemplate.NOT_EMPTY,
                    "Please enter the location"
                );
                if (awesomeValidation_riser!!.validate()) {
                    params["stage_type"] = lifestageVal.text.toString().toLowerCase()
                    params["username"] = fullnameVal.text.toString()
                    params["title"] = designationVal.text.toString()
                    params["organization"] = companyVal.text.toString()
                    params["location"] = locationVal.text.toString()
                    params["description"] = aboutmeVal.text.toString()
                    Log.d("TAGG", "PARAMS" + params)
                    updateProfileData(params)
                    dialog.cancel()
                }
            }


        }

        Log.d("TAGG", listOfProfiledata1[0].stage_type!!.toLowerCase())

        if (listOfProfiledata1[0].stage_type!!.toLowerCase().equals("starter")) {
            lifestage.visibility = View.VISIBLE
            fullname.visibility = View.VISIBLE
            degree.visibility = View.VISIBLE
            university.visibility = View.VISIBLE
            whycareerbreak.visibility = View.GONE
            designation.visibility = View.GONE
            company.visibility = View.GONE
            location.visibility = View.VISIBLE
            aboutme.visibility = View.VISIBLE

            lifestageVal.setText(listOfProfiledata1[0].stage_type!!.toLowerCase())
            fullnameVal.setText(listOfProfiledata1[0].username)
            degreeVal.setText(listOfProfiledata1[0].title)
            universityVal.setText(listOfProfiledata1[0].organization)
            locationVal.setText(listOfProfiledata1[0].location)
            aboutmeVal.setText(listOfProfiledata1[0].description)
            spinnerlifestage.setSelection(2)
            selectedItemId = 2

        } else if (listOfProfiledata1[0].stage_type!!.toLowerCase().equals("restarter")) {
            lifestage.visibility = View.VISIBLE
            fullname.visibility = View.VISIBLE
            degree.visibility = View.GONE
            university.visibility = View.GONE
            whycareerbreak.visibility = View.VISIBLE
            designation.visibility = View.VISIBLE
            company.visibility = View.VISIBLE
            location.visibility = View.VISIBLE
            aboutme.visibility = View.VISIBLE

            lifestageVal.setText(listOfProfiledata1[0].stage_type!!.toLowerCase())
            fullnameVal.setText(listOfProfiledata1[0].username)
            whycareerbreakVal.setText(listOfProfiledata1[0].caption)
            designationVal.setText(listOfProfiledata1[0].title)
            companyVal.setText(listOfProfiledata1[0].organization)
            locationVal.setText(listOfProfiledata1[0].location)
            aboutmeVal.setText(listOfProfiledata1[0].description)
            spinnerlifestage.setSelection(1)
            selectedItemId = 1
        } else if (listOfProfiledata1[0].stage_type!!.toLowerCase().equals("riser")) {
            lifestage.visibility = View.VISIBLE
            fullname.visibility = View.VISIBLE
            degree.visibility = View.GONE
            university.visibility = View.GONE
            whycareerbreak.visibility = View.GONE
            designation.visibility = View.VISIBLE
            company.visibility = View.VISIBLE
            location.visibility = View.VISIBLE
            aboutme.visibility = View.VISIBLE

            lifestageVal.setText(listOfProfiledata1[0].stage_type!!.toLowerCase())
            fullnameVal.setText(listOfProfiledata1[0].username)
            designationVal.setText(listOfProfiledata1[0].title)
            companyVal.setText(listOfProfiledata1[0].organization)
            locationVal.setText(listOfProfiledata1[0].location)
            aboutmeVal.setText(listOfProfiledata1[0].description)
            spinnerlifestage.setSelection(0)
            selectedItemId = 0
        }

        var window: Window = dialog.getWindow()!!;
        var wlp: WindowManager.LayoutParams = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setAttributes(wlp);
        dialog.show()

    }

    fun updateProfileData(params: HashMap<String, String>) {

        val paramsVal = HashMap<String, String>()

        //paramsVal = params

        var model: CertificateModel = CertificateModel();
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.UpdateProfileData(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )

        Logger.d("URL", "" + "HI")
        call.enqueue(object : Callback<UpdateProfileResponse> {
            override fun onResponse(
                call: Call<UpdateProfileResponse>,
                response: Response<UpdateProfileResponse>
            ) {
                Logger.d("URL", "" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))

                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)

                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )

                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                    val responseCode: Int = jsonObject1.optInt("response_code")
                    val message: String = jsonObject1.optString("message")

                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Update Success!!", Toast.LENGTH_LONG)
                            .show()
                    } else {
//                    ToastHelper.makeToast(applicationContext, "message")
                    }
                    listOfProfiledata.clear()
                    loadDashboardData()
                }
            }

            override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext, "Update Failed!!", Toast.LENGTH_LONG).show()

            }
        })
    }

    fun loadCategoryData() {

        listOfCategories.clear()
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getCategories(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN
        )
        call.enqueue(object : Callback<GetCategoryResponse> {

            override fun onResponse(
                call: Call<GetCategoryResponse>,
                response: Response<GetCategoryResponse>
            ) {

                Log.d("TAG", "CODE" + response.code().toString() + "")
                Log.d("TAG", "MESSAGE" + response.message() + "")
                Log.d("TAG", "RESPONSE" + "" + Gson().toJson(response))
                Log.d("TAG", "URL" + "" + response)
                //var str_response = Gson().toJson(response)
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                    val responseCode: Int = jsonObject1.optInt("response_code")
                    val message: String = jsonObject1.optString("message")
                    var jsonarray: JSONArray = jsonObject1.optJSONArray("body")

                    if (response.isSuccessful) {
                        for (i in 0 until response.body()!!.body!!.size) {
                            var json_objectdetail: JSONObject = jsonarray.optJSONObject(i)
                            val model: CategoryView = CategoryView();
                            model.id = json_objectdetail.optInt("id")
                            model.name = json_objectdetail.optString("name")
                            model.image_url = json_objectdetail.optString("image_url")
                            model.status = json_objectdetail.optString("status")
                            model.created_on = json_objectdetail.optString("created_on")
                            model.modified_on = json_objectdetail.optString("modified_on")
                            listOfCategories.add(model)
                        }
                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid")
                    }

                    Log.d("TAG", "DATA:" + listOfCategories.size)
                    loadCityData()
//                    openBottomSheetFilter()
                } else
                    Toast.makeText(applicationContext, "No data exists", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<GetCategoryResponse>, t: Throwable) {
                Logger.d("TAG", "FAILED : $t")
            }
        })


    }

    fun loadCityData() {

        listOfCities.clear()
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call =
            retrofitInterface!!.getCities(EndPoints.CLIENT_ID, "Bearer " + EndPoints.ACCESS_TOKEN)
        call.enqueue(object : Callback<GetCityResponse> {

            override fun onResponse(
                call: Call<GetCityResponse>,
                response: Response<GetCityResponse>
            ) {

                Log.d("TAG", "CODE" + response.code().toString() + "")
                Log.d("TAG", "MESSAGE" + response.message() + "")
                Log.d("TAG", "RESPONSE" + "" + Gson().toJson(response))
                Log.d("TAG", "URL" + "" + response)
                //var str_response = Gson().toJson(response)
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                    val responseCode: Int = jsonObject1.optInt("response_code")
                    val message: String = jsonObject1.optString("message")
                    var jsonarray: JSONArray = jsonObject1.optJSONArray("body")

                    if (response.isSuccessful) {
                        for (i in 0 until response.body()!!.body!!.size) {
                            var json_objectdetail: JSONObject = jsonarray.optJSONObject(i)
                            val model: CityView = CityView();
                            model.id = json_objectdetail.optInt("id")
                            model.name = json_objectdetail.optString("label")
                            listOfCities.add(model)
                        }
                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid")
                    }
                    addJobType()
                } else
                    Toast.makeText(applicationContext, "No data exists", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<GetCityResponse>, t: Throwable) {
                Logger.d("TAG", "FAILED : $t")
            }
        })
    }

    fun addJobType() {

        listOfJobType.clear()
        listOfJobType.add(JobTypeView("Full Time", "@drawable/ic_clock"))
        listOfJobType.add(JobTypeView("Part Time", "@drawable/ic_clock"))
        listOfJobType.add(JobTypeView("Work From Home", "@drawable/ic_house"))
        listOfJobType.add(JobTypeView("Returnee Program", "@drawable/ic_clock"))
        listOfJobType.add(JobTypeView("Freelance/Projects", "@drawable/ic_volunteer"))
        listOfJobType.add(JobTypeView("Volunteer", "@drawable/ic_freelance"))
        addJobFunctionalArea()
    }

    fun addJobFunctionalArea() {
        listOfJobFArea.clear()
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getFunctionalArea(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN
        )
        call.enqueue(object : Callback<GetMobilityResponse> {

            override fun onResponse(
                call: Call<GetMobilityResponse>,
                response: Response<GetMobilityResponse>
            ) {

                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                    val responseCode: Int = jsonObject1.optInt("response_code")
                    val message: String = jsonObject1.optString("message")
                    var jsonarray: JSONArray = jsonObject1.optJSONArray("body")

                    if (response.isSuccessful) {
                        for (i in 0 until response.body()!!.body!!.size) {
                            var json_objectdetail: JSONObject = jsonarray.optJSONObject(i)
                            val model: FunctionalAreaView = FunctionalAreaView();
                            //model.id = json_objectdetail.optInt("id")
                            model.name = json_objectdetail.optString("name")
                            listOfJobFArea.add(model)
                        }
                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid")
                    }
                } else
                    Toast.makeText(applicationContext, "No data exists", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<GetMobilityResponse>, t: Throwable) {
                Logger.d("TAG", "FAILED : $t")
            }
        })
        Collections.sort(listOfJobFArea, object : Comparator<FunctionalAreaView> {
            override fun compare(lhs: FunctionalAreaView, rhs: FunctionalAreaView): Int {
                return lhs.name!!.compareTo(rhs.name!!)
            }
        })
//        listOfJobFAreaSorted = listOfJobFArea.sortedWith(compareBy({ it.customProperty }))
//        for (obj in listOfJobFAreaSorted) {
//            println(obj.customProperty)
//        }
        val functionalareaList: ArrayList<String> = ArrayList()

        for (model in listOfJobFArea) {
            functionalareaList.add(model.name!!.toString())
        }

        val adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, functionalareaList)
//        functionalarea.setAdapter(adapter)
//        functionalarea.setThreshold(1)
//        functionalarea.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        addJobIndustry()

    }

    fun addJobIndustry() {

        listOfJobIndustry.clear()
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call =
            retrofitInterface!!.getIndustry(EndPoints.CLIENT_ID, "Bearer " + EndPoints.ACCESS_TOKEN)
        call.enqueue(object : Callback<GetMobilityResponse> {

            override fun onResponse(
                call: Call<GetMobilityResponse>,
                response: Response<GetMobilityResponse>
            ) {

                Log.d("TAG", "CODE" + response.code().toString() + "")
                Log.d("TAG", "MESSAGE" + response.message() + "")
                Log.d("TAG", "RESPONSE" + "" + Gson().toJson(response))
                Log.d("TAG", "URL" + "" + response)
                //var str_response = Gson().toJson(response)
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                    val responseCode: Int = jsonObject1.optInt("response_code")
                    val message: String = jsonObject1.optString("message")
                    var jsonarray: JSONArray = jsonObject1.optJSONArray("body")

                    if (response.isSuccessful) {
                        for (i in 0 until response.body()!!.body!!.size) {
                            var json_objectdetail: JSONObject = jsonarray.optJSONObject(i)
                            val model: FunctionalAreaView = FunctionalAreaView();
                            //model.id = json_objectdetail.optInt("id")
                            model.name = json_objectdetail.optString("name")
                            listOfJobIndustry.add(model)
                        }
                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid")
                    }
                } else
                    Toast.makeText(applicationContext, "No data exists", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<GetMobilityResponse>, t: Throwable) {
                Logger.d("TAG", "FAILED : $t")
            }
        })

        Collections.sort(listOfJobIndustry, object : Comparator<FunctionalAreaView> {
            override fun compare(lhs: FunctionalAreaView, rhs: FunctionalAreaView): Int {
                return lhs.name!!.compareTo(rhs.name!!)
            }
        })

        val industryList: ArrayList<String> = ArrayList()

        for (model in listOfJobIndustry) {
            industryList.add(model.name!!.toString())
        }

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, industryList)
//        industry.setAdapter(adapter)
//        industry.setThreshold(1)
//        industry.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        //openBottomSheetFilter()

        loadDashboardData()
    }


    //    Following List
    fun loadFollowingList() {

        followingheader.setText("Following - " + following_id.text.toString())

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getFollowingList(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN
        )
        call.enqueue(object : Callback<FollowingDetails> {

            override fun onResponse(
                call: Call<FollowingDetails>,
                response: Response<FollowingDetails>
            ) {

                Log.d("TAG", "CODE" + response.code().toString() + "")
                Log.d("TAG", "MESSAGE" + response.message() + "")
                Log.d("TAG", "RESPONSE" + "" + Gson().toJson(response))
                Log.d("TAG", "URL" + "" + response)
                //var str_response = Gson().toJson(response)
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                    val responseCode: Int = jsonObject1.optInt("response_code")
                    val message: String = jsonObject1.optString("message")
                    var jsonarray: JSONObject = jsonObject1!!.optJSONObject("body")
                    var json_arraydetail: JSONArray = jsonarray.optJSONArray("user_following")
                    var listOfFollowing: ArrayList<Following> = ArrayList()

                    if (response.isSuccessful) {
                        for (i in 0 until json_arraydetail!!.length()) {

                            var detail: JSONObject = json_arraydetail.optJSONObject(i)
                            val model: FollowingData = FollowingData();
                            val model_following = Following()
                            model_following.user_id = detail.optInt("user_id")
                            model_following.username = detail.optString("username")
                            model_following.stage_type = detail.optString("stage_type")
                            model_following.profile_url = detail.optString("profile_url")
                            model_following.profile_icon = detail.optString("profile_icon")
                            model_following.profile_image = detail.optString("profile_image")
                            model_following.company_name = detail.optString("company_name")

                            model.user_following = listOfFollowing!!

                            listOfFollowing.add(model_following)
                        }

                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                        mRecyclerViewFollowing!!.layoutManager =
                            mgroupsLayoutManager as RecyclerView.LayoutManager?
                        mAdapterGroups = FollowingAdapter(listOfFollowing, isLoggedIn)
                        mRecyclerViewFollowing!!.adapter = mAdapterGroups

                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid")
                    }

                    Log.d("TAG", "DATA:" + listOfFollowing.size)
                    if (listOfFollowing.size > 0)
                        default_following_companies.visibility = View.GONE
                    else
                        default_following_companies.visibility = View.VISIBLE
                } else {
                    Toast.makeText(applicationContext, "No data exists", Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<FollowingDetails>, t: Throwable) {
                Logger.d("TAG", "FAILED : $t")
            }
        })
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val navView: NavigationView = findViewById(R.id.nav_view)
        val m = navView.getMenu()

        when (item.itemId) {
            R.id.action_dashboard -> {
                val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
                drawerLayout.closeDrawer(GravityCompat.START)
                intent = Intent(applicationContext, ZActivityDashboard::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("isLoggedIn", true)
                startActivity(intent)
                closeDrawer()

            }
            R.id.action_groups -> {
                val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
                drawerLayout.closeDrawer(GravityCompat.START)
                intent = Intent(applicationContext, ZActivityGroups::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("isLoggedIn", true)
                startActivity(intent)
                closeDrawer()

            }
            R.id.action_jobs -> {
                val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
                drawerLayout.closeDrawer(GravityCompat.START)
                intent = Intent(applicationContext, ZActivityJobs::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("isLoggedIn", true)
                startActivity(intent)
                closeDrawer()
            }
            R.id.action_companies -> {
                val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
                drawerLayout.closeDrawer(GravityCompat.START)
                intent = Intent(applicationContext, ZActivityCompanies::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("isLoggedIn", true)
                startActivity(intent)
                closeDrawer()
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
                val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
                drawerLayout.closeDrawer(GravityCompat.START)
                intent = Intent(applicationContext, ZActivityEvents::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("isLoggedIn", true)
                startActivity(intent)
                closeDrawer()

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
                // Initialize a new instance of
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Do you really want to logout of the app ?")
                // Set a positive button and its click listener on alert dialog
                builder.setPositiveButton("YES") { dialog, which ->
                    val sharedPref: SharedPreferences = getSharedPreferences(
                        "mysettings",
                        Context.MODE_PRIVATE
                    )
                    sharedPref.edit().clear().commit();
                    val intent = Intent(applicationContext, SplashActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                // Display a negative button on alert dialog
                builder.setNegativeButton("No") { dialog, which ->
                    dialog.cancel()
                }
                // Finally, make the alert dialog using builder
                val dialog: AlertDialog = builder.create()
                // Display the alert dialog on app interface
                dialog.show()
            }
            R.id.action_signup -> {

            }
            R.id.action_login -> {

            }
            R.id.action_share -> {
                HelperMethods.showAppShareOptions(this@ZActivityDashboard)
            }
        }

        return true
    }

    private fun closeDrawer() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GALLERY_PDF) {
            if (data != null) {
                try {

                    val fileName = HelperMethods.getFilePath(this, data.data)

                    val fileString = HelperMethods.testUriFile(this, fileName!!, data.data)

                  val  file = data?.data?.let {
                        HelperMethods.getFile(
                            this@ZActivityDashboard,
                            it, fileName
                        )
                    }


                    if (file != null) {
                        picturepath_life = fileName
                        imageEncoded_life = HelperMethods.convertToBase64(file!!)
                        Toast.makeText(applicationContext,"Upload Success", Toast.LENGTH_LONG).show()
                    } else {
                        ToastHelper.makeToast(this, "Unable to convert as Base64")
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.d("TAGG", "STACK" + e.printStackTrace())
                    Toast.makeText(applicationContext, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

