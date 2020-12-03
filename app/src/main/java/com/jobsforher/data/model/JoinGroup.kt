package com.jobsforher.data.model

import com.jobsforher.data.model.common_response.JobsForHerAuth

data class JoinGroup(
    var response_code: Int? = null,
    var message: String? = null,
    var auth: JobsForHerAuth? = null,
    var body: JoinGroupBody? = null
)

data class JoinGroupBody(
    var id: Int? = null,
    var group_id: Int? = null,
    var user_id: Int? = null,
    var role: String? = null,
    var request_status: String? = null,
    var approver_id: String? = null,
    var is_member: Boolean? = null,
    var created_on: Boolean? = null,
    var modified_on: Boolean? = null
)