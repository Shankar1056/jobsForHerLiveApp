package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class GetCityResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: List<CityListBody>? = null
}

class CityListBody {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("label")
    @Expose
    var name: String? = null
}

