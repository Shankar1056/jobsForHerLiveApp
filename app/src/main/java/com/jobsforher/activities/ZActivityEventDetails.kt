package com.jobsforher.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import androidx.core.view.GravityCompat
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.jobsforher.R
import com.jobsforher.adapters.EventLocation1Adapter
import com.jobsforher.adapters.EventLocationAdapter
import com.jobsforher.adapters.ImagesAdapter
import com.jobsforher.adapters.ResumeAdapter
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.models.*
import com.jobsforher.network.responsemodels.*
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.toolbar_groups.*
import kotlinx.android.synthetic.main.zactivity_company_details.compname
import kotlinx.android.synthetic.main.zactivity_company_details.main
import kotlinx.android.synthetic.main.zactivity_event_details.*
import kotlinx.android.synthetic.main.zactivity_event_details.addtocalendar
import kotlinx.android.synthetic.main.zactivity_event_details.dis_text
import kotlinx.android.synthetic.main.zactivity_event_details.event_date
import kotlinx.android.synthetic.main.zactivity_event_details.event_logo
import kotlinx.android.synthetic.main.zactivity_event_details.event_time
import kotlinx.android.synthetic.main.zactivity_event_details.event_venue
import kotlinx.android.synthetic.main.zactivity_event_details.find_morejobs11
import kotlinx.android.synthetic.main.zactivity_event_details.goto_events
import kotlinx.android.synthetic.main.zactivity_event_details.layout_0
import kotlinx.android.synthetic.main.zactivity_event_details.layout_1
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import java.text.ParseException as ParseException1

class ZActivityEventDetails : Footer(), NavigationView.OnNavigationItemSelectedListener {

    var mRecyclerView: RecyclerView? = null
    var mAdapter: RecyclerView.Adapter<*>? = null
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
    var itemList_Images: ArrayList<String> = ArrayList()
    private val PAYMENT_CODE = 10
    var ticket_typee:String = ""
    var pref_req:Boolean = false
    var res_req:Boolean = false

    var listOfLocations: ArrayList<EventLocation>  = ArrayList()

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
    var start_date = ""
    var end_date = ""
    var name = ""
    var address = ""
    var externalUrl = ""
    var typee:Int = 0

    private val GALLERY_PDF = 3


    private var retrofitInterface: RetrofitInterface? = null
    private var retrofitInterface1: RetrofitInterface? = null
    var next_page_no_posts: String="1"
    var prev_page_no_posts: String="1"

    private var retrofitInterface_post: RetrofitInterface? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zgroups_eventdetails_toolbr)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        isLoggedIn=intent.getBooleanExtra("isLoggedIn",false)
        groupId = intent.getIntExtra("group_Id",0)
        title = intent.getStringExtra("title")
        isMyGroup = intent.getIntExtra("isMygroup",0)
        page = intent.getStringExtra("page")
        typee = intent.getIntExtra("type",0)

        //groupType = intent.getStringExtra("group_type")
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close
        )

