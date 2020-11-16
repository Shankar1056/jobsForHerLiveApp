package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PreferenceResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: List<Preference>? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}

class Preference {

    @SerializedName("industry")
    @Expose
    var industry: String? = null
    @SerializedName("experience_min_year")
    @Expose
    var experience_min_year: Int? = null
    @SerializedName("experience_max_year")
    @Expose
    var experience_max_year: Int? = null
    @SerializedName("city_name")
    @Expose
    var city_name: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("skills")
    @Expose
    var skills: String? = null
    @SerializedName("job_type")
    @Expose
    var job_type: String? = null
    @SerializedName("user_id")
    @Expose
    var user_id: Int? = null
    @SerializedName("functional_area")
    @Expose
    var functional_area: String? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null

}


