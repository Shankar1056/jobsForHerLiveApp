package com.jobsforher.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.jobsforher.R;

public class PaymentStatusActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.payment_status_layout);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
//            Boolean status = extras.getBoolean("status");
//            String transaction_id = extras.getString("transaction_id");
//            int id = extras.getInt("id");
//            Boolean isFromOrder = extras.getBoolean("isFromOrder");

            String payid = extras.getString("paymentId");
            String res = extras.getString("result");

            Log.d("TAGG",res+" , "+ payid);

//            Log.d("TAGG",status+" , "+ transaction_id+" , "+id+" , "+isFromOrder);
        }

        Button b = (Button)findViewById(R.id.click);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
