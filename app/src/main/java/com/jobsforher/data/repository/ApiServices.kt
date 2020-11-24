package com.jobsforher.data.repository

import com.jobsforher.data.model.*
import com.jobsforher.network.responsemodels.Company
import com.jobsforher.network.responsemodels.Featured_Group
import com.jobsforher.network.responsemodels.NewsDetails
import com.jobsforher.network.responsemodels.VoteResponse
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
    @GET("/api/v3.0/es_recommended_jobs")
    fun getRecommendedJobs(
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Observable<RecommendedJobsResponse>

    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @GET("/api/v3.0/es_recommended_companies")
    fun getRecommendedCompanies(
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Observable<RecommendedCompaniesResponse>

    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @GET("/api/v3.0/es_recommended_groups")
    fun getRecommendedMyGroupData(
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Observable<RecommendedGropsResponse>

    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @GET("/api/v3.0/news_feed")
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


}