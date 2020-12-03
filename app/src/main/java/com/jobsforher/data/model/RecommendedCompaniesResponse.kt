package com.jobsforher.data.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jobsforher.data.model.common_response.JobsForHerAuth
import com.jobsforher.data.model.common_response.JobsForHerPagination
import com.jobsforher.data.model.common_response.JobsForHerPreferences

data class RecommendedCompaniesResponse(

    var response_code: Int? = null,
    var message: String? = null,
    var auth: JobsForHerAuth? = null,
    var pagination: JobsForHerPagination? = null,
    var preferences: JobsForHerPreferences? = null,
    var body: ArrayList<RecommendedCompaniesBody>? = null
)

data class RecommendedCompaniesBody(
    var company_type: String? = null,
    var featured: Boolean? = null,
    var website: String? = null,
    var active_jobs_count: Int? = null,
    var cities: String? = null,
    var follow_count: Int? = null,
    var name: String? = null,
    var logo: String? = null,
    var id: Int? = null,
    var banner_image: String? = null,
    var status: String? = null,
    var follow_status: String? = "Follow",
    var industry: List<String>? = null
)

@BindingAdapter("companyLogo")
fun loadCompanyLogo(view: ImageView, imageUrl: String?) {
    Glide.with(view.context)
        .load(imageUrl).apply(RequestOptions().circleCrop())
        .into(view)
}