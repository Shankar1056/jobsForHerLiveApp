package com.jobsforher.models

class EducationModel {



    var college: String? = null
    var end_date: String? = null
    var skills: List<String>?=null
    var college_id: Int? = 0
    var location: String? = null
    var image_url: String?=null

    var degree: String?=null
    var ongoing: Boolean?=null
    var location_id: Int?=0
    var description: String?=null
    var skills_id: List<Int>?=null
    var user_id: Int?=0
    var start_date: String?=null
    var id: Int = 0
    var degree_id: Int = 0

    constructor() {}

    constructor(college: String, end_date: String,skills: List<String>, college_id: Int,location : String,
                image_url: String, degree: String, ongoing:Boolean, location_id: Int, description: String,
                skills_id: List<Int>, user_id: Int, start_date: String, id: Int, degree_id: Int) {

        this.college=college
        this.end_date=end_date
        this.skills=skills
        this.college_id=college_id
        this.location=location

        this.image_url=image_url
        this.degree = degree
        this.ongoing=ongoing
        this.location_id=location_id
        this.description=description
        this.skills_id = skills_id
        this.user_id = user_id
        this.start_date =start_date
        this.id =id
        this.degree_id =degree_id

    }
}




