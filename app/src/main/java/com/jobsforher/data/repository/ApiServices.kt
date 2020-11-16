package com.bigappcompany.healthtunnel.data.repository

import com.jobsforher.data.model.ExpertChatReq
import com.jobsforher.data.model.ExpertChatResponse
import com.jobsforher.data.model.RecommendedJobsResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import io.reactivex.Observable
import retrofit2.http.*
import java.util.*


interface ApiServices {
    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @POST("expert_chat_details")
    fun getExpertChat(
        @Header("Authorization") auth: String,
        @Body request: ExpertChatReq
    ): Observable<ExpertChatResponse>


    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @GET("/api/v3.0/es_recommended_jobs")
    fun getRecommendedJobs(
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Observable<RecommendedJobsResponse>

}