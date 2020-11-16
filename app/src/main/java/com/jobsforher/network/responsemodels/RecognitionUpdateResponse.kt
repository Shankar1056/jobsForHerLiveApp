package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RecognitionUpdateResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: RecognitionUpdateBody? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}
class RecognitionUpdateBody {

    @SerializedName("organization")
    @Expose
    var organization: String? = null
    @SerializedName("start_date")
    @Expose
    var start_date: String? = null
    @SerializedName("user_id")
    @Expose
    var user_id: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("image_url")
    @Expose
    var image_url: String? = null

}
