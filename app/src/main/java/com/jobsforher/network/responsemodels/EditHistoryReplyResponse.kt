package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class EditHistoryReplyResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: List<EditHistoryReplyBody>? = null
}

class EditHistoryReplyBody {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("parent_id")
    @Expose
    var parent_id: Int? = null
    @SerializedName("entity_type")
    @Expose
    var entity_type: String? = null
    @SerializedName("entity_value")
    @Expose
    var entity_value: String? = null
    @SerializedName("url")
    @Expose
    var url: String? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null
    @SerializedName("modified_on_str")
    @Expose
    var modified_on_str: String? = null
    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("username")
    @Expose
    var username: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("profile_icon")
    @Expose
    var profile_icon: String? = null

}


