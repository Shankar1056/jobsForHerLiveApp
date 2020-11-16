package com.jobsforher.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
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
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jobsforher.R
import com.jobsforher.adapters.GroupCategoryAdapter
import com.jobsforher.adapters.MyGroupsAdapter
import com.jobsforher.adapters.ZGroupsCategoryAdapter
import com.jobsforher.expert_chat.ExpertChatActivity
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
import kotlinx.android.synthetic.main.toolbar_groups.*
import kotlinx.android.synthetic.main.zgroups_app_bar_main.*
import kotlinx.android.synthetic.main.zgroups_content_main.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import com.jobsforher.models.CategoryView as CategoryView1

class ZActivityGroups() : Footer(), NavigationView.OnNavigationItemSelectedListener{

    var mRecyclerView: RecyclerView? = null
    var mAdapter: RecyclerView.Adapter<*>? = null
    var listOfMyGroupdata: ArrayList<GroupsView> = ArrayList()
    var listOfPostdataDump: ArrayList<GroupsView> = ArrayList()
    var listOfFilterMygroups: ArrayList<GroupsView> = ArrayList()
    var listOfFeaturedMygroups: ArrayList<GroupsView> = ArrayList()
    var listOfCompareGroupdata: ArrayList<GroupsView> = ArrayList()
    var message_filter:String=""
    var message_keyword:String=""
    var scroll:Int = 0

    var mgroupsRecyclerView: RecyclerView? = null
    var mgroupsAdapter: RecyclerView.Adapter<*>? = null
    var isLoggedIn=true
    private var retrofitInterface: RetrofitInterface? = null
    private var retrofitInterface1: RetrofitInterface? = null
    var next_page_no_featured: String="1"
    var prev_page_no_featured: String="1"
    var type_group: Int=0
    var isfilter:Int = 0
    private var doubleBackToExitPressedOnce = false

    var listOfCategories: ArrayList<CategoryView1> = ArrayList()
    var listOfCities: ArrayList<CityView> = ArrayList()

    var listCities: String=""
    var listCategory: String=""
    var listCategoryArray: ArrayList<String> = ArrayList()

    var listOfMainCategories: ArrayList<CategoriesMainView> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zactivity_groups)
        isLoggedIn=intent.getBooleanExtra("isLoggedIn",false)
        Log.d("TAGG", isLoggedIn.toString())
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close
        )
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        view_all_categories.setOnClickListener {
            intent = Intent(applicationContext, GroupsCategoryPage::class.java)
            startActivityForResult(intent, 20);
        }

        create_group.setOnClickListener {
            fabAddGroup.callOnClick()
        }

        expertChat.setOnClickListener {
            startActivity(Intent(this@ZActivityGroups, ExpertChatActivity::class.java))
        }


        fabAddGroup.setOnClickListener {
            intent = Intent(applicationContext, GroupCreateActivity::class.java)
            startActivity(intent)
        }

        fabFilterGroup.setOnClickListener {
            filter.callOnClick()
        }

        mappingWidgets()
        handleBackgrounds(btnGroups)
        loadNotificationbubble()
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
                menu.findItem(R.id.action_groups).title = Html.fromHtml("<font color='#99CA3B'>Groups</font>")
                navView.setNavigationItemSelectedListener(this)
                menu.findItem(R.id.action_groups).getIcon().setColorFilter(Color.GREEN,PorterDuff.Mode.SRC_ATOP);

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
                //menu.findItem(R.id.action_groups).title = Html.fromHtml("<font color='#99CA3B'>Groups</font>")
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
        mgroupsAdapter = MyGroupsAdapter(listOfMyGroupdata, isLoggedIn, 1,listOfCompareGroupdata,0,0,"Groups")
        mgroupsRecyclerView!!.adapter = mgroupsAdapter

        mappingWidgets()
        handleBackgrounds(btnGroups)

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
            listOfMyGroupdata.clear()
            loadMyGroupData("1")
            listOfCompareGroupdata.clear()
            type_group=1
            mygroups.setTypeface(null, Typeface.BOLD);
            featured.setTypeface(null, Typeface.NORMAL);
            all.setTypeface(null, Typeface.NORMAL);
        }
        else{
            img_profile_toolbar.visibility = View.GONE
//            notificaton.visibility = View.GONE
            sign_in.visibility = View.VISIBLE
            mygroups.visibility = View.GONE
            mygroups_selected.visibility = View.GONE
            featured_selected.visibility = View.VISIBLE
            listOfMyGroupdata.clear()
            loadFeaturedGroupData("1")
            type_group=2
            featured.setTypeface(null, Typeface.BOLD);
            mygroups.setTypeface(null, Typeface.NORMAL);
            all.setTypeface(null, Typeface.NORMAL);
        }

        sign_in.setOnClickListener {
            intent = Intent(applicationContext, SplashActivity::class.java)
            startActivity(intent)
        }

        filter.setOnClickListener {
            loadCategoryData()

        }

        getAllCategories()

        mainScroll_grops.getViewTreeObserver().addOnScrollChangedListener(object: ViewTreeObserver.OnScrollChangedListener {
            override fun onScrollChanged() {
                Log.d("TAGG","END EFORE")
                val view: View = mainScroll_grops.getChildAt(mainScroll_grops.getChildCount() - 1);
                val diff:Int = (view.getBottom() - (mainScroll_grops.getHeight() + mainScroll_grops.getScrollY()));
                if (diff == 0 && scroll==1) {
                    Log.d("TAGG","END")
                    if (type_group==1 )
                        loadMyGroupData(next_page_no_featured)
                    else if (type_group==2)
                        loadFeaturedGroupData(next_page_no_featured!!)
                    else if(type_group==3)
                        loadAllGroupData(next_page_no_featured)
                    else if(type_group==4)
                        loadRecommendedMyGroupData(next_page_no_featured)
                }

            }
        });

        mygroups.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                mygroups_selected.visibility = View.VISIBLE
                featured_selected.visibility = View.GONE
                all_selected.visibility = View.GONE
                recommended_selected.visibility = View.GONE

                type_group=1
                //Toast.makeText(applicationContext,"My groups",Toast.LENGTH_LONG).show()
                if (isfilter>0) {
                    if(listOfFilterMygroups.size>0)
                        empty_view.visibility = View.GONE
                    else
                        empty_view.visibility = View.VISIBLE
                    var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                    mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                    mgroupsAdapter = MyGroupsAdapter(listOfFilterMygroups, isLoggedIn, 1, listOfCompareGroupdata,1,0,"Groups")
                    mgroupsRecyclerView!!.adapter = mgroupsAdapter
                }
                else{
                    listOfCompareGroupdata.clear()
                    listOfMyGroupdata.clear()
                    loadMyGroupData("1")
                }


                featured.setTypeface(null, Typeface.NORMAL);
                mygroups.setTypeface(null, Typeface.BOLD);
                all.setTypeface(null, Typeface.NORMAL);
                recommended.setTypeface(null, Typeface.NORMAL)
            }
        })
        featured.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                mygroups_selected.visibility = View.GONE
                featured_selected.visibility = View.VISIBLE
                all_selected.visibility = View.GONE
                recommended_selected.visibility = View.GONE
                type_group=2
                if (isfilter>0){
                    if(listOfFeaturedMygroups.size>0)
                        empty_view.visibility = View.GONE
                    else
                        empty_view.visibility = View.VISIBLE
                    var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                    mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                    mgroupsAdapter = MyGroupsAdapter(listOfFeaturedMygroups, isLoggedIn, 2, listOfCompareGroupdata,1,0,"Groups")
                    mgroupsRecyclerView!!.adapter = mgroupsAdapter
                }
                else{
                    listOfMyGroupdata.clear()
                    loadFeaturedGroupData("1")
                }

                featured.setTypeface(null, Typeface.BOLD);
                mygroups.setTypeface(null, Typeface.NORMAL);
                all.setTypeface(null, Typeface.NORMAL);
                recommended.setTypeface(null, Typeface.NORMAL)
            }
        })
        all.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                mygroups_selected.visibility = View.GONE
                featured_selected.visibility = View.GONE
                all_selected.visibility = View.VISIBLE
                recommended_selected.visibility = View.GONE
                type_group=3
