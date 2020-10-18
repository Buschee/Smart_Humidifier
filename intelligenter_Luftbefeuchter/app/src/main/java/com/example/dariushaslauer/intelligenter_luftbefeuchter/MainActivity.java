package com.example.dariushaslauer.intelligenter_luftbefeuchter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import pl.pawelkleczkowski.customgauge.CustomGauge;



public class MainActivity extends AppCompatActivity{

    MqttHelper mqttHelper;
    TextView temp;
    public TextView text1;
    public TextView text2;
    public TextView temperature;
    //Integer demo1;
    //Integer demo2;
    private static final String TAG = "rootView";
    //private LineChart mChart;
    private CustomGauge gauge1;
    int i;
    private CustomGauge gauge2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        temp = (TextView)findViewById(R.id.textViewTemperature);
        startMqtt();
        //text1 = (TextView)findViewById(R.id.textView1);
        //text2 = (TextView)findViewById(R.id.textView2);
        //startMqtt();
        //startMqtt2();

        //Temperaturanzeige
        //gauge1 = findViewById(R.id.gauge1);!!!!!
        //gauge1.setEndValue(40);!!!!!!!!!
        //text1 = findViewById(R.id.textView1);!!!!!!!!
        //text1.setText(Integer.toString(gauge1.getValue()));!!!!!!!!!!!
        //Demo-Daten für Gauge (Temperatur) Darstellung
        //Integer demo1 = 20;
        //Integer demo1 = 10;
        //Integer demo1 = 30;
        //Integer demo1 = 35;
        //gauge1.setValue(demo1);!!!!!!!!
        //Integer temperature = demo1;!!!!!!!!!
        //text1.setText(Integer.toString(temperature) + "°");!!!!!!!!!!

        //Luftfeuchtigkeitsanzeige
        //Integer demo2 = 10;
        //Integer demo2 = 20;
        //Integer demo2 = 30;
        //Integer demo2 = 40;
        //Integer demo2 = 50;
        //Integer demo2 = 60;
        //Integer demo2 = 70;
        //Integer demo2 = 80;
        //Integer demo2 = 90;
        //Integer demo2 = 100;
        //gauge2 = findViewById(R.id.gauge2);!!!!!!!
        //gauge2.setEndValue(100);!!!!!!
        //text2 = findViewById(R.id.textView2);!!!!!!!!!
        //text2.setText(Integer.toString(gauge2.getValue()));!!!!!!!!
        //gauge2.setValue(demo2);!!!!!!!!!
        //Integer humidy = demo2;!!!!!!!!!!
        //text2.setText(Integer.toString(humidy) + "%");!!!!!!!



        //mChart = (LineChart)findViewById(R.id.LineChart);
        //mChart.setOnChartGestureListener(rootView.this);
        //mChart.setOnChartValueSelectedListener(rootView.this);
        //mChart.setDragEnabled(true);
        //mChart.setScaleEnabled(false);
        //ArrayList<Entry> yValues = new ArrayList<>();

        //yValues.add(new Entry(0, 60f));
        //yValues.add(new Entry(1, 50f));
        //yValues.add(new Entry(2, 70f));
        //yValues.add(new Entry(3, 30f));
        //yValues.add(new Entry(4, 50f));
        //yValues.add(new Entry(5, 60f));
        //yValues.add(new Entry(6, 65f));

        //LineDataSet set1 = new LineDataSet(yValues, "Data Set 1");
        //set1.setFillAlpha(110);
        //ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        //dataSets.add(set1);
        //LineData data = new LineData(dataSets);
        //mChart.setData(data);



        //final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar)findViewById(R.id.rangeSeekbar3);
        //final TextView tvMin = (TextView)findViewById(R.id.bereich1);
        //final TextView tvMax = (TextView)findViewById(R.id.bereich2);
        //rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
        //    @Override muss mögl. weg
        //    public void valueChanged(Number minValue, Number maxValue) {
        //        tvMin.setText(String.valueOf(minValue) + "%");
        //        tvMax.setText(String.valueOf(maxValue) + "%");
        //    }
        //});
    }

    public void onclickButton(View v){

    }

    private void startMqtt() {
        mqttHelper = new MqttHelper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {

                Log.w("Debug", mqttMessage.toString());
                temperature.setText(mqttMessage.toString());
                Context context = getApplicationContext();
                CharSequence text = mqttMessage.toString();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }


}
