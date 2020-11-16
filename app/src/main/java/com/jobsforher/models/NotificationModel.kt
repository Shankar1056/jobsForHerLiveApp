package com.jobsforher.models

class NotificationModel {
    var id: Int = 0
    var entity_id: Int? = 0
    var entity_type: String? =null
    var created_on: String?= null
    var created_on_str: String?= null
    var notification_str:String?=null
    var post_id:Int? =0
    var comment_id:Int?=0
    var group_id:Int?= 0
    var group_name:String? =null
    var viewed:Boolean? =false
    var notification_type:String? = null
    var icon:String?=null

    constructor() {}

    constructor(id: Int, entity_id: Int,entity_type:String,created_on:String,created_on_str:String,notification_str:String,
                post_id:Int,comment_id:Int,group_id:Int,group_name:String,viewed:Boolean, notification_type:String,icon:String) {
        this.id = id
        this.entity_id = entity_id
        this.entity_type =entity_type
        this.created_on = created_on
        this.created_on_str = created_on_str
        this.notification_str = notification_str
        this.post_id = post_id
        this.comment_id = comment_id
        this.group_id = group_id
        this.group_name = group_name
        this.viewed = viewed
        this.notification_type = notification_type
        this.icon = icon

    }
}
