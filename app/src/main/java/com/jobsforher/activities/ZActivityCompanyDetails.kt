package com.jobsforher.activities

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.*
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jobsforher.R
import com.jobsforher.adapters.*
import com.jobsforher.helpers.LinePagerIndicatorDecoration
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.models.*
import com.jobsforher.models.Blogs
import com.jobsforher.models.CompanyGroups
import com.jobsforher.models.CompanyImages
import com.jobsforher.models.CompanyVideos
import com.jobsforher.models.Employers
import com.jobsforher.models.EventCompanies
import com.jobsforher.models.Events
import com.jobsforher.models.ImagesData
import com.jobsforher.models.Location
import com.jobsforher.models.Policies
import com.jobsforher.models.Testimonials
import com.jobsforher.network.responsemodels.*
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.toolbar_groups.*
import kotlinx.android.synthetic.main.zactivity_company_details.*
import kotlinx.android.synthetic.main.zactivity_groups_details.*
import kotlinx.android.synthetic.main.zactivity_groups_details.btnJoined
import kotlinx.android.synthetic.main.zactivity_groups_details.button_applied
import kotlinx.android.synthetic.main.zactivity_groups_details.button_apply
import kotlinx.android.synthetic.main.zactivity_groups_details.join_group
import kotlinx.android.synthetic.main.zactivity_groups_details.main
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class ZActivityCompanyDetails : Footer(), NavigationView.OnNavigationItemSelectedListener {

    var listOfPhotos: ArrayList<String> = ArrayList()
    var mRecyclerViewPhotos: RecyclerView? = null
    var mpoliciesRecyclerView: RecyclerView? = null
    var mpoliciesAdapter: RecyclerView.Adapter<*>? = null
    var listOfMyGroupdata: ArrayList<GroupsView> = ArrayList()
    var listOfGroupdata: ArrayList<GroupsView> = ArrayList()
    var listOfCompareGroupdata: ArrayList<GroupsView> = ArrayList()
    var listOfMyGroupdataTrimmed: ArrayList<GroupsView> = ArrayList()
    var mRecyclerViewGroups: RecyclerView? = null
    var mRecyclerViewEvents: RecyclerView? = null
    var mRecyclerViewBlogs: RecyclerView? = null
    var mRecyclerViewTestimonials: RecyclerView? = null
    var listOfEventsdata: ArrayList<EventsView> = ArrayList()
    var listOfBlogsdata: ArrayList<BlogsView> = ArrayList()
    var listOfTestimonialsdata: ArrayList<TestimonialsView> = ArrayList()
    var listOfEventsdataTrimmed: ArrayList<EventsView> = ArrayList()
    var listOfResumes: ArrayList<ResumeView>  = ArrayList()
    var listOfCompareJoineddata: ArrayList<Int> = ArrayList()

    var itemList_AboutUs: ArrayList<String> = ArrayList()
    var itemList_AboutUs_Trimmed: ArrayList<String> = ArrayList()
    var itemList_Culture: ArrayList<String> = ArrayList()
    var itemList_Culture_Trimmed: ArrayList<String> = ArrayList()
    var itemList_Diversity: ArrayList<String> = ArrayList()
    var itemList_Diversity_Trimmed: ArrayList<String> = ArrayList()

    var gridview_abutus: RecyclerView? = null
    var aboutUsAdapter: RecyclerView.Adapter<*>? = null
    var gridview_abutus1: GridView? = null
    var gridview_culture: RecyclerView? = null
    var cultureAdapter: RecyclerView.Adapter<*>? = null
    var gridview_diversity: RecyclerView? = null
    var diversityAdapter: RecyclerView.Adapter<*>? = null

    var listOfVideos: ArrayList<String> = ArrayList()
    var mRecyclerViewVideos: RecyclerView? = null
    var mAdapterVideos: RecyclerView.Adapter<*>? = null
    var KEY_RECYCLER_STATE: String = "recycler_state"
    var mBundleRecyclerViewState: Bundle? = null

    var listOfhotJobsdata: ArrayList<JobsView> = ArrayList()
    var listOfhotJobsdataTrimmed: ArrayList<JobsView> = ArrayList()
    var listOfPostdataDump: ArrayList<GroupsPostModel> = ArrayList()
    var mRecyclerViewPosts: RecyclerView? = null
    var mAdapterJobs: RecyclerView.Adapter<*>? = null
    var mAdapterGroups: RecyclerView.Adapter<*>? = null
    var mAdapterEvents: RecyclerView.Adapter<*>? = null
    var mAdapterBlogs: RecyclerView.Adapter<*>? = null
    var mAdapterTestimonials: RecyclerView.Adapter<*>? = null

    var listOfJobType: ArrayList<String> = ArrayList()
    var listOfJobFArea: ArrayList<String> = ArrayList()
    var listOfJobIndustry: ArrayList<String> = ArrayList()
    var listOfCities: ArrayList<CityView> = ArrayList()

    var isLoggedIn=false
    var groupId = 0
    var title=""
    var isMyGroup = 0
    var isOwner=""
    var groupType="1"
    var scroll = 0
    var type = 0
    var page = ""
    var jobname:String = ""

    private val GALLERY_PDF = 3


    private var retrofitInterface: RetrofitInterface? = null
    private var retrofitInterface1: RetrofitInterface? = null
    var next_page_no_posts: String="1"
    var prev_page_no_posts: String="1"

    private var retrofitInterface_post: RetrofitInterface? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zgroups_companydetails_toolbr)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        isLoggedIn = intent.getBooleanExtra("isLoggedIn", false)
        groupId = intent.getIntExtra("group_Id", 0)
        title = intent.getStringExtra("title")
        isMyGroup = intent.getIntExtra("isMygroup", 0)
        page = intent.getStringExtra("page")

        //groupType = intent.getStringExtra("group_type")
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close
        )

        comp_aboutus.setTextColor(resources.getColor(R.color.green))
        comp_aboutus1.setTextColor(resources.getColor(R.color.green))
        addJobType()

        mappingWidgets()

