package com.jobsforher.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.jobsforher.models.Tag;
import com.plumillonforge.android.chipview.Chip;
import com.plumillonforge.android.chipview.ChipView;
import com.plumillonforge.android.chipview.ChipViewAdapter;
import com.plumillonforge.android.chipview.OnChipClickListener;
import studio.carbonylgroup.textfieldboxes.SimpleTextChangedWatcher;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;
import com.jobsforher.R;
import com.jobsforher.adapters.MainChipViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class AddEducationStatusActivity extends AppCompatActivity implements OnChipClickListener {

    private List<Chip> mTagList1;
    private List<Chip> mTagList2;


    private ChipView mTextChipLayout;

    private TextFieldBoxes textFieldBoxes;

    private EditText etSkill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_education);

        ImageView addChip = findViewById(R.id.imageAddSkill);

        textFieldBoxes = findViewById(R.id.text_field_boxes);


        etSkill = findViewById(R.id.etSkill);

        mTagList1 = new ArrayList<>();
        mTagList1.add(new Tag("Lorem"));
        mTagList1.add(new Tag("Ipsum dolor"));
        mTagList1.add(new Tag("Sit amet"));
        mTagList1.add(new Tag("Consectetur"));
        mTagList1.add(new Tag("adipiscing elit"));

        mTagList2 = new ArrayList<>();
        mTagList2.add(new Tag("Lorem", 1));
        mTagList2.add(new Tag("Ipsum dolor", 2));
        mTagList2.add(new Tag("Sit amet", 3));
        mTagList2.add(new Tag("Consectetur", 4));
        mTagList2.add(new Tag("adipiscing elit", 5));

        // Adapter
        ChipViewAdapter adapterLayout = new MainChipViewAdapter(this);

        // Custom layout and background colors
        mTextChipLayout = findViewById(R.id.text_chip_layout);
        mTextChipLayout.setAdapter(adapterLayout);
        mTextChipLayout.setChipLayoutRes(R.layout.chip_close);
        mTextChipLayout.setChipBackgroundColor(getResources().getColor(R.color.green));
        mTextChipLayout.setChipBackgroundColorSelected(getResources().getColor(R.color.green));
        mTextChipLayout.setChipList(mTagList1);
        mTextChipLayout.setOnChipClickListener(this);

        addChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mTagList1.add(new Tag(etSkill.getText().toString()));
                mTextChipLayout.setChipList(mTagList1);
                etSkill.setText("                ");
            }
        });


        textFieldBoxes.setSimpleTextChangeWatcher(new SimpleTextChangedWatcher() {
            @Override
            public void onTextChanged(String theNewText, boolean isError) {

                if(isError){

                    textFieldBoxes.setError("Only 150 characters are allowed !",true);

                }else{}
            }
        });


    }

    @Override
    public void onChipClick(Chip chip) {

        mTextChipLayout.remove(chip);
    }
}
