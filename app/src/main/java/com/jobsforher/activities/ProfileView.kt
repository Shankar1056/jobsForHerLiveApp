package com.jobsforher.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.*
import android.graphics.Bitmap
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Html
import android.util.Base64
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jobsforher.R
import com.jobsforher.adapters.*
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.models.*
import com.jobsforher.network.responsemodels.*
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import com.plumillonforge.android.chipview.Chip
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import de.hdodenhof.circleimageview.CircleImageView
import info.abdolahi.CircularMusicProgressBar
import kotlinx.android.synthetic.main.layout_profileview.*
import kotlinx.android.synthetic.main.toolbar_groups.*
import kotlinx.android.synthetic.main.zgroups_app_bar_main_profile.*
import kotlinx.android.synthetic.main.zgroups_nav_header_main.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File

private var retrofitInterface: RetrofitInterface? = null
var listOfProfiledata: ArrayList<ProfileModel> = ArrayList()
var listOfProfiledata1: ArrayList<MyProfile> = ArrayList()
var listOfCertificateData: ArrayList<CertificateModel> = ArrayList()
var listOfWorkingData: ArrayList<WorkingModel> = ArrayList()
var listOfLifeData: ArrayList<LifeModel> = ArrayList()

var listOfEducationData: ArrayList<EducationModel> = ArrayList()
var listDataPass: ArrayList<String> = ArrayList()
var mRecyclerViewCetificates: RecyclerView? = null
var mAdapterCertificates: RecyclerView.Adapter<*>? = null
var mRecyclerViewTimline: RecyclerView? = null
var mAdapterTimeline: RecyclerView.Adapter<*>? = null
var list : ArrayList<TimeListModel> = ArrayList()
private var awesomeValidation_starter: AwesomeValidation? = null
private var awesomeValidation_riser: AwesomeValidation? = null
private var awesomeValidation_restarter: AwesomeValidation? = null

var listOfRecognitionData: ArrayList<RecognitionModel> = ArrayList()

var name: TextView? =null
var imageprofile: CircularMusicProgressBar? =null
var imagep: CircleImageView? =null
var descr: TextView?=null
var followingId: TextView?=null
var followersId: TextView?=null
var stageType: TextView?=null
var profilesumm: TextView?=null
var profile_add:TextView?=null

private val PREF_ICON = "image"

var profileUrl: String?=""

var degree_val: TextView? =null
var degreeValue_val: TextView? =null
var institution_val: TextView? =null
var institutionValue_val: TextView? =null
var caption_val: TextView? =null
var captionValue_val: TextView? =null
var previous_val: TextView? =null
var previousValue_val: TextView? =null
var company_val: TextView? =null
var companyValue_val: TextView? =null
var location_val: TextView?= null
var locationValue_val: TextView?=null
var designation_val: TextView?= null
var desigationValue_val: TextView?=null
var editprofileL : ImageView?=null
var model: ProfileModel = ProfileModel();
var isLoggedIn=true
var modelSkillsDisplay: SkillsList = SkillsList();
var click: Int = 0
private var PRIVATE_MODE = 0
private val PREF_STATUS = "isLoggedInStatus"
private val PREF_NAME = "name"
private val PREF_PROFILEURL = "profileUrl"
private val PREF_ACCESSTOKEN = "accesstoken"
private val PREF_PERCENTAGE = "0"

class ProfileView : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var doubleBackToExitPressedOnce = false
    var profileUsername: String = ""
    private var mNetworkReceiver: BroadcastReceiver? = null

//    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            val notConnected = intent.getBooleanExtra(
//                ConnectivityManager
//                .EXTRA_NO_CONNECTIVITY, false)
//            if (notConnected) {
//                disconnected()
//            } else {
//                connected()
//            }
//        }
//    }

    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zactivity_profileview)


//        mNetworkReceiver = com.jobsforher.activities.NetworkChangeReceiver()
//        registerNetworkBroadcastForNougat()

//        FirebaseInstanceId.getInstance().instanceId
//            .addOnCompleteListener(OnCompleteListener { task ->
//                if (!task.isSuccessful) {
//                    Log.w("TAG", "getInstanceId failed", task.exception)
//                    return@OnCompleteListener
//                }
//
//                // Get new Instance ID token
//                val token = task.result?.token
//
//                // Log and toast
//                //val msg = getString(R.string.msg_token_fmt, token)
//                Log.d("TAG", token)
//                Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
//            })

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close
        )
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        loadNotificationbubble()

        val sharedPref: SharedPreferences = getSharedPreferences(
            "mysettings",
            Context.MODE_PRIVATE
        );
        EndPoints.ACCESS_TOKEN = sharedPref.getString(PREF_ACCESSTOKEN, "")!!
        EndPoints.USERNAME = sharedPref.getString(PREF_NAME, "")!!
        EndPoints.PROFILE_URL = sharedPref.getString(PREF_PROFILEURL, "")!!
        EndPoints.profileUrl=sharedPref.getString(PREF_PROFILEURL, "")!!

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
//                        intent = Intent(applicationContext, ProfileView::class.java)
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
                navView.setNavigationItemSelectedListener(this)
                val hView = navView.getHeaderView(0)
                val loggedin_header = hView.findViewById(R.id.loggedin_layout) as RelativeLayout
                val nologgedin_layout = hView.findViewById(R.id.nologgedin_layout) as RelativeLayout
                loggedin_header.visibility = View.VISIBLE
                nologgedin_layout.visibility = View.GONE
                val profile_name = hView.findViewById(R.id.profile_name) as TextView
                val img_profile_sidemenu = hView.findViewById(R.id.img_profile_sidemenu) as CircleImageView
                // val img_profile_toolbar = hView.findViewById(R.id.img_profile_toolbar) as CircleImageView
//                val img_profilemain = hView.findViewById(R.id.img_profilemain) as CircleImageView

                profile_name.setText(EndPoints.USERNAME)
//                Picasso.with(applicationContext)
//                    .load(EndPoints.PROFILE_ICON)
//                    .placeholder(R.drawable.ic_launcher_foreground)
//                    .into(img_profile)

                img_profile.visibility = View.VISIBLE
//                notificaton.visibility = View.VISIBLE
                sign_in.visibility = View.GONE


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
                navView.setNavigationItemSelectedListener(this)
                val hView = navView.getHeaderView(0)
                val loggedin_header = hView.findViewById(R.id.loggedin_layout) as RelativeLayout
                val nologgedin_layout = hView.findViewById(R.id.nologgedin_layout) as RelativeLayout
                loggedin_header.visibility = View.GONE
                nologgedin_layout.visibility = View.VISIBLE
                img_profile.visibility = View.GONE
