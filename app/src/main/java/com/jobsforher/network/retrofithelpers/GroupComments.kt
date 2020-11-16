package com.android.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class GroupComments {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("pagination")
    @Expose
    var pagination: PaginationComments? = null
    @SerializedName("body")
    @Expose
    var body: List<GroupCommentsBody>? = null
    @SerializedName("auth")
    @Expose
    var auth: com.jobsforher.network.responsemodels.Auth? = null

}
class PaginationComments{
    @SerializedName("page_no")
    @Expose
    var page_no: String? = null
    @SerializedName("page_size")
    @Expose
    var page_size: String? = null
    @SerializedName("pages")
    @Expose
    var pages: String? = null
    @SerializedName("has_next")
    @Expose
    var has_next: String? = null
    @SerializedName("has_prev")
    @Expose
    var has_prev: String? = null
    @SerializedName("next_page")
    @Expose
    var next_page: String? = null
    @SerializedName("prev_page")
    @Expose
    var prev_page: String? = null

}


class GroupCommentsBody {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("parent_id")
    @Expose
    var parent_id: String? = null
    @SerializedName("entity_type")
    @Expose
    var entity_type: String? = null
    @SerializedName("entity_value")
    @Expose
    var entity_value: String? = null
    @SerializedName("group_id")
    @Expose
    var group_id: String? = null
    @SerializedName("post_id")
    @Expose
    var post_id: String? = null
    @SerializedName("created_by")
    @Expose
    var created_by: String? = null
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
    @SerializedName("created_on_str")
    @Expose
    var created_on_str: String? = null

    @SerializedName("comment_count")
    @Expose
    var comments_count: String? = null
    @SerializedName("replies_count")
    @Expose
    var replies_count: String? = null
    @SerializedName("aggregate_count")
    @Expose
    var aggregate_count: String? = null
    @SerializedName("username")
    @Expose
    var username: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("profile_icon")
    @Expose
    var profile_icon: String? = null
    @SerializedName("is_owner")
    @Expose
    var is_owner: String? = null


//    @SerializedName("vote")
//    @Expose
//    var vote: List<Vote>? = null

}




