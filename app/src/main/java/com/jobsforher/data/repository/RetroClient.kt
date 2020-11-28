package com.jobsforher.data.repository

import com.google.gson.GsonBuilder
import com.jobsforher.helpers.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


class RetroClient {


    companion object {
        private val BASE_URL = Constants.BASE_URL
        private var retrofit: Retrofit? = null
        fun getRetrofit(): Retrofit? {

            val gson = GsonBuilder()
                .setLenient()
                .create()

            if (retrofit == null) {
                val builder = OkHttpClient.Builder()
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES)
                val okHttpClient = builder.build()



                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build()

            }

            return retrofit
        }
    }

}