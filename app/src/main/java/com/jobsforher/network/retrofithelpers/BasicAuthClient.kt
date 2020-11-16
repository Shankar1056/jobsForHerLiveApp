package com.jobsforher.network.retrofithelpers

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class BasicAuthClient<T> {
    private val client =  OkHttpClient.Builder()
        .addInterceptor(BasicAuthInterceptor("jFhUser", "c@ndid@teRegister@pi"))
        .build()

    val gson = GsonBuilder()
        .setLenient()
        .create();

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.jobsforher.com/") //http://www.workingnaari.in/
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun create(service: Class<T>): T {
        return retrofit.create(service)
    }
}