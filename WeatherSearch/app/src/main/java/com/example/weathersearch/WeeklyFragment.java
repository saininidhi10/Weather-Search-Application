package com.example.weathersearch;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.highsoft.highcharts.common.HIColor;
import com.highsoft.highcharts.common.HIGradient;
import com.highsoft.highcharts.common.HIStop;
import com.highsoft.highcharts.common.hichartsclasses.HIArearange;
import com.highsoft.highcharts.common.hichartsclasses.HIAxis;
import com.highsoft.highcharts.common.hichartsclasses.HIChart;
import com.highsoft.highcharts.common.hichartsclasses.HILegend;
import com.highsoft.highcharts.common.hichartsclasses.HIOptions;
import com.highsoft.highcharts.common.hichartsclasses.HITitle;
import com.highsoft.highcharts.common.hichartsclasses.HITooltip;
import com.highsoft.highcharts.common.hichartsclasses.HIXAxis;
import com.highsoft.highcharts.common.hichartsclasses.HIYAxis;
import com.highsoft.highcharts.core.HIChartView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class WeeklyFragment extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weekly, container, false);
        try {
            createHC(view);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return view;
    }

    private void createHC(View view) throws ParseException {
        HIChartView hiChartView = view.findViewById(R.id.hc_temp);
        HIOptions options = new HIOptions();
        HIChart chart = new HIChart();
        chart.setType("areaRange");
        chart.setZoomType("x");
        options.setChart(chart);

        HITitle title = new HITitle();
        title.setText("Temperature variation by day");
        options.setTitle(title);

        HIXAxis xAxis = new HIXAxis();
        xAxis.setType("datetime");
        ArrayList<HIXAxis> hixAxes = new ArrayList<HIXAxis>();
        hixAxes.add(xAxis);
        options.setXAxis(hixAxes);

        HIYAxis yAxis = new HIYAxis();
        yAxis.setTitle(new HITitle());
        ArrayList<HIYAxis> hiyAxes = new ArrayList<HIYAxis>();
        hiyAxes.add(yAxis);
        options.setYAxis(hiyAxes);

        HITooltip tooltip = new HITooltip();
        tooltip.setShadow(true);
        tooltip.setValueSuffix("°F");
        options.setTooltip(tooltip);

        HILegend hiLegend = new HILegend();
        hiLegend.setEnabled(false);
        options.setLegend(hiLegend);

        HIArearange areaRange = new HIArearange();
        areaRange.setName("Temperatures");
        areaRange.setLineColor(HIColor.initWithHexValue("fdb611"));
        HIGradient gradient = new HIGradient(0f,0f,1f,0f);
        LinkedList<HIStop> stops = new LinkedList<HIStop>();
        stops.add(new HIStop(0f, HIColor.initWithRGBA(244,175,26,0.72)));
        stops.add(new HIStop(1f, HIColor.initWithRGBA(134,181,247,0.55)));
        areaRange.setFillColor(HIColor.initWithLinearGradient(gradient,stops));

        ArrayList<ArrayList<Long>> series = new ArrayList<ArrayList<Long>>();
        WeatherDetails weatherDetails = (WeatherDetails) getActivity();
        ArrayList<WeatherData> dailyData = weatherDetails.getDailyData();
        for(int i=0; i<dailyData.size(); i++){
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            Long day = date.parse(dailyData.get(i).startTime).getTime();
            Long tempMin = Long.parseLong(dailyData.get(i).temperatureMin);
            Long tempMax = Long.parseLong(dailyData.get(i).temperatureMax);
            ArrayList<Long> record = new ArrayList<Long>();
            record.add(day);
            record.add(tempMin);
            record.add(tempMax);
            series.add(record);
        }

        areaRange.setData(series);
        options.setSeries(new ArrayList<>(Arrays.asList(areaRange)));

        hiChartView.setOptions(options);
    }
}