//        comp_aboutus.setTextColor(resources.getColor(R.color.green))
//        comp_aboutus1.setTextColor(resources.getColor(R.color.green))
        addJobType()
        mappingWidgets()




        Log.d("TAGG", "JOB ID IS "+groupId.toString())
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

        goto_events.setOnClickListener {
            find_morejobs11.callOnClick()
        }
        find_morejobs11.setOnClickListener {
            finish()
            intent.putExtra("isLoggedIn", true)
            intent.putExtra("group_Id", groupId)
            intent.putExtra("title", title)
            intent.putExtra("page",page)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("isMygroup", isMyGroup)
            startActivity(intent)
        }

        if(isLoggedIn && isMyGroup==1){

            val menu = navView.menu
            menu.findItem(R.id.action_employerzone).setVisible(false)
            menu.findItem(R.id.action_mentorzone).setVisible(false)
            menu.findItem(R.id.action_partnerzone).setVisible(false)
            menu.findItem(R.id.action_logout).setVisible(false)
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
            btnApplied.visibility = View.VISIBLE

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
            menu.findItem(R.id.action_logout).setVisible(false)
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

            loggedin_header.setOnClickListener{
                intent = Intent(applicationContext, ProfileView::class.java)
                startActivity(intent)
            }
            img_profile_toolbar.visibility = View.VISIBLE
//            notificaton.visibility = View.VISIBLE
            sign_in.visibility = View.GONE

//            var mgroupsLayoutManager = LinearLayoutManager(this)
////            mRecyclerViewPosts!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
////            mAdapterJobs = PostsAdapter(listOfhotJobsdata, isLoggedIn)
////            mRecyclerViewPosts!!.adapter = mAdapterJobs
        }

        share.setOnClickListener {
            shareJobFunction()
        }

        addtocalendar.setOnClickListener {
            //            getDate(model.event_start_date_time!!)+" to "+getDate(model.event_end_date_time!!)

            val formatter1: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            var datestart : Date? = null
            var dateend : Date? = null
            try {
                datestart=formatter1.parse(start_date)
                dateend = formatter1.parse(end_date)

            }catch ( e: ParseException1) {
                e.printStackTrace()
            }
            var millisdate_start: Long = datestart!!.time
            var millisdate_end: Long = dateend!!.time

            Log.d("TAGG","CITY IS"+address)

            val calendarEvent = Calendar.getInstance()
            val intent = Intent(Intent.ACTION_EDIT)
            intent.type = "vnd.android.cursor.item/event"
            intent.putExtra("beginTime", millisdate_start)
            intent.putExtra("endTime", millisdate_end)
            intent.putExtra("title", name)
            intent.putExtra("eventLocation", address)
//            intent.putExtra("allDay", true)
//            intent.putExtra("rule", "FREQ=YEARLY")
            startActivity(intent)
        }

        img_profile_toolbar.setOnClickListener{
            //            val popupMenu: PopupMenu = PopupMenu(applicationContext,img_profile_toolbar)
//            popupMenu.menuInflater.inflate(R.menu.toolbar_main_menu,popupMenu.menu)
//            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
//                when(item.itemId) {
//                    R.id.action_dashboard -> {
//                        intent = Intent(applicationContext, ZActivityDashboard::class.java)
//                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        intent.putExtra("isLoggedIn", true)
//                        intent.putExtra("pagetype", 0)
//                        startActivity(intent)
//                    }
//                    R.id.action_profile ->{
//                        intent = Intent(applicationContext, SignUpWelcomeActivity::class.java)
//                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        intent.putExtra("isLoggedIn", true)
//                        startActivity(intent)
//                    }
//                    R.id.action_resume ->{
//                        intent = Intent(applicationContext, ZActivityDashboard::class.java)
//                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        intent.putExtra("isLoggedIn", true)
//                        intent.putExtra("pagetype", 2)
//                        startActivity(intent)
//                    }
//                    R.id.action_jobalerts ->{
//                        intent = Intent(applicationContext, ZActivityDashboard::class.java)
//                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        intent.putExtra("isLoggedIn", true)
//                        intent.putExtra("pagetype", 3)
//                        startActivity(intent)
//                    }
//                    R.id.action_settings ->{
//                        intent = Intent(applicationContext, ZActivityDashboard::class.java)
//                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        intent.putExtra("isLoggedIn", true)
//                        intent.putExtra("pagetype", 4)
//                        startActivity(intent)
//                    }
//                    R.id.action_preferences ->{
//                        intent = Intent(applicationContext, ZActivityDashboard::class.java)
//                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        intent.putExtra("isLoggedIn", true)
//                        intent.putExtra("pagetype", 5)
//                        startActivity(intent)
//                    }
//                }
//                true
//            })
//            popupMenu.show()
            intent = Intent(applicationContext, ZActivityDashboard::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("isLoggedIn", true)
            intent.putExtra("pagetype", 0)
            startActivity(intent)
        }

        button_apply.setOnClickListener {


            getListOfLocations(groupId!!, title.toString(), res_req,
                pref_req!!,ticket_typee!!)
        }

        apply_job.setOnClickListener {
            button_apply.callOnClick()
        }

        row_interested.setOnClickListener {
            row_interested.isEnabled = false
            addInterest(groupId,row_interested)
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








//        find_morejobs_details.setOnClickListener {
//            finish()
//        }

        loadAppliedJobs()


        sign_in.setOnClickListener {
            intent = Intent(applicationContext, ZActivityEventDetails::class.java)
            intent.putExtra("isLoggedIn",true)
            startActivity(intent)
        }


//        edittext_createpost.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
//                val DRAWABLE_LEFT :Int = 0;
//                val DRAWABLE_TOP :Int = 1;
//                val DRAWABLE_RIGHT: Int = 2;
//                val DRAWABLE_BOTTOM: Int = 3;
//
//                if(event!!.action == MotionEvent.ACTION_UP) {
//                    if (event.getRawX() >= (edittext_createpost.getRight() - edittext_createpost.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
//                        if (edittext_createpost.text.length>0)
//                            addPost("text")
//                        else{
//                            Toast.makeText(applicationContext,"Please enter text to post", Toast.LENGTH_LONG).show()
//                        }
//                        return true;
//                    }
//                }
//                return false;
//            }
//        })

//        starJob.setOnClickListener {
//            starJob(groupId)
//        }

//        shareJob.setOnClickListener {
//            shareJobFunction()
//        }

//        mainScroll_grpdetails.getViewTreeObserver().addOnScrollChangedListener(object :ViewTreeObserver.OnScrollChangedListener {
//            override fun onScrollChanged() {
//                Log.d("TAGG","END EFORE")
//                val view: View = mainScroll_grpdetails.getChildAt(mainScroll_grpdetails.getChildCount() - 1);
//
//                val diff:Int = (view.getBottom() - (mainScroll_grpdetails.getHeight() + mainScroll_grpdetails.getScrollY()));
//
//                if (diff == 0 && scroll ==1) {
//                    Log.d("TAGG","END"+next_page_no_posts)
//                }
//            }
//        });
//
//
//        mainScroll.getViewTreeObserver().addOnScrollChangedListener(object: ViewTreeObserver.OnScrollChangedListener {
//            override fun onScrollChanged() {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                if (mainScroll.getChildAt(0).getBottom()
//                    <= (mainScroll.getHeight() + mainScroll.getScrollY())) {
//                    //scroll view is at bottom
//                    Log.d("TAGG","END")
//                } else {
//                    //scroll view is not at bottom
//                    Log.d("TAGG","NOT END")
//
//                }
//            }
//        });

//        mRecyclerViewPosts!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                val layoutManager = GridLayoutManager::class.java.cast(recyclerView.layoutManager)
//                val visibleItemCount = layoutManager.childCount
//                val totalItemCount = layoutManager.itemCount
//                val pastVisibleItems = layoutManager.findFirstCompletelyVisibleItemPosition()
//                Toast.makeText(applicationContext,"End of List..before", Toast.LENGTH_LONG).show()
//                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
//                    // End of the list is here.
//                    Toast.makeText(applicationContext,"End of List", Toast.LENGTH_LONG).show()
//                }
//            }
//        })
        aboutevent.setTextColor(resources.getColor(R.color.green))
        agenda.setTextColor(resources.getColor(R.color.black))
        pricing.setTextColor(resources.getColor(R.color.black))
        gallery.setTextColor(resources.getColor(R.color.black))

        aboutevent.setOnClickListener{
            aboutevent.setTextColor(resources.getColor(R.color.green))
            agenda.setTextColor(resources.getColor(R.color.black))
            pricing.setTextColor(resources.getColor(R.color.black))
            gallery.setTextColor(resources.getColor(R.color.black))
            mainlayout1.visibility = View.VISIBLE
            if(itemList_Images.size>0) {
                imagegallery.visibility = View.VISIBLE
                gridview_aboutus.visibility = View.VISIBLE
            }
            else{
                imagegallery.visibility = View.GONE
                gridview_aboutus.visibility = View.GONE
            }
            pricing_text.visibility = View.VISIBLE
            mRecyclerView!!.visibility = View.VISIBLE
        }

        agenda.visibility = View.GONE
        agenda.setOnClickListener{
            aboutevent.setTextColor(resources.getColor(R.color.black))
            agenda.setTextColor(resources.getColor(R.color.green))
            pricing.setTextColor(resources.getColor(R.color.black))
            gallery.setTextColor(resources.getColor(R.color.black))
            mainlayout1.visibility = View.VISIBLE
            if(itemList_Images.size>0) {
                imagegallery.visibility = View.VISIBLE
                gridview_aboutus.visibility = View.VISIBLE
            }
            else{
                imagegallery.visibility = View.GONE
                gridview_aboutus.visibility = View.GONE
            }
            pricing_text.visibility = View.VISIBLE
            mRecyclerView!!.visibility = View.VISIBLE
        }

        pricing.setOnClickListener{
            aboutevent.setTextColor(resources.getColor(R.color.black))
            agenda.setTextColor(resources.getColor(R.color.black))
            pricing.setTextColor(resources.getColor(R.color.green))
            gallery.setTextColor(resources.getColor(R.color.black))
            mainlayout1.visibility = View.GONE
            imagegallery.visibility = View.GONE
            gridview_aboutus.visibility = View.GONE
            pricing_text.visibility = View.VISIBLE
            mRecyclerView!!.visibility = View.VISIBLE
        }

        gallery.setOnClickListener{
            aboutevent.setTextColor(resources.getColor(R.color.black))
            agenda.setTextColor(resources.getColor(R.color.black))
            pricing.setTextColor(resources.getColor(R.color.black))
            gallery.setTextColor(resources.getColor(R.color.green))
            mainlayout1.visibility = View.GONE
            if(itemList_Images.size>0) {
                imagegallery.visibility = View.VISIBLE
                gridview_aboutus.visibility = View.VISIBLE
            }
            else{
                imagegallery.visibility = View.GONE
                gridview_aboutus.visibility = View.GONE
            }
            pricing_text.visibility = View.GONE
            mRecyclerView!!.visibility = View.GONE
        }
    }






    fun shareJobFunction(){

        val s:String = title.replace(" ","-")
        val s1:String = s.replace("_","-")

        intent = Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(Intent.EXTRA_SUBJECT, title.toString()+"| JobsForHer");
        intent.putExtra(Intent.EXTRA_TEXT, "Click on the link \n http://www.jobsforher.com/events/"+s1.toLowerCase()+"/"+groupId+"\n\n"+
                "Application Link : https://play.google.com/store/apps/details?id=com.jobsforher");
        startActivity(Intent.createChooser(intent, "Share Events link!"));
    }

    fun getListOfLocations(id:Int,  title:String,  resumeRequired:Boolean, prefRequired:Boolean,
                           ticket_type:String){

        ticket_typee = ticket_type
        Log.d("URL","Inside function call")
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.GetLocationList(id, EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<EventLocationDetails> {
            override fun onResponse(call: Call<EventLocationDetails>, response: Response<EventLocationDetails>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.e("RESPONSE location", "" + Gson().toJson(response))

                if (response.isSuccessful ) {

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

                        val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                        val locations: JSONArray = jsonObject1.getJSONArray("body")
                        listOfLocations.clear()

                        for (k in 0 until locations.length()) {
                            val locModel: EventLocation = EventLocation()
                            locModel.pincode =
                                if (locations.getJSONObject(k).isNull("pincode")) "" else locations.getJSONObject(k).getString("pincode")
                            locModel.discounted_price =
                                if (locations.getJSONObject(k).isNull("discounted_price")) 0 else locations.getJSONObject(k).getInt("discounted_price")
                            locModel.event_register_start_date_time =
                                if (locations.getJSONObject(k).isNull("event_register_start_date_time")) "" else locations.getJSONObject(k).getString("event_register_start_date_time")
                            locModel.event_type =
                                if (locations.getJSONObject(k).isNull("event_type")) "" else locations.getJSONObject(k).getString("event_type")
                            locModel.event_start_date_time =
                                if (locations.getJSONObject(k).isNull("event_start_date_time")) "" else locations.getJSONObject(k).getString("event_start_date_time")
                            locModel.amount =
                                if (locations.getJSONObject(k).isNull("amount")) 0 else locations.getJSONObject(k).getInt("amount")
                            locModel.discount_start_date_time =
                                if (locations.getJSONObject(k).isNull("discount_start_date_time")) "" else locations.getJSONObject(k).getString("discount_start_date_time")
                            locModel.event_register_end_date_time =
                                if (locations.getJSONObject(k).isNull("event_register_end_date_time")) "" else locations.getJSONObject(k).getString("event_register_end_date_time")
                            locModel.discount_end_date_time =
                                if (locations.getJSONObject(k).isNull("discount_end_date_time")) "" else locations.getJSONObject(k).getString("discount_end_date_time")
                            locModel.discount_active =
                                if (locations.getJSONObject(k).isNull("discount_active")) "" else locations.getJSONObject(k).getString("discount_active")
                            locModel.address =
                                if (locations.getJSONObject(k).isNull("address")) "" else locations.getJSONObject(k).getString("address")
                            locModel.event_end_date_time =
                                if (locations.getJSONObject(k).isNull("event_end_date_time")) "" else locations.getJSONObject(k).getString("event_end_date_time")

                            locModel.id =
                                if (locations.getJSONObject(k).isNull("id")) 0 else locations.getJSONObject(k).getInt("id")
                            locModel.state =
                                if (locations.getJSONObject(k).isNull("state")) "" else locations.getJSONObject(k).getString("state")
                            locModel.seats =
                                if (locations.getJSONObject(k).isNull("seats")) "" else locations.getJSONObject(k).getString("seats")
                            locModel.event_id =
                                if (locations.getJSONObject(k).isNull("event_id")) 0 else locations.getJSONObject(k).getInt("event_id")
                            locModel.country =
                                if (locations.getJSONObject(k).isNull("country")) "" else locations.getJSONObject(k).getString("country")
                            locModel.registration_open =
                                if (locations.getJSONObject(k).isNull("registration_open")) false else locations.getJSONObject(k).getBoolean("registration_open")
                            locModel.google_map_url =
                                if (locations.getJSONObject(k).isNull("google_map_url")) "" else locations.getJSONObject(k).getString("google_map_url")
                            locModel.city =
                                if (locations.getJSONObject(k).isNull("city")) "" else locations.getJSONObject(k).getString("city")
                            locModel.external_url =
                                if (locations.getJSONObject(k).isNull("external_url")) "" else locations.getJSONObject(k).getString("external_url")

                            listOfLocations.add(locModel)
                        }

                    }

                } else {
                    // ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
                openBottomSheetLocations(id, apply_job, title, apply_job, resumeRequired, prefRequired,listOfLocations)
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
        mAdapterResume = EventLocationAdapter(dialog,listlocations,id,btnJoinGroup,title,btnJoined,resumeRequired,prefRequired,"Details")
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
                    prefRequired:Boolean,externalUrl:String){



        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.CheckPreferences("application/json",EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<CheckPreferenceResponse> {
            override fun onResponse(call: Call<CheckPreferenceResponse>, response: Response<CheckPreferenceResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful && response.body()!!.responseCode == 11405) {
                    // Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG).show()
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
                        val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                        val jsonarray_info: JSONArray = jsonObject1.getJSONArray("body")

                        var a:Int = 0
                        for (i in 0 until response.body()!!.body!!.size) {

                            val json_objectdetail: JSONObject = jsonarray_info.getJSONObject(0)
                            a = json_objectdetail.getInt("user_id")
                            Log.d("TAGG", "PREf ID:"+json_objectdetail.getInt("user_id").toString())
                        }
                        resumeRequired_onClick(a, title, event_id,location_id, resRequired,prefRequired)
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
                        val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                        val jsonarray_info: JSONArray = jsonObject1.getJSONArray("body")
                        listOfResumes.clear()
                        for (i in 0 until response.body()!!.body!!.size) {

                            val json_objectdetail: JSONObject = jsonarray_info.getJSONObject(i)

                            val model: ResumeView = ResumeView();
                            model.is_default = ""//json_objectdetail.getString("is_default")
                            model.path  =json_objectdetail.getString("path")
                            model.id = json_objectdetail.getInt("id")
                            model.title = if(json_objectdetail.isNull("title"))"" else json_objectdetail.getString("title")
                            model.is_parsed = false//json_objectdetail.getBoolean("is_parsed")
                            model.created_on = json_objectdetail.getString("created_on")
                            model.modified_on = json_objectdetail.getString("modified_on")
                            model.deleted  =json_objectdetail.getBoolean("deleted")
                            model.user_id = json_objectdetail.getInt("user_id")

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
                    //  ToastHelper.makeToast(applicationContext, "Invalid Request")
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
            if (listOfResumes.size>0) {
                //if(isSelected) {
                Toast.makeText(applicationContext, "Applied", Toast.LENGTH_LONG).show()
                saveEvent(jobId, note.text.toString(), listOfResumes[0].id, "registered",location_id)
                dialog.cancel()
//                    }
//                    else{
//                        Toast.makeText(applicationContext,"Please select a resume",Toast.LENGTH_LONG).show()
//                    }
            }
            else{
                sendResume(restitle.getText().toString(),jobId,location_id)
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
                Toast.makeText(applicationContext, "Applied", Toast.LENGTH_LONG).show()
                saveEvent(jobId, note.text.toString(), listOfResumes[0].id, "registered",location_id)
                dialog.cancel()
//                    }
//                    else{
//                        Toast.makeText(applicationContext,"Please select a resume",Toast.LENGTH_LONG).show()
//                    }
            } else {
                sendResume(restitle.getText().toString(), jobId,location_id)
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
//        val jobtype_multiAutoCompleteTextView = dialog.findViewById(R.id.jobtype_multiAutoCompleteTextView) as MultiAutoCompleteTextView
//        val randomArray = ArrayAdapter(this, android.R.layout.simple_list_item_1, listOfEventType)
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
//                    layout_0.visibility = View.GONE
//                    layout_1.visibility = View.VISIBLE
                    //Logger.e("TAGG", "" + response.body()!!.message.toString())

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
                        val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                        val jsonarray_info: JSONObject = jsonObject1.getJSONObject("body")

                        var a:Int = 0
                        var b: String=""
                        var hash:String = ""
                        var uname:String = ""
                        var amt:Int = 0
                        var email:String = ""
                        var uphone:String = ""
                        var txnid:Int = 0
                        var productinfo:Int = 0
                        val discount:Int =0
                        //for (i in 0 until response.body()!!.body!!.) {

                        //val json_objectdetail: JSONObject = jsonarray_info.getJSONObject(0)

                        b = ticket_typee //jsonarray_info.getString("ticket_type")
                        if(b.equals("paid")){
                            hash = jsonarray_info.getString("hash_key")
                            uname = jsonarray_info.getString("username")
                            if(jsonarray_info.isNull("discount")||jsonarray_info.getInt("discount")==0)
                                amt = jsonarray_info.getInt("amount")
                            else
                                amt = jsonarray_info.getInt("discount")
                            email = jsonarray_info.getString("email")
                            uphone = jsonarray_info.getString("phone_no")
                            txnid = jsonarray_info.getInt("txnid")
                            productinfo = jsonarray_info.getInt("productinfo")

                        }
                        Log.d("TAGG", "Reg ID:"+jsonarray_info.getInt("id").toString()+"  "+b)
                        // }
                        if(b.equals("free")) {
                            a = jsonarray_info.getInt("id")
                            successEvent(a, b)
                        }
                        else if(b.equals("other")) {
                            a = jsonarray_info.getInt("id")
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
                        val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                        val jsonarray_info: JSONObject = jsonObject1.getJSONObject("body")
                        var json_objectdetail: JSONObject =jsonarray_info
                        if (json_objectdetail.length() ==0 ){
                            return
                        }
                        val s:String = "Your registration for "+ json_objectdetail.getString("title")+" events is successfully booked!"
                        dis_text.setText(s)
                        val json: JSONObject = json_objectdetail.getJSONObject("registerred_event_locations")
                        event_date_success.setText( getDate(json.getString("event_start_date_time"))+" to "+
                                getDate(json.getString("event_end_date_time")))
                        event_time_success.setText(getTime(json.getString("event_start_date_time"))+" to "+
                                getTime(json.getString("event_end_date_time")))
                        event_venue.setText(json.getString("city"))
                        if (json_objectdetail.getString("image_url").length>0) {
                            Picasso.with(applicationContext)
                                .load(json_objectdetail.getString("image_url"))
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .into(event_logo_success)
                        }
                        else{
                            Picasso.with(applicationContext)
                                .load(R.drawable.ic_launcher_foreground)
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .into(event_logo_success)
                            event_logo_success.visibility = View.GONE
                        }

                        val extrUrRL:String =  if (json.isNull("external_url")) "" else json.getString("external_url")
                        //   ToastHelper.makeToast(applicationContext, extrUrRL)
                        layout_0.visibility = View.GONE
                        button_action.visibility = View.GONE
                        layout_1.visibility = View.VISIBLE
                        Logger.e("TAGG", "" + response.body()!!.message.toString())
                        if(extrUrRL.length>0) {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(extrUrRL))
                            startActivity(browserIntent)
                        }
                    }


                } else {
                    //ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<EventRegApiDetails>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
            }
        })
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


        //val txn:Int = txnid.toInt()
//        val parser :JSONParser =  JSONParser
//        val json :JSONObject= parser.parse(response)
        //val j: JSONObject = JSONObject(params)


        Log.d("TAGG","PARAMS"+params+ EndPoints.ACCESS_TOKEN)
        Log.d("TAGG", "TOKEN"+EndPoints.ACCESS_TOKEN.toString())

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

                    Logger.d("MESSAGE", response.message() + "")
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


    fun followCompany(id:Int, btnJoinGroup:Button, btnApplied: Button, status: String){

        Logger.d("CODE",id.toString() + "")

        val params = HashMap<String, String>()

        params["entity_id"] = id.toString()
        params["entity_type"] = "company"
        params["status"] = status

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.FollowCompayny(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)

        call.enqueue(object : Callback<UpdateReplyResponse> {
            override fun onResponse(call: Call<UpdateReplyResponse>, response: Response<UpdateReplyResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE follow company", "" + Gson().toJson(response))

                if (response.isSuccessful) {
//                    Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                    if (status.compareTo("follow")==0) {
                        btnJoinGroup.visibility = View.GONE
                        btnApplied.visibility = View.VISIBLE
                        Snackbar.make(
                            main,
                            "Thank you for following "+compname.text,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    else{
                        btnJoinGroup.visibility = View.VISIBLE
                        btnApplied.visibility = View.GONE
                        Snackbar.make(
                            main,
                            "You have successfully unfollowed the company",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }


                } else {
                    // ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())


                }
            }

            override fun onFailure(call: Call<UpdateReplyResponse>, t: Throwable) {
                Logger.d("TAGG", "Apply Job FAILED : $t")
            }
        })
    }

//

    private fun showPictureDialog() {

        val chooseFile: Intent
        val intent: Intent
        chooseFile = Intent(Intent.ACTION_GET_CONTENT)
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE)
        chooseFile.type = "application/pdf"
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
                Logger.d("URL", "" + response+ EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
                var str_response = Gson().toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                val jsonObject1 : JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                // var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                if(response.isSuccessful){
                    Toast.makeText(applicationContext,"Adding Success!!", Toast.LENGTH_LONG).show()
                    val resumeId  = 200

                    saveEvent(jobId,"",resumeId, "registered",location_id)
                }
                else {
                    ToastHelper.makeToast(applicationContext, "message")
                }
            }
            override fun onFailure(call: Call<UpdateReplyResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext,"Adding Failed!!", Toast.LENGTH_LONG).show()

            }
        })
    }

    fun saveJob(job_id:Int,note:String,resume_id:Int,applied_status:String){

        val params = HashMap<String, String>()

        params["job_id"] = job_id.toString()
        params["note"] = note
        params["resume_id"] = resume_id.toString()
        params["applied_status"] = applied_status

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
                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                    Logger.e("TAGG", "" + response.body()!!.message.toString())

                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<SaveJobResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
            }
        })
    }



