package com.jobsforher.data.model

import com.jobsforher.data.model.common_response.JobsForHerAuth

data class CompanyFollow(
    var response_code: Int? = null,
    var message: String? = null,
    var auth: JobsForHerAuth? = null,
    var body: CompanyFollowBody? = null
)

data class CompanyFollowBody(
    var id: Int? = null,
    var follower_id: Int? = null,
    var entity_id: Int? = null,
    var entity_type: String? = null,
    var modified_on: String? = null,
    var created_on: String? = null,
    var starred: Boolean? = null,
    var status: Boolean? = null,
    var company_name: String? = null,
    var mentor_name: String? = null

)