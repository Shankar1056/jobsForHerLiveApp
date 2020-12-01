package com.jobsforher.data.model

import com.jobsforher.data.model.common_response.JobsForHerAuth

data class PreferenceListResponse (
        var response_code: Int? = null,
        var message: String? = null,
        var auth: JobsForHerAuth? = null,
        var body: ArrayList<PreferenceListBody>? = null
    )

    data class PreferenceListBody(
        var exp_to_year: Int? = null,
        var user_id: Int? = null,
        var skills: String? = null,
        var preferred_salary: String? = null,
        var preferred_job_type: String? = null,
        var preferred_industry: String? = null,
        var exp_from_year: Int? = null,
        var preferred_city: String? = null,
        var preferred_functional_area: String? = null
    )
