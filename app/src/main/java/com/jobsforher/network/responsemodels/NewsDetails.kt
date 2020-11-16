package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jobsforher.models.GroupsCommentModel


class NewsDetails {
    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("body")
    @Expose
    var body: ArrayList<NewsDetailsBody>? = null

    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

    @SerializedName("pagination")
    @Expose
    var pagination: PaginationPostsNew? = null

}

class NewsDetailsBody {

    @SerializedName("downvote_count")
    @Expose
    var downvote_count: Int? = null

    @SerializedName("upvote_count")
    @Expose
    var upvote_count: Int? = null

    @SerializedName("post_type")
    @Expose
    var post_type: String? = null

    @SerializedName("hash_tags")
    @Expose
    var hash_tags: String? = null

    @SerializedName("created_on")
    @Expose
    var created_on: String? = null

    @SerializedName("created_by")
    @Expose
    var created_by: Int? = null

    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("edited")
    @Expose
    var edited: Boolean? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("pinned_post")
    @Expose
    var pinned_post: Boolean? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("group_id")
    @Expose
    var group_id: Int? = null

    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null

    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("profile_icon")
    @Expose
    var profile_icon: String? = null

    @SerializedName("aggregate_count")
    @Expose
    var aggregate_count: Int? = null

    @SerializedName("comments_count")
    @Expose
    var comments_count: String? = null

    @SerializedName("time_ago")
    @Expose
    var created_on_str: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("user_role")
    @Expose
    var user_role: String? = null

    @SerializedName("is_owner")
    @Expose
    var is_owner: String? = null
    var comment_list: GroupsCommentModel? = null


}