//                notificaton.visibility = View.GONE
                sign_in.visibility = View.VISIBLE
            }
        }

        img_profile.setOnClickListener {
            Log.d("TAGG", "CLICKED")
        }


        name= findViewById(R.id.groupname) as TextView

        imageprofile = findViewById(R.id.group_icon) as CircularMusicProgressBar //CircleImageView

        descr= findViewById(R.id.description) as TextView
        followingId= findViewById(R.id.following_id) as TextView
        followersId= findViewById(R.id.followers_id) as TextView
        stageType = findViewById(R.id.stagetype) as TextView
        editprofileL = findViewById(R.id.edit_icon) as ImageView
        profilesumm= findViewById(R.id.profilesummary_data) as TextView
        profile_add = findViewById(R.id.profilesummary_add) as TextView

        editprofileL!!.setOnClickListener {
            //            intent = Intent(applicationContext, ProfileEdit::class.java)
//            intent.putExtra("lifestage", listOfProfiledata)
//            startActivity(intent)
            openEditProfile()
        }

        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
        mRecyclerViewTimline = findViewById(R.id.recycler_view_milestone)
        mRecyclerViewTimline!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
        mAdapterTimeline = MilestonesAdapter(list)
        mRecyclerViewTimline!!.adapter = mAdapterTimeline

        copy_url.setOnClickListener {
            Toast.makeText(
                applicationContext,
                "http://www.jobsforher.com/profile/" + profileUrl + " Copied!!",
                Toast.LENGTH_LONG
            ).show()
            var clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            var clip: ClipData = ClipData.newPlainText(
                "simple text",
                "http://www.workingnaari.in/profile/" + profileUrl
            )
            //clipboard!!.primaryClip = clip
            clipboard.setPrimaryClip(clip)
            copy_url.setText("Copied")
        }

        share_button.setOnClickListener {
            intent = Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Profile+ for User | JobsForHer");
            intent.putExtra(Intent.EXTRA_TEXT, "http://www.jobsforher.com/profile/" + profileUrl);
            startActivity(Intent.createChooser(intent, "Share Profile+ link!"));
        }

        (imageprofile as CircularMusicProgressBar).setOnClickListener {
            //            val intent = Intent(applicationContext, UpdateProfileActivity::class.java)
//            startActivityForResult(intent,1)
            showPictureDialog()
//            CropImage.activity()
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .setActivityTitle("My Crop")
//                .setCropShape(CropImageView.CropShape.OVAL)
//                .setCropMenuCropButtonTitle("Done")
//                .setRequestedSize(400, 400)
//                .setCropMenuCropButtonIcon(R.drawable.ic_launcher_foreground)
//                .start(this)

//            CropImage.activity()
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .setActivityTitle("Profile Icon")
//                .setCropShape(CropImageView.CropShape.OVAL)
//                .setCropMenuCropButtonTitle("Done")
//                .setFixAspectRatio(false)
//                .setMultiTouchEnabled(true)
//                .setMinCropWindowSize(255, 255)
//                .setAspectRatio(255, 255)
//                .setRequestedSize(255, 255)
//                .setCropMenuCropButtonIcon(R.drawable.ic_tick)
//                .start(this)
        }

        profileUsername = EndPoints.profileUrl

        profilesummary_add.setOnClickListener {
            val intent = Intent(applicationContext, ProfileSummaryEdit::class.java)
            intent.putExtra("summaryData", model.profile_summary!!)
            intent.putExtra("sumId", model.id)
            startActivityForResult(intent, 1)
        }

        summary_edit.setOnClickListener{
            val intent = Intent(applicationContext, ProfileSummaryEdit::class.java)
            intent.putExtra("summaryData", model.profile_summary!!)
            intent.putExtra("sumId", model.id)
            startActivityForResult(intent, 1)
        }

        add_milestones_layout.setOnClickListener {
            openDialogSheetMilestones()
        }

        add_certificates_layout.setOnClickListener {
            val intent = Intent(applicationContext, CertificationEdit::class.java)
            startActivityForResult(intent, 1)
        }

        add_awards_layout.setOnClickListener {
            val intent = Intent(applicationContext, AwardsEdit::class.java)
            startActivityForResult(intent, 1)
        }

        add_workexperience_layout.setOnClickListener {
            val intent = Intent(applicationContext, WorkingEdit::class.java)
            startActivityForResult(intent, 1)
        }

        add_lifeexperience_layout.setOnClickListener {
            val intent = Intent(applicationContext, LifeExperienceEdit::class.java)
            startActivityForResult(intent, 1)
        }

        add_education_layout.setOnClickListener {
            val intent = Intent(applicationContext, EducationEdit::class.java)
            startActivityForResult(intent, 1)
        }

        degree_val= findViewById(R.id.degree) as TextView
        degreeValue_val= findViewById(R.id.degreeValue) as TextView
        institution_val= findViewById(R.id.instittion) as TextView
        institutionValue_val= findViewById(R.id.institutionValue) as TextView
        caption_val = findViewById(R.id.caption) as TextView
        captionValue_val= findViewById(R.id.captionValue) as TextView
        previous_val= findViewById(R.id.previous) as TextView
        previousValue_val= findViewById(R.id.previousValue) as TextView
        company_val= findViewById(R.id.company) as TextView
        companyValue_val= findViewById(R.id.companyValue) as TextView
        location_val= findViewById(R.id.location) as TextView
        locationValue_val =findViewById(R.id.locationValue) as TextView
        designation_val= findViewById(R.id.designation) as TextView
        desigationValue_val =findViewById(R.id.designationValue) as TextView

        loadProfileData()

        milestones.setTextColor(resources.getColor(R.color.green))
        workexperience.setTextColor(resources.getColor(R.color.black))
        liefexoerience.setTextColor(resources.getColor(R.color.black))
        skills.setTextColor(resources.getColor(R.color.black))
        education.setTextColor(resources.getColor(R.color.black))
        certifications.setTextColor(resources.getColor(R.color.black))
        groups.setTextColor(resources.getColor(R.color.black))
        awards_recognitions.setTextColor(resources.getColor(R.color.black))
        add_certificates_layout.visibility = View.GONE
        view_certificate_layout.visibility = View.GONE
        add_awards_layout.visibility = View.GONE
        view_awards_layout.visibility = View.GONE
        add_groups_layout.visibility = View.VISIBLE
        view_groups_layout.visibility = View.VISIBLE
        add_skills_layout.visibility = View.VISIBLE
        view_skills_layout.visibility = View.VISIBLE
        add_milestones_layout.visibility = View.VISIBLE


        milestones.setOnClickListener {

            click = 0
            milestones.setTextColor(resources.getColor(R.color.green))
            workexperience.setTextColor(resources.getColor(R.color.black))
            liefexoerience.setTextColor(resources.getColor(R.color.black))
            skills.setTextColor(resources.getColor(R.color.black))
            education.setTextColor(resources.getColor(R.color.black))
            certifications.setTextColor(resources.getColor(R.color.black))
            groups.setTextColor(resources.getColor(R.color.black))
            awards_recognitions.setTextColor(resources.getColor(R.color.black))
            add_certificates_layout.visibility = View.GONE
            view_certificate_layout.visibility = View.GONE
            add_awards_layout.visibility = View.GONE
            view_awards_layout.visibility = View.GONE
            profilesummary_layout.visibility = View.VISIBLE
            add_milestones_layout.visibility = View.VISIBLE
            view_milestone_layout.visibility = View.VISIBLE
            add_workexperience_layout.visibility = View.GONE
            view_workexperience_layout.visibility = View.GONE
            add_lifeexperience_layout.visibility = View.GONE
            view_lifeexperience_layout.visibility = View.GONE
            add_education_layout.visibility = View.GONE
            view_education_layout.visibility = View.GONE
            add_groups_layout.visibility = View.VISIBLE
            view_groups_layout.visibility = View.VISIBLE
            add_skills_layout.visibility = View.VISIBLE
            view_skills_layout.visibility = View.VISIBLE
            loadProfileData()
        }
        workexperience.setOnClickListener {
            click =1
            milestones.setTextColor(resources.getColor(R.color.black))
            workexperience.setTextColor(resources.getColor(R.color.green))
            liefexoerience.setTextColor(resources.getColor(R.color.black))
            skills.setTextColor(resources.getColor(R.color.black))
            education.setTextColor(resources.getColor(R.color.black))
            certifications.setTextColor(resources.getColor(R.color.black))
            groups.setTextColor(resources.getColor(R.color.black))
            awards_recognitions.setTextColor(resources.getColor(R.color.black))
            add_certificates_layout.visibility = View.GONE
            view_certificate_layout.visibility = View.GONE
            add_awards_layout.visibility = View.GONE
            view_awards_layout.visibility = View.GONE
            profilesummary_layout.visibility = View.GONE
            add_milestones_layout.visibility = View.GONE
            view_milestone_layout.visibility = View.GONE
            add_lifeexperience_layout.visibility = View.GONE
            view_lifeexperience_layout.visibility = View.GONE
            add_workexperience_layout.visibility = View.VISIBLE
            view_workexperience_layout.visibility = View.VISIBLE
            add_education_layout.visibility = View.GONE
            view_education_layout.visibility = View.GONE
            add_groups_layout.visibility = View.GONE
            view_groups_layout.visibility = View.GONE
            add_skills_layout.visibility = View.GONE
            view_skills_layout.visibility = View.GONE
            mRecyclerViewCetificates = findViewById(R.id.recycler_view_workexperience)
            var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
            mRecyclerViewCetificates!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
            mAdapterCertificates = WorkingAdapter(listOfWorkingData)
            mRecyclerViewCetificates!!.adapter = mAdapterCertificates
            loadWorkExperienceData()
        }
        liefexoerience.setOnClickListener {

            click=2
            milestones.setTextColor(resources.getColor(R.color.black))
            workexperience.setTextColor(resources.getColor(R.color.black))
            liefexoerience.setTextColor(resources.getColor(R.color.green))
            skills.setTextColor(resources.getColor(R.color.black))
            education.setTextColor(resources.getColor(R.color.black))
            certifications.setTextColor(resources.getColor(R.color.black))
            groups.setTextColor(resources.getColor(R.color.black))
            awards_recognitions.setTextColor(resources.getColor(R.color.black))
            add_certificates_layout.visibility = View.GONE
            view_certificate_layout.visibility = View.GONE
            add_awards_layout.visibility = View.GONE
            view_awards_layout.visibility = View.GONE
            profilesummary_layout.visibility = View.GONE
            add_milestones_layout.visibility = View.GONE
            view_milestone_layout.visibility = View.GONE
            add_workexperience_layout.visibility = View.GONE
            view_workexperience_layout.visibility = View.GONE
            add_lifeexperience_layout.visibility = View.VISIBLE
            view_lifeexperience_layout.visibility = View.VISIBLE
            add_education_layout.visibility = View.GONE
            view_education_layout.visibility = View.GONE
            add_groups_layout.visibility = View.GONE
            view_groups_layout.visibility = View.GONE
            add_skills_layout.visibility = View.GONE
            view_skills_layout.visibility = View.GONE
            mRecyclerViewCetificates = findViewById(R.id.recycler_view_lifeexperience)
            var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
            mRecyclerViewCetificates!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
            mAdapterCertificates = LifeExperienceAdapter(listOfLifeData)
            mRecyclerViewCetificates!!.adapter = mAdapterCertificates
            loadLifeExperienceData()

        }
        skills.setOnClickListener {

            click=3
            milestones.setTextColor(resources.getColor(R.color.black))
            workexperience.setTextColor(resources.getColor(R.color.black))
            liefexoerience.setTextColor(resources.getColor(R.color.black))
            skills.setTextColor(resources.getColor(R.color.green))
            education.setTextColor(resources.getColor(R.color.black))
            certifications.setTextColor(resources.getColor(R.color.black))
            groups.setTextColor(resources.getColor(R.color.black))
            awards_recognitions.setTextColor(resources.getColor(R.color.black))
            add_certificates_layout.visibility = View.GONE
            view_certificate_layout.visibility = View.GONE
            add_awards_layout.visibility = View.GONE
            view_awards_layout.visibility = View.GONE
            profilesummary_layout.visibility = View.GONE
            add_milestones_layout.visibility = View.GONE
            view_milestone_layout.visibility = View.GONE
            add_workexperience_layout.visibility = View.GONE
            view_workexperience_layout.visibility = View.GONE
            add_lifeexperience_layout.visibility = View.GONE
            view_lifeexperience_layout.visibility = View.GONE

            add_skills_layout.visibility = View.VISIBLE
            view_skills_layout.visibility = View.VISIBLE
            loadProfileData()
            loadSkillData()
            add_education_layout.visibility = View.GONE
            view_education_layout.visibility = View.GONE
            add_groups_layout.visibility = View.GONE
            view_groups_layout.visibility = View.GONE
        }
        education.setOnClickListener {
            click=4
            milestones.setTextColor(resources.getColor(R.color.black))
            workexperience.setTextColor(resources.getColor(R.color.black))
            liefexoerience.setTextColor(resources.getColor(R.color.black))
            skills.setTextColor(resources.getColor(R.color.black))
            education.setTextColor(resources.getColor(R.color.green))
            certifications.setTextColor(resources.getColor(R.color.black))
            groups.setTextColor(resources.getColor(R.color.black))
            awards_recognitions.setTextColor(resources.getColor(R.color.black))
            add_certificates_layout.visibility = View.GONE
            view_certificate_layout.visibility = View.GONE
            add_awards_layout.visibility = View.GONE
            view_awards_layout.visibility = View.GONE
            profilesummary_layout.visibility = View.GONE
            add_milestones_layout.visibility = View.GONE
            view_milestone_layout.visibility = View.GONE
            add_workexperience_layout.visibility = View.GONE
            view_workexperience_layout.visibility = View.GONE
            add_lifeexperience_layout.visibility = View.GONE
            view_lifeexperience_layout.visibility = View.GONE

            add_skills_layout.visibility = View.GONE
            view_skills_layout.visibility = View.GONE
            add_education_layout.visibility = View.VISIBLE
            view_education_layout.visibility = View.VISIBLE
            add_groups_layout.visibility = View.GONE
            view_groups_layout.visibility = View.GONE
            mRecyclerViewCetificates = findViewById(R.id.recycler_view_education)
            var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
            mRecyclerViewCetificates!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
            mAdapterCertificates = EducationAdapter(listOfEducationData)
            mRecyclerViewCetificates!!.adapter = mAdapterCertificates
            loadEducationData()

        }
        certifications.setOnClickListener {
            click=5
            milestones.setTextColor(resources.getColor(R.color.black))
            workexperience.setTextColor(resources.getColor(R.color.black))
            liefexoerience.setTextColor(resources.getColor(R.color.black))
            skills.setTextColor(resources.getColor(R.color.black))
            education.setTextColor(resources.getColor(R.color.black))
            certifications.setTextColor(resources.getColor(R.color.green))
            groups.setTextColor(resources.getColor(R.color.black))
            awards_recognitions.setTextColor(resources.getColor(R.color.black))
            add_certificates_layout.visibility = View.VISIBLE
            view_certificate_layout.visibility = View.VISIBLE
            add_awards_layout.visibility = View.GONE
            view_awards_layout.visibility = View.GONE
            profilesummary_layout.visibility = View.GONE
            add_milestones_layout.visibility = View.GONE
            view_milestone_layout.visibility = View.GONE
            add_workexperience_layout.visibility = View.GONE
            view_workexperience_layout.visibility = View.GONE
            add_lifeexperience_layout.visibility = View.GONE
            view_lifeexperience_layout.visibility = View.GONE
            add_education_layout.visibility = View.GONE
            view_education_layout.visibility = View.GONE
            add_groups_layout.visibility = View.GONE
            view_groups_layout.visibility = View.GONE
            add_skills_layout.visibility = View.GONE
            view_skills_layout.visibility = View.GONE
            mRecyclerViewCetificates = findViewById(R.id.recycler_view_certificates)
            var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
            mRecyclerViewCetificates!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
            mAdapterCertificates = CertificatesAdapter(listOfCertificateData)
            mRecyclerViewCetificates!!.adapter = mAdapterCertificates
            loadCertificateData()

        }
        groups.setOnClickListener {
            click=6
            milestones.setTextColor(resources.getColor(R.color.black))
            workexperience.setTextColor(resources.getColor(R.color.black))
            liefexoerience.setTextColor(resources.getColor(R.color.black))
            skills.setTextColor(resources.getColor(R.color.black))
            education.setTextColor(resources.getColor(R.color.black))
            certifications.setTextColor(resources.getColor(R.color.black))
            groups.setTextColor(resources.getColor(R.color.green))
            awards_recognitions.setTextColor(resources.getColor(R.color.black))
            add_certificates_layout.visibility = View.GONE
            view_certificate_layout.visibility = View.GONE
            add_awards_layout.visibility = View.GONE
            view_awards_layout.visibility = View.GONE
            profilesummary_layout.visibility = View.GONE
            add_milestones_layout.visibility = View.GONE
            view_milestone_layout.visibility = View.GONE
            add_workexperience_layout.visibility = View.GONE
            view_workexperience_layout.visibility = View.GONE
            add_lifeexperience_layout.visibility = View.GONE
            view_lifeexperience_layout.visibility = View.GONE
            add_education_layout.visibility = View.GONE
            view_education_layout.visibility = View.GONE
            add_groups_layout.visibility = View.VISIBLE
            view_groups_layout.visibility = View.VISIBLE
            add_skills_layout.visibility = View.GONE
            view_skills_layout.visibility = View.GONE
            loadMyGroupData("1")
        }
        awards_recognitions.setOnClickListener {
            click=7
            milestones.setTextColor(resources.getColor(R.color.black))
            workexperience.setTextColor(resources.getColor(R.color.black))
            liefexoerience.setTextColor(resources.getColor(R.color.black))
            skills.setTextColor(resources.getColor(R.color.black))
            education.setTextColor(resources.getColor(R.color.black))
            certifications.setTextColor(resources.getColor(R.color.black))
            groups.setTextColor(resources.getColor(R.color.black))
            awards_recognitions.setTextColor(resources.getColor(R.color.green))
            add_certificates_layout.visibility = View.GONE
            view_certificate_layout.visibility = View.GONE
            add_awards_layout.visibility = View.VISIBLE
            view_awards_layout.visibility = View.VISIBLE
            profilesummary_layout.visibility = View.GONE
            add_milestones_layout.visibility = View.GONE
            view_milestone_layout.visibility = View.GONE
            add_workexperience_layout.visibility = View.GONE
            view_workexperience_layout.visibility = View.GONE
            add_lifeexperience_layout.visibility = View.GONE
            view_lifeexperience_layout.visibility = View.GONE
            add_education_layout.visibility = View.GONE
            view_education_layout.visibility = View.GONE
            add_groups_layout.visibility = View.GONE
            view_groups_layout.visibility = View.GONE
            add_skills_layout.visibility = View.GONE
            view_skills_layout.visibility = View.GONE
            mRecyclerViewCetificates = findViewById(R.id.recycler_view_recognitions)
            var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
            mRecyclerViewCetificates!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
            mAdapterCertificates = RecognitiosAdapter(listOfRecognitionData)
            mRecyclerViewCetificates!!.adapter = mAdapterCertificates
            loadRecognitionData()
        }

        my_swipeRefresh_Layout.setOnRefreshListener {
            if (click==0)
                loadProfileData()
            else if(click==1)
                loadWorkExperienceData()
            else if (click==2)
                loadLifeExperienceData()
            else if (click==3) {
                loadProfileData()
                loadSkillData()
            }
            else if (click==4)
                loadEducationData()
            else if (click==5)
                loadCertificateData()
            else if (click==6)
                loadMyGroupData("1")
            else if (click==7)
                loadRecognitionData()
        }
    }

    fun openNetworkDialog(){

//        runOnUiThread {
//            Toast.makeText(ProfileView, "Network not available inside Activity", Toast.LENGTH_LONG).show()
//        }
    }

    private fun showPictureDialog() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setActivityTitle("Profile Icon")
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .setCropMenuCropButtonTitle("Done")
            .setMultiTouchEnabled(true)
            .setMinCropWindowSize(538, 320)
            .setRequestedSize(538, 320)
            .setFixAspectRatio(false)
            .setAspectRatio(538, 320)
            .setCropMenuCropButtonIcon(R.drawable.ic_tick)
            .start(this)

