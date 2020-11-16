package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UpdateSelection {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    //    @SerializedName("body")
//    @Expose
//    var body: Selection? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}

