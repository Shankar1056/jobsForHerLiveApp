package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class HotJobs {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: List<JobsBody>? = null
    @SerializedName("pagination")
    @Expose
    var pagination: Pagination? = null

}
class JobsBody {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null
    @SerializedName("application_count")
    @Expose
    var application_count: Int? = null
    @SerializedName("company_id")
    @Expose
    var company_id: Int? = null
    @SerializedName("employer_name")
    @Expose
    var employer_name: String? = null
    @SerializedName("new_application_count")
    @Expose
    var new_application_count: Int? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("max_year")
    @Expose
    var max_year: Int? = null
    //    @SerializedName("location_id")
//    @Expose
//    var location_id: Int? = null
    @SerializedName("location_name")
    @Expose
    var location_name: String? = null
    @SerializedName("company_logo")
    @Expose
    var company_logo: String? = null
    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("company_name")
    @Expose
    var company_name: String? = null
    @SerializedName("boosted")
    @Expose
    var boosted: String? = null
    @SerializedName("min_year")
    @Expose
    var min_year: Int? = null
    @SerializedName("employer_id")
    @Expose
    var employer_id: Int? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("job_types")
    @Expose
    var job_types: List<String>? = null
    @SerializedName("resume_required")
    @Expose
    var resume_required: Boolean? = null

}

