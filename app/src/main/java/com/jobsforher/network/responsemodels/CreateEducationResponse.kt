package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CreateEducationResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: EducationBody? = null
    @SerializedName("auth")
    @Expose
    var auth: UserNameAuth? = null

}

class EducationBody {

    @SerializedName("location_id")
    @Expose
    var location_id: Int? = null
    @SerializedName("college_id")
    @Expose
    var college_id: Int? = null
    @SerializedName("degree_id")
    @Expose
    var degree_id: Int? = null
    @SerializedName("start_date")
    @Expose
    var start_date: String? = null
    @SerializedName("user_id")
    @Expose
    var user_id: Int? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("college")
    @Expose
    var college: String? = null

    @SerializedName("skills")
    @Expose
    var skills: List<String>? = null


    @SerializedName("ongoing")
    @Expose
    var on_going: Boolean? = null
    @SerializedName("location")
    @Expose
    var location: String? = null

    @SerializedName("skills_id")
    @Expose
    var skills_id: List<Int>? = null


    @SerializedName("end_date")
    @Expose
    var end_date: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("image_url")
    @Expose
    var image_url: String? = null
    @SerializedName("degree")
    @Expose
    var degree: String? = null

}