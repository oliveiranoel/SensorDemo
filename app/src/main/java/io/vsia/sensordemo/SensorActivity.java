package io.vsia.sensordemo;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager = null;
    private Sensor lightSensor = null;
    private Sensor acclerometer = null;
    TextView textView;
    TextView textView1;
    TextView textView2;
    private float lightValue;
    private float accValueZ;
    private float accValueX;
    private float accValueY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        textView = findViewById(R.id.textView_sensor);
        textView1 = findViewById(R.id.textView_lightsensor);
        textView2 = findViewById(R.id.textView_acclerometer);

        sensorCheck();
    }

    private void sensorCheck() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        StringBuilder sensorInfo = new StringBuilder();

        for (Sensor sensor : sensors) {
            sensorInfo.append(sensor.getName() + "\n");

            switch (sensor.getType()) {
                case Sensor.TYPE_LIGHT:
                    lightSensor = sensor;
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    acclerometer = sensor;
            }
        }
        textView.setText(sensorInfo.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (acclerometer != null) {
            sensorManager.registerListener(this, acclerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == lightSensor) {
            lightValue = event.values[0];
            this.textView1.setText("Lichtwert: " + lightValue);
        }

        //2 = Z - Achse
        //1 = Y - Achse
        //0 = X - Achse
        if (event.sensor == acclerometer) {
            accValueZ = event.values[2];
            accValueX = event.values[0];
            accValueY = event.values[1];
            this.textView2.setText("Acclerometer: Z: " + accValueZ + " X: " + accValueX + " Y: " + accValueY);
            if (accValueZ > 15) {
                playSound();
            }
        }
    }

    private void playSound() {
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.fighter_sound);
        mp.start();
    }

    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {

    }
}
