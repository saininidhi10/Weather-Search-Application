package com.example.weathersearch;

import android.opengl.Visibility;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class TodayFragment extends Fragment {

    ImageView windSpeedIcon, pressureIcon, precipitationIcon, tempIcon, statusIcon, humidityIcon, visibilityIcon, cloudCoverIcon, ozoneIcon;
    TextView windSpeed, pressure, precipitation, temp, status, humidity, visibility, cloudCover, ozone;
    TextView windSpeedValue, pressureValue, precipitationValue, tempValue, statusValue, humidityValue, visibilityValue, cloudCoverValue, ozoneValue;
    View windSpeedCard, pressureCard, precipitationCard, tempCard, statusCard, humidityCard, visibilityCard, cloudCoverCard, ozoneCard;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        windSpeedCard = getView().findViewById(R.id.windSpeed);
        pressureCard = getView().findViewById(R.id.pressure);
        precipitationCard = getView().findViewById(R.id.precipitation);
        tempCard = getView().findViewById(R.id.temp);
        statusCard = getView().findViewById(R.id.status);
        humidityCard = getView().findViewById(R.id.humidity);
        visibilityCard = getView().findViewById(R.id.visibility);
        cloudCoverCard = getView().findViewById(R.id.cloudCover);
        ozoneCard = getView().findViewById(R.id.ozone);

        windSpeed = windSpeedCard.findViewById(R.id.field);
        windSpeed.setText("Wind Speed");
        pressure = pressureCard.findViewById(R.id.field);
        pressure.setText("Pressure");
        precipitation = precipitationCard.findViewById(R.id.field);
        precipitation.setText("Precipitation");
        temp = tempCard.findViewById(R.id.field);
        temp.setText("Temperature");
        status = statusCard.findViewById(R.id.field);
        humidity = humidityCard.findViewById(R.id.field);
        humidity.setText("Humidity");
        visibility = visibilityCard.findViewById(R.id.field);
        visibility.setText("Visibility");
        cloudCover = cloudCoverCard.findViewById(R.id.field);
        cloudCover.setText("Cloud Cover");
        ozone = ozoneCard.findViewById(R.id.field);
        ozone.setText("Ozone");
        status.setVisibility(View.INVISIBLE);

        WeatherDetails weatherDetails = (WeatherDetails) getActivity();
        WeatherData weatherData = weatherDetails.getCurrWeatherData();

        windSpeedValue = windSpeedCard.findViewById(R.id.value);
        windSpeedValue.setText(weatherData.windSpeed);
        pressureValue = pressureCard.findViewById(R.id.value);
        pressureValue.setText(weatherData.pressureSeaLevel);
        precipitationValue = precipitationCard.findViewById(R.id.value);
        precipitationValue.setText(weatherData.precipitationIntensity);
        tempValue = tempCard.findViewById(R.id.value);
        tempValue.setText(weatherData.temperature);
        statusValue = statusCard.findViewById(R.id.value);
        statusValue.setText(WeatherData.codeToDescription.get(weatherData.weatherCode));
        humidityValue = humidityCard.findViewById(R.id.value);
        humidityValue.setText(weatherData.humidity);
        visibilityValue = visibilityCard.findViewById(R.id.value);
        visibilityValue.setText(weatherData.visibility);
        cloudCoverValue = cloudCoverCard.findViewById(R.id.value);
        cloudCoverValue.setText(weatherData.cloudCover);
        ozoneValue = ozoneCard.findViewById(R.id.value);
        ozoneValue.setText(weatherData.uvIndex);

        windSpeedIcon = windSpeedCard.findViewById(R.id.icon);
        windSpeedIcon.setImageResource(R.drawable.weather_windy);
        pressureIcon = pressureCard.findViewById(R.id.icon);
        pressureIcon.setImageResource(R.drawable.gauge);
        precipitationIcon = precipitationCard.findViewById(R.id.icon);
        precipitationIcon.setImageResource(R.drawable.weather_pouring);
        tempIcon = tempCard.findViewById(R.id.icon);
        tempIcon.setImageResource(R.drawable.thermometer);
        statusIcon = statusCard.findViewById(R.id.icon);
        statusIcon.setImageResource(WeatherData.codeToPath.get(weatherData.weatherCode));
        humidityIcon = humidityCard.findViewById(R.id.icon);
        humidityIcon.setImageResource(R.drawable.water_percent);
        visibilityIcon = visibilityCard.findViewById(R.id.icon);
        visibilityIcon.setImageResource(R.drawable.eye_outline);
        cloudCoverIcon = cloudCoverCard.findViewById(R.id.icon);
        cloudCoverIcon.setImageResource(R.drawable.weather_fog);
        ozoneIcon = ozoneCard.findViewById(R.id.icon);
        ozoneIcon.setImageResource(R.drawable.earth);
    }
}