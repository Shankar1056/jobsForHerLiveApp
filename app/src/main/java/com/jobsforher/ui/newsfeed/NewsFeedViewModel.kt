package com.jobsforher.ui.newsfeed

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bigappcompany.healthtunnel.data.repository.RetroClient
import com.jobsforher.R
import com.jobsforher.data.model.*
import com.jobsforher.data.repository.ApiServices
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.util.Utility
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class NewsFeedViewModel(val app : Application) : AndroidViewModel(app) {
    private var groupReq = HashMap<String, String>()
    var errorMessage = MutableLiveData<String>()
    var successMessage = MutableLiveData<String>()
    var jobsResponseList = MutableLiveData<ArrayList<RecommendedJobsBody>>()
    var companiesResponseList = MutableLiveData<ArrayList<RecommendedCompaniesBody>>()
    var groupsResponseList = MutableLiveData<ArrayList<RecommendedGropsBody>>()

    init {
        loadRecommendedJobs()
        loadRecommendedCompanies()
        loadRecommendedMyGroupData()
        loadGroupPosts()
    }

    private fun loadGroupPosts() {

    }

    private fun loadRecommendedMyGroupData() {
        if (Utility.isInternetConnected(app)) {
            groupReq["page_no"] = "1"
            groupReq["page_size"] = "2"

            getGroupsObservable.subscribeWith(getGroupsObserver)
        } else {
            errorMessage.value = app.resources.getString(R.string.no_internet_connection)
        }
    }

    private fun loadRecommendedCompanies() {
        if (Utility.isInternetConnected(app)) {
            groupReq["page_no"] = "1"
            groupReq["page_size"] = "4"

            getCompaniesObservable.subscribeWith(getCompaniesObserver)
        } else {
            errorMessage.value = app.resources.getString(R.string.no_internet_connection)
        }
    }

    fun loadRecommendedJobs() {
        if (Utility.isInternetConnected(app)) {
            groupReq["page_no"] = "1"
            groupReq["page_size"] = "2"

            getJobsObservable.subscribeWith(getJobsObserver)
        } else {
            errorMessage.value = app.resources.getString(R.string.no_internet_connection)
        }
    }


    private val getGroupsObservable: Observable<RecommendedGropsResponse>
        get() = RetroClient.getRetrofit()!!.create(ApiServices::class.java)
            .getRecommendedMyGroupData("Bearer " + EndPoints.ACCESS_TOKEN, groupReq)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private val getGroupsObserver: DisposableObserver<RecommendedGropsResponse>
        get() = object : DisposableObserver<RecommendedGropsResponse>() {
            override fun onNext(@NonNull response: RecommendedGropsResponse) {
                if (Utility.isSuccessCode(response.response_code)) {
                    groupsResponseList.value = response.body
                    if (response.body.isNullOrEmpty()) {
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

    private val getCompaniesObservable: Observable<RecommendedCompaniesResponse>
        get() = RetroClient.getRetrofit()!!.create(ApiServices::class.java)
            .getRecommendedCompanies("Bearer " + EndPoints.ACCESS_TOKEN, groupReq)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private val getCompaniesObserver: DisposableObserver<RecommendedCompaniesResponse>
        get() = object : DisposableObserver<RecommendedCompaniesResponse>() {
            override fun onNext(@NonNull response: RecommendedCompaniesResponse) {
                if (Utility.isSuccessCode(response.response_code)) {
                    companiesResponseList.value = response.body
                    if (response.body.isNullOrEmpty()) {
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

    private val getJobsObservable: Observable<RecommendedJobsResponse>
        get() = RetroClient.getRetrofit()!!.create(ApiServices::class.java)
            .getRecommendedJobs("Bearer " + EndPoints.ACCESS_TOKEN, groupReq)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private val getJobsObserver: DisposableObserver<RecommendedJobsResponse>
        get() = object : DisposableObserver<RecommendedJobsResponse>() {
            override fun onNext(@NonNull response: RecommendedJobsResponse) {
                if (Utility.isSuccessCode(response.response_code)) {
                    jobsResponseList.value = response.body
                    if (response.body.isNullOrEmpty()) {
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


}