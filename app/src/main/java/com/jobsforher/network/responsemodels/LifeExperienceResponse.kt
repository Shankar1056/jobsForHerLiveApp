package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LifeExperienceResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: List<ExpBody>? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}

class ExpBody {

    @SerializedName("status")
    @Expose
    var status: Boolean = true
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null

}
