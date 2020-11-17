package com.jobsforher.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.*
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jobsforher.BuildConfig
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
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_newsfeed.*
import kotlinx.android.synthetic.main.toolbar_groups.*
import kotlinx.android.synthetic.main.zactivity_newsfeed.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class NewsFeed : Footer(), NavigationView.OnNavigationItemSelectedListener {

    var listOfPhotos: ArrayList<String> = ArrayList()
    var mRecyclerViewPhotos: RecyclerView? = null
    var mAdapterPhotos: RecyclerView.Adapter<*>? = null
    private var doubleBackToExitPressedOnce = false

    var listOfVideos: ArrayList<String> = ArrayList()
    var mRecyclerViewVideos: RecyclerView? = null
    var mAdapterVideos: RecyclerView.Adapter<*>? = null
    var KEY_RECYCLER_STATE: String = "recycler_state"
    var mBundleRecyclerViewState: Bundle? = null

    var mRecyclerViewAds: RecyclerView? = null
    var mAdapterAds: RecyclerView.Adapter<*>? = null

    var listOfhotJobsdata: ArrayList<JobsView> = ArrayList()
    var listOfhotJobsdataTrimmed: ArrayList<JobsView> = ArrayList()
    var listOfCompareJoineddata: ArrayList<Int> = ArrayList()
    var listOfCompaniesdata: ArrayList<CompaniesView> = ArrayList()
    var mRecyclerViewCompanies: RecyclerView? = null
    var mRecyclerViewJobs: RecyclerView? = null
    var mAdapterCompanies: RecyclerView.Adapter<*>? = null
    var mAdapterJobs: RecyclerView.Adapter<*>? = null

    var listOfGroupdata: ArrayList<GroupsView> = ArrayList()
    var listOfCompareGroupdata: ArrayList<GroupsView> = ArrayList()
    var listOfGroupdataTrimmed: ArrayList<GroupsView> = ArrayList()
    var mAdapterGroups: RecyclerView.Adapter<*>? = null
    var mRecyclerViewGroups: RecyclerView? = null

    var listOfPostdata: ArrayList<GroupsPostModel> = ArrayList()
    var listOfPostdataDump: ArrayList<GroupsPostModel> = ArrayList()
    var mRecyclerViewPosts: RecyclerView? = null
    var mAdapterPosts: RecyclerView.Adapter<*>? = null
    var mRecyclerViewPosts1: RecyclerView? = null
    var mAdapterPosts1: RecyclerView.Adapter<*>? = null

    var isLoggedIn = false
    var groupId = 0
    var isMyGroup = 0
    var isOwner = ""
    var groupType = ""
    var page = ""

    private var retrofitInterface: RetrofitInterface? = null
    private var retrofitInterface1: RetrofitInterface? = null
    private val PREF_FCM = "fcmtoken"
    var next_page_no_posts: Int = 1
    var prev_page_no_posts: String = "1"
    private var loading = true

    private var retrofitInterface_post: RetrofitInterface? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zactivity_newsfeed)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        isLoggedIn = intent.getBooleanExtra("isLoggedIn", false)
        groupId = intent.getIntExtra("group_Id", 0)
        isMyGroup = intent.getIntExtra("isMygroup", 0)
        groupType = "Profile"
        page = "Profile"

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close
        )
        Log.d("TAGG", groupId.toString())
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        mRecyclerViewCompanies = findViewById(R.id.companies_recycler_view)
        mRecyclerViewJobs = findViewById(R.id.jobs_recycler_view)
        mRecyclerViewGroups = findViewById(R.id.groups_recycler_view)

        val sharedPref: SharedPreferences = getSharedPreferences(
            "mysettings",
            Context.MODE_PRIVATE
        );
        var fcm = sharedPref.getString(PREF_FCM, "")
        registerFCMToken(
            Settings.Secure.getString(
                applicationContext.getContentResolver(),
                Settings.Secure.ANDROID_ID
            ), fcm!!
        )
        Log.d("TAGG", "NEWSFEED" + fcm)

        mappingWidgets()
        handleBackgrounds(btnHome)

        loadRecommendedJobs("1")
        loadRecommendedCompanies("1")
        loadRecommendedMyGroupData("1")

        loadGroupPosts("2")

        version.text = "Version - "+ BuildConfig.VERSION_NAME
        if (isLoggedIn) {

//            ToastHelper.makeToast(applicationContext, groupId.toString())
            val menu = navView.menu
            menu.findItem(R.id.action_employerzone).setVisible(false)
            menu.findItem(R.id.action_mentorzone).setVisible(false)
            menu.findItem(R.id.action_partnerzone).setVisible(false)
            // menu.findItem(R.id.action_logout).setVisible(true)
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
                intent = Intent(applicationContext, SignUpWelcomeActivity::class.java)
                startActivity(intent)
            }
            img_profile_toolbar.visibility = View.VISIBLE
//            notificaton.visibility = View.VISIBLE
            sign_in.visibility = View.GONE


        } else {
            val menu = navView.menu
            menu.findItem(R.id.action_employerzone).setVisible(false)
            menu.findItem(R.id.action_mentorzone).setVisible(false)
            menu.findItem(R.id.action_partnerzone).setVisible(false)
            //menu.findItem(R.id.action_logout).setVisible(true)
            menu.findItem(R.id.action_settings).setVisible(false)
            menu.findItem(R.id.action_signup).setVisible(false)
            menu.findItem(R.id.action_login).setVisible(false)
//            menu.findItem(R.id.action_dashboard).title = Html.fromHtml("<font color='#99CA3B'>Dashboard</font>")
//            menu.findItem(R.id.action_dashboard).icon = getDrawable(R.drawable.ic_sidemenu_dashboard_green)
            //navView.getMenu().getItem(0).setChecked(true);

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
                intent = Intent(applicationContext, SignUpWelcomeActivity::class.java)
                startActivity(intent)
            }
            img_profile_toolbar.visibility = View.VISIBLE
