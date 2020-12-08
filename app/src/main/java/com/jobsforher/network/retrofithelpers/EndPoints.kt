package com.jobsforher.network.retrofithelpers

import com.jobsforher.models.CityView
import java.util.ArrayList

object EndPoints {

    lateinit var CITY_LIST: ArrayList<CityView>
    const val user_lookup = "user_lookup"
    const val password_reset = "forgotpassword"
    const val user_signup = "user_signup"
    const val user_signin = "user_signin"
    const val device_Id_registration = "registration_device_id"
    const val get_social_token = "get_social_token"
    const val social_login = "social_login"

    const val group = "groups"
    const val categories = "categories"

    const val mygroup = group + "/my_groups"
    const val allgroups = "groups"
    const val EXPERT_CHAT_DETAILS = "expert_chat_details"
    const val RECOMMENDED_JOBS = "es_recommended_jobs"
    const val RECOMMENDED_COMPANIES = "es_recommended_companies"
    const val RECOMMENDED_GROUPS = "es_recommended_groups"

    const val vote = "vote"
    const val NEWSFEED = "news_feed"
    const val NOTIFICATION = "notification_bubble"
    const val RESUMEUPLOAD = "candidate/resumes"
    const val CITIES = "cities"
    const val JOINGROUP = "groups/join"
    const val GET_FUNCTIONAL_INDUSTRIES = "candidate/mobility/functional-industry"
    const val GET_FUNCTIONAL_AREAS = "candidate/mobility/functional-area"
    const val GET_PREFERENCE = "candidate/preference"
    const val FOLLOWCOMPANY = "followers"
    const val RECOMMENDED_EVENTS = "es_recommended_events"

    const val CLIENT_ID = "NQtAuLKzeYVOFe7b3kmqBSNP"

    var ACCESS_TOKEN = ""

    var profileUrl = ""

    var PROFILE_URL = ""

    var USERNAME = ""

    var PROFILE_ICON=""

    var PHONE_NO=""

    var USERID=""

    var MY_ROLES=""



}