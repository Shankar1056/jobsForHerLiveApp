package com.jobsforher.helpers

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Base64
import java.io.File
import java.io.FileOutputStream
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.util.*
import kotlin.collections.ArrayList


object HelperMethods
{
    fun md5(s: String): String {
        val MD5 = "MD5"
        try {
            // Create MD5 Hash
            val digest = java.security.MessageDigest
                .getInstance(MD5)
            digest.update(s.toByteArray())
            val messageDigest = digest.digest()

            // Create Hex String
            val hexString = StringBuilder()
            for (aMessageDigest in messageDigest) {
                var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                while (h.length < 2)
                    h = "0$h"
                hexString.append(h)
            }
            return hexString.toString()

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return ""
    }

        fun getFile(mContext: Activity?, documentUri: Uri, fileName: String): File {
            val inputStream = mContext?.contentResolver?.openInputStream(documentUri)
            var file =  File("")
            inputStream.use { input ->
                file =
                    File(mContext?.cacheDir, System.currentTimeMillis().toString() + fileName)
                FileOutputStream(file).use { output ->
                    val buffer =
                        ByteArray(4 * 1024) // or other buffer size
                    var read: Int = -1
                    while (input?.read(buffer).also {
                            if (it != null) {
                                read = it
                            }
                        } != -1) {
                        output.write(buffer, 0, read)
                    }
                    output.flush()
                }
            }
            return file
        }

    fun convertToBase64(attachment: File): String {
        return Base64.encodeToString(attachment.readBytes(), Base64.NO_WRAP)
    }

    fun getPDFPath(context: Context, uri: Uri?): String? {
        val id: String = DocumentsContract.getDocumentId(uri)

       /* val contentUriPrefixesToTry = arrayOf(
            "content://downloads/public_downloads",
            "content://downloads/my_downloads"
        )
        var contentUri: Uri? = null
        for (contentUriPrefix in contentUriPrefixesToTry) {
            contentUri =
                ContentUris.withAppendedId(Uri.parse(contentUriPrefix), java.lang.Long.valueOf(id))

        }*/


        val contentUri: Uri = ContentUris.withAppendedId(
            Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
        )
        val projection = arrayOf<String>(MediaStore.MediaColumns.DISPLAY_NAME)
        val cursor: Cursor? = context.getContentResolver().query(
            contentUri,
            projection,
            null,
            null,
            null
        )
        val column_index: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        return column_index?.let { cursor?.getString(it) }
    }


    fun testUriFile(context: Context, fileName: String, uri: Uri?): String {

        if (fileName != null) {
            return Environment.getExternalStorageState().toString() + "/Download/" + fileName
        }

        var id = DocumentsContract.getDocumentId(uri);
        if (id.startsWith("raw:")) {
            id = id.replaceFirst("raw:", "");
            val file = File(id)
            if (file.exists())
                return id;
        }

        val  contentUri : Uri = ContentUris.withAppendedId(
            Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(
                id
            )
        )
        val projection = arrayOf<String>(MediaStore.MediaColumns.DISPLAY_NAME)
        val cursor: Cursor? = context.getContentResolver().query(
            contentUri,
            projection,
            null,
            null,
            null
        )
        val column_index: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        return column_index?.let { cursor?.getString(it) }.toString()
    }

    fun getFilePath(context: Context, uri: Uri?): String? {
        var cursor: Cursor? = null
        val projection = arrayOf(
            MediaStore.MediaColumns.DISPLAY_NAME
        )
        try {
            cursor = context.contentResolver.query(
                uri!!, projection, null, null,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    fun showAppShareOptions(context: Context) {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "text/plain"
        i.putExtra(Intent.EXTRA_SUBJECT, "Try JobsForHer Jobsearch app to find your next job.")
        i.putExtra(
            Intent.EXTRA_TEXT,
            "Try using the JobsForHer job search app. I find it quite useful in finding job opportunities.\n \n https://play.google.com/store/apps/details?id=com.jobsforher"
        )
        context.startActivity(Intent.createChooser(i, "Share via : "))
    }

    fun isSuccessResponse(responseCode: Int?): Boolean {
        if (responseCode != null) {
            return responseCode in 10000..10999
        }
        return false
    }

    fun openPlayStore(context: Context) {
        val appPackageName = "com.jobsforher"
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (anfe: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }

    }


    fun getNumOfMonth(year: Int, month: Int): Int {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val yearMonthObject: YearMonth = YearMonth.of(1999, 2)
            yearMonthObject.lengthOfMonth()
        } else {
            val calendar: Calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.getActualMaximum(Calendar.DATE)
        }
    }

    @JvmStatic
    fun getDateInMonthDay(date: String): String {
            val format: Date = SimpleDateFormat("yyyy-MM-dd").parse(date)
            return  SimpleDateFormat("dd MMM").format(format).toString()

    }

    fun getMonths(): ArrayList<String> {

        var list = ArrayList<String>()

        list.add("January")
        list.add("February")
        list.add("March")
        list.add("April")
        list.add("May")
        list.add("June")
        list.add("July")
        list.add("Auguest")
        list.add("September")
        list.add("October")
        list.add("November")
        list.add("December")
        return list
    }

    fun getYears(): java.util.ArrayList<String> {
        var list = ArrayList<String>()

        list.add("2019")
        list.add("2020")

        return list
    }

    fun getMonthsInInt(value: String?): Int? {
        when (value) {
            "January" -> return 1
            "February" -> return 2
            "March" -> return 3
            "April" -> return 4
            "May" -> return 5
            "June" -> return 6
            "July" -> return 7
            "Auguest" -> return 8
            "September" -> return 9
            "October" -> return 10
            "November" -> return 11
            "December" -> return 12
            else -> return 1
        }
    }

    fun getMonthsInString(value: Int?): String? {
        when (value) {
            1 -> return "January"
            2 -> return "February"
            3 -> return "March"
            4 -> return "April"
            5 -> return "May"
            6 -> return "June"
            7 -> return "July"
            8 -> return "Auguest"
            9 -> return "September"
            10 -> return "October"
            11 -> return "November"
            12 -> return "December"
            else -> return "January"
        }
    }


}