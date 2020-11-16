package com.jobsforher.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.util.Base64
import android.util.Log
import android.view.*
import android.view.animation.OvershootInterpolator
import android.widget.*
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
import com.jobsforher.adapters.ResumeAdapter1
import com.jobsforher.adapters.ZGroupsPhotosAdapter
import com.jobsforher.adapters.ZGroupsVideosAdapter
import com.jobsforher.helpers.HelperMethods
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.models.*
import com.jobsforher.models.JobApplication
import com.jobsforher.models.Specialization
import com.jobsforher.network.responsemodels.*
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.toolbar_groups.*
import kotlinx.android.synthetic.main.zactivity_jobs_details.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ZActivityJobDetails : Footer(), NavigationView.OnNavigationItemSelectedListener {

    var listOfPhotos: ArrayList<String> = ArrayList()
    var mRecyclerViewPhotos: RecyclerView? = null
    var mAdapterPhotos: RecyclerView.Adapter<*>? = null
    var listOfResumes: ArrayList<ResumeView>  = ArrayList()

    var isBoosted:Boolean = false

    var listOfCategories: ArrayList<CategoryView> = ArrayList()
    var listOfCities: ArrayList<CityView> = ArrayList()
    var listOfJobType: ArrayList<JobTypeView> = ArrayList()
    var listOfJobFArea: ArrayList<FunctionalAreaView> = ArrayList()
    var listOfJobFAreaSorted: ArrayList<FunctionalAreaView> = ArrayList()
    var listOfJobIndustry: ArrayList<FunctionalAreaView> = ArrayList()

    var listOfVideos: ArrayList<String> = ArrayList()
    var mRecyclerViewVideos: RecyclerView? = null
    var mAdapterVideos: RecyclerView.Adapter<*>? = null
    var KEY_RECYCLER_STATE: String = "recycler_state"
    var mBundleRecyclerViewState: Bundle? = null

    var listOfPostdata: ArrayList<GroupsPostModel> = ArrayList()
    var listOfPostdataDump: ArrayList<GroupsPostModel> = ArrayList()
    var mRecyclerViewPosts: RecyclerView? = null
    var mAdapterPosts: RecyclerView.Adapter<*>? = null

    var isLoggedIn=false
    var groupId = 0
    var title=""
    var isMyGroup = 0
    var isOwner=""
    var type=1
    var scroll = 0
    var page = ""
    var comp_id = 0
    var isRequired:Boolean = false
    var isboosted:Boolean = false

    private val PREF_PHONE = ""

    private val GALLERY_IMAGE = 2
    private val GALLERY_PDF = 3
    private var retrofitInterface: RetrofitInterface? = null
    private var retrofitInterface1: RetrofitInterface? = null
    var next_page_no_posts: String="1"
    var prev_page_no_posts: String="1"

    private var retrofitInterface_post: RetrofitInterface? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zgroups_jobsdetails_toolbr)



        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        isLoggedIn=intent.getBooleanExtra("isLoggedIn",false)
        groupId = intent.getIntExtra("group_Id",0)
        title = intent.getStringExtra("title")
        isMyGroup = intent.getIntExtra("isMygroup",0)
        type = intent.getIntExtra("type",0)
        page = intent.getStringExtra("page")
        isRequired = intent.getBooleanExtra("isRequired",false)
        isboosted = intent.getBooleanExtra("isboosted",false)
        if (intent.hasExtra("comp_id")) {
            comp_id = intent.getIntExtra("comp_id",0)
        }

        mappingWidgets()

        Log.d("TAGG","TYPE IS"+isMyGroup)
        //groupType = intent.getStringExtra("group_type")
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close
        )

        loadCategoryData()
        jobdescription.setAnimationDuration(750L)
        jobdescription.setInterpolator(OvershootInterpolator())
        jobdescription.setExpandInterpolator( OvershootInterpolator())
        jobdescription.setCollapseInterpolator( OvershootInterpolator())
        jobaboutus.setAnimationDuration(750L)
        jobaboutus.setInterpolator(OvershootInterpolator())
        jobaboutus.setExpandInterpolator( OvershootInterpolator())
        jobaboutus.setCollapseInterpolator( OvershootInterpolator())



        seemore_desc.setOnClickListener{

            jobdescription.toggle()
            if (seemore_desc.text.equals("See More"))
                seemore_desc.setText("See Less")
            else
                seemore_desc.setText("See More")
        }
        seemore_aboutus.setOnClickListener{

            jobaboutus.toggle()
            if (seemore_aboutus.text.equals("See More"))
                seemore_aboutus.setText("See Less")
            else
                seemore_aboutus.setText("See More")
        }

        Log.d("TAGG", "JOB ID IS "+groupId.toString())
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        mRecyclerViewPosts = findViewById(R.id.posts_recycler_view)
        mainScroll_grpdetails.visibility = View.VISIBLE
        button_action_applied.visibility = View.GONE
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
            btnJoined.visibility = View.VISIBLE
            join_group.visibility = View.GONE
            button_applied.visibility = View.VISIBLE
            button_apply.visibility = View.GONE

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
            btnJoined.visibility = View.GONE
            join_group.visibility = View.VISIBLE
            button_applied.visibility = View.GONE
            button_apply.visibility = View.VISIBLE
            loggedin_header.setOnClickListener{
                intent = Intent(applicationContext, ProfileView::class.java)
                startActivity(intent)
            }
            img_profile_toolbar.visibility = View.VISIBLE
