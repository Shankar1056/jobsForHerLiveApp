package com.jobsforher.models


import com.jobsforher.network.responsemodels.EventImages

class EventsView {

    var event_category: String? = null
    var registered: Boolean?=null
    var interested: Boolean?= null
    var event_locations: List<EventLocation>?=null
    var event_images: List<EventImages>?=null
    var company_name: String? =null
    var resume_required: Boolean? = null
    var ticket_type: String? = null
    var seats: String? = null
    var event_type: String? = null
    var display_price: Int? = null
    var agenda: String? = null
    var featured_end_date_time: String? = null
    var preference_required: Boolean? = null
    var terms_and_conditions: String? = null
    var featured_start_date_time: String? = null
    var featured_event: Boolean? = null
    var faq: String? = null
    var modified_by: String? = null
    var interested_count: Int? = null
    var view_count: Int? = null
    var share_count: Int? = null
    var payment: Boolean? = null
    var status: String? = null
    var priority_order: Boolean? = null
    var excerpt: String? = null
    var event_register_start_date_time: String? = null
    var created_on: String? = null
    var author_id: String? = null
    var is_online: Boolean? = null
    var gtm_id: Int? = null
    var author_name: String? = null
    var is_private: Boolean? = null
    var register_count: String? = null
    var title: String? = null
    var event_end_date_time: String? = null
    var slug: String? = null
    var event_register_end_date_time: String? = null
    var event_by: String? = null
    var id: Int? = null
    var post_for: String? = null
    var publish_date: String? = null
    var event_start_date_time: String? = null
    var created_by: Int? = null
    var company_id: Int? = null
    var address: String? = null
    var modified_on: String? = null
    var payment_note: String? = null
    var description: String? =null
    var city: String? = null
    var event_url:String?=null
    var show_on_search: Boolean?=null
    var image_url:String? =null
    var price_after_discount:Int? = 0
    var price_before_discount:Int?= 0
    var discount_start_date_time:String? = null
    var discount_end_date_time:String? = null
    var thumbnail_url: String?=null
    var link_companies_name: List<EventCompanies>? = null

    constructor() {}

    constructor(discount_start_date_time:String,discount_end_date_time:String,price_after_discount:Int,price_before_discount:Int, event_category:String,interested:Boolean,registered:Boolean,event_locations:List<EventLocation>,company_name: String,resume_required: Boolean,ticket_type: String,seats: String,event_type: String,display_price: Int,agenda: String
                ,featured_end_date_time: String,preference_required: Boolean,terms_and_conditions: String,featured_start_date_time: String
                ,featured_event: Boolean,faq: String,modified_by: String, interested_count: Int, view_count: Int,share_count: Int, payment: Boolean, status: String,
                priority_order:Boolean, excerpt:String,event_register_start_date_time: String, created_on: String, author_id: String,is_online: Boolean,
                gtm_id: Int, author_name: String,is_private: Boolean, register_count: String, title: String,event_end_date_time: String,
                slug: String, event_register_end_date_time: String,event_by: String, id: Int, post_for: String,publish_date: String,
                event_start_date_time: String, created_by: Int,company_id: Int, address: String, modified_on: String,payment_note: String,
                description: String, city:String, event_url:String, show_on_search:Boolean, image_url:String, thumbnail_url:String,link_companies_name:List<EventCompanies>) {

        this.link_companies_name = link_companies_name
        this.discount_start_date_time = discount_start_date_time
        this.discount_end_date_time = discount_end_date_time
        this.price_after_discount = price_after_discount
        this.price_before_discount = price_before_discount
        this.event_category = event_category
        this.interested = interested
        this.registered = registered
        this.event_locations = event_locations
        this.company_name = company_name
        this.resume_required = resume_required
        this.ticket_type = ticket_type
        this.seats = seats
        this.event_type = event_type
        this.display_price = display_price
        this.agenda = agenda
        this.featured_end_date_time = featured_end_date_time
        this.preference_required = preference_required
        this.terms_and_conditions = terms_and_conditions
        this.featured_start_date_time =featured_start_date_time
        this.featured_event = featured_event
        this.faq = faq

        this.modified_by = modified_by
        this.interested_count = interested_count
        this.view_count= view_count
        this.share_count = share_count
        this.payment = payment
        this.status = status

        this.priority_order = priority_order
        this.excerpt = excerpt
        this.event_register_start_date_time = event_register_start_date_time
        this.created_on = created_on
        this.author_id = author_id
        this.is_online = is_online

        this.gtm_id = gtm_id
        this.author_name = author_name
        this.is_private = is_private
        this.register_count = register_count
        this.title = title
        this.event_end_date_time = event_end_date_time

        this.slug =slug
        this.event_register_end_date_time = event_register_end_date_time
        this.event_by = event_by
        this.id = id
        this.post_for = post_for
        this.publish_date= publish_date

        this.event_start_date_time = event_start_date_time
        this.created_by = created_by
        this.company_id = company_id
        this.address = address
        this.modified_on = modified_on
        this.payment_note = payment_note

        this.description = description
        this.city = city
        this.event_url = event_url
        this.show_on_search = show_on_search
        this.image_url = image_url
        this.thumbnail_url = thumbnail_url
    }
}

class EventLocation{

    var pincode: String? =null
    var discounted_price: Int? = null
    var event_register_start_date_time: String? = null
    var event_type: String? = null
    var event_start_date_time: String? = null
    var amount: Int? = null
    var discount_start_date_time: String? = null
    var event_register_end_date_time: String? = null
    var discount_end_date_time: String? = null
    var discount_active: String? = null
    var address: String? = null
    var event_end_date_time: String? = null
    var id: Int? = null
    var state: String? = null
    var seats: String? = null
    var event_id: Int? = null
    var country: String? = null
    var registration_open: Boolean? = null
    var google_map_url: String? = null
    var city: String? = null
    var external_url:String? =null

    constructor() {}

    constructor(pincode: String,discounted_price: Int,event_register_start_date_time: String,event_type: String,event_start_date_time: String,amount: Int,discount_start_date_time: String
                ,event_register_end_date_time: String,discount_end_date_time: String,discount_active: String,address: String
                ,event_end_date_time: String,id: Int,state: String, seats: String, event_id: Int,country: String, registration_open: Boolean, google_map_url: String,
                city:String,extenal_url:String) {

        this.pincode =pincode
        this.discounted_price = discounted_price
        this.event_register_start_date_time = event_register_start_date_time
        this.event_type = event_type
        this.event_start_date_time =event_start_date_time
        this.amount =amount
        this.discount_start_date_time =discount_start_date_time
        this.event_register_end_date_time =event_register_end_date_time
        this.discount_end_date_time =discount_end_date_time
        this.discount_active =discount_active
        this.address =address
        this.event_end_date_time= event_end_date_time
        this.id =id
        this.state =state
        this.seats =seats
        this.event_id =event_id
        this.country = country
        this.registration_open =registration_open
        this.google_map_url =google_map_url
        this.city =city
        this.external_url = external_url


    }
}

class EventCompanies{


    var company_id: Int? = null
    var company_name:String? =null

    constructor() {}

    constructor(category_name: String,category_id: Int) {

        this.company_name =category_name
        this.company_id = category_id


    }
}