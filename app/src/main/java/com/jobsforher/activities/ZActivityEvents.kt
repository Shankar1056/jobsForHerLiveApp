package com.jobsforher.activities

//import com.payumoney.core.PayUmoneySdkInitializer
//import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Layout.JUSTIFICATION_MODE_INTER_WORD
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
import com.android.jobsforher.activities.ZEventsTypeAdapter
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.jobsforher.R
import com.jobsforher.adapters.*
import com.jobsforher.helpers.Constants
import com.jobsforher.helpers.HelperMethods
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.models.*
import com.jobsforher.models.EventCompanies
import com.jobsforher.network.responsemodels.*
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import com.plumillonforge.android.chipview.Chip
import com.plumillonforge.android.chipview.OnChipClickListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_jfh_banner_jobs.*
import kotlinx.android.synthetic.main.events_app_bar_main.*
import kotlinx.android.synthetic.main.toolbar_groups.*
import kotlinx.android.synthetic.main.zevents_content_main.*
import kotlinx.android.synthetic.main.zjobs_content_main.activity_jfh_banner_jobs
import kotlinx.android.synthetic.main.zjobs_content_main.all
import kotlinx.android.synthetic.main.zjobs_content_main.all1
import kotlinx.android.synthetic.main.zjobs_content_main.all_selected
import kotlinx.android.synthetic.main.zjobs_content_main.all_selected1
import kotlinx.android.synthetic.main.zjobs_content_main.featured
import kotlinx.android.synthetic.main.zjobs_content_main.featured1
import kotlinx.android.synthetic.main.zjobs_content_main.featured_selected
import kotlinx.android.synthetic.main.zjobs_content_main.featured_selected1
import kotlinx.android.synthetic.main.zjobs_content_main.filer_default_layout
import kotlinx.android.synthetic.main.zjobs_content_main.filter
import kotlinx.android.synthetic.main.zjobs_content_main.filter1
import kotlinx.android.synthetic.main.zjobs_content_main.find_morejobs
import kotlinx.android.synthetic.main.zjobs_content_main.joblisting_tag
import kotlinx.android.synthetic.main.zjobs_content_main.jobstext
import kotlinx.android.synthetic.main.zjobs_content_main.layouttop
import kotlinx.android.synthetic.main.zjobs_content_main.loadnext
import kotlinx.android.synthetic.main.zjobs_content_main.loadprev
import kotlinx.android.synthetic.main.zjobs_content_main.login_view
import kotlinx.android.synthetic.main.zjobs_content_main.login_view1
import kotlinx.android.synthetic.main.zjobs_content_main.mainScroll_jobs
import kotlinx.android.synthetic.main.zjobs_content_main.mainScroll_jobss
import kotlinx.android.synthetic.main.zjobs_content_main.mygroups
import kotlinx.android.synthetic.main.zjobs_content_main.mygroups1
import kotlinx.android.synthetic.main.zjobs_content_main.mygroups_selected
import kotlinx.android.synthetic.main.zjobs_content_main.mygroups_selected1
import kotlinx.android.synthetic.main.zjobs_content_main.recycler_view_groups
import kotlinx.android.synthetic.main.zjobs_content_main.search_default
import kotlinx.android.synthetic.main.zjobs_content_main.search_default_button
import kotlinx.android.synthetic.main.zjobs_content_main.seemore_text
import kotlinx.android.synthetic.main.zjobs_content_main.success_default_layout
import kotlinx.android.synthetic.main.zjobs_content_main.tag_group
import kotlinx.android.synthetic.main.zjobs_content_main.view_all_categories
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ZActivityEvents() : Footer(), NavigationView.OnNavigationItemSelectedListener{

    var mRecyclerView: RecyclerView? = null
    var mAdapter: RecyclerView.Adapter<*>? = null
    var listOfEventsdata: ArrayList<EventsView> = ArrayList()
    var listOfFilterMygroups: ArrayList<GroupsView> = ArrayList()
    var listOfFeaturedMygroups: ArrayList<GroupsView> = ArrayList()
    var listOfCompareGroupdata: ArrayList<GroupsView> = ArrayList()
    var listOfCompareJoineddata: ArrayList<Int> = ArrayList()
    var listOfCompareRecdata: ArrayList<Int> = ArrayList()
    var message_filter:String=""
    var scroll:Int = 0
    private val GALLERY_PDF = 3
    private val PAYMENT_CODE = 10

    var mRecyclerViewAds: RecyclerView? = null
    var mAdapterAds: RecyclerView.Adapter<*>? = null
    var listOfAdsdata: ArrayList<String> = ArrayList()


    var mgroupsRecyclerView: RecyclerView? = null
    var hotEventsAdapter: RecyclerView.Adapter<*>? = null
    var isLoggedIn=true
    private var retrofitInterface: RetrofitInterface? = null
    private var retrofitInterface1: RetrofitInterface? = null
    var next_page_no_featured: String="1"
    var prev_page_no_featured: String="1"
    var type_group: Int=0
    var isfilter:Int = 0

    var listOfCategories: ArrayList<CategoryView> = ArrayList()
    var listOfCities: ArrayList<CityView> = ArrayList()
    var listOfEventType: ArrayList<JobTypeView> = ArrayList()
    var listOfEventType1: ArrayList<JobTypeView> = ArrayList()
    var listOfJobFArea: ArrayList<FunctionalAreaView> = ArrayList()
    var listOfJobFAreaSorted: ArrayList<FunctionalAreaView> = ArrayList()
    var listOfJobIndustry: ArrayList<FunctionalAreaView> = ArrayList()

    var category_name: String=""
    var category_nameArray: ArrayList<String> = ArrayList()
    var location_name: String=""
    var location_nameArray:ArrayList<String> = ArrayList()
    var event_types: String=""
    var event_typesArray: ArrayList<String> = ArrayList()
    var min_year: String=""
    var max_year: String=""
    var functional_area:String=""
    var industries:String=""
    var company_id:String=""
    var type: String=""
    var keyword_text: String=""
    var ticket_typee:String = ""

    var listOfMainCategories: ArrayList<CategoriesMainView> = ArrayList()
    var listOfResumes: ArrayList<ResumeView>  = ArrayList()
    var listOfLocations: ArrayList<EventLocation>  = ArrayList()

    var mAdapterCategories: RecyclerView.Adapter<*>? = null
    var mAdapterCities: RecyclerView.Adapter<*>? = null
    var mAdapterEventtype: RecyclerView.Adapter<*>? = null
    var mAdapterFArea: RecyclerView.Adapter<*>? = null
    private var doubleBackToExitPressedOnce = false
    var mAdapterIndustry: RecyclerView.Adapter<*>? = null

    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.zactivity_events)
        isLoggedIn=intent.getBooleanExtra("isLoggedIn",false)
        Log.d("TAGG", isLoggedIn.toString())
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        val opacity:Int = 130; // from 0 to 255
        overlay.setBackgroundColor(opacity * 0x1000000); // black with a variable alpha
        val params: FrameLayout.LayoutParams =  FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT);
        params.gravity = Gravity.BOTTOM;
        overlay.setLayoutParams(params);
        overlay.invalidate(); // update the view

        mappingWidgets()
        handleBackgrounds(btnEvents)

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

        updatepref.setOnClickListener {
            intent = Intent(applicationContext, ZActivityDashboard::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("isLoggedIn", true)
            intent.putExtra("pagetype", 5)
            startActivity(intent)
        }

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
            else if(event_types.contains(Regex(it.text.toString()))){

                var output:ArrayList<String> = ArrayList(event_types.substring(0,event_types.length-1) . split (","))
                var k:Int=0
                for(i in 0 until output.size){
                    if(output[i].equals(it.text.toString()))
                        k = i
                }
                output.removeAt(k)
                event_types = output.toString()
                event_types = event_types.substring(1,event_types.length-1)
                Log.d("TAGG","PARAMS"+event_types.toString())
                //loadFilteredData("1",a.toString().trim(),"", "","","","","","","")
            }
            else if(category_name.contains(Regex(it.text.toString()))){

                var output:ArrayList<String> = ArrayList(category_name.substring(0,category_name.length-1) . split (","))
                var k:Int=0
                for(i in 0 until output.size){
                    if(output[i].equals(it.text.toString()))
                        k = i
                }
                output.removeAt(k)
                category_name = output.toString()
                category_name = category_name.substring(1,category_name.length-1)
                Log.d("TAGG","PARAMS"+category_name.toString())
                //loadFilteredData("1",a.toString().trim(),"", "","","","","","","")
            }
            tag_group.remove(it)
            if(tag_group.count()==0){
                joblisting_tag.visibility = View.GONE
                layoutdefaultjob.visibility = View.GONE
                find_morejobs.callOnClick()
            }
            loadFilteredData("1",keyword_text,category_name,location_name, event_types,type)



        })
        loadNotificationbubble()
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
                menu.findItem(R.id.action_events).title = Html.fromHtml("<font color='#99CA3B'>Events</font>")
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
                menu.findItem(R.id.action_events).title = Html.fromHtml("<font color='#99CA3B'>Events</font>")
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
        hotEventsAdapter = EventsAdapter(listOfEventsdata, isLoggedIn, 1,0,listOfCompareJoineddata,0,"Events")
        mgroupsRecyclerView!!.adapter = hotEventsAdapter

        img_profile_toolbar.setOnClickListener{
            intent = Intent(applicationContext, ZActivityDashboard::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("isLoggedIn", true)
            intent.putExtra("pagetype", 0)
            startActivity(intent)
        }



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
            listOfEventsdata.clear()
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
            listOfEventsdata.clear()
//            loadotherEventsData("1")
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

        filter.setOnClickListener {
            hideSuccesLayout()
            openBottomSheetFilter()

        }

//        search_layout.setOnClickListener {
//
//            loadFilteredData("1",keyword_edittext.text.toString(),category_name,location_name, event_types,type)
//        }


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

            loadFilteredData("1",search_default.text.toString().trim(),"","", "","")
        }

//        search_success_button.setOnClickListener {
//            loadFilteredData("1",search_success.text.toString().trim(),"", "","","","","","","")
//
//        }

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

            filer_default_layout.visibility = View.GONE
            success_default_layout.visibility = View.GONE
            layouttop.visibility = View.GONE
//            layoutadd.visibility = View.VISIBLE
            // layout1.visibility = View.VISIBLE
            activity_jfh_banner_jobs.visibility = View.GONE
            mainScroll_jobs.fullScroll(View.FOCUS_UP);
            mainScroll_jobs.smoothScrollTo(0,0);
            recycler_view_groups.setFocusable(false);

            filter.setText("FILTER")
            isfilter =0

            keyword_text=""
            category_name= ""
            location_name=""
            event_types=""

            type=""
            mygroups_selected.visibility = View.VISIBLE
            featured_selected.visibility = View.GONE
            mygroups_selected1.visibility = View.VISIBLE
            featured_selected1.visibility = View.GONE
            all_selected.visibility = View.GONE
            all_selected1.visibility = View.GONE
            type_group=1
            listOfCompareGroupdata.clear()
            listOfCompareJoineddata.clear()
            listOfEventsdata.clear()
            loadAppliedJobs()
        }



        goto_events.setOnClickListener {
            find_morejobs11.callOnClick()
        }

        find_morejobs11.setOnClickListener {
            finish()
            intent = Intent(applicationContext, ZActivityEvents::class.java)
            intent.putExtra("isLoggedIn",true)
            startActivity(intent)
//            activity_jfh_banner_jobs.visibility = View.GONE
//            filer_default_layout.visibility = View.GONE
//            success_default_layout.visibility = View.GONE
//            layouttop.visibility = View.GONE
////            layoutadd.visibility = View.VISIBLE
//           // layout1.visibility = View.VISIBLE
//            mainScroll_jobs.fullScroll(View.FOCUS_UP);
//            mainScroll_jobs.smoothScrollTo(0,0);
//            recycler_view_groups.setFocusable(false);


        }

