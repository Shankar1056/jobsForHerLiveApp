package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FollowersResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: FollowesBody? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}
class FollowesBody {

    @SerializedName("company_id")
    @Expose
    var company_id: List<Int>? = null

}
