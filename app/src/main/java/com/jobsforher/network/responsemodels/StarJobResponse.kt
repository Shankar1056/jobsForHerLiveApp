package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StarJobResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: StarJobResponseBody? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}

class StarJobResponseBody {

    @SerializedName("job_id")
    @Expose
    var job_id: Int? = null
    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("company_id")
    @Expose
    var company_id: Int? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("user_id")
    @Expose
    var user_id: Int? = null
}
