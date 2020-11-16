package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CitiesListResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: List<CitiesBody>? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}
class CitiesBody {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("label")
    @Expose
    var name: String? = null
    @SerializedName("ordinal")
    @Expose
    var ordinal: String? = null



}


