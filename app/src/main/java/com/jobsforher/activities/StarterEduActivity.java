package com.jobsforher.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.plumillonforge.android.chipview.Chip;
import com.plumillonforge.android.chipview.OnChipClickListener;
import com.jobsforher.R;

public class StarterEduActivity extends AppCompatActivity implements OnChipClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter_education);





    }

    @Override
    public void onChipClick(Chip chip) {

    }
}
