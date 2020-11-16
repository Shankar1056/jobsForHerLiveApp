package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CheckDefaultResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: List<ResumeBody>? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}

class ResumeBody {

    @SerializedName("is_default")
    @Expose
    var is_default: String? = null
    @SerializedName("path")
    @Expose
    var path: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("is_parsed")
    @Expose
    var is_parsed: Boolean? = null
    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null
    @SerializedName("deleted")
    @Expose
    var deleted: Boolean? = null
    @SerializedName("user_id")
    @Expose
    var user_id: Int? = null

}


