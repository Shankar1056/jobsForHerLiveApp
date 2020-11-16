package com.jobsforher.network.responsemodels

import com.android.jobsforher.network.responsemodels.GroupCommentsBody
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class CreateReplyResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: GroupCommentsBody? = null
}


