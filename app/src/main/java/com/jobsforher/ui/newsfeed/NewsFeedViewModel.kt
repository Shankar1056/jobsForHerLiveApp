package com.jobsforher.ui.newsfeed

import android.app.Application
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.jobsforher.R
import com.jobsforher.data.model.*
import com.jobsforher.data.model.common_response.JobsForHerPagination
import com.jobsforher.data.repository.ApiServices
import com.jobsforher.data.repository.RetroClient
import com.jobsforher.helpers.Constants
import com.jobsforher.helpers.HelperMethods
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.network.responsemodels.NotificationBubbleResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.util.Utility
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.io.IOException

class NewsFeedViewModel(val app : Application) : AndroidViewModel(app) {
    private var groupReq = HashMap<String, String>()
    var errorMessage = MutableLiveData<String>()
    var successMessage = MutableLiveData<String>()
    var isResumeUploaded = MutableLiveData<Boolean>()
    var jobsResponseList = MutableLiveData<ArrayList<RecommendedJobsBody>>()
    var companiesResponseList = MutableLiveData<ArrayList<RecommendedCompaniesBody>>()
    var groupsResponseList = MutableLiveData<ArrayList<RecommendedGropsBody>>()
    var newsPostResponseList = MutableLiveData<ArrayList<NewsPostBody>>()
    var newsPostpagination = JobsForHerPagination()
    var voteResponse = MutableLiveData<VoteBody>()
    var homeBannerResponse = MutableLiveData<ArrayList<HomeBannerData>>()
    var notificationCount = MutableLiveData<Int>()

    init {
        loadHomeBanner()
        loadRecommendedJobs()
        loadRecommendedCompanies()
        loadRecommendedMyGroupData()
        loadGroupPosts("1", Constants.MAXIMUM_PAGINATION_COUNT)
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

    fun loadGroupPosts(pageNo: String, maxRecordToLoad: String) {
        if (Utility.isInternetConnected(app)) {
            groupReq["page_no"] = pageNo
            groupReq["page_size"] = maxRecordToLoad

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

    fun loadNotificationbubble() {
        if (Utility.isInternetConnected(app)) {
            notificationObservable.subscribeWith(notificationObserver)
        } else {
            errorMessage.value = app.resources.getString(R.string.no_internet_connection)
        }
    }

    fun startIntentToOpenFile(activity: NewsFeedActivity) {
        val mimeTypes = arrayOf(
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
            "application/pdf"
        )

        val chooseFile: Intent
        var intent: Intent = Intent()
        chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE)
        chooseFile.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        chooseFile.type = if (mimeTypes.size === 1) mimeTypes[0] else "*/*"
        if (mimeTypes.size > 0) {
            chooseFile.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        }
        //chooseFile.type = ("application/pdf|application/msword")
        intent = Intent.createChooser(chooseFile, "Choose a file")
        activity.startActivityForResult(intent, GALLERY_PDF)

    }

    fun uploadResume(data: Intent, context: NewsFeedActivity) {
        try {
            val fileName = HelperMethods.getFilePath(context, data.data)
            val file = data?.data?.let {
                HelperMethods.getFile(
                    context,
                    it, fileName.toString()
                )
            }

            if (file != null) {
                val picturepath = fileName.toString()
                val imageEncoded = HelperMethods.convertToBase64(file!!)

                if (Utility.isInternetConnected(app)) {
                    groupReq["title"] = Constants.POST
                    groupReq["resume_filename"] = picturepath
                    groupReq["resume_file"] = imageEncoded

                    uploadResumeObservable.subscribeWith(uploadResumeObserver)
                } else {
                    errorMessage.value = app.resources.getString(R.string.no_internet_connection)
                }


            } else {
                ToastHelper.makeToast(context, "Unable to convert as Base64")
            }

        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("TAGG", "STACK" + e.printStackTrace())
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
        }
    }


    private val uploadResumeObservable: Observable<ResumeUploadResponse>
        get() = RetroClient.getRetrofit()!!.create(ApiServices::class.java)
            .uploadResume("Bearer " + EndPoints.ACCESS_TOKEN, groupReq)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private val uploadResumeObserver: DisposableObserver<ResumeUploadResponse>
        get() = object : DisposableObserver<ResumeUploadResponse>() {
            override fun onNext(@NonNull response: ResumeUploadResponse) {
                if (Utility.isSuccessCode(response.response_code)) {
                    successMessage.value = response.message
                    isResumeUploaded.value = true
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

    private val notificationObservable: Observable<NotificationBubbleResponse>
        get() = RetroClient.getRetrofit()!!.create(ApiServices::class.java)
            .getNotificationBubble("Bearer " + EndPoints.ACCESS_TOKEN)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private val notificationObserver: DisposableObserver<NotificationBubbleResponse>
        get() = object : DisposableObserver<NotificationBubbleResponse>() {
            override fun onNext(@NonNull response: NotificationBubbleResponse) {
                if (Utility.isSuccessCode(response.response_code)) {

                    notificationCount.value = response.body?.new_notification
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
                    newsPostpagination = response.pagination!!
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

    companion object {
        val GALLERY_PDF = 3
    }
}