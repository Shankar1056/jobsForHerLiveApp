package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class JobDetails {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body:  List<JobDetailsBody>? = null

}
class JobDetailsBody {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("location_name")
    @Expose
    var location_name: String? = null
    @SerializedName("company_id")
    @Expose
    var company_id: Int? = null
    @SerializedName("min_year")
    @Expose
    var min_year: Int? = null
    @SerializedName("resume_required")
    @Expose
    var resume_required: Boolean? = null
    @SerializedName("co_owners")
    @Expose
    var co_owners: String? = null
    @SerializedName("salary")
    @Expose
    var salary: String? = null
    @SerializedName("specialization_id")
    @Expose
    var specialization_id: Int? = null
    @SerializedName("job_posting_type")
    @Expose
    var job_posting_type: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("user_id")
    @Expose
    var user_id: Int? = null
    @SerializedName("redirect_url")
    @Expose
    var redirect_url: String? = null
    @SerializedName("min_qualification")
    @Expose
    var min_qualification: String? = null
    @SerializedName("vacancy")
    @Expose
    var vacancy: String? = null
    @SerializedName("max_year")
    @Expose
    var max_year: Int? = null
    @SerializedName("location_id")
    @Expose
    var location_id: Int? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null
    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("published_on")
    @Expose
    var published_on: String? = null
    @SerializedName("view_count")
    @Expose
    var view_count: Int? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("application_notification_type")
    @Expose
    var application_notification_type: String? = null
    @SerializedName("boosted")
    @Expose
    var boosted: Boolean? = null
    @SerializedName("boosted_expire_on")
    @Expose
    var boosted_expire_on: String? = null
    @SerializedName("skills")
    @Expose
    var skills: String? = null
    @SerializedName("functional_area")
    @Expose
    var functional_area: String? = null
    @SerializedName("industries")
    @Expose
    var industries: String? = null
    @SerializedName("job_types")
    @Expose
    var job_types: String? = null
    @SerializedName("additional_information")
    @Expose
    var additional_information: String? = null
    @SerializedName("languages")
    @Expose
    var languages: String? = null
    @SerializedName("specialization")
    @Expose
    var specialization: List<Specialization>? = null
    @SerializedName("job_saved")
    @Expose
    var job_saved: Boolean? = null
    @SerializedName("company")
    @Expose
    var company: CompanyDetails? = null
    @SerializedName("job_applications")
    @Expose
    var job_applications: JobApplication? = null

}

class Specialization{
    @SerializedName("course")
    @Expose
    var course: String? = null
    @SerializedName("course_id")
    @Expose
    var course_id: Int? = null
    @SerializedName("specialization")
    @Expose
    var specialization: String? = null
    @SerializedName("specialization_id")
    @Expose
    var specialization_id: Int? = null
}


class JobApplication{

    @SerializedName("applied")
    @Expose
    var applied: Boolean? = null
    @SerializedName("count")
    @Expose
    var count: Int? = null

}

class CompanyDetails{

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("established_date")
    @Expose
    var established_date: String? = null
    @SerializedName("company_type")
    @Expose
    var company_type: String? = null
    @SerializedName("diversity")
    @Expose
    var diversity: String? = null
    @SerializedName("cities")
    @Expose
    var cities: String? = null
    @SerializedName("banner_image")
    @Expose
    var banner_image: String? = null
    @SerializedName("logo")
    @Expose
    var logo: String? = null
    @SerializedName("website")
    @Expose
    var website: String? = null
    @SerializedName("deleted")
    @Expose
    var deleted: Boolean? = null
    @SerializedName("created_by")
    @Expose
    var created_by: Int? = null
    @SerializedName("featured")
    @Expose
    var featured: Boolean? = null
    @SerializedName("team_size")
    @Expose
    var team_size: String? = null
    @SerializedName("featured_end_date")
    @Expose
    var featured_end_date: String? = null
    @SerializedName("industry")
    @Expose
    var industry: String? = null
    @SerializedName("sac_hsc_no")
    @Expose
    var sac_hsc_no: String? = null
    @SerializedName("company_profile_percentage")
    @Expose
    var company_profile_percentage: String? = null
    @SerializedName("company_profile_url")
    @Expose
    var company_profile_url: String? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null
    @SerializedName("gstin_no")
    @Expose
    var gstin_no: String? = null
    @SerializedName("about_us")
    @Expose
    var about_us: String? = null
    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("view_count")
    @Expose
    var view_count: Int? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("culture")
    @Expose
    var culture: String? = null
    @SerializedName("active_jobs_count")
    @Expose
    var active_jobs_count: Int? = null
}
