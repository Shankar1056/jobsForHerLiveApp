package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GroupCategoriesResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: List<CategoriesBody>? = null
    @SerializedName("pagination")
    @Expose
    var pagination: CategoryPagination? = null

}

class CategoriesBody {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("image_url")
    @Expose
    var image_url: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null

}

class CategoryPagination{

    @SerializedName("page_no")
    @Expose
    var page_no: String? = null
    @SerializedName("page_size")
    @Expose
    var page_size: String? = null
    @SerializedName("has_next")
    @Expose
    var has_next: Boolean? = null
    @SerializedName("total_items")
    @Expose
    var total_items: String? = null
    @SerializedName("next_page")
    @Expose
    var next_page: String? = null
}