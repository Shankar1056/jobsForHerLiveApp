package com.jobsforher.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class NetworkChangeReceiver extends BroadcastReceiver {

    AlertDialog.Builder builder;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        if(checkInternet(context))
        {
            Toast.makeText(context, "Network Available Welcome back!!",Toast.LENGTH_LONG).show();

        }
        else{
            Toast.makeText(context, "Network not available",Toast.LENGTH_LONG).show();
            Intent i = new Intent(context, MyAlertDialog.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

    }


    boolean checkInternet(Context context) {
        if(AppStatus.getInstance(context).isOnline()){
            return true;
        } else {
            return false;
        }
    }

}
