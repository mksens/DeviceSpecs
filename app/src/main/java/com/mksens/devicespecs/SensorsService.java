package com.mksens.devicespecs;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import com.mksens.devicespecs.event.ProximityEvent;

public class SensorsService extends Service implements SensorEventListener {
    private static final String TAG = MainActivity.class.getCanonicalName();

    private SensorManager sensorManager;
    private ArrayList<Sensor> sensors = new ArrayList<>();

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensors.add(sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY));

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");

        for (Sensor sensor : sensors) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");

        for (Sensor sensor : sensors) {
            sensorManager.unregisterListener(this, sensor);
        }

        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.i(TAG, "onSensorChanged");

        EventBus.getDefault().post(event);

        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            ProximityEvent proxEvent = new ProximityEvent();
            proxEvent.name = event.sensor.getType();
            proxEvent.values = event.values;
            proxEvent.timestamp = event.timestamp;

            EventBus.getDefault().post(proxEvent);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i(TAG, "onAccuracyChanged");
    }
}
