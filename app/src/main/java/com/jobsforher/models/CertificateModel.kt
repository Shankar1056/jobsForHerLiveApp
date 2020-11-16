package com.jobsforher.models

class CertificateModel {


    var organization: String? = null
    var start_date: String?=null
    var end_date: String?=null
    var user_id: String?=null
    var id: Int = 0
    var skills: List<String>?=null
    var expires: String?=null
    var name: String?=null
    var skills_id: List<Int>?=null
    var description: String?=null
    var image_url: String?=null
    var expires_on: String?=null

    constructor() {}

    constructor(
        organization: String,start_date: String,user_id: String,id: Int,skills: List<String>,expires: String,name: String,skills_id: List<Int>,
        description: String, image_url: String, expires_on:String) {

        this.organization=organization
        this.start_date=start_date
        this.user_id=user_id
        this.id=id
        this.skills=skills
        this.expires=expires
        this.name=name
        this.skills_id = skills_id
        this.description=description
        this.image_url=image_url
        this.expires_on=expires_on
    }

}


