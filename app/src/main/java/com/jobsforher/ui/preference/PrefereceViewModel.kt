package com.jobsforher.ui.preference

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.jobsforher.R
import com.jobsforher.data.model.*
import com.jobsforher.data.repository.ApiServices
import com.jobsforher.data.repository.RetroClient
import com.jobsforher.helpers.Constants
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
    var preferenceSuccessMessage = MutableLiveData<String>()
    var cityList = MutableLiveData<ArrayList<PreferenceCityBody>>()
    var functionalAreaList = MutableLiveData<ArrayList<PreferenceFunctionalAreaBody>>()

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

    fun getFunctionalArea(name: String) {
        if (name == Constants.TYPE_AREAS) {
            if (Utility.isInternetConnected(app)) {
                getFunctionalAreaObservable.subscribeWith(getFunctionalAreaObserver)
            } else {
                errorMessage.value = app.resources.getString(R.string.no_internet_connection)
            }
        } else if (name == Constants.TYPE_INDUSTRIES) {
            if (Utility.isInternetConnected(app)) {
                getFunctionalIndustriesObservable.subscribeWith(getFunctionalIndustriesObserver)
            } else {
                errorMessage.value = app.resources.getString(R.string.no_internet_connection)
            }
        } else if (name == Constants.TYPE_JOB) {
            var tempList = ArrayList<PreferenceFunctionalAreaBody>()
            val jobList: Array<String> = app.resources.getStringArray(R.array.jobType)
            for (i in jobList.indices) {
                tempList.add(PreferenceFunctionalAreaBody(jobList[i], i, true, false))
            }
            functionalAreaList.value = tempList
        }
    }

    fun addUpdateFunctionalArea(
        fuctioalAreaList: java.util.ArrayList<PreferenceFunctionalAreaBody>,
        name: String
    ) {

        var preferred_functional_area: String = ""
        for (item in fuctioalAreaList) {
            if (item.isClicked == true) {
                if (preferred_functional_area == "") {
                    preferred_functional_area = item.name.toString()
                } else {
                    preferred_functional_area = "$preferred_functional_area, ${item.name}"
                }
            }
        }

        if (preferred_functional_area.isNullOrEmpty()) {
            errorMessage.value = app.resources.getString(R.string.select_city_to_add)
            return
        }

        if (Utility.isInternetConnected(app)) {
            if (name == Constants.TYPE_AREAS) {
                groupReq["preferred_functional_area"] = preferred_functional_area
            } else if (name == Constants.TYPE_INDUSTRIES) {
                groupReq["preferred_industry"] = preferred_functional_area
            } else if (name == Constants.TYPE_JOB) {
                groupReq["preferred_job_type"] = preferred_functional_area
            }
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
                    preferenceSuccessMessage.value = response.message
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

    private val getFunctionalIndustriesObservable: Observable<PreferenceFunctionalArea>
        get() = RetroClient.getRetrofit()!!.create(ApiServices::class.java)
            .getFunctionalIndustries("Bearer " + EndPoints.ACCESS_TOKEN)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private val getFunctionalIndustriesObserver: DisposableObserver<PreferenceFunctionalArea>
        get() = object : DisposableObserver<PreferenceFunctionalArea>() {
            override fun onNext(@NonNull response: PreferenceFunctionalArea) {
                if (Utility.isSuccessCode(response.response_code)) {
                    functionalAreaList.value = response.body
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

    private val getFunctionalAreaObservable: Observable<PreferenceFunctionalArea>
        get() = RetroClient.getRetrofit()!!.create(ApiServices::class.java)
            .getFunctionalArea("Bearer " + EndPoints.ACCESS_TOKEN)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private val getFunctionalAreaObserver: DisposableObserver<PreferenceFunctionalArea>
        get() = object : DisposableObserver<PreferenceFunctionalArea>() {
            override fun onNext(@NonNull response: PreferenceFunctionalArea) {
                if (Utility.isSuccessCode(response.response_code)) {
                    functionalAreaList.value = response.body
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