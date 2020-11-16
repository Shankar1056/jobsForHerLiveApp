package com.jobsforher.models

class AppliedJobDetailsData {

    var id: Int = 0
    var company_id: Int? = null
    var is_shortlisted: Boolean?=null
    var employer_id: Int? = null
    var employer_note: String?=null
    var resume_id:Int?=null
    var location:String?=null
    var job_status: String?=null
    var new_applicant:Boolean?=null
    var applied_status:String?=null
    var note:String?=null
    var job_id:Int?=null
    var user_id:Int?=null
    var modified_on: String?=null
    var created_on: String?=null
    var title: String?=null
    var job_boosted:Boolean?=null
    var min_year: Int?=null
    var max_year: Int?=null
    var job_posting_type: String?=null
    var name: String?=null
    var featured: Boolean?=null
    var logo: String?=null
    var status: String?=null

    constructor() {}

    constructor(
        id: Int, company_id: Int,is_shortlisted: Boolean, employer_id: Int, employer_note:String, resume_id: Int,location: String,
        job_status: String, new_applicant: Boolean,
        applied_status: String, note: String,job_id: Int,user_id: Int,modified_on: String,
        created_on: String,title: String,job_boosted: Boolean,min_year: Int,max_year: Int,job_posting_type: String,
        name: String,featured: Boolean,logo: String,string: String) {

        this.id = id
        this.company_id = company_id
        this.is_shortlisted = is_shortlisted
        this.employer_id = employer_id
        this.employer_note = employer_note
        this.resume_id = resume_id
        this.location = location
        this.job_status = job_status
        this.new_applicant = new_applicant
        this.applied_status = applied_status
        this.note = note
        this.job_id = job_id
        this.user_id = user_id
        this.modified_on =modified_on
        this.created_on = created_on
        this.title = title
        this.job_boosted = job_boosted
        this.min_year = min_year
        this.max_year = max_year
        this.job_posting_type = job_posting_type
        this.name = name
        this.featured = featured
        this.logo = logo
        this.status = status
    }
}
