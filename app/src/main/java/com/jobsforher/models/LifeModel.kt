package com.jobsforher.models

class LifeModel {

    var ongoing: Boolean? = null
    var start_date: String?=null
    var user_id: String?=null
    var id: Int = 0
    var skills: List<String>?=null
    var skills_id: List<Int>?=null
    var description: String?=null
    var image_url: String?=null
    var location: String?=null
    var location_id: Int?=null
    var end_date: String?=null
    var caption: String?=null
    var life_experience: List<String>?=null
    var life_experience_id: List<Int>?=null
    var duration: String?=null


    constructor() {}

    constructor(
        ongoing: Boolean,start_date: String,user_id: String,id: Int,skills: List<String>,skills_id: List<Int>,
        description: String, image_url: String, location:String,location_id:Int,end_date:String,caption:String,
        life_experience: List<String>,life_experience_id: List<Int>, duration: String) {

        this.ongoing=ongoing
        this.start_date=start_date
        this.user_id=user_id
        this.id=id
        this.skills=skills
        this.skills_id = skills_id
        this.description=description
        this.image_url=image_url
        this.location=location
        this.location_id = location_id
        this.end_date = end_date
        this.caption =caption
        this.life_experience = life_experience
        this.life_experience_id = life_experience_id
        this.duration = duration

    }

}


