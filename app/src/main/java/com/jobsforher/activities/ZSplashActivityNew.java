package com.jobsforher.activities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.jobsforher.R;
import women.jobs.jobsforher.activities.BaseActivity;

public class ZSplashActivityNew extends BaseActivity {

    PreferenceManager preferenceManager;
    LinearLayout Layout_bars;
    TextView[] bottomBars;
    int[] screens;
    Button login, signup;
    ViewPager vp;
    MyViewPagerAdapter myvpAdapter;
    private int dotsCount=5;    //No of tabs or images
    private ImageView[] dots;
    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zsplasha_activity1);
        Intent intent = getIntent();
        String fcm = intent.getStringExtra("fcm");
        vp = (ViewPager) findViewById(R.id.view_pager);
//        Layout_bars = (LinearLayout) findViewById(R.id.layoutBars);
        login = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);
        screens = new int[]{
                R.layout.intro_screen_one,
                R.layout.intro_screen_two,
                R.layout.intro_screen_three,
                R.layout.intro_screen_four,
                R.layout.intro_screen_five
        };

//        setupPagerIndidcatorDots();
        myvpAdapter = new MyViewPagerAdapter();
        vp.setAdapter(myvpAdapter);
        drawPageSelectionIndicators(0);
        preferenceManager = new PreferenceManager(this);
        vp.addOnPageChangeListener(viewPagerPageChangeListener);
        if (!preferenceManager.FirstLaunch()) {
            launchMain();
            finish();
        }
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String crashString = null;
//                crashString.length();
                Intent intent = new Intent(ZSplashActivityNew.this, SignupActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                throw new RuntimeException("Test Crash set 2"); // Force a crash
                Intent intent = new Intent(ZSplashActivityNew.this, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
//        ColoredBars(0);


    }

    private void drawPageSelectionIndicators(int mPosition){
        if(linearLayout!=null) {
            linearLayout.removeAllViews();
        }
        linearLayout=(LinearLayout)findViewById(R.id.viewPagerCountDots);
        dots = new ImageView[dotsCount];
        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            if(i==mPosition)
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.dots_selected));
            else
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.dots_unselected));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);
            linearLayout.addView(dots[i], params);
        }
    }

    public void next(View v) {
        int i = getItem(+1);
        if (i < screens.length) {
            vp.setCurrentItem(i);
        } else {
            launchMain();
        }
    }

    public void skip(View view) {
        launchMain();
    }

//    private void ColoredBars(int thisScreen) {
//        int[] colorsInactive = getResources().getIntArray(R.array.dot_on_page_not_active);
//        int[] colorsActive = getResources().getIntArray(R.array.dot_on_page_active);
//        bottomBars = new TextView[screens.length];
//
//        Layout_bars.removeAllViews();
//        for (int i = 0; i < bottomBars.length; i++) {
//            bottomBars[i] = new TextView(this);
//            bottomBars[i].setTextSize(100);
//            bottomBars[i].setText(Html.fromHtml("¯"));
//            Layout_bars.addView(bottomBars[i]);
//            bottomBars[i].setTextColor(colorsInactive[thisScreen]);
//        }
//        if (bottomBars.length > 0)
//            bottomBars[thisScreen].setTextColor(colorsActive[thisScreen]);
//    }

    private int getItem(int i) {
        return vp.getCurrentItem() + i;
    }

    private void launchMain() {
        preferenceManager.setFirstTimeLaunch(false);
        startActivity(new Intent(ZSplashActivityNew.this, LoginActivity.class));
        finish();
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
//            ColoredBars(position);
            drawPageSelectionIndicators(position);
            if (position == screens.length - 1) {

            } else {

            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater inflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(screens[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return screens.length;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v = (View) object;
            container.removeView(v);
        }

        @Override
        public boolean isViewFromObject(View v, Object object) {
            return v == object;
        }
    }
}