//            notificaton.visibility = View.VISIBLE
            sign_in.visibility = View.GONE
        }

        notifLayout.setOnClickListener {
            cart_badge.setText("0")
            intent = Intent(applicationContext, Notification::class.java)
            startActivity(intent)
        }
        //  cart_badge.setText("26")
        //loadNotificationbubble()

        val mLayoutManager = LinearLayoutManager(
            applicationContext, LinearLayoutManager.HORIZONTAL,
            false
        )
        var listOfAdsdata: ArrayList<String> = ArrayList()
        var listOfAdsdata1: ArrayList<String> = ArrayList()
        var listOfAdsdata2: ArrayList<String> = ArrayList()
        mRecyclerViewAds = findViewById(R.id.jobsAds_recycler_view)
        listOfAdsdata.add("Complete your Profile +")
        listOfAdsdata.add("Find a group that fits you")
        listOfAdsdata.add("Upcoming Events")
        listOfAdsdata.add("Upcoming Events")
        listOfAdsdata.add("Upcoming Events")

        listOfAdsdata1.add("Add your work and life experience to show who you are")
        listOfAdsdata1.add("Connect, learn, and share with our growing communities of women")
        listOfAdsdata1.add("Get hired by your dream company at exclusive events")
        listOfAdsdata1.add("Get hired by your dream company at exclusive events")
        listOfAdsdata1.add("Get hired by your dream company at exclusive events")

        listOfAdsdata2.add("Create Profile +")
        listOfAdsdata2.add("Join Group")
        listOfAdsdata2.add("Register for free")
        listOfAdsdata2.add("Register for free")
        listOfAdsdata2.add("Register for free")
        mRecyclerViewAds!!.layoutManager = mLayoutManager
        //  mRecyclerViewAds!!.addItemDecoration( LinePagerIndicatorDecoration())
        mAdapterAds =
            NewsAdsAdapter(listOfAdsdata, listOfAdsdata1, listOfAdsdata2, isLoggedIn, 1, 0)
        var snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(mRecyclerViewAds);
        mRecyclerViewAds!!.setOnFlingListener(null);
        mRecyclerViewAds!!.adapter = mAdapterAds

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                //Call your function here
                scroll(listOfAdsdata.size)
                handler.postDelayed(this, 5000)//1 sec delay
            }
        }, 0)

        jobssheader1.setOnClickListener {
            intent = Intent(applicationContext, ZActivityJobs::class.java)
            intent.putExtra("isLoggedIn", true)
            startActivity(intent)
        }
        companiessheader1.setOnClickListener {
            intent = Intent(applicationContext, ZActivityCompanies::class.java)
            intent.putExtra("isLoggedIn", true)
            startActivity(intent)
        }
        groupssheader1.setOnClickListener {
            intent = Intent(applicationContext, ZActivityGroups::class.java)
            intent.putExtra("isLoggedIn", true)
            startActivity(intent)
        }

        img_profile_toolbar.setOnClickListener {
            intent = Intent(applicationContext, ZActivityDashboard::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("isLoggedIn", true)
            intent.putExtra("pagetype", 0)
            startActivity(intent)
        }




        sign_in.setOnClickListener {
            intent = Intent(applicationContext, ZActivityGroupsDetails::class.java)
            intent.putExtra("isLoggedIn", true)
            startActivity(intent)
        }

        my_swipeRefresh_Layout.setOnRefreshListener {
            listOfPostdata.clear()
            loadGroupPosts("2")
            onResume()
        }


        loadprev.setOnClickListener {
            listOfPostdata.clear()
            Log.d("TAGG", "PST NO" + prev_page_no_posts)
            //loadGroupPosts(prev_page_no_posts, "2")
        }

        loadnext.setOnClickListener {
            listOfPostdata.clear()
            loadGroupPosts(Constants.MAXIMUM_PAGINATION_COUNT)
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
                        Log.d("TAGG", "END" + next_page_no_posts)
                        loadGroupPosts(Constants.MAXIMUM_PAGINATION_COUNT)
                        loading = false
                    }
                }
            })


    }

    var position: Int = 0
    fun scroll(size: Int) {
        //var current = mRecyclerViewAds.getCurrentItem()
        if (position == size) {
            position = 0;

        } else {
            position++
        }
        // mRecyclerViewAds.getLayoutManager().scrollToPosition(position).
        mRecyclerViewAds!!.smoothScrollToPosition(position)
    }


    fun sharegroupdetails() {

        intent = Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(Intent.EXTRA_SUBJECT, "| JobsForHer");
        intent.putExtra(
            Intent.EXTRA_TEXT, "Click on the link \n https://www.jobsforher.com" + "\n\n" +
                    "Application Link : https://play.google.com/store/apps/details?id=com.jobsforher"
        );
        startActivity(Intent.createChooser(intent, "Share link!"));
    }





    /*public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 1) {

            if (resultCode == 1) {
                listOfPostdata.clear()
                loadGroupPosts("2")
                mRecyclerViewPosts!!.smoothScrollToPosition(listOfPostdata.size)

            }
            if (resultCode == 0) {
            }
        }
    }*/


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
                    if (jsonObject1.has("body") && !jsonObject1.isNull("body")) {
                        var jsonarray: JSONObject = jsonObject1.optJSONObject("body")
                        if (response.isSuccessful) {

                            if (jsonarray.has("new_notification")) {
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

    /*private fun addPost(post_type: String) {

        val params = HashMap<String, String>()
        var msg: String = ""
        val description = edittext_createpost.text.toString()
        val pinned_post = "false"
        val post_type = post_type


        params["description"] = description
        params["post_type"] = post_type
        if (post_type.equals("text")) {
            params["image_filename"] = ""
            params["image_file"] = ""
            params["url"] = ""
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

                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject =
                    JSONObject(
                        str_response.substring(
                            str_response.indexOf("{"),
                            str_response.lastIndexOf("}") + 1
                        )
                    )
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.optJSONObject("body")
                    val responseCode: Int = jsonObject1.optInt("response_code")
                    val message: String = jsonObject1.optString("message")
                    if (jsonObject1.has("body") && !jsonObject1.isNull("body")) {
                        var jsonaobj: JSONObject = jsonObject1.optJSONObject("body")
                        if (response.isSuccessful) {
                            if (responseCode == 10200) {
                                //mDataList[position].comment_list!! =
                                Log.d("TAGG", "DATA" + jsonaobj.optString("id"))
                                Toast.makeText(
                                    applicationContext,
                                    "Post Created!!",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                edittext_createpost.text.clear()
                                //finish()
                            } else {

                            }
                            //finish()
                        } else {

                        }
                    }
                }
            }

            override fun onFailure(call: Call<CreatePostResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })
    }*/

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


                if (response.isSuccessful && response.body()!!.responseCode == 10104) {
                    if (type.equals("private")) {
                        btnJoinGroup.text = "Requested"
                    } else {
//                        btnJoinGroup.visibility =View.GONE
//                        btnJoined.visibility = View.VISIBLE
                        finish()
                        val intent = Intent(applicationContext, ZActivityGroupsDetails::class.java)
                        intent.putExtra("isLoggedIn", true)
                        intent.putExtra("group_id", id)
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


    private fun registerFCMToken(device_id: String, fcm_id: String) {

        val params = HashMap<String, String>()

        val installationId = device_id

        params["device_id"] = installationId
        params["fcm_id"] = fcm_id


        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.registerFcmId(
            "application/json", EndPoints.CLIENT_ID, "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<RegisterIdResponse> {

            override fun onResponse(
                call: Call<RegisterIdResponse>,
                response: Response<RegisterIdResponse>
            ) {
                if (response.isSuccessful) {

                    Logger.d("URLDeviceId", "" + response.body()!!.body!!.device_id.toString())
                } else {
                    ToastHelper.makeToast(applicationContext, "Response Failed")
                }

            }

            override fun onFailure(call: Call<RegisterIdResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })
    }

    fun loadGroupPosts(pageSize: String) {


        listOfPostdataDump.clear()
        val params = HashMap<String, String>()
        params["page_no"] = next_page_no_posts.toString()
        params["page_size"] = pageSize
        var model = GroupsPostModel()
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getNewsDetailsData(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<NewsDetails> {
            override fun onResponse(call: Call<NewsDetails>, response: Response<NewsDetails>) {

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
                Log.d("TAGG", "BODY")
                val responseCode: Int = jsonObject1.optInt("response_code")
                val message: String = jsonObject1.optString("message")
                var jsonarray: JSONArray = jsonObject1.optJSONArray("body")
                Log.d("TAGG", "BODY 1")

                var jsonarray_pagination: JSONObject = jsonObject1.optJSONObject("pagination")

                val has_next: String = jsonarray_pagination.getBoolean("has_next").toString()

                val pagenoo: String = jsonarray_pagination.optString("page_no")
                val prev_page: Int
                if (jsonarray_pagination.has("has_next") && jsonarray_pagination.getBoolean("has_next") == true) {
                    if (jsonarray_pagination.optInt("next_page") > 1) {
                        prev_page =
                            Integer.parseInt((jsonarray_pagination.optString("next_page"))) - 2
                    } else
                        prev_page = 0
                } else
                    prev_page = 0

//                Log.d("TAGG","Mins value"+(Integer.parseInt(jsonarray_pagination.optString("next_page"))))
                if (response.isSuccessful) {
                    for (i in 0 until response.body()!!.body!!.size) {

                        var json_objectdetail: JSONObject = jsonarray.optJSONObject(i)
                        model = GroupsPostModel();
                        model.id = json_objectdetail.optInt("id")
                        model.description = json_objectdetail.optString("description")
                        model.group_id = json_objectdetail.optInt("group_id").toString()
                        model.created_by = json_objectdetail.optInt("created_by").toString()
                        model.pinned_post = json_objectdetail.getBoolean("pinned_post").toString()
                        model.post_type = json_objectdetail.optString("post_type")
                        model.upvote_count = json_objectdetail.optInt("upvote_count").toString()
                        model.downvote_count = json_objectdetail.optInt("downvote_count").toString()
                        model.url = json_objectdetail.optString("url")
                        model.hash_tags = json_objectdetail.optString("hash_tags")
                        model.status = json_objectdetail.optString("status")
                        model.edited = json_objectdetail.getBoolean("edited").toString()
                        model.created_on = json_objectdetail.optString("created_on")
                        model.modified_on = json_objectdetail.optString("modified_on")
                        model.aggregate_count =
                            json_objectdetail.optInt("aggregate_count").toString()
                        model.comments_count =
                            json_objectdetail.optString("comments_count").toString()
                        model.created_on_str = ""//json_objectdetail.optString("created_on_str")
                        model.username = json_objectdetail.optString("username")
                        model.email = ""//json_objectdetail.optString("email")
                        model.profile_icon = json_objectdetail.optString("profile_icon")
                        model.user_role = ""//json_objectdetail.optString("user_role")
                        model.is_owner = ""// json_objectdetail.optString("is_owner")
                        model.comment_list = GroupsCommentModel()


                        //"time_ago": "2 hours ago",


                        listOfPostdataDump.add(
                            GroupsPostModel(
                                model.id,
                                model.description!!,
                                model.group_id!!,
                                model.created_by!!,
                                model.pinned_post!!,
                                model.post_type!!,
                                model.upvote_count!!,
                                model.downvote_count!!,
                                model.url!!,
                                model.hash_tags!!,
                                model.status!!,
                                model.edited!!,
                                model.created_on!!,
                                model.modified_on!!,
                                model.aggregate_count!!,
                                model.comments_count!!,
                                model.created_on_str!!,
                                model.username!!,
                                model.email!!,
                                model.profile_icon!!,
                                model.user_role!!,
                                model.is_owner!!,
                                model.comment_list!!
                            )
                        )
                    }

                    //listOfPostdata.add(listOfPostdataDump[x])

                    mRecyclerViewPosts = findViewById(R.id.posts_recycler_view)
                    mRecyclerViewPosts1 = findViewById(R.id.posts_recycler_view1)

                } else {
//                    ToastHelper.makeToast(applicationContext, "message")
                }

                if (has_next.equals("true")) {
                    val next_page: Int = jsonarray_pagination.optInt("next_page")
                    loadnext.visibility = View.VISIBLE
                    next_page_no_posts = next_page
                    loading = true
                } else {
                    loadnext.visibility = View.GONE
                    loading = false
                }


                if (pagenoo == "1")
                    loadprev.visibility = View.GONE
                else {
                    //loadprev.visibility = View.VISIBLE
                    prev_page_no_posts = prev_page.toString()
                }


//                Sneha comment

                var isComment: Boolean = false
                for (y in 0 until listOfPostdataDump.size) {
                    if (listOfPostdataDump[y].comments_count.equals("0")) {
                        isComment = false
                    } else {
                        isComment = true
                        break
                    }
                }
                Log.d("TAGG1", "Comment value is" + isComment.toString())

                for (x in 0 until listOfPostdataDump.size) {

                    if (!isComment) {
                        mRecyclerViewPosts = findViewById(R.id.posts_recycler_view)
                        mRecyclerViewPosts1 = findViewById(R.id.posts_recycler_view1)
                        listOfPostdata.add(listOfPostdataDump[x])

                        var posts1: ArrayList<GroupsPostModel> = ArrayList()
                        var posts2: ArrayList<GroupsPostModel> = ArrayList()
                        for (i in 0 until listOfPostdata.size) {
                            if (i < 2)
                                posts1.add(listOfPostdata[i])
                            else
                                posts2.add(listOfPostdata[i])
                        }
                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                        mRecyclerViewPosts!!.layoutManager =
                            mgroupsLayoutManager as RecyclerView.LayoutManager?
                        mAdapterPosts = PostsNewsAdapter(posts1, isLoggedIn)
                        mRecyclerViewPosts!!.adapter = mAdapterPosts

                        var mgroupsLayoutManager1 = GridLayoutManager(applicationContext, 1)
                        mRecyclerViewPosts1!!.layoutManager =
                            mgroupsLayoutManager1 as RecyclerView.LayoutManager?
                        mAdapterPosts1 = PostsNewsAdapter(posts2, isLoggedIn)
                        mRecyclerViewPosts1!!.adapter = mAdapterPosts1

                    } else {
                        retrofitInterface1 =
                            RetrofitClient.client!!.create(RetrofitInterface::class.java)


                        val call = retrofitInterface1!!.getPostComments(
                            listOfPostdataDump[x].id,
                            EndPoints.CLIENT_ID,
                            "Bearer " + EndPoints.ACCESS_TOKEN
                        )
                        call.enqueue(object : Callback<GroupCommentsNew> {
                            override fun onResponse(
                                call: Call<GroupCommentsNew>,
                                response1: Response<GroupCommentsNew>
                            ) {
                                val gson = GsonBuilder().serializeNulls().create()
                                var str_responses = gson.toJson(response1)
                                val jsonObj: JSONObject = JSONObject(
                                    str_responses.substring(
                                        str_responses.indexOf("{"),
                                        str_responses.lastIndexOf("}") + 1
                                    )
                                )
                                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                                val jsonObj1: JSONObject = jsonObj.optJSONObject("body")
                                //Log.d("TAGG", "Response "+jsonObj1.toString())
                                val response_code: Int = jsonObj1.optInt("response_code")

//                                if (response_code == 11301) {
//                                    listOfPostdata[x].comment_list = GroupsCommentModel()
//                                } else {
                                val messages: String = jsonObj1.optString("message")
                                Log.d("TAGG1", "Response " + messages)
                                var jsonArray: JSONArray = jsonObj1.optJSONArray("body")
                                if (response1.isSuccessful && jsonArray.length()>0) {
                                    // for (j in 0 until response1.body()!!.body!!.size) {
                                    var json_objectdetails: JSONObject = jsonArray.optJSONObject(0)
                                    Log.d(
                                        "TAGG1",
                                        "Response " + jsonArray.optJSONObject(0).optInt("parent_id")
                                    )
                                    val l: Int = jsonArray.length() - 1
                                    var model1: GroupsCommentModel = GroupsCommentModel();
                                    if (jsonArray.length() > 0) {
                                        model1 = GroupsCommentModel(
                                            json_objectdetails.optInt("id"),
                                            jsonArray.optJSONObject(l).optInt("parent_id")
                                                .toString()!!,
                                            jsonArray.optJSONObject(l).optString("entity_type")!!,
                                            jsonArray.optJSONObject(l).optString("entity_value")!!,
                                            jsonArray.optJSONObject(l).optString("group_id")!!,
                                            jsonArray.optJSONObject(l).optString("post_id")!!,
                                            jsonArray.optJSONObject(l).optString("created_by")!!,
                                            jsonArray.optJSONObject(l).optString("upvote_count")!!,
                                            jsonArray.optJSONObject(l)
                                                .optString("downvote_count")!!,
                                            jsonArray.optJSONObject(l).optString("url")!!,
                                            jsonArray.optJSONObject(l).optString("hash_tags")!!,
                                            jsonArray.optJSONObject(l).optString("status")!!,
                                            jsonArray.optJSONObject(l).optString("edited")!!,
                                            jsonArray.optJSONObject(l).optString("created_on")!!,
                                            jsonArray.optJSONObject(l).optString("modified_on")!!,
                                            jsonArray.optJSONObject(l)
                                                .optString("created_on_str")!!,
                                            jsonArray.optJSONObject(l).optString("comment_count")!!,
                                            jsonArray.optJSONObject(l).optString("replies_count")!!,
                                            jsonArray.optJSONObject(l)
                                                .optString("aggregate_count")!!,
                                            jsonArray.optJSONObject(l).optString("username")!!,
                                            jsonArray.optJSONObject(l).optString("email")!!,
                                            jsonArray.optJSONObject(l).optString("profile_icon")!!,
                                            jsonArray.optJSONObject(l).optString("is_owner")!!,
                                            GroupsReplyModel()
                                        )

                                        // }
                                        try {
                                            listOfPostdataDump[x].comment_list = model1
                                        } catch (e: IndexOutOfBoundsException) {
                                            e.printStackTrace()
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    }

                                } else {
//                                        ToastHelper.makeToast(applicationContext, "message")
                                }

                                mRecyclerViewPosts = findViewById(R.id.posts_recycler_view)
                                try {
                                    listOfPostdata.add(listOfPostdataDump[x])
                                } catch (e: java.lang.IndexOutOfBoundsException) {
                                    e.printStackTrace()
                                } catch (e: java.lang.Exception) {
                                    e.printStackTrace()
                                }

                                var posts1: ArrayList<GroupsPostModel> = ArrayList()
                                var posts2: ArrayList<GroupsPostModel> = ArrayList()
                                for (i in 0 until listOfPostdata.size) {
                                    if (i < 2)
                                        posts1.add(listOfPostdata[i])
                                    else
                                        posts2.add(listOfPostdata[i])
                                }
                                var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                                mRecyclerViewPosts!!.layoutManager =
                                    mgroupsLayoutManager as RecyclerView.LayoutManager?
                                mAdapterPosts = PostsNewsAdapter(posts1, isLoggedIn)
                                mRecyclerViewPosts!!.adapter = mAdapterPosts

                                var mgroupsLayoutManager1 = GridLayoutManager(applicationContext, 1)
                                mRecyclerViewPosts1!!.layoutManager =
                                    mgroupsLayoutManager1 as RecyclerView.LayoutManager?
                                mAdapterPosts1 = PostsNewsAdapter(posts2, isLoggedIn)
                                mRecyclerViewPosts1!!.adapter = mAdapterPosts1


                            }
                        }

                            override fun onFailure(call: Call<GroupCommentsNew>, t: Throwable) {
                                Logger.d("TAGG", "FAILED : $t")
                            }
                        })
                    }
                }
            }
        }

            override fun onFailure(call: Call<NewsDetails>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
               // mainlayout.visibility = View.GONE
                posts_layout3.visibility = View.GONE
                Toast.makeText(applicationContext, "No Posts to load!!", Toast.LENGTH_LONG).show()
                loadprev.visibility = View.GONE
                loadnext.visibility = View.GONE
                loading = false
            }

        })



        if (my_swipeRefresh_Layout.isRefreshing) {
            my_swipeRefresh_Layout.isRefreshing = false
        }

    }


    fun loadGroupPinnedPosts(pageno: String) {
        val params = HashMap<String, String>()
        //loadprev.visibility = View.VISIBLE
        loadnext.visibility = View.VISIBLE
        params["page_no"] = pageno.toString()
        params["page_size"] = 2.toString()
        var model: GroupsPostModel = GroupsPostModel();
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getGroupPinnedPosts(
            groupId,
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<GroupPostsNew> {
            override fun onResponse(call: Call<GroupPostsNew>, response: Response<GroupPostsNew>) {
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

                    var jsonarray_pagination: JSONObject = jsonObject1.optJSONObject("pagination")

                    val has_next: String = jsonarray_pagination.getBoolean("has_next").toString()

                    val pagenoo: String = jsonarray_pagination.optString("page_no")
                    val prev_page: Int
                    if (jsonarray_pagination.has("has_next") && jsonarray_pagination.getBoolean("has_next") == true) {
                        if (jsonarray_pagination.optInt("next_page") > 1) {
                            prev_page =
                                Integer.parseInt((jsonarray_pagination.optString("next_page"))) - 2
                        } else
                            prev_page = 0
                    } else
                        prev_page = 0

//                Log.d("TAGG","Mins value"+(Integer.parseInt(jsonarray_pagination.optString("next_page"))))
                    if (response.isSuccessful && responseCode !== 11203) {
                        for (i in 0 until response.body()!!.body!!.size) {
                            var json_objectdetail: JSONObject = jsonarray.optJSONObject(i)
                            model = GroupsPostModel();
                            model.id = json_objectdetail.optInt("id")
                            model.description = json_objectdetail.optString("description")
                            model.group_id = json_objectdetail.optString("group_id")
                            model.created_by = json_objectdetail.optString("created_by")
                            model.pinned_post = json_objectdetail.optString("pinned_post")
                            model.post_type = json_objectdetail.optString("post_type")
                            model.upvote_count = json_objectdetail.optString("upvote_count")
                            model.downvote_count = json_objectdetail.optString("downvote_count")
                            model.url = json_objectdetail.optString("url")
                            model.hash_tags = json_objectdetail.optString("hash_tags")
                            model.status = json_objectdetail.optString("status")
                            model.edited = json_objectdetail.optString("edited")
                            model.created_on = json_objectdetail.optString("created_on")
                            model.modified_on = json_objectdetail.optString("modified_on")
                            model.aggregate_count = json_objectdetail.optString("aggregate_count")
                            model.comments_count = json_objectdetail.optString("comments_count")
                            model.created_on_str = json_objectdetail.optString("created_on_str")
                            model.username = json_objectdetail.optString("username")
                            model.email = json_objectdetail.optString("email")
                            model.profile_icon = json_objectdetail.optString("profile_icon")
                            model.user_role = json_objectdetail.optString("user_role")
                            model.is_owner = json_objectdetail.optString("is_owner")
                            model.comment_list = GroupsCommentModel()

                            listOfPostdata.add(
                                GroupsPostModel(
                                    model.id,
                                    model.description!!,
                                    model.group_id!!,
                                    model.created_by!!,
                                    model.pinned_post!!,
                                    model.post_type!!,
                                    model.upvote_count!!,
                                    model.downvote_count!!,
                                    model.url!!,
                                    model.hash_tags!!,
                                    model.status!!,
                                    model.edited!!,
                                    model.created_on!!,
                                    model.modified_on!!,
                                    model.aggregate_count!!,
                                    model.comments_count!!,
                                    model.created_on_str!!,
                                    model.username!!,
                                    model.email!!,
                                    model.profile_icon!!,
                                    model.user_role!!,
                                    model.is_owner!!,
                                    model.comment_list!!
                                )
                            )
                        }

//                    var mgroupsLayoutManager = GridLayoutManager(applicationContext,1)
//                    mRecyclerViewPosts!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
//                    mAdapterPosts = PostsNewsAdapter(listOfPostdata, isLoggedIn)
//                    mRecyclerViewPosts!!.adapter = mAdapterPosts
                    } else {
//                    ToastHelper.makeToast(applicationContext, "message")
                    }

                    if (has_next.equals("true")) {
                        val next_page: Int = jsonarray_pagination.optInt("next_page")
                        loadnext.visibility = View.VISIBLE
                        next_page_no_posts = next_page
                        loading = true
                    } else {
                        loadnext.visibility = View.GONE
                        loading = false
                    }


                    if (pagenoo == "1")
                        loadprev.visibility = View.GONE
                    else {
                        //loadprev.visibility = View.VISIBLE
                        prev_page_no_posts = prev_page.toString()
                    }

                    var isCommentp: Boolean = false
                    for (y in 0 until listOfPostdata.size) {
                        if (listOfPostdata[y].comments_count.equals("0")) {
                            isCommentp = false
                        } else {
                            isCommentp = true
                            break
                        }
                    }
                    Log.d("TAGG1", "Comment pinned value is" + isCommentp.toString())

                    for (x in 0 until listOfPostdata.size) {

                        if (!isCommentp) {
                        } else {
                            retrofitInterface1 =
                                RetrofitClient.client!!.create(RetrofitInterface::class.java)

                            val call = retrofitInterface1!!.getPostComments(
                                listOfPostdata[x].id,
                                EndPoints.CLIENT_ID,
                                "Bearer " + EndPoints.ACCESS_TOKEN
                            )
                            call.enqueue(object : Callback<GroupCommentsNew> {
                                override fun onResponse(
                                    call: Call<GroupCommentsNew>,
                                    response1: Response<GroupCommentsNew>
                                ) {
                                    val gson = GsonBuilder().serializeNulls().create()
                                    var str_responses = gson.toJson(response1)
                                    val jsonObj: JSONObject = JSONObject(
                                        str_responses.substring(
                                            str_responses.indexOf("{"),
                                            str_responses.lastIndexOf("}") + 1
                                        )
                                    )
                                    if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                                        val jsonObj1: JSONObject = jsonObj.optJSONObject("body")
                                        //Log.d("TAGG", "Response "+jsonObj1.toString())
                                        val response_code: Int = jsonObj1.optInt("response_code")

//                                if (response_code == 11301) {
//                                    listOfPostdata[x].comment_list = GroupsCommentModel()
//                                } else {
                                        val messages: String = jsonObj1.optString("message")
                                        Log.d("TAGG1", "Response " + messages)
                                        var jsonArray: JSONArray = jsonObj1.optJSONArray("body")
                                        if (response1.isSuccessful) {
                                            // for (j in 0 until response1.body()!!.body!!.size) {
                                            var json_objectdetails: JSONObject =
                                                jsonArray.optJSONObject(0)
                                            Log.d(
                                                "TAGG1",
                                                "Response " + jsonArray.optJSONObject(0)
                                                    .optInt("parent_id")
                                            )
                                            var model1: GroupsCommentModel = GroupsCommentModel();
                                            model1 = GroupsCommentModel(
                                                json_objectdetails.optInt("id"),
                                                jsonArray.optJSONObject(0).optInt("parent_id")
                                                    .toString()!!,
                                                jsonArray.optJSONObject(0)
                                                    .optString("entity_type")!!,
                                                jsonArray.optJSONObject(0)
                                                    .optString("entity_value")!!,
                                                jsonArray.optJSONObject(0).optString("group_id")!!,
                                                jsonArray.optJSONObject(0).optString("post_id")!!,
                                                jsonArray.optJSONObject(0)
                                                    .optString("created_by")!!,
                                                jsonArray.optJSONObject(0)
                                                    .optString("upvote_count")!!,
                                                jsonArray.optJSONObject(0)
                                                    .optString("downvote_count")!!,
                                                jsonArray.optJSONObject(0).optString("url")!!,
                                                jsonArray.optJSONObject(0).optString("hash_tags")!!,
                                                jsonArray.optJSONObject(0).optString("status")!!,
                                                jsonArray.optJSONObject(0).optString("edited")!!,
                                                jsonArray.optJSONObject(0)
                                                    .optString("created_on")!!,
                                                jsonArray.optJSONObject(0)
                                                    .optString("modified_on")!!,
                                                jsonArray.optJSONObject(0)
                                                    .optString("created_on_str")!!,
                                                jsonArray.optJSONObject(0)
                                                    .optString("comment_count")!!,
                                                jsonArray.optJSONObject(0)
                                                    .optString("replies_count")!!,
                                                jsonArray.optJSONObject(0)
                                                    .optString("aggregate_count")!!,
                                                jsonArray.optJSONObject(0).optString("username")!!,
                                                jsonArray.optJSONObject(0).optString("email")!!,
                                                jsonArray.optJSONObject(0)
                                                    .optString("profile_icon")!!,
                                                jsonArray.optJSONObject(0).optString("is_owner")!!,
                                                GroupsReplyModel()
                                            )

                                            // }
                                            listOfPostdata[x].comment_list = model1

                                        } else {
//                                    ToastHelper.makeToast(applicationContext, "message")
                                        }


                                    }
                                }

                                override fun onFailure(call: Call<GroupCommentsNew>, t: Throwable) {
                                    Logger.d("TAGG", "FAILED : $t")
                                }
                            })
                        }
                    }
                }
            }
            override fun onFailure(call: Call<GroupPostsNew>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")

//                Toast.makeText(applicationContext,"No Posts to load!!",Toast.LENGTH_LONG).show()
                loadprev.visibility = View.GONE
                loadnext.visibility = View.GONE
                loading = false
            }
        })

        Thread.sleep(1000)
    }


    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true

        ToastHelper.makeToast(applicationContext, "Please click BACK again to exit")

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
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
                HelperMethods.showAppShareOptions(this@NewsFeed)
                closeDrawer()
            }
        }


        return true
    }

    private fun closeDrawer() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
    }


    fun loadPhotosData(post_type: String) {

        listOfPhotos.clear()
        val params = HashMap<String, String>()

        params["post_type"] = post_type

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getImages(
            groupId,
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<ImageResponse> {

            override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {

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
                            )
                                .show()
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

    fun loadVideosData() {


        loadPhotosData("video")

//        mRecyclerViewVideos = findViewById(R.id.recycler_view_videos)
//        var mLayoutManager = GridLayoutManager(this, 2)
//        mRecyclerViewVideos!!.layoutManager = mLayoutManager
//        mAdapterVideos = ZGroupsVideosAdapter(listOfVideos)
//        mRecyclerViewVideos!!.adapter = mAdapterVideos
    }


    public fun openBottomSheet(
        edited: String?,
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

        val editpost = dialog.findViewById(R.id.edit_post) as LinearLayout

        editpost.setOnClickListener {
            if (isOwner.equals("true")) {
                dialog.cancel()
                intent = Intent(applicationContext, ZEditPostActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra("data", data)
                intent.putExtra("post_type", posttype)
                intent.putExtra("url", url)
                intent.putExtra("icon", icon)
                intent.putExtra("grpName", "")
                intent.putExtra("owner", isOwner)
                startActivityForResult(intent, 1);
            } else {
                Toast.makeText(applicationContext, "You cannot edit the post", Toast.LENGTH_LONG)
                    .show()
            }
        }

        val edit_post_history = dialog.findViewById(R.id.edit_post_history) as LinearLayout

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

        val reportpost = dialog.findViewById(R.id.edit_report) as LinearLayout
        val reporttext = dialog.findViewById(R.id.reportedtext) as TextView
        Log.d("REPORT", yesno)
        if (yesno.compareTo("yes") == 0) {
            reporttext.setText("Reported")
            reporttext.setTextColor(Color.RED)
            reportpost.isEnabled = false
        } else {
//            reporttext.setText("Report this post")
//            reporttext.setTextColor(Color.BLACK)
            reportpost.isEnabled = true
        }
        reportpost.setOnClickListener {
            openBottomSheetReports(id, "post")
        }

        if (isOwner.equals("true")) {
            editpost.visibility = View.VISIBLE
            if (edited.equals("true")) {
                edit_post_history.visibility = View.VISIBLE
            } else {
                edit_post_history.visibility = View.GONE
            }
        } else {
            editpost.visibility = View.GONE
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
        edited: String?,
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
            } else
                Toast.makeText(applicationContext, "You cannot edit the comment", Toast.LENGTH_LONG)
                    .show()
        }

        val edit_commenthistory = dialog.findViewById(R.id.edit_comment_history) as LinearLayout
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

        val cancel = dialog.findViewById(R.id.cancel) as LinearLayout
        cancel.setOnClickListener {
            dialog.cancel()
        }

        val reportcomment = dialog.findViewById(R.id.report_comment) as LinearLayout
        val reporttext = dialog.findViewById(R.id.reporttextcomment) as TextView
        Log.d("REPORT", yesno)
        if (yesno.compareTo("yes") == 0) {
            reporttext.setText("Reported")
            reporttext.setTextColor(Color.RED)
            reportcomment.isEnabled = false
        } else {
//            reporttext.setText("Report this comment")
//            reporttext.setTextColor(Color.BLACK)
            reportcomment.isEnabled = true
        }
        reportcomment.setOnClickListener {
            dialog.cancel()
            openBottomSheetReports(id, "comment")
        }

        if (isOwner.equals("true")) {
            editcomment.visibility = View.VISIBLE
            edit_commenthistory.visibility = View.VISIBLE
            if (edited.equals("true")) {
                edit_commenthistory.visibility = View.VISIBLE
            } else {
                edit_commenthistory.visibility = View.GONE
            }
        } else {
            editcomment.visibility = View.GONE
            edit_commenthistory.visibility = View.GONE
        }

        var window: Window = dialog.getWindow()!!;
        var wlp: WindowManager.LayoutParams = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog.show()
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

    fun loadRecommendedJobs(pageno: String) {

        listOfhotJobsdata.clear()
        //listOfhotJobsdataTrimmed.clear()
        //listOfCompareJoineddata.clear()
        val params = HashMap<String, String>()

        params["page_no"] = pageno.toString()
        params["page_size"] = 2.toString()
        retrofitInterface1 = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface1!!.getRecommendedJobs(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<HotJobs> {
            override fun onResponse(call: Call<HotJobs>, response: Response<HotJobs>) {


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
                    //hasnextRecommendedJobs = jsonarray_pagination.getBoolean("has_next")
                    val pagenoo: String = jsonarray_pagination.optString("next_page")
//                if (hasnextRecommendedJobs) {
//                    seemore_jobs.visibility = View.VISIBLE
//                    pagenoRecommendedJobs = pagenoo
//                }
//                else
//                    seemore_jobs.visibility = View.GONE

                    Log.d("TAGG", "Applied " + jsonarray_info.toString())

                    if (response.isSuccessful) {

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
                            mRecyclerViewJobs!!.visibility = View.VISIBLE
                            //default_company_jobs.visibility = View.GONE

                            var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                            mRecyclerViewJobs!!.layoutManager =
                                mgroupsLayoutManager as RecyclerView.LayoutManager?
                            mAdapterJobs =
                                JobsAdapter(
                                    listOfhotJobsdata,
                                    isLoggedIn,
                                    2,
                                    0,
                                    listOfCompareJoineddata,
                                    1,
                                    "NewsFeed"
                                )
                            mRecyclerViewJobs!!.adapter = mAdapterJobs
                        } else {
                            mRecyclerViewJobs!!.visibility = View.GONE
                            //default_company_jobs.visibility = View.VISIBLE
                        }
//                    }

                    } else {
                        ToastHelper.makeToast(applicationContext, message)
                    }
                }
            }

            override fun onFailure(call: Call<HotJobs>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                mRecyclerViewJobs!!.visibility = View.GONE
//                default_company_jobs.visibility = View.VISIBLE
//                seemore_jobs.visibility = View.GONE
            }
        })
    }

    fun loadRecommendedCompanies(pageno: String) {

        listOfCompareJoineddata.clear()
        listOfCompaniesdata.clear()
        //listOfCompaniesdataTrimmed.clear()
        val params = HashMap<String, String>()

        params["page_no"] = pageno.toString()
        params["page_size"] = 2.toString()

        retrofitInterface1 = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface1!!.getRecommendedCompanies(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<Company> {
            override fun onResponse(call: Call<Company>, response: Response<Company>) {


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

                    //hasnextRecommendedCompanies = jsonarray_pagination.getBoolean("has_next")

                    val pagenoo: String = jsonarray_pagination.optString("next_page")
                    //mark6.setText(jsonarray_pagination.optString("total_items"))

//                if (hasnextRecommendedCompanies) {
//                    seemore_companies.visibility = View.VISIBLE
//                    pagenoRecommendedCompanies = pagenoo
//                }
//                else
//                    seemore_companies.visibility = View.GONE

                    if (response.isSuccessful) {

                        for (i in 0 until response.body()!!.body!!.size) {
                            Log.d("TAGG", "Inside response body")
                            // var json_objectdetail: JSONObject = jsonarray_info.optJSONObject(i)

                            val model: CompaniesView = CompaniesView()

                            model.id = jsonarray_info.optJSONObject(i).optInt("id")
                            model.company_type =
                                jsonarray_info.optJSONObject(i).optString("company_type")
                            model.featured = jsonarray_info.optJSONObject(i).getBoolean("featured")
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
                        //default_company_companies.visibility = View.GONE

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
                            "NewsFeed"
                        )
                        mRecyclerViewCompanies!!.adapter = mAdapterCompanies

                    } else {
                        ToastHelper.makeToast(applicationContext, message)
                        companies_layout.visibility = View.GONE
                    }
                }
            }

            override fun onFailure(call: Call<Company>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                mRecyclerViewCompanies!!.visibility = View.GONE
                companies_layout.visibility = View.GONE
                //default_company_companies.visibility = View.VISIBLE
                //default_company_companies.setText("There are no recommended companies for you at the moment. Follow your favoured companies on jobsforher.com/companies to stay up-to-date with their job openings and policies offered.")
                //seemore_companies.visibility = View.GONE
            }
        })
    }

    fun loadRecommendedMyGroupData(pageno: String) {


        listOfGroupdata.clear()
//        listOfGroupdataTrimmed.clear()
        listOfCompareGroupdata.clear()

        val params = java.util.HashMap<String, String>()

        params["page_no"] = pageno.toString()
        params["page_size"] = 2.toString()

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

                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)

                val jsonObject: JSONObject = JSONObject(
                    str_response.substring(
                        str_response.indexOf("{"),
                        str_response.lastIndexOf("}") + 1
                    )
                )
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {

                    if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                        val jsonObject1: JSONObject = jsonObject.optJSONObject("body")

                        var jsonarray_info: JSONArray = jsonObject1.optJSONArray("body")

                        var jsonarray_pagination: JSONObject =
                            jsonObject1.optJSONObject("pagination")

                        //hasnextRecommendedGroups = jsonarray_pagination.getBoolean("has_next")

                        val pagenoo: String = jsonarray_pagination.optString("next_page")

//                    if (hasnextRecommendedGroups) {
//                        seemore_groups.visibility = View.VISIBLE
//                        pagenoRecommendedGroups = pagenoo
//                    }
//                    else
//                        seemore_groups.visibility = View.GONE

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
                                model.featured = json_objectdetail.getBoolean("featured")
                                model.status = json_objectdetail.optString("status")
                                model.is_member = false

                                var citiesArray: JSONArray =
                                    json_objectdetail.optJSONArray("cities")
                                val listOfCity: ArrayList<Int> = ArrayList()
//                        for (j in 0 until citiesArray.length()) {
//                            var citiesObject:JSONObject=citiesArray.optJSONObject(j).
//                            listOfCity.add(citiesObject)
//                        }
                                val categoriesArray: JSONArray =
                                    json_objectdetail.optJSONArray("categories")
                                val listOfCategories: ArrayList<Categories> = ArrayList()
                                for (k in 0 until categoriesArray.length()) {
                                    var categoriesIdObj: JSONObject =
                                        categoriesArray.optJSONObject(k)
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
//                        default_company_groups.visibility = View.GONE
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
                                "NewsFeed"
                            )
                            mRecyclerViewGroups!!.adapter = mAdapterGroups

                        } else {
                            ToastHelper.makeToast(applicationContext, "Invalid Request")
                        }
                    } else
                        Toast.makeText(applicationContext, "No data exists", Toast.LENGTH_LONG)
                            .show()

                }
            }

            override fun onFailure(call: Call<Featured_Group>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                //Toast.makeText(applicationContext, "No Groups Exixts, Join a group today!", Toast.LENGTH_LONG).show()
                mRecyclerViewGroups!!.visibility = View.GONE
//                default_company_groups.visibility = View.VISIBLE
//                default_company_groups.setText("There are no recommended groups for you at the moment. Meet peers, interact with experts and stay up-to-date with industry trends by joining the many JobsForHer Groups here: jobsforher.com/groups")
//                seemore_groups.visibility = View.GONE
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadNotificationbubble()
    }
}

