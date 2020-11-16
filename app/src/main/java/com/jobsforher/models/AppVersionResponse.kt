package com.jobsforher.models

class AppVersionResponse {
    var response_code: Int? = null
    var message: String? = null
    var body: AppVersionBody? = null
}

data class AppVersionBody(
    var created_on: String? = null,
    var version: Int? = null,
    var id: Int? = null,
    var status: String
)