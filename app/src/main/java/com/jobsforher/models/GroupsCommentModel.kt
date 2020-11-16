package com.jobsforher.models

class GroupsCommentModel {
    var id: Int = 0
    var parent_id: String? = null
    var entity_type: String?=null
    var entity_value: String?=null
    var group_id: String?=null
    var post_id: String?=null
    var created_by: String?=null
    var upvote_count: String?=null
    var downvote_count: String?=null
    var url: String?=null
    var hash_tags: String?=null
    var status: String?=null
    var edited: String?=null
    var created_on: String?=null
    var modified_on: String?=null
    var created_on_str: String?=null
    var comment_count: String?=null
    var replies_counts:String?=null
    var aggregate_count:String?=null
    var username: String?=null
    var email: String?=null
    var profile_icon: String?=null
    var is_owner: String?=null
    var reply_list: GroupsReplyModel?=null

    constructor() {}

    constructor(
        id: Int, parent_id: String, entity_type: String, entity_value:String, group_id: String, post_id: String, created_by:String, upvote_count: String,
        downvote_count: String, url: String, hash_tags: String, status: String,edited:String,created_on:String,modified_on:String,created_on_str:String,
        comment_count:String,replies_count:String,aggregate_count:String,username:String,email:String,profile_icon:String,is_owner:String,
        reply_list: GroupsReplyModel) {

        this.id = id
        this.parent_id = parent_id
        this.entity_type = entity_type
        this.entity_value = entity_value
        this.group_id = group_id
        this.post_id = post_id
        this.created_by = created_by
        this.upvote_count = upvote_count
        this.downvote_count = downvote_count
        this.url = url
        this.hash_tags = hash_tags
        this.status = status
        this.edited=edited
        this.created_on=created_on
        this.modified_on=modified_on
        this.created_on_str=created_on_str
        this.comment_count=comment_count
        this.replies_counts=replies_count
        this.aggregate_count=aggregate_count
        this.username=username
        this.email=email
        this.profile_icon=profile_icon
        this.is_owner=is_owner
        this.reply_list = reply_list

    }

}