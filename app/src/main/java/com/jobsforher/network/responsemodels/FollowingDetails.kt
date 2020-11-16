package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class FollowingDetails {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body:  FollowingBody? = null
    @SerializedName("pagination")
    @Expose
    var pagination: Pagination? = null

}
class FollowingBody{

    @SerializedName("user_following")
    @Expose
    var user_following: List<Following>? = null


}

class Following{

    @SerializedName("user_id")
    @Expose
    var user_id: Int? = null
    @SerializedName("username")
    @Expose
    var username: String? = null
    @SerializedName("stage_type")
    @Expose
    var stage_type: String? = null
    @SerializedName("profile_url")
    @Expose
    var profile_url: String? = null
    @SerializedName("profile_icon")
    @Expose
    var profile_icon: String? = null
    @SerializedName("profile_image")
    @Expose
    var profile_image: String? = null
    @SerializedName("company_name")
    @Expose
    var company_name: String? = null

}