//    fun sharegroupdetails(){
//
//        intent = Intent(android.content.Intent.ACTION_SEND);
//        intent.setType("text/plain");
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//        intent.putExtra(Intent.EXTRA_SUBJECT, groupname.text.toString()+"| JobsForHer");
//        intent.putExtra(Intent.EXTRA_TEXT, "Click on the link \n http://www.workingnaari.in/groups"+"\n\n" +"Group Name: "+groupname.text.toString());
//        startActivity(Intent.createChooser(intent, "Share Group link!"));
//    }


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

                }
                else
                    Toast.makeText(applicationContext,"No data exists",Toast.LENGTH_LONG).show()
            }
            override fun onFailure(call: Call<GetMobilityResponse>, t: Throwable) {
                Logger.d("TAG", "FAILED : $t")
            }
        })
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

                }
                else
                    Toast.makeText(applicationContext,"No data exists",Toast.LENGTH_LONG).show()
            }
            override fun onFailure(call: Call<GetMobilityResponse>, t: Throwable) {
                Logger.d("TAG", "FAILED : $t")
            }
        })
        loadCityData()
    }

    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?)  {

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

        if (requestCode == PAYMENT_CODE){

            if (resultCode == 1) {
//                Log.d("TAGG", "Success - Payment ID : " + data!!.getExtras().getString("transaction_id")+" , "
//                        +data!!.getExtras().getBoolean("status"));
                Log.d("TAGG", "Success - Payment : " + data!!.getExtras()!!.getString("result")+ "TXNID: "+
                        data!!.getExtras()!!.getString("txnid")+"HASH: "+data!!.getExtras()!!.getString("hash")!!);
                updatePaymentEvent(data!!.getExtras()!!.getString("result")!!,data!!.getExtras()!!.getString("hash")!!,
                    data!!.getExtras()!!.getString("hash")!!)
                // successEvent(data!!.getExtras().getInt("id"),"paid")

            }
            else if(resultCode == 0){
                Log.d("TAGG", "Failure - Payment ID : " + data!!.getExtras()!!.getString("transaction_id")+" , "
                        +data!!.getExtras()!!.getBoolean("status")!!);
            }
            else{
                Log.d("TAGG", "Cancelled")
            }
            // successEvent()

        }
    }

