package com.jobsforher.models

class PreferenceModel {

    var industry: String? = null
    var experience_min_year: Int?=null
    var skills: String?=null
    var modified_on: String? = null
    var user_id: Int?=null
    var city_name: String?=null
    var experience_max_year: Int?=null
    var functional_area: String?= null
    var job_type: String? =null
    var title: String? = null
    var id: Int? = 0

    constructor() {}

    constructor(
        industry: String,experience_min_year: Int,skills: String,modified_on: String,user_id: Int,city_name: String,
        experience_max_year: Int,functional_area: String, job_type:String, title:String, id:Int ) {

        this.industry=industry
        this.experience_min_year=experience_min_year
        this.skills=skills
        this.modified_on=modified_on
        this.user_id=user_id
        this.city_name=city_name
        this.experience_max_year=experience_max_year
        this.functional_area = functional_area
        this.job_type = job_type
        this.title = title
        this.id = id
    }

}


