package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CertificateResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: List<CertificateBody>? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}
class CertificateBody {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("organization")
    @Expose
    var organization: String? = null
    @SerializedName("start_date")
    @Expose
    var start_date: String? = null
    @SerializedName("end_date")
    @Expose
    var end_date: String? = null
    @SerializedName("user_id")
    @Expose
    var user_id: String? = null
    @SerializedName("skills")
    @Expose
    var skills: List<String>? = null
    @SerializedName("expires")
    @Expose
    var expires: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("skills_id")
    @Expose
    var skills_id: List<String>? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("image_url")
    @Expose
    var image_url: String? = null
    @SerializedName("expires_on")
    @Expose
    var expires_on: String? = null

}
