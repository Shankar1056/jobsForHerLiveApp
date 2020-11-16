package com.jobsforher.network.retrofithelpers

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jobsforher.network.responsemodels.UserNameAuth
import com.jobsforher.network.responsemodels.UsernameBody

class UpdateProfileUrl {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: UsernameBody? = null
    @SerializedName("auth")
    @Expose
    var auth: UserNameAuth? = null

}