//            notificaton.visibility = View.VISIBLE
            sign_in.visibility = View.GONE

//            var mgroupsLayoutManager = LinearLayoutManager(this)
////            mRecyclerViewPosts!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
////            mAdapterPosts = PostsAdapter(listOfPostdata, isLoggedIn)
////            mRecyclerViewPosts!!.adapter = mAdapterPosts
        }

        join_group.setOnClickListener {
            button_apply.callOnClick()
        }

        button_apply.setOnClickListener {

            applyJob(groupId,title,isRequired,isboosted)
        }

        create_preferences.setOnClickListener {
            create_preferences_layout.callOnClick()
        }

        create_preferences_layout.setOnClickListener {

            openBottomSheetJobAlerts(0, jobskill.text.toString(),jobtypefulltime.text.toString(),jobexp.text.toString(),
                jobindustry.text.toString(),jobfunctionalarea.text.toString(),create_preferences_layout)
        }

        switchButton.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {

                if(isChecked){
                    openBottomSheetJobAlerts(0, jobskill.text.toString(),jobtypefulltime.text.toString(),jobexp.text.toString(),
                        jobindustry.text.toString(),jobfunctionalarea.text.toString(),switchButton)
                }
//                Snackbar.make(buttonView, "Switch state checked $isChecked", Snackbar.LENGTH_LONG)
//                    .setAction("ACTION", null).show()
            }
        })


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


//        join_group.setOnClickListener{
//            joinGroup(groupId,join_group)
//        }

        find_morejobs_details.setOnClickListener {
            finish()
        }


        loadJobDetailsData()

        sign_in.setOnClickListener {
            intent = Intent(applicationContext, ZActivityJobDetails::class.java)
            intent.putExtra("isLoggedIn",true)
            startActivity(intent)
        }

