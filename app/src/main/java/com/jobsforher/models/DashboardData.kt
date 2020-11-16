package com.jobsforher.models

import com.jobsforher.network.responsemodels.CategoryDashboard

class DashboardData {


    var my_profile:MyProfile?=null
    var rec_jobs: List<Rec_Jobs>?=null
    var rec_groups: List<Rec_Groups>? = null
    var rec_companies: List<Rec_Company>?=null
    var my_resumes:List<MyResume>?=null
    var my_preferences: List<MyPreference>?=null

    constructor() {}

    constructor(
        my_profile: MyProfile, rec_jobs: List<Rec_Jobs>,rec_groups: List<Rec_Groups>,rec_companies: List<Rec_Company>,
        my_resumes: List<MyResume>, my_preferences:List<MyPreference>) {


        this.my_profile = my_profile
        this.rec_jobs = rec_jobs
        this.rec_groups = rec_groups
        this.rec_companies = rec_companies
        this.my_resumes = my_resumes
        this.my_preferences = my_preferences
    }
}

class MyProfile{

    var id: Int? = null
    var username: String? = null
    var profile_percentage: Int? = null
    var profile_image: String? = null
    var title: String? = null
    var caption: String? = null
    var organization: String? = null
    var description: String? = null
    var location: String? = null
    var profile_summary: String? = null
    var stage_type: String? = null
    var profile_visibility: Boolean? = null
    var profile_url: String? = null
    var followers: Int? = null
    var following: Int? = null

    constructor() {}

    constructor(id: Int, username: String, profile_percentage: Int, profile_image: String, title:String,caption:String, organization:String,
                description:String, location:String, profile_summary:String, stage_type:String, profile_visibility:Boolean,profile_url:String ,followers:Int,
                following:Int) {

        this.id = id
        this.username = username
        this.profile_percentage = profile_percentage
        this.profile_image = profile_image
        this.title = title
        this.caption = caption
        this.organization = organization
        this.description = description
        this.location = location
        this.profile_summary = profile_summary
        this.stage_type = stage_type
        this.profile_visibility = profile_visibility
        this.profile_url = profile_url
        this.followers = followers
        this.following = following

    }
}

class Rec_Jobs{

    var id: Int = 0
    var modified_on: String? = null
    var application_count: Int?=null
    var company_id: Int?=null
    var job_types: List<String>? = null
    var employer_name: String?=null
    var new_application_count: Int?=null
    var title: String? = null
    var max_year: Int?=null
    var location_name: String?=null
    var company_logo: String?=null
    var created_on: String?=null
    var company_name: String?=null
    var boosted: Boolean?=null
    var min_year: Int?=null
    var employer_id: Int?=null
    var status: String?=null
    var resume_required:Boolean? = null

    constructor() {}

    constructor(
        id: Int, modified_on: String,application_count:Int,company_id:Int, job_types:List<String>,employer_name: String, new_application_count:Int,title:String, max_year: Int, location_name: String, company_logo: String,
        created_on: String, company_name: String,boosted:Boolean, min_year: Int,employer_id:Int, status: String, resume_required:Boolean) {

        this.id = id
        this.modified_on = modified_on
        this.application_count = application_count
        this.company_id = company_id
        this.job_types = job_types
        this.employer_name = employer_name
        this.new_application_count = new_application_count
        this.title = title
        this.max_year = max_year
        this.location_name = location_name
        this.company_logo = company_logo
        this.created_on = created_on
        this.company_name = company_name
        this.boosted = boosted
        this.min_year= min_year
        this.employer_id = employer_id
        this.status = status
        this.resume_required = resume_required
    }
}

class Rec_Groups {

    var id: Int = 0
    var icon_url: String? = null
    var name: String?=null
    var noOfMembers: Int?=null
    var featured: Boolean?=null
    var status: String?=null
    var cities: List<Int>?=null
    var categories: List<CategoryDashboard>?=null
    var visiblity_type: String? = null
    var excerpt: String?= null
    var is_member:Boolean? = null

    constructor() {}

    constructor(
        id: Int, icon_url: String, name: String,  noOfMembers: Int, featured: Boolean, status: String, cities: List<Int>,
        categories: List<CategoryDashboard>, visiblity_type:  String, excerpt: String,is_member:Boolean) {

        this.id = id
        this.icon_url = icon_url
        this.name = name
        this.noOfMembers = noOfMembers
        this.featured = featured
        this.status = status
        this.cities = cities
        this.categories = categories
        this.visiblity_type = visiblity_type
        this.excerpt = excerpt
        this.is_member = is_member
    }
}

class Rec_Company{

    var id: Int = 0
    var company_type: String?=null
    var featured: Boolean? = null
    var website: String?=null
    var active_jobs_count: Int? = null
    var cities: String?=null
    var follow_count: Int? = null
    var name: String?=null
    var logo: String?=null
    var industry: List<String>?=null
    var banner_image: String?=null
    var status: String? = null

    constructor() {}

    constructor(
        id: Int, company_type: String,featured:Boolean,website:String, active_jobs_count:Int,cities: String, follow_count:Int,name:String, logo: String, industry: List<String>, banner_image: String,
        status: String) {

        this.id = id
        this.company_type = company_type
        this.featured = featured
        this.website = website
        this.active_jobs_count = active_jobs_count
        this.cities = cities
        this.follow_count = follow_count
        this.name = name
        this.logo = logo
        this.industry = industry
        this.banner_image = banner_image
        this.status = status
    }
}

class MyResume{

    var id: Int = 0
    var title: String?=null
    var created_on: String? = null
    var deleted: Boolean?=null
    var is_parsed: Boolean? = null
    var modified_on: String?=null
    var is_default: String? = null
    var user_id: Int?=null
    var path: String?=null

    constructor() {}

    constructor(
        id: Int, title: String,created_on:String,deleted:Boolean, is_parsed:Boolean,modified_on: String, is_default:String,user_id:Int, path: String) {

        this.id = id
        this.title = title
        this.created_on = created_on
        this.deleted = deleted
        this.is_parsed = is_parsed
        this.modified_on = modified_on
        this.is_default = is_default
        this.user_id = user_id
        this.path = path

    }
}

class MyPreference{

    var id: Int = 0
    var preferred_job_type: String?=null
    var exp_to_year: Int? = null
    var skills: String?=null
    var preferred_city: String? = null
    var preferred_industry: String?=null
    var preferred_functional_area: String?=null
    var exp_from_year: Int? = null
    var user_id: Int?=null

    constructor() {}

    constructor(
        id: Int, preferred_job_type: String,exp_to_year:Int,skills:String, preferred_city:String,preferred_industry: String,
        preferred_functional_area:String,exp_from_year:Int, user_id: Int) {

        this.id = id
        this.preferred_job_type = preferred_job_type
        this.exp_to_year = exp_to_year
        this.skills = skills
        this.preferred_city = preferred_city
        this.preferred_industry = preferred_industry
        this.preferred_functional_area = preferred_functional_area
        this.exp_from_year = exp_from_year
        this.user_id = user_id

    }

}