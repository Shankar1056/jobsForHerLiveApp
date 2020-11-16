package com.jobsforher.data.model


data class ExpertChatResponse(
    var status: String? = null,
    var response_code: Int? = null,
    var auth: ExpertChatAuth? = null,
    var body: ArrayList<ExpertChatBody>? = null

)


data class ExpertChatBody(
    var id: Int? = null,
    var title: String? = null,
    var date: String? = null,
    var start_time: String? = null,
    var end_time: String? = null,
    var speaker: String? = null,
    var designation: String? = null,
    var group_id: Int? = null,
    var post_id: Int? = null,
    var description: String? = null,
    var created_by: Int? = null,
    var created_on: String? = null,
    var modified_on: String? = null,
    var group_name: String? = null
)

class ExpertChatAuth {

}
