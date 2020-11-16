package com.jobsforher.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.TouchDelegate
import android.view.View
import android.widget.Toast
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.jobsforher.R
import com.jobsforher.helpers.HelperMethods
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.network.responsemodels.CheckPreferenceResponse
import com.jobsforher.network.responsemodels.RegisterSocialSignInResponse
import com.jobsforher.network.responsemodels.RegisterSocialTokenResponse
import com.jobsforher.network.responsemodels.SignInResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import com.kusu.linkedinlogin.Linkedin
import com.kusu.linkedinlogin.LinkedinLoginListener
import com.kusu.linkedinlogin.model.SocialUser
import com.linkedin.platform.APIHelper
import com.linkedin.platform.LISessionManager
import com.linkedin.platform.errors.LIApiError
import com.linkedin.platform.listeners.ApiListener
import com.linkedin.platform.listeners.ApiResponse
import com.linkedin.platform.utils.Scope
import kotlinx.android.synthetic.main.activity_signin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import women.jobs.jobsforher.activities.BaseActivity
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


class SignInActivity : BaseActivity() {

    private val TAG = javaClass.simpleName

    private var retrofitInterface: RetrofitInterface? = null
    private var callbackManager: CallbackManager? = null

    private var socialTokenUp: String? = null

    var RC_SIGN_IN = 1
    var mGoogleSignInClient: GoogleSignInClient? = null
    private var PRIVATE_MODE = 0
    private val PREF_STATUS = "isLoggedInStatus"
    private val PREF_NAME = "name"
    private val PREF_PROFILEURL = "profileUrl"
    private val PREF_ACCESSTOKEN = "accesstoken"
    private val PREF_PERCENTAGE = "0"
    private val PREF_ICON = "icon"
    private val PREF_PHONE = ""
    private val PREF_USERID = "id"
    private val PREF_VERIFIED_EMAIL = ""
    private val PREF_FCM = "fcmtoken"
    var fcm:String = ""
    var facebookName:String=""
    private var awesomeValidation: AwesomeValidation? = null

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_signin)
        // fcm = intent.getStringExtra("fcm")
        initGoogleLogin()

        awesomeValidation = AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation!!.addValidation(
            edittext_emailid,
            Patterns.EMAIL_ADDRESS,
            "Enter valid Email address"
        );
        awesomeValidation!!.addValidation(
            editText,
            RegexTemplate.NOT_EMPTY,
            "Password must not be empty"
        )

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d("TAGG", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                val token = task.result?.token
                // Log and toast
                val msg = getString(R.string.msg_token_fmt, token)
                fcm = token.toString()
                Log.d("TAGG", "ID IS:" + token)
            })
