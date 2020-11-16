package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NotificationResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: List<NotificationBody>? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null
    @SerializedName("pagination")
    @Expose
    var pagination: Pagination? = null

}
class NotificationBody {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("entity_id")
    @Expose
    var entity_id: Int? = null
    @SerializedName("entity_type")
    @Expose
    var entity_type: String? = null
    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("created_on_str")
    @Expose
    var created_on_str: String? = null
    @SerializedName("notification_str")
    @Expose
    var notification_str: String? = null
    @SerializedName("post_id")
    @Expose
    var post_id: Int? = null
    @SerializedName("comment_id")
    @Expose
    var comment_id: Int? = null
    @SerializedName("group_id")
    @Expose
    var group_id: Int? = null
    @SerializedName("group_name")
    @Expose
    var group_name: String? = null
    @SerializedName("viewed")
    @Expose
    var viewed: Boolean? = null
    @SerializedName("notification_type")
    @Expose
    var notification_type: String? = null
    @SerializedName("icon")
    @Expose
    var icon: String? = null


}
