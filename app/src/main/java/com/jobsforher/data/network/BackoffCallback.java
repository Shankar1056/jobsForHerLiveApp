package com.jobsforher.data.network;

import android.util.Log;

import androidx.annotation.NonNull;

import com.bigappcompany.healthtunnel.data.network.ApiCallback;
import com.google.gson.JsonObject;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BackoffCallback<T> implements Callback<T> {
    private static final String TAG = "CallbackWithRetry";
    private static final int RETRY_COUNT = 3;

    private static final double RETRY_DELAY = 300;
    private int retryCount = 0;
    private ApiCallback callback;

    protected BackoffCallback(ApiCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        if (response.isSuccessful())
            onSuccess(response);
        else {
            if (callback != null) {
                Log.d("responses", "chweck" + response.code());
                switch (response.code()) {
                    case HttpURLConnection.HTTP_UNAUTHORIZED:
                        callback.onUnAuthentic();
                        break;
                    case HttpURLConnection.HTTP_NOT_FOUND:
                        callback.onError("Not Found");
                        break;
                    case HttpURLConnection.HTTP_INTERNAL_ERROR:
                        JsonObject myObject = (JsonObject) response.body();
                        try {
                            if (myObject != null) {
                                if (myObject.has("message")) {
                                    callback.onError(myObject.get("message").toString());
                                } else {
                                    callback.onError("Server ic_error");
                                }
                            } else {
                                callback.onError("Server ic_error");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.onError("Server ic_error");
                        }
                        break;
                    default:
                        callback.onNetworkError();
                        break;
                }
            }
        }
    }

    @Override
    public void onFailure(@NonNull final Call<T> call, @NonNull Throwable t) {
        callback.onHandledError();
        //           callback.onNetworkError();
    }

    private void retry(Call<T> call) {
        call.clone().enqueue(this);
    }

    public abstract void onSuccess(Response<T> response);
}
