package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CreateWorkResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: WorkExpBody? = null
    @SerializedName("auth")
    @Expose
    var auth: UserNameAuth? = null

}

class WorkExpBody {

    @SerializedName("designation")
    @Expose
    var designation: String? = null
    @SerializedName("company_name")
    @Expose
    var company_name: String? = null
    @SerializedName("current_company")
    @Expose
    var current_company: Boolean? = null
    @SerializedName("start_date")
    @Expose
    var start_date: String? = null
    @SerializedName("end_date")
    @Expose
    var end_date: String? = null
    @SerializedName("location")
    @Expose
    var location: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("skills")
    @Expose
    var skills: List<String>? = null

//    @SerializedName("user_id")
//    @Expose
//    var user_id: Int? = null
//    @SerializedName("id")
//    @Expose
//    var id: Int? = null
//    @SerializedName("life_experience")
//    @Expose
//    var life_experience: List<String>? = null
//    @SerializedName("life_experience_id")
//    @Expose
//    var life_experience_id: List<Int>? = null
//    @SerializedName("location_id")
//    @Expose
//    var location_id: Int? = null
//    @SerializedName("ongoing")
//    @Expose
//    var ongoing: Boolean? = null
//    @SerializedName("image_url")
//    @Expose
//    var image_url: String? = null
//    @SerializedName("end_date")
//    @Expose
//    var end_date: String? = null
//    @SerializedName("skills_id")
//    @Expose
//    var skills_id: List<Int>? = null
//    @SerializedName("skills")
//    @Expose
//    var skills: List<String>? = null

}
