package com.example.weathersearch;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CurrentWeather extends Fragment {
    private String latlong = "", cityState = "";
    private FusedLocationProviderClient fusedLocationProviderClient;

    JSONObject curr;
    JSONArray daily, hourly;
    View tempCard, detailsCard, dailyCard;
    TextView currentTemp, loc, weatherStatus, humidity, wind_speed, visibility, pressure, progressBarText;
    ImageView weatherIcon;
    RecyclerView dailyDataList;
    DailyDataAdapter dailyDataListAdapter;
    ProgressBar progressBar;


    WeatherData currWeatherData;
    ArrayList<WeatherData> dailyData, hourlyData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(R.layout.fragment_current_weather,container,false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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

        dailyDataList = dailyCard.findViewById(R.id.daily_detail_list);
        progressBar = getView().findViewById(R.id.progressBar);
        progressBarText = getView().findViewById(R.id.pbarText);

        tempCard.setVisibility(View.GONE);
        detailsCard.setVisibility(View.GONE);
        dailyCard.setVisibility(View.GONE);

        ((MainActivity) getActivity()).tabLayout.setVisibility(View.GONE);

        getLocationAndWeatherIP();

        tempCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((MainActivity) getActivity(), WeatherDetails.class);
                intent.putExtra("currWeatherData", currWeatherData);
                intent.putExtra("dailyWeatherData",dailyData);
                getActivity().startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getCurrentWeather() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String url = "https://nrs-weather-search.wl.r.appspot.com/api/search?address=&latlong="+latlong;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, (JSONObject) null,
                response -> {
                    try {
                        curr = response.getJSONObject("curr");
                        daily = response.getJSONArray("days");
                        hourly = response.getJSONArray("hours");
//                        Toast.makeText(getContext(), curr.toString(), Toast.LENGTH_SHORT).show();
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
                        tempCard.setVisibility(View.VISIBLE);
                        detailsCard.setVisibility(View.VISIBLE);
                        dailyCard.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        progressBarText.setVisibility(View.GONE);


                        ((MainActivity) getActivity()).tabLayout.setVisibility(View.VISIBLE);

                    }
                    catch(JSONException e) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                },
                error -> {

                });
        requestQueue.add(jsonObjectRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getLocationAndWeatherIP(){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String url = "https://ipinfo.io/?token=b1ac7c2d0e80a3";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, (JSONObject) null,
                response -> {
                    try {
                        latlong = response.getString("loc");
                        cityState = response.getString("city")+", "+ response.getString("region");
//                        Toast.makeText(getContext(), "Hellloooo", Toast.LENGTH_SHORT).show();
                        getCurrentWeather();
                    }
                    catch(JSONException e) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                },
                error -> {

                });
        requestQueue.add(jsonObjectRequest);
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void getLocationAndWeather(){
        if(checkPermissions()){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener((Activity) getContext(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location!=null){
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        latlong = latitude +","+ longitude;
//                        Toast.makeText(getContext(),"Hello",Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getContext(), latlong, Toast.LENGTH_SHORT).show();
                        Log.d("latlong",latlong);
                        getCurrentWeather();
                    }
                }
            })
            .addOnFailureListener((Activity) getContext(), new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });
        }
        else{
            Toast.makeText(getContext(), "Location Permissions are not granted", Toast.LENGTH_SHORT).show();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    private boolean checkPermissions() {
        return getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
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