package com.jobsforher.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
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
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jobsforher.R
import com.jobsforher.adapters.*
import com.jobsforher.helpers.Constants
import com.jobsforher.helpers.HelperMethods
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.models.*
import com.jobsforher.network.responsemodels.*
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import com.plumillonforge.android.chipview.Chip
import com.plumillonforge.android.chipview.OnChipClickListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.companies_app_bar_main.*
import kotlinx.android.synthetic.main.toolbar_groups.*
import kotlinx.android.synthetic.main.zcompanies_content_main.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import com.jobsforher.models.CategoryView as CategoryView1

class ZActivityCompanies() : Footer(), NavigationView.OnNavigationItemSelectedListener{

    var mRecyclerView: RecyclerView? = null
    var mAdapter: RecyclerView.Adapter<*>? = null
    var listOfhotJobsdata: ArrayList<CompaniesView> = ArrayList()
    var listOfFilterMygroups: ArrayList<GroupsView> = ArrayList()
    var listOfFeaturedMygroups: ArrayList<GroupsView> = ArrayList()
    var listOfCompareGroupdata: ArrayList<GroupsView> = ArrayList()
    var listOfCompareJoineddata: ArrayList<Int> = ArrayList()
    var message_filter:String=""
    var scroll:Int = 0
    private var doubleBackToExitPressedOnce = false

    var mgroupsRecyclerView: RecyclerView? = null
    var hotJobsAdapter: RecyclerView.Adapter<*>? = null
    var isLoggedIn=true
    private var retrofitInterface: RetrofitInterface? = null
    private var retrofitInterface1: RetrofitInterface? = null
    var next_page_no_featured: String="1"
    var prev_page_no_featured: String="1"
    var type_group: Int=1
    var isfilter:Int = 0

    var mAdapterCategories: RecyclerView.Adapter<*>? = null
    var mAdapterCities: RecyclerView.Adapter<*>? = null
    var mAdapterJobtype: RecyclerView.Adapter<*>? = null
    var mAdapterFArea: RecyclerView.Adapter<*>? = null
    var mAdapterIndustry: RecyclerView.Adapter<*>? = null

    var listOfCategories: ArrayList<CategoryView1> = ArrayList()
    var listOfCities: ArrayList<CityView> = ArrayList()
    var listOfJobType: ArrayList<JobTypeView> = ArrayList()
    var listOfJobFArea: ArrayList<FunctionalAreaView> = ArrayList()
    var listOfJobFAreaSorted: ArrayList<FunctionalAreaView> = ArrayList()
    var listOfJobIndustry: ArrayList<FunctionalAreaView> = ArrayList()

    var location_name: String=""
    var location_nameArray: ArrayList<String> = ArrayList()
    var keyword:String =""
    var job_types: String=""
    var min_year: String=""
    var max_year: String=""
    var functional_area:String=""
    var functional_areaArray:ArrayList<String> = ArrayList()
    var industries:String=""
    var industriesArray:ArrayList<String> = ArrayList()
    var company_id:String=""
    var type: String=""

    var listOfMainCategories: ArrayList<CategoriesMainView> = ArrayList()
    var listOfResumes: ArrayList<ResumeView>  = ArrayList()

    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zactivity_companies)
        isLoggedIn=intent.getBooleanExtra("isLoggedIn",false)
        Log.d("TAGG", isLoggedIn.toString())
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mappingWidgets()
        loadNotificationbubble()
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

