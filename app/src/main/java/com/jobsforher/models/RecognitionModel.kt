package com.jobsforher.models

class RecognitionModel {


    var organization: String? = null
    var start_date: String?=null
    var user_id: String?=null
    var id: Int = 0
    var description: String?=null
    var name: String?=null
    var image_url: String?=null

    constructor() {}

    constructor(
        organization: String,start_date: String,user_id: String,id: Int,description: String,name: String,
        image_url: String) {

        this.organization=organization
        this.start_date=start_date
        this.user_id=user_id
        this.id=id
        this.description=description
        this.name=name
        this.image_url=image_url
    }

}


