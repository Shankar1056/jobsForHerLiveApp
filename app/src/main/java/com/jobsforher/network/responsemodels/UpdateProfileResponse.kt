package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UpdateProfileResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: UpdateProfileBody? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}
class UpdateProfileBody {

    @SerializedName("username")
    @Expose
    var username: String? = null
    @SerializedName("organization")
    @Expose
    var organization: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("profile_url")
    @Expose
    var profile_url: String? = null
    @SerializedName("location")
    @Expose
    var location: String? = null
    @SerializedName("profile_summary")
    @Expose
    var profile_summary: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("caption")
    @Expose
    var caption: String? = null
    @SerializedName("stage_type")
    @Expose
    var stage_type: String? = null

}
