package com.jobsforher.network.retrofithelpers


import com.google.gson.JsonObject
import com.jobsforher.models.AppVersionResponse
import com.jobsforher.models.CommentNotificationModel
import com.jobsforher.network.responsemodels.GroupReplyNotificationModel
import com.jobsforher.models.PostDetailsModel
import com.jobsforher.models.ReplyNotificationModel
import com.jobsforher.network.responsemodels.*
import entertainment.minersinc.tfhy.network.responsemodels.ForgotPasswordResponse
import entertainment.minersinc.tfhy.network.responsemodels.LoginResponse
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.Body
import java.util.*


interface RetrofitInterface {

    @POST(EndPoints.user_lookup)
    fun doLogin(
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Body loginCredentials: HashMap<String, String>
    ): Call<LoginResponse>

    @POST(EndPoints.password_reset)
    fun passwodReset(
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Body loginCredentials: HashMap<String, String>
    ): Call<ForgotPasswordResponse>

    @POST(EndPoints.user_signup)
    fun doSignUp(
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Body signUpinCredentials: HashMap<String, String>
    ): Call<SignUpResponse>

    @POST(EndPoints.user_signin)
    fun doSignIn(
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Body signInCredentials: HashMap<String, String>
    ): Call<SignInResponse>

    @POST(EndPoints.device_Id_registration)
    fun registerInstallationId(
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Body registerIdCredentials: HashMap<String, String>
    ): Call<RegisterIdResponse>

    @PUT(EndPoints.device_Id_registration)
    fun registerFcmId(
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body registerIdCredentials: HashMap<String, String>
    ): Call<RegisterIdResponse>

    @POST(EndPoints.get_social_token)
    fun registerSocialToken(
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Body registerSocialToken: HashMap<String, String>
    ): Call<RegisterSocialTokenResponse>

    @POST(EndPoints.social_login)
    fun registerSocialSignIn(
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Body registerSocialSignInCredentials: HashMap<String, String>
    ): Call<RegisterSocialSignInResponse>

