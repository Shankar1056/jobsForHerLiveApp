package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class GroupDetails {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: GroupDetailsBody? = null
    @SerializedName("auth")
    @Expose
    var auth: com.jobsforher.network.responsemodels.Auth? = null

}
class GroupDetailsBody {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("icon_url")
    @Expose
    var icon_url: String? = null
    @SerializedName("banner_url")
    @Expose
    var banner_url: String? = null
    @SerializedName("excerpt")
    @Expose
    var excerpt: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("group_type")
    @Expose
    var group_type: String? = null
    @SerializedName("opened_to")
    @Expose
    var opened_to: String? = null
    @SerializedName("group_creation")
    @Expose
    var group_creation: String? = null
    @SerializedName("group_assocation")
    @Expose
    var group_assocation: String? = null
    @SerializedName("created_by")
    @Expose
    var created_by: String? = null
    @SerializedName("visiblity_type")
    @Expose
    var visiblity_type: String? = null
    @SerializedName("no_of_members")
    @Expose
    var no_of_members: String? = null
    @SerializedName("notification")
    @Expose
    var notification: String? = null
    @SerializedName("featured")
    @Expose
    var featured: String? = null
    @SerializedName("feature_start_date")
    @Expose
    var feature_start_date: String? = null
    @SerializedName("feature_end_date")
    @Expose
    var feature_end_date: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("categories")
    @Expose
    var categories: List<Category>? = null
    @SerializedName("cities")
    @Expose
    var cities: List<Int>? = null
    @SerializedName("external_group")
    @Expose
    var external_group: Int? = null
    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null
    @SerializedName("creator_name")
    @Expose
    var creator_name: String? = null
    @SerializedName("creator_icon")
    @Expose
    var creator_icon: String? = null
    @SerializedName("my_roles")
    @Expose
    var my_roles: String? = null
    @SerializedName("is_member")
    @Expose
    var is_member: Boolean? = null
    @SerializedName("is_owner")
    @Expose
    var is_owner: Boolean? = null
    @SerializedName("members")
    @Expose
    var members: Members? = null
    @SerializedName("member_details")
    @Expose
    var member_details: MemberDetails? = null
}


class Members{

    @SerializedName("is_member")
    @Expose
    var is_member: Boolean? = null
    @SerializedName("request_status")
    @Expose
    var request_status: String? = null

}

class MemberDetails{

    @SerializedName("coadmins")
    @Expose
    var coadmins: List<String>? = null
    @SerializedName("experts")
    @Expose
    var experts: List<String>? = null

}
