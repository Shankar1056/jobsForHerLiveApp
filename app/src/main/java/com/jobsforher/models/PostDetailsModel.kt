package com.jobsforher.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jobsforher.network.responsemodels.GroupPostsBodyNew
import com.jobsforher.network.responsemodels.PaginationPostsNew

class PostDetailsModel {
    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("pagination")
    @Expose
    var pagination: PaginationPostsNew? = null

    @SerializedName("body")
    @Expose
    var body: GroupPostsBodyNew? = null

    @SerializedName("auth")
    @Expose
    var auth: com.jobsforher.network.responsemodels.Auth? = null
}