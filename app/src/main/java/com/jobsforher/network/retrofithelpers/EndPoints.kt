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

    const val vote = "vote"
    const val NEWSFEED = "news_feed"
    const val NOTIFICATION = "notification_bubble"

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