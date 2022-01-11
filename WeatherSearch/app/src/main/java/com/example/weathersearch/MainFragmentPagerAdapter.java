package com.example.weathersearch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class MainFragmentPagerAdapter extends FragmentStateAdapter {
    static ArrayList<Fragment> fragments = new ArrayList<>();

    public MainFragmentPagerAdapter(MainActivity mainActivity) {
        super(mainActivity);
        fragments.add(new CurrentWeather());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
