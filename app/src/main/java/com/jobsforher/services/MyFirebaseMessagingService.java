/*
 * Copyright 2016-2017 Tom Misawa, riversun.org@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 *  INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package com.jobsforher.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.jobsforher.R;
import com.jobsforher.activities.Logg;
import com.jobsforher.activities.SplashActivity;

import java.util.Map;

/**
 * Service called when firebase cloud message received
 *
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static int count = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Logg.d();
        Log.d("TAGG","Message received ");

        //Log.d("TAGG", remoteMessage.getMessageType());
        final Map<String, String> data = remoteMessage.getData();
        final String val1 = data.get("entity_id");        //jobs, groups, events, posts
        final String val2 = data.get("entity_type");
        final String title = data.get("title");
        final String body = data.get("body");


        Log.d("TAGG","Message received val1=" +val1 +"val2= "+val2 + "body="+body+"title="+title);
        sendNotification(title,body);

        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(MyFirebaseMessagingService.this.getApplicationContext(),
                        "Message received " + "val1=" + val1 + " val2=" + val2, Toast.LENGTH_SHORT).show();
//                Log.d("TAGG","Message received val1=" + val1 + " val2=" + val2 + "body="+body+"title="+title);
//                sendNotification(title,body);
            }
        });


    }
    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
//you can use your launcher Activity insted of SplashActivity, But if the Activity you used here is not launcher Activty than its not work when App is in background.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//Add Any key-value to pass extras to intent
        intent.putExtra("pushnotification", "yes");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //For Android Version Orio and greater than orio.

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Log.d("TAGG", "INSIDE O");

            int importance = NotificationManager.IMPORTANCE_NONE;
            NotificationChannel mChannel = new NotificationChannel(" ", " ",importance);
            mChannel.setDescription(messageBody);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotifyManager.createNotificationChannel(mChannel);
        }
        else{
            //For Android Version lower than oreo.
            NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this);
            mBuilder.setContentTitle(title)
                    .setContentText(messageBody)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setColor(Color.parseColor("#FFD600"))
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            mNotifyManager.notify(count, mBuilder.build());
            count++;
        }

    }

    @Override
    public void onNewToken(String registrationToken) {


        Log.d("Firebase" , registrationToken);

//        val intent = Intent(this, FcmTokenRegistrationService::class.java)
//        startService(intent)
    }
}
