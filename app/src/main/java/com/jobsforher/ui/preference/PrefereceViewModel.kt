package com.jobsforher.ui.preference

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.jobsforher.R
import com.jobsforher.data.model.PreferenceAddResponse
import com.jobsforher.data.model.PreferenceCityBody
import com.jobsforher.data.model.PreferenceCityResponse
import com.jobsforher.data.repository.ApiServices
import com.jobsforher.data.repository.RetroClient
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.util.Utility
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class PrefereceViewModel(val app: Application) : AndroidViewModel(app) {
    private var groupReq = HashMap<String, String>()
    var errorMessage = MutableLiveData<String>()
    var successMessage = MutableLiveData<String>()
    var cityList = MutableLiveData<ArrayList<PreferenceCityBody>>()

    fun getCity() {
        if (Utility.isInternetConnected(app)) {
            getCityObservable.subscribeWith(getCityObserver)
        } else {
            errorMessage.value = app.resources.getString(R.string.no_internet_connection)
        }
    }

    fun addUpdateCity(allCityList: java.util.ArrayList<PreferenceCityBody>) {
        var preferred_city : String = ""
        for (item in allCityList){
            if (item.isClicked == true){
                if (preferred_city == ""){
                    preferred_city = item.value.toString()
                } else {
                    preferred_city = "$preferred_city, ${item.value}"
                }
            }
        }

        if (preferred_city.isNullOrEmpty()){
            errorMessage.value = app.resources.getString(R.string.select_city_to_add)
            return
        }

        if (Utility.isInternetConnected(app)) {
            groupReq["preferred_city"] = preferred_city
            addCityObservable.subscribeWith(addCityObserver)
        } else {
            errorMessage.value = app.resources.getString(R.string.no_internet_connection)
        }
    }

    private val addCityObservable: Observable<PreferenceAddResponse>
        get() = RetroClient.getRetrofit()!!.create(ApiServices::class.java)
            .saveCityPreference("Bearer " + EndPoints.ACCESS_TOKEN, groupReq)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private val addCityObserver: DisposableObserver<PreferenceAddResponse>
        get() = object : DisposableObserver<PreferenceAddResponse>() {
            override fun onNext(@NonNull response: PreferenceAddResponse) {
                if (Utility.isSuccessCode(response.response_code)) {
                    successMessage.value = response.message
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

    private val getCityObservable: Observable<PreferenceCityResponse>
        get() = RetroClient.getRetrofit()!!.create(ApiServices::class.java)
            .getCities("Bearer " + EndPoints.ACCESS_TOKEN)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private val getCityObserver: DisposableObserver<PreferenceCityResponse>
        get() = object : DisposableObserver<PreferenceCityResponse>() {
            override fun onNext(@NonNull response: PreferenceCityResponse) {
                if (Utility.isSuccessCode(response.response_code)) {
                    cityList.value = response.body
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

}