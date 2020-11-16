package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class AppliedJobDetails {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body:  List<AppliedJobDetailsBody>? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null
    @SerializedName("pagination")
    @Expose
    var pagination: Pagination? = null

}
class AppliedJobDetailsBody {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("company_id")
    @Expose
    var company_id: Int? = null
    @SerializedName("is_shortlisted")
    @Expose
    var is_shortlisted: Boolean? = null
    @SerializedName("employer_id")
    @Expose
    var employer_id: Int? = null
    @SerializedName("employer_note")
    @Expose
    var employer_note: String? = null
    @SerializedName("resume_id")
    @Expose
    var resume_id: Int? = null
    @SerializedName("location")
    @Expose
    var location: String? = null
    @SerializedName("job_status")
    @Expose
    var job_status: String? = null
    @SerializedName("new_applicant")
    @Expose
    var new_applicant: Boolean? = null
    @SerializedName("applied_status")
    @Expose
    var applied_status: String? = null
    @SerializedName("note")
    @Expose
    var note: String? = null
    @SerializedName("job_id")
    @Expose
    var job_id: Int? = null
    @SerializedName("user_id")
    @Expose
    var user_id: Int? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null
    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("job_boosted")
    @Expose
    var job_boosted: Boolean? = null
    @SerializedName("min_year")
    @Expose
    var min_year: Int? = null
    @SerializedName("max_year")
    @Expose
    var max_year: Int? = null
    @SerializedName("job_posting_type")
    @Expose
    var job_posting_type: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("featured")
    @Expose
    var featured: Boolean? = null
    @SerializedName("logo")
    @Expose
    var logo: String? = null
    @SerializedName("company_name")
    @Expose
    var company_name: String? = null
    @SerializedName("company_status")
    @Expose
    var company_status: String? = null
    @SerializedName("company_deleted")
    @Expose
    var company_deleted: String? = null
    @SerializedName("company_logo")
    @Expose
    var company_logo: String? = null
    @SerializedName("location_name")
    @Expose
    var location_name: String? = null
}