package com.jobsforher.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Layout.JUSTIFICATION_MODE_INTER_WORD
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.util.Log
import android.view.*
import android.view.animation.OvershootInterpolator
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.jobsforher.activities.ZJobTypeAdapter
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jobsforher.R
import com.jobsforher.adapters.*
import com.jobsforher.helpers.*
import com.jobsforher.models.*
import com.jobsforher.network.responsemodels.*
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import com.plumillonforge.android.chipview.Chip
import com.plumillonforge.android.chipview.OnChipClickListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_jfh_banner_jobs.*
import kotlinx.android.synthetic.main.jobs_app_bar_main.*
import kotlinx.android.synthetic.main.toolbar_groups.*
import kotlinx.android.synthetic.main.zjobs_content_main.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class ZActivityJobs() : Footer(), NavigationView.OnNavigationItemSelectedListener  {

    private lateinit var mFirebaseAnalytics: FirebaseAnalytics
    var mRecyclerView: RecyclerView? = null
    var mAdapter: RecyclerView.Adapter<*>? = null
    var listOfhotJobsdata: ArrayList<JobsView> = ArrayList()
    var listOfFilterMygroups: ArrayList<GroupsView> = ArrayList()
    var listOfFeaturedMygroups: ArrayList<GroupsView> = ArrayList()
    var listOfCompareGroupdata: ArrayList<GroupsView> = ArrayList()
    var listOfCompareJoineddata: ArrayList<Int> = ArrayList()
    var listOfCompareRecdata: ArrayList<Int> = ArrayList()
    var message_filter:String=""
    var scroll:Int = 0
    var jobname:String = ""
    var keyword_data:String = ""
    private val GALLERY_PDF = 3


    private val PREF_PHONE = ""
    var mRecyclerViewAds: RecyclerView? = null
    var mAdapterAds: RecyclerView.Adapter<*>? = null
    var listOfAdsdata: ArrayList<String> = ArrayList()

    var mgroupsRecyclerView: RecyclerView? = null
    var hotJobsAdapter: RecyclerView.Adapter<*>? = null
    var isLoggedIn=true
    private var retrofitInterface: RetrofitInterface? = null
    private var retrofitInterface1: RetrofitInterface? = null
    var next_page_no_featured: String="1"
    var prev_page_no_featured: String="1"
    var type_group: Int=0
    var isfilter:Int = 0
    private var doubleBackToExitPressedOnce = false

    var listOfCategories: ArrayList<CategoryView> = ArrayList()
    var listOfCities: ArrayList<CityView> = ArrayList()
    var listOfJobType: ArrayList<JobTypeView> = ArrayList()
    var listOfJobFArea: ArrayList<FunctionalAreaView> = ArrayList()
    var listOfJobFAreaSorted: ArrayList<FunctionalAreaView> = ArrayList()
    var listOfJobIndustry: ArrayList<FunctionalAreaView> = ArrayList()

    var category: String=""
    var location_name: String=""
    var location_nameArray: ArrayList<String> = ArrayList()
    var job_types: String=""
    var job_typesArray: ArrayList<String> = ArrayList()
    var min_year: String="0"
    var max_year: String="0"
    var functional_area:String=""
    var functional_areaArray:ArrayList<String> = ArrayList()
    var industries:String=""
    var industriesArray:ArrayList<String> = ArrayList()
    var company_id:String=""
    var type: String=""



    var listOfMainCategories: ArrayList<CategoriesMainView> = ArrayList()
    var listOfResumes: ArrayList<ResumeView>  = ArrayList()

    var mAdapterCategories: RecyclerView.Adapter<*>? = null
    var mAdapterCities: RecyclerView.Adapter<*>? = null
    var mAdapterJobtype: RecyclerView.Adapter<*>? = null
    var mAdapterFArea: RecyclerView.Adapter<*>? = null
    var mAdapterIndustry: RecyclerView.Adapter<*>? = null

    @SuppressLint("WrongConstant", "MissingPermission")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zactivity_jobs)
        isLoggedIn=intent.getBooleanExtra("isLoggedIn",false)
        Log.d("TAGG", isLoggedIn.toString())
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        mappingWidgets()
        handleBackgrounds(btnJobs)

        loadData()
        loadFunctionalData()
        loadIndustryData()
        loadNotificationbubble()

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
//
//        var bundle :Bundle = Bundle()
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "100");
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Test-sneha");
//        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
//
//        var bundle1 :Bundle = Bundle()
//        bundle1.putString(FirebaseAnalytics.Param.ITEM_NAME, "Test-sneha_123");
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN,bundle1);
//
        mFirebaseAnalytics.setCurrentScreen(this, "JobsPage", "Jobs Listing Page" /* class override */)
//
//        mFirebaseAnalytics.setUserProperty("myname", "sneha")



        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        val opacity:Int = 130; // from 0 to 255
        overlay.setBackgroundColor(opacity * 0x1000000); // black with a variable alpha
        val params: FrameLayout.LayoutParams =  FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT);
        params.gravity = Gravity.BOTTOM;
        overlay.setLayoutParams(params);
        overlay.invalidate(); // update the view



        jobstext.setAnimationDuration(750L);
        jobstext.setInterpolator(OvershootInterpolator());
        jobstext.setExpandInterpolator( OvershootInterpolator());
        jobstext.setCollapseInterpolator( OvershootInterpolator());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            jobstext.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }
        seemore_text.setOnClickListener{

            jobstext.toggle()
            if (seemore_text.text.equals("See More"))
                seemore_text.setText("See Less")
            else
                seemore_text.setText("See More")
        }

        //tag_group.setChipBackgroundRes(R.drawable.chip)
        tag_group.chipSidePadding =70
        tag_group.chipLayoutRes = R.layout.chip_close

        tag_group.setOnChipClickListener(OnChipClickListener{

            if(location_name.contains(Regex(it.text.toString()))){

                var output:ArrayList<String> = ArrayList(location_name.substring(0,location_name.length-1) . split (","))
                var k:Int=0
                for(i in 0 until output.size){
                    if(output[i].equals(it.text.toString()))
                        k = i
                }
                output.removeAt(k)
                location_name = output.toString()
                location_name = location_name.substring(1,location_name.length-1)
                Log.d("TAGG","PARAMS"+location_name.toString())
                //loadFilteredData("1",a.toString().trim(),"", "","","","","","","")
            }
            else if(job_types.contains(Regex(it.text.toString()))){

                var output:ArrayList<String> = ArrayList(job_types.substring(0,job_types.length-1) . split (","))
                var k:Int=0
                for(i in 0 until output.size){
                    if(output[i].equals(it.text.toString()))
                        k = i
                }
                output.removeAt(k)
                job_types = output.toString()
                job_types = job_types.substring(1,job_types.length-1)
                Log.d("TAGG","PARAMS"+job_types.toString())
                //loadFilteredData("1",a.toString().trim(),"", "","","","","","","")
            }
            else if(functional_area.contains(Regex(it.text.toString()))){

                var output:ArrayList<String> = ArrayList(functional_area.substring(0,functional_area.length-1) . split (","))
                var k:Int=0
                for(i in 0 until output.size){
                    if(output[i].equals(it.text.toString()))
                        k = i
                }
                output.removeAt(k)
                functional_area = output.toString()
                functional_area = functional_area.substring(1,functional_area.length-1)
                Log.d("TAGG","PARAMS"+functional_area.toString())
                //loadFilteredData("1",a.toString().trim(),"", "","","","","","","")
            }
            else if(industries.contains(Regex(it.text.toString()))){

                var output:ArrayList<String> = ArrayList(industries.substring(0,industries.length-1) . split (","))
                var k:Int=0
                for(i in 0 until output.size){
                    if(output[i].equals(it.text.toString()))
                        k = i
                }
                output.removeAt(k)
                industries = output.toString()
                industries = industries.substring(1,industries.length-1)
                Log.d("TAGG","PARAMS"+industries.toString())
                //loadFilteredData("1",a.toString().trim(),"", "","","","","","","")
            }

            scroll = 0
            Log.d("TAGG","CLICJED  hkjkkk  " +it.text.toString())
            tag_group.remove(it)

            if(tag_group.count()==0){
                joblisting_tag.visibility = View.GONE
                layoutdefaultjob.visibility = View.GONE
                find_morejobs.callOnClick()
            }
            else if (type_group==1)
                loadFilteredData("1",keyword_data,location_name, job_types,min_year,
                    max_year,functional_area,industries,company_id,"boosted")
            else
                loadFilteredData("1",keyword_data,location_name, job_types,min_year,
                    max_year,functional_area,industries,company_id,"basic")
        })

//        city.setOnClickListener {
////            city.maxLines = 5
////            city.showDropDown()
////        }

