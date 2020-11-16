package com.jobsforher.data.model

import com.bigappcompany.healthtunnel.data.network.ApiCallback
import com.bigappcompany.healthtunnel.data.repository.ApiServices
import com.bigappcompany.healthtunnel.data.repository.RetroClient
import com.jobsforher.data.network.BackoffCallback
import retrofit2.Response

data class ExpertChatReq(
    var month: Int ? = null,
    var year: Int ?= null
) {
    fun getExpertChat(auth: String, request: ExpertChatReq, apiCallback: ApiCallback) {

        val myCallback = object : BackoffCallback<ExpertChatResponse>(apiCallback) {
            override fun onSuccess(response: Response<ExpertChatResponse>?) {
                if (response != null) {
                    val region = response.body()
                    if (region != null) {
                        apiCallback.onSuccess(region)
                        return
                    }
                }
            }
        }
        RetroClient.getRetrofit()?.create(ApiServices::class.java)?.getExpertChat(auth, request)
            ?.enqueue(myCallback)

    }
}