package com.jobsforher.activities

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.linkedin.platform.APIHelper
import com.linkedin.platform.LISessionManager.*
import com.linkedin.platform.errors.LIApiError
import com.linkedin.platform.listeners.ApiListener
import com.linkedin.platform.listeners.ApiResponse
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import com.jobsforher.R
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.network.responsemodels.RegisterSocialTokenResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import java.time.LocalDateTime
import com.linkedin.platform.utils.Scope
import com.jobsforher.network.responsemodels.RegisterSocialSignInResponse
import com.kusu.linkedinlogin.Linkedin
import com.kusu.linkedinlogin.LinkedinLoginListener
import com.kusu.linkedinlogin.model.SocialUser
import kotlinx.android.synthetic.main.activity_login_new.*
import women.jobs.jobsforher.activities.BaseActivity
import java.util.*


class LoginActivity : BaseActivity() {

    private val TAG = javaClass.simpleName

    var retrofitInterface: RetrofitInterface? = null
    var callbackManager: CallbackManager? = null

    var socialTokenUp: String? = null

    var RC_SIGN_IN = 1
    var mGoogleSignInClient: GoogleSignInClient? = null

    private var PRIVATE_MODE = 0
    private val PREF_STATUS = "isLoggedInStatus"
    private val PREF_NAME = "name"
    private val PREF_PROFILEURL = "profileUrl"
    private val PREF_ACCESSTOKEN = "accesstoken"
    private val PREF_PERCENTAGE = "0"

