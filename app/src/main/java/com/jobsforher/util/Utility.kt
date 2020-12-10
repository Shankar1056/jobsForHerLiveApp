package com.jobsforher.util

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import com.jobsforher.activities.SplashActivity
import com.jobsforher.data.model.EventsLocation
import com.jobsforher.helpers.Constants
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import java.util.regex.Pattern

class Utility {

    companion object {

        @Suppress("DEPRECATION")
        fun isInternetConnected(context: Context): Boolean {
            var result = false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val actNw =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                result = when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                connectivityManager.run {
                    connectivityManager.activeNetworkInfo?.run {
                        result = when (type) {
                            ConnectivityManager.TYPE_WIFI -> true
                            ConnectivityManager.TYPE_MOBILE -> true
                            ConnectivityManager.TYPE_ETHERNET -> true
                            else -> false
                        }

                    }
                }
            }

            return result
        }

        fun isSuccessCode(responseCode: Int?): Boolean {
            if (responseCode != null) {
                if (responseCode in 10000..10999) {
                    return true
                } else if (responseCode in 11000..11999) {
                    return false
                }
            }
            return false
        }

        fun showToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        fun doLogoutPopup(context: Context, message: String) {
            val builder = AlertDialog.Builder(context)
            builder.setMessage(message)
            builder.setPositiveButton("YES") { dialog, which ->
                /*val sharedPref: SharedPreferences = context.getSharedPreferences(
                    "mysettings",
                    Context.MODE_PRIVATE
                )
                sharedPref.edit().clear().commit();*/
                Preference.clearPreference()
                val intent = Intent(context, SplashActivity::class.java)
                context.startActivity(intent)
            }
            builder.setNegativeButton("No") { dialog, which ->
                dialog.cancel()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        fun sessionExpiredPopup(context: Context, message: String) {
            val builder = AlertDialog.Builder(context)
            builder.setMessage(message)
            builder.setCancelable(false)
            builder.setPositiveButton("OK") { dialog, which ->
                val sharedPref: SharedPreferences = context.getSharedPreferences(
                    "mysettings",
                    Context.MODE_PRIVATE
                )
                sharedPref.edit().clear().commit();
                val intent = Intent(context, SplashActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        @JvmStatic
        fun getVideoDocumentUrl(data: String, docType: String): String {
            if (docType == "document") {
                return "http://docs.google.com/gview?embedded=true&url=$data"
            }
            if (docType == "video") {
                var text = data.replace("watch?", "embed/")
                var t = extractYTId(text)
                return if (t!!.contains("v=")) {
                    val splitText = t!!.split("v=")
                    val v =
                        splitText[1].split("&".toRegex()).dropLastWhile({ it.isEmpty() })
                            .toTypedArray()
                    "https://www.youtube.com/embed/" + v[0]
                } else {
                    "https://www.youtube.com/embed/$t"
                }
            }
            return data
        }

        private fun extractYTId(ytUrl: String): String? {
            var vId: String? = null
            val pattern = Pattern.compile(
                "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
                Pattern.CASE_INSENSITIVE
            )
            val matcher = pattern.matcher(ytUrl)
            if (matcher.matches()) {
                vId = matcher.group(1)
            }
            return vId
        }

        @JvmStatic
        fun getEventCity(eventLocations: ArrayList<EventsLocation>): String {
            var city = ""
            for (data in eventLocations) {
                city = if (city == "") {
                    data.city.toString()
                } else {
                    city + ", " + data.city.toString()
                }
            }

            return city
        }

        @JvmStatic
        fun getFromAndToDate(date: String): String {

            var format = SimpleDateFormat("yyyy-mm-dd hh:mm:ss")
            val newDate: Date = format.parse(date)

            format = SimpleDateFormat("MMM dd,yyyy")
            return format.format(newDate)

        }

        @JvmStatic
        fun getFromAndToTime(time: String): String {

            var format = SimpleDateFormat("yyyy-mm-dd hh:mm:ss")
            val newTime: Date = format.parse(time)

            format = SimpleDateFormat("hh:mm aa")
            return format.format(newTime)

        }

        @JvmStatic
        fun getDeviceID(context: Context): String {
            return Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ANDROID_ID
            )

        }

        @JvmStatic
        fun isValidMobileNumber(number: String): Boolean {
            if (!Pattern.compile(Constants.MOBILE_VALIDATION).matcher(number).matches()) {
                return false
            }
            return true
        }

        fun getCurrentDateTime(): String {
            return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                LocalDateTime.now().toString()
            } else {
                val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                sdf.format(Date())
            }
        }
    }


}