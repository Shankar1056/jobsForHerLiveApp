package com.jobsforher.ui.login

import android.app.Application
import android.os.Build
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.jobsforher.R
import com.jobsforher.data.repository.ApiServices
import com.jobsforher.data.repository.RetroClient
import com.jobsforher.helpers.Constants
import com.jobsforher.helpers.HelperMethods
import com.jobsforher.network.responsemodels.RegisterSocialSignInResponse
import com.jobsforher.network.responsemodels.RegisterSocialTokenResponse
import com.jobsforher.network.responsemodels.SignInResponse
import com.jobsforher.network.responsemodels.SignUpResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.util.Preference
import com.jobsforher.util.Utility
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.HashMap

class LoginViewModel(val app: Application) : AndroidViewModel(app) {

    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var mobile = MutableLiveData<String>()
    var fullname = MutableLiveData<String>()
    var errorMessage = MutableLiveData<String>()
    var inputError = MutableLiveData<String>()
    var checkSocial = MutableLiveData<String>()
    var socialType: String? = null

    private var groupReq = HashMap<String, String>()
    private var socialReq = HashMap<String, String>()
    var selectedSocialType = MutableLiveData<SocialType>()

    var profile_percentage = MutableLiveData<String>()
    var fcm: String = ""


    init {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d("TAGG", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                val token = task.result?.token
                // Log and toast
                val msg = app.getString(R.string.msg_token_fmt, token)
                fcm = token.toString()
                Log.d("TAGG", "ID IS:" + token)
            })
    }

    fun onPasswordTextChanged(
        s: CharSequence, start: Int, before: Int,
        count: Int
    ) {
        password.value = s.toString()
    }

    fun onEmailTextChanged(
        s: CharSequence, start: Int, before: Int,
        count: Int
    ) {
        email.value = s.toString()
    }

    fun onPasswordCreateTextChanged(
        s: CharSequence, start: Int, before: Int,
        count: Int
    ) {
        password.value = s.toString()
    }

    fun onMobileTextChanged(
        s: CharSequence, start: Int, before: Int,
        count: Int
    ) {
        mobile.value = s.toString()
    }

    fun onFullNameTextChanged(
        s: CharSequence, start: Int, before: Int,
        count: Int
    ) {
        fullname.value = s.toString()
    }

    fun loginClicked(view: View) {

        if (email.value.isNullOrEmpty() || password.value.isNullOrEmpty()) {
            inputError.value = "Please enter all the fields" //TODO
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
            inputError.value = "Please enter valid email id" //TODO
            return
        }

        if (Utility.isInternetConnected(app)) {
            groupReq["email_mobile"] = email.value.toString()
            groupReq["password"] = HelperMethods.md5(password.value.toString())
            groupReq["ip"] = "72.197.243.187"
            groupReq["device"] = Utility.getDeviceID(app)
            groupReq["platform"] = Build.MODEL
            groupReq["browser"] = ""
            loginObservable.subscribeWith(loginObserver)
        } else {
            errorMessage.value = app.resources.getString(R.string.no_internet_connection)
        }
    }

    fun socialLoginClicked(view: View) {

        val installationId = Utility.getDeviceID(app) + Utility.getCurrentDateTime()

        if (Utility.isInternetConnected(app)) {
            socialReq["social_token"] = installationId
            socialType = view.tag.toString()
            socialloginObservable.subscribeWith(socialloginObserver)
        } else {
            errorMessage.value = app.resources.getString(R.string.no_internet_connection)
        }
    }

    fun updateSocialLoginDetails(email: String, socialToken: String, type: String, id: String) {

        if (Utility.isInternetConnected(app)) {
            groupReq["email"] = email
            groupReq["social_token"] = socialToken
            groupReq["social_login"] = type
            groupReq["device_id"] = id

            updatesocialloginObservable.subscribeWith(updatesocialloginObserver)
        } else {
            errorMessage.value = app.resources.getString(R.string.no_internet_connection)
        }
    }

    fun registerClicked(view: View) {

        if (email.value.isNullOrEmpty() || password.value.isNullOrEmpty() || mobile.value.isNullOrEmpty() || fullname.value.isNullOrEmpty()) {
            inputError.value = "Please enter all the fields" //TODO
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
            inputError.value = "Please enter valid email id" //TODO
            return
        }
        if (!Patterns.PHONE.matcher(mobile.value).matches()) {
            inputError.value = "Please enter valid mobile number" //TODO
            return
        }
        if (password?.value!!.length < 6) {
            inputError.value = "Password must be atleast 6 characters" //TODO
            return
        }
        /*if (Utility.isValidMobileNumber(mobile.value.toString())) {
            inputError.value = "Please enter valid mobile number" //TODO
            return
        }*/

        if (Utility.isInternetConnected(app)) {
            groupReq["email"] = email.value.toString()
            groupReq["password"] = HelperMethods.md5(password.value.toString())
            groupReq["ip"] = "72.197.243.187"
            groupReq["username"] = fullname.value.toString()
            groupReq["role"] = "user"
            groupReq["phone_no"] = mobile.value.toString()
            registerObservable.subscribeWith(registerObserver)
        } else {
            errorMessage.value = app.resources.getString(R.string.no_internet_connection)
        }
    }


    private val loginObservable: Observable<SignInResponse>
        get() = RetroClient.getRetrofit()!!.create(ApiServices::class.java)
            .doSignIn("application/json", groupReq)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private val socialloginObservable: Observable<RegisterSocialTokenResponse>
        get() = RetroClient.getRetrofit()!!.create(ApiServices::class.java)
            .registerSocialToken("application/json", socialReq)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private val updatesocialloginObservable: Observable<RegisterSocialSignInResponse>
        get() = RetroClient.getRetrofit()!!.create(ApiServices::class.java)
            .registerSocialSignIn("application/json", groupReq)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private val registerObservable: Observable<SignUpResponse>
        get() = RetroClient.getRetrofit()!!.create(ApiServices::class.java)
            .doSignUp("application/json", groupReq)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private val loginObserver: DisposableObserver<SignInResponse>
        get() = object : DisposableObserver<SignInResponse>() {
            override fun onNext(@NonNull response: SignInResponse) {
                if (Utility.isSuccessCode(response.responseCode)) {
                    if (response.body != null) {
                        errorMessage.value = response.message

                        EndPoints.ACCESS_TOKEN = response.auth?.accessToken.toString()
                        EndPoints.profileUrl = response.body?.profile_url.toString()

                        EndPoints.PROFILE_URL = response.body?.profile_url.toString()

                        EndPoints.USERNAME = response.body?.username.toString()
                        EndPoints.PROFILE_ICON = response.body?.profile_icon.toString()
                        EndPoints.PHONE_NO = response.body?.phoneNo.toString()
                        EndPoints.USERID = response.body?.id.toString()
                        EndPoints.PROFILE_PERC = response.body!!.profile_percentage!!.toString()

                        addSharedPreference()
                        profile_percentage.value = response.body!!.profile_percentage!!.toString()

                    } else {
                        errorMessage.value = response.message
                    }
                } else {
                    errorMessage.value = response.message
                }
            }

            override fun onError(@NonNull e: Throwable) {
                e.printStackTrace()
                errorMessage.value = e.message
            }

            override fun onComplete() {}
        }

    private val socialloginObserver: DisposableObserver<RegisterSocialTokenResponse>
        get() = object : DisposableObserver<RegisterSocialTokenResponse>() {
            override fun onNext(@NonNull response: RegisterSocialTokenResponse) {
                if (Utility.isSuccessCode(response.responseCode)) {
                    if (response.body != null) {
                        errorMessage.value = response.message
                        checkSocial.value = response!!.body!!.social_token.toString()
                        checkSocialType()

                    } else {
                        errorMessage.value = response.message
                    }
                } else {
                    errorMessage.value = response.message
                }
            }

            override fun onError(@NonNull e: Throwable) {
                e.printStackTrace()
                errorMessage.value = e.message
            }

            override fun onComplete() {}
        }

    private val updatesocialloginObserver: DisposableObserver<RegisterSocialSignInResponse>
        get() = object : DisposableObserver<RegisterSocialSignInResponse>() {
            override fun onNext(@NonNull response: RegisterSocialSignInResponse) {
                if (Utility.isSuccessCode(response.responseCode)) {
                    if (response.body != null) {
                        errorMessage.value = response.message

                        updateEndPoint(response)

                        addSharedPreference()
                        profile_percentage.value = EndPoints.PROFILE_PERC


                    } else {
                        errorMessage.value = response.message
                    }
                } else {
                    errorMessage.value = response.message
                }
            }

            override fun onError(@NonNull e: Throwable) {
                e.printStackTrace()
                errorMessage.value = e.message
            }

            override fun onComplete() {}
        }

    private fun addSharedPreference() {
        Preference.setPreferences(Constants.FCM_TOKEN, fcm)
        Preference.setBooleanPreference(Constants.LOGIN_STATUS, true)
        Preference.setPreferences(Constants.NAME, EndPoints.USERNAME)
        Preference.setPreferences(Constants.ACCESS_TOKEN, EndPoints.ACCESS_TOKEN)
        Preference.setPreferences(Constants.PROFILEURL, EndPoints.PROFILE_URL)
        Preference.setPreferences(Constants.ICON, EndPoints.PROFILE_ICON)
        Preference.setPreferences(Constants.PERCENTAGE, EndPoints.PROFILE_PERC)
        Preference.setPreferences(Constants.PHONE, EndPoints.PHONE_NO)
        Preference.setPreferences(Constants.USERID, EndPoints.USERID)
        Preference.setPreferences(Constants.EMAIL, EndPoints.VERIFIED_EMAIL)

    }

    private fun updateEndPoint(response: RegisterSocialSignInResponse) {

        EndPoints.ACCESS_TOKEN = response.auth!!.accessToken.toString()

        EndPoints.profileUrl = response.body!!.profile_url.toString()
        EndPoints.PROFILE_URL = response.body!!.profile_url.toString()

        if (response.body!!.username!!.trim().length > 0)
            EndPoints.USERNAME = response.body!!.username.toString()
        EndPoints.USERID = response.body!!.id.toString()

        EndPoints.PROFILE_PERC = response.body!!.profile_percentage!!.toString()
        EndPoints.PROFILE_ICON = response.body!!.profile_icon.toString()
        EndPoints.VERIFIED_EMAIL = response.body!!.verified_email.toString()

    }

    private val registerObserver: DisposableObserver<SignUpResponse>
        get() = object : DisposableObserver<SignUpResponse>() {
            override fun onNext(@NonNull response: SignUpResponse) {
                if (Utility.isSuccessCode(response.responseCode)) {
                    if (response.body != null) {
                        errorMessage.value = response.message
                        EndPoints.ACCESS_TOKEN = response.auth!!.access_token.toString()

                        EndPoints.profileUrl = response.body!!.profile_url.toString()

                        EndPoints.USERNAME = response.body!!.username.toString()
                        EndPoints.PROFILE_URL = response.body!!.profile_url.toString()
                        EndPoints.PHONE_NO = response.body!!.phoneNo.toString()
                        EndPoints.USERID = response.body!!.id.toString()

                        Log.d("TAGG", EndPoints.USERNAME)

                        addSharedPreference()
                        profile_percentage.value = "0"

                    } else {
                        errorMessage.value = response.message
                    }
                } else {
                    errorMessage.value = response.message
                }
            }

            override fun onError(@NonNull e: Throwable) {
                e.printStackTrace()
                errorMessage.value = e.message
            }

            override fun onComplete() {}
        }

    fun checkSocialType() {
        if (socialType == "facebook") {
            selectedSocialType.value = SocialType.FACEBOOK
        }
        if (socialType == "gmail") {
            selectedSocialType.value = SocialType.GMAIL
        }
        if (socialType == "linkedin") {
            selectedSocialType.value = SocialType.LINKEDIN
        }
    }

    enum class SocialType {
        FACEBOOK,
        GMAIL,
        LINKEDIN
    }
}

