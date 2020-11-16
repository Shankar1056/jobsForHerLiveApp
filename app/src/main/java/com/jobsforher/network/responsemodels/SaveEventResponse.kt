package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SaveEventResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: EventResponseBody? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}

class EventResponseBody {

//
//    "posted": {
//        "txnid": 73,
//        "key": "gtKFFx"
//    },



    @SerializedName("user_id")
    @Expose
    var user_id: Int? = null
    @SerializedName("hash_key")
    @Expose
    var hash_key: String? = null
    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null
    @SerializedName("discount")
    @Expose
    var discount: Int? = null
    @SerializedName("amount")
    @Expose
    var amount: Int? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("order_id")
    @Expose
    var order_id: Int? = null
    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("package_id")
    @Expose
    var package_id: Int? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("hashh")
    @Expose
    var hashh: String? = null
    @SerializedName("hash_string")
    @Expose
    var hash_string: String? = null
    @SerializedName("key")
    @Expose
    var key: String? = null
    @SerializedName("txnid")
    @Expose
    var txnid: Int? = null
    @SerializedName("productinfo")
    @Expose
    var productinfo: Int? = null
    @SerializedName("SALT")
    @Expose
    var SALT: String? = null
    @SerializedName("action")
    @Expose
    var action: String? = null
    @SerializedName("username")
    @Expose
    var username: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("phone_no")
    @Expose
    var phone_no: String? = null
    @SerializedName("gateway")
    @Expose
    var gateway: String? = null
}