//        create_preferences.setOnClickListener {
//            create_preferences_layout.callOnClick()
//        }
//
//        create_preferences_layout.setOnClickListener {
//
//            openBottomSheetJobAlerts(0)
//        }

        getAllCategories()



        mainScroll_jobss.getViewTreeObserver().addOnScrollChangedListener(object: ViewTreeObserver.OnScrollChangedListener {
            override fun onScrollChanged() {
                Log.d("TAGG","END EFORE")
                val view: View = mainScroll_jobss.getChildAt(mainScroll_jobss.getChildCount() - 1);
                val diff:Int = (view.getBottom() - (mainScroll_jobss.getHeight() + mainScroll_jobss.getScrollY()));
                if (diff == 0 && scroll==1) {
                    Log.d("TAGG","END")
                    if (type_group==1 )
                        loadhotEventsData(next_page_no_featured)
                    else if (type_group==2)
                        loadotherEventsData(next_page_no_featured!!)
                    else if(type_group==3)
                        loadpastEventsData(next_page_no_featured!!)
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

        recommended1.setOnClickListener {
            recommended.callOnClick()
        }

        mygroups.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {

                mygroups_selected.visibility = View.VISIBLE
                featured_selected.visibility = View.GONE
                mygroups_selected1.visibility = View.VISIBLE
                featured_selected1.visibility = View.GONE
                recommended_selected.visibility = View.GONE
                recommended_selected1.visibility = View.GONE
                all_selected.visibility = View.GONE
                all_selected1.visibility = View.GONE
                search_default.clearFocus()
                search_default.setText(null)

                type_group=1
                //Toast.makeText(applicationContext,"My groups",Toast.LENGTH_LONG).show()
                if (isfilter>0) {
                    loadFilteredData("1",keyword_text,category_name,location_name, event_types,type)
                }
                else{
                    listOfCompareGroupdata.clear()
                    listOfCompareJoineddata.clear()
                    listOfEventsdata.clear()
                    loadAppliedJobs()
                }


                featured.setTypeface(null, Typeface.NORMAL);
                mygroups.setTypeface(null, Typeface.BOLD);
                featured1.setTypeface(null, Typeface.NORMAL);
                mygroups1.setTypeface(null, Typeface.BOLD);
                all.setTypeface(null, Typeface.NORMAL);
                all1.setTypeface(null, Typeface.NORMAL);
                recommended.setTypeface(null, Typeface.NORMAL);
                recommended1.setTypeface(null, Typeface.NORMAL);
            }
        })
        featured.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {

                mygroups_selected.visibility = View.GONE
                featured_selected.visibility = View.VISIBLE
                mygroups_selected1.visibility = View.GONE
                featured_selected1.visibility = View.VISIBLE
                recommended_selected.visibility = View.GONE
                recommended_selected1.visibility = View.GONE
                all_selected.visibility = View.GONE
                all_selected1.visibility = View.GONE
                search_default.clearFocus()
                search_default.setText(null)

                type_group=2
                if (isfilter>0){
                    loadFilteredData("1",keyword_text,category_name,location_name, event_types,type)
                }
                else{
                    listOfEventsdata.clear()
                    loadotherEventsData("1")
                }

                featured.setTypeface(null, Typeface.BOLD);
                mygroups.setTypeface(null, Typeface.NORMAL);
                featured1.setTypeface(null, Typeface.BOLD);
                mygroups1.setTypeface(null, Typeface.NORMAL);
                all.setTypeface(null, Typeface.NORMAL);
                all1.setTypeface(null, Typeface.NORMAL);
                recommended.setTypeface(null, Typeface.NORMAL);
                recommended1.setTypeface(null, Typeface.NORMAL);
            }
        })

        all.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {

                mygroups_selected.visibility = View.GONE
                featured_selected.visibility = View.GONE
                mygroups_selected1.visibility = View.GONE
                featured_selected1.visibility = View.GONE
                recommended_selected.visibility = View.GONE
                recommended_selected1.visibility = View.GONE
                all_selected.visibility = View.VISIBLE
                all_selected1.visibility = View.VISIBLE
                search_default.clearFocus()
                search_default.setText(null)
                type_group=3
//                Toast.makeText(applicationContext,"All groups",Toast.LENGTH_LONG).show()
                if (isfilter>0){
                    loadFilteredData("1",keyword_text,category_name,location_name, event_types,type)
                }
                else{
                    listOfEventsdata.clear()
                    loadpastEventsData("1")
                }

                featured.setTypeface(null, Typeface.NORMAL);
                mygroups.setTypeface(null, Typeface.NORMAL);
                featured1.setTypeface(null, Typeface.NORMAL);
                mygroups1.setTypeface(null, Typeface.NORMAL);
                all.setTypeface(null, Typeface.BOLD);
                all1.setTypeface(null, Typeface.BOLD);
                recommended.setTypeface(null, Typeface.NORMAL);
                recommended1.setTypeface(null, Typeface.NORMAL);
            }
        })

        recommended.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {

                mygroups_selected.visibility = View.GONE
                featured_selected.visibility = View.GONE
                mygroups_selected1.visibility = View.GONE
                featured_selected1.visibility = View.GONE
                all_selected.visibility = View.GONE
                all_selected1.visibility = View.GONE
                recommended_selected.visibility = View.VISIBLE
                recommended_selected1.visibility = View.VISIBLE
                search_default.clearFocus()
                search_default.setText(null)
                type_group=3
//                Toast.makeText(applicationContext,"All groups",Toast.LENGTH_LONG).show()
                if (isfilter>0){
                    loadFilteredData("1",keyword_text,category_name,location_name, event_types,type)
                }
                else{
                    listOfEventsdata.clear()
                    loadrecommendedEventsData("1")
                }

                featured.setTypeface(null, Typeface.NORMAL);
                mygroups.setTypeface(null, Typeface.NORMAL);
                featured1.setTypeface(null, Typeface.NORMAL);
                mygroups1.setTypeface(null, Typeface.NORMAL);
                all.setTypeface(null, Typeface.NORMAL);
                all1.setTypeface(null, Typeface.NORMAL);
                recommended.setTypeface(null, Typeface.BOLD);
                recommended1.setTypeface(null, Typeface.BOLD);
            }
        })

        loadprev.setOnClickListener {

            // listOfEventsdata.clear()
            if (type_group==1)
                loadhotEventsData(prev_page_no_featured)
            else if (type_group==2){}
//                loadotherEventsData(prev_page_no_featured!!)
            else if(type_group==3){}
//                loadAllGroupData(prev_page_no_featured)
        }

        loadnext.setOnClickListener {
            // listOfEventsdata.clear()
            if (type_group==1)
                loadhotEventsData(next_page_no_featured)
            else if (type_group==2){}
//                loadotherEventsData(next_page_no_featured!!)
            else if(type_group==3){}
//                loadAllGroupData(next_page_no_featured)
        }

        notifLayout.setOnClickListener {
            cart_badge.setText("0")
            intent = Intent(applicationContext, Notification::class.java)
            startActivity(intent)
        }
    }

    fun getListOfLocations(id:Int, btnJoinGroup:Button, title:String, btnJoined: Button, resumeRequired:Boolean, prefRequired:Boolean,
                           ticket_type:String){

        ticket_typee = ticket_type
        Log.d("TAGG",ticket_typee)
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.GetLocationList(id, EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<EventLocationDetails> {
            override fun onResponse(call: Call<EventLocationDetails>, response: Response<EventLocationDetails>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.e("RESPONSE location", "" + Gson().toJson(response))

                if (response.isSuccessful ) {

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
                        val locations: JSONArray = jsonObject1.optJSONArray("body")
                        listOfLocations.clear()

                        for (k in 0 until locations.length()) {
                            val locModel: EventLocation = EventLocation()
                            locModel.pincode =
                                if (locations.optJSONObject(k).isNull("pincode")) "" else locations.optJSONObject(k).optString("pincode")
                            locModel.discounted_price =
                                if (locations.optJSONObject(k).isNull("discounted_price")) 0 else locations.optJSONObject(k).optInt("discounted_price")
                            locModel.event_register_start_date_time =
                                if (locations.optJSONObject(k).isNull("event_register_start_date_time")) "" else locations.optJSONObject(k).optString("event_register_start_date_time")
                            locModel.event_type =
                                if (locations.optJSONObject(k).isNull("event_type")) "" else locations.optJSONObject(k).optString("event_type")
                            locModel.event_start_date_time =
                                if (locations.optJSONObject(k).isNull("event_start_date_time")) "" else locations.optJSONObject(k).optString("event_start_date_time")
                            locModel.amount =
                                if (locations.optJSONObject(k).isNull("amount")) 0 else locations.optJSONObject(k).optInt("amount")
                            locModel.discount_start_date_time =
                                if (locations.optJSONObject(k).isNull("discount_start_date_time")) "" else locations.optJSONObject(k).optString("discount_start_date_time")
                            locModel.event_register_end_date_time =
                                if (locations.optJSONObject(k).isNull("event_register_end_date_time")) "" else locations.optJSONObject(k).optString("event_register_end_date_time")
                            locModel.discount_end_date_time =
                                if (locations.optJSONObject(k).isNull("discount_end_date_time")) "" else locations.optJSONObject(k).optString("discount_end_date_time")
                            locModel.discount_active =
                                if (locations.optJSONObject(k).isNull("discount_active")) "" else locations.optJSONObject(k).optString("discount_active")
                            locModel.address =
                                if (locations.optJSONObject(k).isNull("address")) "" else locations.optJSONObject(k).optString("address")
                            locModel.event_end_date_time =
                                if (locations.optJSONObject(k).isNull("event_end_date_time")) "" else locations.optJSONObject(k).optString("event_end_date_time")

                            locModel.id =
                                if (locations.optJSONObject(k).isNull("id")) 0 else locations.optJSONObject(k).optInt("id")
                            locModel.state =
                                if (locations.optJSONObject(k).isNull("state")) "" else locations.optJSONObject(k).optString("state")
                            locModel.seats =
                                if (locations.optJSONObject(k).isNull("seats")) "" else locations.optJSONObject(k).optString("seats")
                            locModel.event_id =
                                if (locations.optJSONObject(k).isNull("event_id")) 0 else locations.optJSONObject(k).optInt("event_id")
                            locModel.country =
                                if (locations.optJSONObject(k).isNull("country")) "" else locations.optJSONObject(k).optString("country")
                            locModel.registration_open =
                                if (locations.optJSONObject(k).isNull("registration_open")) false else locations.optJSONObject(k).optBoolean("registration_open")
                            locModel.google_map_url =
                                if (locations.optJSONObject(k).isNull("google_map_url")) "" else locations.optJSONObject(k).optString("google_map_url")
                            locModel.city =
                                if (locations.optJSONObject(k).isNull("city")) "" else locations.optJSONObject(k).optString("city")
                            locModel.external_url =
                                if (locations.optJSONObject(k).isNull("external_url")) "" else locations.optJSONObject(k).optString("external_url")

                            listOfLocations.add(locModel)
                        }

                    }

                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }

                if(listOfLocations.size>1)
                    openBottomSheetLocations(id, btnJoinGroup, title, btnJoined, resumeRequired, prefRequired,listOfLocations)
                else {
                    applyEvents(
                        listOfLocations[0].city!!,
                        listOfLocations[0].event_id!!,
                        listOfLocations[0].id!!,
                        btnJoinGroup,
                        title,
                        btnJoined,
                        resumeRequired,
                        prefRequired,
                        listOfLocations[0].external_url!!
                    )
                }
            }

            override fun onFailure(call: Call<EventLocationDetails>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")

            }
        })
    }

    public fun openBottomSheetLocations(id:Int, btnJoinGroup:Button, title:String, btnJoined: Button,
                                        resumeRequired:Boolean, prefRequired:Boolean,listlocations:ArrayList<EventLocation>) {

        val dialog = Dialog(this)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog .setCancelable(true)
        dialog .setContentView(R.layout.bottom_sheet_event_locations)

        var mAdapterResume: RecyclerView.Adapter<*>? = null
        val mRecyclerView = dialog.findViewById(R.id.event_loc_recycler_view) as RecyclerView
        val mLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL,false)
        mAdapterResume = EventLocationAdapter(dialog,listlocations,id,btnJoinGroup,title,btnJoined,resumeRequired,prefRequired,"Events")
        mRecyclerView!!.adapter = mAdapterResume
        mRecyclerView!!.layoutManager = mLayoutManager


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
                    if (event.getRawX() >= (closebutton.getRight() - closebutton.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        dialog.cancel()
                        return true;
                    }
                }
                return false;

                return v?.onTouchEvent(event) ?: true
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

    fun applyEvents(city: String, event_id:Int, location_id:Int, btnJoinGroup:Button, title:String, btnJoined: Button, resRequired:Boolean,
                    prefRequired:Boolean, externalUrl:String){


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

                } else {
                    //   ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
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
                            Log.d("TAGG", "PREf ID:" + json_objectdetail.optInt("user_id").toString())
                        }
                        resumeRequired_onClick(a, title, event_id, location_id, resRequired, prefRequired)
                    }

                }
            }

            override fun onFailure(call: Call<CheckPreferenceResponse>, t: Throwable) {
                Logger.d("TAGG", "Apply Job FAILED : $t")
                openBottomSheetPreferences()
            }
        })

    }

    fun resumeRequired_onClick(prefid:Int,title:String,event_id:Int,location_id:Int,resumeRequired:Boolean, prefRequired:Boolean){

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.CheckDefault(prefid, EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<CheckDefaultResponse> {
            override fun onResponse(call: Call<CheckDefaultResponse>, response: Response<CheckDefaultResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.e("RESPONSE leave group", "" + Gson().toJson(response))

                if (response.isSuccessful && response.body()!!.responseCode == 10996) {

                    //   ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
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
                            model.path  =json_objectdetail.optString("path")
                            model.id = json_objectdetail.optInt("id")
                            model.title = if(json_objectdetail.isNull("title"))"" else json_objectdetail.optString("title")
                            model.is_parsed = false//json_objectdetail.optBoolean("is_parsed")
                            model.created_on = json_objectdetail.optString("created_on")
                            model.modified_on = json_objectdetail.optString("modified_on")
                            model.deleted  =json_objectdetail.optBoolean("deleted")
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
                    // ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
                if (resumeRequired)
                    openBottomSheetUploadDoc_Events(event_id,title, listOfResumes, location_id)
                else{
                    //saveEvent(jobId, note.text.toString(), listOfResumes[0].id, "registered",location_id)
                    if(listOfResumes.size>0)
                        saveEvent(event_id, "", listOfResumes[0].id, "registered",location_id)
                    else
                        saveEvent(event_id, "", 0, "registered",location_id)
                }
            }

            override fun onFailure(call: Call<CheckDefaultResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                if (resumeRequired)
                    openBottomSheetUploadDoc_Events(event_id,title, listOfResumes,location_id)
                else{
                    if(listOfResumes.size>0)
                        saveEvent(event_id, "", listOfResumes[0].id, "registered",location_id)
                    else
                        saveEvent(event_id, "", 0, "registered",location_id)
                }
            }
        })
    }

    public fun openBottomSheetUploadDoc_Events(jobId:Int, title: String ,listOfResume: ArrayList<ResumeView>,location_id:Int) {

        val dialog = Dialog(this)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog .setCancelable(true)
        dialog .setContentView(R.layout.bottom_sheet_applyjobs)
        val add_resume = dialog.findViewById(R.id.add_resume) as LinearLayout
        val add_resume_title = dialog.findViewById(R.id.add_resume_title) as LinearLayout
        val mobile_num = dialog.findViewById(R.id.mobile_layout) as LinearLayout
        mobile_num.visibility = View.GONE
        val note_layout = dialog.findViewById(R.id.note_layout) as LinearLayout
        note_layout.visibility = View.GONE
        val title_name = dialog.findViewById(R.id.title) as TextView
        title_name.setText("Title")
        val note = dialog.findViewById(R.id.note) as EditText

        Log.d("TAGG","List"+listOfResume.size)

        var mAdapterResume: RecyclerView.Adapter<*>? = null
        val mRecyclerView = dialog.findViewById(R.id.resume_recycler_view) as RecyclerView
        val mLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL,false)
        mAdapterResume = ResumeAdapter(listOfResumes)
        mRecyclerView!!.adapter = mAdapterResume
        mRecyclerView!!.layoutManager = mLayoutManager
//        val mobileLayout = dialog.findViewById(R.id.mobile_layout) as LinearLayout
//        if(EndPoints.PHONE_NO.length<2)
//            mobileLayout.visibility = View.GONE
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
                    if (event.getRawX() >= (closebutton.getRight() - closebutton.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
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
            //if (mobileno.getText().length>0){
            if (listOfResumes.size > 0) {
                //if(isSelected) {
                //  Toast.makeText(applicationContext, "Applied", Toast.LENGTH_LONG).show()
                saveEvent(
                    jobId,
                    note.text.toString(),
                    listOfResumes[0].id,
                    "registered",
                    location_id
                )
                dialog.cancel()
//                    }
//                    else{
//                        Toast.makeText(applicationContext,"Please select a resume",Toast.LENGTH_LONG).show()
//                    }
            } else {
                if (imageEncoded_life.isNullOrEmpty()) {
                    Toast.makeText(this, "Please upload a resume", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (restitle.getText().toString().trim().isNullOrEmpty()) {
                    Toast.makeText(this, "Please Enter your Title", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                sendResume(restitle.getText().toString(), jobId, location_id)
                dialog.cancel()
            }
//            }
//            else{
//                Toast.makeText(applicationContext,"Please enter mobile number", Toast.LENGTH_LONG).show()
//
//            }
        }
        val apply_job = dialog.findViewById(R.id.apply_pref) as Button
        apply_job.setText("Register")
        apply_job.setOnClickListener {
            //if (mobileno.getText().length>0) {
            if (listOfResumes.size > 0) {
//                    if(isSelected) {
                // Toast.makeText(applicationContext, "Applied", Toast.LENGTH_LONG).show()
                saveEvent(jobId, note.text.toString(), listOfResumes[0].id, "registered",location_id)
                dialog.cancel()
//                    }
//                    else{
//                        Toast.makeText(applicationContext,"Please select a resume",Toast.LENGTH_LONG).show()
//                    }
            } else {
                if (imageEncoded_life.isNullOrEmpty()) {
                    Toast.makeText(this, "Please upload a resume", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (restitle.getText().toString().trim().isNullOrEmpty()) {
                    Toast.makeText(this, "Please enter title", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                sendResume(restitle.getText().toString(), jobId, location_id)
                dialog.cancel()
            }
//            }
//            else{
//                Toast.makeText(applicationContext,"Please enter mobile number", Toast.LENGTH_LONG).show()
//
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

    public fun hideSuccesLayout(){
        success_default_layout.visibility = View.GONE
        layouttop.visibility = View.GONE
//        layoutadd .visibility = View.VISIBLE
        // layout1.visibility = View.VISIBLE
        activity_jfh_banner_jobs.visibility = View.GONE
    }

    fun loadNotificationbubble(){


        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.GetNotificationBubble(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<NotificationBubbleResponse> {
            override fun onResponse(call: Call<NotificationBubbleResponse>, response: Response<NotificationBubbleResponse>) {
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
        for (model in listOfEventType1) {
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
            params["experience_max_year"] = experience.toString()
            params["experience_min_year"] = "0"//experience.toString()
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

                    // ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                    Logger.e("TAGG", "Add Success" + response.body()!!.message.toString())
                    //loadJobAlert()
                } else {
                    // loadJobAlert()
                    //  ToastHelper.makeToast(applicationContext, "Invalid Request")
                    Logger.e("TAGG", "Job Alert Created" + response.body()!!.message.toString())
                }
            }

            override fun onFailure(call: Call<JobAlertResponse>, t: Throwable) {
                //loadJobAlert()
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
        for (model in listOfEventType1) {
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


                if (response.isSuccessful) {

                    ToastHelper.makeToast(applicationContext, response.body()?.message.toString())

                } else {

                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<UpdateReplyResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })

    }

    var isSelected = false
    fun isRadioSelected(){
        isSelected = true
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
        intent = Intent.createChooser(chooseFile, "Choose a file")
        startActivityForResult(intent, GALLERY_PDF)
    }

    fun sendResume(title: String,jobId:Int,location_id:Int){

        val params = HashMap<String, String>()

        params["title"] = title
        params["resume_filename"] = picturepath_life.toString()
        params["resume_file"] = imageEncoded_life.toString()

        Logger.d("TAGG", "PARAMS: "+params)
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.SendResume(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        Logger.d("URL", "" + params)
        call.enqueue(object : Callback<UpdateReplyResponse> {
            override fun onResponse(call: Call<UpdateReplyResponse>, response: Response<UpdateReplyResponse>) {
                Logger.d("URL", "" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
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
                    // var jsonarray: JSONArray = jsonObject1.optJSONArray("body")

                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Adding Success!!", Toast.LENGTH_LONG)
                            .show()
                        val resumeId = 200

                        saveEvent(jobId, "", resumeId, "registered", location_id)
                    } else {
                        ToastHelper.makeToast(applicationContext, "message")
                    }
                }
            }
            override fun onFailure(call: Call<UpdateReplyResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext,"Adding Failed!!", Toast.LENGTH_LONG).show()

            }
        })
    }

    fun saveEvent(job_id:Int,note:String,resume_id:Int,applied_status:String,location_id:Int){

        val params = HashMap<String, String>()

        params["event_id"] = job_id.toString()
        params["event_location_id"] = location_id.toString()
        params["note"] = note
        params["register_status"] = applied_status
        if(resume_id>0)
            params["resume_id"] = resume_id.toString()
        params["attended"] = "false"

        val sdformat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dateobj = Date()
        var s: String = sdformat.format(dateobj);

        params["register_date"] = s

        Log.d("TAGG", "PARAMS"+params)

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.saveEvent( EndPoints.CLIENT_ID, "Bearer "+
                EndPoints.ACCESS_TOKEN,params)

        call.enqueue(object : Callback<SaveEventResponse> {
            override fun onResponse(call: Call<SaveEventResponse>, response: Response<SaveEventResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.e("RESPONSE success report", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    listOfEventsdata.clear()
                    listOfCompareGroupdata.clear()

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
                        val jsonarray_info: JSONObject = jsonObject1.optJSONObject("body")

                        var a:Int = 0
                        var b: String=""
                        var hash:String = ""
                        var uname:String = ""
                        var amt:Int = 0
                        var email:String = ""
                        var uphone:String = ""
                        var txnid:Int = 0
                        var productinfo:Int = 0

                        //for (i in 0 until response.body()!!.body!!.) {

                        //val json_objectdetail: JSONObject = jsonarray_info.optJSONObject(0)

                        b = ticket_typee //jsonarray_info.optString("ticket_type")
                        if(b.equals("paid")){
                            hash = jsonarray_info.optString("hash_key")
                            uname = jsonarray_info.optString("username")
                            if(jsonarray_info.isNull("discount")||jsonarray_info.optInt("discount")==0)
                                amt = jsonarray_info.optInt("amount")
                            else
                                amt = jsonarray_info.optInt("discount")
                            email = jsonarray_info.optString("email")
                            uphone = jsonarray_info.optString("phone_no")
                            txnid = jsonarray_info.optInt("txnid")
                            productinfo = jsonarray_info.optInt("productinfo")

                        }
                        Log.d("TAGG", "Reg ID:"+jsonarray_info.optInt("id").toString()+"  "+b)
                        // }
                        if(b.equals("free")) {
                            a = jsonarray_info.optInt("id")
                            successEvent(a, b)
                        }
                        else if(b.equals("other")) {
                            a = jsonarray_info.optInt("id")
                            successEvent(a, b)
                        }
                        else{
                            callpayment(a,hash,uname,amt,email,uphone,txnid,productinfo)
//                            var hashSequence:String  = "Android"
//                            val serverCalculatedHash = hashCal("SHA-512", hashSequence)
//                            var builder:PayUmoneySdkInitializer.PaymentParam.Builder = PayUmoneySdkInitializer.PaymentParam.Builder()
//                            builder.setAmount("10")                          // Payment amount
//                                .setTxnId("1000")                                             // Transaction ID
//                                .setPhone("8792119844")                                           // User Phone number
//                                .setProductName("Bag")                   // Product Name or description
//                                .setFirstName("Sneha")                              // User First name
//                                .setEmail("snehapm13@gmail.com")                                            // User Email ID
//                                .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")                    // Success URL (surl)
//                                .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")                     //Failure URL (furl)
//                                .setUdf1("")
//                                .setUdf2("")
//                                .setUdf3("")
//                                .setUdf4("")
//                                .setUdf5("")
//                                .setUdf6("")
//                                .setUdf7("")
//                                .setUdf8("")
//                                .setUdf9("")
//                                .setUdf10("")
//                                .setIsDebug(true)                              // Integration environment - true (Debug)/ false(Production)
//                                .setKey("QylhKRVd")                        // Merchant key
//                                .setMerchantId("5960507")                // Merchant ID
//                            //declare paymentParam object
//                            val paymentParam = builder.build()
//                            //set the hash
//                            paymentParam.setMerchantHash(serverCalculatedHash)
//                            // Invoke the following function to open the checkout page.
//                            PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam,
//                                Activity(), R.style.AppTheme_default, false)
                        }
                    }
                } else {
                    //ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<SaveEventResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
            }
        })
    }

    fun callpay(){

        val amt = java.lang.Double.toString(100.0)
        intent = Intent(applicationContext, MainActivity::class.java)
        val b = Bundle()
        b.putString("name","sneha")
        b.putString("email","sneha@jfh.in")
        b.putDouble("amount",100.0)
        b.putString("phone","8792119844")
        b.putString("hash","ca78188cfdc8277f2da7d41087d10wwe")
        b.putString("txnid","133899")
        b.putString("productinfo","91")
        intent.putExtras(b)
        startActivityForResult(intent, PAYMENT_CODE)
    }

    fun callpayment(id:Int,hash:String,uname:String,amt:Int,email:String,uphone:String,txnid:Int,productinfo:Int){

        val d:Double = amt.toDouble()
        intent = Intent(applicationContext, MainActivity::class.java)
        val b = Bundle()
        b.putString("name",uname)
        b.putString("email",email)
        b.putDouble("amount",d)
        b.putString("phone",uphone)
        b.putString("hash",hash)
        b.putString("txnid",txnid.toString())
        b.putString("productinfo",productinfo.toString())
        intent.putExtras(b)
        startActivityForResult(intent, PAYMENT_CODE)

        Log.d("TAGG", "PAY before atatus"+uname+email+d+uphone+hash+txnid+productinfo)

//        var hashSequence:String  = "Android"
//        val serverCalculatedHash = hashCal("SHA-512", hashSequence)
//        var builder:PayUmoneySdkInitializer.PaymentParam.Builder = PayUmoneySdkInitializer.PaymentParam.Builder()
//        builder.setAmount("10")                          // Payment amount
//            .setTxnId("1000")                                             // Transaction ID
//            .setPhone("8792119844")                                           // User Phone number
//            .setProductName("Bag")                   // Product Name or description
//            .setFirstName("Sneha")                              // User First name
//            .setEmail("snehapm13@gmail.com")                                            // User Email ID
//            .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")                    // Success URL (surl)
//            .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")                     //Failure URL (furl)
//            .setUdf1("")
//            .setUdf2("")
//            .setUdf3("")
//            .setUdf4("")
//            .setUdf5("")
//            .setUdf6("")
//            .setUdf7("")
//            .setUdf8("")
//            .setUdf9("")
//            .setUdf10("")
//            .setIsDebug(true)                              // Integration environment - true (Debug)/ false(Production)
//            .setKey("QylhKRVd")                        // Merchant key
//            .setMerchantId("5960507")                // Merchant ID
//        //declare paymentParam object
//        val paymentParam = builder.build()
//        //set the hash
//        paymentParam.setMerchantHash(serverCalculatedHash)
//        // Invoke the following function to open the checkout page.
//        PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam,
//            this@ZActivityEvents, R.style.AppTheme_default, false)
    }

    fun successEvent(id:Int,ttype:String){

        Log.d("TAGG", id.toString())

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getEventSuccessData( id,EndPoints.CLIENT_ID, "Bearer "+
                EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<EventRegApiDetails> {
            override fun onResponse(call: Call<EventRegApiDetails>, response: Response<EventRegApiDetails>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.e("RESPONSE success", "" + Gson().toJson(response))
                val gson = GsonBuilder().serializeNulls().create()
                val str_response = Gson().toJson(response)
                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )
                if (response.isSuccessful) {
                    if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                        val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                        val jsonarray_info: JSONObject = jsonObject1.optJSONObject("body")
                        var json_objectdetail: JSONObject =jsonarray_info
                        val s:String = "Your registration for "+ json_objectdetail.optString("title")+" events is successfully booked!"
                        dis_text.setText(s)
                        val json: JSONObject = json_objectdetail.optJSONObject("registerred_event_locations")

                        event_date.setText( getDate(json.optString("event_start_date_time"))+" to "+
                                getDate(json.optString("event_end_date_time")))
                        event_time.setText(getTime(json.optString("event_start_date_time"))+" to "+
                                getTime(json.optString("event_end_date_time")))
                        event_venue.setText(json.optString("city"))
                        if (json_objectdetail.isNull("image_url")) {
                            Picasso.with(applicationContext)
                                .load(R.drawable.ic_launcher_foreground)
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .into(event_logo)
                            event_logo.visibility = View.GONE
                        }
                        else if (json_objectdetail.optString("image_url").length>0) {
                            Picasso.with(applicationContext)
                                .load(json_objectdetail.optString("image_url"))
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .into(event_logo)
                        }
                        else{
                            Picasso.with(applicationContext)
                                .load(R.drawable.ic_launcher_foreground)
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .into(event_logo)
                            event_logo.visibility = View.GONE
                        }
                        layout_0.visibility = View.GONE
                        layout_1.visibility = View.VISIBLE
                        val extrUrRL:String =  if (json.isNull("external_url")) "" else json.optString("external_url")
                        //  ToastHelper.makeToast(applicationContext, extrUrRL)
                        if(extrUrRL.length>0) {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(extrUrRL))
                            startActivity(browserIntent)
                        }
                    }
                    listOfEventsdata.clear()
                    listOfCompareGroupdata.clear()
                } else {
                    //ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<EventRegApiDetails>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
            }
        })
    }

    fun getTime(times:String):String{
        val formatter1:SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        val dateInString:String = times
        val formatterOut:SimpleDateFormat = SimpleDateFormat("hh:mm a");
        var date : Date? = null
        var s: String=""
        try {
            date=formatter1.parse(dateInString)
            System.out.println(formatterOut.format(date));
            s = formatterOut.format(date)

        } catch ( e:ParseException) {
            e.printStackTrace()
        }
        return s
    }

    fun getDate(times:String):String{
        val formatter1:SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        val dateInString:String = times
        val formatterOut:SimpleDateFormat = SimpleDateFormat("dd MMM yyyy");
        var date : Date? = null
        var s: String=""
        try {
            date=formatter1.parse(dateInString)
            System.out.println(formatterOut.format(date));
            s = formatterOut.format(date)

        } catch ( e:ParseException) {
            e.printStackTrace()
        }
        return s
    }

    fun hashCal(type: String, hashString: String): String {
        val hash = StringBuilder()
        var messageDigest: MessageDigest? = null
        try {
            messageDigest = MessageDigest.getInstance(type)
            messageDigest!!.update(hashString.toByteArray())
            val mdbytes = messageDigest.digest()
            for (hashByte in mdbytes) {
                hash.append(Integer.toString((hashByte.toInt() and 0xff) + 0x100, 16).substring(1))
            }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return hash.toString()
    }

    fun updatePaymentEvent(response:String,txnid:String,hash:String){

        val s :String= "ordered"
        val params = HashMap<String, String>()

        params["hash_key"] = hash
        params["status"] = s
        params["response"] = response


        val jsonObj_ = JsonObject()
        jsonObj_.addProperty("hash_key", hash)
        jsonObj_.addProperty("status", s)
        jsonObj_.addProperty("response", response)

//        val txn:Int = txnid.toInt()
      //  val j: JSONObject = JSONObject(params)

      //  Log.d("TAGG","PARAMS"+j)

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.UpdatePaymentEvents( hash,"application/json",EndPoints.CLIENT_ID, "Bearer "+
                EndPoints.ACCESS_TOKEN,jsonObj_)

        call.enqueue(object : Callback<PaymetUpdateResponse> {
            override fun onResponse(call: Call<PaymetUpdateResponse>, response: Response<PaymetUpdateResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.e("RESPONSE success", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    Logger.d("MESSAGE", "SUCCESS" + "")
                    var idd:Int = response.body()!!.body!!.registered_event!!.id!!
                    successEvent(idd,"paid")

                } else {
                    //ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<PaymetUpdateResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
            }
        })
    }


    fun loadotherEventsData(pageno:String){

        if (pageno.equals("1")) {
            listOfEventsdata.clear()
            hotEventsAdapter!!.notifyDataSetChanged()
        }
        else{}

        val params = HashMap<String, String>()

        params["page_no"] = pageno.toString()
        params["page_size"] = Constants.MAXIMUM_PAGINATION_COUNT

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getotherEventsData( EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        call.enqueue(object : Callback<Event> {
            override fun onResponse(call: Call<Event>, response: Response<Event>) {

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

                    val has_next: String = jsonarray_pagination.optBoolean("has_next").toString()

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

                        default_events.visibility = View.GONE
                        updatepref.visibility = View.GONE
                        mgroupsRecyclerView!!.visibility = View.VISIBLE

                        for (i in 0 until response.body()!!.body!!.size) {

                            var json_objectdetail: JSONObject = jsonarray_info.optJSONObject(i)

                            val model_events: EventsView = EventsView();

                            var locations: JSONArray
                            var locdata: ArrayList<EventLocation> = ArrayList()
                            if(json_objectdetail.isNull("events_locations")){}
                            else {
                                locations = json_objectdetail.optJSONArray("events_locations")

                                for (k in 0 until locations.length()) {
                                    val locModel: EventLocation = EventLocation()
                                    locModel.pincode =
                                        if (locations.optJSONObject(k).isNull("pincode")) "" else locations.optJSONObject(k).optString("pincode")
                                    locModel.discounted_price =
                                        if (locations.optJSONObject(k).isNull("discounted_price")) 0 else locations.optJSONObject(k).optInt("discounted_price")
                                    locModel.event_register_start_date_time =
                                        if (locations.optJSONObject(k).isNull("event_register_start_date_time")) "" else locations.optJSONObject(k).optString("event_register_start_date_time")
                                    locModel.event_type =
                                        if (locations.optJSONObject(k).isNull("event_type")) "" else locations.optJSONObject(k).optString("event_type")
                                    locModel.event_start_date_time =
                                        if (locations.optJSONObject(k).isNull("event_start_date_time")) "" else locations.optJSONObject(k).optString("event_start_date_time")
                                    locModel.amount =
                                        if (locations.optJSONObject(k).isNull("amount")) 0 else locations.optJSONObject(k).optInt("amount")
                                    locModel.discount_start_date_time =
                                        if (locations.optJSONObject(k).isNull("discount_start_date_time")) "" else locations.optJSONObject(k).optString("discount_start_date_time")
                                    locModel.event_register_end_date_time =
                                        if (locations.optJSONObject(k).isNull("event_register_end_date_time")) "" else locations.optJSONObject(k).optString("event_register_end_date_time")
                                    locModel.discount_end_date_time =
                                        if (locations.optJSONObject(k).isNull("discount_end_date_time")) "" else locations.optJSONObject(k).optString("discount_end_date_time")
                                    locModel.discount_active =
                                        if (locations.optJSONObject(k).isNull("discount_active")) "" else locations.optJSONObject(k).optString("discount_active")
                                    locModel.address =
                                        if (locations.optJSONObject(k).isNull("address")) "" else locations.optJSONObject(k).optString("address")
                                    locModel.event_end_date_time =
                                        if (locations.optJSONObject(k).isNull("event_end_date_time")) "" else locations.optJSONObject(k).optString("event_end_date_time")

                                    locModel.id =
                                        if (locations.optJSONObject(k).isNull("id")) 0 else locations.optJSONObject(k).optInt("id")
                                    locModel.state =
                                        if (locations.optJSONObject(k).isNull("state")) "" else locations.optJSONObject(k).optString("state")
                                    locModel.seats =
                                        if (locations.optJSONObject(k).isNull("seats")) "" else locations.optJSONObject(k).optString("seats")
                                    locModel.event_id =
                                        if (locations.optJSONObject(k).isNull("event_id")) 0 else locations.optJSONObject(k).optInt("event_id")
                                    locModel.country =
                                        if (locations.optJSONObject(k).isNull("country")) "" else locations.optJSONObject(k).optString("country")
                                    locModel.registration_open =
                                        if (locations.optJSONObject(k).isNull("registration_open")) false else locations.optJSONObject(k).optBoolean("registration_open")
                                    locModel.google_map_url =
                                        if (locations.optJSONObject(k).isNull("google_map_url")) "" else locations.optJSONObject(k).optString("google_map_url")
                                    locModel.city =
                                        if (locations.optJSONObject(k).isNull("city")) "" else locations.optJSONObject(k).optString("city")

                                    locdata.add(locModel)
                                }

                            }
                            model_events.event_locations= locdata

                            model_events.discount_start_date_time =if (json_objectdetail.isNull("discount_start_date_time")) "" else json_objectdetail.optString("discount_start_date_time")
                            model_events.discount_end_date_time =if (json_objectdetail.isNull("discount_end_date_time")) "" else json_objectdetail.optString("discount_end_date_time")
                            model_events.price_after_discount =if (json_objectdetail.isNull("price_after_discount")) 0 else json_objectdetail.optInt("price_after_discount")
                            model_events.price_before_discount =if (json_objectdetail.isNull("price_before_discount")) 0 else json_objectdetail.optInt("price_before_discount")


                            model_events.interested =if (json_objectdetail.isNull("interested")) false else json_objectdetail.optBoolean("interested")
                            model_events.registered =if (json_objectdetail.isNull("registered")) false else json_objectdetail.optBoolean("registered")
                            model_events.event_category =if (json_objectdetail.isNull("events_category")) "" else json_objectdetail.optString("events_category")

                            model_events.company_name =if (json_objectdetail.isNull("company_name")) "" else json_objectdetail.optString("company_name")
                            model_events.featured_end_date_time =if (json_objectdetail.isNull("featured_end_date_time")) "" else json_objectdetail.optString("featured_end_date_time")
                            model_events.preference_required =if (json_objectdetail.isNull("preference_required")) false else json_objectdetail.optBoolean("preference_required")
                            model_events.terms_and_conditions =if (json_objectdetail.isNull("terms_and_conditions")) "" else  json_objectdetail.optString("terms_and_conditions")
                            model_events.featured_start_date_time =if (json_objectdetail.isNull("featured_start_date_time")) "" else  json_objectdetail.optString("featured_start_date_time")
                            model_events.featured_event =if (json_objectdetail.isNull("featured_event")) false else  json_objectdetail.optBoolean("featured_event")
                            model_events.faq = if (json_objectdetail.isNull("faq")) "" else json_objectdetail.optString("faq")

                            model_events.resume_required =if (json_objectdetail.isNull("resume_required")) false else json_objectdetail.optBoolean("resume_required")
                            model_events.ticket_type =if (json_objectdetail.isNull("ticket_type")) "" else json_objectdetail.optString("ticket_type")
                            model_events.seats =if (json_objectdetail.isNull("seats")) "" else  json_objectdetail.optString("seats")
                            model_events.event_type =if (json_objectdetail.isNull("event_type")) "" else  json_objectdetail.optString("event_type")
                            model_events.display_price =if (json_objectdetail.isNull("display_price")) 0 else  json_objectdetail.optInt("display_price")
                            model_events.agenda = if (json_objectdetail.isNull("agenda")) "" else json_objectdetail.optString("agenda")

                            model_events.modified_by =if (json_objectdetail.isNull("modified_by")) "" else json_objectdetail.optString("modified_by")
                            model_events.interested_count =if (json_objectdetail.isNull("interested_count")) 0 else json_objectdetail.optInt("interested_count")
                            model_events.view_count =if (json_objectdetail.isNull("view_count")) 0 else  json_objectdetail.optInt("view_count")
                            model_events.share_count =if (json_objectdetail.isNull("share_count")) 0 else  json_objectdetail.optInt("share_count")
                            model_events.payment =if (json_objectdetail.isNull("payment")) false else  json_objectdetail.optBoolean("payment")
                            model_events.status = if (json_objectdetail.isNull("status")) "" else json_objectdetail.optString("status")

                            model_events.priority_order = if (json_objectdetail.isNull("priority_order")) false else json_objectdetail.optBoolean("priority_order")
                            model_events.excerpt =if (json_objectdetail.isNull("excerpt")) "" else json_objectdetail.optString("excerpt")
                            model_events.event_register_start_date_time =if (json_objectdetail.isNull("event_register_start_date_time")) "" else json_objectdetail.optString("event_register_start_date_time")
                            model_events.created_on =if (json_objectdetail.isNull("created_on")) "" else json_objectdetail.optString("created_on")
                            model_events.author_id =if (json_objectdetail.isNull("author_id")) "" else json_objectdetail.optString("author_id")
                            model_events.is_online =if (json_objectdetail.isNull("is_online")) false else json_objectdetail.optBoolean("is_online")

                            model_events.gtm_id =if (json_objectdetail.isNull("gtm_id")) 0 else json_objectdetail.optInt("gtm_id")
                            model_events.author_name =if (json_objectdetail.isNull("author_name")) "" else json_objectdetail.optString("author_name")
                            model_events.is_private =if (json_objectdetail.isNull("is_private")) false else json_objectdetail.optBoolean("is_private")
                            model_events.register_count =if (json_objectdetail.isNull("register_count")) "" else json_objectdetail.optString("register_count")
                            model_events.title =if (json_objectdetail.isNull("title")) "" else json_objectdetail.optString("title")
                            model_events.event_end_date_time =if (json_objectdetail.isNull("event_end_date_time")) "" else json_objectdetail.optString("event_end_date_time")

                            model_events.slug =if (json_objectdetail.isNull("slug")) "" else json_objectdetail.optString("slug")
                            model_events.event_register_end_date_time =if (json_objectdetail.isNull("event_register_end_date_time")) "" else json_objectdetail.optString("event_register_end_date_time")
                            model_events.event_by =if (json_objectdetail.isNull("event_by")) "" else json_objectdetail.optString("event_by")
                            model_events.id =if (json_objectdetail.isNull("id")) 0 else json_objectdetail.optInt("id")
                            model_events.post_for =if (json_objectdetail.isNull("post_for")) "" else json_objectdetail.optString("post_for")
                            model_events.publish_date =if (json_objectdetail.isNull("publish_date")) "" else json_objectdetail.optString("publish_date")

                            model_events.event_start_date_time =if (json_objectdetail.isNull("event_start_date_time")) ""  else json_objectdetail.optString("event_start_date_time")
                            model_events.created_by =if (json_objectdetail.isNull("created_by")) 0  else json_objectdetail.optInt("created_by")
                            model_events.company_id =if (json_objectdetail.isNull("company_id")) 0  else json_objectdetail.optInt("company_id")
                            model_events.address =if (json_objectdetail.isNull("address")) ""  else json_objectdetail.optString("address")
                            model_events.modified_on =if (json_objectdetail.isNull("modified_on")) ""  else json_objectdetail.optString("modified_on")
                            model_events.payment_note =if (json_objectdetail.isNull("payment_note")) ""  else json_objectdetail.optString("payment_note")

                            model_events.description =if (json_objectdetail.isNull("description")) "" else json_objectdetail.optString("description")
                            model_events.city =if (json_objectdetail.isNull("city")) "" else json_objectdetail.optString("city")
                            model_events.event_url =if (json_objectdetail.isNull("event_url")) "" else json_objectdetail.optString("event_url")
                            model_events.show_on_search =if (json_objectdetail.isNull("show_on_search")) false else json_objectdetail.optBoolean("show_on_search")
                            model_events.image_url =if (json_objectdetail.isNull("image_url")) "" else json_objectdetail.optString("image_url")
                            model_events.thumbnail_url =if (json_objectdetail.isNull("thumbnail_url")) "" else json_objectdetail.optString("thumbnail_url")
                            var compdata: List<String>
                            var data: ArrayList<EventCompanies> = ArrayList()
                            if(json_objectdetail.isNull("link_companies_name")){

                            }
                            else {
                                var s:String = ""
                                compdata = json_objectdetail.optString("link_companies_name").split(Regex(","),0)
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
                                    model_events.gtm_id!! ,
                                    model_events.author_name!!,
                                    model_events.is_private!! ,
                                    model_events.register_count!! ,
                                    model_events.title!!,
                                    model_events.event_end_date_time!!,
                                    model_events.slug!! ,
                                    model_events.event_register_end_date_time!! ,
                                    model_events.event_by!! ,
                                    model_events.id!! ,
                                    model_events.post_for!!,
                                    model_events.publish_date!! ,
                                    model_events.event_start_date_time!! ,
                                    model_events.created_by!! ,
                                    model_events.company_id!! ,
                                    model_events.address!!,
                                    model_events.modified_on!! ,
                                    model_events.payment_note!! ,
                                    model_events.description!! ,
                                    model_events.city!! ,
                                    model_events.event_url!! ,
                                    model_events.show_on_search!! ,
                                    model_events.image_url!!,
                                    model_events.thumbnail_url!!,
                                    model_events.link_companies_name!!
                                ))
                        }

//                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
//                        mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
//                        hotEventsAdapter = EventsAdapter(listOfEventsdata, isLoggedIn, 3,0,listOfCompareJoineddata,0,"Events")
//                        mgroupsRecyclerView!!.adapter = hotEventsAdapter

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


                    loadprev.visibility = View.GONE
//                    if (pagenoo == "1")
//                        loadprev.visibility = View.GONE
//                    else {
//                        loadprev.visibility = View.VISIBLE
//                        prev_page_no_featured = prev_page.toString()
//                    }

                    if(listOfEventsdata.size>0) {
                        val array: ArrayList<Int> = ArrayList()
                        for (i in 0 until listOfEventsdata.size) {

                            array.add(listOfEventsdata[i].id!!)
                        }
                        Log.d("TAGG", array.toString());
                        val params = HashMap<String, ArrayList<Int>>()
                        params["event_id"] = array

                        var a: String = ""
                        var b: Boolean = false
                        retrofitInterface1 = RetrofitClient.client!!.create(RetrofitInterface::class.java)

                        val call =
                            retrofitInterface1!!.GetInterest(
                                EndPoints.CLIENT_ID,
                                "Bearer " + EndPoints.ACCESS_TOKEN,
                                params
                            )

                        call.enqueue(object : Callback<GetInterestResponse> {
                            override fun onResponse(
                                call: Call<GetInterestResponse>,
                                response: Response<GetInterestResponse>
                            ) {

                                Logger.d("CODE", response.code().toString() + "")
                                Logger.d("MESSAGE", response.message() + "")
                                Logger.d("URL", "" + response)
                                Logger.d("RESPONSE follow company", "" + Gson().toJson(response))

                                if (response.isSuccessful) {
                                    // Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG) .show()
                                    for(k in 0 until array.size){
                                        Log.d("TAGG", (response.body()!!?.body!!+"  "+array[k]).toString())
                                        if(response.body()!!?.body!!.contains(array[k]))
                                            listOfEventsdata[k].interested = true
                                        else
                                            listOfEventsdata[k].interested = false
                                    }

//                                    var entityList: List<Map<String, MemberBody>> = response.body()!!.body!!
//                                    for (mapKey in 0 until entityList.size) {
//                                        Log.d("TAGG", "mapKey : " + mapKey + " , mapValue : " + entityList.get(mapKey));
//                                        for (k in 0 until array.size) {
//                                            if(entityList.get(mapKey).containsKey(listOfMyGroupdata[k].id.toString())){
//                                                val jsonObj: JSONObject =
//                                                    JSONObject(entityList.get(mapKey).get(listOfMyGroupdata[k].id.toString()).toString())
//                                                Log.d("TAGG", jsonObj.optString("member_count"))
//                                                a = jsonObj.optString("member_count").toString()
//                                                    .substring(0, jsonObj.optString("member_count").length - 2)
//                                                listOfMyGroupdata[k].noOfMembers = a
//                                            }
//                                            else{
//                                                listOfMyGroupdata[k].noOfMembers = "0"
//                                            }
//                                        }
//                                    }

                                    // listOfMyGroupdata.add(listOfPostdataDump[i])
                                    var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                                    mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                                    hotEventsAdapter = EventsAdapter(listOfEventsdata, isLoggedIn, 1,0,listOfCompareJoineddata,0,"Events")
                                    mgroupsRecyclerView!!.adapter = hotEventsAdapter

                                } else {
                                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                                    var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                                    mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                                    hotEventsAdapter = EventsAdapter(listOfEventsdata, isLoggedIn, 1,0,listOfCompareJoineddata,0,"Events")
                                    mgroupsRecyclerView!!.adapter = hotEventsAdapter
                                }
                            }

                            override fun onFailure(call: Call<GetInterestResponse>, t: Throwable) {
                                Logger.d("TAGG", "Apply Job FAILED : $t")
                                var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                                mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                                hotEventsAdapter = EventsAdapter(listOfEventsdata, isLoggedIn, 1,0,listOfCompareJoineddata,0,"Events")
                                mgroupsRecyclerView!!.adapter = hotEventsAdapter
                            }
                        })
                    }
                    else{
                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                        mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                        hotEventsAdapter = EventsAdapter(listOfEventsdata, isLoggedIn, 3,0,listOfCompareJoineddata,0,"Events")
                        mgroupsRecyclerView!!.adapter = hotEventsAdapter
                    }
                }
                else
                    Toast.makeText(applicationContext,"No data exists",Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<Event>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")

            }
        })
    }

    fun loadpastEventsData(pageno:String){

        if (pageno.equals("1")) {
            listOfEventsdata.clear()
            listOfCompareRecdata.clear()
            hotEventsAdapter!!.notifyDataSetChanged()
        }
        val params = java.util.HashMap<String, String>()

        params["page_no"] = pageno.toString()
        params["page_size"] = Constants.MAXIMUM_PAGINATION_COUNT
        params["payment_type"] = "both"

        retrofitInterface1 = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface1!!.getpastEventsData(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        call.enqueue(object : Callback<Event> {
            override fun onResponse(call: Call<Event>, response: Response<Event>) {

                Logger.d("URL", "Applied" + response+ EndPoints.CLIENT_ID)
                Logger.d("CODE", "Applied"+response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "Applied OnClick" + Gson().toJson(response))

                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))

                var jsonObject1 : JSONObject? = null
                var jsonarray_info: JSONArray? = null

                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    jsonObject1 = jsonObject.optJSONObject("body")
                }
                if (jsonObject1 != null) {
                    val responseCode: Int = jsonObject1.optInt("response_code")
                    jsonarray_info = jsonObject1.optJSONArray("body")
                    var jsonarray_pagination: JSONObject = jsonObject1.optJSONObject("pagination")

                    val hasnextRecommendedJobs = jsonarray_pagination.optBoolean("has_next")
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

                    default_events.visibility = View.GONE
                    updatepref.visibility = View.GONE
                    mgroupsRecyclerView!!.visibility = View.VISIBLE

                    for(k in 0 until response.body()!!.body!!.size) {
                        var json_objectdetail: JSONObject = jsonarray_info?.optJSONObject(k)!!
                        val model_events: EventsView = EventsView();

                        var locations: JSONArray
                        var locdata: ArrayList<EventLocation> = ArrayList()
                        if(json_objectdetail.isNull("events_locations")){}
                        else {
                            locations = json_objectdetail.optJSONArray("events_locations")

                            for (k in 0 until locations.length()) {
                                val locModel: EventLocation = EventLocation()
                                locModel.pincode =
                                    if (locations.optJSONObject(k).isNull("pincode")) "" else locations.optJSONObject(k).optString("pincode")
                                locModel.discounted_price =
                                    if (locations.optJSONObject(k).isNull("discounted_price")) 0 else locations.optJSONObject(k).optInt("discounted_price")
                                locModel.event_register_start_date_time =
                                    if (locations.optJSONObject(k).isNull("event_register_start_date_time")) "" else locations.optJSONObject(k).optString("event_register_start_date_time")
                                locModel.event_type =
                                    if (locations.optJSONObject(k).isNull("event_type")) "" else locations.optJSONObject(k).optString("event_type")
                                locModel.event_start_date_time =
                                    if (locations.optJSONObject(k).isNull("event_start_date_time")) "" else locations.optJSONObject(k).optString("event_start_date_time")
                                locModel.amount =
                                    if (locations.optJSONObject(k).isNull("amount")) 0 else locations.optJSONObject(k).optInt("amount")
                                locModel.discount_start_date_time =
                                    if (locations.optJSONObject(k).isNull("discount_start_date_time")) "" else locations.optJSONObject(k).optString("discount_start_date_time")
                                locModel.event_register_end_date_time =
                                    if (locations.optJSONObject(k).isNull("event_register_end_date_time")) "" else locations.optJSONObject(k).optString("event_register_end_date_time")
                                locModel.discount_end_date_time =
                                    if (locations.optJSONObject(k).isNull("discount_end_date_time")) "" else locations.optJSONObject(k).optString("discount_end_date_time")
                                locModel.discount_active =
                                    if (locations.optJSONObject(k).isNull("discount_active")) "" else locations.optJSONObject(k).optString("discount_active")
                                locModel.address =
                                    if (locations.optJSONObject(k).isNull("address")) "" else locations.optJSONObject(k).optString("address")
                                locModel.event_end_date_time =
                                    if (locations.optJSONObject(k).isNull("event_end_date_time")) "" else locations.optJSONObject(k).optString("event_end_date_time")

                                locModel.id =
                                    if (locations.optJSONObject(k).isNull("id")) 0 else locations.optJSONObject(k).optInt("id")
                                locModel.state =
                                    if (locations.optJSONObject(k).isNull("state")) "" else locations.optJSONObject(k).optString("state")
                                locModel.seats =
                                    if (locations.optJSONObject(k).isNull("seats")) "" else locations.optJSONObject(k).optString("seats")
                                locModel.event_id =
                                    if (locations.optJSONObject(k).isNull("event_id")) 0 else locations.optJSONObject(k).optInt("event_id")
                                locModel.country =
                                    if (locations.optJSONObject(k).isNull("country")) "" else locations.optJSONObject(k).optString("country")
                                locModel.registration_open =
                                    if (locations.optJSONObject(k).isNull("registration_open")) false else locations.optJSONObject(k).optBoolean("registration_open")
                                locModel.google_map_url =
                                    if (locations.optJSONObject(k).isNull("google_map_url")) "" else locations.optJSONObject(k).optString("google_map_url")
                                locModel.city =
                                    if (locations.optJSONObject(k).isNull("city")) "" else locations.optJSONObject(k).optString("city")

                                locdata.add(locModel)
                            }

                        }
                        model_events.event_locations= locdata

                        model_events.discount_start_date_time =if (json_objectdetail.isNull("discount_start_date_time")) "" else json_objectdetail.optString("discount_start_date_time")
                        model_events.discount_end_date_time =if (json_objectdetail.isNull("discount_end_date_time")) "" else json_objectdetail.optString("discount_end_date_time")
                        model_events.price_after_discount =if (json_objectdetail.isNull("price_after_discount")) 0 else json_objectdetail.optInt("price_after_discount")
                        model_events.price_before_discount =if (json_objectdetail.isNull("price_before_discount")) 0 else json_objectdetail.optInt("price_before_discount")


                        model_events.interested =if (json_objectdetail.isNull("interested")) false else json_objectdetail.optBoolean("interested")
                        model_events.registered =if (json_objectdetail.isNull("registered")) false else json_objectdetail.optBoolean("registered")
                        model_events.event_category =if (json_objectdetail.isNull("events_category")) "" else json_objectdetail.optString("events_category")

                        model_events.company_name =if (json_objectdetail.isNull("company_name")) "" else json_objectdetail.optString("company_name")
                        model_events.featured_end_date_time =if (json_objectdetail.isNull("featured_end_date_time")) "" else json_objectdetail.optString("featured_end_date_time")
                        model_events.preference_required =if (json_objectdetail.isNull("preference_required")) false else json_objectdetail.optBoolean("preference_required")
                        model_events.terms_and_conditions =if (json_objectdetail.isNull("terms_and_conditions")) "" else  json_objectdetail.optString("terms_and_conditions")
                        model_events.featured_start_date_time =if (json_objectdetail.isNull("featured_start_date_time")) "" else  json_objectdetail.optString("featured_start_date_time")
                        model_events.featured_event =if (json_objectdetail.isNull("featured_event")) false else  json_objectdetail.optBoolean("featured_event")
                        model_events.faq = if (json_objectdetail.isNull("faq")) "" else json_objectdetail.optString("faq")

                        model_events.resume_required =if (json_objectdetail.isNull("resume_required")) false else json_objectdetail.optBoolean("resume_required")
                        model_events.ticket_type =if (json_objectdetail.isNull("ticket_type")) "" else json_objectdetail.optString("ticket_type")
                        model_events.seats =if (json_objectdetail.isNull("seats")) "" else  json_objectdetail.optString("seats")
                        model_events.event_type =if (json_objectdetail.isNull("event_type")) "" else  json_objectdetail.optString("event_type")
                        model_events.display_price =if (json_objectdetail.isNull("display_price")) 0 else  json_objectdetail.optInt("display_price")
                        model_events.agenda = if (json_objectdetail.isNull("agenda")) "" else json_objectdetail.optString("agenda")


                        model_events.modified_by =if (json_objectdetail.isNull("modified_by")) "" else json_objectdetail.optString("modified_by")
                        model_events.interested_count =if (json_objectdetail.isNull("interested_count")) 0 else json_objectdetail.optInt("interested_count")
                        model_events.view_count =if (json_objectdetail.isNull("view_count")) 0 else  json_objectdetail.optInt("view_count")
                        model_events.share_count =if (json_objectdetail.isNull("share_count")) 0 else  json_objectdetail.optInt("share_count")
                        model_events.payment =if (json_objectdetail.isNull("payment")) false else  json_objectdetail.optBoolean("payment")
                        model_events.status = if (json_objectdetail.isNull("status")) "" else json_objectdetail.optString("status")

                        model_events.priority_order = if (json_objectdetail.isNull("priority_order")) false else json_objectdetail.optBoolean("priority_order")
                        model_events.excerpt =if (json_objectdetail.isNull("excerpt")) "" else json_objectdetail.optString("excerpt")
                        model_events.event_register_start_date_time =if (json_objectdetail.isNull("event_register_start_date_time")) "" else json_objectdetail.optString("event_register_start_date_time")
                        model_events.created_on =if (json_objectdetail.isNull("created_on")) "" else json_objectdetail.optString("created_on")
                        model_events.author_id =if (json_objectdetail.isNull("author_id")) "" else json_objectdetail.optString("author_id")
                        model_events.is_online =if (json_objectdetail.isNull("is_online")) false else json_objectdetail.optBoolean("is_online")

                        model_events.gtm_id =if (json_objectdetail.isNull("gtm_id")) 0 else json_objectdetail.optInt("gtm_id")
                        model_events.author_name =if (json_objectdetail.isNull("author_name")) "" else json_objectdetail.optString("author_name")
                        model_events.is_private =if (json_objectdetail.isNull("is_private")) false else json_objectdetail.optBoolean("is_private")
                        model_events.register_count =if (json_objectdetail.isNull("register_count")) "" else json_objectdetail.optString("register_count")
                        model_events.title =if (json_objectdetail.isNull("title")) "" else json_objectdetail.optString("title")
                        model_events.event_end_date_time =if (json_objectdetail.isNull("event_end_date_time")) "" else json_objectdetail.optString("event_end_date_time")

                        model_events.slug =if (json_objectdetail.isNull("slug")) "" else json_objectdetail.optString("slug")
                        model_events.event_register_end_date_time =if (json_objectdetail.isNull("event_register_end_date_time")) "" else json_objectdetail.optString("event_register_end_date_time")
                        model_events.event_by =if (json_objectdetail.isNull("event_by")) "" else json_objectdetail.optString("event_by")
                        model_events.id =if (json_objectdetail.isNull("id")) 0 else json_objectdetail.optInt("id")
                        model_events.post_for =if (json_objectdetail.isNull("post_for")) "" else json_objectdetail.optString("post_for")
                        model_events.publish_date =if (json_objectdetail.isNull("publish_date")) "" else json_objectdetail.optString("publish_date")

                        model_events.event_start_date_time =if (json_objectdetail.isNull("event_start_date_time")) ""  else json_objectdetail.optString("event_start_date_time")
                        model_events.created_by =if (json_objectdetail.isNull("created_by")) 0  else json_objectdetail.optInt("created_by")
                        model_events.company_id =if (json_objectdetail.isNull("company_id")) 0  else json_objectdetail.optInt("company_id")
                        model_events.address =if (json_objectdetail.isNull("address")) ""  else json_objectdetail.optString("address")
                        model_events.modified_on =if (json_objectdetail.isNull("modified_on")) ""  else json_objectdetail.optString("modified_on")
                        model_events.payment_note =if (json_objectdetail.isNull("payment_note")) ""  else json_objectdetail.optString("payment_note")

                        model_events.description =if (json_objectdetail.isNull("description")) "" else json_objectdetail.optString("description")
                        model_events.city =if (json_objectdetail.isNull("city")) "" else json_objectdetail.optString("city")
                        model_events.event_url =if (json_objectdetail.isNull("event_url")) "" else json_objectdetail.optString("event_url")
                        model_events.show_on_search =if (json_objectdetail.isNull("show_on_search")) false else json_objectdetail.optBoolean("show_on_search")
                        model_events.image_url =if (json_objectdetail.isNull("image_url")) "" else json_objectdetail.optString("image_url")
                        model_events.thumbnail_url =if (json_objectdetail.isNull("thumbnail_url")) "" else json_objectdetail.optString("thumbnail_url")
                        var compdata: List<String>
                        var data: ArrayList<EventCompanies> = ArrayList()
                        if(json_objectdetail.isNull("link_companies_name")){

                        }
                        else {
                            var s:String = ""
                            compdata = json_objectdetail.optString("link_companies_name").split(Regex(","),0)
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
                                model_events.gtm_id!! ,
                                model_events.author_name!!,
                                model_events.is_private!! ,
                                model_events.register_count!! ,
                                model_events.title!!,
                                model_events.event_end_date_time!!,
                                model_events.slug!! ,
                                model_events.event_register_end_date_time!! ,
                                model_events.event_by!! ,
                                model_events.id!! ,
                                model_events.post_for!!,
                                model_events.publish_date!! ,
                                model_events.event_start_date_time!! ,
                                model_events.created_by!! ,
                                model_events.company_id!! ,
                                model_events.address!!,
                                model_events.modified_on!! ,
                                model_events.payment_note!! ,
                                model_events.description!! ,
                                model_events.city!! ,
                                model_events.event_url!! ,
                                model_events.show_on_search!! ,
                                model_events.image_url!!,
                                model_events.thumbnail_url!!,
                                model_events.link_companies_name!!
                            ))
                    }

//                    if (listOfAppliedJobsdata.size>3) {
//                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
//                        mRecyclerViewPosts!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
//                        mAdapterJobs = JobsAdapter(listOfAppliedJobsdataTrimmed, isLoggedIn, 2, 0, listOfCompareJoineddata,1)
//                        mRecyclerViewPosts!!.adapter = mAdapterJobs
//                    }
//                    else{

                    var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                    mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                    hotEventsAdapter = EventsAdapter(listOfEventsdata, isLoggedIn, 2, 0, listOfCompareRecdata,0,"Events")
                    mgroupsRecyclerView!!.adapter = hotEventsAdapter
//                    }

                } else {
                    jsonObject1?.optString("message")?.let {
                        ToastHelper.makeToast(applicationContext,
                            it
                        )
                    }
                }
            }

            override fun onFailure(call: Call<Event>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")

            }
        })
    }

    fun loadrecommendedEventsData(pageno:String){

        if (pageno.equals("1")) {
            listOfEventsdata.clear()
            listOfCompareRecdata.clear()
            hotEventsAdapter!!.notifyDataSetChanged()
        }
        val params = java.util.HashMap<String, String>()

        params["page_no"] = pageno.toString()
        params["page_size"] = Constants.MAXIMUM_PAGINATION_COUNT
        params["payment_type"] = "both"

        retrofitInterface1 = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface1!!.getRecommendedEvents(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        call.enqueue(object : Callback<Event> {
            override fun onResponse(call: Call<Event>, response: Response<Event>) {

                Logger.d("URL", "Applied" + response+ EndPoints.CLIENT_ID)
                Logger.d("CODE", "Applied"+response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "Recommended" + Gson().toJson(response))

                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))

                var jsonObject1 : JSONObject? = null
                var jsonarray_info: JSONArray? = null
                
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    jsonObject1 = jsonObject.optJSONObject("body")
                }
                if (jsonObject1 != null) {
                    jsonarray_info = jsonObject1.optJSONArray("body")
                    var jsonarray_pagination: JSONObject = jsonObject1.optJSONObject("pagination")
                    val hasnextRecommendedJobs = jsonarray_pagination.optBoolean("has_next")
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

                    default_events.visibility = View.GONE
                    updatepref.visibility = View.GONE
                    mgroupsRecyclerView!!.visibility = View.VISIBLE

                    for(k in 0 until response.body()!!.body!!.size) {
                        var json_objectdetail: JSONObject = jsonarray_info?.optJSONObject(k)!!
                        val model_events: EventsView = EventsView();

                        var locations: JSONArray
                        var locdata: ArrayList<EventLocation> = ArrayList()
                        if(json_objectdetail.isNull("events_locations")){}
                        else {
                            locations = json_objectdetail.optJSONArray("events_locations")

                            for (k in 0 until locations.length()) {
                                val locModel: EventLocation = EventLocation()
                                locModel.pincode =
                                    if (locations.optJSONObject(k).isNull("pincode")) "" else locations.optJSONObject(k).optString("pincode")
                                locModel.discounted_price =
                                    if (locations.optJSONObject(k).isNull("discounted_price")) 0 else locations.optJSONObject(k).optInt("discounted_price")
                                locModel.event_register_start_date_time =
                                    if (locations.optJSONObject(k).isNull("event_register_start_date_time")) "" else locations.optJSONObject(k).optString("event_register_start_date_time")
                                locModel.event_type =
                                    if (locations.optJSONObject(k).isNull("event_type")) "" else locations.optJSONObject(k).optString("event_type")
                                locModel.event_start_date_time =
                                    if (locations.optJSONObject(k).isNull("event_start_date_time")) "" else locations.optJSONObject(k).optString("event_start_date_time")
                                locModel.amount =
                                    if (locations.optJSONObject(k).isNull("amount")) 0 else locations.optJSONObject(k).optInt("amount")
                                locModel.discount_start_date_time =
                                    if (locations.optJSONObject(k).isNull("discount_start_date_time")) "" else locations.optJSONObject(k).optString("discount_start_date_time")
                                locModel.event_register_end_date_time =
                                    if (locations.optJSONObject(k).isNull("event_register_end_date_time")) "" else locations.optJSONObject(k).optString("event_register_end_date_time")
                                locModel.discount_end_date_time =
                                    if (locations.optJSONObject(k).isNull("discount_end_date_time")) "" else locations.optJSONObject(k).optString("discount_end_date_time")
                                locModel.discount_active =
                                    if (locations.optJSONObject(k).isNull("discount_active")) "" else locations.optJSONObject(k).optString("discount_active")
                                locModel.address =
                                    if (locations.optJSONObject(k).isNull("address")) "" else locations.optJSONObject(k).optString("address")
                                locModel.event_end_date_time =
                                    if (locations.optJSONObject(k).isNull("event_end_date_time")) "" else locations.optJSONObject(k).optString("event_end_date_time")

                                locModel.id =
                                    if (locations.optJSONObject(k).isNull("id")) 0 else locations.optJSONObject(k).optInt("id")
                                locModel.state =
                                    if (locations.optJSONObject(k).isNull("state")) "" else locations.optJSONObject(k).optString("state")
                                locModel.seats =
                                    if (locations.optJSONObject(k).isNull("seats")) "" else locations.optJSONObject(k).optString("seats")
                                locModel.event_id =
                                    if (locations.optJSONObject(k).isNull("event_id")) 0 else locations.optJSONObject(k).optInt("event_id")
                                locModel.country =
                                    if (locations.optJSONObject(k).isNull("country")) "" else locations.optJSONObject(k).optString("country")
                                locModel.registration_open =
                                    if (locations.optJSONObject(k).isNull("registration_open")) false else locations.optJSONObject(k).optBoolean("registration_open")
                                locModel.google_map_url =
                                    if (locations.optJSONObject(k).isNull("google_map_url")) "" else locations.optJSONObject(k).optString("google_map_url")
                                locModel.city =
                                    if (locations.optJSONObject(k).isNull("city")) "" else locations.optJSONObject(k).optString("city")

                                locdata.add(locModel)
                            }

                        }
                        model_events.event_locations= locdata

                        model_events.discount_start_date_time =if (json_objectdetail.isNull("discount_start_date_time")) "" else json_objectdetail.optString("discount_start_date_time")
                        model_events.discount_end_date_time =if (json_objectdetail.isNull("discount_end_date_time")) "" else json_objectdetail.optString("discount_end_date_time")
                        model_events.price_after_discount =if (json_objectdetail.isNull("price_after_discount")) 0 else json_objectdetail.optInt("price_after_discount")
                        model_events.price_before_discount =if (json_objectdetail.isNull("price_before_discount")) 0 else json_objectdetail.optInt("price_before_discount")


                        model_events.interested =if (json_objectdetail.isNull("interested")) false else json_objectdetail.optBoolean("interested")
                        model_events.registered =if (json_objectdetail.isNull("registered")) false else json_objectdetail.optBoolean("registered")
                        model_events.event_category =if (json_objectdetail.isNull("events_category")) "" else json_objectdetail.optString("events_category")

                        model_events.company_name =if (json_objectdetail.isNull("company_name")) "" else json_objectdetail.optString("company_name")
                        model_events.featured_end_date_time =if (json_objectdetail.isNull("featured_end_date_time")) "" else json_objectdetail.optString("featured_end_date_time")
                        model_events.preference_required =if (json_objectdetail.isNull("preference_required")) false else json_objectdetail.optBoolean("preference_required")
                        model_events.terms_and_conditions =if (json_objectdetail.isNull("terms_and_conditions")) "" else  json_objectdetail.optString("terms_and_conditions")
                        model_events.featured_start_date_time =if (json_objectdetail.isNull("featured_start_date_time")) "" else  json_objectdetail.optString("featured_start_date_time")
                        model_events.featured_event =if (json_objectdetail.isNull("featured_event")) false else  json_objectdetail.optBoolean("featured_event")
                        model_events.faq = if (json_objectdetail.isNull("faq")) "" else json_objectdetail.optString("faq")

                        model_events.resume_required =if (json_objectdetail.isNull("resume_required")) false else json_objectdetail.optBoolean("resume_required")
                        model_events.ticket_type =if (json_objectdetail.isNull("ticket_type")) "" else json_objectdetail.optString("ticket_type")
                        model_events.seats =if (json_objectdetail.isNull("seats")) "" else  json_objectdetail.optString("seats")
                        model_events.event_type =if (json_objectdetail.isNull("event_type")) "" else  json_objectdetail.optString("event_type")
                        model_events.display_price =if (json_objectdetail.isNull("display_price")) 0 else  json_objectdetail.optInt("display_price")
                        model_events.agenda = if (json_objectdetail.isNull("agenda")) "" else json_objectdetail.optString("agenda")


                        model_events.modified_by =if (json_objectdetail.isNull("modified_by")) "" else json_objectdetail.optString("modified_by")
                        model_events.interested_count =if (json_objectdetail.isNull("interested_count")) 0 else json_objectdetail.optInt("interested_count")
                        model_events.view_count =if (json_objectdetail.isNull("view_count")) 0 else  json_objectdetail.optInt("view_count")
                        model_events.share_count =if (json_objectdetail.isNull("share_count")) 0 else  json_objectdetail.optInt("share_count")
                        model_events.payment =if (json_objectdetail.isNull("payment")) false else  json_objectdetail.optBoolean("payment")
                        model_events.status = if (json_objectdetail.isNull("status")) "" else json_objectdetail.optString("status")

                        model_events.priority_order = if (json_objectdetail.isNull("priority_order")) false else json_objectdetail.optBoolean("priority_order")
                        model_events.excerpt =if (json_objectdetail.isNull("excerpt")) "" else json_objectdetail.optString("excerpt")
                        model_events.event_register_start_date_time =if (json_objectdetail.isNull("event_register_start_date_time")) "" else json_objectdetail.optString("event_register_start_date_time")
                        model_events.created_on =if (json_objectdetail.isNull("created_on")) "" else json_objectdetail.optString("created_on")
                        model_events.author_id =if (json_objectdetail.isNull("author_id")) "" else json_objectdetail.optString("author_id")
                        model_events.is_online =if (json_objectdetail.isNull("is_online")) false else json_objectdetail.optBoolean("is_online")

                        model_events.gtm_id =if (json_objectdetail.isNull("gtm_id")) 0 else json_objectdetail.optInt("gtm_id")
                        model_events.author_name =if (json_objectdetail.isNull("author_name")) "" else json_objectdetail.optString("author_name")
                        model_events.is_private =if (json_objectdetail.isNull("is_private")) false else json_objectdetail.optBoolean("is_private")
                        model_events.register_count =if (json_objectdetail.isNull("register_count")) "" else json_objectdetail.optString("register_count")
                        model_events.title =if (json_objectdetail.isNull("title")) "" else json_objectdetail.optString("title")
                        model_events.event_end_date_time =if (json_objectdetail.isNull("event_end_date_time")) "" else json_objectdetail.optString("event_end_date_time")

                        model_events.slug =if (json_objectdetail.isNull("slug")) "" else json_objectdetail.optString("slug")
                        model_events.event_register_end_date_time =if (json_objectdetail.isNull("event_register_end_date_time")) "" else json_objectdetail.optString("event_register_end_date_time")
                        model_events.event_by =if (json_objectdetail.isNull("event_by")) "" else json_objectdetail.optString("event_by")
                        model_events.id =if (json_objectdetail.isNull("id")) 0 else json_objectdetail.optInt("id")
                        model_events.post_for =if (json_objectdetail.isNull("post_for")) "" else json_objectdetail.optString("post_for")
                        model_events.publish_date =if (json_objectdetail.isNull("publish_date")) "" else json_objectdetail.optString("publish_date")

                        model_events.event_start_date_time =if (json_objectdetail.isNull("event_start_date_time")) ""  else json_objectdetail.optString("event_start_date_time")
                        model_events.created_by =if (json_objectdetail.isNull("created_by")) 0  else json_objectdetail.optInt("created_by")
                        model_events.company_id =if (json_objectdetail.isNull("company_id")) 0  else json_objectdetail.optInt("company_id")
                        model_events.address =if (json_objectdetail.isNull("address")) ""  else json_objectdetail.optString("address")
                        model_events.modified_on =if (json_objectdetail.isNull("modified_on")) ""  else json_objectdetail.optString("modified_on")
                        model_events.payment_note =if (json_objectdetail.isNull("payment_note")) ""  else json_objectdetail.optString("payment_note")

                        model_events.description =if (json_objectdetail.isNull("description")) "" else json_objectdetail.optString("description")
                        model_events.city =if (json_objectdetail.isNull("city")) "" else json_objectdetail.optString("city")
                        model_events.event_url =if (json_objectdetail.isNull("event_url")) "" else json_objectdetail.optString("event_url")
                        model_events.show_on_search =if (json_objectdetail.isNull("show_on_search")) false else json_objectdetail.optBoolean("show_on_search")
                        model_events.image_url =if (json_objectdetail.isNull("image_url")) "" else json_objectdetail.optString("image_url")
                        model_events.thumbnail_url =if (json_objectdetail.isNull("thumbnail_url")) "" else json_objectdetail.optString("thumbnail_url")
                        var compdata: List<String>
                        var data: ArrayList<EventCompanies> = ArrayList()
                        if(json_objectdetail.isNull("link_companies_name")){

                        }
                        else {
                            var s:String = ""
                            compdata = json_objectdetail.optString("link_companies_name").split(Regex(","),0)
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
                                model_events.gtm_id!! ,
                                model_events.author_name!!,
                                model_events.is_private!! ,
                                model_events.register_count!! ,
                                model_events.title!!,
                                model_events.event_end_date_time!!,
                                model_events.slug!! ,
                                model_events.event_register_end_date_time!! ,
                                model_events.event_by!! ,
                                model_events.id!! ,
                                model_events.post_for!!,
                                model_events.publish_date!! ,
                                model_events.event_start_date_time!! ,
                                model_events.created_by!! ,
                                model_events.company_id!! ,
                                model_events.address!!,
                                model_events.modified_on!! ,
                                model_events.payment_note!! ,
                                model_events.description!! ,
                                model_events.city!! ,
                                model_events.event_url!! ,
                                model_events.show_on_search!! ,
                                model_events.image_url!!,
                                model_events.thumbnail_url!!,
                                model_events.link_companies_name!!
                            ))
                    }

//                    if (listOfAppliedJobsdata.size>3) {
//                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
//                        mRecyclerViewPosts!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
//                        mAdapterJobs = JobsAdapter(listOfAppliedJobsdataTrimmed, isLoggedIn, 2, 0, listOfCompareJoineddata,1)
//                        mRecyclerViewPosts!!.adapter = mAdapterJobs
//                    }
//                    else{

                    var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                    mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                    hotEventsAdapter = EventsAdapter(listOfEventsdata, isLoggedIn, 1, 0, listOfCompareRecdata,1,"Events")
                    mgroupsRecyclerView!!.adapter = hotEventsAdapter
//                    }

                } else {
                    jsonObject1?.optString("message")?.let {
                        ToastHelper.makeToast(applicationContext,
                            it
                        )
                    }
                }
            }

            override fun onFailure(call: Call<Event>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")

            }
        })
    }

    fun loadAppliedJobs(){    //sneha

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

//
        val params = java.util.HashMap<String, String>()
        params["page_no"] = "1"

        listOfCompareJoineddata.clear()
        retrofitInterface1 = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface1!!.getAppliedEvents(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        call.enqueue(object : Callback<EventsResponse> {
            override fun onResponse(call: Call<EventsResponse>, response: Response<EventsResponse>) {

                Logger.d("URL", "Applied" + response+ EndPoints.ACCESS_TOKEN)
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

                        for (l in 0 until jsonarray_info!!.length()) {
                            listOfCompareJoineddata.add(jsonarray_info[l] as Int)

                        }
                        loadhotEventsData("1")

                    } else {
                        ToastHelper.makeToast(applicationContext, message)
                    }
                }
            }

            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                loadhotEventsData("1")
            }
        })
    }

    fun loadhotEventsData(pageno:String){

        if (pageno.equals("1")) {
            listOfEventsdata.clear()
            hotEventsAdapter!!.notifyDataSetChanged()
        }
        else{}
        val params = HashMap<String, String>()

        params["page_no"] = pageno.toString()
        params["page_size"] = Constants.MAXIMUM_PAGINATION_COUNT

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface!!.gethotEventsData( EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        call.enqueue(object : Callback<Event> {
            override fun onResponse(call: Call<Event>, response: Response<Event>) {

                Logger.d("URL HOT JOBS", "" + response+ EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE HOT JOBS", response.message() + "")
                Logger.d("RESPONSE HOT JOBS", "" + Gson().toJson(response))
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)

                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                Log.d("TAGG", "Outside body")
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {

                    Log.d("TAGG", "Inside body")
                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")

                    var jsonarray_info: JSONArray = jsonObject1.optJSONArray("body")

                    var jsonarray_pagination: JSONObject = jsonObject1.optJSONObject("pagination")

                    val has_next: String = jsonarray_pagination.optBoolean("has_next").toString()

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

                        default_events.visibility = View.GONE
                        updatepref.visibility = View.GONE
                        mgroupsRecyclerView!!.visibility = View.VISIBLE

                        // ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                        Log.d("TAGG", "Inside response success")
                        for (i in 0 until response.body()!!.body!!.size) {
                            Log.d("TAGG", "Inside response body")
                            var json_objectdetail: JSONObject = jsonarray_info.optJSONObject(i)

                            val model_events: EventsView = EventsView();

                            var locations: JSONArray
                            var locdata: ArrayList<EventLocation> = ArrayList()
                            if(json_objectdetail.isNull("events_locations")){}
                            else {
                                locations = json_objectdetail.optJSONArray("events_locations")

                                for (k in 0 until locations.length()) {
                                    val locModel: EventLocation = EventLocation()
                                    locModel.pincode =
                                        if (locations.optJSONObject(k).isNull("pincode")) "" else locations.optJSONObject(k).optString("pincode")
                                    locModel.discounted_price =
                                        if (locations.optJSONObject(k).isNull("discounted_price")) 0 else locations.optJSONObject(k).optInt("discounted_price")
                                    locModel.event_register_start_date_time =
                                        if (locations.optJSONObject(k).isNull("event_register_start_date_time")) "" else locations.optJSONObject(k).optString("event_register_start_date_time")
                                    locModel.event_type =
                                        if (locations.optJSONObject(k).isNull("event_type")) "" else locations.optJSONObject(k).optString("event_type")
                                    locModel.event_start_date_time =
                                        if (locations.optJSONObject(k).isNull("event_start_date_time")) "" else locations.optJSONObject(k).optString("event_start_date_time")
                                    locModel.amount =
                                        if (locations.optJSONObject(k).isNull("amount")) 0 else locations.optJSONObject(k).optInt("amount")
                                    locModel.discount_start_date_time =
                                        if (locations.optJSONObject(k).isNull("discount_start_date_time")) "" else locations.optJSONObject(k).optString("discount_start_date_time")
                                    locModel.event_register_end_date_time =
                                        if (locations.optJSONObject(k).isNull("event_register_end_date_time")) "" else locations.optJSONObject(k).optString("event_register_end_date_time")
                                    locModel.discount_end_date_time =
                                        if (locations.optJSONObject(k).isNull("discount_end_date_time")) "" else locations.optJSONObject(k).optString("discount_end_date_time")
                                    locModel.discount_active =
                                        if (locations.optJSONObject(k).isNull("discount_active")) "" else locations.optJSONObject(k).optString("discount_active")
                                    locModel.address =
                                        if (locations.optJSONObject(k).isNull("address")) "" else locations.optJSONObject(k).optString("address")
                                    locModel.event_end_date_time =
                                        if (locations.optJSONObject(k).isNull("event_end_date_time")) "" else locations.optJSONObject(k).optString("event_end_date_time")

                                    locModel.id =
                                        if (locations.optJSONObject(k).isNull("id")) 0 else locations.optJSONObject(k).optInt("id")
                                    locModel.state =
                                        if (locations.optJSONObject(k).isNull("state")) "" else locations.optJSONObject(k).optString("state")
                                    locModel.seats =
                                        if (locations.optJSONObject(k).isNull("seats")) "" else locations.optJSONObject(k).optString("seats")
                                    locModel.event_id =
                                        if (locations.optJSONObject(k).isNull("event_id")) 0 else locations.optJSONObject(k).optInt("event_id")
                                    locModel.country =
                                        if (locations.optJSONObject(k).isNull("country")) "" else locations.optJSONObject(k).optString("country")
                                    locModel.registration_open =
                                        if (locations.optJSONObject(k).isNull("registration_open")) false else locations.optJSONObject(k).optBoolean("registration_open")
                                    locModel.google_map_url =
                                        if (locations.optJSONObject(k).isNull("google_map_url")) "" else locations.optJSONObject(k).optString("google_map_url")
                                    locModel.city =
                                        if (locations.optJSONObject(k).isNull("city")) "" else locations.optJSONObject(k).optString("city")

                                    locdata.add(locModel)

                                }

                            }
                            model_events.event_locations= locdata

                            model_events.discount_start_date_time =if (json_objectdetail.isNull("discount_start_date_time")) "" else json_objectdetail.optString("discount_start_date_time")
                            model_events.discount_end_date_time =if (json_objectdetail.isNull("discount_end_date_time")) "" else json_objectdetail.optString("discount_end_date_time")
                            model_events.price_after_discount =if (json_objectdetail.isNull("price_after_discount")) 0 else json_objectdetail.optInt("price_after_discount")
                            model_events.price_before_discount =if (json_objectdetail.isNull("price_before_discount")) 0 else json_objectdetail.optInt("price_before_discount")


                            model_events.interested =if (json_objectdetail.isNull("interested")) false else json_objectdetail.optBoolean("interested")
                            model_events.registered =if (json_objectdetail.isNull("registered")) false else json_objectdetail.optBoolean("registered")
                            model_events.event_category =if (json_objectdetail.isNull("events_category")) "" else json_objectdetail.optString("events_category")

                            model_events.company_name =if (json_objectdetail.isNull("company_name")) "" else json_objectdetail.optString("company_name")
                            model_events.featured_end_date_time =if (json_objectdetail.isNull("featured_end_date_time")) "" else json_objectdetail.optString("featured_end_date_time")
                            model_events.preference_required =if (json_objectdetail.isNull("preference_required")) false else json_objectdetail.optBoolean("preference_required")
                            model_events.terms_and_conditions =if (json_objectdetail.isNull("terms_and_conditions")) "" else  json_objectdetail.optString("terms_and_conditions")
                            model_events.featured_start_date_time =if (json_objectdetail.isNull("featured_start_date_time")) "" else  json_objectdetail.optString("featured_start_date_time")
                            model_events.featured_event =if (json_objectdetail.isNull("featured_event")) false else  json_objectdetail.optBoolean("featured_event")
                            model_events.faq = if (json_objectdetail.isNull("faq")) "" else json_objectdetail.optString("faq")

                            model_events.resume_required =if (json_objectdetail.isNull("resume_required")) false else json_objectdetail.optBoolean("resume_required")
                            model_events.ticket_type =if (json_objectdetail.isNull("ticket_type")) "" else json_objectdetail.optString("ticket_type")
                            model_events.seats =if (json_objectdetail.isNull("seats")) "" else  json_objectdetail.optString("seats")
                            model_events.event_type =if (json_objectdetail.isNull("event_type")) "" else  json_objectdetail.optString("event_type")
                            model_events.display_price =if (json_objectdetail.isNull("display_price")) 0 else  json_objectdetail.optInt("display_price")
                            model_events.agenda = if (json_objectdetail.isNull("agenda")) "" else json_objectdetail.optString("agenda")

                            model_events.modified_by =if (json_objectdetail.isNull("modified_by")) "" else json_objectdetail.optString("modified_by")
                            model_events.interested_count =if (json_objectdetail.isNull("interested_count")) 0 else json_objectdetail.optInt("interested_count")
                            model_events.view_count =if (json_objectdetail.isNull("view_count")) 0 else  json_objectdetail.optInt("view_count")
                            model_events.share_count =if (json_objectdetail.isNull("share_count")) 0 else  json_objectdetail.optInt("share_count")
                            model_events.payment =if (json_objectdetail.isNull("payment")) false else  json_objectdetail.optBoolean("payment")
                            model_events.status = if (json_objectdetail.isNull("status")) "" else json_objectdetail.optString("status")

                            model_events.priority_order = if (json_objectdetail.isNull("priority_order")) false else json_objectdetail.optBoolean("priority_order")
                            model_events.excerpt =if (json_objectdetail.isNull("excerpt")) "" else json_objectdetail.optString("excerpt")
                            model_events.event_register_start_date_time =if (json_objectdetail.isNull("event_register_start_date_time")) "" else json_objectdetail.optString("event_register_start_date_time")
                            model_events.created_on =if (json_objectdetail.isNull("created_on")) "" else json_objectdetail.optString("created_on")
                            model_events.author_id =if (json_objectdetail.isNull("author_id")) "" else json_objectdetail.optString("author_id")
                            model_events.is_online =if (json_objectdetail.isNull("is_online")) false else json_objectdetail.optBoolean("is_online")

                            model_events.gtm_id =if (json_objectdetail.isNull("gtm_id")) 0 else json_objectdetail.optInt("gtm_id")
                            model_events.author_name =if (json_objectdetail.isNull("author_name")) "" else json_objectdetail.optString("author_name")
                            model_events.is_private =if (json_objectdetail.isNull("is_private")) false else json_objectdetail.optBoolean("is_private")
                            model_events.register_count =if (json_objectdetail.isNull("register_count")) "" else json_objectdetail.optString("register_count")
                            model_events.title =if (json_objectdetail.isNull("title")) "" else json_objectdetail.optString("title")
                            model_events.event_end_date_time =if (json_objectdetail.isNull("event_end_date_time")) "" else json_objectdetail.optString("event_end_date_time")

                            model_events.slug =if (json_objectdetail.isNull("slug")) "" else json_objectdetail.optString("slug")
                            model_events.event_register_end_date_time =if (json_objectdetail.isNull("event_register_end_date_time")) "" else json_objectdetail.optString("event_register_end_date_time")
                            model_events.event_by =if (json_objectdetail.isNull("event_by")) "" else json_objectdetail.optString("event_by")
                            model_events.id =if (json_objectdetail.isNull("id")) 0 else json_objectdetail.optInt("id")
                            model_events.post_for =if (json_objectdetail.isNull("post_for")) "" else json_objectdetail.optString("post_for")
                            model_events.publish_date =if (json_objectdetail.isNull("publish_date")) "" else json_objectdetail.optString("publish_date")

                            model_events.event_start_date_time =if (json_objectdetail.isNull("event_start_date_time")) ""  else json_objectdetail.optString("event_start_date_time")
                            model_events.created_by =if (json_objectdetail.isNull("created_by")) 0  else json_objectdetail.optInt("created_by")
                            model_events.company_id =if (json_objectdetail.isNull("company_id")) 0  else json_objectdetail.optInt("company_id")
                            model_events.address =if (json_objectdetail.isNull("address")) ""  else json_objectdetail.optString("address")
                            model_events.modified_on =if (json_objectdetail.isNull("modified_on")) ""  else json_objectdetail.optString("modified_on")
                            model_events.payment_note =if (json_objectdetail.isNull("payment_note")) ""  else json_objectdetail.optString("payment_note")

                            model_events.description =if (json_objectdetail.isNull("description")) "" else json_objectdetail.optString("description")
                            model_events.city =if (json_objectdetail.isNull("city")) "" else json_objectdetail.optString("city")
                            model_events.event_url =if (json_objectdetail.isNull("event_url")) "" else json_objectdetail.optString("event_url")
                            model_events.show_on_search =if (json_objectdetail.isNull("show_on_search")) false else json_objectdetail.optBoolean("show_on_search")
                            model_events.image_url =if (json_objectdetail.isNull("image_url")) "" else json_objectdetail.optString("image_url")
                            model_events.thumbnail_url =if (json_objectdetail.isNull("thumbnail_url")) "" else json_objectdetail.optString("thumbnail_url")
                            model_events.interested = false// getInterested(model_events.id!!)

                            var compdata: List<String>
                            var data: ArrayList<EventCompanies> = ArrayList()
                            if(json_objectdetail.isNull("link_companies_name")){

                            }
                            else {
                                var s:String = ""
                                compdata = json_objectdetail.optString("link_companies_name").split(Regex(","),0)
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
                                    model_events.gtm_id!! ,
                                    model_events.author_name!!,
                                    model_events.is_private!! ,
                                    model_events.register_count!! ,
                                    model_events.title!!,
                                    model_events.event_end_date_time!!,
                                    model_events.slug!! ,
                                    model_events.event_register_end_date_time!! ,
                                    model_events.event_by!! ,
                                    model_events.id!! ,
                                    model_events.post_for!!,
                                    model_events.publish_date!! ,
                                    model_events.event_start_date_time!! ,
                                    model_events.created_by!! ,
                                    model_events.company_id!! ,
                                    model_events.address!!,
                                    model_events.modified_on!! ,
                                    model_events.payment_note!! ,
                                    model_events.description!! ,
                                    model_events.city!! ,
                                    model_events.event_url!! ,
                                    model_events.show_on_search!! ,
                                    model_events.image_url!!,
                                    model_events.thumbnail_url!!,
                                    model_events.link_companies_name!!
                                )
                            )
                            Log.d("TAGG", "Inside response body...if")
                        }

                        Log.d("TAGG", "Inside response body..data load")
                        if (type_group == 2){
                        }
                        else if (type_group == 3){
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
//                    if (pagenoo == "1")
//                        loadprev.visibility = View.GONE
//                    else {
//                        loadprev.visibility = View.VISIBLE
//                        prev_page_no_featured = prev_page.toString()
//                    }
                    if(listOfEventsdata.size>0) {
                        val array: ArrayList<Int> = ArrayList()
                        for (i in 0 until listOfEventsdata.size) {

                            array.add(listOfEventsdata[i].id!!)
                        }
                        Log.d("TAGG PARAMS", array.toString());
                        val params = HashMap<String, ArrayList<Int>>()
                        params["event_id"] = array

                        var a: String = ""
                        var b: Boolean = false
                        retrofitInterface1 = RetrofitClient.client!!.create(RetrofitInterface::class.java)

                        val call =
                            retrofitInterface1!!.GetInterest(
                                EndPoints.CLIENT_ID,
                                "Bearer " + EndPoints.ACCESS_TOKEN,
                                params
                            )

                        call.enqueue(object : Callback<GetInterestResponse> {
                            override fun onResponse(
                                call: Call<GetInterestResponse>,
                                response: Response<GetInterestResponse>
                            ) {

                                Logger.d("CODE", response.code().toString() + "")
                                Logger.d("MESSAGE", response.message() + "")
                                Logger.d("URL", "" + response)
                                Logger.d("RESPONSE follow company", "" + Gson().toJson(response))
                                if (response.isSuccessful) {
                                    //   Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG) .show()

                                    for(k in 0 until array.size){
                                        if(response.body()!!?.body!!.contains(array[k]))
                                            listOfEventsdata[k].interested = true
                                        else
                                            listOfEventsdata[k].interested = false
                                    }

                                    var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                                    mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                                    hotEventsAdapter = EventsAdapter(listOfEventsdata, isLoggedIn, 1,0,listOfCompareJoineddata,0,"Events")
                                    mgroupsRecyclerView!!.adapter = hotEventsAdapter

                                } else {
                                    //  ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                                    var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                                    mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                                    hotEventsAdapter = EventsAdapter(listOfEventsdata, isLoggedIn, 1,0,listOfCompareJoineddata,0,"Events")
                                    mgroupsRecyclerView!!.adapter = hotEventsAdapter
                                }
                            }

                            override fun onFailure(call: Call<GetInterestResponse>, t: Throwable) {
                                Logger.d("TAGG", "Apply Job FAILED : $t")
                                var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                                mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                                hotEventsAdapter = EventsAdapter(listOfEventsdata, isLoggedIn, 1,0,listOfCompareJoineddata,0,"Events")
                                mgroupsRecyclerView!!.adapter = hotEventsAdapter
                            }
                        })
                    }
                    else{
                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                        mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                        hotEventsAdapter = EventsAdapter(listOfEventsdata, isLoggedIn, 1,0,listOfCompareJoineddata,0,"Events")
                        mgroupsRecyclerView!!.adapter = hotEventsAdapter
                    }
                }
                else {
                    Toast.makeText(applicationContext, "No data exists", Toast.LENGTH_LONG).show()
                    loadnext.visibility = View.GONE
                    loadprev.visibility = View.GONE
                    scroll =0
                }

            }

            override fun onFailure(call: Call<Event>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                //   Toast.makeText(applicationContext, "No Groups Exixts, Join a group today!", Toast.LENGTH_LONG).show()
                loadnext.visibility = View.GONE
                loadprev.visibility = View.GONE
                scroll =0
                default_events.visibility = View.VISIBLE
                default_events.setText("There are no events for the time being, but do update your preferences here to get instant notifications when a relevant event comes up")
                mgroupsRecyclerView!!.visibility = View.GONE
                updatepref.visibility = View.VISIBLE
            }
        })
    }

    fun loadFilteredData(pageno:String,keyword:String,category_name:String,location_name:String,event_types:String,type:String){

        isfilter=1

        if (pageno.equals("1")) {
//            listOfEventsdata.clear()
//            hotEventsAdapter!!.notifyDataSetChanged()
        }
        else{}
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
        if (category_name.equals("")){}
        else{
            if(category_name.contains(",")) {
                params["event_categories"] = category_name.substring(0, category_name.length - 1)
                string = string+ category_name.substring(0,category_name.length-1)+","
            }
            else {
                params["event_categories"] = category_name.toString()
                string = string+ category_name.substring(0,category_name.length)+","

            }
            if(category_name.substring(0,category_name.length).contains(",")) {
                var output:List<String> = category_name.substring(0,category_name.length-1) . split (",");
                for(i in 0 until output.size)
                    list.add(Tag(output.get(i)))
            }
            else
                list.add(Tag(category_name))
        }
        if (location_name.equals("")){}
        else {
            if(location_name.contains(",")) {
                params["city"] = location_name.substring(0, location_name.length - 1)
                string = string+ location_name.substring(0,location_name.length-1)+","
            }
            else {
                params["city"] = location_name.toString()
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
        if (event_types.equals("")){}
        else{
            if(event_types.contains(",")) {
                params["payment_type"/**/] = event_types.substring(0, event_types.length - 1)
                string = string+ event_types.substring(0,event_types.length-1)+","
            }
            else {
                params["payment_type"/*payment_type*/] = event_types.toString()
                string = string+ event_types.substring(0,event_types.length)+","

            }
            if(event_types.substring(0,event_types.length).contains(",")) {
                var output:List<String> = event_types.substring(0,event_types.length-1) . split (",");
                for(i in 0 until output.size)
                    list.add(Tag(output.get(i)))
            }
            else
                list.add(Tag(event_types))

        }
        if (type.equals("")){}
        else{
            if (type_group==1)
                params["event_type"] = "featured"
            else if (type_group==2)
                params["event_type"] = "basic"
            else
                params["event_type"] = "past"
            //params["event_type"] = type
//            string = string+ type.substring(0,type.length-1)+","
//            list.add(Tag(type.substring(0,type.length-1)))
        }

        Log.d("TAGG", "PARAMS"+params)

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface!!.getfilteredEventsData( EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        call.enqueue(object : Callback<Event> {
            override fun onResponse(call: Call<Event>, response: Response<Event>) {

                Logger.d("URL", "" + response+ EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)

                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                Log.d("TAGG", "Outside body")
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {

                    Log.d("TAGG", "Inside body")
                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")

                    var jsonarray_info: JSONArray = jsonObject1.optJSONArray("body")

                    var jsonarray_pagination: JSONObject = jsonObject1.optJSONObject("pagination")

                    val has_next: String = jsonarray_pagination.optBoolean("has_next").toString()

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
                        listOfEventsdata.clear()
                        hotEventsAdapter!!.notifyDataSetChanged()
                        //  ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                        Log.d("TAGG", "Inside response success")
                        for (i in 0 until response.body()!!.body!!.size) {
                            Log.d("TAGG", "Inside response body")
                            var json_objectdetail: JSONObject = jsonarray_info.optJSONObject(i)

                            val model_events: EventsView = EventsView();

                            var locations: JSONArray
                            var locdata: ArrayList<EventLocation> = ArrayList()
                            if(json_objectdetail.isNull("events_locations")){}
                            else {
                                locations = json_objectdetail.optJSONArray("events_locations")

                                for (k in 0 until locations.length()) {
                                    val locModel: EventLocation = EventLocation()
                                    locModel.pincode =
                                        if (locations.optJSONObject(k).isNull("pincode")) "" else locations.optJSONObject(k).optString("pincode")
                                    locModel.discounted_price =
                                        if (locations.optJSONObject(k).isNull("discounted_price")) 0 else locations.optJSONObject(k).optInt("discounted_price")
                                    locModel.event_register_start_date_time =
                                        if (locations.optJSONObject(k).isNull("event_register_start_date_time")) "" else locations.optJSONObject(k).optString("event_register_start_date_time")
                                    locModel.event_type =
                                        if (locations.optJSONObject(k).isNull("event_type")) "" else locations.optJSONObject(k).optString("event_type")
                                    locModel.event_start_date_time =
                                        if (locations.optJSONObject(k).isNull("event_start_date_time")) "" else locations.optJSONObject(k).optString("event_start_date_time")
                                    locModel.amount =
                                        if (locations.optJSONObject(k).isNull("amount")) 0 else locations.optJSONObject(k).optInt("amount")
                                    locModel.discount_start_date_time =
                                        if (locations.optJSONObject(k).isNull("discount_start_date_time")) "" else locations.optJSONObject(k).optString("discount_start_date_time")
                                    locModel.event_register_end_date_time =
                                        if (locations.optJSONObject(k).isNull("event_register_end_date_time")) "" else locations.optJSONObject(k).optString("event_register_end_date_time")
                                    locModel.discount_end_date_time =
                                        if (locations.optJSONObject(k).isNull("discount_end_date_time")) "" else locations.optJSONObject(k).optString("discount_end_date_time")
                                    locModel.discount_active =
                                        if (locations.optJSONObject(k).isNull("discount_active")) "" else locations.optJSONObject(k).optString("discount_active")
                                    locModel.address =
                                        if (locations.optJSONObject(k).isNull("address")) "" else locations.optJSONObject(k).optString("address")
                                    locModel.event_end_date_time =
                                        if (locations.optJSONObject(k).isNull("event_end_date_time")) "" else locations.optJSONObject(k).optString("event_end_date_time")

                                    locModel.id =
                                        if (locations.optJSONObject(k).isNull("id")) 0 else locations.optJSONObject(k).optInt("id")
                                    locModel.state =
                                        if (locations.optJSONObject(k).isNull("state")) "" else locations.optJSONObject(k).optString("state")
                                    locModel.seats =
                                        if (locations.optJSONObject(k).isNull("seats")) "" else locations.optJSONObject(k).optString("seats")
                                    locModel.event_id =
                                        if (locations.optJSONObject(k).isNull("event_id")) 0 else locations.optJSONObject(k).optInt("event_id")
                                    locModel.country =
                                        if (locations.optJSONObject(k).isNull("country")) "" else locations.optJSONObject(k).optString("country")
                                    locModel.registration_open =
                                        if (locations.optJSONObject(k).isNull("registration_open")) false else locations.optJSONObject(k).optBoolean("registration_open")
                                    locModel.google_map_url =
                                        if (locations.optJSONObject(k).isNull("google_map_url")) "" else locations.optJSONObject(k).optString("google_map_url")
                                    locModel.city =
                                        if (locations.optJSONObject(k).isNull("city")) "" else locations.optJSONObject(k).optString("city")

                                    locdata.add(locModel)
                                }

                            }
                            model_events.event_locations= locdata

                            model_events.discount_start_date_time =if (json_objectdetail.isNull("discount_start_date_time")) "" else json_objectdetail.optString("discount_start_date_time")
                            model_events.discount_end_date_time =if (json_objectdetail.isNull("discount_end_date_time")) "" else json_objectdetail.optString("discount_end_date_time")
                            model_events.price_after_discount =if (json_objectdetail.isNull("price_after_discount")) 0 else json_objectdetail.optInt("price_after_discount")
                            model_events.price_before_discount =if (json_objectdetail.isNull("price_before_discount")) 0 else json_objectdetail.optInt("price_before_discount")


                            model_events.interested =if (json_objectdetail.isNull("interested")) false else json_objectdetail.optBoolean("interested")
                            model_events.registered =if (json_objectdetail.isNull("registered")) false else json_objectdetail.optBoolean("registered")
                            model_events.event_category =if (json_objectdetail.isNull("events_category")) "" else json_objectdetail.optString("events_category")

                            model_events.company_name =if (json_objectdetail.isNull("company_name")) "" else json_objectdetail.optString("company_name")
                            model_events.featured_end_date_time =if (json_objectdetail.isNull("featured_end_date_time")) "" else json_objectdetail.optString("featured_end_date_time")
                            model_events.preference_required =if (json_objectdetail.isNull("preference_required")) false else json_objectdetail.optBoolean("preference_required")
                            model_events.terms_and_conditions =if (json_objectdetail.isNull("terms_and_conditions")) "" else  json_objectdetail.optString("terms_and_conditions")
                            model_events.featured_start_date_time =if (json_objectdetail.isNull("featured_start_date_time")) "" else  json_objectdetail.optString("featured_start_date_time")
                            model_events.featured_event =if (json_objectdetail.isNull("featured_event")) false else  json_objectdetail.optBoolean("featured_event")
                            model_events.faq = if (json_objectdetail.isNull("faq")) "" else json_objectdetail.optString("faq")

                            model_events.resume_required =if (json_objectdetail.isNull("resume_required")) false else json_objectdetail.optBoolean("resume_required")
                            model_events.ticket_type =if (json_objectdetail.isNull("ticket_type")) "" else json_objectdetail.optString("ticket_type")
                            model_events.seats =if (json_objectdetail.isNull("seats")) "" else  json_objectdetail.optString("seats")
                            model_events.event_type =if (json_objectdetail.isNull("event_type")) "" else  json_objectdetail.optString("event_type")
                            model_events.display_price =if (json_objectdetail.isNull("display_price")) 0 else  json_objectdetail.optInt("display_price")
                            model_events.agenda = if (json_objectdetail.isNull("agenda")) "" else json_objectdetail.optString("agenda")

                            model_events.modified_by =if (json_objectdetail.isNull("modified_by")) "" else json_objectdetail.optString("modified_by")
                            model_events.interested_count =if (json_objectdetail.isNull("interested_count")) 0 else json_objectdetail.optInt("interested_count")
                            model_events.view_count =if (json_objectdetail.isNull("view_count")) 0 else  json_objectdetail.optInt("view_count")
                            model_events.share_count =if (json_objectdetail.isNull("share_count")) 0 else  json_objectdetail.optInt("share_count")
                            model_events.payment =if (json_objectdetail.isNull("payment")) false else  json_objectdetail.optBoolean("payment")
                            model_events.status = if (json_objectdetail.isNull("status")) "" else json_objectdetail.optString("status")

                            model_events.priority_order = if (json_objectdetail.isNull("priority_order")) false else json_objectdetail.optBoolean("priority_order")
                            model_events.excerpt =if (json_objectdetail.isNull("excerpt")) "" else json_objectdetail.optString("excerpt")
                            model_events.event_register_start_date_time =if (json_objectdetail.isNull("event_register_start_date_time")) "" else json_objectdetail.optString("event_register_start_date_time")
                            model_events.created_on =if (json_objectdetail.isNull("created_on")) "" else json_objectdetail.optString("created_on")
                            model_events.author_id =if (json_objectdetail.isNull("author_id")) "" else json_objectdetail.optString("author_id")
                            model_events.is_online =if (json_objectdetail.isNull("is_online")) false else json_objectdetail.optBoolean("is_online")

                            model_events.gtm_id =if (json_objectdetail.isNull("gtm_id")) 0 else json_objectdetail.optInt("gtm_id")
                            model_events.author_name =if (json_objectdetail.isNull("author_name")) "" else json_objectdetail.optString("author_name")
                            model_events.is_private =if (json_objectdetail.isNull("is_private")) false else json_objectdetail.optBoolean("is_private")
                            model_events.register_count =if (json_objectdetail.isNull("register_count")) "" else json_objectdetail.optString("register_count")
                            model_events.title =if (json_objectdetail.isNull("title")) "" else json_objectdetail.optString("title")
                            model_events.event_end_date_time =if (json_objectdetail.isNull("event_end_date_time")) "" else json_objectdetail.optString("event_end_date_time")

                            model_events.slug =if (json_objectdetail.isNull("slug")) "" else json_objectdetail.optString("slug")
                            model_events.event_register_end_date_time =if (json_objectdetail.isNull("event_register_end_date_time")) "" else json_objectdetail.optString("event_register_end_date_time")
                            model_events.event_by =if (json_objectdetail.isNull("event_by")) "" else json_objectdetail.optString("event_by")
                            model_events.id =if (json_objectdetail.isNull("id")) 0 else json_objectdetail.optInt("id")
                            model_events.post_for =if (json_objectdetail.isNull("post_for")) "" else json_objectdetail.optString("post_for")
                            model_events.publish_date =if (json_objectdetail.isNull("publish_date")) "" else json_objectdetail.optString("publish_date")

                            model_events.event_start_date_time =if (json_objectdetail.isNull("event_start_date_time")) ""  else json_objectdetail.optString("event_start_date_time")
                            model_events.created_by =if (json_objectdetail.isNull("created_by")) 0  else json_objectdetail.optInt("created_by")
                            model_events.company_id =if (json_objectdetail.isNull("company_id")) 0  else json_objectdetail.optInt("company_id")
                            model_events.address =if (json_objectdetail.isNull("address")) ""  else json_objectdetail.optString("address")
                            model_events.modified_on =if (json_objectdetail.isNull("modified_on")) ""  else json_objectdetail.optString("modified_on")
                            model_events.payment_note =if (json_objectdetail.isNull("payment_note")) ""  else json_objectdetail.optString("payment_note")

                            model_events.description =if (json_objectdetail.isNull("description")) "" else json_objectdetail.optString("description")
                            model_events.city =if (json_objectdetail.isNull("city")) "" else json_objectdetail.optString("city")
                            model_events.event_url =if (json_objectdetail.isNull("event_url")) "" else json_objectdetail.optString("event_url")
                            model_events.show_on_search =if (json_objectdetail.isNull("show_on_search")) false else json_objectdetail.optBoolean("show_on_search")
                            model_events.image_url =if (json_objectdetail.isNull("image_url")) "" else json_objectdetail.optString("image_url")
                            model_events.thumbnail_url =if (json_objectdetail.isNull("thumbnail_url")) "" else json_objectdetail.optString("thumbnail_url")


                            var compdata: List<String>
                            var data: ArrayList<EventCompanies> = ArrayList()
                            if(json_objectdetail.isNull("link_companies_name")){

                            }
                            else {
                                var s:String = ""
                                compdata = json_objectdetail.optString("link_companies_name").split(Regex(","),0)
                                for (k in 0 until compdata.size) {
                                    val locModel: EventCompanies = EventCompanies()
                                    locModel.company_id = 0
                                    locModel.company_name = compdata[k].toString()
                                    data.add(locModel)
                                }

                            }
                            Log.d("TAGG","DATA COMP "+data.toString())
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
                                    model_events.gtm_id!! ,
                                    model_events.author_name!!,
                                    model_events.is_private!! ,
                                    model_events.register_count!! ,
                                    model_events.title!!,
                                    model_events.event_end_date_time!!,
                                    model_events.slug!! ,
                                    model_events.event_register_end_date_time!! ,
                                    model_events.event_by!! ,
                                    model_events.id!! ,
                                    model_events.post_for!!,
                                    model_events.publish_date!! ,
                                    model_events.event_start_date_time!! ,
                                    model_events.created_by!! ,
                                    model_events.company_id!! ,
                                    model_events.address!!,
                                    model_events.modified_on!! ,
                                    model_events.payment_note!! ,
                                    model_events.description!! ,
                                    model_events.city!! ,
                                    model_events.event_url!! ,
                                    model_events.show_on_search!! ,
                                    model_events.image_url!!,
                                    model_events.thumbnail_url!!,
                                    model_events.link_companies_name!!
                                )
                            )

                        }

                        if (pageno.equals("1")) {
                            hotEventsAdapter!!.notifyDataSetChanged()
                        }
                        else {
                            if(listOfEventsdata.size>0){
                                var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                                mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                                hotEventsAdapter = EventsAdapter(listOfEventsdata, isLoggedIn, 1,0,listOfCompareJoineddata,0,"Events")
                                mgroupsRecyclerView!!.adapter = hotEventsAdapter
                            }
                        }

                        if (type_group == 2){
                            //loadhotEventsData("1")
//                            loadotherEventsData("1")
                        }
                        else if (type_group == 3){
                            //loadhotEventsData("1")
//                            loadAllGroupData("1")
                        }

                    }
                    else {
                        //  ToastHelper.makeToast(applicationContext, "Invalid Request")
                        default_events.visibility = View.VISIBLE
                        default_events.setText("There are no events for the time being, but do update your preferences here to get instant notifications when a relevant event comes up")
                        mgroupsRecyclerView!!.visibility = View.GONE
                        updatepref.visibility = View.VISIBLE
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

                    activity_jfh_banner_jobs.visibility = View.GONE
                    filer_default_layout.visibility = View.VISIBLE
                    success_default_layout.visibility = View.GONE
                    layouttop.visibility = View.GONE
                    def_text_jobs.visibility = View.GONE
//                    layoutadd.visibility = View.VISIBLE
                    // layout1.visibility = View.VISIBLE
                    search_default.setText("")
                    tag_group.setChipList(list)
                    joblisting_tag.text = total_items.toString()+" matching events found"
                    mainScroll_jobs.fullScroll(View.FOCUS_UP);
                    mainScroll_jobs.smoothScrollTo(0,0);
                    recycler_view_groups.setFocusable(false);
//                    if (pagenoo == "1")
//                        loadprev.visibility = View.GONE
//                    else {
//                        loadprev.visibility = View.VISIBLE
//                        prev_page_no_featured = prev_page.toString()
//                    }
                }
                else {
                    //   Toast.makeText(applicationContext, "No data exists", Toast.LENGTH_LONG).show()
                    loadnext.visibility = View.GONE
                    loadprev.visibility = View.GONE
                    scroll =0
                    updatepref.visibility = View.VISIBLE
                    default_events.visibility = View.VISIBLE
                    default_events.setText("There are no events for the time being, but do update your preferences here to get instant notifications when a relevant event comes up")
                    mgroupsRecyclerView!!.visibility = View.GONE
                    activity_jfh_banner_jobs.visibility = View.GONE
                    filer_default_layout.visibility = View.VISIBLE
                    success_default_layout.visibility = View.GONE
                    search_default.setText("")
                    tag_group.setChipList(list)
                    joblisting_tag.text = "'"+string.substring(0,string.length-1) +"'" + " Event listing not found"
                    layouttop.visibility = View.GONE
//                    layoutadd.visibility = View.GONE
                    // layout1.visibility = View.GONE
                    mainScroll_jobs.fullScroll(View.FOCUS_UP);
                    mainScroll_jobs.smoothScrollTo(0,0);
                    recycler_view_groups.setFocusable(false);
                }
            }

            override fun onFailure(call: Call<Event>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext, "No Events Exists!", Toast.LENGTH_LONG).show()
                loadnext.visibility = View.GONE
                loadprev.visibility = View.GONE
                scroll =0
                updatepref.visibility = View.VISIBLE
                default_events.visibility = View.VISIBLE
                default_events.setText("There are no events for the time being, but do update your preferences here to get instant notifications when a relevant event comes up")
                mgroupsRecyclerView!!.visibility = View.GONE
                filer_default_layout.visibility = View.VISIBLE
                success_default_layout.visibility = View.GONE
                search_default.setText("")
                tag_group.setChipList(list)
                joblisting_tag.text = "'"+string.substring(0,string.length-1) +"'" + " Event listing not found"
                layouttop.visibility = View.GONE
//                layoutadd.visibility = View.GONE
                // layout1.visibility = View.GONE
                mainScroll_jobs.fullScroll(View.FOCUS_UP);
                mainScroll_jobs.smoothScrollTo(0,0);
                recycler_view_groups.setFocusable(false);
                activity_jfh_banner_jobs.visibility = View.GONE
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
                intent.putExtra("isLoggedIn",true)
                startActivity(intent)
                closeDrawer()


            }
            R.id.action_groups -> {
                val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
                drawerLayout.closeDrawer(GravityCompat.START)
                intent = Intent(applicationContext, ZActivityGroups::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("isLoggedIn",true)
                startActivity(intent)
                closeDrawer()


            }
            R.id.action_jobs -> {
                val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
                drawerLayout.closeDrawer(GravityCompat.START)
                intent = Intent(applicationContext, ZActivityJobs::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("isLoggedIn",true)
                startActivity(intent)
                closeDrawer()
            }
            R.id.action_companies -> {
                val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
                drawerLayout.closeDrawer(GravityCompat.START)
                intent = Intent(applicationContext, ZActivityCompanies::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("isLoggedIn",true)
                startActivity(intent)
                closeDrawer()
            }
            R.id.action_reskilling -> {
                intent = Intent(applicationContext, ZActivityDashboard::class.java)
                intent.putExtra("isLoggedIn",true)
                startActivity(intent)
                closeDrawer()
            }
            R.id.action_mentors -> {
                intent = Intent(applicationContext, WebActivity::class.java)
                intent.putExtra("value","https://www.jobsforher.com/mentors")
                startActivity(intent)
            }
            R.id.action_events -> {
                val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
                drawerLayout.closeDrawer(GravityCompat.START)
                intent = Intent(applicationContext, ZActivityEvents::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("isLoggedIn",true)
                startActivity(intent)
                closeDrawer()

            }
            R.id.action_blogs-> {
                intent = Intent(applicationContext, WebActivity::class.java)
                intent.putExtra("value","https://www.jobsforher.com/blogs")
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
            R.id.action_login->{

            }

            R.id.action_share -> {
                HelperMethods.showAppShareOptions(this@ZActivityEvents)
            }
        }

        return true
    }

    private fun closeDrawer() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    fun search(searchview: SearchView){


    }



    public fun openBottomSheetFilter() {

//        val mLayoutManager1 = GridLayoutManager(applicationContext, 3)
//        mRecyclerView_city!!.layoutManager = mLayoutManager1
//        mAdapterCities = ZGroupsPhotosAdapter(listOfCities)
//        mRecyclerView_city!!.adapter = mAdapterCities

        val dialog = Dialog(this, R.style.AppTheme)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog .setCancelable(true)
        dialog .setContentView(R.layout.bottom_sheet_filter_events)
        var searchView:SearchView = dialog.findViewById(R.id.search_filter) as SearchView
        search(searchView);

        var mRecyclerView_category = dialog.findViewById(R.id.recycler_view_filtercategory) as RecyclerView
        var mRecyclerView_city = dialog.findViewById(R.id.recycler_view_filtercity) as RecyclerView
        var mRecyclerView_eventtype = dialog.findViewById(R.id.recycler_view_filtereventtype) as RecyclerView


        val mLayoutManager = GridLayoutManager(applicationContext, 1)
        mRecyclerView_category!!.layoutManager = mLayoutManager
        mAdapterCategories = ZEventsCategoryAdapter(listOfCategories,category_nameArray)
        mRecyclerView_category!!.adapter = mAdapterCategories


        val mLayoutManager1 = GridLayoutManager(applicationContext, 1)
        mRecyclerView_city!!.layoutManager = mLayoutManager1
        mAdapterCities = ZEventsCitiesAdapter(listOfCities,location_nameArray)
        mRecyclerView_city!!.adapter = mAdapterCities

        val data= ArrayList<String>()
        data!!.add("all")
        data!!.add("free")
        data!!.add("paid")
        data!!.add("other")

        val mLayoutManager2 = GridLayoutManager(applicationContext, 1)
        mRecyclerView_eventtype!!.layoutManager = mLayoutManager2
        mAdapterEventtype = ZEventsTypeAdapter(this, listOfEventType,event_typesArray,data)
        mRecyclerView_eventtype!!.adapter = mAdapterEventtype


        val category = dialog .findViewById(R.id.category) as TextView
        val city = dialog .findViewById(R.id.city) as TextView
        val eventType = dialog .findViewById(R.id.eventType) as TextView

        val filterClose = dialog.findViewById(R.id.close_filter) as ImageView
        filterClose.setOnClickListener {
            location_nameArray.clear()
            event_typesArray.clear()
            category_nameArray.clear()
            dialog.cancel();
        }
        category.setTextColor(Color.BLACK)
        city.setTextColor(Color.GRAY)
        category.setOnClickListener {
            mRecyclerView_category.visibility = View.VISIBLE
            mRecyclerView_city.visibility = View.GONE
            mRecyclerView_eventtype.visibility = View.GONE
            category.setTextColor(Color.BLACK)
            city.setTextColor(Color.GRAY)
            eventType.setTextColor(Color.GRAY)
        }

        city.setOnClickListener {
            mRecyclerView_category.visibility = View.GONE
            mRecyclerView_city.visibility = View.VISIBLE
            mRecyclerView_eventtype.visibility = View.GONE
            category.setTextColor(Color.GRAY)
            city.setTextColor(Color.BLACK)
            eventType.setTextColor(Color.GRAY)
        }

        eventType.setOnClickListener {
            mRecyclerView_category.visibility = View.GONE
            mRecyclerView_city.visibility = View.GONE
            mRecyclerView_eventtype.visibility = View.VISIBLE
            category.setTextColor(Color.GRAY)
            city.setTextColor(Color.GRAY)
            eventType.setTextColor(Color.BLACK)
        }

        val keyword_edittext = dialog .findViewById(R.id.keyword_edittext) as EditText
        keyword_edittext.setText(keyword_text)
        var filter_apply = dialog.findViewById(R.id.filter_apply) as TextView
        var filter_reset = dialog.findViewById(R.id.filter_reset) as TextView
        var datePicker = dialog.findViewById(R.id.datePicker) as TextView
        val reset = dialog.findViewById(R.id.reset) as TextView
        reset.setOnClickListener {
            filter_reset.callOnClick()
        }
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

            isfilter =1
            if (type_group==1)
                type = "featured"
            else if(type_group==2)
                type = "basic"
            else
                type = "past"
            filter.setText("FILTER *")
            keyword_text = keyword_edittext.text.toString()
            loadFilteredData("1",keyword_text,category_name,location_name, event_types,type)
            dialog.cancel()
        }

        filter_reset.setOnClickListener {
            filer_default_layout.visibility = View.GONE
            keyword_edittext.text.clear()
            keyword_text=""
            category_name= ""
            location_name=""
            event_types=""
            location_name=""
            event_types=""
            type=""
            isfilter = 0
            filter.setText("FILTER")
            category_nameArray.clear()
            val mLayoutManager = GridLayoutManager(applicationContext, 1)
            mRecyclerView_category!!.layoutManager = mLayoutManager
            mAdapterCategories = ZEventsCategoryAdapter(listOfCategories,category_nameArray)
            mRecyclerView_category!!.adapter = mAdapterCategories

            location_nameArray.clear()
            val mLayoutManager1 = GridLayoutManager(applicationContext, 1)
            mRecyclerView_city!!.layoutManager = mLayoutManager1
            mAdapterCities = ZEventsCitiesAdapter(listOfCities,location_nameArray)
            mRecyclerView_city!!.adapter = mAdapterCities

            event_typesArray.clear()
            val mLayoutManager2 = GridLayoutManager(applicationContext, 1)
            mRecyclerView_eventtype!!.layoutManager = mLayoutManager2
            mAdapterEventtype = ZEventsTypeAdapter(this, listOfEventType,event_typesArray,data)
            mRecyclerView_eventtype!!.adapter = mAdapterEventtype

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

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        datePicker.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
                datePicker.setText("" + dayOfMonth + "/" + (monthOfYear+1) + "/" + year)
            }, year, month, day)
            dpd.show()
        }

        var window : Window = dialog.getWindow()!!;
        var wlp: WindowManager.LayoutParams  = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setAttributes(wlp);
        dialog .show()


    }

    public fun getSelectedCategory(id:Int, name:String) {
        Log.d("TAGG", name)
        if (id==0) {
            //location_name = ""
            category_nameArray.remove(name)
            category_name = category_nameArray.toString().substring(1,category_nameArray.toString().length-1)
        }
        else {
            //location_name = location_name + name + ","
            category_nameArray.add(name)
            category_name = category_nameArray.toString().substring(1,category_nameArray.toString().length-1)
        }
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

    public fun getSelectedEventType(id:Int,name:String) {
        Log.d("TAGG", name)

        if (id==0) {
            //location_name = ""
            event_typesArray.remove(name)
            event_types = event_typesArray.toString().substring(1,event_typesArray.toString().length-1)
        }
        else {
            //location_name = location_name + name + ","
            event_typesArray.add(name)
            event_types = event_typesArray.toString().substring(1,event_typesArray.toString().length-1)
        }
    }


    fun loadCategoryData(){

        listOfCategories.clear()
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getCategories(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)
        call.enqueue(object : Callback<GetCategoryResponse> {

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(call: Call<GetCategoryResponse>, response: Response<GetCategoryResponse>) {

                Log.d("TAG","CODE"+ response.code().toString() + "")
                Log.d("TAG","MESSAGE"+ response.message() + "")
                Log.d("TAG","RESPONSE"+ "" + Gson().toJson(response))
                Log.d("TAG","URL"+ "" + response)
                //var str_response = Gson().toJson(response)
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
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
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getCities(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)
        call.enqueue(object : Callback<GetCityResponse> {

            override fun onResponse(call: Call<GetCityResponse>, response: Response<GetCityResponse>) {

                Log.d("TAG","CODE"+ response.code().toString() + "")
                Log.d("TAG","MESSAGE"+ response.message() + "")
                Log.d("TAG","RESPONSE"+ "" + Gson().toJson(response))
                Log.d("TAG","URL"+ "" + response)
                //var str_response = Gson().toJson(response)
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
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
                }
                else
                    Toast.makeText(applicationContext,"No data exists",Toast.LENGTH_LONG).show()
            }
            override fun onFailure(call: Call<GetCityResponse>, t: Throwable) {
                Logger.d("TAG", "FAILED : $t")
            }
        })
    }

    fun addJobType(){

        listOfEventType.clear()
        listOfEventType.add(JobTypeView("All","@drawable/ic_clock"))
        listOfEventType.add(JobTypeView("Free Events","@drawable/ic_clock"))
        listOfEventType.add(JobTypeView("Paid Events","@drawable/ic_house"))
        listOfEventType.add(JobTypeView("Other","@drawable/ic_clock"))

        listOfEventType1.clear()
        listOfEventType1.add(JobTypeView("Full Time","@drawable/ic_clock"))
        listOfEventType1.add(JobTypeView("Part Time","@drawable/ic_clock"))
        listOfEventType1.add(JobTypeView("Work From Home","@drawable/ic_house"))
        listOfEventType1.add(JobTypeView("Returnee Program","@drawable/ic_clock"))
        listOfEventType1.add(JobTypeView("Freelance/Projects","@drawable/ic_volunteer"))
        listOfEventType1.add(JobTypeView("Volunteer","@drawable/ic_freelance"))

        addJobFunctionalArea()
    }

    fun addJobFunctionalArea(){
        listOfJobFArea.clear()
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getFunctionalArea(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)
        call.enqueue(object : Callback<GetMobilityResponse> {

            override fun onResponse(call: Call<GetMobilityResponse>, response: Response<GetMobilityResponse>) {

                Log.d("TAG","CODE"+ response.code().toString() + "")
                Log.d("TAG","MESSAGE"+ response.message() + "")
                Log.d("TAG","RESPONSE"+ "" + Gson().toJson(response))
                Log.d("TAG","URL"+ "" + response)
                //var str_response = Gson().toJson(response)
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
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

                }
                else
                    Toast.makeText(applicationContext,"No data exists",Toast.LENGTH_LONG).show()
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

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, functionalareaList)
        functionalarea.setAdapter(adapter)
        functionalarea.setThreshold(1)
        functionalarea.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        addJobIndustry()

    }

    fun addJobIndustry(){

        listOfJobIndustry.clear()
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getIndustry(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)
        call.enqueue(object : Callback<GetMobilityResponse> {

            override fun onResponse(call: Call<GetMobilityResponse>, response: Response<GetMobilityResponse>) {

                Log.d("TAG","CODE"+ response.code().toString() + "")
                Log.d("TAG","MESSAGE"+ response.message() + "")
                Log.d("TAG","RESPONSE"+ "" + Gson().toJson(response))
                Log.d("TAG","URL"+ "" + response)
                //var str_response = Gson().toJson(response)
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
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

                }
                else
                    Toast.makeText(applicationContext,"No data exists",Toast.LENGTH_LONG).show()
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
        industry.setAdapter(adapter)
        industry.setThreshold(1)
        industry.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        //openBottomSheetFilter()
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

                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())

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

                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<ReportResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })

    }

//    fun getInterested(id:Int):Boolean {
//
//        Logger.d("CODE",id.toString() + "")
//
//        val params = HashMap<String, ArrayList<Int>>()
//
//        val array: ArrayList<Int>  = ArrayList()
//        array.add(id)
//        params["event_id"] = array
//
//        var a:Int = 0
//        var b:Boolean = false
//        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
//
//        val call = retrofitInterface!!.GetInterest("application/json",EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
//
//        call.enqueue(object : Callback<GetInterestResponse> {
//            override fun onResponse(call: Call<GetInterestResponse>, response: Response<GetInterestResponse>) {
//
//                Logger.d("CODE", response.code().toString() + "")
//                Logger.d("MESSAGE", response.message() + "")
//                Logger.d("URL", "" + response)
//                Logger.d("RESPONSE follow company", "" + Gson().toJson(response))
//
//                if (response.isSuccessful) {
//                    Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG).show()
//                    a = response.body()!!.body!![0]
//                    if(a==id)
//                        b = true
//                    else
//                        b = false
//                    Logger.d("TAGG", "Interest"+b)
//
//                } else {
//                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
//
//                }
//
//            }
//
//            override fun onFailure(call: Call<GetInterestResponse>, t: Throwable) {
//                Logger.d("TAGG", "Apply Job FAILED : $t")
//            }
//        })
//
//        return b;
//    }


    fun addInterest(id:Int, btnJoinGroup:TextView){

        Logger.d("CODE",id.toString() + "")

        val params = HashMap<String, String>()

        params["event_id"] = id.toString()
        params["interested"] = "1"

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.AddInterest(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)

        call.enqueue(object : Callback<UpdateReplyResponse> {
            override fun onResponse(call: Call<UpdateReplyResponse>, response: Response<UpdateReplyResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE follow company", "" + Gson().toJson(response))

                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                    btnJoinGroup.setTextColor(Color.parseColor("#B2B2B2"))



                } else {
                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())


                }
            }

            override fun onFailure(call: Call<UpdateReplyResponse>, t: Throwable) {
                Logger.d("TAGG", "Apply Job FAILED : $t")
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
            saveEvent(data!!.getExtras()!!.getInt("jobID")!!, data!!.getExtras()!!.getString("note")!!,
                data!!.getExtras()!!.getInt("resId")!!, data!!.getExtras()!!.getString("status")!!, data!!.getExtras()!!.getInt("event_location_id"))
        }


        if (requestCode == GALLERY_PDF) {

            if (data != null) {
                val contentURI1 = data!!.data
                try {

                    val fileName = HelperMethods.getFilePath(this, data.data)

                    val fileString = HelperMethods.testUriFile(this, fileName!!, data.data)

                    val  file = data?.data?.let {
                        HelperMethods.getFile(
                            this@ZActivityEvents,
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

        if (requestCode == PAYMENT_CODE){

            if (resultCode == 1) {
//                Log.d("TAGG", "Success - Payment ID : " + data!!.getExtras().optString("transaction_id")+" , "
//                        +data!!.getExtras().optBoolean("status"));
                Log.d("TAGG", "Success - Payment : " + data!!.getExtras()!!.getString("result")+ "TXNID: "+
                        data!!.getExtras()!!.getString("txnid")+"HASH: "+data!!.getExtras()!!.getString("hash"));
                updatePaymentEvent(data!!.getExtras()!!.getString("result")!!,data!!.getExtras()!!.getString("hash")!!,
                    data!!.getExtras()!!.getString("hash")!!)
                // successEvent(data!!.getExtras().optInt("id"),"paid")

            }
            else if(resultCode == 0){
                Log.d("TAGG", "Failure - Payment ID : " + data!!.getExtras()!!.getString("transaction_id")+" , "
                        +data!!.getExtras()!!.getBoolean("status"));
            }
            else{
                Log.d("TAGG", "Cancelled")
            }
            // successEvent()

        }
    }

    public override fun onResume() {
        super.onResume()
        // put your code here...

    }
}