//            .setFixAspectRatio(false)
//            .setMultiTouchEnabled(true)
//            .setMinCropWindowSize(255, 255)
//            .setAspectRatio(255, 255)
//            .setRequestedSize(255, 255)
//            .setCropMenuCropButtonIcon(R.drawable.ic_tick)
//            .start(this)

//        CropImage.activity()
//            .setGuidelines(CropImageView.Guidelines.ON)
//            .setActivityTitle("Education")
//            .setCropShape(CropImageView.CropShape.RECTANGLE)
//            .setCropMenuCropButtonTitle("Done")
//            .setMultiTouchEnabled(true)
//            .setMinCropWindowSize(538,320)
//            .setRequestedSize(538, 320)
//            .setFixAspectRatio(false)
//            .setAspectRatio(538,320)
//            .setCropMenuCropButtonIcon(R.drawable.ic_tick)
//            .start(this)
    }

    fun registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(
                mNetworkReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            );
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(
                mNetworkReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            );
        }
    }

//     fun unregisterNetworkChanges() {
//        try {
//            unregisterReceiver(mNetworkReceiver);
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//    }

//    override fun onStart() {
//        super.onStart()
//        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
//    }
//
//    override fun onStop() {
//        super.onStop()
//        unregisterReceiver(broadcastReceiver)
//    }
//
//    private fun disconnected() {
//        header_layout.visibility = View.INVISIBLE
//        textView.visibility = View.VISIBLE
//    }
//
//    private fun connected() {
//        header_layout.visibility = View.VISIBLE
//        textView.visibility = View.INVISIBLE
//        onCreate(Bundle())
//    }



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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        loadProfileData()
//        Toast.makeText(applicationContext,"Inside activityresult"+ click, Toast.LENGTH_LONG).show()

        if (resultCode == 1) {

            if (click==0)
                loadProfileData()
            else if(click==1)
                loadWorkExperienceData()
            else if (click==2)
                loadLifeExperienceData()
            else if (click==3) {
                loadProfileData()
                loadSkillData()
            }
            else if (click==4)
                loadEducationData()
            else if (click==5)
                loadCertificateData()
            else if (click==6)
                loadMyGroupData("1")
            else if (click==7)
                loadRecognitionData()
        }

        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === Activity.RESULT_OK) {
//                (findViewById(R.id.circleView) as ImageView).setImageURI(result.uri)
                Toast.makeText(this, "Cropping successful", Toast.LENGTH_LONG).show()
//                Picasso.with(applicationContext)
//                    .load(result.uri)
//                    .centerCrop().resize(150,150)
//                    .placeholder(R.drawable.ic_launcher_foreground)
//                    .into(imageprofile)
                val contentURI = result.uri
                val f = File(contentURI.toString())
                profilePicturePathUpdate = f.getName()//+".png"
                Log.d("TAGG", picturepath.toString())
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
//                circleView!!.setImageBitmap(bitmap)
                // imageprofile!!.setPadding(0,0,0,0)
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                val b = baos.toByteArray()
                profileImageEncodedUpdate = Base64.encodeToString(b, Base64.DEFAULT)

                submitProfileDetails()

            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.error, Toast.LENGTH_LONG).show()
            }
        }

    }

    fun loadNotificationbubble(){


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

                    if (jsonarray.has("new_notification")) {
                        Log.d("TAGG", "bubble:" + jsonarray.getInt("new_notification"))
                        cart_badge.setText(jsonarray.getInt("new_notification").toString())
                    } else {
                        cart_badge.setText("0")
                    }
                } else {
                    ToastHelper.makeToast(applicationContext, "message")
                }
            }

            override fun onFailure(call: Call<NotificationBubbleResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
            }
        })
    }

    fun loadProfileData() {

        if(AppStatus.getInstance(this).isOnline()){
            /** Do your network operation **/
            listOfProfiledata.clear()
            list.clear()

            retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

            val call = retrofitInterface!!.GetProfileDetails(
                profileUsername,
                EndPoints.CLIENT_ID,
                "Bearer " + EndPoints.ACCESS_TOKEN
            )

            Logger.d("URL", "" + "HI")
            call.enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {
                    Logger.d("URL", "" + response + EndPoints.CLIENT_ID)
                    Logger.d("CODE", response.code().toString() + "")
                    Logger.d("MESSAGE", response.message() + "")
                    Logger.d("RESPONSE", "" + Gson().toJson(response))

                    val gson = GsonBuilder().serializeNulls().create()
                    var str_response = gson.toJson(response)
                    Logger.d("RESPONSE", "" + str_response)
                    //var str_response = Gson().toJson(response)
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
                    var jsonarray: JSONObject = jsonObject1.getJSONObject("body")

                    if (response.isSuccessful && response.body()!!.responseCode != 11013) {
//                    for (i in 0 until response.body()!!.body!!.size) {
                        var json_objectdetail: JSONObject = jsonarray
                        model = ProfileModel();
                        model.title = json_objectdetail.getString("title")
                        model.caption = json_objectdetail.getString("caption")
                        model.organization = json_objectdetail.getString("organization")
                        model.description = json_objectdetail.getString("description")
                        model.location = json_objectdetail.getString("location")
                        model.profile_summary = json_objectdetail.getString("profile_summary")
                        model.stage_type = json_objectdetail.getString("stage_type")
                        model.id = json_objectdetail.getInt("id")
                        model.profile_url = json_objectdetail.getString("profile_url")
                        model.my_profile = json_objectdetail.getString("my_profile")
                        model.already_following = json_objectdetail.getString("already_following")
                        model.username = json_objectdetail.getString("username")
                        model.profile_percentage = json_objectdetail.getString("profile_percentage")
                        Log.d("TAGG", "PERS" + json_objectdetail.getString("profile_percentage"))
                        val first = model.profile_percentage!! + "%"
                        val next = "<font color='#99CA3B'>"+model.profile_percentage!! + "%</font>"+" Complete"
                        perc_text.setText(Html.fromHtml( next))
                        group_icon.setValue(model.profile_percentage!!.toFloat())
                       // perc_text.setText(json_objectdetail.getString("profile_percentage") + "% Complete")
                        model.profile_image = json_objectdetail.getString("profile_image")
                        model.followers = json_objectdetail.getString("followers")
                        model.following = json_objectdetail.getString("following")
                        model.updates = UpdatesModel()
                        profileUrl = json_objectdetail.getString("profile_url")

                        var jsonarrayMile: JSONArray =
                            json_objectdetail.getJSONArray("timeline_list")
                        for (j in 0 until response.body()!!.body!!.timeline_list!!.size) {
                            var json_objectdetail: JSONObject = jsonarrayMile.getJSONObject(j)
                            var modelTime: TimeListModel = TimeListModel();
                            if (json_objectdetail.getString("type").equals("working")) {
                                Log.d(
                                    "TAGG",
                                    "TIMELINE" + json_objectdetail.getString("company_name")
                                )
                                var json_objectskills: JSONArray =
                                    json_objectdetail.getJSONArray("skills")
                                var skilldata: ArrayList<String> = ArrayList()
                                for (k in 0 until response.body()!!.body!!.timeline_list!![j].skills!!.size) {
                                    skilldata.add(json_objectskills.getString(k))
                                }
                                modelTime = TimeListModel(
                                    json_objectdetail.getString("company_name"),
                                    json_objectdetail.getString("start_date"),
                                    skilldata,
                                    json_objectdetail.getInt("id"),
                                    json_objectdetail.getString("location"),
                                    json_objectdetail.getString("designation"),
                                    json_objectdetail.getString("duration"),
                                    json_objectdetail.getString("end_date"),
                                    json_objectdetail.getString("current_company"),
                                    json_objectdetail.getString("type"),
                                    json_objectdetail.getString("description"),
                                    json_objectdetail.getString("image_url")
                                )
                            } else if (json_objectdetail.getString("type").equals("recognition")) {
                                modelTime = TimeListModel(
                                    json_objectdetail.getString("organization"),
                                    json_objectdetail.getString("start_date"),
                                    json_objectdetail.getInt("id"),
                                    json_objectdetail.getString("type"),
                                    json_objectdetail.getString("description"),
                                    json_objectdetail.getString("title"),
                                    json_objectdetail.getString("image_url")
                                )
                            } else if (json_objectdetail.getString("type")
                                    .equals("certification")
                            ) {
                                var json_objectskills: JSONArray =
                                    json_objectdetail.getJSONArray("skills")
                                var skilldata: ArrayList<String> = ArrayList()
                                for (k in 0 until response.body()!!.body!!.timeline_list!![j].skills!!.size) {
                                    skilldata.add(json_objectskills.getString(k))
                                }
                                modelTime = TimeListModel(
                                    json_objectdetail.getString("organization"),
                                    json_objectdetail.getString("start_date"),
                                    json_objectdetail.getString("end_date"),
                                    skilldata,
                                    json_objectdetail.getInt("id"),
                                    json_objectdetail.getString("expires"),
                                    json_objectdetail.getString("name"),
                                    json_objectdetail.getString("duration"),
                                    json_objectdetail.getString("type"),
                                    json_objectdetail.getString("description"),
                                    json_objectdetail.getString("image_url"),
                                    json_objectdetail.getString("expires_on")
                                )
                            } else if (json_objectdetail.getString("type")
                                    .equals("life_experience")
                            ) {
                                var json_objectskills: JSONArray =
                                    json_objectdetail.getJSONArray("life_experience")
                                var life_experience: ArrayList<String> = ArrayList()
                                for (k in 0 until response.body()!!.body!!.timeline_list!![j].life_experience!!.size) {
                                    life_experience.add(json_objectskills.getString(k))
                                }
                                var json_objectskills1: JSONArray =
                                    json_objectdetail.getJSONArray("skills")
                                var skilldata: ArrayList<String> = ArrayList()
                                for (k in 0 until response.body()!!.body!!.timeline_list!![j].skills!!.size) {
                                    skilldata.add(json_objectskills1.getString(k))
                                }
                                modelTime = TimeListModel(
                                    json_objectdetail.getInt("id"),
                                    json_objectdetail.getString("location"),
                                    json_objectdetail.getString("duration"),
                                    json_objectdetail.getString("start_date"),
                                    json_objectdetail.getString("end_date"),
                                    json_objectdetail.getString("type"),
                                    json_objectdetail.getString("description"),
                                    life_experience,
                                    json_objectdetail.getString("image_url"),
                                    json_objectdetail.getString("caption"),
                                    skilldata
                                )

                            } else if (json_objectdetail.getString("type").equals("education")) {
                                var json_objectskills: JSONArray =
                                    json_objectdetail.getJSONArray("skills")
                                var skilldata: ArrayList<String> = ArrayList()
                                for (k in 0 until response.body()!!.body!!.timeline_list!![j].skills!!.size) {
                                    skilldata.add(json_objectskills.getString(k))
                                }

                                modelTime = TimeListModel(
                                    json_objectdetail.getInt("id"),
                                    json_objectdetail.getString("duration"),
                                    json_objectdetail.getString("start_date"),
                                    json_objectdetail.getString("end_date"),
                                    skilldata,
                                    json_objectdetail.getString("image_url"),
                                    json_objectdetail.getString("type"),
                                    json_objectdetail.getString("location"),
                                    json_objectdetail.getString("degree"),
                                    json_objectdetail.getBoolean("ongoing"),
                                    json_objectdetail.getString("college"),
                                    json_objectdetail.getString("description")
                                )

                            }
                            list.add(modelTime)
                        }
                        Log.d("TAGG", "TIMELINE SIZE" + list.size)
                        model.timeline_list = list
                        var jsonarraySkill: JSONObject =
                            json_objectdetail.getJSONObject("working_life_experience_skills")
                        var jsonarrayWork: JSONArray = jsonarraySkill.getJSONArray("work_skills")
                        var jsonarrayLife: JSONArray =
                            jsonarraySkill.getJSONArray("life_experience_skills")
                        var jsonarrayEdu: JSONArray =
                            jsonarraySkill.getJSONArray("education_skills")
                        var workskilldata: ArrayList<String> = ArrayList()
                        for (k1 in 0 until response.body()!!.body!!.working_life_experience_skills!!.work_skills!!.size) {
                            workskilldata.add(jsonarrayWork.getString(k1))
                        }
                        var lifeskilldata: ArrayList<String> = ArrayList()
                        for (k1 in 0 until response.body()!!.body!!.working_life_experience_skills!!.life_experience_skills!!.size) {
                            lifeskilldata.add(jsonarrayLife.getString(k1))
                        }
                        var eduskilldata: ArrayList<String> = ArrayList()
                        for (k1 in 0 until response.body()!!.body!!.working_life_experience_skills!!.education_skills!!.size) {
                            eduskilldata.add(jsonarrayEdu.getString(k1))
                        }
                        var modelSkills: SkillsList = SkillsList();
                        modelSkills = SkillsList(workskilldata, lifeskilldata, eduskilldata)
                        model.skills_list = modelSkills

                        modelSkillsDisplay = modelSkills
                        listOfProfiledata.add(
                            ProfileModel(
                                model.title!!,
                                model.caption!!,
                                model.organization!!,
                                model.description!!,
                                model.location!!,
                                model.profile_summary!!,
                                model.stage_type!!,
                                model.id,
                                model.profile_url!!,
                                model.my_profile!!,
                                model.already_following!!,
                                model.username!!,
                                model.profile_percentage!!,
                                model.profile_image!!,
                                model.followers!!,
                                model.following!!,
                                model.updates!!,
                                model.timeline_list!!,
                                model.skills_list!!
                            )

                        )

                        name!!.text = model.username
                        Picasso.with(applicationContext)
                            .load(model.profile_image.toString())
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .into(imageprofile)
                        EndPoints.PROFILE_ICON = model.profile_image.toString()
                        val sharedPref: SharedPreferences = getSharedPreferences(
                            "mysettings",
                            Context.MODE_PRIVATE
                        );
                        val editor = sharedPref.edit()
                        editor.putString(PREF_ICON, EndPoints.PROFILE_ICON)
                        editor.commit()

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
//                    Picasso.with(applicationContext)
//                        .load(EndPoints.PROFILE_ICON)
//                        .placeholder(R.drawable.ic_launcher_foreground)
//                        .into(img_profilemain)
                        descr!!.text = model.description
                        followersId!!.text = model.followers
                        followingId!!.text = model.following
                        stageType!!.text = model.stage_type

                        if (model.profile_summary.toString().length > 0) {
                            profilesumm!!.text = model.profile_summary
                            profilesummary_add.visibility = View.GONE
                            profilesumm!!.visibility = View.VISIBLE
                        } else {
                            profilesummary_add.visibility = View.VISIBLE
                            profilesumm!!.visibility = View.GONE
                        }

                        if (model.stage_type!!.toLowerCase().equals("starter")) {
                            if (model.title!!.length > 0) {
                                degree_val!!.visibility = View.VISIBLE
                                degreeValue_val!!.visibility = View.VISIBLE
                                degreeValue_val!!.text = model.title
                            } else {
                                degree_val!!.visibility = View.GONE
                                degreeValue_val!!.visibility = View.GONE
                            }

                            if (model.organization!!.length > 0) {
                                institution_val!!.visibility = View.VISIBLE
                                institutionValue_val!!.visibility = View.VISIBLE
                                institutionValue_val!!.text = model.organization
                            } else {
                                institution_val!!.visibility = View.GONE
                                institutionValue_val!!.visibility = View.GONE
                            }

                            if (model.location!!.length > 0) {
                                location_val!!.visibility = View.VISIBLE
                                locationValue_val!!.visibility = View.VISIBLE
                                locationValue_val!!.text = model.location
                            } else {
                                location_val!!.visibility = View.GONE
                                locationValue_val!!.visibility = View.GONE
                            }

                            caption_val!!.visibility = View.GONE
                            captionValue_val!!.visibility = View.GONE
                            previous_val!!.visibility = View.GONE
                            previousValue_val!!.visibility = View.GONE
                            company_val!!.visibility = View.GONE
                            companyValue_val!!.visibility = View.GONE
                            designation!!.visibility = View.GONE
                            designationValue!!.visibility = View.GONE
                        } else if (model.stage_type!!.toLowerCase().equals("restarter")) {
                            degree_val!!.visibility = View.GONE
                            degreeValue_val!!.visibility = View.GONE
                            institution_val!!.visibility = View.GONE
                            institutionValue_val!!.visibility = View.GONE
                            designation!!.visibility = View.GONE
                            designationValue!!.visibility = View.GONE

                            if (model.caption!!.length > 0) {
                                caption_val!!.visibility = View.VISIBLE
                                captionValue_val!!.visibility = View.VISIBLE
                                captionValue_val!!.text = model.caption
                            } else {
                                caption_val!!.visibility = View.GONE
                                captionValue_val!!.visibility = View.GONE
                            }

                            if (model.title!!.length > 0) {
                                previous_val!!.visibility = View.VISIBLE
                                previousValue_val!!.visibility = View.VISIBLE
                                previousValue_val!!.text = model.title
                            } else {
                                previous_val!!.visibility = View.GONE
                                previousValue_val!!.visibility = View.GONE
                            }

                            if (model.organization!!.length > 0) {
                                company_val!!.visibility = View.VISIBLE
                                companyValue_val!!.visibility = View.VISIBLE
                                companyValue_val!!.text = model.organization
                            } else {
                                company_val!!.visibility = View.GONE
                                companyValue_val!!.visibility = View.GONE
                            }

                            if (model.location!!.length > 0) {
                                location_val!!.visibility = View.VISIBLE
                                locationValue_val!!.visibility = View.VISIBLE
                                locationValue_val!!.text = model.location
                            } else {
                                location_val!!.visibility = View.GONE
                                locationValue_val!!.visibility = View.GONE
                            }


                        } else if (model.stage_type!!.toLowerCase().equals("riser")) {
                            degree_val!!.visibility = View.GONE
                            degreeValue_val!!.visibility = View.GONE
                            institution_val!!.visibility = View.GONE
                            institutionValue_val!!.visibility = View.GONE
                            caption_val!!.visibility = View.GONE
                            captionValue_val!!.visibility = View.GONE
                            previous_val!!.visibility = View.GONE
                            previousValue_val!!.visibility = View.GONE
                            previousValue_val!!.text = ""

                            if (model.organization!!.length > 0) {
                                company_val!!.visibility = View.VISIBLE
                                companyValue_val!!.visibility = View.VISIBLE
                                companyValue_val!!.text = model.organization
                            } else {
                                company_val!!.visibility = View.GONE
                                companyValue_val!!.visibility = View.GONE
                            }

                            if (model.location!!.length > 0) {
                                location_val!!.visibility = View.VISIBLE
                                locationValue_val!!.visibility = View.VISIBLE
                                locationValue_val!!.text = model.location
                            } else {
                                location_val!!.visibility = View.GONE
                                locationValue_val!!.visibility = View.GONE
                            }

                            if (model.title!!.length > 0) {
                                designation!!.visibility = View.VISIBLE
                                designationValue!!.visibility = View.VISIBLE
                                desigationValue_val!!.text = model.title
                            } else {
                                designation!!.visibility = View.GONE
                                designationValue!!.visibility = View.GONE
                            }

                        }
                    } else {
//                    ToastHelper.makeToast(applicationContext, "message")
                    }

                    loadSkillData()
                    loadMyGroupData("1")
                    mAdapterTimeline!!.notifyDataSetChanged()

                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    Logger.d("TAGG", "FAILED : $t")
                    Toast.makeText(applicationContext, "No Posts to load!!", Toast.LENGTH_LONG)
                        .show()
//                loadprev.visibility = View.GONE
//                loadnext.visibility = View.GONE
                }
            })

            if (my_swipeRefresh_Layout.isRefreshing) {
                my_swipeRefresh_Layout.isRefreshing = false
            }
        }
        else {
            val builder = AlertDialog.Builder(this)
            //set title for alert dialog
            builder.setTitle("Alert!")
            //set message for alert dialog
            builder.setMessage("Internet connectivity not available, Try again later!")
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton("OK"){ dialogInterface, which ->
                //                Toast.makeText(applicationContext,"clicked yes",Toast.LENGTH_LONG).show()
                finish()
            }
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }

    public fun openEditProfile() {

        val dialog = Dialog(this, R.style.AppTheme)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog .setCancelable(true)
        dialog .setContentView(R.layout.layout_profileedit)
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        var selectedItemId:Int =0
        val lifestageVal = dialog .findViewById(R.id.edittext_leftstages) as EditText
        val fullnameVal = dialog .findViewById(R.id.edittext_fullname) as EditText
        val degreeVal = dialog .findViewById(R.id.edittext_degree) as EditText
        val universityVal = dialog .findViewById(R.id.edittext_university) as EditText
        val whycareerbreakVal = dialog .findViewById(R.id.edittext_whycareerbreak) as EditText
        val designationVal = dialog .findViewById(R.id.edittext_designation) as EditText
        val companyVal = dialog .findViewById(R.id.edittext_company) as EditText
        val locationVal = dialog .findViewById(R.id.edittext_location) as EditText
        val aboutmeVal = dialog .findViewById(R.id.edittext_aboutme) as EditText
        val saveTop = dialog .findViewById(R.id.saveTop) as TextView

        val lifestage = dialog .findViewById(R.id.lifestages) as LinearLayout
        val fullname = dialog .findViewById(R.id.fullname) as LinearLayout
        val degree = dialog .findViewById(R.id.degree) as LinearLayout
        val university = dialog .findViewById(R.id.university) as LinearLayout
        val whycareerbreak = dialog .findViewById(R.id.whyimoncareerbreak) as LinearLayout
        val designation = dialog .findViewById(R.id.designation) as LinearLayout
        val company = dialog .findViewById(R.id.company) as LinearLayout
        val location = dialog .findViewById(R.id.location) as LinearLayout
        val aboutme = dialog .findViewById(R.id.aboutme) as LinearLayout
        val savecontinue = dialog.findViewById(R.id.savecontinue) as Button
        val editprofile_back = dialog.findViewById(R.id.editprofile_back) as ImageView

        fullnameVal.setOnClickListener {
            Toast.makeText(
                dialog.context,
                dialog.context.resources.getString(R.string.title_name_not_editable),
                Toast.LENGTH_SHORT
            ).show()
        }

        editprofile_back.setOnClickListener {
            dialog.cancel()
        }

        val params = HashMap<String, String>()
        val spinnerlifestage  = dialog.findViewById(R.id.spinner1) as Spinner
        val languages = arrayOf("Riser", "Restarter", "Starter")
        //val adapter = ArrayAdapter(this, android.R.layout.select_dialog_singlechoice, languages)
        val adapter = ArrayAdapter<String>(
            this@ProfileView,
            android.R.layout.simple_spinner_item,
            languages
        )
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
                    selectedItemId =0
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
                    selectedItemId =1
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
                    selectedItemId =2
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
            if (lifestageVal.isFocusable){
                spinnerlifestage.performClick()
            }
            else {
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
            if (selectedItemId ==2){
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
            }
            else if (selectedItemId ==1){
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

            }
            else if (selectedItemId ==0){
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

        Log.d("TAGG", listOfProfiledata[0].stage_type!!.toLowerCase())

        if(listOfProfiledata[0].stage_type!!.toLowerCase().equals("starter")){
            lifestage.visibility=View.VISIBLE
            fullname.visibility=View.VISIBLE
            degree.visibility=View.VISIBLE
            university .visibility=View.VISIBLE
            whycareerbreak.visibility=View.GONE
            designation.visibility=View.GONE
            company .visibility=View.GONE
            location.visibility=View.VISIBLE
            aboutme.visibility=View.VISIBLE

            lifestageVal.setText(listOfProfiledata[0].stage_type!!.toLowerCase())
            fullnameVal.setText(listOfProfiledata[0].username)
            degreeVal.setText(listOfProfiledata[0].title)
            universityVal.setText(listOfProfiledata[0].organization)
            locationVal.setText(listOfProfiledata[0].location)
            aboutmeVal.setText(listOfProfiledata[0].description)
            spinnerlifestage.setSelection(2)
            selectedItemId =2


        }
        else if(listOfProfiledata[0].stage_type!!.toLowerCase().equals("restarter")){
            lifestage.visibility=View.VISIBLE
            fullname.visibility=View.VISIBLE
            degree.visibility=View.GONE
            university .visibility=View.GONE
            whycareerbreak.visibility=View.VISIBLE
            designation.visibility=View.VISIBLE
            company .visibility=View.VISIBLE
            location.visibility=View.VISIBLE
            aboutme.visibility=View.VISIBLE

            lifestageVal.setText(listOfProfiledata[0].stage_type!!.toLowerCase())
            fullnameVal.setText(listOfProfiledata[0].username)
            whycareerbreakVal.setText(listOfProfiledata[0].caption)
            designationVal.setText(listOfProfiledata[0].title)
            companyVal.setText(listOfProfiledata[0].organization)
            locationVal.setText(listOfProfiledata[0].location)
            aboutmeVal.setText(listOfProfiledata[0].description)
            spinnerlifestage.setSelection(1)
            selectedItemId =1
        }
        else if(listOfProfiledata[0].stage_type!!.toLowerCase().equals("riser")){
            lifestage.visibility=View.VISIBLE
            fullname.visibility=View.VISIBLE
            degree.visibility=View.GONE
            university .visibility=View.GONE
            whycareerbreak.visibility=View.GONE
            designation.visibility=View.VISIBLE
            company .visibility=View.VISIBLE
            location.visibility=View.VISIBLE
            aboutme.visibility=View.VISIBLE

            lifestageVal.setText(listOfProfiledata[0].stage_type!!.toLowerCase())
            fullnameVal.setText(listOfProfiledata[0].username)
            designationVal.setText(listOfProfiledata[0].title)
            companyVal.setText(listOfProfiledata[0].organization)
            locationVal.setText(listOfProfiledata[0].location)
            aboutmeVal.setText(listOfProfiledata[0].description)
            spinnerlifestage.setSelection(0)
            selectedItemId =0
        }

        var window : Window = dialog.getWindow()!!;
        var wlp: WindowManager.LayoutParams  = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setAttributes(wlp);
        dialog .show()


    }

    class OnSpinnerItemClicked : AdapterView.OnItemSelectedListener {

        override fun onItemSelected(
            parent: AdapterView<*>,
            view: View, pos: Int, id: Long
        ) {
            Log.d("TAGG", "OnClick")
//            Toast.makeText(
//                parent.context,
//                "Clicked : " + parent.getItemAtPosition(pos).toString(),
//                Toast.LENGTH_LONG
//            ).show()
        }
        override fun onNothingSelected(parent: AdapterView<*>) {
            // Do nothing.
        }
    }

    fun updateProfileData(params: HashMap<String, String>){

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
                        str_response.indexOf(
                            "{"
                        ), str_response.lastIndexOf("}") + 1
                    )
                )
                val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                // var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "Updated Successfully!!", Toast.LENGTH_LONG)
                        .show()
//                    for (i in 0 until response.body()!!.body!!.size) {
//                        var json_objectdetail:JSONObject=jsonarray.getJSONObject(i)
//                        model = CertificateModel();
//                        model.organization = json_objectdetail.getString("organization")
//                        model.start_date = json_objectdetail.getString("start_date")
//                        model.user_id = json_objectdetail.getString("user_id")
//                        model.id = json_objectdetail.getInt("id")
//                        model.skills = ArrayList<String>()
//                        model.expires = json_objectdetail.getString("expires")
//                        model.name = json_objectdetail.getString("name")
//                        model.skills_id = java.util.ArrayList<Int>()
//                        model.description = json_objectdetail.getString("description")
//                        model.image_url = json_objectdetail.getString("image_url")
//                        model.expires_on = ""
//                        listOfCertificateData.add(
//                            CertificateModel(
//                                model.organization!!,
//                                model.start_date!!,
//                                model.user_id!!,
//                                model.id,
//                                model.skills!!,
//                                model.expires!!,
//                                model.name!!,
//                                model.skills_id!!,
//                                model.description!!,
//                                model.image_url!!,
//                                model.expires_on!!
//                            )
//                        )
//                    }
                } else {
//                    ToastHelper.makeToast(applicationContext, "message")
                }
                listOfProfiledata.clear()
                loadProfileData()
            }

            override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext, "Update Failed!!", Toast.LENGTH_LONG).show()

            }
        })
    }

    fun loadCertificateData(){
        mRecyclerViewCetificates = findViewById(R.id.recycler_view_certificates)
        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
        mRecyclerViewCetificates!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
        mAdapterCertificates = CertificatesAdapter(listOfCertificateData)
        mRecyclerViewCetificates!!.adapter = mAdapterCertificates
        listOfCertificateData.clear()

        var model: CertificateModel = CertificateModel();
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)


        val call = retrofitInterface!!.GetCertificateData(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN
        )

        Logger.d("URL", "" + "HI")
        call.enqueue(object : Callback<CertificateResponse> {
            override fun onResponse(
                call: Call<CertificateResponse>,
                response: Response<CertificateResponse>
            ) {
                Logger.d("URL", "" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
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
                var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                if (response.isSuccessful) {
                    for (i in 0 until response.body()!!.body!!.size) {
                        var json_objectdetail: JSONObject = jsonarray.getJSONObject(i)
                        model = CertificateModel();
                        model.organization = json_objectdetail.getString("organization")
                        model.start_date = json_objectdetail.getString("start_date")
                        model.user_id = json_objectdetail.getString("user_id")
                        model.id = json_objectdetail.getInt("id")
                        var json_objectskills: JSONArray = json_objectdetail.getJSONArray("skills")
                        var skilldata: ArrayList<String> = ArrayList()
                        for (k in 0 until response.body()!!.body!![i].skills!!.size) {
                            skilldata.add(json_objectskills.getString(k))
                        }
                        model.skills = skilldata
                        model.expires = json_objectdetail.getString("expires")
                        model.name = json_objectdetail.getString("name")
                        model.skills_id = java.util.ArrayList<Int>()
                        model.description = json_objectdetail.getString("description")
                        model.image_url = json_objectdetail.getString("image_url")
                        model.expires_on = json_objectdetail.getString("expires_on")
                        listOfCertificateData.add(
                            CertificateModel(
                                model.organization!!,
                                model.start_date!!,
                                model.user_id!!,
                                model.id,
                                model.skills!!,
                                model.expires!!,
                                model.name!!,
                                model.skills_id!!,
                                model.description!!,
                                model.image_url!!,
                                model.expires_on!!
                            )
                        )
                    }
                } else {
//                    ToastHelper.makeToast(applicationContext, "message")
                }
                mAdapterCertificates!!.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<CertificateResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext, "No Certificates to load!!", Toast.LENGTH_LONG)
                    .show()

            }
        })

        if(my_swipeRefresh_Layout.isRefreshing){
            my_swipeRefresh_Layout.isRefreshing = false
        }
    }

    fun loadRecognitionData(){
        mRecyclerViewCetificates = findViewById(R.id.recycler_view_recognitions)
        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
        mRecyclerViewCetificates!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
        mAdapterCertificates = RecognitiosAdapter(listOfRecognitionData)
        mRecyclerViewCetificates!!.adapter = mAdapterCertificates

        listOfRecognitionData.clear()

        var model: RecognitionModel = RecognitionModel();
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.GetRecognitionData(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN
        )

        Logger.d("URL", "" + "HI")
        call.enqueue(object : Callback<RecognitionResponse> {
            override fun onResponse(
                call: Call<RecognitionResponse>,
                response: Response<RecognitionResponse>
            ) {
                Logger.d("URL", "" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))

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
                var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                if (response.isSuccessful) {
                    for (i in 0 until response.body()!!.body!!.size) {
                        var json_objectdetail: JSONObject = jsonarray.getJSONObject(i)
                        model = RecognitionModel();
                        model.organization = json_objectdetail.getString("organization")
                        model.start_date = json_objectdetail.getString("start_date")
                        model.user_id = json_objectdetail.getString("user_id")
                        model.id = json_objectdetail.getInt("id")
                        model.name = json_objectdetail.getString("title")
                        model.description = json_objectdetail.getString("description")
                        model.image_url = json_objectdetail.getString("image_url")

                        listOfRecognitionData.add(
                            RecognitionModel(
                                model.organization!!,
                                model.start_date!!,
                                model.user_id!!,
                                model.id,
                                model.description!!,
                                model.name!!,
                                model.image_url!!
                            )
                        )
                    }
                } else {
//                    ToastHelper.makeToast(applicationContext, "message")
                }

                mAdapterCertificates!!.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<RecognitionResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext, "No Awards to load!!", Toast.LENGTH_LONG).show()

            }
        })

        if(my_swipeRefresh_Layout.isRefreshing){
            my_swipeRefresh_Layout.isRefreshing = false
        }
    }

    fun loadWorkExperienceData(){
        mRecyclerViewCetificates = findViewById(R.id.recycler_view_workexperience)
        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
        mRecyclerViewCetificates!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
        mAdapterCertificates = WorkingAdapter(listOfWorkingData)
        mRecyclerViewCetificates!!.adapter = mAdapterCertificates
        listOfWorkingData.clear()

        var model: WorkingModel = WorkingModel();
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.GetWorkExperienceData(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN
        )

        Logger.d("URL", "" + "HI")
        call.enqueue(object : Callback<ViewWorkingResponse> {
            override fun onResponse(
                call: Call<ViewWorkingResponse>,
                response: Response<ViewWorkingResponse>
            ) {
                Logger.d("URL", "" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))

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
                var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                if (response.isSuccessful) {
                    for (i in 0 until response.body()!!.body!!.size) {
                        var json_objectdetail: JSONObject = jsonarray.getJSONObject(i)
                        model = WorkingModel();
                        model.current_company = json_objectdetail.getBoolean("current_company")
                        model.start_date = json_objectdetail.getString("start_date")
                        model.user_id = json_objectdetail.getString("user_id")
                        model.id = json_objectdetail.getInt("id")
                        var json_objectskills: JSONArray = json_objectdetail.getJSONArray("skills")
                        var skilldata: ArrayList<String> = ArrayList()
                        for (k in 0 until response.body()!!.body!![i].skills!!.size) {
                            skilldata.add(json_objectskills.getString(k))
                        }
                        model.skills = skilldata
                        model.company_name = json_objectdetail.getString("company_name")
                        var json_objectskillsId: JSONArray =
                            json_objectdetail.getJSONArray("skills_id")
                        var skilldataId: ArrayList<Int> = ArrayList()
                        for (k in 0 until response.body()!!.body!![i].skills_id!!.size) {
                            skilldataId.add(json_objectskillsId.getInt(k))
                        }
                        model.skills_id = skilldataId
                        model.description = json_objectdetail.getString("description")
                        model.image_url = json_objectdetail.getString("image_url")
                        model.location = json_objectdetail.getString("location")
                        model.location_id = json_objectdetail.getInt("location_id")
                        model.end_date = json_objectdetail.getString("end_date")
                        model.designation = json_objectdetail.getString("designation")
                        listOfWorkingData.add(
                            WorkingModel(
                                model.current_company!!,
                                model.start_date!!,
                                model.user_id!!,
                                model.id,
                                model.skills!!,
                                model.company_name!!,
                                model.skills_id!!,
                                model.description!!,
                                model.image_url!!,
                                model.location!!,
                                model.location_id!!,
                                model.end_date!!,
                                model.designation!!
                            )
                        )
                    }
                } else {
//                    ToastHelper.makeToast(applicationContext, "message")
                }

                mAdapterCertificates!!.notifyDataSetChanged()

            }

            override fun onFailure(call: Call<ViewWorkingResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext, "No Working data to load!!", Toast.LENGTH_LONG)
                    .show()

            }
        })

        if(my_swipeRefresh_Layout.isRefreshing){
            my_swipeRefresh_Layout.isRefreshing = false
        }
    }

    fun loadLifeExperienceData(){

        mRecyclerViewCetificates = findViewById(R.id.recycler_view_lifeexperience)
        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
        mRecyclerViewCetificates!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
        mAdapterCertificates = LifeExperienceAdapter(listOfLifeData)
        mRecyclerViewCetificates!!.adapter = mAdapterCertificates

        listOfLifeData.clear()

        var model: LifeModel = LifeModel();
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.GetLifeExperienceData(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN
        )

        Logger.d("URL", "" + "HI")
        call.enqueue(object : Callback<ViewLifeResponse> {
            override fun onResponse(
                call: Call<ViewLifeResponse>,
                response: Response<ViewLifeResponse>
            ) {
                Logger.d("URL", "" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))

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
                var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                if (response.isSuccessful) {
                    for (i in 0 until response.body()!!.body!!.size) {
                        var json_objectdetail: JSONObject = jsonarray.getJSONObject(i)
                        model = LifeModel();
                        model.ongoing = json_objectdetail.getBoolean("ongoing")
                        model.start_date = json_objectdetail.getString("start_date")
                        model.user_id = json_objectdetail.getString("user_id")
                        model.duration = json_objectdetail.getString("duration")
                        model.id = json_objectdetail.getInt("id")
                        var json_objectskills: JSONArray = json_objectdetail.getJSONArray("skills")
                        var skilldata: ArrayList<String> = ArrayList()
                        for (k in 0 until response.body()!!.body!![i].skills!!.size) {
                            skilldata.add(json_objectskills.getString(k))
                        }
                        model.skills = skilldata
                        var json_objectskillsId: JSONArray =
                            json_objectdetail.getJSONArray("skills_id")
                        var skilldataId: ArrayList<Int> = ArrayList()
                        for (k in 0 until response.body()!!.body!![i].skills_id!!.size) {
                            skilldataId.add(json_objectskillsId.getInt(k))
                        }
                        model.skills_id = skilldataId
                        model.description = json_objectdetail.getString("description")
                        model.image_url = json_objectdetail.getString("image_url")
                        model.location = json_objectdetail.getString("location")
                        model.location_id = 0//json_objectdetail.getInt("location_id")
                        model.end_date = json_objectdetail.getString("end_date")
                        if (json_objectdetail.isNull("caption")) {
                            model.caption = "";
                        } else {
                            model.caption = json_objectdetail.getString("caption")
                        }

                        var json_objectlife: JSONArray =
                            json_objectdetail.getJSONArray("life_experience")
                        var lifedata: ArrayList<String> = ArrayList()
                        for (k in 0 until response.body()!!.body!![i].life_experience!!.size) {
                            lifedata.add(json_objectlife.getString(k))
                        }
                        model.life_experience = lifedata
                        var json_objectlifeId: JSONArray =
                            json_objectdetail.getJSONArray("life_experience_id")
                        var lifedataId: ArrayList<Int> = ArrayList()
                        for (k in 0 until response.body()!!.body!![i].life_experience_id!!.size) {
                            lifedataId.add(json_objectlifeId.getInt(k))
                        }
                        model.life_experience_id = lifedataId
                        listOfLifeData.add(
                            LifeModel(
                                model.ongoing!!,
                                model.start_date!!,
                                model.user_id!!,
                                model.id,
                                model.skills!!,
                                model.skills_id!!,
                                model.description!!,
                                model.image_url!!,
                                model.location!!,
                                model.location_id!!,
                                model.end_date!!,
                                model.caption!!,
                                model.life_experience!!,
                                model.life_experience_id!!,
                                model.duration!!
                            )
                        )
                    }
                } else {
//                    ToastHelper.makeToast(applicationContext, "message")
                }
                mAdapterCertificates!!.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<ViewLifeResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext, "No Life data to load!!", Toast.LENGTH_LONG)
                    .show()

            }
        })

        if(my_swipeRefresh_Layout.isRefreshing){
            my_swipeRefresh_Layout.isRefreshing = false
        }
    }