//        compaboutus.setAnimationDuration(750L)
//        compaboutus.setInterpolator(OvershootInterpolator())
//        compaboutus.setExpandInterpolator(OvershootInterpolator())
//        compaboutus.setCollapseInterpolator(OvershootInterpolator())


        seemore_aboutus.setOnClickListener {

            //compaboutus.toggle()
            if (seemore_aboutus.text.equals("See More")) {
                seemore_aboutus.setText("See Less")
                compaboutus_.visibility = View.VISIBLE
                compaboutus.visibility = View.GONE
            } else {
                seemore_aboutus.setText("See More")
                compaboutus_.visibility = View.GONE
                compaboutus.visibility = View.VISIBLE
            }
        }
        seemore_comculture.setOnClickListener{

            if (seemore_comculture.text.equals("See More"))
                seemore_comculture.setText("See Less")
            else
                seemore_comculture.setText("See More")
        }
        seemore_comdiversity.setOnClickListener{

            company_diversity.toggle()
            if (seemore_comdiversity.text.equals("See More"))
                seemore_comdiversity.setText("See Less")
            else
                seemore_comdiversity.setText("See More")
        }
        seemore_maternity.setOnClickListener{

            row_text_maternity.toggle()
            if (seemore_maternity.text.equals("See More"))
                seemore_maternity.setText("See Less")
            else
                seemore_maternity.setText("See More")
        }
        seemore_paternity.setOnClickListener{

            row_text_paternity.toggle()
            if (seemore_paternity.text.equals("See More"))
                seemore_paternity.setText("See Less")
            else
                seemore_paternity.setText("See More")
        }
        seemore_childcare.setOnClickListener{

            row_text_childcare.toggle()
            if (seemore_childcare.text.equals("See More"))
                seemore_childcare.setText("See Less")
            else
                seemore_childcare.setText("See More")
        }
        seemore_harrasment.setOnClickListener{

            row_text_harrasment.toggle()
            if (seemore_harrasment.text.equals("See More"))
                seemore_harrasment.setText("See Less")
            else
                seemore_harrasment.setText("See More")
        }
        seemore_transportation.setOnClickListener{

            row_text_transportation.toggle()
            if (seemore_transportation.text.equals("See More"))
                seemore_transportation.setText("See Less")
            else
                seemore_transportation.setText("See More")
        }
        seemore_flexi.setOnClickListener{

            row_text_flexi.toggle()
            if (seemore_flexi.text.equals("See More"))
                seemore_flexi.setText("See Less")
            else
                seemore_flexi.setText("See More")
        }
        seemore_other.setOnClickListener{

            row_text_other.toggle()
            if (seemore_other.text.equals("See More"))
                seemore_other.setText("See Less")
            else
                seemore_other.setText("See More")
        }

        seemore_policylayout.setOnClickListener {
            if (seemore_policylayout.text.equals("See More")) {
                llCard_maternity.visibility = View.VISIBLE
                llCard_paternity.visibility = View.VISIBLE
                llCard_childcare.visibility = View.VISIBLE
                llCard_harrasment.visibility = View.VISIBLE
                llCard_transportation.visibility = View.VISIBLE
                llCard_flexi.visibility = View.VISIBLE
                llCard_other.visibility = View.VISIBLE
                seemore_policylayout.setText("See Less")
            }
            else{
                llCard_maternity.visibility = View.VISIBLE
                llCard_paternity.visibility = View.VISIBLE
                llCard_childcare.visibility = View.VISIBLE
                llCard_harrasment.visibility = View.GONE
                llCard_transportation.visibility = View.GONE
                llCard_flexi.visibility = View.GONE
                llCard_other.visibility = View.GONE
                seemore_policylayout.setText("See More")
            }
        }

        seemore_branches.setOnClickListener {
            if (seemore_branches.text.equals("See More")) {
                address_layout3.visibility = View.VISIBLE
                address_layout4.visibility = View.GONE
                seemore_branches.setText("See Less")
            }
            else{
                address_layout3.visibility = View.GONE
                address_layout4.visibility = View.VISIBLE
                seemore_branches.setText("See More")
            }
        }

        seemore_jobs.setOnClickListener {
            if (seemore_jobs.text.equals("See More")){
                if(listOfhotJobsdata.size>listOfhotJobsdataTrimmed.size+5) {
                    for (i in listOfhotJobsdataTrimmed.size until listOfhotJobsdataTrimmed.size + 5) {
                        listOfhotJobsdataTrimmed.add(listOfhotJobsdata[i])
                    }
                    seemore_jobs.setText("See More")
                } else {
                    for (i in listOfhotJobsdataTrimmed.size until listOfhotJobsdata.size) {
                        listOfhotJobsdataTrimmed.add(listOfhotJobsdata[i])
                    }
                    seemore_jobs.setText("See Less")
                }
                var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                mRecyclerViewPosts!!.layoutManager =
                    mgroupsLayoutManager as RecyclerView.LayoutManager?
                mAdapterJobs = JobsAdapter(
                    listOfhotJobsdataTrimmed,
                    isLoggedIn,
                    2,
                    0,
                    listOfCompareJoineddata,
                    0,
                    "Company Details"
                )
                mRecyclerViewPosts!!.adapter = mAdapterJobs

            }
            else {
                listOfhotJobsdataTrimmed.clear()
                for (i in 0 until 5) {
                    listOfhotJobsdataTrimmed.add(listOfhotJobsdata[i])
                }
                var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                mRecyclerViewPosts!!.layoutManager =
                    mgroupsLayoutManager as RecyclerView.LayoutManager?
                mAdapterJobs = JobsAdapter(
                    listOfhotJobsdataTrimmed,
                    isLoggedIn,
                    2,
                    0,
                    listOfCompareJoineddata,
                    0,
                    "Company Details"
                )
                mRecyclerViewPosts!!.adapter = mAdapterJobs
                seemore_jobs.setText("See More")
            }
        }

        seemore_groups.setOnClickListener {
            if (seemore_groups.text.equals("See More")){
                if(listOfGroupdata.size>listOfMyGroupdataTrimmed.size+5) {
                    for (i in listOfMyGroupdataTrimmed.size until listOfMyGroupdataTrimmed.size + 5) {
                        listOfMyGroupdataTrimmed.add(listOfGroupdata[i])
                    }
                    seemore_groups.setText("See More")
                } else {
                    for (i in listOfMyGroupdataTrimmed.size until listOfGroupdata.size) {
                        listOfMyGroupdataTrimmed.add(listOfGroupdata[i])
                    }
                    seemore_groups.setText("See Less")
                }
                var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                mRecyclerViewGroups!!.layoutManager =
                    mgroupsLayoutManager as RecyclerView.LayoutManager?
                mAdapterGroups = MyGroupsAdapter(
                    listOfMyGroupdataTrimmed,
                    isLoggedIn,
                    1,
                    listOfCompareGroupdata,
                    0,
                    0,
                    "Company Details"
                )
                mRecyclerViewGroups!!.adapter = mAdapterGroups

            }
            else {
                listOfMyGroupdataTrimmed.clear()
                for (i in 0 until 5) {
                    listOfMyGroupdataTrimmed.add(listOfGroupdata[i])
                }
                var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                mRecyclerViewGroups!!.layoutManager =
                    mgroupsLayoutManager as RecyclerView.LayoutManager?
                mAdapterGroups = MyGroupsAdapter(
                    listOfMyGroupdataTrimmed,
                    isLoggedIn,
                    1,
                    listOfCompareGroupdata,
                    0,
                    0,
                    "Company Details"
                )
                mRecyclerViewGroups!!.adapter = mAdapterGroups
                seemore_groups.setText("See More")
            }
        }

        seemore_events.setOnClickListener {
            if (seemore_events.text.equals("See More")){
                if(listOfEventsdata.size>listOfEventsdataTrimmed.size+5) {
                    for (i in listOfEventsdataTrimmed.size until listOfEventsdataTrimmed.size + 5) {
                        listOfEventsdataTrimmed.add(listOfEventsdata[i])
                    }
                    seemore_events.setText("See More")
                } else {
                    for (i in listOfEventsdataTrimmed.size until listOfEventsdata.size) {
                        listOfEventsdataTrimmed.add(listOfEventsdata[i])
                    }
                    seemore_events.setText("See Less")
                }
                var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                mRecyclerViewEvents!!.layoutManager =
                    mgroupsLayoutManager as RecyclerView.LayoutManager?
                mAdapterEvents = EventsAdapter(
                    listOfEventsdataTrimmed,
                    isLoggedIn,
                    1,
                    0,
                    listOfCompareJoineddata,
                    0,
                    "Company Details"
                )
                mRecyclerViewEvents!!.adapter = mAdapterEvents

            }
            else {
                listOfEventsdataTrimmed.clear()
                for (i in 0 until 5) {
                    listOfEventsdataTrimmed.add(listOfEventsdata[i])
                }
                var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                mRecyclerViewEvents!!.layoutManager =
                    mgroupsLayoutManager as RecyclerView.LayoutManager?
                mAdapterEvents = EventsAdapter(
                    listOfEventsdataTrimmed,
                    isLoggedIn,
                    1,
                    0,
                    listOfCompareJoineddata,
                    0,
                    "Company Details"
                )
                mRecyclerViewEvents!!.adapter = mAdapterEvents
                seemore_events.setText("See More")
            }
        }

        seemore_aboutus_images.setOnClickListener {
            if (seemore_aboutus_images.text.equals("See More")){
                Log.d("TAGG", "INSIDE IF")
                var mgroupsLayoutManager = GridLayoutManager(applicationContext, 2)
                gridview_abutus!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                aboutUsAdapter = ImagesAdapter(itemList_AboutUs, isLoggedIn)
                gridview_abutus!!.adapter = aboutUsAdapter
                seemore_aboutus_images.setText("See Less")

            }
            else{
                var mgroupsLayoutManager = GridLayoutManager(applicationContext, 2)
                gridview_abutus!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                aboutUsAdapter = ImagesAdapter(itemList_AboutUs_Trimmed, isLoggedIn)
                gridview_abutus!!.adapter = aboutUsAdapter
                seemore_aboutus_images.setText("See More")
            }
        }

        seemore_comculture_images.setOnClickListener {
            if (seemore_comculture_images.text.equals("See More")){
                var mgroupsLayoutManager1 = GridLayoutManager(applicationContext, 2)
                gridview_culture!!.layoutManager = mgroupsLayoutManager1 as RecyclerView.LayoutManager?
                cultureAdapter = ImagesAdapter(itemList_Culture, isLoggedIn)
                gridview_culture!!.adapter = cultureAdapter
                seemore_comculture_images.setText("See Less")
            }
            else{
                var mgroupsLayoutManager1 = GridLayoutManager(applicationContext, 2)
                gridview_culture!!.layoutManager = mgroupsLayoutManager1 as RecyclerView.LayoutManager?
                cultureAdapter = ImagesAdapter(itemList_Culture_Trimmed, isLoggedIn)
                gridview_culture!!.adapter = cultureAdapter
                seemore_comculture_images.setText("See More")
            }
        }

        seemore_comdiversity_images.setOnClickListener {
            if (seemore_comdiversity_images.text.equals("See More")){
                var mgroupsLayoutManager2 = GridLayoutManager(applicationContext, 2)
                gridview_diversity!!.layoutManager = mgroupsLayoutManager2 as RecyclerView.LayoutManager?
                diversityAdapter = ImagesAdapter(itemList_Diversity, isLoggedIn)
                gridview_diversity!!.adapter = diversityAdapter
                seemore_comdiversity_images.setText("See Less")
            }
            else{
                var mgroupsLayoutManager2 = GridLayoutManager(applicationContext, 2)
                gridview_diversity!!.layoutManager = mgroupsLayoutManager2 as RecyclerView.LayoutManager?
                diversityAdapter = ImagesAdapter(itemList_Diversity_Trimmed, isLoggedIn)
                gridview_diversity!!.adapter = diversityAdapter
                seemore_comdiversity_images.setText("See More")
            }
        }

        comp_aboutus.callOnClick()
        comp_aboutus.setOnClickListener {
            type=0
            comp_aboutus.setTextColor(resources.getColor(R.color.green))
            comp_jobs.setTextColor(resources.getColor(R.color.black))
            comp_events.setTextColor(resources.getColor(R.color.black))
//            comp_mentors.setTextColor(resources.getColor(R.color.black))
            comp_blogs.setTextColor(resources.getColor(R.color.black))
            comp_groups.setTextColor(resources.getColor(R.color.black))

            layout_aboutus.visibility = View.VISIBLE
            layout_compdetails.visibility = View.VISIBLE
            layout_compculture.visibility = View.VISIBLE
            layout_comppolicies.visibility = View.VISIBLE
            layout_compdiversity.visibility = View.VISIBLE
            jobs_layout.visibility = View.VISIBLE
            groups_layout.visibility = View.VISIBLE
            events_layout.visibility = View.VISIBLE
            blogs_layout.visibility = View.VISIBLE
            address_layout.visibility = View.VISIBLE
            testimonials_layout.visibility = View.VISIBLE
        }

        comp_jobs.setOnClickListener {
            type=1
            comp_aboutus.setTextColor(resources.getColor(R.color.black))
            comp_jobs.setTextColor(resources.getColor(R.color.green))
            comp_events.setTextColor(resources.getColor(R.color.black))
//            comp_mentors.setTextColor(resources.getColor(R.color.black))
            comp_blogs.setTextColor(resources.getColor(R.color.black))
            comp_groups.setTextColor(resources.getColor(R.color.black))

            layout_aboutus.visibility = View.GONE
            layout_compdetails.visibility = View.GONE
            layout_compculture.visibility = View.GONE
            layout_comppolicies.visibility = View.GONE
            layout_compdiversity.visibility = View.GONE
            jobs_layout.visibility = View.VISIBLE
            groups_layout.visibility = View.GONE
            events_layout.visibility = View.GONE
            blogs_layout.visibility = View.GONE
            address_layout.visibility = View.GONE
            testimonials_layout.visibility = View.GONE
        }

        comp_events.setOnClickListener {
            type=1
            comp_aboutus.setTextColor(resources.getColor(R.color.black))
            comp_jobs.setTextColor(resources.getColor(R.color.black))
            comp_events.setTextColor(resources.getColor(R.color.green))
//            comp_mentors.setTextColor(resources.getColor(R.color.black))
            comp_blogs.setTextColor(resources.getColor(R.color.black))
            comp_groups.setTextColor(resources.getColor(R.color.black))

            layout_aboutus.visibility = View.GONE
            layout_compdetails.visibility = View.GONE
            layout_compculture.visibility = View.GONE
            layout_comppolicies.visibility = View.GONE
            layout_compdiversity.visibility = View.GONE
            jobs_layout.visibility = View.GONE
            groups_layout.visibility = View.GONE
            events_layout.visibility = View.VISIBLE
            blogs_layout.visibility = View.GONE
            address_layout.visibility = View.GONE
            testimonials_layout.visibility = View.GONE
        }


        comp_blogs.setOnClickListener {
            type=1
            comp_aboutus.setTextColor(resources.getColor(R.color.black))
            comp_jobs.setTextColor(resources.getColor(R.color.black))
            comp_events.setTextColor(resources.getColor(R.color.black))
//            comp_mentors.setTextColor(resources.getColor(R.color.black))
            comp_blogs.setTextColor(resources.getColor(R.color.green))
            comp_groups.setTextColor(resources.getColor(R.color.black))

            layout_aboutus.visibility = View.GONE
            layout_compdetails.visibility = View.GONE
            layout_compculture.visibility = View.GONE
            layout_comppolicies.visibility = View.GONE
            layout_compdiversity.visibility = View.GONE
            jobs_layout.visibility = View.GONE
            groups_layout.visibility = View.GONE
            events_layout.visibility = View.GONE
            blogs_layout.visibility = View.VISIBLE
            address_layout.visibility = View.GONE
            testimonials_layout.visibility = View.GONE
        }

        comp_groups.setOnClickListener {
            type=1
            comp_aboutus.setTextColor(resources.getColor(R.color.black))
            comp_jobs.setTextColor(resources.getColor(R.color.black))
            comp_events.setTextColor(resources.getColor(R.color.black))
//            comp_mentors.setTextColor(resources.getColor(R.color.black))
            comp_blogs.setTextColor(resources.getColor(R.color.black))
            comp_groups.setTextColor(resources.getColor(R.color.green))

            layout_aboutus.visibility = View.GONE
            layout_compdetails.visibility = View.GONE
            layout_compculture.visibility = View.GONE
            layout_comppolicies.visibility = View.GONE
            layout_compdiversity.visibility = View.GONE
            jobs_layout.visibility = View.GONE
            groups_layout.visibility = View.VISIBLE
            events_layout.visibility = View.GONE
            blogs_layout.visibility = View.GONE
            address_layout.visibility = View.GONE
            testimonials_layout.visibility = View.GONE
        }

        active_jobs.setOnClickListener {
            view_company.callOnClick()
        }

        view_company.setOnClickListener {

            //jobs_layout.requestFocus()

            val rect:Rect =  Rect(0, 0, jobs_layout.getWidth(), jobs_layout.getHeight());
            jobs_layout.requestRectangleOnScreen(rect, false);
        }

        comp_aboutus1.setOnClickListener {
            comp_aboutus1.setTextColor(resources.getColor(R.color.green))
            comp_jobs1.setTextColor(resources.getColor(R.color.black))
            comp_events1.setTextColor(resources.getColor(R.color.black))
//            comp_mentors.setTextColor(resources.getColor(R.color.black))
            comp_blogs1.setTextColor(resources.getColor(R.color.black))
            comp_groups1.setTextColor(resources.getColor(R.color.black))
            comp_aboutus.callOnClick()
        }

        comp_jobs1.setOnClickListener {
            comp_aboutus1.setTextColor(resources.getColor(R.color.black))
            comp_jobs1.setTextColor(resources.getColor(R.color.green))
            comp_events1.setTextColor(resources.getColor(R.color.black))
//            comp_mentors.setTextColor(resources.getColor(R.color.black))
            comp_blogs1.setTextColor(resources.getColor(R.color.black))
            comp_groups1.setTextColor(resources.getColor(R.color.black))
            comp_jobs.callOnClick()
        }

        comp_events1.setOnClickListener {
            comp_aboutus1.setTextColor(resources.getColor(R.color.black))
            comp_jobs1.setTextColor(resources.getColor(R.color.black))
            comp_events1.setTextColor(resources.getColor(R.color.green))
//            comp_mentors.setTextColor(resources.getColor(R.color.black))
            comp_blogs1.setTextColor(resources.getColor(R.color.black))
            comp_groups1.setTextColor(resources.getColor(R.color.black))
            comp_events.callOnClick()
        }

        comp_blogs1.setOnClickListener {
            comp_aboutus1.setTextColor(resources.getColor(R.color.black))
            comp_jobs1.setTextColor(resources.getColor(R.color.black))
            comp_events1.setTextColor(resources.getColor(R.color.black))
//            comp_mentors.setTextColor(resources.getColor(R.color.black))
            comp_blogs1.setTextColor(resources.getColor(R.color.green))
            comp_groups1.setTextColor(resources.getColor(R.color.black))
            comp_blogs.callOnClick()
        }

        comp_groups1.setOnClickListener {
            comp_aboutus1.setTextColor(resources.getColor(R.color.black))
            comp_jobs1.setTextColor(resources.getColor(R.color.black))
            comp_events1.setTextColor(resources.getColor(R.color.black))
//            comp_mentors.setTextColor(resources.getColor(R.color.black))
            comp_blogs1.setTextColor(resources.getColor(R.color.black))
            comp_groups1.setTextColor(resources.getColor(R.color.green))
            comp_groups.callOnClick()
        }


        mainScroll_jobss.getViewTreeObserver().addOnScrollChangedListener(object :
            ViewTreeObserver.OnScrollChangedListener {
            override fun onScrollChanged() {
                val scrollBounds = Rect()
                view_main.getHitRect(scrollBounds)
                if (view_main.getLocalVisibleRect(scrollBounds)) {
                    // if layout even a single pixel, is within the visible area do something
                    view_main1.visibility = View.GONE
                } else {
                    // if layout goes out or scrolled out of the visible area do something
                    view_main1.visibility = View.VISIBLE
                }
            }
        })


        Log.d("TAGG", "JOB ID IS " + groupId.toString())
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        mRecyclerViewPosts = findViewById(R.id.posts_recycler_view)
        mRecyclerViewGroups = findViewById(R.id.groups_recycler_view)
        mRecyclerViewEvents = findViewById(R.id.events_recycler_view)
        mRecyclerViewBlogs = findViewById(R.id.blogs_recycler_view)
        mRecyclerViewTestimonials = findViewById(R.id.testimonials_recycler_view)

        gridview_abutus = findViewById(R.id.gridview_aboutus)
        gridview_culture = findViewById(R.id.gridview_compculture)
        gridview_diversity = findViewById(R.id.gridview_diversity)

        if(isLoggedIn && isMyGroup==1){

            val menu = navView.menu
            menu.findItem(R.id.action_employerzone).setVisible(false)
            menu.findItem(R.id.action_mentorzone).setVisible(false)
            menu.findItem(R.id.action_partnerzone).setVisible(false)
            menu.findItem(R.id.action_logout).setVisible(true)
            menu.findItem(R.id.action_settings).setVisible(false)
            menu.findItem(R.id.action_signup).setVisible(false)
            menu.findItem(R.id.action_login).setVisible(false)
            navView.setNavigationItemSelectedListener(this)
            val hView = navView.getHeaderView(0)
            val loggedin_header = hView.findViewById(R.id.loggedin_layout) as RelativeLayout
            val nologgedin_layout = hView.findViewById(R.id.nologgedin_layout) as RelativeLayout
            loggedin_header.visibility = View.VISIBLE
            nologgedin_layout.visibility = View.GONE
            val profile_name = hView.findViewById(R.id.profile_name) as TextView
            val img_profile_sidemenu = hView.findViewById(R.id.img_profile_sidemenu) as CircleImageView
            //val img_profile_toolbar = hView.findViewById(R.id.img_profile_toolbar) as CircleImageView
            profile_name.setText(EndPoints.USERNAME)
            if (EndPoints.PROFILE_ICON.length>4) {
                Picasso.with(applicationContext)
                    .load(EndPoints.PROFILE_ICON)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(img_profile_sidemenu)
                Picasso.with(applicationContext)
                    .load(EndPoints.PROFILE_ICON)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(img_profile_toolbar)
            }
            btnJoined.visibility = View.VISIBLE
            join_group.visibility = View.GONE
            def_comp_unfollow.visibility = View.VISIBLE
            def_comp_follow.visibility = View.GONE
            button_apply.visibility = View.GONE
            button_applied.visibility = View.VISIBLE

            loggedin_header.setOnClickListener{
                intent = Intent(applicationContext, ProfileView::class.java)
                startActivity(intent)
            }
            img_profile_toolbar.visibility = View.VISIBLE
//            notificaton.visibility = View.VISIBLE
            sign_in.visibility = View.GONE

        }
        else{
            val menu = navView.menu
            menu.findItem(R.id.action_employerzone).setVisible(false)
            menu.findItem(R.id.action_mentorzone).setVisible(false)
            menu.findItem(R.id.action_partnerzone).setVisible(false)
            menu.findItem(R.id.action_logout).setVisible(true)
            menu.findItem(R.id.action_settings).setVisible(false)
            menu.findItem(R.id.action_signup).setVisible(false)
            menu.findItem(R.id.action_login).setVisible(false)
            navView.setNavigationItemSelectedListener(this)
            val hView = navView.getHeaderView(0)
            val loggedin_header = hView.findViewById(R.id.loggedin_layout) as RelativeLayout
            val nologgedin_layout = hView.findViewById(R.id.nologgedin_layout) as RelativeLayout
            loggedin_header.visibility = View.VISIBLE
            nologgedin_layout.visibility = View.GONE
            val profile_name = hView.findViewById(R.id.profile_name) as TextView
            val img_profile_sidemenu = hView.findViewById(R.id.img_profile_sidemenu) as CircleImageView
            //val img_profile_toolbar = hView.findViewById(R.id.img_profile_toolbar) as CircleImageView
            profile_name.setText(EndPoints.USERNAME)
            if (EndPoints.PROFILE_ICON.length>4) {
                Picasso.with(applicationContext)
                    .load(EndPoints.PROFILE_ICON)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(img_profile_sidemenu)
                Picasso.with(applicationContext)
                    .load(EndPoints.PROFILE_ICON)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(img_profile_toolbar)
            }
            btnJoined.visibility = View.GONE
            join_group.visibility = View.VISIBLE
            def_comp_unfollow.visibility = View.GONE
            def_comp_follow.visibility = View.VISIBLE
            button_apply.visibility = View.VISIBLE
            button_applied.visibility = View.GONE

            loggedin_header.setOnClickListener{
                intent = Intent(applicationContext, ProfileView::class.java)
                startActivity(intent)
            }
            img_profile_toolbar.visibility = View.VISIBLE
//            notificaton.visibility = View.VISIBLE
            sign_in.visibility = View.GONE

        }

        img_profile_toolbar.setOnClickListener{
            intent = Intent(applicationContext, ZActivityDashboard::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("isLoggedIn", true)
            intent.putExtra("pagetype", 0)
            startActivity(intent)
        }





//        create_preferences.setOnClickListener {
//            openBottomSheetPreferences()
//        }


//        for (i in 0 until nav_view.getMenu().size()) {
//            val item = nav_view.getMenu().getItem(i)
//            val spanString = SpannableString(nav_view.getMenu().getItem(i).getTitle().toString())
//            spanString.setSpan(ForegroundColorSpan(resources.getColor(R.color.menu_grey)), 0, spanString.length, 0)
//            item.setTitle(spanString)
//        }
//        nav_view.getMenu().getItem(11).setActionView(R.layout.menu_image);
//        nav_view.getMenu().getItem(12).setActionView(R.layout.menu_image);
//        nav_view.getMenu().getItem(13).setActionView(R.layout.menu_image);
//        val menuItem = nav_view.getMenu().findItem(R.id.action_companies)
//        val s = SpannableString(menuItem.getTitle())
//        s.setSpan(ForegroundColorSpan(resources.getColor(R.color.green)), 0, s.length, 0)
//        menuItem.setTitle(s)
        button_apply.setOnClickListener {
            join_group.callOnClick()
        }

        button_applied.setOnClickListener{
            btnJoined.callOnClick()
        }

        def_comp_unfollow.setOnClickListener{
            btnJoined.callOnClick()
        }

        def_comp_follow.setOnClickListener {
            join_group.callOnClick()
        }

        join_group.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Follow Company")
            builder.setMessage("Are you sure you want to follow this company?")
            //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                followCompany(groupId, join_group, btnJoined, "follow")
                dialog.cancel()
            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->
                dialog.cancel()
            }
            builder.show()

        }

        btnJoined.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Unfollow Company")
            builder.setMessage("Are you sure you want to unfollow this company?")
            builder.setPositiveButton(android.R.string.yes) { dialogInterface, which ->
                followCompany(groupId, join_group, btnJoined, "unfollow")
                //alertDialog.cancel()
            }
            builder.setNegativeButton(android.R.string.no) { dialogInterface, which ->
                //alertDialog.cancel()
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()

        }

        loadAppliedJobs()


        sign_in.setOnClickListener {
            intent = Intent(applicationContext, ZActivityCompanyDetails::class.java)
            intent.putExtra("isLoggedIn", true)
            startActivity(intent)
        }


        shareCompany.setOnClickListener {
            shareCompany()
        }

    }




    fun shareCompany() {

        intent = Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(Intent.EXTRA_SUBJECT, compname.text.toString() + "| JobsForHer");
        intent.putExtra(
            Intent.EXTRA_TEXT, "Click on the link \n https://www.jobsforher.com/company" + "\n\n" +
                    "Application Link : https://play.google.com/store/apps/details?id=com.jobsforher"
        );
        startActivity(Intent.createChooser(intent, "Share Company link!"));
    }


    fun followCompany(id: Int, btnJoinGroup: Button, btnJoined: Button, status: String) {

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
//                    Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                    if (status.compareTo("follow") == 0) {
                        btnJoinGroup.visibility = View.GONE
                        btnJoined.visibility = View.VISIBLE
                        def_comp_unfollow.visibility = View.VISIBLE
                        def_comp_follow.visibility = View.GONE
                        button_apply.visibility = View.GONE
                        button_applied.visibility = View.VISIBLE
                        Snackbar.make(
                            main,
                            "Thank you for following " + compname.text,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        btnJoinGroup.visibility = View.VISIBLE
                        btnJoined.visibility = View.GONE
                        def_comp_unfollow.visibility = View.GONE
                        def_comp_follow.visibility = View.VISIBLE
                        button_applied.visibility = View.GONE
                        button_apply.visibility = View.VISIBLE
                        Snackbar.make(
                            main,
                            "You have successfully unfollowed the company",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }


                } else {
                    //  ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())


                }
            }

            override fun onFailure(call: Call<UpdateReplyResponse>, t: Throwable) {
                Logger.d("TAGG", "Apply Job FAILED : $t")
            }
        })
    }

    fun applyJob(id: Int, title: String, isRequired: Boolean) {

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
                    //  Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                    openBottomSheetPreferences()
//                    if (type.equals("private")){
//                        btnJoinGroup.text = "Requested"
//                    }
//                    else{
//                        btnJoinGroup.visibility =View.GONE
//                        btnJoined.visibility = View.VISIBLE
//                    }

//                    if (isfilter>0) {
//                        val gson = GsonBuilder().serializeNulls().create()
//                        var str_response = gson.toJson(response)
//
//                        val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
//                        val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
//
//                        var jsonarray_info: JSONObject = jsonObject1.getJSONObject("body")
//                        var json_objectdetail: JSONObject = jsonarray_info  //jsonarray_info.getJSONObject(0)
//
//                        val model: GroupsView = GroupsView();
//                       // "approver_id":"","created_on":"2019-09-24 11:11:44","group_id":24,"is_member":true,"modified_on":"2019-09-24 11:11:44","request_status":"approved","role":"member","user_id":1274
//                        model.id = json_objectdetail.getInt("group_id")
//                        model.label = ""
//                        model.icon_url = ""
//                        model.groupType = ""
//                        model.noOfMembers = ""
//                        model.description = ""
//                        model.featured = false
//                        model.status = ""
//
//                       // var citiesArray: JSONArray = json_objectdetail.getJSONArray("cities")
//                        val listOfCity: ArrayList<Int> = ArrayList()
////                        for (j in 0 until citiesArray.length()) {
////                            var citiesObject:JSONObject=citiesArray.getJSONObject(j).
////                            listOfCity.add(citiesObject)
////                        }
//                       // val categoriesArray: JSONArray = json_objectdetail.getJSONArray("categories")
//                        val listOfCategories: ArrayList<Categories> = ArrayList()
////                        for (k in 0 until categoriesArray.length()) {
////                            var categoriesIdObj: JSONObject = categoriesArray.getJSONObject(k)
////                            listOfCategories.add(
////                                Categories(
////                                    categoriesIdObj.getInt("category_id"),
////                                    categoriesIdObj.getString("category")
////                                )
////                            )
////                        }
//                        model.categories = listOfCategories
//                        model.cities = listOfCity
////                        for (k in 0 until listOfCompareGroupdata.size) {
////                            if (listOfCompareGroupdata[k].id == id) {
////                                listOfCompareGroupdata.add()
////                                break
////                            }
////                        }
//                        listOfCompareGroupdata.add(
//                            GroupsView(
//                                model.id,
//                                model.icon_url!!,
//                                model.label!!,
//                                "",
//                                model.groupType!!,
//                                model.noOfMembers!!,
//                                model.description!!,
//                                model.featured!!,
//                                model.status!!,
//                                model.cities!!,
//                                model.categories!!
//                            )
//                        )
//
////                        loadFilteredData("","",message_filter)
//                    }
//                    if (response.body()!!.message.equals("User added to group")){
//                        btnJoinGroup.text = "Requested"
//
//                    }else {
//
//                        ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
//                    }

                } else {
                    //  ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                    val gson = GsonBuilder().serializeNulls().create()
                    val str_response = Gson().toJson(response)
                    val jsonObject: JSONObject = JSONObject(
                        str_response.substring(
                            str_response.indexOf("{"),
                            str_response.lastIndexOf("}") + 1
                        )
                    )
                    if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                        val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                        val jsonarray_info: JSONArray = jsonObject1.getJSONArray("body")

                        var a: Int = 0
                        for (i in 0 until response.body()!!.body!!.size) {

                            val json_objectdetail: JSONObject = jsonarray_info.getJSONObject(0)
                            a = json_objectdetail.getInt("user_id")
                            Log.d(
                                "TAGG",
                                "PREf ID:" + json_objectdetail.getInt("user_id").toString()
                            )
                        }
                        checkDefault(a, title, id, isRequired)
                    }

                }
            }

            override fun onFailure(call: Call<CheckPreferenceResponse>, t: Throwable) {
                Logger.d("TAGG", "Apply Job FAILED : $t")
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

                    //  ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                    val gson = GsonBuilder().serializeNulls().create()
                    val str_response = Gson().toJson(response)
                    val jsonObject: JSONObject = JSONObject(
                        str_response.substring(
                            str_response.indexOf("{"),
                            str_response.lastIndexOf("}") + 1
                        )
                    )
                    if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                        val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                        val jsonarray_info: JSONArray = jsonObject1.getJSONArray("body")
                        listOfResumes.clear()
                        for (i in 0 until response.body()!!.body!!.size) {

                            val json_objectdetail: JSONObject = jsonarray_info.getJSONObject(i)

                            val model: ResumeView = ResumeView();
                            model.is_default = ""//json_objectdetail.getString("is_default")
                            model.path = json_objectdetail.getString("path")
                            model.id = json_objectdetail.getInt("id")
                            model.title =
                                if (json_objectdetail.isNull("title")) "" else json_objectdetail.getString(
                                    "title"
                                )
                            model.is_parsed = false//json_objectdetail.getBoolean("is_parsed")
                            model.created_on = json_objectdetail.getString("created_on")
                            model.modified_on = json_objectdetail.getString("modified_on")
                            model.deleted = json_objectdetail.getBoolean("deleted")
                            model.user_id = json_objectdetail.getInt("user_id")

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

                    //  ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
                if (isRequired)
                    openBottomSheetUploadDoc(jobId, title, listOfResumes)
                else {
                    //saveJob(jobId, note.text.toString(), listOfResumes[0].id, "Applied")
                    if (listOfResumes.size > 0)
                        saveJob(jobId, "", listOfResumes[0].id, "Applied")
                    else
                        saveJob(jobId, "", 0, "Applied")
                }
            }

            override fun onFailure(call: Call<CheckDefaultResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
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

        jobname = title
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.bottom_sheet_applyjobs)
        val add_resume = dialog.findViewById(R.id.add_resume) as LinearLayout
        val add_resume_title = dialog.findViewById(R.id.add_resume_title) as LinearLayout
        val note = dialog.findViewById(R.id.note) as EditText

        var mAdapterResume: RecyclerView.Adapter<*>? = null
        val mRecyclerView = dialog.findViewById(R.id.resume_recycler_view) as RecyclerView
        val mLayoutManager = LinearLayoutManager(
            applicationContext,
            LinearLayoutManager.VERTICAL,
            false
        )
        var checklist: java.util.ArrayList<Int> = java.util.ArrayList()
        for (k in 0 until listOfResume.size) {
            if (k == 0)
                checklist.add(1)
            else
                checklist.add(0)
        }
        if (listOfResumes.size > 1)
            mRecyclerView.getLayoutParams().height = 600;
        mAdapterResume = ResumeAdapter1(listOfResumes, checklist, "Company Details")
        mRecyclerView!!.adapter = mAdapterResume
        mRecyclerView!!.layoutManager = mLayoutManager
        val mobileLayout = dialog.findViewById(R.id.mobile_layout) as LinearLayout
        if(EndPoints.PHONE_NO.length>2)
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
            showPictureDialog()
        }
        val cancel_pref = dialog.findViewById(R.id.cancel_pref) as Button
        cancel_pref.setOnClickListener {
            dialog.cancel()
        }
        val restitle = dialog.findViewById(R.id.resumeTitle) as EditText
        val mobileno = dialog.findViewById(R.id.mobile) as EditText
        val save_pref = dialog.findViewById(R.id.save_pref) as Button
        save_pref.setOnClickListener {
            if(restitle.getText().length>0) {
                if (mobileno.getText().length > 0 && mobileno.length() >6 && mobileno.length() < 13) {
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
                    Toast.makeText(
                        applicationContext,
                        "Please enter valid mobile number",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }
            else{
                Toast.makeText(applicationContext, "Please enter resume title", Toast.LENGTH_LONG).show()
            }
        }
        val apply_job = dialog.findViewById(R.id.apply_pref) as Button
        apply_job.setOnClickListener {
            if (mobileno.getText().length > 0 && mobileno.length() >6 && mobileno.length() < 13) {
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
                    if(picturepath_life!!.length>0) {
                        if(restitle.getText().length>0) {
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
                        Toast.makeText(
                            applicationContext,
                            "Please upload a resume",
                            Toast.LENGTH_LONG
                        ).show()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Please enter a valid mobile number",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        if (listOfResume.size>0){
            closebutton.setText("Select Resume")
            mRecyclerView.visibility = View.VISIBLE
            add_resume.visibility = View.GONE
            add_resume_title.visibility = View.GONE
        }
        else{
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

        var window :Window = dialog.getWindow()!!;
        window.setBackgroundDrawable(null);
        var wlp: WindowManager.LayoutParams = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog .show()
    }

    private fun showPictureDialog() {

        val chooseFile: Intent
        val intent: Intent
        chooseFile = Intent(Intent.ACTION_GET_CONTENT)
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE)
        chooseFile.type = "application/pdf"
        intent = Intent.createChooser(chooseFile, "Choose a file")
        startActivityForResult(intent, GALLERY_PDF)
    }

    fun sendResume(title: String, jobId: Int) {

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
                Logger.d("URL", "" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
                var str_response = Gson().toJson(response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf(
                            "{"
                        ), str_response.lastIndexOf("}") + 1
                    )
                )
                val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                var jsonarray: JSONObject = jsonObject1.getJSONObject("body")

                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "Adding Success!!", Toast.LENGTH_LONG).show()
                    val resumeId = jsonarray.getInt("id")

                    saveJob(jobId, "", resumeId, "Applied")
                } else {
                    ToastHelper.makeToast(applicationContext, "message")
                }
            }

            override fun onFailure(call: Call<UpdateReplyResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext, "Adding Failed!!", Toast.LENGTH_LONG).show()

            }
        })
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
                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                    Logger.e("TAGG", "" + response.body()!!.message.toString())

                    finish()
                    val intent = Intent(applicationContext, ZActivityJobDetails::class.java)
                    intent.putExtra("isLoggedIn", true)
                    intent.putExtra("group_Id", job_id)
                    intent.putExtra("comp_id", groupId)
                    intent.putExtra("title", jobname)
                    intent.putExtra("isMygroup", 1)
                    intent.putExtra("page", "Company Details")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent)

                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<SaveJobResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
            }
        })
    }

    fun sharegroupdetails() {

        intent = Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(Intent.EXTRA_SUBJECT, groupname.text.toString() + "| JobsForHer");
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Click on the link \n http://www.workingnaari.in/groups" + "\n\n" + "Group Name: " + groupname.text.toString()
        );
        startActivity(Intent.createChooser(intent, "Share Group link!"));

    }


