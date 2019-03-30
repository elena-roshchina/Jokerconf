package ru.jokerconf.jokerconf;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {


    //private final int PAGE_COUNT = 3;
    private int page_count_number;
    //private String tabTitles[] = new String[]{"Day 1", "Day 2", "Agenda"};

    private List<String> tabTitles = new ArrayList<>();
    private Context context;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context, int days) {
        super(fm);
        this.context = context;
        this.page_count_number = days + 1; // pages = days + agenda

        for (int i = 0; i < page_count_number - 1; i ++){
            int pageNum = i + 1;
            String title = "Day " + pageNum;
            this.tabTitles.add(title);
        }
        this.tabTitles.add("Agenda");
    }

    @Override
    public int getCount() {
            //return PAGE_COUNT;
        return page_count_number;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position + 1);
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        // генерируем заголовок в зависимости от позиции
        //return tabTitles[position];
        return tabTitles.get(position);
    }
}