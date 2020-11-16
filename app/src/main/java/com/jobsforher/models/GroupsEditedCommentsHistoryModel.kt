package com.jobsforher.models

class GroupsEditedCommentsHistoryModel {
    var index: Int = 0
    var entity_type: String? = null
    var entity_value: String?=null
    var url: String?=null
    var modified_on: String?=null
    var modified_on_str: String?=null
    var created_by: String?=null
    var username: String?=null
    var email: String?=null
    var profile_icon: String?=null
    var user_role: String?=null

    constructor() {}

    constructor(
        index: Int, entity_type: String, entity_value: String,  url: String,modified_on:String,modified_on_str:String,
        created_by:String,
        username:String,email:String,profile_icon:String,user_role:String) {

        this.index = index
        this.entity_type = entity_type
        this.entity_value = entity_value
        this.url = url
        this.modified_on=modified_on
        this.modified_on_str=modified_on_str
        this.created_by = created_by
        this.username=username
        this.email=email
        this.profile_icon=profile_icon
        this.user_role=user_role
    }

}

