package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CollegesListResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: List<CollegeBody>? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}

class CollegeBody {

    @SerializedName("status")
    @Expose
    var status: Boolean = true
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("college")
    @Expose
    var college: String? = null

}
