package com.jobsforher.ui.newsfeed

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bigappcompany.healthtunnel.data.repository.RetroClient
import com.jobsforher.R
import com.jobsforher.activities.SplashActivity
import com.jobsforher.data.model.*
import com.jobsforher.data.repository.ApiServices
import com.jobsforher.helpers.Constants
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
    var newsPostResponseList = MutableLiveData<ArrayList<NewsPostBody>>()
    var voteResponse = MutableLiveData<VoteBody>()
    var homeBannerResponse = MutableLiveData<ArrayList<HomeBannerData>>()

    init {
        loadHomeBanner()
        loadRecommendedJobs()
        loadRecommendedCompanies()
        loadRecommendedMyGroupData()
        loadGroupPosts()
    }

    private fun loadHomeBanner() {
        var bannerList = ArrayList<HomeBannerData>()
        bannerList.add(HomeBannerData(R.drawable.news_1))
        bannerList.add(HomeBannerData(R.drawable.news_2))
        bannerList.add(HomeBannerData(R.drawable.news_3))
        bannerList.add(HomeBannerData(R.drawable.news_4))
        bannerList.add(HomeBannerData(R.drawable.news_5))
        homeBannerResponse.value = bannerList
    }

    private fun loadGroupPosts() {
        if (Utility.isInternetConnected(app)) {
            groupReq["page_no"] = "1"
            groupReq["page_size"] = "2"

            getNewsPostObservable.subscribeWith(getNewsPostObserver)
        } else {
            errorMessage.value = app.resources.getString(R.string.no_internet_connection)
        }
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

    fun upVoteClicked(id: Int?) {
        if (Utility.isInternetConnected(app)) {
            groupReq["entity_type"] = Constants.POST
            groupReq["entity_id"] = id.toString()
            groupReq["vote_type"] = Constants.UPVOTE

            doUpVoteObservable.subscribeWith(doUpVoteObserver)
        } else {
            errorMessage.value = app.resources.getString(R.string.no_internet_connection)
        }
    }

    fun downVoteClicked(id: Int?) {
        if (Utility.isInternetConnected(app)) {
            groupReq["entity_type"] = Constants.POST
            groupReq["entity_id"] = id.toString()
            groupReq["vote_type"] = Constants.UPVOTE

            doDownVoteObservable.subscribeWith(doDownVoteObserver)
        } else {
            errorMessage.value = app.resources.getString(R.string.no_internet_connection)
        }
    }



    private val doDownVoteObservable: Observable<VoteResponseModel>
        get() = RetroClient.getRetrofit()!!.create(ApiServices::class.java)
            .downVoteData("Bearer " + EndPoints.ACCESS_TOKEN, groupReq)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private val doDownVoteObserver: DisposableObserver<VoteResponseModel>
        get() = object : DisposableObserver<VoteResponseModel>() {
            override fun onNext(@NonNull response: VoteResponseModel) {
                if (Utility.isSuccessCode(response.response_code)) {

                    if (response.body != null) {
                        voteResponse.value = response.body
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

    private val doUpVoteObservable: Observable<VoteResponseModel>
        get() = RetroClient.getRetrofit()!!.create(ApiServices::class.java)
            .upVoteData("Bearer " + EndPoints.ACCESS_TOKEN, groupReq)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private val doUpVoteObserver: DisposableObserver<VoteResponseModel>
        get() = object : DisposableObserver<VoteResponseModel>() {
            override fun onNext(@NonNull response: VoteResponseModel) {
                if (Utility.isSuccessCode(response.response_code)) {
                    if (response.body != null) {
                        voteResponse.value = response.body
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

    private val getNewsPostObservable: Observable<NewsPostResponse>
        get() = RetroClient.getRetrofit()!!.create(ApiServices::class.java)
            .getNewsPostData("Bearer " + EndPoints.ACCESS_TOKEN, groupReq)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private val getNewsPostObserver: DisposableObserver<NewsPostResponse>
        get() = object : DisposableObserver<NewsPostResponse>() {
            override fun onNext(@NonNull response: NewsPostResponse) {
                if (Utility.isSuccessCode(response.response_code)) {
                    newsPostResponseList.value = response.body
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