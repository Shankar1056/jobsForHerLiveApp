package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetGroupMemberResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: List<Map<String, MemberBody>>? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}


class MemberBody{
    @SerializedName("member_count")
    @Expose
    var member_count: Int? = null
    @SerializedName("post_count")
    @Expose
    var post_count: Int? = null
}