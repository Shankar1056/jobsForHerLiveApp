package com.jobsforher.data.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jobsforher.data.model.common_response.JobsForHerAuth
import com.jobsforher.data.model.common_response.JobsForHerPagination
import com.jobsforher.data.model.common_response.JobsForHerPreferences

data class RecommendedJobsResponse(
    var response_code: Int? = null,
    var message: String? = null,
    var auth: JobsForHerAuth? = null,
    var page_no: JobsForHerPagination? = null,
    var body: ArrayList<RecommendedJobsBody>? = null,
    var preferences: JobsForHerPreferences? = null
)

data class RecommendedJobsBody(
    var modified_on: String? = null,
    var application_count: Int? = null,
    var resume_required: Boolean? = null,
    var company_id: Long? = null,
    var job_types: Array<String>? = null,
    var employer_name: String? = null,
    var new_application_count: Int? = null,
    var title: String? = null,
    var max_year: Int? = null,
    var location_id: String? = null,
    var location_name: String? = null,
    var company_logo: String? = null,
    var created_on: String? = null,
    var job_posting_type: String? = null,
    var company_name: String? = null,
    var id: Long? = null,
    var boosted: Boolean? = null,
    var min_year: Int? = null,
    var employer_id: Long? = null,
    var status: String? = null
)
@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    Glide.with(view.context)
        .load(imageUrl).apply(RequestOptions().circleCrop())
        .into(view)
}