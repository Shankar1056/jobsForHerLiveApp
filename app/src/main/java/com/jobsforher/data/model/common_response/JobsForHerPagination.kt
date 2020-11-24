package com.jobsforher.data.model.common_response

data class JobsForHerPagination(
    var page_no: String? = null,
    var page_size: String? = null,
    var pages: Int? = null,
    var total_items: Int? = null,
    var has_next: Boolean? = null,
    var next_page: String? = null,
    var prev_page: String? = null
)