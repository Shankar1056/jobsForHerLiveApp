package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class SignInResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: Bodyy? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}

class Bodyy {

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
    @SerializedName("ip")
    @Expose
    var ip: String? = null
    @SerializedName("reference_url")
    @Expose
    var reference_url: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("verified_email")
    @Expose
    var verified_email: Boolean? = null
    @SerializedName("entity_type")
    @Expose
    var entity_type: String? = null
    @SerializedName("entity_id")
    @Expose
    var entity_id: String? = null
    @SerializedName("referral")
    @Expose
    var referral: String? = null
    @SerializedName("utm")
    @Expose
    var utm: String? = null
    @SerializedName("referral_full_url")
    @Expose
    var referral_full_url: String? = null
    @SerializedName("profile_url")
    @Expose
    var profile_url: String? = null
    @SerializedName("profile_visibility")
    @Expose
    var profile_visibility: String? = null
    @SerializedName("linkedin_url")
    @Expose
    var linkedin_url: String? = null
    @SerializedName("other_url")
    @Expose
    var other_url: String? = null
    @SerializedName("user_linkedin_id")
    @Expose
    var user_linkedin_id: String? = null
    @SerializedName("user_fb_id")
    @Expose
    var userFbId: String? = null
    @SerializedName("user_gmail_id")
    @Expose
    var userGmailId: String? = null
    @SerializedName("profile_percentage")
    @Expose
    var profile_percentage: Int? = null
    @SerializedName("profile_icon")
    @Expose
    var profile_icon: String? = null

}

