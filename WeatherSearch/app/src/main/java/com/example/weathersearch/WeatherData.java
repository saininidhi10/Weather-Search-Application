package com.example.weathersearch;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class WeatherData implements Serializable {
    String startTime, temperature,temperatureApparent,temperatureMin,temperatureMax,windSpeed,humidity, cloudCover;
    String pressureSeaLevel,uvIndex,precipitationIntensity,visibility;
    int weatherCode;
    String cityState;
    int[] weatherCodes = {4201,4001,4200,6201,6001,6200,6000,4000,7101,7000,7102,5101,5000,5100,5001,8000,2100,2000,1001,1102,1101,1100,1000,3000,3001,3002};
    String[] descriptions = {"Heavy Rain","Rain","Light Rain","Heavy Freezing Rain","Freezing Rain",
            "Light Freezing Rain","Freezing Drizzle","Drizzle","Heavy Ice Pellets","Ice Pellets",
            "Light Ice Pellets","Heavy Snow","Snow","Light Snow","Flurries","Thunderstorm","Light Fog",
            "Fog","Cloudy","Mostly Cloudy","Partly Cloudy","Mostly Clear","Clear","Light Wind","Wind","Strong Wind"};
    int[] paths = {R.drawable.rain_heavy,R.drawable.rain ,R.drawable.rain_light ,R.drawable.freezing_rain_heavy ,
            R.drawable.freezing_rain,R.drawable.freezing_rain_light ,R.drawable.freezing_drizzle ,R.drawable.drizzle ,
            R.drawable.ice_pellets_heavy,R.drawable.ice_pellets ,R.drawable.ice_pellets_light ,
            R.drawable.snow_heavy ,R.drawable.snow ,R.drawable.snow_light ,R.drawable.flurries,
            R.drawable.tstorm ,R.drawable.fog_light ,R.drawable.fog ,R.drawable.cloudy,
            R.drawable.mostly_cloudy ,R.drawable.partly_cloudy_day ,R.drawable.mostly_clear_day ,
            R.drawable.clear_day ,R.drawable.light_wind ,R.drawable.wind ,R.drawable.strong_wind};

    static HashMap<Integer,String> codeToDescription;
    static HashMap<Integer,Integer> codeToPath;

    @RequiresApi(api = Build.VERSION_CODES.O)
    WeatherData(JSONObject values) throws JSONException {
        codeToDescription = new HashMap<>();
        codeToPath = new HashMap<>();
        for(int i = 0; i<weatherCodes.length; i++){
            codeToDescription.put(weatherCodes[i],descriptions[i]);
            codeToPath.put(weatherCodes[i],paths[i]);
        }

        if(values.has("startTime")) {
            startTime = values.getString("startTime");
            String[] arr = startTime.split("T");
            startTime = arr[0];
            LocalDate parsedDate = LocalDate.parse(startTime, DateTimeFormatter.ISO_LOCAL_DATE);
            startTime = parsedDate.toString();
        }
        if(values.has("values")) {
            values = values.getJSONObject("values");
        }
        temperature = String.valueOf((int) Math.round(values.getDouble("temperature"))) + "°F";
        temperatureApparent = String.valueOf(Math.round(values.getDouble("temperatureApparent")*100.0)/100.0) + "°F";
        temperatureMin = String.valueOf((int) Math.round(values.getDouble("temperatureMin")));
        temperatureMax = String.valueOf((int) Math.round(values.getDouble("temperatureMax")));
        windSpeed = String.valueOf(values.getDouble("windSpeed")) + "mph";
        humidity = String.valueOf(values.getInt("humidity")) + "%";
        cloudCover = String.valueOf(values.getInt("cloudCover")) + "%";
        pressureSeaLevel = String.valueOf(values.getDouble("pressureSeaLevel")) + "inHg";
        if(values.has("uvIndex")) {
            uvIndex = String.valueOf(values.getInt("uvIndex"));
        }
        else{
            uvIndex = "0";
        }
        weatherCode = values.getInt("weatherCode");
        if(values.has("precipitationIntensity")) {
            precipitationIntensity = String.valueOf(Math.round(values.getDouble("precipitationIntensity") * 100.0) / 100.0) + "%";//rounding off to 2 dec
        }
        else{
            precipitationIntensity = "0.00%";
        }
        visibility = String.valueOf(values.getDouble("visibility")) + "mi";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeatherData that = (WeatherData) o;
        return Objects.equals(cityState, that.cityState);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(cityState);
        return result;
    }
}
