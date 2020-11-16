package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Company {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: List<CompanyBody>? = null
    @SerializedName("pagination")
    @Expose
    var pagination: Pagination? = null

}
class CompanyBody {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("company_type")
    @Expose
    var company_type: String? = null
    @SerializedName("featured")
    @Expose
    var featured: Boolean? = null
    @SerializedName("website")
    @Expose
    var website: String? = null
    @SerializedName("active_jobs_count")
    @Expose
    var active_jobs_count: Int? = null
    @SerializedName("cities")
    @Expose
    var cities: String? = null
    @SerializedName("follow_count")
    @Expose
    var follow_count: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("logo")
    @Expose
    var logo: String? = null
    @SerializedName("industry")
    @Expose
    var industry: List<String>? = null
    @SerializedName("banner_image")
    @Expose
    var banner_image: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null

}

