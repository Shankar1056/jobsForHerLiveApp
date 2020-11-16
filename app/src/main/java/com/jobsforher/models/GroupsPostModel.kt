package com.jobsforher.models

class GroupsPostModel {
    var id: Int = 0
    var description: String? = null
    var group_id: String?=null
    var created_by: String?=null
    var pinned_post: String?=null
    var post_type: String?=null
    var upvote_count: String?=null
    var downvote_count: String?=null
    var url: String?=null
    var hash_tags: String?=null
    var status: String?=null
    var edited: String?=null
    var created_on: String?=null
    var modified_on: String?=null
    var aggregate_count: String?=null
    var comments_count: String?=null
    var created_on_str: String?=null
    var username: String?=null
    var email: String?=null
    var profile_icon: String?=null
    var user_role: String?=null
    var is_owner: String?=null
    var comment_list: GroupsCommentModel?=null


    constructor() {}

    constructor(
        id: Int, description: String, group_id: String, created_by:String, pinned_post: String, post_type: String, upvote_count: String,
        downvote_count: String, url: String, hash_tags: String, status: String,edited:String,created_on:String,modified_on:String,aggregate_count:String,
        comments_count:String,created_on_str:String,username:String,email:String,profile_icon:String,user_role:String,is_owner:String,
        commentsList: GroupsCommentModel) {

        this.id = id
        this.description = description
        this.group_id = group_id
        this.created_by = created_by
        this.pinned_post = pinned_post
        this.post_type = post_type
        this.upvote_count = upvote_count
        this.downvote_count = downvote_count
        this.url = url
        this.hash_tags = hash_tags
        this.status = status
        this.edited=edited
        this.created_on=created_on
        this.modified_on=modified_on
        this.aggregate_count=aggregate_count
        this.comments_count=comments_count
        this.created_on_str=created_on_str
        this.username=username
        this.email=email
        this.profile_icon=profile_icon
        this.user_role=user_role
        this.is_owner=is_owner
        this.comment_list = commentsList

    }

    constructor(
        id: Int, description: String, group_id: String, created_by:String, pinned_post: String, post_type: String, upvote_count: String,
        downvote_count: String, url: String, hash_tags: String, status: String,edited:String,created_on:String,modified_on:String,aggregate_count:String,
        comments_count:String,created_on_str:String,username:String,email:String,profile_icon:String,user_role:String,is_owner:String) {

        this.id = id
        this.description = description
        this.group_id = group_id
        this.created_by = created_by
        this.pinned_post = pinned_post
        this.post_type = post_type
        this.upvote_count = upvote_count
        this.downvote_count = downvote_count
        this.url = url
        this.hash_tags = hash_tags
        this.status = status
        this.edited=edited
        this.created_on=created_on
        this.modified_on=modified_on
        this.aggregate_count=aggregate_count
        this.comments_count=comments_count
        this.created_on_str=created_on_str
        this.username=username
        this.email=email
        this.profile_icon=profile_icon
        this.user_role=user_role
        this.is_owner=is_owner
    }
}

