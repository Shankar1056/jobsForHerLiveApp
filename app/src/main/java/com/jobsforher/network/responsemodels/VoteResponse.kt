package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class VoteResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: VoteBody? = null
}

class VoteBody {

    @SerializedName("downvote_count")
    @Expose
    var downvote_count: Int? = null
    @SerializedName("upvote_count")
    @Expose
    var upvote_count: Int? = null
    @SerializedName("entity_count")
    @Expose
    var entity_count: String? = null

}

