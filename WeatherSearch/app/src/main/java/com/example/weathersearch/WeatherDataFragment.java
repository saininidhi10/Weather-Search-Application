package com.example.weathersearch;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.highsoft.highcharts.common.HIColor;
import com.highsoft.highcharts.common.hichartsclasses.HIBackground;
import com.highsoft.highcharts.common.hichartsclasses.HICSSObject;
import com.highsoft.highcharts.common.hichartsclasses.HIChart;
import com.highsoft.highcharts.common.hichartsclasses.HIData;
import com.highsoft.highcharts.common.hichartsclasses.HIEvents;
import com.highsoft.highcharts.common.hichartsclasses.HIOptions;
import com.highsoft.highcharts.common.hichartsclasses.HIPane;
import com.highsoft.highcharts.common.hichartsclasses.HIPlotOptions;
import com.highsoft.highcharts.common.hichartsclasses.HISolidgauge;
import com.highsoft.highcharts.common.hichartsclasses.HITitle;
import com.highsoft.highcharts.common.hichartsclasses.HITooltip;
import com.highsoft.highcharts.common.hichartsclasses.HIYAxis;
import com.highsoft.highcharts.core.HIChartView;
import com.highsoft.highcharts.core.HIFunction;

import java.util.ArrayList;
import java.util.Arrays;

public class WeatherDataFragment extends Fragment {