//        loadAppliedJobs()

        var s = Html.fromHtml("<font color='white'><b>JOBS</b></font> for you to <br>" +
                "<font color='white'><b>START, RESTART</b></font> and <font color='white'><b>RISE</b></font>")
        banner_text.setText(s, TextView.BufferType.SPANNABLE)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close
        )
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        search_toggle.setOnClickListener {
            if (search_toggle.text.equals("Advanced Search")){
                search_toggle.setText("Basic Search")
                keyword.visibility = View.VISIBLE
                city.visibility = View.VISIBLE
                functionalarea.visibility = View.VISIBLE
                industry.visibility = View.VISIBLE
                salary.visibility = View.VISIBLE
                experience.visibility = View.VISIBLE
            }
            else{
                search_toggle.setText("Advanced Search")
                keyword.visibility = View.VISIBLE
                city.visibility = View.VISIBLE
                functionalarea.visibility = View.GONE
                industry.visibility = View.GONE
                salary.visibility = View.GONE
                experience.visibility = View.GONE

            }
        }

        view_all_categories.setOnClickListener{
            // Toast.makeText(applicationContext, "View Categories", Toast.LENGTH_LONG).show()
//            intent = Intent(applicationContext, GroupsCategoryPage::class.java)
//            startActivityForResult(intent, 20);
        }

        val mLayoutManager = LinearLayoutManager(
            applicationContext, LinearLayoutManager.HORIZONTAL,
            false
        )

        mRecyclerViewAds = findViewById(R.id.jobsAds_recycler_view)
        listOfAdsdata.add("Over 6000 companies looking for female talent")
        listOfAdsdata.add("Over 6000 companies looking for female talent")
        listOfAdsdata.add("Over 6000 companies looking for female talent")
        mRecyclerViewAds!!.layoutManager = mLayoutManager
        mRecyclerViewAds!!.addItemDecoration( LinePagerIndicatorDecoration())
        mAdapterAds = JobsAdsAdapter(listOfAdsdata, isLoggedIn, 1, 0)
        mRecyclerViewAds!!.adapter = mAdapterAds

        fabFilter.setOnClickListener {

            filter.callOnClick()

        }

        mgroupsRecyclerView = findViewById(R.id.recycler_view_groups)
        if(isLoggedIn){

            if (navView != null) {
                val menu = navView.menu
                menu.findItem(R.id.action_employerzone).setVisible(false)
                menu.findItem(R.id.action_mentorzone).setVisible(false)
                menu.findItem(R.id.action_partnerzone).setVisible(false)
                menu.findItem(R.id.action_logout).setVisible(false)
                menu.findItem(R.id.action_settings).setVisible(false)
                menu.findItem(R.id.action_signup).setVisible(false)
                menu.findItem(R.id.action_login).setVisible(false)
                menu.findItem(R.id.action_jobs).title = Html.fromHtml("<font color='#99CA3B'>Jobs</font>")
                menu.findItem(R.id.action_jobs).getIcon().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
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

                loggedin_header.setOnClickListener{
                    intent = Intent(applicationContext, ProfileView::class.java)
                    startActivity(intent)
                }
            }
        }
        else{
            if (navView != null) {
                val menu = navView.menu
                menu.findItem(R.id.action_employerzone).setVisible(true)
                menu.findItem(R.id.action_mentorzone).setVisible(true)
                menu.findItem(R.id.action_partnerzone).setVisible(true)
                menu.findItem(R.id.action_logout).setVisible(false)
                menu.findItem(R.id.action_settings).setVisible(false)
                menu.findItem(R.id.action_signup).setVisible(true)
                menu.findItem(R.id.action_login).setVisible(true)
                menu.findItem(R.id.action_jobs).title = Html.fromHtml("<font color='#99CA3B'>Jobs</font>")
                menu.findItem(R.id.action_jobs).getIcon().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
                navView.setNavigationItemSelectedListener(this)
                val hView = navView.getHeaderView(0)
                val loggedin_header = hView.findViewById(R.id.loggedin_layout) as RelativeLayout
                val nologgedin_layout = hView.findViewById(R.id.nologgedin_layout) as RelativeLayout
                loggedin_header.visibility = View.GONE
                nologgedin_layout.visibility = View.VISIBLE
            }
        }

        mgroupsRecyclerView = findViewById(R.id.recycler_view_groups)
        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
        mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
        hotJobsAdapter = JobsAdapter(listOfhotJobsdata, isLoggedIn, 1,0,listOfCompareJoineddata,0,"Jobs")
        mgroupsRecyclerView!!.adapter = hotJobsAdapter

        img_profile_toolbar.setOnClickListener{
            intent = Intent(applicationContext, ZActivityDashboard::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("isLoggedIn", true)
            intent.putExtra("pagetype", 0)
            startActivity(intent)
        }

//        profile_name.setText(EndPoints.USERNAME)
//        Picasso.with(applicationContext)
//            .load(EndPoints.PROFILE_ICON)
//            .placeholder(R.drawable.ic_launcher_foreground)
//            .into(img_profile)

//        Picasso.with(applicationContext)
//            .load(EndPoints.PROFILE_ICON)
//            .placeholder(R.drawable.ic_launcher_foreground)
//            .into(img_profilemain)

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

//        mainScroll_jobs.getViewTreeObserver().addOnScrollChangedListener(object : ViewTreeObserver.OnScrollChangedListener{
//
//            override fun onScrollChanged() {
//                val scrollBounds = Rect()
//                login_view.getHitRect(scrollBounds)
//                val scrollBounds1 = Rect()
//                layouttop.getHitRect(scrollBounds)
//                if (login_view.getLocalVisibleRect(scrollBounds) || layouttop.getLocalVisibleRect(scrollBounds1) ) {
//                    // if layout even a single pixel, is within the visible area do something
//                    Toast.makeText(applicationContext,"Down", Toast.LENGTH_LONG).show()
//                   // login_view1.visibility = View.GONE
//                } else {
//                    // if layout goes out or scrolled out of the visible area do something
//                   // login_view1.visibility = View.VISIBLE
//                    Toast.makeText(applicationContext,"Up", Toast.LENGTH_LONG).show()
//                }
//
//            }
//        })


        if(isLoggedIn){
            img_profile_toolbar.visibility = View.VISIBLE
//            notificaton.visibility = View.VISIBLE
            sign_in.visibility = View.GONE
            mygroups.visibility = View.VISIBLE
            mygroups_selected.visibility = View.VISIBLE
            mygroups1.visibility = View.VISIBLE
            mygroups_selected1.visibility = View.VISIBLE
            all_selected.visibility = View.GONE
            all_selected1.visibility = View.GONE
            listOfhotJobsdata.clear()
            loadAppliedJobs()
            listOfCompareGroupdata.clear()
            type_group=1
            mygroups.setTypeface(null, Typeface.BOLD);
            featured.setTypeface(null, Typeface.NORMAL);
            mygroups1.setTypeface(null, Typeface.BOLD);
            featured1.setTypeface(null, Typeface.NORMAL);
            all.setTypeface(null, Typeface.NORMAL);
            all1.setTypeface(null, Typeface.NORMAL);
        }
        else{
            img_profile_toolbar.visibility = View.GONE
//            notificaton.visibility = View.GONE
            sign_in.visibility = View.VISIBLE
            mygroups.visibility = View.GONE
            mygroups_selected.visibility = View.GONE
            featured_selected.visibility = View.VISIBLE
            mygroups1.visibility = View.GONE
            mygroups_selected1.visibility = View.GONE
            featured_selected1.visibility = View.VISIBLE
            all_selected.visibility = View.GONE
            all_selected1.visibility = View.GONE
            listOfhotJobsdata.clear()
//            loadFeaturedGroupData("1")
            type_group=2
            featured.setTypeface(null, Typeface.BOLD);
            mygroups.setTypeface(null, Typeface.NORMAL);
            mygroups1.setTypeface(null, Typeface.BOLD);
            featured1.setTypeface(null, Typeface.NORMAL);
            all.setTypeface(null, Typeface.NORMAL);
            all1.setTypeface(null, Typeface.NORMAL);
        }

        sign_in.setOnClickListener {
            intent = Intent(applicationContext, SplashActivity::class.java)
            startActivity(intent)
        }

        loadCategoryData()

        filter1.setOnClickListener {
            filter.callOnClick()
        }

        filter_bottom.setOnClickListener{
            filter.callOnClick()
        }


        filter.setOnClickListener {
            find_morejobs.callOnClick()
            hideSuccesLayout()
            openBottomSheetFilter()

        }


        search_layout.setOnClickListener {

            keyword_data = keyword.text.toString().trim()
            var key = ""

            if(keyword_data.length>0)
                key = keyword_data
            if(city.text.toString().trim().length>0)
                location_name = city.text.toString().trim()
            if(experience.text.toString().trim().length>0)
                min_year = experience.text.toString().trim()
            if(functionalarea.text.toString().trim().length>0)
                functional_area = functionalarea.text.toString().trim()
            if(industry.text.toString().trim().length>0)
                industries = industry.text.toString().trim()

            loadFilteredData("1",key,location_name, "",min_year,min_year,functional_area,industries,"","")
        }


//        search_default.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                val DRAWABLE_LEFT = 0
//                val DRAWABLE_TOP = 1
//                val DRAWABLE_RIGHT = 2
//                val DRAWABLE_BOTTOM = 3
//                if (event!!.getAction() == MotionEvent.ACTION_UP) {
//                    if (event.getRawX() >= (search_default.getRight() - search_default.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
//                        loadFilteredData("1",search_default.text.toString().trim(),"", "","","","","","","")
//                        return true;
//                    }
//                }
//                return false;
//
//                return v?.onTouchEvent(event) ?: true
//            }
//        })

        search_default_button.setOnClickListener {
            keyword_data = search_default.text.toString().trim()
            loadFilteredData("1",keyword_data,"", "","","","","","","")
        }

        search_success_button.setOnClickListener {
            loadFilteredData("1",search_success.text.toString().trim(),"", "","","","","","","")

        }

//        search_success.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                val DRAWABLE_LEFT = 0
//                val DRAWABLE_TOP = 1
//                val DRAWABLE_RIGHT = 2
//                val DRAWABLE_BOTTOM = 3
//                if (event!!.getAction() == MotionEvent.ACTION_UP) {
//                    if (event.getRawX() >= (search_success.getRight() - search_success.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
//                        hideSuccesLayout()
//                        loadFilteredData("1",search_success.text.toString().trim(),"", "","","","","","","")
//                        return true;
//                    }
//                }
//                return false;
//
//                return v?.onTouchEvent(event) ?: true
//            }
//        })


        find_morejobs.setOnClickListener {

            filter.setText("FILTER")
            isfilter =0
            location_name=""
            job_types=""
//            min_year="0"
//            max_year="0"
            functional_area=""
            industries=""
            company_id=""
            type=""

            keyword.setText("")
            city.setText("")
            functionalarea.setText("")
            industry.setText("")
            salary.setText("")
            experience.setText("")

            filer_default_layout.visibility = View.GONE

//            layouttop.visibility = View.VISIBLE
//            layoutadd.visibility = View.VISIBLE
            // layout1.visibility = View.VISIBLE
//            activity_jfh_banner_jobs.visibility = View.VISIBLE
            mainScroll_jobs.fullScroll(View.FOCUS_UP);
            mainScroll_jobs.smoothScrollTo(0,0);
            recycler_view_groups.setFocusable(false);

            mygroups_selected.visibility = View.VISIBLE
            featured_selected.visibility = View.GONE
            mygroups_selected1.visibility = View.VISIBLE
            featured_selected1.visibility = View.GONE
            all_selected.visibility = View.GONE
            all_selected1.visibility = View.GONE
            type_group=1
            // Toast.makeText(applicationContext,"hot jobs",Toast.LENGTH_LONG).show()
            if (isfilter>0) {
                loadFilteredData("1",keyword_data,location_name, job_types,min_year,max_year,
                    functional_area,industries,company_id,"boosted")
            }
            else{
                listOfCompareGroupdata.clear()
                listOfCompareJoineddata.clear()
                listOfhotJobsdata.clear()
                loadAppliedJobs()
                //loadhotJobsData("1")
            }


            featured.setTypeface(null, Typeface.NORMAL);
            mygroups.setTypeface(null, Typeface.BOLD);
            featured1.setTypeface(null, Typeface.NORMAL);
            mygroups1.setTypeface(null, Typeface.BOLD);
            all.setTypeface(null, Typeface.NORMAL);
            all1.setTypeface(null, Typeface.NORMAL);
        }

        find_morejobs1.setOnClickListener {
            success_default_layout.visibility = View.GONE
            joblisting_tag.visibility = View.GONE
            layoutdefaultjob.visibility = View.GONE
            find_morejobs.callOnClick()
//            filter.setText("FILTER")
//            isfilter =0
//
//            activity_jfh_banner_jobs.visibility = View.VISIBLE
//            filer_default_layout.visibility = View.GONE
//            success_default_layout.visibility = View.GONE
//            layouttop.visibility = View.VISIBLE
//            layoutadd.visibility = View.VISIBLE
//           // layout1.visibility = View.VISIBLE
//            mainScroll_jobs.fullScroll(View.FOCUS_UP);
//            mainScroll_jobs.smoothScrollTo(0,0);
//            recycler_view_groups.setFocusable(false);
        }

        create_preferences.setOnClickListener {
            create_preferences_layout.callOnClick()
        }

        create_preferences_layout.setOnClickListener {

            openBottomSheetJobAlerts(0)
        }

        getAllCategories()

        mainScroll_jobss.getViewTreeObserver().addOnScrollChangedListener(object: ViewTreeObserver.OnScrollChangedListener {
            override fun onScrollChanged() {
                Log.d("TAGG","END EFORE")
                if(isfilter==0) {
                    val view: View = mainScroll_jobss.getChildAt(mainScroll_jobss.getChildCount() - 1);
                    val diff: Int = (view.getBottom() - (mainScroll_jobss.getHeight() + mainScroll_jobss.getScrollY()));
                    if (diff == 0 && scroll == 1) {
                        Log.d("TAGG", "END")
                        if (type_group == 1)
                            loadhotJobsData(next_page_no_featured)
                        else if (type_group == 2)
                            loadFeaturedGroupData(next_page_no_featured!!)
                        else if (type_group == 3)
                            loadRecommendedJobs(next_page_no_featured!!)
                        else if(type_group ==4)
                            loadOtherGroupData(next_page_no_featured!!)
                    }

                }
                else{
//                    val view: View = mainScroll_jobss.getChildAt(mainScroll_jobss.getChildCount() - 1);
//                    val diff: Int = (view.getBottom() - (mainScroll_jobss.getHeight() + mainScroll_jobss.getScrollY()));
//                    if (diff == 0 && scroll == 1) {
//                        if (type_group==1) {
//                            loadFilteredData(
//                                next_page_no_featured, keyword_data, location_name, job_types, min_year, max_year,
//                                functional_area, industries, company_id, "boosted"
//                            )
//                        }
//                        else{
//                            loadFilteredData(
//                                next_page_no_featured, keyword_data, location_name, job_types, min_year, max_year,
//                                functional_area, industries, company_id, "basic"
//                            )
//                        }
//
//                    }
                }

                val scrollBounds = Rect()
                login_view.getHitRect(scrollBounds)
                val scrollBounds1 = Rect()
                layouttop.getHitRect(scrollBounds)
                if (login_view.getLocalVisibleRect(scrollBounds) || layouttop.getLocalVisibleRect(scrollBounds1) ) {
                    login_view1.visibility = View.GONE
                }
                else{
                    login_view1.visibility = View.VISIBLE
                }
            }
        })

        mygroups1.setOnClickListener {
            mygroups.callOnClick()
        }

        featured1.setOnClickListener {
            featured.callOnClick()
        }

        all1.setOnClickListener {
            all.callOnClick()
        }

        mygroups.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                hideSuccesLayout()
                //find_morejobs.callOnClick()
                mygroups_selected.visibility = View.VISIBLE
                featured_selected.visibility = View.GONE
                mygroups_selected1.visibility = View.VISIBLE
                featured_selected1.visibility = View.GONE
                all_selected.visibility = View.GONE
                all_selected1.visibility = View.GONE
                type_group=1
//                Toast.makeText(applicationContext,"hot jobs",Toast.LENGTH_LONG).show()
                if (isfilter>0) {
                    loadFilteredData("1",keyword_data,location_name, job_types,min_year,max_year,
                        functional_area,industries,company_id,"boosted")
                }
                else{
                    listOfCompareGroupdata.clear()
                    listOfCompareJoineddata.clear()
                    listOfhotJobsdata.clear()
                    loadAppliedJobs()
                    //loadhotJobsData("1")
                }


                featured.setTypeface(null, Typeface.NORMAL);
                mygroups.setTypeface(null, Typeface.BOLD);
                featured1.setTypeface(null, Typeface.NORMAL);
                mygroups1.setTypeface(null, Typeface.BOLD);
                all.setTypeface(null, Typeface.NORMAL);
                all1.setTypeface(null, Typeface.NORMAL);
            }
        })
        featured.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                hideSuccesLayout()
                //find_morejobs.callOnClick()
                mygroups_selected.visibility = View.GONE
                featured_selected.visibility = View.VISIBLE
                mygroups_selected1.visibility = View.GONE
                featured_selected1.visibility = View.VISIBLE
                all_selected.visibility = View.GONE
                all_selected1.visibility = View.GONE
                type_group=2
//                Toast.makeText(applicationContext,"Other",Toast.LENGTH_LONG).show()
                if (isfilter>0){
                    loadFilteredData("1",keyword_data,location_name, job_types,min_year,max_year,
                        functional_area,industries,company_id,"basic")
                }
                else{
                    listOfhotJobsdata.clear()
                    loadFeaturedGroupData("1")
                }

                featured.setTypeface(null, Typeface.BOLD);
                mygroups.setTypeface(null, Typeface.NORMAL);
                featured1.setTypeface(null, Typeface.BOLD);
                mygroups1.setTypeface(null, Typeface.NORMAL);
                all.setTypeface(null, Typeface.NORMAL);
                all1.setTypeface(null, Typeface.NORMAL);
            }
        })

        all.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                // find_morejobs.callOnClick()
                mygroups_selected.visibility = View.GONE
                featured_selected.visibility = View.GONE
                mygroups_selected1.visibility = View.GONE
                featured_selected1.visibility = View.GONE
                all_selected.visibility = View.VISIBLE
                all_selected1.visibility = View.VISIBLE
                type_group=3
//                Toast.makeText(applicationContext,"All groups",Toast.LENGTH_LONG).show()
                if (isfilter>0){
//                    var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
//                    mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
//                    hotJobsAdapter = MyGroupsAdapter(listOfhotJobsdata, isLoggedIn, type_group, listOfCompareGroupdata,1)
//                    mgroupsRecyclerView!!.adapter = hotJobsAdapter
                }
                else{
                    listOfhotJobsdata.clear()
                    loadRecommendedJobs("1")
                }

                featured.setTypeface(null, Typeface.NORMAL);
                mygroups.setTypeface(null, Typeface.NORMAL);
                featured1.setTypeface(null, Typeface.NORMAL);
                mygroups1.setTypeface(null, Typeface.NORMAL);
                all.setTypeface(null, Typeface.BOLD);
                all1.setTypeface(null, Typeface.BOLD);
            }
        })

        loadprev.setOnClickListener {

            // listOfhotJobsdata.clear()
            if (type_group==1)
                loadhotJobsData(prev_page_no_featured)
            else if (type_group==2){}
//                loadFeaturedGroupData(prev_page_no_featured!!)
            else if(type_group==3){}
//                loadAllGroupData(prev_page_no_featured)
        }

        loadnext.setOnClickListener {
            // listOfhotJobsdata.clear()
            if (type_group==1)
                loadhotJobsData(next_page_no_featured)
            else if (type_group==2){}
//                loadFeaturedGroupData(next_page_no_featured!!)
            else if(type_group==3){}
//                loadAllGroupData(next_page_no_featured)
        }

        notifLayout.setOnClickListener {
            cart_badge.setText("0")
            intent = Intent(applicationContext, Notification::class.java)
            startActivity(intent)
        }
    }

