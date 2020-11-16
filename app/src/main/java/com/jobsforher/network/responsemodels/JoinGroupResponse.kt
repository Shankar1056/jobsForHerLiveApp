package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class JoinGroupResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: JoinBody? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}

class JoinBody {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("group_id")
    @Expose
    var group_id: Int? = null
    @SerializedName("user_id")
    @Expose
    var user_id: Int? = null

    @SerializedName("role")
    @Expose
    var role: String? = null

    @SerializedName("request_status")
    @Expose
    var request_status: String? = null
    @SerializedName("approver_id")
    @Expose
    var approver_id: String? = null
    @SerializedName("is_member")
    @Expose
    var is_member: Boolean? = null
    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null

}


