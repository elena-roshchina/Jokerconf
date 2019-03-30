package ru.jokerconf.jokerconf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScheduleActivity extends AppCompatActivity {

    private static final String SHARED_PREF = "JOKER_SHARED_PREF";
    private static final String SHARED_DAYS = "SHARED_DAYS";
    private static final int DAYS_DEFAULT = 1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    openInfoActivity();
                    return true;
                case R.id.navigation_dashboard:
                    //openScheduleActivity
                    return true;
                case R.id.navigation_notifications:
                    openMapActivity();
                    return true;
            }
            return false;
        }
    };

    private void openInfoActivity(){
        Intent secondActivityIntent = new Intent(this, InfoActivity.class);
        startActivity(secondActivityIntent);
    }

    private void openMapActivity(){
        Intent secondActivityIntent = new Intent(this, MapActivity.class);
        startActivity(secondActivityIntent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        int restoredDays = DAYS_DEFAULT;

        if (sharedPref.contains(SHARED_DAYS)){
            restoredDays = sharedPref.getInt(SHARED_DAYS, DAYS_DEFAULT);
        }

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(
                new SampleFragmentPagerAdapter(getSupportFragmentManager(),
                        ScheduleActivity.this,
                         restoredDays));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                List<Fragment> listFragments = getSupportFragmentManager().getFragments();
                for (Fragment f : listFragments){
                    PageFragment pF = (PageFragment) f;
                    pF.update();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
