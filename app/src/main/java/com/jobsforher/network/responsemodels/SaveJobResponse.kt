package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SaveJobResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: JobResponseBody? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}

class JobResponseBody {

    @SerializedName("job_id")
    @Expose
    var job_id: Int? = null
    @SerializedName("company_id")
    @Expose
    var company_id: Int? = null
    @SerializedName("employer_note")
    @Expose
    var employer_note: String? = null
    @SerializedName("job_status")
    @Expose
    var job_status: String? = null
    @SerializedName("new_applicant")
    @Expose
    var new_applicant: Boolean? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("note")
    @Expose
    var note: String? = null
    @SerializedName("applied_status")
    @Expose
    var applied_status: String? = null
    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null
    @SerializedName("employer_id")
    @Expose
    var employer_id: String? = null
    @SerializedName("resume_id")
    @Expose
    var resume_id: String? = null
    @SerializedName("is_shortlisted")
    @Expose
    var is_shortlisted: Boolean? = null
    @SerializedName("user_id")
    @Expose
    var user_id: Int? = null
    @SerializedName("location")
    @Expose
    var location: String? = null
}
