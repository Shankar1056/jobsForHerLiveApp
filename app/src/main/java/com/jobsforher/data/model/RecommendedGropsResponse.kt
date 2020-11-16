package com.jobsforher.data.model

import com.jobsforher.data.model.common_response.JobsForHerAuth
import com.jobsforher.data.model.common_response.JobsForHerPagination

data class RecommendedGropsResponse(
    var response_code: Int? = null,
    var message: String? = null,
    var auth: JobsForHerAuth? = null,
    var pagination: JobsForHerPagination? = null,
    var body: ArrayList<RecommendedGropsBody>? = null

)

data class RecommendedGropsBody(
    var icon_url: String? = null,
    var featured: Boolean? = null,
    var no_of_members: Int? = null,
    var visiblity_type: String? = null,
    var name: String? = null,
    var banner_url: String? = null,
    var id: Int? = null,
    var excerpt: String? = null,
    var status: String? = null,
    var cities: List<Int>? = null,
    var categories: List<GroupCategories>? = null
)

data class GroupCategories(
    var category_id: Int? = null,
    var category: String? = null
)