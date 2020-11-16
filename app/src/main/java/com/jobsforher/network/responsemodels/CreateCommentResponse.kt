package com.jobsforher.network.responsemodels

import com.android.jobsforher.network.responsemodels.GroupPostsBody
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class CreateCommentResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: GroupPostsBody? = null
}


