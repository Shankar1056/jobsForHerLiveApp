package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegisterIdResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: Body? = null

}

class Body {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("device_id")
    @Expose
    var device_id: String? = null
    @SerializedName("user_id")
    @Expose
    var user_id: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("created_on")
    @Expose
    var created_on: String? = null

    @SerializedName("modified_on")
    @Expose
    var modified_on: Boolean? = null

}
