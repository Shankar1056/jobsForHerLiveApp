package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Featured_Group {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: List<GroupBody>? = null
    @SerializedName("pagination")
    @Expose
    var pagination: Pagination? = null

}
class GroupBody {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("icon_url")
    @Expose
    var icon_url: String? = null
    @SerializedName("featured")
    @Expose
    var featured: String? = null
    @SerializedName("no_of_members")
    @Expose
    var no_of_members: String? = null
    @SerializedName("visiblity_type")
    @Expose
    var visiblity_type: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("excerpt")
    @Expose
    var excerpt: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("cities")
    @Expose
    var cities: List<Int>? = null
    @SerializedName("categories")
    @Expose
    var categories: List<Category>? = null
    @SerializedName("is_member")
    @Expose
    var is_member: Boolean? = null

}

class Pagination{
    @SerializedName("page_no")
    @Expose
    var page_no: String? = null
    @SerializedName("page_size")
    @Expose
    var page_size: String? = null
    @SerializedName("pages")
    @Expose
    var pages: String? = null
    @SerializedName("total_items")
    @Expose
    var total_items: String? = null
    @SerializedName("next_page")
    @Expose
    var next_page: String? = null
    //    @SerializedName("prev_page")
//    @Expose
//    var prev_page: Boolean? = null
    @SerializedName("has_next")
    @Expose
    var has_next: String? = null
    @SerializedName("has_prev")
    @Expose
    var has_prev: Boolean? = null


}

class Category{

    @SerializedName("category_id")
    @Expose
    var category_id: Int? = null
    @SerializedName("category")
    @Expose
    var category: String? = null

}
