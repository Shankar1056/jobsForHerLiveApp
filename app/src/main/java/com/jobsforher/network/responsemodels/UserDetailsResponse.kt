package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserDetailsResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: UserBody? = null
    @SerializedName("auth")
    @Expose
    var auth: UserAuth? = null

}

class UserBody {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("username")
    @Expose
    var username: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("phone_no")
    @Expose
    var phone_no: String? = null
    @SerializedName("role")
    @Expose
    var role: String? = null
    @SerializedName("profile_image")
    @Expose
    var profile_image: String? = null
    @SerializedName("profile_icon")
    @Expose
    var profile_icon: String? = null
    @SerializedName("profile_url")
    @Expose
    var profile_url: String? = null
    @SerializedName("profile_visibility")
    @Expose
    var profile_visibility: Boolean? = null
    @SerializedName("location")
    @Expose
    var location: String? = null
    @SerializedName("location_id")
    @Expose
    var location_id: String? = null
    @SerializedName("profile_percentage")
    @Expose
    var profile_percentage: String? = null
    @SerializedName("stage_type")
    @Expose
    var stage_type: String? = null

}

class UserAuth {

    @SerializedName("token_type")
    @Expose
    var tokenType: String? = null
    @SerializedName("access_token")
    @Expose
    var accessToken: String? = null
    @SerializedName("refresh_token")
    @Expose
    var refreshToken: String? = null
    @SerializedName("issued_at")
    @Expose
    var issuedAt: Int? = null
    @SerializedName("expires_in")
    @Expose
    var expiresIn: Int? = null
}



