package com.example.weathersearch;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchResults extends AppCompatActivity {
    private String cityState = "";
    String query;
    JSONObject curr;
    JSONArray daily, hourly;
    View tempCard, detailsCard, dailyCard;
    TextView currentTemp, loc, weatherStatus, humidity, wind_speed, visibility, pressure, searchResult,progressBarText;
    ImageView weatherIcon;
    RecyclerView dailyDataList;
    DailyDataAdapter dailyDataListAdapter;
    ProgressBar progressBar;
    FavoriteService favoriteService;
    private FloatingActionButton floatingActionButton;


    WeatherData currWeatherData;
    ArrayList<WeatherData> dailyData, hourlyData;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Intent intent = getIntent();
        query = intent.getStringExtra("searchQuery");
        cityState = query;
        query = query.replaceAll(" ","+");
        //Toast.makeText(this,query,Toast.LENGTH_SHORT);

        ActionBar actionBar = getSupportActionBar();
        // Set below attributes to add logo in ActionBar.
        actionBar.setTitle(cityState);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2e2e2e")));

        favoriteService = new FavoriteService(this);
        floatingActionButton = findViewById(R.id.favoriteButton);

        tempCard = findViewById(R.id.tempCard);
        detailsCard = findViewById(R.id.detailsCard);
        dailyCard = findViewById(R.id.dailyCard);

        currentTemp = tempCard.findViewById(R.id.temp);
        loc = tempCard.findViewById(R.id.location);
        weatherIcon = tempCard.findViewById(R.id.weatherIcon);
        weatherStatus = tempCard.findViewById(R.id.weatherStatus);

        humidity = detailsCard.findViewById(R.id.humidity);
        wind_speed = detailsCard.findViewById(R.id.wind_speed);
        visibility = detailsCard.findViewById(R.id.visibility);
        pressure = detailsCard.findViewById(R.id.pressure);

        progressBar = findViewById(R.id.progressBar);
        progressBarText = findViewById(R.id.pbarText);

        searchResult = findViewById(R.id.searchResult);

        dailyDataList = dailyCard.findViewById(R.id.daily_detail_list);

        tempCard.setVisibility(View.GONE);
        detailsCard.setVisibility(View.GONE);
        dailyCard.setVisibility(View.GONE);
        searchResult.setVisibility(View.GONE);

        if(favoriteService.isPresent(currWeatherData)){
            floatingActionButton.setImageResource(R.drawable.map_marker_minus);
        }
        else{
            floatingActionButton.setImageResource(R.drawable.map_marker_plus);
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(favoriteService.isPresent(currWeatherData)){
                    int idx = favoriteService.getIndex(currWeatherData);
                    MainFragmentPagerAdapter.fragments.remove(idx+1);
                    favoriteService.remove(currWeatherData,dailyData);
                    floatingActionButton.setImageResource(R.drawable.map_marker_plus);
                    MainActivity.tabLayout.removeTabAt(idx+1);
                    Toast.makeText(SearchResults.this, currWeatherData.cityState + " was removed from favorites", Toast.LENGTH_SHORT).show();
                }
                else{
                    favoriteService.push(currWeatherData,dailyData);
                    MainFragmentPagerAdapter.fragments.add(new FavoriteFragment(currWeatherData,dailyData));
                    floatingActionButton.setImageResource(R.drawable.map_marker_minus);
                    MainActivity.tabLayout.addTab(MainActivity.tabLayout.newTab());
                    Toast.makeText(SearchResults.this, currWeatherData.cityState + " was added to favorites", Toast.LENGTH_SHORT).show();

                }
            }
        });

        tempCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchResults.this, WeatherDetails.class);
                intent.putExtra("currWeatherData", currWeatherData);
                intent.putExtra("dailyWeatherData",dailyData);
                startActivity(intent);
                finish();
            }
        });

        getCurrentWeather();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getCurrentWeather() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://nrs-weather-search.wl.r.appspot.com/api/search?latlong=&address="+query;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, (JSONObject) null,
                response -> {
                    try {
                        curr = response.getJSONObject("curr");
                        daily = response.getJSONArray("days");
                        hourly = response.getJSONArray("hours");
//                        Toast.makeText(this, curr.toString(), Toast.LENGTH_SHORT).show();
//                        currentTemp.setText(String.valueOf((int) Math.round(curr.getDouble("temperature"))) + "°F");
//                        loc.setText(curr.getString());
                        currWeatherData = new WeatherData(curr);
                        currWeatherData.cityState = cityState;
                        dailyData = new ArrayList<>();
                        hourlyData = new ArrayList<>();
                        WeatherData data = null;
                        for(int i=0; i<daily.length(); i++){
                            data = new WeatherData(daily.getJSONObject(i));
                            data.cityState = cityState;
                            dailyData.add(data);
                        }
                        for(int i=0; i<hourly.length(); i++){
                            data = new WeatherData(hourly.getJSONObject(i));
                            data.cityState = cityState;
                            hourlyData.add(data);
                        }

                        populateTemperatureCard();
                        populateDetailsCard();
                        populateDailyCard();

                        searchResult.setVisibility(View.VISIBLE);
                        tempCard.setVisibility(View.VISIBLE);
                        detailsCard.setVisibility(View.VISIBLE);
                        dailyCard.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        progressBarText.setVisibility(View.GONE);
                    }
                    catch(JSONException e) {
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                },
                error -> {
                });
        requestQueue.add(jsonObjectRequest);
    }

    private void populateTemperatureCard() {
        currentTemp.setText(currWeatherData.temperature);
        loc.setText(currWeatherData.cityState);
        weatherIcon.setImageResource(WeatherData.codeToPath.get(currWeatherData.weatherCode));
        weatherStatus.setText(WeatherData.codeToDescription.get(currWeatherData.weatherCode));
    }

    private void populateDetailsCard(){
        humidity.setText(currWeatherData.humidity);
        wind_speed.setText(currWeatherData.windSpeed);
        visibility.setText(currWeatherData.visibility);
        pressure.setText(currWeatherData.pressureSeaLevel);
    }

    private void populateDailyCard(){
        dailyDataListAdapter = new DailyDataAdapter(dailyData,this);
        dailyDataList.setAdapter(dailyDataListAdapter);
//        dailyDataList.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        dailyDataList.setHasFixedSize(true);
        dailyDataList.setLayoutManager(linearLayoutManager);
        dailyDataList.setAdapter(dailyDataListAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}