    @SuppressLint("HardwareIds")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login_new)

        initGoogleLogin()

        ll_FacebookLogin.setOnClickListener {

            doRegisterIdSocialLogIn( Settings.Secure.getString(applicationContext.getContentResolver(),
                Settings.Secure.ANDROID_ID)
                , "facebook")
        }

        headline1.setOnClickListener {
            finish()
        }

        ll_GoogleLogin.setOnClickListener {

            doRegisterIdSocialLogIn( Settings.Secure.getString(applicationContext.getContentResolver(),
                Settings.Secure.ANDROID_ID)
                , "gmail")

        }

        ll_LinkedInLogin.setOnClickListener {

            //doLinkedInLogin()
            doRegisterIdSocialLogIn( Settings.Secure.getString(applicationContext.getContentResolver(),
                Settings.Secure.ANDROID_ID)
                , "linkedin")
        }

        tvSignInHere.setOnClickListener{

            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)

        }

        btnSignUpWithEmail.setOnClickListener {

            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
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

        //LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
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
            Logger.d(TAG,"Display Name: "+account!!.displayName)
            Logger.d(TAG,"Email: "+ account.email)
            Logger.d(TAG,"ID: "+ account.id)

            updateSocialLoginDetails(
                account.email.toString(), socialTokenUp.toString(),"gmail",
                Settings.Secure.getString(applicationContext.getContentResolver(), Settings.Secure.ANDROID_ID))

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Logger.d(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    fun doFacebookLogin(socialToken : String) {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                @SuppressLint("HardwareIds")
                override fun onSuccess(loginResult: LoginResult) {

                    Logger.d("FBLOGIN", loginResult.recentlyDeniedPermissions.toString())
                    Logger.d("FBLOGIN", loginResult.recentlyGrantedPermissions.toString())
                    Logger.d(TAG, "Facebook token Login: " + loginResult.accessToken.token)

                    val profile: Profile = Profile.getCurrentProfile()

                    Log.d(TAG, "Picture: " + profile.getProfilePictureUri(300, 300))
                    Log.d(TAG, "Name: ${profile.firstName} ${profile.middleName} ${profile.lastName}")
                    Log.d(TAG, "Link: " + profile.linkUri)
                    Log.d(TAG, "ID: " + profile.id)
                    var email:String=""
                    val request = GraphRequest.newMeRequest(loginResult.accessToken) { `object`, response ->
                        try {
                            //here is the data that you want
                            Logger.d("FBLOGIN_JSON_RES", `object`.toString())

                            if (`object`.has("email")) {
                                Log.d(TAG,`object`.getString("email"))
                                email = `object`.getString("email")
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

                    updateSocialLoginDetails(profile.id, socialToken,"facebook",
                        Settings.Secure.getString(applicationContext.getContentResolver(), Settings.Secure.ANDROID_ID))
                }

                override fun onCancel() {
                    Logger.d(TAG, "Facebook onCancel.")
                }

                override fun onError(error: FacebookException) {
                    Logger.d(TAG, "Facebook onError. $error")
                }
            })
    }

    fun doGoogleLogin()
    {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun doRegisterIdSocialLogIn(device_id: String, type: String) {

        val params = HashMap<String, String>()

        val current = LocalDateTime.now()

        val installationId = device_id+current

        params["social_token"] = installationId

        Logger.d(ContentValues.TAG, "Params : " + Gson().toJson(params))

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.registerSocialToken("application/json", EndPoints.CLIENT_ID, params)
        call.enqueue(object : Callback<RegisterSocialTokenResponse> {

            override fun onResponse(call: Call<RegisterSocialTokenResponse>, response: Response<RegisterSocialTokenResponse>) {

                if (response.isSuccessful) {
                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                    Logger.d("CODE", response.code().toString() + "")
                    Logger.d("MESSAGE", response.message() + "")
                    Logger.d("RESPONSE", "" + Gson().toJson(response))
                    Logger.d("URL", "" + response)
                    socialTokenUp = response.body()!!.body!!.social_token

                    if (type.equals("facebook")){

                        doFacebookLogin(response.body()!!.body!!.social_token.toString())

                    }else if(type.equals("gmail")){

                        doGoogleLogin()

                    }else if(type.equals("linkedin")){

                        doLinkedInLogin()

                    }

                } else {
                    ToastHelper.makeToast(applicationContext, "Social Token Response Failed")
                }

            }

            override fun onFailure(call: Call<RegisterSocialTokenResponse>, t: Throwable) {

                Logger.d(ContentValues.TAG, "FAILED : $t")
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
            scopes = listOf("r_emailaddress","r_liteprofile") // app permission options like-> "r_liteprofile", "r_emailaddress", "w_member_social"
        )

        Linkedin.login(this, object : LinkedinLoginListener {
            override fun failedLinkedinLogin(error: String) {
                Log.e(TAG,  error)
                //Toast.makeText(applicationContext, "Failed to authenticate with LinkedIn. Please try again.", Toast.LENGTH_SHORT).show();
            }

            override fun successLinkedInLogin(socialUser: SocialUser) {

                Log.e(TAG, "ID IS: "+socialUser.socialId + ", " + socialUser.email)
                Toast.makeText(applicationContext, "Successfully authenticated with LinkedIn.", Toast.LENGTH_SHORT).show();
                updateSocialLoginDetails(socialUser.email.toString(),
                    socialTokenUp.toString(), "linkedin",
                    Settings.Secure.getString(applicationContext.getContentResolver(), Settings.Secure.ANDROID_ID))
            }
        })



//        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope()//pass the build scope here
//                    ,  object : AuthListener {
//
//                        override fun onAuthSuccess() {
//                            // Authentication was successful. You can now do
//                            // other calls with the SDK.
//                            Toast.makeText(applicationContext, "Successfully authenticated with LinkedIn.", Toast.LENGTH_SHORT).show();
//                        }
//
//
//                         override fun onAuthError( error:LIAuthError) {
//                            // Handle authentication errors
//                            Log.e(TAG, "Auth Error :" + error.toString());
//                            Toast.makeText(applicationContext, "Failed to authenticate with LinkedIn. Please try again.", Toast.LENGTH_SHORT).show();
//                        }
//                    }, true);//if TRUE then it will show dialog if
//            // any device has no LinkedIn app installed to download app else won't show anything





//        //First check if user is already authenticated or not and session is valid or not
//        if (!getInstance(this).getSession().isValid()) {
//            getInstance(applicationContext).init(this, buildScope()//pass the build scope here
//                , object : AuthListener {
//                    override fun onAuthSuccess() {
//                        // Authentication was successful. You can now do
//                        // other calls with the SDK.
//                        Toast.makeText(applicationContext, "Successfully authenticated with LinkedIn.", Toast.LENGTH_SHORT)
//                            .show()
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
        apiHelper.getRequest(this,url,object: ApiListener {
            @SuppressLint("HardwareIds")
            override fun onApiSuccess(apiResponse: ApiResponse?) {
                // Success!
                Log.d(TAG, "API Res : " + apiResponse!!.getResponseDataAsString() + "\n" + apiResponse.getResponseDataAsJson().toString())
//                Toast.makeText(applicationContext, "Successfully fetched LinkedIn profile data.", Toast.LENGTH_SHORT).show();

                //update UI on successful data fetched
//                updateUI(apiResponse);

                updateSocialLoginDetails(apiResponse.responseDataAsJson!!.getString("email-address"),
                    socialTokenUp.toString(), "linkedin",
                    Settings.Secure.getString(applicationContext.getContentResolver(), Settings.Secure.ANDROID_ID))

            }

            override fun onApiError(LIApiError: LIApiError?) {
                // Error making GET request!
                Log.e(TAG, "Fetch profile Error   :" + LIApiError!!.getLocalizedMessage());
                Toast.makeText(applicationContext, "Failed to fetch basic profile data. Please try again.", Toast.LENGTH_SHORT).show();
            }
        })
    }

    //logout functionality for LindkeIn signin
    fun doLogout() {
        //clear session on logout click
        getInstance(this).clearSession()

        //show toast
        Toast.makeText(this, "Logout successfully.", Toast.LENGTH_SHORT).show()
    }


    private fun updateSocialLoginDetails(email: String, installation_id : String, type: String, device_id: String) {

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

        val call = retrofitInterface!!.registerSocialSignIn("application/json", EndPoints.CLIENT_ID, params)
        call.enqueue(object : Callback<RegisterSocialSignInResponse> {

            override fun onResponse(call: Call<RegisterSocialSignInResponse>, response: Response<RegisterSocialSignInResponse>) {

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
                Logger.d("URL", "" + response)


                if (response.isSuccessful) {
//                    ToastHelper.makeToast(applicationContext, response.body()!!.auth!!.access_token)
                    EndPoints.ACCESS_TOKEN = response.body()!!.auth!!.accessToken.toString()

                    EndPoints.profileUrl = response.body()!!.body!!.profile_url.toString()

                    EndPoints.USERNAME = response.body()!!.body!!.username.toString()
                    EndPoints.PROFILE_URL = response.body()!!.body!!.profile_url.toString()

                    Log.d("TAGG", EndPoints.ACCESS_TOKEN)

                    val sharedPref: SharedPreferences = getSharedPreferences("mysettings",
                        Context.MODE_PRIVATE);
                    val editor = sharedPref.edit()
                    editor.clear()
                    editor.putBoolean(PREF_STATUS, true)
                    editor.putString(PREF_NAME,EndPoints.USERNAME)
                    editor.putString(PREF_ACCESSTOKEN, EndPoints.ACCESS_TOKEN)
                    editor.putString(PREF_PROFILEURL, EndPoints.PROFILE_URL)
                    editor.putString(PREF_PERCENTAGE, "0")
                    editor.commit()

                    val homeIntent = Intent(applicationContext, HomePage::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(homeIntent)
                    finish()

                } else {
                    ToastHelper.makeToast(applicationContext, "Updating Social SignIn Response Failed")
                }

            }

            override fun onFailure(call: Call<RegisterSocialSignInResponse>, t: Throwable) {

                Logger.d(TAG, "FAILED : $t")
            }
        })
    }

}


