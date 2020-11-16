package com.jobsforher.models


class GroupDetailsData {
    var id: Int = 0
    var name: String?=null
    var icon_url: String? = null
    var banner_url: String?=null
    var excerpt:String?=null
    var description:String?=null
    var groupType: String?=null
    var opened_to:String?=null
    var group_creation:String?=null
    var group_assocation:String?=null
    var created_by:String?=null
    var visiblity_type:String?=null
    var no_of_members: String?=null
    var notification:String?=null
    var featured: Boolean?=null
    var feature_start_date: String?=null
    var feature_end_date: String?=null
    var status: String?=null
    var cities: List<Int>?=null
    var categories: List<Categories>?=null
    var external_group: String?=null
    var created_on:String?=null
    var modified_on:String?=null
    var creator_name: String?=null
    var creator_icon: String?=null
    var my_roles: String?=null
    var is_member: String?=null
    var is_owner: String?=null
    var members: List<Members>?=null
    var member_details: List<MemberDetails>?=null


    constructor() {}

    constructor(
        id: Int, name: String,icon_url: String,banner_url: String, excerpt: String, description:String, groupType: String, opened_to: String, group_creation: String,
        group_assocation: String, created_by: String,visiblity_type: String,no_of_members: String,notification: String,
        featured: Boolean,feature_start_date: String,feature_end_date: String,status: String,categories: List<Categories>,cities: List<Int>,
        external_group: String,created_on: String,modified_on: String,creator_name: String,creator_icon: String,my_roles: String,is_member: String,
        is_owner: String,members: List<Members>,member_details: List<MemberDetails>) {

        this.id = id
        this.name = name
        this.icon_url = icon_url
        this.banner_url = banner_url
        this.excerpt = excerpt
        this.description = description
        this.groupType = groupType
        this.opened_to = opened_to
        this.group_creation = group_creation
        this.group_assocation = group_assocation
        this.created_by = created_by
        this.visiblity_type = visiblity_type
        this.no_of_members = no_of_members
        this.notification =notification
        this.featured = featured
        this.feature_start_date = feature_start_date
        this.feature_end_date = feature_end_date
        this.status = status
        this.cities = cities
        this.categories = categories
        this.external_group = external_group
        this.created_on = created_on
        this.modified_on = modified_on
        this.creator_name = creator_name
        this.creator_icon = creator_icon
        this.my_roles = my_roles
        this.is_member = is_member
        this.is_owner=is_owner
        this.members = members
        this.member_details = member_details

    }
}

class Members{
    var coadmins: List<String>? = null
    var experts: List<String>? = null

    constructor() {}

    constructor(coadmins: List<String>, experts: List<String>) {
        this.coadmins = coadmins
        this.experts = experts

    }
}

class MemberDetails{
    var is_member: Boolean = false
    var request_status: String? = null

    constructor() {}

    constructor(is_member: Boolean, request_status: String) {
        this.is_member = is_member
        this.request_status = request_status

    }
}