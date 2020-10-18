package com.example.dariushaslauer.mqtt_connection;

import android.app.Notification;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.github.mikephil.charting.data.Entry;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID1 = "1";
    private static final String CHANNEL_ID2 = "2";
    private static final int NOTIFICATION_ID = 1;
    private static final int NOTIFICATION_ID2 = 2;
    boolean errorIsSet = false;
    MqttHelper mqttHelper;
    MqttHelper2 mqttHelper2;
    MqttHelper3 mqttHelper3;
    TextView dataReceived;
    TextView dataReceived2;
    Integer hum;
    Integer temp;
    String maxHumidity;
    String minHumidity;
    private static final String TAG = "rootView";
    private CustomGauge gauge1;
    private CustomGauge gauge2;
    boolean notificationSetTooHigh = false;
    boolean notificationSetTooLow = false;
    ArrayList<Entry> arrayListTemperature = new ArrayList<>();
    ArrayList<Entry> arrayListHumidity = new ArrayList<>();
    public Integer counter1;
    public Integer counter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Demo-Daten der Statistik
        counter1 = 0;
        counter2 = 0;
//        arrayListTemperature.add(new Entry(1, 10f));
//        arrayListTemperature.add(new Entry(2, 30f));
//        arrayListTemperature.add(new Entry(3, 40f));
//        arrayListTemperature.add(new Entry(4, 20f));
//        arrayListTemperature.add(new Entry(5, 50f));
//        arrayListTemperature.add(new Entry(6, 50f));
//        arrayListTemperature.add(new Entry(7, 50f));
//        arrayListTemperature.add(new Entry(8, 50f));
//        arrayListTemperature.add(new Entry(9, 30f));
//        arrayListTemperature.add(new Entry(10, 10f));
//        arrayListTemperature.add(new Entry(11, 60f));
//        arrayListTemperature.add(new Entry(12, 10f));




        //Integer temperature = Integer.parseInt(temp);
        dataReceived = (TextView)findViewById(R.id.dataReceived);
        dataReceived2 = (TextView)findViewById(R.id.dataRecieved2);
        gauge1 = findViewById(R.id.gauge1);
        gauge1.setEndValue(40);
        gauge2 = findViewById(R.id.gauge2);
        gauge2.setEndValue(100);


        startMqtt();
        startMqtt2();


        final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar)findViewById(R.id.rangeSeekbar3);
        final TextView tvMin = (TextView)findViewById(R.id.bereich1);
        final TextView tvMax = (TextView)findViewById(R.id.bereich2);

        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin.setText("min. " + String.valueOf(minValue) + "%");
                tvMax.setText("max. " + String.valueOf(maxValue) + "%");
                maxHumidity = String.valueOf(maxValue);
                minHumidity = String.valueOf(minValue);
            }
        });
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
                dataReceived.setText(mqttMessage.toString() + "°");
                Integer temp = Integer.parseInt(mqttMessage.toString());
                gauge1.setValue(temp);
                createArrayListTemperatureEntry(mqttMessage);
                if(temp == 0 || temp < 20){
                    gauge1.setValue(10);
                }
                if(temp > 40){
                    gauge1.setValue(40);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

    private void startMqtt2() {
        mqttHelper2 = new MqttHelper2(getApplicationContext());
        mqttHelper2.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {
                if(!errorIsSet) {
                    Context context = getApplicationContext();
                    CharSequence text = "Verbindung fehlgeschlagen";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    errorIsSet = true;
                }
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug", mqttMessage.toString());
                dataReceived2.setText(mqttMessage.toString() + "%");
                hum = Integer.parseInt(mqttMessage.toString());
                gauge2.setValue(hum);
                createArrayListHumidityEntry(mqttMessage);
                //arrayListHumidity.add(hum);


                if(Integer.parseInt(maxHumidity) < hum && !notificationSetTooHigh){
                    //luftfeuchtigkeit zu hoch
                    notificationSetTooHigh = true;
                    showNotification_TooHigh();
                }
                if(Integer.parseInt(minHumidity) > hum && !notificationSetTooLow){
                    //luftfeuchtigkeit zu niedrig
                    notificationSetTooLow = true;
                    showNotification_TooLow();
                }
                if(Integer.parseInt(minHumidity) < hum && Integer.parseInt(maxHumidity) > hum){
                    //luftfeuchtigkeit im richtigen bereich
                    notificationSetTooLow = false;
                    notificationSetTooHigh = false;
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

    public void startMqtt3(){
        mqttHelper3 = new MqttHelper3(getApplicationContext());
        mqttHelper3.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug", mqttMessage.toString());
                String notificationMessage = mqttMessage.toString();
                showNotification(notificationMessage);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }


    public void disconnect(View v) throws MqttException {
        mqttHelper.mqttAndroidClient.disconnect();
    }

    public void onClickSave(View v) throws UnsupportedEncodingException, MqttException {
        //publish the min seekbar value
        String topic1 = "app/min";
        String payload1 = minHumidity;
        byte[] encodedPayload1 = new byte[0];
        try{
            encodedPayload1 = payload1.getBytes("UTF-8");
            MqttMessage message1 = new MqttMessage(encodedPayload1);
            mqttHelper.mqttAndroidClient.publish(topic1, message1);
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        //Publish the max seekbar value
        String topic2 = "app/max";
        String payload2 = maxHumidity;
        byte[] encodedPayload2 = new byte[0];
        try{
            encodedPayload2 = payload2 .getBytes("UTF-8");
            MqttMessage message2 = new MqttMessage(encodedPayload2);
            mqttHelper.mqttAndroidClient.publish(topic2, message2);
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        Context context = getApplicationContext();
        CharSequence text = "Luftfeuchtigkeitsbereich gespeichert";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void onClickOnOff(View v) throws UnsupportedEncodingException, MqttException{
        boolean checked = ((ToggleButton)v).isChecked();
        if (checked){

            String topic3 = "app/min";
            String payload3 = "100";
            byte[] encodedPayload1 = new byte[0];
            try{
                encodedPayload1 = payload3.getBytes("UTF-8");
                MqttMessage message3 = new MqttMessage(encodedPayload1);
                mqttHelper.mqttAndroidClient.publish(topic3, message3);
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String topic4 = "app/max";
            String payload4 = "100";
            byte[] encodedPayload4 = new byte[0];
            try {
                encodedPayload4 = payload4.getBytes("UTF-8");
                MqttMessage message4 = new MqttMessage(encodedPayload4);
                mqttHelper.mqttAndroidClient.publish(topic4, message4);
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else
        {
            String topic5 = "app/min";
            String payload5 = "0";
            byte[] encodedPayload5 = new byte[0];
            try{
                encodedPayload5 = payload5.getBytes("UTF-8");
                MqttMessage message5 = new MqttMessage(encodedPayload5);
                mqttHelper.mqttAndroidClient.publish(topic5, message5);
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String topic6 = "app/max";
            String payload6 = "100";
            byte[] encodedPayload6 = new byte[0];
            try {
                encodedPayload6 = payload6.getBytes("UTF-8");
                MqttMessage message6 = new MqttMessage(encodedPayload6);
                mqttHelper.mqttAndroidClient.publish(topic6, message6);
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public void openActivity2(View v){
        Intent intent = new Intent(this, Activity2.class);
        intent.putExtra("arrayListTemperature", arrayListTemperature);
        intent.putExtra("arrayListHumidity", arrayListHumidity);
        startActivity(intent);
    }

    public void createArrayListTemperatureEntry(MqttMessage mqttMessage){
        counter1++;
        Float temperature = Float.parseFloat(mqttMessage.toString());
        Float entryNumber = (float)counter1;
        arrayListTemperature.add(new Entry(entryNumber, temperature));
    }

    public void createArrayListHumidityEntry(MqttMessage mqttMessage){
        counter2++;
        Float humidity = Float.parseFloat(mqttMessage.toString());
        Float entryNumber2 = (float)counter2;
        arrayListHumidity.add(new Entry(entryNumber2, humidity));

    }

    public void showNotification_TooHigh(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID1);
        builder.setSmallIcon(R.drawable.notificationicon);
        builder.setContentTitle("Luftfeuchtigkeit");
        builder.setContentText("Die Luftfeuchtigkeit hat den konfigurierten Bereich überschritten, bitte öffnen Sie das Fenster.");
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());
    }

    public void showNotification_TooLow(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID2);
        builder.setSmallIcon(R.drawable.notificationicon);
        builder.setContentTitle("Luftfeuchtigkeit");
        builder.setContentText("Die Luftfeuchtigkeit hat den konfigurierten Bereich unterschritten, der Luftbefeuchter wird eingeschalten.");
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID2,builder.build());
    }

    public void showNotification(String message){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID2);
        builder.setSmallIcon(R.drawable.notificationicon);
        builder.setContentTitle("Luftfeuchtigkeit");
        builder.setContentText(message);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID2,builder.build());
    }
}
