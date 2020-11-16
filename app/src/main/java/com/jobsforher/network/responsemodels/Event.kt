package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Event {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: List<EventBody>? = null
    @SerializedName("pagination")
    @Expose
    var pagination: Pagination? = null
}

class EventBody{

    //"link_partners": "13,5",
    //"link_groups": "1,22,48",
    //"link_mentor": "1,23,45",
    //"link_jobs": "13,4",
    //"view_for": "",
    //"link_companies": "12",
    //"display_name": "free_event",
    // "thumbnail_url": null,

//    "is_open": true,
//    "events_images": [],

    //link_companies_name

    @SerializedName("link_companies_name")
    @Expose
    var link_companies_name: String? = null
    @SerializedName("discount_start_date_time")
    @Expose
    var discount_start_date_time: String? = null
    @SerializedName("discount_end_date_time")
    @Expose
    var discount_end_date_time: String? = null

    @SerializedName("price_after_discount")
    @Expose
    var price_after_discount: Int? = null
    @SerializedName("price_before_discount")
    @Expose
    var price_before_discount: Int? = null
    @SerializedName("registration_required")
    @Expose
    var registration_required: Boolean? = null
    @SerializedName("events_category")
    @Expose
    var events_category: String? = null
    @SerializedName("events_locations")
    @Expose
    var events_locations: List<EventLocations>? = null
    @SerializedName("registered")
    @Expose
    var registered: Boolean? = null
    @SerializedName("interested")
    @Expose
    var interested: Boolean? = null
    @SerializedName("company_name")
    @Expose
    var company_name: String? = null
    @SerializedName("resume_required")
    @Expose
    var resume_required: Boolean? = null
    @SerializedName("ticket_type")
    @Expose
    var ticket_type: String? = null
    @SerializedName("seats")
    @Expose
    var seats: String? = null
    @SerializedName("event_type")
    @Expose
    var event_type: String? = null
    @SerializedName("display_price")
    @Expose
    var display_price: Int? = null
    @SerializedName("agenda")
    @Expose
    var agenda: String? = null
    @SerializedName("featured_end_date_time")
    @Expose
    var featured_end_date_time: String? = null
    @SerializedName("preference_required")
    @Expose
    var preference_required: Boolean? = null
    @SerializedName("terms_and_conditions")
    @Expose
    var terms_and_conditions: String? = null
    @SerializedName("featured_start_date_time")
    @Expose
    var featured_start_date_time: String? = null
    @SerializedName("featured_event")
    @Expose
    var featured_event: Boolean? = null
    @SerializedName("faq")
    @Expose
    var faq: String? = null

    @SerializedName("modified_by")
    @Expose
    var modified_by: String? = null
    @SerializedName("interested_count")
    @Expose
    var interested_count: Int? = null
    @SerializedName("view_count")
    @Expose
    var view_count: Int? = null
    @SerializedName("share_count")
    @Expose
    var share_count: Int? = null
    @SerializedName("payment")
    @Expose
    var payment: Boolean? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("priority_order")
    @Expose
    var priority_order: Boolean? = null
    @SerializedName("excerpt")
    @Expose
    var excerpt: String? = null
    @SerializedName("event_register_start_date_time")
    @Expose
    var event_register_start_date_time: String? = null
    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("author_id")
    @Expose
    var author_id: String? = null
    @SerializedName("is_online")
    @Expose
    var is_online: Boolean? = null
    @SerializedName("gtm_id")
    @Expose
    var gtm_id: Int? = null
    @SerializedName("author_name")
    @Expose
    var author_name: String? = null
    @SerializedName("is_private")
    @Expose
    var is_private: Boolean? = null
    @SerializedName("register_count")
    @Expose
    var register_count: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("event_end_date_time")
    @Expose
    var event_end_date_time: String? = null
    @SerializedName("slug")
    @Expose
    var slug: String? = null
    @SerializedName("event_register_end_date_time")
    @Expose
    var event_register_end_date_time: String? = null
    @SerializedName("event_by")
    @Expose
    var event_by: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("post_for")
    @Expose
    var post_for: String? = null
    @SerializedName("publish_date")
    @Expose
    var publish_date: String? = null
    @SerializedName("event_start_date_time")
    @Expose
    var event_start_date_time: String? = null
    @SerializedName("created_by")
    @Expose
    var created_by: Int? = null
    @SerializedName("company_id")
    @Expose
    var company_id: Int? = null
    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("modified_on")
    @Expose
    var modified_on: String? = null
    @SerializedName("payment_note")
    @Expose
    var payment_note: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("city")
    @Expose
    var city: String? = null
    @SerializedName("event_url")
    @Expose
    var event_url: String? = null
    @SerializedName("show_on_search")
    @Expose
    var show_on_search: Boolean? = null
    @SerializedName("image_url")
    @Expose
    var image_url: String? = null
    @SerializedName("thumbnail_url")
    @Expose
    var thumbnail_url: String? = null

}