//                Toast.makeText(applicationContext,"All groups",Toast.LENGTH_LONG).show()
                if (isfilter>0){
                    if(listOfMyGroupdata.size>0)
                        empty_view.visibility = View.GONE
                    else
                        empty_view.visibility = View.VISIBLE
                    var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                    mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                    mgroupsAdapter = MyGroupsAdapter(listOfMyGroupdata, isLoggedIn, type_group, listOfCompareGroupdata,1,0,"Groups")
                    mgroupsRecyclerView!!.adapter = mgroupsAdapter
                }
                else{
                    listOfMyGroupdata.clear()
                    loadAllGroupData("1")
                }

                featured.setTypeface(null, Typeface.NORMAL);
                mygroups.setTypeface(null, Typeface.NORMAL);
                all.setTypeface(null, Typeface.BOLD);
                recommended.setTypeface(null, Typeface.NORMAL)
            }

        })

        recommended.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                mygroups_selected.visibility = View.GONE
                featured_selected.visibility = View.GONE
                all_selected.visibility = View.GONE
                recommended_selected.visibility = View.VISIBLE
                type_group=4
//                Toast.makeText(applicationContext,"All groups",Toast.LENGTH_LONG).show()
                if (isfilter>0){
                    var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                    mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                    mgroupsAdapter = MyGroupsAdapter(listOfMyGroupdata, isLoggedIn, type_group, listOfCompareGroupdata,1,0,"Groups")
                    mgroupsRecyclerView!!.adapter = mgroupsAdapter
                }
                else{
                    listOfMyGroupdata.clear()
                    loadRecommendedMyGroupData("1")
                }

                featured.setTypeface(null, Typeface.NORMAL);
                mygroups.setTypeface(null, Typeface.NORMAL);
                all.setTypeface(null, Typeface.NORMAL);
                recommended.setTypeface(null, Typeface.BOLD)
            }

        })

        loadprev.setOnClickListener {

            // listOfMyGroupdata.clear()
            if (type_group==1)
                loadMyGroupData(prev_page_no_featured)
            else if (type_group==2)
                loadFeaturedGroupData(prev_page_no_featured!!)
            else if(type_group==3)
                loadAllGroupData(prev_page_no_featured)
        }

        loadnext.setOnClickListener {
            // listOfMyGroupdata.clear()
            if (type_group==1)
                loadMyGroupData(next_page_no_featured)
            else if (type_group==2)
                loadFeaturedGroupData(next_page_no_featured!!)
            else if(type_group==3)
                loadAllGroupData(next_page_no_featured)
        }

        notifLayout.setOnClickListener {
            cart_badge.setText("0")
            intent = Intent(applicationContext, Notification::class.java)
            startActivity(intent)
        }
    }


    fun loadFeaturedGroupData(pageno:String){


        if (pageno.equals("1")) {
            listOfMyGroupdata.clear()
            mgroupsAdapter!!.notifyDataSetChanged()
        }
        else{}


        val params = HashMap<String, String>()
        params["type"] = "featured"
        params["page_no"] = pageno.toString()
        params["page_size"] = 4.toString()
        Logger.d("TAGG", "Params : " + Gson().toJson(params))
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getFeatredData("application/json", EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        call.enqueue(object : Callback<Featured_Group> {
            override fun onResponse(call: Call<Featured_Group>, response: Response<Featured_Group>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)

                Logger.d("RESPONSE1 join group", "" + Gson().toJson(response))

                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                Logger.d("RESPONSE", "" + str_response)
                //var str_response = Gson().toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                    val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                    var jsonarray_info: JSONArray = jsonObject1.getJSONArray("body")
                    var jsonarray_pagination: JSONObject = jsonObject1.getJSONObject("pagination")

                    val has_next: String = jsonarray_pagination.getBoolean("has_next").toString()

                    val pagenoo: String = jsonarray_pagination.getString("page_no")
                    val prev_page: Int
//                    if (jsonarray_pagination.has("prev_page"))
//                        prev_page = jsonarray_pagination.getInt("prev_page")
//                    else
//                        prev_page = 0
                    if (Integer.parseInt(pagenoo)>1)
                        prev_page = Integer.parseInt(pagenoo)-1
                    else
                        prev_page = 0


                    if (response.isSuccessful) {

                        for (i in 0 until response.body()!!.body!!.size) {

                            var json_objectdetail: JSONObject = jsonarray_info.getJSONObject(i)

                            val model: GroupsView = GroupsView();

                            model.id = json_objectdetail.getInt("id")
                            model.label = json_objectdetail.getString("name")
                            model.icon_url = json_objectdetail.getString("icon_url")
                            model.groupType = json_objectdetail.getString("visiblity_type")
                            // model.noOfMembers = json_objectdetail.getString("no_of_members")
                            model.description = json_objectdetail.getString("excerpt")
                            model.featured = json_objectdetail.getBoolean("featured")
                            model.status = json_objectdetail.getString("status")
                            model.is_member = if(json_objectdetail.isNull("is_member")) false else json_objectdetail.getBoolean("is_member")

                            model.noOfMembers = "0"

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
                        }

                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid Request")
                    }

                    if (has_next.equals("true")) {
                        val next_page: String = jsonarray_pagination.getInt("next_page").toString()
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

                    if(listOfMyGroupdata.size>0) {
                        val array: ArrayList<Int> = ArrayList()
                        for (i in 0 until listOfMyGroupdata.size) {

                            array.add(listOfMyGroupdata[i].id)
                        }
                        Log.d("TAGG", array.toString());
                        val params = HashMap<String, ArrayList<Int>>()
                        params["group_ids"] = array

                        var a: String = ""
                        var b: Boolean = false
                        retrofitInterface1 = RetrofitClient.client!!.create(RetrofitInterface::class.java)

                        val call =
                            retrofitInterface1!!.GetMemberCount(
                                EndPoints.CLIENT_ID,
                                "Bearer " + EndPoints.ACCESS_TOKEN,
                                params
                            )

                        call.enqueue(object : Callback<GetGroupMemberResponse> {
                            override fun onResponse(
                                call: Call<GetGroupMemberResponse>,
                                response: Response<GetGroupMemberResponse>
                            ) {

                                Logger.d("CODE", response.code().toString() + "")
                                Logger.d("MESSAGE", response.message() + "")
                                Logger.d("URL", "" + response)
                                Logger.d("RESPONSE follow company", "" + Gson().toJson(response))

                                if (response.isSuccessful) {
//                                    Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG)
//                                        .show()
                                    var entityList: List<Map<String, MemberBody>> = response.body()!!.body!!
                                    for (mapKey in 0 until entityList.size) {
                                        Log.d("TAGG", "mapKey : " + mapKey + " , mapValue : " + entityList.get(mapKey));
                                        for (k in 0 until array.size) {
                                            if(entityList.get(mapKey).containsKey(listOfMyGroupdata[k].id.toString())){
                                                Log.d("TAGG",entityList.get(mapKey).get(listOfMyGroupdata[k].id.toString())!!.member_count.toString())
//                                                val jsonObj: JSONObject =
//                                                    JSONObject(entityList.get(mapKey).get(listOfMyGroupdata[k].id.toString())!!.member_count.toString())
                                               // Log.d("TAGG", jsonObj.getString("member_count"))
//                                                a = jsonObj.getString("member_count").toString()
//                                                    .substring(0, jsonObj.getString("member_count").length - 2)
                                                a = entityList.get(mapKey).get(listOfMyGroupdata[k].id.toString())!!.member_count.toString()
                                                listOfMyGroupdata[k].noOfMembers = a
                                            }
                                            else{
                                                listOfMyGroupdata[k].noOfMembers = "0"
                                            }
                                        }
                                    }

                                    // listOfMyGroupdata.add(listOfPostdataDump[i])
                                    var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                                    mgroupsRecyclerView!!.layoutManager =
                                        mgroupsLayoutManager as RecyclerView.LayoutManager?
                                    mgroupsAdapter = MyGroupsAdapter(
                                        listOfMyGroupdata,
                                        isLoggedIn,
                                        2,
                                        listOfCompareGroupdata,
                                        0,
                                        0,
                                        "Groups"
                                    )
                                    mgroupsRecyclerView!!.adapter = mgroupsAdapter

                                } else {
                                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                                }
                            }

                            override fun onFailure(call: Call<GetGroupMemberResponse>, t: Throwable) {
                                Logger.d("TAGG", "Apply Job FAILED : $t")
                            }
                        })
                    }
                    else{
                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                        mgroupsRecyclerView!!.layoutManager =
                            mgroupsLayoutManager as RecyclerView.LayoutManager?
                        mgroupsAdapter = MyGroupsAdapter(
                            listOfMyGroupdata,
                            isLoggedIn,
                            2,
                            listOfCompareGroupdata,
                            0,
                            0,
                            "Groups"
                        )
                    }


                }
                else {
                    Toast.makeText(applicationContext, "No data exists", Toast.LENGTH_LONG).show()
                    empty_view.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<Featured_Group>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                empty_view.visibility = View.VISIBLE

            }
        })
//        listOfMyGroupdata.add(
//            GroupsView(0,"Women Freelancers","Working Women, Women Professionals","","Open Group","9652",
//                "Wish to work as per your time of convenience? " +
//                        "Come aboard this group of freelancers to stay in the know of all things that matter in your world.")
//        )

    }

    fun loadMyGroupData(pageno:String){

        mgroupsRecyclerView = findViewById(R.id.recycler_view_groups)
        if (pageno.equals("1")) {
            listOfMyGroupdata.clear()
            mgroupsAdapter!!.notifyDataSetChanged()
        }
        else{}
        val params = HashMap<String, String>()

        params["page_no"] = pageno.toString()
        params["page_size"] = Constants.MAXIMUM_PAGINATION_COUNT // it was 30 before

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface!!.getMyGroupData( EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        call.enqueue(object : Callback<Featured_Group> {
            override fun onResponse(call: Call<Featured_Group>, response: Response<Featured_Group>) {

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

                       // ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
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
                            val categoriesArray: JSONArray = json_objectdetail.optJSONArray("categories")
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

                        if (pageno.equals("1")) {
                            var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                            mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                            mgroupsAdapter = MyGroupsAdapter(listOfMyGroupdata, isLoggedIn, 1, listOfCompareGroupdata,0,0,"Groups")
                            mgroupsRecyclerView!!.adapter = mgroupsAdapter
                        }
                        else {
                            var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                            mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                            mgroupsAdapter = MyGroupsAdapter(listOfMyGroupdata, isLoggedIn, 1, listOfCompareGroupdata,0,0,"Groups")
                            mgroupsRecyclerView!!.adapter = mgroupsAdapter
                        }

                        if (type_group == 2){
                            //loadMyGroupData("1")
                            loadFeaturedGroupData("1")
                        }
                        else if (type_group == 3){
                            //loadMyGroupData("1")
                            loadAllGroupData("1")
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

            override fun onFailure(call: Call<Featured_Group>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext, "No Groups Exists, Join a group today!", Toast.LENGTH_LONG).show()
                empty_view.visibility = View.VISIBLE
                loadnext.visibility = View.GONE
                loadprev.visibility = View.GONE
                scroll =0
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

    fun loadAllGroupData(pageno:String){

        if (pageno.equals("1")) {
            listOfMyGroupdata.clear()
            mgroupsAdapter!!.notifyDataSetChanged()
        }
        else{}
        val params = HashMap<String, String>()

        params["page_no"] = pageno.toString()
        params["page_size"] = Constants.MAXIMUM_PAGINATION_COUNT
        params["type"] = "basic"

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getAllGroupData( "application/json",EndPoints.CLIENT_ID,
            "Bearer "+EndPoints.ACCESS_TOKEN,params)

        call.enqueue(object : Callback<Featured_Group> {
            override fun onResponse(
                call: Call<Featured_Group>,
                response: Response<Featured_Group>
            ) {

                Logger.d("URL", "" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))

                // var str_response = Gson().toJson(response)
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
                    var jsonarray_info: JSONArray = jsonObject1.optJSONArray("body")

                    var jsonarray_pagination: JSONObject = jsonObject1.optJSONObject("pagination")

                    val has_next: String = jsonarray_pagination.optBoolean("has_next").toString()

                    val pagenoo: String = jsonarray_pagination.optString("page_no")
                    var prev_page: Int
                    if (Integer.parseInt(pagenoo) > 1)
                        prev_page = Integer.parseInt(pagenoo) - 1
                    else
                        prev_page = 0


                    if (response.isSuccessful) {

                        for (i in 0 until response.body()!!.body!!.size) {

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
                            model.is_member =
                                if (json_objectdetail.isNull("is_member")) false else json_objectdetail.optBoolean(
                                    "is_member"
                                )


                            var citiesArray: JSONArray = json_objectdetail.optJSONArray("cities")
                            val listOfCity: ArrayList<Int> = ArrayList()
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
                        empty_view.visibility = View.GONE
                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                        mgroupsRecyclerView!!.layoutManager =
                            mgroupsLayoutManager as RecyclerView.LayoutManager?
                        mgroupsAdapter = MyGroupsAdapter(
                            listOfMyGroupdata,
                            isLoggedIn,
                            3,
                            listOfCompareGroupdata,
                            0,
                            0,
                            "Groups"
                        )
                        mgroupsRecyclerView!!.adapter = mgroupsAdapter

                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid Request")
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


                    loadprev.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<Featured_Group>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })
    }

    fun loadRecommendedMyGroupData(pageno:String){


        if (pageno.equals("1")) {
            listOfMyGroupdata.clear()
            mgroupsAdapter!!.notifyDataSetChanged()
        }
        else{}
        listOfCompareGroupdata.clear()

        val params = java.util.HashMap<String, String>()

        params["page_no"] = pageno.toString()
        params["page_size"] = Constants.MAXIMUM_PAGINATION_COUNT

        mRecyclerViewCetificates = findViewById(R.id.recycler_view_groups)
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface!!.getRecommendedMyGroupData( EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        call.enqueue(object : Callback<Featured_Group> {
            override fun onResponse(call: Call<Featured_Group>, response: Response<Featured_Group>) {

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

                    //hasnextRecommendedGroups = jsonarray_pagination.optBoolean("has_next")

                    // val pagenoo: String = jsonarray_pagination.optString("next_page")

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
                            model.featured = json_objectdetail.optBoolean("featured")
                            model.status = json_objectdetail.optString("status")
                            model.is_member = false

                            var citiesArray: JSONArray = json_objectdetail.optJSONArray("cities")
                            val listOfCity: ArrayList<Int> = ArrayList()
//                        for (j in 0 until citiesArray.length()) {
//                            var citiesObject:JSONObject=citiesArray.optJSONObject(j).
//                            listOfCity.add(citiesObject)
//                        }
                            val categoriesArray: JSONArray = json_objectdetail.optJSONArray("categories")
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

                        mgroupsRecyclerView!!.visibility = View.VISIBLE
//                        default_company_groups.visibility = View.GONE
                        // seemore_groups.visibility = View.VISIBLE
                        empty_view.visibility = View.GONE
                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                        mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
                        mgroupsAdapter = MyGroupsAdapter(listOfMyGroupdata, isLoggedIn, 3, listOfCompareGroupdata, 0,1,"NewsFeed")
                        mgroupsRecyclerView!!.adapter = mgroupsAdapter

                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid Request")
                    }

                    if (has_next.equals("true")) {
                        val next_page: String = jsonarray_pagination.optInt("next_page").toString()
                        loadnext.visibility = View.GONE
                        next_page_no_featured = next_page
                        scroll =1
                    } else {
                        loadnext.visibility = View.GONE
                        scroll =0
                    }
                }
                else
                    Toast.makeText(applicationContext,"No data exists",Toast.LENGTH_LONG).show()




                loadprev.visibility = View.GONE
            }

            override fun onFailure(call: Call<Featured_Group>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                //Toast.makeText(applicationContext, "No Groups Exixts, Join a group today!", Toast.LENGTH_LONG).show()
                mgroupsRecyclerView!!.visibility = View.GONE
//                default_company_groups.visibility = View.VISIBLE
//                default_company_groups.setText("There are no recommended groups for you at the moment. Meet peers, interact with experts and stay up-to-date with industry trends by joining the many JobsForHer Groups here: jobsforher.com/groups")
//                seemore_groups.visibility = View.GONE
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
                closeDrawer()
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
                HelperMethods.showAppShareOptions(this@ZActivityGroups)
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }

    private fun closeDrawer() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    public fun openBottomSheetFilter() {

//        val mLayoutManager1 = GridLayoutManager(applicationContext, 3)
//        mRecyclerView_city!!.layoutManager = mLayoutManager1
//        mAdapterCities = ZGroupsPhotosAdapter(listOfCities)
//        mRecyclerView_city!!.adapter = mAdapterCities

        val dialog = Dialog(this, R.style.AppTheme)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog .setCancelable(true)
        dialog .setContentView(R.layout.bottom_sheet_filter)

        var mRecyclerView_category = dialog.findViewById(R.id.recycler_view_filtercategory) as RecyclerView
        //var mRecyclerView_city = dialog.findViewById(R.id.recycler_view_filtercity) as RecyclerView
        var adapter: ZGroupsCategoryAdapter? = null
        var mAdapterCategories: RecyclerView.Adapter<*>? = null
        var mAdapterCities: RecyclerView.Adapter<*>? = null

        val mLayoutManager = GridLayoutManager(applicationContext, 1)
        mRecyclerView_category!!.layoutManager = mLayoutManager
//        adapter = ZGroupsCategoryAdapter(listOfCategories)
        mAdapterCategories = ZGroupsCategoryAdapter(listOfCategories,listCategoryArray)
        mRecyclerView_category!!.adapter = mAdapterCategories

//        val mLayoutManager1 = GridLayoutManager(applicationContext, 1)
//        mRecyclerView_city!!.layoutManager = mLayoutManager1
//        mAdapterCities = ZGroupsCitiesAdapter(listOfCities)
//        mRecyclerView_city!!.adapter = mAdapterCities


        val category = dialog .findViewById(R.id.category) as TextView
        val city = dialog .findViewById(R.id.city) as TextView
        val close = dialog.findViewById(R.id.filter_sidecancel) as TextView
        close.setOnClickListener {
            // listCategoryArray.clear()
            dialog.cancel();
        }

        val filterClose = dialog.findViewById(R.id.close_filter) as ImageView
        filterClose.setOnClickListener {
            listCategoryArray.clear()
            dialog.cancel();
        }

        category.setTextColor(Color.BLACK)
        city.setTextColor(Color.GRAY)
        category.setOnClickListener {
            mRecyclerView_category.visibility = View.VISIBLE
            //mRecyclerView_city.visibility = View.GONE
            category.setTextColor(Color.BLACK)
            city.setTextColor(Color.GRAY)
        }

//        city.setOnClickListener {
//            mRecyclerView_category.visibility = View.GONE
//            mRecyclerView_city.visibility = View.VISIBLE
//            category.setTextColor(Color.GRAY)
//            city.setTextColor(Color.BLACK)
//        }
        Log.d("TAGG", message_keyword+"1")
        val keyword_edittext = dialog .findViewById(R.id.keyword_edittext) as EditText
        keyword_edittext.setText(message_keyword)
        var filter_apply = dialog.findViewById(R.id.filter_apply) as TextView
        var filter_reset = dialog.findViewById(R.id.filter_reset) as TextView
        val reset = dialog.findViewById(R.id.reset) as TextView

        Log.d("TAGG", message_keyword+"2")
        reset.setOnClickListener {
            message_keyword = ""
            keyword_edittext.setText("")
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
            Log.d("TAGG", message_keyword+"3")
            Log.d("TAGG", keyword_edittext.text.toString()+"3")
            message_keyword = keyword_edittext.text.toString()
            loadFilteredData(keyword_edittext.text.toString(),listCities, listCategory)
            dialog.cancel()

        }

        filter_reset.setOnClickListener {
            keyword_edittext.text.clear()
            listCategoryArray.clear()
            listCities = ""
            listCategory=""
            isfilter = 0
            filter.setText("FILTER")
            val mLayoutManager = GridLayoutManager(applicationContext, 1)
            mRecyclerView_category!!.layoutManager = mLayoutManager
            mAdapterCategories = ZGroupsCategoryAdapter(listOfCategories, listCategoryArray)
            mRecyclerView_category!!.adapter = mAdapterCategories
            //dialog.cancel()
            loadAllGroupData("1")
            type_group = 3
            mygroups_selected.visibility = View.GONE
            featured_selected.visibility = View.GONE
            all_selected.visibility = View.VISIBLE
            featured.setTypeface(null, Typeface.NORMAL);
            mygroups.setTypeface(null, Typeface.NORMAL);
            all.setTypeface(null, Typeface.BOLD);
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

        wlp.gravity = Gravity.CENTER;
        wlp.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setAttributes(wlp);
        dialog .show()


    }


    public fun getSelectedCities(id:Int, name:String) {
        Log.d("TAGG", name)
        if (id==0)
            listCities = ""
        else
            listCities = id.toString()
    }

    public fun getSelectedCategory(id:Int, name:String) {
        Log.d("TAGG", name)
        if (id==0) {
            listCategory = ""
            listCategoryArray.remove(name)
        }
        else {
            listCategory = id.toString()
            listCategoryArray.add(name)
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
                    //loadCityData()
                    openBottomSheetFilter()
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
                            model.name = json_objectdetail.optString("name")
                            listOfCities.add(model)
                        }
                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid")
                    }

                    //openBottomSheetFilter()
                }
                else
                    Toast.makeText(applicationContext,"No data exists",Toast.LENGTH_LONG).show()
            }
            override fun onFailure(call: Call<GetCityResponse>, t: Throwable) {
                Logger.d("TAG", "FAILED : $t")
            }
        })
    }

    fun loadFilteredData(keywords:String,city:String,category:String){

        mygroups_selected.visibility = View.GONE
        loadnext.visibility = View.GONE
        loadprev.visibility = View.GONE
        listOfMyGroupdata.clear()
        listOfFilterMygroups.clear()
        listOfFeaturedMygroups.clear()
        mgroupsAdapter!!.notifyDataSetChanged()
        isfilter =1

        val params = HashMap<String, String>()

        Log.d("TAGG", "FILTERED DATA PRMS "+keywords+city+category)

        if (type_group==2){
            //params["type"] = "featured"
            featured_selected.visibility = View.VISIBLE
            all_selected.visibility = View.GONE
            mygroups_selected.visibility = View.GONE
            featured.setTypeface(null, Typeface.BOLD);
            mygroups.setTypeface(null, Typeface.NORMAL);
            all.setTypeface(null, Typeface.NORMAL);
            filter.setText("FILTER *")
        }
        else if(type_group==3){
            //params["type"] = "featured"//"basic"
            featured_selected.visibility = View.GONE
            mygroups_selected.visibility = View.GONE
            all_selected.visibility = View.VISIBLE
            featured.setTypeface(null, Typeface.NORMAL);
            mygroups.setTypeface(null, Typeface.NORMAL);
            all.setTypeface(null, Typeface.BOLD);
            filter.setText("FILTER *")
        }
        else if(type_group==1){
            featured_selected.visibility = View.GONE
            mygroups_selected.visibility = View.VISIBLE
            all_selected.visibility = View.GONE
            featured.setTypeface(null, Typeface.NORMAL);
            mygroups.setTypeface(null, Typeface.BOLD);
            all.setTypeface(null, Typeface.NORMAL);
            filter.setText("FILTER *")
        }
        if (keywords.equals("")){
            if (city.equals("")){
                if (category.equals("")){
                    Toast.makeText(applicationContext,"Please select atlest one flter value",Toast.LENGTH_LONG).show()
                }
                else{
                    params["category"] = category
                }
            }else{
                params["city"] = city
                if (category.equals("")){
                }
                else{
                    params["category"] = category
                }
            }
        }
        else{
            params["keyword"] = keywords
            if (city.equals("")){
                if (category.equals("")){
                }
                else{
                    params["category"] = category
                }
            }else{
                params["city"] = city
                if (category.equals("")){
                }
                else{
                    params["category"] = category
                }
            }
        }


        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getFiltersData(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN,params)
        call.enqueue(object : Callback<Featured_Group> {

            override fun onResponse(
                call: Call<Featured_Group>,
                response: Response<Featured_Group>
            ) {

                Log.d("TAG", "CODE" + response.code().toString() + "")
                Log.d("TAG", "MESSAGE" + response.message() + "")
                Log.d("TAG", "RESPONSE" + "" + Gson().toJson(response))
                Log.d("TAG", "URL" + "" + response)
                // var str_response = Gson().toJson(response)
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
                    var jsonarray_info: JSONArray = jsonObject1.optJSONArray("body")
                    var jsonarray_pagination: JSONObject = jsonObject1.optJSONObject("pagination")

                    val has_next: String = jsonarray_pagination.optBoolean("has_next").toString()

                    val pagenoo: String = jsonarray_pagination.optString("page_no")
                    val prev_page: Int
                    if (jsonarray_pagination.has("prev_page"))
                        prev_page = jsonarray_pagination.optInt("prev_page")
                    else
                        prev_page = 0

                    if (response.isSuccessful) {
                        for (i in 0 until response.body()?.body?.size!!) {

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
                            model.is_member =
                                if (json_objectdetail.isNull("is_member")) false else json_objectdetail.optBoolean(
                                    "is_member"
                                )

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

                            empty_view.visibility = View.GONE
                            if (model.featured == false) {
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
                            if (model.featured == true) {
                                listOfFeaturedMygroups.add(
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
                            for (k in 0 until listOfCompareGroupdata.size) {
                                val listOfCompareGroupdataId = listOfCompareGroupdata[k].id
                                val modelId = model.id
                                if (listOfCompareGroupdata[k].id == model.id) {
                                    listOfFilterMygroups.add(
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
                                    break
                                }
                            }
                        }
                        empty_view.visibility = View.VISIBLE
                        if (type_group == 3) {
                            if (listOfMyGroupdata.size > 0)
                                empty_view.visibility = View.GONE
                            mgroupsRecyclerView = findViewById(R.id.recycler_view_groups)
                            var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                            mgroupsRecyclerView!!.layoutManager =
                                mgroupsLayoutManager as RecyclerView.LayoutManager?
                            mgroupsAdapter =
                                MyGroupsAdapter(
                                    listOfMyGroupdata,
                                    isLoggedIn,
                                    type_group,
                                    listOfCompareGroupdata,
                                    1,
                                    0,
                                    "Groups"
                                )
                            mgroupsRecyclerView!!.adapter = mgroupsAdapter
                        } else if (type_group == 2) {
                            if (listOfFeaturedMygroups.size > 0)
                                empty_view.visibility = View.GONE
                            mgroupsRecyclerView = findViewById(R.id.recycler_view_groups)
                            var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                            mgroupsRecyclerView!!.layoutManager =
                                mgroupsLayoutManager as RecyclerView.LayoutManager?
                            mgroupsAdapter =
                                MyGroupsAdapter(
                                    listOfFeaturedMygroups,
                                    isLoggedIn,
                                    type_group,
                                    listOfCompareGroupdata,
                                    1,
                                    0,
                                    "Groups"
                                )
                            mgroupsRecyclerView!!.adapter = mgroupsAdapter
                        } else if (type_group == 1) {
                            if (listOfFilterMygroups.size > 0)
                                empty_view.visibility = View.GONE
                            mgroupsRecyclerView = findViewById(R.id.recycler_view_groups)
                            var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                            mgroupsRecyclerView!!.layoutManager =
                                mgroupsLayoutManager as RecyclerView.LayoutManager?
                            mgroupsAdapter =
                                MyGroupsAdapter(
                                    listOfFilterMygroups,
                                    isLoggedIn,
                                    type_group,
                                    listOfCompareGroupdata,
                                    1,
                                    0,
                                    "Groups"
                                )
                            mgroupsRecyclerView!!.adapter = mgroupsAdapter
                        }
                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid")
                    }
                } else {
                    Toast.makeText(applicationContext, "No data exists", Toast.LENGTH_LONG).show()
                    empty_view.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<Featured_Group>, t: Throwable) {
                Logger.d("TAG", "FAILED : $t")
                empty_view.visibility = View.VISIBLE
            }
        })


    }

    fun getMemberCount(id:Int):String {

        val params = HashMap<String, ArrayList<Int>>()

        val array: ArrayList<Int> = ArrayList()
        array.add(id)
        params["group_ids"] = array

        var a: String = ""
        var b: Boolean = false
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call =
            retrofitInterface!!.GetMemberCount(EndPoints.CLIENT_ID, "Bearer " + EndPoints.ACCESS_TOKEN, params)

        call.enqueue(object : Callback<GetGroupMemberResponse> {
            override fun onResponse(
                call: Call<GetGroupMemberResponse>,
                response: Response<GetGroupMemberResponse>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE follow company", "" + Gson().toJson(response))

                if (response.isSuccessful) {
                    //  Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                    var entityList: List<Map<String, MemberBody>> = response.body()!!.body!!
                    for (mapKey in 0 until entityList.size) {
                        Log.d("TAGG", "mapKey : " + mapKey + " , mapValue : " + entityList.get(mapKey));
                        val jsonObj: JSONObject = JSONObject(entityList.get(mapKey).get(id.toString()).toString())
                        Log.d("TAGG", jsonObj.optString("member_count"))
                        a = jsonObj.optString("member_count").toString()
                            .substring(0, jsonObj.optString("member_count").length - 2)
                    }

                } else {
                    // ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                }
            }

            override fun onFailure(call: Call<GetGroupMemberResponse>, t: Throwable) {
                Logger.d("TAGG", "Apply Job FAILED : $t")
            }
        })

        return a

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


                        for (i in 0 until response.body()!!.body!!.size) {

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
                        val mLayoutManager = LinearLayoutManager(
                            applicationContext, LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        mRecyclerView!!.layoutManager = mLayoutManager
                        mAdapter = GroupCategoryAdapter(listOfMainCategories)
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

    fun leaveGroup(id:Int, btnJoinGroup:Button, btnJoined: Button){

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
                    //listOfMyGroupdata.clear()
//                    listOfCompareGroupdata.clear()
//                    loadMyGroupData("1")
                    btnJoinGroup.visibility =View.VISIBLE
                    btnJoined.visibility = View.GONE

                    Logger.e("TAGG", "" + response.body()!!.message.toString())
                    if (isfilter>0) {
                        for (k in 0 until listOfCompareGroupdata.size) {
                            if (listOfCompareGroupdata[k].id == id) {
                                listOfCompareGroupdata.removeAt(k)
                                break
                            }
                        }
                        loadFilteredData("","",message_filter)
                    }
                    else{

                        listOfCompareGroupdata.clear()
                        listOfMyGroupdata.clear()
                        loadMyGroupData("1")

//                        val intent = Intent(applicationContext, ZActivityGroups::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or  Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        intent.putExtra("isLoggedIn", true)
//                        startActivity(intent)
//                        finish()
                    }
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

    fun joinGroup(id:Int, btnJoinGroup:Button, type:String, btnJoined: Button){

        Logger.d("CODE",id.toString() + "")

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.JoinGroup(id,EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<JoinGroupResponse> {
            override fun onResponse(call: Call<JoinGroupResponse>, response: Response<JoinGroupResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                if (response.isSuccessful && response.body()!!.responseCode == 10104) {

                    if(response.body()!!.responseCode == 10108){
                        if (response.body()!!.responseCode == 10108){
                            btnJoinGroup.text = "Requested"
                        }
                    }
                    else {
                        if (type.equals("private")) {
                            btnJoinGroup.text = "Requested"
                        } else {
                            btnJoinGroup.visibility = View.GONE
                            btnJoined.visibility = View.VISIBLE
                        }

                        if (isfilter > 0) {
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

                                var jsonarray_info: JSONObject = jsonObject1.optJSONObject("body")
                                var json_objectdetail: JSONObject =
                                    jsonarray_info  //jsonarray_info.optJSONObject(0)

                                val model: GroupsView = GroupsView();
                                // "approver_id":"","created_on":"2019-09-24 11:11:44","group_id":24,"is_member":true,"modified_on":"2019-09-24 11:11:44","request_status":"approved","role":"member","user_id":1274
                                model.id = json_objectdetail.optInt("group_id")
                                model.label = ""
                                model.icon_url = ""
                                model.groupType = ""
                                model.noOfMembers = ""
                                model.description = ""
                                model.featured = false
                                model.status = ""
                                model.is_member = false

                                val listOfCity: ArrayList<Int> = ArrayList()
                                val listOfCategories: ArrayList<Categories> = ArrayList()
                                model.categories = listOfCategories
                                model.cities = listOfCity
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

                                loadFilteredData("", "", message_filter)
                            }
                        }
                    }

                } else {
                    if (response.body()!!.responseCode == 10108){
                        btnJoinGroup.text = "Requested"
                    }
                }
            }

            override fun onFailure(call: Call<JoinGroupResponse>, t: Throwable) {

                Logger.d("TAGG", "Join Group FAILED : $t")

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

        if (resultCode == 20) {
            val message:String=data!!.getStringExtra("VAL");
            message_filter = message
            loadFilteredData("","",message)
        }
        else{
        }
    }

    public override fun onResume() {
        super.onResume()
        // put your code here...

    }
}




