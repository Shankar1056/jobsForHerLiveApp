package com.jobsforher.models

class GroupsEditedHistoryModel {
    var index: Int = 0
    var id: Int = 0
    var description: String? = null
    var pinned_post: String?=null
    var post_type: String?=null
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
        index: Int,id: Int, description: String, pinned_post: String, post_type: String, url: String,modified_on:String,modified_on_str:String,
        created_by:String,
        username:String,email:String,profile_icon:String,user_role:String) {

        this.index = index
        this.id = id
        this.description = description
        this.pinned_post = pinned_post
        this.post_type = post_type
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

