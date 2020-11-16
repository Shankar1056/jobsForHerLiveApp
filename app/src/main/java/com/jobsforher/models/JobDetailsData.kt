package com.jobsforher.models

class JobDetailsData {

    var id: Int = 0
    var description:String?=null
    var location_name: String?=null
    var company_id: Int? = null
    var min_year: Int?=null
    var resume_required:Boolean?=null
    var co_owners: String?=null
    var salary:String?=null
    var specialization_id:Int?=null
    var job_posting_type:String?=null
    var title:String?=null
    var user_id:Int?=null
    var redirect_url: String?=null
    var min_qualification:String?=null
    var vacancy: String?=null
    var max_year: Int?=null
    var location_id: Int?=null
    var modified_on: String?=null
    var created_on: String?=null
    var published_on: String?=null
    var view_count: Int?=null
    var status: String?=null
    var application_notification_type: String?=null
    var boosted_expire_on: String? = null
    var boosted: Boolean?=null
    var company: CompanyDetailss?= null
    var skills: String?=null
    var functional_area: String?=null
    var industries: String?=null
    var job_types: String?=null
    var additional_information: String?=null
    var languages: String?=null
    var job_applications: JobApplication? =null
    var specialization: List<Specialization>?=null
    var job_saved: Boolean?=null

    constructor() {}

    constructor(
        id: Int, description: String,location_name: String,company_id: Int, min_year: Int, resume_required:Boolean, co_owners: String, salary: String, specialization_id: Int,
        job_posting_type: String, title: String,user_id: Int,redirect_url: String,min_qualification: String,
        vacancy: String,max_year: Int,location_id: Int,modified_on: String,created_on: String,published_on: String,
        view_count: Int,status: String,application_notification_type: String,boosted: Boolean,boosted_expire_on: String,company: CompanyDetailss,skills: String,functional_area: String,
        industries: String,job_types: String,additional_information: String, languages: String,job_applications: JobApplication,
        specialization: List<Specialization>, job_saved: Boolean) {

        this.id = id
        this.description = description
        this.location_name = location_name
        this.company_id = company_id
        this.min_year = min_year
        this.resume_required = resume_required
        this.co_owners = co_owners
        this.salary = salary
        this.specialization_id = specialization_id
        this.job_posting_type = job_posting_type
        this.title = title
        this.user_id = user_id
        this.redirect_url = redirect_url
        this.min_qualification =min_qualification
        this.vacancy = vacancy
        this.max_year = max_year
        this.location_id = location_id
        this.modified_on = modified_on
        this.created_on = created_on
        this.published_on = published_on
        this.view_count = view_count
        this.status = status
        this.application_notification_type = application_notification_type
        this.boosted = boosted
        this.boosted_expire_on = boosted_expire_on
        this.company = company
        this.skills = skills
        this.functional_area = functional_area
        this.industries=industries
        this.job_types = job_types
        this.additional_information = additional_information
        this.languages = languages
        this.job_applications = job_applications
        this.specialization = specialization
        this.job_saved = job_saved

    }
}

class Specialization{
    var course: String? = null
    var course_id: Int? = null
    var specialization: String? = null
    var specialization_id: Int? = null
    constructor() {}

    constructor(course: String, course_id: Int,specialization: String, specialization_id: Int) {
        this.course = course
        this.course_id = course_id
        this.specialization = specialization
        this.specialization_id = specialization_id

    }
}

class JobApplication{
    var applied: Boolean? = null
    var count: Int? = null

    constructor() {}

    constructor(applied: Boolean, count: Int) {
        this.applied = applied
        this.count = count

    }
}

class CompanyDetailss{

    var id: Int = 0
    var established_date: String? = null
    var company_type: String? = null
    var diversity: String?= null
    var cities: String?=null
    var banner_image : String?= null
    var logo: String?= null
    var website: String? = null
    var deleted: Boolean?= null
    var created_by: Int? = 0
    var featured: Boolean? = null
    var team_size: String? = null
    var featured_end_date: String? = null
    var industry: String? = null
    var sac_hsc_no: String? = null
    var company_profile_percentage: String? = null
    var company_profile_url: String? = null
    var modified_on: String? = null
    var gstin_no: String? = null
    var about_us: String? = null
    var created_on: String? = null
    var name: String? = null
    var view_count: String? = null
    var status: String? = null
    var culture: String? = null
    var active_jobs_count: Int? = null

    constructor() {}

    constructor(id: Int, established_date: String, company_type: String, diversity: String, cities: String,banner_image: String,
                logo: String, website: String, deleted: Boolean, created_by: Int, featured: Boolean,
                team_size: String, featured_end_date: String, industry: String, sac_hsc_no: String, company_profile_percentage: String,
                company_profile_url: String, modified_on: String, gstin_no: String, about_us: String, created_on: String, name: String,
                view_count: String,status: String, culture:String, active_jobs_count:Int) {
        this.id = id
        this.established_date = established_date
        this.company_type = company_type
        this.diversity =diversity
        this.cities = cities
        this.banner_image = banner_image
        this.logo = logo
        this.website = website
        this.deleted = deleted
        this.created_by  = created_by
        this.featured  =featured
        this.team_size = team_size
        this.featured_end_date = featured_end_date
        this.industry = industry
        this.sac_hsc_no = sac_hsc_no
        this.company_profile_percentage  =company_profile_percentage
        this.company_profile_url = company_profile_url
        this.modified_on = modified_on
        this.gstin_no = gstin_no
        this.about_us = about_us
        this.created_on = created_on
        this.name = name
        this.view_count = view_count
        this.status = status
        this.culture = culture
        this.active_jobs_count = active_jobs_count

    }
}