//        loadAppliedJobs()

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close
        )
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        fabFilter.setOnClickListener {
            filter.callOnClick()
        }

        if(isLoggedIn){

            if (navView != null) {
                val menu = navView.menu
                menu.findItem(R.id.action_employerzone).setVisible(false)
                menu.findItem(R.id.action_mentorzone).setVisible(false)
                menu.findItem(R.id.action_partnerzone).setVisible(false)
                menu.findItem(R.id.action_logout).setVisible(true)
                menu.findItem(R.id.action_settings).setVisible(false)
                menu.findItem(R.id.action_signup).setVisible(false)
                menu.findItem(R.id.action_login).setVisible(false)
                menu.findItem(R.id.action_companies).title = Html.fromHtml("<font color='#99CA3B'>Companies</font>")
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
                menu.findItem(R.id.action_companies).title = Html.fromHtml("<font color='#99CA3B'>Companies</font>")
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
        hotJobsAdapter = CompaniesAdapter(listOfhotJobsdata, isLoggedIn, 1,0,listOfCompareJoineddata,0,"Companies")
        mgroupsRecyclerView!!.adapter = hotJobsAdapter



        img_profile_toolbar.setOnClickListener{
            intent = Intent(applicationContext, ZActivityDashboard::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("isLoggedIn", true)
            intent.putExtra("pagetype", 0)
            startActivity(intent)
        }
        mainScroll_jobs.getViewTreeObserver().addOnScrollChangedListener(object : ViewTreeObserver.OnScrollChangedListener{

            override fun onScrollChanged() {
                val scrollBounds = Rect()
                login_view.getHitRect(scrollBounds)
                val scrollBounds1 = Rect()
                layouttop.getHitRect(scrollBounds)
                if (login_view.getLocalVisibleRect(scrollBounds) || layouttop.getLocalVisibleRect(scrollBounds1) ) {
                    // if layout even a single pixel, is within the visible area do something
                    login_view1.visibility = View.GONE
                } else {
                    // if layout goes out or scrolled out of the visible area do something
                    login_view1.visibility = View.VISIBLE
                }

            }
        })

        mainScroll_jobss.getViewTreeObserver().addOnScrollChangedListener(object: ViewTreeObserver.OnScrollChangedListener {
            override fun onScrollChanged() {
                Log.d("TAGG","END EFORE")
                if(isfilter==0) {
                    val view: View = mainScroll_jobss.getChildAt(mainScroll_jobss.getChildCount() - 1);
                    val diff: Int =
                        ((view.getBottom() - filter_bottom.getHeight()) - (mainScroll_jobss.getHeight() + mainScroll_jobss.getScrollY() - filter_bottom.getHeight()));
                    if (diff == 0 && scroll == 1) {
                        Log.d("TAGG", "END")
                        if (type_group == 1) {
                            loadhotJobsData(next_page_no_featured)
                            Log.d("URL", "Reached here")
                        } else if (type_group == 2)
                            loadFeaturedGroupData(next_page_no_featured!!)
                        else if(type_group ==4)
                            loadOtherGroupData(next_page_no_featured!!)
                    }
                }
                else{
                    val view: View = mainScroll_jobss.getChildAt(mainScroll_jobss.getChildCount() - 1);
                    val diff: Int =
                        ((view.getBottom() - filter_bottom.getHeight()) - (mainScroll_jobss.getHeight() + mainScroll_jobss.getScrollY() - filter_bottom.getHeight()));
                    if (diff == 0 && scroll == 1) {
                        loadFilteredData(next_page_no_featured, keyword.toString().trim(), location_name, industries)
                    }
                }
                val scrollBounds = Rect()
                login_view.getHitRect(scrollBounds)
                val scrollBounds1 = Rect()
                layouttop.getHitRect(scrollBounds)
                if (login_view.getLocalVisibleRect(scrollBounds) || layouttop.getLocalVisibleRect(scrollBounds1)) {
                    login_view1.visibility = View.GONE
                } else {
                    login_view1.visibility = View.VISIBLE
                }

            }
        })

        //tag_group_comp.setChipBackgroundRes(R.drawable.chip2)
        tag_group_comp.chipSidePadding =70
        tag_group_comp.chipLayoutRes = R.layout.chip_close

        tag_group_comp.setOnChipClickListener(OnChipClickListener{

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

            tag_group_comp.remove(it)
            if(tag_group_comp.count()==0){
                complisting_tag.visibility = View.GONE
                layoutdefaultjob.visibility = View.GONE
                find_morejobs.callOnClick()
            }
            else
                loadFilteredData("1",keyword,location_name,industries)

        })

        loadCategoryData()

        loadData()
        loadFunctionalData()
        loadIndustryData()


        if(isLoggedIn){
            img_profile_toolbar.visibility = View.VISIBLE
//            notificaton.visibility = View.VISIBLE
            sign_in.visibility = View.GONE
            mygroups.visibility = View.VISIBLE
            mygroups_selected.visibility = View.VISIBLE
            mygroups1.visibility = View.VISIBLE
            mygroups_selected1.visibility = View.VISIBLE
            listOfhotJobsdata.clear()
            loadFollowedCompanies()
//            loadAppliedJobs()
            listOfCompareGroupdata.clear()
            type_group=1
            mygroups.setTypeface(null, Typeface.BOLD);
            featured.setTypeface(null, Typeface.NORMAL);
            mygroups1.setTypeface(null, Typeface.BOLD);
            featured1.setTypeface(null, Typeface.NORMAL);


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
            listOfhotJobsdata.clear()
//            loadFeaturedGroupData("1")
            type_group=2
            featured.setTypeface(null, Typeface.BOLD);
            mygroups.setTypeface(null, Typeface.NORMAL);
            mygroups1.setTypeface(null, Typeface.BOLD);
            featured1.setTypeface(null, Typeface.NORMAL);
        }

        sign_in.setOnClickListener {
            intent = Intent(applicationContext, SplashActivity::class.java)
            startActivity(intent)
        }


        filter_bottom.setOnClickListener{
            filter.callOnClick()
        }

        filter.setOnClickListener {
            openBottomSheetFilter()

        }



        find_morejobs.setOnClickListener {

            filter.setText("FILTER")
            filer_default_layout.visibility = View.GONE
            isfilter =0
            location_name=""
            keyword=""
            industries=""
            type=""

            layouttop.visibility = View.GONE
            layoutadd.visibility = View.VISIBLE
            //layout1.visibility = View.VISIBLE
            mainScroll_jobs.fullScroll(View.FOCUS_UP);
            mainScroll_jobs.smoothScrollTo(0,0);
            recycler_view_groups.setFocusable(false);

            mygroups_selected.visibility = View.VISIBLE
            featured_selected.visibility = View.GONE
            mygroups_selected1.visibility = View.VISIBLE
            featured_selected1.visibility = View.GONE
            search_companies.clearFocus()
            search_companies.setText(null)

            mgroupsRecyclerView!!.visibility = View.VISIBLE
            empty_view.visibility = View.GONE

            //Toast.makeText(applicationContext,"My groups",Toast.LENGTH_LONG).show()
            if (isfilter>0) {
                loadFilteredData("1",keyword,location_name,industries)
            }
            else{
                listOfCompareGroupdata.clear()
                listOfhotJobsdata.clear()
                loadhotJobsData("1")
            }
            type_group=1

            featured.setTypeface(null, Typeface.NORMAL);
            mygroups.setTypeface(null, Typeface.BOLD);
            featured1.setTypeface(null, Typeface.NORMAL);
            mygroups1.setTypeface(null, Typeface.BOLD);
        }




        mygroups1.setOnClickListener {
            mygroups.callOnClick()
        }

        featured1.setOnClickListener {
            featured.callOnClick()
        }

        mygroups.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                mygroups_selected.visibility = View.VISIBLE
                featured_selected.visibility = View.GONE
                mygroups_selected1.visibility = View.VISIBLE
                featured_selected1.visibility = View.GONE
                search_companies.clearFocus()
                search_companies.setText(null)

                mgroupsRecyclerView!!.visibility = View.VISIBLE
                empty_view.visibility = View.GONE
                type_group=1
                //Toast.makeText(applicationContext,"My groups",Toast.LENGTH_LONG).show()
                if (isfilter>0) {

                    loadFilteredData("1",keyword,location_name,industries)
                }
                else{
                    listOfCompareGroupdata.clear()
                    listOfhotJobsdata.clear()
                    loadhotJobsData("1")
                }


                featured.setTypeface(null, Typeface.NORMAL);
                mygroups.setTypeface(null, Typeface.BOLD);
                featured1.setTypeface(null, Typeface.NORMAL);
                mygroups1.setTypeface(null, Typeface.BOLD);
            }
        })
        featured.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                mygroups_selected.visibility = View.GONE
                featured_selected.visibility = View.VISIBLE
                mygroups_selected1.visibility = View.GONE
                featured_selected1.visibility = View.VISIBLE
                search_companies.clearFocus()
                search_companies.setText(null)
                type_group=2
                mgroupsRecyclerView!!.visibility = View.VISIBLE
                empty_view.visibility = View.GONE

                if (isfilter>0){

                    loadFilteredData("1",keyword,location_name,industries)
                }
                else{
                    listOfCompareGroupdata.clear()
                    listOfhotJobsdata.clear()
                    loadFeaturedGroupData("1")
                }

                featured.setTypeface(null, Typeface.BOLD);
                mygroups.setTypeface(null, Typeface.NORMAL);
                featured1.setTypeface(null, Typeface.BOLD);
                mygroups1.setTypeface(null, Typeface.NORMAL);
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


        search_companies_button.setOnClickListener {
            keyword = search_companies.text.toString().trim()
            loadFilteredData("1",search_companies.text.toString().trim(),"", "")

        }


        search_default_button.setOnClickListener {
            keyword = search_companies.text.toString().trim()
            loadFilteredData("1",search_companies.text.toString().trim(),"", "")

        }

        notifLayout.setOnClickListener {
            cart_badge.setText("0")
            intent = Intent(applicationContext, Notification::class.java)
            startActivity(intent)
        }

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

    public fun openBottomSheetPreferences() {

        val dialog = Dialog(this)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog .setCancelable(true)
        dialog .setContentView(R.layout.bottom_sheet_preferences)
        val cancel_pref = dialog.findViewById(R.id.cancel_pref) as Button
        cancel_pref.setOnClickListener {
            dialog.cancel()
        }
        val save_pref = dialog.findViewById(R.id.save_pref) as Button
        save_pref.setOnClickListener {

        }
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
        val adapter1 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, industryList)
        industry_multiAutoCompleteTextView.setAdapter(adapter1)
        industry_multiAutoCompleteTextView.setThreshold(1)
        industry_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

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

    public fun openBottomSheetUploadDoc(jobId:Int, title: String ,listOfResume: ArrayList<ResumeView>) {

        val dialog = Dialog(this)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog .setCancelable(true)
        dialog .setContentView(R.layout.bottom_sheet_applyjobs)
        val add_resume = dialog.findViewById(R.id.add_resume) as LinearLayout
        val add_resume_title = dialog.findViewById(R.id.add_resume_title) as LinearLayout
        val note = dialog.findViewById(R.id.note) as EditText

        var mAdapterResume: RecyclerView.Adapter<*>? = null
        val mRecyclerView = dialog.findViewById(R.id.resume_recycler_view) as RecyclerView
        val mLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL,false)
        mAdapterResume = ResumeAdapter(listOfResumes)
        mRecyclerView!!.adapter = mAdapterResume
        mRecyclerView!!.layoutManager = mLayoutManager
        val jobname = dialog.findViewById(R.id.jobname) as TextView
        jobname.setText(title)
        val mobileLayout = dialog.findViewById(R.id.mobile_layout) as LinearLayout
        if(EndPoints.PHONE_NO.length>2)
            mobileLayout.visibility = View.GONE
        val cancel_pref = dialog.findViewById(R.id.cancel_pref) as Button
        cancel_pref.setOnClickListener {
            dialog.cancel()
        }
        val save_pref = dialog.findViewById(R.id.save_pref) as Button
        save_pref.setOnClickListener {
            Toast.makeText(applicationContext, "Applied", Toast.LENGTH_LONG).show()
            saveJob(jobId,note.text.toString(),listOfResumes[0].id, "Applied")
            dialog.cancel()
        }
        if (listOfResume.size>0){
            mRecyclerView.visibility = View.VISIBLE
            add_resume.visibility = View.GONE
            add_resume_title.visibility = View.GONE
        }
        else{
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

    fun loadFeaturedGroupData(pageno:String){

        if (pageno.equals("1")) {
            listOfhotJobsdata.clear()
            hotJobsAdapter!!.notifyDataSetChanged()
        }


        val params = HashMap<String, String>()

        params["page_no"] = pageno.toString()
        params["page_size"] = Constants.MAXIMUM_PAGINATION_COUNT


        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface!!.getCompanyListBasic( EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        call.enqueue(object : Callback<Company> {
            override fun onResponse(call: Call<Company>, response: Response<Company>) {

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
                        for (i in 0 until response.body()!!.body!!.size) {
                            Log.d("TAGG", "Inside response body")
                            var json_objectdetail: JSONObject = jsonarray_info.optJSONObject(i)

                            val model: CompaniesView = CompaniesView()

                            model.id = json_objectdetail.optInt("id")
                            model.company_type = json_objectdetail.optString("company_type")
                            model.featured = json_objectdetail.optBoolean("featured")
                            model.website = json_objectdetail.optString("website")
                            model.active_jobs_count = json_objectdetail.optInt("active_jobs_count")
                            model.cities = json_objectdetail.optString("cities")
                            model.follow_count =json_objectdetail.optInt("follow_count")
                            model.name = json_objectdetail.optString("name")
                            model.logo = json_objectdetail.optString("logo")
                            val listOfIndustry: ArrayList<String> = ArrayList()
                            if(json_objectdetail.isNull("industry")){}
                            else{
                                var industryArray: JSONArray = json_objectdetail.optJSONArray("industry")
                                for (j in 0 until industryArray.length()) {
                                    listOfIndustry.add(industryArray.optString(j))
                                }
                            }

                            model.industry = listOfIndustry
                            model.banner_image = ""//json_objectdetail.optString("banner_image")
                            model.status = json_objectdetail.optString("status")

                            if (!model.featured!!) {
                                listOfhotJobsdata.add(
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
                            }
                        }

//                        if (pageno.equals("1")) {
//                            hotJobsAdapter!!.notifyDataSetChanged()
//                        }
//                        else {
                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                        mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                        hotJobsAdapter = CompaniesAdapter(listOfhotJobsdata, isLoggedIn, 2,0,listOfCompareJoineddata,0,"Companies")
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
                    recycler_view_groups.setFocusable(false)
                }
                else {
                    Toast.makeText(applicationContext, "No data exists", Toast.LENGTH_LONG).show()
                    loadnext.visibility = View.GONE
                    loadprev.visibility = View.GONE
                    scroll =0
                }

            }

            override fun onFailure(call: Call<Company>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext, "No Groups Exixts, Join a group today!", Toast.LENGTH_LONG).show()
                loadnext.visibility = View.GONE
                loadprev.visibility = View.GONE
                scroll =0
            }
        })
    }

    fun loadFollowedCompanies(){

        listOfCompareJoineddata.clear()
        retrofitInterface1 = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface1!!.getCompanyFollowed(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)
        call.enqueue(object : Callback<FollowersResponse> {
            override fun onResponse(
                call: Call<FollowersResponse>,
                response: Response<FollowersResponse>
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

                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                    val responseCode: Int = jsonObject1.optInt("response_code")
                    val message: String = jsonObject1.optString("message")
                    val jsonarray_info: JSONObject = jsonObject1.optJSONObject("body")
                    val jsonarray_comp: JSONArray = jsonarray_info.optJSONArray("company_id")
                    Log.d("TAGG", "Applied " + jsonarray_info.toString())

                    if (response.isSuccessful) {

                        for (l in 0 until jsonarray_comp!!.length()) {
                            listOfCompareJoineddata.add(jsonarray_comp[l] as Int)

                        }

                        loadhotJobsData("1")

                    } else {
                        ToastHelper.makeToast(applicationContext, message)
                    }
                }
            }

            override fun onFailure(call: Call<FollowersResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
            }
        })
    }

    fun loadhotJobsData(pageno:String){

        if (pageno.equals("1")) {
            listOfhotJobsdata.clear()
            hotJobsAdapter!!.notifyDataSetChanged()


        }


        val params = HashMap<String, String>()

        params["page_no"] = pageno.toString()
        params["page_size"] = Constants.MAXIMUM_PAGINATION_COUNT


        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface!!.getCompanyListFeatured( EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        call.enqueue(object : Callback<Company> {
            override fun onResponse(call: Call<Company>, response: Response<Company>) {

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
                    val prev_page: Int
                    if (Integer.parseInt(pagenoo)>1)
                        prev_page = Integer.parseInt(pagenoo)-1
                    else
                        prev_page = 0

                    if (response.isSuccessful) {

                        //     ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                        Log.d("TAGG", "Inside response success")
                        for (i in 0 until response.body()!!.body!!.size) {
                            Log.d("TAGG", "Inside response body")
                            var json_objectdetail: JSONObject = jsonarray_info.optJSONObject(i)

                            val model: CompaniesView = CompaniesView()

                            model.id = json_objectdetail.optInt("id")
                            model.company_type = json_objectdetail.optString("company_type")
                            model.featured = json_objectdetail.optBoolean("featured")
                            model.website = json_objectdetail.optString("website")
                            model.active_jobs_count = json_objectdetail.optInt("active_jobs_count")
                            model.cities = json_objectdetail.optString("cities")
                            model.follow_count =json_objectdetail.optInt("follow_count")
                            model.name = json_objectdetail.optString("name")
                            model.logo = json_objectdetail.optString("logo")
                            val listOfIndustry: ArrayList<String> = ArrayList()
                            if(json_objectdetail.isNull("industry")){}
                            else {
                                var industryArray: JSONArray = json_objectdetail.optJSONArray("industry")
                                for (j in 0 until industryArray.length()) {
                                    listOfIndustry.add(industryArray.optString(j))
                                }
                            }
                            model.industry = listOfIndustry
                            model.banner_image = ""//json_objectdetail.optString("banner_image")
                            model.status = json_objectdetail.optString("status")

                            if (model.featured!!) {
                                listOfhotJobsdata.add(
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
                            }

                        }

//                        if (pageno.equals("1")) {
//                            hotJobsAdapter!!.notifyDataSetChanged()
//                        }
//                        else {
                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                        mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                        hotJobsAdapter = CompaniesAdapter(listOfhotJobsdata, isLoggedIn, 3,0,listOfCompareJoineddata,0,"Companies")
                        mgroupsRecyclerView!!.adapter = hotJobsAdapter
//                        }

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
                        //loadOtherGroupData("1")
                    }

                    loadprev.visibility = View.GONE
//                    if (pagenoo == "1")
//                        loadprev.visibility = View.GONE
//                    else {
//                        loadprev.visibility = View.VISIBLE
//                        prev_page_no_featured = prev_page.toString()
//                    }
                    recycler_view_groups.setFocusable(false)
                }
                else {
                    Toast.makeText(applicationContext, "No data exists", Toast.LENGTH_LONG).show()
                    loadnext.visibility = View.GONE
                    loadprev.visibility = View.GONE
                    scroll =0
                }
//                progressDoalog.dismiss();

            }

            override fun onFailure(call: Call<Company>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                //  Toast.makeText(applicationContext, "No Groups Exixts, Join a group today!", Toast.LENGTH_LONG).show()
                loadnext.visibility = View.GONE
                loadprev.visibility = View.GONE
                scroll =0
//                progressDoalog.dismiss();
            }
        })
    }


    fun loadOtherGroupData(pageno:String){

        type_group = 4
        mygroups_selected.visibility = View.GONE
        featured_selected.visibility = View.VISIBLE
        mygroups_selected1.visibility = View.GONE
        featured_selected1.visibility = View.VISIBLE

        featured.setTypeface(null, Typeface.BOLD);
        mygroups.setTypeface(null, Typeface.NORMAL);
        featured1.setTypeface(null, Typeface.BOLD);
        mygroups1.setTypeface(null, Typeface.NORMAL);

        val params = HashMap<String, String>()

        params["page_no"] = pageno.toString()
        params["page_size"] = Constants.MAXIMUM_PAGINATION_COUNT


        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface!!.getCompanyListBasic( EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        call.enqueue(object : Callback<Company> {
            override fun onResponse(call: Call<Company>, response: Response<Company>) {

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
                    val prev_page: Int
                    if (Integer.parseInt(pagenoo)>1)
                        prev_page = Integer.parseInt(pagenoo)-1
                    else
                        prev_page = 0

                    if (response.isSuccessful) {

                        //  ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                        Log.d("TAGG", "Inside response success")
                        for (i in 0 until response.body()!!.body!!.size) {
                            Log.d("TAGG", "Inside response body")
                            var json_objectdetail: JSONObject = jsonarray_info.optJSONObject(i)

                            val model: CompaniesView = CompaniesView()

                            model.id = json_objectdetail.optInt("id")
                            model.company_type = json_objectdetail.optString("company_type")
                            model.featured = json_objectdetail.optBoolean("featured")
                            model.website = json_objectdetail.optString("website")
                            model.active_jobs_count = json_objectdetail.optInt("active_jobs_count")
                            model.cities = json_objectdetail.optString("cities")
                            model.follow_count =json_objectdetail.optInt("follow_count")
                            model.name = json_objectdetail.optString("name")
                            model.logo = json_objectdetail.optString("logo")
                            val listOfIndustry: ArrayList<String> = ArrayList()
                            if(json_objectdetail.isNull("industry")){}
                            else{
                                var industryArray: JSONArray = json_objectdetail.optJSONArray("industry")
                                for (j in 0 until industryArray.length()) {
                                    listOfIndustry.add(industryArray.optString(j))
                                }
                            }

                            model.industry = listOfIndustry
                            model.banner_image = ""//json_objectdetail.optString("banner_image")
                            model.status = json_objectdetail.optString("status")

                            if (!model.featured!!) {
                                listOfhotJobsdata.add(
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
                            }
                        }

//                        if (pageno.equals("1")) {
//                            hotJobsAdapter!!.notifyDataSetChanged()
//                        }
//                        else {
                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                        mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                        hotJobsAdapter = CompaniesAdapter(listOfhotJobsdata, isLoggedIn, 2,0,listOfCompareJoineddata,0,"Companies")
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
                    recycler_view_groups.setFocusable(false)
                }
                else {
                    Toast.makeText(applicationContext, "No data exists", Toast.LENGTH_LONG).show()
                    loadnext.visibility = View.GONE
                    loadprev.visibility = View.GONE
                    scroll =0
                }

            }

            override fun onFailure(call: Call<Company>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext, "No Groups Exixts, Join a group today!", Toast.LENGTH_LONG).show()
                loadnext.visibility = View.GONE
                loadprev.visibility = View.GONE
                scroll =0
            }
        })
    }

    fun loadFilteredData(pageno:String,keyword:String,location_name:String, industries:String){

        isfilter = 1

        filter.setText("FILTER *")

        if (pageno.equals("1")) {
            listOfhotJobsdata.clear()
            hotJobsAdapter!!.notifyDataSetChanged()
        }
        else{}
        val params = HashMap<String, String>()
        var string: String = ""
        var list: ArrayList<Chip> = ArrayList()

        params["page_no"] = pageno.toString()
        //params["page_size"] = 100.toString()
        if (keyword.equals("")){}
        else{
            params["keyword"] = keyword.toString()
            string = string+ keyword.toString()+","
            list.add(Tag(keyword.toString()))
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
//            params["city"] = location_name.substring(0,location_name.length-1)
//            string = string+ location_name.substring(0,location_name.length-1)+","
//            list.add(Tag(location_name.substring(0,location_name.length-1)))
        }
        if (industries.equals("")){}
        else{
            if(industries.contains(",")) {
                params["industry"] = industries.substring(0, industries.length - 1)
                string = string+ industries.substring(0,industries.length-1)+","
            }
            else {
                params["industry"] = industries.toString()
                string = string+ industries.substring(0,industries.length)+","

            }

            if(industries.substring(0,industries.length).contains(",")) {
                var output:List<String> = industries.substring(0,industries.length-1) . split (",");
                for(i in 0 until output.size)
                    list.add(Tag(output.get(i)))
            }
            else
                list.add(Tag(industries))
//            params["industry"] = industries.substring(0,industries.length-1)
//            string = string+ industries.substring(0,industries.length-1)+","
//            list.add(Tag(industries.substring(0,industries.length-1)))
        }

        Log.d("TAGG", "PARAMS"+params)

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface!!.getfilteredCompanies( EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        call.enqueue(object : Callback<Company> {
            override fun onResponse(call: Call<Company>, response: Response<Company>) {

                Logger.d("URL", "" + response+ EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE FILTERED", "" + Gson().toJson(response))
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)

                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                Log.d("TAGG", "Filter Outside body")
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {

                    Log.d("TAGG", "Inside body")
                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")

                    var jsonarray_info: JSONArray = jsonObject1.optJSONArray("body")

                    var jsonarray_pagination: JSONObject = jsonObject1.optJSONObject("pagination")

                    val has_next: String = jsonarray_pagination.optBoolean("has_next").toString()

                    val pagenoo: String = jsonarray_pagination.optString("page_no")
                    val total_items = jsonarray_pagination.optString("total_items")
                    val prev_page: Int
                    if (Integer.parseInt(pagenoo)>1)
                        prev_page = Integer.parseInt(pagenoo)-1
                    else
                        prev_page = 0

                    if (response.isSuccessful) {
                        //listOfhotJobsdata.clear()
                        // ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                        Log.d("TAGG", "Inside response success")
                        for (i in 0 until response.body()!!.body!!.size) {
                            Log.d("TAGG", "Inside response body")
                            var json_objectdetail: JSONObject = jsonarray_info.optJSONObject(i)

                            val model: CompaniesView = CompaniesView()

                            model.id = json_objectdetail.optInt("id")
                            model.company_type = json_objectdetail.optString("company_type")
                            model.featured = json_objectdetail.optBoolean("featured")
                            model.website = json_objectdetail.optString("website")
                            model.active_jobs_count = json_objectdetail.optInt("active_jobs_count")
                            model.cities = json_objectdetail.optString("cities")
                            model.follow_count =json_objectdetail.optInt("follow_count")
                            model.name = json_objectdetail.optString("name")
                            model.logo = json_objectdetail.optString("logo")
                            val listOfIndustry: ArrayList<String> = ArrayList()
                            if(json_objectdetail.isNull("industry")){}
                            else {
                                var industryArray: JSONArray = json_objectdetail.optJSONArray("industry")

                                for (j in 0 until industryArray.length()) {
                                    listOfIndustry.add(industryArray.optString(j))
                                }
                            }
                            model.industry = listOfIndustry
                            model.banner_image = ""//json_objectdetail.optString("banner_image")
                            model.status = json_objectdetail.optString("status")

                            if(type_group==1) {
                                if(model.featured==true) {
                                    listOfhotJobsdata.add(
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
                                }
                            }
                            else{
                                if(model.featured==false) {
                                    listOfhotJobsdata.add(
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
                                }
                            }

                        }

                        if (has_next.equals("true")) {
                            val next_page: String = jsonarray_pagination.optInt("next_page").toString()
                            loadnext.visibility = View.VISIBLE
                            next_page_no_featured = next_page
                            scroll = 1
                        } else {
                            loadnext.visibility = View.GONE
                            scroll = 0
                        }

//                        if (pageno.equals("1")) {
//                            hotJobsAdapter!!.notifyDataSetChanged()
//                        }
//                        else {
                        if(type_group==1) {
                            Log.d("TAGG","SIZE OF DATA"+listOfhotJobsdata.size)
                            if(listOfhotJobsdata.size>0) {
                                var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                                mgroupsRecyclerView!!.layoutManager =
                                    mgroupsLayoutManager as RecyclerView.LayoutManager?
                                hotJobsAdapter =
                                    CompaniesAdapter(
                                        listOfhotJobsdata,
                                        isLoggedIn,
                                        3,
                                        1,
                                        listOfCompareJoineddata,
                                        0,
                                        "Companies"
                                    )
                                mgroupsRecyclerView!!.adapter = hotJobsAdapter

                                tag_group_comp.setChipList(list)
                                def_text_comp.visibility = View.GONE
                                complisting_tag.text = total_items.toString()+" matching companies found"
                                filer_default_layout.visibility = View.VISIBLE
                                layouttop.visibility = View.GONE
                                layoutadd.visibility = View.GONE
                                layoutdefaultjob.visibility = View.VISIBLE
                            }
                            else{
                                mgroupsRecyclerView!!.visibility = View.GONE
                                empty_view.visibility = View.VISIBLE
                                loadnext.visibility = View.GONE
                                loadprev.visibility = View.GONE
                                scroll =0
                                search_default.setText("")
                                filer_default_layout.visibility = View.VISIBLE
                                layouttop.visibility = View.GONE
                                layoutadd.visibility = View.GONE
                                //layout1.visibility = View.GONE
                                tag_group_comp.setChipList(list)
                                def_text_comp.visibility = View.VISIBLE
                                complisting_tag.text = "'"+string.substring(0,string.length-1) +"'" + " not found"
                                mainScroll_jobs.fullScroll(View.FOCUS_UP);
                                mainScroll_jobs.smoothScrollTo(0,0);
                                recycler_view_groups.setFocusable(false);
                                layoutdefaultjob.visibility = View.VISIBLE
                            }
                        }
                        else{
                            Log.d("TAGG","SIZE OF DATA..."+listOfhotJobsdata.size)
                            if(listOfhotJobsdata.size>0) {
                                mgroupsRecyclerView = findViewById(R.id.recycler_view_groups)
                                var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                                mgroupsRecyclerView!!.layoutManager =
                                    mgroupsLayoutManager as RecyclerView.LayoutManager?
                                hotJobsAdapter =
                                    CompaniesAdapter(listOfhotJobsdata, isLoggedIn, 2, 1, listOfCompareJoineddata, 0,"Companies")
                                mgroupsRecyclerView!!.adapter = hotJobsAdapter

                                tag_group_comp.setChipList(list)
                                def_text_comp.visibility = View.GONE
                                complisting_tag.text = total_items.toString()+" matching companies found"
                                layoutdefaultjob.visibility = View.VISIBLE
                                filer_default_layout.visibility = View.VISIBLE
                                layouttop.visibility = View.GONE
                                layoutadd.visibility = View.GONE
                            }
                            else{
                                mgroupsRecyclerView!!.visibility = View.GONE
                                empty_view.visibility = View.VISIBLE
                                loadnext.visibility = View.GONE
                                loadprev.visibility = View.GONE
                                scroll =0
                                search_default.setText("")
                                filer_default_layout.visibility = View.VISIBLE
                                layouttop.visibility = View.GONE
                                layoutadd.visibility = View.GONE
                                //layout1.visibility = View.GONE
                                tag_group_comp.setChipList(list)
                                def_text_comp.visibility = View.VISIBLE
                                complisting_tag.text = "'"+string.substring(0,string.length-1) +"'" + " not found"
                                mainScroll_jobs.fullScroll(View.FOCUS_UP);
                                mainScroll_jobs.smoothScrollTo(0,0);
                                recycler_view_groups.setFocusable(false);
                                layoutdefaultjob.visibility = View.VISIBLE
                            }
                        }
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

                    tag_group_comp.setChipList(list)
                    complisting_tag.text = total_items.toString()+" matching companies found"
                    loadprev.visibility = View.GONE
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
                    empty_view.visibility = View.VISIBLE
                    search_default.setText("")
                    filer_default_layout.visibility = View.VISIBLE
                    layouttop.visibility = View.GONE
                    layoutadd.visibility = View.GONE
                    //layout1.visibility = View.GONE
                    tag_group_comp.setChipList(list)
                    complisting_tag.text = "'"+string.substring(0,string.length-1) +"'" + " not found"
                    mainScroll_jobs.fullScroll(View.FOCUS_UP);
                    mainScroll_jobs.smoothScrollTo(0,0);
                    recycler_view_groups.setFocusable(false);
                    layoutdefaultjob.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<Company>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                // Toast.makeText(applicationContext, "No Data Exists!", Toast.LENGTH_LONG).show()
                loadnext.visibility = View.GONE
                loadprev.visibility = View.GONE
                empty_view.visibility = View.VISIBLE
                scroll =0
                search_default.setText("")
                filer_default_layout.visibility = View.VISIBLE
                layouttop.visibility = View.GONE
                layoutadd.visibility = View.GONE
                tag_group_comp.setChipList(list)
                complisting_tag.text = "'"+string.substring(0,string.length-1) +"'" + " not found"
                //layout1.visibility = View.GONE
                mainScroll_jobs.fullScroll(View.FOCUS_UP);
                mainScroll_jobs.smoothScrollTo(0,0);
                recycler_view_groups.setFocusable(false);
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

                intent = Intent(applicationContext, ZActivityGroups::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("isLoggedIn", true)
                startActivity(intent)
                closeDrawer()

            }
            R.id.action_jobs -> {
                intent = Intent(applicationContext, ZActivityJobs::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("isLoggedIn", true)
                startActivity(intent)
                closeDrawer()

            }
            R.id.action_companies -> {
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
                closeDrawer()
            }
            R.id.action_events -> {

                intent = Intent(applicationContext, ZActivityEvents::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("isLoggedIn",true)
                startActivity(intent)

            }
            R.id.action_blogs-> {
                intent = Intent(applicationContext, WebActivity::class.java)
                intent.putExtra("value","https://www.jobsforher.com/blogs")
                startActivity(intent)
                closeDrawer()
            }
            R.id.action_settings->{
                if(m.findItem(R.id.action_logout).isVisible==true)
                    m.findItem(R.id.action_logout).setVisible(false)
                else
                    m.findItem(R.id.action_logout).setVisible(true)
            }
            R.id.action_logout -> {

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

            R.id.action_share -> {
                HelperMethods.showAppShareOptions(this@ZActivityCompanies)
            }
        }
        return true
    }

    private fun closeDrawer() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    public fun openBottomSheetFilter() {

//        val mLayoutManager1 = GridLayoutManager(applicationContext, 3)
//        mRecyclerView_city!!.layoutManager = mLayoutManager1
//        mAdapterCities = ZGroupsPhotosAdapter(listOfCities)
//        mRecyclerView_city!!.adapter = mAdapterCities

        val dialog = Dialog(this, R.style.AppTheme)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog .setCancelable(true)
        dialog .setContentView(R.layout.bottom_sheet_filter_companies)

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

        val mLayoutManager1 = GridLayoutManager(applicationContext, 1)
        mRecyclerView_city!!.layoutManager = mLayoutManager1
        mAdapterCities = ZCompanyCitiesAdapter(listOfCities,location_nameArray)
        mRecyclerView_city!!.adapter = mAdapterCities

//        val mLayoutManager2 = GridLayoutManager(applicationContext, 1)
//        mRecyclerView_jobtype!!.layoutManager = mLayoutManager2
//        mAdapterJobtype = ZJobTypeAdapter(listOfJobType)
//        mRecyclerView_jobtype!!.adapter = mAdapterJobtype

        val numbers = ArrayList<String>()
        for(i in 0..30)
            numbers.add(i.toString())

        var spinner = dialog.findViewById(R.id.spinner) as Spinner
        var spinner1 = dialog.findViewById(R.id.spinner1) as Spinner
        val dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, numbers)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter)
        spinner1.setAdapter(dataAdapter)

        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                min_year = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        })
        spinner1.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                max_year = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        })

        val mLayoutManager3 = GridLayoutManager(applicationContext, 1)
        mRecyclerView_functionalArea!!.layoutManager = mLayoutManager3
        mAdapterFArea = ZJobsFAreaAdapter(listOfJobFArea,1,functional_areaArray)
        mRecyclerView_functionalArea!!.adapter = mAdapterFArea

        val mLayoutManager4 = GridLayoutManager(applicationContext, 1)
        mRecyclerView_industry!!.layoutManager = mLayoutManager4
        mAdapterIndustry = ZCompanyFAreaAdapter(listOfJobIndustry,2,industriesArray)
        mRecyclerView_industry!!.adapter = mAdapterIndustry


        val city = dialog .findViewById(R.id.city) as TextView
        val industry = dialog .findViewById(R.id.industry) as TextView
        val filterClose = dialog.findViewById(R.id.close_filter) as ImageView
        filterClose.setOnClickListener {
            location_nameArray.clear()
            industriesArray.clear()
            functional_areaArray.clear()
            dialog.cancel();
        }

        city.setTextColor(Color.BLACK)
        city.setOnClickListener {
            mRecyclerView_category.visibility = View.GONE
            mRecyclerView_city.visibility = View.VISIBLE
            mRecyclerView_jobtype.visibility = View.GONE
            mlayout_experience.visibility  =View.GONE
            mRecyclerView_functionalArea.visibility = View.GONE
            mRecyclerView_industry.visibility = View.GONE
            city.setTextColor(Color.BLACK)
            industry.setTextColor(Color.GRAY)
        }

        industry.setOnClickListener {
            mRecyclerView_category.visibility = View.GONE
            mRecyclerView_city.visibility = View.GONE
            mRecyclerView_jobtype.visibility = View.GONE
            mlayout_experience.visibility  =View.GONE
            mRecyclerView_functionalArea.visibility = View.GONE
            mRecyclerView_industry.visibility = View.VISIBLE
            city.setTextColor(Color.GRAY)
            industry.setTextColor(Color.BLACK)
        }

        val keyword_edittext = dialog .findViewById(R.id.keyword_edittext) as EditText
        keyword_edittext.setText(keyword)
        var filter_apply = dialog.findViewById(R.id.filter_apply) as TextView
        var filter_reset = dialog.findViewById(R.id.filter_reset) as TextView
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

        filter_apply.setOnClickListener {

            if (type_group==1)
                type = "boosted"
            else
                type = "basic"

            keyword = keyword_edittext.text.toString()

            loadFilteredData("1",keyword,location_name,industries)
            dialog.cancel()
        }

        val close = dialog.findViewById(R.id.filter_sidecancel) as TextView
        close.setOnClickListener {

            dialog.cancel();
        }

        filter_reset.setOnClickListener {
            keyword_edittext.text.clear()
            keyword=""
            location_name=""
            industries=""
            type=""
            isfilter = 0
            filter.setText("FILTER")
            location_nameArray.clear()
            val mLayoutManager9 = GridLayoutManager(applicationContext, 1)
            mRecyclerView_city!!.layoutManager = mLayoutManager9
            mAdapterCategories = ZCompanyCitiesAdapter(listOfCities,location_nameArray)
            mRecyclerView_city!!.adapter = mAdapterCategories

            functional_areaArray.clear()
            val mLayoutManager4 = GridLayoutManager(applicationContext, 1)
            mRecyclerView_industry!!.layoutManager = mLayoutManager4
            mAdapterIndustry = ZCompanyFAreaAdapter(listOfJobIndustry,2,functional_areaArray)
            mRecyclerView_industry!!.adapter = mAdapterIndustry
            //dialog.cancel()
            loadhotJobsData("1")
            type_group = 1
            mgroupsRecyclerView!!.visibility = View.VISIBLE
            empty_view.visibility = View.GONE
            filer_default_layout.visibility = View.GONE
            mygroups_selected.visibility = View.VISIBLE
            featured_selected.visibility = View.GONE
            featured.setTypeface(null, Typeface.NORMAL);
            mygroups.setTypeface(null, Typeface.BOLD);

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

    public fun getSelectedJobType(name:String) {
        Log.d("TAGG", name)
        if (name.equals(""))
            job_types = ""
        else
            job_types = job_types+name+","
    }

    public fun getSelectedFArea(id:Int ,name:String) {
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
                            val model: CategoryView1 = CategoryView1();
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

        Collections.sort(listOfJobFArea, object : Comparator<FunctionalAreaView> {
            override fun compare(lhs: FunctionalAreaView, rhs: FunctionalAreaView): Int {
                return lhs.name!!.compareTo(rhs.name!!)
            }
        })
        val functionalareaList: ArrayList<String> = ArrayList()

        for (model in listOfJobFArea) {
            functionalareaList.add(model.name!!.toString())
        }

        addJobIndustry()

    }

    fun addJobIndustry(){

        listOfJobIndustry.clear()
        listOfJobIndustry = listOfJobIndustrynew


        Collections.sort(listOfJobIndustry, object : Comparator<FunctionalAreaView> {
            override fun compare(lhs: FunctionalAreaView, rhs: FunctionalAreaView): Int {
                return lhs.name!!.compareTo(rhs.name!!)
            }
        })

        val industryList: ArrayList<String> = ArrayList()

        for (model in listOfJobIndustry) {
            industryList.add(model.name!!.toString())
        }

    }



    fun followCompany(id:Int, btnJoinGroup:Button, btnJoined: Button, status:String, name:String){

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
                    //  Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                    if (status.compareTo("follow")==0) {
                        btnJoinGroup.visibility = View.GONE
                        btnJoined.visibility = View.VISIBLE
                        Snackbar.make(
                            main,
                            "Thank you for following "+name+"",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    else{
                        btnJoinGroup.visibility = View.VISIBLE
                        btnJoined.visibility = View.GONE
                        Snackbar.make(
                            main,
                            "You have successfully unfollowed "+name+"",
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

    fun applyJob(id:Int, btnJoinGroup:Button, title:String, btnJoined: Button){

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
                    //   Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                    openBottomSheetPreferences()


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
                        val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                        val jsonarray_info: JSONArray = jsonObject1.optJSONArray("body")

                        var a:Int = 0
                        for (i in 0 until response.body()!!.body!!.size) {

                            val json_objectdetail: JSONObject = jsonarray_info.optJSONObject(0)
                            a = json_objectdetail.optInt("user_id")
                            Log.d("TAGG", "PREf ID:"+json_objectdetail.optInt("user_id").toString())
                        }
                        checkDefault(a, title, id)
                    }

                }
            }

            override fun onFailure(call: Call<CheckPreferenceResponse>, t: Throwable) {
                Logger.d("TAGG", "Apply Job FAILED : $t")
            }
        })
    }

    fun checkDefault(id:Int, title: String, jobId: Int){


        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.CheckDefault(id, EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<CheckDefaultResponse> {
            override fun onResponse(call: Call<CheckDefaultResponse>, response: Response<CheckDefaultResponse>) {

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
                        val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                        val jsonarray_info: JSONArray = jsonObject1.optJSONArray("body")

                        for (i in 0 until response.body()!!.body!!.size) {

                            val json_objectdetail: JSONObject = jsonarray_info.optJSONObject(i)

                            val model: ResumeView = ResumeView();
                            model.is_default = ""//json_objectdetail.optString("is_default")
                            model.path  =json_objectdetail.optString("path")
                            model.id = json_objectdetail.optInt("id")
                            model.title = if(json_objectdetail.isNull("title"))"" else json_objectdetail.optString("title")
                            model.is_parsed = json_objectdetail.optBoolean("is_parsed")
                            model.created_on = json_objectdetail.optString("created_on")
                            model.modified_on = json_objectdetail.optString("modified_on")
                            model.deleted  =json_objectdetail.optBoolean("deleted")
                            model.user_id = json_objectdetail.optInt("user_id")
                            listOfResumes.clear()
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
                openBottomSheetUploadDoc(jobId,title, listOfResumes)
            }

            override fun onFailure(call: Call<CheckDefaultResponse>, t: Throwable) {

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

                    //  ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())

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

                    //  ToastHelper.makeToast(applicationContext, "Invalid Request")
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



    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?)  {

        if (resultCode == 20) {
            val message:String=data!!.getStringExtra("VAL");
            message_filter = message
//            loadFilteredData("","",message)
        }
        else{
        }
    }

    public override fun onResume() {
        super.onResume()
        // put your code here...

    }
}