//    override fun onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed()
//            return
//        }
//        this.doubleBackToExitPressedOnce = true
//
//        ToastHelper.makeToast(applicationContext, "Please click BACK again to exit")
//
//        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
//    }

    public fun hideSuccesLayout(){
        success_default_layout.visibility = View.GONE
//        layouttop.visibility = View.VISIBLE
//        layoutadd.visibility = View.VISIBLE
        // layout1.visibility = View.VISIBLE
//        activity_jfh_banner_jobs.visibility = View.VISIBLE
    }

    public fun openBottomSheetJobAlerts(id: Int) {

        val dialog = Dialog(this)
        dialog .setCancelable(true)
        dialog .setContentView(R.layout.bottom_sheet_preferencesdashboard)

        val header = dialog.findViewById(R.id.closetext) as TextView
        if(id==0)
            header.setText("Add Job Alert")
        else
            header.setText("Edit Job Alert")

        val pref_title= dialog.findViewById(R.id.title) as EditText
        val pref_keyword= dialog.findViewById(R.id.pref_keyword) as EditText
        val pref_location_multiAutoCompleteTextView = dialog.findViewById(R.id.pref_location_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val locationList: ArrayList<String> = ArrayList()
        for (model in listOfCities) {
            locationList.add(model.name!!.toString())
        }
        val locationArray = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        pref_location_multiAutoCompleteTextView.setAdapter(locationArray)
        pref_location_multiAutoCompleteTextView.setThreshold(1)
        pref_location_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        val jobtype_multiAutoCompleteTextView = dialog.findViewById(R.id.jobtype_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val jobTypeList: ArrayList<String> = ArrayList()
        for (model in listOfJobType) {
            jobTypeList.add(model.name!!.toString())
        }
        val randomArray = ArrayAdapter(this, android.R.layout.simple_list_item_1, jobTypeList)
        jobtype_multiAutoCompleteTextView.setAdapter(randomArray)
        jobtype_multiAutoCompleteTextView.setThreshold(1)
        jobtype_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        val farea_multiAutoCompleteTextView = dialog.findViewById(R.id.farea_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val functionalareaList: ArrayList<String> = ArrayList()
        for (model in listOfJobFArea) {
            functionalareaList.add(model.name!!.toString())
        }
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, functionalareaList)
        farea_multiAutoCompleteTextView.setAdapter(adapter)
        farea_multiAutoCompleteTextView.setThreshold(1)
        farea_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        val industry_multiAutoCompleteTextView = dialog.findViewById(R.id.industry_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val industryList: ArrayList<String> = ArrayList()
        for (model in listOfJobIndustry) {
            industryList.add(model.name!!.toString())
        }
        val experienceArray = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, industryList)
        industry_multiAutoCompleteTextView.setAdapter(experienceArray)
        industry_multiAutoCompleteTextView.setThreshold(1)
        industry_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        val experience_multiAutoCompleteTextView = dialog.findViewById(R.id.experience_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val experienceList: ArrayList<String> = ArrayList()
        for(i in 0..50)
            experienceList.add(i.toString())
        val adapter1 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, experienceList)
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
            if(pref_location_multiAutoCompleteTextView.text.trim().toString().length>0 &&
                pref_title.text.trim().toString().length>0 ) {

                if(id>0) {
//                    saveJobAlerts(id,
//                        pref_title.text.trim().toString(),
//                        pref_location_multiAutoCompleteTextView.text.trim().toString(),
//                        jobtype_multiAutoCompleteTextView.text.trim().toString(),
//                        farea_multiAutoCompleteTextView.text.trim().toString(),
//                        industry_multiAutoCompleteTextView.text.trim().toString(),
//                        pref_keyword.text.trim().toString(),
//                        experience_multiAutoCompleteTextView.text.trim().toString()
//                    )
                }
                else{
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
            }
            else{
                Toast.makeText(applicationContext,"Please enter title and city",Toast.LENGTH_LONG).show()
            }
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


    fun addJobAlerts(pref_title: String,pref_location: String, jobtype: String, farea:String, industry: String, pref_keyword:String,experience:String ){

        val params = HashMap<String, String>()

        params["title"] = pref_title.toString()
        if (pref_location.equals("")){}
        else{
            params["city_name"] = pref_location.substring(0,pref_location.length-1)
        }
        if (jobtype.equals("")){}
        else{
            params["job_type"] = jobtype.substring(0,jobtype.length-1)
        }
        if (farea.equals("")){}
        else{
            params["functional_area"] = farea.substring(0,farea.length-1)
        }
        if (industry.equals("")){}
        else{
            params["industry"] = industry.substring(0,industry.length-1)
        }
        if (pref_keyword.equals("")){}
        else{
            params["skills"] = pref_keyword.toString()
        }
        if (experience.equals("")){}
        else{
            if (experience.contains("Yrs")==true) {
                var s = experience.substring(0, experience.length - 3)
                val parts = s.split("-")
                params["experience_max_year"] = parts.get(1).toString()
                params["experience_min_year"] = parts.get(0).toString()
            }
            else{
                params["experience_max_year"] = experience.toString()
                params["experience_min_year"] = "0"
            }
        }

        Log.d("TAGG", params.toString())

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.AddJobAlerts(EndPoints.CLIENT_ID, "Bearer "+
                EndPoints.ACCESS_TOKEN,params)

        call.enqueue(object : Callback<JobAlertResponse> {
            override fun onResponse(call: Call<JobAlertResponse>, response: Response<JobAlertResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.e("RESPONSE report group", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                    Logger.e("TAGG", "Add Success" + response.body()!!.message.toString())
                    //loadJobAlert()
                    Snackbar.make(
                        main,
                        "Your Job Alert ‘"+pref_title.toString()+"’ has been created. You will now receive job alerts " +
                                "in your email, twice a week.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    // loadJobAlert()
                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                    Logger.e("TAGG", "Job Alert Created" + response.body()!!.message.toString())
                }
            }

            override fun onFailure(call: Call<JobAlertResponse>, t: Throwable) {
                //loadJobAlert()
                Snackbar.make(
                    main,
                    "Your Job Alert ‘"+pref_title.toString()+"’ has been created. You will now receive job alerts " +
                            "in your email, twice a week.",
                    Snackbar.LENGTH_SHORT
                ).show()
                Logger.d("TAGG", "FAILED : $t")
                Logger.e("TAGG", "Job Alert Created" )
            }
        })

    }



    public fun openBottomSheetPreferences() {

        val dialog = Dialog(this)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog .setCancelable(true)
        dialog .setContentView(R.layout.bottom_sheet_preferences)

        val pref_keyword= dialog.findViewById(R.id.pref_keyword) as EditText
        val pref_location_multiAutoCompleteTextView = dialog.findViewById(R.id.pref_location_multiAutoCompleteTextView) as MultiAutoCompleteTextView
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

        val jobtype_multiAutoCompleteTextView = dialog.findViewById(R.id.jobtype_multiAutoCompleteTextView) as MultiAutoCompleteTextView
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

        val farea_multiAutoCompleteTextView = dialog.findViewById(R.id.farea_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val functionalareaList: ArrayList<String> = ArrayList()
        for (model in listOfJobFArea) {
            functionalareaList.add(model.name!!.toString())
        }
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, functionalareaList)
        farea_multiAutoCompleteTextView.setAdapter(adapter)
        farea_multiAutoCompleteTextView.setThreshold(1)
        farea_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        farea_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                farea_multiAutoCompleteTextView.showDropDown()
                return false
            }
        })

        val industry_multiAutoCompleteTextView = dialog.findViewById(R.id.industry_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val industryList: ArrayList<String> = ArrayList()
        for (model in listOfJobIndustry) {
            industryList.add(model.name!!.toString())
        }
        val experienceArray = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, industryList)
        industry_multiAutoCompleteTextView.setAdapter(experienceArray)
        industry_multiAutoCompleteTextView.setThreshold(1)
        industry_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        industry_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                industry_multiAutoCompleteTextView.showDropDown()
                return false
            }
        })

        val experience_multiAutoCompleteTextView = dialog.findViewById(R.id.experience_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val experienceList: ArrayList<String> = ArrayList()
        for(i in 0..50)
            experienceList.add(i.toString())
        val adapter1 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, experienceList)
        experience_multiAutoCompleteTextView.setAdapter(adapter1)
        experience_multiAutoCompleteTextView.setThreshold(1)
        experience_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        experience_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                experience_multiAutoCompleteTextView.showDropDown()
                return false
            }
        })

        val experienceto_multiAutoCompleteTextView = dialog.findViewById(R.id.experienceto_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        experienceto_multiAutoCompleteTextView.setAdapter(adapter1)
        experienceto_multiAutoCompleteTextView.setThreshold(1)
        experienceto_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        experienceto_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                experienceto_multiAutoCompleteTextView.showDropDown()
                return false
            }
        })

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
            if(pref_location_multiAutoCompleteTextView.text.trim().toString().length>0 ||
                jobtype_multiAutoCompleteTextView.text.trim().toString().length>0 ||
                farea_multiAutoCompleteTextView.text.trim().toString().length>0  ||
                industry_multiAutoCompleteTextView.text.trim().toString().length>0 ||
                pref_keyword.text.trim().toString().length>0 ||
                experience_multiAutoCompleteTextView.text.trim().toString().length>0    ) {
                if(Integer.parseInt(experience_multiAutoCompleteTextView.text.toString())<
                    Integer.parseInt(experienceto_multiAutoCompleteTextView.text.toString())) {
                savePreferences(
                    pref_location_multiAutoCompleteTextView.text.trim().toString(),
                    jobtype_multiAutoCompleteTextView.text.trim().toString(),
                    farea_multiAutoCompleteTextView.text.trim().toString(),
                    industry_multiAutoCompleteTextView.text.trim().toString(),
                    pref_keyword.text.trim().toString(),
                    experience_multiAutoCompleteTextView.text.trim().toString(),
                    experienceto_multiAutoCompleteTextView.text.trim().toString()
                )
                dialog.cancel()
                }
                else
                    Toast.makeText(applicationContext,"From experience should be less then To experience",Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(applicationContext,"Please enter all the values",Toast.LENGTH_LONG).show()
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


    fun savePreferences(pref_location: String, jobtype: String, farea:String, industry: String, pref_keyword:String,experience:String ,
                        maxexperience:String){

        val params = HashMap<String, String>()

        if (pref_location.equals("")){}
        else{
            params["preferred_city"] = pref_location.toString()
        }
        if (jobtype.equals("")){}
        else{
            params["preferred_job_type"] = jobtype.substring(0,jobtype.length-1)
        }
        if (farea.equals("")){}
        else{
            params["preferred_functional_area"] = farea.substring(0,farea.length-1)
        }
        if (industry.equals("")){}
        else{
            params["preferred_industry"] = industry.substring(0,industry.length-1)
        }
        if (pref_keyword.equals("")){}
        else{
            params["skills"] = pref_keyword.toString()
        }
        if (experience.equals("")){}
        else{
            if(experience.contains(",")) {
                params["exp_from_year"] = experience.substring(0, experience.length - 1)
            }
            else{
                params["exp_from_year"] = experience.toString()
            }
        }
        if (maxexperience.equals("")){}
        else{
            if(maxexperience.contains(",")) {
                params["exp_to_year"] = maxexperience.substring(0, experience.length - 1)
            }
            else{
                params["exp_to_year"] = maxexperience.toString()
            }
        }

        Log.d("TAGG", params.toString())

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.PostPreferences(EndPoints.CLIENT_ID, "Bearer "+
                EndPoints.ACCESS_TOKEN,params)

        call.enqueue(object : Callback<UpdateReplyResponse> {
            override fun onResponse(call: Call<UpdateReplyResponse>, response: Response<UpdateReplyResponse>) {

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


    public fun openBottomSheetPreferencesDashboard(id:Int, title:String, id1:Int, isRequired: Boolean, isboosted: Boolean) {

        val dialog = Dialog(this)
        dialog .setCancelable(true)
        dialog .setContentView(R.layout.bottom_sheet_preferences)

        val pref_keyword= dialog.findViewById(R.id.pref_keyword) as EditText
        val pref_location_multiAutoCompleteTextView = dialog.findViewById(R.id.pref_location_multiAutoCompleteTextView) as MultiAutoCompleteTextView
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

        val jobtype_multiAutoCompleteTextView = dialog.findViewById(R.id.jobtype_multiAutoCompleteTextView) as MultiAutoCompleteTextView
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

        val farea_multiAutoCompleteTextView = dialog.findViewById(R.id.farea_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val functionalareaList: ArrayList<String> = ArrayList()
        for (model in listOfJobFArea) {
            functionalareaList.add(model.name!!.toString())
        }
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, functionalareaList)
        farea_multiAutoCompleteTextView.setAdapter(adapter)
        farea_multiAutoCompleteTextView.setThreshold(1)
        farea_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        farea_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                farea_multiAutoCompleteTextView.showDropDown()
                return false
            }
        })

        val industry_multiAutoCompleteTextView = dialog.findViewById(R.id.industry_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val industryList: ArrayList<String> = ArrayList()
        for (model in listOfJobIndustry) {
            industryList.add(model.name!!.toString())
        }
        val experienceArray = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, industryList)
        industry_multiAutoCompleteTextView.setAdapter(experienceArray)
        industry_multiAutoCompleteTextView.setThreshold(1)
        industry_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        industry_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                industry_multiAutoCompleteTextView.showDropDown()
                return false
            }
        })

        val experience_multiAutoCompleteTextView = dialog.findViewById(R.id.experience_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val experienceList: ArrayList<String> = ArrayList()
        for(i in 0..50)
            experienceList.add(i.toString())
        val adapter1 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, experienceList)
        experience_multiAutoCompleteTextView.setAdapter(adapter1)
        experience_multiAutoCompleteTextView.setThreshold(1)
        experience_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        experience_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                experience_multiAutoCompleteTextView.showDropDown()
                return false
            }
        })

        val experienceto_multiAutoCompleteTextView = dialog.findViewById(R.id.experienceto_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        experienceto_multiAutoCompleteTextView.setAdapter(adapter1)
        experienceto_multiAutoCompleteTextView.setThreshold(1)
        experienceto_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        experienceto_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                experienceto_multiAutoCompleteTextView.showDropDown()
                return false
            }
        })

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
            if(pref_location_multiAutoCompleteTextView.text.trim().toString().length>0  &&
                pref_keyword.text.trim().toString().length>0 &&
                jobtype_multiAutoCompleteTextView.text.trim().toString().length>0  &&
                farea_multiAutoCompleteTextView.text.trim().toString().length>0  &&
                industry_multiAutoCompleteTextView.text.trim().toString().length>0
            ) {
                updatePreferences(
                    id,title,id1,isRequired,isboosted,
                    pref_location_multiAutoCompleteTextView.text.trim().toString(),
                    jobtype_multiAutoCompleteTextView.text.trim().toString(),
                    farea_multiAutoCompleteTextView.text.trim().toString(),
                    industry_multiAutoCompleteTextView.text.trim().toString(),
                    pref_keyword.text.trim().toString(),
                    experience_multiAutoCompleteTextView.text.trim().toString(),
                    experienceto_multiAutoCompleteTextView.text.trim().toString()
                )
                dialog.cancel()
            }
            else{
                Toast.makeText(applicationContext,"Please enter all the values",Toast.LENGTH_LONG).show()
            }
        }

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.CheckPreferences("application/json",EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)

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

                    if (response.isSuccessful) {
                        val json_objectdetail: JSONObject = jsonarray_info.optJSONObject(0)
//                    Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                        pref_location_multiAutoCompleteTextView.setText(
                            (if (json_objectdetail.isNull(
                                    "preferred_city"
                                )
                            ) "" else json_objectdetail.optString("preferred_city") + ",")
                        )
                        jobtype_multiAutoCompleteTextView.setText(
                            (if (json_objectdetail.isNull("preferred_job_type")) "" else json_objectdetail.optString(
                                "preferred_job_type"
                            ) + ",")
                        )
                        farea_multiAutoCompleteTextView.setText(
                            (if (json_objectdetail.isNull("preferred_functional_area")) "" else json_objectdetail.optString(
                                "preferred_functional_area"
                            ) + ",")
                        )
                        industry_multiAutoCompleteTextView.setText(
                            (if (json_objectdetail.isNull("preferred_industry")) "" else json_objectdetail.optString(
                                "preferred_industry"
                            ) + ",")
                        )
                        experience_multiAutoCompleteTextView.setText(json_objectdetail.optString("exp_to_year"))
                        pref_keyword.setText(json_objectdetail.optString("skills"))
                    }

                }
            }

            override fun onFailure(call: Call<CheckPreferenceResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
            }
        })

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

    fun updatePreferences(id:Int,title:String,id1:Int,isRequired: Boolean,isboosted: Boolean,pref_location: String, jobtype: String, farea:String, industry: String, pref_keyword:String,
                          experience:String, maxexperience:String ){

        val params = HashMap<String, String>()

        if (pref_location.equals("")){}
        else{
            params["preferred_city"] = pref_location.substring(0,pref_location.length-1)
        }
        if (jobtype.equals("")){}
        else{
            params["preferred_job_type"] = jobtype.substring(0,jobtype.length-1)
        }
        if (farea.equals("")){}
        else{
            params["preferred_functional_area"] = farea.substring(0,farea.length-1)
        }
        if (industry.equals("")){}
        else{
            params["preferred_industry"] = industry.substring(0,industry.length-1)
        }
        if (pref_keyword.equals("")){}
        else{
            params["skills"] = pref_keyword.toString()
        }
        if (experience.equals("")||experience.equals("Not specified")){params["exp_from_year"] = ""
            params["exp_to_year"] = ""}
        else{
            if(experience.contains(",")) {
                params["exp_from_year"] = experience.substring(0, experience.length - 1)
            }
            else{
                params["exp_from_year"] = experience.toString()
            }
        }
        if (maxexperience.equals("")||experience.equals("Not specified")){params["exp_from_year"] = ""
            params["exp_to_year"] = ""}
        else{
            if(maxexperience.contains(",")) {
                params["exp_to_year"] = maxexperience.substring(0, experience.length - 1)
            }
            else{
                params["exp_to_year"] = maxexperience.toString()
            }
        }

        Log.d("TAGG", params.toString())
        //val s:Int = email_resendp.getTag(R.string.Follow_us_on) as Int
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.updatePreferences(id,EndPoints.CLIENT_ID, "Bearer "+
                EndPoints.ACCESS_TOKEN,params)

        call.enqueue(object : Callback<UpdateReplyResponse> {
            override fun onResponse(call: Call<UpdateReplyResponse>, response: Response<UpdateReplyResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.e("RESPONSE report group", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                    Logger.e("TAGG", "SAVE PREFERENCES" + response.body()!!.message.toString())

                    checkDefault(id, title, id1, isRequired, isboosted)
                } else {

                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<UpdateReplyResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })

    }


    var isSelectedId:Int = 0
    fun getRadioSelected(a:Int){
        Log.d("TAGG","Slected"+a)
        isSelectedId = a
    }



    public fun openBottomSheetUploadDoc(jobId:Int, title: String ,listOfResume: ArrayList<ResumeView>, isboosted: Boolean) {

        jobname = title

        var dialog1  = Dialog(this)
        dialog1 .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog1.setCancelable(true)
        dialog1 .setContentView(R.layout.bottom_sheet_applyjobs)
        val add_resume = dialog1.findViewById(R.id.add_resume) as LinearLayout
        val add_resume_title = dialog1.findViewById(R.id.add_resume_title) as LinearLayout
        val note = dialog1.findViewById(R.id.note) as EditText

        Log.d("TAGG","List"+listOfResume.size)

        var mAdapterResume: RecyclerView.Adapter<*>? = null
        val mRecyclerView = dialog1.findViewById(R.id.resume_recycler_view) as RecyclerView
        val mLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL,false)
        var checklist: ArrayList<Int>  = ArrayList()
        for(k in 0 until listOfResume.size) {
            if (k == 0)
                checklist.add(1)
            else
                checklist.add(0)
        }
        if(listOfResumes.size>1)
            mRecyclerView.getLayoutParams().height = 600;

        mAdapterResume = ResumeAdapter1(listOfResumes,checklist,"Jobs")
        mRecyclerView!!.adapter = mAdapterResume
        mRecyclerView!!.layoutManager = mLayoutManager




        val closebutton = dialog1.findViewById(R.id.reportedtext) as TextView
        closebutton.setOnClickListener {
            dialog1.cancel()
        }
        closebutton.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val DRAWABLE_LEFT = 0;
                val DRAWABLE_TOP = 1;
                val DRAWABLE_RIGHT = 2;
                val DRAWABLE_BOTTOM = 3;
                if (event!!.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (closebutton.getRight() - closebutton.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        dialog1.cancel()
                        return true;
                    }
                }
                return false;

                return v?.onTouchEvent(event) ?: true
            }
        })
        val jobname = dialog1.findViewById(R.id.jobname) as TextView
        if(isboosted) {
            val msg: String = "Job Title: "+title + " "
            val mImageSpan: ImageSpan = ImageSpan(applicationContext, R.drawable.ic_hot_job);
            val text: SpannableString = SpannableString(msg);
            text.setSpan(mImageSpan, msg.length - 1, msg.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            jobname.setText(text);
        }
        else{
            jobname.setText("Job Title: "+title)
        }

        var upload_doc = dialog1.findViewById(R.id.upload_doc) as RelativeLayout
        upload_doc.setOnClickListener {
            showPictureDialog()
        }
        val cancel_pref = dialog1.findViewById(R.id.cancel_pref) as Button
        cancel_pref.setOnClickListener {
            dialog1.cancel()
        }
        val restitle = dialog1.findViewById(R.id.resumeTitle) as EditText
        val mobileno = dialog1.findViewById(R.id.mobile) as EditText
        val mobileLayout = dialog1.findViewById(R.id.mobile_layout) as LinearLayout
        Log.d("TAGG","MOBILE"+EndPoints.PHONE_NO)
        if(EndPoints.PHONE_NO.length>2)
            mobileLayout.visibility = View.GONE
        val save_pref = dialog1.findViewById(R.id.save_pref) as Button
        save_pref.setOnClickListener {
            if(restitle.getText().length>0) {
                // if (mobileno.getText().length > 0 && mobileno.length() >6 && mobileno.length() < 13) {
                if (listOfResumes.size > 0) {
                    //if(isSelected) {
                    Toast.makeText(applicationContext, "Applied", Toast.LENGTH_LONG).show()
                    saveJob(jobId, note.text.toString(), isSelectedId, "Applied")
                    dialog1.cancel()
//                    }
//                    else{
//                        Toast.makeText(applicationContext,"Please select a resume",Toast.LENGTH_LONG).show()
//                    }
                } else {
                    sendResume(restitle.getText().toString(), jobId)
                    dialog1.cancel()
                }
//                } else {
//                    Toast.makeText(applicationContext, "Please enter valid mobile number", Toast.LENGTH_LONG).show()
//
//                }
            }
            else{
                Toast.makeText(applicationContext, "Please enter resume title", Toast.LENGTH_LONG).show()
            }
        }
        val apply_job = dialog1.findViewById(R.id.apply_pref) as Button
        apply_job.setOnClickListener {

            // if (mobileno.getText().length > 0 && mobileno.length() >6 && mobileno.length() < 13) {
            if (listOfResumes.size > 0) {
//                    if(isSelected) {
                Toast.makeText(applicationContext, "Applied", Toast.LENGTH_LONG).show()
                saveJob(jobId, note.text.toString(), isSelectedId, "Applied")
                dialog1.cancel()
//                    }
//                    else{
//                        Toast.makeText(applicationContext,"Please select a resume",Toast.LENGTH_LONG).show()
//                    }
            } else {
                if(picturepath_life!!.length>0) {
                    if(restitle.getText().length>0) {
                        sendResume(restitle.getText().toString(), jobId)
                        dialog1.cancel()
                    }
                    else{
                        Toast.makeText(applicationContext, "Please enter resume title", Toast.LENGTH_LONG).show()
                    }
                }
                else
                    Toast.makeText(applicationContext, "Please upload a resume", Toast.LENGTH_LONG).show()
            }
//                } else {
//                Toast.makeText(applicationContext, "Please enter a valid mobile number", Toast.LENGTH_LONG).show()
//            }
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


        var window :Window = dialog1.getWindow()!!;
        window.setBackgroundDrawable(null);
        var wlp: WindowManager.LayoutParams = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog1 .show()
    }

    private fun showPictureDialog() {

        val mimeTypes = arrayOf(
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
            "application/pdf"
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

    fun sendResume(title: String,jobId:Int){

        val params = HashMap<String, String>()

        params["title"] = title
        params["resume_filename"] = picturepath_life.toString()
        params["resume_file"] = imageEncoded_life.toString()

        Logger.d("TAGG", "PARAMS: "+params)
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.SendResume(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
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
                        Toast.makeText(
                            applicationContext,
                            "Adding Success!!" + jsonarray.optInt("id"),
                            Toast.LENGTH_LONG
                        ).show()

                        val resumeId = jsonarray.optInt("id")

                        saveJob(jobId, "", resumeId, "Applied")
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

    fun saveJob(job_id:Int,note:String,resume_id:Int,applied_status:String){

        val params = HashMap<String, String>()

        params["job_id"] = job_id.toString()
        params["note"] = note
        params["applied_status"] = applied_status
        if(resume_id>0)
            params["resume_id"] = resume_id.toString()

        Log.d("TAGG","PARAMS:"+params)
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.saveJob( EndPoints.CLIENT_ID, "Bearer "+
                EndPoints.ACCESS_TOKEN,params)

        call.enqueue(object : Callback<SaveJobResponse> {
            override fun onResponse(call: Call<SaveJobResponse>, response: Response<SaveJobResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.e("RESPONSE leave group", "" + Gson().toJson(response))

                if (response.isSuccessful) {
                    success_default_layout.visibility = View.VISIBLE
                    filer_default_layout.visibility  = View.GONE
//                    layouttop.visibility = View.GONE
//                    layoutadd.visibility = View.GONE
                    //  layout1.visibility = View.GONE
                    mainScroll_jobs.fullScroll(View.FOCUS_UP);
                    mainScroll_jobs.smoothScrollTo(0,0);
                    recycler_view_groups.setFocusable(false);
//                    activity_jfh_banner_jobs.visibility = View.GONE
                    Logger.e("TAGG", "" + response.body()!!.message.toString())

                    listOfhotJobsdata.clear()
                    loadAppliedJobs()
//                    val intent = Intent(applicationContext, ZActivityJobDetails::class.java)
//                    intent.putExtra("isLoggedIn",true)
//                    intent.putExtra("group_Id", job_id)
//                    intent.putExtra("title", jobname)
//                    intent.putExtra("isMygroup",1)
//                    intent.putExtra("page","Jobs")
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    startActivity(intent)

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

    fun loadFeaturedGroupData(pageno:String){

        if (pageno.equals("1")) {
            listOfhotJobsdata.clear()
            hotJobsAdapter!!.notifyDataSetChanged()
        }
        else{}

        val params = HashMap<String, String>()

        params["page_no"] = pageno.toString()
        params["page_size"] = Constants.MAXIMUM_PAGINATION_COUNT

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getotherJobsData( EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        call.enqueue(object : Callback<HotJobs> {
            override fun onResponse(call: Call<HotJobs>, response: Response<HotJobs>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)

                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                Logger.d("RESPONSE", "" + str_response)
                //var str_response = Gson().toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                    var jsonarray_info: JSONArray = jsonObject1.optJSONArray("body")
                    var jsonarray_pagination: JSONObject = jsonObject1.optJSONObject("pagination")

                    val has_next: String = jsonarray_pagination.getBoolean("has_next").toString()

                    val pagenoo: String = jsonarray_pagination.optString("page_no")
                    val prev_page: Int
//                    if (jsonarray_pagination.has("prev_page"))
//                        prev_page = jsonarray_pagination.optInt("prev_page")
//                    else
//                        prev_page = 0
                    if (Integer.parseInt(pagenoo)>1)
                        prev_page = Integer.parseInt(pagenoo)-1
                    else
                        prev_page = 0


                    if (response.isSuccessful) {

                        for (i in 0 until response.body()?.body?.size!!) {

                            var json_objectdetail: JSONObject = jsonarray_info.optJSONObject(i)

                            val model: JobsView = JobsView()

                            model.id = json_objectdetail.optInt("id")
                            model.title = json_objectdetail.optString("title")
                            model.company_name = json_objectdetail.optString("company_name")
                            model.company_logo = json_objectdetail.optString("company_logo")
                            model.location_name = json_objectdetail.optString("location_name")
                            model.max_year = json_objectdetail.optInt("max_year")
                            model.min_year = json_objectdetail.optInt("min_year")
                            model.status = json_objectdetail.optString("status")
                            model.modified_on = json_objectdetail.optString("modified_on")
                            model.application_count = 1//json_objectdetail.optInt("application_count")
                            model.company_id = 1//json_objectdetail.optInt("company_id")
                            model.employer_name = json_objectdetail.optString("employer_name")
                            model.new_application_count = 1//json_objectdetail.optInt("new_application_count")
                            model.created_on = json_objectdetail.optString("created_on")
                            model.boosted = json_objectdetail.getBoolean("boosted")
                            model.employer_id = json_objectdetail.optInt("employer_id")
                            model.resume_required = json_objectdetail.getBoolean("resume_required")

                            val listOfJobTypes: ArrayList<String> = ArrayList()
                            val listjson: JSONArray = JSONArray()
                            var job_typesArray: JSONArray = if(json_objectdetail.isNull("job_types"))listjson else json_objectdetail.optJSONArray("job_types")

                            for (j in 0 until job_typesArray.length()) {
                                    listOfJobTypes.add(job_typesArray.get(j).toString())
                            }

                            model.job_types = listOfJobTypes

                            listOfhotJobsdata.add(
                                JobsView(
                                    model.id,
                                    model.modified_on!!,
                                    model.application_count!!,
                                    model.company_id!!,
                                    model.job_types!!,
                                    model.employer_name!!,
                                    model.new_application_count!!,
                                    model.title!!,
                                    model.max_year!!,
                                    model.location_name!!,
                                    model.company_logo!!,
                                    model.created_on!!,
                                    model.company_name!!,
                                    model.boosted!!,
                                    model.min_year!!,
                                    model.employer_id!!,
                                    model.status!!,
                                    model.resume_required!!
                                )
                            )
                        }

                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                        mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                        hotJobsAdapter = JobsAdapter(listOfhotJobsdata, isLoggedIn, 2,0,listOfCompareJoineddata,0,"Jobs")
                        mgroupsRecyclerView!!.adapter = hotJobsAdapter

                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid Request")
                    }

                    if (has_next.equals("true")) {
                        val next_page: String = jsonarray_pagination.optInt("next_page").toString()
                        loadnext.visibility = View.VISIBLE
                        next_page_no_featured = next_page
                        scroll =1
                    } else {
                        loadnext.visibility = View.GONE
                        scroll =0
                    }

                    empty_view.visibility = View.GONE
                    loadprev.visibility = View.GONE
//                    if (pagenoo == "1")
//                        loadprev.visibility = View.GONE
//                    else {
//                        loadprev.visibility = View.VISIBLE
//                        prev_page_no_featured = prev_page.toString()
//                    }
                }
                else
                    Toast.makeText(applicationContext,"No data exists",Toast.LENGTH_LONG).show()
                empty_view.visibility = View.VISIBLE
            }

            override fun onFailure(call: Call<HotJobs>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                empty_view.visibility = View.VISIBLE

            }
        })
    }

    fun loadRecommendedJobs(pageno:String){

        if (pageno.equals("1")) {
            listOfhotJobsdata.clear()
            listOfCompareRecdata.clear()
            hotJobsAdapter!!.notifyDataSetChanged()
        }
        val params = java.util.HashMap<String, String>()

        params["page_no"] = pageno.toString()
        params["page_size"] = Constants.MAXIMUM_PAGINATION_COUNT
        retrofitInterface1 = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface1!!.getRecommendedJobs(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
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
                var jsonarray_pagination: JSONObject? = null
                var jsonarray_info: JSONArray? = null
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    jsonObject1 = jsonObject.optJSONObject("body")
                }
                if (jsonObject1 != null) {
                    val responseCode: Int = jsonObject1.optInt("response_code")
                    val message: String = jsonObject1.optString("message")
                    jsonarray_info = jsonObject1.optJSONArray("body")
                    jsonarray_pagination = jsonObject1.optJSONObject("pagination")
                }

                if (jsonarray_pagination != null) {
                    val hasnextRecommendedJobs = jsonarray_pagination.getBoolean("has_next")
                    val pagenoo: String = jsonarray_pagination.optString("next_page")
                    if (hasnextRecommendedJobs) {
                        val next_page: String = jsonarray_pagination.optInt("next_page").toString()
                        loadnext.visibility = View.VISIBLE
                        next_page_no_featured = next_page
                        scroll = 1
                    } else {
                        loadnext.visibility = View.GONE
                        scroll = 0
                    }
                }

                if (response.isSuccessful) {
                    if (jsonarray_info != null) {
                        for (k in 0 until response.body()?.body?.size!!) {
                            var jobs: JSONObject = jsonarray_info.optJSONObject(k)
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
                                ) false else jsonarray_info.optJSONObject(k).getBoolean(
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
                                jobs_types =
                                    jsonarray_info.optJSONObject(k).optJSONArray("job_types")
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
                                ) false else jsonarray_info.optJSONObject(k).getBoolean(
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
                            listOfCompareRecdata.add(0)

                        }
                    }

//                    if (listOfAppliedJobsdata.size>3) {
//                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
//                        mRecyclerViewPosts!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
//                        mAdapterJobs = JobsAdapter(listOfAppliedJobsdataTrimmed, isLoggedIn, 2, 0, listOfCompareJoineddata,1)
//                        mRecyclerViewPosts!!.adapter = mAdapterJobs
//                    }
//                    else{
                    var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                    mgroupsRecyclerView!!.layoutManager =
                        mgroupsLayoutManager as RecyclerView.LayoutManager?
                    hotJobsAdapter = JobsAdapter(
                        listOfhotJobsdata,
                        isLoggedIn,
                        2,
                        0,
                        listOfCompareRecdata,
                        0,
                        "Jobs"
                    )
                    mgroupsRecyclerView!!.adapter = hotJobsAdapter
//                    }

                    empty_view.visibility = View.GONE
                } else {
                    // ToastHelper.makeToast(applicationContext, message)
                    empty_view.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<HotJobs>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                empty_view.visibility = View.VISIBLE

            }
        })
    }

    fun loadAppliedJobs(){

        mygroups.visibility = View.VISIBLE
        mygroups_selected.visibility = View.VISIBLE
        mygroups1.visibility = View.VISIBLE
        mygroups_selected1.visibility = View.VISIBLE
        featured_selected.visibility = View.GONE
        featured_selected1.visibility = View.GONE
        all_selected.visibility = View.GONE
        all_selected1.visibility = View.GONE
        mygroups.setTypeface(null, Typeface.BOLD);
        featured.setTypeface(null, Typeface.NORMAL);
        mygroups1.setTypeface(null, Typeface.BOLD);
        featured1.setTypeface(null, Typeface.NORMAL);
        all.setTypeface(null, Typeface.NORMAL);
        all1.setTypeface(null, Typeface.NORMAL);


        val params = java.util.HashMap<String, String>()
        params["page_no"] = "1"

        listOfCompareJoineddata.clear()
        retrofitInterface1 = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface1!!.getAppliedJobs("application/json", EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        call.enqueue(object : Callback<AppliedJobDetails> {
            override fun onResponse(call: Call<AppliedJobDetails>, response: Response<AppliedJobDetails>) {

                Logger.d("URL", "Applied" + response+ EndPoints.CLIENT_ID)
                Logger.d("CODE", "Applied"+response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "Applied" + Gson().toJson(response))

                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))

                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                    val responseCode: Int = jsonObject1.optInt("response_code")
                    val message: String = jsonObject1.optString("message")
                    val jsonarray_info: JSONArray = jsonObject1.optJSONArray("body")
                    Log.d("TAGG", "Applied " + jsonarray_info.toString())

                    if (response.isSuccessful) {

                        for (l in 0 until response.body()?.body?.size!!) {
                            var json_objectdetail: JSONObject = jsonarray_info.optJSONObject(l)
                            val model: AppliedJobDetailsData = AppliedJobDetailsData();

                            model.id = json_objectdetail.optInt("id")
                            model.company_id = json_objectdetail.optInt("company_id")
                            model.is_shortlisted = json_objectdetail.getBoolean("is_shortlisted")
                            model.employer_id = 0 //json_objectdetail.optInt("employer_id")
                            model.employer_note = json_objectdetail.optString("employer_note")
                            model.resume_id = 0 //json_objectdetail.optInt("resume_id")
                            model.location = json_objectdetail.optString("location")
                            model.job_status = json_objectdetail.optString("job_status")
                            model.new_applicant = json_objectdetail.getBoolean("new_applicant")
                            model.applied_status = json_objectdetail.optString("applied_status")
                            model.note = json_objectdetail.optString("note")
                            model.job_id = json_objectdetail.optInt("job_id")
                            model.user_id = json_objectdetail.optInt("user_id")
                            model.modified_on = json_objectdetail.optString("modified_on")
                            model.created_on = json_objectdetail.optString("created_on")
                            model.title = json_objectdetail.optString("title")
                            model.job_boosted = false //json_objectdetail.getBoolean("job_boosted")
                            model.min_year = 0//json_objectdetail.optInt("min_year")
                            model.max_year = 0//json_objectdetail.optInt("max_year")
                            model.job_posting_type = json_objectdetail.optString("job_posting_type")
                            model.name = json_objectdetail.optString("name")
                            model.featured = false//json_objectdetail.getBoolean("featured")
                            model.logo = json_objectdetail.optString("logo")
                            Log.d("TAGG", model.job_id!!.toString() + " JOB ID")
                            listOfCompareJoineddata.add(model.job_id!!)
                        }

                        loadhotJobsData("1")

                    } else {
                        ToastHelper.makeToast(applicationContext, message)
                    }
                }
            }

            override fun onFailure(call: Call<AppliedJobDetails>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                loadhotJobsData("1")
            }
        })
    }

    fun loadhotJobsData(pageno:String){

        if (pageno.equals("1")) {
            listOfhotJobsdata.clear()
            hotJobsAdapter!!.notifyDataSetChanged()
        }
        else{}
        val params = HashMap<String, String>()

        params["page_no"] = pageno.toString()
        params["page_size"] = Constants.MAXIMUM_PAGINATION_COUNT

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface!!.gethotJobsData( EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        call.enqueue(object : Callback<HotJobs> {
            override fun onResponse(call: Call<HotJobs>, response: Response<HotJobs>) {

                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)

                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                Log.d("TAGG", "Outside body")
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {

                    Log.d("TAGG", "Inside body")
                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")

                    var jsonarray_info: JSONArray = jsonObject1.optJSONArray("body")

                    var jsonarray_pagination: JSONObject = jsonObject1.optJSONObject("pagination")

                    val has_next: String = jsonarray_pagination.getBoolean("has_next").toString()

                    val pagenoo: String = jsonarray_pagination.optString("page_no")
                    val prev_page: Int
//                    if (jsonarray_pagination.has("prev_page") && !jsonarray_pagination.optString("prev_page").equals(""))
//                        prev_page = jsonarray_pagination.optInt("prev_page")
//                    else
//                        prev_page = 0
                    if (Integer.parseInt(pagenoo)>1)
                        prev_page = Integer.parseInt(pagenoo)-1
                    else
                        prev_page = 0

                    if (response.isSuccessful) {

                        //  ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                        Log.d("TAGG", "Inside response success")
                        for (i in 0 until response.body()?.body?.size!!) {
                            Log.d("TAGG", "Inside response body")
                            var json_objectdetail: JSONObject = jsonarray_info.optJSONObject(i)

                            val model: JobsView = JobsView()

                            model.id = json_objectdetail.optInt("id")
                            model.title = json_objectdetail.optString("title")
                            model.company_name = json_objectdetail.optString("company_name")
                            model.company_logo = json_objectdetail.optString("company_logo")
                            model.location_name = json_objectdetail.optString("location_name")
                            model.max_year = json_objectdetail.optInt("max_year")
                            model.min_year = json_objectdetail.optInt("min_year")
                            model.status = json_objectdetail.optString("status")
                            model.modified_on = json_objectdetail.optString("modified_on")
                            model.application_count = 0//json_objectdetail.optInt("application_count")
                            model.company_id = json_objectdetail.optInt("company_id")
                            model.employer_name = json_objectdetail.optString("employer_name")
                            model.new_application_count = 0//json_objectdetail.optInt("new_application_count")
                            model.created_on = json_objectdetail.optString("created_on")
                            model.boosted = json_objectdetail.getBoolean("boosted")
                            model.employer_id = json_objectdetail.optInt("employer_id")
                            model.resume_required = json_objectdetail.getBoolean("resume_required")

                            val listOfJobTypes: ArrayList<String> = ArrayList()
                            val listjson: JSONArray = JSONArray()
                            var job_typesArray: JSONArray = if(json_objectdetail.isNull("job_types"))listjson else json_objectdetail.optJSONArray("job_types")

                            for (j in 0 until job_typesArray.length()) {
                                    listOfJobTypes.add(job_typesArray.get(j).toString())
                            }

                            model.job_types = listOfJobTypes

                            listOfhotJobsdata.add(
                                JobsView(
                                    model.id,
                                    model.modified_on!!,
                                    model.application_count!!,
                                    model.company_id!!,
                                    model.job_types!!,
                                    model.employer_name!!,
                                    model.new_application_count!!,
                                    model.title!!,
                                    model.max_year!!,
                                    model.location_name!!,
                                    model.company_logo!!,
                                    model.created_on!!,
                                    model.company_name!!,
                                    model.boosted!!,
                                    model.min_year!!,
                                    model.employer_id!!,
                                    model.status!!,
                                    model.resume_required!!
                                )
                            )

                        }

//                        if (pageno.equals("1")) {
//                            hotJobsAdapter!!.notifyDataSetChanged()
//                        }
//                        else {
                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                        mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                        hotJobsAdapter = JobsAdapter(listOfhotJobsdata, isLoggedIn, 1,0,listOfCompareJoineddata,0,"Jobs")
                        mgroupsRecyclerView!!.adapter = hotJobsAdapter
//                        }

                        if (type_group == 2){
                            //loadhotJobsData("1")
//                            loadFeaturedGroupData("1")
                        }
                        else if (type_group == 3){
                            //loadhotJobsData("1")
//                            loadAllGroupData("1")
                        }

                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid Request")
                    }
                    Log.d("TAGG", "Beforeb has next")
                    if (has_next.equals("true")) {
                        val next_page: String = jsonarray_pagination.optInt("next_page").toString()
                        loadnext.visibility = View.VISIBLE
                        next_page_no_featured = next_page
                        scroll = 1
                    } else {

//                        loadnext.visibility = View.GONE
//                        scroll = 0


                        loadOtherGroupData("1")
                    }
                    empty_view.visibility = View.GONE
                    loadprev.visibility = View.GONE
//                    if (pagenoo == "1")
//                        loadprev.visibility = View.GONE
//                    else {
//                        loadprev.visibility = View.VISIBLE
//                        prev_page_no_featured = prev_page.toString()
//                    }
                }
                else {
                    Toast.makeText(applicationContext, "No data exists", Toast.LENGTH_LONG).show()
                    empty_view.visibility = View.VISIBLE
                    loadnext.visibility = View.GONE
                    loadprev.visibility = View.GONE
                    scroll =0
                }

            }

            override fun onFailure(call: Call<HotJobs>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                //  Toast.makeText(applicationContext, "No Groups Exixts, Join a group today!", Toast.LENGTH_LONG).show()
                empty_view.visibility = View.VISIBLE
                loadnext.visibility = View.GONE
                loadprev.visibility = View.GONE
                scroll =0
            }
        })
    }

    fun loadOtherGroupData(pageno:String){

        mygroups_selected.visibility = View.GONE
        featured_selected.visibility = View.VISIBLE
        mygroups_selected1.visibility = View.GONE
        featured_selected1.visibility = View.VISIBLE
        all_selected.visibility = View.GONE
        all_selected1.visibility = View.GONE

        featured.setTypeface(null, Typeface.BOLD);
        mygroups.setTypeface(null, Typeface.NORMAL);
        featured1.setTypeface(null, Typeface.BOLD);
        mygroups1.setTypeface(null, Typeface.NORMAL);
        all.setTypeface(null, Typeface.NORMAL);
        all1.setTypeface(null, Typeface.NORMAL);

        type_group = 4
        val params = HashMap<String, String>()

        params["page_no"] = pageno.toString()
        params["page_size"] = Constants.MAXIMUM_PAGINATION_COUNT

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getotherJobsData( EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        call.enqueue(object : Callback<HotJobs> {
            override fun onResponse(call: Call<HotJobs>, response: Response<HotJobs>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)

                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                Logger.d("RESPONSE", "" + str_response)
                //var str_response = Gson().toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                    var jsonarray_info: JSONArray = jsonObject1.optJSONArray("body")
                    var jsonarray_pagination: JSONObject = jsonObject1.optJSONObject("pagination")

                    val has_next: String = jsonarray_pagination.getBoolean("has_next").toString()

                    val pagenoo: String = jsonarray_pagination.optString("page_no")
                    val prev_page: Int
//                    if (jsonarray_pagination.has("prev_page"))
//                        prev_page = jsonarray_pagination.optInt("prev_page")
//                    else
//                        prev_page = 0
                    if (Integer.parseInt(pagenoo)>1)
                        prev_page = Integer.parseInt(pagenoo)-1
                    else
                        prev_page = 0


                    if (response.isSuccessful) {

                        for (i in 0 until response.body()?.body?.size!!) {

                            var json_objectdetail: JSONObject = jsonarray_info.optJSONObject(i)

                            val model: JobsView = JobsView()

                            model.id = json_objectdetail.optInt("id")
                            model.title = json_objectdetail.optString("title")
                            model.company_name = json_objectdetail.optString("company_name")
                            model.company_logo = json_objectdetail.optString("company_logo")
                            model.location_name = json_objectdetail.optString("location_name")
                            model.max_year = json_objectdetail.optInt("max_year")
                            model.min_year = json_objectdetail.optInt("min_year")
                            model.status = json_objectdetail.optString("status")
                            model.modified_on = json_objectdetail.optString("modified_on")
                            model.application_count = 1//json_objectdetail.optInt("application_count")
                            model.company_id = 1//json_objectdetail.optInt("company_id")
                            model.employer_name = json_objectdetail.optString("employer_name")
                            model.new_application_count = 1//json_objectdetail.optInt("new_application_count")
                            model.created_on = json_objectdetail.optString("created_on")
                            model.boosted = json_objectdetail.getBoolean("boosted")
                            model.employer_id = json_objectdetail.optInt("employer_id")
                            model.resume_required = json_objectdetail.getBoolean("resume_required")

                            val listOfJobTypes: ArrayList<String> = ArrayList()
                            val listjson: JSONArray = JSONArray()
                            var job_typesArray: JSONArray = if(json_objectdetail.isNull("job_types"))listjson else json_objectdetail.optJSONArray("job_types")

                            for (j in 0 until job_typesArray.length()) {
                                    listOfJobTypes.add(job_typesArray.get(j).toString())
                            }

                            model.job_types = listOfJobTypes

                            listOfhotJobsdata.add(
                                JobsView(
                                    model.id,
                                    model.modified_on!!,
                                    model.application_count!!,
                                    model.company_id!!,
                                    model.job_types!!,
                                    model.employer_name!!,
                                    model.new_application_count!!,
                                    model.title!!,
                                    model.max_year!!,
                                    model.location_name!!,
                                    model.company_logo!!,
                                    model.created_on!!,
                                    model.company_name!!,
                                    model.boosted!!,
                                    model.min_year!!,
                                    model.employer_id!!,
                                    model.status!!,
                                    model.resume_required!!
                                )
                            )
                        }

                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                        mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                        hotJobsAdapter = JobsAdapter(listOfhotJobsdata, isLoggedIn, 2,0,listOfCompareJoineddata,0,"Jobs")
                        mgroupsRecyclerView!!.adapter = hotJobsAdapter

                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid Request")
                    }

                    if (has_next.equals("true")) {
                        val next_page: String = jsonarray_pagination.optInt("next_page").toString()
                        loadnext.visibility = View.VISIBLE
                        next_page_no_featured = next_page
                        scroll =1
                    } else {
                        loadnext.visibility = View.GONE
                        scroll =0
                    }

                    empty_view.visibility = View.GONE
                    loadprev.visibility = View.GONE
//                    if (pagenoo == "1")
//                        loadprev.visibility = View.GONE
//                    else {
//                        loadprev.visibility = View.VISIBLE
//                        prev_page_no_featured = prev_page.toString()
//                    }
                }
                else
                    Toast.makeText(applicationContext,"No data exists",Toast.LENGTH_LONG).show()
                empty_view.visibility = View.VISIBLE
            }

            override fun onFailure(call: Call<HotJobs>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                empty_view.visibility = View.VISIBLE

            }
        })
    }

    fun loadNotificationbubble(){


        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.GetNotificationBubble(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)

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
                    var jsonarray: JSONObject = jsonObject1.optJSONObject("body")
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

            override fun onFailure(call: Call<NotificationBubbleResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
            }
        })
    }

    fun loadFilteredData(pageno:String,keyword:String,location_name:String,job_types:String,min_year:String,max_year:String,
                         functional_area:String,industries:String,company_id:String,type:String){

        if (pageno.equals("1")) {
            listOfhotJobsdata.clear()
            hotJobsAdapter!!.notifyDataSetChanged()
        }
        else{}
        isfilter =1
        var string: String = ""
        var list: ArrayList<Chip> = ArrayList()
        var params = HashMap<String, String>()

        params["page_no"] = pageno.toString()
        //params["page_size"] = 4.toString()
        if (keyword.equals("")){}
        else{
            params["keyword"] = keyword.toString()
            string = string+ keyword.toString()+","
            list.add(Tag(keyword.toString()))
        }
        if (location_name.equals("")){}
        else {
            if(location_name.contains(",")) {
                params["location_name"] = location_name.substring(0, location_name.length - 1)
                string = string+ location_name.substring(0,location_name.length-1)+","
            }
            else {
                params["location_name"] = location_name.toString()
                string = string+ location_name.substring(0,location_name.length)+","

            }
            if(location_name.substring(0,location_name.length).contains(",")) {
                var output:List<String> = location_name.substring(0,location_name.length-1) . split (",");
                for(i in 0 until output.size)
                    list.add(Tag(output.get(i)))
            }
            else
                list.add(Tag(location_name))
        }
        if (job_types.equals("")){}
        else{
            if(job_types.contains(",")) {
                params["job_types"] = job_types.substring(0, job_types.length - 1)
                string = string+ job_types.substring(0,job_types.length-1)+","
            }
            else {
                params["job_types"] = job_types.toString()
                string = string+ job_types.substring(0,job_types.length)+","

            }
            if(job_types.substring(0,job_types.length).contains(",")) {
                var output:List<String> = job_types.substring(0,job_types.length-1) . split (",");
                for(i in 0 until output.size)
                    list.add(Tag(output.get(i)))
            }
            else
                list.add(Tag(job_types))
        }
        if (min_year.equals("")){}
        else{
            if(min_year.contains(","))
                params["min_year"] = min_year.substring(0,min_year.length-1)
            else if(Integer.parseInt(min_year)>0)
                    params["min_year"] = min_year.toString()
        }
        if (max_year.equals("")){}
        else{
            if(max_year.contains(","))
                params["max_year"] = max_year.substring(0,max_year.length-1)
            else if(Integer.parseInt(max_year)>0)
                    params["max_year"] = max_year.toString()
        }
        if (functional_area.equals("")){}
        else{
            if(functional_area.contains(",")) {
                params["functional_area"] = functional_area.substring(0, functional_area.length - 1)
                string = string+ functional_area.substring(0,functional_area.length-1)+","
            }
            else {
                params["functional_area"] = functional_area.toString()
                string = string+ functional_area.substring(0,functional_area.length)+","

            }

            if(functional_area.substring(0,functional_area.length).contains(",")) {
                var output:List<String> = functional_area.substring(0,functional_area.length-1) . split (",");
                for(i in 0 until output.size)
                    list.add(Tag(output.get(i)))
            }
            else
                list.add(Tag(functional_area))
        }
        if (industries.equals("")){}
        else{
            if(industries.contains(",")) {
                params["industries"] = industries.substring(0, industries.length - 1)
                string = string+ industries.substring(0,industries.length-1)+","
            }
            else {
                params["industries"] = industries.toString()
                string = string+ industries.substring(0,industries.length)+","

            }

            if(industries.substring(0,industries.length).contains(",")) {
                var output:List<String> = industries.substring(0,industries.length-1) . split (",");
                for(i in 0 until output.size)
                    list.add(Tag(output.get(i)))
            }
            else
                list.add(Tag(industries))
        }
        if (company_id.equals("")){}
        else{
            params["company_id"] = company_id.toString()
        }
        if (type_group==1)
            params["type"] = "boosted"
        else
            params["type"] = "basic"


        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface!!.getfilteredJobsData( EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        call.enqueue(object : Callback<HotJobs> {
            override fun onResponse(call: Call<HotJobs>, response: Response<HotJobs>) {
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)

                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                Log.d("TAGG", "Outside body")
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {

                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")

                    var jsonarray_info: JSONArray = jsonObject1.optJSONArray("body")

                    var jsonarray_pagination: JSONObject = jsonObject1.optJSONObject("pagination")

                    val has_next: String = jsonarray_pagination.getBoolean("has_next").toString()

                    val pagenoo: String = jsonarray_pagination.optString("page_no")

                    val total_items = jsonarray_pagination.optString("total_items")
                    val prev_page: Int
//                    if (jsonarray_pagination.has("prev_page") && !jsonarray_pagination.optString("prev_page").equals(""))
//                        prev_page = jsonarray_pagination.optInt("prev_page")
//                    else
//                        prev_page = 0
                    if (Integer.parseInt(pagenoo)>1)
                        prev_page = Integer.parseInt(pagenoo)-1
                    else
                        prev_page = 0

                    if (response.isSuccessful) {

                        hotJobsAdapter!!.notifyDataSetChanged()
                        //  ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                        Log.d("TAGG", "Inside response success")
                        for (i in 0 until response.body()?.body?.size!!) {
                            Log.d("TAGG", "Inside response body")
                            var json_objectdetail: JSONObject = jsonarray_info.optJSONObject(i)

                            val model: JobsView = JobsView()

                            model.id = json_objectdetail.optInt("id")
                            model.title = json_objectdetail.optString("title")
                            model.company_name = json_objectdetail.optString("company_name")
                            model.company_logo = json_objectdetail.optString("company_logo")
                            model.location_name = json_objectdetail.optString("location_name")
                            model.max_year = json_objectdetail.optInt("max_year")
                            model.min_year = json_objectdetail.optInt("min_year")
                            model.status = json_objectdetail.optString("status")
                            model.modified_on = json_objectdetail.optString("modified_on")
                            model.application_count = 0//json_objectdetail.optInt("application_count")
                            model.company_id = json_objectdetail.optInt("company_id")
                            model.employer_name = json_objectdetail.optString("employer_name")
                            model.new_application_count = 0//json_objectdetail.optInt("new_application_count")
                            model.created_on = json_objectdetail.optString("created_on")
                            model.boosted = json_objectdetail.getBoolean("boosted")
                            model.employer_id = 0//json_objectdetail.optInt("employer_id")
                            model.resume_required = json_objectdetail.getBoolean("resume_required")

                            val listOfJobTypes: ArrayList<String> = ArrayList()
                            val listjson: JSONArray = JSONArray()
                            var job_typesArray: JSONArray = if(json_objectdetail.isNull("job_types"))listjson else json_objectdetail.optJSONArray("job_types")

                            for (j in 0 until job_typesArray.length()) {
                                    listOfJobTypes.add(job_typesArray.get(j).toString())
                            }

                            model.job_types = listOfJobTypes

                            listOfhotJobsdata.add(
                                JobsView(
                                    model.id,
                                    model.modified_on!!,
                                    model.application_count!!,
                                    model.company_id!!,
                                    model.job_types!!,
                                    model.employer_name!!,
                                    model.new_application_count!!,
                                    model.title!!,
                                    model.max_year!!,
                                    model.location_name!!,
                                    model.company_logo!!,
                                    model.created_on!!,
                                    model.company_name!!,
                                    model.boosted!!,
                                    model.min_year!!,
                                    model.employer_id!!,
                                    model.status!!,
                                    model.resume_required!!
                                )
                            )

                        }

                        if (pageno.equals("1")) {
                            hotJobsAdapter!!.notifyDataSetChanged()
                        }
                        else {
                            if(listOfhotJobsdata.size>0){
                                mgroupsRecyclerView = findViewById(R.id.recycler_view_groups)
                                var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                                mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                                hotJobsAdapter = JobsAdapter(listOfhotJobsdata, isLoggedIn, type_group,1,listOfCompareJoineddata,0,"Jobs")
                                mgroupsRecyclerView!!.adapter = hotJobsAdapter
                            }
                        }

                        if (type_group == 2){
                            //loadhotJobsData("1")
//                            loadFeaturedGroupData("1")
                        }
                        else if (type_group == 3){
                            //loadhotJobsData("1")
//                            loadAllGroupData("1")
                        }

                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid Request")
                    }
                    Log.d("TAGG", "Beforeb has next")
                    if (has_next.equals("true")) {
                        val next_page: String = jsonarray_pagination.optInt("next_page").toString()
                        loadnext.visibility = View.VISIBLE
                        next_page_no_featured = next_page
                        scroll = 1
                    } else {
                        loadnext.visibility = View.GONE
                        scroll = 0
                    }

                    loadprev.visibility = View.GONE

//                    activity_jfh_banner_jobs.visibility = View.GONE
                    filer_default_layout.visibility = View.VISIBLE
                    success_default_layout.visibility = View.GONE
                    filter_bottom.visibility = View.GONE
                    empty_view.visibility = View.GONE
                    search_default.setText("")
                    def_text_jobs.visibility = View.GONE
                    tag_group.setChipList(list)
                    joblisting_tag.text = total_items.toString()+" matching jobs found"
//                    layouttop.visibility = View.GONE
//                    layoutadd.visibility = View.GONE
//                    activity_jfh_banner_jobs.visibility = View.VISIBLE
//                    filer_default_layout.visibility = View.GONE
//                    success_default_layout.visibility = View.GONE
//                    layouttop.visibility = View.VISIBLE
//                    layoutadd.visibility = View.VISIBLE
//                   // layout1.visibility = View.VISIBLE
                    mainScroll_jobs.fullScroll(View.FOCUS_UP);
                    mainScroll_jobs.smoothScrollTo(0,0);
                    recycler_view_groups.setFocusable(true);

                }
                else {
                    Toast.makeText(applicationContext, "No data exists", Toast.LENGTH_LONG).show()
                    loadnext.visibility = View.GONE
                    loadprev.visibility = View.GONE
                    scroll =0
                    empty_view.visibility = View.VISIBLE
//                    activity_jfh_banner_jobs.visibility = View.GONE
                    filer_default_layout.visibility = View.VISIBLE
                    success_default_layout.visibility = View.GONE
                    filter_bottom.visibility = View.GONE
                    search_default.setText("")
                    def_text_jobs.visibility = View.VISIBLE
                    tag_group.setChipList(list)
                    if(string.length>0)
                        joblisting_tag.text = "'"+string.substring(0,string.length-1) +"'" + " Job listing not found"
                    else
                        joblisting_tag.text = " Job listing not found"
//                    layouttop.visibility = View.GONE
//                    layoutadd.visibility = View.GONE
                    // layout1.visibility = View.GONE
                    mainScroll_jobs.fullScroll(View.FOCUS_UP);
                    mainScroll_jobs.smoothScrollTo(0,0);
                    recycler_view_groups.setFocusable(false);
                }
            }
            override fun onFailure(call: Call<HotJobs>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                //  Toast.makeText(applicationContext, "No Jobs Exists!", Toast.LENGTH_LONG).show()
                loadnext.visibility = View.GONE
                loadprev.visibility = View.GONE
                scroll =0
                empty_view.visibility = View.VISIBLE
                filer_default_layout.visibility = View.VISIBLE
                success_default_layout.visibility = View.GONE
                filter_bottom.visibility = View.GONE
                search_default.setText("")
                tag_group.setChipList(list)
                def_text_jobs.visibility = View.VISIBLE
                if(string.length>0)
                    joblisting_tag.text = "'"+string.substring(0,string.length-1) +"'" + " Job listing not found"
                else
                    joblisting_tag.text = " Job listing not found"
//                layouttop.visibility = View.GONE
//                layoutadd.visibility = View.GONE
                // layout1.visibility = View.GONE
                mainScroll_jobs.fullScroll(View.FOCUS_UP);
                mainScroll_jobs.smoothScrollTo(0,0);
                recycler_view_groups.setFocusable(false);
//                activity_jfh_banner_jobs.visibility = View.GONE
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
            R.id.action_sett->{
                if(m.findItem(R.id.action_logout).isVisible==true)
                    m.findItem(R.id.action_logout).setVisible(false)
                else
                    m.findItem(R.id.action_logout).setVisible(true)
                //m.findItem(R.id.action_logout).setVisible(true)
            }
            R.id.action_logout->{
                val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
                drawerLayout.closeDrawer(GravityCompat.START)
                // Initialize a new instance of
                val builder = AlertDialog.Builder(this)
//                // Set the alert dialog title
//                builder.setTitle("Do you really want to logout of the app ?")
                // Display a message on alert dialog
                builder.setMessage("Do you really want to logout of the app ?")
                // Set a positive button and its click listener on alert dialog
                builder.setPositiveButton("YES"){dialog, which ->
                    val sharedPref: SharedPreferences = getSharedPreferences("mysettings",
                        Context.MODE_PRIVATE)
                    sharedPref.edit().clear().commit();
                    val intent = Intent(applicationContext, SplashActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                // Display a negative button on alert dialog
                builder.setNegativeButton("No"){dialog,which ->
                    dialog.cancel()
                }
                // Finally, make the alert dialog using builder
                val dialog: AlertDialog = builder.create()
                // Display the alert dialog on app interface
                dialog.show()
            }
            R.id.action_signup->{

            }
            R.id.action_login -> {

            }

            R.id.action_share -> {
                HelperMethods.showAppShareOptions(this@ZActivityJobs)
            }
        }

        return true
    }

    private fun closeDrawer() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    fun search(searchview: SearchView) {


    }


    public fun openBottomSheetFilter() {

//        val mLayoutManager1 = GridLayoutManager(applicationContext, 3)
//        mRecyclerView_city!!.layoutManager = mLayoutManager1
//        mAdapterCities = ZGroupsPhotosAdapter(listOfCities)
//        mRecyclerView_city!!.adapter = mAdapterCities
        val data= ArrayList<String>()
        data!!.add("full_time")
        data!!.add("part_time")
        data!!.add("work_from_home")
        data!!.add("returnee_program")
        data!!.add("freelance")
        data!!.add("volunteer")

        val dialog = Dialog(this, R.style.AppTheme)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog .setCancelable(true)
        dialog .setContentView(R.layout.bottom_sheet_filter_jobs)
        var searchView:SearchView = dialog.findViewById(R.id.search_filter) as SearchView
        search(searchView);

        var mRecyclerView_category = dialog.findViewById(R.id.recycler_view_filtercategory) as RecyclerView
        var mRecyclerView_city = dialog.findViewById(R.id.recycler_view_filtercity) as RecyclerView
        var mRecyclerView_jobtype = dialog.findViewById(R.id.recycler_view_filterjobtype) as RecyclerView
        var mlayout_experience = dialog.findViewById(R.id.layout_filter_experience) as LinearLayout
        var mRecyclerView_functionalArea = dialog.findViewById(R.id.recycler_view_filterfunctionalArea) as RecyclerView
        var mRecyclerView_industry = dialog.findViewById(R.id.recycler_view_filterIndustry) as RecyclerView

        val mLayoutManager = GridLayoutManager(applicationContext, 1)
        mRecyclerView_category!!.layoutManager = mLayoutManager
        mAdapterCategories = ZJobsCategoryAdapter(listOfCategories)
        mRecyclerView_category!!.adapter = mAdapterCategories

        val mLayoutManager1 = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL,false)
        mRecyclerView_city!!.layoutManager = mLayoutManager1
        mAdapterCities = ZJobsCitiesAdapter(listOfCities, location_nameArray)
        mRecyclerView_city!!.adapter = mAdapterCities

        val mLayoutManager2 = GridLayoutManager(applicationContext, 1)
        mRecyclerView_jobtype!!.layoutManager = mLayoutManager2
        mAdapterJobtype = ZJobTypeAdapter(listOfJobType,job_typesArray,data)
        mRecyclerView_jobtype!!.adapter = mAdapterJobtype

        val numbers = ArrayList<String>()
        for(i in 0..30)
            numbers.add(i.toString())

        var spinner = dialog.findViewById(R.id.spinner) as Spinner
        var spinner1 = dialog.findViewById(R.id.spinner1) as Spinner
        val dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, numbers)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter)
        spinner1.setAdapter(dataAdapter)



        var min:String = "0"
        var max:String = "0"
        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                min = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        })

        spinner1.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                max = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        })

        val mLayoutManager3 = GridLayoutManager(applicationContext, 1)
        mRecyclerView_functionalArea!!.layoutManager = mLayoutManager3
        mAdapterFArea = ZJobsFAreaAdapter(listOfJobFArea,1,functional_areaArray)
        mRecyclerView_functionalArea!!.adapter = mAdapterFArea

        val mLayoutManager4 = GridLayoutManager(applicationContext, 1)
        mRecyclerView_industry!!.layoutManager = mLayoutManager4
        mAdapterIndustry = ZJobsFAreaAdapter(listOfJobIndustry,2,industriesArray)
        mRecyclerView_industry!!.adapter = mAdapterIndustry


        val category = dialog .findViewById(R.id.category) as TextView
        val city = dialog .findViewById(R.id.city) as TextView
        val jobtype = dialog .findViewById(R.id.jobtype) as TextView
        val experience = dialog .findViewById(R.id.experience) as TextView
        val functionalarea = dialog .findViewById(R.id.functionalarea) as TextView
        val industry = dialog .findViewById(R.id.industry) as TextView
        val filterClose = dialog.findViewById(R.id.close_filter) as ImageView
        filterClose.setOnClickListener {
            job_typesArray.clear()
            location_nameArray.clear()
            functional_areaArray.clear()
            industriesArray.clear()
            dialog.cancel();
        }
        category.setTextColor(Color.BLACK)
        city.setTextColor(Color.GRAY)
        category.setOnClickListener {
            mRecyclerView_category.visibility = View.VISIBLE
            mRecyclerView_city.visibility = View.GONE
            mRecyclerView_jobtype.visibility = View.GONE
            mlayout_experience.visibility  =View.GONE
            mRecyclerView_functionalArea.visibility = View.GONE
            mRecyclerView_industry.visibility = View.GONE
            category.setTextColor(Color.BLACK)
            city.setTextColor(Color.GRAY)
            jobtype.setTextColor(Color.GRAY)
            experience.setTextColor(Color.GRAY)
            functionalarea.setTextColor(Color.GRAY)
            industry.setTextColor(Color.GRAY)
        }

        city.setOnClickListener {
            mRecyclerView_category.visibility = View.GONE
            mRecyclerView_city.visibility = View.VISIBLE
            mRecyclerView_jobtype.visibility = View.GONE
            mlayout_experience.visibility  =View.GONE
            mRecyclerView_functionalArea.visibility = View.GONE
            mRecyclerView_industry.visibility = View.GONE
            category.setTextColor(Color.GRAY)
            city.setTextColor(Color.BLACK)
            jobtype.setTextColor(Color.GRAY)
            experience.setTextColor(Color.GRAY)
            functionalarea.setTextColor(Color.GRAY)
            industry.setTextColor(Color.GRAY)
        }

        jobtype.setOnClickListener {
            mRecyclerView_category.visibility = View.GONE
            mRecyclerView_city.visibility = View.GONE
            mRecyclerView_jobtype.visibility = View.VISIBLE
            mlayout_experience.visibility  =View.GONE
            mRecyclerView_functionalArea.visibility = View.GONE
            mRecyclerView_industry.visibility = View.GONE
            category.setTextColor(Color.GRAY)
            city.setTextColor(Color.GRAY)
            jobtype.setTextColor(Color.BLACK)
            experience.setTextColor(Color.GRAY)
            functionalarea.setTextColor(Color.GRAY)
            industry.setTextColor(Color.GRAY)
        }

        experience.setOnClickListener {
            mRecyclerView_category.visibility = View.GONE
            mRecyclerView_city.visibility = View.GONE
            mRecyclerView_jobtype.visibility = View.GONE
            mlayout_experience.visibility  =View.VISIBLE
            mRecyclerView_functionalArea.visibility = View.GONE
            mRecyclerView_industry.visibility = View.GONE
            category.setTextColor(Color.GRAY)
            city.setTextColor(Color.GRAY)
            jobtype.setTextColor(Color.GRAY)
            experience.setTextColor(Color.BLACK)
            functionalarea.setTextColor(Color.GRAY)
            industry.setTextColor(Color.GRAY)
        }

        functionalarea.setOnClickListener {
            mRecyclerView_category.visibility = View.GONE
            mRecyclerView_city.visibility = View.GONE
            mRecyclerView_jobtype.visibility = View.GONE
            mlayout_experience.visibility  =View.GONE
            mRecyclerView_functionalArea.visibility = View.VISIBLE
            mRecyclerView_industry.visibility = View.GONE
            category.setTextColor(Color.GRAY)
            city.setTextColor(Color.GRAY)
            jobtype.setTextColor(Color.GRAY)
            experience.setTextColor(Color.GRAY)
            functionalarea.setTextColor(Color.BLACK)
            industry.setTextColor(Color.GRAY)
        }

        industry.setOnClickListener {
            mRecyclerView_category.visibility = View.GONE
            mRecyclerView_city.visibility = View.GONE
            mRecyclerView_jobtype.visibility = View.GONE
            mlayout_experience.visibility  =View.GONE
            mRecyclerView_functionalArea.visibility = View.GONE
            mRecyclerView_industry.visibility = View.VISIBLE
            category.setTextColor(Color.GRAY)
            city.setTextColor(Color.GRAY)
            jobtype.setTextColor(Color.GRAY)
            experience.setTextColor(Color.GRAY)
            functionalarea.setTextColor(Color.GRAY)
            industry.setTextColor(Color.BLACK)
        }

        val keyword_edittext = dialog .findViewById(R.id.keyword_edittext) as EditText
        keyword_edittext.setText(keyword_data)
        var filter_apply = dialog.findViewById(R.id.filter_apply) as TextView
        var filter_reset = dialog.findViewById(R.id.filter_reset) as TextView
        val reset = dialog.findViewById(R.id.reset) as TextView
        reset.setOnClickListener {
            filter_reset.callOnClick()
        }
        Log.d("TAGG", "Min MAx"+min_year+max_year)

        spinner.setSelection(Integer.parseInt(min_year))
        spinner1.setSelection(Integer.parseInt(max_year))
