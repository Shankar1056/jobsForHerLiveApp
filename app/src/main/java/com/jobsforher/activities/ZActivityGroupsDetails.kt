package com.jobsforher.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jobsforher.R
import com.jobsforher.adapters.PostsAdapter
import com.jobsforher.adapters.ZGroupsPhotosAdapter
import com.jobsforher.adapters.ZGroupsVideosAdapter
import com.jobsforher.helpers.Constants
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.models.*
import com.jobsforher.network.responsemodels.*
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_jfh_banner.*
import kotlinx.android.synthetic.main.toolbar_groups.*
import kotlinx.android.synthetic.main.zactivity_groups_details.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ZActivityGroupsDetails : Footer(), NavigationView.OnNavigationItemSelectedListener {

    var listOfPhotos: ArrayList<String> = ArrayList()
    var mRecyclerViewPhotos: RecyclerView? = null
    var mAdapterPhotos: RecyclerView.Adapter<*>? = null

    var listOfVideos: ArrayList<String> = ArrayList()
    var mRecyclerViewVideos: RecyclerView? = null
    var mAdapterVideos: RecyclerView.Adapter<*>? = null
    var KEY_RECYCLER_STATE: String = "recycler_state"
    var mBundleRecyclerViewState: Bundle? = null

    var listOfPostdata: ArrayList<GroupsPostModel> = ArrayList()
    var listOfPostdataDump: ArrayList<GroupsPostModel> = ArrayList()
    var mRecyclerViewPosts: RecyclerView? = null
    var mAdapterPosts: RecyclerView.Adapter<*>? = null

    var isLoggedIn = true
    var groupId = 0
    var isMyGroup = 0
    var isOwner = ""
    var groupType = ""
    var scroll = 0
    var page = ""

    private var retrofitInterface: RetrofitInterface? = null
    private var retrofitInterface1: RetrofitInterface? = null
    var next_page_no_posts: String = "1"
    var prev_page_no_posts: String = "1"
    private val PREF_ACCESSTOKEN = "accesstoken"
    private var retrofitInterface_post: RetrofitInterface? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zgroups_groudetails_toolbr)


        var bundle: Bundle? = intent.extras!!
        if (bundle != null) {
            //isLoggedIn=intent.getBooleanExtra("isLoggedIn",false)
            Log.d("TAGG", bundle!!.getString("group_id").toString())
            groupId = Integer.parseInt(bundle!!.getString("group_id").toString())
            groupType = bundle!!.getString("group_type").toString()
            if (intent.hasExtra("page")) {
                isMyGroup = bundle!!.getInt("isMygroup")
                page = bundle!!.getString("page").toString()
                Log.d("TAGG", "PAGE" + page)
            } else {
                page = "NewsFeed"
                isMyGroup = 1
            }

        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close
        )
        Log.d("TAGG", groupId.toString())

        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        mappingWidgets()
        handleBackgrounds(btnGroups)


        mRecyclerViewPosts = findViewById(R.id.posts_recycler_view)
        if (isLoggedIn && isMyGroup == 1) {

//            ToastHelper.makeToast(applicationContext, groupId.toString())
            val menu = navView.menu
            menu.findItem(R.id.action_employerzone).setVisible(false)
            menu.findItem(R.id.action_mentorzone).setVisible(false)
            menu.findItem(R.id.action_partnerzone).setVisible(false)
            menu.findItem(R.id.action_logout).setVisible(false)
            menu.findItem(R.id.action_settings).setVisible(false)
            menu.findItem(R.id.action_signup).setVisible(false)
            menu.findItem(R.id.action_login).setVisible(false)
            navView.setNavigationItemSelectedListener(this)
            // menu.findItem(R.id.navItem1).getIcon().setColorFilter(Color.RED,PorterDuff.Mode.SRC_IN);
            val hView = navView.getHeaderView(0)
            val loggedin_header = hView.findViewById(R.id.loggedin_layout) as RelativeLayout
            val nologgedin_layout = hView.findViewById(R.id.nologgedin_layout) as RelativeLayout
            loggedin_header.visibility = View.VISIBLE
            nologgedin_layout.visibility = View.GONE
            aboutus.setTextColor(resources.getColor(R.color.green))
            posts.setTextColor(resources.getColor(R.color.black))
            photos.setTextColor(resources.getColor(R.color.black))
            videos.setTextColor(resources.getColor(R.color.black))
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

            btnJoined.visibility = View.VISIBLE
            button_applied.visibility = View.VISIBLE
            join_group.visibility = View.GONE
            button_apply.visibility = View.GONE

            default_layout.visibility = View.GONE
            aboutus_layout.visibility = View.VISIBLE
            posts_layout.visibility = View.GONE
            photos_layout.visibility = View.GONE
            videos_layout.visibility = View.GONE
            loggedin_header.setOnClickListener {
                intent = Intent(applicationContext, ProfileView::class.java)
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
            menu.findItem(R.id.action_logout).setVisible(false)
            menu.findItem(R.id.action_settings).setVisible(true)
            menu.findItem(R.id.action_signup).setVisible(false)
            menu.findItem(R.id.action_login).setVisible(false)
            navView.setNavigationItemSelectedListener(this)
            val hView = navView.getHeaderView(0)
            val loggedin_header = hView.findViewById(R.id.loggedin_layout) as RelativeLayout
            val nologgedin_layout = hView.findViewById(R.id.nologgedin_layout) as RelativeLayout
            loggedin_header.visibility = View.VISIBLE
            nologgedin_layout.visibility = View.GONE
            aboutus.setTextColor(resources.getColor(R.color.green))
            posts.setTextColor(resources.getColor(R.color.black))
            photos.setTextColor(resources.getColor(R.color.black))
            videos.setTextColor(resources.getColor(R.color.black))
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

            if (isMyGroup == -1) {
                btnJoined.setText("Pending")
                button_applied.setText("Pending")
                btnJoined.isEnabled = false

                btnJoined.visibility = View.VISIBLE
                button_applied.visibility = View.VISIBLE
                join_group.visibility = View.GONE
                button_apply.visibility = View.GONE

            } else {


                btnJoined.visibility = View.GONE
                join_group.visibility = View.VISIBLE
                button_apply.visibility = View.VISIBLE
                button_applied.visibility = View.GONE
            }
            default_layout.visibility = View.VISIBLE
            aboutus_layout.visibility = View.VISIBLE
            posts_layout.visibility = View.GONE
            photos_layout.visibility = View.GONE
            videos_layout.visibility = View.GONE
            loggedin_header.setOnClickListener {
                intent = Intent(applicationContext, ProfileView::class.java)
                startActivity(intent)
            }
            img_profile_toolbar.visibility = View.VISIBLE
//            notificaton.visibility = View.VISIBLE
            sign_in.visibility = View.GONE
        }
        var mgroupsLayoutManager = LinearLayoutManager(this)
        mRecyclerViewPosts!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
        mAdapterPosts = PostsAdapter(listOfPostdata, isLoggedIn)
        mRecyclerViewPosts!!.adapter = mAdapterPosts


        img_profile_toolbar.setOnClickListener {

            intent = Intent(applicationContext, ZActivityDashboard::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("isLoggedIn", true)
            intent.putExtra("pagetype", 0)
            startActivity(intent)
        }


        button_applied.setOnClickListener {
            btnJoined.callOnClick()
        }

        btnJoined.setOnClickListener {

            retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

            val call = retrofitInterface!!.ReportCheck(
                groupId,
                EndPoints.CLIENT_ID,
                "Bearer " + EndPoints.ACCESS_TOKEN
            )

            call.enqueue(object : Callback<DeletePostResponse> {
                @SuppressLint("RestrictedApi")
                override fun onResponse(
                    call: Call<DeletePostResponse>,
                    response: Response<DeletePostResponse>
                ) {

                    Logger.d("CODE", response.code().toString() + "")
                    Logger.d("MESSAGE", response.message() + "")
                    Logger.d("URL", "" + response)
                    Logger.d("TAGG", "CODE IS " + response.body()!!.responseCode.toString())
                    Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                    if (response.isSuccessful && response.body()!!.responseCode.toString()
                            .equals("10802")
                    ) {      //11804
//                        Toast.makeText(applicationContext,"Hello",Toast.LENGTH_LONG).show()

                        val popupMenu: PopupMenu = PopupMenu(applicationContext, btnJoined)
                        popupMenu.menuInflater.inflate(R.menu.group_popup_menu1, popupMenu.menu)
                        val positionOfMenuItem = 1 // or whatever...
                        val item = popupMenu.menu.getItem(positionOfMenuItem)
                        val s = SpannableString("Reported")
                        s.setSpan(ForegroundColorSpan(Color.RED), 0, s.length, 0)
                        item.setTitle(s)

                        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.action_leave ->
                                    leaveGroup(groupId, btnJoined)
                                R.id.action_report ->
                                    openBottomSheetReports(groupId, "group")
                                R.id.action_reported ->
                                    Toast.makeText(
                                        applicationContext,
                                        "Already Reported",
                                        Toast.LENGTH_LONG
                                    ).show()
                            }
                            true
                        })
                        popupMenu.show()

                    } else {
//                        Toast.makeText(applicationContext,"HI",Toast.LENGTH_LONG).show()
                        val popupMenu: PopupMenu = PopupMenu(applicationContext, btnJoined)
                        popupMenu.menuInflater.inflate(R.menu.group_popup_menu, popupMenu.menu)
                        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.action_leave ->
                                    leaveGroup(groupId, btnJoined)
                                R.id.action_report ->
                                    openBottomSheetReports(groupId, "group")
                                R.id.action_reported ->
                                    Toast.makeText(
                                        applicationContext,
                                        "Already Reported",
                                        Toast.LENGTH_LONG
                                    ).show()
                            }
                            true
                        })
                        popupMenu.show()
                    }
                }

                override fun onFailure(call: Call<DeletePostResponse>, t: Throwable) {
                    Logger.d("TAGG", "FAILED : $t")
                }
            })
        }

        button_apply.setOnClickListener {
            join_group.callOnClick()
        }
        join_group.setOnClickListener {

            //            Log.d("CODE", mDataList[position].status)
            joinGroup(groupId, join_group, groupType, btnJoined)
        }

        my_swipeRefresh_Layout.setOnRefreshListener {
            listOfPostdata.clear()
            loadGroupPosts("1")
        }

