package com.jobsforher.ui.auth

import android.app.Application
import android.util.Patterns
import android.view.View
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.jobsforher.R
import com.jobsforher.data.repository.ApiServices
import com.jobsforher.data.repository.RetroClient
import com.jobsforher.util.Utility
import entertainment.minersinc.tfhy.network.responsemodels.ForgotPasswordResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class ForgotPasswordViewModel(val app: Application) : AndroidViewModel(app) {
    private var paramsReq = HashMap<String, String>()
    var email = MutableLiveData<String>()
    var errorMessage = MutableLiveData<String>()
    var inputError = MutableLiveData<String>()

    fun onEmailTextChanged(
        s: CharSequence, start: Int, before: Int,
        count: Int
    ) {
        email.value = s.toString()
    }

    fun resetPasswordClicked(view: View) {

        if (email.value.isNullOrEmpty()) {
            inputError.value = "Please enter the email id"
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
            inputError.value = "Please enter valid email id"
            return
        }

        if (Utility.isInternetConnected(app)) {
            paramsReq["email"] = email.value.toString()
            paramsReq["role"] = "user"

            resetPasswordObservable.subscribeWith(resetPasswordObserver)
        } else {
            errorMessage.value = app.resources.getString(R.string.no_internet_connection)
        }
    }

    private val resetPasswordObservable: Observable<ForgotPasswordResponse>
        get() = RetroClient.getRetrofit()!!.create(ApiServices::class.java)
            .passwodReset("application/json", paramsReq)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private val resetPasswordObserver: DisposableObserver<ForgotPasswordResponse>
        get() = object : DisposableObserver<ForgotPasswordResponse>() {
            override fun onNext(@NonNull response: ForgotPasswordResponse) {
                if (Utility.isSuccessCode(response.message?.response_code)) {
                    if (response.message?.message != null) {
                        errorMessage.value =
                            app.resources.getString(R.string.text_password_reset_link_sent)
                    } else {
                        errorMessage.value = response.message?.message
                    }
                } else {
                    errorMessage.value = response.message?.message
                }
            }

            override fun onError(@NonNull e: Throwable) {
                e.printStackTrace()
                errorMessage.value = e.message
            }

            override fun onComplete() {}
        }
}