//        var search_edittext= dialog.findViewById(R.id.search_edittext) as SearchView
//        search_edittext.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//
//            override fun onQueryTextChange(newText: String): Boolean {
//                //adapter.filter(newText)
//                return false
//            }
//
//            override fun onQueryTextSubmit(query: String): Boolean {
//                adapter.filter(query)
//                return false
//            }
//
//        })

        var searchview = dialog.findViewById(R.id.search_filter) as SearchView
        searchview.setOnQueryTextListener( object :SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query:String):Boolean {
                return false;
            }

            override fun onQueryTextChange(newText:String): Boolean {

//                var vText:String = ""
//                vText=newText.toLowerCase()
//                var newList: ArrayList<CategoryView> = ArrayList()
//                for ( i in 0 until newList.size){
//                    val userInfo: CategoryView = newList.get(i)
//                    val name:String = userInfo!!.name!!.toString()
//                    if (name.contains(newText)){
//                        newList.add(userInfo);
//                    }
//                }
                return true;
            }
        })

        val close = dialog.findViewById(R.id.filter_sidecancel) as TextView
        close.setOnClickListener {

            dialog.cancel();
        }

        filter_apply.setOnClickListener {

            min_year = min
            max_year = max

            isfilter =1
            if (type_group==1)
                type = "boosted"
            else
                type = "basic"
            filter.setText("FILTER *")
            keyword_data = keyword_edittext.text.toString()
            if(Integer.parseInt(min)>Integer.parseInt(max)){
                Toast.makeText(applicationContext,"Min experience should be less then Max experience",Toast.LENGTH_LONG).show()
            }else{
                loadFilteredData("1",keyword_data,location_name, job_types,min_year,max_year,
                    functional_area,industries,company_id,type)
                dialog.cancel()
            }
        }

        filter_reset.setOnClickListener {

            keyword_edittext.text.clear()
            keyword_data=""
            location_name=""
            job_types=""
            min_year="0"
            max_year="0"
            functional_area=""
            industries=""
            company_id=""
            type=""
            isfilter = 0
            spinner.setSelection(0)
            spinner1.setSelection(0)
            filter.setText("FILTER")
            val mLayoutManager = GridLayoutManager(applicationContext, 1)
            mRecyclerView_category!!.layoutManager = mLayoutManager
            mAdapterCategories = ZJobsCategoryAdapter(listOfCategories)
            mRecyclerView_category!!.adapter = mAdapterCategories

            location_nameArray.clear()
            val mLayoutManager1 = GridLayoutManager(applicationContext, 1)
            mRecyclerView_city!!.layoutManager = mLayoutManager1
            mAdapterCities = ZJobsCitiesAdapter(listOfCities,location_nameArray)
            mRecyclerView_city!!.adapter = mAdapterCities

            job_typesArray.clear()
            val mLayoutManager2 = GridLayoutManager(applicationContext, 1)
            mRecyclerView_jobtype!!.layoutManager = mLayoutManager2
            mAdapterJobtype = ZJobTypeAdapter(listOfJobType,job_typesArray,data)
            mRecyclerView_jobtype!!.adapter = mAdapterJobtype

            functional_areaArray.clear()
            val mLayoutManager3 = GridLayoutManager(applicationContext, 1)
            mRecyclerView_functionalArea!!.layoutManager = mLayoutManager3
            mAdapterFArea = ZJobsFAreaAdapter(listOfJobFArea,1,functional_areaArray)
            mRecyclerView_functionalArea!!.adapter = mAdapterFArea

            industriesArray.clear()
            val mLayoutManager4 = GridLayoutManager(applicationContext, 1)
            mRecyclerView_industry!!.layoutManager = mLayoutManager4
            mAdapterIndustry = ZJobsFAreaAdapter(listOfJobIndustry,2,industriesArray)
            mRecyclerView_industry!!.adapter = mAdapterIndustry
            //dialog.cancel()
            loadAppliedJobs()
            type_group = 1
            mygroups_selected.visibility = View.VISIBLE
            mygroups_selected1.visibility = View.VISIBLE
            featured_selected.visibility = View.GONE
            featured_selected1.visibility = View.GONE
            all_selected.visibility = View.GONE
            all_selected1.visibility = View.GONE
            featured.setTypeface(null, Typeface.NORMAL);
            featured1.setTypeface(null, Typeface.NORMAL);
            mygroups.setTypeface(null, Typeface.BOLD);
            mygroups1.setTypeface(null, Typeface.BOLD);
            all.setTypeface(null, Typeface.NORMAL);
            all1.setTypeface(null, Typeface.NORMAL);
        }