//        edittext_createpost.setOnClickListener {
//            intent = Intent(applicationContext, ZCreatePostActivity::class.java)
//            intent.putExtra("groupID",groupId.toString())
//            intent.putExtra("groupName", groupname.text.toString())
//            intent.putExtra("onwner",isOwner )
//            edittext_createpost.text.clear()
//            startActivityForResult(intent, 1);
//        }

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

        starJob.setOnClickListener {
            if("not saved".equals(starJob.getTag())) {
                starJob.setBackgroundResource(R.drawable.ic_starfilled)
                starJob.setTag("saved")
                starJob(groupId)
            }
            else{
                starJob.setBackgroundResource(R.drawable.ic_star)
                starJob.setTag("not saved")
                unstarJob(groupId)
            }

        }

        shareJob.setOnClickListener {
            shareJobFunction()
        }

        opencreatepost.setOnClickListener {
            intent = Intent(applicationContext, ZCreatePostActivity::class.java)
            intent.putExtra("groupID",groupId.toString())
            intent.putExtra("groupName", title.toString())
            intent.putExtra("edittext_data",edittext_createpost.text.toString() )
            intent.putExtra("onwner",isOwner )
            edittext_createpost.text.clear()
            startActivity(intent)
        }


        mainScroll_grpdetails.getViewTreeObserver().addOnScrollChangedListener(object :ViewTreeObserver.OnScrollChangedListener {
            override fun onScrollChanged() {
                Log.d("TAGG","END EFORE")
                val view: View = mainScroll_grpdetails.getChildAt(mainScroll_grpdetails.getChildCount() - 1);

                val diff:Int = (view.getBottom() - (mainScroll_grpdetails.getHeight() + mainScroll_grpdetails.getScrollY()));

                if (diff == 0 && scroll ==1) {
                    Log.d("TAGG","END"+next_page_no_posts)
                    //loadGroupPosts(next_page_no_posts)
                }
            }
        });

        img_profile_toolbar.setOnClickListener{
            intent = Intent(applicationContext, ZActivityDashboard::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("isLoggedIn", true)
            intent.putExtra("pagetype", 0)
            startActivity(intent)
        }
    }


    fun shareJobFunction(){

        val s:String = title.replace(" ","-")
        val s1:String = s.replace("_","-")

        intent = Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(Intent.EXTRA_SUBJECT, title.toString()+"| JobsForHer");
        intent.putExtra(Intent.EXTRA_TEXT, "Click on the link \n http://www.jobsforher.com/jobs/"+s1.toLowerCase()+"/"+groupId+"\n\n"+
                "Application Link : https://play.google.com/store/apps/details?id=com.jobsforher");
        //intent.putExtra(Intent.EXTRA_TEXT, "Application Link : https://play.google.com/store/apps/details?id=${context.getPackageName()}")
        startActivity(Intent.createChooser(intent, "Share Job link!"));
    }

    fun starJob(id:Int){

        val params = HashMap<String, String>()
        params["job_id"] = id.toString()

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.starJob(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN, params)

        call.enqueue(object : Callback<StarJobResponse> {
            override fun onResponse(call: Call<StarJobResponse>, response: Response<StarJobResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful) {
                    // Toast.makeText(applicationContext, "Job saved succesfuly", Toast.LENGTH_LONG).show()
                    Snackbar.make(
                        my_swipeRefresh_jobs,
                        "Job saved succesfully",
                        Snackbar.LENGTH_SHORT
                    ).show()

                } else {

                }
            }

            override fun onFailure(call: Call<StarJobResponse>, t: Throwable) {
                Logger.d("TAGG", "Apply Job FAILED : $t")
            }
        })
    }

    fun unstarJob(id:Int){

        val params = HashMap<String, String>()
        params["job_id"] = id.toString()

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.unstarJob(id,EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<StarJobResponse> {
            override fun onResponse(call: Call<StarJobResponse>, response: Response<StarJobResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful) {
                    // Toast.makeText(applicationContext, "Job saved deleted succesfuly", Toast.LENGTH_LONG).show()
                    Snackbar.make(
                        my_swipeRefresh_jobs,
                        "Job saved deleted succesfully",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {

                }
            }

            override fun onFailure(call: Call<StarJobResponse>, t: Throwable) {
                Logger.d("TAGG", "Apply Job FAILED : $t")
            }
        })
    }

    fun applyJob(id:Int, title:String,isRequired:Boolean, isboosted:Boolean){

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
                    //Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG).show()
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
                            for (i in 0 until response.body()!!.body!!.size) {

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
                        listOfResumes.clear()
                        for (i in 0 until response.body()!!.body!!.size) {

                            val json_objectdetail: JSONObject = jsonarray_info.optJSONObject(i)

                            val model: ResumeView = ResumeView();
                            model.is_default = ""//json_objectdetail.optString("is_default")
                            model.path  =json_objectdetail.optString("path")
                            model.id = json_objectdetail.optInt("id")
                            model.title = if(json_objectdetail.isNull("title"))"" else json_objectdetail.optString("title")
                            model.is_parsed = false//json_objectdetail.getBoolean("is_parsed")
                            model.created_on = json_objectdetail.optString("created_on")
                            model.modified_on = if(json_objectdetail.isNull("modified_on"))"" else json_objectdetail.optString("modified_on")
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

                    //  ToastHelper.makeToast(applicationContext, "Invalid Request")
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
                openBottomSheetUploadDoc(jobId,title, listOfResumes,isboosted)
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
                    experience_multiAutoCompleteTextView.text.trim().toString()
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

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

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
                        //  Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG).show()
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

    fun updatePreferences(id:Int,title:String,id1:Int,isRequired: Boolean,isboosted: Boolean,pref_location: String, jobtype: String, farea:String, industry: String, pref_keyword:String,experience:String ){

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
        if (experience.equals("")){}
        else{
            params["exp_from_year"] = experience.toString()
            params["exp_to_year"] = experience.toString()
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
        var checklist: ArrayList<Int>  = ArrayList()
        for(k in 0 until listOfResume.size) {
            if (k == 0)
                checklist.add(1)
            else
                checklist.add(0)
        }
        if(listOfResumes.size>1)
            mRecyclerView.getLayoutParams().height = 600;
        mAdapterResume = ResumeAdapter1(listOfResumes,checklist,"Jobs Details")
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
        if(isBoosted) {
            val msg: String = title + "   "
            val mImageSpan: ImageSpan = ImageSpan(applicationContext, R.drawable.ic_hot_job);
            val text: SpannableString = SpannableString(msg);
            text.setSpan(mImageSpan, msg.length - 1, msg.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            jobname.setText(text);
        }
        else{
            jobname.setText(title)
        }
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
        val mobileLayout = dialog.findViewById(R.id.mobile_layout) as LinearLayout
        if(EndPoints.PHONE_NO.length>2)
            mobileLayout.visibility = View.GONE
        val save_pref = dialog.findViewById(R.id.save_pref) as Button
        save_pref.setOnClickListener {
            if(restitle.getText().length>0) {
                //if (mobileno.getText().length > 0 && mobileno.length() >6 && mobileno.length() < 13) {
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
//                } else {
//                    Toast.makeText(applicationContext, "Please enter valid mobile number", Toast.LENGTH_LONG).show()
//
//                }
            }
            else{
                Toast.makeText(applicationContext, "Please enter resume title", Toast.LENGTH_LONG).show()
            }
        }

        val apply_job = dialog.findViewById(R.id.apply_pref) as Button
        apply_job.setOnClickListener {
            //if (mobileno.getText().length > 0 && mobileno.length() >6 && mobileno.length() < 13) {
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
                    }
                    else{
                        Toast.makeText(applicationContext, "Please enter resume title", Toast.LENGTH_LONG).show()
                    }
                }
                else
                    Toast.makeText(applicationContext, "Please upload a resume", Toast.LENGTH_LONG).show()
            }
//            } else {
//                Toast.makeText(applicationContext, "Please enter a valid mobile number", Toast.LENGTH_LONG).show()
//            }
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

        var window :Window = dialog.getWindow()!!
        window.setBackgroundDrawable(null)
        var wlp: WindowManager.LayoutParams = window.getAttributes()
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT
        wlp.gravity = Gravity.BOTTOM
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        window.setAttributes(wlp)
        dialog .show()
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
                    var jsonarray: JSONObject = jsonObject1.optJSONObject("body")

                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Adding Success!!", Toast.LENGTH_LONG)
                            .show()
                        val resumeId = jsonarray.optInt("id")

                        saveJob(jobId, "", resumeId, "Applied")
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

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY_IMAGE)
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

                    mainScroll_grpdetails.visibility = View.VISIBLE
                    //button_action_applied.visibility = View.VISIBLE
                    button_apply.visibility = View.GONE
                    button_applied.visibility = View.VISIBLE
                    join_group.visibility = View.GONE
                    btnJoined.visibility = View.VISIBLE



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


    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {

        if (requestCode == GALLERY_IMAGE) {
            if (data != null) {
                val contentURI = data!!.data
                try {
                    val f = File(contentURI.toString())
                    picturepath_life = f.getName() + ".png"
                    Log.d("TAGG", picturepath_life.toString())
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
//                    pick_image!!.setImageBitmap(bitmap)
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val b = baos.toByteArray()
                    imageEncoded_life = Base64.encodeToString(b, Base64.DEFAULT)
                    Log.d("TAGG", picturepath_life.toString())
                    Log.d("TAGG", imageEncoded_life.toString())
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(applicationContext, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        if (requestCode == GALLERY_PDF) {

            if (data != null) {

                if (data != null) {
                    val contentURI1 = data!!.data
                    try {

                        val fileName = HelperMethods.getFilePath(this, data.data)

                        val fileString = HelperMethods.testUriFile(this, fileName!!, data.data)

                        val file = data?.data?.let {
                            HelperMethods.getFile(
                                this@ZActivityJobDetails,
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


    fun loadJobDetailsData(){

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getJobDetailsData(groupId,EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)
        Logger.d("URL", "" + "HI" +groupId)
        call.enqueue(object : Callback<JobDetails> {
            override fun onResponse(call: Call<JobDetails>, response: Response<JobDetails>) {

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
                var message: String? = null
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    jsonObject1 = jsonObject.optJSONObject("body")
                }
                if (jsonObject1 != null) {
                    val responseCode: Int = jsonObject1.optInt("response_code")
                    message = jsonObject1.optString("message")
                    jsonarray_info = jsonObject1.optJSONArray("body")
                }

                if (response.isSuccessful) {

                    var json_objectdetail: JSONObject = jsonarray_info?.optJSONObject(0)!!
                    val model: JobDetailsData = JobDetailsData();

                    model.id = json_objectdetail.optInt("id")
                    model.description = json_objectdetail.optString("description")
                    model.location_name = json_objectdetail.optString("location_name")
                    model.company_id = json_objectdetail.optInt("company_id")
                    model.min_year = json_objectdetail.optInt("min_year")
                    model.resume_required = json_objectdetail.getBoolean("resume_required")
//                        model.co_owners = json_objectdetail.optString("co_owners")
                    model.salary = json_objectdetail.optString("salary")
//                        model.specialization_id = json_objectdetail.optInt("specialization_id")
                    model.job_posting_type = json_objectdetail.optString("job_posting_type")
                    model.title = json_objectdetail.optString("title")
                    model.user_id = json_objectdetail.optInt("user_id")
//                        model.redirect_url = json_objectdetail.optString("redirect_url")
//                        model.min_qualification = json_objectdetail.optString("min_qualification")
                    model.vacancy = json_objectdetail.optString("vacancy")
                    model.max_year = json_objectdetail.optInt("max_year")
//                        model.location_id = json_objectdetail.optInt("location_id")
                    model.modified_on = json_objectdetail.optString("modified_on")
                    model.created_on = json_objectdetail.optString("created_on")
                    model.published_on = json_objectdetail.optString("published_on")
//                        model.view_count = json_objectdetail.optInt("view_count")
                    model.status = json_objectdetail.optString("status")
                    model.application_notification_type =
                        json_objectdetail.optString("application_notification_type")
                    model.boosted = json_objectdetail.getBoolean("boosted")
                    model.boosted_expire_on = json_objectdetail.optString("boosted_expire_on")
                    model.skills = json_objectdetail.optString("skills")
                    model.functional_area = json_objectdetail.optString("functional_area")
                    model.industries = json_objectdetail.optString("industries")
                    model.job_types = json_objectdetail.optString("job_types")
                    model.additional_information =
                        json_objectdetail.optString("additional_information")
                    model.languages = json_objectdetail.optString("languages")
                    var job_applications: JSONObject =
                        json_objectdetail.optJSONObject("job_applications")
                    val jobapplication_model: JobApplication = JobApplication()
                    jobapplication_model.applied = job_applications.getBoolean("applied")
                    jobapplication_model.count = job_applications.optInt("count")
                    model.job_applications = jobapplication_model

                    var job_specialization: JSONArray =
                        json_objectdetail.optJSONArray("specialization")
                    var specdata: ArrayList<Specialization> = ArrayList()
                    for (k in 0 until job_specialization.length()) {

                        val jobspecializtion_model: Specialization = Specialization()
                        jobspecializtion_model.course =
                            job_specialization.optJSONObject(k).optString("course")
                        jobspecializtion_model.course_id =
                            job_specialization.optJSONObject(k).optInt("course_id")
                        jobspecializtion_model.specialization =
                            job_specialization.optJSONObject(k).optString("specialization")
                        jobspecializtion_model.specialization_id =
                            job_specialization.optJSONObject(k).optInt("specialization_id")
                        specdata.add(jobspecializtion_model)
                    }
                    model.specialization = specdata
                    model.job_applications = jobapplication_model
                    model.job_saved = json_objectdetail.getBoolean("job_saved")

                    if (model.status.equals("active")) {
//                            btnJoined.visibility = View.VISIBLE
//                            join_group.visibility = View.GONE
//                            button_apply.visibility = View.GONE
//                            button_applied.visibility = View.VISIBLE
                        //row_status.visibility = View.GONE
                    } else {
                        btnJoined.visibility = View.VISIBLE
                        join_group.visibility = View.GONE
                        // row_status.visibility = View.VISIBLE
                        button_apply.visibility = View.VISIBLE
                        button_applied.visibility = View.GONE

                        starJob.visibility = View.GONE
                        shareJob.visibility = View.GONE

                        btnJoined.isEnabled = false
                        btnJoined.setBackgroundResource(R.drawable.curved_grey_without_border)
                        btnJoined.setTextColor(Color.BLACK)
                        btnJoined.setText("Job is closed")
                        join_group.isEnabled = false

                        button_apply.isEnabled = false
                        button_apply.setBackgroundResource(R.drawable.curved_grey_without_border)
                        button_apply.setTextColor(Color.BLACK)
                        button_apply.setText("Job is closed")
                        button_applied.isEnabled = false
                    }


                    isBoosted = model.boosted!!

                    if (model.job_saved!!) {
                        starJob.setBackgroundResource(R.drawable.ic_starfilled)
                        starJob.setTag("saved")
                    } else {
                        starJob.setBackgroundResource(R.drawable.ic_star)
                        starJob.setTag("not saved")
                    }

                    var company: JSONObject = json_objectdetail.optJSONObject("company")
                    val company_model: CompanyDetailss = CompanyDetailss()
                    company_model.id = company.optInt("id")
                    company_model.established_date = company.optString("established_date")
                    company_model.company_type = company.optString("company_type")
                    company_model.diversity = company.optString("diversity")
                    company_model.cities = company.optString("cities")
//                            company_model.banner_image = company.optString("banner_image")
                    company_model.logo = company.optString("logo")
//                            company_model.website = company.optString("website")
                    company_model.deleted = company.getBoolean("deleted")
                    company_model.created_by = company.optInt("created_by")
                    company_model.featured = company.getBoolean("featured")
//                            company_model.team_size = company.optString("team_size")
                    company_model.featured_end_date = company.optString("featured_end_date")
                    company_model.industry = company.optString("industry")
//                            company_model.sac_hsc_no = company.optString("sac_hsc_no")
//                            company_model.company_profile_percentage = company.optString("company_profile_percentage")
//                            company_model.company_profile_url = company.optString("company_profile_url")
                    company_model.modified_on = company.optString("modified_on")
                    company_model.gstin_no = company.optString("gstin_no")
                    company_model.about_us = company.optString("about_us")
                    company_model.created_on = company.optString("created_on")
                    company_model.name = company.optString("name")
//                            company_model.view_count = company.optString("view_count")
                    company_model.status = company.optString("status")
                    company_model.culture = company.optString("culture")
                    company_model.active_jobs_count = company.optInt("active_jobs_count")

                    model.company = company_model

                    if (model.boosted!!) {
                        val msg: String = model.title + "   "
                        val mImageSpan: ImageSpan =
                            ImageSpan(applicationContext, R.drawable.ic_hot_job);
                        val text: SpannableString = SpannableString(msg);
                        text.setSpan(
                            mImageSpan,
                            msg.length - 1,
                            msg.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        );
                        jobname.setText(text);
                    } else {
                        jobname.setText(model.title)
                    }
                    val msg1: String = "   " + model.company!!.name
                    val mImageSpan1: ImageSpan =
                        ImageSpan(applicationContext, R.drawable.ic_companies);
                    val text1: SpannableString = SpannableString(msg1);
                    text1.setSpan(mImageSpan1, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    companytype.setText(text1);
                    // companytype.setText(model.company!!.name)

                    val msg2: String = "   " + model.location_name!!
                    val mImageSpan2: ImageSpan =
                        ImageSpan(applicationContext, R.drawable.ic_location);
                    val text2: SpannableString = SpannableString(msg2);
                    text2.setSpan(mImageSpan2, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    joblocation.setText(text2);
                    //joblocation.setText(model.location_name)

                    if (model.job_applications!!.count!! > 9) {
                        jobapplied.setText(model.job_applications!!.count.toString() + " Applied")
                    } else {
                        jobapplied.visibility = View.GONE
                        jobappliedView.visibility = View.GONE
                    }


                    jobskill.setText(model.skills)
                    if (model.job_types.equals("full_time")) {
                        jobtypefulltime.visibility = View.VISIBLE
                        jobtypeparttime.visibility = View.GONE
                        jobtypefreelance.visibility = View.GONE
                    } else if (model.job_types.equals("part_time")) {
                        jobtypefulltime.visibility = View.GONE
                        jobtypeparttime.visibility = View.VISIBLE
                        jobtypefreelance.visibility = View.GONE
                    } else {
                        jobtypefulltime.visibility = View.GONE
                        jobtypeparttime.visibility = View.GONE
                        jobtypefreelance.visibility = View.VISIBLE
                    }
                    jobexp.setText(model.min_year.toString() + " - " + model.max_year + "Yrs")
                    if (model.salary!!.length > 0)
                        jobrupee.setText(model.salary)
                    else {
                        jobrupee.visibility = View.GONE
                        jobrupeeView.visibility = View.GONE
                    }
                    jobindustry.setText(model.industries)
                    jobfunctionalarea.setText(model.functional_area)

                    jobdescription.setText(Html.fromHtml(model.description))
                    if (jobdescription.lineCount == 5) {

                        seemore_desc.visibility = View.VISIBLE
                    } else {
                        seemore_desc.visibility = View.GONE
                    }

                    jobaboutus.setText(Html.fromHtml(model.company!!.about_us))

                    if (jobaboutus.lineCount == 5) {
                        seemore_aboutus.visibility = View.VISIBLE
                    } else {
                        seemore_aboutus.visibility = View.GONE
                    }

                    val userDob: Date =
                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(model.created_on.toString());
                    val today: Date = Date();
                    val diff = today.getTime() - userDob.getTime()
                    val numOfDays: Long = (diff / (1000 * 60 * 60 * 24));
//                        int hours = (int) (diff / (1000 * 60 * 60));
//                        int minutes = (int) (diff / (1000 * 60));
//                        int seconds = (int) (diff / (1000));
                    jobtiming.setText(numOfDays.toString() + " days ago")
                    if (model.company!!.logo!!.isNotEmpty()) {
                        Picasso.with(applicationContext)
                            .load(model.company!!.logo)
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .into(job_logo)
                    } else {
                        Picasso.with(applicationContext)
                            .load(R.drawable.ic_launcher_foreground)
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .into(job_logo)
                    }

                } else {
                    if (message != null) {
                        ToastHelper.makeToast(applicationContext, message)
                    }
                    finish()
                    overridePendingTransition(0, 0);
                }
            }

            override fun onFailure(call: Call<JobDetails>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
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
        if(page.equals("Company Details")){
            val intent = Intent(applicationContext, ZActivityCompanyDetails::class.java)
            //intent.flags = FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("isLoggedIn", true)
            intent.putExtra("group_Id", comp_id)
            intent.putExtra("title", title)
            intent.putExtra("isMygroup",1)
            intent.putExtra("page","Company")
            startActivity(intent)
            finish()

        }
        else if(page.equals("Dashboard")){
            val intent = Intent(applicationContext, ZActivityDashboard::class.java)
            intent.flags = FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("isLoggedIn", true)
            startActivity(intent)
            finish()
        }
        else if(page.equals("NewsFeed")){
            val intent = Intent(applicationContext, NewsFeed::class.java)
            intent.flags = FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("isLoggedIn", true)
            startActivity(intent)
            finish()
        }
        else {
            val intent = Intent(applicationContext, ZActivityJobs::class.java)
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

    fun loadPhotosData(post_type: String){

        listOfPhotos.clear()
        val params = HashMap<String, String>()

        params["post_type"] = post_type

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getImages(groupId, EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN, params)
        call.enqueue(object : Callback<ImageResponse> {

            override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {

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
                            listOfPhotos.add(json_objectdetail.optString("url"))
                        }

                        if (listOfPhotos.size > 0) {
                            if (post_type.equals("image")) {
                                mRecyclerViewPhotos = findViewById(R.id.recycler_view_photos)
                                val mLayoutManager = GridLayoutManager(applicationContext, 3)
                                mRecyclerViewPhotos!!.layoutManager = mLayoutManager
                                mAdapterPhotos = ZGroupsPhotosAdapter(listOfPhotos)
                                mRecyclerViewPhotos!!.adapter = mAdapterPhotos
                            } else {
                                mRecyclerViewVideos = findViewById(R.id.recycler_view_videos)
                                var mLayoutManager = GridLayoutManager(applicationContext, 1)
                                mRecyclerViewVideos!!.layoutManager = mLayoutManager
                                mAdapterVideos = ZGroupsVideosAdapter(listOfPhotos)
                                mRecyclerViewVideos!!.adapter = mAdapterVideos
                            }
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "No data to load!!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid")
                    }
                }
            }

            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {

                Logger.d("TAG", "FAILED : $t")
                Toast.makeText(applicationContext, "No data to load!!", Toast.LENGTH_LONG).show()
            }
        })


    }

    fun loadVideosData(){


        loadPhotosData("video")

//        mRecyclerViewVideos = findViewById(R.id.recycler_view_videos)
//        var mLayoutManager = GridLayoutManager(this, 2)
//        mRecyclerViewVideos!!.layoutManager = mLayoutManager
//        mAdapterVideos = ZGroupsVideosAdapter(listOfVideos)
//        mRecyclerViewVideos!!.adapter = mAdapterVideos
    }

    public fun openBottomSheetJobAlerts(id: Int,skill:String,jobtype:String,experience:String,jobindustry:String,functionalarea:String,
                                        v:View) {

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
//        for (model in listOfJobType) {
//            jobTypeList.add(model.name!!.toString())
//        }
        val randomArray = ArrayAdapter(this, android.R.layout.simple_list_item_1, listOfJobType)
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
                pref_title.text.trim().toString().length>0   ) {

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
                    //Toast.makeText(applicationContext,"Job Alert created!!", Toast.LENGTH_LONG).show()
                }
                dialog.cancel()
            }
            else{
                Toast.makeText(applicationContext,"Please enter all the values",Toast.LENGTH_LONG).show()
            }
        }

        pref_title.setText(jobname.text.toString())
        pref_location_multiAutoCompleteTextView.setText(joblocation.text.toString()+",")
        jobtype_multiAutoCompleteTextView.setText(jobtype+",")
        farea_multiAutoCompleteTextView.setText(functionalarea+",")
        industry_multiAutoCompleteTextView.setText(jobindustry+",")
        experience_multiAutoCompleteTextView.setText(experience)
        pref_keyword.setText(skill+",")


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

        val pref_keyword= dialog.findViewById(R.id.pref_keyword) as EditText

        val experience_multiAutoCompleteTextView = dialog.findViewById(R.id.experience_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        val experienceList: ArrayList<String> = ArrayList()
        for(i in 0..50)
            experienceList.add(i.toString())
        val adapter2 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, experienceList)
        experience_multiAutoCompleteTextView.setAdapter(adapter2)
        experience_multiAutoCompleteTextView.setThreshold(1)
        experience_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        experience_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                experience_multiAutoCompleteTextView.showDropDown()
                return false
            }
        })

        val experienceto_multiAutoCompleteTextView = dialog.findViewById(R.id.experienceto_multiAutoCompleteTextView) as MultiAutoCompleteTextView
        experienceto_multiAutoCompleteTextView.setAdapter(adapter2)
        experienceto_multiAutoCompleteTextView.setThreshold(1)
        experienceto_multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        experienceto_multiAutoCompleteTextView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                experienceto_multiAutoCompleteTextView.showDropDown()
                return false
            }
        })

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

        val save_pref = dialog.findViewById(R.id.save_pref) as Button
        save_pref.setOnClickListener {
            if(pref_location_multiAutoCompleteTextView.text.trim().toString().length>0 &&
                pref_keyword.text.trim().toString().length>0    ) {
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
            else{
                Toast.makeText(applicationContext,"Please enter all the values",Toast.LENGTH_LONG).show()
            }

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

    fun addJobAlerts(pref_title: String,pref_location: String, jobtype: String, farea:String, industry: String, pref_keyword:String,
                     experience:String ){

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

                if (response.isSuccessful && response.body()!!.responseCode == 10999) {

                    // ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                    Logger.e("TAGG", "Add Success" + response.body()!!.message.toString())
                    //loadJobAlert()
                    Snackbar.make(
                        my_swipeRefresh_jobs,
                        response.body()!!.message.toString(),
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    // loadJobAlert()
                    ToastHelper.makeToast(applicationContext, "You have reached the maximum limit of Job alerts. You need to delete one job alert to add a new one")
                    switchButton.isChecked = false
                }
            }

            override fun onFailure(call: Call<JobAlertResponse>, t: Throwable) {
                //loadJobAlert()
                Logger.d("TAGG", "FAILED : $t")
//                Snackbar.make(
//                    my_swipeRefresh_jobs,
//                    "Your Job Alert ‘"+pref_title.toString()+"’ has been created. You will now receive job alerts " +
//                            "in your email, twice a week.",
//                    Snackbar.LENGTH_SHORT
//                ).show()
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

    fun savePreferences(pref_location: String, jobtype: String, farea:String, industry: String, pref_keyword:String,experience:String,
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

    fun loadCategoryData(){

        listOfCategories.clear()
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getCategories(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)
        call.enqueue(object : Callback<GetCategoryResponse> {

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
//        functionalarea.setAdapter(adapter)
//        functionalarea.setThreshold(1)
//        functionalarea.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
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

//        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, industryList)
//        industry.setAdapter(adapter)
//        industry.setThreshold(1)
//        industry.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())


        val cityList: ArrayList<String> = ArrayList()

        for (model in listOfCities) {
            cityList.add(model.name!!.toString())
        }

//        val adapter1 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cityList)
//        city.setAdapter(adapter1)
//        city.setThreshold(1)
//        city.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        //openBottomSheetFilter()

        val numbers = ArrayList<String>()
        for(i in 0..45)
            numbers.add(i.toString())
        val adapter2 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, numbers)
//        experience.setAdapter(adapter2)
//        experience.setThreshold(1)
//        experience.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
    }


}

