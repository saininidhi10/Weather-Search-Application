package com.example.weathersearch;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class FavoriteService {
    ArrayList<WeatherData> favoriteCurr;
    ArrayList<ArrayList<WeatherData>> favoriteDaily;
    SharedPreferences sharedPreferences;
    Context context;
    SharedPreferences.Editor editor;
    Gson gson;

    public FavoriteService(Context context){
        this.sharedPreferences = context.getSharedPreferences("myPref",context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        this.context = context;
        String defaultSet = "[]";
        gson = new Gson();
        this.favoriteCurr = gson.fromJson(this.sharedPreferences.getString("favoriteCurr",defaultSet),
                new TypeToken<ArrayList<WeatherData>>(){}.getType());
        this.favoriteDaily = gson.fromJson(this.sharedPreferences.getString("favoriteDaily",defaultSet),
                new TypeToken<ArrayList<ArrayList<WeatherData>>>(){}.getType());
    }

    public void updateSet(){
        String defaultSet = "[]";
        this.favoriteCurr = gson.fromJson(this.sharedPreferences.getString("favoriteCurr",defaultSet),
                new TypeToken<ArrayList<WeatherData>>(){}.getType());
        this.favoriteDaily = gson.fromJson(this.sharedPreferences.getString("favoriteDaily",defaultSet),
                new TypeToken<ArrayList<ArrayList<WeatherData>>>(){}.getType());
    }

    public void updateSharedPreferences(){
        editor.putString("favoriteCurr",gson.toJson(this.favoriteCurr));
        editor.putString("favoriteDaily",gson.toJson(this.favoriteDaily));
        editor.commit();
    }

    public void push(WeatherData weatherData, ArrayList<WeatherData> dailyData){
        if(!isPresent(weatherData)){
            this.favoriteCurr.add(weatherData);
            this.favoriteDaily.add(dailyData);
        }
        updateSharedPreferences();
        //Toast.makeText(context, this.favorites.toString(), Toast.LENGTH_SHORT).show();
    }

    public int getIndex(WeatherData weatherData){
        return favoriteCurr.indexOf(weatherData);
    }
    public void remove(WeatherData weatherData, ArrayList<WeatherData> dailyData){
        if(!this.favoriteCurr.isEmpty() && isPresent(weatherData)){
            this.favoriteCurr.remove(weatherData);
            this.favoriteDaily.remove(dailyData);
        }
        updateSharedPreferences();
        //Toast.makeText(context, this.favorites.toString(), Toast.LENGTH_SHORT).show();
    }

    public boolean isPresent(WeatherData weatherData){
        if(this.favoriteCurr.contains(weatherData)){
            return true;
        }
        else{
            return false;
        }
    }

    public ArrayList<WeatherData> getFavoritesCurrArray(){
        updateSet();
        return this.favoriteCurr;
    }

    public ArrayList<ArrayList<WeatherData>> getFavoritesDailyArray(){
        return this.favoriteDaily;
    }



}