//        join_group.setOnClickListener{
//            joinGroup(groupId,join_group)
//        }

        invite_gpdetails.setOnClickListener {
            sharegroupdetails()
        }


        val sharedPref: SharedPreferences =
            getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        if (sharedPref.contains(PREF_ACCESSTOKEN)) {
            loadGroupDetailsData()
        } else {
            val intent = Intent(applicationContext, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        sign_in.setOnClickListener {
            intent = Intent(applicationContext, ZActivityGroupsDetails::class.java)
            intent.putExtra("isLoggedIn", true)
            startActivity(intent)
        }

        aboutus.setOnClickListener {
            aboutus.setTextColor(resources.getColor(R.color.green))
            posts.setTextColor(resources.getColor(R.color.black))
            photos.setTextColor(resources.getColor(R.color.black))
            videos.setTextColor(resources.getColor(R.color.black))
            if (isLoggedIn) {
                aboutus_layout.visibility = View.VISIBLE
                posts_layout.visibility = View.GONE
                photos_layout.visibility = View.GONE
                videos_layout.visibility = View.GONE
                default_layout.visibility = View.GONE
            } else {
                aboutus_layout.visibility = View.VISIBLE
                posts_layout.visibility = View.GONE
                photos_layout.visibility = View.GONE
                videos_layout.visibility = View.GONE
                default_layout.visibility = View.VISIBLE
            }
        }
        posts.setOnClickListener {
            aboutus.setTextColor(resources.getColor(R.color.black))
            posts.setTextColor(resources.getColor(R.color.green))
            photos.setTextColor(resources.getColor(R.color.black))
            videos.setTextColor(resources.getColor(R.color.black))
            if (EndPoints.PROFILE_ICON.length > 4) {
                Picasso.with(applicationContext)
                    .load(EndPoints.PROFILE_ICON)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(group_icon_posts)
            }

            if (isLoggedIn && isMyGroup == 1) {
                aboutus_layout.visibility = View.GONE
                posts_layout.visibility = View.VISIBLE
                photos_layout.visibility = View.GONE
                videos_layout.visibility = View.GONE
                default_layout.visibility = View.GONE
                listOfPostdata.clear()
                loadGroupPosts("1")
            } else {
                aboutus_layout.visibility = View.GONE
                posts_layout.visibility = View.GONE
                photos_layout.visibility = View.GONE
                videos_layout.visibility = View.GONE
                default_layout.visibility = View.VISIBLE
            }
        }
        photos.setOnClickListener {
            aboutus.setTextColor(resources.getColor(R.color.black))
            posts.setTextColor(resources.getColor(R.color.black))
            photos.setTextColor(resources.getColor(R.color.green))
            videos.setTextColor(resources.getColor(R.color.black))
            if (isLoggedIn && isMyGroup == 1) {
                aboutus_layout.visibility = View.GONE
                posts_layout.visibility = View.GONE
                photos_layout.visibility = View.VISIBLE
                videos_layout.visibility = View.GONE
                default_layout.visibility = View.GONE
                loadPhotosData("image")
            } else {
                aboutus_layout.visibility = View.GONE
                posts_layout.visibility = View.GONE
                photos_layout.visibility = View.GONE
                videos_layout.visibility = View.GONE
                default_layout.visibility = View.VISIBLE
            }

        }
        videos.setOnClickListener {
            aboutus.setTextColor(resources.getColor(R.color.black))
            posts.setTextColor(resources.getColor(R.color.black))
            photos.setTextColor(resources.getColor(R.color.black))
            videos.setTextColor(resources.getColor(R.color.green))
            if (isLoggedIn && isMyGroup == 1) {
                aboutus_layout.visibility = View.GONE
                posts_layout.visibility = View.GONE
                photos_layout.visibility = View.GONE
                videos_layout.visibility = View.VISIBLE
                default_layout.visibility = View.GONE
                loadPhotosData("video")
            } else {
                aboutus_layout.visibility = View.GONE
                posts_layout.visibility = View.GONE
                photos_layout.visibility = View.GONE
                videos_layout.visibility = View.GONE
                default_layout.visibility = View.VISIBLE
            }
        }

        edittext_createpost.setOnClickListener {
            intent = Intent(applicationContext, ZCreatePostActivity::class.java)
            intent.putExtra("groupID", groupId.toString())
            intent.putExtra("groupName", groupname.text.toString())
            intent.putExtra("onwner", isOwner)
            edittext_createpost.text.clear()
            startActivityForResult(intent, 1);
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

        opencreatepost.setOnClickListener {
            intent = Intent(applicationContext, ZCreatePostActivity::class.java)
            intent.putExtra("groupID", groupId.toString())
            intent.putExtra("groupName", groupname.text.toString())
            intent.putExtra("edittext_data", edittext_createpost.text.toString())
            intent.putExtra("onwner", isOwner)
            edittext_createpost.text.clear()
            startActivity(intent)
        }

        loadprev.setOnClickListener {
            listOfPostdata.clear()
            Log.d("TAGG", "PST NO" + prev_page_no_posts)
            loadGroupPosts(prev_page_no_posts)
        }

        loadnext.setOnClickListener {
            listOfPostdata.clear()
            loadGroupPosts(next_page_no_posts)
        }

        mainScroll_grpdetails.getViewTreeObserver()
            .addOnScrollChangedListener(object : ViewTreeObserver.OnScrollChangedListener {
                override fun onScrollChanged() {
                    Log.d("TAGG", "END EFORE")
                    val view: View =
                        mainScroll_grpdetails.getChildAt(mainScroll_grpdetails.getChildCount() - 1);

                    val diff: Int =
                        (view.getBottom() - (mainScroll_grpdetails.getHeight() + mainScroll_grpdetails.getScrollY()));

                    if (diff == 0 && scroll == 1) {
                        Log.d("TAGG", "END" + next_page_no_posts)
                        loadGroupPosts(next_page_no_posts)

                    }
                }
            });
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
    }

    fun sharegroupdetails() {


        val s: String = groupname.text.toString().replace(" ", "-")
        val s1: String = s.replace("_", "-")

        intent = Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(Intent.EXTRA_SUBJECT, groupname.text.toString() + "| JobsForHer");
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Click on the link \n http://www.jobsforher.com/groups/" + s1.toLowerCase() + "/" + groupId + "\n\n" +
                    "Application Link : https://play.google.com/store/apps/details?id=com.jobsforher"
        );
        startActivity(Intent.createChooser(intent, "Share Group link!"));
    }

    fun sharepostdetails(id: Int) {


        val s: String = groupname.text.toString().replace(" ", "-")
        val s1: String = s.replace("_", "-")

        intent = Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(Intent.EXTRA_SUBJECT, groupname.text.toString() + "| JobsForHer");
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Click on the link \n http://www.jobsforher.com/groups/" + s1.toLowerCase() + "/post/" + id + "\n\n" +
                    "Application Link : https://play.google.com/store/apps/details?id=com.jobsforher"
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

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 1) {

            if (resultCode == 1) {
                listOfPostdata.clear()
                loadGroupPosts("1")
                mRecyclerViewPosts!!.smoothScrollToPosition(listOfPostdata.size)
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

    private fun addPost(post_type: String) {

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


                //var str_response = Gson().toJson(response)
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
                    var jsonaobj: JSONObject = jsonObject1.optJSONObject("body")
                    if (response.isSuccessful) {
                        if (responseCode == 10200) {
                            //mDataList[position].comment_list!! =
                            Log.d("TAGG", "DATA" + jsonaobj.optString("id"))
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
                        button_apply.text = "Requested"
                    } else {
//                        btnJoinGroup.visibility =View.GONE
//                        btnJoined.visibility = View.VISIBLE
                        finish()
//                        val intent = Intent(applicationContext, ZActivityGroupsDetails::class.java)
//                        intent.putExtra("isLoggedIn", true)
//                        intent.putExtra("group_id", id.toString())
//                        intent.putExtra("group_type", type)
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                        intent.putExtra("page",page)
//                        intent.putExtra("isMygroup", 1)
//                        startActivity(intent)
                        val bundle = Bundle()
                        val intent = Intent(applicationContext, ZActivityGroupsDetails::class.java)
                        bundle.putBoolean("isLoggedIn", false)
                        bundle.putString("group_type", type)
                        bundle.putString("group_id", id.toString())
                        bundle.putInt("isMygroup", 1)
                        bundle.putString("page", page)
                        intent.putExtras(bundle)
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

    fun loadGroupDetailsData() {

        listOfPhotos.clear()
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getGroupDetailsData(
            groupId,
            "application/json",
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN
        )
        Logger.d("URL", "" + "HI")
        call.enqueue(object : Callback<GroupDetails> {
            override fun onResponse(call: Call<GroupDetails>, response: Response<GroupDetails>) {


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
                    val jsonarray_info: JSONObject = jsonObject1.optJSONObject("body")
                    Log.d("TAGG", jsonarray_info.toString())

                    if (response.isSuccessful && responseCode == 10100) {

                        var json_objectdetail: JSONObject = jsonarray_info
                        val model: GroupDetailsData = GroupDetailsData();
                        model.id = json_objectdetail.optInt("id")
                        model.name = json_objectdetail.optString("name")
                        model.icon_url = json_objectdetail.optString("icon_url")
                        model.banner_url = json_objectdetail.optString("banner_url")
                        model.excerpt = json_objectdetail.optString("excerpt")
                        model.description = json_objectdetail.optString("description")
                        model.groupType = json_objectdetail.optString("visiblity_type")
                        model.no_of_members = json_objectdetail.optString("no_of_members")
                        model.my_roles = json_objectdetail.optString("my_roles")
                        // model.is_member = json_objectdetail.optString("is_member")
                        model.is_owner = json_objectdetail.optString("is_owner")
                        isOwner = model.is_owner.toString()
//
                        val categoriesArray: JSONArray =
                            json_objectdetail.optJSONArray("categories")
                        val listOfCategories: ArrayList<Categories> = ArrayList()
                        var category: String = ""
                        for (k in 0 until categoriesArray.length()) {
                            var categoriesIdObj: JSONObject = categoriesArray.optJSONObject(k)
                            listOfCategories.add(
                                Categories(
                                    categoriesIdObj.optInt("category_id"),
                                    categoriesIdObj.optString("category")
                                )
                            )
                            category = category + categoriesIdObj.optString("category") + ", "
                        }
                        model.categories = listOfCategories

                        groupname.setText(model.name)
                        if (model.groupType.equals("public"))
                            grouptype.setText("Open Group")
                        else
                            grouptype.setText("Closed Group")
                        if (Integer.parseInt(model.no_of_members!!) > 10)
                            noOfmembers.setText(model.no_of_members + " Members")
                        else
                            noOfmembers.visibility = View.GONE
                        aboutusheader.setText("About Group")
                        if (category.length > 0) {
                            val msg: String = "  " + category.substring(0, category.length - 2)
                            val mImageSpan: ImageSpan =
                                ImageSpan(applicationContext, R.drawable.ic_category);
                            val text: SpannableString = SpannableString(msg);
                            text.setSpan(mImageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            aboutusdept.setText(text);
                        }

                        aboutusdescription.setText(Html.fromHtml(model.description))
                        if (model.icon_url!!.isNotEmpty()) {
                            Picasso.with(applicationContext)
                                .load(model.icon_url)
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .into(group_icon)
                        } else {
                            Picasso.with(applicationContext)
                                .load(R.drawable.ic_default_profile_icon)
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .into(group_icon)
                        }
                        if (model.banner_url!!.isNotEmpty()) {
                            Picasso.with(applicationContext)
                                .load(model.banner_url)
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .into(banner_image)
                        }

                    } else {
                        ToastHelper.makeToast(applicationContext, message)
                        finish()
                        overridePendingTransition(0, 0);
                    }
                }
            }

            override fun onFailure(call: Call<GroupDetails>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })
//        listOfMyGroupdata.add(
//            GroupsView(0,"Women Freelancers","Working Women, Women Professionals","","Open Group","9652",
//                "Wish to work as per your time of convenience? " +
//                        "Come aboard this group of freelancers to stay in the know of all things that matter in your world.")
//        )
    }

    fun loadGroupPosts(pageno: String) {
        if (listOfPostdata.size == 0)
            loadGroupPinnedPosts("1")


        listOfPostdataDump.clear()
        val params = HashMap<String, String>()
        //loadprev.visibility = View.VISIBLE
        loadnext.visibility = View.VISIBLE
        params["page_no"] = pageno.toString()
        params["page_size"] = Constants.MAXIMUM_PAGINATION_COUNT
        var model: GroupsPostModel = GroupsPostModel();
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getGroupPosts(
            groupId,
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        Logger.d("URL", "" + "HI")
        call.enqueue(object : Callback<GroupPostsNew> {
            override fun onResponse(call: Call<GroupPostsNew>, response: Response<GroupPostsNew>) {
                Logger.d("URL", "" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "POSTS" + Gson().toJson(response))
                //var str_response = Gson().toJson(response)
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
                var jsonarray: JSONArray? = null
                var has_next: String = ""
                var pagenoo: String = ""
                var prev_page: Int = 0
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    jsonObject1 = jsonObject.optJSONObject("body")
                }
                if (jsonObject1 != null) {
                    val responseCode: Int = jsonObject1.optInt("response_code")
                    val message: String = jsonObject1.optString("message")
                    jsonarray = jsonObject1.optJSONArray("body")

                    jsonarray_pagination = jsonObject1.optJSONObject("pagination")
                }

                if (jsonarray_pagination != null) {
                    has_next = jsonarray_pagination.getBoolean("has_next").toString()

                    pagenoo = jsonarray_pagination.optString("page_no")

                    if (jsonarray_pagination.has("has_next") && jsonarray_pagination.getBoolean("has_next") == true) {
                        if (jsonarray_pagination.optInt("next_page") > 1) {
                            prev_page =
                                Integer.parseInt((jsonarray_pagination.optString("next_page"))) - 2
                        } else
                            prev_page = 0
                    } else {
                        prev_page = 0
                    }
                }

                if (response.isSuccessful) {
                    for (i in 0 until response.body()!!.body!!.size) {
                        if (jsonarray == null) {
                            return
                        }
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

//                    var mgroupsLayoutManager = GridLayoutManager(applicationContext,1)
//                    mRecyclerViewPosts!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
//                    mAdapterPosts = PostsAdapter(listOfPostdata, isLoggedIn)
//                    mRecyclerViewPosts!!.adapter = mAdapterPosts
                } else {
//                    ToastHelper.makeToast(applicationContext, "message")
                }

                if (has_next.equals("true")) {
                    val next_page: String = jsonarray_pagination?.optInt("next_page").toString()
                    loadnext.visibility = View.VISIBLE
                    next_page_no_posts = next_page
                    scroll = 1
                } else {
                    loadnext.visibility = View.GONE
                    scroll = 0
                }


                if (pagenoo == "1")
                    loadprev.visibility = View.GONE
                else {
                    //loadprev.visibility = View.VISIBLE
                    prev_page_no_posts = prev_page.toString()
                }

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
                        listOfPostdata.add(listOfPostdataDump[x])
                        var mgroupsLayoutManager = LinearLayoutManager(applicationContext)
                        mRecyclerViewPosts!!.layoutManager =
                            mgroupsLayoutManager as RecyclerView.LayoutManager?
                        mAdapterPosts = PostsAdapter(listOfPostdata, isLoggedIn)
                        mRecyclerViewPosts!!.adapter = mAdapterPosts

//                        mAdapterPosts!!.notifyDataSetChanged()
//                        Log.d("TAGG1","Comment value is"+isComment.toString())
                    } else {
                        retrofitInterface1 =
                            RetrofitClient.client!!.create(RetrofitInterface::class.java)


                        val call = retrofitInterface1!!.getPostComments(
                            listOfPostdataDump[x].id,
                            EndPoints.CLIENT_ID,
                            "Bearer " + EndPoints.ACCESS_TOKEN
                        )
                        Logger.d("URL1", "" + "HI")
                        call.enqueue(object : Callback<GroupCommentsNew> {
                            override fun onResponse(
                                call: Call<GroupCommentsNew>,
                                response1: Response<GroupCommentsNew>
                            ) {
                                Logger.d("URL1", "" + response1 + EndPoints.CLIENT_ID)
                                Logger.d("CODE1", response1.code().toString() + "")
                                Logger.d("MESSAGE1", response1.message() + "")
                                Logger.d("RESPONSE1", "" + Gson().toJson(response1))
                                //var str_responses = Gson().toJson(response1)
                                val gson = GsonBuilder().serializeNulls().create()
                                var str_responses = gson.toJson(response1)
                                val jsonObj: JSONObject = JSONObject(
                                    str_responses.substring(
                                        str_responses.indexOf("{"),
                                        str_responses.lastIndexOf("}") + 1
                                    )
                                )

                                if (jsonObj.has("body") && !jsonObj.isNull("body")) {
                                    val jsonObj1: JSONObject = jsonObj.optJSONObject("body")
                                    val response_code: Int = jsonObj1.optInt("response_code")

//
                                    val messages: String = jsonObj1.optString("message")
                                    Log.d("TAGG1", "Response " + messages)
                                    var jsonArray: JSONArray = jsonObj1.optJSONArray("body")
                                    if (response1.isSuccessful && jsonArray.length()>0) {
                                        var json_objectdetails: JSONObject =
                                            jsonArray.optJSONObject(0)
                                        Log.d(
                                            "TAGG1",
                                            "Response " + jsonArray.optJSONObject(0)
                                                .optInt("parent_id")
                                        )
                                        val l: Int = jsonArray.length() - 1
                                        var model1: GroupsCommentModel = GroupsCommentModel();
                                        model1 = GroupsCommentModel(
                                            jsonArray.optJSONObject(l).optInt("id"),
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
                                        listOfPostdataDump[x].comment_list = model1

                                    } else {
//                                        ToastHelper.makeToast(applicationContext, "message")
                                    }
                                }

                                if (listOfPostdataDump.size>0) {
                                    listOfPostdata.add(listOfPostdataDump[x])
                                }
                                var mgroupsLayoutManager = LinearLayoutManager(applicationContext)
                                mRecyclerViewPosts!!.layoutManager =
                                    mgroupsLayoutManager as RecyclerView.LayoutManager?
                                mAdapterPosts = PostsAdapter(listOfPostdata, isLoggedIn)
                                mRecyclerViewPosts!!.adapter = mAdapterPosts

//                                    mAdapterPosts!!.notifyItemRangeChanged(0, mAdapterPosts!!.getItemCount());
//                                    mAdapterPosts!!.notifyDataSetChanged()
//                                }
                            }

                            override fun onFailure(call: Call<GroupCommentsNew>, t: Throwable) {
                                Logger.d("TAGG", "FAILED : $t")
                            }
                        })
                    }
                }
            }

            override fun onFailure(call: Call<GroupPostsNew>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")

                Toast.makeText(applicationContext, "No Posts to load!!", Toast.LENGTH_LONG).show()
                loadprev.visibility = View.GONE
                loadnext.visibility = View.GONE
                scroll = 0
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
        params["page_size"] = Constants.MAXIMUM_PAGINATION_COUNT
        var model: GroupsPostModel = GroupsPostModel();
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getGroupPinnedPosts(
            groupId,
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN,
            params
        )
        Logger.d("URL", "" + "HI")
        call.enqueue(object : Callback<GroupPostsNew> {
            override fun onResponse(call: Call<GroupPostsNew>, response: Response<GroupPostsNew>) {
                Logger.d("URL", "" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "POSTS" + Gson().toJson(response))
                //var str_response = Gson().toJson(response)
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
                var jsonarray: JSONArray? = null
                var has_next: String = ""
                var pagenoo: String = ""
                var prev_page: Int = 0
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    jsonObject1 = jsonObject.optJSONObject("body")
                }
                if (jsonObject1 != null) {
                    val responseCode: Int = jsonObject1.optInt("response_code")
                    val message: String = jsonObject1.optString("message")
                    jsonarray = jsonObject1.optJSONArray("body")

                    jsonarray_pagination = jsonObject1.optJSONObject("pagination")
                }

                if (jsonarray_pagination != null) {
                    has_next = jsonarray_pagination.getBoolean("has_next").toString()

                    pagenoo = jsonarray_pagination.optString("page_no")

                    if (jsonarray_pagination.has("has_next") && jsonarray_pagination.getBoolean("has_next") == true) {
                        if (jsonarray_pagination.optInt("next_page") > 1) {
                            prev_page =
                                Integer.parseInt((jsonarray_pagination.optString("next_page"))) - 2
                        } else
                            prev_page = 0
                    } else {
                        prev_page = 0
                    }
                }

//                Log.d("TAGG","Mins value"+(Integer.parseInt(jsonarray_pagination.optString("next_page"))))
                if (response.isSuccessful && jsonObject1?.optInt("response_code") !== 11203) {
                    for (i in 0 until response.body()!!.body!!.size) {
                        if (jsonarray == null) {
                            return
                        }
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
//                    mAdapterPosts = PostsAdapter(listOfPostdata, isLoggedIn)
//                    mRecyclerViewPosts!!.adapter = mAdapterPosts
                } else {
//                    ToastHelper.makeToast(applicationContext, "message")
                }

                if (has_next.equals("true")) {
                    val next_page: String = jsonarray_pagination?.optInt("next_page").toString()
                    loadnext.visibility = View.VISIBLE
                    next_page_no_posts = next_page
                    scroll = 1
                } else {
                    loadnext.visibility = View.GONE
                    scroll = 0
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
                        Logger.d("URL1", "" + "HI")
                        call.enqueue(object : Callback<GroupCommentsNew> {
                            override fun onResponse(
                                call: Call<GroupCommentsNew>,
                                response1: Response<GroupCommentsNew>
                            ) {
                                Logger.d("URL1", "" + response1 + EndPoints.CLIENT_ID)
                                Logger.d("CODE1", response1.code().toString() + "")
                                Logger.d("MESSAGE1", response1.message() + "")
                                Logger.d("RESPONSE1", "" + Gson().toJson(response1))
                                //var str_responses = Gson().toJson(response1)
                                val gson = GsonBuilder().serializeNulls().create()
                                var str_responses = gson.toJson(response1)
                                val jsonObj: JSONObject = JSONObject(
                                    str_responses.substring(
                                        str_responses.indexOf("{"),
                                        str_responses.lastIndexOf("}") + 1
                                    )
                                )

                                if (jsonObj.has("body") && !jsonObj.isNull("body")) {
                                    val jsonObj1: JSONObject = jsonObj.optJSONObject("body")
                                    val response_code: Int = jsonObj1.optInt("response_code")

//
                                    val messages: String = jsonObj1.optString("message")
                                    Log.d("TAGG1", "Response " + messages)
                                    var jsonArray: JSONArray = jsonObj1.optJSONArray("body")
                                    if (response1.isSuccessful) {
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
                                            jsonArray.optJSONObject(0).optString("entity_type")!!,
                                            jsonArray.optJSONObject(0).optString("entity_value")!!,
                                            jsonArray.optJSONObject(0).optString("group_id")!!,
                                            jsonArray.optJSONObject(0).optString("post_id")!!,
                                            jsonArray.optJSONObject(0).optString("created_by")!!,
                                            jsonArray.optJSONObject(0).optString("upvote_count")!!,
                                            jsonArray.optJSONObject(0)
                                                .optString("downvote_count")!!,
                                            jsonArray.optJSONObject(0).optString("url")!!,
                                            jsonArray.optJSONObject(0).optString("hash_tags")!!,
                                            jsonArray.optJSONObject(0).optString("status")!!,
                                            jsonArray.optJSONObject(0).optString("edited")!!,
                                            jsonArray.optJSONObject(0).optString("created_on")!!,
                                            jsonArray.optJSONObject(0).optString("modified_on")!!,
                                            jsonArray.optJSONObject(0)
                                                .optString("created_on_str")!!,
                                            jsonArray.optJSONObject(0).optString("comment_count")!!,
                                            jsonArray.optJSONObject(0).optString("replies_count")!!,
                                            jsonArray.optJSONObject(0)
                                                .optString("aggregate_count")!!,
                                            jsonArray.optJSONObject(0).optString("username")!!,
                                            jsonArray.getJSONObject(0).optString("email")!!,
                                            jsonArray.getJSONObject(0).optString("profile_icon")!!,
                                            jsonArray.getJSONObject(0).optString("is_owner")!!,
                                            GroupsReplyModel()
                                        )

                                        // }
                                        listOfPostdata[x].comment_list = model1

                                    } else {
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

            override fun onFailure(call: Call<GroupPostsNew>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")

//                Toast.makeText(applicationContext,"No Posts to load!!",Toast.LENGTH_LONG).show()
                loadprev.visibility = View.GONE
                loadnext.visibility = View.GONE
                scroll = 0
            }
        })

//        if(my_swipeRefresh_Layout.isRefreshing){
//            my_swipeRefresh_Layout.isRefreshing = false
//        }
        Thread.sleep(1000)
    }


    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }

        if (page.equals("Dashboard")) {
            val intent = Intent(applicationContext, ZActivityDashboard::class.java)
            intent.flags = FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("isLoggedIn", true)
            startActivity(intent)
            finish()
        }
        if (page.equals("Jobs")) {
            val intent = Intent(applicationContext, ZActivityJobs::class.java)
            intent.flags = FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("isLoggedIn", true)
            startActivity(intent)
            finish()
        } else if (page.equals("Companies")) {
            val intent = Intent(applicationContext, ZActivityCompanies::class.java)
            intent.flags = FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("isLoggedIn", true)
            startActivity(intent)
            finish()
        } else if (page.equals("NewsFeed")) {
            val intent = Intent(applicationContext, NewsFeed::class.java)
            intent.flags = FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("isLoggedIn", true)
            startActivity(intent)
            finish()
        } else if (page.equals("Groups")) {
            val intent = Intent(applicationContext, ZActivityGroups::class.java)
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
                            var json_objectdetail: JSONObject = jsonarray.getJSONObject(i)
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
                intent.putExtra("grpName", groupname.text.toString())
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

        Log.d("TAGG", "Edited is" + yesno)
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


}

