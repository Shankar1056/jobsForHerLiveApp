package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CheckUsernameResponse {

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
class UsernameBody {

    @SerializedName("suggestion")
    @Expose
    var suggestion: String? = null


}

class UserNameAuth {

    @SerializedName("token_type")
    @Expose
    var tokenType: String? = null
    @SerializedName("access_token")
    @Expose
    var accessToken: String? = null
    @SerializedName("refresh_token")
    @Expose
    var refreshToken: String? = null
    @SerializedName("issued_at")
    @Expose
    var issuedAt: Int? = null
    @SerializedName("expires_in")
    @Expose
    var expiresIn: Int? = null
}



