package com.example.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import org.jetbrains.annotations.NotNull;

public class IntroductoryActivity extends AppCompatActivity {

    ImageView backgroundImage , logo ;
    TextView logoText;
    LottieAnimationView lottieAnimationView;

    private static final int NUM_PAGES = 3;
    private ViewPager viewPager;
    private ScreenSlidePagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introductory);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        backgroundImage = findViewById(R.id.background_image);
        logo = findViewById(R.id.logo);
        logoText = findViewById(R.id.logo_txt);
        lottieAnimationView = findViewById(R.id.lottie);
        viewPager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        backgroundImage.animate().translationY(-2600).setDuration(1000).setStartDelay(3500);
        logo.animate().translationY(2600).setDuration(1000).setStartDelay(3500);
        logoText.animate().translationY(2600).setDuration(1000).setStartDelay(3500);
        lottieAnimationView.animate().translationY(2600).setDuration(1000).setStartDelay(3500);



    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter{

        public ScreenSlidePagerAdapter(@NonNull @NotNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @NotNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0 :
                    OnBoardingFragment1 tab1 = new OnBoardingFragment1();
                    return tab1;
                case 1 :
                    OnBoardingFragment2 tab2 = new OnBoardingFragment2();
                    return tab2;
                case 2 :
                    OnBoardingFragment3 tab3 = new OnBoardingFragment3();
                    return tab3;
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}