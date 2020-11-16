package com.jobsforher.models

class GroupsView {

    var id: Int = 0
    var icon_url: String? = null
    var label: String?=null
    var location: String?=null
    var groupType: String?=null
    var noOfMembers: String?=null
    var description: String?=null
    var featured: Boolean?=null
    var status: String?=null
    var cities: List<Int>?=null
    var categories: List<Categories>?=null
    var is_member: Boolean? = null

    constructor() {}

    constructor(
        id: Int, icon_url: String, label: String, location:String, groupType: String, noOfMembers: String, description: String,
        featured: Boolean, status: String, cities: List<Int>, categories: List<Categories>, is_member:Boolean) {

        this.id = id
        this.icon_url = icon_url
        this.label = label
        this.location = location
        this.groupType = groupType
        this.noOfMembers = noOfMembers
        this.description = description
        this.featured = featured
        this.status = status
        this.cities = cities
        this.categories = categories
        this.is_member = is_member
    }
}