//    private fun addPost(post_type: String){
//
//        val params = HashMap<String, String>()
//        var msg: String=""
//        val description = edittext_createpost.text.toString()
//        val pinned_post = "false"
//        val post_type = post_type
//
//
//        params["description"] = description
//        params["post_type"] = post_type
//        if (post_type.equals("text")) {
//            params["image_filename"] = ""
//            params["image_file"] = ""
//            params["url"]= ""
//        }
//
//
//
//        retrofitInterface_post = RetrofitClient.client!!.create(RetrofitInterface::class.java)
//        val call = retrofitInterface_post!!.addPost(
//            Integer.parseInt(groupId.toString()),
//            "application/json",
//            EndPoints.CLIENT_ID,
//            "Bearer "+EndPoints.ACCESS_TOKEN,
//            params
//        )
//        call.enqueue(object : Callback<CreatePostResponse> {
//
//            override fun onResponse(call: Call<CreatePostResponse>, response: Response<CreatePostResponse>) {
//
//                Log.d("TAG", "CODE Value" + response.code().toString() + "")
//                Log.d("TAG", "MESSAGE VAlue" + response.message() + "")
//                Log.d("TAG", "RESPONSE Value" + "" + Gson().toJson(response))
//                Log.d("TAGG", "URL Value" + "" + response.raw().request().url())
//
//                //var str_response = Gson().toJson(response)
//                val gson = GsonBuilder().serializeNulls().create()
//                var str_response = gson.toJson(response)
//                val jsonObject: JSONObject =
//                    JSONObject(str_response.substring(str_response.indexOf("{"), str_response.lastIndexOf("}") + 1))
//                val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
//                val responseCode: Int = jsonObject1.getInt("response_code")
//                val message: String = jsonObject1.getString("message")
//                var jsonaobj: JSONObject = jsonObject1.getJSONObject("body")
//                if (response.isSuccessful) {
//                    if(responseCode==10200) {
//                        //mDataList[position].comment_list!! =
//                        Log.d("TAGG", "DATA" + jsonaobj.getString("id"))
//                        Toast.makeText(applicationContext,"Post Created!!", Toast.LENGTH_LONG).show()
//                        edittext_createpost.text.clear()
//                        //finish()
//                    }
//                    else{
//
//                    }
//                    //finish()
//                } else {
//
//                }
//            }
//
//            override fun onFailure(call: Call<CreatePostResponse>, t: Throwable) {
//
//                Logger.d("TAGG", "FAILED : $t")
//            }
//        })
//    }

    fun joinGroup(id:Int, btnJoinGroup:Button,type:String, btnApplied: Button){

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.JoinGroup(id,EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<JoinGroupResponse> {
            override fun onResponse(call: Call<JoinGroupResponse>, response: Response<JoinGroupResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful && response.body()!!.responseCode == 10104) {
                    if (type.equals("private")){
                        btnJoinGroup.text = "Requested"
                    }
                    else{
//                        btnJoinGroup.visibility =View.GONE
//                        btnApplied.visibility = View.VISIBLE
                        finish()
                        val intent = Intent(applicationContext, ZActivityEventDetails::class.java)
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

    fun loadEventDetailsData(){

        Log.d("TAGG", "Inside")
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getEventDetailsData(groupId,EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)
        Logger.d("URL", "" + "HI" +groupId)
        call.enqueue(object : Callback<EventApiDetails> {
            override fun onResponse(call: Call<EventApiDetails>, response: Response<EventApiDetails>) {

                Logger.d("URL", "JOBS" + response+ EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "JOB DETAILS" + Gson().toJson(response))

                val maxLogSize = 1000
                val stringLength = Gson().toJson(response).length
                for (i in 0..stringLength / maxLogSize) {
                    val start = i * maxLogSize
                    var end = (i + 1) * maxLogSize
                    end = if (end > Gson().toJson(response).length) Gson().toJson(response).length else end
                    Log.d("YOURTAG", Gson().toJson(response).substring(start, end))
                }

//                //var str_response = Gson().toJson(response)
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                val jsonObject1 : JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                val jsonarray_info: JSONArray = jsonObject1.getJSONArray("body")
                //val json_objectdetail: JSONObject = jsonObject1.getJSONObject("body")

                if (response.isSuccessful) {

                    var json_objectdetail: JSONObject =jsonarray_info.getJSONObject(0)
                    val model: EventsView= EventsView()

                    var locations: JSONArray
                    var locdata: ArrayList<EventLocation> = ArrayList()
                    if(json_objectdetail.isNull("events_locations")){}
                    else {
                        locations = json_objectdetail.getJSONArray("events_locations")

                        for (k in 0 until locations.length()) {
                            val locModel: EventLocation = EventLocation()
                            locModel.pincode =
                                if (locations.getJSONObject(k).isNull("pincode")) "" else locations.getJSONObject(k).getString("pincode")
                            locModel.discounted_price =
                                if (locations.getJSONObject(k).isNull("discounted_price")) 0 else locations.getJSONObject(k).getInt("discounted_price")
                            locModel.event_register_start_date_time =
                                if (locations.getJSONObject(k).isNull("event_register_start_date_time")) "" else locations.getJSONObject(k).getString("event_register_start_date_time")
                            locModel.event_type =
                                if (locations.getJSONObject(k).isNull("event_type")) "" else locations.getJSONObject(k).getString("event_type")
                            locModel.event_start_date_time =
                                if (locations.getJSONObject(k).isNull("event_start_date_time")) "" else locations.getJSONObject(k).getString("event_start_date_time")
                            locModel.amount =
                                if (locations.getJSONObject(k).isNull("amount")) 0 else locations.getJSONObject(k).getInt("amount")
                            locModel.discount_start_date_time =
                                if (locations.getJSONObject(k).isNull("discount_start_date_time")) "" else locations.getJSONObject(k).getString("discount_start_date_time")
                            locModel.event_register_end_date_time =
                                if (locations.getJSONObject(k).isNull("event_register_end_date_time")) "" else locations.getJSONObject(k).getString("event_register_end_date_time")
                            locModel.discount_end_date_time =
                                if (locations.getJSONObject(k).isNull("discount_end_date_time")) "" else locations.getJSONObject(k).getString("discount_end_date_time")
                            locModel.discount_active =
                                if (locations.getJSONObject(k).isNull("discount_active")) "" else locations.getJSONObject(k).getString("discount_active")
                            locModel.address =
                                if (locations.getJSONObject(k).isNull("address")) "" else locations.getJSONObject(k).getString("address")
                            locModel.event_end_date_time =
                                if (locations.getJSONObject(k).isNull("event_end_date_time")) "" else locations.getJSONObject(k).getString("event_end_date_time")

                            locModel.id =
                                if (locations.getJSONObject(k).isNull("id")) 0 else locations.getJSONObject(k).getInt("id")
                            locModel.state =
                                if (locations.getJSONObject(k).isNull("state")) "" else locations.getJSONObject(k).getString("state")
                            locModel.seats =
                                if (locations.getJSONObject(k).isNull("seats")) "" else locations.getJSONObject(k).getString("seats")
                            locModel.event_id =
                                if (locations.getJSONObject(k).isNull("event_id")) 0 else locations.getJSONObject(k).getInt("event_id")
                            locModel.country =
                                if (locations.getJSONObject(k).isNull("country")) "" else locations.getJSONObject(k).getString("country")
                            locModel.registration_open =
                                if (locations.getJSONObject(k).isNull("registration_open")) false else locations.getJSONObject(k).getBoolean("registration_open")
                            locModel.google_map_url =
                                if (locations.getJSONObject(k).isNull("google_map_url")) "" else locations.getJSONObject(k).getString("google_map_url")
                            locModel.city =
                                if (locations.getJSONObject(k).isNull("city")) "" else locations.getJSONObject(k).getString("city")
                            locModel.external_url =
                                if (locations.getJSONObject(k).isNull("external_url")) "" else locations.getJSONObject(k).getString("external_url")

                            locdata.add(locModel)
                        }

                    }

                    model.event_locations= locdata

                    if (model!!.event_locations!!.size>0) {
//snehacomment
//                        val N = model!!.event_locations!!.size
//                        val myTextViews = arrayOfNulls<TextView>(N)
//                        var l = 0
//                        for (k in 0 until model!!.event_locations!!.size) {
//
//                            val rowTextView = TextView(applicationContext)
////                            rowTextView.setTypeface(rowTextView.getTypeface(), Typeface.BOLD)
////
////                            rowTextView.text =
////                                    Html.fromHtml("\"<a href='https://www.google.com/maps/search/?api=1&query=36.26577,-92.54324'>Google.com</a>\"")//"https://www.google.com/maps/search/?api=1&query=36.26577,-92.54324"
//
////                            model.event_locations!![k].google_map_url
//                            //val img = getContext().resources.getDrawable(R.drawable.ic_location)
//                           // var msg:String ="  "+model!!.event_locations!![k].city+"\n"+model!!.event_locations!![k].google_map_url
//                            var mImageSpan: ImageSpan= ImageSpan(applicationContext, R.drawable.ic_location);
//                            var text : SpannableString = SpannableString(model!!.event_locations!![k].google_map_url);
//                            text.setSpan(mImageSpan, 0, 1, 0);
//                            rowTextView.text = text
//                            rowTextView.isClickable = true
////                            val rowTextViewAddress = TextView(applicationContext)
////                            rowTextViewAddress.text =
////                                listOflocations[k].address + "\n" + listOflocations[k].address_2 + " " + listOflocations[k].city_name + "-" + listOflocations[k].pincode + "\n"
//                            rowTextView.setMovementMethod(LinkMovementMethod.getInstance());
//                            locationlist.addView(rowTextView)
////                            address_layout3.addView(rowTextViewAddress)
//                            rowTextView.setOnClickListener {
//                                val urlString:String  = model!!.event_locations!![k].google_map_url.toString()
//                                intent =  Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                intent.setPackage("com.google.android.gms.maps");
//                                try {
//                                    context.startActivity(intent);
//                                } catch ( ex: ActivityNotFoundException) {
//                                    // Chrome browser presumably not installed so allow user to choose instead
//                                    intent.setPackage(null);
//                                    context.startActivity(intent);
//                                }
//                            }
//
//                            myTextViews[l] = rowTextView
////                            myTextViews[l++] = rowTextView
//                            l++
//
//                        }
                        //sneh close
                    }

                    var companies: JSONArray
                    var compdata: JSONArray
                    if(json_objectdetail.isNull("link_companies_name")){
                        event_company.visibility = View.GONE
                    }
                    else {
                        var s:String = ""
                        compdata = json_objectdetail.getJSONArray("link_companies_name")
                        for (k in 0 until compdata.length()) {
                            s = s+ compdata.getJSONObject(k).getString("company_name")+","

                        }
                        Log.d("TAGG","Comp names"+s)
                        event_company.setText(s.substring(0,s.length-1))
                    }

                    var images: JSONArray
                    var imagesdata: ArrayList<EventImages> = ArrayList()
                    if(json_objectdetail.isNull("events_images")){

                    }
                    else {
                        images = json_objectdetail.getJSONArray("events_images")

                        for (k in 0 until images.length()) {
                            val imageModel: EventImages = EventImages()
                            imageModel.image_url = if (images.getJSONObject(k).isNull("image_url")) "" else images.getJSONObject(k).getString("image_url")
                            imageModel.created_on = if (images.getJSONObject(k).isNull("created_on")) "" else images.getJSONObject(k).getString("created_on")
                            imageModel.event_id = if (images.getJSONObject(k).isNull("event_id")) 0 else images.getJSONObject(k).getInt("event_id")
                            imageModel.modified_on = if (images.getJSONObject(k).isNull("modified_on")) "" else images.getJSONObject(k).getString("modified_on")
                            imageModel.id = if (images.getJSONObject(k).isNull("id")) 0 else images.getJSONObject(k).getInt("id")


                            imagesdata.add(imageModel)
                        }
                        model.event_images = imagesdata

                        if(model.event_images!!.size>0) {

                            for (i in 0 until model!!.event_images!!.size) {
                                itemList_Images.add(model!!.event_images!![i].image_url!!.toString())
                                itemList_Images.add(model!!.event_images!![i].image_url!!.toString())
                            }
                        }
                        else{
                            gridview_abutus!!.visibility = View.GONE
                            imagegallery.visibility = View.GONE
                        }

                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 2)
                        gridview_abutus!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                        aboutUsAdapter = ImagesAdapter(itemList_Images, isLoggedIn)
                        gridview_abutus!!.adapter = aboutUsAdapter
                    }
                    if(itemList_Images.size>0){
                        gallery.visibility = View.VISIBLE
                    }
                    else
                        gallery.visibility = View.GONE



                    model.interested =if (json_objectdetail.isNull("interested")) false else json_objectdetail.getBoolean("interested")
                    model.registered =if (json_objectdetail.isNull("registered")) false else json_objectdetail.getBoolean("registered")
                    model.company_name =if (json_objectdetail.isNull("company_name")) "" else json_objectdetail.getString("company_name")
                    model.featured_end_date_time =if (json_objectdetail.isNull("featured_end_date_time")) "" else json_objectdetail.getString("featured_end_date_time")
                    model.preference_required =if (json_objectdetail.isNull("preference_required")) false else json_objectdetail.getBoolean("preference_required")
                    model.terms_and_conditions =if (json_objectdetail.isNull("terms_and_conditions")) "" else  json_objectdetail.getString("terms_and_conditions")
                    model.featured_start_date_time =if (json_objectdetail.isNull("featured_start_date_time")) "" else  json_objectdetail.getString("featured_start_date_time")
                    model.featured_event =if (json_objectdetail.isNull("featured_event")) false else  json_objectdetail.getBoolean("featured_event")
                    model.faq = if (json_objectdetail.isNull("faq")) "" else json_objectdetail.getString("faq")

                    model.resume_required =if (json_objectdetail.isNull("resume_required")) false else json_objectdetail.getBoolean("resume_required")
                    model.ticket_type =if (json_objectdetail.isNull("ticket_type")) "" else json_objectdetail.getString("ticket_type")
                    model.seats =if (json_objectdetail.isNull("seats")) "" else  json_objectdetail.getString("seats")
                    model.event_type =if (json_objectdetail.isNull("event_type")) "" else  json_objectdetail.getString("event_type")
                    model.display_price =if (json_objectdetail.isNull("display_price")) 0 else  json_objectdetail.getInt("display_price")
                    model.agenda = if (json_objectdetail.isNull("agenda")) "" else json_objectdetail.getString("agenda")
                    ticket_typee = model.ticket_type.toString()
                    pref_req = model.preference_required!!
                    res_req = model.resume_required!!
                    if(model.ticket_type.equals("paid")) {
                        event_price.text =
                            "\u20B9" + (if (json_objectdetail.isNull("price_after_discount")) json_objectdetail.getInt("price_before_discount") else json_objectdetail.getInt(
                                "price_after_discount"
                            )).toString() + " Onwards"
                        event_original_price.text =
                            "\u20B9" + (if (json_objectdetail.isNull("price_before_discount")) 0 else json_objectdetail.getInt(
                                "price_before_discount"
                            )).toString()
                        event_original_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
                        if (json_objectdetail.isNull("price_after_discount")) {
                            event_original_price.visibility = View.GONE
                        }
                    }
                    else{
                        event_original_price.visibility = View.GONE
                        event_price.visibility = View.GONE
                    }



                    model.modified_by =if (json_objectdetail.isNull("modified_by")) "" else json_objectdetail.getString("modified_by")
                    model.interested_count =if (json_objectdetail.isNull("interested_count")) 0 else json_objectdetail.getInt("interested_count")
                    model.view_count =if (json_objectdetail.isNull("view_count")) 0 else  json_objectdetail.getInt("view_count")
                    model.share_count =if (json_objectdetail.isNull("share_count")) 0 else  json_objectdetail.getInt("share_count")
                    model.payment =if (json_objectdetail.isNull("payment")) false else  json_objectdetail.getBoolean("payment")
                    model.status = if (json_objectdetail.isNull("status")) "" else json_objectdetail.getString("status")

                    model.priority_order = if (json_objectdetail.isNull("priority_order")) false else json_objectdetail.getBoolean("priority_order")
                    model.excerpt =if (json_objectdetail.isNull("excerpt")) "" else json_objectdetail.getString("excerpt")
                    model.event_register_start_date_time =if (json_objectdetail.isNull("event_register_start_date_time")) "" else json_objectdetail.getString("event_register_start_date_time")
                    model.created_on =if (json_objectdetail.isNull("created_on")) "" else json_objectdetail.getString("created_on")
                    model.author_id =if (json_objectdetail.isNull("author_id")) "" else json_objectdetail.getString("author_id")
                    model.is_online =if (json_objectdetail.isNull("is_online")) false else json_objectdetail.getBoolean("is_online")

                    model.gtm_id =if (json_objectdetail.isNull("gtm_id")) 0 else json_objectdetail.getInt("gtm_id")
                    model.author_name =if (json_objectdetail.isNull("author_name")) "" else json_objectdetail.getString("author_name")
                    model.is_private =if (json_objectdetail.isNull("is_private")) false else json_objectdetail.getBoolean("is_private")
                    model.register_count =if (json_objectdetail.isNull("register_count")) "" else json_objectdetail.getString("register_count")
                    model.title =if (json_objectdetail.isNull("title")) "" else json_objectdetail.getString("title")
                    model.event_end_date_time =if (json_objectdetail.isNull("event_end_date_time")) "" else json_objectdetail.getString("event_end_date_time")

                    model.slug =if (json_objectdetail.isNull("slug")) "" else json_objectdetail.getString("slug")
                    model.event_register_end_date_time =if (json_objectdetail.isNull("event_register_end_date_time")) "" else json_objectdetail.getString("event_register_end_date_time")
                    model.event_by =if (json_objectdetail.isNull("event_by")) "" else json_objectdetail.getString("event_by")
                    model.id =if (json_objectdetail.isNull("id")) 0 else json_objectdetail.getInt("id")
                    model.post_for =if (json_objectdetail.isNull("post_for")) "" else json_objectdetail.getString("post_for")
                    model.publish_date =if (json_objectdetail.isNull("publish_date")) "" else json_objectdetail.getString("publish_date")

                    model.event_start_date_time =if (json_objectdetail.isNull("event_start_date_time")) ""  else json_objectdetail.getString("event_start_date_time")
                    model.created_by =if (json_objectdetail.isNull("created_by")) 0  else json_objectdetail.getInt("created_by")
                    model.company_id =if (json_objectdetail.isNull("company_id")) 0  else json_objectdetail.getInt("company_id")
                    model.address =if (json_objectdetail.isNull("address")) ""  else json_objectdetail.getString("address")
                    model.modified_on =if (json_objectdetail.isNull("modified_on")) ""  else json_objectdetail.getString("modified_on")
                    model.payment_note =if (json_objectdetail.isNull("payment_note")) ""  else json_objectdetail.getString("payment_note")

                    model.description =if (json_objectdetail.isNull("description")) "" else json_objectdetail.getString("description")
                    model.city =if (json_objectdetail.isNull("city")) "" else json_objectdetail.getString("city")
                    model.event_url =if (json_objectdetail.isNull("event_url")) "" else json_objectdetail.getString("event_url")
                    model.show_on_search =if (json_objectdetail.isNull("show_on_search")) false else json_objectdetail.getBoolean("show_on_search")
                    model.image_url =if (json_objectdetail.isNull("image_url")) "" else json_objectdetail.getString("image_url")
                    model.thumbnail_url =if (json_objectdetail.isNull("thumbnail_url")) "" else json_objectdetail.getString("thumbnail_url")


                    start_date = model.event_start_date_time!!
                    end_date = model.event_end_date_time!!
                    name= model.title!!
                    address = model.event_locations!![0].city!!


                    if(model.title!!.length>0) {
                        if (model.featured_event == true) {
                            val msg: String = model.title + "   "
                            val mImageSpan: ImageSpan = ImageSpan(applicationContext, R.drawable.ic_group_featured);
                            val text: SpannableString = SpannableString(msg);
                            text.setSpan(mImageSpan, msg.length - 1, msg.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            compname.setText(text);
                        } else {
                            compname.setText(model.title)
                        }
                    }
                    else
                        compname.visibility = View.GONE
                    // event_company.setText(model.company_name)
                    event_date.setText(getDate(model.event_start_date_time!!)+" to "+getDate(model.event_end_date_time!!))
                    event_time.setText(getTime(model.event_start_date_time!!)+" to "+getTime(model.event_end_date_time!!))
                    eventlocation.setText(model.city)

                    var s:String = ""
                    for(i in 0 until model.event_locations!!.size){
                        s = s+ model.event_locations!![i].city.toString()+","
                    }
                    eventlocation.setText(s.substring(0,s.length-1))

                    if(model.description!!.length>0) {
                        //   abutus.setText("About " + model.title)
//                        var s :String = model.description!!.replace("&lt;", "<")
//                        var d :String = s.replace("&gt;",">")
//                            .replace("lsquo;","'")
//                            .replace("rsquo;","'")
//                            .replace("nbsp;"," ")
//                            .replace("&amp;","")
//                        abutus_text.setText(Html.fromHtml(d))

//                        abutus_web.setInitialScale(1);
//                        abutus_web.getSettings().setJavaScriptEnabled(true);
//                        abutus_web.getSettings().setPluginState(WebSettings.PluginState.ON);
//                        abutus_web.getSettings().setLoadWithOverviewMode(true);
//                        abutus_web.getSettings().setUseWideViewPort(true);
//                        abutus_web.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
//                        abutus_web.setScrollbarFadingEnabled(false);
                        abutus_web.loadDataWithBaseURL("", model.description!!, "text/html", "UTF-8", "");
                    }
                    else{
                        abutus.visibility = View.GONE
                        abutus_text.visibility = View.GONE
                    }

                    Log.d("TAGG", "IMAGE"+model.thumbnail_url!!)
                    if(model.image_url!!.length>0) {
                        event_logo.visibility = View.VISIBLE
                        Picasso.with(applicationContext)
                            .load(model.image_url!!)
                            .into(event_logo)
                    }
                    else {
                        event_logo.visibility = View.VISIBLE
                        Picasso.with(applicationContext)
                            .load(R.drawable.event_default_banner)
                            .into(event_logo)
                    }

                    if(model.thumbnail_url!!.length>0) {
                        event_logo.visibility = View.VISIBLE
                        Picasso.with(applicationContext)
                            .load(model.thumbnail_url)
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .into(event_logo1)
                    }
//                    else
//                        event_logo.visibility = View.GONE
                    if(model.interested!!) {
                        row_interested.setTextColor(Color.parseColor("#B2B2B2"))
                        row_interested.isEnabled = false
                    }
                    else{
                        row_interested.setTextColor(Color.parseColor("#99CA3B"))
                        row_interested.isEnabled = true
                    }

                    if(model.registered!!) {
                        btnApplied.visibility = View.VISIBLE
                        apply_job.visibility = View.GONE
                        button_apply.visibility = View.GONE
                        button_applied.visibility = View.VISIBLE
                        row_interested.setTextColor(Color.parseColor("#B2B2B2"))
                        row_interested.isEnabled = false
                    }
                    else{
                        btnApplied.visibility = View.GONE
                        apply_job.visibility = View.VISIBLE
                        button_apply.visibility = View.VISIBLE
                        button_applied.visibility = View.GONE

                    }


                    Log.d("TAGG","DATE IS"+model.event_register_end_date_time.toString())
                    if(compare(model.event_locations!![0].event_register_end_date_time.toString())){
                        apply_job.visibility = View.GONE
                        btnApplied.visibility = View.GONE
                        button_apply.visibility = View.GONE
                        button_applied.visibility = View.GONE
                        registration_closed.visibility = View.VISIBLE
                        if(typee==2)
                            row_interested.visibility = View.GONE
                    }

                    mRecyclerView = findViewById(R.id.my_recycler_view)
                    val mLayoutManager = GridLayoutManager(
                        applicationContext,2, GridLayoutManager.HORIZONTAL,
                        false
                    )
                    mRecyclerView!!.isHorizontalScrollBarEnabled= false
                    mRecyclerView!!.layoutManager = mLayoutManager
                    mAdapter = EventLocation1Adapter(locdata)
                    mRecyclerView!!.adapter = mAdapter

                } else {
                    // ToastHelper.makeToast(applicationContext, message)
                    finish()
                    overridePendingTransition(0, 0);
                }
            }

            override fun onFailure(call: Call<EventApiDetails>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })
//        listOfMyGroupdata.add(
//            GroupsView(0,"Women Freelancers","Working Women, Women Professionals","","Open Group","9652",
//                "Wish to work as per your time of convenience? " +
//                        "Come aboard this group of freelancers to stay in the know of all things that matter in your world.")
//        )
    }

    fun getTime(times:String):String{
        val formatter1: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        val dateInString:String = times
        val formatterOut: SimpleDateFormat = SimpleDateFormat("hh:mm a");
        var date : Date? = null
        var s: String=""
        try {
            date=formatter1.parse(dateInString)
            System.out.println(formatterOut.format(date));
            s = formatterOut.format(date)

        } catch ( e: ParseException1) {
            e.printStackTrace()
        }
        return s
    }

    fun getDate(times:String):String{
        val formatter1: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        val dateInString:String = times
        val formatterOut: SimpleDateFormat = SimpleDateFormat("dd MMM yyyy");
        var date : Date? = null
        var s: String=""
        try {
            date=formatter1.parse(dateInString)
            System.out.println(formatterOut.format(date));
            s = formatterOut.format(date)

        } catch ( e: ParseException1) {
            e.printStackTrace()
        }
        return s
    }

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

    fun loadAppliedJobs(){

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
                val jsonObject1 : JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                val jsonarray_info: JSONArray = jsonObject1.getJSONArray("body")
                Log.d("TAGG","Applied "+jsonarray_info.toString())

                if (response.isSuccessful) {

                    for(l in 0 until response.body()!!.body!!.size) {
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
                        Log.d("TAGG", model.job_id!!.toString()+" JOB ID")
                        listOfCompareJoineddata.add(model.job_id!!)
                    }
                    loadMyGroupData()

                } else {
                    // ToastHelper.makeToast(applicationContext, message)
                    loadMyGroupData()
                }
            }

            override fun onFailure(call: Call<AppliedJobDetails>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                loadMyGroupData()
            }
        })
    }

    fun compare(date:String):Boolean{
        val sdformat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dateobj = Date()
        val d1 = sdformat.parse(date)                           //event date
        val d2 = sdformat.parse(sdformat.format(dateobj))       //current date

        if (d1.compareTo(d2) > 0) {
            Log.d("TAGG","Date 1 occurs after Date 2")
            return false
        } else if (d1.compareTo(d2) < 0) {
            Log.d("TAGG","Date 1 occurs before Date 2")
            return true
        } else {
            Log.d("TAGG","Both dates are equal")
            return false
        }
    }

    fun loadMyGroupData(){

        val params = HashMap<String, String>()

        params["page_no"] = "1"
        params["page_size"] = 30.toString()

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface!!.getMyGroupData( EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        call.enqueue(object : Callback<Featured_Group> {
            override fun onResponse(call: Call<Featured_Group>, response: Response<Featured_Group>) {

                Logger.d("URL", "" + response+ EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE COMPANIES", "" + Gson().toJson(response))
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)

                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
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
                    if (Integer.parseInt(pagenoo)>1)
                        prev_page = Integer.parseInt(pagenoo)-1
                    else
                        prev_page = 0

                    if (response.isSuccessful) {

                        //ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
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
                            val categoriesArray: JSONArray = json_objectdetail.getJSONArray("categories")
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
                        loadEventDetailsData()
                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid Request")

                    }
                    Log.d("TAGG", "Beforeb has next")
                }
                else {
                    Toast.makeText(applicationContext, "No data exists", Toast.LENGTH_LONG).show()
                    loadEventDetailsData()
                }
            }

            override fun onFailure(call: Call<Featured_Group>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext, "No Groups Exixts, Join a group today!", Toast.LENGTH_LONG).show()
                loadEventDetailsData()
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
                intent.putExtra("isLoggedIn",true)
                startActivity(intent)

//                ToastHelper.makeToast(applicationContext, "Groups Under Maintanence")

            }
            R.id.action_groups -> {
                val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
                drawerLayout.closeDrawer(GravityCompat.START)
                intent = Intent(applicationContext, ZActivityGroups::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("isLoggedIn",true)
                startActivity(intent)

//                ToastHelper.makeToast(applicationContext, "Groups Under Maintanence")

            }
            R.id.action_jobs -> {
                val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
                drawerLayout.closeDrawer(GravityCompat.START)
                intent = Intent(applicationContext, ZActivityJobs::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("isLoggedIn",true)
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
                intent.putExtra("isLoggedIn",true)
//                intent = Intent(applicationContext, WebActivity::class.java)
//                intent.putExtra("value","https://www.jobsforher.com/company")
                startActivity(intent)
            }
            R.id.action_reskilling -> {
                intent = Intent(applicationContext, ZActivityDashboard::class.java)
                intent.putExtra("isLoggedIn",true)
                //intent = Intent(applicationContext, WebActivity::class.java)
                //intent.putExtra("value","https://www.jobsforher.com/reskilling")
                startActivity(intent)
            }
            R.id.action_mentors -> {
                intent = Intent(applicationContext, WebActivity::class.java)
                intent.putExtra("value","https://www.jobsforher.com/mentors")
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
                intent.putExtra("isLoggedIn",true)
                startActivity(intent)

            }
            R.id.action_blogs-> {
                intent = Intent(applicationContext, WebActivity::class.java)
                intent.putExtra("value","https://www.jobsforher.com/blogs")
                startActivity(intent)
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

        val pref_keyword= dialog.findViewById(R.id.pref_keyword) as EditText

        val experience_multiAutoCompleteTextView = dialog.findViewById(R.id.experience_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val experienceList: java.util.ArrayList<String> = java.util.ArrayList()
        for(i in 0..50)
            experienceList.add(i.toString())
        val adapter2 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, experienceList)
        experience_multiAutoCompleteTextView.setAdapter(adapter2)
        experience_multiAutoCompleteTextView.setThreshold(1)
        experience_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        val jobtype_multiAutoCompleteTextView = dialog.findViewById(R.id.jobtype_multiAutoCompleteTextView) as MultiAutoCompleteTextView
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

        val farea_multiAutoCompleteTextView = dialog.findViewById(R.id.farea_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfJobFArea)
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
        val adapter1 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfJobIndustry)
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
            savePreferences(pref_location_multiAutoCompleteTextView.text.trim().toString(),jobtype_multiAutoCompleteTextView.text.trim().toString(),
                farea_multiAutoCompleteTextView.text.trim().toString(),industry_multiAutoCompleteTextView.text.trim().toString(),
                pref_keyword.text.trim().toString(),experience_multiAutoCompleteTextView.text.trim().toString())
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
                }
                else
                    Toast.makeText(applicationContext,"No data exists",Toast.LENGTH_LONG).show()
            }
            override fun onFailure(call: Call<GetCityResponse>, t: Throwable) {
                Logger.d("TAG", "FAILED : $t")
            }
        })
    }

//    public fun openBottomSheet(id: Int, data: String?, isOwner: String?, icon: String, url:String, posttype:String, yesno: String) {
//
//        val dialog = Dialog(this)
//        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog .setCancelable(true)
//        dialog .setContentView(R.layout.bottom_sheet_posts)
////        val editreport = dialog .findViewById(R.id.edit_report) as LinearLayout
////        editreport.setOnClickListener {
////            if(isOwner.equals("true")) {
////                dialog.cancel()
////                intent = Intent(applicationContext, ZEditPostActivity::class.java)
////                intent.putExtra("id", id)
////                intent.putExtra("data", data)
////                intent.putExtra("icon",icon)
////                startActivity(intent)
////            }
////            else
////                Toast.makeText(applicationContext, "You cannot edit the post",Toast.LENGTH_LONG).show()
////        }
//
//        val editpost = dialog .findViewById(R.id.edit_post) as LinearLayout
//
//        editpost.setOnClickListener {
//            if(isOwner.equals("true")) {
//                dialog.cancel()
//                intent = Intent(applicationContext, ZEditPostActivity::class.java)
//                intent.putExtra("id", id)
//                intent.putExtra("data", data)
//                intent.putExtra("post_type",posttype)
//                intent.putExtra("url",url)
//                intent.putExtra("icon",icon)
//                intent.putExtra("grpName", groupname.text.toString())
//                intent.putExtra("owner",isOwner)
//                startActivityForResult(intent, 1);
//            }
//            else {
//                Toast.makeText(applicationContext, "You cannot edit the post", Toast.LENGTH_LONG).show()
//            }
//        }
//
//        val edit_post_history = dialog .findViewById(R.id.edit_post_history) as LinearLayout
//
//        edit_post_history.setOnClickListener {
//
//            if(isOwner.equals("true")) {
//                dialog.cancel()
//                intent = Intent(applicationContext, ZEditPostHistoryActivity::class.java)
//                intent.putExtra("id", id)
//                startActivity(intent)
//            }
//            else
//                Toast.makeText(applicationContext, "You cannot view edit history",Toast.LENGTH_LONG).show()
//        }
//
//        val reportpost = dialog .findViewById(R.id.edit_report) as LinearLayout
//        val reporttext = dialog.findViewById(R.id.reportedtext) as TextView
//        Log.d("REPORT",yesno)
//        if (yesno.compareTo("yes")==0){
//            reporttext.setText("Reported")
//            reporttext.setTextColor(Color.RED)
//            reportpost.isEnabled = false
//        }
//        else{
////            reporttext.setText("Report this post")
////            reporttext.setTextColor(Color.BLACK)
//            reportpost.isEnabled = true
//        }
//        reportpost.setOnClickListener {
//            openBottomSheetReports(id,"post")
//        }
//
////        val share_link = dialog.findViewById(R.id.share_link) as LinearLayout
////        share_link.setOnClickListener {
////            sharegroupdetails()
////        }
//
////        val deletepost = dialog .findViewById(R.id.delete_post) as LinearLayout
////        deletepost.setOnClickListener {
////            if(isOwner.equals("true")) {
////                retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
////
////                val call = retrofitInterface!!.deletePost(id, EndPoints.CLIENT_ID, "Bearer "+
////                        EndPoints.ACCESS_TOKEN)
////
////                call.enqueue(object : Callback<DeletePostResponse> {
////                    override fun onResponse(call: Call<DeletePostResponse>, response: Response<DeletePostResponse>) {
////
////                        Logger.d("CODE", response.code().toString() + "")
////                        Logger.d("MESSAGE", response.message() + "")
////                        Logger.d("URL", "" + response)
////                        Logger.e("RESPONSE ", "" + Gson().toJson(response))
////
////                        if (response.isSuccessful) {
////
////                            ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
////                            Logger.e("TAGG", "" + response.body()!!.message.toString())
////                            dialog.cancel()
////
////                        } else {
////
////                            ToastHelper.makeToast(applicationContext, "Invalid Request")
////                        }
////                    }
////
////                    override fun onFailure(call: Call<DeletePostResponse>, t: Throwable) {
////
////                        Logger.d("TAGG", "FAILED : $t")
////                    }
////                })
////                dialog.cancel()
////            }
////            else
////                Toast.makeText(applicationContext, "You cannot delete the post",Toast.LENGTH_LONG).show()
////        }
//        if(isOwner.equals("true")){
//            editpost.visibility= View.VISIBLE
//            edit_post_history.visibility = View.VISIBLE
//        }
//        else{
//            editpost.visibility= View.GONE
//            edit_post_history.visibility = View.GONE
//        }
//
//
//        var window :Window = dialog.getWindow();
//        var wlp: WindowManager.LayoutParams = window.getAttributes();
//
//        wlp.gravity = Gravity.BOTTOM;
//        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        window.setAttributes(wlp);
//        dialog .show()
//    }
//
//    public fun openBottomSheetComments(id: Int, data: String?, isOwner: String?, icon:String?,yesno: String) {
//
//        val dialog = Dialog(this)
//        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog .setCancelable(true)
//        dialog .setContentView(R.layout.bottom_sheet_comments)
//        val editcomment = dialog .findViewById(R.id.edit_comment) as LinearLayout
//
//        editcomment.setOnClickListener {
//            if(isOwner.equals("true")) {
//                dialog.cancel()
//                intent = Intent(applicationContext, ZEditCommentActivity::class.java)
//                intent.putExtra("id", id)
//                intent.putExtra("data", data)
//                intent.putExtra("icon",icon)
//                startActivityForResult(intent,1)
//                dialog.cancel()
//            }
//            else
//                Toast.makeText(applicationContext, "You cannot edit the comment", Toast.LENGTH_LONG).show()
//        }
//
//        val edit_commenthistory = dialog .findViewById(R.id.edit_comment_history) as LinearLayout
//        edit_commenthistory.setOnClickListener {
//            if(isOwner.equals("true")) {
//                dialog.cancel()
//                intent = Intent(applicationContext, ZEditComentHistoryActivity::class.java)
//                intent.putExtra("id", id)
//                intent.putExtra("icon",icon)
//                startActivity(intent)
//                dialog.cancel()
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
//        val reportcomment = dialog .findViewById(R.id.report_comment) as LinearLayout
//        val reporttext = dialog.findViewById(R.id.reporttextcomment) as TextView
//        Log.d("REPORT",yesno)
//        if (yesno.compareTo("yes")==0){
//            reporttext.setText("Reported")
//            reporttext.setTextColor(Color.RED)
//            reportcomment.isEnabled = false
//        }
//        else{
////            reporttext.setText("Report this comment")
////            reporttext.setTextColor(Color.BLACK)
//            reportcomment.isEnabled = true
//        }
//        reportcomment.setOnClickListener {
//            dialog.cancel()
//            openBottomSheetReports(id, "comment")
//        }
//
//        if(isOwner.equals("true")){
//            editcomment.visibility= View.VISIBLE
//            edit_commenthistory.visibility = View.VISIBLE
//        }
//        else{
//            editcomment.visibility= View.GONE
//            edit_commenthistory.visibility = View.GONE
//        }
//
//        var window : Window = dialog.getWindow();
//        var wlp: WindowManager.LayoutParams  = window.getAttributes();
//
//        wlp.gravity = Gravity.BOTTOM;
//        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        window.setAttributes(wlp);
//        dialog .show()
//    }
//
//    public fun openBottomSheetReports(id: Int, type:String) {
//
//        val dialog = Dialog(this)
//        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog .setCancelable(true)
//        dialog .setContentView(R.layout.bottom_sheet_reports)
//        val textheadline = dialog.findViewById(R.id.textreports) as TextView
//        if (type.equals("group"))
//            textheadline.setText("Report or give feedback on this group")
//        else if (type.equals("post"))
//            textheadline.setText("Report or give feedback on this post")
//        else if (type.equals("comment"))
//            textheadline.setText("Report or give feedback on this comment")
//        val spinner = dialog.findViewById(R.id.report_spinner) as Spinner
//        val adapter = ArrayAdapter.createFromResource(this, R.array.report_list, android.R.layout.simple_spinner_item)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinner.adapter = adapter
//
//        val reportdetails = dialog .findViewById(R.id.reportdetails) as EditText
//
//        val sendreport = dialog .findViewById(R.id.sendreport) as LinearLayout
//        sendreport.setOnClickListener {
//           reportGroup(id,type, spinner.selectedItem.toString(), reportdetails.text.toString(), dialog)
//            dialog.cancel()
//        }
//
//        val cancel = dialog .findViewById(R.id.cancelblock) as LinearLayout
//        cancel.setOnClickListener {
//            dialog.cancel()
//        }
//
//
//        var window : Window = dialog.getWindow();
//        var wlp: WindowManager.LayoutParams  = window.getAttributes();
//
//        wlp.gravity = Gravity.BOTTOM;
//        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        window.setAttributes(wlp);
//        dialog .show()
//    }



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


    fun leaveGroup(id:Int, btnJoinGroup:Button){

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.leaveGroup(id, EndPoints.CLIENT_ID, "Bearer "+
                EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<JoinGroupResponse> {
            override fun onResponse(call: Call<JoinGroupResponse>, response: Response<JoinGroupResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.e("RESPONSE leave group", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())

                    val intent = Intent(applicationContext, ZActivityGroups::class.java)
                    intent.flags = FLAG_ACTIVITY_NEW_TASK or  Intent.FLAG_ACTIVITY_CLEAR_TASK
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


    fun reportGroup(id: Int, type: String, problem_type:String, reportdetail: String, dialog:Dialog){

//        ToastHelper.makeToast(applicationContext, "Report Group")
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

    fun savePreferences(pref_location: String, jobtype: String, farea:String, industry: String, pref_keyword:String,experience:String ){

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
                params["exp_to_year"] = experience.substring(0, experience.length - 1)
            }
            else{
                params["exp_from_year"] = experience.toString()
                params["exp_to_year"] = experience.toString()
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




}

