package com.jobsforher.data.model

import com.jobsforher.data.model.common_response.JobsForHerAuth

data class PreferenceCityResponse(
    var response_code: Int? = null,
    var message: String? = null,
    var auth: JobsForHerAuth? = null,
    var body: ArrayList<PreferenceCityBody>? = null
)

data class PreferenceCityBody(
    var id: Int? = null,
    var value: String? = null,
    var label: String? = null,
    var ordinal: Int? = null,
    var isClicked: Boolean? = null
)