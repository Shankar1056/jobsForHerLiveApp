package com.jobsforher.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jobsforher.R;
import com.jobsforher.models.CityView;
import com.jobsforher.models.FunctionalAreaView;
import com.jobsforher.network.retrofithelpers.EndPoints;
import com.jobsforher.network.retrofithelpers.RetrofitClient;
import com.jobsforher.network.retrofithelpers.RetrofitInterface;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kotlin.jvm.internal.Intrinsics;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Footer extends AppCompatActivity implements View.OnClickListener {

    protected LinearLayout btnHome,btnJobs,btnEvents,btnGroups;
    protected ImageView homeImage, jobsImage, eventsImage, groupsImage;
    protected TextView homeText, jobsText, eventsText, groupsText;
    private Activity mActivity;
    ArrayList<CityView> listOfCitiesnew = new ArrayList<>();
    ArrayList<FunctionalAreaView> listOfJobFAreanew = new ArrayList();
    ArrayList<FunctionalAreaView> listOfJobIndustrynew = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
    }

    protected void mappingWidgets() {

        btnHome = (LinearLayout) findViewById(R.id.newsfeed_home);
        btnJobs = (LinearLayout) findViewById(R.id.newsfeed_jobs);
        btnEvents = (LinearLayout) findViewById(R.id.newsfeed_events);
        btnGroups = (LinearLayout) findViewById(R.id.newsfeed_groups);

        homeImage = (ImageView) findViewById(R.id.home_image);
        homeText= (TextView) findViewById(R.id.home_text);

        jobsImage = (ImageView) findViewById(R.id.jobs_image);
        jobsText= (TextView) findViewById(R.id.jobs_text);

        eventsImage = (ImageView) findViewById(R.id.events_image);
        eventsText= (TextView) findViewById(R.id.events_text);

        groupsImage = (ImageView) findViewById(R.id.groups_image);
        groupsText= (TextView) findViewById(R.id.groups_text);

        btnHome.setOnClickListener(this);
        btnJobs.setOnClickListener(this);
        btnEvents.setOnClickListener(this);
        btnGroups.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == null)
            throw new NullPointerException(
                    "You are refering null object. "
                            + "Please check weather you had called super class method mappingWidgets() or not");
        if (v == btnHome) {
            Intent intent = new Intent(this, NewsFeed.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("isLoggedIn",true);
            finish();
            startActivity(intent);

        } else if (v == btnEvents) {
            Intent intent = new Intent(this, ZActivityEvents.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("isLoggedIn",true);
            startActivity(intent);

        }else if(v == btnJobs) {
            Intent intent = new Intent(this, ZActivityJobs.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("isLoggedIn",true);
            startActivity(intent);

        }
        else if(v == btnGroups) {
            Intent intent = new Intent(this, ZActivityGroups.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("isLoggedIn",true);
            startActivity(intent);

        }
    }

    protected void handleBackgrounds(View v) {
        if (v == btnHome) {
            //  Toast.makeText(this,"Home1",Toast.LENGTH_LONG).show();
            Log.d("TAGG","HOME1");
            homeText.setTextColor(ContextCompat.getColor(this, R.color.green));
            homeImage.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green)));

        } else if (v == btnJobs) {
            //   Toast.makeText(this,"Jobs",Toast.LENGTH_LONG).show();
            jobsText.setTextColor(ContextCompat.getColor(this, R.color.green));
            jobsImage.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green)));

        } else if (v == btnEvents) {
            //  Toast.makeText(this,"Events",Toast.LENGTH_LONG).show();
            eventsText.setTextColor(ContextCompat.getColor(this, R.color.green));
            eventsImage.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green)));
        }
        else if (v == btnGroups) {
            //  Toast.makeText(this,"Groups",Toast.LENGTH_LONG).show();
            groupsText.setTextColor(ContextCompat.getColor(this, R.color.green));
            groupsImage.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green)));
        }
    }




    void loadData(){

        listOfCitiesnew.clear();
        Retrofit var10001 = RetrofitClient.Companion.getClient();
        if (var10001 == null) {
            Intrinsics.throwNpe();
        }

        RetrofitInterface retrofitInterface = (RetrofitInterface)var10001.create(RetrofitInterface.class);

        Call call = retrofitInterface.getCities(EndPoints.CLIENT_ID, "Bearer " + EndPoints.INSTANCE.getACCESS_TOKEN());
        call.enqueue((Callback)(new Callback() {
            public void onResponse(@NotNull Call call, @NotNull Response response) {


                Gson gson = new GsonBuilder().serializeNulls().create();
                String str_response = gson.toJson(response);
                try {
                    JSONObject jsonObject = new JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1));
                    if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                        JSONObject jsonObject1 = jsonObject.optJSONObject("body");
                        int responseCode = jsonObject1.optInt("response_code");
                        String message = jsonObject1.optString("message");
                        JSONArray jsonarray = jsonObject1.optJSONArray("body");

                        if (response.isSuccessful()) {
                            for (int i = 0; i < response.body().toString().length(); i++) {
                                JSONObject json_objectdetail = jsonarray.optJSONObject(i);
                                CityView model = new CityView();
                                model.setId(json_objectdetail.optInt("id"));
                                model.setName(json_objectdetail.optString("label"));
                                listOfCitiesnew.add(model);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),"Invalid",Toast.LENGTH_LONG).show();
                        }
                        Log.d("TAG", "JAVA SiZE" + listOfCitiesnew.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                EndPoints.INSTANCE.setCITY_LIST(listOfCitiesnew);
            }

            public void onFailure(@NotNull Call call, @NotNull Throwable t) {
                Log.d("TAG", "FAILED : " + t);
            }
        }));
}

    void loadFunctionalData(){


        listOfJobFAreanew.clear();
        Retrofit var10001 = RetrofitClient.Companion.getClient();
        if (var10001 == null) {
            Intrinsics.throwNpe();
        }

        RetrofitInterface retrofitInterface = (RetrofitInterface)var10001.create(RetrofitInterface.class);

        Call call = retrofitInterface.getFunctionalArea(EndPoints.CLIENT_ID, "Bearer " + EndPoints.INSTANCE.getACCESS_TOKEN());
        call.enqueue((Callback)(new Callback() {
            public void onResponse(@NotNull Call call, @NotNull Response response) {


                Gson gson = new GsonBuilder().serializeNulls().create();
                String str_response = gson.toJson(response);
                try {
                    JSONObject jsonObject = new JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1));
                    if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                        JSONObject jsonObject1 = jsonObject.optJSONObject("body");
                        int responseCode = jsonObject1.optInt("response_code");
                        String message = jsonObject1.optString("message");
                        JSONArray jsonarray = jsonObject1.optJSONArray("body");

                        if (response.isSuccessful()) {
                            for (int i = 0; i < response.body().toString().length(); i++) {
                                JSONObject json_objectdetail = jsonarray.optJSONObject(i);
                                FunctionalAreaView model = new FunctionalAreaView();
                                //model.setId(json_objectdetail.getInt("id"));
                                model.setName(json_objectdetail.optString("name"));
                                listOfJobFAreanew.add(model);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),"Invalid",Toast.LENGTH_LONG).show();
                        }
                        Log.d("TAG", "JAVA SiZE" + listOfCitiesnew.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(@NotNull Call call, @NotNull Throwable t) {
                Log.d("TAG", "FAILED : " + t);
            }
        }));
    }

    void loadIndustryData(){


        listOfJobIndustrynew.clear();
        Retrofit var10001 = RetrofitClient.Companion.getClient();
        if (var10001 == null) {
            Intrinsics.throwNpe();
        }

        RetrofitInterface retrofitInterface = (RetrofitInterface)var10001.create(RetrofitInterface.class);

        Call call = retrofitInterface.getIndustry(EndPoints.CLIENT_ID, "Bearer " + EndPoints.INSTANCE.getACCESS_TOKEN());
        call.enqueue((Callback)(new Callback() {
            public void onResponse(@NotNull Call call, @NotNull Response response) {


                Gson gson = new GsonBuilder().serializeNulls().create();
                String str_response = gson.toJson(response);
                try {
                    JSONObject jsonObject = new JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1));
                    if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                        JSONObject jsonObject1 = jsonObject.optJSONObject("body");
                        int responseCode = jsonObject1.optInt("response_code");
                        String message = jsonObject1.optString("message");
                        JSONArray jsonarray = jsonObject1.optJSONArray("body");

                        if (response.isSuccessful()) {
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject json_objectdetail = jsonarray.optJSONObject(i);
                                FunctionalAreaView model = new FunctionalAreaView();
                                //model.setId(json_objectdetail.getInt("id"));
                                try {
                                    model.setName(json_objectdetail.optString("name"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                listOfJobIndustrynew.add(model);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),"Invalid",Toast.LENGTH_LONG).show();
                        }
                        Log.d("TAG", "JAVA SiZE" + listOfCitiesnew.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(@NotNull Call call, @NotNull Throwable t) {
                Log.d("TAG", "FAILED : " + t);
            }
        }));
    }
}
