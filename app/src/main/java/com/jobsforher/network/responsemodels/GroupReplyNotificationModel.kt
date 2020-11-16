package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GroupReplyNotificationModel{
    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("pagination")
    @Expose
    var pagination: PaginationReplyNew? = null

    @SerializedName("body")
    @Expose
    var body: ArrayList<GroupCommentReplyBodyNew>? = null

    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}

class PaginationReplyNew {
    @SerializedName("page_no")
    @Expose
    var page_no: String? = null

    @SerializedName("page_size")
    @Expose
    var page_size: String? = null

    @SerializedName("pages")
    @Expose
    var pages: String? = null

    @SerializedName("has_next")
    @Expose
    var has_next: String? = null

    @SerializedName("has_prev")
    @Expose
    var has_prev: String? = null

    @SerializedName("next_page")
    @Expose
    var next_page: String? = null

    @SerializedName("prev_page")
    @Expose
    var prev_page: String? = null

}






