package com.jobsforher.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;



public class AppController extends Application implements Application.ActivityLifecycleCallbacks {
    private static AppController ourInstance = null;
    private Activity activity;

    public static AppController getInstance() {
        return ourInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        this.activity = null;
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public Activity getActivity() {
        return activity;
    }
}
