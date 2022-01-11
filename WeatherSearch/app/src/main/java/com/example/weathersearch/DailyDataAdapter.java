package com.example.weathersearch;

import static java.lang.Math.max;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DailyDataAdapter extends RecyclerView.Adapter<DailyDataAdapter.ViewHolder>{
    private ArrayList<WeatherData> dailyData;
    private Context context;

    public DailyDataAdapter(ArrayList<WeatherData> dailyData, Context context){
        this.dailyData = dailyData;
        this.context = context;
    }

    @NonNull
    @Override
    public DailyDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyDataAdapter.ViewHolder holder, int position) {
        WeatherData data = dailyData.get(position);
        holder.date.setText(data.startTime);
        holder.weatherStatus.setImageResource(WeatherData.codeToPath.get(data.weatherCode));
        holder.minTemp.setText(String.format("%02d",Integer.parseInt(data.temperatureMin)));
        holder.maxTemp.setText(String.format("%02d",Integer.parseInt(data.temperatureMax)));
    }

    @Override
    public int getItemCount() {
        return max(dailyData.size(),1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView date, minTemp, maxTemp;
        private ImageView weatherStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            minTemp = itemView.findViewById(R.id.minTemp);
            maxTemp = itemView.findViewById(R.id.maxTemp);
            weatherStatus = itemView.findViewById(R.id.day_status);
        }
    }
}
