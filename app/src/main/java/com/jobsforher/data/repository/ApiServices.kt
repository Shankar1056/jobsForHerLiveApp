package com.jobsforher.data.repository

import com.jobsforher.data.model.*
import com.jobsforher.network.responsemodels.*
import com.jobsforher.network.retrofithelpers.EndPoints
import entertainment.minersinc.tfhy.network.responsemodels.ForgotPasswordResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.Body
import java.util.*


interface ApiServices {
    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @POST(EndPoints.EXPERT_CHAT_DETAILS)
    fun getExpertChat(
        @Header("Authorization") auth: String,
        @Body request: ExpertChatReq
    ): Call<ExpertChatResponse>


    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @GET(EndPoints.RECOMMENDED_JOBS)
    fun getRecommendedJobs(
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Observable<RecommendedJobsResponse>

    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @GET(EndPoints.RECOMMENDED_COMPANIES)
    fun getRecommendedCompanies(
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Observable<RecommendedCompaniesResponse>

    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @GET(EndPoints.RECOMMENDED_GROUPS)
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
    @POST(EndPoints.vote)
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
    @GET(EndPoints.CITIES)
    fun getCities(
        @Header("Authorization") accesstoken: String
    ): Observable<PreferenceCityResponse>

    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @PUT(EndPoints.GET_PREFERENCE)
    fun saveCityPreference(
        @Header("Authorization") accesstoken: String,
        @Body getSkillsCredentials: HashMap<String, String>
    ): Observable<PreferenceAddResponse>

    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @GET(EndPoints.GET_PREFERENCE)
    fun getPreference(
        @Header("Authorization") accesstoken: String
    ): Observable<PreferenceListResponse>

    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @GET(EndPoints.GET_FUNCTIONAL_AREAS)
    fun getFunctionalArea(
        @Header("Authorization") accesstoken: String
    ): Observable<PreferenceFunctionalArea>

    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @GET(EndPoints.GET_FUNCTIONAL_INDUSTRIES)
    fun getFunctionalIndustries(
        @Header("Authorization") accesstoken: String
    ): Observable<PreferenceFunctionalArea>

    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @POST(EndPoints.JOINGROUP + "/{id}")
    fun joinGroup(
        @Path("id") id: Int,
        @Header("Authorization") accesstoken: String
    ): Observable<JoinGroup>

    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @POST(EndPoints.FOLLOWCOMPANY)
    fun FollowCompayny(
        @Header("Authorization") accesstoken: String,
        @Body getSkillsCredentials: HashMap<String, String>
    ): Observable<CompanyFollow>

    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @GET(EndPoints.RECOMMENDED_EVENTS)
    fun recommendedEvents(
        @Header("Authorization") accesstoken: String
       // @Body getSkillsCredentials: HashMap<String, String>
    ): Observable<RecommendedEventsResponse>

    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @POST(EndPoints.password_reset)
    fun passwodReset(
        @Header("Content-Type") contentType: String,
        @Body loginCredentials: HashMap<String, String>
    ): Observable<ForgotPasswordResponse>

    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @POST(EndPoints.user_signin)
    fun doSignIn(
        @Header("Content-Type") contentType: String,
        @Body signInCredentials: HashMap<String, String>
    ): Observable<SignInResponse>

    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @POST(EndPoints.get_social_token)
    fun registerSocialToken(
        @Header("Content-Type") contentType: String,
        @Body registerSocialToken: HashMap<String, String>
    ): Observable<RegisterSocialTokenResponse>

    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @POST(EndPoints.social_login)
    fun registerSocialSignIn(
        @Header("Content-Type") contentType: String,
        @Body registerSocialSignInCredentials: HashMap<String, String>
    ): Observable<RegisterSocialSignInResponse>

    @Headers("clientid:" + EndPoints.CLIENT_ID)
    @POST(EndPoints.user_signup)
    fun doSignUp(
        @Header("Content-Type") contentType: String,
        @Body signUpinCredentials: HashMap<String, String>
    ): Observable<SignUpResponse>


}