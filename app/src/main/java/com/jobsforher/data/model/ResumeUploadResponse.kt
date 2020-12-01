package com.jobsforher.data.model

import com.jobsforher.data.model.common_response.JobsForHerAuth

data class ResumeUploadResponse(
    var response_code: Int? = null,
    var message: String? = null,
    var auth: JobsForHerAuth? = null,
    var body: ResumeResponseBody? = null

)

data class ResumeResponseBody(
    var path: String? = null,
    var created_on: String? = null,
    var title: String? = null,
    var content: String? = null,
    var id: String? = null,
    var user_id: String? = null,
    var deleted: String? = null,
    var is_default: String? = null,
    var is_parsed: String? = null,
    var modified_on: String? = null
)