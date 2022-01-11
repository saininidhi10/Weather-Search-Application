package com.example.weathersearch;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {

    View tempCard, detailsCard, dailyCard;
    TextView currentTemp, loc, weatherStatus, humidity, wind_speed, visibility, pressure;
    ImageView weatherIcon;
    RecyclerView dailyDataList;
    FloatingActionButton removeFavorite;
    DailyDataAdapter dailyDataListAdapter;
    FavoriteService favoriteService;

    WeatherData currWeatherData;
    ArrayList<WeatherData> dailyData;
    public FavoriteFragment(WeatherData currWeatherData, ArrayList<WeatherData> dailyData) {
        this.currWeatherData = currWeatherData;
        this.dailyData = dailyData;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        favoriteService = new FavoriteService(getContext());
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tempCard = getView().findViewById(R.id.tempCard);
        detailsCard = getView().findViewById(R.id.detailsCard);
        dailyCard = getView().findViewById(R.id.dailyCard);

        currentTemp = tempCard.findViewById(R.id.temp);
        loc = tempCard.findViewById(R.id.location);
        weatherIcon = tempCard.findViewById(R.id.weatherIcon);
        weatherStatus = tempCard.findViewById(R.id.weatherStatus);

        humidity = detailsCard.findViewById(R.id.humidity);
        wind_speed = detailsCard.findViewById(R.id.wind_speed);
        visibility = detailsCard.findViewById(R.id.visibility);
        pressure = detailsCard.findViewById(R.id.pressure);

        removeFavorite = getView().findViewById(R.id.removeFavorite);

        removeFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(favoriteService.isPresent(currWeatherData)){
                    int idx = favoriteService.getIndex(currWeatherData);
                    MainFragmentPagerAdapter.fragments.remove(idx+1);
                    favoriteService.remove(currWeatherData,dailyData);
                    MainActivity.tabLayout.removeTabAt(idx+1);
                    Toast.makeText(getContext(), currWeatherData.cityState + " was removed from favorites", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dailyDataList = dailyCard.findViewById(R.id.daily_detail_list);

        tempCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((MainActivity) getActivity(), WeatherDetails.class);
                intent.putExtra("currWeatherData", currWeatherData);
                intent.putExtra("dailyWeatherData",dailyData);
                getActivity().startActivity(intent);
            }
        });

        populateTemperatureCard();
        populateDetailsCard();
        populateDailyCard();
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
        dailyDataListAdapter = new DailyDataAdapter(dailyData,getContext());
        dailyDataList.setAdapter(dailyDataListAdapter);
//        dailyDataList.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getContext());
        dailyDataList.setHasFixedSize(true);
        dailyDataList.setLayoutManager(linearLayoutManager);
        dailyDataList.setAdapter(dailyDataListAdapter);
    }
}