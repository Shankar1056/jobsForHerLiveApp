package com.jobsforher.util

import android.content.Context

object Preference {
    fun setPreferences(key: String?, value: String?) {
        val editor = AppController.getInstance().getSharedPreferences(
            "WED_APP", Context.MODE_PRIVATE
        ).edit()
        editor.putString(key, value)
        editor.commit()
    }

    fun getPreferences(key: String?): String? {
        val prefs = AppController.getInstance().getSharedPreferences(
            "WED_APP",
            Context.MODE_PRIVATE
        )
        return prefs.getString(key, "")
    }

    fun setBooleanPreference(key: String?, value: Boolean?) {
        val editor = AppController.getInstance().getSharedPreferences(
            "WED_APP", Context.MODE_PRIVATE
        ).edit()
        editor.putBoolean(key, value!!)
        editor.commit()
    }

    fun getBooleanPreferences(key: String?): Boolean {
        val prefs = AppController.getInstance().getSharedPreferences(
            "WED_APP",
            Context.MODE_PRIVATE
        )
        return prefs.getBoolean(key, false)
    }

    fun setIntPreference(key: String?, value: Int?) {
        val editor = AppController.getInstance().getSharedPreferences(
            "WED_APP", Context.MODE_PRIVATE
        ).edit()
        editor.putInt(key, value!!)
        editor.commit()
    }

    fun getIntPreferences(key: String?): Int {
        val prefs = AppController.getInstance().getSharedPreferences(
            "WED_APP",
            Context.MODE_PRIVATE
        )
        return prefs.getInt(key, 0)
    }

    fun clearPreference(){
        AppController.getInstance().getSharedPreferences(
            "WED_APP",
            Context.MODE_PRIVATE
        ).edit().clear().commit()
    }
}