package com.jobsforher.data.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jobsforher.data.model.common_response.JobsForHerAuth
import com.jobsforher.data.model.common_response.JobsForHerPagination

data class RecommendedEventsResponse(
    var response_code: Int? = null,
    var message: String? = null,
    var auth: JobsForHerAuth? = null,
    var pagination: JobsForHerPagination? = null,
    var body: ArrayList<RecommendedEventsBody>? = null
)

data class RecommendedEventsBody(
    var author_name: String? = null,
    var description: String? = null,
    var excerpt: String? = null,
    var event_url: String? = null,
    var image_url: String? = null,
    var is_private: Boolean? = null,
    var registration_required: Boolean? = null,
    var view_count: Int? = null,
    var resume_required: Boolean? = null,
    var priority_order: Boolean? = null,
    var payment_note: String? = null,
    var event_by: String? = null,
    var company_linked: String? = null,
    var created_on: String? = null,
    var company_id: String? = null,
    var display_name: String? = null,
    var link_partners: String? = null,
    var modified_by: String? = null,
    var register_count: Int? = null,
    var modified_on: String? = null,
    var ticket_type: String? = null,
    var mentor_id: String? = null,
    var terms_and_conditions: String? = null,
    var status: String? = null,
    var slug: String? = null,
    var view_for: String? = null,
    var agenda: String? = null,
    var jobs_linked: String? = null,
    var id: Int? = null,
    var featured_end_date_time: String? = null,
    var link_jobs: String? = null,
    var faq: String? = null,
    var author_id: Int? = null,
    var publish_date: String? = null,
    var preference_required: Boolean? = null,
    var show_on_search: Boolean? = null,
    var gtm_id: String? = null,
    var event_type: String? = null,
    var created_by: Int? = null,
    var featured_event: Boolean? = null,
    var interested_count: Int? = null,
    var title: String? = null,
    var thumbnail_url: String? = null,
    var featured_start_date_time: String? = null,
    var link_mentor: String? = null,
    var link_groups: String? = null,
    var link_companies: String? = null,
    var share_count: Int? = null,
    var price_after_discount: String? = null,
    var price_before_discount: String? = null,
    var event_start_date_time: String? = null,
    var event_end_date_time: String? = null,
    var event_register_start_date_time: String? = null,
    var event_register_end_date_time: String? = null,
    var is_open: Boolean? = null,
    var discount_start_date_time: String? = null,
    var discount_end_date_time: String? = null,
    var events_category: String? = null,
    var events_images: ArrayList<EventsImages>? = null,
    var events_locations: ArrayList<EventsLocation>? = null,
    var event_category_list: ArrayList<EventsCategoryList>? = null
)

data class EventsCategoryList(
    var category_name: String? = null,
    var category_id: Int? = null
)

data class EventsLocation(
    var google_map_url: String? = null,
    var country: String? = null,
    var discount_active: String? = null,
    var pincode: Int? = null,
    var amount: String? = null,
    var address: String? = null,
    var event_register_start_date_time: String? = null,
    var city: String? = null,
    var event_register_end_date_time: String? = null,
    var seats: String? = null,
    var event_start_date_time: String? = null,
    var registration_required: Boolean? = null,
    var discounted_price: String? = null,
    var external_url: String? = null,
    var event_type: String? = null,
    var event_id: Int? = null,
    var state: String? = null,
    var id: Int? = null,
    var discount_start_date_time: Int? = null,
    var registration_open: Boolean? = null,
    var discounted_text: String? = null,
    var event_end_date_time: String? = null,
    var discount_end_date_time: String? = null
)

class EventsImages {

}

@BindingAdapter("image_url")
fun loadEventImage(view: ImageView, imageUrl: String?) {
    Glide.with(view.context)
        .load(imageUrl).apply(RequestOptions().circleCrop())
        .into(view)
}
