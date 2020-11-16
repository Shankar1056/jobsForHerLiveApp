package com.jobsforher.network.retrofithelpers

import com.jobsforher.network.responsemodels.AccountSettingsDetails
import retrofit2.Call
import retrofit2.http.*
import java.util.HashMap


interface DemoRemoteService {

    @POST("/mobileapi")
    fun requestpartyOTP(@Body signInCredentials: HashMap<String, String>):Call<AccountSettingsDetails>

}