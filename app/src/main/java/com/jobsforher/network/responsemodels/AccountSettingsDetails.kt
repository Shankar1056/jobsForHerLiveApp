package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class AccountSettingsDetails {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body:  AccountSettingsBody? = null

}
class AccountSettingsBody{

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("username")
    @Expose
    var username: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("secondary_email")
    @Expose
    var secondary_email: String? = null
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
    @SerializedName("verified_email")
    @Expose
    var verified_email: Boolean? = null
    @SerializedName("phone_no_verified")
    @Expose
    var phone_no_verified:  Boolean? = null
    @SerializedName("secondary_email_verified")
    @Expose
    var secondary_email_verified:  Boolean? = null
    @SerializedName("profile_url")
    @Expose
    var profile_url:  String? = null
    @SerializedName("profile_visibility")
    @Expose
    var profile_visibility:  Boolean? = null
    @SerializedName("location")
    @Expose
    var location:  String? = null
    //    @SerializedName("location_id")
//    @Expose
//    var location_id:  Int? = null
    @SerializedName("profile_percentage")
    @Expose
    var profile_percentage:  Int? = null
    @SerializedName("stage_type")
    @Expose
    var stage_type:  String? = null
    @SerializedName("profile_summary")
    @Expose
    var profile_summary:  String? = null
    @SerializedName("verification_code")
    @Expose
    var verification_code:  String? = null

}
