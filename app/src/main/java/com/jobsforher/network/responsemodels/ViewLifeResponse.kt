package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ViewLifeResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: List<LifeBody>? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}
class LifeBody {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("location")
    @Expose
    var location: String? = null
    @SerializedName("career_break")
    @Expose
    var career_break: Boolean? = null
    @SerializedName("image_url")
    @Expose
    var duration: String? = null
    @SerializedName("duration")
    @Expose
    var image_url: String? = null
    @SerializedName("ongoing")
    @Expose
    var ongoing: String? = null
    @SerializedName("location_id")
    @Expose
    var location_id: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("skills")
    @Expose
    var skills: List<String>? = null
    @SerializedName("skills_id")
    @Expose
    var skills_id: List<String>? = null
    @SerializedName("start_date")
    @Expose
    var start_date: String? = null
    @SerializedName("end_date")
    @Expose
    var end_date: String? = null
    @SerializedName("user_id")
    @Expose
    var user_id: String? = null
    @SerializedName("caption")
    @Expose
    var caption: String? = null
    @SerializedName("life_experience")
    @Expose
    var life_experience: List<String>? = null
    @SerializedName("life_experience_id")
    @Expose
    var life_experience_id: List<String>? = null

}
