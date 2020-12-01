package com.jobsforher.data.model

import com.jobsforher.data.model.common_response.JobsForHerAuth

data class PreferenceAddResponse(
    var response_code: Int? = null,
    var message: String? = null,
    var auth: JobsForHerAuth? = null,
    var body: PreferenceAddBody? = null
)

class PreferenceAddBody {

}
