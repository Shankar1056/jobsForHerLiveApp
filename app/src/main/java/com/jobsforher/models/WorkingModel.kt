package com.jobsforher.models

class WorkingModel {

    var current_company: Boolean? = null
    var start_date: String?=null
    var user_id: String?=null
    var id: Int = 0
    var skills: List<String>?=null
    var company_name: String?=null
    var skills_id: List<Int>?=null
    var description: String?=null
    var image_url: String?=null
    var location: String?=null
    var location_id: Int?=null
    var end_date: String?=null
    var designation: String?=null

    constructor() {}

    constructor(
        current_company: Boolean,start_date: String,user_id: String,id: Int,skills: List<String>,company_name: String,skills_id: List<Int>,
        description: String, image_url: String, location:String,location_id:Int,end_date:String,designation:String) {

        this.current_company=current_company
        this.start_date=start_date
        this.user_id=user_id
        this.id=id
        this.skills=skills
        this.company_name=company_name
        this.skills_id = skills_id
        this.description=description
        this.image_url=image_url
        this.location=location
        this.location_id = location_id
        this.end_date = end_date
        this.designation =designation
    }

}


