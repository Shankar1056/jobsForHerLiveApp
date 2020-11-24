package com.jobsforher.data.model

data class VoteResponseModel(
    var response_code: Int? = null,
    var message: String? = null,
    var body: VoteBody? = null
)

data class VoteBody(
    var downvote_count: Int,
    var upvote_count: Int,
    var entity_count: Int
)