package com.jobsforher.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Window;

public class MyAlertDialog extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //hide activity title
//        setContentView(R.layout.activity_my_alert_dialog);

        AlertDialog.Builder Builder=new AlertDialog.Builder(this)
                .setMessage("Internet connectivity not available, Try again later!")
                .setTitle("Alert")
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                });
        AlertDialog alertDialog=Builder.create();
        alertDialog.show();
    }
}