package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class CompanyApiDetails {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body:  CompanyDetailsBody? = null

}
class CompanyDetailsBody {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("industry")
    @Expose
    var industry: String? = null
    @SerializedName("view_count")
    @Expose
    var view_count: Int? = null
    @SerializedName("banner_image")
    @Expose
    var banner_image: String? = null
    @SerializedName("website")
    @Expose
    var website: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("cities")
    @Expose
    var cities: String? = null
    @SerializedName("deleted")
    @Expose
    var deleted: Boolean? = null
    @SerializedName("sac_hsc_no")
    @Expose
    var sac_hsc_no: String? = null
    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("established_date")
    @Expose
    var established_date: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("company_profile_url")
    @Expose
    var company_profile_url: String? = null
    @SerializedName("company_profile_percentage")
    @Expose
    var company_profile_percentage: String? = null
    @SerializedName("team_size")
    @Expose
    var team_size: String? = null
    @SerializedName("diversity")
    @Expose
    var diversity: String? = null
    @SerializedName("culture")
    @Expose
    var culture: String? = null
    @SerializedName("created_by")
    @Expose
    var created_by: Int? = null
    @SerializedName("featured")
    @Expose
    var featured: Boolean? = null
    @SerializedName("gstin_no")
    @Expose
    var gstin_no: String? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null
    @SerializedName("about_us")
    @Expose
    var about_us: String? = null
    @SerializedName("logo")
    @Expose
    var logo: String? = null
    @SerializedName("company_type")
    @Expose
    var company_type: String? = null
    @SerializedName("featured_end_date")
    @Expose
    var featured_end_date: String? = null
    @SerializedName("active_jobs_count")
    @Expose
    var active_jobs_count: Int? = null

    @SerializedName("locations")
    @Expose
    var locations: List<Location>? = null

    @SerializedName("policies")
    @Expose
    var policies: Policies? = null

    @SerializedName("jobs")
    @Expose
    var jobs: List<JobData>? = null

    @SerializedName("employers")
    @Expose
    var employers: List<Employers>? = null

    @SerializedName("images")
    @Expose
    var images: CompanyImages? = null

    @SerializedName("followers")
    @Expose
    var followers: Int? = null

    @SerializedName("groups")
    @Expose
    var groups: List<CompanyGroups>? = null

    @SerializedName("videos")
    @Expose
    var videos: List<CompanyVideos>? = null

    @SerializedName("events")
    @Expose
    var events: List<Events>? = null

    @SerializedName("testimonials")
    @Expose
    var testimonials: List<Testimonials>? = null

    @SerializedName("blogs")
    @Expose
    var blogs: List<Blogs>? = null

}

class Location{

    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("hide_on_web")
    @Expose
    var hide_on_web: Boolean? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("state_id")
    @Expose
    var state_id: Int? = null
    @SerializedName("state_name")
    @Expose
    var state_name: String? = null
    @SerializedName("city_id")
    @Expose
    var city_id: Int? = null
    @SerializedName("city_name")
    @Expose
    var city_name: String? = null
    @SerializedName("country_id")
    @Expose
    var country_id: Int? = null
    @SerializedName("pincode")
    @Expose
    var pincode: Int? = null
    @SerializedName("country_name")
    @Expose
    var country_name: String? = null
    @SerializedName("location_type")
    @Expose
    var location_type: String? = null
    @SerializedName("address_2")
    @Expose
    var address_2: String? = null
    @SerializedName("company_id")
    @Expose
    var company_id: Int? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null

}

class Policies{

    @SerializedName("maternity")
    @Expose
    var maternity: PoliciesData? = null
    @SerializedName("paternity")
    @Expose
    var paternity: PoliciesData? = null
    @SerializedName("child-care")
    @Expose
    var childcare: PoliciesData? = null
    @SerializedName("harassment")
    @Expose
    var harassment: PoliciesData? = null
    @SerializedName("transportation")
    @Expose
    var transportation: PoliciesData? = null
    @SerializedName("flexi")
    @Expose
    var flexi: PoliciesData? = null
    @SerializedName("other")
    @Expose
    var other: PoliciesData? = null
    @SerializedName("transport")
    @Expose
    var transport: PoliciesData? = null

}

class PoliciesData{

    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("company_id")
    @Expose
    var company_id: Int? = null
    @SerializedName("policy_type")
    @Expose
    var policy_type: String? = null
    @SerializedName("image_url")
    @Expose
    var image_url: String? = null
}

