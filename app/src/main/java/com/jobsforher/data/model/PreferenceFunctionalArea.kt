package com.jobsforher.data.model

import com.jobsforher.data.model.common_response.JobsForHerAuth

data class PreferenceFunctionalArea(
    var response_code: Int? = null,
    var message: String? = null,
    var auth: JobsForHerAuth? = null,
    var body: ArrayList<PreferenceFunctionalAreaBody>? = null
)

data class PreferenceFunctionalAreaBody(
    var name: String? = null,
    var id: Int? = null,
    var status: Boolean? = null,
    var isClicked: Boolean? = null
)
