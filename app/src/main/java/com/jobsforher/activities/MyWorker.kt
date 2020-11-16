//package com.jobsforher.activities
//
//import android.annotation.SuppressLint
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import android.media.RingtoneManager
//import android.os.Build
//import android.support.v4.app.NotificationCompat
//import android.util.Log
//import com.google.firebase.messaging.RemoteMessage
//import com.jobsforher.R
//
//
//
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import android.media.RingtoneManager
//import android.os.Build
//import android.util.Log
//import androidx.core.app.NotificationCompat
//import androidx.work.OneTimeWorkRequest
//import androidx.work.WorkManager
//import com.google.firebase.messaging.FirebaseMessagingService
//import com.google.firebase.messaging.RemoteMessage
//import com.google.firebase.quickstart.fcm.R
//import android.content.Context
//import android.util.Log
//import androidx.work.ListenableWorker
//
//import androidx.work.Worker
//import androidx.work.WorkerParameters
//
//class MyWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
//
//    override fun doWork(): ListenableWorker.Result {
//        Log.d(TAG, "Performing long running task in scheduled job")
//        // TODO(developer): add long running task here.
//        return ListenableWorker.Result.success()
//    }
//
//    companion object {
//        private val TAG = "MyWorker"
//    }
//}