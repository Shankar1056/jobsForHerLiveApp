package com.jobsforher.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jobsforher.network.responsemodels.Auth
import com.jobsforher.network.responsemodels.GroupCommentReplyBodyNew
import com.jobsforher.network.responsemodels.PaginationCommentsNew

class ReplyNotificationModel {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("pagination")
    @Expose
    var pagination: PaginationCommentsNew? = null

    @SerializedName("body")
    @Expose
    var body: GroupCommentReplyBodyNew? = null

    @SerializedName("auth")
    @Expose
    var auth: Auth? = null
}