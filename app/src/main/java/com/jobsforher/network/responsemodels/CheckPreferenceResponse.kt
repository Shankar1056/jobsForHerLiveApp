package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CheckPreferenceResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: List<PreferenceBody>? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}

class PreferenceBody {

    @SerializedName("exp_to_year")
    @Expose
    var exp_to_year: Int? = null
    @SerializedName("preferred_city")
    @Expose
    var preferred_city: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("exp_from_year")
    @Expose
    var exp_from_year: Int? = null
    @SerializedName("skills")
    @Expose
    var skills: String? = null
    @SerializedName("preferred_job_type")
    @Expose
    var preferred_job_type: String? = null
    @SerializedName("user_id")
    @Expose
    var user_id: Int? = null
    @SerializedName("preferred_industry")
    @Expose
    var preferred_industry: String? = null
    @SerializedName("preferred_functional_area")
    @Expose
    var preferred_functional_area: String? = null
    @SerializedName("preferred_salary")
    @Expose
    var preferred_salary: String? = null

}


