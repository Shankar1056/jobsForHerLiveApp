package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NotificationBubbleResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: NotificationBubbleBody? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null
    @SerializedName("pagination")
    @Expose
    var pagination: Pagination? = null

}
class NotificationBubbleBody {

    @SerializedName("new_notification")
    @Expose
    var new_notification: Int? = null

}
