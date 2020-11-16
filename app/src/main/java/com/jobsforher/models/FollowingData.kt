package com.jobsforher.models

class FollowingData {

    var user_following: List<Following>?=null

    constructor() {}
    constructor(
        user_following: List<Following>) {

        this.user_following = user_following
    }


}

class Following{
    var user_id: Int = 0
    var username: String?=null
    var stage_type:String?=null
    var profile_url: String?=null
    var profile_icon:String?=null
    var profile_image:String?=null
    var company_name: String?=null

    constructor() {}

    constructor(
        user_id: Int, username:String,stage_type: String,
        profile_url: String, profile_icon: String, profile_image: String,company_name: String) {

        this.user_id = user_id
        this.username = username
        this.stage_type = stage_type
        this.profile_url = profile_url
        this.profile_icon = profile_icon
        this.profile_image = profile_image
        this.company_name = company_name
    }
}