class JobData{

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("view_count")
    @Expose
    var view_count: Int? = null
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
    @SerializedName("specialization_id")
    @Expose
    var specialization_id: Int? = null
    @SerializedName("user_id")
    @Expose
    var user_id: Int? = null
    @SerializedName("max_year")
    @Expose
    var max_year: Int? = null
    @SerializedName("company_id")
    @Expose
    var company_id: Int? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null
    @SerializedName("redirect_url")
    @Expose
    var redirect_url: String? = null
    @SerializedName("co_owners")
    @Expose
    var co_owners: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("published_on")
    @Expose
    var published_on: String? = null
    @SerializedName("job_posting_type")
    @Expose
    var job_posting_type: String? = null
    @SerializedName("vacancy")
    @Expose
    var vacancy: String? = null
    @SerializedName("application_notification_type")
    @Expose
    var application_notification_type: String? = null
    @SerializedName("salary")
    @Expose
    var salary: String? = null
    @SerializedName("min_qualification")
    @Expose
    var min_qualification: String? = null

}

class Employers{

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("designation")
    @Expose
    var designation: String? = null
    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("boost_count")
    @Expose
    var boost_count: Int? = null
    @SerializedName("employer_role")
    @Expose
    var employer_role: String? = null
    @SerializedName("share_job_access")
    @Expose
    var share_job_access: Boolean? = null
    @SerializedName("notification_type")
    @Expose
    var notification_type: String? = null
    @SerializedName("package_job_count")
    @Expose
    var package_job_count: Int? = null
    @SerializedName("email_verified")
    @Expose
    var email_verified: Int? = null
    @SerializedName("job_count")
    @Expose
    var job_count: Int? = null
    @SerializedName("application_access")
    @Expose
    var application_access: Boolean? = null
    @SerializedName("user_id")
    @Expose
    var user_id: Int? = null
    @SerializedName("subscription")
    @Expose
    var subscription: Int? = null
    @SerializedName("paid")
    @Expose
    var paid: Boolean? = null
    @SerializedName("deleted")
    @Expose
    var deleted: Boolean? = null
    @SerializedName("transfer_job_access")
    @Expose
    var transfer_job_access: Boolean? = null
    @SerializedName("employer_status")
    @Expose
    var employer_status: String? = null
    @SerializedName("company_id")
    @Expose
    var company_id: Int? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null
}

class CompanyImages{

    @SerializedName("about_us")
    @Expose
    var about_us: List<ImagesData>? = null
    @SerializedName("featured_images")
    @Expose
    var featured_images: List<ImagesData>? = null
    @SerializedName("culture")
    @Expose
    var culture: List<ImagesData>? = null
    @SerializedName("diversity")
    @Expose
    var diversity: List<ImagesData>? = null

}

class ImagesData{

    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("image_category")
    @Expose
    var image_category: String? = null
    @SerializedName("company_id")
    @Expose
    var company_id: Int? = null
    @SerializedName("image_url")
    @Expose
    var image_url: String? = null
}

class CompanyGroups{

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("icon_url")
    @Expose
    var icon_url: String? = null
    @SerializedName("banner_url")
    @Expose
    var banner_url: String? = null
    @SerializedName("excerpt")
    @Expose
    var excerpt: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("group_type")
    @Expose
    var group_type: String? = null
    @SerializedName("opened_to")
    @Expose
    var opened_to: String? = null
    @SerializedName("group_creation")
    @Expose
    var group_creation: String? = null
    @SerializedName("group_assocation")
    @Expose
    var group_assocation: Int? = null
    @SerializedName("created_by")
    @Expose
    var created_by: Int? = null
    @SerializedName("visiblity_type")
    @Expose
    var visiblity_type: String? = null
    @SerializedName("no_of_members")
    @Expose
    var no_of_members: Int? = null
    @SerializedName("notification")
    @Expose
    var notification: Boolean? = null
    @SerializedName("featured")
    @Expose
    var featured: Boolean? = null
    @SerializedName("feature_start_date")
    @Expose
    var feature_start_date: String? = null
    @SerializedName("feature_end_date")
    @Expose
    var feature_end_date: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("categories")
    @Expose
    var categories: String? = null
    @SerializedName("cities")
    @Expose
    var cities: String? = null
    @SerializedName("notes")
    @Expose
    var notes: String? = null
    @SerializedName("phone_number")
    @Expose
    var phone_number: String? = null
    @SerializedName("external_group")
    @Expose
    var external_group: Int? = null
    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null
}