//    fun loadEducationData(){
//        listOfEducationData.clear()
//        mRecyclerViewCetificates = findViewById(R.id.recycler_view_education)
//        var model: EducationViewModel = EducationViewModel();
//        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
//
//        val call = retrofitInterface!!.GetEducationData(EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)
//        Logger.d("URL", "" + "HI")
//        call.enqueue(object : Callback<ViewEducationResponse> {
//            override fun onResponse(call: Call<ViewEducationResponse>, response: Response<ViewEducationResponse>) {
//                Logger.d("URL", "" + response+ EndPoints.CLIENT_ID)
//                Logger.d("CODE", response.code().toString() + "")
//                Logger.d("MESSAGE", response.message() + "")
//                Logger.d("RESPONSE", "" + Gson().toJson(response))
//
//                var str_response = Gson().toJson(response)
//                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
//                val jsonObject1 : JSONObject = jsonObject.getJSONObject("body")
//                val responseCode: Int = jsonObject1.getInt("response_code")
//                val message: String = jsonObject1.getString("message")
//                var jsonarray: JSONArray = jsonObject1.getJSONArray("body")
//
//                if(response.isSuccessful){
//                    for (i in 0 until response.body()!!.body!!.size) {
//                        var json_objectdetail:JSONObject=jsonarray.getJSONObject(i)
//                        model = EducationViewModel();
//                        model.ongoing = json_objectdetail.getBoolean("ongoing")
//                        model.start_date = json_objectdetail.getString("start_date")
//                        model.user_id = json_objectdetail.getString("user_id")
//                        model.id = json_objectdetail.getInt("id")
//                        var json_objectskills: JSONArray = json_objectdetail.getJSONArray("skills")
//                        var skilldata: ArrayList<String> = ArrayList()
//                        for (k in 0 until response.body()!!.body!![i].skills!!.size) {
//                            skilldata.add(json_objectskills.getString(k))
//                        }
//                        model.skills = skilldata
//                        var json_objectskillsId: JSONArray = json_objectdetail.getJSONArray("skills_id")
//                        var skilldataId: ArrayList<Int> = ArrayList()
//                        for (k in 0 until response.body()!!.body!![i].skills_id!!.size) {
//                            skilldataId.add(json_objectskillsId.getInt(k))
//                        }
//                        model.skills_id = skilldataId
//                        model.description = json_objectdetail.getString("description")
//                        model.image_url = json_objectdetail.getString("image_url")
//                        model.location = json_objectdetail.getString("location")
//                        model.location_id = json_objectdetail.getInt("location_id")
//                        model.end_date = ""//json_objectdetail.getString("end_date")
//                        model.college = json_objectdetail.getString("college")
//                        model.degree = json_objectdetail.getString("degree")
//                        model.college_id = json_objectdetail.getInt("college_id")
//                        model.degree_id = json_objectdetail.getInt("degree_id")
//
//                        listOfEducationData.add(
//                            EducationViewModel(
//                                model.ongoing!!,
//                                model.start_date!!,
//                                model.user_id!!,
//                                model.id,
//                                model.skills!!,
//                                model.skills_id!!,
//                                model.description!!,
//                                model.image_url!!,
//                                model.location!!,
//                                model.location_id!!,
//                                model.end_date!!,
//                                model.college!!,
//                                model.degree!!,
//                                model.college_id!!,
//                                model.degree_id!!
//                            )
//                        )
//                    }
//                }
//                else {
//                    ToastHelper.makeToast(applicationContext, "message")
//                }
//
//                var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
//                mRecyclerViewCetificates!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
//                mAdapterCertificates = EducationAdapter(listOfEducationData)
//                mRecyclerViewCetificates!!.adapter = mAdapterCertificates
//            }
//            override fun onFailure(call: Call<ViewEducationResponse>, t: Throwable) {
//                Logger.d("TAGG", "FAILED : $t")
//                Toast.makeText(applicationContext,"No Education data to load!!", Toast.LENGTH_LONG).show()
//
//            }
//        })
//    }


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


    fun loadEducationData(){
        mRecyclerViewCetificates = findViewById(R.id.recycler_view_education)
        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
        mRecyclerViewCetificates!!.layoutManager = mgroupsLayoutManager as RecyclerView.LayoutManager?
        mAdapterCertificates = EducationAdapter(listOfEducationData)
        mRecyclerViewCetificates!!.adapter = mAdapterCertificates

        listOfEducationData.clear()

        var model: EducationModel = EducationModel();
        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.GetEducationData(
            EndPoints.CLIENT_ID,
            "Bearer " + EndPoints.ACCESS_TOKEN
        )
        Logger.d("URL", "" + "HI")
        call.enqueue(object : Callback<ViewEducationResponse> {
            override fun onResponse(
                call: Call<ViewEducationResponse>,
                response: Response<ViewEducationResponse>
            ) {
                Logger.d("URL", "" + response + EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))

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
                var jsonarray: JSONArray = jsonObject1.getJSONArray("body")


                if (response.isSuccessful) {
                    for (i in 0 until response.body()!!.body!!.size) {
                        var json_objectdetail: JSONObject = jsonarray.getJSONObject(i)
                        model = EducationModel();
                        model.ongoing = json_objectdetail.getBoolean("ongoing")
                        model.start_date = json_objectdetail.getString("start_date")
                        model.user_id = json_objectdetail.getInt("user_id")
                        model.id = json_objectdetail.getInt("id")
                        var json_objectskills: JSONArray = json_objectdetail.getJSONArray("skills")
                        var skilldata: ArrayList<String> = ArrayList()
                        for (k in 0 until response.body()!!.body!![i].skills!!.size) {
                            skilldata.add(json_objectskills.getString(k))
                        }
                        model.skills = skilldata
                        var json_objectskillsId: JSONArray =
                            json_objectdetail.getJSONArray("skills_id")
                        var skilldataId: ArrayList<Int> = ArrayList()
                        for (k in 0 until response.body()!!.body!![i].skills_id!!.size) {
                            skilldataId.add(json_objectskillsId.getInt(k))
                        }
                        model.skills_id = skilldataId
                        model.description = json_objectdetail.getString("description")
                        model.image_url = json_objectdetail.getString("image_url")
                        model.location = json_objectdetail.getString("location")
                        model.location_id = 0//json_objectdetail.getInt("location_id")
                        model.end_date = json_objectdetail.getString("end_date")
                        model.college = json_objectdetail.getString("college")
                        model.degree = json_objectdetail.getString("degree")
                        model.college_id = json_objectdetail.getInt("college_id")
                        model.degree_id = json_objectdetail.getInt("degree_id")

                        listOfEducationData.add(
                            EducationModel(
                                model.college!!,
                                model.end_date!!,
                                model.skills!!,
                                model.college_id!!,
                                model.location!!,
                                model.image_url!!,
                                model.degree!!,
                                model.ongoing!!,
                                model.location_id!!,
                                model.description!!,
                                model.skills_id!!,
                                model.user_id!!,
                                model.start_date!!,
                                model.id,
                                model.degree_id!!
                            )
                        )
                    }
                } else {
//                    ToastHelper.makeToast(applicationContext, "message")
                }
                mAdapterCertificates!!.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<ViewEducationResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(applicationContext, "No Education data to load!!", Toast.LENGTH_LONG)
                    .show()

            }
        })

        if(my_swipeRefresh_Layout.isRefreshing){
            my_swipeRefresh_Layout.isRefreshing = false
        }
    }

    fun loadMyGroupData(pageno: String){

        val lp = add_groups_layout.getLayoutParams() as RelativeLayout.LayoutParams
        lp.addRule(RelativeLayout.BELOW, R.id.view_skills_layout)
        add_groups_layout.setLayoutParams(lp)

        var listOfMyGroupdata: ArrayList<GroupsView> = ArrayList()
        listOfMyGroupdata.clear()

        val params = java.util.HashMap<String, String>()

        params["page_no"] = pageno.toString()
        params["page_size"] = 8.toString()

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

//                        ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
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

                        }

                        var mgroupsLayoutManager = GridLayoutManager(applicationContext, 1)
                        mRecyclerViewCetificates!!.layoutManager =
                            mgroupsLayoutManager as RecyclerView.LayoutManager?
                        mAdapterCertificates = GroupsProfileAdapter(listOfMyGroupdata)
                        mRecyclerViewCetificates!!.adapter = mAdapterCertificates

                    } else {
                        ToastHelper.makeToast(applicationContext, "Invalid Request")
                        empty_view.visibility = View.VISIBLE
                    }
                } else {
                    Toast.makeText(applicationContext, "No data exists", Toast.LENGTH_LONG).show()
                    empty_view.visibility = View.VISIBLE
                }

            }

            override fun onFailure(call: Call<Featured_Group>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
                //Toast.makeText(applicationContext, "No Groups Exixts, Join a group today!", Toast.LENGTH_LONG).show()
                empty_view.visibility = View.VISIBLE
            }
        })

        if(my_swipeRefresh_Layout.isRefreshing){
            my_swipeRefresh_Layout.isRefreshing = false
        }
    }

    fun loadSkillData(){
        val lp = add_skills_layout.getLayoutParams() as RelativeLayout.LayoutParams
        lp.addRule(RelativeLayout.BELOW, R.id.view_milestone_layout)
        add_skills_layout.setLayoutParams(lp)

        we.visibility = View.GONE
        wetext.visibility = View.GONE
        lif.visibility = View.GONE
        liftext.visibility = View.GONE
        ed.visibility = View.GONE
        edtext.visibility = View.GONE

        Log.d("TAGG", modelSkillsDisplay.work_skills.toString())
        var work: String = ""
        val listwork = ArrayList<Chip>()
        if (!modelSkillsDisplay.work_skills.isNullOrEmpty()) {
            we.visibility = View.VISIBLE
            wetext.visibility = View.VISIBLE
        }
        if (!modelSkillsDisplay.life_experience_skills.isNullOrEmpty()) {
            lif.visibility = View.VISIBLE
            liftext.visibility = View.VISIBLE
        }
        if (!modelSkillsDisplay.education_skills.isNullOrEmpty()) {
            ed.visibility = View.VISIBLE
            edtext.visibility = View.VISIBLE
        }
        if (!modelSkillsDisplay.work_skills.isNullOrEmpty()) {
            for (k1 in 0 until modelSkillsDisplay.work_skills!!.size) {
                if (modelSkillsDisplay.work_skills!![k1].toString().equals("null")) {
                } else {
                    if (!isWhitespace(modelSkillsDisplay.work_skills!![k1].trim())) {
                        work = modelSkillsDisplay.work_skills!![k1].toString() + ", " + work
                        listwork.add(
                            com.jobsforher.models.Tag(
                                modelSkillsDisplay.work_skills!![k1].toString().trim()
                            )
                        )
                    }
                }
            }
        }
        var life: String = ""
        val listlife = ArrayList<Chip>()
        if (!modelSkillsDisplay.life_experience_skills.isNullOrEmpty()) {
            for (k1 in 0 until modelSkillsDisplay.life_experience_skills!!.size) {
                if (modelSkillsDisplay.life_experience_skills!![k1].toString().equals("null")) {
                } else {
                    if (!isWhitespace(modelSkillsDisplay.life_experience_skills!![k1].trim())) {
                        life =
                            modelSkillsDisplay.life_experience_skills!![k1].toString() + ", " + life
                        listlife.add(
                            com.jobsforher.models.Tag(
                                modelSkillsDisplay.life_experience_skills!![k1].toString().trim()
                            )
                        )
                    }
                }
            }
        }
        var edu: String = ""
        val listedu = ArrayList<Chip>()
        if (!modelSkillsDisplay.education_skills.isNullOrEmpty()) {
            for (k1 in 0 until modelSkillsDisplay.education_skills!!.size) {
                if (modelSkillsDisplay.education_skills!![k1].toString().equals("null")) {
                } else {
                    if (!isWhitespace(modelSkillsDisplay.education_skills!![k1].trim())) {
                        edu = modelSkillsDisplay.education_skills!![k1].toString() + ", " + edu
                        listedu.add(
                            com.jobsforher.models.Tag(
                                modelSkillsDisplay.education_skills!![k1].toString().trim()
                            )
                        )
                    }
                }
            }
        }

        we.setChipList(listwork)
        lif.setChipList(listlife)
        ed.setChipList(listedu)
        if(my_swipeRefresh_Layout.isRefreshing){
            my_swipeRefresh_Layout.isRefreshing = false
        }

    }

    fun isWhitespace(str: String?): Boolean {
        if (str == null) {
            return false
        }
        val sz = str.length
        for (i in 0 until sz) {
            if (Character.isWhitespace(str[i]) == false) {
                return false
            }
        }
        return true
    }

    fun openDialogSheetMilestones() {

        val dialog = Dialog(this)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog .setCancelable(true)
        dialog .setContentView(R.layout.dialog_sheet_miletsones)

        val addwork = dialog .findViewById(R.id.add_workexperience_layout_dialog) as LinearLayout
        addwork.setOnClickListener {
            dialog.cancel()
            val intent = Intent(applicationContext, WorkingEdit::class.java)
            startActivityForResult(intent, 1)
        }

        val addlife = dialog .findViewById(R.id.add_lifeexperience_layout_dialog) as LinearLayout
        addlife.setOnClickListener {
            dialog.cancel()
            val intent = Intent(applicationContext, LifeExperienceEdit::class.java)
            startActivityForResult(intent, 1)
        }

        val addedu = dialog .findViewById(R.id.add_education_layout_dialog) as LinearLayout
        addedu.setOnClickListener {
            dialog.cancel()
            val intent = Intent(applicationContext, EducationEdit::class.java)
            startActivityForResult(intent, 1)
        }

        val addcert = dialog .findViewById(R.id.add_certificates_layout_dialog) as LinearLayout
        addcert.setOnClickListener {
            dialog.cancel()
            val intent = Intent(applicationContext, CertificationEdit::class.java)
            startActivityForResult(intent, 1)
        }

        val addrec = dialog .findViewById(R.id.add_awards_layout_dialog) as LinearLayout
        addrec.setOnClickListener {
            dialog.cancel()
            val intent = Intent(applicationContext, AwardsEdit::class.java)
            startActivityForResult(intent, 1)
        }

        var window : Window = dialog.getWindow()!!;
        var wlp: WindowManager.LayoutParams  = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog .show()
    }


    fun submitProfileDetails() {

        val params = java.util.HashMap<String, String>()

        // params["about"] = etWorkSkills.text.toString()
        params["image_filename"] = profilePicturePathUpdate.toString()
        params["image_file"] = profileImageEncodedUpdate

        Logger.d("TAGG", params.toString())

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.AddProfileDetails(
            EndPoints.CLIENT_ID, "Bearer " +
                    EndPoints.ACCESS_TOKEN, params
        )

        call.enqueue(object : Callback<CreateProfileResponse> {
            override fun onResponse(
                call: Call<CreateProfileResponse>,
                response: Response<CreateProfileResponse>
            ) {

                Logger.d("CODE life exp", response.code().toString() + "")
                Logger.d("MESSAGE life exp", response.message() + "")
                Logger.d("URL life exp", "" + response)
                Logger.d("RESPONSE life exp", "" + Gson().toJson(response))

                if (response.isSuccessful) {

//                    EndPoints.ACCESS_TOKEN = response.body()!!.auth!!.accessToken.toString()
//
//                    val intent = Intent(applicationContext, ProfileView::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    startActivity(intent)
                    loadProfileData()

                    ToastHelper.makeToast(applicationContext, "Updated Successfullt ")

                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<CreateProfileResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })
    }
}