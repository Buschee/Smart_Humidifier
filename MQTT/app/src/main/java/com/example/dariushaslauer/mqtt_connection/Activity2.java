package com.example.dariushaslauer.mqtt_connection;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.lang.reflect.Array;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Map;

public class Activity2 extends AppCompatActivity  {

    private static final String Tag = "Activity2";
    private LineChart mChart;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        Intent intent = getIntent();
        ArrayList<Entry> yValues = (ArrayList<Entry>)getIntent().getSerializableExtra("arrayListTemperature");
        ArrayList<Entry> xValues = (ArrayList<Entry>)getIntent().getSerializableExtra("arrayListHumidity");

        mChart = (LineChart)findViewById(R.id.linechart);
        //mChart.setOnChartGestureListener(Activity2.this);
        //mChart.setOnChartValueSelectedListener(Activity2.this);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);
        //ArrayList<Entry> yValues = new ArrayList<>();
        //yValues.add(new Entry(0, 60f));
        //yValues.add(new Entry(1, 50f));
        //yValues.add(new Entry(2, 70f));
        //yValues.add(new Entry(3, 30f));
        //yValues.add(new Entry(4, 50f));
        //yValues.add(new Entry(6, 40f));
        //yValues.add(new Entry(7, 40f));
        //yValues.add(new Entry(8, 30f));
        //yValues.add(new Entry(9, 20f));
        //yValues.add(new Entry(10, 10f));
        //yValues.add(new Entry(11, 50f));
        //yValues.add(new Entry(13, 40f));

        LineDataSet set1 = new LineDataSet(yValues, "Temperatur");
        LineDataSet set2 = new LineDataSet(xValues, "Luftfeuchtigkeit");

        //design-technische Änderungen
        set1.setFillAlpha(110);
        set1.setColor(Color.RED);
        set1.setLineWidth(3f);
        set1.setDrawCircles(false);
        set1.setDrawValues(false);

        set2.setFillAlpha(110);
        set2.setColor(Color.BLUE);
        set2.setLineWidth(3f);
        set2.setDrawCircles(false);
        set2.setDrawValues(false);

        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.getDescription().setEnabled(false);

        //Legende
        Legend legend = mChart.getLegend();
        legend.setTextSize(20f);


        //ArrayList<LineDataSet> dataSets = new ArrayList<>();
        //dataSets.add(set1);
        //LineData data = new LineData(set1);
        //mChart.setData(data);

        LineData chartData = new LineData();
        LineDataSet linedataset1 = new LineDataSet(xValues, "Dataset1");
        chartData.addDataSet(linedataset1);
        linedataset1.setColor(Color.BLUE);
        linedataset1.setLineWidth(3f);
        linedataset1.setDrawCircles(false);
        linedataset1.setDrawValues(false);

        LineDataSet linedataset2 = new LineDataSet(yValues, "Dataset2");
        chartData.addDataSet(linedataset2);
        linedataset2.setColor(Color.RED);
        linedataset2.setLineWidth(3f);
        linedataset2.setDrawCircles(false);
        linedataset2.setDrawValues(false);
        mChart.setData(chartData);

    }
}

