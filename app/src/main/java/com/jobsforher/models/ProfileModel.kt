package com.jobsforher.models

class ProfileModel  {
    var title: String?=null
    var caption: String?=null
    var organization: String?=null
    var description: String?=null
    var location: String?=null
    var profile_summary: String?=null
    var stage_type: String?=null
    var id: Int = 0
    var profile_url: String? = null
    var my_profile: String?=null
    var already_following: String?=null
    var username: String?=null
    var profile_percentage: String?=null
    var profile_image: String?=null
    var followers: String?=null
    var following: String?=null
    var updates: UpdatesModel?=null
    var timeline_list: List<TimeListModel>?=null
    var skills_list: SkillsList?=null

    constructor() {}

    constructor(
        title: String,caption: String,organization: String,description: String,location: String,profile_summary: String,stage_type: String,
        id: Int, profile_url: String, my_profile: String, already_following:String,username:String, profile_percentage: String, profile_image: String, followers: String,
        following: String, updates: UpdatesModel, timeline_list: List<TimeListModel>,skills_list: SkillsList) {

        this.title=title
        this.caption=caption
        this.organization=organization
        this.description=description
        this.location=location
        this.profile_summary=profile_summary
        this.stage_type=stage_type
        this.id = id
        this.profile_url=profile_url
        this.my_profile=my_profile
        this.already_following=already_following
        this.username=username
        this.profile_percentage=profile_percentage
        this.profile_image=profile_image
        this.following=following
        this.updates=updates
        this.timeline_list=timeline_list
        this.skills_list =skills_list
    }

}

class UpdatesModel{
    var created_on: String?=null
    var id: Int = 0
    var user_v3_id: String? = null
    var description: String?=null
    var image_url: String?=null

    constructor() {}

    constructor(created_on:String, id: Int, user_v3_id: String, description: String, image_url:String) {

        this.created_on=created_on
        this.id = id
        this.user_v3_id=user_v3_id
        this.description=description
        this.image_url=image_url
    }
}

class TimeListModel{

    var location: String?=null
    var designation: String?=null
    var end_date: String?=null
    var current_company: String?=null
    var organization: String?= null
    var start_date: String?=null
    var skills: List<String>? = null
    var lifeexperience: List<String>? = null
    var id: Int = 0
    var expires: String?=null
    var name: String?=null
    var duration: String?=null
    var type: String?=null
    var description: String?=null
    var image_url: String?=null
    var expires_on: String?=null
    var title :String?=null
    var caption: String?=null
    var startdte: String?=null
    var degree:String?=null
    var ongoing:Boolean?=null
    var college:String?=null



    constructor() {}

    constructor( id: Int, duration:String,start_date: String, end_date: String, skills:List<String>, image_url:String,type: String,
                 location: String,degree:String,ongoing:Boolean,college:String,description:String) {

        this.id=id
        this.duration = duration
        this.start_date = start_date
        this.end_date = end_date
        this.type=type
        this.skills  =skills
        this.image_url=image_url
        this.location = location
        this.degree = degree
        this.ongoing = ongoing
        this.college = college
        this.description = description


    }


    constructor( id: Int, location:String ,duration:String,start_date:String,end_date: String,type: String,description: String,
                 lifeexperience:List<String>,image_url:String,caption: String, skills:List<String> ) {

        this.id=id
        this.location =location
        this.duration = duration
        this.start_date = start_date
        this.end_date = end_date
        this.type=type
        this.description=description
        this.lifeexperience =lifeexperience
        this.image_url=image_url
        this.caption =caption
        this.skills  =skills

    }

    constructor(organization:String,start_date:String, id: Int, type:String, description:String ,title:String,image_url:String ) {

        this.organization=organization
        this.start_date = start_date
        this.id=id
        this.type=type
        this.description=description
        this.title =title
        this.image_url=image_url

    }

    constructor(organization:String,start_date:String, skills:List<String>, id: Int, location: String, designation: String, duration:String,
                end_date:String, current_company:String,type:String, description:String ,image_url:String ) {

        this.organization=organization
        this.start_date = start_date
        this.skills=skills
        this.id=id
        this.location =location
        this.designation = designation
        this.duration =duration
        this.end_date = end_date
        this.current_company = current_company
        this.type=type
        this.description=description
        this.image_url=image_url

    }

    constructor(organization:String,start_date:String, end_date: String, skills:List<String>, id: Int, expires: String, name: String, duration:String,
                type:String, description:String,image_url:String, expires_on:String  ) {

        this.organization=organization
        this.start_date = start_date
        this.end_date = end_date
        this.skills=skills
        this.id=id
        this.expires=expires
        this.name=name
        this.duration=duration
        this.type=type
        this.description=description
        this.image_url=image_url
        this.expires_on=expires_on
    }
}


class SkillsList{

    var work_skills: List<String>?=null
    var life_experience_skills: List<String>?=null
    var education_skills: List<String>?=null

    constructor() {}

    constructor( work_skills: List<String>, life_experience_skills: List<String>,education_skills: List<String>) {

        this.work_skills = work_skills
        this.life_experience_skills = life_experience_skills
        this.education_skills = education_skills
    }
}

