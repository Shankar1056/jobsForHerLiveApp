package com.jobsforher.helpers

import android.util.Log

object Logger {

    fun d(TAG: String, e: String) {
        Log.d(TAG, e)
    }

    fun e(TAG: String, e: String) {
        Log.e(TAG, e)
    }

}
