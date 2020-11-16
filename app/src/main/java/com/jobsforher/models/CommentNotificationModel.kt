package com.jobsforher.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jobsforher.network.responsemodels.Auth
import com.jobsforher.network.responsemodels.GroupCommentsBodyNew
import com.jobsforher.network.responsemodels.PaginationCommentsNew

class CommentNotificationModel {

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
    var body: GroupCommentsBodyNew? = null

    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}