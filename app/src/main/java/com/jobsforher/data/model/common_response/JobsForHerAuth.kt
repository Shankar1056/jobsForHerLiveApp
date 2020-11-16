package com.jobsforher.data.model.common_response

data class JobsForHerAuth(
    var token_type: String? = null,
    var access_token: String? = null,
    var refresh_token: String? = null,
    var issued_at: Long? = null,
    var expires_in: Long? = null
)