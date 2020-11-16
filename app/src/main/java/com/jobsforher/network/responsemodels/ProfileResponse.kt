package com.jobsforher.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProfileResponse {

    @SerializedName("response_code")
    @Expose
    var responseCode: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: ProfileBody? = null
    @SerializedName("auth")
    @Expose
    var auth: Auth? = null

}
class ProfileBody {

    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("caption")
    @Expose
    var caption: String? = null
    @SerializedName("organization")
    @Expose
    var organization: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("location")
    @Expose
    var location: String? = null
    @SerializedName("profile_summary")
    @Expose
    var profile_summary: String? = null
    @SerializedName("stage_type")
    @Expose
    var stage_type: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("profile_url")
    @Expose
    var profile_url: String? = null
    @SerializedName("my_profile")
    @Expose
    var my_profile: String? = null
    @SerializedName("already_following")
    @Expose
    var already_following: String? = null
    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("profile_percentage")
    @Expose
    var profile_percentage: String? = null
    @SerializedName("profile_image")
    @Expose
    var profile_image: String? = null
    @SerializedName("followers")
    @Expose
    var followers: String? = null
    @SerializedName("following")
    @Expose
    var following: String? = null
    @SerializedName("updates")
    @Expose
    var updates: List<Updates>? = null
    @SerializedName("timeline_list")
    @Expose
    var timeline_list: List<Timelist>? = null

    @SerializedName("working_life_experience_skills")
    @Expose
    var working_life_experience_skills: SkillsLists? = null





}

class Updates{
    @SerializedName("created_on")
    @Expose
    var created_on: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("user_v3_id")
    @Expose
    var user_v3_id: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("image_url")
    @Expose
    var image_url: String? = null
}

class Timelist{

    @SerializedName("company_name")
    @Expose
    var company_name: String? = null
    @SerializedName("designation")
    @Expose
    var designation: String? = null
    @SerializedName("current_company")
    @Expose
    var current_company: String? = null
    @SerializedName("organization")
    @Expose
    var organization: String? = null
    @SerializedName("expires")
    @Expose
    var expires: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("expires_on")
    @Expose
    var expires_on: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("lifeexperience")
    @Expose
    var lifeexperience: ArrayList<String>? = null

    @SerializedName("start_date")
    @Expose
    var start_date: String? = null
    @SerializedName("skills")
    @Expose
    var skills: ArrayList<String>? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("location")
    @Expose
    var location: String? = null
    @SerializedName("duration")
    @Expose
    var duration: String? = null
    @SerializedName("end_date")
    @Expose
    var end_date: String? = null
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("life_experience")
    @Expose
    var life_experience: ArrayList<String>? = null
    @SerializedName("image_url")
    @Expose
    var image_url: String? = null
    @SerializedName("caption")
    @Expose
    var caption: String? = null
    @SerializedName("degree")
    @Expose
    var degree: String? = null
    @SerializedName("college")
    @Expose
    var college: String? = null
    @SerializedName("ongoing")
    @Expose
    var ongoing: Boolean? = null

}

class SkillsLists{
    @SerializedName("work_skills")
    @Expose
    var work_skills: ArrayList<String>? = null
    @SerializedName("life_experience_skills")
    @Expose
    var life_experience_skills: ArrayList<String>? = null
    @SerializedName("education_skills")
    @Expose
    var education_skills: ArrayList<String>? = null
}