//        changeTouchableAreaOfView(view,40)

        signin.setOnClickListener {
            if (!isEmailValid(edittext_emailid.text!!.toString().trim())) {
                Toast.makeText(this, "wrong email id", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (awesomeValidation!!.validate()) {

                doSignIn(
//                    "arjun@appi.com",
                    edittext_emailid.text!!.trim().toString(),
//                    HelperMethods.md5("arjun1234"),
                    HelperMethods.md5(editText.text!!.trim().toString()),
                    "72.197.243.187",
                    Settings.Secure.getString(
                        applicationContext.getContentResolver(),
                        Settings.Secure.ANDROID_ID
                    ),
                    android.os.Build.MODEL,
                    ""
                )
            } else {
                //ToastHelper.makeToast(applicationContext, "Enter all the values")
            }

//            var intent = Intent(this, StarterActivity::class.java)
//            startActivity(intent)
        }

        btn_Back.setOnClickListener {

            onBackPressed()

        }

        fb_button.setOnClickListener {

           // Toast.makeText(applicationContext,"Coming soon", Toast.LENGTH_LONG).show()
            doRegisterIdSocialSignIn(
                Settings.Secure.getString(
                    applicationContext.getContentResolver(),
                    Settings.Secure.ANDROID_ID
                ), "facebook"
            )

        }

        gplus_button.setOnClickListener {

            //Toast.makeText(applicationContext,"Coming soon", Toast.LENGTH_LONG).show()
            doRegisterIdSocialSignIn(
                Settings.Secure.getString(
                    applicationContext.getContentResolver(),
                    Settings.Secure.ANDROID_ID
                ), "gmail"
            )

        }

        linkedin_button.setOnClickListener{
            //            doLinkedInLogin()
            //Toast.makeText(applicationContext,"Coming soon", Toast.LENGTH_LONG).show()
            doRegisterIdSocialSignIn(
                Settings.Secure.getString(
                    applicationContext.getContentResolver(),
                    Settings.Secure.ANDROID_ID
                ), "linkedin"
            )

        }

        forgotpasswordid.setOnClickListener {

            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        signuphere.setOnClickListener {

            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)

        }
    }

    private fun isEmailValid(email: String): Boolean {
        val a =  !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        return a
    }

    private fun changeTouchableAreaOfView(view: View, extraSpace: Int) {
        val parent = view.parent as View
//        Observable.just(parent)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(AndroidSchedulers.mainThread())
//            .subscribe {
//                val touchableArea = Rect()
//                view.getHitRect(touchableArea)
//                touchableArea.top -= extraSpace
//                touchableArea.bottom += extraSpace
//                touchableArea.left -= extraSpace
//                touchableArea.right += extraSpace
//                parent.touchDelegate = TouchDelegate(touchableArea, button)
//            }

//        In case you don't want to use Rx java
        parent.post {
            val touchableArea = Rect()
            signuphere.getHitRect(touchableArea)
            touchableArea.top += extraSpace
            touchableArea.bottom += extraSpace
            touchableArea.left -= extraSpace
            touchableArea.right += extraSpace
            parent.touchDelegate = TouchDelegate(touchableArea, signuphere)
        }

    }

    private fun doSignIn(
        email: String,
        password: String,
        ip: String,
        device: String,
        platform: String,
        browser: String
    ) {

        val params = HashMap<String, String>()

        val email = email
        val password = password
        val ip = ip
        val device = device
        val platform = platform
        val browser = browser

        params["email_mobile"] = email
        params["password"] = password
        params["ip"] = ip
        params["device"] = device
        params["platform"] = platform
        params["browser"] = browser

        Logger.e(TAG, "password : " + password)

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.doSignIn("application/json", EndPoints.CLIENT_ID, params)
        call.enqueue(object : Callback<SignInResponse> {

            override fun onResponse(
                call: Call<SignInResponse>,
                response: Response<SignInResponse>
            ) {


                if (response.isSuccessful) {


                        if (response.body()?.auth == null) {
                            Toast.makeText(
                                this@SignInActivity,
                                response.body()?.message,
                                Toast.LENGTH_SHORT
                            ).show()

                            return
                        }

                        EndPoints.ACCESS_TOKEN = response.body()!!.auth!!.accessToken.toString()
                        EndPoints.profileUrl = response.body()!!.body!!.profile_url.toString()

                        EndPoints.PROFILE_URL = response.body()!!.body!!.profile_url.toString()

                        EndPoints.USERNAME = response.body()!!.body!!.username.toString()
                        EndPoints.PROFILE_ICON = response.body()!!.body!!.profile_icon.toString()
                        EndPoints.PHONE_NO = response.body()!!.body!!.phoneNo.toString()
                        EndPoints.USERID = response.body()!!.body!!.id.toString()

                        Log.d("TAGG", EndPoints.USERNAME)

                        val sharedPref: SharedPreferences = getSharedPreferences(
                            "mysettings",
                            Context.MODE_PRIVATE
                        );
                        val editor = sharedPref.edit()
                        editor.clear()
                        editor.putString(PREF_FCM, fcm)
                        editor.putBoolean(PREF_STATUS, true)
                        editor.putString(PREF_NAME, EndPoints.USERNAME)
                        editor.putString(PREF_ACCESSTOKEN, EndPoints.ACCESS_TOKEN)
                        editor.putString(PREF_PROFILEURL, EndPoints.PROFILE_URL)
                        editor.putString(PREF_ICON, EndPoints.PROFILE_ICON)
                        editor.putString(
                            PREF_PERCENTAGE,
                            response.body()!!.body!!.profile_percentage!!.toString()
                        )
                        editor.putString(PREF_PHONE, EndPoints.PHONE_NO)
                        editor.putString(PREF_USERID, response.body()!!.body!!.id!!.toString())
                        editor.putString(
                            PREF_VERIFIED_EMAIL,
                            response.body()!!.body!!.verified_email.toString()
                        )

                        editor.commit()
                        val sharedPref1: SharedPreferences = getSharedPreferences(
                            "mysettings",
                            Context.MODE_PRIVATE
                        )
                        Log.d("TAGG", "USERID" + sharedPref1.getString(PREF_USERID, "id"))
                        Log.d("TAGG", "USERID" + EndPoints.USERID)
                        //checkPref()
                        intent = Intent(applicationContext, HomePagePreferences::class.java)
                        intent.putExtra("userID", 0)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)


                    } else {

                        ToastHelper.makeToast(
                            applicationContext,
                            response.body()?.message.toString()
                        )

                    }


            }

            override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                Logger.d(TAG, "FAILED : $t")
                Toast.makeText(
                    applicationContext,
                    t.message,
                    Toast.LENGTH_SHORT
                ).show();
            }
        })
    }

    fun doGoogleLogin()
    {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun doFacebookLogin(socialToken: String) {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logInWithReadPermissions(
            this, Arrays.asList(
                "public_profile",
                "email"
            )
        )
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                @SuppressLint("HardwareIds")
                override fun onSuccess(loginResult: LoginResult) {

                    Logger.d(TAG, "Facebook token SignIN: " + loginResult.accessToken.token)

                    val profile: Profile = Profile.getCurrentProfile()

                    Log.d(TAG, "Picture: " + profile.getProfilePictureUri(300, 300))
                    Log.d(
                        TAG,
                        "Name: ${profile.firstName} ${profile.middleName} ${profile.lastName}"
                    )
                    Log.d(TAG, "Link: " + profile.linkUri)
                    Log.d(TAG, "ID: " + profile.id)
                    facebookName = "${profile.firstName} ${profile.middleName} ${profile.lastName}"
                    var email: String = ""
                    val request =
                        GraphRequest.newMeRequest(loginResult.accessToken) { `object`, response ->
                            try {
                                //here is the data that you want
                                Logger.d("FBLOGIN_JSON_RES", `object`.toString())

                                if (`object`.has("email")) {
                                    Log.d(TAG, `object`.getString("email"))
                                    email = `object`.getString("email")
                                    updateSocialLoginDetails(
                                        email, socialToken, "facebook",
                                        Settings.Secure.getString(
                                            applicationContext.getContentResolver(),
                                            Settings.Secure.ANDROID_ID
                                        )
                                    )
                                } else {
                                    Log.d(TAG, `object`.toString())
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    val parameters = Bundle()
                    parameters.putString("fields", "name,email,id,picture.type(large)")
                    request.parameters = parameters
                    request.executeAsync()


                }

                override fun onCancel() {
                    Logger.d(TAG, "Facebook onCancel.")
                }

                override fun onError(error: FacebookException) {
                    Logger.d(TAG, "Facebook onError. $error")
                    if (error is FacebookAuthorizationException) {
                        if (AccessToken.getCurrentAccessToken() != null) {
                            LoginManager.getInstance().logOut();
                        }
                    }
                }
            })
    }

    //logout functionality for Facebook signin
    fun doFacebookLogout()
    {
        LoginManager.getInstance().logOut()
    }

    fun initGoogleLogin()
    {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        LISessionManager.getInstance(getApplicationContext()).onActivityResult(
            this,
            requestCode,
            resultCode,
            data
        );
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

        else
        {
            callbackManager?.onActivityResult(requestCode, resultCode, data)
        }

    }

    @SuppressLint("HardwareIds")
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            Logger.d(TAG, "Display Name: " + account!!.displayName)
            Logger.d(TAG, "Email: " + account.email)
            Logger.d(TAG, "ID: " + account.id)

            updateSocialLoginDetails(
                account.email.toString(), socialTokenUp.toString(), "gmail",
                Settings.Secure.getString(
                    applicationContext.getContentResolver(),
                    Settings.Secure.ANDROID_ID
                )
            )

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Logger.d(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    // logout functionality for google signin
    private fun doGoogleLogout() {
        mGoogleSignInClient!!.signOut()
            .addOnCompleteListener(this) {

                Toast.makeText(applicationContext, "Logged Out", Toast.LENGTH_SHORT).show()
            }
    }


    private fun doRegisterIdSocialSignIn(device_id: String, type: String) {

        val params = HashMap<String, String>()

        var current:String = ""

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            current = LocalDateTime.now().toString()
        }else{
            val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            current = sdf.format(Date())
        }

        val installationId = device_id+current

        params["social_token"] = installationId

        Logger.d(TAG, "Params : " + Gson().toJson(params))

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.registerSocialToken(
            "application/json",
            EndPoints.CLIENT_ID,
            params
        )
        call.enqueue(object : Callback<RegisterSocialTokenResponse> {

            override fun onResponse(
                call: Call<RegisterSocialTokenResponse>,
                response: Response<RegisterSocialTokenResponse>
            ) {

                if (response.isSuccessful) {
//                    ToastHelper.makeToast(applicationContext, response.body()!!.auth!!.access_token)
                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                    socialTokenUp = response.body()!!.body!!.social_token

                    if (type.equals("facebook")) {

                        doFacebookLogin(response.body()!!.body!!.social_token.toString())

                    } else if (type.equals("gmail")) {
                        doGoogleLogout()
                        doGoogleLogin()

                    } else if (type.equals("linkedin")) {

                        doLinkedInLogin()

                    }

                } else {
                    ToastHelper.makeToast(applicationContext, "Register Id Response Failed")
                }

            }

            override fun onFailure(call: Call<RegisterSocialTokenResponse>, t: Throwable) {

                Logger.d(TAG, "FAILED : $t")
            }
        })
    }

    private fun updateSocialLoginDetails(
        email: String,
        installation_id: String,
        type: String,
        device_id: String
    ) {

        val params = HashMap<String, String>()

        val emaiId = email
        val installationId = installation_id
        val signInType = type
        val deviceId = device_id

        params["email"] = emaiId
        params["social_token"] = installationId
        params["social_login"] = signInType
        params["device_id"] = deviceId

        Logger.d(TAG, "Params : " + Gson().toJson(params))

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.registerSocialSignIn(
            "application/json",
            EndPoints.CLIENT_ID,
            params
        )
        call.enqueue(object : Callback<RegisterSocialSignInResponse> {

            override fun onResponse(
                call: Call<RegisterSocialSignInResponse>,
                response: Response<RegisterSocialSignInResponse>
            ) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
                Logger.d("URL", "" + response)


                if (response.isSuccessful) {
//                    ToastHelper.makeToast(applicationContext, response.body()!!.auth!!.access_token)
                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())

                    EndPoints.ACCESS_TOKEN = response.body()!!.auth!!.accessToken.toString()

                    EndPoints.profileUrl = response.body()!!.body!!.profile_url.toString()
                    EndPoints.PROFILE_URL = response.body()!!.body!!.profile_url.toString()

                    if (response.body()!!.body!!.username!!.trim().length > 0)
                        EndPoints.USERNAME = response.body()!!.body!!.username.toString()
                    else
                        EndPoints.USERNAME = facebookName
                    EndPoints.USERID = response.body()!!.body!!.id.toString()

                    var perc: String = response.body()!!.body!!.profile_percentage!!.toString()
                    EndPoints.PROFILE_ICON = response.body()!!.body!!.profile_icon.toString()

                    val sharedPref: SharedPreferences = getSharedPreferences(
                        "mysettings",
                        Context.MODE_PRIVATE
                    );
                    val editor = sharedPref.edit()
                    editor.clear()
                    editor.putString(PREF_FCM, fcm)
                    editor.putBoolean(PREF_STATUS, true)
                    editor.putString(PREF_NAME, EndPoints.USERNAME)
                    editor.putString(PREF_ACCESSTOKEN, EndPoints.ACCESS_TOKEN)
                    editor.putString(PREF_PROFILEURL, EndPoints.PROFILE_URL)
                    editor.putString(PREF_ICON, EndPoints.PROFILE_ICON)
                    editor.putString(PREF_PERCENTAGE, perc)
                    editor.putString(PREF_USERID, response.body()!!.body!!.id!!.toString())
                    editor.putString(
                        PREF_VERIFIED_EMAIL,
                        response.body()!!.body!!.verified_email.toString()
                    )
                    editor.commit()
                    // checkPref()
                    intent = Intent(applicationContext, HomePagePreferences::class.java)
                    intent.putExtra("userID", 0)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
//                    if (response.body()!!.body!!.profile_percentage!!.toInt() < 21){
//
//                        val intent = Intent(applicationContext, SignUpWelcomeActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        startActivity(intent)
//
//                    }else{
//
//                        val intent = Intent(applicationContext, ProfileView::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        startActivity(intent)
//                    }


                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid Username or Password")
                }

            }

            override fun onFailure(call: Call<RegisterSocialSignInResponse>, t: Throwable) {

                Logger.d(TAG, "FAILED : $t")
            }
        })
    }

    //LinkedIn

    fun buildScope(): Scope? {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS, Scope.W_SHARE);
    }

    fun doLinkedInLogin(){

        Linkedin.initialize(
            context = applicationContext,
            clientId = "81nasohje8zom3", //Client Id of your linkedin app like-> "47sf33fjflf"
            clientSecret = "pXwsDW90mhfykF81", //Client Secret of your linkedin app like-> "Udhfksfeu324uh4"
            redirectUri = "http://www.workingnaari.in/account/authenticatedLinkedIn", //Redirect url which has to be add in your linkedin app like-> "https://example.com/auth/linkedin/callback"
            state = "sneha23", //For security purpose used to prevent CSRF like-> "nfw4wfhw44r34fkwh"
            scopes = listOf(
                "r_emailaddress",
                "r_liteprofile"
            ) // app permission options like-> "r_liteprofile", "r_emailaddress", "w_member_social"
        )

        Linkedin.login(this, object : LinkedinLoginListener {
            override fun failedLinkedinLogin(error: String) {
                Log.e(TAG, error)
                //Toast.makeText(applicationContext, "Failed to authenticate with LinkedIn. Please try again.", Toast.LENGTH_SHORT).show();
            }

            override fun successLinkedInLogin(socialUser: SocialUser) {

                Log.e(TAG, "ID IS: " + socialUser.socialId + ", " + socialUser.email)
                Toast.makeText(
                    applicationContext,
                    "Successfully authenticated with LinkedIn.",
                    Toast.LENGTH_SHORT
                ).show();
                updateSocialLoginDetails(
                    socialUser.email.toString(),
                    socialTokenUp.toString(), "linkedin",
                    Settings.Secure.getString(
                        applicationContext.getContentResolver(),
                        Settings.Secure.ANDROID_ID
                    )
                )
            }
        })

//        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope()//pass the build scope here
//            ,  object : AuthListener {
//
//                override fun onAuthSuccess() {
//                    // Authentication was successful. You can now do
//                    // other calls with the SDK.
//                    Toast.makeText(applicationContext, "Successfully authenticated with LinkedIn.", Toast.LENGTH_SHORT).show();
//                }
//
//                override fun onAuthError( error:LIAuthError) {
//                    // Handle authentication errors
//                    Log.e(TAG, "Auth Error :" + error.toString());
//                    Toast.makeText(applicationContext, "Failed to authenticate with LinkedIn. Please try again.", Toast.LENGTH_SHORT).show();
//                }
//            }, true);//if TRUE then it will show dialog if
//        // any device has no LinkedIn app installed to download app else won't show anything





//        //First check if user is already authenticated or not and session is valid or not
//        if (!LISessionManager.getInstance(this).getSession().isValid()) {
//            LISessionManager.getInstance(applicationContext).init(this, buildScope()//pass the build scope here
//                , object : AuthListener {
//                    override fun onAuthSuccess() {
//                        // Authentication was successful. You can now do
//                        // other calls with the SDK.
//                        Toast.makeText(applicationContext, "Successfully authenticated with LinkedIn.", Toast.LENGTH_SHORT)
//                            .show()
//
//                    }
//
//                    override fun onAuthError(error: LIAuthError) {
//                        // Handle authentication errors
//                        Log.d("TAGG", "Auth Error :" + error.toString())
//
//                        Toast.makeText(
//                            applicationContext,
//                            "Failed to authenticate with LinkedIn. Please try again.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }, true
//            )//if TRUE then it will show dialog if
//            // any device has no LinkedIn app installed to download app else won't show anything
//        } else {
//            Toast.makeText(this, "You are already authenticated.", Toast.LENGTH_SHORT).show();
//            //if user is already authenticated fetch basic profile data for user
//            fetchBasicProfileData();
//        }
    }

    fun fetchBasicProfileData(){
        //In URL pass whatever data from user you want for more values check below link
        //LINK : https://developer.linkedin.com/docs/fields/basic-profile
        val url =
            "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,headline,public-profile-url,picture-url,email-address,picture-urls::(original))"
        val apiHelper = APIHelper.getInstance(applicationContext)
        apiHelper.getRequest(this, url, object : ApiListener {
            @SuppressLint("HardwareIds")
            override fun onApiSuccess(apiResponse: ApiResponse?) {
                // Success!
                Log.d(
                    TAG,
                    "API Res : " + apiResponse!!.getResponseDataAsString() + "\n" + apiResponse.getResponseDataAsJson()
                        .toString()
                )
//                Toast.makeText(applicationContext, "Successfully fetched LinkedIn profile data.", Toast.LENGTH_SHORT).show();

                //update UI on successful data fetched
//                updateUI(apiResponse);

                updateSocialLoginDetails(
                    apiResponse.responseDataAsJson!!.getString("email-address"),
                    socialTokenUp.toString(), "linkedin",
                    Settings.Secure.getString(
                        applicationContext.getContentResolver(),
                        Settings.Secure.ANDROID_ID
                    )
                )

            }

            override fun onApiError(LIApiError: LIApiError?) {
                // Error making GET request!
                Log.e(TAG, "Fetch profile Error   :" + LIApiError!!.getLocalizedMessage());
                Toast.makeText(
                    applicationContext,
                    "Failed to fetch basic profile data. Please try again.",
                    Toast.LENGTH_SHORT
                ).show();
            }
        })
    }

    //logout functionality for LindkeIn signin
    fun doLogout() {
        //clear session on logout click
        LISessionManager.getInstance(this).clearSession()

        //show toast
        Toast.makeText(this, "Logout successfully.", Toast.LENGTH_SHORT).show()
    }

    fun checkPref(){

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
                    Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG)
                        .show()
                    val homeIntent = Intent(applicationContext, HomePage::class.java)
                    intent.putExtra("fcm", fcm)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(homeIntent)
                    finish()

                } else {
                    val homeIntent = Intent(applicationContext, HomePage::class.java)
                    intent.putExtra("fcm", fcm)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(homeIntent)
                    finish()
                }
            }

            override fun onFailure(call: Call<CheckPreferenceResponse>, t: Throwable) {
                Logger.d("TAGG", "Apply Job FAILED : $t")
                val homeIntent = Intent(applicationContext, HomePage::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(homeIntent)
                finish()
            }
        })
    }
}
