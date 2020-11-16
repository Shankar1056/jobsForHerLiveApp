package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegisterSocialTokenResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: RegisterBody? = null

}

class RegisterBody {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("social_token")
    @Expose
    var social_token: String? = null
    @SerializedName("user_id")
    @Expose
    var user_id: String? = null

    @SerializedName("used")
    @Expose
    var used: String? = null

    @SerializedName("created_on")
    @Expose
    var created_on: String? = null

    @SerializedName("modified_on")
    @Expose
    var modified_on: Boolean? = null

}
