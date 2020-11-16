package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class CreatePostResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: CreatePostsBody? = null
}

class CreatePostsBody {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("group_id")
    @Expose
    var group_id: String? = null
    @SerializedName("created_by")
    @Expose
    var created_by: String? = null
    @SerializedName("pinned_post")
    @Expose
    var pinned_post: String? = null
    @SerializedName("post_type")
    @Expose
    var post_type: String? = null
    @SerializedName("upvote_count")
    @Expose
    var upvote_count: String? = null
    @SerializedName("downvote_count")
    @Expose
    var downvote_count: String? = null
    @SerializedName("url")
    @Expose
    var url: String? = null
    @SerializedName("hash_tags")
    @Expose
    var hash_tags: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("edited")
    @Expose
    var edited: String? = null
    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null
    @SerializedName("aggregate_count")
    @Expose
    var aggregate_count: String? = null
    @SerializedName("comments_count")
    @Expose
    var comments_count: String? = null
    @SerializedName("created_on_str")
    @Expose
    var created_on_str: String? = null
    @SerializedName("username")
    @Expose
    var username: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("profile_icon")
    @Expose
    var profile_icon: String? = null
    @SerializedName("user_role")
    @Expose
    var user_role: String? = null
    @SerializedName("is_owner")
    @Expose
    var is_owner: String? = null
//    @SerializedName("vote")
//    @Expose
//    var vote: List<Vote>? = null

}