class CompanyVideos{

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("video_url")
    @Expose
    var video_url: String? = null
    @SerializedName("created_by")
    @Expose
    var created_by: String? = null
    @SerializedName("company_id")
    @Expose
    var company_id: Int? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null
}

class Events{

    @SerializedName("events_category")
    @Expose
    var events_category: String? = null
    @SerializedName("modified_by")
    @Expose
    var modified_by: String? = null
    @SerializedName("interested_count")
    @Expose
    var interested_count: Int? = null
    @SerializedName("view_count")
    @Expose
    var view_count: Int? = null
    @SerializedName("share_count")
    @Expose
    var share_count: Int? = null
    @SerializedName("payment")
    @Expose
    var payment: Boolean? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("priority_order")
    @Expose
    var priority_order: Boolean? = null
    @SerializedName("excerpt")
    @Expose
    var excerpt: String? = null
    @SerializedName("event_register_start_date_time")
    @Expose
    var event_register_start_date_time: String? = null
    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("author_id")
    @Expose
    var author_id: String? = null
    @SerializedName("is_online")
    @Expose
    var is_online: Boolean? = null
    @SerializedName("gtm_id")
    @Expose
    var gtm_id: Int? = null
    @SerializedName("author_name")
    @Expose
    var author_name: String? = null
    @SerializedName("is_private")
    @Expose
    var is_private: Boolean? = null
    @SerializedName("register_count")
    @Expose
    var register_count: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("event_end_date_time")
    @Expose
    var event_end_date_time: String? = null
    @SerializedName("slug")
    @Expose
    var slug: String? = null
    @SerializedName("event_register_end_date_time")
    @Expose
    var event_register_end_date_time: String? = null
    @SerializedName("event_by")
    @Expose
    var event_by: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("post_for")
    @Expose
    var post_for: String? = null
    @SerializedName("publish_date")
    @Expose
    var publish_date: String? = null
    @SerializedName("event_start_date_time")
    @Expose
    var event_start_date_time: String? = null
    @SerializedName("created_by")
    @Expose
    var created_by: Int? = null
    @SerializedName("company_id")
    @Expose
    var company_id: Int? = null
    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null
    @SerializedName("payment_note")
    @Expose
    var payment_note: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("city")
    @Expose
    var city: String? = null
    @SerializedName("event_url")
    @Expose
    var event_url: String? = null
    @SerializedName("show_on_search")
    @Expose
    var show_on_search: Boolean? = null
    @SerializedName("image_url")
    @Expose
    var image_url: String? = null
    @SerializedName("thumbnail_url")
    @Expose
    var thumbnail_url: String? = null
    @SerializedName("link_companies_name")
    @Expose
    var link_companies_name: List<EventCompanies>? = null

}

class Testimonials{

    @SerializedName("designation")
    @Expose
    var designation: String? = null
    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("image_icon")
    @Expose
    var image_icon: String? = null
    @SerializedName("created_by")
    @Expose
    var created_by: Int? = null
    @SerializedName("company_name")
    @Expose
    var company_name: String? = null
    @SerializedName("testimonial")
    @Expose
    var testimonial: String? = null
    @SerializedName("company_id")
    @Expose
    var company_id: Int? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null
}

class Blogs{

    @SerializedName("blog_view")
    @Expose
    var blog_view: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("excerpt")
    @Expose
    var excerpt: String? = null
    @SerializedName("show_register_message")
    @Expose
    var show_register_message: Boolean? = null
    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("author_id")
    @Expose
    var author_id: Int? = null
    @SerializedName("author_name")
    @Expose
    var author_name: String? = null
    @SerializedName("category")
    @Expose
    var category: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("slug")
    @Expose
    var slug: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("blog_like")
    @Expose
    var blog_like: String? = null
    @SerializedName("post_for")
    @Expose
    var post_for: String? = null
    @SerializedName("publish_date")
    @Expose
    var publish_date: String? = null
    @SerializedName("admin_id")
    @Expose
    var admin_id: Int? = null
    @SerializedName("created_by")
    @Expose
    var created_by: Int? = null
    @SerializedName("company_id")
    @Expose
    var company_id: Int? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("modified_id")
    @Expose
    var modified_id: Int? = null

    @SerializedName("city")
    @Expose
    var city: String? = null
    @SerializedName("blog_link")
    @Expose
    var blog_link: String? = null
    @SerializedName("blog_share")
    @Expose
    var blog_share: String? = null
    @SerializedName("image_url")
    @Expose
    var image_url: String? = null

}