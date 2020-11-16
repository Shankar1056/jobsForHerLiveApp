package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DegreesListResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: List<DegreeBody>? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}
class DegreeBody {

    @SerializedName("status")
    @Expose
    var status: Boolean = true
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("degree")
    @Expose
    var degree: String? = null


}
