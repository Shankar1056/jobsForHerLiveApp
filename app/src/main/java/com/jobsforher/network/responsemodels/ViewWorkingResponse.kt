package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ViewWorkingResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: List<WorkingBody>? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}
class WorkingBody {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("company_name")
    @Expose
    var company_name: String? = null
    @SerializedName("start_date")
    @Expose
    var start_date: String? = null
    @SerializedName("end_date")
    @Expose
    var end_date: String? = null
    @SerializedName("user_id")
    @Expose
    var user_id: String? = null
    @SerializedName("skills")
    @Expose
    var skills: List<String>? = null

    @SerializedName("current_company")
    @Expose
    var current_company: Boolean? = null
    @SerializedName("skills_id")
    @Expose
    var skills_id: List<String>? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("image_url")
    @Expose
    var image_url: String? = null
    @SerializedName("location")
    @Expose
    var location: String? = null
    @SerializedName("location_id")
    @Expose
    var location_id: String? = null
    @SerializedName("designation")
    @Expose
    var designation: String? = null

}
