package com.jobsforher.activities;

import android.app.Application;
//import org.acra.ACRA;
//import org.acra.ReportField;
//import org.acra.ReportingInteractionMode;
//import org.acra.annotation.ReportsCrashes;
//import org.acra.sender.HttpSender;

//@ReportsCrashes(mailTo = "sneha@jobsforher.com", customReportContent = {ReportField.APP_VERSION_CODE,
//        ReportField.APP_VERSION_NAME,
//        ReportField.ANDROID_VERSION,
//        ReportField.PHONE_MODEL,
//        ReportField.CUSTOM_DATA,
//        ReportField.STACK_TRACE,
//        ReportField.LOGCAT},
//        mode = ReportingInteractionMode.TOAST, resToastText = R.string.crash)
//@ReportsCrashes(mailTo = "snehapm13@gmail.com",
//        mode = ReportingInteractionMode.TOAST,
//        customReportContent = {ReportField.ANDROID_VERSION},
//        resToastText = R.string.crash)

public class MyApplication extends Application {

    @Override
    public void onCreate(){

        super.onCreate();
        //ACRA.init(this);


    }
}