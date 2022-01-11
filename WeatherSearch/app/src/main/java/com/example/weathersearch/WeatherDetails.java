package com.example.weathersearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.ArrayList;

public class WeatherDetails extends AppCompatActivity {

    private TodayFragment todayFragment;
    private WeeklyFragment weeklyFragment;
    private WeatherDataFragment weatherDataFragment;
    WeatherData currWeatherData;
    ArrayList<WeatherData> dailyData;
    private FavoriteService favoriteService;
    private FragmentStateAdapter pagerAdapter;

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        currWeatherData = (WeatherData) b.getSerializable("currWeatherData");
        dailyData = (ArrayList<WeatherData>) b.getSerializable("dailyWeatherData");
        favoriteService = new FavoriteService(this);


        ActionBar actionBar = getSupportActionBar();
        // Set below attributes to add logo in ActionBar.
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(currWeatherData.cityState);

        viewPager2 = findViewById(R.id.detailsViewPager);
        tabLayout = findViewById(R.id.tabLayout);
        pagerAdapter = new DetailsFragmentPagerAdapter(this);
        viewPager2.setAdapter(pagerAdapter);
        viewPager2.setUserInputEnabled(true);

         new TabLayoutMediator(tabLayout,viewPager2,(tab, position) -> {
            tab.setText(position==0?"TODAY":(position==1?"WEEKLY":"WEATHER DATA"));
            tab.setIcon(position==0?R.drawable.calendar_today:(position==1?R.drawable.trending_up:R.drawable.thermometer));
        }).attach();

         tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().equals("TODAY")){
                    viewPager2.setCurrentItem(0,true);
                }
                else if(tab.getText().equals("WEEKLY")){
                    //Toast.makeText(MainActivity.this,tab.getText(), Toast.LENGTH_SHORT).show();
                    viewPager2.setCurrentItem(1,true);
                }
                else if(tab.getText().equals("WEEKLY")){
                    viewPager2.setCurrentItem(2,true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        todayFragment = new TodayFragment();
        weeklyFragment = new WeeklyFragment();
        weatherDataFragment = new WeatherDataFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.tweet_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
        case android.R.id.home: onBackPressed(); return true;
        case R.id.tweet: String tweetText = "Check%20out%20" + currWeatherData.cityState
                + "'s%20weather!%20" + "It%20is%20"+currWeatherData.temperatureApparent+"!%0A"+"%23CSCI571WeatherSearch";
        Uri uri = Uri.parse("https://twitter.com/intent/tweet?text="+tweetText);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
        return true;

        default: return super.onOptionsItemSelected(item);
        }
    }

    protected WeatherData getCurrWeatherData(){
        return currWeatherData;
    }

    protected ArrayList<WeatherData> getDailyData(){
        return  dailyData;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private class DetailsFragmentPagerAdapter extends FragmentStateAdapter {

        public DetailsFragmentPagerAdapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @NotNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return todayFragment;
                case 1:
                    return weeklyFragment;
                case 2:
                    return weatherDataFragment;
                default:
                    return todayFragment;
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}