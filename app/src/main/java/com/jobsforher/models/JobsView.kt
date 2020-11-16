package com.jobsforher.models

class JobsView {

//    "location_id": null,

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