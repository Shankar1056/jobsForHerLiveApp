package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ImageResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("body")
    @Expose
    var body: List<ImageBody>? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}


class ImageBody {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("url")
    @Expose
    var url: String? = null


}




