package com.jobsforher.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.jobsforher.R
import com.jobsforher.activities.HomePage
import com.jobsforher.activities.HomePagePreferences
import com.jobsforher.activities.WebActivity
import com.jobsforher.databinding.ActivitySigninBinding
import com.jobsforher.helpers.Constants
import com.jobsforher.helpers.Logger
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.ui.login.LoginViewModel
import com.jobsforher.util.Utility
import com.kusu.linkedinlogin.Linkedin
import com.kusu.linkedinlogin.LinkedinLoginListener
import com.kusu.linkedinlogin.model.SocialUser
import com.linkedin.platform.LISessionManager
import kotlinx.android.synthetic.main.activity_signin.*
import women.jobs.jobsforher.activities.BaseActivity
import java.util.*


class LoginActivity : BaseActivity() {

    private val TAG = javaClass.simpleName
    private var callbackManager: CallbackManager? = null
    val viewModel: LoginViewModel by viewModels()
    var RC_SIGN_IN = 1
    var mGoogleSignInClient: GoogleSignInClient? = null
    var facebookName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySigninBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_signin)
        binding?.viewmodel = viewModel
        binding?.lifecycleOwner = this

        /*if (intent.getIntExtra("isLogin", 0) == 0) {
            signup_layout.visibility =
                View.VISIBLE
        } else signin_layout.visibility = View.VISIBLE*/

        if (intent.getStringExtra("from") == "login") {
            signin_layout.visibility = View.VISIBLE
            signup_layout.visibility = View.GONE
        } else {
            signin_layout.visibility = View.GONE
            signup_layout.visibility = View.VISIBLE
        }

        initGoogleLogin()
        initObservables()
        clickListener()

    }

    private fun initObservables() {

        viewModel.errorMessage.observe(this, Observer {
            Utility.showToast(this, it)

        })

        viewModel.selectedSocialType.observe(this, Observer {
            when (it) {
                LoginViewModel.SocialType.FACEBOOK -> doFacebookLogin(viewModel.checkSocial.value.toString())
                LoginViewModel.SocialType.GMAIL -> {
                    doGoogleLogout()
                    doGoogleLogin()
                }
                LoginViewModel.SocialType.LINKEDIN -> doLinkedInLogin()
            }
        })

        viewModel.inputError.observe(this, Observer {
            Utility.showToast(this, it)
        })

        viewModel.profile_percentage.observe(this, Observer {
            when (it) {
                "0" -> {
                    startActivity(Intent(applicationContext, HomePage::class.java))
                    finishAffinity()
                }
                else -> {
                    startActivity(
                        Intent(applicationContext, HomePagePreferences::class.java)
                            .putExtra("userID", 0)
                    )
                    finishAffinity()
                }
            }
        })
    }

    private fun clickListener() {

        signup_text.setText(
            HtmlCompat.fromHtml(
                application.getString(R.string.login_terms_conditions),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        )

        signup_text.setOnClickListener {
            intent = Intent(applicationContext, WebActivity::class.java)
            intent.putExtra("value", R.string.weblink_terms_conditions)
            startActivity(intent)
        }

        btn_Back.setOnClickListener {
            onBackPressed()
        }

        forgotpasswordid.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        signuphere.setOnClickListener {
            signin_layout.visibility = View.GONE
            signup_layout.visibility = View.VISIBLE
        }

        loginhere.setOnClickListener {
            signin_layout.visibility = View.VISIBLE
            signup_layout.visibility = View.GONE
        }
    }

    fun doGoogleLogin() {
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
                    EndPoints.USERNAME = facebookName.toString()
                    var email: String = ""
                    val request =
                        GraphRequest.newMeRequest(loginResult.accessToken) { `object`, response ->
                            try {
                                //here is the data that you want
                                Logger.d("FBLOGIN_JSON_RES", `object`.toString())

                                if (`object`.has("email")) {
                                    email = `object`.getString("email")
                                    viewModel.updateSocialLoginDetails(
                                        email, socialToken, "facebook",
                                        Utility.getDeviceID(applicationContext)
                                    )
                                } else {
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

    fun doLinkedInLogin() {

        Linkedin.initialize(
            context = applicationContext,
            clientId = Constants.LINKEDIN_CLIENTID, //Client Id of your linkedin app like-> "47sf33fjflf"
            clientSecret = Constants.LINKEDIN_CLIENTSECRET, //Client Secret of your linkedin app like-> "Udhfksfeu324uh4"
            redirectUri = Constants.LINKEDIN_REDIRECTURI, //Redirect url which has to be add in your linkedin app like-> "https://example.com/auth/linkedin/callback"
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


                viewModel.updateSocialLoginDetails(
                    socialUser.email.toString(),
                    viewModel.checkSocial.value.toString(), "linkedin",
                    Utility.getDeviceID(applicationContext)
                )
            }
        })


    }

    //logout functionality for Facebook signin
    fun doFacebookLogout() {
        LoginManager.getInstance().logOut()
    }

    fun initGoogleLogin() {
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
        } else {
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

            viewModel.updateSocialLoginDetails(
                account.email.toString(), viewModel.checkSocial.value.toString(), "gmail",
                Utility.getDeviceID(applicationContext)
            )

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Logger.d(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    // logout functionality for google signin
    private fun doGoogleLogout() {
        if (mGoogleSignInClient != null) {
            mGoogleSignInClient!!.signOut()
                .addOnCompleteListener(this) {

                    Toast.makeText(applicationContext, "Logged Out", Toast.LENGTH_SHORT).show()
                }
        }
    }

}