    private String renderIconsString = "function renderIcons() {" +
            "                            if(!this.series[0].icon) {" +
            "                               this.series[0].icon = this.renderer.path(['M', -8, 0, 'L', 8, 0, 'M', 0, -8, 'L', 8, 0, 0, 8]).attr({'stroke': '#303030','stroke-linecap': 'round','stroke-linejoin': 'round','stroke-width': 2,'zIndex': 10}).add(this.series[2].group);}this.series[0].icon.translate(this.chartWidth / 2 - 10,this.plotHeight / 2 - this.series[0].points[0].shapeArgs.innerR -(this.series[0].points[0].shapeArgs.r - this.series[0].points[0].shapeArgs.innerR) / 2); if(!this.series[1].icon) {this.series[1].icon = this.renderer.path(['M', -8, 0, 'L', 8, 0, 'M', 0, -8, 'L', 8, 0, 0, 8,'M', 8, -8, 'L', 16, 0, 8, 8]).attr({'stroke': '#ffffff','stroke-linecap': 'round','stroke-linejoin': 'round','stroke-width': 2,'zIndex': 10}).add(this.series[2].group);}this.series[1].icon.translate(this.chartWidth / 2 - 10,this.plotHeight / 2 - this.series[1].points[0].shapeArgs.innerR -(this.series[1].points[0].shapeArgs.r - this.series[1].points[0].shapeArgs.innerR) / 2); if(!this.series[2].icon) {this.series[2].icon = this.renderer.path(['M', 0, 8, 'L', 0, -8, 'M', -8, 0, 'L', 0, -8, 8, 0]).attr({'stroke': '#303030','stroke-linecap': 'round','stroke-linejoin': 'round','stroke-width': 2,'zIndex': 10}).add(this.series[2].group);}this.series[2].icon.translate(this.chartWidth / 2 - 10,this.plotHeight / 2 - this.series[2].points[0].shapeArgs.innerR -(this.series[2].points[0].shapeArgs.r - this.series[2].points[0].shapeArgs.innerR) / 2);}";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather_data, container, false);
        createHC(view);
        return view;
    }

    private void createHC(View view){
        HIChartView chartView = view.findViewById(R.id.hc_wd);

        HIOptions options = new HIOptions();

        HIChart chart = new HIChart();
        chart.setType("solidgauge");
        chart.setEvents(new HIEvents());
        chart.getEvents().setRender(new HIFunction(renderIconsString));
        options.setChart(chart);

        HITitle title = new HITitle();
        title.setText("Stat Summary");
        title.setStyle(new HICSSObject());
        title.getStyle().setFontSize("24px");
        options.setTitle(title);

        HITooltip tooltip = new HITooltip();
        tooltip.setBorderWidth(0);
        tooltip.setBackgroundColor(HIColor.initWithName("none"));
        tooltip.setShadow(false);
        tooltip.setStyle(new HICSSObject());
        tooltip.getStyle().setFontSize("16px");
        tooltip.setPointFormat("{series.name}<br><span style=\"font-size:2em; color: {point.color}; font-weight: bold\">{point.y}%</span>");
        tooltip.setPositioner(new HIFunction(
                "function (labelWidth) {" +
                        "   return {" +
                        "       x: (this.chart.chartWidth - labelWidth) /2," +
                        "       y: (this.chart.plotHeight / 2) + 15" +
                        "   };" +
                        "}"
        ));

        options.setTooltip(tooltip);

        HIPane pane = new HIPane();
        pane.setStartAngle(0);
        pane.setEndAngle(360);

        HIBackground background1 = new HIBackground();
        background1.setOuterRadius("112%");
        background1.setInnerRadius("88%");
        background1.setBackgroundColor(HIColor.initWithRGBA(131, 239, 105, 0.36));
        background1.setBorderWidth(0);

        HIBackground background2 = new HIBackground();
        background2.setOuterRadius("87%");
        background2.setInnerRadius("63%");
        background2.setBackgroundColor(HIColor.initWithRGBA(105, 161, 232, 0.36));
        background2.setBorderWidth(0);

        HIBackground background3 = new HIBackground();
        background3.setOuterRadius("62%");
        background3.setInnerRadius("38%");
        background3.setBackgroundColor(HIColor.initWithRGBA(252, 202, 202, 1.0));
        background3.setBorderWidth(0);

        pane.setBackground(new ArrayList<HIBackground>(Arrays.asList(background1, background2, background3)));
        options.setPane(pane);

        HIYAxis hiyAxis = new HIYAxis();
        hiyAxis.setMin(0);
        hiyAxis.setMax(100);
        hiyAxis.setLineWidth(0);
        hiyAxis.setTickPositions(new ArrayList<>());

        options.setYAxis(new ArrayList<>(Arrays.asList(hiyAxis)));

        HIPlotOptions plotOptions = new HIPlotOptions();
        plotOptions.setSolidgauge(new HISolidgauge());
        plotOptions.getSolidgauge().setDataLabels(new ArrayList<>());
        plotOptions.getSolidgauge().setLinecap("round");
        plotOptions.getSolidgauge().setStickyTracking(false);
        plotOptions.getSolidgauge().setRounded(true);
        options.setPlotOptions(plotOptions);

        WeatherDetails weatherDetails = (WeatherDetails) getActivity();
        WeatherData weatherData = weatherDetails.getCurrWeatherData();

        HISolidgauge solidgauge1 = new HISolidgauge();
        solidgauge1.setName("Cloud Cover");
        HIData data1 = new HIData();
        data1.setColor(HIColor.initWithHexValue("65cc01"));
        data1.setRadius("112%");
        data1.setInnerRadius("88%");
        data1.setY(Integer.parseInt(weatherData.cloudCover.split("%")[0]));
        solidgauge1.setData(new ArrayList<>(Arrays.asList(data1)));

        HISolidgauge solidgauge2 = new HISolidgauge();
        solidgauge2.setName("Precipitation");
        HIData data2 = new HIData();
        data2.setColor(HIColor.initWithHexValue("66b2ff"));
        data2.setRadius("87%");
        data2.setInnerRadius("63%");
        Log.d("pp",weatherData.precipitationIntensity);
        data2.setY((int) Double.parseDouble(weatherData.precipitationIntensity.split("%")[0]));
        solidgauge2.setData(new ArrayList<>(Arrays.asList(data2)));

        HISolidgauge solidgauge3 = new HISolidgauge();
        solidgauge3.setName("Humidity");
        HIData data3 = new HIData();
        data3.setColor(HIColor.initWithHexValue("f47b5b"));
        data3.setRadius("62%");
        data3.setInnerRadius("38%");
        data3.setY((int) Integer.parseInt(weatherData.humidity.split("%")[0]));
        solidgauge3.setData(new ArrayList<>(Arrays.asList(data3)));

        options.setSeries(new ArrayList<>(Arrays.asList(solidgauge1,solidgauge2,solidgauge3)));

        chartView.setOptions(options);

    }
}