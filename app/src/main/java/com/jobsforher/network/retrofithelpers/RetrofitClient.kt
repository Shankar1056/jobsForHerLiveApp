package com.jobsforher.network.retrofithelpers

import android.content.Context
import com.google.gson.GsonBuilder
import com.jobsforher.helpers.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient(context: Context) {

    companion object {

        private var retrofit: Retrofit? = null

        private val context: Context? = null

        val client: Retrofit?
            get() {

                if (retrofit == null) {

                    val gson = GsonBuilder().setLenient().create()

                    val client = OkHttpClient.Builder()
                        .connectTimeout(100, TimeUnit.SECONDS)
                        .readTimeout(100, TimeUnit.SECONDS)
                        .build()

                    retrofit = Retrofit.Builder()
                        .baseUrl(Constants.BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
                }

                return retrofit
            }

        val clientWithLongTimeout: Retrofit?
            get() {

                if (retrofit == null) {

                    val gson = GsonBuilder().setLenient().create()

                    val client = OkHttpClient.Builder()
                        .connectTimeout(300, TimeUnit.SECONDS)
                        .readTimeout(300, TimeUnit.SECONDS)
                        .build()

                    retrofit = Retrofit.Builder()
                        .baseUrl(Constants.BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
                }

                return retrofit
            }
    }



}
