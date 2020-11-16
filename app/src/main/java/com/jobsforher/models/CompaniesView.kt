package com.jobsforher.models

class CompaniesView {

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