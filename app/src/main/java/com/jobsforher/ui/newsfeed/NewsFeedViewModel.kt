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
import com.jobsforher.network.retrofithelpers.Test
import com.jobsforher.util.Preference
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
    var joinGroupResponse = MutableLiveData<JoinGroupBody>()
    var followCompanyResponse = MutableLiveData<CompanyFollowBody>()
    var jobsResponseList = MutableLiveData<ArrayList<RecommendedJobsBody>>()
    var companiesResponseList = MutableLiveData<ArrayList<RecommendedCompaniesBody>>()
    var groupsResponseList = MutableLiveData<ArrayList<RecommendedGropsBody>>()
    var newsPostResponseList = MutableLiveData<ArrayList<NewsPostBody>>()
    var newsPostpagination = JobsForHerPagination()
    var voteResponse = MutableLiveData<VoteBody>()
    var homeBannerResponse = MutableLiveData<ArrayList<HomeBannerData>>()
    var notificationCount = MutableLiveData<Int>()
    var switchPreferenceName = MutableLiveData<PreferenceName>()
    var allPreferenceUpdated = MutableLiveData<Boolean>()
    var recommendedEventsList = MutableLiveData<ArrayList<RecommendedEventsBody>>()
    private var joinGroupId: Int = 0

    init {
        loadHomeBanner()
        loadRecommendedJobs()
        loadRecommendedCompanies()
        loadRecommendedMyGroupData()
        loadGroupPosts("1", Constants.MAXIMUM_PAGINATION_COUNT)
        loadRecommendedEvents("1", Constants.MAXIMUM_PAGINATION_COUNT)
        loadPreference()
    }

    private fun loadRecommendedEvents(pageNo: String, maxRecordToLoad: String) {
        groupReq["page_no"] = pageNo
        groupReq["page_size"] = maxRecordToLoad
        groupReq["payment_type"] = "both"
        if (Utility.isInternetConnected(app)) {
            getRecomEventsObservable.subscribeWith(getRecomEventsObserver)
        } else {
            errorMessage.value = app.resources.getString(R.string.no_internet_connection)
        }
    }

    fun loadPreference() {
        if (Utility.isInternetConnected(app)) {
            getPreferenceObservable.subscribeWith(getPreferenceObserver)
        } else {
            errorMessage.value = app.resources.getString(R.string.no_internet_connection)
        }
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
            groupReq["vote_type"] = Constants.DOWNUPVOTE

            // doDownVoteObservable.subscribeWith(doDownVoteObserver)
            doUpVoteObservable.subscribeWith(doUpVoteObserver)
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


    private val getRecomEventsObservable: Observable<RecommendedEventsResponse>
        get() = RetroClient.getRetrofit()!!.create(ApiServices::class.java)
            .recommendedEvents("Bearer " + Preference.getPreferences(Constants.ACCESS_TOKEN)/*, groupReq*/)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private val getRecomEventsObserver: DisposableObserver<RecommendedEventsResponse>
        get() = object : DisposableObserver<RecommendedEventsResponse>() {
            override fun onNext(@NonNull response: RecommendedEventsResponse) {
                if (Utility.isSuccessCode(response.response_code)) {
                    recommendedEventsList.value = response.body
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

    private val getPreferenceObservable: Observable<PreferenceListResponse>
        get() = RetroClient.getRetrofit()!!.create(ApiServices::class.java)
            .getPreference("Bearer " + Preference.getPreferences(Constants.ACCESS_TOKEN))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private val getPreferenceObserver: DisposableObserver<PreferenceListResponse>
        get() = object : DisposableObserver<PreferenceListResponse>() {
            override fun onNext(@NonNull response: PreferenceListResponse) {
                if (Utility.isSuccessCode(response.response_code)) {
                    checkNullPreference(response.body)
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

    private fun checkNullPreference(body: java.util.ArrayList<PreferenceListBody>?) {
        if (body != null && body.size > 0) {
            val data = body?.get(0)
            if (data.preferred_city.isNullOrEmpty()) {
                switchPreferenceName.value = PreferenceName.PREFERRED_CITY
                return
            }
            if (data.preferred_job_type.isNullOrEmpty()) {
                switchPreferenceName.value = PreferenceName.PREFERRED_JOB_TYPE
                return
            }
            if (data.preferred_functional_area.isNullOrEmpty()) {
                switchPreferenceName.value = PreferenceName.PREFERRED_FUNCTIONAL_AREA
                return
            }
            if (data.preferred_industry.isNullOrEmpty()) {
                switchPreferenceName.value = PreferenceName.PREFERRED_INDUSTRIES
                return
            }
            else{
                allPreferenceUpdated.value = true
                return
            }
            if (data.preferred_salary.isNullOrEmpty()) {
                // switchPreferenceName.value = PreferenceName.PREFERRED_SALARY
                return
            }
            if (data.skills.isNullOrEmpty()) {
                //switchPreferenceName.value = PreferenceName.SKILLS
                return
            }
            if (data.exp_from_year == 0) {
               // switchPreferenceName.value = PreferenceName.EXP_FROM_YEAR
                return
            }
            if (data.exp_to_year == 0) {
                // switchPreferenceName.value = PreferenceName.EXP_TO_YEAR
                return
            }

        }
    }

    fun joinGroup(id: Int?) {
        if (id != null) {
            joinGroupId = id
        }
        if (Utility.isInternetConnected(app)) {
            joinGroupObservable.subscribeWith(joinGroupObserver)
        } else {
            errorMessage.value = app.resources.getString(R.string.no_internet_connection)
        }
    }

    fun followUnFollowCompany(id: Int?, followUnfollow: String) {

        if (Utility.isInternetConnected(app)) {
            groupReq["entity_id"] = id.toString()
            groupReq["entity_type"] = Constants.COMPANY
            groupReq["status"] = followUnfollow

            followUnFollowObservable.subscribeWith(followUnFollowObserver)
        } else {
            errorMessage.value = app.resources.getString(R.string.no_internet_connection)
        }
    }

    private val followUnFollowObservable: Observable<CompanyFollow>
        get() = RetroClient.getRetrofit()!!.create(ApiServices::class.java)
            .FollowCompayny("Bearer " + Preference.getPreferences(Constants.ACCESS_TOKEN), groupReq)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private val followUnFollowObserver: DisposableObserver<CompanyFollow>
        get() = object : DisposableObserver<CompanyFollow>() {
            override fun onNext(@NonNull response: CompanyFollow) {
                if (Utility.isSuccessCode(response.response_code)) {
                    successMessage.value = response.message
                    followCompanyResponse.value = response.body
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

    private val joinGroupObservable: Observable<JoinGroup>
        get() = RetroClient.getRetrofit()!!.create(ApiServices::class.java)
            .joinGroup(joinGroupId, "Bearer " + Preference.getPreferences(Constants.ACCESS_TOKEN))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private val joinGroupObserver: DisposableObserver<JoinGroup>
        get() = object : DisposableObserver<JoinGroup>() {
            override fun onNext(@NonNull response: JoinGroup) {
                if (Utility.isSuccessCode(response.response_code)) {
                    successMessage.value = response.message
                    joinGroupResponse.value = response.body
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

    private val uploadResumeObservable: Observable<ResumeUploadResponse>
        get() = RetroClient.getRetrofit()!!.create(ApiServices::class.java)
            .uploadResume("Bearer " + Preference.getPreferences(Constants.ACCESS_TOKEN), groupReq)
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
            .getNotificationBubble("Bearer " + Preference.getPreferences(Constants.ACCESS_TOKEN))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private val notificationObserver: DisposableObserver<NotificationBubbleResponse>
        get() = object : DisposableObserver<NotificationBubbleResponse>() {
            override fun onNext(@NonNull response: NotificationBubbleResponse) {
                if (Utility.isSuccessCode(response.response_code)) {

                    notificationCount.value = response.body?.new_notification
                } else {
                    // errorMessage.value = response.message
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
            .downVoteData("Bearer " + Preference.getPreferences(Constants.ACCESS_TOKEN), groupReq)
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
            .upVoteData("Bearer " + Preference.getPreferences(Constants.ACCESS_TOKEN), groupReq)
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
            .getNewsPostData("Bearer " + Preference.getPreferences(Constants.ACCESS_TOKEN), groupReq)
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
            .getRecommendedMyGroupData("Bearer " + Preference.getPreferences(Constants.ACCESS_TOKEN), groupReq)
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
            .getRecommendedCompanies("Bearer " + Preference.getPreferences(Constants.ACCESS_TOKEN), groupReq)
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
            .getRecommendedJobs("Bearer " + Preference.getPreferences(Constants.ACCESS_TOKEN), groupReq)
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

    enum class PreferenceName {
        PREFERRED_CITY,
        PREFERRED_FUNCTIONAL_AREA,
        PREFERRED_INDUSTRIES,
        PREFERRED_JOB_TYPE,
        PREFERRED_SALARY,
        SKILLS,
        EXP_TO_YEAR,
        EXP_FROM_YEAR,

    }
}