//        fun boolean onQueryTextSubmit(query:String) {
//            return false
//        }
//
//        fun boolean onQueryTextChange(newText:String ) {
//            String text = newText;
//            adapter.filter(text);
//            return false
//        }


        var window : Window = dialog.getWindow()!!;
        var wlp: WindowManager.LayoutParams  = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN;
        window.setAttributes(wlp);

        dialog .show()


    }

    public fun getSelectedCategory(id:Int, name:String) {
        Log.d("TAGG", name)
        if (name.equals(""))
            category = ""
        else
            category = category+name+","
    }

    public fun getSelectedCities(id:Int, name:String) {
        Log.d("TAGG", name)
        if (id==0) {
            //location_name = ""
            location_nameArray.remove(name)
            location_name = location_nameArray.toString().substring(1,location_nameArray.toString().length-1)
        }
        else {
            //location_name = location_name + name + ","
            location_nameArray.add(name)
            location_name = location_nameArray.toString().substring(1,location_nameArray.toString().length-1)
        }
    }

    public fun getSelectedJobType(id:Int,name:String) {

        var st:String =""
        if(name.equals("Part Time"))
            st = "part_time"
        else if(name.equals("Full Time"))
            st = "full_time"
        else if(name.equals("Work From Home"))
            st = "work_from_home"
        else if(name.equals("Freelance/Projects"))
            st = "freelance"
        Log.d("TAGG", name)
        if (id==0) {
            //job_types = ""
            job_typesArray.remove(name)
            job_types =job_typesArray.toString().substring(1,job_typesArray.toString().length-1)
        }
        else {
            //job_types = job_types + name + ","
            job_typesArray.add(name)
            job_types =job_typesArray.toString().substring(1,job_typesArray.toString().length-1)
        }
    }

    public fun getSelectedFArea(id:Int,name:String) {
        Log.d("TAGG", name)
        if (id==0) {
            //functional_area = ""
            functional_areaArray.remove(name)
            functional_area = functional_areaArray.toString().substring(1,functional_areaArray.toString().length-1)
        }
        else {
            // functional_area = functional_area + name + ","
            functional_areaArray.add(name)
            functional_area = functional_areaArray.toString().substring(1,functional_areaArray.toString().length-1)
        }
    }

    public fun getSelectedIndustries(id:Int,name:String) {
        Log.d("TAGG", name)
        if (id==0) {
            //industries = ""
            industriesArray.remove(name)
            industries = industriesArray.toString().substring(1,industriesArray.toString().length-1)
        }
        else {
            //industries = industries + name + ","
            industriesArray.add(name)
            industries = industriesArray.toString().substring(1,industriesArray.toString().length-1)
        }
    }

    fun loadCategoryData(){

        listOfCategories.clear()
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getCategories(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)
        call.enqueue(object : Callback<GetCategoryResponse> {

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(call: Call<GetCategoryResponse>, response: Response<GetCategoryResponse>) {

                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                    val responseCode: Int = jsonObject1.optInt("response_code")
                    val message: String = jsonObject1.optString("message")
                    var jsonarray: JSONArray = jsonObject1.optJSONArray("body")

                    if (response.isSuccessful) {
                        for (i in 0 until response.body()?.body?.size!!) {
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
                }
                else
                    Toast.makeText(applicationContext,"No data exists",Toast.LENGTH_LONG).show()
            }
            override fun onFailure(call: Call<GetCategoryResponse>, t: Throwable) {
                Logger.d("TAG", "FAILED : $t")
            }
        })

    }

    fun loadCityData(){

        listOfCities.clear()
        listOfCities = listOfCitiesnew
        addJobType()
    }

    fun addJobType(){

        listOfJobType.clear()
        listOfJobType.add(JobTypeView("Full Time","@drawable/ic_clock"))
        listOfJobType.add(JobTypeView("Part Time","@drawable/ic_clock"))
        listOfJobType.add(JobTypeView("Work From Home","@drawable/ic_house"))
        listOfJobType.add(JobTypeView("Returnee Program","@drawable/ic_clock"))
        listOfJobType.add(JobTypeView("Freelance/Projects","@drawable/ic_volunteer"))
        listOfJobType.add(JobTypeView("Volunteer","@drawable/ic_freelance"))
        addJobFunctionalArea()
    }

    fun addJobFunctionalArea(){
        listOfJobFArea.clear()
        listOfJobFArea = listOfJobFAreanew
//        listOfJobFArea.add(FunctionalAreaView("Airline/Reservation/Ticketing/Travel"))
//        listOfJobFArea.add(FunctionalAreaView("Research/Analytics/Business Intelligence/Big data"))
//        listOfJobFArea.add(FunctionalAreaView("Anchoring/TV/Films/Production"))
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

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, functionalareaList)
        functionalarea.setAdapter(adapter)
        functionalarea.setThreshold(1)
        functionalarea.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        addJobIndustry()

    }

    fun addJobIndustry(){

        listOfJobIndustry.clear()
        listOfJobIndustry = listOfJobIndustrynew
//        listOfJobIndustry.add(FunctionalAreaView("Advertising/PR/MR/Events"))
//        listOfJobIndustry.add(FunctionalAreaView("Architecture/Interior Design"))
//        listOfJobIndustry.add(FunctionalAreaView("Auto/Auto Ancillary"))
//        listOfJobIndustry.add(FunctionalAreaView("Banking/Financial Services/Insurance"))

        try {
            Collections.sort(listOfJobIndustry, object : Comparator<FunctionalAreaView> {
                override fun compare(lhs: FunctionalAreaView, rhs: FunctionalAreaView): Int {
                    return lhs.name!!.compareTo(rhs.name!!)
                }
            })
        } catch (e : Exception){

        }

        val industryList: ArrayList<String> = ArrayList()

        for (model in listOfJobIndustry) {
            industryList.add(model.name!!.toString())
        }

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, industryList)
        industry.setAdapter(adapter)
        industry.setThreshold(1)
        industry.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())


        val cityList: ArrayList<String> = ArrayList()

        for (model in listOfCities) {
            cityList.add(model.name!!.toString())
        }

        val adapter1 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cityList)
        city.setAdapter(adapter1)
        city.setThreshold(1)
        city.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        //openBottomSheetFilter()

        val numbers = ArrayList<String>()
        for(i in 0..45)
            numbers.add(i.toString())
        val adapter2 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, numbers)
        experience.setAdapter(adapter2)
        experience.setThreshold(1)
        experience.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

    }



    fun getAllCategories(){

        val params = HashMap<String, String>()

        params["page_no"] = 1.toString()
        params["size"] = 5.toString()

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getMainCategories(EndPoints.CLIENT_ID, params)

        call.enqueue(object : Callback<GroupCategoriesResponse> {
            override fun onResponse(call: Call<GroupCategoriesResponse>, response: Response<GroupCategoriesResponse>) {


                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE Categories", "" + Gson().toJson(response))

                if (response.isSuccessful) {
//                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())

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


                        for (i in 0 until 6) { //response.body()!!.body!!.size

                            val json_objectdetail: JSONObject = jsonarray_info.optJSONObject(i)

                            val model: CategoriesView = CategoriesView();

                            model.id = json_objectdetail.optInt("id")
                            model.name = json_objectdetail.optString("name")
                            model.image_url = json_objectdetail.optString("image_url")
                            model.status = json_objectdetail.optString("status")
                            model.created_on = json_objectdetail.optString("created_on")
                            model.modified_on = json_objectdetail.optString("modified_on")

                            listOfMainCategories.add(
                                CategoriesMainView(
                                    model.id, model.name!!, model.image_url!!,
                                    model.status!!, model.created_on!!, model.modified_on!!
                                )
                            )

                        }

                        mRecyclerView = findViewById(R.id.my_recycler_view)
                        val mLayoutManager = GridLayoutManager(
                            applicationContext,2, GridLayoutManager.HORIZONTAL,
                            false
                        )
                        mRecyclerView!!.isHorizontalScrollBarEnabled= false
                        mRecyclerView!!.layoutManager = mLayoutManager
                        mAdapter = JobsCategoryAdapter(listOfMainCategories)
                        mRecyclerView!!.adapter = mAdapter

                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid Request")
                    }
                } else
                    Toast.makeText(applicationContext,"No data exists",Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<GroupCategoriesResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })

    }


    fun applyJob(id:Int, btnJoinGroup:Button, title:String, btnJoined: Button, isRequired:Boolean, isboosted:Boolean){

        Logger.d("CODE",id.toString() + "")

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.CheckPreferences("application/json",EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<CheckPreferenceResponse> {
            override fun onResponse(call: Call<CheckPreferenceResponse>, response: Response<CheckPreferenceResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful && response.body()!!.responseCode == 11405) {
                    //  Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                    openBottomSheetPreferences()

                } else {
                    // ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
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

                        val json_objectdetail: JSONObject = jsonarray_info.optJSONObject(0)
                        if(json_objectdetail.isNull("preferred_job_type") ||
                            json_objectdetail.isNull("preferred_industry") ||
                            json_objectdetail.isNull("preferred_functional_area")||json_objectdetail.isNull("preferred_city")
                            || json_objectdetail.isNull("skills") || json_objectdetail.optString("skills").equals("")){
                            openBottomSheetPreferencesDashboard(json_objectdetail.optInt("user_id"), title,id,isRequired,isboosted)
                        }
                        else {
                            var a: Int = 0
                            for (i in 0 until response.body()?.body?.size!!) {

                                val json_objectdetail: JSONObject = jsonarray_info.optJSONObject(0)
                                a = json_objectdetail.optInt("user_id")
                                Log.d("TAGG", "PREf ID:" + json_objectdetail.optInt("user_id").toString())
                            }
                            checkDefault(a, title, id, isRequired, isboosted)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<CheckPreferenceResponse>, t: Throwable) {
                Logger.d("TAGG", "Apply Job FAILED : $t")
                openBottomSheetPreferences()
            }
        })
    }

    fun checkDefault(id:Int, title: String, jobId: Int, isRequired: Boolean, isboosted: Boolean){


        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.CheckDefault(id, EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<CheckDefaultResponse> {
            override fun onResponse(call: Call<CheckDefaultResponse>, response: Response<CheckDefaultResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.e("RESPONSE leave group", "" + Gson().toJson(response))

                if (response.isSuccessful && response.body()!!.responseCode == 10996) {

                    // ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
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
                        for (i in 0 until response.body()?.body?.size!!) {

                            val json_objectdetail: JSONObject = jsonarray_info.optJSONObject(i)

                            val model: ResumeView = ResumeView();
                            model.is_default = ""//json_objectdetail.optString("is_default")
                            model.path  =if(json_objectdetail.isNull("path"))"" else json_objectdetail.optString("path")
                            model.id = json_objectdetail.optInt("id")
                            model.title = if(json_objectdetail.isNull("title"))"" else json_objectdetail.optString("title")
                            model.is_parsed = false//json_objectdetail.getBoolean("is_parsed")
                            model.created_on = json_objectdetail.optString("created_on")
                            model.modified_on = json_objectdetail.optString("modified_on")
                            model.deleted  =json_objectdetail.getBoolean("deleted")
                            model.user_id = json_objectdetail.optInt("user_id")

                            listOfResumes.add(
                                ResumeView(model.is_default!!, model.path!!,
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
                    openBottomSheetUploadDoc(jobId,title, listOfResumes, isboosted)
                else{
                    //saveJob(jobId, note.text.toString(), listOfResumes[0].id, "Applied")
                    if(listOfResumes.size>0)
                        saveJob(jobId, "", listOfResumes[0].id, "Applied")
                    else
                        saveJob(jobId, "", 0, "Applied")
                }
            }

            override fun onFailure(call: Call<CheckDefaultResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                if (isRequired)
                    openBottomSheetUploadDoc(jobId,title, listOfResumes,isboosted)
                else{
                    if(listOfResumes.size>0)
                        saveJob(jobId, "", listOfResumes[0].id, "Applied")
                    else
                        saveJob(jobId, "", 0, "Applied")
                }
            }
        })
    }

    fun reportGroup(id: Int, type: String, problem_type:String, reportdetail: String, dialog:Dialog){

        //ToastHelper.makeToast(applicationContext, "Report Group")
        val params = HashMap<String, String>()

        params["entity_id"] = id.toString()
        params["entity_type"] = type
        params["problem_type"] = problem_type
        params["reason"] = reportdetail

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.ReportGroup(EndPoints.CLIENT_ID, "Bearer "+
                EndPoints.ACCESS_TOKEN,params)

        call.enqueue(object : Callback<ReportResponse> {
            override fun onResponse(call: Call<ReportResponse>, response: Response<ReportResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.e("RESPONSE report group", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    //   ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())

                    Logger.e("TAGG", "" + response.body()!!.message.toString())

                    val builder = AlertDialog.Builder(dialog.context)
                    builder.setTitle("Thanks for letting us know")
                    builder.setMessage("Report sent successfully! Your request is under review, we will notify you on this")
                    builder.setIcon(android.R.drawable.ic_dialog_alert)
                    builder.setPositiveButton("Ok"){dialogInterface, which ->
                        // Toast.makeText(applicationContext,"clicked yes",Toast.LENGTH_LONG).show()
                        dialog.cancel()
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                } else {

                    // ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<ReportResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })

    }

    public fun openBottomSheetReports(id: Int, type:String) {

        val dialog = Dialog(this)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog .setCancelable(true)
        dialog .setContentView(R.layout.bottom_sheet_reports)
        val textheadline = dialog.findViewById(R.id.textreports) as TextView
        if (type.equals("group"))
            textheadline.setText("Report or give feedback on this group")
        else if (type.equals("post"))
            textheadline.setText("Report or give feedback on this post")
        else if (type.equals("comment"))
            textheadline.setText("Report or give feedback on this comment")
        val spinner = dialog.findViewById(R.id.report_spinner) as Spinner
        val adapter = ArrayAdapter.createFromResource(this, R.array.report_list, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val reportdetails = dialog .findViewById(R.id.reportdetails) as EditText

        val sendreport = dialog .findViewById(R.id.sendreport) as LinearLayout
        sendreport.setOnClickListener {
            reportGroup(id,type, spinner.selectedItem.toString(), reportdetails.text.toString(), dialog)
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

//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeByte(if (isLoggedIn) 1 else 0)
//        parcel.writeString(next_page_no_featured)
//        parcel.writeString(prev_page_no_featured)
//        parcel.writeInt(type_group)
//        parcel.writeString(listCities)
//        parcel.writeString(listCategory)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<ZActivityGroups> {
//        override fun createFromParcel(parcel: Parcel): ZActivityGroups {
//            return ZActivityGroups(parcel)
//        }
//
//        override fun newArray(size: Int): Array<ZActivityGroups?> {
//            return arrayOfNulls(size)
//        }
//    }


    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?)  {

        if (requestCode == 2) {
            mygroups_selected.visibility = View.VISIBLE
            featured_selected.visibility = View.GONE
            mygroups_selected1.visibility = View.VISIBLE
            featured_selected1.visibility = View.GONE
            all_selected.visibility = View.GONE
            all_selected1.visibility = View.GONE
            saveJob(data!!.getExtras()!!.getInt("jobID")!!, data!!.getExtras()!!.getString("note")!!,
                data!!.getExtras()!!.getInt("resId")!!, data!!.getExtras()!!.getString("status")!!)
        }


        if (requestCode == GALLERY_PDF) {

            if (data != null) {
                val contentURI1 = data!!.data
               /* try
                {
                    val f = File(contentURI1.toString())
                    val file: File = File(contentURI1!!.getPath());

                    picturepath_life = file.getName();
                    //upload_doc.setText(picturepath_life)
                    val data1 = applicationContext.contentResolver.openInputStream(contentURI1!!)!!.readBytes()
                    val encodedBytes = Base64.encode(data1, Base64.DEFAULT)
                    imageEncoded_life = encodedBytes.toString()
                    Toast.makeText(applicationContext,"Upload Success", Toast.LENGTH_LONG).show()

                }catch (e: IOException) {
                    e.printStackTrace()
                    Log.d("TAGG", "STACK" + e.printStackTrace())
                    Toast.makeText(applicationContext, "Failed!", Toast.LENGTH_SHORT).show()
                }*/

                try {

                    val fileName = HelperMethods.getFilePath(this, data.data)

                    val fileString = HelperMethods.testUriFile(this, fileName!!, data.data)

                    val  file = data?.data?.let {
                        HelperMethods.getFile(
                            this@ZActivityJobs,
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

    public override fun onResume() {
        super.onResume()
        // put your code here...

    }
}




