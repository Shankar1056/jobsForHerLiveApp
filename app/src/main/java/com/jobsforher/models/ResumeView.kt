package com.jobsforher.models

class ResumeView {

    var is_default: String? = null
    var path: String?=null
    var id: Int = 0
    var title: String?=null
    var is_parsed: Boolean?=null
    var created_on: String?=null
    var modified_on: String?=null
    var deleted: Boolean?=null
    var user_id: Int = 0

    constructor() {}

    constructor(is_default:String, path:String,
                id: Int, title: String, is_parsed:Boolean, created_on: String, modified_on: String, deleted:Boolean,user_id:Int ) {

        this.is_default = is_default
        this.path = path
        this.id = id
        this.title = title
        this.is_parsed = is_parsed
        this.created_on = created_on
        this.modified_on = modified_on
        this.deleted = deleted
        this.user_id = user_id

    }

}
