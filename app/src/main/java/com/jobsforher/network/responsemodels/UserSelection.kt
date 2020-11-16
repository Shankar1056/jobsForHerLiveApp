package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserSelection {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: Selection? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}
class Selection {

    @SerializedName("entity_values")
    @Expose
    var entity_values: String? = null
    @SerializedName("user_id")
    @Expose
    var user_id: Int? = null
    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("entity_type")
    @Expose
    var entity_type: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null

}
