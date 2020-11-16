package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReportResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: ReportBody? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}

class ReportBody {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("user_id")
    @Expose
    var user_id: Int? = null
    @SerializedName("entity_id")
    @Expose
    var entity_id: Int? = null
    @SerializedName("entity_type")
    @Expose
    var entity_type: String? = null

    @SerializedName("problem_type")
    @Expose
    var problem_type: String? = null
    @SerializedName("reason")
    @Expose
    var reason: String? = null
    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null


}


