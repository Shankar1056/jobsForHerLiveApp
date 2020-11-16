package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class DashboardApiDetails {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body:  DashboardBody? = null

}
class DashboardBody{

    @SerializedName("my_profile")
    @Expose
    var my_profile:  ProfileData? = null
    @SerializedName("rec_jobs")
    @Expose
    var rec_jobs:  List<Rec_JobData>? = null
    @SerializedName("rec_groups")
    @Expose
    var rec_groups:  List<Rec_Groups>? = null
    @SerializedName("rec_companies")
    @Expose
    var rec_companies:  List<Rec_Companies>? = null
    @SerializedName("my_resumes")
    @Expose
    var my_resumes:  List<MyResumes>? = null
    @SerializedName("my_preferences")
    @Expose
    var my_preferences:  List<MyPreferences>? = null
    @SerializedName("saved_jobs_count")
    @Expose
    var saved_jobs_count:  Int? = null
    @SerializedName("applied_jobs_count")
    @Expose
    var applied_jobs_count:  Int? = null
    @SerializedName("rec_jobs_count")
    @Expose
    var rec_jobs_count:  Int? = null
    @SerializedName("rec_groups_count")
    @Expose
    var rec_groups_count:  Int? = null
    @SerializedName("my_groups_count")
    @Expose
    var my_groups_count:  Int? = null
    @SerializedName("rec_companies_count")
    @Expose
    var rec_companies_count:  Int? = null
    @SerializedName("followed_comp_count")
    @Expose
    var followed_comp_count:  Int? = null

}

class ProfileData{

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("username")
    @Expose
    var username: String? = null
    @SerializedName("profile_percentage")
    @Expose
    var profile_percentage: Int? = null
    @SerializedName("profile_image")
    @Expose
    var profile_image: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("caption")
    @Expose
    var caption: String? = null
    @SerializedName("organization")
    @Expose
    var organization: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("location")
    @Expose
    var location: String? = null
    @SerializedName("profile_summary")
    @Expose
    var profile_summary: String? = null
    @SerializedName("stage_type")
    @Expose
    var stage_type: String? = null
    @SerializedName("profile_visibility")
    @Expose
    var profile_visibility: String? = null
    @SerializedName("profile_url")
    @Expose
    var profile_url: String? = null
    @SerializedName("followers")
    @Expose
    var followers: String? = null
    @SerializedName("following")
    @Expose
    var following: String? = null

}

class Rec_JobData{

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("location_id")
    @Expose
    var location_id: Int? = null
    @SerializedName("boosted")
    @Expose
    var boosted: Boolean? = null
    @SerializedName("min_year")
    @Expose
    var min_year: Int? = null
    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("location_name")
    @Expose
    var location_name: String? = null
    @SerializedName("resume_required")
    @Expose
    var resume_required: Boolean? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("max_year")
    @Expose
    var max_year: Int? = null
    @SerializedName("company_id")
    @Expose
    var company_id: Int? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null
    @SerializedName("application_count")
    @Expose
    var application_count: Int? = null
    @SerializedName("employer_name")
    @Expose
    var employer_name: String? = null
    @SerializedName("new_application_count")
    @Expose
    var new_application_count: Int? = null
    @SerializedName("employer_id")
    @Expose
    var employer_id: Int? = null
    @SerializedName("company_logo")
    @Expose
    var company_logo: String? = null
    @SerializedName("company_name")
    @Expose
    var company_name: String? = null
    @SerializedName("job_types")
    @Expose
    var job_types: List<String>? = null

}

class Rec_Groups{

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("icon_url")
    @Expose
    var icon_url: String? = null
    @SerializedName("featured")
    @Expose
    var featured: Boolean? = null
    @SerializedName("no_of_members")
    @Expose
    var no_of_members: Int? = null
    @SerializedName("cities")
    @Expose
    var cities: List<Int>? = null
    @SerializedName("visiblity_type")
    @Expose
    var visiblity_type: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("categories")
    @Expose
    var categories: List<CategoryDashboard>? = null
    @SerializedName("excerpt")
    @Expose
    var excerpt: String? = null
    @SerializedName("is_member")
    @Expose
    var is_member: String? = null
}

class CategoryDashboard{
    @SerializedName("category_id")
    @Expose
    var category_id: Int? = null
    @SerializedName("category")
    @Expose
    var category: String? = null
}

class Rec_Companies{

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
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
}

class MyResumes{

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("is_parsed")
    @Expose
    var is_parsed: Boolean? = null
    @SerializedName("deleted")
    @Expose
    var deleted: Boolean? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null
    @SerializedName("path")
    @Expose
    var path: String? = null
    @SerializedName("user_id")
    @Expose
    var user_id: Int? = null
    @SerializedName("is_default")
    @Expose
    var is_default: Boolean? = null
}

class MyPreferences{

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("preferred_job_type")
    @Expose
    var preferred_job_type: String? = null
    @SerializedName("preferred_functional_area")
    @Expose
    var preferred_functional_area: String? = null
    @SerializedName("exp_from_year")
    @Expose
    var exp_from_year: Int? = null
    @SerializedName("preferred_city")
    @Expose
    var preferred_city: String? = null
    @SerializedName("exp_to_year")
    @Expose
    var exp_to_year: Int? = null
    @SerializedName("user_id")
    @Expose
    var user_id: Int? = null
    @SerializedName("skills")
    @Expose
    var skills: String? = null
    @SerializedName("preferred_industry")
    @Expose
    var preferred_industry: String? = null
}