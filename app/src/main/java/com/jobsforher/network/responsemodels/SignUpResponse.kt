package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import entertainment.minersinc.tfhy.network.responsemodels.Auth


class SignUpResponse {

    @SerializedName("auth")
    @Expose
    var auth: Auth? = null
    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: Data? = null

}

class Auth {

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

class Data() {

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
    var phoneNo: String? = null
    @SerializedName("role")
    @Expose
    var role: String? = null
    @SerializedName("verified_email")
    @Expose
    var verified_email: Boolean? = null
    @SerializedName("profile_url")
    @Expose
    var profile_url: String? = null
    @SerializedName("profile_visibility")
    @Expose
    var profile_visibility: Boolean? = null
    @SerializedName("user_linkedin_id")
    @Expose
    var userLinkedinId: String? = null
    @SerializedName("user_fb_id")
    @Expose
    var userFbId: String? = null
    @SerializedName("user_gmail_id")
    @Expose
    var userGmailId: String? = null

}
