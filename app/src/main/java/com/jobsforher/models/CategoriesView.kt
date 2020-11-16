package com.jobsforher.models

class CategoriesView {

    var id: Int = 0
    var name: String? = null
    var image_url: String?=null
    var status: String?=null
    var created_on: String?=null
    var modified_on: String?=null

    constructor() {}

    constructor(
        id: Int, name: String, image_url: String, status:String, created_on: String, modified_on: String) {

        this.id = id
        this.name = name
        this.image_url = image_url
        this.status = status
        this.created_on = created_on
        this.modified_on = modified_on

    }

}
