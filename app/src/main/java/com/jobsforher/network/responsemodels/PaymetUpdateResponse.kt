package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PaymetUpdateResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: ResponseBody? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}

class ResponseBody{

    @SerializedName("registered_event")
    @Expose
    var registered_event: REvent? = null
}

class REvent{
    @SerializedName("id")
    @Expose
    var id: Int? = null
}



