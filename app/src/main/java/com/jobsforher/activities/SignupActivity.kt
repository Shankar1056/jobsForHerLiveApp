package com.jobsforher.activities

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.galleonsoft.safetynetrecaptcha.ApiPostHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_signup_new.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.jobsforher.R
import com.jobsforher.helpers.HelperMethods
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.network.responsemodels.RegisterSocialSignInResponse
import com.jobsforher.network.responsemodels.RegisterSocialTokenResponse
import com.jobsforher.network.responsemodels.SignUpResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import com.jobsforher.ui.auth.LoginActivity
import com.kusu.linkedinlogin.Linkedin
import com.kusu.linkedinlogin.LinkedinLoginListener
import com.kusu.linkedinlogin.model.SocialUser
import kotlinx.android.synthetic.main.activity_signup_new.iv_Back
import women.jobs.jobsforher.activities.BaseActivity
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class SignupActivity : BaseActivity() {

    private val TAG = javaClass.simpleName
    private var retrofitInterface: RetrofitInterface? = null

    private var PRIVATE_MODE = 0
    private val PREF_STATUS = "isLoggedInStatus"
    private val PREF_NAME = "name"
    private val PREF_PROFILEURL = "profileUrl"
    private val PREF_ACCESSTOKEN = "accesstoken"
    private val PREF_PERCENTAGE = "0"
    private val PREF_PHONE = ""
    private val PREF_USERID = "id"
    private val PREF_VERIFIED_EMAIL = ""
    private val PREF_FCM = "fcmtoken"
    var fcm:String = ""
    var facebookName:String=""
    private var awesomeValidation: AwesomeValidation? = null
    var RC_SIGN_IN = 1
    var mGoogleSignInClient: GoogleSignInClient? = null
    var callbackManager: CallbackManager? = null
    var socialTokenUp: String? = null

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_signup_new)

        awesomeValidation = AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation!!.addValidation(etFullname, RegexTemplate.NOT_EMPTY, "Full name is missing")
        val regexPassword = ".{6,}"
        awesomeValidation!!.addValidation(etPassword_, regexPassword, "Password must be atleast 6 characters")
        awesomeValidation!!.addValidation(etEmail_Id, Patterns.EMAIL_ADDRESS, "Enter valid Email address");
        awesomeValidation!!.addValidation(etMobileNo, "^[+]?[0-9]{10,13}$", "Enter valid phone number");

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
                Log.d("TAGG", "ID IS:"+token)
            })


        initGoogleLogin()

        fb_button.setOnClickListener {

            //Toast.makeText(applicationContext,"Coming soon", Toast.LENGTH_LONG).show()
            doRegisterIdSocialLogIn(
                Settings.Secure.getString(
                    applicationContext.getContentResolver(),
                    Settings.Secure.ANDROID_ID
                )
                , "facebook"
            )
        }

        gplus_button.setOnClickListener {

            //Toast.makeText(applicationContext,"Coming soon", Toast.LENGTH_LONG).show()
            doRegisterIdSocialLogIn(
                Settings.Secure.getString(
                    applicationContext.getContentResolver(),
                    Settings.Secure.ANDROID_ID
                )
                , "gmail"
            )

        }

        linkedin_button.setOnClickListener {

            //Toast.makeText(applicationContext,"Coming soon", Toast.LENGTH_LONG).show()
            //doLinkedInLogin()
            doRegisterIdSocialLogIn(
                Settings.Secure.getString(
                    applicationContext.getContentResolver(),
                    Settings.Secure.ANDROID_ID
                )
                , "linkedin"
            )
        }

        forgotpasswordid.setText(Html.fromHtml("By signing up, I agree to the <font color='#3FA9E6'><u>Terms and Conditions</u></font> \nand certify that I am a woman"));

        forgotpasswordid.setOnClickListener {
            intent = Intent(applicationContext, WebActivity::class.java)
            intent.putExtra("value", " https://www.jobsforher.com/terms-of-use")
            startActivity(intent)
        }
        btnSign_Up.setOnClickListener {
            if (!isEmailValid( etEmail_Id.text!!.trim().toString())) {
                Toast.makeText(this, "wrong email id", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (awesomeValidation!!.validate()) {

//                doLogin(etEmailId.text!!.trim().toString(), HelperMethods.md5(etPassword.text!!.trim().toString()))

                doSignUp(
                    etEmail_Id.text!!.trim().toString(), HelperMethods.md5(etPassword_.text!!.trim().toString()),
                    etFullname.text!!.trim().toString(), etMobileNo.text!!.trim().toString()
                )

            } else {
//                ToastHelper.makeToast(applicationContext, "Enter all the values")
            }

//            CheckSafetynetreCAPTCHA()
        }

        tvLogin_Here.setOnClickListener {
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        signuphere.setOnClickListener {
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


        iv_Back.setOnClickListener {
            finish()
        }

//        tvTerms.setOnClickListener {
//            openChromeTab(URLs.tnc)
//        }

    }

    private fun isEmailValid(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onBackPressed() {
        finish()
    }

    fun initGoogleLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    fun CheckSafetynetreCAPTCHA() {

        val feedback = etPassword_.text.toString().trim()
        // checking for empty text message
        if (TextUtils.isEmpty(feedback)) {
            Toast.makeText(applicationContext, "Enter Password!", Toast.LENGTH_LONG).show()
            return
        }

        // Showing SafetyNet reCAPTCHA dialog
        SafetyNet.getClient(this).verifyWithRecaptcha(SAFETY_NET_API_KEY)
            .addOnSuccessListener(this) { response ->
                Log.d(TAG, "onSuccess")

                if (!response.tokenResult.isEmpty()) {

                    // Received reCaptcha token and This token still needs to be validated on
                    // the server using the SECRET key api.
                    verifyTokenFromServer(response.tokenResult, feedback).execute()
                    Log.i(TAG, "onSuccess: " + response.tokenResult)
                }
            }
            .addOnFailureListener(this) { e ->
                if (e is ApiException) {
                    Log.d(TAG, "SafetyNet Error: " + CommonStatusCodes.getStatusCodeString(e.statusCode))
                } else {
                    Log.d(TAG, "Unknown SafetyNet error: " + e.message)
                }
            }
    }

    private fun doRegisterIdSocialLogIn(device_id: String, type: String) {

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

                        doGoogleLogout()
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
                    facebookName = "${profile.firstName} ${profile.middleName} ${profile.lastName}"
                    var email:String=""
                    val request = GraphRequest.newMeRequest(loginResult.accessToken) { `object`, response ->
                        try {
                            //here is the data that you want
                            Logger.d("FBLOGIN_JSON_RES", `object`.toString())

                            if (`object`.has("email")) {
                                Log.d(TAG,`object`.getString("email"))
                                email = `object`.getString("email")
                                //profile.id
                                updateSocialLoginDetails(email, socialToken,"facebook",
                                    Settings.Secure.getString(applicationContext.getContentResolver(), Settings.Secure.ANDROID_ID))
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

    fun doGoogleLogin()
    {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
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

    private fun doSignUp(email: String, password: String, fullName: String, mobileNo: String) {

        val params = HashMap<String, String>()

        val username = fullName
        val email = email
        val phone_no = mobileNo
        val password = password
        val role = "user"
        val ip = ""//"72.197.243.187"

        params["username"] = username
        params["email"] = email
        params["phone_no"] = phone_no
        params["password"] = password
        params["role"] = role
        params["ip"] = ip



        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.doSignUp("application/json", EndPoints.CLIENT_ID, params)
        call.enqueue(object : Callback<SignUpResponse> {

            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {


                if (response.body()!!.responseCode!!.equals(11005)) {
                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                } else if (response.isSuccessful) {
//                    ToastHelper.makeToast(applicationContext, response.body()!!.auth!!.access_token)
                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())

                    EndPoints.ACCESS_TOKEN = response.body()!!.auth!!.access_token.toString()

                    EndPoints.profileUrl = response.body()!!.body!!.profile_url.toString()

                    EndPoints.USERNAME = response.body()!!.body!!.username.toString()
                    EndPoints.PROFILE_URL = response.body()!!.body!!.profile_url.toString()
                    EndPoints.PHONE_NO = response.body()!!.body!!.phoneNo.toString()
                    EndPoints.USERID = response.body()!!.body!!.id.toString()

                    Log.d("TAGG", EndPoints.USERNAME)

                    val sharedPref: SharedPreferences = getSharedPreferences(
                        "mysettings",
                        Context.MODE_PRIVATE
                    );
                    val editor = sharedPref.edit()
                    editor.clear()
                    editor.putString(PREF_FCM,fcm)
                    editor.putBoolean(PREF_STATUS, true)
                    editor.putString(PREF_NAME, EndPoints.USERNAME)
                    editor.putString(PREF_ACCESSTOKEN, EndPoints.ACCESS_TOKEN)
                    editor.putString(PREF_PROFILEURL, EndPoints.PROFILE_URL)
                    editor.putString(PREF_PERCENTAGE, "0")
                    editor.putString(PREF_PHONE, EndPoints.PHONE_NO)
                    editor.putString(PREF_USERID, response.body()!!.body!!.id!!.toString())
                    editor.putString(PREF_VERIFIED_EMAIL, response.body()!!.body!!.verified_email.toString())
                    editor.apply()

                    Log.d("TAGG", "USERID"+EndPoints.USERID)

                    val homeIntent = Intent(applicationContext, HomePage::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(homeIntent)
                    finish()


//                    val intent = Intent(applicationContext, SignUpWelcomeActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    intent.putExtra("isLoggedIn",true)
//                    startActivity(intent)

                } else {
                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())
                }

            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {

                Logger.d(TAG, "FAILED : $t")
            }
        })
    }


    /**
     * Verifying the captcha token on the server
     * Server makes call to https://www.google.com/recaptcha/api/siteverify
     * with SECRET Key and SafetyNet token.
     */
    @SuppressLint("StaticFieldLeak")
    private inner class verifyTokenFromServer(internal var sToken: String, internal var msg: String) :
        AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg args: String): String {

            // object to hold the information, which is sent to the server
            val hashMap = HashMap<String, String>()
            hashMap["recaptcha-response"] = sToken
            // Optional params you can use like this
            // hashMap.put("feedback-message", msg)

            // Send the recaptcha response token and receive a Result in return
            return ApiPostHelper.SendParams(VERIFY_ON_API_URL_SERVER, hashMap)
        }

        override fun onPostExecute(result: String?) {

            if (result == null)
                return

            Log.i("onPost::: ", result)
            try {
                val jsonObject = JSONObject(result)
                val success = jsonObject.getBoolean("success")
                val message = jsonObject.getString("message")

                if (success) {
                    // reCaptcha verified successfully.
//                    layoutForm.visibility = View.GONE
//                    Tv_Message.visibility = View.VISIBLE
                } else {
                    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                }

            } catch (e: JSONException) {
                e.printStackTrace()
                Log.i("Error: ", e.message)
            }
        }
    }

    private fun doGoogleLogout() {
        mGoogleSignInClient!!.signOut()
            .addOnCompleteListener(this) {

                Toast.makeText(applicationContext,"Logged Out", Toast.LENGTH_SHORT).show()
            }
    }


    companion object {
        private val TAG = "reCAPTCHA_Activity"

        // TODO: replace the reCAPTCHA KEY with yours //
        private val SAFETY_NET_API_KEY = "6LeKYa0UAAAAAChM_0JcC4nXDqeF3LPK-7UoZASa"


        // TODO: replace the SERVER API URL with yours
        private val VERIFY_ON_API_URL_SERVER =
            "http://apps.galleonsoft.com/api-example/safetynet-recaptcha-verfication.php"
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

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        } else {
            callbackManager?.onActivityResult(requestCode, resultCode, data)
        }

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

                    if(response.body()!!.body!!.username!!.trim().length>0)
                        EndPoints.USERNAME = response.body()!!.body!!.username.toString()
                    else
                        EndPoints.USERNAME = facebookName
                    EndPoints.PROFILE_URL = response.body()!!.body!!.profile_url.toString()
                    EndPoints.USERID = response.body()!!.body!!.id.toString()

                    Log.d("TAGG", EndPoints.ACCESS_TOKEN)

                    val sharedPref: SharedPreferences = getSharedPreferences("mysettings",
                        Context.MODE_PRIVATE);
                    val editor = sharedPref.edit()
                    editor.clear()
                    editor.putString(PREF_FCM,fcm)
                    editor.putBoolean(PREF_STATUS, true)
                    editor.putString(PREF_NAME,EndPoints.USERNAME)
                    editor.putString(PREF_ACCESSTOKEN, EndPoints.ACCESS_TOKEN)
                    editor.putString(PREF_PROFILEURL, EndPoints.PROFILE_URL)
                    editor.putString(PREF_PERCENTAGE, "0")
                    editor.putString(PREF_USERID, response.body()!!.body!!.id!!.toString())
                    editor.putString(PREF_VERIFIED_EMAIL, response.body()!!.body!!.verified_email.toString())
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

//code to be put in bckend server

//safetynet-recaptcha-verfication.php
//
//
//<?php
//$ch = curl_init();
//
//$secretKey = '6LeKYa0UAAAAAGqgMuIkvDm4vRWfskoQWZmfXssK';
//$captchaToken = isset($_POST['recaptcha-response']) && !empty($_POST['recaptcha-response']) ? $_POST['recaptcha-response']: '';
//
//curl_setopt_array($ch, [
//CURLOPT_URL => 'https://www.google.com/recaptcha/api/siteverify',
//CURLOPT_POST => true,
//CURLOPT_POSTFIELDS => [
//'secret' => $secretKey,
//'response' => $captchaToken,
//'remoteip' => $_SERVER['REMOTE_ADDR']
//],
//CURLOPT_RETURNTRANSFER => true
//]);
//
//$output = curl_exec($ch);
//curl_close($ch);
//
//$json = json_decode($output);
//$response = array();
//
//if($json->success){
//    $response['success'] = true;
//    $response['message'] = 'reCAPTCHA verified successfully.';
//}else{
//    $response['success'] = false;
//    $response['message'] = 'Failed to verify reCAPTCHA.';
//}
//
//echo json_encode($response);
//?>