//    fun shareItem(img:ImageView, url: String) {
//        val bmpUri = getLocalBitmapUri(img)
//        if (bmpUri != null) {
//            // Construct a ShareIntent with link to image
//            val shareIntent = Intent()
//            shareIntent.action = Intent.ACTION_SEND
//            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri)
//            shareIntent.type = "image/*"
//            startActivity(Intent.createChooser(shareIntent, "Share Image"))
//        }
//    }
//
//    fun getLocalBitmapUri(bmp: Bitmap): Uri? {
//        var bmpUri: Uri? = null
//        try {
//            val file = File(
//                getExternalFilesDir(Environment.DIRECTORY_PICTURES),
//                "share_image_" + System.currentTimeMillis() + ".png"
//            )
//            val out = FileOutputStream(file)
//            bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
//            out.close()
//            bmpUri = Uri.fromFile(file)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//        return bmpUri
//    }

//    override fun onPause() {
//        super.onPause()
//
//        // save RecyclerView state
//        mBundleRecyclerViewState = Bundle()
//        val listState = mRecyclerViewPosts!!.getLayoutManager()!!.onSaveInstanceState()
//        mBundleRecyclerViewState!!.putParcelable(KEY_RECYCLER_STATE, listState)
//    }

    fun addJobType(){

        listOfJobType.clear()
        listOfJobType.add("Full Time")
        listOfJobType.add("Part Time")
        listOfJobType.add("Work From Home")
        listOfJobType.add("Returnee Program")
        listOfJobType.add("Freelance/Projects")
        listOfJobType.add("Volunteer")
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

                Log.d("TAG", "CODE" + response.code().toString() + "")
                Log.d("TAG", "MESSAGE" + response.message() + "")
                Log.d("TAG", "RESPONSE" + "" + Gson().toJson(response))
                Log.d("TAG", "URL" + "" + response)
                //var str_response = Gson().toJson(response)
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf(
                            "{"
                        ), str_response.lastIndexOf("}") + 1
                    )
                )
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                    val responseCode: Int = jsonObject1.getInt("response_code")
                    val message: String = jsonObject1.getString("message")
                    var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                    if (response.isSuccessful) {
                        for (i in 0 until response.body()!!.body!!.size) {
                            var json_objectdetail: JSONObject = jsonarray.getJSONObject(i)
                            val model: FunctionalAreaView = FunctionalAreaView();
                            //model.id = json_objectdetail.getInt("id")
                            model.name = json_objectdetail.getString("name")
                            listOfJobFArea.add(model!!.name!!.toString())
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
        addJobIndustry()
    }

    fun addJobIndustry() {

        listOfJobIndustry.clear()
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getIndustry(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN
        )
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
                        str_response.indexOf(
                            "{"
                        ), str_response.lastIndexOf("}") + 1
                    )
                )
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                    val responseCode: Int = jsonObject1.getInt("response_code")
                    val message: String = jsonObject1.getString("message")
                    var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                    if (response.isSuccessful) {
                        for (i in 0 until response.body()!!.body!!.size) {
                            var json_objectdetail: JSONObject = jsonarray.getJSONObject(i)
                            val model: FunctionalAreaView = FunctionalAreaView();
                            //model.id = json_objectdetail.getInt("id")
                            model.name = json_objectdetail.getString("name")
                            listOfJobIndustry.add(model!!.name!!.toString())
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
        loadCityData()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 1) {

            if (resultCode == 1) {
                listOfhotJobsdata.clear()

                mRecyclerViewPosts!!.smoothScrollToPosition(listOfhotJobsdata.size)
//                mRecyclerViewPosts!!.post({
//                    val y = mRecyclerViewPosts!!.getY() + mRecyclerViewPosts!!.getChildAt(2).getY()
//                    mainScroll_grpdetails.smoothScrollTo(0, y.toInt())
//                    mainScroll_grpdetails.smoothScrollTo(0,mainScroll_grpdetails.bottom)
//                })

            }
            if (resultCode == 0) {
            }
        }
    }

    private fun addPost(post_type: String){

        val params = HashMap<String, String>()
        var msg: String=""
        val description = edittext_createpost.text.toString()
        val pinned_post = "false"
        val post_type = post_type


        params["description"] = description
        params["post_type"] = post_type
        if (post_type.equals("text")) {
            params["image_filename"] = ""
            params["image_file"] = ""
            params["url"]= ""
        }



        retrofitInterface_post = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface_post!!.addPost(
            Integer.parseInt(groupId.toString()),
            "application/json",
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<CreatePostResponse> {

            override fun onResponse(
                call: Call<CreatePostResponse>,
                response: Response<CreatePostResponse>
            ) {

                Log.d("TAG", "CODE Value" + response.code().toString() + "")
                Log.d("TAG", "MESSAGE VAlue" + response.message() + "")
                Log.d("TAG", "RESPONSE Value" + "" + Gson().toJson(response))
                Log.d("TAGG", "URL Value" + "" + response)

                //var str_response = Gson().toJson(response)
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject =
                    JSONObject(
                        str_response.substring(
                            str_response.indexOf("{"), str_response.lastIndexOf(
                                "}"
                            ) + 1
                        )
                    )
                val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                var jsonaobj: JSONObject = jsonObject1.getJSONObject("body")
                if (response.isSuccessful) {
                    if (responseCode == 10200) {
                        //mDataList[position].comment_list!! =
                        Log.d("TAGG", "DATA" + jsonaobj.getString("id"))
                        Toast.makeText(applicationContext, "Post Created!!", Toast.LENGTH_LONG)
                            .show()
                        edittext_createpost.text.clear()
                        //finish()
                    } else {

                    }
                    //finish()
                } else {

                }
            }

            override fun onFailure(call: Call<CreatePostResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })
    }

    fun joinGroup(id: Int, btnJoinGroup: Button, type: String, btnJoined: Button) {

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
                    } else {
//                        btnJoinGroup.visibility =View.GONE
//                        btnJoined.visibility = View.VISIBLE
                        finish()
                        val intent = Intent(applicationContext, ZActivityCompanyDetails::class.java)
                        intent.putExtra("isLoggedIn", true)
                        intent.putExtra("group_Id", id)
                        intent.putExtra("group_type", type)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("isMygroup", 1)
                        startActivity(intent)
                    }

                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<JoinGroupResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })

    }

    fun loadCompanyDetailsData(){

        var progressDoalog: ProgressDialog
        progressDoalog = ProgressDialog(this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("loading....");
        progressDoalog.setTitle("Company details");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.isIndeterminate = false
        // show it
        progressDoalog.show();

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getCompanyDetailsData(
            groupId,
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN
        )
        Logger.d("URL", "" + "HI" + groupId)
        call.enqueue(object : Callback<CompanyApiDetails> {
            override fun onResponse(
                call: Call<CompanyApiDetails>,
                response: Response<CompanyApiDetails>
            ) {

                Logger.d("URL", "JOBS" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "JOB DETAILS" + Gson().toJson(response))

                val maxLogSize = 1000
                val stringLength = Gson().toJson(response).length
                for (i in 0..stringLength / maxLogSize) {
                    val start = i * maxLogSize
                    var end = (i + 1) * maxLogSize
                    end =
                        if (end > Gson().toJson(response).length) Gson().toJson(response).length else end
                    Log.d("YOURTAG", Gson().toJson(response).substring(start, end))
                }

//                //var str_response = Gson().toJson(response)
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf(
                            "{"
                        ), str_response.lastIndexOf("}") + 1
                    )
                )
                val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                //val jsonarray_info: JSONArray = jsonObject1.getJSONArray("body")
                val json_objectdetail: JSONObject = jsonObject1.getJSONObject("body")

                if (response.isSuccessful) {

                    //var json_objectdetail: JSONObject =jsonarray_info.getJSONObject(0)
                    val model: CompanyDetailsData = CompanyDetailsData();

                    model.id = json_objectdetail.getInt("id")
                    model.industry =
                        if (json_objectdetail.isNull("industry")) "" else json_objectdetail.getString(
                            "industry"
                        )
                    model.view_count =
                        if (json_objectdetail.isNull("view_count")) 0 else json_objectdetail.getInt(
                            "view_count"
                        )
                    model.banner_image =
                        if (json_objectdetail.isNull("banner_image")) "" else json_objectdetail.getString(
                            "banner_image"
                        )
                    model.website =
                        if (json_objectdetail.isNull("website")) "" else json_objectdetail.getString(
                            "website"
                        )
                    model.status =
                        if (json_objectdetail.isNull("status")) "" else json_objectdetail.getString(
                            "status"
                        )
                    model.cities =
                        if (json_objectdetail.isNull("cities")) "" else json_objectdetail.getString(
                            "cities"
                        )
                    model.deleted =
                        if (json_objectdetail.isNull("deleted")) false else json_objectdetail.getBoolean(
                            "deleted"
                        )
                    model.sac_hsc_no =
                        if (json_objectdetail.isNull("sac_hsc_no")) "" else json_objectdetail.getString(
                            "sac_hsc_no"
                        )
                    model.created_on =
                        if (json_objectdetail.isNull("created_on")) "" else json_objectdetail.getString(
                            "created_on"
                        )
                    model.established_date =
                        if (json_objectdetail.isNull("established_date")) 0 else json_objectdetail.getInt(
                            "established_date"
                        )
                    model.name =
                        if (json_objectdetail.isNull("name")) "" else json_objectdetail.getString(
                            "name"
                        )
                    model.company_profile_url =
                        if (json_objectdetail.isNull("company_profile_url")) "" else json_objectdetail.getString(
                            "company_profile_url"
                        )
                    model.company_profile_percentage =
                        if (json_objectdetail.isNull("company_profile_percentage")) "" else json_objectdetail.getString(
                            "company_profile_percentage"
                        )
                    model.team_size =
                        if (json_objectdetail.isNull("team_size")) "" else json_objectdetail.getString(
                            "team_size"
                        )
                    model.diversity =
                        if (json_objectdetail.isNull("diversity")) "" else json_objectdetail.getString(
                            "diversity"
                        )
                    model.culture =
                        if (json_objectdetail.isNull("culture")) "" else json_objectdetail.getString(
                            "culture"
                        )
                    model.created_by =
                        if (json_objectdetail.isNull("created_by")) 0 else json_objectdetail.getInt(
                            "created_by"
                        )
                    model.featured =
                        if (json_objectdetail.isNull("featured")) false else json_objectdetail.getBoolean(
                            "featured"
                        )
                    model.gstin_no =
                        if (json_objectdetail.isNull("gstin_no")) "" else json_objectdetail.getString(
                            "gstin_no"
                        )
                    model.modified_on =
                        if (json_objectdetail.isNull("modified_on")) "" else json_objectdetail.getString(
                            "modified_on"
                        )
                    model.about_us =
                        if (json_objectdetail.isNull("about_us")) "" else json_objectdetail.getString(
                            "about_us"
                        )
                    model.logo =
                        if (json_objectdetail.isNull("logo")) "" else json_objectdetail.getString(
                            "logo"
                        )
                    model.company_type =
                        if (json_objectdetail.isNull("company_type")) "" else json_objectdetail.getString(
                            "company_type"
                        )
                    model.featured_end_date =
                        if (json_objectdetail.isNull("featured_end_date")) "" else json_objectdetail.getString(
                            "featured_end_date"
                        )
                    model.active_jobs_count =
                        if (json_objectdetail.isNull("active_jobs_count")) 0 else json_objectdetail.getInt(
                            "active_jobs_count"
                        )

                    // Locations

                    var location: JSONArray
                    var locdata: ArrayList<Location> = ArrayList()
                    if (json_objectdetail.isNull("locations")) {
                    } else {
                        location = json_objectdetail.getJSONArray("locations")

                        for (k in 0 until location.length()) {

                            val locationModel: Location = Location()
                            locationModel.created_on =
                                if (location.getJSONObject(k)
                                        .isNull("created_on")
                                ) "" else location.getJSONObject(
                                    k
                                ).getString(
                                    "created_on"
                                )
                            locationModel.hide_on_web =
                                if (location.getJSONObject(k)
                                        .isNull("hide_on_web")
                                ) false else location.getJSONObject(
                                    k
                                ).getBoolean("hide_on_web")
                            locationModel.id = location.getJSONObject(k).getInt("id")
                            locationModel.state_id =
                                if (location.getJSONObject(k)
                                        .isNull("state_id")
                                ) 0 else (location.getJSONObject(
                                    k
                                ).getInt(
                                    "state_id"
                                ))
                            locationModel.city_name =
                                if (location.getJSONObject(k)
                                        .isNull("city_name")
                                ) "" else location.getJSONObject(
                                    k
                                ).getString(
                                    "city_name"
                                )
                            locationModel.state_name =
                                if (location.getJSONObject(k)
                                        .isNull("state_name")
                                ) "" else location.getJSONObject(
                                    k
                                ).getString(
                                    "state_name"
                                )
                            locationModel.city_id =
                                if (location.getJSONObject(k)
                                        .isNull("city_id")
                                ) 0 else (location.getJSONObject(
                                    k
                                ).getInt(
                                    "city_id"
                                ))
                            locationModel.country_id =
                                if (location.getJSONObject(k)
                                        .isNull("country_id")
                                ) 0 else (location.getJSONObject(
                                    k
                                ).getInt(
                                    "country_id"
                                ))
                            locationModel.pincode =
                                if (location.getJSONObject(k)
                                        .isNull("pincode")
                                ) 0 else location.getJSONObject(
                                    k
                                ).getInt(
                                    "pincode"
                                )
                            locationModel.country_name =
                                if (location.getJSONObject(k)
                                        .isNull("country_name")
                                ) "" else location.getJSONObject(
                                    k
                                ).getString("country_name")
                            locationModel.location_type =
                                if (location.getJSONObject(k)
                                        .isNull("location_type")
                                ) "" else location.getJSONObject(
                                    k
                                ).getString("location_type")
                            locationModel.address_2 =
                                if (location.getJSONObject(k)
                                        .isNull("address_2")
                                ) "" else location.getJSONObject(
                                    k
                                ).getString(
                                    "address_2"
                                )
                            locationModel.company_id =
                                if (location.getJSONObject(k)
                                        .isNull("company_id")
                                ) 0 else location.getJSONObject(
                                    k
                                ).getInt(
                                    "company_id"
                                )
                            locationModel.address =
                                if (location.getJSONObject(k)
                                        .isNull("address")
                                ) "" else location.getJSONObject(
                                    k
                                ).getString(
                                    "address"
                                )
                            locationModel.modified_on =
                                if (location.getJSONObject(k)
                                        .isNull("modified_on")
                                ) "" else location.getJSONObject(
                                    k
                                ).getString("modified_on")
                            locdata.add(locationModel)
                        }
                    }
                    model.locations = locdata

                    //Policies

                    val policyModel: Policies = Policies()
                    var policies: JSONObject
                    var maternitydata: ArrayList<PolicyData> = ArrayList()
                    if (json_objectdetail.isNull("policies")) {
                        default_company_policy.visibility = View.VISIBLE
                        policy_layout.visibility = View.GONE
                    } else {
                        policies = json_objectdetail.getJSONObject("policies")
                        var maternity: JSONObject
                        if (policies.isNull("maternity")) {
                        } else {
                            maternity = policies.getJSONObject("maternity")
//                        for (k in 0 until maternity.length()) {
                            val maternityModel: PolicyData = PolicyData()
                            maternityModel.created_on = maternity.getString("created_on")
                            maternityModel.modified_on = maternity.getString("modified_on")
                            maternityModel.description = maternity.getString("description")
                            maternityModel.id = maternity.getInt("id")
                            maternityModel.title = maternity.getString("title")
                            maternityModel.company_id = maternity.getInt("company_id")
                            maternityModel.policy_type = maternity.getString("policy_type")
                            maternityModel.image_url = maternity.getString("image_url")
                            maternitydata.add(maternityModel)
//                        }
                        }

                        var paternity: JSONObject
                        var paternitydata: ArrayList<PolicyData> = ArrayList()
                        if (policies.isNull("paternity")) {
                        } else {
                            paternity = policies.getJSONObject("paternity")
//                        for (k in 0 until paternity.length()) {
                            val paternityModel: PolicyData = PolicyData()
                            paternityModel.created_on = paternity.getString("created_on")
                            paternityModel.modified_on = paternity.getString("modified_on")
                            paternityModel.description = paternity.getString("description")
                            paternityModel.id = paternity.getInt("id")
                            paternityModel.title = paternity.getString("title")
                            paternityModel.company_id = paternity.getInt("company_id")
                            paternityModel.policy_type = paternity.getString("policy_type")
                            paternityModel.image_url = paternity.getString("image_url")
                            paternitydata.add(paternityModel)
//                        }
                        }

                        var childcare: JSONObject
                        var childcaredata: ArrayList<PolicyData> = ArrayList()
                        if (policies.isNull("child-care")) {
                        } else {
                            childcare = policies.getJSONObject("child-care")
//                        for (k in 0 until childcare.length()) {
                            val childcareModel: PolicyData = PolicyData()
                            childcareModel.created_on = childcare.getString("created_on")
                            childcareModel.modified_on = childcare.getString("modified_on")
                            childcareModel.description = childcare.getString("description")
                            childcareModel.id = childcare.getInt("id")
                            childcareModel.title = childcare.getString("title")
                            childcareModel.company_id = childcare.getInt("company_id")
                            childcareModel.policy_type = childcare.getString("policy_type")
                            childcareModel.image_url = childcare.getString("image_url")
                            childcaredata.add(childcareModel)
//                        }
                        }

                        var harassment: JSONObject
                        var harassmentdata: ArrayList<PolicyData> = ArrayList()
                        if (policies.isNull("harassment")) {
                        } else {
                            harassment = policies.getJSONObject("harassment")
//                        for (k in 0 until harassment.length()) {
                            val harassmentModel: PolicyData = PolicyData()
                            harassmentModel.created_on = harassment.getString("created_on")
                            harassmentModel.modified_on = harassment.getString("modified_on")
                            harassmentModel.description = harassment.getString("description")
                            harassmentModel.id = harassment.getInt("id")
                            harassmentModel.title = harassment.getString("title")
                            harassmentModel.company_id = harassment.getInt("company_id")
                            harassmentModel.policy_type = harassment.getString("policy_type")
                            harassmentModel.image_url = harassment.getString("image_url")
                            harassmentdata.add(harassmentModel)
//                        }
                        }

                        var transportation: JSONObject
                        var transportationdata: ArrayList<PolicyData> = ArrayList()
                        if (policies.isNull("transportation")) {
                        } else {
                            transportation = policies.getJSONObject("transportation")
//                        for (k in 0 until transportation.length()) {
                            val transportationModel: PolicyData = PolicyData()
                            transportationModel.created_on = transportation.getString("created_on")
                            transportationModel.modified_on =
                                transportation.getString("modified_on")
                            transportationModel.description =
                                transportation.getString("description")
                            transportationModel.id = transportation.getInt("id")
                            transportationModel.title = transportation.getString("title")
                            transportationModel.company_id = transportation.getInt("company_id")
                            transportationModel.policy_type =
                                transportation.getString("policy_type")
                            transportationModel.image_url = transportation.getString("image_url")
                            transportationdata.add(transportationModel)
//                        }
                        }

                        var flexi: JSONObject
                        var flexidata: ArrayList<PolicyData> = ArrayList()
                        if (policies.isNull("flexi")) {
                        } else {
                            flexi = policies.getJSONObject("flexi")
//                        for (k in 0 until flexi.length()) {
                            val flexiModel: PolicyData = PolicyData()
                            flexiModel.created_on = flexi.getString("created_on")
                            flexiModel.modified_on = flexi.getString("modified_on")
                            flexiModel.description = flexi.getString("description")
                            flexiModel.id = flexi.getInt("id")
                            flexiModel.title = flexi.getString("title")
                            flexiModel.company_id = flexi.getInt("company_id")
                            flexiModel.policy_type = flexi.getString("policy_type")
                            flexiModel.image_url = flexi.getString("image_url")
                            flexidata.add(flexiModel)
//                        }
                        }

                        var other: JSONObject
                        var otherdata: ArrayList<PolicyData> = ArrayList()
                        if (policies.isNull("other")) {
                        } else {
                            other = policies.getJSONObject("other")
//                        for (k in 0 until other.length()) {
                            val otherModel: PolicyData = PolicyData()
                            otherModel.created_on = other.getString("created_on")
                            otherModel.modified_on = other.getString("modified_on")
                            otherModel.description = other.getString("description")
                            otherModel.id = other.getInt("id")
                            otherModel.title = other.getString("title")
                            otherModel.company_id = other.getInt("company_id")
                            otherModel.policy_type = other.getString("policy_type")
                            otherModel.image_url = other.getString("image_url")
                            otherdata.add(otherModel)
//                        }
                        }

                        var transport: JSONObject
                        var transportdata: ArrayList<PolicyData> = ArrayList()
                        if (policies.isNull("transport")) {
                        } else {
                            transport = policies.getJSONObject("transport")
//                        for (k in 0 until transport.length()) {
                            val transportModel: PolicyData = PolicyData()
                            transportModel.created_on = transport.getString("created_on")
                            transportModel.modified_on = transport.getString("modified_on")
                            transportModel.description = transport.getString("description")
                            transportModel.id = transport.getInt("id")
                            transportModel.title = transport.getString("title")
                            transportModel.company_id = transport.getInt("company_id")
                            transportModel.policy_type = transport.getString("policy_type")
                            transportModel.image_url = transport.getString("image_url")
                            transportdata.add(transportModel)
//                        }
                        }
                        policyModel.maternity = maternitydata
                        policyModel.paternity = paternitydata
                        policyModel.childcare = childcaredata
                        policyModel.harassment = harassmentdata
                        policyModel.transportation = transportationdata
                        policyModel.other = otherdata
                        policyModel.transport = transportdata
                        policyModel.flexi = flexidata
                    }
                    model.policies = policyModel

                    //Jobs
                    var jobs: JSONArray
                    var jobsdata: ArrayList<Jobs> = ArrayList()
                    if (json_objectdetail.isNull("jobs")) {
                    } else {
                        jobs = json_objectdetail.getJSONArray("jobs")

                        for (k in 0 until jobs.length()) {

                            val jobsModel: Jobs = Jobs()
                            jobsModel.view_count =
                                if (jobs.getJSONObject(k)
                                        .isNull("view_count")
                                ) 0 else jobs.getJSONObject(
                                    k
                                ).getInt(
                                    "view_count"
                                )
                            jobsModel.status =
                                if (jobs.getJSONObject(k)
                                        .isNull("status")
                                ) "" else jobs.getJSONObject(
                                    k
                                ).getString(
                                    "status"
                                )
                            jobsModel.location_id =
                                if (jobs.getJSONObject(k)
                                        .isNull("location_id")
                                ) 0 else jobs.getJSONObject(
                                    k
                                ).getInt(
                                    "location_id"
                                )
                            jobsModel.boosted =
                                if (jobs.getJSONObject(k)
                                        .isNull("boosted")
                                ) false else jobs.getJSONObject(
                                    k
                                ).getBoolean(
                                    "boosted"
                                )
                            jobsModel.min_year =
                                if (jobs.getJSONObject(k)
                                        .isNull("min_year")
                                ) 0 else jobs.getJSONObject(
                                    k
                                ).getInt("min_year")
                            jobsModel.created_on =
                                if (jobs.getJSONObject(k)
                                        .isNull("created_on")
                                ) "" else jobs.getJSONObject(
                                    k
                                ).getString(
                                    "created_on"
                                )
                            jobsModel.location_name =
                                if (jobs.getJSONObject(k)
                                        .isNull("location_name")
                                ) "" else jobs.getJSONObject(
                                    k
                                ).getString(
                                    "location_name"
                                )
                            jobsModel.resume_required =
                                if (jobs.getJSONObject(k)
                                        .isNull("resume_required")
                                ) true else jobs.getJSONObject(
                                    k
                                ).getBoolean(
                                    "resume_required"
                                )
                            jobsModel.title =
                                if (jobs.getJSONObject(k)
                                        .isNull("title")
                                ) "" else jobs.getJSONObject(
                                    k
                                ).getString("title")
                            jobsModel.id =
                                if (jobs.getJSONObject(k).isNull("id")) 0 else jobs.getJSONObject(k)
                                    .getInt(
                                        "id"
                                    )
                            jobsModel.specialization_id =
                                if (jobs.getJSONObject(k)
                                        .isNull("specialization_id")
                                ) 0 else jobs.getJSONObject(
                                    k
                                ).getInt(
                                    "specialization_id"
                                )
                            jobsModel.user_id =
                                if (jobs.getJSONObject(k)
                                        .isNull("user_id")
                                ) 0 else jobs.getJSONObject(
                                    k
                                ).getInt("user_id")
                            jobsModel.max_year =
                                if (jobs.getJSONObject(k)
                                        .isNull("max_year")
                                ) 0 else jobs.getJSONObject(
                                    k
                                ).getInt("max_year")
                            jobsModel.company_id =
                                if (jobs.getJSONObject(k)
                                        .isNull("company_id")
                                ) 0 else jobs.getJSONObject(
                                    k
                                ).getInt(
                                    "company_id"
                                )
                            jobsModel.modified_on =
                                if (jobs.getJSONObject(k)
                                        .isNull("modified_on")
                                ) "" else jobs.getJSONObject(
                                    k
                                ).getString(
                                    "modified_on"
                                )
                            jobsModel.redirect_url =
                                if (jobs.getJSONObject(k)
                                        .isNull("redirect_url")
                                ) "" else jobs.getJSONObject(
                                    k
                                ).getString(
                                    "redirect_url"
                                )
                            jobsModel.co_owners =
                                if (jobs.getJSONObject(k)
                                        .isNull("co_owners")
                                ) "" else jobs.getJSONObject(
                                    k
                                ).getString(
                                    "co_owners"
                                )
                            jobsModel.description =
                                if (jobs.getJSONObject(k)
                                        .isNull("description")
                                ) "" else jobs.getJSONObject(
                                    k
                                ).getString(
                                    "description"
                                )
                            jobsModel.published_on =
                                if (jobs.getJSONObject(k)
                                        .isNull("published_on")
                                ) "" else jobs.getJSONObject(
                                    k
                                ).getString(
                                    "published_on"
                                )
                            jobsModel.job_posting_type =
                                if (jobs.getJSONObject(k)
                                        .isNull("job_posting_type")
                                ) "" else jobs.getJSONObject(
                                    k
                                ).getString(
                                    "job_posting_type"
                                )
                            jobsModel.vacancy =
                                if (jobs.getJSONObject(k)
                                        .isNull("vacancy")
                                ) "" else jobs.getJSONObject(
                                    k
                                ).getString(
                                    "vacancy"
                                )
                            jobsModel.application_notification_type =
                                if (jobs.getJSONObject(k)
                                        .isNull("application_notification_type")
                                ) "" else jobs.getJSONObject(
                                    k
                                ).getString("application_notification_type")
                            jobsModel.salary =
                                if (jobs.getJSONObject(k)
                                        .isNull("salary")
                                ) "" else jobs.getJSONObject(
                                    k
                                ).getString(
                                    "salary"
                                )
                            jobsModel.min_qualification =
                                if (jobs.getJSONObject(k)
                                        .isNull("min_qualification")
                                ) "" else jobs.getJSONObject(
                                    k
                                ).getString(
                                    "min_qualification"
                                )

                            jobsdata.add(jobsModel)
                        }
                    }
                    model.jobs = jobsdata

                    //Employers

                    var employers: JSONArray
                    var employersdata: ArrayList<Employers> = ArrayList()
                    if (json_objectdetail.isNull("employers")) {
                    } else {
                        employers = json_objectdetail.getJSONArray("employers")

                        for (k in 0 until employers.length()) {

                            val employersModel: Employers = Employers()
                            employersModel.designation =
                                if (employers.getJSONObject(k)
                                        .isNull("designation")
                                ) "" else employers.getJSONObject(
                                    k
                                ).getString(
                                    "designation"
                                )
                            employersModel.created_on =
                                if (employers.getJSONObject(k)
                                        .isNull("created_on")
                                ) "" else employers.getJSONObject(
                                    k
                                ).getString(
                                    "created_on"
                                )
                            employersModel.boost_count =
                                if (employers.getJSONObject(k)
                                        .isNull("boost_count")
                                ) 0 else employers.getJSONObject(
                                    k
                                ).getInt(
                                    "boost_count"
                                )
                            employersModel.employer_role =
                                if (employers.getJSONObject(k)
                                        .isNull("employer_role")
                                ) "" else employers.getJSONObject(
                                    k
                                ).getString("employer_role")
                            employersModel.id =
                                if (employers.getJSONObject(k)
                                        .isNull("id")
                                ) 0 else employers.getJSONObject(
                                    k
                                ).getInt("id")
                            employersModel.share_job_access =
                                if (employers.getJSONObject(k)
                                        .isNull("share_job_access")
                                ) false else employers.getJSONObject(
                                    k
                                ).getBoolean("share_job_access")
                            employersModel.notification_type =
                                if (employers.getJSONObject(k)
                                        .isNull("notification_type")
                                ) "" else employers.getJSONObject(
                                    k
                                ).getString("notification_type")
                            employersModel.package_job_count =
                                if (employers.getJSONObject(k)
                                        .isNull("package_job_count")
                                ) 0 else employers.getJSONObject(
                                    k
                                ).getInt("package_job_count")
                            employersModel.email_verified =
                                if (employers.getJSONObject(k)
                                        .isNull("email_verified")
                                ) 0 else employers.getJSONObject(
                                    k
                                ).getInt("email_verified")
                            employersModel.job_count =
                                if (employers.getJSONObject(k)
                                        .isNull("job_count")
                                ) 0 else employers.getJSONObject(
                                    k
                                ).getInt(
                                    "job_count"
                                )
                            employersModel.application_access =
                                if (employers.getJSONObject(k)
                                        .isNull("application_access")
                                ) false else employers.getJSONObject(
                                    k
                                ).getBoolean("application_access")
                            employersModel.user_id =
                                if (employers.getJSONObject(k)
                                        .isNull("user_id")
                                ) 0 else employers.getJSONObject(
                                    k
                                ).getInt(
                                    "user_id"
                                )
                            employersModel.subscription =
                                if (employers.getJSONObject(k)
                                        .isNull("subscription")
                                ) 0 else employers.getJSONObject(
                                    k
                                ).getInt(
                                    "subscription"
                                )
                            employersModel.paid =
                                if (employers.getJSONObject(k)
                                        .isNull("paid")
                                ) false else employers.getJSONObject(
                                    k
                                ).getBoolean(
                                    "paid"
                                )
                            employersModel.deleted =
                                if (employers.getJSONObject(k)
                                        .isNull("deleted")
                                ) false else employers.getJSONObject(
                                    k
                                ).getBoolean(
                                    "deleted"
                                )
                            employersModel.transfer_job_access =
                                if (employers.getJSONObject(k)
                                        .isNull("transfer_job_access")
                                ) false else employers.getJSONObject(
                                    k
                                ).getBoolean("transfer_job_access")
                            employersModel.employer_status =
                                if (employers.getJSONObject(k)
                                        .isNull("employer_status")
                                ) "" else employers.getJSONObject(
                                    k
                                ).getString("employer_status")
                            employersModel.company_id =
                                if (employers.getJSONObject(k)
                                        .isNull("company_id")
                                ) 0 else employers.getJSONObject(
                                    k
                                ).getInt(
                                    "company_id"
                                )
                            employersModel.modified_on =
                                if (employers.getJSONObject(k)
                                        .isNull("modified_on")
                                ) "" else employers.getJSONObject(
                                    k
                                ).getString(
                                    "modified_on"
                                )
                            employersdata.add(employersModel)
                        }
                    }
                    model.employers = employersdata

                    //Images

                    val imagesModel: CompanyImages = CompanyImages()
                    var companyImages: JSONObject
                    var about_usdata: ArrayList<ImagesData> = ArrayList()
                    var featured_imagesdata: ArrayList<ImagesData> = ArrayList()
                    var culturedata: ArrayList<ImagesData> = ArrayList()
                    var diversitydata: ArrayList<ImagesData> = ArrayList()
                    if (json_objectdetail.isNull("images")) {
                    } else {
                        companyImages = json_objectdetail.getJSONObject("images")
                        var about_us: JSONArray
                        if (companyImages.isNull("about_us")) {
                        } else {
                            about_us = companyImages.getJSONArray("about_us")
                            for (k in 0 until about_us.length()) {
                                val about_usModel: ImagesData = ImagesData()
                                about_usModel.created_on =
                                    if (about_us.getJSONObject(k)
                                            .isNull("created_on")
                                    ) "" else about_us.getJSONObject(
                                        k
                                    ).getString(
                                        "created_on"
                                    )
                                about_usModel.id =
                                    if (about_us.getJSONObject(k)
                                            .isNull("id")
                                    ) 0 else about_us.getJSONObject(
                                        k
                                    ).getInt(
                                        "id"
                                    )
                                about_usModel.modified_on =
                                    if (about_us.getJSONObject(k)
                                            .isNull("modified_on")
                                    ) "" else about_us.getJSONObject(
                                        k
                                    ).getString("modified_on")
                                about_usModel.image_category =
                                    if (about_us.getJSONObject(k)
                                            .isNull("image_category")
                                    ) "" else about_us.getJSONObject(
                                        k
                                    ).getString("image_category")
                                about_usModel.image_url =
                                    if (about_us.getJSONObject(k)
                                            .isNull("image_url")
                                    ) "" else about_us.getJSONObject(
                                        k
                                    ).getString(
                                        "image_url"
                                    )
                                about_usModel.company_id =
                                    if (about_us.getJSONObject(k)
                                            .isNull("company_id")
                                    ) 0 else about_us.getJSONObject(
                                        k
                                    ).getInt(
                                        "company_id"
                                    )
                                about_usdata.add(about_usModel)
                            }
                        }

                        var featured_images: JSONArray
                        if (companyImages.isNull("featured_images")) {
                        } else {
                            featured_images = companyImages.getJSONArray("featured_images")
                            for (k in 0 until featured_images.length()) {
                                val featured_imagesModel: ImagesData = ImagesData()
                                featured_imagesModel.created_on =
                                    if (featured_images.getJSONObject(k)
                                            .isNull("created_on")
                                    ) "" else featured_images.getJSONObject(
                                        k
                                    ).getString("created_on")
                                featured_imagesModel.id =
                                    if (featured_images.getJSONObject(k)
                                            .isNull("id")
                                    ) 0 else featured_images.getJSONObject(
                                        k
                                    ).getInt("id")
                                featured_imagesModel.modified_on =
                                    if (featured_images.getJSONObject(k)
                                            .isNull("modified_on")
                                    ) "" else featured_images.getJSONObject(
                                        k
                                    ).getString("modified_on")
                                featured_imagesModel.image_category =
                                    if (featured_images.getJSONObject(k)
                                            .isNull("image_category")
                                    ) "" else featured_images.getJSONObject(
                                        k
                                    ).getString("image_category")
                                featured_imagesModel.image_url =
                                    if (featured_images.getJSONObject(k)
                                            .isNull("image_url")
                                    ) "" else featured_images.getJSONObject(
                                        k
                                    ).getString("image_url")
                                featured_imagesModel.company_id =
                                    if (featured_images.getJSONObject(k)
                                            .isNull("company_id")
                                    ) 0 else featured_images.getJSONObject(
                                        k
                                    ).getInt("company_id")
                                featured_imagesdata.add(featured_imagesModel)
                            }
                        }

                        var culture: JSONArray
                        if (companyImages.isNull("culture")) {
                        } else {
                            culture = companyImages.getJSONArray("culture")
                            for (k in 0 until culture.length()) {
                                val cultureModel: ImagesData = ImagesData()
                                cultureModel.created_on =
                                    if (culture.getJSONObject(k)
                                            .isNull("created_on")
                                    ) "" else culture.getJSONObject(
                                        k
                                    ).getString(
                                        "created_on"
                                    )
                                cultureModel.id =
                                    if (culture.getJSONObject(k)
                                            .isNull("id")
                                    ) 0 else culture.getJSONObject(
                                        k
                                    ).getInt("id")
                                cultureModel.modified_on =
                                    if (culture.getJSONObject(k)
                                            .isNull("modified_on")
                                    ) "" else culture.getJSONObject(
                                        k
                                    ).getString(
                                        "modified_on"
                                    )
                                cultureModel.image_category =
                                    if (culture.getJSONObject(k)
                                            .isNull("image_category")
                                    ) "" else culture.getJSONObject(
                                        k
                                    ).getString("image_category")
                                cultureModel.image_url =
                                    if (culture.getJSONObject(k)
                                            .isNull("image_url")
                                    ) "" else culture.getJSONObject(
                                        k
                                    ).getString(
                                        "image_url"
                                    )
                                cultureModel.company_id =
                                    if (culture.getJSONObject(k)
                                            .isNull("company_id")
                                    ) 0 else culture.getJSONObject(
                                        k
                                    ).getInt(
                                        "company_id"
                                    )
                                culturedata.add(cultureModel)
                            }
                        }

                        var diversity: JSONArray
                        if (companyImages.isNull("diversity")) {
                        } else {
                            diversity = companyImages.getJSONArray("diversity")
                            for (k in 0 until diversity.length()) {
                                val diversityModel: ImagesData = ImagesData()
                                diversityModel.created_on =
                                    if (diversity.getJSONObject(k)
                                            .isNull("created_on")
                                    ) "" else diversity.getJSONObject(
                                        k
                                    ).getString("created_on")
                                diversityModel.id =
                                    if (diversity.getJSONObject(k)
                                            .isNull("id")
                                    ) 0 else diversity.getJSONObject(
                                        k
                                    ).getInt(
                                        "id"
                                    )
                                diversityModel.modified_on =
                                    if (diversity.getJSONObject(k)
                                            .isNull("modified_on")
                                    ) "" else diversity.getJSONObject(
                                        k
                                    ).getString("modified_on")
                                diversityModel.image_category =
                                    if (diversity.getJSONObject(k)
                                            .isNull("image_category")
                                    ) "" else diversity.getJSONObject(
                                        k
                                    ).getString("image_category")
                                diversityModel.image_url =
                                    if (diversity.getJSONObject(k)
                                            .isNull("image_url")
                                    ) "" else diversity.getJSONObject(
                                        k
                                    ).getString("image_url")
                                diversityModel.company_id =
                                    if (diversity.getJSONObject(k)
                                            .isNull("company_id")
                                    ) 0 else diversity.getJSONObject(
                                        k
                                    ).getInt("company_id")
                                diversitydata.add(diversityModel)
                            }
                        }
                    }
                    imagesModel.about_us = about_usdata
                    imagesModel.featured_images = featured_imagesdata
                    imagesModel.culture = culturedata
                    imagesModel.diversity = diversitydata

                    model.images = imagesModel

                    //Followers
                    model.followers =
                        if (json_objectdetail.isNull("followers")) 0 else json_objectdetail.getInt(
                            "followers"
                        )

                    //Groups

                    var groups: JSONArray
                    var groupsdata: ArrayList<CompanyGroups> = ArrayList()
                    if (json_objectdetail.isNull("groups")) {
                    } else {
                        groups = json_objectdetail.getJSONArray("groups")

                        for (k in 0 until groups.length()) {

                            val groupsModel: CompanyGroups = CompanyGroups()
                            groupsModel.id =
                                if (groups.getJSONObject(k)
                                        .isNull("id")
                                ) 0 else groups.getJSONObject(
                                    k
                                ).getInt("id")
                            groupsModel.name =
                                if (groups.getJSONObject(k)
                                        .isNull("name")
                                ) "" else groups.getJSONObject(
                                    k
                                ).getString("name")
                            groupsModel.icon_url =
                                if (groups.getJSONObject(k)
                                        .isNull("icon_url")
                                ) "" else groups.getJSONObject(
                                    k
                                ).getString(
                                    "icon_url"
                                )
                            groupsModel.banner_url =
                                if (groups.getJSONObject(k)
                                        .isNull("banner_url")
                                ) "" else groups.getJSONObject(
                                    k
                                ).getString(
                                    "banner_url"
                                )
                            groupsModel.excerpt =
                                if (groups.getJSONObject(k)
                                        .isNull("excerpt")
                                ) "" else groups.getJSONObject(
                                    k
                                ).getString(
                                    "excerpt"
                                )
                            groupsModel.description =
                                if (groups.getJSONObject(k)
                                        .isNull("description")
                                ) "" else groups.getJSONObject(
                                    k
                                ).getString(
                                    "description"
                                )
                            groupsModel.group_type =
                                if (groups.getJSONObject(k)
                                        .isNull("group_type")
                                ) "" else groups.getJSONObject(
                                    k
                                ).getString(
                                    "group_type"
                                )
                            groupsModel.opened_to =
                                if (groups.getJSONObject(k)
                                        .isNull("opened_to")
                                ) "" else groups.getJSONObject(
                                    k
                                ).getString(
                                    "opened_to"
                                )
                            groupsModel.group_creation =
                                if (groups.getJSONObject(k)
                                        .isNull("group_creation")
                                ) "" else groups.getJSONObject(
                                    k
                                ).getString(
                                    "group_creation"
                                )
                            groupsModel.group_assocation =
                                if (groups.getJSONObject(k)
                                        .isNull("group_assocation")
                                ) "" else groups.getJSONObject(
                                    k
                                ).getString(
                                    "group_assocation"
                                )
                            groupsModel.created_by =
                                if (groups.getJSONObject(k)
                                        .isNull("created_by")
                                ) 0 else groups.getJSONObject(
                                    k
                                ).getInt(
                                    "created_by"
                                )
                            groupsModel.visiblity_type =
                                if (groups.getJSONObject(k)
                                        .isNull("visiblity_type")
                                ) "" else groups.getJSONObject(
                                    k
                                ).getString(
                                    "visiblity_type"
                                )
                            groupsModel.no_of_members =
                                if (groups.getJSONObject(k)
                                        .isNull("no_of_members")
                                ) 0 else groups.getJSONObject(
                                    k
                                ).getInt(
                                    "no_of_members"
                                )
                            groupsModel.notification =
                                if (groups.getJSONObject(k)
                                        .isNull("notification")
                                ) false else groups.getJSONObject(
                                    k
                                ).getBoolean(
                                    "notification"
                                )
                            groupsModel.featured =
                                if (groups.getJSONObject(k)
                                        .isNull("featured")
                                ) false else groups.getJSONObject(
                                    k
                                ).getBoolean(
                                    "featured"
                                )
                            groupsModel.feature_start_date =
                                if (groups.getJSONObject(k)
                                        .isNull("feature_start_date")
                                ) "" else groups.getJSONObject(
                                    k
                                ).getString(
                                    "feature_start_date"
                                )
                            groupsModel.feature_end_date =
                                if (groups.getJSONObject(k)
                                        .isNull("feature_end_date")
                                ) "" else groups.getJSONObject(
                                    k
                                ).getString(
                                    "feature_end_date"
                                )
                            groupsModel.status =
                                if (groups.getJSONObject(k)
                                        .isNull("status")
                                ) "" else groups.getJSONObject(
                                    k
                                ).getString(
                                    "status"
                                )
                            groupsModel.categories =
                                if (groups.getJSONObject(k)
                                        .isNull("categories")
                                ) "" else groups.getJSONObject(
                                    k
                                ).getString(
                                    "categories"
                                )
                            groupsModel.cities =
                                if (groups.getJSONObject(k)
                                        .isNull("cities")
                                ) "" else groups.getJSONObject(
                                    k
                                ).getString(
                                    "cities"
                                )
                            groupsModel.notes =
                                if (groups.getJSONObject(k)
                                        .isNull("notes")
                                ) "" else groups.getJSONObject(
                                    k
                                ).getString("notes")
                            groupsModel.phone_number =
                                if (groups.getJSONObject(k)
                                        .isNull("phone_number")
                                ) "" else groups.getJSONObject(
                                    k
                                ).getString(
                                    "phone_number"
                                )
                            groupsModel.external_group =
                                if (groups.getJSONObject(k)
                                        .isNull("external_group")
                                ) 0 else groups.getJSONObject(
                                    k
                                ).getInt(
                                    "external_group"
                                )
                            groupsModel.created_on =
                                if (groups.getJSONObject(k)
                                        .isNull("created_on")
                                ) "" else groups.getJSONObject(
                                    k
                                ).getString(
                                    "created_on"
                                )
                            groupsModel.modified_on =
                                if (groups.getJSONObject(k)
                                        .isNull("modified_on")
                                ) "" else groups.getJSONObject(
                                    k
                                ).getString(
                                    "modified_on"
                                )
                            groupsdata.add(groupsModel)
                        }
                    }
                    model.groups = groupsdata

                    //Videos
                    var videos: JSONArray
                    var videosdata: ArrayList<CompanyVideos> = ArrayList()
                    if (json_objectdetail.isNull("videos")) {
                    } else {
                        videos = json_objectdetail.getJSONArray("videos")

                        for (k in 0 until videos.length()) {

                            val videosModel: CompanyVideos = CompanyVideos()
                            videosModel.created_by =
                                if (videos.getJSONObject(k)
                                        .isNull("created_by")
                                ) 0 else videos.getJSONObject(
                                    k
                                ).getInt(
                                    "created_by"
                                )
                            videosModel.name =
                                if (videos.getJSONObject(k)
                                        .isNull("name")
                                ) "" else videos.getJSONObject(
                                    k
                                ).getString("name")
                            videosModel.created_on =
                                if (videos.getJSONObject(k)
                                        .isNull("created_on")
                                ) "" else videos.getJSONObject(
                                    k
                                ).getString(
                                    "created_on"
                                )
                            videosModel.id =
                                if (videos.getJSONObject(k)
                                        .isNull("id")
                                ) 0 else videos.getJSONObject(
                                    k
                                ).getInt("id")
                            videosModel.modified_on =
                                if (videos.getJSONObject(k)
                                        .isNull("modified_on")
                                ) "" else videos.getJSONObject(
                                    k
                                ).getString(
                                    "modified_on"
                                )
                            videosModel.video_url =
                                if (videos.getJSONObject(k)
                                        .isNull("video_url")
                                ) "" else videos.getJSONObject(
                                    k
                                ).getString(
                                    "video_url"
                                )
                            videosModel.description =
                                if (videos.getJSONObject(k)
                                        .isNull("description")
                                ) "" else videos.getJSONObject(
                                    k
                                ).getString(
                                    "description"
                                )
                            videosModel.company_id =
                                if (videos.getJSONObject(k)
                                        .isNull("company_id")
                                ) 0 else videos.getJSONObject(
                                    k
                                ).getInt(
                                    "company_id"
                                )
                            videosdata.add(videosModel)
                        }
                    }
                    model.videos = videosdata

                    //Events display
                    var events: JSONArray
                    var eventsdata: ArrayList<Events> = ArrayList()
                    //   Log.d("TAGG", "EVENTS"+json_objectdetail.getJSONArray("events"))
                    if (json_objectdetail.isNull("events")) {
                    } else {
                        events = json_objectdetail.getJSONArray("events")
                        for (k in 0 until events.length()) {

                            val eventsModel: Events = Events()

                            var locations: JSONArray
                            var locdata: ArrayList<EventLocation> = ArrayList()
                            if (events.getJSONObject(k).isNull("events_locations")) {
                            } else {
                                locations = events.getJSONObject(k).getJSONArray("events_locations")

                                for (k in 0 until locations.length()) {
                                    val locModel: EventLocation = EventLocation()
                                    locModel.pincode =
                                        if (locations.getJSONObject(k)
                                                .isNull("pincode")
                                        ) "" else locations.getJSONObject(
                                            k
                                        ).getString("pincode")
                                    locModel.discounted_price =
                                        if (locations.getJSONObject(k)
                                                .isNull("discounted_price")
                                        ) 0 else locations.getJSONObject(
                                            k
                                        ).getInt("discounted_price")
                                    locModel.event_register_start_date_time =
                                        if (locations.getJSONObject(k)
                                                .isNull("event_register_start_date_time")
                                        ) "" else locations.getJSONObject(
                                            k
                                        ).getString("event_register_start_date_time")
                                    locModel.event_type =
                                        if (locations.getJSONObject(k)
                                                .isNull("event_type")
                                        ) "" else locations.getJSONObject(
                                            k
                                        ).getString("event_type")
                                    locModel.event_start_date_time =
                                        if (locations.getJSONObject(k)
                                                .isNull("event_start_date_time")
                                        ) "" else locations.getJSONObject(
                                            k
                                        ).getString("event_start_date_time")
                                    locModel.amount =
                                        if (locations.getJSONObject(k)
                                                .isNull("amount")
                                        ) 0 else locations.getJSONObject(
                                            k
                                        ).getInt("amount")
                                    locModel.discount_start_date_time =
                                        if (locations.getJSONObject(k)
                                                .isNull("discount_start_date_time")
                                        ) "" else locations.getJSONObject(
                                            k
                                        ).getString("discount_start_date_time")
                                    locModel.event_register_end_date_time =
                                        if (locations.getJSONObject(k)
                                                .isNull("event_register_end_date_time")
                                        ) "" else locations.getJSONObject(
                                            k
                                        ).getString("event_register_end_date_time")
                                    locModel.discount_end_date_time =
                                        if (locations.getJSONObject(k)
                                                .isNull("discount_end_date_time")
                                        ) "" else locations.getJSONObject(
                                            k
                                        ).getString("discount_end_date_time")
                                    locModel.discount_active =
                                        if (locations.getJSONObject(k)
                                                .isNull("discount_active")
                                        ) "" else locations.getJSONObject(
                                            k
                                        ).getString("discount_active")
                                    locModel.address =
                                        if (locations.getJSONObject(k)
                                                .isNull("address")
                                        ) "" else locations.getJSONObject(
                                            k
                                        ).getString("address")
                                    locModel.event_end_date_time =
                                        if (locations.getJSONObject(k)
                                                .isNull("event_end_date_time")
                                        ) "" else locations.getJSONObject(
                                            k
                                        ).getString("event_end_date_time")

                                    locModel.id =
                                        if (locations.getJSONObject(k)
                                                .isNull("id")
                                        ) 0 else locations.getJSONObject(
                                            k
                                        ).getInt("id")
                                    locModel.state =
                                        if (locations.getJSONObject(k)
                                                .isNull("state")
                                        ) "" else locations.getJSONObject(
                                            k
                                        ).getString("state")
                                    locModel.seats =
                                        if (locations.getJSONObject(k)
                                                .isNull("seats")
                                        ) "" else locations.getJSONObject(
                                            k
                                        ).getString("seats")
                                    locModel.event_id =
                                        if (locations.getJSONObject(k)
                                                .isNull("event_id")
                                        ) 0 else locations.getJSONObject(
                                            k
                                        ).getInt("event_id")
                                    locModel.country =
                                        if (locations.getJSONObject(k)
                                                .isNull("country")
                                        ) "" else locations.getJSONObject(
                                            k
                                        ).getString("country")
                                    locModel.registration_open =
                                        if (locations.getJSONObject(k)
                                                .isNull("registration_open")
                                        ) false else locations.getJSONObject(
                                            k
                                        ).getBoolean("registration_open")
                                    locModel.google_map_url =
                                        if (locations.getJSONObject(k)
                                                .isNull("google_map_url")
                                        ) "" else locations.getJSONObject(
                                            k
                                        ).getString("google_map_url")
                                    locModel.city =
                                        if (locations.getJSONObject(k)
                                                .isNull("city")
                                        ) "" else locations.getJSONObject(
                                            k
                                        ).getString("city")

                                    locdata.add(locModel)
                                }

                            }
                            eventsModel.event_locations = locdata
                            eventsModel.thumbnail_url = if (events.getJSONObject(k)
                                    .isNull("thumbnail_url")
                            ) "" else events.getJSONObject(
                                k
                            ).getString(
                                "thumbnail_url"
                            )
                            eventsModel.interested =
                                if (events.getJSONObject(k)
                                        .isNull("interested")
                                ) false else events.getJSONObject(
                                    k
                                ).getBoolean(
                                    "interested"
                                )
                            eventsModel.registered =
                                if (events.getJSONObject(k)
                                        .isNull("registered")
                                ) false else events.getJSONObject(
                                    k
                                ).getBoolean(
                                    "registered"
                                )
                            eventsModel.event_category =
                                if (events.getJSONObject(k)
                                        .isNull("events_category")
                                ) "" else events.getJSONObject(
                                    k
                                ).getString(
                                    "events_category"
                                )
                            eventsModel.created_on =
                                if (events.getJSONObject(k)
                                        .isNull("created_on")
                                ) "" else events.getJSONObject(
                                    k
                                ).getString(
                                    "created_on"
                                )
                            eventsModel.is_online =
                                if (events.getJSONObject(k)
                                        .isNull("is_online")
                                ) false else events.getJSONObject(
                                    k
                                ).getBoolean(
                                    "is_online"
                                )
                            eventsModel.description =
                                if (events.getJSONObject(k)
                                        .isNull("description")
                                ) "" else events.getJSONObject(
                                    k
                                ).getString(
                                    "description"
                                )
                            eventsModel.share_count =
                                if (events.getJSONObject(k)
                                        .isNull("share_count")
                                ) 0 else events.getJSONObject(
                                    k
                                ).getInt(
                                    "share_count"
                                )
                            eventsModel.excerpt =
                                if (events.getJSONObject(k)
                                        .isNull("excerpt")
                                ) "" else events.getJSONObject(
                                    k
                                ).getString(
                                    "excerpt"
                                )
                            eventsModel.created_by =
                                if (events.getJSONObject(k)
                                        .isNull("created_by")
                                ) 0 else events.getJSONObject(
                                    k
                                ).getInt(
                                    "created_by"
                                )
                            eventsModel.event_url =
                                if (events.getJSONObject(k)
                                        .isNull("event_url")
                                ) "" else events.getJSONObject(
                                    k
                                ).getString(
                                    "event_url"
                                )
                            eventsModel.address =
                                if (events.getJSONObject(k)
                                        .isNull("address")
                                ) "" else events.getJSONObject(
                                    k
                                ).getString(
                                    "address"
                                )
                            eventsModel.event_end_date_time =
                                if (events.getJSONObject(k)
                                        .isNull("event_end_date_time")
                                ) "" else events.getJSONObject(
                                    k
                                ).getString("event_end_date_time")
                            eventsModel.status =
                                if (events.getJSONObject(k)
                                        .isNull("status")
                                ) "" else events.getJSONObject(
                                    k
                                ).getString(
                                    "status"
                                )
                            eventsModel.event_start_date_time =
                                if (events.getJSONObject(k)
                                        .isNull("event_start_date_time")
                                ) "" else events.getJSONObject(
                                    k
                                ).getString("event_start_date_time")
                            eventsModel.view_count =
                                if (events.getJSONObject(k)
                                        .isNull("view_count")
                                ) 0 else events.getJSONObject(
                                    k
                                ).getInt(
                                    "view_count"
                                )
                            eventsModel.register_count =
                                if (events.getJSONObject(k)
                                        .isNull("register_count")
                                ) "" else events.getJSONObject(
                                    k
                                ).getString(
                                    "register_count"
                                )
                            eventsModel.event_register_end_date_time =
                                if (events.getJSONObject(k)
                                        .isNull("event_register_end_date_time")
                                ) "" else events.getJSONObject(
                                    k
                                ).getString("event_register_end_date_time")
                            eventsModel.company_id =
                                if (events.getJSONObject(k)
                                        .isNull("company_id")
                                ) 0 else events.getJSONObject(
                                    k
                                ).getInt(
                                    "company_id"
                                )
                            eventsModel.interested_count =
                                if (events.getJSONObject(k)
                                        .isNull("interested_count")
                                ) 0 else events.getJSONObject(
                                    k
                                ).getInt(
                                    "interested_count"
                                )
                            eventsModel.event_by =
                                if (events.getJSONObject(k)
                                        .isNull("event_by")
                                ) "" else events.getJSONObject(
                                    k
                                ).getString(
                                    "event_by"
                                )
                            eventsModel.payment_note =
                                if (events.getJSONObject(k)
                                        .isNull("payment_note")
                                ) "" else events.getJSONObject(
                                    k
                                ).getString(
                                    "payment_note"
                                )
                            eventsModel.modified_by =
                                if (events.getJSONObject(k)
                                        .isNull("modified_by")
                                ) "" else events.getJSONObject(
                                    k
                                ).getString(
                                    "modified_by"
                                )
                            eventsModel.priority_order =
                                if (events.getJSONObject(k)
                                        .isNull("priority_order")
                                ) false else events.getJSONObject(
                                    k
                                ).getBoolean(
                                    "priority_order"
                                )
                            eventsModel.is_private =
                                if (events.getJSONObject(k)
                                        .isNull("is_private")
                                ) false else events.getJSONObject(
                                    k
                                ).getBoolean(
                                    "is_private"
                                )
                            eventsModel.slug =
                                if (events.getJSONObject(k)
                                        .isNull("slug")
                                ) "" else events.getJSONObject(
                                    k
                                ).getString("slug")
                            eventsModel.image_url =
                                if (events.getJSONObject(k)
                                        .isNull("image_url")
                                ) "" else events.getJSONObject(
                                    k
                                ).getString(
                                    "image_url"
                                )
                            eventsModel.author_name =
                                if (events.getJSONObject(k)
                                        .isNull("author_name")
                                ) "" else events.getJSONObject(
                                    k
                                ).getString(
                                    "author_name"
                                )
                            eventsModel.title =
                                if (events.getJSONObject(k)
                                        .isNull("title")
                                ) "" else events.getJSONObject(
                                    k
                                ).getString("title")

                            eventsModel.gtm_id =
                                if (events.getJSONObject(k)
                                        .isNull("gtm_id")
                                ) 0 else events.getJSONObject(
                                    k
                                ).getInt("gtm_id")
                            eventsModel.id =
                                if (events.getJSONObject(k)
                                        .isNull("id")
                                ) 0 else events.getJSONObject(
                                    k
                                ).getInt("id")
                            eventsModel.publish_date =
                                if (events.getJSONObject(k)
                                        .isNull("publish_date")
                                ) "" else events.getJSONObject(
                                    k
                                ).getString(
                                    "publish_date"
                                )
                            eventsModel.modified_on =
                                if (events.getJSONObject(k)
                                        .isNull("modified_on")
                                ) "" else events.getJSONObject(
                                    k
                                ).getString(
                                    "modified_on"
                                )
                            eventsModel.city =
                                if (events.getJSONObject(k)
                                        .isNull("city")
                                ) "" else events.getJSONObject(
                                    k
                                ).getString("city")
                            eventsModel.show_on_search =
                                if (events.getJSONObject(k)
                                        .isNull("show_on_search")
                                ) false else events.getJSONObject(
                                    k
                                ).getBoolean(
                                    "show_on_search"
                                )
                            eventsModel.author_id =
                                if (events.getJSONObject(k)
                                        .isNull("author_id")
                                ) "" else events.getJSONObject(
                                    k
                                ).getString(
                                    "author_id"
                                )
                            eventsModel.event_register_start_date_time =
                                if (events.getJSONObject(k)
                                        .isNull("event_register_start_date_time")
                                ) "" else events.getJSONObject(
                                    k
                                ).getString("event_register_start_date_time")
                            eventsModel.payment =
                                if (events.getJSONObject(k)
                                        .isNull("payment")
                                ) false else events.getJSONObject(
                                    k
                                ).getBoolean(
                                    "payment"
                                )
                            eventsModel.post_for =
                                if (events.getJSONObject(k)
                                        .isNull("post_for")
                                ) "" else events.getJSONObject(
                                    k
                                ).getString(
                                    "post_for"
                                )
                            eventsdata.add(eventsModel)

                        }
                    }
                    model.events = eventsdata

                    //Testimonials
                    var testimonials: JSONArray
                    var testimonialsdata: ArrayList<Testimonials> = ArrayList()
                    if (json_objectdetail.isNull("testimonials")) {
                    } else {
                        testimonials = json_objectdetail.getJSONArray("testimonials")

                        for (k in 0 until testimonials.length()) {

                            val testimonialsModel: Testimonials = Testimonials()
                            testimonialsModel.created_on =
                                if (testimonials.getJSONObject(k)
                                        .isNull("created_on")
                                ) "" else testimonials.getJSONObject(
                                    k
                                ).getString("created_on")
                            testimonialsModel.name =
                                if (testimonials.getJSONObject(k)
                                        .isNull("name")
                                ) "" else testimonials.getJSONObject(
                                    k
                                ).getString(
                                    "name"
                                )
                            testimonialsModel.created_by =
                                if (testimonials.getJSONObject(k)
                                        .isNull("created_by")
                                ) 0 else testimonials.getJSONObject(
                                    k
                                ).getInt("created_by")
                            testimonialsModel.id =
                                if (testimonials.getJSONObject(k)
                                        .isNull("id")
                                ) 0 else testimonials.getJSONObject(
                                    k
                                ).getInt(
                                    "id"
                                )
                            testimonialsModel.modified_on =
                                if (testimonials.getJSONObject(k)
                                        .isNull("modified_on")
                                ) "" else testimonials.getJSONObject(
                                    k
                                ).getString("modified_on")
                            testimonialsModel.company_name =
                                if (testimonials.getJSONObject(k)
                                        .isNull("company_name")
                                ) "" else testimonials.getJSONObject(
                                    k
                                ).getString("company_name")
                            testimonialsModel.designation =
                                if (testimonials.getJSONObject(k)
                                        .isNull("designation")
                                ) "" else testimonials.getJSONObject(
                                    k
                                ).getString("designation")
                            testimonialsModel.image_icon =
                                if (testimonials.getJSONObject(k)
                                        .isNull("image_icon")
                                ) "" else testimonials.getJSONObject(
                                    k
                                ).getString("image_icon")
                            testimonialsModel.company_id =
                                if (testimonials.getJSONObject(k)
                                        .isNull("company_id")
                                ) 0 else testimonials.getJSONObject(
                                    k
                                ).getInt("company_id")
                            testimonialsModel.testimonial =
                                if (testimonials.getJSONObject(k)
                                        .isNull("testimonial")
                                ) "" else testimonials.getJSONObject(
                                    k
                                ).getString("testimonial")
                            testimonialsdata.add(testimonialsModel)
                        }
                    }
                    model.testimonials = testimonialsdata

                    //Blogs
                    var blogs: JSONArray
                    var blogsdata: ArrayList<Blogs> = ArrayList()
                    if (json_objectdetail.isNull("blogs")) {
                    } else {
                        blogs = json_objectdetail.getJSONArray("blogs")

                        for (k in 0 until blogs.length()) {

                            val blogsModel: Blogs = Blogs()
                            blogsModel.created_on =
                                if (blogs.getJSONObject(k)
                                        .isNull("created_on")
                                ) "" else blogs.getJSONObject(
                                    k
                                ).getString(
                                    "created_on"
                                )
                            blogsModel.blog_view =
                                if (blogs.getJSONObject(k)
                                        .isNull("blog_view")
                                ) "" else blogs.getJSONObject(
                                    k
                                ).getString(
                                    "blog_view"
                                )
                            blogsModel.description =
                                if (blogs.getJSONObject(k)
                                        .isNull("description")
                                ) "" else blogs.getJSONObject(
                                    k
                                ).getString(
                                    "description"
                                )
                            blogsModel.modified_id =
                                if (blogs.getJSONObject(k)
                                        .isNull("modified_id")
                                ) "" else blogs.getJSONObject(
                                    k
                                ).getString(
                                    "modified_id"
                                )
                            blogsModel.excerpt =
                                if (blogs.getJSONObject(k)
                                        .isNull("excerpt")
                                ) "" else blogs.getJSONObject(
                                    k
                                ).getString("excerpt")
                            blogsModel.created_by =
                                if (blogs.getJSONObject(k)
                                        .isNull("created_by")
                                ) 0 else blogs.getJSONObject(
                                    k
                                ).getInt("created_by")
                            blogsModel.blog_link =
                                if (blogs.getJSONObject(k)
                                        .isNull("blog_link")
                                ) "" else blogs.getJSONObject(
                                    k
                                ).getString(
                                    "blog_link"
                                )
                            blogsModel.status =
                                if (blogs.getJSONObject(k)
                                        .isNull("status")
                                ) "" else blogs.getJSONObject(
                                    k
                                ).getString("status")
                            blogsModel.company_id =
                                if (blogs.getJSONObject(k)
                                        .isNull("company_id")
                                ) 0 else blogs.getJSONObject(
                                    k
                                ).getInt("company_id")
                            blogsModel.slug =
                                if (blogs.getJSONObject(k)
                                        .isNull("slug")
                                ) "" else blogs.getJSONObject(
                                    k
                                ).getString("slug")
                            blogsModel.admin_id =
                                if (blogs.getJSONObject(k)
                                        .isNull("admin_id")
                                ) "" else blogs.getJSONObject(
                                    k
                                ).getString(
                                    "admin_id"
                                )
                            blogsModel.image_url =
                                if (blogs.getJSONObject(k)
                                        .isNull("image_url")
                                ) "" else blogs.getJSONObject(
                                    k
                                ).getString(
                                    "image_url"
                                )
                            blogsModel.author_name =
                                if (blogs.getJSONObject(k)
                                        .isNull("author_name")
                                ) "" else blogs.getJSONObject(
                                    k
                                ).getString(
                                    "author_name"
                                )
                            blogsModel.category =
                                if (blogs.getJSONObject(k)
                                        .isNull("category")
                                ) "" else blogs.getJSONObject(
                                    k
                                ).getString(
                                    "category"
                                )
                            blogsModel.title =
                                if (blogs.getJSONObject(k)
                                        .isNull("title")
                                ) "" else blogs.getJSONObject(
                                    k
                                ).getString("title")
                            blogsModel.blog_share =
                                if (blogs.getJSONObject(k)
                                        .isNull("blog_share")
                                ) "" else blogs.getJSONObject(
                                    k
                                ).getString(
                                    "blog_share"
                                )
                            blogsModel.id =
                                if (blogs.getJSONObject(k).isNull("id")) 0 else blogs.getJSONObject(
                                    k
                                ).getInt("id")
                            blogsModel.publish_date =
                                if (blogs.getJSONObject(k)
                                        .isNull("publish_date")
                                ) "" else blogs.getJSONObject(
                                    k
                                ).getString(
                                    "publish_date"
                                )
                            blogsModel.modified_on =
                                if (blogs.getJSONObject(k)
                                        .isNull("modified_on")
                                ) "" else blogs.getJSONObject(
                                    k
                                ).getString(
                                    "modified_on"
                                )
                            blogsModel.blog_like =
                                if (blogs.getJSONObject(k)
                                        .isNull("blog_like")
                                ) "" else blogs.getJSONObject(
                                    k
                                ).getString(
                                    "blog_like"
                                )
                            blogsModel.show_register_message =
                                if (blogs.getJSONObject(k)
                                        .isNull("show_register_message")
                                ) false else blogs.getJSONObject(
                                    k
                                ).getBoolean("show_register_message")
                            blogsModel.city =
                                if (blogs.getJSONObject(k)
                                        .isNull("city")
                                ) "" else blogs.getJSONObject(
                                    k
                                ).getString("city")
                            blogsModel.author_id =
                                if (blogs.getJSONObject(k)
                                        .isNull("author_id")
                                ) 0 else blogs.getJSONObject(
                                    k
                                ).getInt("author_id")
                            blogsModel.post_for =
                                if (blogs.getJSONObject(k)
                                        .isNull("post_for")
                                ) "" else blogs.getJSONObject(
                                    k
                                ).getString(
                                    "post_for"
                                )
                            blogsdata.add(blogsModel)
                        }
                    }
                    model.blogs = blogsdata

////                        companytype.setText(model.company!!.name)
                    if (model.featured == true) {
                        val msg: String = model.name + "   "
                        val mImageSpan: ImageSpan = ImageSpan(
                            applicationContext,
                            R.drawable.ic_group_featured
                        );
                        val text: SpannableString = SpannableString(msg);
                        text.setSpan(
                            mImageSpan,
                            msg.length - 1,
                            msg.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        );
                        compname.setText(text);
                    } else {
                        compname.setText(model.name)
                    }
                    complocation.setText(model.cities)
                    if (model.followers!! > 10)
                        compfollowers.setText(model.followers.toString() + " Followers")
                    else
                        compfollowers.visibility = View.GONE
                    if (model.logo!!.isNotEmpty()) {
                        Picasso.with(applicationContext)
                            .load(model.logo)
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .into(comp_logo)
                    } else {
                        Picasso.with(applicationContext)
                            .load(R.drawable.comp_default)
                            .placeholder(R.drawable.comp_default)
                            .into(comp_logo)
                    }
                    if (model.banner_image!!.isNotEmpty()) {
                        Picasso.with(applicationContext)
                            .load(model.banner_image)
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .into(banner)
                    } else {
                        Picasso.with(applicationContext)
                            .load(R.drawable.companies_banner)
                            .placeholder(R.drawable.companies_banner)
                            .into(banner)
                    }
                    if (model!!.about_us!!.length > 0) {
                        //val htmlAsSpanned = Html.fromHtml(model.about_us)
                        //var s: String = model.about_us.toString().replace(Regex("&.*?;"),"")
                        var s: String = model.about_us!!.replace("&lt;", "<")
                        var d: String = s.replace("&gt;", ">")
                            .replace("lsquo;", "'")
                            .replace("rsquo;", "'")
                            //.replace("nbsp;", " ")
                            .replace("&nbsp;", " ")
                            .replace("&amp;", "")
                        compaboutus.setText(Html.fromHtml(d))
                        compaboutus_.setText(Html.fromHtml(d))
                    } else {
                        default_aboutus.visibility = View.VISIBLE
                        compaboutus.visibility = View.GONE
                    }


                    /*  if (compaboutus.lineCount == 5)
                          seemore_aboutus.visibility = View.VISIBLE
                      else
                          seemore_aboutus.visibility = View.GONE*/

                    //  Log.d("TAGG", model.policies!!.maternity!![0]!!.policy_type)

                    comp_industry.setText(model.industry)
                    if (model.team_size.toString().length > 0) {
                        comp_size.setText(model.team_size)
                    } else {
                        comp_size.visibility = View.GONE
                        comp_size_text.visibility = View.GONE
                        size_view.visibility = View.GONE
                    }

                    if (model.established_date.toString().length > 0 && model.established_date.toString()
                            .compareTo(
                                "0"
                            ) == 1
                    ) {
                        established_date.setText(model.established_date.toString())
                    } else {
                        established_date.visibility = View.GONE
                        comp_estd_text.visibility = View.GONE
                        estd_view.visibility = View.GONE
                    }

                    if (model.active_jobs_count!! > 0) {
                        active_jobs.setText(model.active_jobs_count.toString() + " active job openings")
                    } else {
                        active_jobs.setText("No active job openings")
                        view_company.visibility = View.GONE
                    }

                    if (model?.culture!!.length > 0) {
                        /*var s :String = model.culture!!.replace("&lt;", "<")
                        var d :String = s.replace("&gt;",">")
                            .replace("lsquo;","'")
                            .replace("rsquo;","'")
                            .replace("nbsp;"," ")
                            .replace("&amp;","")
                        if(company_culture.text.length>50)
                            seemore_comculture.visibility = View.VISIBLE
                        else
                            seemore_comculture.visibility = View.GONE*/
                        setCulterDataInWebView(model?.culture!!)
                    } else {
                        company_culture.visibility = View.GONE
                        default_company_culture.visibility = View.VISIBLE
                        seemore_comculture.visibility = View.GONE
                    }


                    if (model!!.diversity!!.length > 0) {
                        var s: String = model.diversity!!.replace("&lt;", "<")
                        var d: String = s.replace("&gt;", ">")
                            .replace("lsquo;", "'")
                            .replace("rsquo;", "'")
                            .replace("nbsp;", " ")
                            .replace("&amp;", "")
                        company_diversity.setText(Html.fromHtml(d))
                        company_diversity.setText(Html.fromHtml(d))

                        //company_diversity.setText(Html.fromHtml(model.diversity))
                        if (company_diversity.text.length > 50)
                            seemore_comdiversity.visibility = View.VISIBLE
                        else
                            seemore_comdiversity.visibility = View.GONE
                    } else {
                        default_company_diversity.visibility = View.VISIBLE
                        company_diversity.visibility = View.GONE
                        seemore_comdiversity.visibility = View.GONE
                    }

                    for (i in 0 until model!!.images!!.about_us!!.size) {
                        itemList_AboutUs.add(model!!.images!!.about_us!![i].image_url!!.toString())
                        if (i < 2) {
                            itemList_AboutUs_Trimmed.add(model!!.images!!.about_us!![i].image_url!!.toString())
                            seemore_aboutus_images.visibility = View.GONE
                        } else {
                            seemore_aboutus_images.visibility = View.VISIBLE
                        }
                    }
                    Log.d(
                        "TAGG",
                        itemList_AboutUs.size.toString() + ",,,," + itemList_AboutUs_Trimmed.size
                    )
                    var mgroupsLayoutManager = GridLayoutManager(applicationContext, 2)
                    gridview_abutus!!.layoutManager =
                        mgroupsLayoutManager as RecyclerView.LayoutManager?
                    aboutUsAdapter = ImagesAdapter(itemList_AboutUs_Trimmed, isLoggedIn)
                    gridview_abutus!!.adapter = aboutUsAdapter

                    for (i in 0 until model!!.images!!.culture!!.size) {
                        itemList_Culture.add(model!!.images!!.culture!![i].image_url!!.toString())
                        if (i < 2) {
                            itemList_Culture_Trimmed.add(model!!.images!!.culture!![i].image_url!!.toString())
                            seemore_comculture_images.visibility = View.GONE
                        } else {
                            seemore_comculture_images.visibility = View.VISIBLE
                        }
                    }

                    var mgroupsLayoutManager1 = GridLayoutManager(applicationContext, 2)
                    gridview_culture!!.layoutManager =
                        mgroupsLayoutManager1 as RecyclerView.LayoutManager?
                    cultureAdapter = ImagesAdapter(itemList_Culture_Trimmed, isLoggedIn)
                    gridview_culture!!.adapter = cultureAdapter


                    for (i in 0 until model!!.images!!.diversity!!.size) {
                        itemList_Diversity.add(model!!.images!!.diversity!![i].image_url!!.toString())
                        if (i < 2) {
                            itemList_Diversity_Trimmed.add(model!!.images!!.diversity!![i].image_url!!.toString())
                            seemore_comdiversity_images.visibility = View.GONE
                        } else {
                            seemore_comdiversity_images.visibility = View.VISIBLE
                        }
                    }
                    var mgroupsLayoutManager2 = GridLayoutManager(applicationContext, 2)
                    gridview_diversity!!.layoutManager =
                        mgroupsLayoutManager2 as RecyclerView.LayoutManager?
                    diversityAdapter = ImagesAdapter(itemList_Diversity_Trimmed, isLoggedIn)
                    gridview_diversity!!.adapter = diversityAdapter


                    seemore_maternity.visibility = View.GONE
                    seemore_paternity.visibility = View.GONE
                    seemore_childcare.visibility = View.GONE
                    seemore_harrasment.visibility = View.VISIBLE
                    seemore_transportation.visibility = View.VISIBLE
                    seemore_flexi.visibility = View.VISIBLE
                    seemore_other.visibility = View.VISIBLE

                    if (model.policies!!.maternity!!.size > 0) {
                        row_name_maternity.setText("Maternity Leave Policy")
                        if (!model.policies!!.maternity!![0]!!.description.equals(""))
                            row_text_maternity.setText(Html.fromHtml(model.policies!!.maternity!![0]!!.description))
                        if (row_text_maternity.lineCount == 3 && row_text_maternity.text.length > 50)
                            seemore_maternity.visibility = View.VISIBLE
                        else
                            seemore_maternity.visibility = View.GONE
                    }
                    if (model.policies!!.paternity!!.size > 0) {
                        row_name_paternity.setText("Paternity Leave Policy")
                        if (!model.policies!!.paternity!![0]!!.description.equals(""))
                            row_text_paternity.setText(Html.fromHtml(model.policies!!.paternity!![0]!!.description))
                        if (row_text_paternity.lineCount == 3 && row_text_paternity.text.length > 50)
                            seemore_paternity.visibility = View.VISIBLE
                        else
                            seemore_paternity.visibility = View.GONE
                    }
                    if (model.policies!!.childcare!!.size > 0) {
                        row_name_childcare.setText("Child Care Policy")
                        if (!model.policies!!.childcare!![0]!!.description.equals(""))
                            row_text_childcare.setText(Html.fromHtml(model.policies!!.childcare!![0]!!.description))
                        if (row_text_childcare.lineCount == 3 && row_text_childcare.text.length > 50)
                            seemore_childcare.visibility = View.VISIBLE
                        else
                            seemore_childcare.visibility = View.GONE
                    }
                    if (model.policies!!.harassment!!.size > 0) {
                        row_name_harrasment.setText("Harresment Policy")
                        if (!model.policies!!.harassment!![0]!!.description.equals(""))
                            row_text_harrasment.setText(Html.fromHtml(model.policies!!.harassment!![0]!!.description))
                        if (row_text_harrasment.lineCount == 3 && model.policies!!.harassment!![0]!!.description!!.length > 50)
                            seemore_harrasment.visibility = View.VISIBLE
                        else
                            seemore_harrasment.visibility = View.GONE
                    }
                    if (model.policies!!.transport!!.size > 0) {
                        row_name_transportation.setText("Transportation Policy")
                        if (!model.policies!!.transport!![0]!!.description.equals(""))
                            row_text_transportation.setText(Html.fromHtml(model.policies!!.transport!![0]!!.description))
                        if (row_text_transportation.lineCount == 3 && row_text_transportation.text.length > 50)
                            seemore_transportation.visibility = View.VISIBLE
                        else
                            seemore_transportation.visibility = View.GONE
                    }
                    if (model.policies!!.flexi!!.size > 0) {
                        row_name_flexi.setText("Flexi Option Policy")
                        if (!model.policies!!.flexi!![0]!!.description.equals(""))
                            row_text_flexi.setText(Html.fromHtml(model.policies!!.flexi!![0]!!.description))
                        if (row_text_flexi.lineCount == 3 && row_text_transportation.text.length > 50)
                            seemore_flexi.visibility = View.VISIBLE
                        else
                            seemore_flexi.visibility = View.GONE
                    }
                    if (model.policies!!.other!!.size > 0) {
                        row_name_other.setText("Other Policy")
                        if (!model.policies!!.other!![0]!!.description.equals(""))
                            row_text_other.setText(Html.fromHtml(model.policies!!.other!![0]!!.description))
                        if (row_text_other.lineCount == 3 && row_text_other.text.length > 50)
                            seemore_other.visibility = View.VISIBLE
                        else
                            seemore_other.visibility = View.GONE
                    }


                    llCard_harrasment.visibility = View.GONE
                    llCard_flexi.visibility = View.GONE
                    llCard_other.visibility = View.GONE
                    llCard_transportation.visibility = View.GONE
                    //listOfhotJobsdata.clear()
                    //var j = 0
                    if (listOfhotJobsdata.size > 0) { //model!!.jobs!!.size>0
//                        for (i in 0 until model!!.jobs!!.size) {
//                            val model_jobs: JobsView = JobsView()
//                            model_jobs.id = model!!.jobs!![i].id
//                            model_jobs.title =  model!!.jobs!![i].title
//                            model_jobs.company_name = model!!.name
//                            model_jobs.company_logo = model!!.logo
//                            model_jobs.location_name =  model!!.jobs!![i].location_name
//                            model_jobs.max_year =  model!!.jobs!![i].max_year
//                            model_jobs.min_year =  model!!.jobs!![i].min_year
//                            model_jobs.status =  model!!.jobs!![i].status
//                            model_jobs.modified_on =  model!!.jobs!![i].modified_on
//                            model_jobs.application_count = 0//json_objectdetail.getInt("application_count")
//                            model_jobs.company_id =  model!!.jobs!![i].company_id
//                            model_jobs.employer_name = ""
//                            model_jobs.new_application_count = 0//json_objectdetail.getInt("new_application_count")
//                            model_jobs.created_on =  model!!.jobs!![i].created_on
//                            model_jobs.boosted =  model!!.jobs!![i].boosted
//                            model_jobs.employer_id = 0
//                            model_jobs.resume_required= model!!.jobs!![i].resume_required
//
////                            var job_typesArray: JSONArray = json_objectdetail.getJSONArray("job_types")
//                            val listOfJobTypes: ArrayList<String> = ArrayList()
////                            for (j in 0 until job_typesArray.length()) {
////                                    listOfJobTypes.add(job_typesArray.getString())
////                            }
//
//                            model_jobs.job_types = listOfJobTypes
//
//                            if(model_jobs.status.equals("active")) {
//                                listOfhotJobsdata.add(
//                                    JobsView(
//                                        model_jobs.id,
//                                        model_jobs.modified_on!!,
//                                        model_jobs.application_count!!,
//                                        model_jobs.company_id!!,
//                                        model_jobs.job_types!!,
//                                        model_jobs.employer_name!!,
//                                        model_jobs.new_application_count!!,
//                                        model_jobs.title!!,
//                                        model_jobs.max_year!!,
//                                        model_jobs.location_name!!,
//                                        model_jobs.company_logo!!,
//                                        model_jobs.created_on!!,
//                                        model_jobs.company_name!!,
//                                        model_jobs.boosted!!,
//                                        model_jobs.min_year!!,
//                                        model_jobs.employer_id!!,
//                                        model_jobs.status!!,
//                                        model_jobs.resume_required!!
//                                    )
//                                )
//                                if (j < 5) {
//                                    j++
//                                    listOfhotJobsdataTrimmed.add(
//                                        JobsView(
//                                            model_jobs.id,
//                                            model_jobs.modified_on!!,
//                                            model_jobs.application_count!!,
//                                            model_jobs.company_id!!,
//                                            model_jobs.job_types!!,
//                                            model_jobs.employer_name!!,
//                                            model_jobs.new_application_count!!,
//                                            model_jobs.title!!,
//                                            model_jobs.max_year!!,
//                                            model_jobs.location_name!!,
//                                            model_jobs.company_logo!!,
//                                            model_jobs.created_on!!,
//                                            model_jobs.company_name!!,
//                                            model_jobs.boosted!!,
//                                            model_jobs.min_year!!,
//                                            model_jobs.employer_id!!,
//                                            model_jobs.status!!,
//                                            model_jobs.resume_required!!
//                                        )
//                                    )
//                                }
//                            }
//                        }

                        for (i in 0 until listOfhotJobsdata.size) {
                            listOfhotJobsdata.get(i).company_logo = model.logo
                            listOfhotJobsdata.get(i).company_name = model.name

                        }
                        for (k in 0 until listOfhotJobsdataTrimmed.size) {
                            Log.d("TAGG", "LIST SIZE" + listOfhotJobsdataTrimmed.size)
                            listOfhotJobsdataTrimmed.get(k).company_logo = model.logo
                            listOfhotJobsdataTrimmed.get(k).company_name = model.name
                        }


                        if (listOfhotJobsdata.size > 5) {
                            var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                            mRecyclerViewPosts!!.layoutManager =
                                mgroupsLayoutManager as RecyclerView.LayoutManager?
                            mAdapterJobs =
                                JobsAdapter(
                                    listOfhotJobsdataTrimmed,
                                    isLoggedIn,
                                    2,
                                    0,
                                    listOfCompareJoineddata,
                                    0,
                                    "Company Details"
                                )
                            mRecyclerViewPosts!!.adapter = mAdapterJobs
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
                                    0,
                                    "Company Details"
                                )
                            mRecyclerViewPosts!!.adapter = mAdapterJobs
                        }
                        if (listOfhotJobsdata.size > 5)
                            seemore_jobs.visibility = View.VISIBLE
                        else
                            seemore_jobs.visibility = View.GONE

                        if (listOfhotJobsdata.size == 0) {
                            mRecyclerViewPosts!!.visibility = View.GONE
                            default_company_jobs.visibility = View.VISIBLE
                            seemore_jobs.visibility = View.GONE
                            if (type == 0)
                                jobs_layout.visibility = View.GONE
                            else
                                jobs_layout.visibility = View.VISIBLE
                        }
                    } else {
                        mRecyclerViewPosts!!.visibility = View.GONE
                        default_company_jobs.visibility = View.VISIBLE
                        seemore_jobs.visibility = View.GONE
                        if (type == 0)
                            jobs_layout.visibility = View.GONE
                        else
                            jobs_layout.visibility = View.VISIBLE
                    }


                    if (model!!.groups!!.size > 0) {
                        for (i in 0 until model!!.groups!!.size) {
                            val model_groups: GroupsView = GroupsView();

                            model_groups.id = model!!.groups!![i].id!!
                            model_groups.label = model!!.groups!![i].name
                            model_groups.icon_url = model!!.groups!![i].icon_url
                            model_groups.groupType = model!!.groups!![i].group_type
                            model_groups.noOfMembers = model!!.groups!![i].no_of_members.toString()
                            model_groups.description = model!!.groups!![i].description
                            model_groups.featured = model!!.groups!![i].featured
                            model_groups.status = model!!.groups!![i].status


                            // var citiesArray: JSONArray = json_objectdetail.getJSONArray("cities")
                            val listOfCity: ArrayList<Int> = ArrayList()
//                        for (j in 0 until citiesArray.length()) {
//                            var citiesObject:JSONObject=citiesArray.getJSONObject(j).
//                            listOfCity.add(citiesObject)
//                        }
                            // val categoriesArray: JSONArray = json_objectdetail.getJSONArray("categories")
                            val listOfCategories: ArrayList<Categories> = ArrayList()
//                        for (k in 0 until categoriesArray.length()) {
//                            var categoriesIdObj: JSONObject = categoriesArray.getJSONObject(k)
//                            listOfCategories.add(
//                                Categories(
//                                    categoriesIdObj.getInt("category_id"),
//                                    categoriesIdObj.getString("category")
//                                )
//                            )
//                        }

                            model_groups.categories = listOfCategories
                            model_groups.cities = listOfCity

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

                            if (i < 5) {
                                listOfMyGroupdataTrimmed.add(
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
                        if (model!!.groups!!.size > 5) {
                            var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                            mRecyclerViewGroups!!.layoutManager =
                                mgroupsLayoutManager as RecyclerView.LayoutManager?
                            mAdapterGroups =
                                MyGroupsAdapter(
                                    listOfMyGroupdataTrimmed,
                                    isLoggedIn,
                                    1,
                                    listOfCompareGroupdata,
                                    0,
                                    0,
                                    "Company Details"
                                )
                            mRecyclerViewGroups!!.adapter = mAdapterGroups
                        } else {
                            var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                            mRecyclerViewGroups!!.layoutManager =
                                mgroupsLayoutManager as RecyclerView.LayoutManager?
                            mAdapterGroups =
                                MyGroupsAdapter(
                                    listOfGroupdata,
                                    isLoggedIn,
                                    1,
                                    listOfCompareGroupdata,
                                    0,
                                    0,
                                    "Company Details"
                                )
                            mRecyclerViewGroups!!.adapter = mAdapterGroups
                        }
                        if (listOfGroupdata.size > 5)
                            seemore_groups.visibility = View.VISIBLE
                        else
                            seemore_groups.visibility = View.GONE
                    } else {
                        groups_recycler_view.visibility = View.GONE
                        default_company_groups.visibility = View.VISIBLE
                        seemore_groups.visibility = View.GONE
                        if (type == 0)
                            groups_layout.visibility = View.GONE
                        else
                            groups_layout.visibility = View.VISIBLE
                    }

                    if (model!!.events!!.size > 0) {
                        for (i in 0 until model!!.events!!.size) {
                            val model_events: EventsView = EventsView()


                            model_events.event_locations = model!!.events!![i].event_locations!!
                            model_events.event_category = model!!.events!![i].event_category!!
                            model_events.interested = model!!.events!![i].interested!!
                            model_events.registered = model!!.events!![i].registered!!
                            model_events.company_name = model!!.events!![i].city!!
                            model_events.resume_required = false
                            model_events.ticket_type = ""
                            model_events.seats = ""
                            model_events.event_type = ""
                            model_events.display_price = 0
                            model_events.agenda = ""
                            model_events.featured_end_date_time = ""
                            model_events.preference_required = false
                            model_events.terms_and_conditions = ""
                            model_events.featured_start_date_time = ""
                            model_events.featured_event = false
                            model_events.faq = ""
                            model_events.discount_start_date_time =
                                if (json_objectdetail.isNull("discount_start_date_time")) "" else json_objectdetail.getString(
                                    "discount_start_date_time"
                                )
                            model_events.discount_end_date_time =
                                if (json_objectdetail.isNull("discount_end_date_time")) "" else json_objectdetail.getString(
                                    "discount_end_date_time"
                                )
                            model_events.price_after_discount =
                                if (json_objectdetail.isNull("price_after_discount")) 0 else json_objectdetail.getInt(
                                    "price_after_discount"
                                )
                            model_events.price_before_discount =
                                if (json_objectdetail.isNull("price_before_discount")) 0 else json_objectdetail.getInt(
                                    "price_before_discount"
                                )


                            model_events.modified_by = model!!.events!![i].modified_by!!
                            model_events.interested_count = model!!.events!![i].interested_count
                            model_events.view_count = model!!.events!![i].view_count
                            model_events.share_count = model!!.events!![i].share_count
                            model_events.payment = model!!.events!![i].payment
                            model_events.status = model!!.events!![i].status

                            model_events.priority_order = model!!.events!![i].priority_order
                            model_events.excerpt = model!!.events!![i].excerpt
                            model_events.event_register_start_date_time =
                                model!!.events!![i].event_register_start_date_time!!
                            model_events.created_on = model!!.events!![i].created_on
                            model_events.author_id = model!!.events!![i].author_id
                            model_events.is_online = model!!.events!![i].is_online

                            model_events.gtm_id = model!!.events!![i].gtm_id
                            model_events.author_name = model!!.events!![i].author_name
                            model_events.is_private = model!!.events!![i].is_private
                            model_events.register_count = model!!.events!![i].register_count
                            model_events.title = model!!.events!![i].title
                            model_events.event_end_date_time =
                                model!!.events!![i].event_end_date_time!!

                            model_events.slug = model!!.events!![i].slug
                            model_events.event_register_end_date_time =
                                model!!.events!![i].event_register_end_date_time
                            model_events.event_by = model!!.events!![i].event_by
                            model_events.id = model!!.events!![i].id
                            model_events.post_for = model!!.events!![i].post_for
                            model_events.publish_date = model!!.events!![i].publish_date

                            model_events.event_start_date_time =
                                model!!.events!![i].event_start_date_time
                            model_events.created_by = model!!.events!![i].created_by
                            model_events.company_id = model!!.events!![i].company_id!!
                            model_events.address = model!!.events!![i].address
                            model_events.modified_on = model!!.events!![i].modified_on
                            model_events.payment_note = model!!.events!![i].payment_note

                            model_events.description = model!!.events!![i].description
                            model_events.city = model!!.events!![i].city
                            model_events.event_url = model!!.events!![i].event_url
                            model_events.show_on_search = model!!.events!![i].show_on_search
                            model_events.image_url = model!!.events!![i].image_url
                            model_events.thumbnail_url = model!!.events!![i].thumbnail_url
                            //    Log.d("TAGG","SIZE IS "+model!!.events!![i].id)

                            var comp: JSONArray
                            var data: ArrayList<EventCompanies> = ArrayList()
                            if (json_objectdetail.isNull("link_companies_name")) {
                            } else {
                                comp = json_objectdetail.getJSONArray("link_companies_name")

                                for (k in 0 until comp.length()) {

                                    val testimonialsModel: EventCompanies = EventCompanies()
                                    testimonialsModel.company_id =
                                        if (comp.getJSONObject(k)
                                                .isNull("company_id")
                                        ) 0 else comp.getJSONObject(
                                            k
                                        ).getInt("company_id")
                                    testimonialsModel.company_name =
                                        if (comp.getJSONObject(k)
                                                .isNull("company_name")
                                        ) "" else comp.getJSONObject(
                                            k
                                        ).getString(
                                            "company_name"
                                        )

                                    data.add(testimonialsModel)
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

                            if (i < 5) {
                                listOfEventsdataTrimmed.add(
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
                        }

                        if (model!!.events!!.size > 5) {
                            var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                            mRecyclerViewEvents!!.layoutManager =
                                mgroupsLayoutManager as RecyclerView.LayoutManager?
                            mAdapterEvents = EventsAdapter(
                                listOfEventsdataTrimmed,
                                isLoggedIn,
                                1,
                                0,
                                listOfCompareJoineddata,
                                0,
                                "Company Details"
                            )
                            mRecyclerViewEvents!!.adapter = mAdapterEvents
                        } else {
                            //  Log.d("TAGG","SIZE IS "+listOfEventsdata.size)
                            var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                            mRecyclerViewEvents!!.layoutManager =
                                mgroupsLayoutManager as RecyclerView.LayoutManager?
                            mAdapterEvents = EventsAdapter(
                                listOfEventsdata,
                                isLoggedIn,
                                1,
                                0,
                                listOfCompareJoineddata,
                                0,
                                "Company Details"
                            )
                            mRecyclerViewEvents!!.adapter = mAdapterEvents
                        }
                        if (listOfEventsdata.size > 5)
                            seemore_events.visibility = View.VISIBLE
                        else
                            seemore_events.visibility = View.GONE
                    } else {
                        default_company_events.visibility = View.VISIBLE
                        mRecyclerViewEvents!!.visibility = View.GONE
                        seemore_events.visibility = View.GONE
                        if (type == 0)
                            events_layout.visibility = View.GONE
                        else
                            events_layout.visibility = View.VISIBLE
                    }

                    if (model!!.blogs!!.size > 0) {
                        for (i in 0 until model!!.blogs!!.size) {
                            val model_blogs: BlogsView = BlogsView();

                            model_blogs.author_name = model!!.blogs!![i].author_name!!
                            model_blogs.post_for = model!!.blogs!![i].post_for
                            model_blogs.company_id = model!!.blogs!![i].company_id
                            model_blogs.slug = model!!.blogs!![i].slug
                            model_blogs.city = model!!.blogs!![i].city
                            model_blogs.title = model!!.blogs!![i].title
                            model_blogs.id = model!!.blogs!![i].id!!
                            model_blogs.blog_view = model!!.blogs!![i].blog_view
                            model_blogs.created_on = model!!.blogs!![i].created_on
                            model_blogs.created_by = model!!.blogs!![i].created_by
                            model_blogs.blog_share = model!!.blogs!![i].blog_share
                            model_blogs.publish_date = model!!.blogs!![i].publish_date
                            model_blogs.category = model!!.blogs!![i].category!!
                            model_blogs.status = model!!.blogs!![i].status
                            model_blogs.blog_link = model!!.blogs!![i].blog_link
                            model_blogs.modified_id = model!!.blogs!![i].modified_id
                            model_blogs.blog_like = model!!.blogs!![i].blog_like
                            model_blogs.excerpt = model!!.blogs!![i].excerpt
                            model_blogs.author_id = model!!.blogs!![i].author_id
                            model_blogs.admin_id = model!!.blogs!![i].admin_id
                            model_blogs.description = model!!.blogs!![i].description
                            model_blogs.modified_on = model!!.blogs!![i].modified_on
                            model_blogs.show_register_message =
                                model!!.blogs!![i].show_register_message
                            model_blogs.image_url = model!!.blogs!![i].image_url

                            listOfBlogsdata.add(
                                BlogsView(
                                    model_blogs.author_name!!,
                                    model_blogs.post_for!!,
                                    model_blogs.company_id!!,
                                    model_blogs.slug!!,
                                    model_blogs.city!!,
                                    model_blogs.title!!,
                                    model_blogs.id!!,
                                    model_blogs.blog_view!!,
                                    model_blogs.created_on!!,
                                    model_blogs.created_by!!,
                                    model_blogs.blog_share!!,
                                    model_blogs.publish_date!!,
                                    model_blogs.category!!,
                                    model_blogs.status!!,
                                    model_blogs.blog_link!!,
                                    model_blogs.modified_id!!,
                                    model_blogs.blog_like!!,
                                    model_blogs.excerpt!!,
                                    model_blogs.author_id!!,
                                    model_blogs.admin_id!!,
                                    model_blogs.description!!,
                                    model_blogs.modified_on!!,
                                    model_blogs.show_register_message!!,
                                    model_blogs.image_url!!
                                )
                            )

                            val mLayoutManager = LinearLayoutManager(
                                applicationContext, LinearLayoutManager.HORIZONTAL,
                                false
                            )
                            mRecyclerViewBlogs!!.layoutManager = mLayoutManager
                            if (listOfBlogsdata.size > 1)
                                mRecyclerViewBlogs!!.addItemDecoration(LinePagerIndicatorDecoration())
                            mAdapterBlogs = BlogsAdapter(listOfBlogsdata, isLoggedIn, 1, 0)
                            mRecyclerViewBlogs!!.adapter = mAdapterBlogs
                            var snapHelper: SnapHelper = PagerSnapHelper()
                            snapHelper.attachToRecyclerView(mRecyclerViewBlogs);
                            mRecyclerViewBlogs!!.setOnFlingListener(null);

                        }

                    } else {
                        default_company_blogs.visibility = View.VISIBLE
                        mRecyclerViewBlogs!!.visibility = View.GONE
                        if (type == 0)
                            blogs_layout.visibility = View.GONE
                        else
                            blogs_layout.visibility = View.VISIBLE
                    }

                    if (model!!.locations!!.size > 0) {
                        var listOflocations: ArrayList<Location> = ArrayList()
                        for (i in 0 until model!!.locations!!.size) {
                            val model_location: Location = Location();

                            model_location.created_on = model!!.locations!![i].created_on!!
                            model_location.hide_on_web = model!!.locations!![i].hide_on_web
                            model_location.id = model!!.locations!![i].id!!
                            model_location.state_id = model!!.locations!![i].state_id
                            model_location.state_name = model!!.locations!![i].state_name!!
                            model_location.city_id = model!!.locations!![i].city_id
                            model_location.city_name = model!!.locations!![i].city_name!!
                            model_location.country_id = model!!.locations!![i].country_id
                            model_location.pincode = model!!.locations!![i].pincode
                            model_location.country_name = model!!.locations!![i].country_name!!
                            model_location.location_type = model!!.locations!![i].location_type
                            model_location.address_2 = model!!.locations!![i].address_2!!
                            model_location.company_id = model!!.locations!![i].company_id
                            model_location.address = model!!.locations!![i].address!!
                            model_location.modified_on = model!!.locations!![i].modified_on

                            if (model_location.location_type.equals("branch")) {
                                listOflocations.add(
                                    Location(
                                        model_location.created_on!!,
                                        model_location.hide_on_web!!,
                                        model_location.id!!,
                                        model_location.state_id!!,
                                        model_location.state_name!!,
                                        model_location.city_id!!,
                                        model_location.city_name!!,
                                        model_location.country_id!!,
                                        model_location.pincode!!,
                                        model_location.country_name!!,
                                        model_location.location_type!!,
                                        model_location.address_2!!,
                                        model_location.company_id!!,
                                        model_location.address!!,
                                        model_location.modified_on!!
                                    )
                                )
                            } else {
                                var s: String = ""
                                if (model_location.address!!.length > 0)
                                    s = s + model_location.address + "\n"
                                if (model_location.address_2!!.length > 0)
                                    s = s + model_location.address_2 + "\n"
                                if (model_location.city_name!!.length > 0)
                                    s = s + model_location.city_name
                                if (model_location.pincode!! > 0)
                                    s = s + "-" + model_location.pincode
                                //head_quarter.setText((model_location.address+"\n"+model_location.address_2+"\n"+model_location.city_name+"-"+model_location.pincode))
                                head_quarter.setText(s)
                            }
                        }
                        if (listOflocations.size > 0) {
                            branches_default.visibility = View.GONE
                            val N = listOflocations.size + listOflocations.size
                            val myTextViews = arrayOfNulls<TextView>(N)
                            var l = 0
                            for (k in 0 until listOflocations.size) {

                                val rowTextView = TextView(applicationContext)
                                rowTextView.setTypeface(rowTextView.getTypeface(), Typeface.BOLD)
                                rowTextView.text = listOflocations[k].city_name + " Branch"

                                val rowTextViewAddress = TextView(applicationContext)
                                rowTextViewAddress.text =
                                    listOflocations[k].address + "\n" + listOflocations[k].address_2 + " " + listOflocations[k].city_name + "-" + listOflocations[k].pincode + "\n"

                                address_layout3.addView(rowTextView)
                                address_layout3.addView(rowTextViewAddress)

                                myTextViews[l] = rowTextView
                                myTextViews[l++] = rowTextView
                                l++
                                if (k == 0) {
                                    val rowTextView1 = TextView(applicationContext)
                                    rowTextView1.setTypeface(
                                        rowTextView1.getTypeface(),
                                        Typeface.BOLD
                                    )
                                    rowTextView1.text = listOflocations[k].city_name + " Branch"

                                    val rowTextViewAddress1 = TextView(applicationContext)
                                    rowTextViewAddress1.text =
                                        listOflocations[k].address + "\n" + listOflocations[k].address_2 + " " + listOflocations[k].city_name + "-" + listOflocations[k].pincode + "\n"

                                    address_layout4.addView(rowTextView1)
                                    address_layout4.addView(rowTextViewAddress1)
                                }
                            }
                            if (listOflocations.size > 1)
                                seemore_branches.visibility = View.VISIBLE
                            else
                                seemore_branches.visibility = View.GONE
                        } else {
                            seemore_branches.visibility = View.GONE
                            var sourceString: String =
                                "<b>" + "Branches" + "</b><br>     No branches added"
                            branches_default.setText(Html.fromHtml(sourceString))
                            branches_default.visibility = View.VISIBLE
                        }
                    }

                    if (model!!.testimonials!!.size > 0) {
                        for (i in 0 until model!!.testimonials!!.size) {
                            val model_testimonials: TestimonialsView = TestimonialsView();

                            model_testimonials.id = model!!.testimonials!![i].id!!
                            model_testimonials.testimonial = model!!.testimonials!![i].testimonial!!
                            model_testimonials.created_on = model!!.testimonials!![i].created_on
                            model_testimonials.designation = model!!.testimonials!![i].designation
                            model_testimonials.company_name = model!!.testimonials!![i].company_name
                            model_testimonials.name = model!!.testimonials!![i].name
                            model_testimonials.created_by = model!!.testimonials!![i].created_by
                            model_testimonials.modified_on = model!!.testimonials!![i].modified_on
                            model_testimonials.company_id = model!!.testimonials!![i].company_id
                            model_testimonials.image_icon = model!!.testimonials!![i].image_icon

                            listOfTestimonialsdata.add(
                                TestimonialsView(
                                    model_testimonials.id!!,
                                    model_testimonials.testimonial!!,
                                    model_testimonials.created_on!!,
                                    model_testimonials.designation!!,
                                    model_testimonials.company_name!!,
                                    model_testimonials.name!!,
                                    model_testimonials.created_by!!,
                                    model_testimonials.modified_on!!,
                                    model_testimonials.company_id!!,
                                    model_testimonials.image_icon!!
                                )
                            )

                            val mLayoutManager = LinearLayoutManager(
                                applicationContext, LinearLayoutManager.HORIZONTAL,
                                false
                            )
                            mRecyclerViewTestimonials!!.layoutManager = mLayoutManager
                            mRecyclerViewTestimonials!!.addItemDecoration(
                                LinePagerIndicatorDecoration()
                            )
                            mAdapterTestimonials = TestimonialsAdapter(
                                listOfTestimonialsdata,
                                isLoggedIn,
                                1,
                                0
                            )
                            mRecyclerViewTestimonials!!.adapter = mAdapterTestimonials
                            var snapHelper: SnapHelper = PagerSnapHelper()
                            snapHelper.attachToRecyclerView(mRecyclerViewTestimonials);
                            mRecyclerViewTestimonials!!.setOnFlingListener(null);
                        }
                    } else {
                        default_company_testimonials.visibility = View.VISIBLE
                        mRecyclerViewTestimonials!!.visibility = View.GONE
                        if (type == 0)
                            testimonials_layout.visibility = View.GONE
                        else
                            testimonials_layout.visibility = View.VISIBLE
                    }
                    //progressDoalog.dismiss();
                } else {
                    ToastHelper.makeToast(applicationContext, message)
                    finish()
                    overridePendingTransition(0, 0);
                }
                progressDoalog.dismiss();
            }

            override fun onFailure(call: Call<CompanyApiDetails>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                progressDoalog.dismiss();
            }
        })
//        listOfMyGroupdata.add(
//            GroupsView(0,"Women Freelancers","Working Women, Women Professionals","","Open Group","9652",
//                "Wish to work as per your time of convenience? " +
//                        "Come aboard this group of freelancers to stay in the know of all things that matter in your world.")
//        )
    }

    private fun setCulterDataInWebView(culture: String) {
        if (culture.isNullOrEmpty()) {
            return
        }
        company_culture.setInitialScale(1)
        company_culture.setWebChromeClient(WebChromeClient())
        company_culture.getSettings().setAllowFileAccess(true)
        company_culture.getSettings().setPluginState(WebSettings.PluginState.ON)
        company_culture.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND)
        company_culture.setWebViewClient(WebViewClient())
        company_culture.getSettings().setJavaScriptEnabled(true)
        company_culture.getSettings().setLoadWithOverviewMode(true)
        company_culture.getSettings().setUseWideViewPort(true)
        val displaymetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displaymetrics)

        company_culture.loadDataWithBaseURL("http://vimeo.com", culture, "text/html", "UTF-8", null);
    }

    class WebViewController(/*val progress: ProgressBar*/) : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            // progress.visibility = View.VISIBLE
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            //progress.visibility = View.GONE
        }
    }

    fun loadAppliedJobs() {
//
        var progressDoalog: ProgressDialog
        progressDoalog = ProgressDialog(this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("loading....");
        progressDoalog.setTitle("Company details");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.isIndeterminate = false
        // show it
        progressDoalog.show();
        val params = java.util.HashMap<String, String>()
        params["page_no"] = "1"
        listOfCompareJoineddata.clear()
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

                Logger.d("URL", "Applied" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", "Applied" + response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "Applied" + Gson().toJson(response))

                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf(
                            "{"
                        ), str_response.lastIndexOf("}") + 1
                    )
                )
                val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                val jsonarray_info: JSONArray = jsonObject1.getJSONArray("body")
                Log.d("TAGG", "Applied " + jsonarray_info.toString())

                if (response.isSuccessful) {

                    for (l in 0 until response.body()!!.body!!.size) {
                        var json_objectdetail: JSONObject = jsonarray_info.getJSONObject(l)
                        val model: AppliedJobDetailsData = AppliedJobDetailsData()

                        model.id = json_objectdetail.getInt("id")
                        model.company_id = json_objectdetail.getInt("company_id")
                        model.is_shortlisted = json_objectdetail.getBoolean("is_shortlisted")
                        model.employer_id = 0 //json_objectdetail.getInt("employer_id")
                        model.employer_note = json_objectdetail.getString("employer_note")
                        model.resume_id = 0 //json_objectdetail.getInt("resume_id")
                        model.location = json_objectdetail.getString("location")
                        model.job_status = json_objectdetail.getString("job_status")
                        model.new_applicant = json_objectdetail.getBoolean("new_applicant")
                        model.applied_status = json_objectdetail.getString("applied_status")
                        model.note = json_objectdetail.getString("note")
                        model.job_id = json_objectdetail.getInt("job_id")
                        model.user_id = json_objectdetail.getInt("user_id")
                        model.modified_on = json_objectdetail.getString("modified_on")
                        model.created_on = json_objectdetail.getString("created_on")
                        model.title = json_objectdetail.getString("title")
                        model.job_boosted = false //json_objectdetail.getBoolean("job_boosted")
                        model.min_year = 0//json_objectdetail.getInt("min_year")
                        model.max_year = 0//json_objectdetail.getInt("max_year")
                        model.job_posting_type = json_objectdetail.getString("job_posting_type")
                        model.name = json_objectdetail.getString("name")
                        model.featured = false//json_objectdetail.getBoolean("featured")
                        model.logo = json_objectdetail.getString("logo")
                        Log.d("TAGG", model.job_id!!.toString() + " JOB ID")
                        listOfCompareJoineddata.add(model.job_id!!)
                    }
                    progressDoalog.dismiss()
                    loadMyGroupData()

                } else {
                    ToastHelper.makeToast(applicationContext, message)
                }
            }

            override fun onFailure(call: Call<AppliedJobDetails>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                progressDoalog.dismiss()
                loadMyGroupData()
            }
        })
    }

    fun loadMyGroupData() {

        val params = HashMap<String, String>()

        params["page_no"] = "1"
        params["page_size"] = 30.toString()

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
                Logger.d("RESPONSE COMPANIES", "" + Gson().toJson(response))
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)

                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf(
                            "{"
                        ), str_response.lastIndexOf("}") + 1
                    )
                )
                Log.d("TAGG", "Outside body")
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {

                    Log.d("TAGG", "Inside body")
                    val jsonObject1: JSONObject = jsonObject.getJSONObject("body")

                    var jsonarray_info: JSONArray = jsonObject1.getJSONArray("body")

                    var jsonarray_pagination: JSONObject = jsonObject1.getJSONObject("pagination")

                    val has_next: String = jsonarray_pagination.getBoolean("has_next").toString()

                    val pagenoo: String = jsonarray_pagination.getString("page_no")
                    val prev_page: Int
//                    if (jsonarray_pagination.has("prev_page") && !jsonarray_pagination.getString("prev_page").equals(""))
//                        prev_page = jsonarray_pagination.getInt("prev_page")
//                    else
//                        prev_page = 0
                    if (Integer.parseInt(pagenoo) > 1)
                        prev_page = Integer.parseInt(pagenoo) - 1
                    else
                        prev_page = 0

                    if (response.isSuccessful) {

                        //  ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                        Log.d("TAGG", "Inside response success")
                        for (i in 0 until response.body()!!.body!!.size) {
                            Log.d("TAGG", "Inside response body")
                            var json_objectdetail: JSONObject = jsonarray_info.getJSONObject(i)

                            val model: GroupsView = GroupsView();

                            model.id = json_objectdetail.getInt("id")
                            model.label = json_objectdetail.getString("name")
                            model.icon_url = json_objectdetail.getString("icon_url")
                            model.groupType = json_objectdetail.getString("visiblity_type")
                            model.noOfMembers = json_objectdetail.getString("no_of_members")
                            model.description = json_objectdetail.getString("excerpt")
                            model.featured = json_objectdetail.getBoolean("featured")
                            model.status = json_objectdetail.getString("status")
                            model.is_member = json_objectdetail.getBoolean("is_member")

                            var citiesArray: JSONArray = json_objectdetail.getJSONArray("cities")
                            val listOfCity: ArrayList<Int> = ArrayList()
//                        for (j in 0 until citiesArray.length()) {
//                            var citiesObject:JSONObject=citiesArray.getJSONObject(j).
//                            listOfCity.add(citiesObject)
//                        }
                            val categoriesArray: JSONArray =
                                json_objectdetail.getJSONArray("categories")
                            val listOfCategories: ArrayList<Categories> = ArrayList()
                            for (k in 0 until categoriesArray.length()) {
                                var categoriesIdObj: JSONObject = categoriesArray.getJSONObject(k)
                                listOfCategories.add(
                                    Categories(
                                        categoriesIdObj.getInt("category_id"),
                                        categoriesIdObj.getString("category")
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

                            listOfCompareGroupdata.add(
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

                        loadJobsData()
                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid Request")

                    }
                    Log.d("TAGG", "Beforeb has next")
                } else {
                    Toast.makeText(applicationContext, "No data exists", Toast.LENGTH_LONG).show()
                    loadJobsData()
                }
            }

            override fun onFailure(call: Call<Featured_Group>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                // Toast.makeText(applicationContext, "No Groups Exixts, Join a group today!", Toast.LENGTH_LONG).show()
                loadJobsData()
            }
        })
    }

    fun loadJobsData() {

        val params = HashMap<String, String>()

        params["company_id"] = groupId.toString()
        params["page_no"] = "1"
        params["page_size"] = 30.toString()

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface!!.getcompJobsData(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<HotJobs> {
            override fun onResponse(call: Call<HotJobs>, response: Response<HotJobs>) {

                Logger.d("URL", "" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE COMPANIES", "" + Gson().toJson(response))
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)

                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf(
                            "{"
                        ), str_response.lastIndexOf("}") + 1
                    )
                )
                Log.d("TAGG", "Outside body")
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {

                    Log.d("TAGG", "Inside body")
                    val jsonObject1: JSONObject = jsonObject.getJSONObject("body")

                    var jsonarray_info: JSONArray = jsonObject1.getJSONArray("body")

                    var jsonarray_pagination: JSONObject = jsonObject1.getJSONObject("pagination")

                    val has_next: String = jsonarray_pagination.getBoolean("has_next").toString()

                    val pagenoo: String = jsonarray_pagination.getString("page_no")
                    val prev_page: Int
//                    if (jsonarray_pagination.has("prev_page") && !jsonarray_pagination.getString("prev_page").equals(""))
//                        prev_page = jsonarray_pagination.getInt("prev_page")
//                    else
//                        prev_page = 0
                    if (Integer.parseInt(pagenoo) > 1)
                        prev_page = Integer.parseInt(pagenoo) - 1
                    else
                        prev_page = 0

                    if (response.isSuccessful) {

                        //  ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                        Log.d("TAGG", "Inside response success")
                        var j = 0
                        for (i in 0 until response.body()!!.body!!.size) {
                            Log.d("TAGG", "Inside response body")
                            var json_objectdetail: JSONObject = jsonarray_info.getJSONObject(i)

                            val jobsModel: Jobs = Jobs()
                            jobsModel.view_count =
                                if (json_objectdetail.isNull("view_count")) 0 else json_objectdetail.getInt(
                                    "view_count"
                                )
                            jobsModel.status =
                                if (json_objectdetail.isNull("status")) "" else json_objectdetail.getString(
                                    "status"
                                )
                            jobsModel.location_id =
                                if (json_objectdetail.isNull("location_id")) 0 else json_objectdetail.getInt(
                                    "location_id"
                                )
                            jobsModel.boosted =
                                if (json_objectdetail.isNull("boosted")) false else json_objectdetail.getBoolean(
                                    "boosted"
                                )
                            jobsModel.min_year =
                                if (json_objectdetail.isNull("min_year")) 0 else json_objectdetail.getInt(
                                    "min_year"
                                )
                            jobsModel.created_on =
                                if (json_objectdetail.isNull("created_on")) "" else json_objectdetail.getString(
                                    "created_on"
                                )
                            jobsModel.location_name =
                                if (json_objectdetail.isNull("location_name")) "" else json_objectdetail.getString(
                                    "location_name"
                                )
                            jobsModel.resume_required =
                                if (json_objectdetail.isNull("resume_required")) true else json_objectdetail.getBoolean(
                                    "resume_required"
                                )
                            jobsModel.title =
                                if (json_objectdetail.isNull("title")) "" else json_objectdetail.getString(
                                    "title"
                                )
                            jobsModel.id =
                                if (json_objectdetail.isNull("id")) 0 else json_objectdetail.getInt(
                                    "id"
                                )
                            jobsModel.specialization_id =
                                if (json_objectdetail.isNull("specialization_id")) 0 else json_objectdetail.getInt(
                                    "specialization_id"
                                )
                            jobsModel.user_id =
                                if (json_objectdetail.isNull("user_id")) 0 else json_objectdetail.getInt(
                                    "user_id"
                                )
                            jobsModel.max_year =
                                if (json_objectdetail.isNull("max_year")) 0 else json_objectdetail.getInt(
                                    "max_year"
                                )
                            jobsModel.company_id =
                                if (json_objectdetail.isNull("company_id")) 0 else json_objectdetail.getInt(
                                    "company_id"
                                )
                            jobsModel.modified_on =
                                if (json_objectdetail.isNull("modified_on")) "" else json_objectdetail.getString(
                                    "modified_on"
                                )
                            jobsModel.redirect_url =
                                if (json_objectdetail.isNull("redirect_url")) "" else json_objectdetail.getString(
                                    "redirect_url"
                                )
                            jobsModel.co_owners =
                                if (json_objectdetail.isNull("co_owners")) "" else json_objectdetail.getString(
                                    "co_owners"
                                )
                            jobsModel.description =
                                if (json_objectdetail.isNull("description")) "" else json_objectdetail.getString(
                                    "description"
                                )
                            jobsModel.published_on =
                                if (json_objectdetail.isNull("published_on")) "" else json_objectdetail.getString(
                                    "published_on"
                                )
                            jobsModel.job_posting_type =
                                if (json_objectdetail.isNull("job_posting_type")) "" else json_objectdetail.getString(
                                    "job_posting_type"
                                )
                            jobsModel.vacancy =
                                if (json_objectdetail.isNull("vacancy")) "" else json_objectdetail.getString(
                                    "vacancy"
                                )
                            jobsModel.application_notification_type =
                                if (json_objectdetail.isNull("application_notification_type")) "" else json_objectdetail.getString(
                                    "application_notification_type"
                                )
                            jobsModel.salary =
                                if (json_objectdetail.isNull("salary")) "" else json_objectdetail.getString(
                                    "salary"
                                )
                            jobsModel.min_qualification =
                                if (json_objectdetail.isNull("min_qualification")) "" else json_objectdetail.getString(
                                    "min_qualification"
                                )
                           // var job_typesArray: JSONArray = json_objectdetail.getJSONArray("job_types")
                            val listjson: JSONArray = JSONArray()
                            var job_typesArray: JSONArray = if(json_objectdetail.isNull("job_types"))listjson else json_objectdetail.getJSONArray("job_types")

                            val listOfJobTypes: ArrayList<String> = ArrayList()
                            for (j in 0 until job_typesArray.length()) {
                                listOfJobTypes.add(job_typesArray.get(j).toString())
                            }


                            if (jobsModel.status.equals("active")) {
                                listOfhotJobsdata.add(
                                    JobsView(
                                        jobsModel.id,
                                        jobsModel.modified_on!!,
                                        0,
                                        jobsModel.company_id!!,
                                        listOfJobTypes!!,
                                        "",
                                        0,
                                        jobsModel.title!!,
                                        jobsModel.max_year!!,
                                        jobsModel.location_name!!,
                                        "",
                                        jobsModel.created_on!!,
                                        "",
                                        jobsModel.boosted!!,
                                        jobsModel.min_year!!,
                                        0,
                                        jobsModel.status!!,
                                        jobsModel.resume_required!!
                                    )
                                )
                                if (j < 5) {
                                    j++
                                    listOfhotJobsdataTrimmed.add(
                                        JobsView(
                                            jobsModel.id,
                                            jobsModel.modified_on!!,
                                            0,
                                            jobsModel.company_id!!,
                                            listOfJobTypes,
                                            "",
                                            0,
                                            jobsModel.title!!,
                                            jobsModel.max_year!!,
                                            jobsModel.location_name!!,
                                            "",
                                            jobsModel.created_on!!,
                                            "",
                                            jobsModel.boosted!!,
                                            jobsModel.min_year!!,
                                            0,
                                            jobsModel.status!!,
                                            jobsModel.resume_required!!
                                        )
                                    )
                                }
                            }
                        }

                        loadCompanyDetailsData()
                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid Request")

                    }
                    Log.d("TAGG", "Beforeb has next")
                } else {
                    Toast.makeText(applicationContext, "No data exists", Toast.LENGTH_LONG).show()
                    loadCompanyDetailsData()
                }
            }

            override fun onFailure(call: Call<HotJobs>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                //  Toast.makeText(applicationContext, "No Jobs Exixts, Join a group today!", Toast.LENGTH_LONG).show()
                loadCompanyDetailsData()
            }
        })
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
        if(page.equals("Dashboard")){
            val intent = Intent(applicationContext, ZActivityDashboard::class.java)
            intent.flags = FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("isLoggedIn", true)
            startActivity(intent)
            finish()
        }
        if(page.equals("Jobs")){
            val intent = Intent(applicationContext, ZActivityJobs::class.java)
            intent.flags = FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("isLoggedIn", true)
            startActivity(intent)
            finish()
        }
        if(page.equals("NewsFeed")){
            val intent = Intent(applicationContext, NewsFeed::class.java)
            intent.flags = FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("isLoggedIn", true)
            startActivity(intent)
            finish()
        }
        else if(page.equals("Companies")) {
            val intent = Intent(applicationContext, ZActivityCompanies::class.java)
            intent.flags = FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("isLoggedIn", true)
            startActivity(intent)
            finish()
        }
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
//                ToastHelper.makeToast(applicationContext, "Groups Under Maintanence")
            }
            R.id.action_groups -> {
                val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
                drawerLayout.closeDrawer(GravityCompat.START)
                intent = Intent(applicationContext, ZActivityGroups::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("isLoggedIn", true)
                startActivity(intent)

//                ToastHelper.makeToast(applicationContext, "Groups Under Maintanence")

            }
            R.id.action_jobs -> {
                val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
                drawerLayout.closeDrawer(GravityCompat.START)
                intent = Intent(applicationContext, ZActivityJobs::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("isLoggedIn", true)
//                intent = Intent(applicationContext, WebActivity::class.java)
//                intent.putExtra("value","https://www.jobsforher.com/jobs/jobs_search")
                startActivity(intent)
//                intent = Intent(applicationContext, ProfileView::class.java)
//                startActivity(intent)
            }
            R.id.action_companies -> {
                val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
                drawerLayout.closeDrawer(GravityCompat.START)
                intent = Intent(applicationContext, ZActivityCompanies::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("isLoggedIn", true)
//                intent = Intent(applicationContext, WebActivity::class.java)
//                intent.putExtra("value","https://www.jobsforher.com/company")
                startActivity(intent)
            }
            R.id.action_reskilling -> {
                intent = Intent(applicationContext, ZActivityDashboard::class.java)
                intent.putExtra("isLoggedIn", true)
                //intent = Intent(applicationContext, WebActivity::class.java)
                //intent.putExtra("value","https://www.jobsforher.com/reskilling")
                startActivity(intent)
            }
            R.id.action_mentors -> {
                intent = Intent(applicationContext, WebActivity::class.java)
                intent.putExtra("value", "https://www.jobsforher.com/mentors")
                startActivity(intent)
            }
            R.id.action_events -> {
//                intent = Intent(applicationContext, WebActivity::class.java)
//                intent.putExtra("value","https://www.jobsforher.com/events")
//                startActivity(intent)
                val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
                drawerLayout.closeDrawer(GravityCompat.START)
                intent = Intent(applicationContext, ZActivityEvents::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("isLoggedIn", true)
                startActivity(intent)

            }
            R.id.action_blogs -> {
                intent = Intent(applicationContext, WebActivity::class.java)
                intent.putExtra("value", "https://www.jobsforher.com/blogs")
                startActivity(intent)
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
//                // Set the alert dialog title
//                builder.setTitle("Do you really want to logout of the app ?")
                // Display a message on alert dialog
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
        }

        return true
    }

    public fun openBottomSheetPreferences() {

        val dialog = Dialog(this)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog .setCancelable(true)
        dialog .setContentView(R.layout.bottom_sheet_preferences)
        val cancel_pref = dialog.findViewById(R.id.cancel_pref) as Button
        cancel_pref.setOnClickListener {
            dialog.cancel()
        }
        val pref_location_multiAutoCompleteTextView = dialog.findViewById(R.id.pref_location_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val locationList: java.util.ArrayList<String> = java.util.ArrayList()
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

        val pref_keyword = dialog.findViewById(R.id.pref_keyword) as EditText

        val experience_multiAutoCompleteTextView =
            dialog.findViewById(R.id.experience_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val experienceList: java.util.ArrayList<String> = java.util.ArrayList()
        for (i in 0..50)
            experienceList.add(i.toString())
        val adapter2 = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            experienceList
        )
        experience_multiAutoCompleteTextView.setAdapter(adapter2)
        experience_multiAutoCompleteTextView.setThreshold(1)
        experience_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        val jobtype_multiAutoCompleteTextView =
            dialog.findViewById(R.id.jobtype_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val randomArray = ArrayAdapter(this, android.R.layout.simple_list_item_1, listOfJobType)
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
        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            listOfJobFArea
        )
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
        val adapter1 = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            listOfJobIndustry
        )
        industry_multiAutoCompleteTextView.setAdapter(adapter1)
        industry_multiAutoCompleteTextView.setThreshold(1)
        industry_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        industry_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                industry_multiAutoCompleteTextView.showDropDown()
                return false
            }
        })
        val save_pref = dialog.findViewById(R.id.save_pref) as Button
        save_pref.setOnClickListener {
            savePreferences(
                pref_location_multiAutoCompleteTextView.text.trim().toString(),
                jobtype_multiAutoCompleteTextView.text.trim().toString(),
                farea_multiAutoCompleteTextView.text.trim().toString(),
                industry_multiAutoCompleteTextView.text.trim().toString(),
                pref_keyword.text.trim().toString(),
                experience_multiAutoCompleteTextView.text.trim().toString()
            )
            dialog.cancel()
        }

        val crossbutton_pref = dialog.findViewById(R.id.closetext) as TextView
        crossbutton_pref.setOnClickListener {
            dialog.cancel()
        }

        var window :Window = dialog.getWindow()!!;
        window.setBackgroundDrawable(null);
        var wlp: WindowManager.LayoutParams = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog .show()
    }

    fun loadCityData() {

        listOfCities.clear()
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getCities(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN
        )
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
                        str_response.indexOf(
                            "{"
                        ), str_response.lastIndexOf("}") + 1
                    )
                )
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                    val responseCode: Int = jsonObject1.getInt("response_code")
                    val message: String = jsonObject1.getString("message")
                    var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                    if (response.isSuccessful) {
                        for (i in 0 until response.body()!!.body!!.size) {
                            var json_objectdetail: JSONObject = jsonarray.getJSONObject(i)
                            val model: CityView = CityView();
                            model.id = json_objectdetail.getInt("id")
                            model.name = json_objectdetail.getString("label")
                            listOfCities.add(model)
                        }
                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid")
                    }
                } else
                    Toast.makeText(applicationContext, "No data exists", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<GetCityResponse>, t: Throwable) {
                Logger.d("TAG", "FAILED : $t")
            }
        })
    }

    public fun openBottomSheet(
        id: Int,
        data: String?,
        isOwner: String?,
        icon: String,
        url: String,
        posttype: String,
        yesno: String
    ) {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.bottom_sheet_posts)
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

        val editpost = dialog .findViewById(R.id.edit_post) as LinearLayout

        editpost.setOnClickListener {
            if(isOwner.equals("true")) {
                dialog.cancel()
                intent = Intent(applicationContext, ZEditPostActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra("data", data)
                intent.putExtra("post_type", posttype)
                intent.putExtra("url", url)
                intent.putExtra("icon", icon)
                intent.putExtra("grpName", groupname.text.toString())
                intent.putExtra("owner", isOwner)
                startActivityForResult(intent, 1);
            }
            else {
                Toast.makeText(applicationContext, "You cannot edit the post", Toast.LENGTH_LONG).show()
            }
        }

        val edit_post_history = dialog .findViewById(R.id.edit_post_history) as LinearLayout

        edit_post_history.setOnClickListener {

            if (isOwner.equals("true")) {
                dialog.cancel()
                intent = Intent(applicationContext, ZEditPostHistoryActivity::class.java)
                intent.putExtra("id", id)
                startActivity(intent)
            } else
                Toast.makeText(
                    applicationContext,
                    "You cannot view edit history",
                    Toast.LENGTH_LONG
                ).show()
        }

        val reportpost = dialog .findViewById(R.id.edit_report) as LinearLayout
        val reporttext = dialog.findViewById(R.id.reportedtext) as TextView
        Log.d("REPORT", yesno)
        if (yesno.compareTo("yes")==0){
            reporttext.setText("Reported")
            reporttext.setTextColor(Color.RED)
            reportpost.isEnabled = false
        }
        else{
//            reporttext.setText("Report this post")
//            reporttext.setTextColor(Color.BLACK)
            reportpost.isEnabled = true
        }
        reportpost.setOnClickListener {
            openBottomSheetReports(id, "post")
        }

        if(isOwner.equals("true")){
            editpost.visibility= View.VISIBLE
            edit_post_history.visibility = View.VISIBLE
        }
        else{
            editpost.visibility= View.GONE
            edit_post_history.visibility = View.GONE
        }


        var window: Window = dialog.getWindow()!!;
        var wlp: WindowManager.LayoutParams = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog.show()
    }

    public fun openBottomSheetComments(
        id: Int,
        data: String?,
        isOwner: String?,
        icon: String?,
        yesno: String
    ) {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.bottom_sheet_comments)
        val editcomment = dialog.findViewById(R.id.edit_comment) as LinearLayout

        editcomment.setOnClickListener {
            if (isOwner.equals("true")) {
                dialog.cancel()
                intent = Intent(applicationContext, ZEditCommentActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra("data", data)
                intent.putExtra("icon", icon)
                startActivityForResult(intent, 1)
                dialog.cancel()
            }
            else
                Toast.makeText(applicationContext, "You cannot edit the comment", Toast.LENGTH_LONG).show()
        }

        val edit_commenthistory = dialog .findViewById(R.id.edit_comment_history) as LinearLayout
        edit_commenthistory.setOnClickListener {
            if (isOwner.equals("true")) {
                dialog.cancel()
                intent = Intent(applicationContext, ZEditComentHistoryActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra("icon", icon)
                startActivity(intent)
                dialog.cancel()
            } else
                Toast.makeText(
                    applicationContext,
                    "You cannot view the edit history",
                    Toast.LENGTH_LONG
                ).show()
        }

        val cancel = dialog .findViewById(R.id.cancel) as LinearLayout
        cancel.setOnClickListener {
            dialog.cancel()
        }

        val reportcomment = dialog .findViewById(R.id.report_comment) as LinearLayout
        val reporttext = dialog.findViewById(R.id.reporttextcomment) as TextView
        Log.d("REPORT", yesno)
        if (yesno.compareTo("yes")==0){
            reporttext.setText("Reported")
            reporttext.setTextColor(Color.RED)
            reportcomment.isEnabled = false
        }
        else{
//            reporttext.setText("Report this comment")
//            reporttext.setTextColor(Color.BLACK)
            reportcomment.isEnabled = true
        }
        reportcomment.setOnClickListener {
            dialog.cancel()
            openBottomSheetReports(id, "comment")
        }

        if(isOwner.equals("true")){
            editcomment.visibility= View.VISIBLE
            edit_commenthistory.visibility = View.VISIBLE
        }
        else{
            editcomment.visibility= View.GONE
            edit_commenthistory.visibility = View.GONE
        }

        var window : Window = dialog.getWindow()!!;
        var wlp: WindowManager.LayoutParams  = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog .show()
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

        val cancel = dialog .findViewById(R.id.cancelblock) as LinearLayout
        cancel.setOnClickListener {
            dialog.cancel()
        }


        var window : Window = dialog.getWindow()!!;
        var wlp: WindowManager.LayoutParams  = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog .show()
    }



//    public fun openBottomSheetComments(id: Int, data: String?, isOwner: String?) {
//
//        val dialog = Dialog(this)
//        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog .setCancelable(true)
//        dialog .setContentView(R.layout.bottom_sheet_comments)
//        val editpost = dialog .findViewById(R.id.edit_post) as LinearLayout
//
//        editpost.setOnClickListener {
//            if(isOwner.equals("true")) {
//                intent = Intent(applicationContext, ZEditCommentActivity::class.java)
//                intent.putExtra("id", id)
//                intent.putExtra("data", data)
//                startActivity(intent)
//            }
//            else
//                Toast.makeText(applicationContext, "You cannot edit the comment", Toast.LENGTH_LONG).show()
//        }
//
//        val edit_post_history = dialog .findViewById(R.id.edit_post_history) as LinearLayout
//        edit_post_history.setOnClickListener {
//            if(isOwner.equals("true")) {
//                intent = Intent(applicationContext, ZEditComentHistoryActivity::class.java)
//                intent.putExtra("id", id)
//                startActivity(intent)
//            }
//            else
//                Toast.makeText(applicationContext, "You cannot view the edit history", Toast.LENGTH_LONG).show()
//        }
//
//        val cancel = dialog .findViewById(R.id.cancel) as LinearLayout
//        cancel.setOnClickListener {
//            dialog.cancel()
//        }
//
//        var window : Window = dialog.getWindow();
//        var wlp: WindowManager.LayoutParams = window.getAttributes();
//
//        wlp.gravity = Gravity.BOTTOM;
//        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        window.setAttributes(wlp);
//        dialog .show()
//
//
//    }


    fun leaveGroup(id: Int, btnJoinGroup: Button) {

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

                    val intent = Intent(applicationContext, ZActivityGroups::class.java)
                    intent.flags = FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.putExtra("isLoggedIn", true)
                    startActivity(intent)
                    finish()

                    Logger.e("TAGG", "" + response.body()!!.message.toString())

                } else {

                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<JoinGroupResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
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

//        ToastHelper.makeToast(applicationContext, "Report Group")
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
                        //                        Toast.makeText(applicationContext,"clicked yes",Toast.LENGTH_LONG).show()
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

    fun savePreferences(
        pref_location: String,
        jobtype: String,
        farea: String,
        industry: String,
        pref_keyword: String,
        experience: String
    ) {

        val params = HashMap<String, String>()

        if (pref_location.equals("")) {
        } else {
            params["preferred_city"] = pref_location.toString()
        }
        if (jobtype.equals("")) {
        } else {
            params["preferred_job_type"] = jobtype.substring(0, jobtype.length - 1)
        }
        if (farea.equals("")){}
        else{
            params["preferred_functional_area"] = farea.substring(0, farea.length - 1)
        }
        if (industry.equals("")){}
        else{
            params["preferred_industry"] = industry.substring(0, industry.length - 1)
        }
        if (pref_keyword.equals("")){}
        else{
            params["skills"] = pref_keyword.toString()
        }
        if (experience.equals("")){}
        else{
            if(experience.contains(",")) {
                params["exp_from_year"] = experience.substring(0, experience.length - 1)
                params["exp_to_year"] = experience.substring(0, experience.length - 1)
            } else {
                params["exp_from_year"] = experience.toString()
                params["exp_to_year"] = experience.toString()
            }
        }

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




}

