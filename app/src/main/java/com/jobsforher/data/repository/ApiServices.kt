package com.jobsforher.data.repository

import com.jobsforher.data.model.*
import com.jobsforher.network.responsemodels.NotificationBubbleResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*
import java.util.*


interface ApiServices {
    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @POST("expert_chat_details")
    fun getExpertChat(
        @Header("Authorization") auth: String,
        @Body request: ExpertChatReq
    ): Call<ExpertChatResponse>


    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @GET("es_recommended_jobs")
    fun getRecommendedJobs(
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Observable<RecommendedJobsResponse>

    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @GET("es_recommended_companies")
    fun getRecommendedCompanies(
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Observable<RecommendedCompaniesResponse>

    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @GET("es_recommended_groups")
    fun getRecommendedMyGroupData(
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Observable<RecommendedGropsResponse>

    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @GET(EndPoints.NEWSFEED)
    fun getNewsPostData(
        @Header("Authorization") accesstoken: String,
        @QueryMap signInCredentials: HashMap<String, String>
    ): Observable<NewsPostResponse>

    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @POST(EndPoints.vote)
    fun upVoteData(
        @Header("Authorization") accesstoken: String,
        @Body signInCredentials: HashMap<String, String>
    ): Observable<VoteResponseModel>

    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @HTTP(method = "DELETE", path = EndPoints.vote, hasBody = true)
    fun downVoteData(
        @Header("Authorization") accesstoken: String,
        @Body signInCredentials: HashMap<String, String>
    ): Observable<VoteResponseModel>

    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @GET(EndPoints.NOTIFICATION)
    fun getNotificationBubble(
        @Header("Authorization") accesstoken: String
    ): Observable<NotificationBubbleResponse>

    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @POST(EndPoints.RESUMEUPLOAD)
    fun uploadResume(
        @Header("Authorization") accesstoken: String,
        @Body getSkillsCredentials: HashMap<String, String>
    ): Observable<ResumeUploadResponse>

    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @GET("cities")
    fun getCities(
        @Header("Authorization") accesstoken: String
    ): Observable<PreferenceCityResponse>

    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @POST("/api/v3.0/candidate/preference")
    fun saveCityPreference(
        @Header("Authorization") accesstoken: String,
        @Body getSkillsCredentials: HashMap<String, String>
    ): Observable<PreferenceAddResponse>

    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @GET("/api/v3.0/candidate/preference")
    fun getPreference(
        @Header("Authorization") accesstoken: String
    ): Observable<PreferenceListResponse>

}