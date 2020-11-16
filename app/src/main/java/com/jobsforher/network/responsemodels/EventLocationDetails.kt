package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class EventLocationDetails {


    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: List<EventLocationsBody>? = null
    @SerializedName("pagination")
    @Expose
    var pagination: Pagination? = null

}

class EventLocationsBody{

    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("country")
    @Expose
    var country: String? = null
    @SerializedName("external_url")
    @Expose
    var external_url: String? = null
    @SerializedName("seats")
    @Expose
    var seats: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("discount_active")
    @Expose
    var discount_active: String? = null
    @SerializedName("pincode")
    @Expose
    var pincode: String? = null
    @SerializedName("event_end_date_time")
    @Expose
    var event_end_date_time: String? = null
    @SerializedName("registration_open")
    @Expose
    var registration_open: String? = null
    @SerializedName("event_register_start_date_time")
    @Expose
    var event_register_start_date_time: String? = null
    @SerializedName("amount")
    @Expose
    var amount: Int? = null
    @SerializedName("event_id")
    @Expose
    var event_id: Int? = null
    @SerializedName("city")
    @Expose
    var city: String? = null
    @SerializedName("state")
    @Expose
    var state: String? = null
    @SerializedName("event_register_end_date_time")
    @Expose
    var event_register_end_date_time: String? = null
    @SerializedName("google_map_url")
    @Expose
    var google_map_url: String? = null
    @SerializedName("discounted_price")
    @Expose
    var discounted_price: Int? = null
    @SerializedName("discount_end_date_time")
    @Expose
    var discount_end_date_time: String? = null
    @SerializedName("event_start_date_time")
    @Expose
    var event_start_date_time: String? = null
    @SerializedName("discount_start_date_time")
    @Expose
    var discount_start_date_time: String? = null
    @SerializedName("event_type")
    @Expose
    var event_type: String? = null

}

