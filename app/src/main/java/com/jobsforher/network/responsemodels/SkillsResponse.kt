package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SkillsResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: List<SkillsBody>? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}

class SkillsBody {

    @SerializedName("approve")
    @Expose
    var status: Boolean = true
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("category_type")
    @Expose
    var category_type: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null

}
