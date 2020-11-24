package com.jobsforher.network.responsemodels

data class NotificationBubbleResponse(

    var response_code: Int? = null,
    var message: String? = null,
    var body: NotificationBubbleBody? = null,
    var auth: Auth? = null,
    var pagination: Pagination? = null

)

data class NotificationBubbleBody(

    var new_notification: Int? = null

)