    @GET(EndPoints.group)
    fun getFeatredData(
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Call<Featured_Group>

    @GET(EndPoints.mygroup)
    fun getMyGroupData(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Call<Featured_Group>

    @GET(EndPoints.allgroups)
    fun getAllGroupData(
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Call<Featured_Group>

    @GET("/api/v3.0/groups/{id}")
    fun getGroupDetailsData(
        @Path("id") groupId: Int,
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<GroupDetails>

    //    @HTTP(method = "GET", path = "/api/v3.0/posts/list/{id}", hasBody = true)
    @GET("/api/v3.0/posts/list/{id}")
    fun getGroupPosts(
        @Path("id") postId: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap signInCredentials: HashMap<String, String>
    ): Call<GroupPostsNew>

    @GET("/api/v3.0/posts/pinned-post/{id}")
    fun getGroupPinnedPosts(
        @Path("id") postId: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap signInCredentials: HashMap<String, String>
    ): Call<GroupPostsNew>

    @GET("/api/v3.0/comments/{id}")
    fun getPostComments(
        @Path("id") Id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<GroupCommentsNew>

    @GET("/api/v3.0/comments/{id}")
    fun getPostCommentsNew(
        @Path("id") Id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap signInCredentials: HashMap<String, String>
    ): Call<GroupCommentsNew>

    @POST(EndPoints.vote)
    fun getVoteData(
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body signInCredentials: HashMap<String, String>
    ): Call<VoteResponse>

    @HTTP(method = "DELETE", path = EndPoints.vote, hasBody = true)
    fun deleteVoteData(
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body signInCredentials: HashMap<String, String>
    ): Call<VoteResponse>

    @POST("/api/v3.0/comments/{id}")
    fun addComment(
        @Path("id") Id: Int,
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body signInCredentials: HashMap<String, String>
    ): Call<CreateCommentResponse>

    @POST("/api/v3.0/comments/{id}")
    fun addCommentForNotification(
        @Path("id") Id: Int,
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body signInCredentials: HashMap<String, String>
    ): Call<CommentNotificationModel>

    @POST("/api/v3.0/posts/{id}")
    fun addPost(
        @Path("id") Id: Int,
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body signInCredentials: HashMap<String, String>
    ): Call<CreatePostResponse>

    @POST("/api/v3.0/replies/{id}")
    fun addReply(
        @Path("id") Id: Int,
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body signInCredentials: HashMap<String, String>
    ): Call<CreateReplyResponse>

    @POST("/api/v3.0/replies/{id}")
    fun addReplyNotification(
        @Path("id") Id: Int,
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body signInCredentials: HashMap<String, String>
    ): Call<ReplyNotificationModel>


    @GET("/api/v3.0/replies/{id}")
    fun getReply(
        @Path("id") Id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<GroupCommentsNew>

    @GET("/api/v3.0/replies/{id}")
    fun getNotificationCommentsReply(
        @Path("id") Id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<GroupReplyNotificationModel>

    @GET("/api/v3.0/replies/details/{id}")
    fun getNotificationCommentsReplyDetails(
        @Path("id") Id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<GroupReplyNotificationModel>


    @GET("/api/v3.0/posts/list/{id}")
    fun getImages(
        @Path("id") Id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap signInCredentials: HashMap<String, String>
    ): Call<ImageResponse>

    @PUT("/api/v3.0/posts/{id}")
    fun updatePost(
        @Path("id") Id: Int,
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body signInCredentials: HashMap<String, String>
    ): Call<CreatePostResponse>

    @GET("/api/v3.0/posts/edited-post/{id}")
    fun getEditedPostHistory(
        @Path("id") Id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<EditHistoryResponse>

    @PUT("/api/v3.0/comments/{id}")
    fun updateComent(
        @Path("id") Id: Int,
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body signInCredentials: HashMap<String, String>
    ): Call<CreatePostResponse>

    @GET("/api/v3.0/comments/edited-comments/{id}")
    fun getEditedCommentHistory(
        @Path("id") Id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<EditHistoryCommentResponse>

    @GET("/api/v3.0/comments/edited-replies/{id}")
    fun getEditedRepliesHistory(
        @Path("id") Id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<EditHistoryCommentResponse>

    @GET("/api/v3.0/categories")
    fun getCategories(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<GetCategoryResponse>

    @GET("/api/v3.0/cities")
    fun getCities(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<GetCityResponse>

    @GET("/api/v3.0/groups")
    fun getFiltersData(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap signInCredentials: HashMap<String, String>
    ): Call<Featured_Group>


    @GET("/api/v3.0/user")
    fun GetUserDetails(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<UserDetailsResponse>

    @GET("/api/v3.0/user_profile_url/{username}")
    fun CheckUsernameExists(
        @Path("username") Id: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<CheckUsernameResponse>

    @PUT("/api/v3.0/user_profile_url")
    fun UpdateProfileUrl(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body updateProfileUrlCredentials: HashMap<String, String>
    ): Call<UpdateProfileUrl>

    @GET("/api/v3.0/cities")
    fun GetCities(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<CitiesListResponse>

    @PUT("/api/v3.0/user")
    fun UpdateLocation(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body updateLocationCredentials: HashMap<String, String>
    ): Call<UpdateLocation>

    @GET(EndPoints.categories)
    fun getMainCategories(
        @Header("clientid") clientid: String,
        @QueryMap categories: HashMap<String, String>
    ): Call<GroupCategoriesResponse>

    @POST("/api/v3.0/groups/join/{id}")
    fun JoinGroup(
        @Path("id") id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<JoinGroupResponse>

    @DELETE("/api/v3.0/groups/leave/{id}")
    fun leaveGroup(
        @Path("id") postId: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<JoinGroupResponse>

    @GET("/api/v3.0/candidate/mobility/degree")
    fun GetDegrees(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<DegreesListResponse>

    @GET("/api/v3.0/candidate/mobility/college")
    fun GetColleges(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<CollegesListResponse>

    @POST("/api/v3.0/candidate/education")
    fun CreateEducation(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body createEducationCredentials: HashMap<String, String>
    ): Call<CreateEducationResponse>

    @POST("/api/v3.0/candidate/mobility/skill")
    fun GetSkills(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body getSkillsCredentials: HashMap<String, String>
    ): Call<SkillsResponse>

    @POST("/api/v3.0/candidate/life_experience")
    fun CreateLifeExperience(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body createLifeExperienceCredentials: HashMap<String, String>
    ): Call<CreatelifeExperienceResponse>

    @GET("/api/v3.0/candidate/mobility/career-break")
    fun GetLifeExperiences(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<LifeExperienceResponse>

    @POST("/api/v3.0/candidate/working")
    fun CreateWorkExperience(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body createWorkCredentials: HashMap<String, String>
    ): Call<CreateWorkResponse>

    @GET("/api/v3.0/candidate/profile/{username}")
    fun GetProfileDetails(
        @Path("username") Id: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<ProfileResponse>

    @PUT("/api/v3.0/candidate/profile")
    fun AddProfileDetails(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body updateLocationCredentials: HashMap<String, String>
    ): Call<CreateProfileResponse>

    @GET("/api/v3.0/candidate/certification")
    fun GetCertificateData(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<CertificateResponse>

    @GET("/api/v3.0/candidate/certification/{username}")
    fun GetCertificateDetails(
        @Path("username") Id: Int,
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<CertificateResponse>

    @GET("/api/v3.0/candidate/recognition")
    fun GetRecognitionData(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<RecognitionResponse>

    @PUT("/api/v3.0/candidate/public-profile")
    fun UpdateProfileData(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body updateLocationCredentials: HashMap<String, String>
    ): Call<UpdateProfileResponse>

    @PUT("/api/v3.0/candidate/certification/{username}")
    fun UpdateCertificationData(
        @Path("username") Id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body updateLocationCredentials: HashMap<String, Any>
    ): Call<CertificateUpdateResponse>

    @DELETE("/api/v3.0/candidate/certification/{username}")
    fun DeleteCertificationData(
        @Path("username") Id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<DeletePostResponse>

    @GET("/api/v3.0/candidate/recognition/{username}")
    fun GetRecognitionDetails(
        @Path("username") Id: Int,
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<RecognitionResponse>

    @PUT("/api/v3.0/candidate/recognition/{username}")
    fun UpdateRecognitionData(
        @Path("username") Id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body updateLocationCredentials: HashMap<String, String>
    ): Call<RecognitionUpdateResponse>

    @DELETE("/api/v3.0/candidate/recognition/{username}")
    fun DeleteRecognitionData(
        @Path("username") Id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<DeletePostResponse>


    @POST("/api/v3.0/candidate/certification")
    fun AddCertificateData(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body updateLocationCredentials: HashMap<String, Any>
    ): Call<CertificateUpdateResponse>


    @POST("/api/v3.0/candidate/recognition")
    fun AddRecognitionData(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body updateLocationCredentials: HashMap<String, String>
    ): Call<RecognitionUpdateResponse>

    @PUT("/api/v3.0/candidate/public-profile")
    fun UpdateSummaryData(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body updateLocationCredentials: HashMap<String, String>
    ): Call<UpdateProfileResponse>

    @GET("/api/v3.0/candidate/working")
    fun GetWorkExperienceData(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<ViewWorkingResponse>

    @GET("/api/v3.0/candidate/working/{username}")
    fun GetWorkingDetails(
        @Path("username") Id: Int,
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<ViewWorkingResponse>

    @PUT("/api/v3.0/candidate/working/{username}")
    fun UpdateWorkExperienceData(
        @Path("username") Id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body updateLocationCredentials: HashMap<String, String>
    ): Call<CreateWorkResponse>

    @DELETE("/api/v3.0/candidate/working/{id}")
    fun DeleteWorkExperienceData(
        @Path("id") postId: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<DeletePostResponse>

    @POST("/api/v3.0/candidate/working")
    fun AddWorkExperienceData(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body updateLocationCredentials: HashMap<String, String>
    ): Call<CreateWorkResponse>

    @GET("/api/v3.0/candidate/life_experience")
    fun GetLifeExperienceData(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<ViewLifeResponse>

    @GET("/api/v3.0/candidate/life_experience/{username}")
    fun GetLifeExperienceDetails(
        @Path("username") Id: Int,
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<ViewLifeResponse>

    @PUT("/api/v3.0/candidate/life_experience/{username}")
    fun UpdateLifeExperienceData(
        @Path("username") Id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body updateLocationCredentials: HashMap<String, String>
    ): Call<UpdateLifeResponse>

    @DELETE("/api/v3.0/candidate/life_experience/{username}")
    fun DeleteLifeExperienceData(
        @Path("username") Id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<DeletePostResponse>

    @POST("/api/v3.0/candidate/life_experience")
    fun AddLifeExperienceData(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body updateLocationCredentials: HashMap<String, String>
    ): Call<UpdateLifeResponse>

    @GET("/api/v3.0/candidate/education")
    fun GetEducationData(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<ViewEducationResponse>

    @GET("/api/v3.0/candidate/education/{username}")
    fun GetEducationDetailsData(
        @Path("username") Id: Int,
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<ViewEducationResponse>

    @PUT("/api/v3.0/candidate/education/{username}")
    fun UpdateEducationData(
        @Path("username") Id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body updateLocationCredentials: HashMap<String, String>
    ): Call<UpdateEducationResponse>

    @DELETE("/api/v3.0/candidate/education/{username}")
    fun DeleteEducationData(
        @Path("username") Id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<DeletePostResponse>

    @DELETE("/api/v3.0/posts/{id}")
    fun deletePost(
        @Path("id") postId: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<DeletePostResponse>

    @GET("/api/v3.0/replies/{id}")
    fun getPostReplies(
        @Path("id") Id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<GroupCommentsNew>

    @POST("/api/v3.0/groups")
    fun addGroup(
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body signInCredentials: HashMap<String, String>
    ): Call<DeletePostResponse>

    @POST("/api/v3.0/flag")
    fun ReportGroup(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body signInCredentials: HashMap<String, String>
    ): Call<ReportResponse>

    @GET("/api/v3.0/flag/group/{id}")
    fun ReportCheck(
        @Path("id") Id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<DeletePostResponse>

    @GET("/api/v3.0/flag/post/{id}")
    fun ReportCheckPost(
        @Path("id") Id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<DeletePostResponse>

    @GET("/api/v3.0/flag/comment/{id}")
    fun ReportCheckComment(
        @Path("id") Id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<DeletePostResponse>

    @PUT("/api/v3.0/replies/{id}")
    fun UpdateReply(
        @Path("id") Id: Int,
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body signInCredentials: HashMap<String, String>
    ): Call<UpdateReplyResponse>

    @GET("/api/v3.0/flag/reply/{id}")
    fun ReportCheckReply(
        @Path("id") Id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<DeletePostResponse>

    @POST("/api/v3.0/groups/member_post_count")
    fun GetMemberCount(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body getSkillsCredentials: HashMap<String, ArrayList<Int>>
    ): Call<GetGroupMemberResponse>


//    Jobs api

    @GET("/api/v3.0/es_candidate_jobs?type=boosted")
    fun gethotJobsData(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Call<HotJobs>

    @GET("/api/v3.0/es_candidate_jobs?type=basic")
    fun getotherJobsData(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Call<HotJobs>

    @GET("/api/v3.0/jobs/{id}")
    fun getJobDetailsData(
        @Path("id") groupId: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<JobDetails>

    @GET("/api/v3.0/my_applied_jobs")
    fun getAppliedJobs(
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Call<AppliedJobDetails>

    @GET("/api/v3.0/candidate/preference")
    fun CheckPreferences(
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<CheckPreferenceResponse>

    @GET("/api/v3.0/candidate/user_default_resume/{id}")
    fun CheckDefault(
        @Path("id") groupId: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<CheckDefaultResponse>

    @GET("/api/v3.0/es_candidate_jobs")
    fun getfilteredJobsData(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Call<HotJobs>

    @POST("/api/v3.0/job_applications")
    fun saveJob(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body getSkillsCredentials: HashMap<String, String>
    ): Call<SaveJobResponse>

    @POST("/api/v3.0/candidate/resumes")
    fun uploadResume(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body getSkillsCredentials: HashMap<String, String>
    ): Call<SaveJobResponse>

    @POST("/api/v3.0/jobs_saved")
    fun starJob(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body getSkillsCredentials: HashMap<String, String>
    ): Call<StarJobResponse>

    @DELETE("/api/v3.0/jobs_saved/{id}")
    fun unstarJob(
        @Path("id") postId: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<StarJobResponse>

    @POST("/api/v3.0/candidate/preference")
    fun PostPreferences(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body getSkillsCredentials: HashMap<String, String>
    ): Call<UpdateReplyResponse>

    @PUT("/api/v3.0/candidate/preference/{id}")
    fun updatePreferences(
        @Path("id") Id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body getSkillsCredentials: HashMap<String, String>
    ): Call<UpdateReplyResponse>


    @POST("/api/v3.0/candidate/resumes")
    fun SendResume(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body getSkillsCredentials: HashMap<String, String>
    ): Call<UpdateReplyResponse>


    //    Companies api
    @GET("/api/v3.0/es_companies?featured_basic=basic")
    fun getCompanyListBasic(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Call<Company>

    @GET("/api/v3.0/es_companies?featured_basic=featured")
    fun getCompanyListFeatured(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Call<Company>


    @GET("/api/v3.0/company/{id}")
    fun getCompanyDetailsData(
        @Path("id") groupId: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<CompanyApiDetails>

    @POST("/api/v3.0/followers")
    fun FollowCompayny(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body getSkillsCredentials: HashMap<String, String>
    ): Call<UpdateReplyResponse>

    @GET("/api/v3.0/company/followed")
    fun getCompanyFollowed(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<FollowersResponse>

    @GET("/api/v3.0/es_companies")
    fun getfilteredCompanies(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Call<Company>

    @GET("/api/v3.0/es_candidate_jobs")
    fun getcompJobsData(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Call<HotJobs>


    //    Dashboard api
    @GET("/api/v3.0/cand_dashboard")
    fun getDashboardData(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<DashboardApiDetails>

    @GET("/api/v3.0/user")
    fun getAccountSettingsData(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<AccountSettingsDetails>

    @GET("/api/v3.0/jobs_saved")
    fun getSavedJobs(
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Call<AppliedJobDetails>

    @GET("/api/v3.0/user_followed_companies")
    fun getMyCompanies(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Call<Company>

    @GET("/api/v3.0/es_recommended_jobs")
    fun getRecommendedJobs(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Call<HotJobs>

    @GET("/api/v3.0/es_recommended_groups")
    fun getRecommendedMyGroupData(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Call<Featured_Group>

    @GET("/api/v3.0/es_recommended_companies")
    fun getRecommendedCompanies(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Call<Company>

    @GET("/api/v3.0/es_recommended_events")
    fun getRecommendedEvents(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Call<Event>


    @GET("/api/v3.0/jobs/jobs_alerts")
    fun getJobAlert(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<PreferenceResponse>

    @PUT("/api/v3.0/jobs/jobs_alerts/{id}")
    fun UpdateJobAlerts(
        @Path("id") Id: Int,
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body signInCredentials: HashMap<String, String>
    ): Call<JobAlertResponse>

    @POST("/api/v3.0/jobs/jobs_alerts")
    fun AddJobAlerts(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body signInCredentials: HashMap<String, String>
    ): Call<JobAlertResponse>

    @DELETE("/api/v3.0/jobs/jobs_alerts/{id}")
    fun DeleteJobAlerts(
        @Path("id") Id: Int,
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<JobAlertResponse>

    @GET("/api/v3.0/candidate/user-followings-list")
    fun getFollowingList(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<FollowingDetails>

    @PUT("/api/v3.0/candidate/user-unfollow")
    fun unFollowProfile(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body getSkillsCredentials: HashMap<String, String>
    ): Call<UpdateReplyResponse>

    @PUT("/api/v3.0/candidate/resumes/{id}")
    fun DeleteResume(
        @Path("id") Id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body signInCredentials: HashMap<String, String>
    ): Call<PreferenceResponse>

    @PUT("/api/v3.0/candidate/resumes/{id}")
    fun makeDefaultResume(
        @Path("id") Id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body getSkillsCredentials: HashMap<String, String>
    ): Call<PreferenceResponse>


    // Account Settings
    @PUT("/api/v3.0/user")
    fun UpdateName(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body signInCredentials: HashMap<String, String>
    ): Call<AccountSettingsBody>

    @DELETE("/api/v3.0/user")
    fun DeleteAccount(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<AccountSettingsBody>


    @POST("/api/v3.0/account/resend_verify")
    fun sendVerifyEmail(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body signInCredentials: HashMap<String, String>
    ): Call<AccountSettingsBody>

    @POST("/api/v3.0/account/verify_mobile")
    fun requestOTP(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body signInCredentials: HashMap<String, String>
    ): Call<AccountSettingsDetails>

//
//    @POST("/mobileapi")
//    fun requestpartyOTP(@Header("Authorization:Basic") clientid: String,@Body signInCredentials: HashMap<String, String>):Call<AccountSettingsDetails>

//    @POST("/mobileapi")
//    fun requestpartyOTP(@Header("Authorization:Basic") clientid: String,@Body signInCredentials: JsonObject):Call<AccountSettingsDetails>


    @POST("/api/v3.0/reset_password")
    fun resetPassword(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body signInCredentials: HashMap<String, String>
    ): Call<AccountSettingsBody>


    //    Events api
    @GET("/api/v3.0/es_candidate_events?event_type=featured")
    fun gethotEventsData(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Call<Event>

    @GET("/api/v3.0/es_candidate_events?event_type=basic")
    fun getotherEventsData(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Call<Event>

    @GET("/api/v3.0/es_candidate_events?event_type=past")
    fun getpastEventsData(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Call<Event>

    @GET("/api/v3.0/es_candidate_events")
    fun getfilteredEventsData(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Call<Event>

    @GET("/api/v3.0/events/{id}")
    fun getEventDetailsData(
        @Path("id") groupId: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<EventApiDetails>

    @GET("/api/v3.0/events_locations_by_event_id/{id}")
    fun GetLocationList(
        @Path("id") groupId: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<EventLocationDetails>


    @POST("/api/v3.0/events/events_registrations")
    fun saveEvent(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body getSkillsCredentials: HashMap<String, String>
    ): Call<SaveEventResponse>

    @GET("/api/v3.0/events/events_registrations/{id}")
    fun getEventSuccessData(
        @Path("id") groupId: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<EventRegApiDetails>

    @GET("/api/v3.0/events/events_registrations_by_user")
    fun getAppliedEvents(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Call<EventsResponse>

    @GET("/api/v3.0/events/my_events_registrations")
    fun getMyEvents(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap groupCredentials: HashMap<String, String>
    ): Call<MyEvent>


//    @PUT("/api/v3.0/orders_events/{id}")
//    fun UpdatePaymentEvents(@Path("id") groupId:String, @Header("Content-Type") contentType: String, @Header("clientid") clientid: String, @Header("Authorization") accesstoken: String, @Body signInCredentials: HashMap<String, String>):Call<AccountSettingsBody>

    @PUT("/api/v3.0/orders_events/{id}")
    fun UpdatePaymentEvents(
        @Path("id") groupId: String,
        @Header("Content-Type") contentType: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body signInCredentials: JsonObject
    ): Call<PaymetUpdateResponse>

    @POST("/api/v3.0/events/events_interested")
    fun AddInterest(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body getSkillsCredentials: HashMap<String, String>
    ): Call<UpdateReplyResponse>

    @HTTP(method = "POST", path = "/api/v3.0/events/events_interested_by_user", hasBody = true)
    fun GetInterest(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body getSkillsCredentials: HashMap<String, ArrayList<Int>>
    ): Call<GetInterestResponse>


    //News Feed api
    @GET("/api/v3.0/user_interested?entity_type=newsfeed")
    fun getUserSelection(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<UserSelection>

    @POST("/api/v3.0/user_interested")
    fun postUserSelection(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body getSkillsCredentials: HashMap<String, String>
    ): Call<UserSelection>

    @PUT("/api/v3.0/user_interested/{id}")
    fun updateUserSelection(
        @Path("id") groupId: String,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @Body getSkillsCredentials: HashMap<String, String>
    ): Call<UserSelection>

    @GET("/api/v3.0/news_feed")
    fun getNewsDetailsData(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap signInCredentials: HashMap<String, String>
    ): Call<NewsDetails>


    //mobility api
    @GET("/api/v3.0/candidate/mobility/functional-area")
    fun getFunctionalArea(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<GetMobilityResponse>

    @GET("/api/v3.0/candidate/mobility/functional-industry")
    fun getIndustry(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<GetMobilityResponse>


    //notification
    @GET("/api/v3.0/notification")
    fun GetNotificationData(
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String,
        @QueryMap signInCredentials: HashMap<String, String>
    ): Call<NotificationResponse>

    @GET("/api/v3.0/posts/details/{id}")
    fun getPostDetails(
        @Path("id") Id: Int,
        @Header("clientid") clientid: String,
        @Header("Authorization") accesstoken: String
    ): Call<PostDetailsModel>

    @GET("mobile_app_version_api")
    fun getAppVersion(
        @Header("clientid") clientid: String
    ): Call<AppVersionResponse>


    @GET("/api/v3.0/notification_bubble")
    fun GetNotificationBubble(@Header("clientid") clientid: String,@Header("Authorization") accesstoken: String):Call<NotificationBubbleResponse>

}
